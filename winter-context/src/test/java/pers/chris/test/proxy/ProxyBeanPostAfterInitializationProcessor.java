package pers.chris.test.proxy;

import pers.chris.core.BeanPostProcessor;
import pers.chris.core.annotation.Component;

/**
 * @Description
 * @Author Chris
 * @Date 2023/11/30
 */
@Component
public class ProxyBeanPostAfterInitializationProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) {
        System.out.println("Bean " + beanName + " is proxied after initialization");

        return bean;
    }
}
