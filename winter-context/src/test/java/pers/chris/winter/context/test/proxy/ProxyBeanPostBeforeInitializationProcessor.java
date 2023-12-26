package pers.chris.winter.context.test.proxy;

import pers.chris.winter.context.core.BeanPostProcessor;
import pers.chris.winter.context.core.annotation.Component;

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
