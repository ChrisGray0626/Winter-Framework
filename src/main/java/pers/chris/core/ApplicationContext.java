package pers.chris.core;

import pers.chris.BeanDefinition;
import pers.chris.core.annotation.Bean;
import pers.chris.core.annotation.Component;
import pers.chris.core.annotation.Configuration;
import pers.chris.core.annotation.Resource;
import pers.chris.exception.ApplicationMissingException;
import pers.chris.exception.MultipleBeanFoundException;
import pers.chris.util.ClassUtil;
import pers.chris.util.ReflectUtil;

import java.lang.reflect.*;
import java.util.*;

/**
 * @Description Application Container
 * @Author Chris
 * @Date 2023/2/26
 */

// TODO Factory 注入 BeanPostProcessor
public class ApplicationContext {

    private final Map<String, BeanDefinition> beanMap;

    @SafeVarargs
    public ApplicationContext(Class<? extends Applicable>... configurationClasses) {
        Set<String> beanNames = new HashSet<>();
        beanMap = new HashMap<>();

        for (Class<? extends Applicable> configurationClass : configurationClasses) {
            String[] packageNames = extractBasePackage(configurationClass);
            beanNames.addAll(ClassUtil.scanPackageForClassName(packageNames));
        }

//        createBeanByConstructor(beanNames);

//        this.beanMap.values().stream().filter(bean -> bean.getClass().isAnnotationPresent(Configuration.class))
//                .forEach(bean -> this.createBeanByFactory(bean.getClass().getName(), bean.getClass()));
        injectBean();
    }

    private String[] extractBasePackage(Class<?> configurationClass) {
        String[] packageNames;
        if (ClassUtil.isAnnotationPresent(configurationClass, Configuration.class)) {
            Configuration configuration = configurationClass.getAnnotation(Configuration.class);
            packageNames = configuration.basePackages();
            // If no base package is specified, use the package of the application class
            if (packageNames.length == 0) {
                packageNames = new String[]{configurationClass.getPackage().getName()};
            }
        } else {
            throw new ApplicationMissingException();
        }
        return packageNames;
    }

    private void createBeanDefinition(Set<String> beanNames) {
        for (String beanName : beanNames) {
            Class<?> clazz;
            try {
                clazz = Class.forName(beanName);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
            if (ClassUtil.isAnnotationPresent(clazz, Component.class)) {
                beanMap.put(beanName, new BeanDefinition(beanName, clazz));
            }
        }
    }

    private void createConfigurationBean(BeanDefinition beanDefinition) {
        if (beanDefinition.getBeanClass().isAnnotationPresent(Configuration.class)) {
            this.createBeanByConstructor(beanDefinition);
        }
    }

    private void createBeanByConstructor(BeanDefinition beanDefinition) {
        Object bean = ReflectUtil.newInstance(beanDefinition.getBeanClass());
        beanDefinition.setInstance(bean);
    }

    private void createBeanByFactory(BeanDefinition beanDefinition) {
        for (Method method : ReflectUtil.getMethods(beanDefinition.getBeanClass(), true)) {
            if (method.isAnnotationPresent(Bean.class)) {
                Object bean = ReflectUtil.invokeMethod(beanMap.get(beanDefinition.getFactoryBeanName()), method);
                beanDefinition.setInstance(bean);
            }
        }
    }

    private void injectBean() {
        for (BeanDefinition beanDefinition : beanMap.values()) {
            injectBean(beanDefinition);
        }
    }

    public void injectBean(BeanDefinition beanDefinition) {
        new Injector(beanDefinition).run();
    }

    public Object getBean(String beanName) {
        if (!beanMap.containsKey(beanName)) {
            return null;
        }
        return beanMap.get(beanName).getInstance();
    }

    private class Injector {

        // The fields waiting to be injected
        private final List<Field> waitingInjectedFields;
        private final BeanDefinition beanDefinition;

        public Injector(BeanDefinition beanDefinition) {
            waitingInjectedFields = new ArrayList<>();
            this.beanDefinition = beanDefinition;
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
                if (beanDefinition.getBeanClass().getGenericSuperclass() instanceof ParameterizedType) {
                    injectGenericBean();
                }
            }
        }

        private void findWaitingInjectedFields() {
            Class<?> clazz = beanDefinition.getBeanClass();
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
                ReflectUtil.setField(beanDefinition.getInstance(), field, value);
                iterator.remove();
            }
        }

        private void injectBeanByType() {
            Iterator<Field> iterator = waitingInjectedFields.iterator();
            while (iterator.hasNext()) {
                Field field = iterator.next();
                Class<?> type = field.getType();
                // TODO Map to List
                Map<String, Object> candidates = new HashMap<>();
                // Inject the bean by type
                for (BeanDefinition beanDefinition : beanMap.values()) {
                    if (type.isAssignableFrom(beanDefinition.getBeanClass())) {
                        candidates.put(beanDefinition.getBeanName(), beanDefinition.getInstance());
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
                ReflectUtil.setField(beanDefinition.getInstance(), field, value);
                iterator.remove();
            }
        }

        private void injectGenericBean() {
            Class<?> clazz = beanDefinition.getBeanClass();
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
                ReflectUtil.setField(beanDefinition.getInstance(), field, value);
                iterator.remove();
            }
        }
    }
}
