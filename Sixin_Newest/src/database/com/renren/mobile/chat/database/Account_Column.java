package com.renren.mobile.chat.database;

import com.core.database.Column;
import com.core.database.DatabaseTypeConstant;


/**
 * @author dingwei.chen
 * @说明 账户数据库的列字段
 * @注:不采用数据表的级联存储,采用数据表的冗余存储
 * */
public interface Account_Column{
	@Column(defineType=DatabaseTypeConstant.INT+" "+DatabaseTypeConstant.PRIMARY)
	public static final String _ID = "_id";
	
	@Column(defineType=DatabaseTypeConstant.TEXT)
	public final String LOGIN_ACCOUNT="login_account";//登录账户
	
	@Column(defineType=DatabaseTypeConstant.TEXT)
	public final String LOGIN_PASSWORD="login_password";//登录密码
	
	@Column(defineType=DatabaseTypeConstant.TEXT)
	public final String LOGIN_SESSIONKEY="login_sessionkey";//登录sessionkey
	
	@Column(defineType=DatabaseTypeConstant.TEXT)
	public final String LOGIN_SECRETKEY="login_secretkey";//登录secretkey
	
	@Column(defineType=DatabaseTypeConstant.TEXT)
	public final String LOGIN_HEADURL="login_headurl";//登录headurl
	
	@Column(defineType=DatabaseTypeConstant.INT)
	public final String LOGIN_USERID="login_userid";//登录用户ID
	
	@Column(defineType=DatabaseTypeConstant.TEXT)
	public final String LOGIN_USERNAME="login_username";//登录用户名
	
	@Column(defineType=DatabaseTypeConstant.TEXT)
	public final String LOGIN_TICKET="login_ticket";//webview使用
	
	@Column(defineType=DatabaseTypeConstant.TEXT) 
	public final String LOGIN_HEAD_LARGE = "head_large";
	
	@Column(defineType=DatabaseTypeConstant.TEXT) 
	public final String LOGIN_HEAD_MEDIUM = "head_medium";
	
	@Column(defineType=DatabaseTypeConstant.TEXT) 
	public final String LOGIN_HEAD_ORIGINAL= "head_original";
	
	@Column(defineType=DatabaseTypeConstant.TEXT)
	public final String BIND_INFO= "bind_info";
	
	@Column(defineType=DatabaseTypeConstant.INT)
	public final String IS_AUTO_LOGIN= "is_auto_login";//是否自动登陆
	
	@Column(defineType=DatabaseTypeConstant.INT)
	public final String IS_LAST_LOGIN= "is_last_login";//是否最后一次登陆
	
	@Column(defineType=DatabaseTypeConstant.INT)
	public final String PROFILE_GENDER = "profile_gender";
	
	@Column(defineType=DatabaseTypeConstant.TEXT)
	public final String PROFILE_BIRTHDAY = "profile_birthday";
	
	@Column(defineType=DatabaseTypeConstant.TEXT)
	public final String PROFILE_SCHOOL = "profile_school";
	
	@Column(defineType=DatabaseTypeConstant.INT)
	public final String PROFILE_PRIVATE = "profile_private";
	
	@Column(defineType=DatabaseTypeConstant.TEXT)
	public final String DOMAIN_NAME = "domain_name";
	
}
