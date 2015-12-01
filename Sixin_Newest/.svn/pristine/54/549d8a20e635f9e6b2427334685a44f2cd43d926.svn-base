package com.renren.mobile.chat.actions.message;

import java.util.ArrayList;
import java.util.List;

import com.common.actions.ActionNotMatchException;
import com.common.network.DomainUrl;
import com.common.network.NetRequestListener;
import com.common.utils.Methods;
import com.data.xmpp.Message;
import com.renren.mobile.chat.actions.ActionDispatcher;
import com.renren.mobile.chat.base.model.ChatBaseItem;
import com.renren.mobile.chat.model.warpper.ChatMessageFactory;
import com.renren.mobile.chat.model.warpper.ChatMessageWarpper;
import com.renren.mobile.chat.ui.contact.RoomInfosData;


/**
 * @author dingwei.chen1988@gmail.com
 * @说明 群消息推送
 * */
public class PushSoftInfoMessage extends Action_Message{

	
	@Override
	public void processAction(Message node, long id) {
		List<ChatMessageWarpper> list = new ArrayList<ChatMessageWarpper>(1);
		ChatMessageWarpper message = ChatMessageFactory.getInstance().obtainMessage("info");
		this.basicParser(message, node);
		message.swapDataFromXML(node);
		message.mMessageContent = Methods.htmlDecoder(node.mBody.mTextNode.mValue);
		
		if(node.mFrom.contains(DomainUrl.MUC_URL)){
			message.mIsGroupMessage = ChatBaseItem.MESSAGE_ISGROUP.IS_GROUP;
			message.mVersion = node.mBody.getVersion();
			RoomInfosData.getInstance().updateRoomInfoFromMessage(node.getFromId(),message.mVersion);
		}else{
			message.mIsGroupMessage = ChatBaseItem.MESSAGE_ISGROUP.IS_SINGLE;
			message.mToChatUserId = node.getFromId();
		}
		list.add(message);
		ActionDispatcher.CALLBACK.onRecive(list);
	}
	
	private void basicParser(ChatMessageWarpper message,Message m){
		message.mUserName = m.getFromName();
		message.mLocalUserId = m.getToId();
		message.mGroupId = m.getFromId();
		message.mToChatUserId = m.getSendId();
		message.mComefrom= ChatBaseItem.MESSAGE_COMEFROM.NOTIFY;
		message.mMessageKey = m.getMsgKey();
		message.mDomain = m.getFromDomain();
	}
	@Override
	public void checkActionType(Message node) throws ActionNotMatchException {
		this.addCheckCase(node.mType.equals("info"));
	}

}
