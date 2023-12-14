package pers.chris.winter.context.winter.util;

import java.lang.annotation.Annotation;

/**
 * @Description
 * @Author Chris
 * @Date 2023/12/14
 */
public class AnnotationUtil {

    private AnnotationUtil() {
    }

    public static boolean isAnnotationPresent(Class<?> clazz, Class<? extends Annotation> targetAnnotationClass) {
        return isAnnotationPresent(clazz, targetAnnotationClass, true);
    }

    public static boolean isAnnotationPresent(Class<?> clazz, Class<? extends Annotation> targetAnnotationClass, boolean checkSuperClass) {
        if (clazz.isAnnotationPresent(targetAnnotationClass)) {
            return true;
        }
        if (checkSuperClass) {
            for (Annotation annotation : clazz.getAnnotations()) {
                Class<? extends Annotation> annotationClass = annotation.annotationType();
                if (isMetaAnnotation(annotationClass)) {
                    continue;
                }
                if (isAnnotationPresent(annotationClass, targetAnnotationClass)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static <A extends Annotation> A getAnnotation(Class<?> clazz, Class<A> targetAnnotationClass) {
        return getAnnotation(clazz, targetAnnotationClass, true);
    }

    public static <A extends Annotation> A getAnnotation(Class<?> clazz, Class<A> targetAnnotationClass, boolean checkSuperClass) {
        if (isAnnotationPresent(clazz, targetAnnotationClass, false)) {
            return clazz.getAnnotation(targetAnnotationClass);
        }
        if (checkSuperClass) {
            for (Annotation annotation : clazz.getAnnotations()) {
                Class<? extends Annotation> annotationClass = annotation.annotationType();
                if (isMetaAnnotation(annotationClass)) {
                    continue;
                }
                return getAnnotation(annotationClass, targetAnnotationClass);
            }
        }
        return null;
    }

    public static boolean isMetaAnnotation(Class<? extends Annotation> annotationClass) {
        return annotationClass.getPackageName().equals("java.lang.annotation");
    }
}
