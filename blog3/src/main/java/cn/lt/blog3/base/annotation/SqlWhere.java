package cn.lt.blog3.base.annotation;

import cn.lt.blog3.base.util.StringHelp;
import org.w3c.dom.Element;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * sql条件
 * @author lt
 * @date 2021/4/8 10:02
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface SqlWhere {

    /**
     * 是否构建为查询条件，默认为是
     *
     * @return
     */
    boolean where() default true;

    /**
     * 列名，默认为StringHelp.EMPTY
     *
     * @return
     */
    String column() default StringHelp.EMPTY;

    /**
     * 是否为or
     *
     * @return
     */
    boolean isOr() default false;

    /**
     * 当类型为or时，与指定字段条件形成or条件
     *
     * @return
     */
    String[] orFiled() default {StringHelp.EMPTY};
}
