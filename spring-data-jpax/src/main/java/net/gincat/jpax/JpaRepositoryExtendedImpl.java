package net.gincat.jpax;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Jpa extender
 *
 * @param <T>
 * @param <ID>
 */
public class JpaRepositoryExtendedImpl<T, ID> extends SimpleJpaRepository implements JpaRepositoryExtend {
    private static final int BATCH_SIZE = 20000;
    private final JpaEntityInformation<T, ?> entityInformation;
    private final EntityManager em;

    public JpaRepositoryExtendedImpl(JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
        this.entityInformation = entityInformation;
        this.em = entityManager;
    }

    @Override
    public Page findAllWith(PageRequest pageRequest) {
        return findAll(new SpecificationResolver(pageRequest, getDomainClass()), pageRequest.toPageable());
    }

    @Override
    public PaginationResult findAllWith(PageRequest query, Function transfer) {
        PaginationResult paginationResult = new PaginationResult().of(
                findAll(new SpecificationResolver(query, getDomainClass()), query.toPageable())
                , transfer
        );

        return paginationResult;
    }

    @Override
    public List findAllNoPageWith(Object o) {
        return findAll(new SpecificationResolver(o, getDomainClass()), Pageable.unpaged()).getContent();
    }

    @Override
    public List findAllWith(Object o, Function transfer) {
        return (List) findAllNoPageWith(o).stream().map(transfer).collect(Collectors.toList());
    }


    class DeletedFalse {
        public DeletedFalse(boolean deleted, Object id) {
            this.deleted = deleted;
            this.id = id;

        }

        private boolean deleted;

        private Object id;

        public boolean isDeleted() {
            return deleted;
        }

        public void setDeleted(boolean deleted) {
            this.deleted = deleted;
        }

        public Object getId() {
            return id;
        }

        public void setId(Object id) {
            this.id = id;
        }
    }
}
