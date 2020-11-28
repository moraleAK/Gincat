package net.gincat.jpax.annotation;

import java.lang.annotation.*;

/**
 * GroupBy page query are not friendly
 *
 * @author Gin
 * @since 2020/1/6 15:15
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Deprecated
public @interface GroupBy {
    String[] properties() default "id";
}
