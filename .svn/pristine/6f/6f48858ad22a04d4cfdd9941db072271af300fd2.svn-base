package com.common.manager;

import java.io.Serializable;

//kaining.yang
public class BindInfo implements Serializable{
	
	private static final long serialVersionUID = 818063380632674332L;
	public static final String BIND_TYPE_RENREN = "renren";
	public static final String BIND_TYPE_MOBILE = "mobile";
	public static final String BIND_TYPE_EMAIL = "email";
	public static final String BIND_TYPE_FACEBOOK = "facebook";
	
	public String mBindId = "";
	public String mBindType = "";
	public String mBindName = "";
	public String mBindPage = "";
	public String mBindMediumUrl = "";
	
	public BindInfo() {	
	}
	
	public String getmBindId() {
		return mBindId;
	}

	public void setmBindId(String mBindId) {
		this.mBindId = mBindId;
	}

	public String getmBindType() {
		return mBindType;
	}

	public void setmBindType(String mBindType) {
		this.mBindType = mBindType;
	}

	public String getmBindName() {
		return mBindName;
	}

	public void setmBindName(String mBindName) {
		this.mBindName = mBindName;
	}

	public String getmBindPage() {
		return mBindPage;
	}

	public void setmBindPage(String mBindPage) {
		this.mBindPage = mBindPage;
	}
	
	public String getmBindMediumUrl() {
		return mBindMediumUrl;
	}

	public void setmBindMediumUrl(String mBindMediumUrl) {
		this.mBindMediumUrl = mBindMediumUrl;
	}

	public void createBindInfo(BindInfo info){
		if(info==null){
			return ;
		}
		this.setmBindId(info.getmBindId());
		this.setmBindName(info.getmBindName());
		this.setmBindPage(info.getmBindPage());
		this.setmBindType(info.getmBindType());
		this.setmBindMediumUrl(info.getmBindMediumUrl());
	}
	
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append("[bind_id=").append(mBindId).append(',');
		sb.append("mBindType=").append(mBindType).append(',');
		sb.append("mBindName=").append(mBindName).append(',');
		sb.append("mBindPage=").append(mBindPage).append(',');
		sb.append("mBindMediumUrl=").append(mBindMediumUrl).append(']');
		return sb.toString();
	}

	
}