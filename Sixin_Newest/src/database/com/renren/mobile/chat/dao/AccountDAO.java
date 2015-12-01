package com.renren.mobile.chat.dao;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.common.manager.LoginManager;
import com.common.manager.LoginManager.LoginInfo;
import com.common.utils.RRSharedPreferences;
import com.core.database.BaseDAO;
import com.core.database.BaseDBTable;
import com.core.database.Query;
import com.core.json.JsonObject;
import com.core.json.JsonParser;
import com.core.orm.ORMUtil;
import com.renren.mobile.account.LoginControlCenter;
import com.renren.mobile.account.LoginfoModel;
import com.renren.mobile.chat.RenrenChatApplication;
import com.renren.mobile.chat.base.util.FileUtil;
import com.renren.mobile.chat.database.Account_Column;
import com.renren.mobile.chat.sql.Query_Account;
import com.sixin.proxy.RenrenClientProxy;

/**
 * @author dingwei.chen
 * @说明 账户DAO 
 * @说明 采用不同的数据存储方式(文件存储方法)
 * @说明 文件存储明文有高度的不可靠性因此要对数据文件进行加密处理	
 * @加密方式:
 * 		对存入文件进行取反操作
 * <b>数据模型</b>
 * @see com.renren.mobile.chat.LoginInfo
 * */
public class AccountDAO extends BaseDAO {

	private static final String LOGINED_ACCOUNTS = "usernames";
	
    Query mQuery_Account = new Query_Account(this);
	
	public AccountDAO(BaseDBTable table) {
		super(table);
	}

	// kaining.yang
	public synchronized LoginfoModel loadPreUserData() {
		LoginfoModel info = null;
		info = loadAutoLoginUserData();
		RRSharedPreferences rrSharedPreferences = new RRSharedPreferences(RenrenChatApplication.mContext, "login_authorize");
		if(info == null) {
			// 本地的帐号中没有 auto_login == 1 的账户，并且检测到本地官方客户端有登陆账户
			// info = readLoginMessageFromRenren(RenrenChatApplication.getAppContext());
			
			LoginfoModel loginfo = readLoginMessageFromRenren(RenrenChatApplication.getAppContext());
			
			if(loginfo != null){
				// 标志人人授权登陆 1 授权登陆页面
				LoginManager.getInstance().getLoginInfo().mAccount = loginfo.mAccount;
				LoginManager.getInstance().getLoginInfo().mPassword = loginfo.mPassword;
				LoginManager.getInstance().getLoginInfo().mHeadUrl = loginfo.mHeadUrl;
				LoginManager.getInstance().getLoginInfo().mUserName = loginfo.mUserName;
				LoginManager.getInstance().getLoginInfo().mUserId = loginfo.mUserId;
				rrSharedPreferences.putIntValue("renren_authorize", 1);
			} else {
				rrSharedPreferences.putIntValue("renren_authorize", 0);
			}
		} else {
			// 标志人人授权登陆 0 自动登陆
			rrSharedPreferences.putIntValue("renren_authorize", 0);
		}
		return info;
    }
	
	
	
	public synchronized LoginfoModel loadAutoLoginUserData() {
		ArrayList<LoginfoModel> infos = getAllAccountInfoDB();
		for(int i = 0; infos != null && i < infos.size(); i ++) {
			if (infos.get(i).mAutoLogin == 1) {
				return infos.get(i);
			}
		}
		return null;
	}
	
	public synchronized LoginfoModel loadLastLoginUserData() {
		ArrayList<LoginfoModel> infos = getAllAccountInfoDB();
		for(int i = 0; infos != null && i < infos.size() ; i ++) {
			if (infos.get(i).mLastLogin == 1) {
				return infos.get(i);
			}
		}
		return null;
	}
	
	
	// 保存新登陆的账户
	public synchronized void saveUserData(String account,String password_md5,JsonObject object){
		LoginfoModel info = LoginControlCenter.getInstance().parseLoginInfo(object);
		info.mAccount = account;
		info.mPassword = password_md5;
		// 新登陆的账户为自动登陆和最后登陆帐号
		info.mAutoLogin = 1;
		info.mLastLogin = 0;
		this.saveUserData(info);
	}
	
