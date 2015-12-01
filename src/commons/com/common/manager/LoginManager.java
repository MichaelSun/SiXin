package com.common.manager;

import java.util.LinkedList;
import java.util.List;

import android.text.TextUtils;

import com.common.app.AbstractRenrenApplication;
import com.common.mcs.INetReponseAdapter;
import com.common.mcs.INetRequest;
import com.common.mcs.INetResponse;
import com.common.mcs.McsServiceProvider;
import com.common.messagecenter.base.Utils;
import com.core.json.JsonArray;
import com.core.json.JsonObject;
import com.core.json.JsonParser;
import com.core.util.CommonUtil;


/**
 * 单例实现，封装了登录和注销的操作
 */
public class LoginManager {
	
	public static final String LOGIN_TYPE = "login_type";
	public static final int LOGIN_SIXIN = 0;
	public static final int LOGIN_RENREN = 1;
	public LoginInfo mLoginInfo = new LoginInfo();
	
	private static LoginManager mManager = new LoginManager();
	private List<LoginListener> mLoginListeners = new LinkedList<LoginListener>();
	private LoginfoRestListener mLoginfoListener;
	
	public void setLoginfo(LoginInfo loginfo){
		this.mLoginInfo = loginfo;
	}
	protected LoginManager() {
		clean();
	}
	public static LoginManager getInstance() {
		return mManager;
	}

	
	public void setLoginfoListener(LoginfoRestListener loginfoListener){
		this.mLoginfoListener = loginfoListener;
	}
	
	/**
	 * 登陆
	 */
	private void login(String account, String pwd, int captcha_needed, String captcha, long session, INetResponse response){
		McsServiceProvider.getProvider().login(account, pwd, captcha_needed, captcha, session, new ResponseProxy(response));
	}
	
	/**
	 * @author kaining.yang
	 * @param account
	 * @param pwd
	 * @param session
	 * @param response
	 * 用 人人帐号 或 私信帐号 登陆
	 */
	private void login(int accountType, String account, String pwd, String captcha, long session,INetResponse response){
		
		// kaining.yang
		if (LOGIN_SIXIN == accountType) {
			McsServiceProvider.getProvider().loginSiXin(account, pwd, session, LoginManager.getInstance().getLoginInfo().mCaptchaNeeded, captcha, new ResponseProxy(response));
		} else if (LOGIN_RENREN == accountType) {
			McsServiceProvider.getProvider().loginRenRen(account, pwd, session, LoginManager.getInstance().getLoginInfo().mCaptchaNeeded, captcha, new ResponseProxy(response));
		}
		
	}
	
	
	class ResponseProxy extends  INetReponseAdapter{
		public INetResponse mResponse = null;
		public ResponseProxy(INetResponse r){
			mResponse = r;
		}
		@Override
		public void onSuccess(INetRequest req, JsonObject data) {
			mResponse.response(req, data);
		}
		@Override
		public void onError(INetRequest req, JsonObject data) {
			mResponse.response(req, data);
		}
	}
	
	/**
	 * 私信用户登陆
	 * @param account
	 * @param pwd
	 * @param session
	 * @param listener
	 */
	public void login(String account, String pwd, int captcha_needed, String captcha, long session, LoginStatusListener listener) {
		this.login(account, pwd, captcha_needed, captcha, session, new LoginResponse(account, pwd, listener));
	}
	
	public void login(int accountType, String account, String pwd, String captcha, long session, LoginStatusListener listener) {
		this.login(accountType, account, pwd, captcha, session,new LoginResponse(account, pwd, listener));
	}
	
	public void register(String username, String password, String captcha, int sex, String realname, LoginStatusListener listener) {
		McsServiceProvider.getProvider().register(username, password, captcha, sex, realname, new ResponseProxy(new RegisterResponse(username, password, listener)));
	}
	
	/**
	 * 获取登录信息，用于票失效
	 * @param account
	 * @param pwd
	 * @param listener
	 */
	public void getLoginInfo(String account, String pwd, LoginStatusListener listener){
		McsServiceProvider.getProvider().getLoginInfo(new LoginResponse(account, pwd, listener));
	}
	
	// Register
	public static class RegisterResponse extends INetReponseAdapter{
		LoginStatusListener mListener = null;
		String mAccount = null;
		String mPassword = null;
		public RegisterResponse(LoginStatusListener listener){
			mListener = listener;
		}
		public RegisterResponse(String account,String password,LoginStatusListener listener){
			mListener = listener;
			this.mAccount=account;
			this.mPassword=password;
		}
		
		@Override
		public void onSuccess(INetRequest req, JsonObject data) {
			CommonUtil.log("cdw", "登陆成功:"+data);
			CommonUtil.log("cdw", "回调"+mListener);
			mListener.onLoginSuccess(data,req.getCurrentSession());
		}
		
