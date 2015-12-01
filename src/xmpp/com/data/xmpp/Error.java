package com.data.xmpp;

import com.data.xml.XMLMapping;
import com.data.xml.XMLType;

public class Error extends XMPPNode {

	@XMLMapping(Type=XMLType.ATTRIBUTE,Name="type")
	public String mType;
	
	@XMLMapping(Type=XMLType.ATTRIBUTE,Name="code")
	public String mCode;
	
	@XMLMapping(Type=XMLType.ATTRIBUTE,Name="msg")
	public String mMsg;
	
	@Override
	public String getNodeName() {
		// TODO Auto-generated method stub
		return "error";
	}

}
