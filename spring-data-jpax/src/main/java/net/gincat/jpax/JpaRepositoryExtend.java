package net.gincat.jpax;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;
import java.util.function.Function;


@NoRepositoryBean
public interface JpaRepositoryExtend<T, ID> extends JpaRepository<T, ID>, JpaSpecificationExecutor {

    List<T> findAllNoPageWith(Object o);

    <R> List<R> findAllWith(Object o, Function<T, R> transfer);

    <X extends PageRequest> Page<T> findAllWith(X pageRequest);

    <X extends PageRequest, R> PaginationResult<R> findAllWith(X query, Function<T, R> transfer);

}
