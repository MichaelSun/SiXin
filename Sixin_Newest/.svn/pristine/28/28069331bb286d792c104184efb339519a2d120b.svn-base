package com.renren.mobile.account;

import java.util.ArrayList;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.common.emotion.emotion.EmotionPool;
import com.common.manager.LoginManager;
import com.common.manager.LoginManager.LoginInfo;
import com.common.manager.LoginManager.LoginListener;
import com.common.manager.LoginManager.LoginStatusListener;
import com.common.manager.LoginfoRestListener;
import com.common.network.DomainUrl;
import com.core.json.JsonArray;
import com.core.json.JsonObject;
import com.core.util.SystemService;
import com.renren.mobile.chat.RenrenChatApplication;
import com.renren.mobile.chat.base.model.QuiteMessageModel;
import com.renren.mobile.chat.base.util.ImagePool;
import com.renren.mobile.chat.base.util.SystemUtil;
import com.renren.mobile.chat.cache.HeadImagePool;
import com.renren.mobile.chat.common.RRSharedPreferences;
import com.renren.mobile.chat.common.ResponseError;
import com.renren.mobile.chat.contact.ContactUtils;
import com.renren.mobile.chat.contact.TitleMapCache;
import com.renren.mobile.chat.dao.AccountDAO;
import com.renren.mobile.chat.dao.ChatHistoryDAO;
import com.renren.mobile.chat.dao.ContactsDAO;
import com.renren.mobile.chat.dao.DAOFactoryImpl;
import com.renren.mobile.chat.service.ChatService;
import com.renren.mobile.chat.ui.MainFragmentActivity;
import com.renren.mobile.chat.ui.account.LoginSixinActivity;
import com.renren.mobile.chat.ui.account.RenrenAuthorizeActivity;
import com.renren.mobile.chat.ui.chatsession.ChatSessionHelper;
import com.renren.mobile.chat.ui.contact.C_ContactsData;
import com.renren.mobile.chat.ui.contact.ContactMessageData;
import com.renren.mobile.chat.ui.contact.RoomInfosData;
import com.renren.mobile.chat.ui.contact.StrangerContactCache;
import com.renren.mobile.chat.ui.contact.feed.ChatFeedAdapter;
import com.renren.mobile.chat.ui.contact.feed.ChatFeedDataManager;
import com.renren.mobile.chat.ui.contact.feed.FeedCallbackSource;
import com.renren.mobile.chat.ui.contact.feed.ObserverImpl;
import com.renren.mobile.chat.ui.guide.WelcomeActivity;
import com.renren.mobile.chat.ui.imageviewer.ImageViewActivity;
import com.renren.mobile.chat.ui.notification.FeedNotificationManager;
import com.renren.mobile.chat.ui.notification.MessageNotificationManager;
import com.renren.mobile.chat.util.ChatDataHelper;
import com.renren.mobile.chat.util.ChatMessageSender;
import com.renren.mobile.chat.webview.RenRenWebView;


/**
 * 单例实现，封装了登录和注销的操作
 */
public final class LoginControlCenter implements LoginStatusListener, LoginListener, LoginfoRestListener{
	private static LoginControlCenter mManager = new LoginControlCenter();
	private static final String FIRST_INSTALL = "first_install";
	private static final String ACCOUNT_LOGOUT_BRODCAST_ACTION = "com.renren.mobile.android.SIXIN_LOGOUT";
	private static final String LOGOUT_PARAMS_USER_ID = "user_id";
	//private Login.LoginStatusListener loginStatusListener;
	private LoginSixinActivity.LoginStatusListener loginStatusListener;
	
	private String mUsername;
	private String mPwd;
	private AccountDAO mAccountDao = null;
	
	private LoginControlCenter() {
		mAccountDao = DAOFactoryImpl.getInstance().buildDAO(AccountDAO.class);
		//LoginManager.getInstance().registorLoginListener(this);
		//LoginManager.LoginInfo testInfo=new LoginManager.LoginInfo();
	}
	
	public static LoginControlCenter getInstance() {
		return mManager;
	}
	
