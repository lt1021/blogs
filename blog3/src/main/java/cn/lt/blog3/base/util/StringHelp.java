package cn.lt.blog3.base.util;

import java.util.Objects;

/**
 * @author lt
 * @date 2021/4/8 10:18
 */
public class StringHelp {
    public static final String EMPTY = "";

    public static boolean  isEmpty(Object o){
        return Objects.isNull(o) || o.toString().trim().isEmpty();
    }
}
