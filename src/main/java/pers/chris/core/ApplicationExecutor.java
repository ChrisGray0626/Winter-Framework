package pers.chris.core;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import pers.chris.util.ReflectUtil;

/**
 * @Description
 * @Author Chris
 * @Date 2023/5/17
 */
public class ApplicationExecutor {

    private static final String RUN_METHOD_NAME = "run";
    private Container container;
    private Class<? extends BaseApplication>[] sources;

    public ApplicationExecutor() {
        container = new Container();
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
                method.invoke(container.getBean(source.getName()));
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
