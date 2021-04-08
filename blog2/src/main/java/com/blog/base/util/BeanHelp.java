package com.blog.base.util;

import com.blog.base.annotation.Update;
import lombok.extern.slf4j.Slf4j;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;


/**
 * 对象工具
 *
 * @Auther: wanglu
 * @Date: 2018/12/24 17:28
 * @Description:
 */
@Slf4j
public class BeanHelp {

    /**
     * 将对象中的带有update注解属性copy到新对象中
     *
     * @param source 源对象
     * @param target 目标对象
     */
    public static void copy(Object source, Object target) {
        Class<?> resourC = source.getClass();
        Class<?> targetC = target.getClass();

        List<Field> resourceF = getAllFields(resourC);
        try {
            for (Field f : resourceF) {
                Update update = f.getAnnotation(Update.class);
                if (Objects.isNull(update)) {
                    continue;
                }
                Method get = resourC.getMethod(fieldGet(f.getName()));
                if (Objects.isNull(get)) {
                    continue;
                }
                Method set = targetC.getMethod(fieldSet(f.getName()), f.getType());
                if (Objects.isNull(set)) {
                    continue;
                }
                set.invoke(target, get.invoke(source, new Object[]{}));
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
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
     * 拼接属性名的get方法名
     *
     * @param fieldName
     * @return
     */
    public static String fieldGet(String fieldName) {
        StringBuilder builder = new StringBuilder();
        builder.append("get").append(upperCaseOne(fieldName.trim()));
        return builder.toString();
    }

    /**
     * 拼接属性名的set方法名
     *
     * @param fieldName
     * @return
     */
    public static String fieldSet(String fieldName) {
        StringBuilder builder = new StringBuilder();
        builder.append("set").append(upperCaseOne(fieldName.trim()));
        return builder.toString();
    }

    public static String getMethodToColumn(String methodName) {
        String str = humpToLine(methodName);
        return str.substring(4);
    }


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

    private static Pattern linePattern = Pattern.compile("_(\\w)");

    /**
     * 下划线转驼峰
     *
     * @param str
     */
    public static String lineToHump(String str) {
        str = str.toLowerCase();
        Matcher matcher = linePattern.matcher(str);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, matcher.group(1).toUpperCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    /**
     * 将字符串的首字母转大写
     *
     * @param str 需要转换的字符串
     * @return
     */
    public static String upperCaseOne(String str) {
        // 进行字母的ascii编码前移
        char[] cs = str.toCharArray();
        //确认首字母是小写
        if (cs[0] >= 97 && cs[0] <= 122) {
            cs[0] -= 32;
        }
        return String.valueOf(cs);
    }

    /**
     * 将字符串的首字母转大写
     *
     * @param str 需要转换的字符串
     * @return
     */
    public static String lowerCaseOne(String str) {
        // 进行字母的ascii编码后移
        char[] cs = str.toCharArray();
        //确认首字母是大写
        if (cs[0] >= 65 && cs[0] <= 90) {
            cs[0] += 32;
        }
        return String.valueOf(cs);
    }

    /**
     * 去重
     *
     * @param keyExtractor
     * @param <T>
     * @return
     */
    public static <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor) {
        Map<Object, Boolean> map = new ConcurrentHashMap<>();
        return t -> Objects.isNull(map.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE));
    }

    /**
     * 获取重复数据
     *
     * @param keyExtractor
     * @param <T>
     * @return
     */
    public static <T> Predicate<T> repeatByKey(Function<? super T, Object> keyExtractor) {
        Map<Object, Boolean> map = new ConcurrentHashMap<>();
        return t -> Objects.nonNull(map.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE));
    }

    /**
     * 将List转成List<Map<String,Object>>
     *
     * @param list
     * @return
     */
    public static List<Map<String, Object>> objectToMap(List<?> list) {
        if (Objects.isNull(list)) {
            return null;
        }
        List<Map<String, Object>> objects = new ArrayList<>();
        for (Object obj : list) {
            try {
                objects.add(convertBean(obj));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (IntrospectionException e) {
                e.printStackTrace();
            }
        }
        return objects;
    }


    /**
     * 将对象转成Map
     *
     * @param bean
     * @return
     */
    public static Map<String, Object> convertBean(Object bean) throws IllegalAccessException,
            InvocationTargetException, IntrospectionException {
        Class<?> type = bean.getClass();
        Map<String, Object> returnMap = new HashMap<>();
        BeanInfo info = Introspector.getBeanInfo(type);

        PropertyDescriptor[] propertyDescriptors = info.getPropertyDescriptors();
        for (int i = 0; i < propertyDescriptors.length; i++) {
            PropertyDescriptor descriptor = propertyDescriptors[i];
            String propertyName = descriptor.getName();
            if (!propertyName.equals("class")) {
                Method readMethod = descriptor.getReadMethod();
                if (Objects.isNull(readMethod)) {
                    continue;
                }
                Object result = readMethod.invoke(bean, new Object[0]);
                returnMap.put(propertyName, result);
            }
        }
        return returnMap;
    }

    public static boolean equals(Object resour, Object target) {
        if (Objects.nonNull(resour)) {
            return resour.equals(target);
        }
        if (Objects.nonNull(target)) {
            return target.equals(resour);
        }
        return false;
    }

    public static <T extends Serializable> T clone(T obj) {
        T cloneObj = null;
        try {
            //写入字节流
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ObjectOutputStream obs = new ObjectOutputStream(out);
            obs.writeObject(obj);
            obs.close();

            //分配内存，写入原始对象，生成新对象
            ByteArrayInputStream ios = new ByteArrayInputStream(out.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(ios);
            //返回生成的新对象
            cloneObj = (T) ois.readObject();
            ois.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cloneObj;
    }

    public static <T> List<T> deepCopy(List<T> src) {
        List<T> dest = null;
        try {
            ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(byteOut);
            out.writeObject(src);

            ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
            ObjectInputStream in = new ObjectInputStream(byteIn);
            dest = (List<T>) in.readObject();
            out.close();
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dest;
    }

    /**
     * 求values之和
     *
     * @param values
     * @return
     */
    public static BigDecimal add(BigDecimal... values) {
        if (Objects.isNull(values)) {
            return BigDecimal.ZERO;
        }
        return Stream.of(values)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * 用target减去后面所有
     *
     * @param target
     * @param values
     * @return
     */
    public static BigDecimal subtract(BigDecimal target, BigDecimal... values) {
        if (Objects.isNull(target)) {
            target = BigDecimal.ZERO;
        }
        if (Objects.isNull(values) || values.length <= 0) {
            return target;
        }

        return Stream.of(values)
                .filter(Objects::nonNull)
                .reduce(target, BigDecimal::subtract);
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
