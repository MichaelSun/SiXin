package com.renren.mobile.chat.model.warpper;
import java.util.LinkedList;
import java.util.List;

import com.data.xmpp.Message;
import com.renren.mobile.chat.R;
import com.renren.mobile.chat.RenrenChatApplication;
import com.renren.mobile.chat.base.inter.Subject;
import com.renren.mobile.chat.base.model.ChatBaseItem;
import com.renren.mobile.chat.views.ItemLongClickDialogProxy.LONGCLICK_COMMAND;
public class ChatMessageWarpper_Text extends ChatMessageWarpper{
	private static final String[] ITEM_SELECT = new String[]{
		RenrenChatApplication.getmContext().getResources().getString(R.string.ChatMessageWarpper_Text_java_1)		//ChatMessageWarpper_Text_java_1=删除文本; 
	};
	public ChatMessageWarpper_Text(){
		this.mMessageType =ChatBaseItem.MESSAGE_TYPE.TEXT;
	}
	@Override
	public List<OnLongClickCommandMapping> getOnClickCommands() {
		List<OnLongClickCommandMapping> list = new LinkedList<ChatMessageWarpper.OnLongClickCommandMapping>();
		if(this.mMessageState == Subject.COMMAND.COMMAND_MESSAGE_ERROR ){
			list.add(new OnLongClickCommandMapping(RenrenChatApplication.getAppResources().getString(R.string.ChatMessageWarpper_FlashEmotion_java_1),LONGCLICK_COMMAND.RESEND));		//ChatMessageWarpper_FlashEmotion_java_1=重新发送; 
		}
		list.add(new OnLongClickCommandMapping(RenrenChatApplication.getAppResources().getString(R.string.ChatMessageWarpper_Text_java_2),LONGCLICK_COMMAND.COPY));		//ChatMessageWarpper_Text_java_2=复制消息; 
		list.add(new OnLongClickCommandMapping(RenrenChatApplication.getAppResources().getString(R.string.ChatMessageWarpper_Text_java_3),LONGCLICK_COMMAND.DELETE));		//ChatMessageWarpper_Text_java_3=删除消息; 
		list.add(new OnLongClickCommandMapping(RenrenChatApplication.getAppResources().getString(R.string.ChatMessageWarpper_FlashEmotion_java_3),LONGCLICK_COMMAND.FORWARD));		//ChatMessageWarpper_FlashEmotion_java_3=转发; 
		list.add(new OnLongClickCommandMapping(RenrenChatApplication.getAppResources().getString(R.string.ChatMessageWarpper_FlashEmotion_java_4),LONGCLICK_COMMAND.CANCEL));		//ChatMessageWarpper_FlashEmotion_java_4=取消; 
		return list;
	}
	@Override
	public String getDescribe() {
		return this.mMessageContent;
	}
	@Override
	public void onAddToAdapter() {}
	@Override
	public void download(boolean isForceDownload) {}
	@Override
	public void onErrorCode() {
		this.addErrorCode(Subject.COMMAND.COMMAND_MESSAGE_ERROR);
	}
	@Override
	public void swapDataFromXML(Message message) {
		this.mMessageContent = message.mBody.mTextNode.mValue;
	}
}
