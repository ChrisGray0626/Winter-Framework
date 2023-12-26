package pers.chris.winter.context.aop.log;

import pers.chris.winter.context.core.annotation.Component;

/**
 * @Description
 * @Author Chris
 * @Date 2023/12/13
 */
//@Around("LogInvocationHandler")
@LogPointcut
@Component
public class LoggedBean {

    public void func1() {
    }

    public void func2() {
    }
}
