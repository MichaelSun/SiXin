package com.renren.mobile.chat.util;

import android.text.TextUtils;

import com.common.manager.DataManager;
import com.common.manager.LoginManager;
import com.common.manager.MessageManager;
import com.common.manager.MessageManager.OnSendTextListener;
import com.common.network.NetRequestListener;
import com.renren.mobile.chat.actions.requests.RequestConstructorProxy;
import com.renren.mobile.chat.activity.RenRenChatActivity.CanTalkable.GROUP;
import com.renren.mobile.chat.base.model.ChatBaseItem.MESSAGE_ISGROUP;
import com.renren.mobile.chat.model.warpper.ChatMessageWarpper;
import com.renren.mobile.chat.model.warpper.ChatMessageWarpper_Image;
import com.renren.mobile.chat.model.warpper.ChatMessageWarpper_Voice;


/**
 * @author dingwei.chen
 * @说明 消息的发送器
 * */
public final class ChatMessageSender{

	private static ChatMessageSender sSender = new ChatMessageSender();
	GROUP mGroup = GROUP.CONTACT_MODEL;
	public void setGroup(GROUP group){
		if(group==null){
			throw new RuntimeException("ERROR");
		}
		mGroup = group;
	}
	private ChatMessageSender(){}
	public static ChatMessageSender getInstance(){
		return sSender;
	}
	/*发送到网络,加入了数据库插入判断*/
	public void sendMessageToNet(final ChatMessageWarpper message,boolean isInsertToDatabase){
		if(message!=null){
			message.onSendTextPrepare();
			switch (mGroup) {
				case CONTACT_MODEL:
					message.mIsGroupMessage = MESSAGE_ISGROUP.IS_SINGLE;
					break;
				default:
					message.mIsGroupMessage = MESSAGE_ISGROUP.IS_GROUP;
					message.mGroupId = message.mToChatUserId;
					break;
			}
			if(isInsertToDatabase){
				ChatDataHelper.getInstance().insertToTheDatabase(message);
			}
			this.sendMessageToNet(message.mLocalUserId,message.mToChatUserId, message.mDomain,message);
		}
	}
	public void sendRequestToNet(NetRequestListener request){
//		NetRequestsPool.getInstance().addNetRequest(request);
		MessageManager.getInstance().sendMessage(request);
	}
	
	public void sendMessageToNet(long fromId,long toId,OnSendTextListener listener){
		sendMessageToNet(fromId, toId, null, listener);
	} 
	
	public void sendMessageToNet(long fromId,long toId, String domain,OnSendTextListener listener){
		NetRequestListener request  = null;
		switch (mGroup) {
		case CONTACT_MODEL:
			request = RequestConstructorProxy.getInstance().sendSynMessage(fromId, toId, domain, listener);
			break;
		default:
			request = RequestConstructorProxy.getInstance().sendGroupMessage(LoginManager.getInstance().getLoginInfo().mUserName,fromId,toId,listener);
			break;
		}
		MessageManager.getInstance().sendMessage(request);
	} 
	
	
	
	public void sendMessageToNet(final String message){
		MessageManager.getInstance().sendMessage(message);
	}
	/*网络语音走正常路线*/
	public void uploadData(ChatMessageWarpper_Voice message, byte[] data) {
		DataManager.getInstance().postVoice(data, message);
	}
	
	public void uploadPNGData(ChatMessageWarpper_Image message, byte[] data){
		DataManager.getInstance().postPNGImage(data, message);
	}
	
	public void uploadData(ChatMessageWarpper_Image message, byte[] data) {
		if (!TextUtils.isEmpty(message.mMessageContent) && message.mMessageContent.contains("brush_png"))
			uploadPNGData(message, data);
		else
			DataManager.getInstance().postImage(data, message);	
	}
	
	
}