	/**
	 * 将所有用户信息更新后保存到数据库中
	 * 新加本次登陆info，修改历史登陆账户的info
	 * 保存到数据库
	 * @param info
	 */
	public synchronized void saveUserData(LoginfoModel info){
		ArrayList<LoginfoModel> infos = getAllAccountInfoDB();
		for (int i = 0; infos != null && i < infos.size(); i ++) {
			if (infos.get(i).mAccount != null && infos.get(i).mAccount.equals(info.mAccount)) {
				infos.remove(i);
				// test
				i --;
			}
		}
		for (int i = 0; infos != null && i < infos.size(); i ++) {
			infos.get(i).mAutoLogin = 0;
			// infos.get(i).mLastLogin = 0;
			infos.get(i).mPassword = "";
		}
		if (infos == null) {
			infos = new ArrayList<LoginfoModel>();
		}
		if (info != null && !TextUtils.isEmpty(info.mAccount)) {
			infos.add(info);
		}
		saveAccountInfoDB(infos); // 将用户信息存储到DB数据库中
		//this.saveLoginedAccount(info.mAccount); // 保存账户到文件
	}
	
	
	public synchronized void saveUserData(String json_String){
		try{
			JsonObject object = (JsonObject)JsonParser.parse(json_String);
			LoginfoModel info = LoginControlCenter.getInstance().parseLoginInfo(object);
			saveAccountInfoDB(info);
		}catch(Exception e){
			
		}
	}
	
	public synchronized void saveUserLogoutInfo(String account) {
		JsonObject object = new JsonObject();
		object.put(LoginControlCenter.NEED_PARAMS.ACCOUNT, account);
		clearAccountInfoDB();//清空账号信息
		//this.writeToAccountFile(object.toJsonString());
	}
	
	// 注销时更新数据库账户
	public synchronized void updateUserInfoLogout(LoginInfo info) {
		ArrayList<LoginfoModel> infos = getAllAccountInfoDB();
		for (int i = 0; infos != null && i < infos.size(); i ++) {
			if (infos.get(i).mAccount.equals(info.mAccount)) {
				infos.remove(i);
				// test
				i --;
			}
		}
		for (int i = 0; infos != null && i < infos.size(); i ++) {
			infos.get(i).mPassword = "";
			infos.get(i).mAutoLogin = 0;
			infos.get(i).mLastLogin = 0;
		}
		info.mPassword = "";
		info.mAutoLogin = 0;
		info.mLastLogin = 1;
		LoginfoModel loginfo = new LoginfoModel();
		loginfo.parse(info);
		if (infos == null) {
			infos = new ArrayList<LoginfoModel>();
		}
		if (loginfo != null && !TextUtils.isEmpty(loginfo.mAccount)) {
			infos.add(loginfo);
		}
		saveAccountInfoDB(infos);
	}
	
	/**
	 * @author kaining.yang
	 * @param info 
	 */
	public synchronized void saveAccountInfoDB(LoginfoModel info) {
		// 不删除历史登陆用户信息
		deleteAccount(null);
		
		if (info == null || TextUtils.isEmpty(info.mAccount)) {
			return;
		}
		
		ContentValues values = new ContentValues();
		values.put(Account_Column.LOGIN_USERID, info.mUserId);
		values.put(Account_Column.LOGIN_PASSWORD, info.mPassword);
		values.put(Account_Column.LOGIN_SESSIONKEY, info.mSessionKey);
		values.put(Account_Column.LOGIN_SECRETKEY, info.mSecretKey);
		values.put(Account_Column.LOGIN_TICKET, info.mTicket);
		values.put(Account_Column.LOGIN_HEADURL, info.mHeadUrl);
		values.put(Account_Column.LOGIN_USERNAME, info.mUserName);
		values.put(Account_Column.LOGIN_ACCOUNT, info.mAccount);
		values.put(Account_Column.LOGIN_HEAD_LARGE, info.mHeadUrl);
		values.put(Account_Column.LOGIN_HEAD_MEDIUM, info.mMediumUrl);
		values.put(Account_Column.LOGIN_HEAD_ORIGINAL, info.mOriginal_Url);
		values.put(Account_Column.BIND_INFO, info.mBindInfo);
		values.put(Account_Column.IS_AUTO_LOGIN, info.mAutoLogin);
		values.put(Account_Column.IS_LAST_LOGIN, info.mLastLogin);
		values.put(Account_Column.PROFILE_GENDER, info.mGender);
		values.put(Account_Column.PROFILE_BIRTHDAY, info.mBirthday);
		values.put(Account_Column.PROFILE_SCHOOL, info.mSchool);
		//values.put(Account_Column.PROFILE_PRIVATE, info.mPrivate);
		ORMUtil.getInstance().ormInsert(LoginfoModel.class, info, values);
		mInsert.insert(values);
	}
	
