package cn.lt.blog3.base.util;

import java.util.Arrays;
import java.util.Objects;

/**
 * @author lt
 * @date 2021/4/30 17:14
 */
public class SysAssert {
    /**
     * objs不能有一个空值，否则将提示msg
     *
     * @param msg  提示信息
     * @param objs 判断对象
     */
    public static void notNull(String msg, Object... objs) {
        boolean flag = Objects.isNull(objs)
                || Arrays.stream(objs).filter(Objects::isNull).count() > 0;
        if (flag) {
            msg(msg);
        }
    }
    /**
     * 提示信息
     *
     * @param msg
     */
    public static void msg(String msg) {
//        throw new ErpParamException(LanguageUtils.msg(msg));
    }
}
