package com.common.manager;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.common.app.AbstractRenrenApplication;
import com.common.mcs.HttpProxy;
import com.common.messagecenter.HttpConnection;
import com.common.messagecenter.NewSocketConnection;
import com.common.messagecenter.ReconnectStrategyImpl;
import com.common.messagecenter.base.Connection;
import com.common.messagecenter.base.GetConnectionStateImpl;
import com.common.messagecenter.base.IConnectionManager;
import com.common.messagecenter.base.IMessage;
import com.common.messagecenter.base.Utils;
import com.common.statistics.LocalStatisticsManager;
import com.core.util.CommonUtil;
import com.core.util.SystemService;

public final class ConnectionManager implements IConnectionManager {
	private static final ConnectionManager mConnectionManager = new ConnectionManager();
	private static Connection sConnection = null;
	public static int sNetworkInfo;
	
	public static ConnectionManager getInstance() {
		return mConnectionManager;
	}
	
	private ConnectionManager() {
		sGetConnectionState.add(true, GetConnectionStateImpl.instance);
		Connection.setReconnectStrategy(ReconnectStrategyImpl.getInstance());
	}

	/**
	 * 根据网络情况发起连接
	 */
	public void start(Context context) {
		Utils.l("||||\n" + CommonUtil.printStackElements() + "\n||||");
		LocalStatisticsManager.getInstance().uploadBackgroundRunStatistics();
			
		if(LoginManager.getInstance().isLogoutWithoutReset()){
			return;
		}
		NetworkInfo mobileInfo = SystemService.sConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		NetworkInfo wifiInfo = SystemService.sConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		
		/** 需要建立的网络连接类型(socket/http) **/
		final int connType = getNetworkType(mobileInfo, wifiInfo, context);
		/** 当前网络连接情况 **/
		int connInfo = getNetworkInfo(mobileInfo, wifiInfo, context);
		// 当前网络全部断掉时，关闭连接.
		if (connType == Connection.NONETWORK) {
			if (sConnection != null) {
				Utils.log("===============ConnectionManager start disconnect=======================");
				sConnection.disconnect(false);
			}
			sConnection = null;
			return;
		}
		// 假如 1.Connection已连接成功或正在连接 或者 2.Connection连接的时候使用的方式和当前的网络状态一致
		// 不做任何处理。
		if(sConnection != null){
			if (((sConnection.Status == Connection.CONNECTED || sConnection.Status == Connection.CONNECTING)
					&& connType == sConnection.Type && connInfo == sNetworkInfo) && !VpnManager.getInstance().isVpnStateChanged()) {
				Utils.log("do nothing");
				return;
			}
		}
		if(Connection.sIsStopBySingleLogin){
			return;
		}
		sNetworkInfo = connInfo;
		VpnManager.getInstance().refreshVpnState();
		// 网络发生变化，假如有连接，断掉。
		checkConnection();

		if(sConnection != null)
			Connection.sIsChangedByNetwork.set(sConnection.Type != Connection.HTTP);
		else
			Connection.sIsChangedByNetwork.set(true);
		sConnection = null;
		Utils.l("Begin New Connection Object");
		if (connType == Connection.HTTP && "com.renren.mobile.chat".equals(AbstractRenrenApplication.PACKAGE_NAME)) {
			sConnection = new HttpConnection(this);
		} else {
			sConnection = new NewSocketConnection(this);
		}
		sConnection.Type = connType;
		sConnection.start();
	}

	/**
	 * 发送消息
	 * 
	 * @param msg
	 */
	public synchronized void sendMessage(IMessage msg) {
		if (sConnection != null) {
			Utils.log(msg.getContent());
			sConnection.sendMessage(msg);
		} else {
			Utils.log("===============connectionManager sendMessage connection is null");
			start(AbstractRenrenApplication.getAppContext());
			if (sConnection != null)
				sConnection.sendMessage(msg);
			else
				msg.onSendFailed("connectionManager sendMessage connection is null");
		}
	}

	/**
	 * 断开连接
	 */
	public synchronized void disConnect() {
		disConnect(true);
	}
	
	public synchronized void disConnect(boolean isLogout) {
		if (sConnection != null) {
			sConnection.disconnect(isLogout);
		}
	}

	/**
	 * 判断是否连接建立成功
	 * 
	 * @return true:建立成功 false:建立失败
	 */
	public synchronized boolean isConnected() {
		if (sConnection == null) {
			return false;
		} else {
			return Connection.isReady.get()
					&& sConnection.Status == Connection.CONNECTED;
		}
	}

	private synchronized void checkConnection() {
		if (sConnection != null && sConnection.Status != Connection.DISCONNECTED) {
			sConnection.disconnect(true);
			sConnection.cleanBeforeReconnect();
		}
	}

	/**
	 * 根据网络情况判断需要建立的连接
	 * 
	 * @return Connection.NONETWORK:当前无可用网络 Connection.HTTP:建立HTTP连接
	 *         Connection.SOCKET:建立Socket连接
	 */
	private static int getNetworkType(NetworkInfo mobile, NetworkInfo wifi,
			Context context) {
		if ((mobile == null || !mobile.isConnected()) && (wifi == null || !wifi.isConnected())) {
			return Connection.NONETWORK;
		}
		
		if (!wifi.isConnected() && mobile.isConnected()
				&& HttpProxy.getProxyNetProxy(context) != null) {
			return Connection.HTTP;
		}
		return Connection.SOCKET;
	}

	private static int getNetworkInfo(NetworkInfo mobile, NetworkInfo wifi,
			Context context) {
		int mobileType = 0;
		int wifiType = 0;
		if (mobile != null && mobile.isConnected()) {
			mobileType = 1;
		}
		if (wifi != null && wifi.isConnected()) {
			wifiType = 2;
		}
		return wifiType | mobileType;
	}
}