	public boolean isFirstInstall(Context context){
		RRSharedPreferences mRenrenPreferences = new RRSharedPreferences(context);
		boolean flag = mRenrenPreferences.getBooleanValue(FIRST_INSTALL, true);
		if(flag){
			mRenrenPreferences.getEditor().putBoolean(FIRST_INSTALL, false);
			mRenrenPreferences.getEditor().commit();
		}
		return flag;
	}
	public void setIsFirstInstall(Context context,boolean flag){
		RRSharedPreferences mRenrenPreferences = new RRSharedPreferences(context);
		mRenrenPreferences.getEditor().putBoolean(FIRST_INSTALL, flag);
		mRenrenPreferences.getEditor().commit();
	}
	
	public LoginfoModel loadLoginInfoFromRenrenClient(Context context){
		return mAccountDao.readLoginMessageFromRenren(context);
	}
		
	/**
	 * @author dingwei.chen
	 * 说明 读取上一个登录用户信息
	 * */
	public LoginfoModel loadPreUserData() {
		if(mAccountDao == null){
			return null;
		}
		LoginfoModel tmp = mAccountDao.loadPreUserData();
		if(tmp != null){
			LoginManager.getInstance().initLoginInfo(tmp.getLoginfo());
		}
		return tmp;
    }
	
	public LoginfoModel loadLastLoginUserData() {
		if(mAccountDao == null){
			SystemUtil.logykn("mAccountDao is null");
			return null;
		}
		LoginfoModel tmp = mAccountDao.loadLastLoginUserData();
		return tmp;
    }
	
	public LoginfoModel loadAutoLoginUserData() {
		if(mAccountDao == null){
			SystemUtil.logykn("mAccountDao is null");
			return null;
		}
		LoginfoModel tmp = mAccountDao.loadAutoLoginUserData();
		return tmp;
    }
	
	/**
	 * @author kaining.yang
	 * 判断直接进入人人帐号授权页 or 欢迎界面（私信帐号、人人帐号、注册私信）
	 */
	public void pageJump(Context context, RRSharedPreferences rRSharedPreferences){
		LoginfoModel loginInfo = loadPreUserData();
		
		if(loginInfo != null) {
			if(!LoginManager.getInstance().isLogout()) {
				/*
				// kaining.yang
				// renren_authorize 1 官方授权 0 直接登陆
				RRSharedPreferences rrSharedPreferences = new RRSharedPreferences(context, "login_authorize");
				
				// 两种登陆
				if (rrSharedPreferences.getIntValue("renren_authorize", 0) == 1) {
					// 检测到已登陆的官方客户端帐号，通过授权页面可直接登陆
					// == 1
					RenrenAuthorizeActivity.show(context);
					
				} else {
					// 用户已登陆，直接进入聊天主页面
					ChatService.start();
					RenrenChatApplication.updateFromPreInfo(loginInfo);// 更新全局量
					RenrenChatApplication.init();
					rRSharedPreferences.putBooleanValue("home", false);
					MainFragmentActivity.show(context,MainFragmentActivity.INDEX_AUTO_CHOOSE);
				}
				*/
				
				
				// kaining.yang 更改逻辑后
				ChatService.start();
//				RenrenChatApplication.updateFromPreInfo(loginInfo);// 更新全局量
				RenrenChatApplication.init();
				rRSharedPreferences.putBooleanValue("home", false);
				MainFragmentActivity.show(context,MainFragmentActivity.INDEX_AUTO_CHOOSE);
			} else {
				// 此处原则上永远跑不到 kaining.yang
				/*
				// 用户注销了上次登陆
				if(loginInfo.mAccount != null){
					RenrenChatApplication.lastAccount = loginInfo.mAccount;
				}else{
					loginInfo.mAccount = "";
				}
				WelcomeActivity.show(context);
				*/
			}
		}else{
			// login_info == null
			RRSharedPreferences rrSharedPreferences = new RRSharedPreferences(context, "login_authorize");
			// 两种登陆
			if (rrSharedPreferences.getIntValue("renren_authorize", 0) == 1) {
				// 检测到已登陆的官方客户端帐号，通过授权页面可直接登陆
				// == 1
				RenrenAuthorizeActivity.show(context);
			} else {
				WelcomeActivity.show(context);
			}
		}
		
	}
	
	/** 
	 * 加载本地文件存储的用户信息 
	 * */
	public LoginfoModel loadLocalUserInfo() {
		if(mAccountDao==null){
			return null;
		}
		LoginfoModel loginfoModel = mAccountDao.loadAutoLoginUserData();
		if(loginfoModel !=null){
			LoginManager.getInstance().initLoginInfo(loginfoModel.getLoginfo());
		}else{
			
		}
		return loginfoModel;
	}
	
