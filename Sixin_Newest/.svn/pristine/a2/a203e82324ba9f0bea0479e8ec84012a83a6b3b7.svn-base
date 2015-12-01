package com.renren.mobile.chat.ui.chatsession;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import android.content.Intent;
import android.os.Handler;
import android.util.Log;

import com.common.manager.LoginManager;
import com.renren.mobile.chat.RenrenChatApplication;
import com.renren.mobile.chat.base.model.ChatBaseItem.MESSAGE_ISGROUP;
import com.renren.mobile.chat.dao.ChatHistoryDAO;
import com.renren.mobile.chat.dao.ChatSessionDAO;
import com.renren.mobile.chat.dao.DAOFactoryImpl;
import com.renren.mobile.chat.model.warpper.ChatMessageWarpper;
import com.renren.mobile.chat.ui.MainFragmentActivity;
import com.renren.mobile.chat.ui.notification.MessageNotificationManager;
import com.renren.mobile.chat.util.ChatDataHelper;

public class ChatSessionManager {
	private static ChatSessionManager mChatSessionManager = new ChatSessionManager();
	private LinkedList<Object> mSessionList = new LinkedList<Object>();
	private HashSet<Long> mContactSet = new HashSet<Long>();
	private ChatSessionAdapter mAdapter;
	private Handler mHandler;

	private ChatSessionManager() {}

	public static ChatSessionManager getInstance() {
		return mChatSessionManager;
	}

	/**
	 * 初始化数据
	 * */
	public void loadSession() {
		if(this.mSessionList == null||this.mSessionList.size() == 0){
			ChatSessionDAO chatListDao = DAOFactoryImpl.getInstance().buildDAO(ChatSessionDAO.class);
			List<ChatSessionDataModel> sessionList = chatListDao.query_ChatSessions(LoginManager.getInstance().getLoginInfo().mUserId);
//			List<ChatSessionDataModel> sessionList = chatListDao.queryMergerChatSessions();
//			Log.v("aa", "loadSession mMessageId = " + sessionList.get(0).mMessageId);
			
			if(sessionList != null){
				for(ChatSessionDataModel model : sessionList){
					ChatMessageWarpper  warpper = ChatDataHelper.getInstance().queryLastMessageByGroupId(model.mGroupId,model.mIsGroup);
					if(warpper == null){
						model.mContent = "";
					}
				}
				
				int count = sessionList.size();
				for(int i=0;i<count;i++){
					if(sessionList.get(i).mIsGroup == MESSAGE_ISGROUP.IS_SINGLE){
						mContactSet.add(sessionList.get(i).mToId);
					}else{
						mContactSet.add(sessionList.get(i).mGroupId);
					}
				}		
				LinkedList<Object> list = new LinkedList<Object>(sessionList);//TODO ???为何要用object?~ 
				this.mSessionList = list;
			}
			
		}
	}

	/*
	 * 第三方调用时使用
	 */
	public void againLoadSession(){
//		ChatHistoryDAO chatHistoryDao =DAOFactoryImpl.getInstance().buildDAO(ChatHistoryDAO.class);
//		List<ChatSessionDataModel> sessionList = chatHistoryDao.query_ChatSessions(RenrenChatApplication.user_id);
		
		ChatSessionDAO chatListDao = DAOFactoryImpl.getInstance().buildDAO(ChatSessionDAO.class);
		List<ChatSessionDataModel> sessionList = chatListDao.query_ChatSessions(LoginManager.getInstance().getLoginInfo().mUserId);
		if(sessionList != null){
			for(ChatSessionDataModel model : sessionList){
				ChatMessageWarpper  warpper = ChatDataHelper.getInstance().queryLastMessageByGroupId(model.mGroupId,model.mIsGroup);
				if(warpper == null){
					model.mContent = "";
				}
			}
			
			int count = sessionList.size();
			for(int i=0;i<count;i++){
				
				if(sessionList.get(i).mIsGroup == MESSAGE_ISGROUP.IS_SINGLE){
					mContactSet.add(sessionList.get(i).mToId);
				}else{
					mContactSet.add(sessionList.get(i).mGroupId);
				}
				
			}		
			LinkedList<Object> list = new LinkedList<Object>(sessionList);
			this.mSessionList = list;
		}
		
	}
	
	public LinkedList<Object> getSessionList() {
		loadSession();
		updateMessageList();
		return this.mSessionList;
	}
	
	public synchronized void delMessage(ChatSessionDataModel messageItem, ChatSessionDataModel model){
		boolean b = mSessionList.remove(model);
		Log.v("rara", "" + b);
		long newTime = messageItem.getmTime();
		int count1 = mSessionList.size();
		for(int index = 0; index < count1; index++){
			ChatSessionDataModel chatSessionDataModel = (ChatSessionDataModel) mSessionList.get(index);
			long oldTime = chatSessionDataModel.getmTime();
			Log.v("rara", "oldTime = " + oldTime);
			Log.v("rara", "newTime = " + newTime);
			if(newTime >= oldTime){
				Log.v("rara", ">>>>>>>");
				mSessionList.add(index, messageItem);
				break;
			}else if(newTime < oldTime){
				Log.v("rara", "<<<<<<<<");
				if(index == mSessionList.size() - 1) {
					mSessionList.add(messageItem);
					break;
				}
			}
		}
		refreshChatSessionList();
	}

