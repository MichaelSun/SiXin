package com.common.messagecenter.base;

import android.content.Intent;

import com.common.app.AbstractRenrenApplication;
import com.common.manager.LoginManager;

public class GetConnectionStateImpl implements IGetConnectionState{
	public final static IGetConnectionState instance = new GetConnectionStateImpl();
	private GetConnectionStateImpl(){}
	
	public static void init(){
		IConnectionManager.sGetConnectionState.add(true, instance);
	}
	
	public static void sendBroadcast(int status){
		if(LoginManager.getInstance().isLogoutWithoutReset()){
			return;
		}
		Intent intent = new Intent();
		intent.setAction(AbstractRenrenApplication.PACKAGE_NAME
				+ ".SingleLogin");
		intent.putExtra("status", status);
		AbstractRenrenApplication.getAppContext().sendBroadcast(intent);
		Utils.l("after send broadcase");
	}

	@Override
	public void onConnectSuccess() {
		sendBroadcast(SUCCESS);
	}

	@Override
	public void onConnectFailed() {
		sendBroadcast(FAILED);
	}

	@Override
	public void onBeginReonnect() {
		sendBroadcast(BEGIN_RECONNECT);
	}

	@Override
	public void onEndReonnect() {
		sendBroadcast(END_RECONNECT);
	}

	@Override
	public void onRecvOffLineMessage() {
		sendBroadcast(RECV_OFFLINE_MSG);
	}
	
}
