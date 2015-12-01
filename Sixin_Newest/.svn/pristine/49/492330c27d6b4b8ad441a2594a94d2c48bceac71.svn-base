package com.renren.mobile.chat.ui.contact;
import com.core.orm.ORM;
import com.renren.mobile.chat.base.util.SystemUtil;
import com.renren.mobile.chat.database.ContactCommon_Column;

public final class CommonContactModel {

	@ORM(mappingColumn = ContactCommon_Column._ID)
	public  int Id = 0;
	
	@ORM(mappingColumn = ContactCommon_Column.USER_ID)
	public long mUserId;
	@ORM(mappingColumn = ContactCommon_Column.DOMAIN)
	public String mDomain  = "";
	
	public long getmUserId() {
		return mUserId;
	}

	public void setmUserId(long mUserId) {
		this.mUserId = mUserId;
	}

	public String getmDomain() {
		return mDomain;
	}

	public void setmDomain(String mDomain) {
		this.mDomain = mDomain;
	}
	
	public String toString(){
		StringBuilder sb =new StringBuilder();
		sb.append("mUserId="+mUserId+"#mDomain="+mDomain+"#id="+Id);
		if(SystemUtil.mDebug){
			SystemUtil.logd("CommonContactModel="+sb.toString());
		}
	    return sb.toString();
	}


}
