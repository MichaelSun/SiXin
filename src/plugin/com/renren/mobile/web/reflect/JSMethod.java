package com.renren.mobile.web.reflect;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * at 6:44 PM, 3/7/12
 * <p/>
 * 一个JSMethod是对JS调用的响应，参数必须是
 * <ul>
 * <li>boolean</li>
 * <li>Boolean</li>
 * <li>int</li>
 * <li>Integer</li>
 * <li>long</li>
 * <li>Long</li>
 * <li>double</li>
 * <li>Double</li>
 * <li>String</li>
 * <li>{@link org.json.JSONObject}</li>
 * <li>{@link org.json.JSONArray}</li>
 * </ul>
 * 也可以更狠的使用Object，因为参数可能是{@link org.json.JSONObject#NULL}
 * eg.
 * {@code @JSMethod("methodName") void fuck()}
 *
 * @author afpro
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface JSMethod {
    String value();
}
