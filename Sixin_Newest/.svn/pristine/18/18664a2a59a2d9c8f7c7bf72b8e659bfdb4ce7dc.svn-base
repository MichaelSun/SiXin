package com.renren.mobile.chat.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.common.messagecenter.base.Utils;
import com.renren.mobile.chat.third.ThirdActionsCenter;

public class ChatCallReceiver extends BroadcastReceiver {
	
	@Override
	public void onReceive(Context context, Intent intent) {
		Utils.l("onReceive");
		ThirdActionsCenter.getInstance()
				.dispatchAction(context,
								intent.getAction(),
								intent);
	}

}
