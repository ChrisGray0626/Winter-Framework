package pers.chris.core.annotation;

import java.lang.annotation.*;

/**
 * @Description Annotation for Application
 * @Author Chris
 * @Date 2023/5/17
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Bean
public @interface Application {
    String[] basePackages() default {};
}
