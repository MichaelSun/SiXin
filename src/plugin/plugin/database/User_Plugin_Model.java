package plugin.database;

import com.core.orm.ORM;

public class User_Plugin_Model {
	
	@ORM(mappingColumn = User_Plugin_Column.USER_ID)
	public String user_id;							//用户uid
	
	@ORM(mappingColumn = User_Plugin_Column.PLUGIN_NAME)
	public String plugin_name;						//插件名称
	
	@ORM(mappingColumn = User_Plugin_Column.PLUGIN_SUBSCRIBE)
	public String plugin_subscribe;					//插件订购状态
	
	@ORM(mappingColumn = User_Plugin_Column.USER_PLUGIN_SETTING)
	public String user_plugin_setting;				//插件默认设置（json）
	
	@ORM(mappingColumn = User_Plugin_Column.PLUGIN_CREAT_TIME)
	public String plugin_creat_time;				//创建时间
	
}
