package com.common.utils;

import java.util.Locale;

import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.text.TextUtils;

import com.common.app.AbstractRenrenApplication;
import com.common.messagecenter.base.Utils;

/**
 * author yuchao.zhang
 * 
 * description 定义MCS、MAS的服务器地址 定义国内、国际版本类型
 * 
 * read me before use : 由当前版本类型 决定 使用哪一个服务（国内：MCS 国际：MAS）
 * 切换版本类型时，一定要切换为相应的服务器地址！
 * 切换为国际版时，string.xml中的api_key需要更改为aaa1，secret_key需要更改为bbb1！
 * 
 * 目前MAS服务在开发阶段，陆续联调更新，之后MAS会全面替换MCS服务，MCS服务废弃 目前架构支持MAS、MCS服务同时使用！
 */
public class Config {

	 /** add by jia.xia
     * 国内版本
     */
    public final static int VERSION_INLAND = 1;
    /**add by jia.xia
     * 新版本类型
     */
    public final static int VERSION_INTERNATIONAL = 2;
    /**add by jia.xia
     * 当前版本类型
     */
    public static int VERSION_CURRENT ;
    
    public static Locale DEFUALT_LOCALE;
	
	/**
	 * 图片请求最大线程数
	 */
	public final static int IMG_THREAD_NUM = 3;

	/**
	 * 文本请求最大线程数
	 */
	public final static int TEXT_THREAD_NUM = 3;

	/**
	 * Preference 的名字
	 */
	public final String PREF = "MY_PREF";

	/**
	 * 上传图片超时时间，毫秒
	 */
	public final static int HTTP_POST_IMAGE_TIMEOUT = 45000;

	/**
	 * 上传文件超时时间，毫秒
	 */
	public final static int HTTP_POST_BIN_TIMEOUT = 45000;

	/**
	 * 文本、图片下载请求超时时间，毫秒
	 */
	public final static int HTTP_POST_TEXT_TIMEOUT = 45000;
	
	/**
	 * 离线消息接收超时时间，毫秒
	 */
	public final static int OFFLINE_MESSAGE_TIMEOUT = 2000;

	/**
	 * V类型： 用于确定所支持的推送消息类型, 例如4：服务器主动推送用户文本聊天数据，语音图片聊天数据，消息提醒，上下线消息 。
	 */
	public final static int V_TYPE = 15;

	public static String HTTP_SEND_URL;
	public static String HTTP_TALK_URL;
	public static String SOCKET_URL;
	public static int SOCKET_DEFAULT_PORT;
	public static boolean IS_ADD_XONLINEHOST;

	private static String SEND_WAP_URL; // 聊天接口
	private static String TALK_WAP_URL ; // 轮循接口
	// TODO 发版前修改地址
	private static String REAL_TALK_URL;
	private static String REAL_SEND_URL;

	/**
	 * talk服务器默认地址
	 */
	public static  String HOST_NAME = null;
	/**
	 * 默认的talk服务器地址
	 */
	/**
	 * MAS国际版本的默认测试服务器
	 */
	/**
	 * mas服务器默认地址前缀
	 */
	private final static String serverUrlPrefix = "http://";
	/**
	 * mas服务器默认地址后缀
	 */
	public static int HTTP_DEFAULT_PORT;
	/**
	 * mas服务器地址
	 */
	public static String CURRENT_SERVER_URI = null;
	
