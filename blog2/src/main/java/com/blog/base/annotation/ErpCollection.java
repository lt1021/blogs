package com.blog.base.annotation;

import com.blog.base.service.BaseService;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Auther: wanglu
 * @Date: 2018/12/30 15:04
 * @Description:
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface ErpCollection {

    /**
     * 返回值前缀
     *
     * @return
     */
    String prefix() default "";

    /**
     * 调用对象class
     *
     * @return
     */
    Class<?> clazz() default BaseService.class;
}
