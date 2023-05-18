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
    private ApplicationContainer applicationContainer;
    private Class<? extends BaseApplication>[] sources;

    public ApplicationExecutor() {
        applicationContainer = new ApplicationContainer();
    }

    @SafeVarargs
    public final ApplicationExecutor source(Class<? extends BaseApplication>... sources) {
        this.sources = sources;
        return this;
    }

    public void run() {
        for (Class<? extends BaseApplication> source : sources) {
            Method method;
            try {
                method = ReflectUtil.getMethod(source, RUN_METHOD_NAME);
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
            try {
                method.invoke(applicationContainer.getBean(source.getName()));
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
