package com.renren.mobile.chat.model.warpper;
import java.util.LinkedList;
import java.util.List;
import org.w3c.dom.Node;
import com.data.xmpp.Message;
import com.renren.mobile.chat.base.model.ChatBaseItem;
import com.renren.mobile.chat.views.ItemLongClickDialogProxy.LONGCLICK_COMMAND;
/**
 * @author dingwei.chen
 * @说明 未知消息包装类
 * */
import com.renren.mobile.chat.R;
import com.renren.mobile.chat.RenrenChatApplication;
public class ChatMessageWarpper_Unknow extends ChatMessageWarpper {
	public ChatMessageWarpper_Unknow(){
		this.mMessageType =ChatBaseItem.MESSAGE_TYPE.UNKNOW;
	}
	@Override
	public List<OnLongClickCommandMapping> getOnClickCommands() {
		List<OnLongClickCommandMapping> list = new LinkedList<ChatMessageWarpper.OnLongClickCommandMapping>();
		list.add(new OnLongClickCommandMapping(RenrenChatApplication.getmContext().getResources().getString(R.string.ChatMessageWarpper_Text_java_2),LONGCLICK_COMMAND.COPY));		//ChatMessageWarpper_Text_java_2=复制消息; 
		list.add(new OnLongClickCommandMapping(RenrenChatApplication.getmContext().getResources().getString(R.string.ChatMessageWarpper_Text_java_3),LONGCLICK_COMMAND.DELETE));		//ChatMessageWarpper_Text_java_3=删除消息; 
		list.add(new OnLongClickCommandMapping(RenrenChatApplication.getmContext().getResources().getString(R.string.ChatMessageWarpper_FlashEmotion_java_3),LONGCLICK_COMMAND.FORWARD));		//ChatMessageWarpper_FlashEmotion_java_3=转发; 
		list.add(new OnLongClickCommandMapping(RenrenChatApplication.getmContext().getResources().getString(R.string.ChatMessageWarpper_FlashEmotion_java_4),LONGCLICK_COMMAND.CANCEL));		//ChatMessageWarpper_FlashEmotion_java_4=取消; 
		return list;
	}
	@Override
	public String getDescribe() {
		return RenrenChatApplication.getmContext().getResources().getString(R.string.ChatMessageWarpper_Unknow_java_2);		//ChatMessageWarpper_Unknow_java_2=[未知消息类型]; 
	}
	@Override
	public void download(boolean isForceDownload) {}
	@Override
	public void onErrorCode() {
	}
	@Override
	public void swapDataFromXML(Message message) {
	}
}
