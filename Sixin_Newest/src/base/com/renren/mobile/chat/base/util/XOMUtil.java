package com.renren.mobile.chat.base.util;

import java.lang.reflect.Field;

import org.w3c.dom.Node;

import android.text.TextUtils;

//import com.renren.mobile.chat.base.xml.ChatBaseXMLNode;


/**
 * @author dingwei.chen
 * @说明 xml数据往对象模型的映射工具
 * 			因为不同层面的模型之间存在阻抗
 * */
//public final class XOMUtil {
//
//	private static XOMUtil sInstance = new XOMUtil();
//	private XOMUtil(){}
//	public static XOMUtil getInstance (){
//		return sInstance;
//	}
//	
//	public void feedMapping(Object object,Node node){
//		Class clazz = object.getClass();
//		Field[] fields = clazz.getFields();
//		for(Field f:fields){
//			f.setAccessible(true);
//			XMLAttributeMapping mapping = f.getAnnotation(XMLAttributeMapping.class);
//			if(mapping!=null){
//				String attrName = mapping.attributeName();
//				XMLTurnType operatorType = mapping.turnType();
//				String str = XmlParseUtil.getAttribute(attrName, node);
//				try {
//					if(!TextUtils.isEmpty(str)){
//						Object returnObject = executeOperator(operatorType, str);
//						if(returnObject!=null){
//							f.set(object, returnObject);
//						}
//					}
//				} catch (Exception e) {}
//			}
//		}
//	}
//	public void xmlNodeMapping(Object object,ChatBaseXMLNode node){
//		Class clazz = object.getClass();
//		Field[] fields = clazz.getFields();
//		for(Field f:fields){
//			f.setAccessible(true);
//			XMLAttributeMapping mapping = f.getAnnotation(XMLAttributeMapping.class);
//			if(mapping!=null){
//				try {
//					Object returnObject =  f.get(object);
//					if(returnObject!=null){
//						node.addAttribute(mapping.attributeName(),returnObject);
//					}
//				} catch (Exception e) {} 
//			}
//		}
//	}
//	
//	
//	/**
//	 * @author dingwei.chen
//	 * @param operatorType 操作符
//	 * */
//	private Object executeOperator(XMLTurnType operatorType,String string) throws Exception{
//		switch (operatorType) {
//			case INT:
//				return Integer.parseInt(string);
//			case BOOL:
//				if(string==null){
//					return Integer.valueOf(0);//false
//				}else{
//					if(string.toLowerCase().equals("true")||string.equals("1")){
//						return Integer.valueOf(1);
//					}else{
//						return Integer.valueOf(0);//false
//					}
//				}
//			case STRING:
//				return string;
//			case LONG:
//				return Long.parseLong(string);
//		}
//		return null;
//	}
//	
//	
//	
//}
