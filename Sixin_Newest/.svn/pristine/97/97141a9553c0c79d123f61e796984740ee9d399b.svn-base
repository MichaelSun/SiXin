package com.renren.mobile.chat.base.model;

import android.text.TextUtils;

public class QuiteMessageModel {
	private String sid;
	
//	<body sid='someSID' rid=‘1573741822’/>
	public QuiteMessageModel(String sid) {
		this.sid=sid;
	}

	
	public String getSid() {
		return sid;
	}


	public void setSid(String sid) {
		this.sid = sid;
	}



	public String toString(){
		StringBuilder sb=new StringBuilder();
		sb.append("<body ");
		if(!TextUtils.isEmpty(sid)){
			sb.append("sid=\"").append(sid).append("\" ");
		}
		sb.append("type=\"terminate\" xmlns=\"http://jabber.org/protocol/httpbind\">");
		sb.append("<presence type=\"unavailable\">");
		sb.append("</body>");
		return sb.toString();
	}
}