	/**
	 * 将本次登陆账户和历史账户一并存入数据库
	 * @author kaining.yang
	 * @param infos
	 */
	public synchronized void saveAccountInfoDB(ArrayList<LoginfoModel> infos) {
		// 不删除历史登陆用户信息
		deleteAccount(null);
		
		for (int i = 0; i < infos.size(); i ++) {
			
			if (infos.get(i) == null || TextUtils.isEmpty(infos.get(i).mAccount)) {
				continue;
			}
			ContentValues values = new ContentValues();
			values.put(Account_Column.LOGIN_USERID, infos.get(i).mUserId);
			values.put(Account_Column.LOGIN_PASSWORD, infos.get(i).mPassword);
			values.put(Account_Column.LOGIN_SESSIONKEY, infos.get(i).mSessionKey);
			values.put(Account_Column.LOGIN_SECRETKEY, infos.get(i).mSecretKey);
			values.put(Account_Column.LOGIN_TICKET, infos.get(i).mTicket);
			values.put(Account_Column.LOGIN_HEADURL, infos.get(i).mHeadUrl);
			values.put(Account_Column.LOGIN_USERNAME, infos.get(i).mUserName);
			values.put(Account_Column.LOGIN_ACCOUNT, infos.get(i).mAccount);
			values.put(Account_Column.LOGIN_HEAD_LARGE, infos.get(i).mHeadUrl);
			values.put(Account_Column.LOGIN_HEAD_MEDIUM, infos.get(i).mMediumUrl);
			values.put(Account_Column.LOGIN_HEAD_ORIGINAL, infos.get(i).mOriginal_Url);
			values.put(Account_Column.BIND_INFO, infos.get(i).mBindInfo);
			values.put(Account_Column.IS_AUTO_LOGIN, infos.get(i).mAutoLogin);
			values.put(Account_Column.IS_LAST_LOGIN, infos.get(i).mLastLogin);
			values.put(Account_Column.PROFILE_GENDER, infos.get(i).mGender);
			values.put(Account_Column.PROFILE_BIRTHDAY, infos.get(i).mBirthday);
			values.put(Account_Column.PROFILE_SCHOOL, infos.get(i).mSchool);
			//values.put(Account_Column.PROFILE_PRIVATE, infos.get(i).mPrivate);
			ORMUtil.getInstance().ormInsert(LoginfoModel.class, infos.get(i), values);
			mInsert.insert(values);
		}
	}
	
	/**
	 * 更新数据库当前帐号的信息
	 * @author kaining.yang
	 * @param infos
	 */
	public synchronized void updateAccountInfoDB(LoginfoModel info) {
		ContentValues values = new ContentValues();
		values.put(Account_Column.LOGIN_USERID, info.mUserId);
		values.put(Account_Column.LOGIN_PASSWORD, info.mPassword);
		values.put(Account_Column.LOGIN_SESSIONKEY, info.mSessionKey);
		values.put(Account_Column.LOGIN_SECRETKEY, info.mSecretKey);
		values.put(Account_Column.LOGIN_TICKET, info.mTicket);
		values.put(Account_Column.LOGIN_HEADURL, info.mHeadUrl);
		values.put(Account_Column.LOGIN_USERNAME, info.mUserName);
		values.put(Account_Column.LOGIN_ACCOUNT, info.mAccount);
		values.put(Account_Column.LOGIN_HEAD_LARGE, info.mLargeUrl);
		values.put(Account_Column.LOGIN_HEAD_MEDIUM, info.mMediumUrl);
		values.put(Account_Column.LOGIN_HEAD_ORIGINAL, info.mOriginal_Url);
		values.put(Account_Column.BIND_INFO, info.mBindInfo);
		values.put(Account_Column.IS_AUTO_LOGIN, info.mAutoLogin);
		values.put(Account_Column.IS_LAST_LOGIN, info.mLastLogin);
		values.put(Account_Column.PROFILE_GENDER, info.mGender);
		values.put(Account_Column.PROFILE_BIRTHDAY, info.mBirthday);
		values.put(Account_Column.PROFILE_SCHOOL, info.mSchool);
		values.put(Account_Column.PROFILE_PRIVATE, info.mPrivate);
		ORMUtil.getInstance().ormUpdate(LoginfoModel.class, info, values);
		String whereString = Account_Column.LOGIN_ACCOUNT + "=" + "'"+ info.mAccount + "'";
		mUpdate.update(values, whereString);
	}
	
	
	public synchronized void deleteAccount(String where){
		mDelete.delete(where);
	}
	
	/**
	 * 更新数据库中的用户信息
	 **/
	public synchronized void clearAccountInfoDB() {
        ContentValues values = new ContentValues();
        values.put(Account_Column.LOGIN_USERID, "");
		values.put(Account_Column.LOGIN_PASSWORD, "");
		values.put(Account_Column.LOGIN_SESSIONKEY, "");
		values.put(Account_Column.LOGIN_SECRETKEY, "");
		values.put(Account_Column.LOGIN_TICKET, "");
		values.put(Account_Column.LOGIN_HEADURL, "");
		values.put(Account_Column.LOGIN_USERNAME, "");
		values.put(Account_Column.LOGIN_HEAD_LARGE, "");
		values.put(Account_Column.LOGIN_HEAD_MEDIUM, "");
		values.put(Account_Column.LOGIN_HEAD_ORIGINAL, "");
		values.put(Account_Column.BIND_INFO, "");
		values.put(Account_Column.IS_AUTO_LOGIN, 0);
		values.put(Account_Column.IS_LAST_LOGIN, 0);
		values.put(Account_Column.PROFILE_GENDER, -1);
		values.put(Account_Column.PROFILE_BIRTHDAY, "");
		values.put(Account_Column.PROFILE_SCHOOL, "");
		//values.put(Account_Column.PROFILE_PRIVATE, -1);
		mUpdate.update(values, null);
	}
	
