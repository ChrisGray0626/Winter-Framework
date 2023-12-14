package pers.chris.winter.context.aop.util;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.scaffold.subclass.ConstructorStrategy;
import net.bytebuddy.implementation.InvocationHandlerAdapter;
import net.bytebuddy.matcher.ElementMatchers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pers.chris.winter.context.core.ApplicationContext;
import pers.chris.winter.context.util.ApplicationContextUtil;
import pers.chris.winter.context.winter.util.ReflectUtil;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;

/**
 * @Description
 * @Author Chris
 * @Date 2023/12/1
 */
public class ProxyUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProxyUtil.class);

    private ProxyUtil() {
    }

    /**
     * Create a proxied bean whose public methods are intercepted
     */
    @SuppressWarnings("unchecked")
    public static <T> T createProxy(T originalBean, String handlerName) {
        ApplicationContext applicationContext = ApplicationContextUtil.getRequiredInstance();
        InvocationHandler handler = (InvocationHandler) applicationContext.getBean(handlerName);
        Class<?> originalBeanClass = originalBean.getClass();
        Class<?> proxidBeanClass = new ByteBuddy()
                // Set subclass with default empty constructor:
                .subclass(originalBeanClass, ConstructorStrategy.Default.DEFAULT_CONSTRUCTOR)
                // Intercept public methods
                .method(ElementMatchers.isPublic())
                .intercept(InvocationHandlerAdapter.of((proxy, method, args) -> handler.invoke(originalBean, method, args)))
                // generate proxy class:
                .make()
                .load(originalBeanClass.getClassLoader())
                .getLoaded();
        T proxiedBean = (T) ReflectUtil.newInstance(proxidBeanClass);
        LOGGER.debug("Create proxy for bean {}", originalBeanClass.getName());

        return proxiedBean;
    }

    /**
     * Create a proxied bean whose annotated methods are intercepted
     */
    @SuppressWarnings("unchecked")
    public static <T, A extends Annotation> T createProxy(T originalBean, String handlerName, Class<A> annotationClass) {
        ApplicationContext applicationContext = ApplicationContextUtil.getRequiredInstance();
        InvocationHandler handler = (InvocationHandler) applicationContext.getBean(handlerName);
        Class<?> originalBeanClass = originalBean.getClass();
        Class<?> proxidBeanClass = new ByteBuddy()
                // Set subclass with default empty constructor:
                .subclass(originalBeanClass, ConstructorStrategy.Default.DEFAULT_CONSTRUCTOR)
                // Intercept all annotated methods
                .method(ElementMatchers.isAnnotatedWith(annotationClass))
                .intercept(InvocationHandlerAdapter.of((proxy, method, args) -> handler.invoke(originalBean, method, args)))
                // generate proxy class:
                .make()
                .load(originalBeanClass.getClassLoader())
                .getLoaded();
        T proxiedBean = (T) ReflectUtil.newInstance(proxidBeanClass);
        LOGGER.debug("Create proxy for bean {}", originalBeanClass.getName());

        return proxiedBean;
    }
}