		@Override
		public void onError(INetRequest req, JsonObject data) {
			long error_code = data.getNum("error_code");
			String error_msg = data.getString("error_msg");
			mListener.onLoginFailed(data,req.getCurrentSession());
		}
	}

	// Login
	public static class LoginResponse extends INetReponseAdapter{
		LoginStatusListener mListener = null;
		String mAccount = null;
		String mPassword = null;
		public LoginResponse(LoginStatusListener listener){
			mListener = listener;
		}
		public LoginResponse(String account,String password,LoginStatusListener listener){
			mListener = listener;
			this.mAccount=account;
			this.mPassword=password;
		}
		
		@Override
		public void onSuccess(INetRequest req, JsonObject data) {
			CommonUtil.log("cdw", "登陆成功:"+data);
			CommonUtil.log("cdw", "回调"+mListener);
			mListener.onLoginSuccess(data,req.getCurrentSession());
		}
		
		@Override
		public void onError(INetRequest req, JsonObject data) {
			long error_code = data.getNum("error_code");
			String error_msg = data.getString("error_msg");
			mListener.onLoginFailed(data,req.getCurrentSession());
		}
	}
	
	/**
	 * 注销及跳转
	 */
	public void logout() {
		clean();
		this.notifyLogout();
		AbstractRenrenApplication.USER_ID = -1;
		AbstractRenrenApplication.sIsLogin = false;
		Utils.l("USER_ID:" + AbstractRenrenApplication.USER_ID);
	}
	
	public void clean(){
		mLoginInfo = new LoginInfo();
	}

	public void notifyLogout(){
		for(LoginListener listener : mLoginListeners){
			listener.onLogout();
		}
	}
	
	public void notifyLogin(){
		if(mLoginInfo==null && mLoginInfo.mSecretKey!=null){
			return;
		}
		for(LoginListener listener : mLoginListeners){
			listener.onLogin(mLoginInfo);
		}
	}
		
	public void registorLoginListener(LoginListener listener){
		if(listener==null||mLoginListeners.contains(listener)){
			return;
		}else{
			mLoginListeners.add(listener);
			if(mLoginInfo!=null && mLoginInfo.mSecretKey!=null){
				listener.onLogin(mLoginInfo);
			}
		}
	}
	
	
	public static interface LoginListener{
		public void onLogin(LoginInfo info);
		public void onLogout();
	}
	

	/**
	 * 处理登陆结果的接口
	 * 
	 * @author he.cao
	 * 
	 */
	public interface LoginStatusListener {

		/**
		 * 登陆请求返回
		 */
		public void onLoginResponse();

		/**
		 * 登陆成功
		 */
		public void onLoginSuccess(JsonObject data,long session);

		/**
		 * 登陆失败
		 */
		public void onLoginFailed(JsonObject data, long session);
		
	}
	
	// 观察者
	public interface BindInfoObserver {
		public void update();
	}
	
	public static class LoginInfo {
		
		public String mAccount = null;
		public String mPassword = null;
		public String mSessionKey = null;
		public String mSecretKey = null;//
		public String mHeadUrl = null;
		public long  mUserId = 0l;
		public String mUserName = null;
		public String mTicket = null;
		
		public String mLargeUrl = null;
		public String mMediumUrl = null;
		public String mOriginal_Url = null;
		public String mBindInfo = null;
		
		public int mAutoLogin = 0;
		public int mLastLogin = 0;
		
		public int mGender = 0;
		// birthday_display
		public String mBirthdayDisPlay = null; 
		// birthday
		public String mBirthday = null;
		 
		public String mSchool = null;
		public int mPrivate = -1;
		public String mDomainName = null;
		
		// 未存数据库
		public boolean mIsLogin = false;
		public String mPasswordToken = null;
		public String mLastAccount;
		
		// 登录验证码相关
		public int mCaptchaNeeded = 0; // 1：支持验证码
		public String mCaptchaUrl = "";
		
		/**
		 * 是否是第一次登陆
		 * 用户是否首次登录。server根据登录次数进行判断。0:首次登录。
		 */
		public int mIsFirstLogin = -1;
		/**
		 * 是否设置密码:
		 * <p>1：无密码  0：有密码
		 */
		public int mFillStage = -1;
		
		public BindInfo mBindInfoRenren = new BindInfo();
		public BindInfo mBindInfoMobile = new BindInfo();
		public BindInfo mBindInfoEmail = new BindInfo();
		
		// add by shichao.song 国际版使用
		public String mFirstName = null;
		public String mLastName = null;
		
		public LoginInfo() {
			
		}
		
