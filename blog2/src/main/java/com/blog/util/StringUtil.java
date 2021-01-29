package com.blog.util;

/**
 * @author lt
 * @date 2021/1/23 16:28
 */
public class StringUtil {

    public static boolean isEmpty(Object o) {
        return o == null || o.toString().trim().isEmpty();
    }
}
