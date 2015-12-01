package com.common.messagecenter;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Timer;
import java.util.concurrent.atomic.AtomicInteger;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.PowerManager;
import android.os.SystemClock;
import android.text.TextUtils;

import com.common.app.AbstractRenrenApplication;
import com.common.manager.LoginManager;
import com.common.messagecenter.base.Connection;
import com.common.messagecenter.base.Element;
import com.common.messagecenter.base.IConnectionManager;
import com.common.messagecenter.base.IMessage;
import com.common.messagecenter.base.Parser;
import com.common.messagecenter.base.Utils;
import com.common.statistics.LocalStatisticsManager;
import com.common.utils.Config;
import com.core.util.CommonUtil;
import com.core.util.Md5;
import com.core.util.SystemService;

/**
 * Socket连接过程
 * 
 * @author yang-chen
 */
public class NewSocketConnection extends Connection {
	private static final int BUILD_STATUS = 0; // 发起Socket连接
	private static final int AUTH_STATUS = 1; // 发起服务器认证
	private static final int RESP_STATUS = 2; // 回应认证
	private static final int COMM_STATUS = 3; // 通信状态
	private static final int CLOSE_STATUS = 4; // 关闭连接
	private static final int[] nextState = new int[] { AUTH_STATUS, // ->BUILD_STATUS
			RESP_STATUS, // ->AUTH_STATUS
			COMM_STATUS, // ->RESP_STATUS
			COMM_STATUS, // still in Comm State
			BUILD_STATUS, // ->Close
			CLOSE_STATUS, };
	private Socket mSocket = null;
	private Writer mOutput = null;
	private BufferedInputStream mBufferedInputStream = null;
	private SocketParser mParser;
	private AtomicInteger mCurrentState;
	private ArrayList<Integer> mStat2Node;
	PendingIntent mSenderIntent;
	private AlarmManager mAlarmManager;
	private KeepConnectionReciver mKeepConnectionReciver = null;
	protected static String BUILD_NODE_STR = null;

	public NewSocketConnection(IConnectionManager cm) {
		super(cm);
		if (mKeepConnectionReciver == null)
			mKeepConnectionReciver = this.new KeepConnectionReciver();
	}

	@Override
	protected void init() {
		// Looper.prepare();
		// mHandler = new Handler();
		// Looper.loop();
		mStat2Node = new ArrayList<Integer>(10);
		mCurrentState = new AtomicInteger(BUILD_STATUS);
		mParser = new SocketParser();
		mStat2Node.add(BUILD_STATUS, BUILD_NODE);
		mStat2Node.add(AUTH_STATUS, AUTH_NODE);
		mStat2Node.add(RESP_STATUS, RESPONSE_NODE);
	}

	@Override
	protected void beginConnection() {
		mCurrentState.set(BUILD_STATUS);
		
		try {
			// mSocket = new Socket(HOST_NAME, 25553);
			Utils.log("=======================new Socket(" + Config.SOCKET_URL
					+ ") begin=====================");
			mSocket = new Socket(Config.SOCKET_URL, Config.SOCKET_DEFAULT_PORT);
			mSocket.setSoTimeout(75000);
			mSocket.setKeepAlive(true);
			Utils.log("=======================new Socket finish=====================");
			mOutput = new OutputStreamWriter(mSocket.getOutputStream());
			mBufferedInputStream = new BufferedInputStream(
					mSocket.getInputStream()) {

				@Override
				public synchronized int read(byte[] buffer, int offset,
						int byteCount) throws IOException {
					int len = super.read(buffer, offset, byteCount);
					String readStr = new String(buffer, offset, len);
					
					if (len <= 0) {
						Utils.l("socket recv:-1");
					} else {
						Utils.l("recv:|" + readStr + "|");
					}
					
					if(TextUtils.isEmpty(readStr.trim())){
						return 0;
					}else{
						return len;
					}
				}
			};

			safeWriteString(getNodeStr(BUILD_NODE,
					AbstractRenrenApplication.APP_ID,
					AbstractRenrenApplication.FROM,
					AbstractRenrenApplication.VERSION_NAME));// 建立连接
			mCurrentState.set(AUTH_STATUS);
			mParser.parse(mBufferedInputStream);
		} catch (Exception e) {
			Utils.log("==============Exception e:" + e.toString());
			e.printStackTrace();
			onConnectionLost("==================beginConnection error================!");
		} finally {
			try {
				mOutput.close();
				mBufferedInputStream.close();
			} catch (Exception ignored) {
			}
		}
		Utils.log("stop thread!!!!!!!!!!");
	}