		// BindInfo 生成 kaining.yang
		public void generateBindInfo() {
			StringBuffer bindInfoStr = new StringBuffer();
			// json
			/**
			 * {"bind_info":
			 * [{"bind_id":"18211111111","type":"mobile","name":"","page":"主页"},
			 *  {"bind_id":"18211111111","type":"renren","name":"","page":"主页"},
			 *  {"bind_id":"18211111111","type":"email","name":"","page":"主页"}]}
			 */
			bindInfoStr.append("{\"bind_info\":[");
			if (mBindInfoRenren != null) {
				String bindrenren = "{\"bind_id\":\"" + mBindInfoRenren.mBindId + 
									"\",\"type\":\"" + mBindInfoRenren.mBindType + 
									"\",\"name\":\"" + mBindInfoRenren.mBindName + 
									"\",\"page\":\"" + mBindInfoRenren.mBindPage + 
									"\",\"medium_url\":\"" + mBindInfoRenren.mBindMediumUrl + "\"}";
				bindInfoStr.append(bindrenren);
				bindInfoStr.append(",");
			}
			if (mBindInfoMobile != null) {
				String bindmobile = "{\"bind_id\":\"" + mBindInfoMobile.mBindId + 
									"\",\"type\":\"" + mBindInfoMobile.mBindType + 
									"\",\"name\":\"" + mBindInfoMobile.mBindName + 
									"\",\"page\":\"" + mBindInfoMobile.mBindPage + 
									"\",\"medium_url\":\"" + mBindInfoMobile.mBindMediumUrl + "\"}";
				bindInfoStr.append(bindmobile);
				bindInfoStr.append(",");
			}
			if (mBindInfoEmail != null) {
				String bindemail = "{\"bind_id\":\"" + mBindInfoEmail.mBindId + 
									"\",\"type\":\"" + mBindInfoEmail.mBindType + 
									"\",\"name\":\"" + mBindInfoEmail.mBindName + 
									"\",\"page\":\"" + mBindInfoEmail.mBindPage + 
									"\",\"medium_url\":\"" + mBindInfoEmail.mBindMediumUrl + "\"}";
				bindInfoStr.append(bindemail);
				bindInfoStr.append(",");
			}
			if(bindInfoStr.charAt(bindInfoStr.length() - 1) == ',') {
				bindInfoStr.deleteCharAt(bindInfoStr.length() - 1);
			}
			bindInfoStr.append("]}");
			mBindInfo = bindInfoStr.toString();
		}
		
		/**
		 * 18211111111
			{
				"session_key":"81NYbreu22hh8tE1",
				"ticket":"ticket",
				"user_id":100001390,
				"secret_key":"d0dbdbaa72de44d9063e268f29fbc5d7",
				"user_name":"Name 100001390",
				"head_url":{
					"medium_url":"http://www.qiqu5.com/wp-content/uploads/2010/03/119.jpg",
					"large_url":"http://t2.baidu.com/it/u=3925358291,1529856573&fm=52&gp=0.jpg",
					"original_rul":"http://t3.baidu.com/it/u=295144277,3149792241&fm=51&gp=0.jpg"},
				"now":1345171798122,
				"login_count":0,
				"bind_info":[{"bind_id":"18211111111","type":"mobile","name":"","page":"主页"},{"bind_id":"18211111111","type":"mobile","name":"","page":"主页"},{"bind_id":"18211111111","type":"mobile","name":"","page":"主页"}]
			}
		 */
		// BindInfo 解析 kaining.yang
		public void parseBindInfo() {
			if (!TextUtils.isEmpty(mBindInfo)) {
				JsonObject joo = (JsonObject) JsonParser.parse(mBindInfo);
				JsonArray mja = joo.getJsonArray("bind_info");
				for (int i = 0; i < mja.size(); i ++) {
					JsonObject jo = (JsonObject) mja.get(i);
					BindInfo bind = new BindInfo();
					bind.mBindId = jo.getString("bind_id");
					bind.mBindType = jo.getString("type");
					bind.mBindName = jo.getString("name");
					bind.mBindPage = jo.getString("page");
					bind.mBindMediumUrl = jo.getString("medium_url");
					if (bind.mBindType.equals("renren")) {
						mBindInfoRenren = bind;
					} else if (bind.mBindType.equals("mobile")) {
						mBindInfoMobile = bind;
					} else if (bind.mBindType.equals("email")) {
						mBindInfoEmail = bind;
					}
				}
			}
		}
		
