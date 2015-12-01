package com.renren.mobile.chat.webview;

import java.net.URLEncoder;

import android.os.Build;

import com.common.manager.LoginManager;
import com.common.mcs.McsServiceProvider;
import com.renren.mobile.chat.RenrenChatApplication;

public class RenRenWapUrlFactory {

	// public static RenRenWapUrlFactory mInstance = new RenRenWapUrlFactory();

	public static interface URL_TYPE {
		final int FEED_BACK = 1;      		// 意见反馈页面
		final int SEARCH_FRIENDS = 2;		// 查找好友页面
		final int PEESONEL_FEED = 3; 		// 个人新鲜事
		final int REGISTER_PROTOCAL = 4; 	// 注册免责声明
		final int NO_URL = 0;
	}

	// private RenRenWapUrlFactory() {
	//
	// }

	// public static RenRenWapUrlFactory getInstance() {
	// return mInstance;
	// }

	/**
	 * 获取需要加载的url
	 * */
	public static String getUrl(int type , long uid) {
		StringBuffer urlSb = null;
		switch (type) {
		case URL_TYPE.FEED_BACK:
			urlSb = new StringBuffer("http://3g.renren.com/ep.do?c=8000097&sid=");
			urlSb.append(LoginManager.getInstance().getLoginInfo().mTicket);
			urlSb.append("&mode=");
			urlSb.append(Build.MODEL);

			urlSb.append("-");
			urlSb.append(Build.VERSION.SDK);
			urlSb.append("-");
			urlSb.append(Build.VERSION.RELEASE);
			urlSb.append("&ver=");
			urlSb.append(RenrenChatApplication.versionName);
			urlSb.append("&stage=sixinclient&device=android");
			urlSb.append("&client_info=").append(URLEncoder.encode(McsServiceProvider.getProvider().getClientInfo()));
			break;
		case URL_TYPE.SEARCH_FRIENDS:
			urlSb = new StringBuffer("http://mt.renren.com/client/search?sid=");
			urlSb.append(LoginManager.getInstance().getLoginInfo().mTicket);
			urlSb.append("&appid=");
			urlSb.append(RenrenChatApplication.appid);
			urlSb.append("&client_info=").append(URLEncoder.encode(McsServiceProvider.getProvider().getClientInfo()));
			break;
		case URL_TYPE.PEESONEL_FEED:
			//http://mt.renren.com/profile/{uid}?sid={sid}&p={高清屏:2|普通屏:1}&client&client_info={json_client_info}&appid={appid}
			urlSb = new StringBuffer("http://mt.renren.com/profile/");
			urlSb.append(uid);
			urlSb.append("?sid=");
			urlSb.append(LoginManager.getInstance().getLoginInfo().mTicket);  //sid
			urlSb.append("&p=1");
			urlSb.append("&client_info=").append(URLEncoder.encode(McsServiceProvider.getProvider().getClientInfo())); //client_info
			urlSb.append("&appid=");
			urlSb.append(RenrenChatApplication.appid);  //appid
			break;
		case URL_TYPE.REGISTER_PROTOCAL:
			urlSb = new StringBuffer("http://mudsky.com/m/privacy.html");
			break;
		default:
			break;
		}
		if (urlSb != null) {
			return urlSb.toString();
		} else {
			return null;
		}

	}

}
