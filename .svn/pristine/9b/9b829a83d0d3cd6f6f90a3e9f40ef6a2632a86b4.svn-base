package com.common.emotion.database;

import com.core.database.Column;
import com.core.database.DatabaseColumn;
import com.core.database.DatabaseTypeConstant;

/**
 * 
 * @author jiaxia
 *  表情列
 */
public interface Emotion_Column extends DatabaseColumn{

	
	@Column(defineType=DatabaseTypeConstant.INT+" "+DatabaseTypeConstant.PRIMARY)
	public static final String _ID = "_id";
	/**
	 * 包ID
	 */
	@Column(defineType=DatabaseTypeConstant.INT) 
	public static final String PACKAGE_ID = "package_id";
	/**
	 * 主题ID
	 */
	@Column(defineType=DatabaseTypeConstant.TEXT)  
	public static final String THEME_ID = "theme_id";
	/**
	 *  表情ID
	 */
	@Column(defineType=DatabaseTypeConstant.INT)  
	public static final String EMOTION_ID = "emotion_id";
	/**
	 * 表情URL
	 */
	@Column(defineType=DatabaseTypeConstant.TEXT)
	public final String EMOTION_URL="emotion_url";
	/**
	 * 表情名(Json串)
	 */
	@Column(defineType=DatabaseTypeConstant.TEXT)
	public final String EMOTION_NAME="emotion_name";
	/**
	 * 表情转义符
	 */
	@Column(defineType=DatabaseTypeConstant.TEXT)
	public final String EMOTION_CODE="emotion_code";
	/**
	 * 是否对用户可见(0:不可见,1:可见)
	 */
	@Column(defineType=DatabaseTypeConstant.INT)
	public final String EMOTION_HIDDEN="emotion_hidden";
	/**
	 * 表情路径
	 */
	@Column(defineType=DatabaseTypeConstant.TEXT)
	public final String EMOTION_PATH="emotion_path";
	/**
	 * 表情点击次数
	 */
	@Column(defineType=DatabaseTypeConstant.INT)
	public final String EMOTION_CLICKNUM="emotion_clicknum";
	
	/**
	 * 表情的编码类型(主要是针对官方表情和Emoji表情的混合)
	 */
	@Column(defineType=DatabaseTypeConstant.INT)
	public final String EMOTION_CODESTYLE="emotion_codestyle";
}
