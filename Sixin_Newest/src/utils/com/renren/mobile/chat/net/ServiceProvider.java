//package com.renren.mobile.chat.net;
//
//import java.io.ByteArrayOutputStream;
//import java.net.URLEncoder;
//import java.util.Vector;
//
//import android.content.Context;
//import android.net.wifi.WifiInfo;
//import android.net.wifi.WifiManager;
//import android.os.Build;
//import android.telephony.TelephonyManager;
//import android.text.TextUtils;
//
//import com.common.mcs.HttpProviderWrapper;
//import com.common.mcs.HttpRequestWrapper;
//import com.common.mcs.INetRequest;
//import com.common.mcs.INetResponse;
//import com.core.json.JsonArray;
//import com.core.json.JsonObject;
//import com.core.json.JsonValue;
//import com.core.util.Md5;
//import com.core.util.SystemService;
//import com.renren.mobile.account.LoginControlCenter;
//import com.renren.mobile.chat.R;
//import com.renren.mobile.chat.RenrenChatApplication;
//import com.renren.mobile.chat.common.Data;
//import com.renren.mobile.chat.common.DateFormat;
//import com.renren.mobile.chat.common.Methods;
//import com.renren.mobile.chat.contact.Contact;
//import com.renren.mobile.chat.friends.Htf;
//import com.renren.mobile.chat.provider.RenRenProvider.Account;
//import com.renren.mobile.chat.ui.Login.LoginStatusListener;
//
//public class ServiceProvider {
//
//	/**
//	 * 线上服务器
//	 */
//	public final static String m_test_apiUrl = "http://api.m.renren.com/api";
//	// public final static String m_test_apiUrl = "http://mc4.test.renren.com/api";
//	/**
//	 * 测试服务器
//	 * */
//	// public final static String m_test_apiUrl =http://mc1.test.renren.com/api
//	// "http://mc3.test.renren.com/api";
////	private final static String m_test_apiUrl = "http://mc4.test.renren.com/api";
//
//	/**
//	 * 开发中的测试版（边开发边更新，更新频度较高） 10.3.19.196
//	 */
//
//	/**
//	 * 定版测试（一个阶段后，需QA整体验证功能，更新频度相对较低）10.3.19.207
//	 */
//	/**
//	 * 线上重大bug紧急更新测试服务 10.3.18.210
//	 */
//	/**
//	 * 图片压缩服务器
//	 */
//	public static String imgUrl = "http://ic.m.renren.com";
//	private final static String test_server = m_test_apiUrl;
//
////	 private static String chatUrlTalk = "http://talk.m.renren.com/talk";
////	
////	 private static String chatUrlSend = "http://talk.m.renren.com/send";
//
//	private static String chatUrlTalk = "http://talk.apis.tk/talk";
//	//
//	private static String chatUrlSend = "http://talk.apis.tk/send";
//
//	public static String session_key = "";
//
//	public static final int MINIFEED = 1;
//
//	public static final int NEWSFEED = 0;
//
//	/**
//	 * 是否需要完善资料的标志位
//	 */
//	public static int mPerfectCode;
//
//	public static int mLoginCount;
//
//	public static JsonObject info;
//
//	public final static String m_apiKey = RenrenChatApplication.getmContext().getResources().getString(R.string.apikey);
//
//	// login返回成功后被付值为"运行時secret key".
//	private final static String m_concrete_secretKey = RenrenChatApplication.getmContext().getResources().getString(R.string.secretkey);
//
//	/*运行时secrekey*/
//	public static String m_secretKey;
//
//	/*运行时session*/
//	public static String m_sessionKey;
//
//	/**
//	 * 构造一个网絡请求的基本数据bundle, 填入api_key, call_id, session_key.
//	 * 
//	 * @param batchRun
//	 *            是否使用批调用. 使用批调用的请求不包含session_key, session_key统一由batch.run方法提供.
//	 */
//	private static JsonObject m_buildRequestBundle(boolean batchRun) {
//
//		JsonObject bundle = new JsonObject();
//		// 使用 3G 服务器的 api_key
//		bundle.put("api_key", m_apiKey);
//		bundle.put("call_id", System.currentTimeMillis());
//		// 使用批调用的请求不包含session_key, session_key统一由batch.run方法提供.
//		if (!batchRun) {
//			if (m_secretKey == null) {
//				Data.loadUserInfo(RenrenChatApplication.mContext);
//			}
//			if(m_secretKey==null){
//				//?update by dingwei.chen?
//				m_secretKey = RenrenChatApplication.secretkey;
//			}
//			assert m_sessionKey != null;
//			bundle.put("session_key", m_sessionKey);
//		}
//		bundle.put("client_info", getClientInfo());
//		
//		// add by xiangchao.fan TODO
//		/* *********************************************************/
//		// misc
//		if(!"".equals(getMISCInfo())){
//			bundle.put("misc", getMISCInfo());
//		}
//		
//		//is_auto
//		if(RenrenChatApplication.sIsAuto == 1){
//			bundle.put("is_auto", RenrenChatApplication.sIsAuto);
//		}
//		/* *********************************************************/
//		
//		return bundle;
//	}
//
//	/**
//	 * 构造一个网絡请求. 老代码.
//	 */
//	private static INetRequest m_buildRequest(String url, JsonObject sm, INetResponse response) {
//
//		INetRequest request = new HttpRequestWrapper();
//		request.setUrl(url);
//		request.setData(sm);
//		request.setResponse(response);
//		request.setSecretKey(m_secretKey);
//		return request;
//	}
//
//	/**
//	 * 使用参数的sessionkey
//	 * 
//	 * @param sessionKey
//	 * @return TODO 与m_buildRequestBundle()有什么区别？
//	 */
//	private static JsonObject m_buildRequestBundleWithSessionKey(String sessionKey) {
//		JsonObject bundle = new JsonObject();
//		bundle.put("api_key", m_apiKey);
//		bundle.put("call_id", System.currentTimeMillis());
//		bundle.put("session_key", sessionKey);
//		bundle.put("client_info", getClientInfo());
//		return bundle;
//	}
//
//	/**
//	 * 使用参数的secretKey
//	 * 
//	 * @param url
//	 * @param sm
//	 * @param response
//	 * @param secretKey
//	 * @return TODO 与m_buildRequest()有什么区别？
//	 */
//	private static INetRequest m_buildRequestWithSrtKey(String url, JsonObject sm, INetResponse response, String secretKey) {
//
//		INetRequest request = new HttpRequestWrapper();
//		request.setUrl(url);
//		request.setData(sm);
//		request.setResponse(response);
//		request.setSecretKey(secretKey);
//		return request;
//	}
//
//	public static void m_batchRun(final INetRequest[] requests) {
//
//		if (requests == null)
//			throw new NullPointerException();
//		if (requests.length == 0)
//			return;
//
//		JsonArray ja = new JsonArray();
//		for (INetRequest request : requests) {
//			if (request != null) {
//				String param = request.getParamsString();
//				ja.add(param);
//			}
//			// logger.info(request.getMethod() + " " + param);
//		}
//		String param = ja.toJsonString();
//		// logger.info("batch.run" + " " + param);
//
//		JsonObject bundle = m_buildRequestBundle(false);
////		bundle.put("method", "batch.run");
//		bundle.put("method_feed", param);
//		bundle.put("v", "1.0");
//		bundle.put("format", "json");
//
//		bundle.put(INetRequest.gzip_key, INetRequest.gzip_value);
//
//		INetResponse response4BatchRun = new INetResponse() {
//
//			@Override
//			public void response(INetRequest req, JsonValue jv) {
//
//				// A failing response.
//				if (jv instanceof JsonObject) {
//
//					JsonObject ret = (JsonObject) jv;
//
//					assert !CheckResponseError.noError(req, ret, false);
//
//					for (INetRequest request : requests) {
//						INetResponse response = request.getResponse();
//						if (response == null)
//							continue;
//						response.response(request, ret);
//					}
//
//					// A successful response.
//				} else if (jv instanceof JsonArray) {
//					JsonArray ja = (JsonArray) jv;
//					for (int i = 0; i < ja.size(); i++) {
//						INetRequest request = requests[i];
//						String method = request.getMethod();
//						JsonValue val = ((JsonObject) ja.get(i)).getJsonValue(method);
//						request.getResponse().response(request, val);
//					}
//				}
//			}
//		};
//
//		INetRequest request = m_buildRequest(m_test_apiUrl+"/batch/run", bundle, response4BatchRun);
//		m_sendRequest(request);
//	}
//
//	/**
//	 * 发送一个网絡请求.
//	 */
//	public static void m_sendRequest(INetRequest request) {
//		HttpProviderWrapper.getInstance().addRequest(request);
//	}
//
////	/*
////	 * Caution! 请确保在每个login入口均正确调用.
////	 * 
////	 * 
////	 * he.cao : password使用md5值 统一response, 其余工作在loginStatusListener中； response中:
////	 * 1)更新sessionkey和secretkey。 2)
////	 */
////	public static void m_login(String account,
////			String passwordMd5,
////			final Context context,
////			final LoginStatusListener loginStatusListener,
////			long session) {
////
////		TelephonyManager tm = SystemService.sTelephonyManager;
////		JsonObject bundle = new JsonObject();
////
////		// 使用 3G 服务器的 api_key
////		bundle.put("api_key", m_apiKey);
////		bundle.put("call_id", System.currentTimeMillis());
////		bundle.put("client_info", getClientInfo());
////		bundle.put("v", "1.0");
////		bundle.put("format", "JSON");
//////		bundle.put("method", "client.login");
////		bundle.put("user", account);
////		bundle.put("password", passwordMd5);
////		bundle.put("uniq_id", tm.getDeviceId());
////		bundle.remove("session_key");
////		bundle.put(INetRequest.gzip_key, INetRequest.gzip_value);
////		// 登录时必须传入原始的SecretKey。
////		bundle.put("sig", getSigForLogin(bundle));
////		INetResponse m_loginResponse = new INetResponse() {
////			@Override
////			public void response(INetRequest req, JsonValue obj) {
////				if (obj instanceof JsonObject) {
////					JsonObject ret = (JsonObject) obj;
////
////					/**
////					 * 用于判断登录异常错误
////					 */
////					if (!CheckResponseError.noError(req, ret)) {
////						long error_code = ret.getNum("error_code");
////						String error_msg = ret.getString("error_msg");
////						if (loginStatusListener != null) {
////							loginStatusListener.onLoginFailed(error_code, error_msg);
////						}
////						return;
////					}
////					// ----------------------------以下为登陆成功以后的用户信息存储策略------------------------------
////					// 保存用户信息到全局变量
////////					LoginInfo info = new LoginInfo(RenrenChatApplication.account,
////////													RenrenChatApplication.password,
////////													ret);
//////					LoginInfo info==null;
//////					if(info==null){
//////						return;
//////					}
//////					RenrenChatApplication.updateFromPreInfo(info);
//////					// 用户是否需要完善资料，0不需要，1需要。
//////					mPerfectCode = (int) ret.getNum("fill_stage");
//////					mLoginCount = (int) ret.getNum("login_count");
//////					
//////					LoginControlCenter.getInstance().saveUserData(info);
////					
////
////					// TODO 个人信息保存到文件
////					// ------------------------------以上为登陆后信息的更新------------------------------
////					loginStatusListener.onLoginSuccess(mPerfectCode,req.getCurrentSession());
////				}
////
////			}
////		};
////
////		INetRequest request = new HttpRequestWrapper();
////		request.setUrl(m_test_apiUrl+"/client/login");
////		request.setData(bundle);
////		request.setResponse(m_loginResponse);
////		request.setSecretKey(m_concrete_secretKey);
////		request.setCurrentSession(session);
////		HttpProviderWrapper.getInstance().addRequest(request);
////	}
//
//	/**
//	 * 获取登录信息，用于票失效
//	 * */
//	public static void getLoginInfo(final Context context, final LoginStatusListener loginStatusListener) {
//		JsonObject bundle = m_buildRequestBundle(false);
//		bundle.put("v", "1.0");
//		bundle.put("format", "JSON");
//		bundle.put(INetRequest.gzip_key, INetRequest.gzip_value);
//		INetResponse m_loginResponse = new INetResponse() {
//			@Override
//			public void response(INetRequest req, JsonValue obj) {
//
//				// obj.toString()) ;
//				if (loginStatusListener != null) {
//					loginStatusListener.onLoginResponse();
//				}
//				assert (obj != null);
//				if (obj instanceof JsonObject) {
//					JsonObject ret = (JsonObject) obj;
//					/**
//					 * 用于判断登录异常错误
//					 */
//					if (!CheckResponseError.noError(req, ret)) {
//						return;
//					}
//					
//					// ----------------------------以下为登陆成功以后的用户信息存储策略------------------------------
//					// 保存用户信息到全局变量
//					String m_sessionKey = ret.getString("session_key");
//					assert (m_sessionKey != null);
//					ServiceProvider.m_sessionKey = m_sessionKey;
//					String m_secretKey = ret.getString("secret_key");
//					assert (m_secretKey != null);
//					ServiceProvider.m_secretKey = m_secretKey;
//					RenrenChatApplication.head_url = ret.getString("head_url");
//					RenrenChatApplication.user_id = ret.getNum("uid");
//					RenrenChatApplication.user_name = ret.getString("user_name");
//					RenrenChatApplication.login_count = ret.getNum("login_count");
//					RenrenChatApplication.ticket = ret.getString("ticket");
//					info = new JsonObject();
//					info.put(Account.UID, RenrenChatApplication.user_id);
//					info.put(Account.HEAD_URL, RenrenChatApplication.head_url);
//					info.put(Account.ACCOUNT, RenrenChatApplication.account);
//					info.put(Account.PWD, RenrenChatApplication.password);
//					info.put(Account.SESSION_KEY, ServiceProvider.m_sessionKey);
//					info.put(Account.TICKET, RenrenChatApplication.ticket);
//					info.put(Account.SECRET_KEY, ServiceProvider.m_secretKey);
//					if (RenrenChatApplication.user_name != null) {
//						info.put(Account.NAME, RenrenChatApplication.user_name);
//					}
//					//Data.save(info, context);
//
//					LoginControlCenter.getInstance().saveUserData(info.toJsonString());
//
//					loginStatusListener.onLoginSuccess(mPerfectCode,req.getCurrentSession());
//				}
//			}
//		};
//		// bundle.toString());
//		INetRequest request = m_buildRequest(m_test_apiUrl + "/client/getLoginInfo", bundle, m_loginResponse);
//		m_sendRequest(request);
//	}
//
//	/**
//	 * 获取登录请求的sig
//	 * */
//	private static String getSigForLogin(JsonObject data) {
//		String[] keys = data.getKeys();
//		StringBuilder sb = new StringBuilder();
//		Vector<String> vecSig = new Vector<String>();
//		for (String key : keys) {
//			String val = data.getJsonValue(key).toString();
//			sb.append(key).append('=').append(URLEncoder.encode(val)).append('&');
//
//			// Modified by lin.zhu@opi-corp.com
//			// 3G Server端计算Sig参数, 对value截长前50个字符.
//			// Caution! 计算sig参数的入口(调用getSig方法)不止这一个,
//			// 确保每个计算sig的入口均截长.
//			if (val.length() > 50) {
//				val = val.substring(0, 50);
//			}
//			// End modify.
//
//			vecSig.add(key + "=" + val);
//		}
//		String[] ss = new String[vecSig.size()];
//		vecSig.copyInto(ss);
//		return ServiceProvider.getSig(ss, m_concrete_secretKey);
//	}
//
//	/**
//	 * 每个接口都必须要携带的统计信息
//	 * @description 
//	 * 		misc字段定义
//	 *         HTF：标志来源页面（即从哪个页面进入当前页面的）
//	 *         离线or在线：0为在线，1为离线
//	 *         联网状态：1对应wifi；2对应非wifi
//	 * 		格式要求：最后一个字段后不加逗号。
//	 * @return misc字符串
//	 */
//	public static String getMISCInfo(){
//
//		StringBuilder misc = new StringBuilder(",,");
//
//		WifiManager wifi = SystemService.sWifiManager;
//		if (wifi != null) {
//			WifiInfo info = wifi.getConnectionInfo();
//			if (info != null) {
//				misc.append("1");
//			}else{
//				misc.append("2");
//			}
//		}
//		else{
//			misc.append("2");
//		}
//		return misc.toString();
//	}
//	
//	/**
//	 * 每个接口都必须要携带的统计信息
//	 * @description 
//	 * 		misc字段定义
//	 *         HTF：标志来源页面（即从哪个页面进入当前页面的）
//	 *         离线or在线：0为在线，1为离线
//	 *         联网状态：1对应wifi；2对应非wifi
//	 * 		格式要求：最后一个字段后不加逗号。
//	 * @param HTF
//	 * @param onlineState 在线状态
//	 * @return misc字符串
//	 */
//	public static String getMISCInfo(String HTF, String onlineState){
//
//		StringBuilder misc = new StringBuilder("");
//		if(null != HTF){
//			misc.append(HTF);
//		}
//		misc.append(",");
//		if(null != onlineState){
//			misc.append(onlineState);
//		}
//		misc.append(",");
//
//		WifiManager wifi = SystemService.sWifiManager;
//		if (wifi != null) {
//			WifiInfo info = wifi.getConnectionInfo();
//			if (info != null) {
//				misc.append("1");
//			}else{
//				misc.append("2");
//			}
//		}
//		else{
//			misc.append("2");
//		}
//		return misc.toString();
//	}
//	
//	/**
//	 * 每个接口都必须要携带的统计信息
//	 * 
//	 * @return
//	 */
//	public static String getClientInfo() {
//		String imei = "";
//		String mac = "";
//		
//		int mcc = -1; 
//		int mnc = -1;
//		
//		if (RenrenChatApplication.getmContext() != null) {
//			TelephonyManager tm = SystemService.sTelephonyManager;
//			
//			mcc = RenrenChatApplication.getmContext().getResources().getConfiguration().mcc;
//			mnc = RenrenChatApplication.getmContext().getResources().getConfiguration().mnc; 
//			if (tm != null && !TextUtils.isEmpty(imei)) {
//
//			} else {
//				WifiManager wifi = SystemService.sWifiManager;
//				if (wifi != null) {
//					WifiInfo info = wifi.getConnectionInfo();
//					if (info != null) {
//						mac = info.getMacAddress();
//						imei = mac;
//					}
//				}
//			}
//		}
//
//		JsonObject clientInfo = new JsonObject();
//		clientInfo.put("screen", RenrenChatApplication.screen);
//		clientInfo.put("os", Build.VERSION.SDK + "_" + Build.VERSION.RELEASE);
//		clientInfo.put("model", Build.MODEL);
//		clientInfo.put("from", RenrenChatApplication.from);
//		clientInfo.put("uniqid", imei);
//		clientInfo.put("mac", mac);
//		clientInfo.put("version", RenrenChatApplication.versionName);
//		
//		// TODO
//		if(mcc != 0){
//			clientInfo.put("other", String.format("%03d", mcc) + String .format("%02d", mnc) + ",");
//		}
//		
//		return clientInfo.toJsonString();
//	}
//
//	/**
//	 * 计算sig
//	 * */
//	public static String getSig(String[] ss, String secretKey) {
//		for (int i = 0; i < ss.length; i++) {
//			for (int j = ss.length - 1; j > i; j--) {
//				if (ss[j].compareTo(ss[j - 1]) < 0) {
//					String temp = ss[j];
//					ss[j] = ss[j - 1];
//					ss[j - 1] = temp;
//				}
//			}
//		}
//		StringBuffer sb = new StringBuffer();
//		for (int i = 0; i < ss.length; i++) {
//			sb.append(ss[i]);
//		}
//		sb.append(secretKey);
//		return Md5.toMD5(sb.toString());
//	}
//
//	/**
//	 * 下载头像，不走压缩服务器
//	 * 
//	 * @param url
//	 *            图片地址
//	 * @param response
//	 */
//	public static void downloadHeadPhoto(String url, INetResponse response) {
//
//		downloadImg(url, response, INetRequest.PRIORITY_LOW_PRIORITY);
//	}
//
//	/**
//	 * 下载头像，可以定制图片大小，走压缩服务器
//	 * 
//	 * @param uid
//	 *            所有者id
//	 */
//	public static void downloadHeadPhoto(long uid, INetResponse response, int priority) {
//
//		String url = imgUrl + "/gn?op=resize&w=150&h=150&p=" + uid;
//		downloadImg(url, response, priority);
//	}
//
//	/**
//	 * 下载图片
//	 * */
//	public static void downloadImg(String url, INetResponse response, int priority) {
//		INetRequest request = new HttpRequestWrapper();
//		request.setUrl(url);
//		request.setResponse(response);
//		request.setType(INetRequest.TYPE_HTTP_GET_IMG);
//		request.setPriority(priority);
//		request.setSecretKey(m_secretKey);
//		HttpProviderWrapper.getInstance().addRequest(request);
//	}
//
//
//
//
//	/**
//	 * 获取在线的好友 接口 2012-2-16
//	 * 
//	 * @param uid
//	 * @param response
//	 * @param page
//	 *            页数 1
//	 * @param page_size
//	 *            页面大小 2000
//	 * @return
//	 */
//	public static INetRequest getOnlineFriendList(final INetResponse response, int page, int page_size,boolean batchRun) {
//		final JsonObject bundle = m_buildRequestBundle(false);
//		if(batchRun){
//			bundle.put("method", "friends.getOnlineFriends");	
//		}
//		bundle.put("v", "1.0");
//		bundle.put("page", page);
//		bundle.put("hasGender", 1);// 包括性别
//		bundle.put("hasBirthday", 1);// 包括生日
//		bundle.put("page_size", page_size);
//		bundle.put("hasLargeHeadUrl", 1);// 包括大头像
//		bundle.put("hasMainHeadUrl", 1);// 包括大头像
//		bundle.put("useShortUrl", 1);// 使用短连接
//		bundle.put("hasFocused", 1);// 请求特别关注
//		INetRequest request;
//		if (!batchRun) {
//			bundle.put(INetRequest.gzip_key, INetRequest.gzip_value);
//			request = m_buildRequest(m_test_apiUrl+"/friends/getOnlineFriends", bundle, response);
//			m_sendRequest(request);
//			return null;
//		}else{
//			request = m_buildRequest(m_test_apiUrl, bundle, response);
//			return request;
//		}
//	}
//	
////	public static INetRequest getGroupContact(final INetResponse response,boolean batchRun){
////		final JsonObject bundle = m_buildRequestBundle(false);
////		if(batchRun){
////			bundle.put("method", "talk.getGroupList");	
////		}
////		bundle.put("v", "1.0");
////		bundle.put("api_key",m_apiKey);
////		bundle.put("call_id",  String.valueOf(System.currentTimeMillis()));
////		bundle.put("sig","");
////		bundle.put("v ", "1.0");
////	    bundle.put("session_key", m_sessionKey);
////	    bundle.put("page",1);
////	    bundle.put("page_size",100);
////	    bundle.put("head_url_switch", 2);
////	    bundle.put("member_detail", 1+2+4+8+16+64+128+256);
////	    INetRequest request;
////		if (!batchRun) {
////			bundle.put(INetRequest.gzip_key, INetRequest.gzip_value);
////			request= m_buildRequest("http://mc1.test.renren.com/api/talk/getGroupList", bundle, response);
////			m_sendRequest(request);
////			return null;
////		}else{
////			request= m_buildRequest("http://mc1.test.renren.com/api", bundle, response);
////			return request;
////		}
////		
////	}
////
////	/**
////	 * 获取在线好友状态列表（仅包含id与在线状态），用于掉线的时候重新获取在线联系人
////	 * 
////	 * @param response
////	 * @param page
////	 * @param page_size
////	 * @return
////	 */
////	@Deprecated
////	public static INetRequest getOnlineFriendListSimple(final INetResponse response, int page, int page_size) {
////		final JsonObject bundle = m_buildRequestBundle(false);
//////		bundle.put("method", "friends.getOnlineFriends");
////		bundle.put("v", "1.0");
////		bundle.put("page", page);
////		bundle.put("page_size", page_size);
////		bundle.put("isOnline", 1);
////		bundle.put("hasUserName", 0);// 不包括姓名
////		bundle.put("hasHeadUrl", 0);// 不包括头像url
////		bundle.put(INetRequest.gzip_key, INetRequest.gzip_value);
////		INetRequest request = m_buildRequest(m_test_apiUrl+ "/friends/getOnlineFriends", bundle, response);
////		m_sendRequest(request);
////		return null;
////	}
//	
//	/**
//	 * 获取用户在线状态
//	 * 返回  在线用户的id
//	 * @param response
//	 * @return
//	 */
//	public static INetRequest getOnlineFriendListSimple(final INetResponse response) {
//		final JsonObject bundle = m_buildRequestBundle(false);
//		bundle.put("v", "1.0");
////		bundle.put(INetRequest.gzip_key, INetRequest.gzip_value);
//		INetRequest request = m_buildRequest(m_test_apiUrl+ "/friends/getOnlineFriendsStatus", bundle, response);
//		m_sendRequest(request);
//		return null;
//	}
//	
//	/**
//	 * 获取某一个人的在线状态
//	 * **/
//	public static INetRequest getOnlineStatusByUserid(final INetResponse response, long id) {
//		final JsonObject bundle = m_buildRequestBundle(false);
////		bundle.put("method", "friends.getOnlineFriends");
//		bundle.put("v", "1.0");
//		bundle.put("id_list", String.valueOf(id));
//		INetRequest request = m_buildRequest(m_test_apiUrl+ "/friends/isOnline", bundle, response);
//		m_sendRequest(request);
//		return null;
//	}
//	
//	
////	/**
////	 * 获取feed列表 接口 2012-3-31
////	 * 
////	 * @return
////	 */
////	public static INetRequest getFeedList(final INetResponse response, int page, int page_size) {
////		final JsonObject bundle = m_buildRequestBundle(false);
//////		bundle.put("method", "friends.getOnlineFriends");
////		bundle.put("v", "1.0");
////		bundle.put("page", page);
////		bundle.put("page_size", page_size);
////		bundle.put("focus", 1);
////		bundle.put("type", "502,701,709");
////		bundle.put(INetRequest.gzip_key, INetRequest.gzip_value);
////		INetRequest request = m_buildRequest(m_test_apiUrl+"/feed/get", bundle, response);
////		m_sendRequest(request);
////		return null;
////	}
////	
//	
//	/**
//	 * 获取feed列表 接口 2012-4-09
//	 * 
//	 * @return
//	 */
//	public static INetRequest getFeedByIds(final INetResponse response, long[] id) {
//		final JsonObject bundle = m_buildRequestBundle(false);
////		bundle.put("method", "friends.getOnlineFriends");
//		bundle.put("v", "1.0");
//		String temp = "" + id[0];
//		for(int num = 1; num < id.length ;num ++){
//			temp += "," + id[num];
//		}
//		bundle.put("fids",temp);
//		bundle.put(INetRequest.gzip_key, INetRequest.gzip_value);
//		INetRequest request = m_buildRequest(m_test_apiUrl+"/feed/getByIds", bundle, response);
//		m_sendRequest(request);
//		return null;
//	}
//	
//	/**
//	 * 获取特别关注好友数目 接口 2012-5-06
//	 * 
//	 * @return
//	 */
//	public static INetRequest getFocusFriendsNUM(final INetResponse response) {
//		final JsonObject bundle = m_buildRequestBundle(false);
//		bundle.put("v", "1.0");
//		bundle.put("exclude_friend", 1);
//		bundle.put(INetRequest.gzip_key, INetRequest.gzip_value);
//		INetRequest request = m_buildRequest(m_test_apiUrl+"/friends/getFocusFriends", bundle, response);
//		m_sendRequest(request);
//		return null;
//	}
//	
//	
////	
////	/**
////	 * 根据photoID获取照片
////	 * @param response
////	 * @param uid
////	 * @param pid
////	 * @return
////	 */
////	public static INetRequest getPhotoByPid(final INetResponse response,long uid, long pid) {
////		final JsonObject bundle = m_buildRequestBundle(false);
//////		bundle.put("method", "friends.getOnlineFriends");
////		bundle.put("v", "1.0");
////		bundle.put("uid", uid);
////		bundle.put("pid",pid);
////		bundle.put(INetRequest.gzip_key, INetRequest.gzip_value);
////		INetRequest request = m_buildRequest(m_test_apiUrl+"/feed/getByIds", bundle, response);
////		m_sendRequest(request);
////		return null;
////	}
//	
//	
//
//	/**
//	 * 请求好友名片数据 Add:yisong.li@renren-inc.com
//	 * 
//	 * @param uid
//	 * @param response
//	 * @return
//	 */
//	public static INetRequest getCardInfomation(long uid, INetResponse response) {
//		final JsonObject requsetObject = m_buildRequestBundle(false);
////		requsetObject.put("method", "contact.detail");
//		requsetObject.put("v", "1.0");
//		requsetObject.put("user_id", uid);
//
//		INetRequest request = m_buildRequest(m_test_apiUrl+"/contact/detail", requsetObject, response);
//
//		m_sendRequest(request);
//
//		return null;
//	}
//
//	/**
//	 * 申请加为好友
//	 */
//	public static INetRequest m_friendsRequest(long uid, String content, INetResponse response, boolean batchRun) {
//
//		JsonObject bundle = m_buildRequestBundle(batchRun);
//
////		bundle.put("method", "friends.request");
//		bundle.put("uid", uid);
//		if (content != null) {
//			bundle.put("content", content);
//		}
//		bundle.put("v", "1.0");
//		bundle.put("format", "json");
//		// if (!batchRun) {
//		// bundle.put(INetRequest.gzip_key, INetRequest.gzip_value);
//		// }
//		INetRequest request = m_buildRequest(m_test_apiUrl+"/friends/request", bundle, response);
//		if (batchRun) {
//			return request;
//		} else {
//			m_sendRequest(request);
//			return null;
//		}
//	}
//	
//	/**
//	 * 
//	 * @param uid
//	 * @param content
//	 * @param response
//	 * @param batchRun
//	 * @param HTF
//	 * @return
//	 */
//	public static INetRequest m_friendsRequest(long uid, String content, INetResponse response, boolean batchRun, String HTF) {
//
//		JsonObject bundle = m_buildRequestBundle(batchRun);
//		
//		
//		bundle.put("misc", getMISCInfo(HTF, null));
//		
//
////		bundle.put("method", "friends.request");
//		bundle.put("uid", uid);
//		if (content != null) {
//			bundle.put("content", content);
//		}
//		bundle.put("v", "1.0");
//		bundle.put("format", "json");
//		// if (!batchRun) {
//		// bundle.put(INetRequest.gzip_key, INetRequest.gzip_value);
//		// }
//		INetRequest request = m_buildRequest(m_test_apiUrl+"/friends/request", bundle, response);
//		if (batchRun) {
//			return request;
//		} else {
//			m_sendRequest(request);
//			return null;
//		}
//	}
//
//	/**
//	 * 
//	 * @param type
//	 *            1：激活统计 2：联网统计 uniqid: 设备的唯一标识，一般手机取IMEI
//	 * @return
//	 */
//	public static INetRequest activeClient(String imei, int type, INetResponse response, boolean batchRun) {
//		JsonObject bundle = m_buildRequestBundle(batchRun);
////		bundle.put("method", "phoneclient.activeClient");
//		bundle.put("v", "1.0");
//		bundle.put("format", "json");
//		bundle.put("uniq_id", imei);
//		JsonArray dataArray = new JsonArray();
//		JsonObject dataJson = new JsonObject();
//		dataArray.add(dataJson);
//		dataJson.put("type", type);
//		dataJson.put("num", 1);
//		bundle.put("data", dataArray.toJsonString());
//		bundle.remove("session_key");
//		INetRequest request = m_buildRequest(m_test_apiUrl+"/phoneclient/activeClient", bundle, response);
//		request.setSecretKey(m_concrete_secretKey);
//		if (batchRun) {
//			return request;
//		} else {
//			m_sendRequest(request);
//			return null;
//		}
//	}
//
//	/**
//	 * 检查更新
//	 * 
//	 * @param type
//	 *            0:手动检查更新，1:自动检查更新
//	 * @param response
//	 * @param batchRun
//	 * @author kuangxiaoyue
//	 * @return
//	 */
//	public static INetRequest getUpdateInfo(int type, final INetResponse response, String lastTag, final boolean batchRun) {
//		JsonObject bundle = m_buildRequestBundle(false);
////		bundle.put("method", "phoneclient.getUpdateInfo");
//		bundle.put("v", "1.0");
//		bundle.put("version", RenrenChatApplication.versionName);
//		bundle.put("up", type);
//		bundle.put("name", 2);// 2代表人人chat
//		bundle.put("property", 5);
//		bundle.put("subproperty", 0);
//		bundle.put("channelId", RenrenChatApplication.from);
//		bundle.put("ua", "");
//		bundle.put("os", Build.VERSION.SDK + "_" + Build.VERSION.RELEASE);
//		bundle.put("pubdate", RenrenChatApplication.pubdate);
//		if (lastTag != null && !lastTag.equals("")) {
//			bundle.put("lastTag", lastTag);
//		}
//		INetRequest request = m_buildRequest(m_test_apiUrl+"/phoneclient/getUpdateInfo", bundle, response);
//		m_sendRequest(request);
////		if (batchRun) {
////			return request;
////		} else {
////			
//			return null;
////		}
//	}
//	
//	
//	/**
//	 * 
//	 * 注销接口
//	 */
//	public static void logout(INetResponse response) {
//		JsonObject bundle = m_buildRequestBundle(false);
//		bundle.put("v", "1.0");
//		bundle.put("format", "json");
//		INetRequest request = m_buildRequest(m_test_apiUrl+"/client/logout", bundle, response);
//		m_sendRequest(request);
//	}
//	
//
//	/**
//	 * 检查业务API调用返回是否出错.
//	 * 
//	 * @param jo
//	 *            业务API调用返回数据包.
//	 * @return 业务API调用返回是否出错. 正确返回true, 否则false.
//	 */
//	public static boolean checkError(JsonObject jo) {
//
//		int error_code = (int) jo.getNum("error_code");
//		return error_code == 0;
//	}
//
//	/**
//	 * 通讯录同步服务接口。 请参考trac上contact.synchronize接口。
//	 * 
//	 * @param contacts
//	 *            上传通讯录数据
//	 * @param response
//	 *            回调handler
//	 */
//	public static void contactSynchronize(Contact[] contacts, INetResponse response) {
//		JsonObject bundle = m_buildRequestBundle(false);
//
////		bundle.put("method", "contact.synchronize");
//
//		String data = Contact.generateContactsJsonString(contacts, m_sessionKey);
//
//		bundle.put("data", data);
//
//		bundle.put("v", "1.0");
//		bundle.put("format", "json");
//
//		INetRequest request = m_buildRequest(test_server+"/contact/synchronize", bundle, response);
//		m_sendRequest(request);
//	}
//
//	/**
//	 * 通讯录同步服务接口。使用参数的sessionkey和secretKey 请参考trac上contact.synchronize接口。
//	 * 
//	 * @param contacts
//	 *            上传通讯录数据
//	 * @param response
//	 *            回调handler
//	 */
//	public static void contactSynchronize(Contact[] contacts, INetResponse response, String sessionKey, String secretKey) {
//		JsonObject bundle = m_buildRequestBundleWithSessionKey(sessionKey);
////		bundle.put("method", "contact.talkContact");
//		String data = Contact.generateContactsJsonString(contacts, sessionKey);
//		bundle.put("data", data);
//		bundle.put("v", "1.0");
//		bundle.put("format", "json");
//		INetRequest request = m_buildRequestWithSrtKey(test_server+"/contact/talkContact", bundle, response, secretKey);
//		m_sendRequest(request);
//	}
//
//	/**
//	 * 联系人整合,这个基本不用到
//	 * 
//	 * @param contacts
//	 * @param response
//	 */
//	public static void combineContacts(Contact[] contacts, INetResponse response, String sessionKey, String secretKey) {
//		JsonObject bundle = m_buildRequestBundleWithSessionKey(sessionKey);
////		bundle.put("method", "contact.talkContact");
//		String data = Contact.generateContactsJsonString(contacts, sessionKey);
//		bundle.put("data", data);
//		bundle.put("v", "1.0");
//		bundle.put("format", "json");
//		INetRequest request = m_buildRequestWithSrtKey(test_server+"/contact/talkContact", bundle, response, secretKey);
//		m_sendRequest(request);
//	}
//
//	/**
//	 * 联系人整合，这个通常用到
//	 * 
//	 * @param contacts
//	 * @param response
//	 */
//	public static void combineContacts(Contact[] contacts, INetResponse response) {
//		JsonObject bundle = m_buildRequestBundle(false);
//
////		bundle.put("method", "contact.talkContact");
//
//		String data = Contact.generateContactsJsonString(contacts, m_sessionKey);
//
//		// Methods.logOnFile(data, "up");
//		bundle.put("data", data);
//
//		bundle.put("v", "1.0");
//		bundle.put("format", "json");
//		// 分页控制，目前就不分页了
//		// bundle.put("page", 1);
//		// bundle.put("page_size", 30);
//		bundle.put("only_cell_phone", 1);
//		bundle.put("only_friend", 1);
//
//		INetRequest request = m_buildRequest(test_server+"/contact/talkContact", bundle, response);
//		m_sendRequest(request);
//	}
//
//	/**
//	 * 匹配通讯录
//	 * 
//	 * @author zhenning.yang, update by xiangchao.fan
//	 * @param contacts
//	 *            通讯录数据
//	 * @param response
//	 *            回调
//	 */
//	public static void matchContacts(boolean isSkip, Contact[] contacts, INetResponse response) {
//		JsonObject bundle = m_buildRequestBundle(false);
//		
//		String data = null;
//		if(!isSkip)
//			data = Contact.generateContactsJsonString(contacts, m_sessionKey);
//		bundle.put("data", data);
//		bundle.put("v", "1.0");
//		bundle.put("format", "json");
//		bundle.put("friend_type_switch", "1");
//		INetRequest request = m_buildRequest(test_server+"/contact/getFriends", bundle, response);
//		m_sendRequest(request);
//	}
//
//	/**
//	 * 
//	 * @param response
//	 * @param only_cell_phone
//	 *            则只取可用的手机，否则全部下发
//	 * @param only_friend
//	 *            等于1时，只下发纯好友的联系人
//	 */
//	public static void getOnlineContacts(INetResponse response, int only_cell_phone, int only_friend) {
//		JsonObject bundle = m_buildRequestBundle(false);
////		bundle.put("method", "contact.talkContact");
//		bundle.put("v", "1.0");
//		bundle.put("format", "json");
//		bundle.put("only_cell_phone", only_cell_phone);// 仅仅手机
//		bundle.put("only_friend", only_friend);// 纯好友
//		INetRequest request = m_buildRequest(test_server+"/contact/talkContact", bundle, response);
//		m_sendRequest(request);
//	}
//	/**
//	 * 获取特别关注好友
//	 * @param response
//	 */
//	public static void getFocusFriends(INetResponse response) {
//		JsonObject bundle = m_buildRequestBundle(false);
//		bundle.put("v", "1.0");
//		bundle.put("format", "json");
//		INetRequest request = m_buildRequest(test_server+"/friends/getFocusFriends", bundle, response);
//		m_sendRequest(request);
//	}
//	
//	
//	/**
//	 * 添加特别关注
//	 * @param response
//	 * @param uid
//	 */
//	public static void addFocusFriend(final INetResponse response,long uid) {
//		final JsonObject bundle = m_buildRequestBundle(false);
//		bundle.put("v", "1.0");
//		bundle.put("uid", uid);
//		INetRequest request = m_buildRequest(test_server+"/friends/addFocusFriend", bundle, response);
//		m_sendRequest(request);
//	}
//	/**
//	 * 取消特别关注
//	 * @param response
//	 * @param uid
//	 */
//	public static void delFocusFriend(final INetResponse response,long uid) {
//		final JsonObject bundle = m_buildRequestBundle(false);
//		bundle.put("v", "1.0");
//		bundle.put("uid", uid);
//		INetRequest request = m_buildRequest(test_server+"/friends/delFocusFriend", bundle, response);
//		m_sendRequest(request);
//	}
//
//	/**
//	 * 分享私信
//	 * 
//	 * @param response
//	 * @param batchRun
//	 * @return
//	 */
//	public static INetRequest m_statusSet(INetResponse response, boolean batchRun) {
//		JsonObject bundle = m_buildRequestBundle(batchRun);
////		bundle.put("method", "share.publishLink");
//		bundle.put("desc", "");
//		bundle.put("thumb_url", "http://fmn.rrimg.com/fmn060/20120628/1950/original_6wNB_2a6600001075118e.jpg");
////		bundle.put("url", "http://3g.renren.com/ep.do?c=9504901");
//		bundle.put("url", "http://mobile.renren.com/home?psf=42053");
//		bundle.put("title", "新版私信可以群聊了！和好友们畅聊一“夏”吧！立即下载！");
//		bundle.put("comment", "");
//		bundle.put("from", RenrenChatApplication.appid);
//		bundle.put("v", "1.0");
//		bundle.put("format", "json");
//		bundle.put("htf", Htf.RAPID_PUB_ACTIVITY);
//		INetRequest request = m_buildRequest(m_test_apiUrl+"/share/publishLink", bundle, response);
//		if (batchRun) {
//			return request;
//		} else {
//			m_sendRequest(request);
//			return null;
//		}
//	}
//	
//	/**
//	 * 异常日志
//	 * 
//	 * @param response
//	 * @param batchRun
//	 * @param ex  异常类型
//	 * @param sb  事件详细信息
//	 * @return
//	 */
//	public static INetRequest eventLog(INetResponse response, boolean batchRun,String ex,String sb) {
//		JsonObject bundle = m_buildRequestBundle(batchRun);
//		bundle.put("v", "1.0");
//		bundle.put("format", "json");
//		bundle.put("event_type", "10000");
//		bundle.put("exception_type",ex);
//		bundle.put("stack_info",sb);
//		bundle.put("client_info", getClientInfo());
//		INetRequest request = m_buildRequest(m_test_apiUrl+"/phoneclient/eventLog", bundle, response);
//		if (batchRun) {
//			return request;
//		} else {
//			m_sendRequest(request);
//			return null;
//		}
//	}
//	
//	/**
//	 * 后台运行
//	 * @param response
//	 * @param batchRun
//	 * @return
//	 */
//	
//	public static void backgroundRunStatistics(INetResponse response) {
//		JsonObject bundle = m_buildRequestBundle(false);
//		bundle.put("v", "1.0");
//		bundle.put("format", "json");
//		bundle.put("client_info", getClientInfo());
//		INetRequest request = m_buildRequest(m_test_apiUrl+"/client/maintain", bundle, response);
//		m_sendRequest(request);
//	}
//	
//
//	/**
//	 * 完善用户信息
//	 * 
//	 * @param response
//	 * @param userName
//	 *            用户名 (长度小于6汉字或12英文)
//	 * @param userGender
//	 *            性别(男生|女生)
//	 * @param year
//	 *            生日(年) （1900至今）
//	 * @param month
//	 *            生日(月)
//	 * @param day
//	 *            生日(日)
//	 * @param stage
//	 *            用户当前身份（已经工作，30| 大学，20| 中学，10| 其他，90）
//	 * @param batchRun
//	 * @return
//	 */
//	public static INetRequest supplyUserInfo(final INetResponse response, String userName, String userGender, int year, int month, int day, int stage, boolean batchRun) {
//		final JsonObject bundle = m_buildRequestBundle(false);
////		bundle.put("method", "user.fillInfo");
//		bundle.put("v", "1.0");
//		bundle.put("name", userName);
//		bundle.put("gender", userGender);
//		bundle.put("year", year);
//		bundle.put("month", month);
//		bundle.put("day", day);
//		bundle.put("stage", stage);
//		INetRequest request = m_buildRequest(m_test_apiUrl+"/user/fillInfo", bundle, response);
//		if (batchRun) {
//			return request;
//		} else {
//			m_sendRequest(request);
//			return null;
//		}
//	}
//
////	/**
////	 * 获取好友请求列表
////	 * 
////	 * @param page
////	 * @param batchRun
////	 * @param page
////	 * @param page_size
////	 * @author rubin.dong@renren-inc.com
////	 * @return
////	 */
////	public static INetRequest getFriendsRequest(final INetResponse response, int page, int page_size, int exclude_list, int del_news, boolean batchRun) {
////		final JsonObject bundle = m_buildRequestBundle(false);
////		bundle.put("method", "friends.getRequests");
////		bundle.put("v", "1.0");
////		bundle.put("page", page);
////		bundle.put("page_size", page_size);
////		bundle.put("del_news", del_news);
////		bundle.put("htf", Htf.REQUEST_BUTTON);
////		bundle.put("exclude_list", exclude_list);
////		if (!batchRun) {
////			bundle.put(INetRequest.gzip_key, INetRequest.gzip_value);
////		}
////		INetRequest request = m_buildRequest(m_test_apiUrl, bundle, response);
////		if (batchRun) {
////			return request;
////		} else {
////			m_sendRequest(request);
////			return null;
////		}
////	}
//
////	public static INetRequest getFriendInfo(int type, Long uid, INetResponse response, boolean batchRun) {
////		JsonObject bundle = m_buildRequestBundle(false);
//////		bundle.put("method", "profile.getInfo");
////		bundle.put("v", "1.0");
////		bundle.put("uid", uid);
////		bundle.put("type", type);
////		if (!batchRun) {
////			bundle.put(INetRequest.gzip_key, INetRequest.gzip_value);
////		}
////		INetRequest request = m_buildRequest(m_test_apiUrl+"/profile/getInfo", bundle, response);
////		if (batchRun) {
////			return request;
////		} else {
////			m_sendRequest(request);
////			return null;
////		}
////	}
//////////////////////////////////////////
////	/**
////	 * 获取留言回复数
////	 * 
////	 * **/
////	public static INetRequest getNewsCount(int type, INetResponse response, boolean batchRun) {
////		JsonObject bundle = m_buildRequestBundle(false);
////		bundle.put("method", "news.getCount");
////		bundle.put("v", "1.1");// 升级为V1.1主要是为了用子类型取代原来的类型
////		bundle.put("format", "JSON"/* TODO */);
////		bundle.put("type", type);
////		bundle.put("update_timestamp", 1);// 更新push消息时间戳
////		INetRequest request = m_buildRequest(m_test_apiUrl, bundle, response);
////		if (batchRun) {
////			return request;
////		} else {
////			m_sendRequest(request);
////			return null;
////		}
////	}
//////////////////////////////////////////////////////////////////////
////	public static INetRequest getRecommendFriendsRequest(final INetResponse response, int page, int page_size, boolean batchRun) {
////
////		final JsonObject bundle = m_buildRequestBundle(false);
//////		bundle.put("method", "contact.talkFriendRecommend");
////		bundle.put("v", "1.0");
////		bundle.put("page", page);
////		bundle.put("page_size", page_size);
////		if (!batchRun) {
////			bundle.put(INetRequest.gzip_key, INetRequest.gzip_value);
////		}
////		INetRequest request = m_buildRequest(m_test_apiUrl+"/contact/talkFriendRecommend", bundle, response);
////		if (batchRun) {
////			return request;
////		} else {
////			m_sendRequest(request);
////			return null;
////		}
////	}
//
////	/**
////	 * 获取祝福短信
////	 * 
////	 */
////	public static INetRequest getBlessSMS(final INetResponse response, boolean batchRun) {
////
////		final JsonObject bundle = m_buildRequestBundle(false);
//////		bundle.put("method", "sms.gets");
////		bundle.put("v", "1.0");
////		if (!batchRun) {
////			bundle.put(INetRequest.gzip_key, INetRequest.gzip_value);
////		}
////		INetRequest request = m_buildRequest(m_test_apiUrl+"/sms/gets", bundle, response);
////		if (batchRun) {
////			return request;
////		} else {
////			m_sendRequest(request);
////			return null;
////		}
////	}
//
////	/**
////	 * 接受好友请求
////	 * 
////	 * @param uid
////	 * @param response
////	 * @param batchRun
////	 * @author rubin.dong@renren-inc.com
////	 * @return
////	 */
////	public static INetRequest acceptFriendRequest(long uid, final INetResponse response, boolean batchRun) {
////		final JsonObject bundle = m_buildRequestBundle(false);
//////		bundle.put("method", "friends.accept");
////		bundle.put("v", "1.0");
////		bundle.put("user_id", uid);
////		bundle.put("htf", Htf.REQUEST_RECIVIED);
////		INetRequest request = m_buildRequest(m_test_apiUrl+"/friends/accept", bundle, response);
////		if (batchRun) {
////			return request;
////		} else {
////			m_sendRequest(request);
////			return null;
////		}
////	}
//
////	/**
////	 * 忽略好友请求
////	 * 
////	 * @param uid
////	 * @param response
////	 * @param batchRun
////	 * @author rubin.dong@renren-inc.com
////	 * @return
////	 */
////	public static INetRequest denyFriendRequest(long uid, final INetResponse response, boolean batchRun) {
////
////		final JsonObject bundle = m_buildRequestBundle(false);
//////		bundle.put("method", "friends.deny");
////		bundle.put("v", "1.0");
////		bundle.put("user_id", uid);
////		INetRequest request = m_buildRequest(m_test_apiUrl+"/friends/deny", bundle, response);
////		if (batchRun) {
////			return request;
////		} else {
////			m_sendRequest(request);
////			return null;
////		}
////	}
//
////	/**
////	 * 
////	 * 获取特定位置的静态地图
////	 * 
////	 * @param latitude
////	 * @param longitude
////	 * @param latlon
////	 * @param height
////	 *            地图的高度,默认为320,单位为像素
////	 * @param width
////	 *            地图的宽度,默认为240,单位为像素
////	 * @param zoom
////	 *            地图的zoom值，尺寸，1-11，默认11
////	 * @author tian.wang
////	 * */
////	public static void getStaticMap(long latitude, long longitude, JsonObject latlon, int height, int width, int zoom, INetResponse response) {
////
////		JsonObject bundle = m_buildRequestBundle(false);
//////		bundle.put("method", "lbs.getStaticMap");
////		bundle.put("v", "1.0");
////		bundle.put("format", "json");
////		bundle.put("latitude", latitude);
////		bundle.put("longitude", longitude);
////		bundle.put("height", height);
////		bundle.put("width", width);
////		bundle.put("zoom", zoom);
////		bundle.put(INetRequest.gzip_key, INetRequest.gzip_value);
////		bundle.put("latlon", latlon.toJsonString());
////
////		INetRequest request = m_buildRequest(m_test_apiUrl+ "/lbs/getStaticMap", bundle, response);
////
////		m_sendRequest(request);
////	}
//
////	/**
////	 * 下载语音文件
////	 * */
////	public static void downLoadVoice(String voiceUrl, INetResponse reponse) {
////		INetRequest request = new HttpRequestWrapper();
////		request.setId(911);// TODO 干什么用的？答:为了查错，可以
////		request.setType(INetRequest.TYPE_HTTP_GET_VOICE);
////		request.setUrl(voiceUrl);
////		request.setResponse(reponse);
////		request.setSecretKey(m_secretKey);
////		HttpProviderWrapper.getInstance().addRequest(request);
////	}
//
//	
////	/**
////	 * 上传照片至人人网
////	 * 
////	 * @param imgData
////	 * @author sunnyykn
////	 */
////	public static void uploadPhoto(byte[] imgData, int type, int htf, int from, String statistic, String aid, String description, String placeData, INetResponse response) {
////
////		JsonObject bundle = new JsonObject();
////		bundle.put("data", buildData(aid, String.valueOf(type), String.valueOf(htf), String.valueOf(from), statistic, description, placeData, imgData));
////		INetRequest request = new HttpRequestWrapper();
////
////		request.setUrl(m_test_apiUrl);
////		request.setData(bundle);
////		request.setResponse(response);
////		// 使用 3G 服务器的 secret_key
////		request.setSecretKey(m_secretKey);
////		request.setType(INetRequest.TYPE_HTTP_POST_IMG);
////		 
//////		Log.v("kxy", "---> photos.upload Request: " + bundle.toString());
////		HttpProviderWrapper.getInstance().addRequest(request);
////	}
////	
////	private static byte[] buildData(String aid, String type, String htf, String from, String statistic, String description, String placeData, byte[] imgData) {
////
////		byte[] ret = null;
////		try {
////			String[] props = { "aid", "api_key", "call_id", "caption", "client_info", "statistic", "format", "htf", "method", "place_data", "session_key", "upload_type", "v", "from", "sig" };
////			String[] values = { aid, m_apiKey, String.valueOf(System.currentTimeMillis()), description, getClientInfo(), statistic, "json", htf, "photos.uploadbin", placeData, m_sessionKey, type, "1.0", from, "" };
////
////			String[] params = new String[props.length - 1];
////			for (int i = 0; i < params.length; i++) {
////				if (values[i] != null && values[i].length() > 50) {
////					params[i] = props[i] + "=" + values[i].substring(0, 50);
////				} else {
////					params[i] = props[i] + "=" + values[i];
////				}
////
////			}
////			values[values.length - 1] = getSig(params, m_secretKey);
////			String BOUNDARY = "FlPm4LpSXsE"; // separate line
////			StringBuffer sb = new StringBuffer();
////			for (int i = 0; i < props.length; i++) { // send each property
////				sb = sb.append("--");
////				sb = sb.append(BOUNDARY);
////				sb = sb.append("\r\n");
////				sb = sb.append("Content-Disposition: form-data; name=\"" + props[i] + "\"\r\n\r\n");
////				sb = sb.append(values[i]);
////				sb = sb.append("\r\n");
////			}
////			sb = sb.append("--");
////			sb = sb.append(BOUNDARY);
////			sb = sb.append("\r\n");
////			sb = sb.append("Content-Disposition: form-data;name=\"data\";filename=\"" + DateFormat.now2() + ".jpg\"\r\n");
////			sb = sb.append("Content-Type: image/jpg\r\n\r\n");
////			byte[] begin_data = sb.toString().getBytes("UTF-8");
////			byte[] end_data = ("\r\n--" + BOUNDARY + "--\r\n").getBytes("UTF-8");
////			ByteArrayOutputStream baos = new ByteArrayOutputStream();
////			baos.write(begin_data);
////			baos.write(imgData);
////			baos.write(end_data);
////			ret = baos.toByteArray();
////		} catch (Exception e) {
////			e.printStackTrace();
////		}
////		return ret;
////	}
//
////	/**
////	 * 构建照片上传的Multipart数据
////	 */
////	private static byte[] buildUploadPhotoData(int toId, byte[] imgData) {
////
////		byte[] ret = null;
////		try {
////			String[] props = { "api_key", "call_id", "client_info", "format", "method", "session_key", "toid", "v", "sig" };
////			String[] values = { m_apiKey, String.valueOf(System.currentTimeMillis()), getClientInfo(), "json", "talk.photoUploadBin", m_sessionKey, String.valueOf(toId), "1.0", "" };
////
////			String[] params = new String[props.length - 1];
////			for (int i = 0; i < params.length; i++) {
////				if (values[i] != null && values[i].length() > 50) {
////					params[i] = props[i] + "=" + values[i].substring(0, 50);
////				} else {
////					params[i] = props[i] + "=" + values[i];
////				}
////
////			}
////			values[values.length - 1] = getSig(params, m_secretKey);
////			String BOUNDARY = "FlPm4LpSXsE"; // separate line
////			StringBuffer sb = new StringBuffer();
////			for (int i = 0; i < props.length; i++) { // send each property
////				sb = sb.append("--");
////				sb = sb.append(BOUNDARY);
////				sb = sb.append("\r\n");
////				sb = sb.append("Content-Disposition: form-data; name=\"" + props[i] + "\"\r\n\r\n");
////				sb = sb.append(values[i]);
////				sb = sb.append("\r\n");
////			}
////			sb = sb.append("--");
////			sb = sb.append(BOUNDARY);
////			sb = sb.append("\r\n");
////			sb = sb.append("Content-Disposition: form-data;name=\"data\";filename=\"" + DateFormat.now2() + ".jpg\"\r\n");
////			sb = sb.append("Content-Type: image/jpg\r\n\r\n");
////			byte[] begin_data = sb.toString().getBytes("UTF-8");
////			byte[] end_data = ("\r\n--" + BOUNDARY + "--\r\n").getBytes("UTF-8");
////			ByteArrayOutputStream baos = new ByteArrayOutputStream();
////			baos.write(begin_data);
////			baos.write(imgData);
////			baos.write(end_data);
////			ret = baos.toByteArray();
////		} catch (Exception e) {
////			e.printStackTrace();
////		}
////		return ret;
////	}
////
////	/**
////	 * @author dingwei.chen
////	 * 
////	 * @说明 上传语音数据文件
////	 * 
////	 * @param vid
////	 *            string 语音组id
////	 * @param seqid
////	 *            int 切割语音的序列号
////	 * @param mode
////	 *            * string 传输模式segment|end，segment为分割语音模式，end为结束标识
////	 * */
////	public static void uploadAudioFile(int toId, String vid, int seqid, String mode, int playTime, byte[] fileData, INetResponse response) {
////		String str = ("toId=" + toId);
////		JsonObject bundle = new JsonObject();
////		bundle.put("data", buildUploadAudioData(toId, vid, seqid, mode, playTime, fileData));
////
////		INetRequest request = new HttpRequestWrapper();
////
////		request.setUrl(m_test_apiUrl);
////		request.setData(bundle);
////		request.setResponse(response);
////		// 使用 3G 服务器的 secret_key
////		request.setSecretKey(m_secretKey);
////		request.setType(INetRequest.TYPE_HTTP_POST_BIN_File);
////		request.setId(9999);
////		// request.setFileName(vid);
////		HttpProviderWrapper.getInstance().addRequest(request);
////	}
////
////	/**
////	 * @author dingwei.chen
////	 * 
////	 * @说明 构建照片上传的Multipart数据
////	 * 
////	 * @param vid
////	 *            string 语音组id
////	 * @param seqid
////	 *            int 切割语音的序列号
////	 * @param mode
////	 *            * string 传输模式segment|end，segment为分割语音模式，end为结束标识
////	 * */
////	private static byte[] buildUploadAudioData(int toId, String vid, int seqid, String mode, int playTime, byte[] imgData) {
////
////		byte[] ret = null;
////		try {
////			String[] props = { "api_key", "call_id", "client_info","format", "method", "mode", "playtime", "seqid", "session_key", "toid", "v", "vid", "sig" };
////			String[] values = { m_apiKey, String.valueOf(System.currentTimeMillis()),getClientInfo(), "json", "talk.voiceUploadBin2", mode, String.valueOf(playTime), String.valueOf(seqid), m_sessionKey, String.valueOf(toId), "1.0", vid, "" };
////			String[] params = new String[props.length - 1];
////			for (int i = 0; i < params.length; i++) {
////				if (values[i] != null && values[i].length() > 50) {
////					params[i] = props[i] + "=" + values[i].substring(0, 50);
////				} else {
////					params[i] = props[i] + "=" + values[i];
////				}
////
////			}
////			values[values.length - 1] = getSig(params, m_secretKey);
////			String BOUNDARY = "FlPm4LpSXsE"; // separate line
////			StringBuffer sb = new StringBuffer();
////			for (int i = 0; i < props.length; i++) { // send each property
////				sb = sb.append("--");
////				sb = sb.append(BOUNDARY);
////				sb = sb.append("\r\n");
////				sb = sb.append("Content-Disposition: form-data; name=\"" + props[i] + "\"\r\n\r\n");
////				sb = sb.append(values[i]);
////				sb = sb.append("\r\n");
////			}
////			sb = sb.append("--");
////			sb = sb.append(BOUNDARY);
////			sb = sb.append("\r\n");
////			sb = sb.append("Content-Disposition: form-data;name=\"data\";filename=\"test.spx" + "\"\r\n");
////			sb = sb.append("Content-Type: application/octet-stream\r\n\r\n");
////			byte[] begin_data = sb.toString().getBytes("UTF-8");
////			byte[] end_data = ("\r\n--" + BOUNDARY + "--\r\n").getBytes("UTF-8");
////			ByteArrayOutputStream baos = new ByteArrayOutputStream();
////			baos.write(begin_data);
////			baos.write(imgData);
////			baos.write(end_data);
////			ret = baos.toByteArray();
////		} catch (Exception e) {
////			e.printStackTrace();
////		}
////		return ret;
////	}
////	
////	public static void login(){
////		
////	}
//	
////	/**
////	 * @author add by xiangchao.fan
////	 * @description 上传统计数据
////	 * @param localStatistics
////	 * @param response
////	 */
////	public static void uploadStatistics(String localStatistics, INetResponse response) {
////		
////		JsonObject bundle = m_buildRequestBundle(false);
////
////		bundle.put("v", "1.0");
////		bundle.put("format", "json");
////		bundle.put("data", localStatistics);
////		bundle.put("fromId", String.valueOf(RenrenChatApplication.from));
////		bundle.put("terminal", "android");
////		bundle.put("version", RenrenChatApplication.versionName);
////		
////		INetRequest request = m_buildRequest(m_test_apiUrl+"/phoneclient/actionLog", bundle, response);
////		m_sendRequest(request);
////	}
//	
////	/**
////	 * @author add by xiangchao.fan
////	 * @description 判断用户userId是否为某公共主页pageId的粉丝
////	 * @param userId
////	 * @param pageId
////	 * @param response
////	 */
////	public static void isFansOfPage(long userId, long pageId, INetResponse response){
////		JsonObject bundle = m_buildRequestBundle(false);
////
////		bundle.put("v", "1.0");
////		bundle.put("format", "json");
////		bundle.put("user_id", userId);
////		bundle.put("page_id", pageId);
////		
////		INetRequest request = m_buildRequest(m_test_apiUrl+"/page/isFans", bundle, response);
////		m_sendRequest(request);
////	}
//	
////	/**
////	 * @author add by xiangchao.fan
////	 * @description 成为某公共主页pageId的粉丝
////	 * @param pageId
////	 * @param response
////	 */
////	public static void becomeFansOfPage(int pageId, INetResponse response){
////		JsonObject bundle = m_buildRequestBundle(false);
////
////		bundle.put("v", "1.0");
////		bundle.put("format", "json");
////		bundle.put("page_id", pageId);
////		
////		INetRequest request = m_buildRequest(m_test_apiUrl+"/page/becomeFan", bundle, response);
////		m_sendRequest(request);
////	}
//
//}
