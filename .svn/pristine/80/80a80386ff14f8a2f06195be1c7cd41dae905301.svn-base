package com.data.xmpp;

import java.util.LinkedList;
import java.util.List;

import com.data.xml.XMLMapping;
import com.data.xml.XMLType;

public class Query extends XMPPNode{

	@XMLMapping(Type=XMLType.ATTRIBUTE,Name="xmlns")
	public String mXmlns = null;
	
	@XMLMapping(Type=XMLType.ATTRIBUTE,Name="prefix")
	public String mPrefix = null;
	
	@XMLMapping(Type=XMLType.ATTRIBUTE,Name="version")
	public String mVersion = null;
	
	@XMLMapping(Type=XMLType.NODE,Name="subject")
	public Subject mSubjectNode = null;
	
	@XMLMapping(Type=XMLType.NODE,Name="item",isIterable=true)
	public List<Item> mItems = new LinkedList<Item>();
	
	@XMLMapping(Type=XMLType.NODE,Name="contact")
	public Contact mContactNode ;
	
	@Override
	public String getNodeName() {
		// TODO Auto-generated method stub
		return "query";
	}
	public int getVersion(){
		try {
			return Integer.parseInt(mVersion);
		} catch (Exception e) {
			return -1;
		}
		
	}
}
