import pers.chris.core.annotation.Resource;
import pers.chris.sample.UserController;
import pers.chris.util.ReflectUtil;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description
 * @Author Chris
 * @Date 2023/5/18
 */
public class GenericTest {

    public static void main(String[] args) {
        Class<?> clazz = UserController.class;
        TypeVariable<? extends Class<?>>[] genericTypes = clazz.getSuperclass().getTypeParameters();
        ParameterizedType parameterizedType = (ParameterizedType) clazz.getGenericSuperclass();
        Type[] actualTypes = parameterizedType.getActualTypeArguments();
        Map<String, String> genericClassMap = new HashMap<>();
        for (int i = 0; i < genericTypes.length; i++) {
            genericClassMap.put(genericTypes[i].getName(), actualTypes[i].getTypeName());
        }

        List<Object> genericBeans = new ArrayList<>();
        Field[] fields = ReflectUtil.getFields(clazz, true);
        for (Field field : fields) {
            if (field.isAnnotationPresent(Resource.class)) {
                String typeName = field.getGenericType().getTypeName();
                System.out.println(typeName);
            }
        }
    }
}
