package pers.chris.winter.context.winter.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Description Reflection Utility
 * @Author Chris
 * @Date 2023/5/17
 */
public class ReflectUtil {

    private ReflectUtil() {
    }

    public static Object newInstance(Class<?> clazz) {
        try {
            return clazz.getDeclaredConstructor().newInstance();
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException |
                 IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static void setField(Object object, Field field, Object value) {
        try {
            field.setAccessible(true);
            field.set(object, value);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static Field getField(Class<?> clazz, String fieldName) throws NoSuchFieldException {
        Field field = searchField(clazz, fieldName);
        if (field == null) {
            throw new NoSuchFieldException(fieldName);
        }
        return field;
    }

    private static Field searchField(Class<?> clazz, String fieldName) {
        for (Field field : getFields(clazz, true)) {
            if (field.getName().equals(fieldName)) {
                return field;
            }
        }
        return null;
    }

    public static Field[] getFields(Class<?> clazz, boolean withSuper) {
        List<Field> fields = new ArrayList<>();
        Class<?> currentClass = clazz;
        // Get fields from the current class and its super classes recursively
        while (currentClass != null) {
            Field[] currentFields = currentClass.getDeclaredFields();
            fields.addAll(Arrays.asList(currentFields));
            currentClass = withSuper ? currentClass.getSuperclass() : null;
        }
        return fields.toArray(new Field[0]);
    }

    public static Object invokeMethod(Object object, Method method, Object... args) {
        try {
            method.setAccessible(true);
            return method.invoke(object, args);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    public static Method getMethod(Class<?> clazz, String methodName) throws NoSuchMethodException {
        Method method = searchMethod(clazz, methodName);
        if (method == null) {
            throw new NoSuchMethodException(methodName);
        }
        return method;
    }

    private static Method searchMethod(Class<?> clazz, String methodName) {
        Method[] methods = getMethod(clazz, true);
        for (Method method : methods) {
            if (method.getName().equals(methodName)) {
                return method;
            }
        }
        return null;
    }

    public static Method[] getDeclaredMethod(Class<?> clazz) {
        return clazz.getDeclaredMethods();
    }

    public static Method[] getMethod(Class<?> clazz, boolean withSuper) {
        // If the class is an interface, get its methods directly since it has no super class
        if (clazz.isInterface()) {
            return withSuper ? clazz.getMethods() : clazz.getDeclaredMethods();
        }

        List<Method> methods = new ArrayList<>();
        Class<?> currentClass = clazz;
        // Get methods from the current class and its super classes recursively
        while (currentClass != null) {
            Method[] currentMethods = currentClass.getDeclaredMethods();
            methods.addAll(Arrays.asList(currentMethods));
            currentClass = withSuper ? currentClass.getSuperclass() : null;
        }
        return methods.toArray(new Method[0]);
    }
}
