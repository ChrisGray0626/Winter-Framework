package pers.chris.winter.context.core.annotation;

import java.lang.annotation.*;

/**
 * @Description
 * @Author Chris
 * @Date 2023/10/10
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Bean {

    String name() default "";
}
