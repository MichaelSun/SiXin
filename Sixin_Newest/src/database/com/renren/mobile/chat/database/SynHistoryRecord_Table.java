package com.renren.mobile.chat.database;

import com.core.database.BaseDB;
import com.core.database.BaseDBTable;
import com.core.database.Table;
import com.renren.mobile.chat.dao.SynHistoryRecordDAO;



/**
 * @author yayun.wei
 * @说明 同步聊天历史记录时间表
 * */
@Table(
	TableName=SynHistoryRecord_Table.TABLE_NAME, //表名
	TableColumns = SynHistoryRecord_Column.class,//表对应的列
	DAO = SynHistoryRecordDAO.class
)
public class SynHistoryRecord_Table extends BaseDBTable{
	
	public final static String TABLE_NAME="renren_syn_history_record_table";//表名

	public SynHistoryRecord_Table(BaseDB database) {
		super(database);
	}
	
}
