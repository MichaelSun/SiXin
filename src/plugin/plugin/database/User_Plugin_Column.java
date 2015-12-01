package plugin.database;

import com.core.database.Column;
import com.core.database.DatabaseColumn;
import com.core.database.DatabaseTypeConstant;

public interface User_Plugin_Column extends DatabaseColumn {
	
	@Column(defineType=DatabaseTypeConstant.INT+" "+DatabaseTypeConstant.PRIMARY)  		
	public final static String PRIMARY_ID = "id";							//主键ID
	
	@Column(defineType=DatabaseTypeConstant.INT)  		
	public final static String USER_ID = "uid";								//用户ID
	
	@Column(defineType=DatabaseTypeConstant.TEXT)  		
	public final static String PLUGIN_NAME = "plugin_id";					//插件名称
	
	@Column(defineType=DatabaseTypeConstant.INT)  		
	public final static String PLUGIN_SUBSCRIBE = "subscribe";				//插件订购状态，0为未订购、1为已订购
	
	@Column(defineType=DatabaseTypeConstant.TEXT)  		
	public final static String USER_PLUGIN_SETTING = "user_plugin_setting";	//插件默认设置（json）
	
	@Column(defineType=DatabaseTypeConstant.TEXT)
	public final static String PLUGIN_CREAT_TIME = "create_time";			//插件创建时间
	
}
