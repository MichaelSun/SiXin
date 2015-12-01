package com.renren.mobile.chat.common;

import android.util.Log;

import com.common.mcs.INetRequest;
import com.core.json.JsonObject;
import com.renren.mobile.chat.base.util.SystemUtil;

public class ResponseError {

	
	/**
	 * 用于判断网络返回的网络数据是否正常
	 * **/
	public static boolean noError(final INetRequest request, JsonObject rsp) {
		return noError(request, rsp, true);
	}

	/* 增加一个show参数用来标示是否弹toast */
	/**
	 * 能够检测session是否过期的noError方法，如果session过期，自动跳转到登录界面
	 * 
	 * @param request
	 * @param rsp
	 * @param show
	 * @param context
	 * @return
	 */
	public static boolean noError(final INetRequest request, JsonObject data,
			boolean show) {
		boolean result = checkError(data);
		if(show){
			showError(data);
		}
		return result;
	}
	
	/**
	 * 显示error_message节点
	 * **/
	public static void showError(JsonObject data){
		int error_code = (int) data.getNum("error_code");
		String error_msg = data.getString("error_msg");
		if(error_code == 0){
			return ;
		}
		switch (error_code) {
		case -99:
			SystemUtil.toast("无法连接网络，请检查您的手机网络设置...");
			break;
		case 11001:
			SystemUtil.toast("注册失败，用户已经存在");
			break;
		case 10001:
			break;
		case 10002:			
			break;
		case 10003:
			break;
		case 10004:
			break;
		default:
			SystemUtil.toast(error_msg);
		}
	}
	
	/**
	 * 判断response是否包含error_code节点
	 * **/
	public static boolean checkError(JsonObject data){
		int error_code = (int) data.getNum("error_code");
		if (error_code != 0) {
			return false;
		}
		return true;
	}

}
