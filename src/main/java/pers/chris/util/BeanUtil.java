package pers.chris.util;

import pers.chris.core.annotation.Bean;

import java.lang.reflect.Method;

/**
 * @Description
 * @Author Chris
 * @Date 2023/10/24
 */
public class BeanUtil {

    private BeanUtil() {
    }

    public static String getBeanName(Method method) {
        Bean bean = method.getAnnotation(Bean.class);
        String beanName = bean.name();
        if (StringUtil.isEmpty(beanName)) {
            beanName = method.getName();
        }
        return beanName;
    }
}
