package pers.chris.winter.context.winter.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @Description
 * @Author Chris
 * @Date 2023/12/14
 */
public class AnnotationTest {

    @Test
    public void getAnnotationTest() {
        AnnotatedBean annotatedBean = new AnnotatedBean();
        Class<AnnotatedBean> annotatedBeanClass = AnnotatedBean.class;
        boolean annotationPresent = AnnotationUtil.isAnnotationPresent(annotatedBeanClass, XAnnotation.class);
        assertTrue(annotationPresent);
        XAnnotation annotation = AnnotationUtil.getAnnotation(annotatedBeanClass, XAnnotation.class);
        assertEquals("Annotated by XAnnotation", annotation.value());
    }
}
