package com.common.messagecenter.base;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import android.content.Intent;
import android.text.TextUtils;

import com.common.app.AbstractRenrenApplication;
import com.common.manager.ConnectionManager;
import com.common.manager.LoginManager;
import com.common.utils.Config;
import com.common.utils.LanguageSettingUtil;
import com.core.util.CommonUtil;

/**
 * @author yang-chen
 */
public abstract class Connection extends Thread {
	public static String sCommonBuildString = null;
	public static final RefKeeper<ConnHandler> refKeeper = new RefKeeper<ConnHandler>();
	public static final AtomicBoolean sIsChangedByNetwork = new AtomicBoolean(
			false);
	/* 连接状态 */
	public static final int DISCONNECTED = 0;
	public static final int CONNECTING = 1;
	public static final int CONNECTED = 2;
	public static final int INTERRUPTED = 3; // 表示有特殊情况，发起的连接不会处理
	/* 网络类型 */
	public static final int NONETWORK = -1;
	public static final int SOCKET = 0;
	public static final int HTTP = 1;

	public static final AtomicBoolean isReady = new AtomicBoolean(false);
	public static IConnectionManager sConnectionManager = null;
	public static boolean sIsRecvOfflineMsg = false;
	public volatile int Status = DISCONNECTED;
	public volatile int Type = NONETWORK;
	public AtomicInteger reConnectionTimes = new AtomicInteger(0);
	public volatile static boolean sIsStopBySingleLogin = false;
	public static int sReconnectTime = 5;

	protected final Parser loopParser = new Parser();
	protected ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();

	private final ConnectionIter lostIter = new ConnectionIter();
	private static final BlockingQueue<SendMessageRunnable> messageTasks = new LinkedBlockingQueue<SendMessageRunnable>(
			10);
	private static IReconnectStrategy mReconnectStrategy = null;

	public static void setReconnectStrategy(IReconnectStrategy reconnectStrategy) {
		mReconnectStrategy = reconnectStrategy;
	}

	private class PollMessageTaskThread extends Thread {
		@Override
		public void run() {
			Utils.l("PollMessageTaskThread start");
			synchronized (messageTasks) {
				while (true) {
					try {
						messageTasks.wait();
						while (!messageTasks.isEmpty()) {
							SendMessageRunnable sendMessageRunnable = messageTasks
									.poll();
							if (sendMessageRunnable != null) {
								sendMessageRunnable.run();
							}
						}
					} catch (InterruptedException ignored) {
					}
				}
			}
		}
	}

	public static PollMessageTaskThread sPollMessageTask = null;

	/**
	 * 假如当前网络状况不对，会直接return 开始连接会在线程池中执行
	 */
	public Connection(IConnectionManager connectionManage) {
		String languange = LanguageSettingUtil.getInstance().getLanguage();
		sCommonBuildString = String
				.format(" to='talk.sixin.com' v='%d' c_appid='%s' c_fromid = '%s' c_version = '%s' xml:lang='%s' ",
						Config.V_TYPE, AbstractRenrenApplication.APP_ID,
						AbstractRenrenApplication.FROM,
						AbstractRenrenApplication.VERSION_NAME,
						languange.toUpperCase());
		sIsRecvOfflineMsg = false;
		if (sPollMessageTask == null) {
			sPollMessageTask = new PollMessageTaskThread();
			sPollMessageTask.start();
		}
		sConnectionManager = connectionManage;
		Status = CONNECTING;
		mReconnectStrategy.endReconnect();
	}

	@Override
	public void run() {
		Utils.l("");
		init();
		ConnectionManager.sGetConnectionState
				.iter(ConnectionManager.sOnBeginReconnectIter);
		beginConnection();
	}

	/**
	 * 子类需要进行的初始化
	 */
	protected abstract void init();

	/**
	 * 建立连接的接口
	 */
	protected abstract void beginConnection();

	/**
	 * 发送信息的接口
	 * 
	 * @param msg
	 *            要发送的信息
	 */
	protected abstract void sendMessageToNet(IMessage msg);

	protected abstract String getNodeStr(int nodeType, Object... args);

	/**
	 * 断开连接
	 * 
	 * @param isLogout
	 *            用来表示是否会发送注销请求。 true:会发送注销请求
	 */
	public synchronized void disconnect(boolean isLogout) {
		Utils.l("\n" + CommonUtil.printStackElements());
		interrupt();
		if (mReconnectStrategy.isInReconnect()) {
			mReconnectStrategy.endReconnect();
		}
		this.disconnectByChild(isLogout);
	}

	public abstract void disconnectByChild(boolean isLogout);

	/**
	 * 发送消息，会向消息队列中添加包含了消息的Runnable对象，然后唤醒进程池中的发送消息的线程，由进程池中的线程发送队列中的所有消息
	 * 
	 * @param msg
	 *            要发送的消息
	 */
	public void sendMessage(IMessage msg) {
		synchronized (messageTasks) {
			try {
				messageTasks.put(new SendMessageRunnable(msg));
				messageTasks.notifyAll();
			} catch (InterruptedException ignored) {
				msg.onSendFailed(null);
			}
		}
	}

