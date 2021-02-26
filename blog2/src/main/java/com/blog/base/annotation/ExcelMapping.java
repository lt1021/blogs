package com.blog.base.annotation;

import java.lang.annotation.*;

@Documented
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelMapping {

	/**
	 * 列头名
	 * 
	 * @return
	 */
	String column() default "";

}
