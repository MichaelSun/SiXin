package com.sixin.proxy;

import android.content.Context;
import android.net.Uri;

import com.renren.mobile.account.LoginfoModel;


/**
 * @author dingwei.chen
 * @说明 人人客户端代理接口
 * */
interface IRenrenClientProxy {

	public String RENREN_CLIENT_APPLICATION_NAME = "com.renren.mobile.android";
	
	public String RENREN_CLIENT_ACCOUNT_URL = "content://com.renren.mobile.account/account";
	
	public Uri ACCOUNT_URI = Uri.parse(RENREN_CLIENT_ACCOUNT_URL);
	
	public boolean isInstall(Context context);
	
	public LoginfoModel queryLoginInfo(Context context);
}
