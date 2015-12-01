package com.data.xmpp.childs;

import com.data.xml.XMLMapping;
import com.data.xml.XMLType;
import com.data.xmpp.XMPPNode;

public class Message_Body_Audio extends XMPPNode{

	@XMLMapping(Type=XMLType.ATTRIBUTE,Name="mine_type")
	public String mMineType;
	@XMLMapping(Type=XMLType.ATTRIBUTE,Name="url")
	public String mUrl;
	@XMLMapping(Type=XMLType.ATTRIBUTE,Name="fullurl")
	public String mFullUrl;
	@XMLMapping(Type=XMLType.ATTRIBUTE,Name="filename")
	public String mFileName;
	@XMLMapping(Type=XMLType.ATTRIBUTE,Name="vid")
	public String mVid;
	@XMLMapping(Type=XMLType.ATTRIBUTE,Name="seqid")
	public String mSeqId;
	@XMLMapping(Type=XMLType.ATTRIBUTE,Name="mode")
	public String mMode;
	@XMLMapping(Type=XMLType.ATTRIBUTE,Name="playtime")
	public String mPlayTime;
	
	@Override
	public String getNodeName() {
		return "audio";
	}
	public int getPlayTime(){
		try {
			return Integer.parseInt(mPlayTime);
		} catch (Exception e) {
			return 0;
		}
		
	}
	
	
}
