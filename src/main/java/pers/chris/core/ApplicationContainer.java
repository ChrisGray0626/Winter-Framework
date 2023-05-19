package pers.chris.core;

import pers.chris.core.annotation.Application;
import pers.chris.core.annotation.Bean;
import pers.chris.core.annotation.Resource;
import pers.chris.exception.ApplicationMissingException;
import pers.chris.exception.MultipleBeanException;
import pers.chris.util.ClassUtil;
import pers.chris.util.ReflectUtil;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description
 * @Author Chris
 * @Date 2023/2/26
 */
public class ApplicationContainer {

    private final Map<String, Object> beanMap;
    private final Class<? extends BaseApplication> applicationClass;
    private String[] packageName;

    public ApplicationContainer(Class<? extends BaseApplication> applicationClass) {
        beanMap = new ConcurrentHashMap<>();
        this.applicationClass = applicationClass;

        configBasePackage();
        registerBean();
        injectBean();
    }

    private void configBasePackage() {
        if (applicationClass.isAnnotationPresent(Application.class)) {
            Application application = applicationClass.getAnnotation(Application.class);
            packageName = application.basePackages();
            if (packageName.length == 0) {
                packageName = new String[]{applicationClass.getPackage().getName()};
            }
        } else {
            throw new ApplicationMissingException();
        }
    }

    // TODO 循环依赖
    private void registerBean() {
        Set<Class<?>> classes = ClassUtil.scanPackage(packageName);
        for (Class<?> clazz : classes) {
            if (clazz.isAnnotationPresent(Bean.class)) {
                Object bean = ReflectUtil.newInstance(clazz);
                beanMap.put(clazz.getName(), bean);
            }
        }
    }

    private void injectBean() {
        for (Object bean : beanMap.values()) {
            injectBean(bean);
        }
    }

    public void injectBean(Object bean) {
        new Injector(bean).run();
    }

    public Object getBean(String name) {
        return beanMap.get(name);
    }

    private class Injector {
        private final List<Field> waitingInjectedFields;
        private final Object bean;

        public Injector(Object bean) {
            waitingInjectedFields = new ArrayList<>();
            this.bean = bean;
        }

        public void run() {
            findWaitingInjectedFields();
            if (waitingInjectedFields.size() > 0) {
                injectBeanByName();
            }
            if (waitingInjectedFields.size() > 0) {
                injectBeanByType();
            }
            if (waitingInjectedFields.size() > 0) {
                if (bean.getClass().getGenericSuperclass() instanceof ParameterizedType) {
                    injectGenericBean();
                }
            }
        }

        private void findWaitingInjectedFields() {
            Class<?> clazz = bean.getClass();
            for (Field field : ReflectUtil.getFields(clazz, true)) {
                if (field.isAnnotationPresent(Resource.class)) {
                    waitingInjectedFields.add(field);
                }
            }
        }

        private void injectBeanByName() {
            Iterator<Field> iterator = waitingInjectedFields.iterator();
            while (iterator.hasNext()) {
                Field field = iterator.next();
                Object value = getBean(field.getType().getName());
                if (value == null) {
                    continue;
                }
                ReflectUtil.setField(bean, field, value);
                iterator.remove();
            }
        }

        private void injectBeanByType() {
            Iterator<Field> iterator = waitingInjectedFields.iterator();
            while (iterator.hasNext()) {
                Field field = iterator.next();
                Class<?> type = field.getType();
                Map<String, Object> candidates = new HashMap<>();
                for (Map.Entry<String, Object> entry : beanMap.entrySet()) {
                    if (type.isAssignableFrom(entry.getValue().getClass())) {
                        candidates.put(entry.getKey(), entry.getValue());
                    }
                }
                if (candidates.isEmpty()) {
                    continue;
                }
                if (candidates.size() > 1) {
                    throw new MultipleBeanException(type.getName(), candidates.keySet());
                } else {
                    Object value = candidates.values().iterator().next();
                    ReflectUtil.setField(bean, field, value);
                    iterator.remove();
                }
            }
        }

        private void injectGenericBean() {
            Class<?> clazz = bean.getClass();
            TypeVariable<? extends Class<?>>[] genericTypes = clazz.getSuperclass().getTypeParameters();
            ParameterizedType parameterizedType = (ParameterizedType) clazz.getGenericSuperclass();
            Type[] actualTypes = parameterizedType.getActualTypeArguments();

            Map<String, String> genericClassMap = new HashMap<>();
            for (int i = 0; i < genericTypes.length; i++) {
                genericClassMap.put(genericTypes[i].getName(), actualTypes[i].getTypeName());
            }

            Iterator<Field> iterator = waitingInjectedFields.iterator();
            while (iterator.hasNext()) {
                Field field = iterator.next();
                String typeName = field.getGenericType().getTypeName();
                if (!genericClassMap.containsKey(typeName)) {
                    continue;
                }
                Object value = getBean(genericClassMap.get(typeName));
                ReflectUtil.setField(bean, field, value);
                iterator.remove();
            }
        }
    }
}
