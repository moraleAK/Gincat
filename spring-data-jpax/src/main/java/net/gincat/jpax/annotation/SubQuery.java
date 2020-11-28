package net.gincat.jpax.annotation;

import java.lang.annotation.*;

/**
 * {@code select a1.* from A as a1 where A.id in (select a2.id from A where ... )}
 *
 * @author Gin
 * @since 2019/11/20 20:50
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface SubQuery {
    /**
     * if these properties not empty, {@link SubQuery} will valid if specify
     * Or empty default valid
     *
     * @return
     */
    String[] properties() default {};
}