	/**
	 * 获取设备所有登录过的用户名
	 * @return
	 */
	public ArrayList<String> getAllAccounts() {
		if(mAccountDao == null) {
			return null;
		}
		
	    return mAccountDao.getAllAccounts();
	}
	/**
	 * @author dingwei.chen
	 * 说明 		存储用户信息
	 * @param account 登录账户
	 * @param password_md5 md5转换后的登录密码
	 * @param object 调用登录接口返回的信息串
	 * */
	public void saveUserData(String account,String password_md5,JsonObject object) {
		mAccountDao.saveUserData(account, password_md5, object);
	}
	/**
	 * @author dingwei.chen
	 * 说明 		存储用户信息
	 * @param account 登录账户
	 * @param password_md5 md5转换后的登录密码
	 * @param object 调用登录接口返回的信息串
	 * */
	public void saveUserData(LoginfoModel info) {
		mAccountDao.saveUserData(info);
	}
	
	/**
	 * @author dingwei.chen
	 * 说明 		存储用户信息
	 * */
	public void saveUserData(String info_json){
		mAccountDao.saveUserData(info_json);
	}
	
	public void updateUserData(LoginfoModel info) {
		mAccountDao.updateAccountInfoDB(info);
	}
	
	/**
	 * 登录 MAS
	 */
	public void login(int accountType, String account, String pwd, String captcha, Context context, LoginSixinActivity.LoginStatusListener loginListener,long session) {
		loginStatusListener=loginListener;
		mUsername=account;
		mPwd=pwd;
		LoginManager.getInstance().login(accountType, account, pwd, captcha, session, this);
	}
	
	/**
	 * 登录 MCS
	 */
	/*public void login(String account, String pwd, Context context, LoginSixinActivity.LoginStatusListener loginListener,long session) {
		//Logd.traces();
		//Logd.log("mUsername="+mUsername+"#mPwd="+mPwd);
		loginStatusListener=loginListener;
		mUsername=account;
		mPwd=pwd;
		LoginManager.getInstance().login(account, pwd, session, this);
	}*/
	
	public void register(String username, String password, String captcha, int sex, String realname, LoginSixinActivity.LoginStatusListener loginListener) {
		mUsername = username;
		mPwd = password;
		loginStatusListener = loginListener;
		LoginManager.getInstance().register(username, password, captcha, sex, realname, this);
	}

	/**
	 * 注销及跳转 
	 * 注销时需设置当前帐号的AutoLogin字段为0（kaining.yang）
	 */
	public void logout(Context context) {
		logout(context, true);
	}
	