	/**
	 * 在重新发送前清空消息队列和进程池
	 * 
	 * @param conn
	 */
	public void cleanBeforeReconnect() {
		synchronized (messageTasks) {
			messageTasks.notify();
		}
	}

	class SendMessageRunnable implements Runnable {
		private IMessage message;

		public SendMessageRunnable(IMessage message) {
			this.message = message;
		}

		@Override
		public void run() {
			Utils.l("isReady:" + isReady.get());
			if (isReady.get()) {
				sendMessageToNet(message);
			} else {
				sConnectionManager.disConnect();
				message.onSendFailed(null);
			}
		}
	}

	/**
	 * 子类当连接断开时会调用该方法，由这里处理连接断开，过程如下 1.断开连接，向服务器发送连接断开的消息 2.回调已注册的ConnHandler
	 * 3.发起重连，重连10次不成功后，休息20s后再次发起重连
	 * 
	 * @param msg
	 */
	protected final synchronized void onConnectionLost(String msg) {
		Utils.log("onConnectionLost:" + msg + " isLogout(" + LoginManager.getInstance().isLogoutWithoutReset() + ")");
		if (Status == INTERRUPTED || isInterrupted() || LoginManager.getInstance().isLogoutWithoutReset()) {
			return;
		}
		ConnectionManager.sGetConnectionState.iter(ConnectionManager.sOnConnectFailedIter);
		Status = DISCONNECTED;
		disconnectByChild(false);
		if(!sIsStopBySingleLogin){
			mReconnectStrategy.beginReconnect();
		}
		refKeeper.iter(lostIter);
	}

	protected final void onRecvMessage(Element e) {
		if (e == null || e.tag == null) {
			return;
		}
		if ("message".equals(e.tag)
				&& "alert".equals(e.getAttr("type"))
				&& e.getFirstChild("action") != null
				&& "terminate"
						.equals(e.getFirstChild("action").getAttr("type"))) {
			try {
				String alert = e.getFirstChild("body").getFirstChild("text").text;
				loginErrorNotification(alert);
			} catch (Exception exception) {
				exception.printStackTrace();
			}
		} else if ("message".equals(e.tag) && "EOOM".equals(e.getAttr("type"))) {
			sIsRecvOfflineMsg = true;
			ConnectionManager.sGetConnectionState
					.iter(ConnectionManager.sOnRecvOfflineIter);
		}
		refKeeper.iter(new RecvMessageIter(Utils.convertSingleElement(e)));
	}

	protected final void loginErrorNotification(String msg) {
		if(sIsStopBySingleLogin == true)
			return;
		sIsStopBySingleLogin = true;
		if (TextUtils.isEmpty(msg)) {
			Utils.l("no msg:" + CommonUtil.printStackElements());
		} else {
			Utils.l("msg:" + msg);
		}
		LoginManager.getInstance().logout();
		disconnect(true);
		Intent intent = new Intent();
		intent.setAction(AbstractRenrenApplication.PACKAGE_NAME
				+ ".SingleLogin");
		intent.putExtra("msg", msg);
		AbstractRenrenApplication.getAppContext().sendBroadcast(intent);
	}

	/**
	 * 用于当前线程阻塞，等待更新完成。 假如已经连接上，会直接返回。
	 */
	protected final void waitForReady() {
		if (isReady.get())
			return;
		synchronized (isReady) {
			try {
				isReady.wait();
			} catch (InterruptedException ignored) {
				return;
			}
		}
	}

	protected final void waitForReady(int waitTime) {
		if (isReady.get() || Status == INTERRUPTED || Type == NONETWORK)
			return;
		synchronized (isReady) {
			try {
				if (waitTime > 0) {
					isReady.wait(waitTime);
				} else {
					isReady.wait();
				}
			} catch (InterruptedException ignored) {
				return;
			}
		}
	}

	/**
	 * 唤醒由于没有连接上而等待的线程
	 */
	protected final void hasReady() {
		Utils.log("================hasReady");
		Status = CONNECTED;
		reConnectionTimes.set(0);
		isReady.set(true);
		sIsRecvOfflineMsg = true;
		sIsStopBySingleLogin = false;
		ConnectionManager.sGetConnectionState
				.iter(ConnectionManager.sOnConnectSuccessIter);
		synchronized (isReady) {
			isReady.notifyAll();
		}
		synchronized (messageTasks) {
			messageTasks.notify();
		}
		
//		new Thread(new Runnable() {
//			
//			@Override
//			public void run() {
//				CommonUtil.waitTime(8000l);
//				loginErrorNotification("");
//			}
//		}).start();
//		
	}

	private class ConnectionIter implements RefKeeper.Iter<ConnHandler> {

		@Override
		public boolean iter(ConnHandler t) {
			t.connectionLost();
			return true;
		}
	}

	private class RecvMessageIter implements RefKeeper.Iter<ConnHandler> {
		private String msgList;

		public RecvMessageIter(String msgList) {
			super();
			this.msgList = msgList;
		}

		@Override
		public boolean iter(ConnHandler t) {
			return t.receive(msgList);
		}
	}
}
