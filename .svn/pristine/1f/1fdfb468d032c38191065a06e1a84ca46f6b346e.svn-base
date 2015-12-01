package com.common.emotion.database;

import com.core.database.Column;
import com.core.database.DatabaseColumn;
import com.core.database.DatabaseTypeConstant;

/**
 * 表情主题包表
 * @author jiaxia
 *
 */
public interface EmotionTheme_Column extends DatabaseColumn {

	
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
	public final String THEME_ID="theme_id";
	/**
	 * 主题选中Icon的路径
	 */
	@Column(defineType=DatabaseTypeConstant.TEXT)
	public final String THEME_CHECK_ICON_PATH="theme_check_icon_path";
	/**
	 * 主题未选中的ICON路径
	 */
	@Column(defineType=DatabaseTypeConstant.TEXT)
	public final String THEME_UNCHECK_ICON_PATH="theme_uncheck_icon_path";
	/**
	 * 主题所处位置
	 */
	@Column(defineType=DatabaseTypeConstant.INT)
	public final String THEME_POSITION="theme_position";
	/**
	 * 主题名
	 */
	@Column(defineType = DatabaseTypeConstant.TEXT)
	public final String THEME_NAME = "theme_name";
	/**
	 * 主题是否隐藏
	 */
	@Column(defineType = DatabaseTypeConstant.INT)
	public final String THEME_HIDDEN = "theme_hidden";
	
	@Column(defineType = DatabaseTypeConstant.TEXT)
	public final String THEME_PATH = "theme_path";
}
