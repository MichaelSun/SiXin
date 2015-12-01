package com.renren.mobile.chat.sql;

import android.database.Cursor;

import com.core.database.BaseDAO;
import com.core.database.Query;
import com.renren.mobile.chat.database.ChatHistory_Column;

public class Query_Count extends Query{

	public Query_Count(BaseDAO dao) {
		super(dao);
	}

	@Override
	public Object warpData(Cursor c) {
		
		int count = 0;
		if(c.getCount()!=0){
			c.moveToFirst();
			count = c.getInt(c.getColumnIndex(ChatHistory_Column.COUNT));
		}
		return count;
	}

}
