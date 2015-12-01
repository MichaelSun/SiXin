package com.data.xmpp;

import java.util.LinkedList;
import java.util.List;

import org.xml.sax.Attributes;

import com.common.utils.Methods;
import com.core.util.CommonUtil;
import com.data.util.ShowFieldsUtil;
import com.data.xml.XOMUtil;

/**
 * @author dingwei.chen1988@gmail.com
 * @说明 XMPP节点基类
 * */
public abstract class XMPPNode {

	public XMPPNode mParent = null;
	public String mValue = null;
	private List<XMPPNode> mChilds = new LinkedList<XMPPNode>();
	private List<String> mAttributes = new LinkedList<String>();
	public abstract String getNodeName();
	
	public void processAttribute(Attributes attributes){
		XOMUtil.getInstance().mappingAttribute(this,attributes);
	}
	public void processChildNode(XMPPNode node){
		XOMUtil.getInstance().mappingNode(this,node);
	}
	
	
	public void setParent(XMPPNode parent){
		this.mParent = parent;
		this.mParent.processChildNode(this);
	}
//	@Override
//	public String toString() {
//		// TODO Auto-generated method stub
//		int length = 0;
//		if(this.mParent!=null){
//			length = 1;
//		}
//		return ShowFieldsUtil.showAllFields(length,this);
//	}
	
	public void addChildNode(XMPPNode node){
		mChilds.add(node);
	}
	public void addAttribute(String key,Object value){
	    if(value != null) {
	        mAttributes.add(key+"=\""+Methods.htmlEncoder(value.toString())+"\"");
	    }
		
	}
	private boolean mIsPrintValueOrTail = true;
	public String toXMLString(){
		mIsPrintValueOrTail = true;
		StringBuilder builder = new StringBuilder();
		builder.append( preXML());
		builder.append( printValue());
		builder.append( printChilds());
		builder.append( aftXML());
		return builder.toString();
	}
	
	
	
	
	
	private String printValue(){
		if(mValue!=null){
			return mValue;
		}
		return "";
	}
	
	private String preXML(){
		StringBuilder builder = new StringBuilder();
		builder.append("<"+this.getNodeName());
		if(mAttributes.size()>0){
			builder.append(" ");
		}
		for(String attr:mAttributes){
			builder.append(attr+" ");
		}
		if(this.mValue==null && this.mChilds.size()==0){
			this.mIsPrintValueOrTail = false;
			builder.append("/>");
		}else{
			builder.append(">");
		}
		
		return builder.toString();
	}
	private String printChilds(){
		if(mChilds.size()==0){
			return "";
		}
		StringBuilder builder = new StringBuilder();
		for(XMPPNode node:mChilds){
			builder.append(node.toXMLString());
		}
		return builder.toString();
	}
	private String aftXML(){
		if(mIsPrintValueOrTail){
			StringBuilder builder = new StringBuilder();
			builder.append("</"+this.getNodeName()+">");
			return builder.toString();
		}
		return "";
	}
	public long parseLong(String longText){
		try {
			return Long.parseLong(longText);
		} catch (Exception e) {
			return -1;
		}
	}
	public void addByIterator(XMPPNode parentNode,Iterable<XMPPNode> nodes){
		for(XMPPNode node:nodes){
			parentNode.addChildNode(node);
		}
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return ShowFieldsUtil.showAllFields(0, this);
	}
}
