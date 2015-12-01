package com.common.messagecenter;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.SystemClock;

import com.common.app.AbstractRenrenApplication;
import com.common.manager.ConnectionManager;
import com.common.manager.LoginManager;
import com.common.messagecenter.base.Connection;
import com.common.messagecenter.base.IConnectionManager;
import com.common.messagecenter.base.IReconnectStrategy;
import com.common.messagecenter.base.Utils;
import com.common.statistics.BackgroundUtils;
import com.core.util.SystemService;

/**
 * @author yang.chen
 * */
public class ReconnectStrategyImpl implements IReconnectStrategy {
	private AlarmManager mAlermManger;
	private PendingIntent mPendingIntent;
	private ReConnectReceiver mKeepConnectReciver = new ReConnectReceiver();
	private static final ReconnectStrategyImpl sInstance = new ReconnectStrategyImpl();
	private boolean mIsReconnection = false;

	private ReconnectStrategyImpl() {}

	public static ReconnectStrategyImpl getInstance() {
		return sInstance;
	}

	@Override
	public void beginReconnect() {
		if (LoginManager.getInstance().isLogoutWithoutReset() || mIsReconnection == true)
			return;
		IConnectionManager.sGetConnectionState.iter(IConnectionManager.sOnBeginReconnectIter);
		mIsReconnection = true;
		Utils.log("===============beginReConnection");
		Intent intent = new Intent(AbstractRenrenApplication.PACKAGE_NAME
				+ ".reconnection");
		mPendingIntent = PendingIntent.getBroadcast(
				AbstractRenrenApplication.getAppContext(), 0, intent, 0);

		// We want the alarm to go off 5 seconds from now.
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());
		if(Connection.sReconnectTime < 5){
			Connection.sReconnectTime = 5;
		}
		int reconnectTime = 5;
		if(!BackgroundUtils.getInstance().isAppOnForeground()){
			reconnectTime = 30;
		}
		Utils.l("reconnect time:" + reconnectTime);
		SystemService.sAlarmManager.setRepeating(
				AlarmManager.ELAPSED_REALTIME_WAKEUP,
				SystemClock.elapsedRealtime() + Connection.sReconnectTime * 1000, Connection.sReconnectTime * 1000,
				mPendingIntent);
		IntentFilter filter = new IntentFilter(
				AbstractRenrenApplication.PACKAGE_NAME + ".reconnection");
		AbstractRenrenApplication.getAppContext().registerReceiver(
				mKeepConnectReciver, filter);
	}

	public synchronized void endReconnect() {
		Utils.log("===============endReConnection");
		if(mIsReconnection == false)
			return;
		mIsReconnection = false;
		try {
			IConnectionManager.sGetConnectionState
			.iter(IConnectionManager.sOnEndReonnectIter);
			AbstractRenrenApplication.getAppContext().unregisterReceiver(
					mKeepConnectReciver);
			if (mAlermManger != null && mPendingIntent != null) {
				mAlermManger.cancel(mPendingIntent);
			}
		} catch (Exception e) {
			Utils.log("endKeepConnection error:" + e.toString());
			Utils.log(e.getStackTrace().toString());
			e.printStackTrace();
		}
	}

	public class ReConnectReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			Utils.l("ReConnectionReciver(isLogout:" +
					LoginManager.getInstance().isLogoutWithoutReset() + ")");
			
			if (!LoginManager.getInstance().isLogoutWithoutReset() && !Connection.sIsStopBySingleLogin)
				ConnectionManager.getInstance().start(
						AbstractRenrenApplication.getAppContext());
			else
				sInstance.endReconnect();
		}
	}

	@Override
	public boolean isInReconnect() {
		return mIsReconnection;
	}

}