	@Override
	protected void sendMessageToNet(IMessage msg) {
		try {
			safeWriteString(msg.getContent());
			msg.onSendSuccess(null);
		} catch (IOException e) {
			e.printStackTrace();
			msg.onSendFailed(null);
			onConnectionLost("error in send Message");
			return;
		}
	}

	class SocketParser extends Parser {
		@Override
		protected void onStackSizeChanged(int size) {
			if (size == 0) {
				mCurrentState.set(CLOSE_STATUS);
				return;
			} else if (size > 1) {
				return;
			}
			Element root = getRoot();
			if (root.getFirstChild("success") != null) {
				hasReady();
				beginKeepConnection();
			}

			if (isReady.get()) {
				if (root != null && root.childs != null
						&& root.childs.size() > 0) {
					StringBuffer sb = new StringBuffer(" ");
					for (Element e : root.childs) {
						onRecvMessage(e);
						if (!TextUtils.isEmpty(e.getAttr("msgkey"))) {
							sb.append("<ack msgkey=\"")
									.append(e.getAttr("msgkey")).append("\"/>");
						}
					}
					try {
						safeWriteString(sb.toString());
					} catch (IOException ignored) {
						onConnectionLost("send ack");
					}
				}
				LocalStatisticsManager.getInstance()
						.uploadBackgroundRunStatistics();
				// 发送ACK消息
			} else {
				try {
					if (root.getFirstChild("stream:error") != null
							|| root.getFirstChild("error") != null) {
						Utils.l("error in login Talk server..........");
						throw new Exception("login");
					}
					String msg = null;
					switch (mCurrentState.get()) {
					case AUTH_STATUS:
						msg = getNodeStr(AUTH_NODE,
								AbstractRenrenApplication.USER_ID);
						break;
					case RESP_STATUS:
						if(root.getFirstChild("auth") == null){
							throw new Exception("login");
						}
						String authPrefix = root.getFirstChild("auth").text;
						String secretkey = LoginManager.getInstance().getSecretKey();
						msg = getNodeStr(RESPONSE_NODE,
								Md5.toMD5(authPrefix + secretkey));
						break;
					}
					safeWriteString(msg);
					mCurrentState.set(nextState[mCurrentState.get()]);
				} catch (Exception e) {
					try {
						getRoot().childs.clear();
					} catch (Exception ignored) {}
					e.printStackTrace();
					if(!"login".equals(e.getMessage())){
						onConnectionLost(e.getMessage());
					}else{
						loginErrorNotification("");
					}
					return;
				}
			}
			getRoot().childs.clear();
		}
	}

	private void safeWriteString(String str) throws IOException {
		synchronized (mOutput) {
			Utils.log("===============socket send:" + str);
			CommonUtil.log("cdw", "socket send:" + str);
			mOutput.write(str);
			mOutput.flush();
		}
	}

