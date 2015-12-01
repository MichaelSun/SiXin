package com.common.emotion.database;

import android.content.Context;

import com.core.database.BaseDB;
import com.core.database.Database;


@Database(
		tables = { 
				/**
				 * 
				 * @author jiaxia
				 *  表情表
				 */
				Emotion_Table.class,
				/**
				 * 表情包表
				 * @author jiaxia
				 *
				 */
				EmotionPackage_Table.class,
				/**
				 * 表情主题表
				 * @author jiaxia
				 *
				 */
				EmotionTheme_Table.class
		}
		
)
public class Emotion_DB extends BaseDB {
	/**表情数据库*/
	public static final String EMOTION_DB_NAME = "emotioncommon.db";
	public static final int EMOTION_DB_VERSION =23;
	
	public Emotion_DB(Context context) {
		super(context, EMOTION_DB_NAME, EMOTION_DB_VERSION, EmotionDaoFactoryImpl.getInstance());
	}

}