	/**
	 * 增加一条新信息，并且按时间进行排序
	 * */
	public synchronized void addMessage(ChatSessionDataModel messageItem) {
		loadSession();
		int count = mSessionList.size();
		ChatSessionDataModel oldSessionItem = null;
//		if(mContactSet.contains(messageItem.mToId)){
//			for (int i = 0; i < count; i++) {
//				oldSessionItem = (ChatSessionDataModel) mSessionList.get(i);
//				Log.v("bb", "messageItem.mToId = " + messageItem.mToId + ",," + "oldSessionItem.mToId = " + oldSessionItem.mToId);
//				if (messageItem.mToId == oldSessionItem.mToId) {
//					Log.v("bb", "remove");
//					if(messageItem.mIsGroup == MESSAGE_ISGROUP.IS_GROUP){
//						if(messageItem.mGroupId == oldSessionItem.mGroupId){
//							mSessionList.remove(oldSessionItem);
//							break;
//						}
//					}
//					mSessionList.remove(oldSessionItem);
//					break;
//				}
//			}
//		}
		if(mContactSet.contains(messageItem.mGroupId)){
			for (int i = 0; i < count; i++) {
				oldSessionItem = (ChatSessionDataModel) mSessionList.get(i);
				if (messageItem.mGroupId == oldSessionItem.mGroupId) {
					mSessionList.remove(oldSessionItem);
					break;
				}
			}
		}else if(mContactSet.contains(messageItem.mToId)){
			for (int i = 0; i < count; i++) {
				oldSessionItem = (ChatSessionDataModel) mSessionList.get(i);
				if (messageItem.mToId == oldSessionItem.mToId && oldSessionItem.mGroupId == -1) {
					mSessionList.remove(oldSessionItem);
					break;
				}
			}
		}
//		mSessionList.add(0, messageItem);
//		mContactSet.add(messageItem.mToId);
		long newTime = messageItem.getmTime();
		int count1 = mSessionList.size();
		for(int index = 0; index < count1; index++){
			ChatSessionDataModel chatSessionDataModel = (ChatSessionDataModel) mSessionList.get(index);
			long oldTime = chatSessionDataModel.getmTime();
			Log.v("xaxa", "oldTime = " + oldTime);
			Log.v("xaxa", "newTime = " + newTime);
			if(newTime >= oldTime){
				Log.v("xaxa", ">>>>>>>");
				mSessionList.add(index, messageItem);
				break;
			}else if(newTime < oldTime){
				Log.v("xaxa", "<<<<<<<<");
				if(index == mSessionList.size() - 1) {
					mSessionList.add(messageItem);
					break;
				}
			}
		}
		
		if(messageItem.mIsGroup == MESSAGE_ISGROUP.IS_SINGLE){
			mContactSet.add(messageItem.mToId);
		}else{
			mContactSet.add(messageItem.mGroupId);
		}
	}
	
	/**
	 * 变更消息状态
	 * */
	public synchronized void updateMessageState(long id,int state) {
		loadSession();
		for(Object o:mSessionList){
			ChatSessionDataModel model = (ChatSessionDataModel)o;
			if(model.mId == id){
				model.mSendState = state;
			}
		}
	}
	
	
	/**
	 * 更新会话列表的未读信息数目
	 * */
	public void updateMessageList() {
		int count = mSessionList.size();
		long id = 0;
		for (int i = 0; i < count; i++) {
			ChatSessionDataModel messageItem = (ChatSessionDataModel) mSessionList.get(i);
				id = messageItem.mGroupId;
				messageItem.mUnreadCount = 0;
				messageItem.mUnreadCount += MessageNotificationManager.getInstance().getMessageNotificationModel()
				.getUnreadMessageCountByGroupId(id);
		}
	}

	public synchronized void clearAll() {
		mSessionList.clear();
	}
	
	/**
	 * 删除所有聊天记录
	 * */
	public void clearAllRecord(){
		ChatHistoryDAO chatHistoryDAO = DAOFactoryImpl.getInstance().buildDAO(ChatHistoryDAO.class);
		ChatSessionDAO chatListDao = DAOFactoryImpl.getInstance().buildDAO(ChatSessionDAO.class);
		chatHistoryDAO.deleteAll();
		chatListDao.deleteAll();
		clearAll();
		mAdapter.clear();
		mHandler.sendEmptyMessage(ChatSessionScreen.REFRESH_TEXT);
		MessageNotificationManager.getInstance().clearAllNotification(RenrenChatApplication.mContext);
		MessageNotificationManager.getInstance().getMessageNotificationModel().clearUnReadMessageList();
		if (RenrenChatApplication.isMainFragementActivity == true) {
			Intent intent = new Intent(
					MainFragmentActivity.REFRESH_NEW_MESSAGE_RECEIVER_ACTION);
			RenrenChatApplication.mContext.sendBroadcast(intent);
		}
	}
	
	public void refreshChatSessionList(){
		mHandler.sendEmptyMessage(ChatSessionScreen.REFRESH_LIST); 
	}
	
	/*
	 * 清空缓存时使用
	 */
	public void setAdapterAndHandler(ChatSessionAdapter adapter,Handler handler){
		this.mAdapter = adapter;
		this.mHandler = handler;
	}
	
}
