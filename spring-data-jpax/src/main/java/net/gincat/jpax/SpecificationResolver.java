package net.gincat.jpax;

import net.gincat.jpax.annotation.Distinct;
import net.gincat.jpax.annotation.SubQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.*;
import java.lang.reflect.Method;
import java.util.*;

import static net.gincat.jpax.SpecificationAnnotationIntrospector.getPropertiesAndGetter;
import static net.gincat.jpax.SpecificationAnnotationIntrospector.getQueryDescriptors;

/**
 * @author Gin
 * @since 2019/4/2 19:06
 */
@SuppressWarnings({"unchecked", "rawtypes", "cast", "AlibabaMethodTooLong"})
class SpecificationResolver<T> implements Specification<T> {
    private static Logger logger = LoggerFactory.getLogger(SpecificationResolver.class);

    private final Class targetClass;
    private final Map<String, Method> entityReadMethods;
    private final List<QueryDescriptor> conditions;
    private final Map<String, Join> joinMap = new HashMap<>();
    private final String id = "id";
    private final Object target;

    private Predicate predicateResult = null;

    public SpecificationResolver(Object queryCondition, Class<T> entityClass) {
        this.targetClass = queryCondition.getClass();
        this.entityReadMethods = getPropertiesAndGetter(entityClass);
        this.conditions = new ArrayList<>(getQueryDescriptors(queryCondition));
        this.target = queryCondition;
    }

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {

        if (predicateResult != null) {
            joinMap.clear();
        }

        Distinct distinct = AnnotationUtils.findAnnotation(targetClass, Distinct.class);
        if (distinct != null && distinct.value()) {
            if (!"".equals(distinct.distinctMethod())) {
                Method method = BeanUtils.findMethod(targetClass, distinct.distinctMethod());
                if (method == null) {
                    throw new RuntimeException("Distinct method is not exist ");
                }

                if ((Boolean) SpecificationAnnotationIntrospector.invoke(method, target)) {
                    query.distinct(true);
                }
            }
        }

        if (AnnotationUtils.findAnnotation(targetClass, SubQuery.class) != null) {
            Subquery subquery = query.subquery(root.getJavaType());
            Root<T> subRoot = subquery.from(root.getJavaType());
            subquery.select(subRoot.get(id));
            subquery.where(handle(subRoot, criteriaBuilder)).alias("subquery");
            predicateResult = criteriaBuilder.and(root.get(id).in(subquery));
        } else {
            predicateResult = criteriaBuilder.and(handle(root, criteriaBuilder));
        }

        return predicateResult;
    }

    private Predicate handle(Root root, CriteriaBuilder criteriaBuilder) {

        Predicate predicate = criteriaBuilder.conjunction();
        // 自定义条件转换
        if (conditions.size() > 0) {
            for (QueryDescriptor condition : conditions) {
                if ((condition.joinName == null || condition.joinName.length == 0) && !entityReadMethods.containsKey(condition.columnName)) {
                    throw new RuntimeException("未知字段:" + condition.columnName);
                }
                logger.debug("查询字段：{}={}", condition.columnName, condition.value);

                Predicate nextPredicate = resolve(condition, criteriaBuilder, root);
                nextPredicate = getInPredicate(nextPredicate, condition, criteriaBuilder, root);

                switch (condition.linkedType) {
                    case AND: {
                        predicate = criteriaBuilder.and(predicate, nextPredicate);
                        break;
                    }

                    case OR: {
                        predicate = criteriaBuilder.or(predicate, nextPredicate);
                        break;
                    }

                    default:
                        break;
                }
            }
        }

        return predicate;
    }

