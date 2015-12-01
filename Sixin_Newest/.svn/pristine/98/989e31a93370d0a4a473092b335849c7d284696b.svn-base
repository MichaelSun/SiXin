package com.renren.mobile.chat.database;

import com.core.database.BaseDB;
import com.core.database.BaseDBTable;
import com.core.database.Table;
import com.renren.mobile.chat.dao.ChatSessionDAO;


@Table(
		TableName=ChatSession_Table.TABLE_NAME, //表名
		TableColumns = ChatSession_Column.class,//表对应的列
		DAO = ChatSessionDAO.class
)
public class ChatSession_Table extends BaseDBTable{

	public static final String TABLE_NAME = "renren_chat_list";
	
	public ChatSession_Table(BaseDB database) {
		super(database);
	}

}
