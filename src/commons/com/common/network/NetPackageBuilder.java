package com.common.network;

import java.util.LinkedList;
import java.util.List;

import com.common.utils.Methods;
import com.data.xmpp.XMPPNode;
import com.data.xmpp.childs.Message_Body;
import com.data.xmpp.childs.Message_Body_Text;

public final class NetPackageBuilder {

	
	private NetPackageBuilder(){}
	private static NetPackageBuilder sInstance = new NetPackageBuilder();
	public static NetPackageBuilder getInstance(){
		return sInstance;
	}
	private Message_Body obtainBodyNode(MessageType type){
		Message_Body body = new Message_Body(type);
		return body;
	}
	public List<XMPPNode> build(MessageType type,String text){
		List<XMPPNode> list = new LinkedList<XMPPNode>();
		Message_Body body = this.obtainBodyNode(type);
		switch (type) {
			case text:build(text, body);break;
		}
		list.add(body);
		return list;
	}
	/*文本消息*/
	private void build(String message_text,Message_Body body){
		Message_Body_Text text = new Message_Body_Text();
		body.addChildNode(text);
		text.mValue = (Methods.htmlEncoder(message_text));
	}
}
