package pers.chris.winter.context.aop.core;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @Description
 * @Author Chris
 * @Date 2023/12/14
 */
public interface AfterInvocationHandlerAdapter extends InvocationHandler {

    Object doAfter(Object proxy, Object returnValue, Method method, Object[] args);

    @Override
    default Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object result = method.invoke(proxy, args);
        return this.doAfter(proxy, result, method, args);
    }
}
