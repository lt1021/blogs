package com.blog.util;

import org.springframework.cglib.beans.BeanMap;

import java.util.Map;

/**
 * @author lt
 * @date 2021/1/23 16:28
 */
public class StringUtil {

    /**
     *
     * @param map
     * @param bean
     * @param <T>
     * @return
     */
    public static <T> T mapToBean(Map<String, Object> map, T bean) {
        BeanMap beanMap = BeanMap.create(bean);
        beanMap.putAll(map);
        return bean;
    }


    public static boolean isEmpty(Object o) {
        return o == null || o.toString().trim().isEmpty();
    }
}
