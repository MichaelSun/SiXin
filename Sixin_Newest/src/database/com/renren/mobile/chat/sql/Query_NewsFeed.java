package com.renren.mobile.chat.sql;

import android.database.Cursor;

import com.core.database.BaseDAO;
import com.core.database.Query;
import com.core.orm.ORMUtil;
import com.renren.mobile.chat.newsfeed.NewsFeedFactory;
import com.renren.mobile.chat.newsfeed.NewsFeedWarpper;

public class Query_NewsFeed extends Query{

	public Query_NewsFeed(BaseDAO dao) {
		super(dao);
	}

	@Override
	public Object warpData(Cursor c) {
		c.moveToFirst();
		if(c.getCount()>0){
			NewsFeedWarpper newsfeed = NewsFeedFactory.getInstance().obtainNewsFeedModel();
			ORMUtil.getInstance().ormQuery(newsfeed.getClass(), newsfeed, c);
			return newsfeed;
		}else{
			return null;
		}
	}

}
