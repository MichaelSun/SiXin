package com.renren.mobile.chat.base.util;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * @author dingwei.chen
 * @说明 新鲜事属性映射注解 在运行时也有效
 * */
@Retention(RetentionPolicy.RUNTIME)
@Target( { ElementType.FIELD })
public @interface XMLAttributeMapping {
	/**
	 * @author dingwei.chen
	 * 表示在xml对应的属性名
	 * */
	String attributeName();
	/**
	 * @see XMLTurnType
	 * 表示需要转换的类型
	 * */
	XMLTurnType turnType();
}
