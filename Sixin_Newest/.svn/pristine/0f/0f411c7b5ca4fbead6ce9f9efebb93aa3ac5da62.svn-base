package com.renren.mobile.chat.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.common.manager.LoginManager;
import com.common.messagecenter.base.IConnectionManager;
import com.common.messagecenter.base.IGetConnectionState;
import com.common.messagecenter.base.RefKeeper;
import com.common.statistics.BackgroundUtils;
import com.core.util.DateFormat;
import com.renren.mobile.account.LoginControlCenter;
import com.renren.mobile.chat.R;
import com.renren.mobile.chat.RenrenChatApplication;
import com.renren.mobile.chat.base.util.SystemUtil;
import com.renren.mobile.chat.ui.account.SingleLoginActivity;
import com.renren.mobile.chat.ui.notification.FeedNotificationManager;
import com.renren.mobile.chat.ui.notification.MessageNotificationManager;

public class SingleLoginReceiver extends BroadcastReceiver{
	
	public static final RefKeeper<IGetConnectionState> sGetConnectionState = new RefKeeper<IGetConnectionState>();
	@Override
	public void onReceive(Context context, Intent i) {
		if(LoginManager.getInstance().isLogout()){
			return;
		}
		
		int status = i.getIntExtra("status", 0);
		switch (status) {
		case IGetConnectionState.SUCCESS:
			sGetConnectionState.iter(IConnectionManager.sOnConnectSuccessIter);
			return;
		case IGetConnectionState.FAILED:
			sGetConnectionState.iter(IConnectionManager.sOnConnectFailedIter);
			return;
		case IGetConnectionState.BEGIN_RECONNECT:
			sGetConnectionState.iter(IConnectionManager.sOnBeginReconnectIter);
			return;
		case IGetConnectionState.END_RECONNECT:
			sGetConnectionState.iter(IConnectionManager.sOnEndReonnectIter);
			return;
		case IGetConnectionState.RECV_OFFLINE_MSG:
			sGetConnectionState.iter(IConnectionManager.sOnRecvOfflineIter);
			return;
		}
		
		String msg = i.getExtras().getString("msg");
		RenrenChatApplication.sIsSingleLoginError = true;
		msg = RenrenChatApplication.getAppContext().getString(R.string.login_error_msg, DateFormat.now3());
		FeedNotificationManager.getInstance().clearAllNotification();
		FeedNotificationManager.getInstance().clearChatFeedList();
		MessageNotificationManager.getInstance().getMessageNotificationModel().clearUnReadMessageList();
		MessageNotificationManager.getInstance().clearAllNotification(context);
		SystemUtil.logerror(msg);
		Intent intent = new Intent();
		if(BackgroundUtils.getInstance().isAppOnForeground()){
			intent.setClass(RenrenChatApplication.getAppContext(),
					SingleLoginActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.putExtra("msg", msg);
			RenrenChatApplication.getAppContext().startActivity(intent);
		}else{
			LoginControlCenter.getInstance().logout(RenrenChatApplication.getmContext());
			MessageNotificationManager.getInstance().sendSingleLoginNotification(RenrenChatApplication.getAppContext(), msg);
		}
	}

}