	/**
	 * 注销及跳转 
	 * 注销时需设置当前帐号的AutoLogin字段为0（kaining.yang）
	 */
	public void logout(Context context, boolean clearStack) {
		//mAccountDao.saveUserLogoutInfo(RenrenChatApplication.account);
		mAccountDao.updateUserInfoLogout(LoginManager.getInstance().getLoginInfo());
		if (clearStack) {
			RenrenChatApplication.clearStack();
		}
		/*发送离线广播*/
		this.sendLogoutBrodcast(context);
		// 清除好友列表
		C_ContactsData.getInstance().clear();
		ContactMessageData.getInstance().clear();
		//清除房间信息
		RoomInfosData.getInstance().clear();
		MessageNotificationManager.getInstance().clearCache();
		
		//RenrenChatApplication.setThird(false);
		
		//清空联系人title
		TitleMapCache.clear();
		// 清除头像缓存
		HeadImagePool.getInstance().clear();
		//清空图片缓存
		ImagePool.getInstance().recycle();
		//清空表情缓存
		EmotionPool.getInstance().clear();
		RenrenChatApplication.rid = 0;
		RenrenChatApplication.currenSession = 0;// 防止注销客户端后，还能收到消息
		RenrenChatApplication.sSessionId = "";
		
		ContactUtils.setIsSynced(false);
		RenrenChatApplication.mNewFriendsRequestCount = 0;
		RenrenChatApplication.sessionList.clear();
		
		LoginManager.getInstance().getLoginInfo().mLastAccount = LoginManager.getInstance().getLoginInfo().mAccount;
		
		//清空联系人缓存列表
		StrangerContactCache.getInstance().clear();
		//清空联系人管理列表
		MessageNotificationManager messageNotificationManager = MessageNotificationManager.getInstance();
		messageNotificationManager.clearUnReadMessageList();
		messageNotificationManager.clearAllNotification(context);
		messageNotificationManager.clearMessageNotificationModel();
		
		// 清空照片分享到人人网通知
		NotificationManager mNotificationManager = SystemService.sNotificationManager;
		mNotificationManager.cancel(ImageViewActivity.mNotificationId);
		mNotificationManager.cancel(ImageViewActivity.mNotificationId - 1);
		mNotificationManager.cancel(ImageViewActivity.mNotificationId - 2);
		
		FeedCallbackSource mCallbackSource = FeedNotificationManager.getInstance().getFeedCallbackSource();
		FeedNotificationManager.getInstance().clearAllNotification();
		if(mCallbackSource != null){
			mCallbackSource.clearFeedList();
		}
		//PollServiceProxy.getProxy().stop();
//		ChatService.stop();
		// 修改Account文件
		
//		RenRenBackgroundService.stop(context);
		
		// 重置sharepre的wait值 保证注销后进入应用可以出现闪屏
		RRSharedPreferences mRRSP = new RRSharedPreferences(RenrenChatApplication.mContext);
//		if (mRRSP.getBooleanValue("isWait", false) == false) {
//			mRRSP.putBooleanValue("isWait", true);
//		}
		if (mRRSP.getBooleanValue("isFirstLogin", true) == false) {
			mRRSP.putBooleanValue("isFirstLogin", true);
		}
		//清空特别关注相关内存
//		AttentionAdapter.clearMemory();
		//清空新鲜事列表和未读新鲜事
		ChatFeedAdapter.mDataList.clear();
		ObserverImpl.getInstance().clearFeedList();
		ChatFeedDataManager.CHAT_FEED_CURRENT_PAGE = ChatFeedDataManager.CHAT_FEED_DEFAULT_PAGE;
		
		//特别关注添加全局锁
//		AttentionData.clearLock();
		//清空特别关注Map缓存
//		ContactAdapter.clearFocusMap();
		RRSharedPreferences mRenrenPreferences = new RRSharedPreferences(context);
		mRenrenPreferences.putBooleanValue("isOverSupply", true);
		ChatHistoryDAO dao = DAOFactoryImpl.getInstance().buildDAO(ChatHistoryDAO.class);
		dao.insertChatMessageList(RenrenChatApplication.mMessageListCache);
		RenrenChatApplication.mMessageListCache.clear();
		
		if(RenRenWebView.sCookieManager != null){
			RenRenWebView.sCookieManager.removeAllCookie();
		}
		LoginManager.getInstance().logout();
	}

	/**
	 * 跳转
	 * 
	 * @param context
	 */
	public void gotoLogin(Context context) {
		WelcomeActivity.show(context);
	}
	
	/**
	 * 发送聊天轮询请求
	 * */
	public static void sendQuiteMessage() {
		QuiteMessageModel message = new QuiteMessageModel(RenrenChatApplication.sSessionId);
		ChatMessageSender.getInstance().sendMessageToNet(message.toString());
	}
	
	public void sendLogoutBrodcast(Context context){
		Intent intent = new Intent(ACCOUNT_LOGOUT_BRODCAST_ACTION);
		intent.putExtra(LOGOUT_PARAMS_USER_ID, LoginManager.getInstance().getLoginInfo().mUserId);
		context.sendBroadcast(intent);
	}
	
	@Override
	public void onLoginResponse() {
		if(loginStatusListener!=null){
			loginStatusListener.onLoginResponse();
		}
	}
	@Override
	public void onLoginSuccess(com.core.json.JsonObject data, long session) {
		LoginManager.getInstance().getLoginInfo().mIsLogin = true;
		SystemUtil.logykn("Json:" + data.toJsonString());
		SystemUtil.logykn("Json:" + data.toString());
		// kaining.yang 登陆成功 
		LoginfoModel tmpInfo = parseLoginInfo(data); 
		tmpInfo.mAutoLogin = 1;
		tmpInfo.mLastLogin = 0;
		tmpInfo.mAccount = mUsername;
		tmpInfo.mPassword = mPwd;
		SystemUtil.logykn("登陆成功用户名:" + mUsername);
		SystemUtil.logykn("登陆成功密码:" + mPwd);
//		RenrenChatApplication.updateFromPreInfo(tmpInfo);
		saveUserData(tmpInfo);
		LoginManager.getInstance().initLoginInfo(tmpInfo.getLoginfo());
		
		if(loginStatusListener != null){
			int mPerfectCode = (int) data.getNum("fill_stage");
			loginStatusListener.onLoginSuccess(mPerfectCode, session);
		}
	}
	
