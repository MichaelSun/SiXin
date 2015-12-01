package com.renren.mobile.chat.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.common.messagecenter.base.Utils;
import com.common.statistics.LocalStatisticsManager;
import com.common.utils.Config;
import com.renren.mobile.chat.RenrenChatApplication;
import com.renren.mobile.chat.service.ChatService;
import com.renren.mobile.chat.ui.contact.C_ContactsData;
import com.renren.mobile.chat.ui.contact.C_ContactsData.TYPE;
import com.renren.mobile.chat.ui.notification.FeedNotificationManager;
/**
 * 监听一下broadcast的receiver
 * 手机启动broadcast
 * 网络连接状态broadcast
 * 收发sms broadcast
 * */


public class RenRenChatReceiver extends BroadcastReceiver {
	
	@Override
	public void onReceive(Context context, Intent intent) {
		if(RenrenChatApplication.from == 0){
			RenrenChatApplication.init();
		}
//		if (!mobNetInfo.isConnected() && !wifiNetInfo.isConnected()) {
//			//TODO C_ContactsData  提供一个将在线联系人全部下线的方法
//			//C_ContactsData.getInstance().clearContactsOnlineStatus();
//		} else {
//			//TODO C_ContactsData  提供一个从新获取所有在线联系人的方法（此方法在联系人列表不为空&&在线联系人列表为空的情况下执行）
//			if(C_ContactsData.getInstance().get(TYPE.ALL_CONTACTS).size()>0 && C_ContactsData.getInstance().get(TYPE.COMMON_CONTACTS).size() == 0){
//				C_ContactsData.getInstance().getContactListOnlineStatus();
//			}
//		}
		Config.changeHttpURL();
		LocalStatisticsManager.getInstance().activityClient();
		ChatService.start();
		FeedNotificationManager.getInstance();
//		RenRenBackgroundService.start(context);

	}
	


}
