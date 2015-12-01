package com.data.xmpp;

import com.data.action.ACTION_TYPE;
import com.data.action.Actions;
import com.data.xml.XMLMapping;
import com.data.xml.XMLType;

public abstract class XMPPNodeRoot extends XMPPNode{

	public static interface ROOT_TYPE{
		public String CHAT 			= "chat";
		public String PRIVATE_CHAT  = "personal";
		public String GROUP_CHAT 	= "groupchat";
		public String ERROR 		= "error";
		public String UNAVAILABLE 	= "unavailable";
		public String SET 			= "set";
		public String GET 			= "get";
		public String RESULT 		= "result";
	}
	
	
	@XMLMapping(Type=XMLType.ATTRIBUTE,Name="from")
	public String mFrom = null;
	
	@XMLMapping(Type=XMLType.ATTRIBUTE,Name="to")
	public String mTo = null;
	
	@XMLMapping(Type=XMLType.ATTRIBUTE,Name="id")
	public String mId = null;
	
	@XMLMapping(Type=XMLType.ATTRIBUTE,Name="type")
	public String mType = null;
	
	@XMLMapping(Type=XMLType.NODE,Name="error")
	public Error mErrorNode;
	
	public abstract String getNodeName();
	
	public abstract Actions getAction();
	
	public abstract ACTION_TYPE getActionType();
	
	public long getFromId(){
		try {
			return Long.parseLong(this.mFrom.split("[@]")[0]);
		} catch (Exception e) {
			return -1L;
		}
		
	}
	public long getToId(){
		try {
			return Long.parseLong(this.mTo.split("[@]")[0]);
		} catch (Exception e) {
			return -1L;
		}
	}

	public long getId() {
		if(this.mId!=null){
			try {
				return Long.parseLong(this.mId);
			} catch (Exception e) {
				return -1;
			}
		}
		return -1L;
	}
}
