package com.renren.mobile.chat.ui.chatsession;

import com.renren.mobile.chat.activity.RenRenChatActivity.CanTalkable.GROUP;
import com.renren.mobile.chat.base.model.ChatBaseItem;
import com.renren.mobile.chat.model.warpper.ChatMessageWarpper;
import com.renren.mobile.chat.ui.contact.C_ContactsData;
import com.renren.mobile.chat.ui.contact.ContactBaseModel;
import com.renren.mobile.chat.ui.contact.ContactModel;
import com.renren.mobile.chat.ui.contact.RoomInfosData;

/**
 * @author dingwei.chen
 * @说明 会话页面数据适配器(将聊天主界面的数据模型转换为会话页面的数据模型)
 * */
public class ChatSessionDataModelAdapter extends ChatSessionDataModel{
	
	public ChatSessionDataModelAdapter(ChatMessageWarpper message){
		this.mToId = message.mToChatUserId;
		this.mType = message.mMessageType;
		this.mHeadUrl = message.mHeadUrl;
		this.mComeFrom = message.mComefrom;
		this.mName = message.mUserName;
		this.mContent = message.getDescribe();
		this.mTime = message.mMessageReceiveTime;
		this.mLocalId = message.mLocalUserId;
		this.mId = message.mMessageId;
		this.mMessageId = message.mMessageId;
		this.mIsGroup = message.mIsGroupMessage;
		this.mGroupId = message.mGroupId;
		this.mSendState=message.mMessageState;
		this.mDomain = message.mDomain;
		if(this.mComeFrom ==ChatBaseItem.MESSAGE_COMEFROM.LOCAL_TO_OUT){
//			this.mName
			ContactBaseModel model = C_ContactsData.getContact(this.mToId, this.mDomain);
			if(model!=null){
				this.mName = model.mContactName;
			}else{
				this.mName = message.mUserName;
			}
		}
		if(this.mIsGroup==GROUP.GROUP.Value){
			this.mRoomInfo = RoomInfosData.getInstance().getRoomInfo(message.mGroupId);
			this.mToId = this.mGroupId;
		}
		
		
	}
	
}
