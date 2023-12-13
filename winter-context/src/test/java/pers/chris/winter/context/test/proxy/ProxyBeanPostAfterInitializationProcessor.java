package pers.chris.winter.context.test.proxy;

import pers.chris.winter.context.core.BeanPostProcessor;
import pers.chris.winter.context.core.annotation.Component;

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
