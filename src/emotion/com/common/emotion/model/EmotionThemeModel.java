package com.common.emotion.model;

import com.common.emotion.database.EmotionTheme_Column;
import com.core.orm.ORM;
/**
 * 表情主题模型类
 * @author jiaxia
 *
 */
public class EmotionThemeModel {


	/** 表情包ID */
	@ORM(mappingColumn = EmotionTheme_Column.PACKAGE_ID)
	public int package_id;
	
	/**主题ID */
	@ORM(mappingColumn = EmotionTheme_Column.THEME_ID)
	public String theme_id = "";
	
	/** 主题ICON选中的路径 */
	@ORM(mappingColumn = EmotionTheme_Column.THEME_CHECK_ICON_PATH)
	public String theme_check_icon_path = "";
	/**
	 * 主题ICON未选中的路径
	 */
	@ORM(mappingColumn = EmotionTheme_Column.THEME_UNCHECK_ICON_PATH)
	public String theme_uncheck_icon_path = "";
	
	/** 主题所在的位置 */
	@ORM(mappingColumn = EmotionTheme_Column.THEME_POSITION)
	public int theme_position;
	
	/** 主题名 */
	@ORM(mappingColumn = EmotionTheme_Column.THEME_NAME)
	public String theme_name = "";
	/**
	 * 主题是否隐藏-->0:隐藏          1:显示
	 */
	@ORM(mappingColumn = EmotionTheme_Column.THEME_HIDDEN)
	public int theme_hidden;
	/**
	 * 主题所在资源包路径
	 */
	@ORM(mappingColumn = EmotionTheme_Column.THEME_PATH)
	public String theme_path = "";
}
