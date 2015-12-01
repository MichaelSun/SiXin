package com.common.messagecenter;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.text.TextUtils;
import android.util.Log;

import com.common.app.AbstractRenrenApplication;
import com.common.binder.RemoteServiceBinder;
import com.common.manager.LoginManager;
import com.common.messagecenter.base.Connection;
import com.common.messagecenter.base.Element;
import com.common.messagecenter.base.IConnectionManager;
import com.common.messagecenter.base.IMessage;
import com.common.messagecenter.base.Utils;
import com.common.statistics.LocalStatisticsManager;
import com.common.utils.Config;
import com.core.util.CommonUtil;
import com.core.util.Md5;

/**
 * Http连接过程
 * 
 * @author yang-chen
 */
public class HttpConnection extends Connection {
	private final AtomicBoolean mIsQuit = new AtomicBoolean(false);
	private int mRid;
	protected String mSid = "";
	protected HttpClient mHttpClient;

	private static final int AUTH_NODE = 1;
	private static final int BUILD_NODE = 2;
	private static final int POLL_NODE = 3;
	private static final int CLOSE_NODE = 4;

	public HttpConnection(IConnectionManager cm) {
		super(cm);
	}

	@Override
	protected void init() {
		mHttpClient = Utils.createHttpClient();
		System.setProperty("http.keepAlive", "true");
	}

	private HttpPost getPost(String str, int type)
			throws UnsupportedEncodingException {
		HttpPost post = null;
		Utils.l("url:::" + Config.HTTP_SEND_URL);
		switch (type) {
		case Element.CHAT:
			post = new HttpPost(Config.HTTP_SEND_URL);
			post.removeHeaders(org.apache.http.protocol.HTTP.EXPECT_DIRECTIVE);
			break;
		case Element.POLL:
			++mRid;
			post = new HttpPost(Config.HTTP_TALK_URL);
			break;
		default:
			post = new HttpPost(Config.HTTP_TALK_URL);
		}
		post.addHeader("Connection", "keep-alive");
		post.addHeader("Accept",
				"text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		post.addHeader("Content-Type",
				"application/x-www-form-urlencoded; charset=UTF-8");
		if (Config.IS_ADD_XONLINEHOST) {
			post.addHeader("X-Online-Host", Config.HOST_NAME);
		}
		post.addHeader("Host", Config.HOST_NAME);
		post.addHeader("User-Agent", "Mozilla/5.0");
		post.addHeader("Accept-Language", "zh_CN,zh");
		post.addHeader("Accept-Charset", "utf-8");
		post.addHeader("Accept-Encoding", "utf-8");
		post.setEntity(new StringEntity(str, "utf-8"));
		return post;
	}
	
	protected String sendNode(String str, int type, IMessage message)
			throws Exception {
		Utils.log("===============http send:" + str);
		Element element = null;
		String response = null;
		int tryCount = 0;
		Utils.l("url:" + Config.HOST_NAME);
		HttpClient client = mHttpClient;
		if (type == Element.CHAT) {
			client = Utils.createHttpClient();
		}
		HttpPost post = getPost(str, type);
		while (tryCount < 4) { // 最多尝试4次，否则认为网络失败，重新连接
			++tryCount;
			HttpResponse httpResponse = client.execute(post);
			Header[] headers = httpResponse.getAllHeaders();
			StringBuffer sb = new StringBuffer();
			for(Header header : headers){
				sb.append("[").append(header.getName()).append(":").append(header.getValue()).append("]\n");
			}
			Utils.l("resp header:\n" + sb.toString());
			response = EntityUtils.toString(httpResponse.getEntity());
			
			Utils.l("resp:" + response);
			// Wap网关返回不正常字符串的时候
			if (!response.contains("<") || response.trim().startsWith("<html>")) {
				CommonUtil.waitTime(RemoteServiceBinder.sIsForeGround ? 5000 : 10000);
				continue;
			}
			loopParser.parse(new ByteArrayInputStream(response.getBytes()));
			element = loopParser.getRoot();
			if ("wml".equals(element.tag) || "html".equals(element.tag)) { // 网关返回wml或者html网页
				CommonUtil.waitTime(RemoteServiceBinder.sIsForeGround ? 5000 : 10000);
				continue;
			}
			return response;
		}
		onConnectionLost("No Data");
		return null;
	}

	private void onException(Exception e) {
		e.printStackTrace();
		onConnectionLost("Exception " + e.toString()); // 网络异常、HTTP异常
	}

