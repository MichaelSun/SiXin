package com.renren.mobile.chat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import plugin.database.Plugin_DB;
import plugin.helpMe.database.HelpMe_DB;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Handler;
import android.util.Log;

import com.common.R;
import com.common.app.AbstractRenrenApplication;
import com.common.emotion.database.Emotion_DB;
import com.common.manager.LoginManager;
import com.core.json.JsonObject;
import com.core.util.CommonUtil;
import com.renren.mobile.account.LoginControlCenter;
import com.renren.mobile.chat.base.model.StateMessageModel;
import com.renren.mobile.chat.database.ChatDB;
import com.renren.mobile.chat.model.warpper.ChatMessageWarpper;
import com.renren.mobile.chat.third.ThirdActionsCenter;
import com.renren.mobile.chat.ui.contact.ContactBaseModel;
import com.renren.mobile.chat.ui.contact.feed.ObserverImpl;

//import com.renren.android.chat.ui.chat.ChatNotificationDataModel;
public class RenrenChatApplication extends AbstractRenrenApplication {
	@Override
	public void onCreate() {
		super.onCreate();
		new ChatDB(this);
		new Plugin_DB(this);
		new HelpMe_DB(this);
		new Emotion_DB(this);
		init();
		LoginManager.getInstance().setLoginfoListener(LoginControlCenter.getInstance());
		LoginControlCenter.getInstance().loadPreUserData();// 给loginmanager初始化
	}
	
	
	
	

	/**
	 * 登录配置项
	 * */
	/*
	public static String account;// 登录账户
	public static String head_url;// 登录用户头像
	public static String password;// 登录用户密码
	public static long user_id;// 登录用户ID
	public static String user_name;// 登录用户名
	public static String ticket;// 登录用户票webview使用
	*/
	public static long login_count = 0;// 登录次数
	
	
	/**
	 * 轮询配置项
	 * */
	public static String sSessionId;// 轮询会话ID
	public static int rid;// 轮询ACKID
	public static long currenSession;// 当前请求的session

	/**
	 * 用户信息配置(ClientInfo)
	 * */
	public static int from;
	public static int pubdate;
	public static int softid;
	public static int appid;
	public static String screen;
	public static String versionName;
	public static String subproperty;

	/**
	 * 其他全局量
	 * */
	public static Context mContext;
	public static Handler mHandler = new Handler();
	public static boolean firstRun;
	public static float density;// 屏幕密度（0.75 / 1.0 / 1.5）
	public static int screenResolution; // 屏幕分辨率
	public static boolean isStartSmsDBObserver = false;
	public static int currentIndex = 1;// 是否在会话页面
	public static int feedIndex = 2; // 是否在新鲜事页面
	public static int mFriendsRequestCount;// 好友请求个数
	public static int mNewFriendsRequestCount;// 好友请求个数
//	public static int mFeedUnReadCount;// 未读新鲜事个数
	public static boolean isMainFragementActivity = false;
	public static boolean isSessionLeave;// 是否从回话页面离开
	public static boolean isFeedLeave;// 是否从新鲜事页面离开
	public static ContactBaseModel sToChatContactInChat = null;
	private static List<Activity> sGlobalActivityStack = new LinkedList<Activity>();// 全局Activity栈
	private static boolean sIsFromThird = false;
	public static ChatMessageWarpper sForwardMessage = null;
	
	public static boolean sIsSingleLoginError = false;
	
	/** 私信秘书自动语言 */
	public static ChatMessageWarpper sAutoMessage = null;
	
	// 联系人是否加载完成
	public static boolean loadOverClock = false;
	public static boolean loadOverContact = true;
	
	public static Activity mWelcomeActivity = null;
	
	public static void clearWelcome() {
		if (mWelcomeActivity != null) {
			mWelcomeActivity.finish();
		}
	}
	/**
	 * @author dingwei.chen 轮询代理
	 * */
	// public static PollBinder sPollServiceProxy = null;
	
	//登录之后是否需要进入到指定页面
	public static Intent sNextIntent = null;

	public static Handler sHandler = new Handler();
	public static LinkedList<Object> sessionList = new LinkedList<Object>();
	// 主要是为了处理离线消息到来时还没有获取到联系人信息，所以先将消息缓存下来
	public static ArrayList<ChatMessageWarpper> mMessageListCache = new ArrayList<ChatMessageWarpper>();

	private static RenrenChatApplication mApplication = null;

