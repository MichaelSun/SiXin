package com.renren.mobile.chat.database;

import com.core.database.BaseDB;
import com.core.database.BaseDBTable;
import com.core.database.Table;
import com.renren.mobile.chat.dao.ContactMessageDAO;



/**
 * @author dingwei.chen
 * @说明 聊天记录表
 * */
@Table(
	TableName=ContactMessage_Table.TABLE_NAME, //表名
	TableColumns = ContactMessage_Column.class,//表对应的列
	DAO = ContactMessageDAO.class
)
public class ContactMessage_Table extends BaseDBTable{
	
	public final static String TABLE_NAME="contact_message";//表名

	public ContactMessage_Table(BaseDB database) {
		super(database);
	}
	
}
