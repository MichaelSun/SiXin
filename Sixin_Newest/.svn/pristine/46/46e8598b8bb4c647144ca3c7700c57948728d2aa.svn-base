package com.renren.mobile.chat.util;

import java.util.LinkedList;
import java.util.List;

import com.common.manager.DataManager.Uploadable_Voice;
import com.common.utils.Methods;
import com.data.xmpp.XMPPNode;
import com.data.xmpp.childs.Message_Body;
import com.data.xmpp.childs.Message_Body_Audio;
import com.data.xmpp.childs.Message_Body_Image;
import com.data.xmpp.childs.Message_Body_Text;
import com.renren.mobile.chat.base.model.ChatBaseItem;
import com.renren.mobile.chat.base.model.StateMessageModel;
import com.renren.mobile.chat.model.warpper.ChatMessageWarpper;
import com.renren.mobile.chat.model.warpper.ChatMessageWarpper_FlashEmotion;
import com.renren.mobile.chat.model.warpper.ChatMessageWarpper_Image;
import com.renren.mobile.chat.model.warpper.ChatMessageWarpper_Text;
import com.renren.mobile.chat.model.warpper.ChatMessageWarpper_Voice;

public class C_NetPackageBuilder {

	private static C_NetPackageBuilder sInstance = new C_NetPackageBuilder();
	private C_NetPackageBuilder(){}
	public static C_NetPackageBuilder getInstance(){
		return sInstance;
	}
	
	public List<XMPPNode> build(ChatMessageWarpper message){
		List<XMPPNode> list = new LinkedList<XMPPNode>();
		Message_Body body = this.obtainBodyNode(message);
		switch (message.getMessageType()) {
		case ChatBaseItem.MESSAGE_TYPE.TEXT:
				build((ChatMessageWarpper_Text)message,body);break;
		case ChatBaseItem.MESSAGE_TYPE.VOICE:
				build((ChatMessageWarpper_Voice)message,body);break;
		case ChatBaseItem.MESSAGE_TYPE.FLASH:
				build((ChatMessageWarpper_FlashEmotion)message,body);break;
		case ChatBaseItem.MESSAGE_TYPE.IMAGE:
				build((ChatMessageWarpper_Image)message,body);break;
		default:
				build((ChatMessageWarpper_Text)message,body);break;
		}
		list.add(body);
		if(message.hasNewsFeed()){
			XMPPNode node = this.addFeedNode(message);
			if(node!=null){
				list.add(node);
			}
		}
		return list;
	}
	
	/*图片消息*/
	private void build(ChatMessageWarpper_Image message_image,Message_Body body){
		Message_Body_Image image = new Message_Body_Image();
		body.addChildNode(image);
		image.addAttribute("mine_type", "image/jpg");
		image.addAttribute("tiny", message_image.mTinyUrl);
		image.addAttribute("main",message_image.mMainUrl);
		image.addAttribute("large", message_image.mLargeUrl);
		if(message_image.isBrush()){
			image.addAttribute("source", "brushpen");
		}
	}
	
	
	/*语音消息*/
	private void build(ChatMessageWarpper_Voice message_voice,Message_Body body){
		Message_Body_Audio audio = new Message_Body_Audio();
		audio.addAttribute("url", message_voice.mVoiceUrl);
		audio.addAttribute("fullurl", message_voice.mVoiceUrl);
		audio.addAttribute("filename", message_voice.mUserName);
		audio.addAttribute("seqid", Uploadable_Voice.SEQ_ID);
		audio.addAttribute("vid", message_voice.mVid);
		audio.addAttribute("mode", Uploadable_Voice.Mode.END);
		audio.addAttribute("playtime", message_voice.mPlayTime);
		body.addChildNode(audio);
	}
	
	/*状态消息*/
	public Message_Body build(StateMessageModel message_state){
		Message_Body body = new Message_Body("action");
		body.addAttribute("action", message_state.mStateType);
		return body;
	}
	/*文本消息*/
	private void build(ChatMessageWarpper_Text message_text,Message_Body body){
		Message_Body_Text text = new Message_Body_Text();
		body.addChildNode(text);
		text.mValue = (Methods.htmlEncoder(message_text.mMessageContent));
	}
	
	/*炫酷表情消息*/
	private void build(ChatMessageWarpper_FlashEmotion message_text,Message_Body body) {
		Message_Body_Text text = new Message_Body_Text();
		body.addChildNode(text);
		text.mValue = (Methods.htmlEncoder(message_text.mMessageContent));
	}
	
	
	
	
	private Message_Body obtainBodyNode(ChatMessageWarpper message){
		Message_Body body = new Message_Body(getType(message.getMessageType()));
		return body;
	}
	private XMPPNode addFeedNode(ChatMessageWarpper message){
		if(message.hasNewsFeed() && message.mNewsFeedMessage!=null){
			return message.mNewsFeedMessage.getFeedNode();
		}
		return null;
	}
	
	private String getType(int type){
		switch (type) {
		case ChatBaseItem.MESSAGE_TYPE.TEXT:
				return "text";
		case ChatBaseItem.MESSAGE_TYPE.VOICE:
				return "voice";
		case ChatBaseItem.MESSAGE_TYPE.FLASH:
				return "expression";
		case ChatBaseItem.MESSAGE_TYPE.IMAGE:
				return "image";
		default:
				return "unknow";
		}
	}

	
	
}
