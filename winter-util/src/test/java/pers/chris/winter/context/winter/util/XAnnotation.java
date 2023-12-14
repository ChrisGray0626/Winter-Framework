package pers.chris.winter.context.winter.util;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @Description
 * @Author Chris
 * @Date 2023/12/14
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface XAnnotation {
    String value() default "";
}
