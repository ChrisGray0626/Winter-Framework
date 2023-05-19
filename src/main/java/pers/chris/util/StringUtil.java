package pers.chris.util;

import java.util.Collection;

/**
 * @Description
 * @Author Chris
 * @Date 2023/5/19
 */
public class StringUtil {

    private StringUtil() {
    }

    public static String CollectionToString(Collection<String> strings) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String string : strings) {
            stringBuilder.append(string).append(", ");
        }
        stringBuilder.delete(stringBuilder.length() - 2, stringBuilder.length());
        return stringBuilder.toString();
    }
}
