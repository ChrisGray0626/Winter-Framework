package pers.chris.test.proxy;

import pers.chris.core.BeanPostProcessor;
import pers.chris.core.annotation.Component;

/**
 * @Description
 * @Author Chris
 * @Date 2023/11/30
 */
@Component
public class ProxyBeanPostBeforeInitializationProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) {
        if (bean instanceof BeanA) {
            return new ProxiedBeanA(bean);
        }
        return bean;
    }
}
