package com.renren.mobile.chat.sql;

import java.util.List;

import android.database.Cursor;

import com.core.database.BaseDAO;
import com.core.database.Query;
import com.renren.mobile.chat.dao.DAOFactoryImpl;
import com.renren.mobile.chat.dao.NewsFeedDAO;
import com.renren.mobile.chat.model.warpper.ChatMessageFactory;
import com.renren.mobile.chat.model.warpper.ChatMessageWarpper;

public class Query_ChatHistory extends Query{

	public Query_ChatHistory(BaseDAO dao) {
		super(dao);
	}

	@Override
	public Object warpData(Cursor c) {
		List<ChatMessageWarpper> list 
			= ChatMessageFactory.getInstance().buildChatMessages(c);
		for(ChatMessageWarpper m:list){
			if(m.mFeedId!=-1){
				NewsFeedDAO dao = DAOFactoryImpl.getInstance().buildDAO(NewsFeedDAO.class);
				m.mNewsFeedMessage = dao.queryNewsFeedModel(m.mFeedId);
			}
		}
		return list;
	}

}
