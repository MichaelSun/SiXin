package com.renren.mobile.chat.views;


public abstract class AbstractFeedAdapter{
	
	protected abstract void processFeedView(ChatFeedView view);
	public void notifyChange(ChatFeedView view){
		this.processFeedView(view);
	}
	
}
