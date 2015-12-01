package com.renren.mobile.chat.database;

import com.core.database.BaseDB;
import com.core.database.BaseDBTable;
import com.core.database.Table;
import com.renren.mobile.chat.dao.RoomDAO;

@Table(
		TableName=Room_Table.TABLE_NAME, //表名
		TableColumns = Room_Column.class,//表对应的列
		DAO = RoomDAO.class
)
public class Room_Table extends BaseDBTable{

	public static final String TABLE_NAME = "room_info";
	
	public Room_Table(BaseDB database) {
		super(database);
	}

}
