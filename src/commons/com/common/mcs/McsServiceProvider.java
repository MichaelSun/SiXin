package com.common.mcs;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import com.core.util.CommonUtil;

/**
 * @author dingwei.chen
 * */
public final class McsServiceProvider {

	public static IMcsService sService = null;

	public static IMcsService getProvider() {
		if (sService == null) {
			sService = (IMcsService) Proxy.newProxyInstance(
					McsServiceProvider.class.getClassLoader(),
					new Class[] { IMcsService.class }, new InvocationHandler() {
						@Override
						public Object invoke(Object proxy, Method method,
								Object[] args) throws Throwable {
							Object o = method.invoke(
									HttpMasService.getInstance(), args);
							StringBuilder builder = new StringBuilder();
							builder.append(method.getName() + "(");
							for (Object arg : args) {
								builder.append(arg + ",");
							}
							builder.append(")");
							CommonUtil.log("mcs", builder.toString());
							return o;
						}
					});
		}
		return sService;
	}

}