	@Override
	public void onLoginFailed(JsonObject data, long session) {
		int error_code = (int) data.getNum("error_code");
		String error_msg = data.getString("error_msg");
		ResponseError.showError(data);
		if (error_code == 10004) {
			// 需要登录验证码的错误码
			LoginfoModel tmpInfo = new LoginfoModel();
			LoginManager.getInstance().initLoginInfo(tmpInfo.getLoginfo());
			LoginManager.getInstance().getLoginInfo().mCaptchaUrl = error_msg;
			LoginManager.getInstance().getLoginInfo().mCaptchaNeeded = 1;
		}
		
		if(loginStatusListener != null){
			loginStatusListener.onLoginFailed(error_code, error_msg, session);
		}
	}

	@Override
	public void onLogin(LoginInfo info) {
		
	}

	@Override
	public void onLogout() {
		
	}
	
	public void onLoginClear() {
		if(SystemUtil.mDebug){
			SystemUtil.logd("账户不相同  删除所有数据");
		}
		RoomInfosData.getInstance().delete_all_rooms();
		ChatDataHelper.getInstance().deleteAll();
		ChatSessionHelper.getInstance().deleteAll();
		ContactsDAO contactDao =DAOFactoryImpl.getInstance().buildDAO(ContactsDAO.class);
		contactDao.delete_All();
		C_ContactsData.getInstance().deleteAllRenRenContact();
		ContactMessageData.getInstance().delete_All();
		C_ContactsData.getInstance().deleteAllCommonContact();
		C_ContactsData.getInstance().clear();
	}
	
