package com.blog.base.annotation;

import java.lang.annotation.*;

/**
 * @author lt
 * @date 2021/3/5 11:43
 */
@Documented
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Export {
}
