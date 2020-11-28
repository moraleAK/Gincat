package net.gincat.jpax.annotation;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * Long timestamp = formatter(pattern) + difference * timeUnit
 *
 * @author Gin
 * @since 2019/12/13 10:38
 */
@Target({ElementType.FIELD,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface Date {
    /**
     * 转换规则
     *
     * @return
     */
    String pattern() default "yyyy-MM-dd";

    /**
     * 时差 +
     *
     * @return
     */
    int difference() default 0;

    /**
     * 单位
     *
     * @return
     */
    TimeUnit timeUnit() default TimeUnit.DAYS;
}
