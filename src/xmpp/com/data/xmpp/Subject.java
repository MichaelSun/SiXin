package com.data.xmpp;

import com.data.xml.XMLMapping;
import com.data.xml.XMLType;

public class Subject extends XMPPNode{
	@XMLMapping(Type=XMLType.ATTRIBUTE,Name="version")
	public String mVersion = null;
	@Override
	public String getNodeName() {
		// TODO Auto-generated method stub
		return "subject";
	}
	
	public int getVersion(){
		try {
			return Integer.parseInt(mVersion);
		} catch (Exception e) {
			return -1;
		}
	}

}
