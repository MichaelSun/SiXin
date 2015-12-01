package com.common.emotion.database;

import com.core.database.Column;
import com.core.database.DatabaseColumn;
import com.core.database.DatabaseTypeConstant;
/**
 * 表情包表列名
 * @author jiaxia
 *
 */
public interface EmotionPackage_Column extends DatabaseColumn {

	@Column(defineType=DatabaseTypeConstant.INT+" "+DatabaseTypeConstant.PRIMARY)
	public static final String _ID = "_id";
	
	@Column(defineType=DatabaseTypeConstant.INT)//包ID
	public static final String PACKAGE_ID = "package_id";
	
	@Column(defineType=DatabaseTypeConstant.TEXT)
	public final String PACKAGE_NAME="package_name";//包名称
	
	@Column(defineType=DatabaseTypeConstant.TEXT)
	public final String PACKAGE_PATH="package_path";//表情包路径
	
	@Column(defineType=DatabaseTypeConstant.INT)
	public final String PACKAGE_HIDDEN="package_hidden";//表情包是否隐藏（0：不隐藏，1：隐藏）
	
	@Column(defineType=DatabaseTypeConstant.INT)
	public final String PACKAGE_TYPE="package_type";//表情包的类型（0：小表情，1：中表情）
	
	@Column(defineType=DatabaseTypeConstant.INT)
	public final String PACKAGE_POSITION="package_position";//表情包在Tab中的位置.
}
