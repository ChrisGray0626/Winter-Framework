package pers.chris.winter.context.aop.annotation;

import java.lang.annotation.*;

/**
 * @Description
 * @Author Chris
 * @Date 2023/12/2
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface Around {

        // Invocation handler bean name
        String value();
}
