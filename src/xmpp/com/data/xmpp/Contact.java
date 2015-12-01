package com.data.xmpp;

import java.util.LinkedList;
import java.util.List;

import com.data.xml.XMLMapping;
import com.data.xml.XMLType;

public class Contact extends XMPPNode {

	@XMLMapping(Type=XMLType.ATTRIBUTE,Name="prefix")
	public String mPrefix ;
	
	@XMLMapping(Type=XMLType.NODE,Name="item",isIterable=true)
	public List<Item> mItems = new LinkedList<Item>();
	
	
	@Override
	public String getNodeName() {
		// TODO Auto-generated method stub
		return "contact";
	}

}
