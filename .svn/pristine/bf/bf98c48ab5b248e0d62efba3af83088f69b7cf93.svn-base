package com.common.network;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;


/**
 * @author dingwei.chen1988@gmail.com
 * @说明 网络请求创建工具
 * */
public final class RequestConstructorDicProxy implements InvocationHandler{

	private static IReqeustConstructor sInstance = null;
	private RequestConstructorDicProxy(){}
	public static <T extends IReqeustConstructor> T getInstance(Class<T> clazz){
		if(sInstance == null){
			sInstance = (T) Proxy.newProxyInstance(
					RequestConstructorDicProxy.class.getClassLoader(),
					new Class[]{clazz},
					new RequestConstructorDicProxy());
		}
		return (T)sInstance;
	}
	@Override
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		Object returnObject = null;
		Request request = method.getAnnotation(Request.class);
		if(request!=null){
			Class clazz = request.request();
			Constructor[] cs = clazz.getConstructors();
			for(Constructor c:cs){
				try {
					returnObject = c.newInstance(args);
					return returnObject;
				} catch (Exception e) {}
			}
		}
		return returnObject;
	}
	
	
	
	
	
}
