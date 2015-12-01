package com.common.emotion.database;

import com.common.emotion.dao.EmotionBaseDAO;
import com.core.database.BaseDB;
import com.core.database.BaseDBTable;
import com.core.database.Table;
/**
 * 
 * @author jiaxia
 *  表情表
 */

@Table(
		DAO = EmotionBaseDAO.class, 
		TableColumns = Emotion_Column.class,
		TableName = Emotion_Table.TABLE_NAME
)
public class Emotion_Table extends BaseDBTable {

	public static final String TABLE_NAME = "renren_emotion_base";
	
	public Emotion_Table(BaseDB database) {
		super(database);
	}

}
