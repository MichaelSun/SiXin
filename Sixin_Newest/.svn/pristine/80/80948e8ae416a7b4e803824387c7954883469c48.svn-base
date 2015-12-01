package com.renren.mobile.chat.util;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;

import com.common.manager.MessageManager.OnSendTextListener.SEND_TEXT_STATE;
import com.common.manager.VoiceManager;
import com.common.network.DomainUrl;
import com.core.util.CommonUtil;
import com.core.voice.PlayerThread.OnAddPlayListener;
import com.core.voice.PlayerThread.PlayRequest;
import com.renren.mobile.chat.RenrenChatApplication;
import com.renren.mobile.chat.activity.RenRenChatActivity;
import com.renren.mobile.chat.activity.RenRenChatActivity.CanTalkable.GROUP;
import com.renren.mobile.chat.activity.ThreadPool;
import com.renren.mobile.chat.base.inter.Subject;
import com.renren.mobile.chat.base.model.ChatBaseItem.MESSAGE_COMEFROM;
import com.renren.mobile.chat.base.util.SystemUtil;
import com.renren.mobile.chat.dao.ChatHistoryDAO;
import com.renren.mobile.chat.dao.ChatHistoryDAOObserver;
import com.renren.mobile.chat.database.ChatHistory_Column;
import com.renren.mobile.chat.model.warpper.ChatMessageWarpper;
import com.renren.mobile.chat.model.warpper.ChatMessageWarpper_Voice;
import com.renren.mobile.chat.newsfeed.NewsFeedWarpper;
import com.renren.mobile.chat.ui.contact.C_ContactsData;
import com.renren.mobile.chat.view.BaseTitleStateFactory;


/**
 * @author dingwei.chen
 * @说明 聊天主界面的适配器(将进行数据检验)
 * */
public class C_ChatListAdapter extends BaseChatListAdapter implements ChatHistoryDAOObserver,OnAddPlayListener{

	
	
	
	ChatHistoryDAO mSubject = null;
	
	RenRenChatActivity mActivity;
	public C_ChatListAdapter(RenRenChatActivity activity) {
		super(activity);
		this.mActivity = activity;
	}
	String mDomain = null;
	public void setDomain(String domain){
		this.mDomain = domain;
	}
	public String getDomain(){
		return this.mDomain;
	}
	List<ChatMessageWarpper> mMessageCache = new ArrayList<ChatMessageWarpper>();
	@Override
	public void onInsert(final ChatMessageWarpper message) {
		if(message.mComefrom== MESSAGE_COMEFROM.OUT_TO_LOCAL){
			this.setDomain(message.mDomain);
		}
		SystemUtil.log("syn", "insert0 ");
		
		ThreadPool.obtain().removeCallbacks(mInsertRunnable);
		synchronized (mMessageCache) {
			mMessageCache.add(message);
		}
		ThreadPool.obtain().executeMainThread(mInsertRunnable);
	}
	InsertRunnable mInsertRunnable = new InsertRunnable();
	public class InsertRunnable implements Runnable{

		@Override
		public void run() {
			synchronized (mMessageCache) {
				for(ChatMessageWarpper message:mMessageCache){
					if(message.mGroupId == mToChatUserId && message.mIsGroupMessage==mIsGroup){
						addChatMessage(message);
						notifyDataSetInvalidated();
						notifyCallback();
					}
				}
				mMessageCache.clear();
			}
		}
	}
	
	
	
	
	@Override
	public void onDelete(String columns,long _id) {
		SystemUtil.log("syn", "ondelete  = "+columns);
		if(columns.equals(ChatHistory_Column._ID)){
			boolean flag = false;
			for(ChatMessageWarpper message:mChatMessages){
				if(message.mMessageId==_id){
					this.mChatMessages.remove(message);
					flag = true;
					break;
				}
			}
			this.processTime();
			if(flag){
				notifyDataSetChanged();
			}
		}
		if(columns.equals(ChatHistory_Column.GROUP_ID)||columns.equals(ChatHistory_Column.TO_CHAT_ID)){//删除全部的时候要设置加载云端按钮
			if(_id ==this.mToChatUserId){
				NewsFeedWarpper m = this.mMessageNull.mNewsFeedMessage;
				this.resetData();
				this.mMessageNull.setNewsFeedModel(m);
				if(this.mNotifyCallback!=null){
					this.mNotifyCallback.onAllDataDelete();
				}
			}
		}
	}

	@Override
	public void onUpdate(String columnName,final long _id,final ContentValues value) {}
	
	public void attachToDAO(ChatHistoryDAO dao){
		this.mSubject = dao;
		this.mSubject.registorObserver(this);
	}
	public void distachToDAO(){
		if(this.mSubject!=null){
			this.mSubject.unregistorObserver(this);
		}
	}




	@Override
	public void onAddPlay(PlayRequest request) {
		boolean flag = false;
		for(ChatMessageWarpper m:this.mChatMessages){
			if(request.mPlayListenner==m){
				flag = true;
				continue;
			}
			if(flag){
				if(m.mSendTextState!=SEND_TEXT_STATE.SEND_PREPARE&&(m.mMessageState==Subject.COMMAND.COMMAND_DOWNLOAD_VOICE_OVER) && (m instanceof ChatMessageWarpper_Voice)){
					VoiceManager.getInstance().addToPlay(m.mMessageContent, (ChatMessageWarpper_Voice)m);
				}
			}
		};
	}
	@Override
	public void onInsert(final List<ChatMessageWarpper> messages) {
		final List<ChatMessageWarpper> list = new ArrayList<ChatMessageWarpper>();
		SystemUtil.log("syn", "oninsert  = "+messages.size()+" mToChatUserId ="+mToChatUserId);
		for(ChatMessageWarpper m:messages){
			SystemUtil.log("syn", m.mGroupId+":"+mToChatUserId+":"+(m.mGroupId==mToChatUserId)+":"+(m.mIsGroupMessage==mIsGroup));
			if(m.mGroupId==mToChatUserId &&m.mIsGroupMessage==mIsGroup){
				
				if(m.mComefrom== MESSAGE_COMEFROM.OUT_TO_LOCAL){
					this.setDomain(m.mDomain);
				}
				list.add(m);
			}
		}
		ThreadPool.obtain().executeMainThread(new Runnable() {
			public void run() {
					addChatMessage(list);
					notifyDataSetInvalidated();
					notifyCallback();
			}
		});
	}
	
}
