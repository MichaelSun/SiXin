package com.renren.mobile.chat.actions.requests;



/**
 * @author dingwei.chen1988@gmail.com
 * @说明 网络请求创建工具
 * */
public final class RequestConstructorProxy{

	private static RequestConstructor sInstance = null;
	private RequestConstructorProxy(){}
	public static RequestConstructor getInstance(){
		if(sInstance==null){
			sInstance =  com.common.network.RequestConstructorDicProxy.getInstance(RequestConstructor.class);
		}
		return sInstance;
	}
	
}