		public String toString(){
			StringBuilder sb = new StringBuilder();
			sb.append("mAccount:").append(this.mAccount).append('\n');
			sb.append("mHeadUrl:").append(this.mHeadUrl).append('\n');
			sb.append("mPassword:").append(this.mPassword).append('\n');
			sb.append("mSecretKey:").append(this.mSecretKey).append('\n');
			sb.append("mSessionKey:").append(this.mSessionKey).append('\n');
			sb.append("mTicket:").append(this.mTicket).append('\n');
			sb.append("mUserId:").append(this.mUserId).append('\n');
			sb.append("mUserName:").append(this.mUserName).append('\n');
			sb.append("mLargeUrl:").append(this.mLargeUrl).append('\n');
			sb.append("mMediumUrl:").append(this.mMediumUrl).append('\n');
			sb.append("mOriginal_Url:").append(this.mOriginal_Url).append('\n');
			sb.append("mBindInfo:").append(this.mBindInfo).append('\n');
			sb.append("mAutoLogin:").append(this.mAutoLogin).append('\n');
			sb.append("mLastLogin:").append(this.mLastLogin).append('\n');
			sb.append("mGender:").append(this.mGender).append('\n');
			sb.append("mBirthday:").append(this.mBirthday).append('\n');
			sb.append("mBirthdayDisplay:").append(this.mBirthdayDisPlay).append('\n');
			sb.append("mSchool:").append(this.mSchool).append('\n');
			sb.append("mPrivate:").append(this.mPrivate).append('\n');
			sb.append("mDomainName:").append(this.mDomainName).append('\n');
			sb.append("mFirstName:").append(this.mFirstName).append('\n');
			sb.append("mLastName:").append(this.mLastName).append('\n');
			sb.append("mIsFirstLogin:").append(this.mIsFirstLogin).append('\n');
			sb.append("mFillStage:").append(this.mFillStage).append('\n');
			sb.append("mPasswordToken:").append(this.mPasswordToken).append('\n');
			return sb.toString();
		}
	}


	public LoginInfo getLoginInfo() {
		isLogout();
		return mLoginInfo;
	}
	public void getLoginInfo(LoginStatusListener listener){
		McsServiceProvider.getProvider().getLoginInfo(new LoginResponse(listener));
	}
	public String getSsssionKey() {
		isLogout();
		if(mLoginInfo!=null) {
			return mLoginInfo.mSessionKey;
		}
		return null;
	}
	
	public String getSecretKey() {
		isLogout();
		if(mLoginInfo!=null){
			return mLoginInfo.mSecretKey;
		}
		return null;
	}
	
	public boolean isLogout() {
		if(mLoginInfo==null || TextUtils.isEmpty(mLoginInfo.mSessionKey)){
			this.mLoginfoListener.reset();
		}
		return mLoginInfo==null || TextUtils.isEmpty(mLoginInfo.mSessionKey) || TextUtils.isEmpty(mLoginInfo.mSecretKey);	
	}
	
	
	public boolean isLogoutWithoutReset() {
//		Utils.l("USER_ID:" + AbstractRenrenApplication.USER_ID);
//		return AbstractRenrenApplication.USER_ID <= 0 || !AbstractRenrenApplication.sIsLogin;
		return isLogout();
	}
	
	public void reset(){
		this.mLoginfoListener.reset();
	}
	
	public void initLoginInfo(LoginInfo li){
		if(li!=null){
			mLoginInfo.mAccount = li.mAccount;
			mLoginInfo.mHeadUrl = li.mHeadUrl;
			mLoginInfo.mPassword = li.mPassword;
			mLoginInfo.mSecretKey = li.mSecretKey;
			mLoginInfo.mSessionKey = li.mSessionKey;
			mLoginInfo.mTicket = li.mTicket;
			mLoginInfo.mUserName = li.mUserName;
			mLoginInfo.mUserId = li.mUserId;
			mLoginInfo.mLargeUrl = li.mLargeUrl;
			mLoginInfo.mMediumUrl = li.mMediumUrl;
			mLoginInfo.mOriginal_Url = li.mOriginal_Url;
			mLoginInfo.mBindInfo = li.mBindInfo;
			mLoginInfo.mAutoLogin = li.mAutoLogin;
			mLoginInfo.mLastLogin = li.mLastLogin;
			mLoginInfo.mGender = li.mGender;
			mLoginInfo.mBirthday = li.mBirthday;
			mLoginInfo.mBirthdayDisPlay = li.mBirthdayDisPlay;
			mLoginInfo.mSchool = li.mSchool;
			mLoginInfo.mPrivate = li.mPrivate;
			mLoginInfo.mDomainName = li.mDomainName;
			mLoginInfo.mIsFirstLogin = li.mIsFirstLogin;
			mLoginInfo.mFillStage = li.mFillStage;
			
			mLoginInfo.mBindInfoEmail = li.mBindInfoEmail;
			mLoginInfo.mBindInfoMobile = li.mBindInfoMobile;
			mLoginInfo.mBindInfoRenren = li.mBindInfoRenren;
			
			// add by shichao.song 国际版使用
			mLoginInfo.mFirstName = li.mFirstName;
			mLoginInfo.mLastName = li.mLastName;
			
//			McsServiceProvider.getProvider().setSessionKey(mLoginInfo.mSessionKey);
			this.notifyLogin();
		}
	}
}
