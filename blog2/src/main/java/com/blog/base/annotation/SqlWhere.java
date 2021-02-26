package com.blog.base.annotation;

import com.blog.base.em.WhereType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * sql条件
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface SqlWhere {

    /**
     * 判断方式，默认为`=`判断
     *
     * @return
     */
    WhereType type() default WhereType.EQ;

    /**
     * 使用find_in_set([0],[1])函数时，值放置的位置，默认为第二个位置
     *
     * @return
     */
    int findInSetIndex() default 1;

    /**
     * 是否构建为查询条件，默认为是
     *
     * @return
     */
    boolean where() default true;

    /**
     * 列名，默认为""
     *
     * @return
     */
    String column() default "";

    /**
     * 当要与当前表的内部字段对比时可指定另一个列名。
     *
     * @return
     */
    String innerColumn() default "";
}
