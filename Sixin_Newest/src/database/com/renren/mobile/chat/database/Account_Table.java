package com.renren.mobile.chat.database;

import com.core.database.BaseDB;
import com.core.database.BaseDBTable;
import com.core.database.Table;
import com.renren.mobile.chat.dao.AccountDAO;



/**
 * @author dingwei.chen
 * @说明 聊天记录表
 * */
@Table(
	TableName=Account_Table.TABLE_NAME, //表名
	TableColumns = Account_Column.class,//表对应的列
	DAO = AccountDAO.class
)
public class Account_Table extends BaseDBTable{
	
	public final static String TABLE_NAME="renren_account_table";//表名

	public Account_Table(BaseDB database) {
		super(database);
	}


	
}
