package com.core.orm;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * @author dingwei.chen
 * @说明 表示对象级别和数据库表之间的字段映射关系保证你的属性的可见域是public
 * */ 
@Retention(RetentionPolicy.RUNTIME)
@Target( { ElementType.FIELD})
public @interface ORM {
	String mappingColumn() default "";
	boolean isInsert() default true;
	/**
	 * @see com.renren.mobile.chat.base.inter.DatabaseTypeConstant
	 * */
	int columnType() default ORMType.NORMAL_COLUMN;
	Class iteratorClass() default Object.class;
}
