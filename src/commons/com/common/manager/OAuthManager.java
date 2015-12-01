package com.common.manager;

import com.common.mcs.INetResponse;
import com.common.mcs.McsServiceProvider;
import com.core.json.JsonObject;

/**
 * 验证第三方登陆
 * @author shichao.song
 * 2012-7-24
 *
 */
public class OAuthManager {
	//TODO 每次调用接口的时候，要先判断用户是否授予了相应的权限
	private static OAuthManager oAuthManagerInstance;
	
	public OAuthManager(){
		
	}
	
	public static OAuthManager getOAuthManager(){
		if(oAuthManagerInstance == null){
			oAuthManagerInstance = new OAuthManager();
		}
		return oAuthManagerInstance;
	}
	
	/**
	 * 获取权限列表
	 * @param permissions
	 * @param response
	 * @param batch
	 */
	public void getPermission(String[] permissions, INetResponse response, boolean batch){
		McsServiceProvider.getProvider().getPermissions(permissions, response, batch);
	}
	
	/**
	 * 上传access_token,并且返回用户绑定的信息
	 * @param third_party
	 * @param access_token
	 * @param expires
	 * @param user_id
	 * @param email
	 * @param response
	 * @param batch
	 */
	public void uploadAccessToken(String third_party, String access_token, long expires, String user_id, String email, INetResponse response, boolean batch){
		McsServiceProvider.getProvider().uploadAccessToken(third_party, access_token, expires, user_id, email, response, batch);
	}
	
	/**
	 * 获取第三方账号的个人信息
	 * @param access_token
	 * @param response
	 * @param batch
	 */
	public void getMeInfo(String access_token, INetResponse response, boolean batch){
		McsServiceProvider.getProvider().getOauthPersonInfo(access_token, response, batch);
	}
	
	/**
	 * 获取第三方的朋友列表
	 */
	public void getThirdFriendsList(){
		McsServiceProvider.getProvider().getThirdFriendsList(null,"","","",0,0);
	}
	
	/**
	 * 当用户access token失效的情况下，客户端重新上传该用户的access token
	 * @param session_key 第三方帐号类型：facebook，建议小写
	 * @param access_token
	 * @param expires 单位为秒的过期时间(facebook为60天)
	 * @param response
	 * @param batch
	 * @return
	 */
	public void refreshAccessToken(String session_key, String access_token, long expires, INetResponse response, boolean batch){
		McsServiceProvider.getProvider().refreshAccessToken(session_key, access_token, expires, response, batch);
	}
	
	/**
	 * 用私信账号登陆后，绑定第三方账户
	 * @param access_token
	 * @param expires
	 * @param session_key
	 */
	public void bindThirdParty(String access_token, long expires, String session_key, INetResponse response){
		McsServiceProvider.getProvider().bindThirdParty(access_token, expires, session_key, response);
	}
	
	/**
	 * 解绑第三方账号
	 * @param session_key 当前登陆私信用户的session_key
	 * @param third_party_type 第三方账户类型，如：facebook,twitter
	 * @param third_party_uid 第三方账户id
	 * @param response
	 */
	public void removeThirdParty(String session_key, String third_party_type, String third_party_uid, INetResponse response){
		McsServiceProvider.getProvider().removeThireParty(session_key, third_party_type, third_party_uid, response);
	}
	
	/**
	 * @see com.renren.mobile.account.ThirdInfo
	 * @author shichao.song
	 *
	 */
	public static class ThirdInfo {
		public ThirdInfo(){
			
		}
		
		public String mLoginThird = null;
		public String mSixinId = null;
		public String mAccessToken = null;
		public long mExpires = 01;
		public String mUserName = null;
		public String mUserId = null;
		
		public String toString(){
			StringBuilder sb = new StringBuilder();
			sb.append("mLoginThird:").append(this.mLoginThird).append('\n');
			sb.append("mAppId:").append(this.mSixinId).append('\n');
			sb.append("mAccessToken:").append(this.mAccessToken).append('\n');
			sb.append("mExpires:").append(this.mExpires).append('\n');
			sb.append("mUserName:").append(this.mUserName).append('\n');
			sb.append("mUserId:").append(this.mUserId).append('\n');
			return sb.toString();
		}
		
		
	}
	
	
}
