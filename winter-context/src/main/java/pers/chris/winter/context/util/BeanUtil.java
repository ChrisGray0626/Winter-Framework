package pers.chris.winter.context.util;

import pers.chris.winter.context.core.BeanDefinition;
import pers.chris.winter.context.core.BeanPostProcessor;
import pers.chris.winter.context.core.annotation.Bean;
import pers.chris.winter.context.core.annotation.Configuration;
import pers.chris.winter.context.exception.MultipleConstructorFoundException;
import pers.chris.winter.context.exception.NoConstructorFoundException;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/**
 * @Description
 * @Author Chris
 * @Date 2023/10/24
 */
public class BeanUtil {

    private BeanUtil() {
    }

    public static String getBeanName(Method method) {
        Bean bean = method.getAnnotation(Bean.class);
        String beanName = bean.name();
        if (StringUtil.isEmpty(beanName)) {
            beanName = method.getName();
        }
        return beanName;
    }

    public static Constructor<?> getBeanConstructor(Class<?> beanClass) {
        Constructor<?>[] constructors = beanClass.getConstructors();
        if (constructors.length == 0) {
            throw new NoConstructorFoundException(beanClass.getName());
        }
        if (constructors.length > 1) {
            throw new MultipleConstructorFoundException(beanClass.getName());
        }
        return constructors[0];
    }

    public static boolean isConfigurationBeanDefinition(BeanDefinition beanDefinition) {
        return beanDefinition.getBeanClass().isAnnotationPresent(Configuration.class);
    }

    public static boolean isBeanPostProcessorDefinition(BeanDefinition beanDefinition) {
        return BeanPostProcessor.class.isAssignableFrom(beanDefinition.getBeanClass());
    }
}
