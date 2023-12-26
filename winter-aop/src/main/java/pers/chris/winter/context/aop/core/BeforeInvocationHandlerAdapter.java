package pers.chris.winter.context.aop.core;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @Description
 * @Author Chris
 * @Date 2023/12/13
 */
public interface BeforeInvocationHandlerAdapter extends InvocationHandler {

    void doBefore(Object proxy, Method method, Object[] args);

    @Override
    default Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        this.doBefore(proxy, method, args);
        return method.invoke(proxy, args);
    }
}
