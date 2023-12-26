package pers.chris.winter.context.aop.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pers.chris.winter.context.aop.core.BeforeInvocationHandlerAdapter;
import pers.chris.winter.context.core.annotation.Component;

import java.lang.reflect.Method;

/**
 * @Description
 * @Author Chris
 * @Date 2023/12/13
 */
@Component
public class LogInvocationHandler implements BeforeInvocationHandlerAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(LogInvocationHandler.class);

    @Override
    public void doBefore(Object proxy, Method method, Object[] args) {
        LOGGER.info("Method {} is invoked.", method.getName());
    }
}
