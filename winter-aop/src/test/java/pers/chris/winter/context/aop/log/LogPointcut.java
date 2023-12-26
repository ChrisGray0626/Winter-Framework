package pers.chris.winter.context.aop.log;

import java.lang.annotation.*;

/**
 * @Description
 * @Author Chris
 * @Date 2023/12/13
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface LogPointcut {
    String value() default "LogInvocationHandler";
}
