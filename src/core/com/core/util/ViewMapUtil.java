package com.core.util;

import java.lang.reflect.Field;

import android.view.View;

/**
 * @author dingwei.chen
 * @说明 视图和ID映射关系工具
 * @类说明  单例类
 * @说明 
 * 			解除了ID映射间的重复代码和类型装换,
 * 			这样可以把重心转移到业务逻辑
 * 			在继承上不侵入任何类
 * @使用
 * 			1.为了提高效率将需要映射的View控件可见域置为public
 * 			2.对于需要映射的对象中的属性导入:
 * 				{@link com.renren.mobile.chat.base.annotation.ViewMapping}
 * 			3.分离视图
 * */
public final class ViewMapUtil {

	private static ViewMapUtil sInstance = new ViewMapUtil();
	private ViewMapUtil(){}
	public static ViewMapUtil getUtil(){
		return sInstance;
	}
	
	/**
	 * @author dingwei.chen
	 * @param	 object 	要映射对象
	 * @param	 rootView 	要映射对象所要查询的根控件
	 * */
	public void viewMapping(Object object,View rootView){
		Class clazz = object.getClass();
		Field[] fields = clazz.getFields();//必须是public
		for(Field f:fields){
			ViewMapping mapping = f.getAnnotation(ViewMapping.class);
			View view = null;
			int id = 0;
			if(mapping!=null){
				try {
					id = mapping.ID();
					view = rootView.findViewById(id);
					f.setAccessible(true);
					f.set(object, view);
				} catch (Exception e) {
					CommonUtil.log("viewmap", "view map error = 0x"+Integer.toHexString(id)+":"+e);
					throw new RuntimeException();
				}
			}
		}
	}
	
}
