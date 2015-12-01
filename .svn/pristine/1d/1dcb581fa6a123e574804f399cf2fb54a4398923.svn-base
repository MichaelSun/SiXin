package com.common.emotion.model;

import com.common.emotion.database.Emotion_Column;
import com.core.orm.ORM;

/**
 * 
 * @author jiaxia
 * 表情基础结构
 */
public class EmotionBaseModel {
	
	/** 表情名 */
	
	@ORM(mappingColumn = Emotion_Column.EMOTION_NAME)
	public String emotion_name ="";
	
	/** 表情包ID */
	@ORM(mappingColumn = Emotion_Column.PACKAGE_ID)
	public int package_id;
	
	/** 表情主题ID */
	@ORM(mappingColumn = Emotion_Column.THEME_ID)
	public String theme_id = "";
	/**
	 * 表情ID
	 */
	@ORM(mappingColumn = Emotion_Column.EMOTION_ID)
	public int emotion_id;
	
	/** 表情URL */
	@ORM(mappingColumn = Emotion_Column.EMOTION_URL)
	public String emotion_url = "";
	
	/** 表情编码(主要是针对Emoji表情) */
	@ORM(mappingColumn = Emotion_Column.EMOTION_CODE)
	public String emotion_code ="";
	
	/** 表情是否对用户可用 */
	@ORM(mappingColumn = Emotion_Column.EMOTION_HIDDEN)
	public String emotion_hidden = "";
	/** 表情路径 */
	@ORM(mappingColumn = Emotion_Column.EMOTION_PATH)
	public String emotion_path ="";
	
	/** 表情被点击次数
	 *  */
	@ORM(mappingColumn = Emotion_Column.EMOTION_CLICKNUM)
	public int emotion_clicknum;
	
	/** 表情的编码类型
	 *  */
	@ORM(mappingColumn = Emotion_Column.EMOTION_CODESTYLE)
	public int emotion_codestyle;
}