	public synchronized void beginKeepConnection() {
		final String actionName = AbstractRenrenApplication.PACKAGE_NAME
				+ ".keepconnection";
		Intent intent = new Intent(actionName);
		IntentFilter filter = new IntentFilter(actionName);
		mSenderIntent = PendingIntent.getBroadcast(
				AbstractRenrenApplication.getAppContext(), 0, intent, 0);
		// We want the alarm to go off 5 seconds from now.
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());
		mAlarmManager = SystemService.sAlarmManager;
		mAlarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
				SystemClock.elapsedRealtime(), 25 * 1000, mSenderIntent);
		try {
			AbstractRenrenApplication.getAppContext().registerReceiver(
					mKeepConnectionReciver, filter);
		} catch (Exception e) {
			Utils.log("===============================beginKeepConnection error:"
					+ e.toString());
		}
		// wl =
		// SystemService.sPowerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
		// "keep connection");
		// timer = new Timer();
		// timer.schedule(new TimerTask() {
		//
		// @Override
		// public void run() {
		// Utils.log("timer is schedule isReady.get():"+isReady.get());
		// if (isReady.get()) {
		// try {
		// wl.acquire();
		// safeWriteString(" ");
		// } catch (IOException e) {
		// e.printStackTrace();
		// onConnectionLost("error in KeepConnectionReciver");
		// }finally{
		// wl.release();
		// }
		// }
		// }
		// }, 5000, 15000);
	}

	private PowerManager.WakeLock wl;
	private Timer timer;
	private Handler mHandler;

	private class KeepConnectionHandler implements Runnable {
		public void run() {
			if (isReady.get()) {
				try {
					safeWriteString(" ");
				} catch (IOException e) {
					e.printStackTrace();
					onConnectionLost("error in KeepConnectionReciver");
				}
				mHandler.postDelayed(this, 15 * 1000);
			}
		}
	}

	public synchronized void endKeepConnection() {
		try {
			SystemService.sAlarmManager.cancel(mSenderIntent);
			if (mKeepConnectionReciver != null) {
				AbstractRenrenApplication.getAppContext().unregisterReceiver(mKeepConnectionReciver);
			}
		} catch (Exception e) {
			Utils.log("===============================endKeepConnection error:" + e.toString());
		}

		// if (timer != null) {
		// timer.purge();
		// timer.cancel();
		// }
	}

	public class KeepConnectionReciver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (isReady.get()) {
				try {
					safeWriteString(" ");
				} catch (IOException e) {
					e.printStackTrace();
					onConnectionLost("error in KeepConnectionReciver");
				}
			}else{
				endKeepConnection();
			}
		}
	}

	@Override
	public synchronized void disconnectByChild(boolean isLogout) {
		Utils.log("===============socket disconnectByChild");
		Status = DISCONNECTED;
		isReady.set(false);
		endKeepConnection();
		if (mOutput == null) {
			return;
		}
		// if (isLogout) {
		// new Thread(new Runnable() {
		// @Override
		// public void run() {
		// synchronized (mOutput) {
		// try {
		// Utils.l("close socket start & send CLOSE_MSG");
		// mOutput.write(CLOSE_MSG);
		// mOutput.flush();
		// mSocket.shutdownInput();
		// mOutput.close();
		// mSocket.close();
		// Utils.l("close socket end");
		// } catch (Exception ignored) {
		// }
		// }
		// }
		// }).start();
		// }
		try {
			Utils.l("mOutput.close();");
			mOutput.close();
			mSocket.shutdownInput();
			mSocket.close();
		} catch (IOException ignored) {
		}
	}

	private static final int AUTH_NODE = 0;
	private static final int BUILD_NODE = 1;
	private static final int CLOSE_NODE = 2;
	private static final int RESPONSE_NODE = 3;

	@Override
	protected String getNodeStr(int nodeType, Object... args) {
		switch (nodeType) {
		case AUTH_NODE:
			return String
					.format("<auth xmlns='urn:ietf:params:xml:ns:xmpp-sasl' mechanism='MAS_SECRET_KEY'>%s</auth>",
							args);
		case BUILD_NODE:
			return String.format("<stream:stream online_deploy='false' "
					+ sCommonBuildString + ">", args);
		case CLOSE_NODE:
			return "</stream:stream>";
		case RESPONSE_NODE:
			return String.format("<response>%s</response>", args);
		default:
			return null;
		}
	}
}
