package com.common.binder;

import android.os.RemoteException;

import com.common.app.AbstractRenrenApplication;
import com.common.manager.ConnectionManager;
import com.common.manager.LoginManager;
import com.common.messagecenter.base.Connection;
import com.common.messagecenter.base.IReconnectStrategy;
import com.common.messagecenter.base.Utils;
import com.common.utils.Config;

/**
 * @author dingwei.chen
 * */
public class RemoteServiceBinder extends PollBinder.Stub {

	private int mConnectNumber = 0;
	public static boolean sIsForeGround = true;
	
	public RemoteServiceBinder(){
//		Reader.getInstance().setBinder(this);
		Sender.getInstance().setBinder(this);
	}
	
	@Override
	public void connect() throws RemoteException {
		this.addConnect(1);
	}
	public boolean hasConnect(){
		return mConnectNumber>0;
	}
	public void addConnect(int offset){
		this.mConnectNumber+=offset;
	}
	
	@Override
	public void send(long key, String message) throws RemoteException {
		Sender.getInstance().send(key, message);
	}

//	@Override
//	public String read() throws RemoteException {
//		return Reader.getInstance().readMessage();
//	}

	@Override
	public MessageState getSendState() throws RemoteException {
		return Sender.getInstance().readSendState();
	}

	@Override
	public boolean isConnect() throws RemoteException {
		return ConnectionManager.getInstance().isConnected();
	}

	@Override
	public void disConnect() throws RemoteException {
		Utils.l("断开轮循连接");
		Connection.sIsStopBySingleLogin = false;
		ConnectionManager.getInstance().disConnect(true);
		LoginManager.getInstance().clean();
	}

	@Override
	public void notifyGetSendStateFromOtherApplication() throws RemoteException {
	}

	@Override
	public void notifyPollFromOtherApplication() throws RemoteException {
	}

	@Override
	public boolean isRecvOfflineMessage() throws RemoteException {
		return Connection.sIsRecvOfflineMsg;
	}

	@Override
	public boolean isServiceRunning() throws RemoteException {
		return AbstractRemoteService.sInstanceRef != null && AbstractRemoteService.sInstanceRef.get() != null;
	}

	@Override
	public void changeURL(String masURI, String host, int httpPort,
			int socketPort) throws RemoteException {
		Config.CURRENT_SERVER_URI = masURI;
		Config.HOST_NAME = host;
		Config.HTTP_DEFAULT_PORT = httpPort;
		Config.SOCKET_DEFAULT_PORT = socketPort;
		Config.SOCKET_URL = host;
		ConnectionManager.getInstance().disConnect(false);
		ConnectionManager.getInstance().start(AbstractRenrenApplication.getAppContext());
	}

	@Override
	public void changeAppGround(boolean isForeGround) throws RemoteException {
		sIsForeGround = isForeGround;
		if(isForeGround){
			ConnectionManager.getInstance().start(AbstractRenrenApplication.getAppContext());
			Connection.sReconnectTime = 5;
		}else{
			Connection.sReconnectTime = 30;
		}
		
	}

}
