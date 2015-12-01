package plugin.database;

import com.core.database.Column;
import com.core.database.DatabaseColumn;
import com.core.database.DatabaseTypeConstant;

/**
 * @author changsheng.ning
 */
public interface Plugin_Column extends DatabaseColumn{

	@Column(defineType=DatabaseTypeConstant.INT+" "+DatabaseTypeConstant.PRIMARY)  		
	public final static String PLUGIN_ID = "plugin_id";						//插件ID
	
	@Column(defineType=DatabaseTypeConstant.TEXT)
	public final static String PLUGIN_NAME = "plugin_name";					//插件名称
	
	@Column(defineType=DatabaseTypeConstant.TEXT)
	public final static String PLUGIN_URL = "plugin_url";					//访问插件的URL(html5插件有效)
	
	@Column(defineType=DatabaseTypeConstant.TEXT)
	public final static String PLUGIN_NAMESPACE = "plugin_namespace";		//插件命名空间（与jid为一一对应关系）
	
	@Column(defineType=DatabaseTypeConstant.TEXT)  		
	public final static String PLUGIN_JID = "plugin_jid";					//插件JID
	
	@Column(defineType=DatabaseTypeConstant.TEXT)
	public final static String PlUGIN_ACCESS_APIS="access_apis";			//插件允许访问的客户端api
	
	@Column(defineType=DatabaseTypeConstant.INT)
	public final static String PlUGIN_GROUP = "plugin_group";				//插件分组: 1, 内部插件 2, 合作商插件 3, 第三方插件
	
	@Column(defineType=DatabaseTypeConstant.INT)
	public final static String PlUGIN_TYPE = "plugin_type";					//插件类型: 1,本地插件 2, HTML5插件
	
	@Column(defineType=DatabaseTypeConstant.INT)
	public final static String PlUGIN_USAGE = "plugin_usage";				//插件用途: 1,输入源类 2,消息通信类 3, 增强扩展类
	
	@Column(defineType=DatabaseTypeConstant.INT)
	public final static String PlUGIN_MESSAGE_TYPE = "message_type";		//接收消息的处理方式: 1,前台消息提示, 2,后台消息, 
	
	@Column(defineType=DatabaseTypeConstant.INT)
	public final static String PLUGIN_CODEC = "plugin_codec";				//扩展<z/>XML节内的编码方式 0, 默认的XML格式 1, BASE64编码
	
	@Column(defineType=DatabaseTypeConstant.TEXT)
	public final static String PLUGIN_DESC = "plugin_desc";					//插件描述
	
	@Column(defineType=DatabaseTypeConstant.INT)
	public final static String PlUGIN_CONFIG_PUSH = "plugin_config_push";	//插件是否包含push配置,0,不包含，1,包含，其他无效
	
	@Column(defineType=DatabaseTypeConstant.INT)
	public final static String PlUGIN_PUSH = "plugin_push";					//插件是否接受推送设置,0不推送，1推送，其他无效
	
	
	@Column(defineType=DatabaseTypeConstant.INT)
	public final static String PlUGIN_STATUS = "plugin_status";				//插件开启状态,默认为0
	
	@Column(defineType=DatabaseTypeConstant.INT)
	public final static String PlUGIN_CATEGORY = "plugin_category";			//插件分类
	
	@Column(defineType=DatabaseTypeConstant.TEXT)
	public final static String PLUGIN_ICON_URL = "icon_url";				//插件图标的URL

	@Column(defineType=DatabaseTypeConstant.TEXT)
	public final static String PLUGIN_DEFAULT_SETTING="default_plugin_setting";	//插件默认设置（json）

	@Column(defineType=DatabaseTypeConstant.TEXT)
	public final static String PLUGIN_CREAT_TIME = "create_time";			//插件创建时间
	
	@Column(defineType=DatabaseTypeConstant.TEXT)
	public final static String PLUGIN_CLASS_NAME = "class_name";			//插件对应的类名
	

	/*
	@Column(defineType=DatabaseTypeConstant.INT)
	public final static String PLUGIN_IS_INPUTSOURCE = "is_input_source";//是否为输入源插件，0为应用插件，1为输入源插件
	@Column(defineType=DatabaseTypeConstant.TEXT)
	public final static String PLUGIN_PERMISSIONS = "plugin_permissions";		//插件权限
	@Column(defineType=DatabaseTypeConstant.TEXT)
	public final static String PLUGIN_VERSION="plugin_version";		//插件版本
	input source plugin or application plugin
	*/

}