	static RRSharedPreferences url;

	
	static {
		url = new RRSharedPreferences(AbstractRenrenApplication.getAppContext());

		
		if ("com.renren.mobile.chat".equals(AbstractRenrenApplication
				.getAppContext().getPackageName())) {
			// 国内版
			/**
			 * MAS服务器
			 */
//			CURRENT_SERVER_URI = "http://123.125.42.189:8080/api/sixin/3.0"; // MAS国内版本的测试服务器
			CURRENT_SERVER_URI = "http://mas.m.renren.com/api/sixin/3.0"; // MAS国内版本的测试服务器
			VERSION_CURRENT = VERSION_INLAND;
			DEFUALT_LOCALE = Locale.SIMPLIFIED_CHINESE;
			/**
			 * Talk服务器
			 */
			HOST_NAME = "talk.m.renren.com";
//			HOST_NAME = "111.13.5.79";
			HTTP_DEFAULT_PORT = 80;
			SOCKET_DEFAULT_PORT = 25553;
			REAL_TALK_URL = "http://" + HOST_NAME + ":" + HTTP_DEFAULT_PORT
					+ "/talk";
			REAL_SEND_URL = "http://" + HOST_NAME + ":" + HTTP_DEFAULT_PORT
					+ "/send";
			changeHttpURL();
			SOCKET_URL = HOST_NAME;
			// SOCKET_PORT = 3555;
			
		} else {
			// 国际版
			initInternation();
			
		}
		
	}
 
	public static void initInternation(){
		/**
		 * MAS服务器
		 */
		String tempUrl = url.getStringValue("mas_url_0", "api.m.appsurdityinc.com");
		CURRENT_SERVER_URI = "http://" + tempUrl + "/api/sixin/3.0";
		 
		VERSION_CURRENT = VERSION_INTERNATIONAL;
		DEFUALT_LOCALE = Locale.US;
		/**
		 * Talk服务器
		 */
		HOST_NAME = url.getStringValue("talk_url_0", "talk.m.appsurdityinc.com");
			
		HTTP_DEFAULT_PORT = 12345;
		SOCKET_DEFAULT_PORT = 3555;
		REAL_TALK_URL = "http://" + HOST_NAME + ":" + HTTP_DEFAULT_PORT
				+ "/talk";
		REAL_SEND_URL = "http://" + HOST_NAME + ":" + HTTP_DEFAULT_PORT
				+ "/send";
		//TODO 下面这段代码是针对国内网络情况的，国际版需要修改
		changeHttpURL();
		SOCKET_URL = HOST_NAME;
	}
	
	public static void changeHttpURL(){
		String proxyHostname = "", proxyPort = "";
		String url;
		Uri apnUri = Uri.parse("content://telephony/carriers/preferapn");
		String[] apnInfo = { "proxy", "port" };
		Cursor cursor = AbstractRenrenApplication.getAppContext()
				.getContentResolver()
				.query(apnUri, apnInfo, null, null, null);
		if (cursor != null && cursor.getCount() > 0) {
			cursor.moveToFirst();
			proxyHostname = cursor
					.getString(cursor.getColumnIndex("proxy"));
			proxyPort = cursor.getString(cursor.getColumnIndex("port"));
			cursor.close();
		}
		url = "http://"
				+ (TextUtils.isEmpty(proxyHostname) ? android.net.Proxy.getDefaultHost() : proxyHostname)
				+ ":"
				+ (TextUtils.isEmpty(proxyPort) ? android.net.Proxy.getDefaultPort() : proxyPort);
		Utils.l("url--->" + url);
		SEND_WAP_URL = url + "/send";
		TALK_WAP_URL = url + "/talk";
		ConnectivityManager cm = (ConnectivityManager) AbstractRenrenApplication
				.getAppContext().getSystemService(
						android.content.Context.CONNECTIVITY_SERVICE);
		NetworkInfo mobileInfo = cm
				.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		if (mobileInfo != null
				&& ("cmwap".equalsIgnoreCase(mobileInfo.getExtraInfo()) || 
					"3gwap".equalsIgnoreCase(mobileInfo.getExtraInfo()) ||
					"uniwap".equalsIgnoreCase(mobileInfo.getExtraInfo()))) {
			HTTP_TALK_URL = TALK_WAP_URL;
			HTTP_SEND_URL = SEND_WAP_URL;
			IS_ADD_XONLINEHOST = true;
		} else {
			HTTP_TALK_URL = REAL_TALK_URL;
			HTTP_SEND_URL = REAL_SEND_URL;
			IS_ADD_XONLINEHOST = false;
		}
		Utils.l("Http:" + HTTP_SEND_URL);
		Utils.l("Socket:" + SOCKET_URL);
	}
}
