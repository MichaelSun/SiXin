package com.renren.mobile.chat.ui.notification;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import com.renren.mobile.chat.base.model.ChatBaseItem;
import com.renren.mobile.chat.model.warpper.ChatMessageWarpper;

public class MessageNotificationModel {
	
	private ArrayList<ChatMessageWarpper> mUnReadMessageList =new ArrayList<ChatMessageWarpper>();
	private Set<Long> idSet = new HashSet<Long>();
	
	public MessageNotificationModel(){
		
	}
	
	public MessageNotificationModel(ArrayList<ChatMessageWarpper> unReadMessageList) {
		this.mUnReadMessageList = unReadMessageList;
	}
	
	public void setUnReadMessageList(ArrayList<ChatMessageWarpper> unReadMessageList){
		this.mUnReadMessageList = unReadMessageList;
	}	
	
	public ArrayList<ChatMessageWarpper> getUnReadMessageList(){
		return this.mUnReadMessageList;
	}
	
	public synchronized int getCount(){
		return this.mUnReadMessageList.size();
	}
	
	public boolean checkMessageIndex(int id){
		if(id>getCount()){
			return false;
		}else{
			return true;
		}
		
	}

	/***
	 * 获取通知的具体内容，最新的一条从id = 1开始
	 * **/
	public String getMessageUserName(int id){
		if(!checkMessageIndex(id)){
			return null; 
		}
		return this.mUnReadMessageList.get(getCount()-id).mUserName;
	}
	
	public String getMessageContent(int id){
		if(!checkMessageIndex(id)){
			return null; 
		}
		String content = this.mUnReadMessageList.get(getCount()-id).mMessageContent;
		int type = this.mUnReadMessageList.get(getCount()-id).mMessageType;
		if(type == ChatBaseItem.MESSAGE_TYPE.VOICE){
			content = "[语音]";
		}else if(type == ChatBaseItem.MESSAGE_TYPE.IMAGE){
			content = "[图片]";
		}else if(type == ChatBaseItem.MESSAGE_TYPE.FLASH){
			content = "[炫酷表情]";
		}
		return content;
	}
	
	public long getMessageDate(int id){
		if(!checkMessageIndex(id)){
			return 0; 
		}
		return this.mUnReadMessageList.get(getCount()-id).mMessageReceiveTime;
	}
	
	public long getMessageUserId(int id){
		if(!checkMessageIndex(id)){
			return 0; 
		}
		return this.mUnReadMessageList.get(getCount()-id).mToChatUserId;
	}
	
	public String getMessageDomain(int id){
		if(!checkMessageIndex(id)){
			return null; 
		}
		return this.mUnReadMessageList.get(getCount()-id).mDomain;
	}
	
	public long getGroupId(int id){
		if(!checkMessageIndex(id)){
			return 0; 
		}
		return this.mUnReadMessageList.get(getCount()-id).mGroupId;
	}
	
	public int getMessageType(int id){
		if(!checkMessageIndex(id)){
			return 0; 
		}
//		return this.mUnReadMessageList.get(getCount()-id).mMessageSendMethod;
		return 0;
	}
	
	public int getMessageSmsId(int id){
		if(!checkMessageIndex(id)){
			return 0; 
		}
		return 0;
//		return this.mUnReadMessageList.get(getCount()-id).mSmsId;
	}
	
	public long getMessageId(int id){
		if(!checkMessageIndex(id)){
			return 0; 
		}
		return this.mUnReadMessageList.get(getCount()-id).mMessageId;
	}
	
	public int getIsGroupMessage(int id){
		if(!checkMessageIndex(id)){
			return 0; 
		}
		return this.mUnReadMessageList.get(getCount()-id).mIsGroupMessage;
	}
	
	public long getSMSId(int id){
		if(!checkMessageIndex(id)){
			return 0; 
		}
		return 0;
//		return this.mUnReadMessageList.get(getCount()-id).mSmsId;
	}
	
	public String getHeadUrl(int id){
		if(!checkMessageIndex(id)){
			return null; 
		}
		return this.mUnReadMessageList.get(getCount()-id).mHeadUrl;
	}
	
	public String getLargeHeadUrl(int id){
		if(!checkMessageIndex(id)){
			return null; 
		}
		return this.mUnReadMessageList.get(getCount()-id).mLargeHeadUrl;
	}
	
	public String getUserPhoneNumber(int id){
		if(!checkMessageIndex(id)){
			return null; 
		}
		return null;
//		return this.mUnReadMessageList.get(getCount()-id).mPhoneNumber;
	}
	

	public synchronized void addNotificaiton(ChatMessageWarpper chatMessage){
		this.mUnReadMessageList.add(chatMessage);
	}
	
	public synchronized void addNotificaitonList(ArrayList<ChatMessageWarpper> chatMessageList){
		this.mUnReadMessageList.addAll(chatMessageList);		
	}
	
//	public synchronized void removeNotificationByUserId(long id){
//		int count = getCount();		
//		
//		for(int i=0;i<count;i++){
//			if(id == this.mUnReadMessageList.get(i).mToChatUserId){
//				this.mUnReadMessageList.remove(i);
//				idSet.remove(id);
//				i--;
//				count--;
//			}
//		}
//	}
	
	public synchronized void removeNotificationByGroupId(long id){
		int count = getCount();		
		
		for(int i=0;i<count;i++){
			if(id == this.mUnReadMessageList.get(i).mGroupId){
				this.mUnReadMessageList.remove(i);
				idSet.remove(id);
				i--;
				count--;
			}
		}
	}
	
	
	/**
	 * 通过消息的message id删除此消息
	 * */
	public synchronized void clearUnReadMessageList(){
		if(mUnReadMessageList != null && mUnReadMessageList.size()>0){
			mUnReadMessageList.clear();
		}
	}
	
	public synchronized void removeNotificationByMessageId(long id){
		int count = getCount();		
		
		for(int i=0;i<count;i++){
			if(id == this.mUnReadMessageList.get(i).mMessageId){
				this.mUnReadMessageList.remove(i);
				break;
			}
		}
	}
	
	public synchronized void removeAllNotification(){
		this.mUnReadMessageList.clear();
	}

//	public synchronized int getUnreadMessageCountById(long id){
//		int count = getCount();
//		int num = 0;
//		for(int i=0;i<count;i++){
//			if(this.mUnReadMessageList.get(i).mToChatUserId == id && this.mUnReadMessageList.get(i).mGroupId == -1){
//				
//				num++;
//			}
//			
//		}		
//		return num;
//	}
	
	public synchronized int getUnreadMessageCountByGroupId(long id){
		int count = getCount();
		int num = 0;
		for(int i=0;i<count;i++){
			if(this.mUnReadMessageList.get(i).mGroupId == id){
				num++;
			}
		}		
		return num;
	}
	
	public synchronized boolean isMutil(){
		int count = getCount();
		long id = 0;
		for(int i=0;i<count;i++){
//			id = this.mUnReadMessageList.get(i).mToChatUserId;
//			if(this.mUnReadMessageList.get(i).mIsGroupMessage == MESSAGE_ISGROUP.IS_SINGLE){
//				id = this.mUnReadMessageList.get(i).mToChatUserId;
//			}else if(this.mUnReadMessageList.get(i).mIsGroupMessage == MESSAGE_ISGROUP.IS_GROUP){
//				id = this.mUnReadMessageList.get(i).mGroupId;
//			}
			
			id = this.mUnReadMessageList.get(i).mGroupId;
			if(!idSet.contains(id)){
				idSet.add(id);
			}
		}
		return (idSet.size()>1)? true:false;
	}
}
