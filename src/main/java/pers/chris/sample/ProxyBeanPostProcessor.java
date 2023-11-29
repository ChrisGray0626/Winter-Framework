package pers.chris.sample;

import pers.chris.core.BeanPostProcessor;
import pers.chris.core.annotation.Component;

/**
 * @Description
 * @Author Chris
 * @Date 2023/11/29
 */
@Component
public class ProxyBeanPostProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) {
        if (bean instanceof A) {
            return new ProxiedA(bean);
        }
        return bean;
    }
}
