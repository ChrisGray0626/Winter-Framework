package pers.chris.winter.context.aop.core;

import pers.chris.winter.context.aop.exception.AopConfigurationException;
import pers.chris.winter.context.core.BeanPostProcessor;

import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @Description
 * @Author Chris
 * @Date 2023/12/2
 */
public abstract class BaseAnnotationProxyBeanPostProcessor<A extends Annotation> implements BeanPostProcessor {

    private static final String VALUE_METHOD_NAME = "value";

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) {
        Class<?> beanClass = bean.getClass();
        Class<A> annotationClass = this.getAnnotationClass();
        A annotation = beanClass.getAnnotation(annotationClass);
        if (annotation != null) {
            String handlerName;
            try {
                handlerName = (String) annotation.annotationType().getMethod("value").invoke(annotation);
            } catch (ReflectiveOperationException e) {
                throw new AopConfigurationException(String.format("@%s must have value() returned String type.", annotationClass.getSimpleName()), e);
            }
        }


    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) {
        return BeanPostProcessor.super.postProcessAfterInitialization(bean, beanName);
    }

    @SuppressWarnings("unchecked")
    private Class<A> getAnnotationClass() {
        Class<?> clazz = this.getClass();
        Type type = clazz.getGenericSuperclass();
        if (!(type instanceof ParameterizedType)) {
            throw new IllegalArgumentException("Class " + clazz.getName() + " does not have parameterized type.");
        }
        ParameterizedType parameterizedType = (ParameterizedType) type;
        Type[] types = parameterizedType.getActualTypeArguments();
        if (types.length != 1) {
            throw new IllegalArgumentException("Class " + clazz.getName() + " has more than 1 parameterized types.");
        }
        Type r = types[0];
        if (!(r instanceof Class<?>)) {
            throw new IllegalArgumentException("Class " + clazz.getName() + " does not have parameterized type of class.");
        }
        return (Class<A>) r;
    }
}