	@Override
	protected void beginConnection() {
		mRid = 1000000 + (int) (Math.random() * 1000000);
		Element element = null;
		if (sIsChangedByNetwork.get())
			CommonUtil.waitTime(3000l);

		String initResult = null;
		try {
			initResult = sendNode(getNodeStr(BUILD_NODE, AbstractRenrenApplication.USER_ID, mRid), Element.POLL, null);
			if (TextUtils.isEmpty(initResult)) {
				throw new Exception("login");
			}
			element = Utils.getElementFromString(initResult);// auth
			if(element.getFirstChild("auth") == null){
				throw new Exception("login");
			}
			AbstractRenrenApplication.SESSION_ID = mSid;
			String authStr = element.getChild("auth").get(0).text;
			mSid = element.getAttr("sid");
			String authResStr = sendNode(getNodeStr(AUTH_NODE, mSid, mRid, Md5.toMD5(authStr + LoginManager.getInstance().getSecretKey())), Element.POLL,
					null);
			if (TextUtils.isEmpty(authResStr)) {
				throw new Exception("login");
			}
			element = Utils.getElementFromString(authResStr);
			if (element == null || element.getFirstChild("success") == null) {
				throw new Exception("login");
			}
		} catch (Exception e) {
			onException(e);
			if(!"login".equals(e.getMessage())){
				onConnectionLost("");
			}else{
				loginErrorNotification("");
			}
			return;
		}
		hasReady();
		mIsQuit.set(false);
		startPoll();
	}

	@Override
	protected void sendMessageToNet(IMessage message) {
		try {
			String result = sendNode(
					"<body sid=\"" + mSid + "\">" + message.getContent()
							+ "</body>", Element.CHAT, message);
			if (result != null) {
				loopParser.parse(new ByteArrayInputStream(result.getBytes()));
				Element element = loopParser.getRoot();
				if (element != null) {
					Utils.l("success");
					for(Element e : element.childs){
						if("success".equals(e.tag))
							e.tag = "message";
						onRecvMessage(e);
					}
				}
			}else{
				throw new Exception();
			}
		} catch (Exception e) {
			e.printStackTrace();
			message.onSendFailed("error in Send Message");
			onConnectionLost("error in Send Message");
		}
	}

	private void startPoll() {
		while (!mIsQuit.get() && !this.isInterrupted()) {
			LocalStatisticsManager.getInstance()
					.uploadBackgroundRunStatistics();
			waitForReady();
			String result;
			try {
				result = sendNode(getNodeStr(POLL_NODE, mSid, mRid),
						Element.POLL, null);
				Utils.log("===============http result:" + result);
				if (!TextUtils.isEmpty(result) && !mIsQuit.get()) {// 防止断开连接后才返回结果
					Element elements = Utils.getElementFromString(result);
					if ("terminate".equals(elements.getAttr("type"))) { // 获得Terminate节点
						onConnectionLost("get Terminat Node");
					}else{
						for (Element element : elements.childs) 
							onRecvMessage(element);// 回调接收
					}
				}else if (TextUtils.isEmpty(result)){
					onConnectionLost("start poll");
				}
			} catch (Exception e1) {
				e1.printStackTrace();
				onException(e1);
			}// 发送轮询节点
		}
	}

	@Override
	public void disconnectByChild(boolean isLogout) {
		Utils.log("===============http disconnectByChild");
		mIsQuit.set(true);
		isReady.set(false);
		Status = DISCONNECTED;
		if (isLogout) {
			Thread t1 = new Thread() {
				@Override
				public void run() {
					String str = getNodeStr(CLOSE_NODE, mSid, mRid);
					Utils.log("===============http send:" + str);
					HttpClient client = mHttpClient;
					try {
						client.execute(getPost(str, Element.POLL));
					} catch (Exception e) {
					}
				}
			};
			t1.start();
		}
		return;
	}

	@Override
	protected String getNodeStr(int nodeType, Object... args) {
		switch (nodeType) {
		case AUTH_NODE:
			return String
					.format("<body sid='%s' rid='%s' to='talk.sixin.com' from='talk.sixin.com' xmlns='http://jabber.org/protocol/httpbind'>"
							+ "<response mechanism='MAS_SECRET_KEY'>%s</response>"
							+ "</body>", args);
		case BUILD_NODE:
			return String.format(
					"<body from='%s@talk.sixin.com' hold='1' rid='%s'"
							+ sCommonBuildString + " wait='20' />", args);
		case POLL_NODE:
			return String.format(
					"<body sid='%s' rid='%s'  to='talk.sixin.com'/>", args);
		case CLOSE_NODE:
			return String
					.format("<body sid='%s' rid='%s'><presence type='unavailable'></presence></body>",
							args);
		default:
			return null;
		}
	}
}
