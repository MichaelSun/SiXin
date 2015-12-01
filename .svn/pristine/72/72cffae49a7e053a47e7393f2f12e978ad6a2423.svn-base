package plugin.helpMe.database;

import com.core.orm.ORM;

public class HelpMe_Help_Model {
	
	@ORM(mappingColumn = HelpMe_Help_Column._ID)
	public int _id;									//主键ID
	
	@ORM(mappingColumn = HelpMe_Help_Column.HOST_UID)
	public int host_uid;							//求助人的uid
	
	@ORM(mappingColumn = HelpMe_Help_Column.MAX_HELPERS)
	public int max_helpers;							//最多参与人数，默认5
	
	@ORM(mappingColumn = HelpMe_Help_Column.CUR_HELPERS)
	public int cur_helpers;							//当前参与人数，默认0
	
	@ORM(mappingColumn = HelpMe_Help_Column.TOPIC_TITLE)
	public String topic_title;						//求助标题
	
	@ORM(mappingColumn = HelpMe_Help_Column.TOPIC_CONTENT)
	public String topic_content;					//求助内容
	
	@ORM(mappingColumn = HelpMe_Help_Column.UNIVERSITY_KEY)
	public String university_key;					//所在学校代码
}
