package com.renren.mobile.chat.ui;

import android.app.Activity;
import android.os.Bundle;

import com.renren.mobile.account.LoginControlCenter;
import com.renren.mobile.chat.RenrenChatApplication;
import com.renren.mobile.chat.ui.account.LoginSixinActivity;
import com.renren.mobile.chat.ui.notification.MessageNotificationModel;


public class SingleLoginNotificationCancel extends Activity {

	public MessageNotificationModel messageNotificationModel;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LoginControlCenter.sendQuiteMessage();
	    LoginSixinActivity.show(this, LoginSixinActivity.LOGIN_SIXIN);
	    finish();
	}
	
}
