package com.common.emotion.model;
import com.common.emotion.database.EmotionPackage_Column;
import com.core.orm.ORM;
/**
 * 表情包基础结构
 * @author jiaxia
 *
 */
public class EmotionPackageModel {

	/** 表情包ID */
	@ORM(mappingColumn = EmotionPackage_Column.PACKAGE_ID)
	public int package_id;
	
	/** 表情包名 */
	@ORM(mappingColumn = EmotionPackage_Column.PACKAGE_NAME)
	public String package_name = "";
	
	/** 表情是否对用户可用 */
	@ORM(mappingColumn = EmotionPackage_Column.PACKAGE_HIDDEN)
	public int package_hidden;
	
	/** 表情路径 */
	@ORM(mappingColumn = EmotionPackage_Column.PACKAGE_PATH)
	public String package_path = "";
	
	/** 表情属于小、中表情
	 *  */
	@ORM(mappingColumn = EmotionPackage_Column.PACKAGE_TYPE)
	public int package_type;
	/**
	 * 表情包在Tab 中所处的位置
	 */
	@ORM(mappingColumn = EmotionPackage_Column.PACKAGE_POSITION)
	public int package_position;
}
