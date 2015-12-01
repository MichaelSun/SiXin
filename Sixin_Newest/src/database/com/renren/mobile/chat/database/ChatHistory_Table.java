package com.renren.mobile.chat.database;

import com.core.database.BaseDB;
import com.core.database.BaseDBTable;
import com.core.database.Table;
import com.renren.mobile.chat.dao.ChatHistoryDAO;



/**
 * @author dingwei.chen
 * @说明 聊天记录表
 * */
@Table(
	TableName=ChatHistory_Table.TABLE_NAME, //表名
	TableColumns = ChatHistory_Column.class,//表对应的列
	DAO = ChatHistoryDAO.class
)
public class ChatHistory_Table extends BaseDBTable{
	
	public final static String TABLE_NAME="renren_chat_table";//表名

	public ChatHistory_Table(BaseDB database) {
		super(database);
	}


	
}
