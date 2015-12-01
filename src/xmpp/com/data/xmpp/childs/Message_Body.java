package com.data.xmpp.childs;

import com.data.xml.XMLMapping;
import com.data.xml.XMLType;
import com.data.xmpp.XMPPNode;

public class Message_Body extends XMPPNode{
	
	
	@XMLMapping(Type=XMLType.ATTRIBUTE,Name="type")
	public String mType;
	
	@XMLMapping(Type=XMLType.ATTRIBUTE,Name="version")
	public String mVersion;
	
	@XMLMapping(Type=XMLType.ATTRIBUTE,Name="action")
	public String mAction;
	
	@XMLMapping(Type=XMLType.NODE,Name="text")
	public Message_Body_Text mTextNode;
	
	@XMLMapping(Type=XMLType.NODE,Name="audio")
	public Message_Body_Audio mAudioNode;
	
	@XMLMapping(Type=XMLType.NODE,Name="image")
	public Message_Body_Image mImageNode;
	
	@Override
	public String getNodeName() {
		// TODO Auto-generated method stub
		return "body";
	}
	public String getType(){
		if(mType!=null){
			return mType;
		}
		return "unknow";
	}
	
	public Message_Body(){}
	
	public Message_Body(String type){
		this.addAttribute("type", type);
	}
	
	public Message_Body(Object type){
		this.addAttribute("type", type);
	}
	
	public int getVersion(){
		try {
			return Integer.parseInt(mVersion);
		} catch (Exception e) {
			return -1;
		}
	}
	
}
