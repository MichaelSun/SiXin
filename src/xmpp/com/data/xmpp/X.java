package com.data.xmpp;

import java.util.LinkedList;
import java.util.List;

import com.data.xml.XMLMapping;
import com.data.xml.XMLType;

/**
 * @author dingwei.chen1988@gmail.com
 * X节点
 * */
public class X extends XMPPNode{
	
	@XMLMapping(Type=XMLType.NODE,Name="item",isIterable=true)
	public List<Item> mItemNode = new LinkedList<Item>();
	
	@XMLMapping(Type=XMLType.NODE,Name="check",isIterable=true)
	public List<Check> mCheckNodes = new LinkedList<Check>();
	
	@XMLMapping(Type=XMLType.NODE,Name="subject")
	public Subject mSubjectNode = null;
	
	@XMLMapping(Type=XMLType.NODE,Name="status",isIterable=true)
	public List<Status> mStatusNodes = new LinkedList<Status>();
	
	@XMLMapping(Type=XMLType.ATTRIBUTE,Name="prefix")
	public String mPrefix;
	
	@XMLMapping(Type=XMLType.NODE,Name="invite")
	public Invite mInvitesNode;
	
	@XMLMapping(Type=XMLType.NODE,Name="destroy")
	public Destroy mDestory;
	
	@XMLMapping(Type=XMLType.ATTRIBUTE,Name="version")
	public String mVersion;
	
	@XMLMapping(Type=XMLType.ATTRIBUTE,Name="xmlns")
	public String mXmlns;
	
	
	
	@Override
	public String getNodeName() {
		return "x";
	}

	public int getVersion(){
		try {
			return Integer.parseInt(mVersion);
		} catch (Exception e) {
			return -1;
		}
		
	}
	
}
