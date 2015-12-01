package com.renren.mobile.web.reflect;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * at 6:26 PM, 3/15/12
 * <p/>
 * String类型的字段或者返回String的函数
 *
 * @author afpro
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.FIELD})
public @interface JSCode {
}
