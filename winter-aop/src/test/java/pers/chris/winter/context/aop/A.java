package pers.chris.winter.context.aop;

import pers.chris.winter.context.aop.annotation.Around;
import pers.chris.winter.context.core.annotation.Component;

/**
 * @Description
 * @Author Chris
 * @Date 2023/12/13
 */
@Around("LogInvocationHandler")
@Component
public class A {

    public void test() {
    }

    public void test2() {
    }
}
