package pers.chris.core;

import pers.chris.BeanDefinition;
import pers.chris.core.annotation.Bean;
import pers.chris.core.annotation.Component;
import pers.chris.core.annotation.Configuration;
import pers.chris.core.annotation.Resource;
import pers.chris.exception.ApplicationMissingException;
import pers.chris.exception.MultipleBeanFoundException;
import pers.chris.util.BeanUtil;
import pers.chris.util.ClassUtil;
import pers.chris.util.ReflectUtil;
import pers.chris.util.StringUtil;

import java.lang.reflect.*;
import java.util.*;

/**
 * @Description Application Container
 * @Author Chris
 * @Date 2023/2/26
 */

public class ApplicationContext {

    private final Map<String, BeanDefinition> beanMap;
    private final List<BeanPostProcessor> beanPostProcessors;

    @SafeVarargs
    public ApplicationContext(Class<? extends Applicable>... configurationClasses) {
        this.beanMap = new HashMap<>();
        this.beanPostProcessors = new ArrayList<>();
        Set<String> beanNames = new HashSet<>();

        for (Class<? extends Applicable> configurationClass : configurationClasses) {
            String[] packageNames = extractBasePackage(configurationClass);
            beanNames.addAll(ClassUtil.scanPackageForClassName(packageNames));
        }

        this.createBeanDefinition(beanNames);
        this.createConfigurationBean();
        this.createBeanPostProcessor();
        this.createNormalBean();
        this.injectBean();
        this.initBean();
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
                BeanDefinition beanDefinition = new BeanDefinition(beanName, clazz);
                this.addBeanDefinition(beanDefinition);
                if (ClassUtil.isAnnotationPresent(clazz, Configuration.class)) {
                    this.scanFactoryMethod(beanName, clazz);
                }
            }
        }
    }

    private void addBeanDefinition(BeanDefinition beanDefinition) {
        String beanName = beanDefinition.getBeanName();
        if (this.beanMap.containsKey(beanName)) {
            throw new DuplicateFormatFlagsException(beanName);
        }
        beanMap.put(beanName, beanDefinition);
    }

    private void scanFactoryMethod(String factoryBeanName, Class<?> factoryBeanClass) {
        for (Method method : ReflectUtil.getDeclaredMethod(factoryBeanClass)) {
            if (method.isAnnotationPresent(Bean.class)) {
                String beanName = BeanUtil.getBeanName(method);
                Class<?> beanClass = method.getReturnType();
                BeanDefinition beanDefinition = new BeanDefinition(beanName, beanClass, factoryBeanName, method);
                this.addBeanDefinition(beanDefinition);
            }
        }
    }

    private void createConfigurationBean() {
        this.beanMap.values().stream().filter(BeanUtil::isConfigurationBeanDefinition)
                .forEach(this::createBean);
    }

    private void createBeanPostProcessor() {
        this.beanMap.values().stream().filter(BeanUtil::isBeanPostProcessorDefinition)
                .forEach(beanDefinition -> {
                    this.createBean(beanDefinition);
                    beanPostProcessors.add((BeanPostProcessor) beanDefinition.getInstance());
                });
    }

    private void createNormalBean() {
        this.beanMap.values().stream().filter(beanDefinition -> beanDefinition.getInstance() == null)
                .forEach(this::createBean);
    }

    private void createBean(BeanDefinition beanDefinition) {
        if (StringUtil.isEmpty(beanDefinition.getFactoryBeanName())) {
            createBeanByConstructor(beanDefinition);
        } else {
            createBeanByFactoryMethod(beanDefinition);
        }
        // Use BeanPostProcessor to process the bean
        this.beanPostProcessors.forEach(
                beanPostProcessor -> {
                    Object processedBean = beanPostProcessor.postProcessBeforeInitialization(beanDefinition.getInstance(), beanDefinition.getBeanName());
                    beanDefinition.setInstance(processedBean);
                }
        );
    }

    private void createBeanByConstructor(BeanDefinition beanDefinition) {
        Object bean;
        try {
            bean = beanDefinition.getBeanConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
        beanDefinition.setInstance(bean);
    }

    // TODO factory method args
    private void createBeanByFactoryMethod(BeanDefinition beanDefinition) {
        Object factoryBean = this.getBean(beanDefinition.getFactoryBeanName());
        Object bean;
        try {
            bean = beanDefinition.getFactoryMethod().invoke(factoryBean);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
        beanDefinition.setInstance(bean);
    }

    private void injectBean() {
        for (BeanDefinition beanDefinition : beanMap.values()) {
            this.injectBean(beanDefinition);
        }
    }

    public void injectBean(BeanDefinition beanDefinition) {
        new Injector(beanDefinition).run();
    }

    private void initBean() {
        for (BeanDefinition beanDefinition : beanMap.values()) {
            this.initBean(beanDefinition);
        }
    }

    private void initBean(BeanDefinition beanDefinition) {
        Object bean = beanDefinition.getInstance();

        for (BeanPostProcessor beanPostProcessor : beanPostProcessors) {
            bean = beanPostProcessor.postProcessAfterInitialization(bean, beanDefinition.getBeanName());
        }
        beanDefinition.setInstance(bean);
    }

    private Object getOriginalInstance(BeanDefinition beanDefinition) {
        Object bean = beanDefinition.getInstance();
        for (BeanPostProcessor beanPostProcessor : beanPostProcessors) {
            bean = beanPostProcessor.postProcessOnSetProperty(bean, beanDefinition.getBeanName());
        }
        return bean;
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
