package pers.chris.winter.context.core.annotation;

import java.lang.annotation.*;

/**
 * @Description Annotation for dependency injection
 * @Author Chris
 * @Date 2023/2/26
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
public @interface Resource {
}
