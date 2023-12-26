package pers.chris.winter.context.aop.exception;

import java.lang.annotation.*;

/**
 * @Description
 * @Author Chris
 * @Date 2023/12/14
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface ExceptionPointcut {
    String value() default "ExceptionInvocationHandler";
}