	/**
	 * 从DB中获取用户信息
	 * */
	/*public LoginfoModel getAccountInfoDB(){
		this.beginTransaction();
		LoginfoModel abm =  mQuery_Account.query(null, null, null, null, null,  LoginfoModel.class);
		Log.v("abm","===================abm:"+abm);
		this.commit();
		return abm;
	}	
	*/
	
	/**
	 * @author kaining.yang
	 * 从DB中获取所有登陆过的用户信息
	 * */
	public synchronized ArrayList<LoginfoModel> getAllAccountInfoDB(){
		ArrayList<LoginfoModel> abm = mQuery_Account.query(null, null, null, null, null,  ArrayList.class);
		return abm;
	}
	
	/**
	 * 获取所有登录过的用户名
	 * @return
	 */
	/*public ArrayList<String> getAllAccounts(){
		try{
			ArrayList<String> list = new ArrayList<String>();
			
			HashMap<String,String> map = getMapAccounts();
			if(map!=null){
				Iterator iter = map.entrySet().iterator();
				while (iter.hasNext()) {
				    Map.Entry entry = (Map.Entry) iter.next();
				    list.add(entry.getKey().toString());
				} 
			}
			return list;
		}catch(Exception e){
			
		}
		return null;
	}*/
	
	public synchronized ArrayList<String>  getAllAccounts(){
		ArrayList<String> accountList = new ArrayList<String>();
		ArrayList<LoginfoModel> infos = getAllAccountInfoDB();
		if (infos != null) {
			for (LoginfoModel info : infos) {
				accountList.add(info.mAccount);
			}
		}
		return accountList;
	}
	
	/*private HashMap<String,String> getMapAccounts(){
		try {
			String path=RenrenChatApplication.getAppContext().getFilesDir().getPath().toString()+"/";
			File f=new File(path+LOGINED_ACCOUNTS);
			
			if(!f.exists()){
				return null;
			}
			
			byte [] tmpNames = FileUtil.getInstance().readBytes(f);
			if(tmpNames==null || tmpNames.length<3){
				return null;
			}
			int len=tmpNames.length;
			char r = '\r';
			char n = '\n';
			
			HashMap<String,String> map=new HashMap<String,String>();
			StringBuilder sb=new StringBuilder();
			
			for(int i=0;i<len;i++){
				if(tmpNames[i]==r){
					
				}else if(tmpNames[i]==n&&tmpNames[i-1]==r){
					map.put(sb.toString(), "");
					sb=new StringBuilder();
				}else{
					sb.append((char)tmpNames[i]);
				}
			}
			if(sb.length()>0){
				map.put(sb.toString(), "");
			}
			return map;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}*/
	
	/**
	 * @author dingwei.chen
	 * 读取官方客户端的账户信息
	 * */
	public synchronized LoginfoModel readLoginMessageFromRenren(Context context){
		try {
			if(RenrenClientProxy.getProxy().isInstall(context)){
				LoginfoModel info = RenrenClientProxy.getProxy().queryLoginInfo(context);
				/*if(info != null){
					this.saveLoginedAccount(info.mAccount);
				}*/
				return  info;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * 保存所有登录过的用户名
	 * @param userName
	 */
	/*private void saveLoginedAccount(String userName){
		try {
			String path=RenrenChatApplication.getAppContext().getFilesDir().getPath().toString()+"/";
			File f=new File (path+LOGINED_ACCOUNTS);
			StringBuilder sb=new StringBuilder();
			char r = '\r';
			char n = '\n';
			
			if(!f.exists()){
				f.createNewFile();
				sb.append(userName).append(r).append(n);
			}else{
				//获取之前的用户名   
				HashMap<String,String> map=getMapAccounts();
				if(map!=null){
					map.put(userName, "");
					Iterator iter = map.entrySet().iterator();
					while (iter.hasNext()) {
					    Map.Entry<String,String> entry = (Map.Entry) iter.next();
					    sb.append(entry.getKey().toString()).append(r).append(n);
					} 
				}
			}
			FileOutputStream fos = new FileOutputStream(f,false);
			fos.write(sb.toString().getBytes());
			fos.close();
			fos = null;
		} catch (Exception e) {
			
		}
	}*/
}
