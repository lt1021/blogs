package com.blog.base.util;


import com.blog.base.exception.ErpParamException;
import com.blog.base.exception.ErpVersionException;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * 全局断言
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
     * objs不能有一个空值，否则将提示msg
     *
     * @param msg
     * @param params
     * @param objs
     */
    public static void notNull(String msg, List<String> params, Object... objs) {
        boolean flag = Objects.isNull(objs)
                || Arrays.stream(objs).filter(Objects::isNull).count() > 0;
        if (flag) {
            msg(msg, params);
        }
    }

    /**
     * objs对象必须为null
     *
     * @param msg
     * @param objs 判断对象
     */
    public static void isNull(String msg, Object... objs) {
        boolean flag = Objects.isNull(objs)
                || Arrays.stream(objs).filter(Objects::isNull).count() == objs.length;
        if (!flag) {
            msg(msg);
        }
    }

    /**
     * objs对象必须为null
     *
     * @param msg
     * @param params 占位符内容
     * @param objs   判断对象
     */
    public static void isNull(String msg, List<String> params, Object... objs) {
        boolean flag = Objects.isNull(objs)
                || Arrays.stream(objs).filter(Objects::isNull).count() == objs.length;
        if (!flag) {
            msg(msg, params);
        }
    }

    /**
     * objs对象必须为true
     *
     * @param msg
     * @param objs 判断对象
     */
    public static void isTrue(String msg, Boolean... objs) {
        notNull(msg, objs);
        boolean flag = Arrays.stream(objs).filter(d -> d).count() == objs.length;
        if (!flag) {
            msg(msg);
        }
    }

    /**
     * objs对象必须为true
     *
     * @param msg
     * @param params 占位符内容
     * @param objs   判断对象
     */
    public static void isTrue(String msg, List<String> params, Boolean... objs) {
        notNull(msg, params, objs);
        boolean flag = Arrays.stream(objs).filter(d -> d).count() == objs.length;
        if (!flag) {
            msg(msg, params);
        }
    }

    /**
     * objs对象必须为 false
     *
     * @param msg
     * @param objs 判断对象
     */
    public static void isFalse(String msg, Boolean... objs) {
        notNull(msg, objs);
        boolean flag = Arrays.stream(objs).filter(d -> !d).count() == objs.length;
        if (!flag) {
            msg(msg);
        }
    }

    /**
     * objs对象必须为 false
     *
     * @param msg
     * @param params 占位符内容
     * @param objs   判断对象
     */
    public static void isFalse(String msg, List<String> params, Boolean... objs) {
        notNull(msg, params, objs);
        boolean flag = Arrays.stream(objs).filter(d -> !d).count() == objs.length;
        if (!flag) {
            msg(msg, params);
        }
    }

    /**
     * objs内容必须为1
     *
     * @param msg
     * @param params
     * @param objs
     */
    public static void isStatusFinish(String msg, List<String> params, Integer... objs) {
        boolean flag = Objects.isNull(objs)
                || Arrays.stream(objs).filter(d -> Objects.isNull(d) || !d.equals(1)).count() > 0;
        if (flag) {
            msg(msg, params);
        }
    }

    /**
     * objs内容必须为1
     *
     * @param msg
     * @param objs
     */
    public static void isStatusFinish(String msg, Integer... objs) {
        boolean flag = Objects.isNull(objs)
                || Arrays.stream(objs).filter(d -> Objects.isNull(d) || !d.equals(1)).count() > 0;
        if (flag) {
            msg(msg);
        }
    }

    /**
     * objs内容必须为0
     *
     * @param msg
     * @param params
     * @param objs
     */
    public static void isStatusNotFinish(String msg, List<String> params, Integer... objs) {
        boolean flag = Objects.isNull(objs)
                || Arrays.stream(objs).filter(d -> Objects.isNull(d) || !d.equals(0)).count() > 0;
        if (flag) {
            msg(msg, params);
        }
    }

    /**
     * objs内容必须为0
     *
     * @param msg
     * @param objs
     */
    public static void isStatusNotFinish(String msg, Integer... objs) {
        boolean flag = Objects.isNull(objs)
                || Arrays.stream(objs).filter(d -> Objects.isNull(d) || !d.equals(0)).count() > 0;
        if (flag) {
            msg(msg);
        }
    }

    /**
     * objs内容必须为-1
     *
     * @param msg
     * @param params
     * @param objs
     */
    public static void isStatusCancel(String msg, List<String> params, Integer... objs) {
        boolean flag = Objects.isNull(objs)
                || Arrays.stream(objs).filter(d -> Objects.isNull(d) || !d.equals(-1)).count() > 0;
        if (flag) {
            msg(msg, params);
        }
    }

    /**
     * objs内容必须为-1
     *
     * @param msg
     * @param objs
     */
    public static void isStatusCancel(String msg, Integer... objs) {
        boolean flag = Objects.isNull(objs)
                || Arrays.stream(objs).filter(d -> Objects.isNull(d) || !d.equals(-1)).count() > 0;
        if (flag) {
            msg(msg);
        }
    }

    /**
     * values所有值不能小于0，值为null时不做判断
     *
     * @param msg
     * @param values
     */
    public static void notLTZero(String msg, BigDecimal... values) {
        boolean flag = Objects.nonNull(values)
                && Stream.of(values)
                .filter(Objects::nonNull)
                .filter(d -> d.compareTo(BigDecimal.ZERO) < 0)
                .count() > 0;
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
        throw new ErpParamException(LanguageUtils.msg(msg));
    }

    /**
     * 如果false则版本错误
     *
     * @param msg
     * @param flag
     */
    public static void version(String msg, boolean flag) {
        if (!flag) {
            throw new ErpVersionException(LanguageUtils.msg(msg));
        }
    }

    /**
     * 提示信息
     *
     * @param msg
     */
    public static void msg(String msg, Object... params) {
        throw new ErpParamException(LanguageUtils.msg(msg, params));
    }

    /**
     * 提示信息
     *
     * @param data
     * @param msg
     */
    public static void msg(Object data, String msg) {
        throw new ErpParamException(LanguageUtils.msg(msg), data);
    }

}
