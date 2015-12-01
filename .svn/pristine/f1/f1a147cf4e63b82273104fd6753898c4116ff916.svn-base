package com.renren.mobile.web.reflect;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * at 6:52 PM, 3/7/12
 * <p/>
 * 一个ResourceMethod是一个响应HTTP请求的方法，原型必须是：
 * {@code WebResponse method(URI path);}
 * <p/>
 * 标记中可以指定对path的过滤，这个正则必须匹配整个path
 * 才行，如果
 * {@code path = "/image/fuck.jpg"}
 * 那么就要是
 * {@code ^/image/.*$}
 * 而不能是
 * {@code ^/image/}
 *
 * @author afpro
 * @see com.renren.mobile.web.WebResponse
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ResourceMethod {
    String value() default "";
}
