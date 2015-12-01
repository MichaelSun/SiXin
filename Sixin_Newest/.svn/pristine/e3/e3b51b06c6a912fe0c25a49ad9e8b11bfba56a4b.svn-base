package com.renren.mobile.chat.base.inter;

import org.w3c.dom.Node;

import com.renren.mobile.chat.model.warpper.ChatMessageWarpper;
import com.renren.mobile.chat.model.warpper.ChatMessageWarpper_FlashEmotion;
import com.renren.mobile.chat.model.warpper.ChatMessageWarpper_Image;
import com.renren.mobile.chat.model.warpper.ChatMessageWarpper_LBS;
import com.renren.mobile.chat.model.warpper.ChatMessageWarpper_Text;
import com.renren.mobile.chat.model.warpper.ChatMessageWarpper_Unknow;
import com.renren.mobile.chat.model.warpper.ChatMessageWarpper_Voice;


/**
 * @author dingwei.chen
 * @说明 xml访问者接口
 * */
public interface XML_Visitor_Interface {

	public void visit(ChatMessageWarpper message,Node node, Node parentNode);
	
	public void visit(ChatMessageWarpper_FlashEmotion message_FlashEmotion,Node node, Node parentNode);
	public void visit(ChatMessageWarpper_Image message_Image,Node node, Node parentNode);
	public void visit(ChatMessageWarpper_LBS message_LBS,Node node, Node parentNode);
//	public void visit(ChatMessageWarpper_SMS message_SMS,Node node, Node parentNode);
	public void visit(ChatMessageWarpper_Text message_Text,Node node, Node parentNode);
	public void visit(ChatMessageWarpper_Voice message_Voice,Node node, Node parentNode);
	public void visit(ChatMessageWarpper_Unknow message_Unknow,Node node, Node parentNode);
}
