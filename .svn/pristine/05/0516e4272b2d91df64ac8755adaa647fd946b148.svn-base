package plugin.database;

import com.core.orm.ORM;

public class Plugin_Model {

	@ORM(mappingColumn = Plugin_Column.PLUGIN_ID)	
	public int plugin_id;						//插件ID
	
	@ORM(mappingColumn = Plugin_Column.PLUGIN_NAME)	
	public String plugin_name;					//插件名称
	
	@ORM(mappingColumn = Plugin_Column.PLUGIN_URL)
	public String plugin_url;					//访问插件的URL(html5插件有效)
	
	@ORM(mappingColumn = Plugin_Column.PLUGIN_NAMESPACE)	
	public String plugin_namespace;				//插件命名空间，与JID一一对应
	
	@ORM(mappingColumn = Plugin_Column.PLUGIN_JID)	
	public String plugin_jid;					//插件JID
	
	@ORM(mappingColumn = Plugin_Column.PlUGIN_ACCESS_APIS)
	public String plugin_access_apis;			//插件允许访问的客户端api
	
	@ORM(mappingColumn = Plugin_Column.PlUGIN_GROUP)
	public int plugin_group;					//插件分组: 1, 内部插件 2, 合作商插件 3, 第三方插件
	
	@ORM(mappingColumn = Plugin_Column.PlUGIN_TYPE)	
	public int plugin_type;						//插件类型: 1,本地插件 2, HTML5插件
	
	@ORM(mappingColumn = Plugin_Column.PlUGIN_USAGE)
	public int plugin_usage;					//插件用途: 1,输入源类 2,消息通信类 3, 增强扩展类
	
	@ORM(mappingColumn = Plugin_Column.PlUGIN_MESSAGE_TYPE)
	public int plugin_message_type;				//消息类型
	
	@ORM(mappingColumn = Plugin_Column.PLUGIN_CODEC)
	public int plugin_codec;					//扩展<z/>XML节内的编码方式 0, 默认的XML格式 1, BASE64编码
	
	@ORM(mappingColumn = Plugin_Column.PLUGIN_DESC)
	public String plugin_desc;					//插件描述
	
	@ORM(mappingColumn = Plugin_Column.PlUGIN_CONFIG_PUSH)
	public int plugin_config_push;				//插件是否包含push配置, 但有此属性客户端则展示push配置
	
	@ORM(mappingColumn = Plugin_Column.PlUGIN_PUSH)
	public int plugin_push;						//插件是否接受推送设置
	
	
	@ORM(mappingColumn = Plugin_Column.PlUGIN_STATUS)	
	public int plugin_status;					//插件开启状态,默认为0
	
	@ORM(mappingColumn = Plugin_Column.PlUGIN_CATEGORY)
	public int plugin_category;					//插件分类
	
	@ORM(mappingColumn = Plugin_Column.PLUGIN_DEFAULT_SETTING)
	public String plugin_default_setting;		//插件默认设置（json）

	@ORM(mappingColumn = Plugin_Column.PLUGIN_ICON_URL)
	public String plugin_icon_url;				//插件的icon_url
	
	@ORM(mappingColumn = Plugin_Column.PLUGIN_CREAT_TIME)
	public String plugin_creat_time;			//插件创建时间
	
	@ORM(mappingColumn = Plugin_Column.PLUGIN_CLASS_NAME)
	public String plugin_class_name;			//插件对应的类名
	
}
