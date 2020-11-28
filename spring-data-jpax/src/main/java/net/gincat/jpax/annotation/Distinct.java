package net.gincat.jpax.annotation;

import java.lang.annotation.*;

/**
 * select distinct a.id as a_id ... from A as a;
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface Distinct {
    boolean value() default true;

    /**
     * 判断是否对结果进行 distinct 处理
     *
     * @return distinct method name， 此method返回boolean对象
     */
    String distinctMethod() default "";
}
