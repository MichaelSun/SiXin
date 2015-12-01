package com.renren.mobile.chat.dao;

import java.util.List;

import android.content.ContentValues;

import com.renren.mobile.chat.model.warpper.ChatMessageWarpper;

/**
 * @author dingwei.chen
 * */
public interface ChatHistoryDAOObserver{
	
	public void onInsert(ChatMessageWarpper message);
	public void onInsert(List<ChatMessageWarpper> message);
	public void onDelete(String columnName,long _id);
	public void onUpdate(String column,long _id,ContentValues values);
}
