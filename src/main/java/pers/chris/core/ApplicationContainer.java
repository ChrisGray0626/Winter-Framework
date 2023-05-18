package pers.chris.core;

import pers.chris.core.annotation.Bean;
import pers.chris.core.annotation.Resource;
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
    // TODO Config package name
    private final String[] packageName = {"pers.chris.sample"};

    public ApplicationContainer() {
        beanMap = new ConcurrentHashMap<>();

        registerBean();
        injectBean();
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
        private final List<Field> unInjectedFields;
        private final Object bean;

        public Injector(Object bean) {
            unInjectedFields = new ArrayList<>();
            this.bean = bean;
        }

        public void run() {
            injectBean();

            if (unInjectedFields.size() > 0) {
                if (bean.getClass().getGenericSuperclass() instanceof ParameterizedType) {
                    injectGenericBean();
                }
            }
        }

        private void injectBean() {
            Class<?> clazz = bean.getClass();
            Field[] fields = ReflectUtil.getFields(clazz, true);
            for (Field field : fields) {
                if (field.isAnnotationPresent(Resource.class)) {
                    Object value = getBean(field.getType().getName());
                    if (value == null) {
                        unInjectedFields.add(field);
                        continue;
                    }
                    ReflectUtil.setField(bean, field, value);
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

            Iterator<Field> iterator = unInjectedFields.iterator();
            while (iterator.hasNext()) {
                Field field = iterator.next();
                String typeName = field.getGenericType().getTypeName();
                if (!genericClassMap.containsKey(typeName)) {
                    continue;
                }
                Object value = getBean(genericClassMap.get(typeName));
                try {
                    field.setAccessible(true);
                    field.set(bean, value);
                    iterator.remove();
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