	/**
	 * {"session_key":"81NYbreu22hh8tE1",
	 * "ticket":"ticket",
	 * "user_id":100001390,
	 * "secret_key":"d0dbdbaa72de44d9063e268f29fbc5d7",
	 * "user_name":"Name 100001390",
	 * "head_url":{"medium_url":"http://www.qiqu5.com/wp-content/uploads/2010/03/119.jpg","large_url":"http://t2.baidu.com/it/u=3925358291,1529856573&fm=52&gp=0.jpg","original_rul":"http://t3.baidu.com/it/u=295144277,3149792241&fm=51&gp=0.jpg"},
	 * "now":1345171798122,
	 * "login_count":0,
	 * "bind_info":[{"bind_id":"18211111111","type":"mobile","name":"","page":"主页"},{"bind_id":"18211111111","type":"mobile","name":"","page":"主页"},{"bind_id":"18211111111","type":"mobile","name":"","page":"主页"}]}
	 * @param object
	 * @return
	 */
	public LoginfoModel parseLoginInfo(com.core.json.JsonObject object) {
		LoginfoModel tmpInfo = new LoginfoModel();
		
		tmpInfo.mSessionKey = object.getString("session_key");
		//tmpInfo.mUserId 	= object.getNum("user_id"); 
		tmpInfo.mSecretKey 	= object.getString("secret_key");
		//tmpInfo.mNow        = object.getNum("now");  
		//tmpInfo.mLoginCount = object.getNum("login_count");
		tmpInfo.mDomainName = object.getString("domain_name");
		if (TextUtils.isEmpty(tmpInfo.mDomainName)) {
			tmpInfo.mDomainName = DomainUrl.SIXIN_DOMAIN;
		}
		
		JsonObject profileInfo = object.getJsonObject("profile_info");
		tmpInfo.mUserId        = profileInfo.getNum("user_id");
		tmpInfo.mUserName      = profileInfo.getString("name");
		//tmpInfo.mFirstName     = profileInfo.getString("first_name");
		//tmpInfo.mLastName      = profileInfo.getString("last_name");
		tmpInfo.mGender        = (int) profileInfo.getNum("gender");
		//tmpInfo.mBirthday      = profileInfo.getString("birth_display");
		JsonObject birthDay    = profileInfo.getJsonObject("birth_day");
		int year = (int) birthDay.getNum("year");
		int month = (int) birthDay.getNum("month");
		int day = (int) birthDay.getNum("day");
		tmpInfo.mBirthday = new StringBuilder().append(year).append("-").append(month).append("-").append(day).toString();
		
		JsonObject profileImage= profileInfo.getJsonObject("profile_image");
		tmpInfo.mLargeUrl      = profileImage.getString("large_url");
		tmpInfo.mMediumUrl     = profileImage.getString("medium_url");
		tmpInfo.mOriginal_Url  = profileImage.getString("original_url");
		
		// 学校
		JsonArray schools      = profileInfo.getJsonArray("school");
		JsonObject school      = (JsonObject) schools.get(0);
		tmpInfo.mSchool        = school.getString("name");
		
		// 公司 暂时不解析
		
		SystemUtil.logykn("mSecretKey:" + tmpInfo.mSecretKey);
		SystemUtil.logykn("mUserName:" + tmpInfo.mUserName);
		
		// kaining.yang BindInfo解析
		SystemUtil.logykn("json bindinfo:" + object.getJsonArray("bind_info"));
		// bindinfo tostring       [{type=renren, name=???, bind_id=258184610}]
		// bindinfo tojsonstring   [{"type":"renren","name":"???","bind_id":"258184610"}]
		if (object.getJsonArray("bind_info") != null) {
			tmpInfo.mBindInfo = "{\"bind_info\":" + object.getJsonArray("bind_info").toJsonString() + "}";
			SystemUtil.logykn("json jsonstring:" + object.getJsonArray("bind_info").toJsonString());
			SystemUtil.logykn("json string:" + object.getJsonArray("bind_info").toString());
		} else {
			tmpInfo.mBindInfo = "";
		}
		SystemUtil.logykn("bindinfo local:" + tmpInfo.mBindInfo);
		
		/*
		// test 可用
		JsonObject joo = (JsonObject) JsonParser.parse(tmpInfo.mBindInfo);
		JsonArray mja = joo.getJsonArray("bind_info");
		JsonObject jo = (JsonObject) mja.get(0);
		SystemUtil.logykn"json bindinfo:" + jo.getString("bind_id"));
		SystemUtil.logykn"json bindinfo:" + jo.getString("name"));
		SystemUtil.logykn"json bindinfo:" + jo.getString("page"));
		SystemUtil.logykn"json bindinfo:" + jo.getString("type"));
		*/
		
		return tmpInfo;
	}
	
	public LoginfoModel parseLoginInfo(Intent intent){
		LoginfoModel tmpInfo=new LoginfoModel();
		tmpInfo.mAccount 	= intent.getStringExtra(NEED_PARAMS.ACCOUNT);
		tmpInfo.mPassword 	= intent.getStringExtra(NEED_PARAMS.PASSWORD);
		tmpInfo.mSessionKey = intent.getStringExtra(NEED_PARAMS.SESSION_KEY);
		tmpInfo.mSecretKey 	= intent.getStringExtra(NEED_PARAMS.SECRET_KEY);
		tmpInfo.mHeadUrl 	= intent.getStringExtra(NEED_PARAMS.HEAD_URL);
		tmpInfo.mUserId 	= intent.getLongExtra(NEED_PARAMS.USER_ID,0l);
		tmpInfo.mUserName 	= intent.getStringExtra(NEED_PARAMS.USER_NAME);
		tmpInfo.mTicket 	= intent.getStringExtra(NEED_PARAMS.TICKET);
		return tmpInfo;
	}
	
	
	public static interface NEED_PARAMS{
		String ACCOUNT 		="logininfo_account";
		String PASSWORD		="logininfo_password";
		String SESSION_KEY	="logininfo_session_key";
		String SECRET_KEY	="logininfo_secret_key";
		String HEAD_URL 	="logininfo_head_url";
		String USER_ID 		="logininfo_uid";
		String USER_NAME	="logininfo_user_name";
		String LOGIN_COUNT  ="logininfo_login_count";
		String TICKET		="logininfo_ticket";
		String FILL_STAGE	="logininfo_fill_stage";
		String JSON_VALUE	="logininfo_json_value";
	}

	@Override
	public void reset() {
		loadLocalUserInfo();
	}
}
