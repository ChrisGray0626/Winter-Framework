package pers.chris.core;

/**
 * @Description
 * @Author Chris
 * @Date 2023/10/26
 */
public interface BeanPostProcessor {

    default Object postProcessBeforeInitialization(Object bean, String beanName) {
        return bean;
    }

    default Object postProcessAfterInitialization(Object bean, String beanName) {
        return bean;
    }

    default Object postProcessOnSetProperty(Object bean, String beanName) {
        return bean;
    }
}
