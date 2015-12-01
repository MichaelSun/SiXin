package com.renren.mobile.account;

import com.common.manager.LoginManager.LoginInfo;
import com.core.orm.ORM;
import com.renren.mobile.chat.database.Account_Column;

public class LoginfoModel {
	
	public LoginfoModel () {
		
	}
	
	@ORM(mappingColumn=Account_Column.LOGIN_ACCOUNT)
	public String mAccount = null;
	
	@ORM(mappingColumn=Account_Column.LOGIN_PASSWORD)
	public String mPassword = null;
	
	@ORM(mappingColumn=Account_Column.LOGIN_SESSIONKEY)
	public String mSessionKey = null;
	
	@ORM(mappingColumn=Account_Column.LOGIN_SECRETKEY)
	public String mSecretKey = null;
	
	@ORM(mappingColumn=Account_Column.LOGIN_HEADURL)
	public String mHeadUrl = null;
	
	@ORM(mappingColumn=Account_Column.LOGIN_USERID)
	public long mUserId = 0l;
	
	@ORM(mappingColumn=Account_Column.LOGIN_USERNAME)
	public String mUserName = null;
	
	@ORM(mappingColumn=Account_Column.LOGIN_TICKET)
	public String mTicket = null;
	
	@ORM(mappingColumn=Account_Column.LOGIN_HEAD_LARGE)
	public String mLargeUrl = null;
	
	@ORM(mappingColumn=Account_Column.LOGIN_HEAD_MEDIUM)
	public String mMediumUrl = null;
	
	@ORM(mappingColumn=Account_Column.LOGIN_HEAD_ORIGINAL)
	public String mOriginal_Url = null;
	
	@ORM(mappingColumn=Account_Column.BIND_INFO)
	public String mBindInfo = null;
	
	@ORM(mappingColumn=Account_Column.IS_AUTO_LOGIN)
	public int mAutoLogin = 0;
	
	@ORM(mappingColumn=Account_Column.IS_LAST_LOGIN)
	public int mLastLogin = 0;
	
	@ORM(mappingColumn=Account_Column.PROFILE_GENDER)
	public int mGender = 0;
	
	@ORM(mappingColumn=Account_Column.PROFILE_BIRTHDAY)
	public String mBirthday = null;
	
	@ORM(mappingColumn=Account_Column.PROFILE_SCHOOL)
	public String mSchool = null;
	
	@ORM(mappingColumn=Account_Column.PROFILE_PRIVATE)
	public int mPrivate = -1;
	
	@ORM(mappingColumn=Account_Column.DOMAIN_NAME)
	public String mDomainName = null;
	
	public void parse(LoginInfo loginfo) {
		loginfo.generateBindInfo();
		this.mAccount = loginfo.mAccount;
		this.mHeadUrl = loginfo.mHeadUrl;
		this.mPassword = loginfo.mPassword;
		this.mSecretKey = loginfo.mSecretKey;
		this.mSessionKey = loginfo.mSessionKey;
		this.mTicket = loginfo.mTicket;
		this.mUserId = loginfo.mUserId;
		this.mUserName = loginfo.mUserName;
		this.mLargeUrl = loginfo.mLargeUrl;
		this.mMediumUrl = loginfo.mMediumUrl;
		this.mOriginal_Url = loginfo.mOriginal_Url;
		this.mBindInfo = loginfo.mBindInfo;
		this.mAutoLogin = loginfo.mAutoLogin;
		this.mLastLogin = loginfo.mLastLogin;
		this.mGender = loginfo.mGender;
		this.mBirthday = loginfo.mBirthday;
		this.mSchool = loginfo.mSchool;
		this.mPrivate = loginfo.mPrivate;
		this.mDomainName = loginfo.mDomainName;
	}
	
	public LoginInfo getLoginfo() {
		LoginInfo loginfo= new LoginInfo();
		loginfo.mAccount = this.mAccount;
		loginfo.mHeadUrl = this.mHeadUrl;
		loginfo.mPassword = this.mPassword;
		loginfo.mSecretKey = this.mSecretKey;
		loginfo.mSessionKey = this.mSessionKey;
		loginfo.mTicket = this.mTicket;
		loginfo.mUserId = this.mUserId;
		loginfo.mUserName = this.mUserName;
		loginfo.mLargeUrl = this.mLargeUrl;
		loginfo.mMediumUrl = this.mMediumUrl;
		loginfo.mOriginal_Url = this.mOriginal_Url;
		loginfo.mBindInfo = this.mBindInfo;
		loginfo.mAutoLogin = this.mAutoLogin;
		loginfo.mLastLogin = this.mLastLogin;
		loginfo.mGender = this.mGender;
		loginfo.mBirthday = this.mBirthday;
		loginfo.mSchool = this.mSchool;
		loginfo.mPrivate = this.mPrivate;
		loginfo.mDomainName = this.mDomainName;
		loginfo.parseBindInfo();
		return loginfo;
	}
	
	public String toString() {
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
		sb.append("mSchool:").append(this.mSchool).append('\n');
		sb.append("mPrivate:").append(this.mPrivate).append('\n');
		sb.append("mDomainName:").append(this.mDomainName).append('\n');
		return sb.toString();
	}
}
