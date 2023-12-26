package pers.chris.winter.context.util;

import pers.chris.winter.context.core.ApplicationContext;

import java.util.Objects;

/**
 * @Description
 * @Author Chris
 * @Date 2023/12/13
 */
public class ApplicationContextUtil {

    private static ApplicationContext instance = null;

    private ApplicationContextUtil() {
    }

    public static ApplicationContext getRequiredInstance() {
        return Objects.requireNonNull(getInstance(), "ApplicationContext is not set.");
    }

    public static void setApplicationContext(ApplicationContext applicationContext) {
        instance = applicationContext;
    }

    private static ApplicationContext getInstance() {
        return instance;
    }
}
