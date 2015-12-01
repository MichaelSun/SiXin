package com.renren.mobile.chat.ui.chatsession;


import com.core.orm.ORM;
import com.data.util.ShowFieldsUtil;
import com.renren.mobile.chat.actions.models.RoomInfoModelWarpper;
import com.renren.mobile.chat.base.model.ChatBaseItem.MESSAGE_ISGROUP;
import com.renren.mobile.chat.common.DateFormat;
import com.renren.mobile.chat.database.ChatSession_Column;
/**
 * 会话列表item的DataModel
 * @author tian.wang
 * 2011-11-25
 * */

public class ChatSessionDataModel {
	
	@ORM(mappingColumn=ChatSession_Column._ID)
	public long mId;
	@ORM(mappingColumn=ChatSession_Column.LOCAL_USER_ID)
	public long mLocalId;
	@ORM(mappingColumn=ChatSession_Column.MESSAGE_ID)
	public int mMessageId;
	@ORM(mappingColumn=ChatSession_Column.CHAT_TIME)
	public long mTime;
	@ORM(mappingColumn=ChatSession_Column.MESSAGE)
	public String mContent;
	@ORM(mappingColumn=ChatSession_Column.TO_CHAT_ID)
	public long mToId;
	@ORM(mappingColumn=ChatSession_Column.TO_CHAT_NAME)
	public String mName;
	@ORM(mappingColumn=ChatSession_Column.COME_FROM)
	public int mComeFrom;
	@ORM(mappingColumn=ChatSession_Column.HEAD_URL)
	public String mHeadUrl;
	@ORM(mappingColumn=ChatSession_Column.MESSAGE_TYPE)
	public int mType;
	@ORM(mappingColumn=ChatSession_Column.IS_GROUP_MESSAGE)
	public int mIsGroup = MESSAGE_ISGROUP.IS_SINGLE;
	
	@ORM(mappingColumn=ChatSession_Column.DOMAIN)
	public String mDomain;
	
	@ORM(mappingColumn=ChatSession_Column.MESSAGE_STATE)
	public int mSendState;
	
	@ORM(mappingColumn=ChatSession_Column.GROUP_ID)
	public long mGroupId;
	
	public String mTitleTime;
	public int mUnreadCount;
	public RoomInfoModelWarpper mRoomInfo = null;

	private void initTileTime(long time){
		this.mTitleTime = DateFormat.getDateByChatSession(time);
	}
	
	
	public long getmId() {
		return mId;
	}



	public void setmId(long mId) {
		this.mId = mId;
	}



	public long getmToId() {
		return mToId;
	}



	public void setmToId(long mToId) {
		this.mToId = mToId;
	}



	public long getmTime() {
		return mTime;
	}



	public void setmTime(long mTime) {
		this.mTime = mTime;
	}



	public String getmContent() {
		return mContent;
	}



	public void setmContent(String mContent) {
		this.mContent = mContent;
	}



	public String getmTitleTime() {
		return mTitleTime;
	}


	public void setmTitleTime(String mTitleTime) {
		this.mTitleTime = mTitleTime;
	}


	public int getmUnreadCount() {
		return mUnreadCount;
	}


	public void setmUnreadCount(int mUnreadCount) {
		this.mUnreadCount = mUnreadCount;
	}


	public String getmName() {
		return mName;
	}


	public void setmName(String mName) {
		this.mName = mName;
	}
	
	@Override
	public String toString() {
		return ShowFieldsUtil.showAllFields(0, this);
	}

	public String getDomain() {
		return mDomain;
	}


	public void setDomain(String mDomain) {
		this.mDomain = mDomain;
	}

}
