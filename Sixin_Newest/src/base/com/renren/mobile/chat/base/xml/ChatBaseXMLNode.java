package com.renren.mobile.chat.base.xml;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


/**
 * @author dingwei.chen
 * @说明 这个类用来减免在XML拼接过程中产生的错误(MD 我不写浏览器太可惜了)
 * */
//public class ChatBaseXMLNode {
//
//	private String mNodeName = null;
//	private ChatBaseXMLNode mParent = null;
//	private List<ChatBaseXMLNode> mChildren = new LinkedList<ChatBaseXMLNode>();
//	private Map<String,String> mAttributes = new HashMap<String,String>();
//	private String mValue = null;
//	
//	public ChatBaseXMLNode getParent(){
//		return mParent;
//	}
//	
//	public ChatBaseXMLNode(String nodeName){
//		this.mNodeName = nodeName;
//	}
//	
//	public void addNode(ChatBaseXMLNode node){
//		if(node==null){
//			return ;
//		}
//		mChildren.add(node);
//		node.mParent = this;
//	}
//	
//	public void addAttribute(String key,Object value){
//		if(value!=null){
//			mAttributes.put(key, value.toString());
//		}
//	}
//	public void setValue(Object value){
//		this.mValue = value.toString();
//	}
//	
//	/**
//	 * XML格式输出
//	 * */
//	public String toXMLString(){
////		if(mChildren.size()==0 && TextUtils.isEmpty(mValue)){
////			StringBuilder sb = new StringBuilder();
////			sb.append("<"+this.mNodeName);
////			if(mAttributes.size()>0){
////				for(Map.Entry<String,String> entry:mAttributes.entrySet()){
////					sb.append(" "+entry.getKey()+"=\""+entry.getValue()+"\"");
////				}
////			}
////			sb.append("/>");
////			return sb.toString();
////		}
//		StringBuilder builder  =new StringBuilder();
//		builder.append(this.preString());
//		if(this.mValue!=null||this.mChildren.size()>0){
//			builder.append(this.printValue());
//			builder.append(this.dispatchChild());
//			builder.append(this.afterString());
//		}
//		return builder.toString();
//	}
//	
//	private String printValue(){
//		if(mValue==null){
//			return "";
//		}else{
//			return mValue;
//		}
//	}
//	
//	private String preString(){
//		StringBuilder sb = new StringBuilder();
//		sb.append("<"+this.mNodeName);
//		if(mAttributes.size()>0){
//			for(Map.Entry<String,String> entry:mAttributes.entrySet()){
//				sb.append(" "+entry.getKey()+"=\""+entry.getValue()+"\"");
//			}
//		}
//		if(this.mValue!=null||this.mChildren.size()>0){
//			sb.append(">");
//		}else{
//			sb.append("/>");
//		}
//		return sb.toString();
//	}
//	private String afterString(){
//		StringBuilder sb = new StringBuilder();
//		sb.append("</"+this.mNodeName+">");
//		return sb.toString();
//	}
//	
//	public String  dispatchChild(){
//		String str = "";
//		for(ChatBaseXMLNode n :mChildren){
//			str+=n.toXMLString();
//		}
//		return str;
//	}
//	
//}
