package pers.chris.core;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import pers.chris.core.annotation.Bean;
import pers.chris.core.annotation.Resource;
import pers.chris.util.ClassUtil;
import pers.chris.util.ReflectUtil;

/**
 * @Description
 * @Author Chris
 * @Date 2023/2/26
 */
public class Container {

    // 存储Bean对象的Map
    private final Map<String, Object> beanMap = new HashMap<>();
    private final String[] packageName = {"pers.chris.sample"};

    public Container() {
        loadBean();
    }

    private void loadBean() {
        Set<Class<?>> classes = ClassUtil.scanPackage(packageName);
        for (Class<?> clazz : classes) {
            if (clazz.isAnnotationPresent(Bean.class)) {
                Object bean = ReflectUtil.newInstance(clazz);
                beanMap.put(clazz.getName(), bean);
            }
        }

        for (Object bean : beanMap.values()) {
            injectBean(bean);
        }
    }

    // 注入依赖
    private void injectBean(Object bean) {
        Class<?> clazz = bean.getClass();
        Field[] fields = ReflectUtil.getFields(clazz, true);
        for (Field field : fields) {
            if (field.isAnnotationPresent(Resource.class)) {
                Object value = getBean(field.getType().getName());
                if (value == null) {
                    continue;
                }
                try {
                    field.setAccessible(true);
                    field.set(bean, value);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    // 获取Bean对象
    public Object getBean(String name) {
        Object o = beanMap.get(name);
        return beanMap.get(name);
    }
}
