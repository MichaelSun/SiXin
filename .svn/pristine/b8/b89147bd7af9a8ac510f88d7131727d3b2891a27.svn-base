package plugin.helpMe.database;

import com.core.orm.ORM;

public class HelpMe_Helper_Model {
	
	@ORM(mappingColumn = HelpMe_Helper_Column._ID)
	public int _id;														//主键ID
	
	@ORM(mappingColumn = HelpMe_Helper_Column.HELPER_UID)
	public int helper_uid;												//用户uid,此id是师兄id还是师妹id？
	
	@ORM(mappingColumn = HelpMe_Helper_Column.HELPED_TOPIC_ID)
	public int helped_topic_uid;										//求助帖的id
	
	@ORM(mappingColumn = HelpMe_Helper_Column.HELPED_STATUS)
	public int helped_status;											//援助状态，1为已援助、0为被取消援助
	
	@ORM(mappingColumn = HelpMe_Helper_Column.HELPED_TOPIC_CREATE_TIME)
	public int helped_topic_create_time;								//创建时间
}
