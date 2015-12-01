package com.renren.mobile.chat.ui.notification;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import com.renren.mobile.chat.ui.contact.feed.ChatFeedModel;

public class FeedNotificationModel {
	private ArrayList<ChatFeedModel> mUnReadFeedList =new ArrayList<ChatFeedModel>();
	private Set<Long> idSet = new HashSet<Long>();
	
	public FeedNotificationModel(){
		
	}
	
	public FeedNotificationModel(ArrayList<ChatFeedModel> unReadFeedList) {
		this.mUnReadFeedList = unReadFeedList;
	}
	
	public void setUnReadFeedList(ArrayList<ChatFeedModel> unReadFeedList){
		this.mUnReadFeedList = unReadFeedList;
	}
	
	public ArrayList<ChatFeedModel> getUnReadFeedList(){
		return this.mUnReadFeedList;
	}
	
	public synchronized int getCount(){
		return this.mUnReadFeedList.size();
	}
	
	public synchronized int getCount(long userId){
		int count = 0;
		for(ChatFeedModel m: this.mUnReadFeedList){
			if(m.getUserId()==userId){
				count++;
			}
		}
		return count;
	}
	
	public boolean checkFeedIndex(int id){
		if(id>getCount()){
			return false;
		}else{
			return true;
		}
	}
	
	/***
	 * 获取通知的具体内容，最新的一条从id = 1开始
	 * **/
	public String getFeedUserName(int id){
		if(!checkFeedIndex(id)){
			return null; 
		}
		return this.mUnReadFeedList.get(getCount()-id).getUserName();
	}
	
	public int getUnReadFeedListIndex(int id){
		if(!checkFeedIndex(id)){
			return 0; 
		}
		return this.getCount()-id;
	}
	
	public String getFeedContent(int id){
		if(!checkFeedIndex(id)){
			return null; 
		}
		String content = this.mUnReadFeedList.get(getCount()-id).getTitle();
		//TODO content type;
		return content;
	}
	
	public long getFeedDate(int id){
		if(!checkFeedIndex(id)){
			return 0; 
		}
		return this.mUnReadFeedList.get(getCount()-id).getTime();
	}
	
	public long getFeedUserId(int id){
		if(!checkFeedIndex(id)){
			return 0; 
		}
		return this.mUnReadFeedList.get(getCount()-id).getUserId();
	}
	
	public String getHeadUrl(int id){
		if(!checkFeedIndex(id)){
			return null; 
		}
		return this.mUnReadFeedList.get(getCount()-id).getHeadUrl();
	}
	
	public synchronized void addNotificaiton(ChatFeedModel chatFeed){
		this.mUnReadFeedList.add(chatFeed);
	}
	
	public synchronized void addNotificaitonList(ArrayList<ChatFeedModel> chatFeedList){
		this.mUnReadFeedList.addAll(chatFeedList);		
	}
	
	public synchronized void removeNotificationByUserId(long id){
		int count = getCount();		
		
		for(int i=0;i<count;i++){
			if(id == this.mUnReadFeedList.get(i).getUserId()){
				this.mUnReadFeedList.remove(i);
				idSet.remove(id);
				i--;
				count--;
			}
		}
	}
	
	public synchronized void clearUnReadFeedList(){
		if(mUnReadFeedList != null && mUnReadFeedList.size()>0){
			mUnReadFeedList.clear();
		}
	}
	
	public synchronized int getUnreadFeedCountById(long id){
		int count = getCount();
		int num = 0;
		for(int i=0;i<count;i++){
			if(this.mUnReadFeedList.get(i).getUserId() == id){
				num++;
			}
		}		
		return num;
	}
	
	public synchronized boolean isMutil(){
		int count = getCount();
		long id;
		for(int i=0;i<count;i++){
			id = this.mUnReadFeedList.get(i).getUserId();
			if(!idSet.contains(id)){
				idSet.add(id);
			}
		}
		return (idSet.size()>1)? true:false;
	}
}
