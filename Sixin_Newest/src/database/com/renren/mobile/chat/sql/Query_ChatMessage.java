package com.renren.mobile.chat.sql;

import android.database.Cursor;

import com.core.database.BaseDAO;
import com.core.database.Query;
import com.core.orm.ORMUtil;
import com.renren.mobile.chat.model.warpper.ChatMessageFactory;
import com.renren.mobile.chat.model.warpper.ChatMessageWarpper;

public class Query_ChatMessage extends Query{

	public Query_ChatMessage(BaseDAO dao) {
		super(dao);
	}

	@Override
	public Object warpData(Cursor c) {
		if(c.getCount()>0){
			c.moveToFirst();
			ChatMessageWarpper message = ChatMessageFactory.getInstance().createChatMessage(c);
			ORMUtil.getInstance().ormQuery(message.getClass(), message, c);
			return message;
		}
		return null;
	}

}
