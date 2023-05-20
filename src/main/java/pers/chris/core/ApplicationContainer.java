package pers.chris.core;

import pers.chris.core.annotation.Application;
import pers.chris.core.annotation.Bean;
import pers.chris.core.annotation.Resource;
import pers.chris.exception.ApplicationMissingException;
import pers.chris.exception.MultipleBeanFoundException;
import pers.chris.util.ClassUtil;
import pers.chris.util.ReflectUtil;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description Application Container
 * @Author Chris
 * @Date 2023/2/26
 */
public class ApplicationContainer {

    private final Map<String, Object> beanMap;
    private final Class<? extends Applicable> applicationClass;
    private String[] packageNames;

    public ApplicationContainer(Class<? extends Applicable> applicationClass) {
        beanMap = new ConcurrentHashMap<>();
        this.applicationClass = applicationClass;

        configBasePackage();
        registerBean();
        injectBean();
    }

    private void configBasePackage() {
        if (applicationClass.isAnnotationPresent(Application.class)) {
            Application application = applicationClass.getAnnotation(Application.class);
            packageNames = application.basePackages();
            // If no base package is specified, use the package of the application class
            if (packageNames.length == 0) {
                packageNames = new String[]{applicationClass.getPackage().getName()};
            }
        } else {
            throw new ApplicationMissingException();
        }
    }

    private void registerBean() {
        Set<Class<?>> classes = ClassUtil.scanPackage(packageNames);
        for (Class<?> clazz : classes) {
            // Only register the bean
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
        if (!beanMap.containsKey(name)) {
            return null;
        }
        return beanMap.get(name);
    }

    private class Injector {

        // The fields waiting to be injected
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
                // Inject the bean by name
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
                // Inject the bean by type
                for (Map.Entry<String, Object> entry : beanMap.entrySet()) {
                    if (type.isAssignableFrom(entry.getValue().getClass())) {
                        candidates.put(entry.getKey(), entry.getValue());
                    }
                }
                if (candidates.isEmpty()) {
                    continue;
                }
                // It is not allowed to multiple beans of the same type
                if (candidates.size() > 1) {
                    throw new MultipleBeanFoundException(type.getName(), candidates.keySet());
                }
                Object value = candidates.values().iterator().next();
                ReflectUtil.setField(bean, field, value);
                iterator.remove();
            }
        }

        private void injectGenericBean() {
            Class<?> clazz = bean.getClass();
            // Get the generic type
            TypeVariable<? extends Class<?>>[] genericTypes = clazz.getSuperclass().getTypeParameters();
            // Get the actual type
            ParameterizedType parameterizedType = (ParameterizedType) clazz.getGenericSuperclass();
            Type[] actualTypes = parameterizedType.getActualTypeArguments();
            // Map the generic type to the actual type
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
