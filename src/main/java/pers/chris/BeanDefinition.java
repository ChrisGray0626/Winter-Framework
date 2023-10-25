package pers.chris;

import pers.chris.util.BeanUtil;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/**
 * @Description
 * @Author Chris
 * @Date 2023/10/10
 */
public class BeanDefinition {

    private final String beanName;
    private final Class<?> beanClass;
    private Object instance = null;
    private final Constructor<?> beanConstructor;
    private String factoryBeanName = null;
    private Method factoryMethod = null;

    public BeanDefinition(String beanName, Class<?> beanClass) {
        this.beanName = beanName;
        this.beanClass = beanClass;
        this.beanConstructor = BeanUtil.getBeanConstructor(beanClass);
    }

    public BeanDefinition(String beanName, Class<?> beanClass, String factoryBeanName, Method factoryMethod) {
        this(beanName, beanClass);
        this.factoryBeanName = factoryBeanName;
        this.factoryMethod = factoryMethod;
    }

    public String getBeanName() {
        return beanName;
    }

    public Class<?> getBeanClass() {
        return beanClass;
    }

    public Object getInstance() {
        return instance;
    }

    public void setInstance(Object instance) {
        this.instance = instance;
    }

    public Constructor<?> getBeanConstructor() {
        return beanConstructor;
    }

    public String getFactoryBeanName() {
        return factoryBeanName;
    }

    public void setFactoryBeanName(String factoryBeanName) {
        this.factoryBeanName = factoryBeanName;
    }

    public Method getFactoryMethod() {
        return factoryMethod;
    }

    public void setFactoryMethod(Method factoryMethod) {
        this.factoryMethod = factoryMethod;
    }
}
