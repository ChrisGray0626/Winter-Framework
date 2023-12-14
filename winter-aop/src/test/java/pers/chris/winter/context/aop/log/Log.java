package pers.chris.winter.context.aop.log;

import pers.chris.winter.context.aop.annotation.Around;

import java.lang.annotation.*;

/**
 * @Description
 * @Author Chris
 * @Date 2023/12/13
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@Around("LogInvocationHandler")
public @interface Log {
}
