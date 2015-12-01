package com.data.xmpp;

import com.data.xml.XMLMapping;
import com.data.xml.XMLType;

public class Check extends XMPPNode{

	public static interface IS_MEMEBER{
		int TRUE 	=0;
		int FLASE 	=1;
		int NOROOM 	=2;
	}
	
	@XMLMapping(Type=XMLType.ATTRIBUTE,Name="from")
	public String mFrom;
	@XMLMapping(Type=XMLType.ATTRIBUTE,Name="update")
	public String mUpdate;
	@XMLMapping(Type=XMLType.ATTRIBUTE,Name="isMember")
	public String mIsMember;
	
	@Override
	public String getNodeName() {
		// TODO Auto-generated method stub
		return "check";
	}
	
	public boolean getUpdate(){
		if(mUpdate.equals("true")){
			return true;
		}
		return false;
	}
	
	public boolean getIsMember(){
		if(mIsMember.equals("true")){
			return true;
		}
		return false;
	}
	
	public long getRoomId(){
		return parseLong(mFrom);
	}

}
