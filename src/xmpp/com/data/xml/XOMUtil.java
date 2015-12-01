package com.data.xml;

import java.lang.reflect.Field;
import java.util.Collection;

import org.xml.sax.Attributes;

import com.core.util.CommonUtil;
import com.data.xmpp.XMPPNode;


/**
 * @author dingwei.chen
 * */
public final class XOMUtil {

	private static XOMUtil sInstance = new XOMUtil();
	private XOMUtil(){}
	public static XOMUtil getInstance (){
		return sInstance;
	}
	
	
	/*映射节点*/
	public void mappingNode(Object object,XMPPNode node){
		if(node==null){
			return;
		}
		Class clazz = object.getClass();
		Field[] fields = clazz.getFields();
		for(Field f:fields){
			f.setAccessible(true);
			XMLMapping xm = f.getAnnotation(XMLMapping.class);
			if(xm!=null){
				switch(xm.Type()){
					case NODE:
						{
							if(xm.Name().equals(node.getNodeName())){
								try {
									if(!xm.isIterable()){
										f.set(object, node);
									}else{
										Collection iter = (Collection)f.get(object);
										if(iter!=null){
											iter.add(node);
										}
									}
								} catch (Exception e) {}
								break;
							}
						}
					break;
				}
			}
		}
	}
	
	/*object to xml MAPPING*/
	public void OXMapping(XMPPNode node,Object object){
		Class clazz = object.getClass();
		Field[] fields = clazz.getFields();
		for(Field f:fields){
			f.setAccessible(true);
			XMLMapping mapping = f.getAnnotation(XMLMapping.class);
			if(mapping!=null){
				try {
					Object returnObject =  f.get(object);
					CommonUtil.log("load", "--->"+mapping.Name()+" = "+returnObject);
					if(returnObject!=null){
						node.addAttribute(mapping.Name(),returnObject);
					}
				} catch (Exception e) {} 
			}
		}
	}
	
	/*映射属性*/
	public void mappingAttribute(XMPPNode object,Attributes attributes){
		if(attributes==null){
			return;
		}
		Class clazz = object.getClass();
		Field[] fields = clazz.getFields();
		for(Field f:fields){
			f.setAccessible(true);
			XMLMapping xm = f.getAnnotation(XMLMapping.class);
			if(xm!=null){
				switch(xm.Type()){
					case ATTRIBUTE:
						{
							String value = attributes.getValue(xm.Name());
//							System.out.println(xm.Name()+"="+value);
							try {
								if(value!=null && value.length()>0){
									f.set(object, value);
								}
							} catch (Exception e) {}
						}
					break;
				}
			}
		}
		
	}
	

	
	
	/**
	 * @author dingwei.chen
	 * */
	private Object executeOperator(XMLType operatorType,String string) throws Exception{
		switch (operatorType) {
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
		}
		return null;
	}
	
	
	
}