    /**
     * 根据操作符组合查询语句
     *
     * @param condition
     * @param criteriaBuilder
     * @param root
     *
     * @return
     */
    private Predicate resolve(QueryDescriptor condition, CriteriaBuilder criteriaBuilder, Root<T> root) {
        Predicate predicate;
        switch (condition.operatorType) {
            // 小于等于
            case LESS_THAN_OR_EQUAL:
                predicate = criteriaBuilder.lessThanOrEqualTo(root.get(condition.columnName), (Comparable) condition.value);
                break;

            // 包含
            case LIKE:
                if (condition.ignoreCase) {
                    predicate = (criteriaBuilder.like(criteriaBuilder.lower(getPath(condition, root)), "%" + escape(((String) condition.value).toLowerCase()) + "%"));
                } else {
                    predicate = criteriaBuilder.like(getPath(condition, root), "%" + escape(String.valueOf(condition.value)) + "%");
                }
                break;

            // 小于
            case LESS_THAN:
                predicate = criteriaBuilder.lessThan(getPath(condition, root), (Comparable) condition.value);
                break;

            // 大于
            case GRATER_THAN:
                predicate = criteriaBuilder.greaterThan(getPath(condition, root), (Comparable) condition.value);
                break;

            // 大于等于
            case GRATER_THAN_OR_EQUAL:
                predicate = criteriaBuilder.greaterThanOrEqualTo(getPath(condition, root), (Comparable) condition.value);
                break;

            // 不等于
            case NOT_EQUAL:
                predicate = criteriaBuilder.notEqual(getPath(condition, root), condition.value);
                break;

            case LEFT_LIKE:
                predicate = criteriaBuilder.notLike(getPath(condition, root), condition.value + "%");
                break;

            case RIGHT_LIKE:
                predicate = criteriaBuilder.notLike(getPath(condition, root), "%" + condition.value);
                break;

            case NULL:
                predicate = criteriaBuilder.isNull(getPath(condition, root));
                break;

            case NOT_NULL:
                predicate = criteriaBuilder.isNotNull(getPath(condition, root));
                break;

            case IN:
                predicate = criteriaBuilder.in(getPath(condition, root));
                if (condition.value instanceof Collection) {
                    Collection collection = (Collection) condition.value;
                    for (Object o : collection) {
                        ((CriteriaBuilder.In) predicate).value(o);
                    }
                }
                break;

            case NOT_IN:
                predicate = criteriaBuilder.in(getPath(condition, root));
                if (condition.value instanceof Collection) {
                    Collection collection = (Collection) condition.value;
                    for (Object o : collection) {
                        ((CriteriaBuilder.In) predicate).value(o);
                    }
                }

                predicate = predicate.not();
                break;

            // 默认等于
            case EQUAL:
            default:
                predicate = criteriaBuilder.equal(getPath(condition, root), condition.value);
                break;
        }

        return predicate;
    }

    private Path getPath(QueryDescriptor condition, Root<T> root) {
        if (condition.joinName == null || condition.joinName.length == 0) {
            return root.get(condition.columnName);
        }

        return getJoin(root, condition.joinName).get(condition.columnName);
    }

    /**
     * 获取关联表
     * <p>
     * root.getJoins().add(...)
     * 后续可使用 root本身 不用新建一个Map,解析 查询可能会比较麻烦
     *
     * @param root
     * @param joinNames
     *
     * @return
     */
    private Join getJoin(Root root, String[] joinNames) {
        StringBuilder sb = new StringBuilder();
        Join join = null;
        for (String joinName : joinNames) {
            sb.append(joinName).append(".");
            if (joinMap.containsKey(sb.toString())) {
                join = joinMap.get(sb.toString());
            } else {
                if (join == null) {
                    joinMap.put(sb.toString(), root.join(joinName));
                    join = joinMap.get(sb.toString());
                } else {
                    joinMap.put(sb.toString(), join.join(joinName));
                    join = joinMap.get(sb.toString());
                }
            }
        }
        return join;
    }

    private Predicate getInPredicate(Predicate predicate, QueryDescriptor condition, CriteriaBuilder criteriaBuilder, Root<T> root) {
        if (condition.inNames.size() == 0) {
            return predicate;
        }

        List<Predicate> predicates = new ArrayList<>();
        for (String name : condition.inNames) {
            QueryDescriptor newCondition = new QueryDescriptor(name, name, condition.value, condition.operatorType);
            newCondition.setJoinName(condition.joinName);
            newCondition.setJoinType(condition.joinType);
            predicates.add(resolve(newCondition, criteriaBuilder, root));
        }

        predicates.add(predicate);

        switch (condition.inLinkedType) {
            case AND:
                return criteriaBuilder.and(criteriaBuilder.and(predicates.toArray(new Predicate[]{})));

            case OR:
                return criteriaBuilder.or(criteriaBuilder.or(predicates.toArray(new Predicate[]{})));
        }

        return predicate;
    }

    /**
     * mysql 特殊字符转译处理
     *
     * @param value 待转译字符
     *
     * @return
     */
    private String escape(String value) {
        if (!StringUtils.hasText(value)) {
            return "";
        }

        if (value.contains("\\")) {
            value = value.replaceAll("\\\\", "\\\\\\\\");
        }

        if (value.contains("/")) {
            value = value.replaceAll("/", "\\\\/");
        }

        if (value.contains("_")) {
            value = value.replaceAll("_", "\\\\_");
        }

        if (value.contains("%")) {
            value = value.replaceAll("%", "\\\\%");
        }

        return value;
    }
}
