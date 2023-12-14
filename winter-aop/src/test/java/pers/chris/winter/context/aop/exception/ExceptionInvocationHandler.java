package pers.chris.winter.context.aop.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pers.chris.winter.context.core.annotation.Component;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @Description
 * @Author Chris
 * @Date 2023/12/14
 */
@Component
public class ExceptionInvocationHandler implements InvocationHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionInvocationHandler.class);

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object result = null;
        try {
            result = method.invoke(proxy, args);
        } catch (InvocationTargetException e) {
            Throwable targetException = e.getTargetException();
            if (targetException instanceof HandledException) {
                LOGGER.info("HandledException happened, everything is under control.");
            } else {
                throw targetException;
            }
        }
        return result;
    }
}
