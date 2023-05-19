package pers.chris.core;

import pers.chris.util.ReflectUtil;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @Description
 * @Author Chris
 * @Date 2023/5/17
 */
public class ApplicationExecutor {

    private static final String RUN_METHOD_NAME = "run";
    private Class<? extends BaseApplication>[] applications;

    @SafeVarargs
    public final ApplicationExecutor application(Class<? extends BaseApplication>... application) {
        this.applications = application;
        return this;
    }

    public void run() {
        for (Class<? extends BaseApplication> application : applications) {
            ApplicationContainer applicationContainer = new ApplicationContainer(application);
            Method method;
            try {
                method = ReflectUtil.getMethod(application, RUN_METHOD_NAME);
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
            try {
                method.invoke(applicationContainer.getBean(application.getName()));
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
