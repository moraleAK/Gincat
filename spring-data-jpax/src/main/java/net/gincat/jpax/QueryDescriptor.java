package net.gincat.jpax;


import net.gincat.jpax.annotation.Query;

import javax.persistence.criteria.JoinType;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Gin
 * @since 2019/6/10 17:23
 */
public class QueryDescriptor {
    QueryDescriptor(String columnName, String name, Object value, CompareType operatorType) {
        this.columnName = columnName;
        this.name = name;
        this.value = value;
        this.operatorType = operatorType;
    }

    /**
     * 实体对应的列名
     */
    String columnName;

    /**
     * 查询条件的名称
     */
    String name;

    /**
     * 内部查询条件
     */
    Set<String> inNames = new HashSet<>();

    /**
     * 条件对应值，须和实体列类型一一对应
     */
    Object value;

    /**
     * 操作符，大于、小于 .et
     */
    CompareType operatorType;

    boolean ignoreCase;

    String[] joinName;

    JoinType joinType;

    /**
     * 外部查询关联
     */
    Query.OperatorType linkedType = Query.OperatorType.AND;

    /**
     * 内部查询关联
     */
    Query.OperatorType inLinkedType = Query.OperatorType.AND;

    void setLinkedType(Query.OperatorType linkedType) {
        this.linkedType = linkedType;
    }

    void setIgnoreCase(boolean ignoreCase) {
        this.ignoreCase = ignoreCase;
    }

    public void setJoinName(String[] joinName) {
        this.joinName = joinName;
    }

    public void setJoinType(JoinType joinType) {
        this.joinType = joinType;
    }
}
