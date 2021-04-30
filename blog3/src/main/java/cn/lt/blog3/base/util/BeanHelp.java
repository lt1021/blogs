package cn.lt.blog3.base.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author lt
 * @date 2021/4/30 17:12
 */
public class BeanHelp {

    private static Pattern humpPattern = Pattern.compile("[A-Z0-9]");

    /**
     * 驼峰转下划线
     *
     * @param str
     * @return
     */

    public static String humpToLine(String str) {
        Matcher matcher = humpPattern.matcher(str);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, "_" + matcher.group(0).toLowerCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    /**
     * 获取class所有属性.包含夫类的.
     *
     * @param cls
     * @return
     */
    public static List<Field> getAllFields(final Class<?> cls) {
        final List<Field> allFields = new ArrayList<>();
        Class<?> currentClass = cls;
        while (Objects.nonNull(currentClass)) {
            final Field[] declaredFields = currentClass.getDeclaredFields();
            for (final Field field : declaredFields) {
                allFields.add(field);
            }
            currentClass = currentClass.getSuperclass();
        }
        return allFields;
    }
    /**
     * 通过属性名字获取值
     *
     * @param o
     * @param fieldName
     * @return
     */
    public static Object getFieldValue(Object o, String fieldName) {
        try {
            Field field = o.getClass().getDeclaredField(fieldName);
            if (Objects.isNull(field)) {
                return null;
            }
            field.setAccessible(true);
            return field.get(o);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public static Object getFieldValue(Object o, Field field) {
        try {
            if (Objects.isNull(field)) {
                return null;
            }
            field.setAccessible(true);
            return field.get(o);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
