package com.data.xml;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * @author dingwei.chen
 * */
@Retention(RetentionPolicy.RUNTIME)
@Target( { ElementType.FIELD })
public @interface XMLMapping {
	XMLType Type();
	String Name() default "";
	boolean isIterable() default false;
}
