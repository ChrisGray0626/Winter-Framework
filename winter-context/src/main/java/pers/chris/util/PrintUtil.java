package pers.chris.util;

/**
 * @Description
 * @Author Chris
 * @Date 2023/5/18
 */
public class PrintUtil {

    private PrintUtil() {
    }

    public static void printClassName(Object object) {
        System.out.println(object.getClass().getName());
    }
}
