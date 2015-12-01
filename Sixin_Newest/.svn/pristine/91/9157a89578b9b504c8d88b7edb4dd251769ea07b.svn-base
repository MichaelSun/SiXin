package com.renren.mobile.chat.dao;

import android.content.ContentValues;

import com.core.database.BaseDAO;
import com.core.database.BaseDBTable;
import com.core.database.Delete;
import com.core.database.Insert;
import com.core.orm.ORMUtil;
import com.renren.mobile.chat.database.NewsFeed_Column;
import com.renren.mobile.chat.newsfeed.NewsFeedWarpper;
import com.renren.mobile.chat.sql.Query_NewsFeed;


/**
 * @author dingwei.chen
 * @说明 新鲜事数据库DAO
 * 
 * */
public class NewsFeedDAO extends BaseDAO {

	Insert mInsert  = new Insert(this);
	Delete mDelete = new Delete(this);
	Query_NewsFeed mQuery_NewsFeed = new Query_NewsFeed(this);
	
	public NewsFeedDAO(BaseDBTable table) {
		super(table);
	}
	
	public long insertNewsFeedModel(NewsFeedWarpper newsfeedMessage){
		ContentValues values = new ContentValues();
		ORMUtil.getInstance().ormInsert(newsfeedMessage.getClass(), newsfeedMessage, values);
		long id = mInsert.insert(values);
		return id;
	}
	public NewsFeedWarpper queryNewsFeedModel(long id){
		String whereStr = NewsFeed_Column._ID+" = "+id;
		NewsFeedWarpper newsfeed = 
				mQuery_NewsFeed.query(null, whereStr, null, null, null,NewsFeedWarpper.class);
		return newsfeed;
	}
	public long  deleteNewsFeedModel(NewsFeedWarpper newsfeedMessage){
		String whereStr = NewsFeed_Column._ID+" = "+newsfeedMessage.mFeedId;
		return mDelete.delete(whereStr);
	}
	
	
	

}
