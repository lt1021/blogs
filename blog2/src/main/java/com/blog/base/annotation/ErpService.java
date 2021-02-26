package com.blog.base.annotation;

import com.baomidou.mybatisplus.extension.service.IService;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author tiger (tiger@microsoul.com) 2016/11/15
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface ErpService {

    String prefix() default "";

    Class<?> clazz() default IService.class;
}
