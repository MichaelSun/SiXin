package com.renren.mobile.chat.base.model;

import java.io.Serializable;

import android.content.Intent;

import com.common.manager.LoginManager;
import com.common.network.DomainUrl;
import com.renren.mobile.chat.RenrenChatApplication;
import com.renren.mobile.chat.activity.DataPool;
import com.renren.mobile.chat.activity.RenRenChatActivity;
import com.renren.mobile.chat.activity.RenRenChatActivity.CanTalkable;
import com.renren.mobile.chat.activity.RenRenChatActivity.CanTalkable.GROUP;

/**
 * @author dingwei.chen
 * @说明 解决以为系统崩溃导致的数据丢失
 * */
public class ChatSaveModel implements Serializable,CanTalkable{

	transient public Intent mSaveIntent = null;
	public String mSessionId = null;
	public long mLocalUserId = 0l;
	public long mToUserId = 0L;
	public String mDomain = DomainUrl.SIXIN_DOMAIN;
	public String mHeadUrl = null;
	public int mIsGroup = GROUP.CONTACT_MODEL.Value;
	public String mName = "#";
	public ChatSaveModel(Intent save){
		this.mSaveIntent = save;
		CanTalkable mToChatUser = DataPool.obtain().getObject(RenRenChatActivity.PARAM_NEED.TO_CHAT_USER_MODEL, CanTalkable.class);
		mLocalUserId = LoginManager.getInstance().getLoginInfo().mUserId;
		mToUserId = mToChatUser.getUId();
		mDomain = mToChatUser.getDomain();
		mHeadUrl = mToChatUser.getHeadUrl();
		mIsGroup = mToChatUser.isGroup();
		mName = mToChatUser.getName();
		mSessionId = RenrenChatApplication.SESSION_ID;
	}
	@Override
	public long getUId() {
		// TODO Auto-generated method stub
		return mToUserId;
	}
	@Override
	public long getLocalUId() {
		// TODO Auto-generated method stub
		return mLocalUserId;
	}
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return mName;
	}
	@Override
	public int isGroup() {
		// TODO Auto-generated method stub
		return mIsGroup;
	}
	@Override
	public String getHeadUrl() {
		// TODO Auto-generated method stub
		return mHeadUrl;
	}
	@Override
	public String getDomain() {
		// TODO Auto-generated method stub
		return mDomain;
	}
	
}
