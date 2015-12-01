package com.common.binder;

import com.common.app.AbstractRenrenApplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * @author dingwei.chen
 * @说明 消息推送广播接收
 * */
public abstract class AbstractPushReceiver extends BroadcastReceiver{

	public static final String ACTION_NAME =  ".push.PushMessages";
	public static final String DATA = "message_content";
	@Override
	public void onReceive(Context context, Intent intent) {
		String message_content = intent.getStringExtra(DATA);
		if(message_content!=null&& message_content.trim().length()>0){
			this.onPush(message_content);
		}
	}
	public abstract void onPush(String message);
	
	public static interface OnReceiveListener{
		public void onnReceive(String message);
	}
}
