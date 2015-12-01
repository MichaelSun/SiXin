package com.renren.mobile.chat.database;

import android.content.Context;

import com.core.database.BaseDB;
import com.core.database.Database;
import com.renren.mobile.chat.dao.DAOFactoryImpl;


/**
 * @author dingwei.chen
 * @说明 数据库是表的群 数据库只存一个
 * 		聊天数据库
 * */
@Database(
		tables = { 
			/**账号 */
			Account_Table.class,
			/*新鲜事表*/
			NewsFeed_Table.class,
			/*通信录表*/
			Contact_Table.class,
			/*绑定的人人*/
			Renren_Contact_Table.class,
			/*组信息表*/
			Room_Table.class,
			/*聊天记录表*/
			ChatHistory_Table.class,
			/*对话列表*/
			ChatSession_Table.class,
			/*同步聊天记录时间表*/
			SynHistoryRecord_Table.class,
			ContactMessage_Table.class,
			ContactCommon_Table.class
		}
)
public class ChatDB extends BaseDB{

	public static final String DATABASE_NAME="sixin.db";

	public static final int DATABASE_VERSION = 118;

	public ChatDB(Context context) {
		super(context,
			  DATABASE_NAME,
			  DATABASE_VERSION,
			  DAOFactoryImpl.getInstance());
	}
	
	
}
