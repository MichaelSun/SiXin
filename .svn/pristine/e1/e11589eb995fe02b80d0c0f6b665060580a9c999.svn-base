package com.common.binder;

import java.lang.ref.WeakReference;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.common.app.AbstractRenrenApplication;
import com.common.manager.ConnectionManager;
import com.common.manager.LoginManager;
import com.common.messagecenter.base.ConnHandler;
import com.common.messagecenter.base.Connection;
import com.common.messagecenter.base.Utils;
import com.core.util.AbstractApplication;
import com.core.util.CommonUtil;

public abstract class AbstractRemoteService extends Service {
	public static final RemoteServiceBinder BINDER = new RemoteServiceBinder();
	public static WeakReference<AbstractRemoteService> sInstanceRef = null;
	/**
	 * 网络回调
	 * */
	private final ConnHandler CONN_HANDLER = new ConnHandler() {
		@Override
		public boolean receive(String msgFromServer) {
			sendBrodcast(msgFromServer);
			return false;
		}

		@Override
		public void connectionLost() {}
	};
	void sendBrodcast(String message){
		Intent i = new Intent(AbstractRenrenApplication.PACKAGE_NAME + AbstractPushReceiver.ACTION_NAME);
		i.putExtra(AbstractPushReceiver.DATA, message);
		this.sendBroadcast(i);
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		sInstanceRef = new WeakReference<AbstractRemoteService>(this);
		Connection.refKeeper.clean();
		Connection.refKeeper.add(false, CONN_HANDLER);
		if (!LoginManager.getInstance().isLogout() && AbstractRenrenApplication.USER_ID != -1) {
			this.startPollThread();// 开启轮询
		}
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		sInstanceRef = new WeakReference<AbstractRemoteService>(this);
		if (!LoginManager.getInstance().isLogout() && AbstractRenrenApplication.USER_ID != -1 && !Connection.sIsStopBySingleLogin) {
			this.startPollThread();// 开启轮询
		}
		return super.onStartCommand(intent, flags, startId);
	}

	protected void startPollThread() {
		Connection.sIsStopBySingleLogin = false;
		ConnectionManager.getInstance().start(
				AbstractApplication.getAppContext());
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		sInstanceRef = null;
		System.exit(0);
	};
	@Override
	public IBinder onBind(Intent intent) {
		return BINDER.asBinder();
	}
	@Override
	public boolean onUnbind(Intent intent) {
		boolean flag = super.onUnbind(intent);
		BINDER.addConnect(-1);//减少一个连接数
		return flag;
	}
	
}
