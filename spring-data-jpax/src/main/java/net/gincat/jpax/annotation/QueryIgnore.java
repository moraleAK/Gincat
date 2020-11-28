package net.gincat.jpax.annotation;

import java.lang.annotation.*;

/**
 * ignore field
 *
 * @author Gin
 * @since 2019/4/13 13:43
 */
@Target({ElementType.FIELD,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface QueryIgnore {
}
