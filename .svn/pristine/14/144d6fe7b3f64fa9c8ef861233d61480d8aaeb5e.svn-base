package com.common.messagecenter.base;

import java.util.ArrayList;

import com.common.messagecenter.base.RefKeeper.Iter;

public interface IGetConnectionState {
	
	public final static int SUCCESS = 1;
	public final static int FAILED = 2;
	public final static int BEGIN_RECONNECT = 3;
	public final static int END_RECONNECT = 4;
	public final static int RECV_OFFLINE_MSG = 5;
	
	public void onConnectSuccess();

	public void onConnectFailed();

	public void onBeginReonnect();

	public void onEndReonnect();

	public void onRecvOffLineMessage();
}