	public static JsonObject userInfo;

	// public static String lastAccount = "";

	private static ObserverImpl sObserver = ObserverImpl.getInstance();
	public static String secretkey = "";

	public static void init() {
		String packageName = mContext.getPackageName();
		PackageManager pm = mContext.getPackageManager();
		try {
			from = Integer.parseInt(mContext.getResources().getString(
					R.string.from));
			pubdate = Integer.parseInt(mContext.getResources().getString(
					R.string.pubdate));
			softid = Integer.parseInt(mContext.getResources().getString(
					R.string.softid));
			appid = Integer.parseInt(mContext.getResources().getString(
					R.string.appid));
			subproperty = mContext.getResources().getString(
					R.string.subproperty);
			secretkey = mContext.getResources().getString(R.string.secretkey);
			PackageInfo info = pm.getPackageInfo(packageName, 0);
			versionName = info.versionName;
			
			FROM = String.valueOf(from);
			PUBLIC_DATE = String.valueOf(pubdate);
			SOFT_ID = String.valueOf(softid);
			APP_ID = String.valueOf(appid);
			VERSION = versionName;
			SUB_PROPERTY = subproperty;
			SCERET_KEY = secretkey;
			API_KEY =mContext.getResources().getString(R.string.apikey);
			
		} catch (NameNotFoundException e) {
		}
	}

	public static void pushStack(Activity activity) {
		if (sGlobalActivityStack.contains(activity)) {
			sGlobalActivityStack.remove(activity);
		}
		sGlobalActivityStack.add(0, activity);
	}

	public static void popStack(Activity activity) {
		if (sGlobalActivityStack.size() > 0) {
			Activity a = sGlobalActivityStack.get(0);
			if (a == activity) {
				sGlobalActivityStack.remove(0);
			}
		}
	}

	public static void removeActivity(Activity activity) {
		if (sGlobalActivityStack.size() > 0) {
			for (Activity a : sGlobalActivityStack) {
				if (a == activity) {
					a.finish();
					break;
				}
			}
		}
	}

	public static Context getCurrentActivity() {
		Activity a = null;
		if (sGlobalActivityStack.size() > 0) {
			a = sGlobalActivityStack.get(0);
		}
		if (a == null) {
			return mContext;
		}
		return a;
	}

	public static void clearStack() {
		for (Activity activity : sGlobalActivityStack) {
			activity.finish();
		}
		sGlobalActivityStack.clear();
	}

	public static Context getmContext() {
		return mContext;
	}

	/**
	 * @author dingwei.chen add by dingwei.chen 3-28
	 *         注*:登录信息对象不统一不方便处理(重构时需要统一对象)
	 *         
	 * @author tian.wang 满足你的愿望了
	 * */
//	public static void updateFromPreInfo(LoginfoModel info) {
//		McsServiceProvider.getProvider().setSessionKey(info.mSessionKey);
//		McsServiceProvider.getProvider().setSecretKey(info.mSecretKey);
//	}

	// ////////////////在线状态和线程池配置//////////////////
	public static interface Settings {
		/**
		 * 图片请求线程数
		 */
		int IMG_THREAD_NUM = 3;

		/**
		 * 文本请求线程数
		 */
		int TEXT_THREAD_NUM = 3;

		final public String PREF = "MY_PREF";
	}

	public RenrenChatApplication() throws IOException {
		mContext = this;
	}

	public static RenrenChatApplication getInstance() {
		return mApplication;
	}

	public static interface OnChatStateCallback {
		public void onStateChange(StateMessageModel message);
	}

	public static OnChatStateCallback mCallback = null;

	public static void registorChatStateCallback(OnChatStateCallback callback) {
		mCallback = callback;
	}

	public static void unregistorChatStateCallback(OnChatStateCallback callback) {
		if (mCallback == callback) {
			mCallback = null;
		}
	}

	public static void onStateUpdateCallback(StateMessageModel message) {
		if (mCallback != null) {
			mCallback.onStateChange(message);
		}
	}

	public static boolean getThird() {
		return sIsFromThird;
	}

	public static void setThird(boolean flag) {
		sIsFromThird = flag;
	}

	public static void onThirdCallExit() {
		ThirdActionsCenter.getInstance().sendThirdBroadcast();
	}

	public static final String PACKAGE_NAME = "com.renren.mobile.chat";

	@Override
	public String getPackageName() {
		return PACKAGE_NAME;
	}

}
