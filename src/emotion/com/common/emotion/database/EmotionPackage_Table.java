package com.common.emotion.database;

import com.common.emotion.dao.EmotionPackageBaseDao;
import com.core.database.BaseDB;
import com.core.database.BaseDBTable;
import com.core.database.Table;

@Table(
		DAO = EmotionPackageBaseDao.class, 
		TableColumns = EmotionPackage_Column.class,
		TableName = EmotionPackage_Table.TABLE_NAME
)
/**
 * 表情包表
 * @author jiaxia
 *
 */
public class EmotionPackage_Table extends BaseDBTable {

	public static final String TABLE_NAME = "renren_emotion_package";
	public EmotionPackage_Table(BaseDB database) {
		super(database);
		// TODO Auto-generated constructor stub
	}

	
}
