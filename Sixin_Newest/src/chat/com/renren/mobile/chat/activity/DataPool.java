package com.renren.mobile.chat.activity;


import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

import android.widget.TextView;

import com.renren.mobile.chat.base.util.SystemUtil;

public class DataPool{

	
	private Map<String,Object> mData = new HashMap<String,Object>();
	public static final DataPool POOL =new DataPool();
	public static IDataPool sPool = null;
	
	public static IDataPool obtain(){
		if(sPool==null){
			sPool = (IDataPool)Proxy.newProxyInstance(IDataPool.class.getClassLoader(), new Class[]{IDataPool.class},new InvocationHandler() {
				public Object invoke(Object proxy, Method method, Object[] args)
						throws Throwable {
					String methodName = method.getName();
					String name = null;
					Object o = null;
					
					if(args!=null&&args.length>0){
						name = ""+args[0];
					}
					if(methodName.startsWith("get")){
						o = POOL.mData.get(name);
					}
					if(methodName.startsWith("put")){
						POOL.mData.put(name, args[1]);
					}
					
					if(methodName.startsWith("size")){
						return POOL.mData.size();
					}
					if(methodName.startsWith("clear")){
						POOL.mData.clear();
					}
					return o;
				}
			});
		}
		return sPool;
	}

	
	
}
