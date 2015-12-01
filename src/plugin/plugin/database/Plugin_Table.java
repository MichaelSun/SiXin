package plugin.database;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import plugin.DBBasedPluginManager;
import plugin.database.dao.PluginBaseDAO;
import android.database.sqlite.SQLiteDatabase;

import com.core.database.BaseDB;
import com.core.database.BaseDBTable;
import com.core.database.Table;

/**
 * @author changsheng.ning
 */
@Table(
		TableName = Plugin_Table.TABLE_NAME,
		TableColumns = Plugin_Column.class,
		DAO = PluginBaseDAO.class

)
public class Plugin_Table extends BaseDBTable {
	
	public static final String TABLE_NAME = "plugin";
	
	public Plugin_Table(BaseDB database) {
		super(database);
	}

	@Override
	protected void onCreateTableOver(SQLiteDatabase db) {
		PluginBaseDAO dao = PluginDaoFactoryImpl.getInstance().buildDAO(PluginBaseDAO.class);
		List<Plugin_Model> models = new ArrayList<Plugin_Model>();
		Plugin_Model model = new Plugin_Model();
		model.plugin_id=DBBasedPluginManager.PLUGIN_ID_ATTETION;
		model.plugin_jid = "attention";
		model.plugin_desc="将好友设为特别关注，第一时间获取Ta的状态和照片！";
		model.plugin_name="人人好友特别关注";
		model.plugin_icon_url="cy_renren_icon.png";
		model.plugin_type=1;
		model.plugin_usage=3;
		model.plugin_status=1;
		model.plugin_push=1;
		model.plugin_class_name="com.renren.mobile.chat.ui.plugins.AttentionPlugin";
		model.plugin_url="null";
		model.plugin_namespace="native";
		model.plugin_access_apis="0";
		model.plugin_group=0;
		model.plugin_message_type=0;
		model.plugin_codec=0;
		model.plugin_config_push=1;
		model.plugin_category=0;
		model.plugin_default_setting="null";

	    SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");     
	    String date = sDateFormat.format(new java.util.Date());  
		model.plugin_creat_time=date;

		models.add(model);
		
		Plugin_Model another= new Plugin_Model();
		another.plugin_id=DBBasedPluginManager.PLUGIN_ID_BRUSH;
		another.plugin_jid="brush";
		another.plugin_desc="我能帮助你写出大师级的字体哦，亲！";
		another.plugin_name="毛笔字";
		another.plugin_icon_url="plugin_brush.png";
		another.plugin_type=1;
		another.plugin_usage=1;
		another.plugin_status=1;
		another.plugin_push=1;
		another.plugin_class_name="com.renren.mobile.chat.ui.plugins.BrushPlugin";
		model.plugin_url="null";
		another.plugin_namespace="native";
		another.plugin_access_apis="0";
		another.plugin_group=0;
		another.plugin_message_type=0;
		another.plugin_codec=0;
		another.plugin_config_push=1;
		another.plugin_category=0;
		another.plugin_default_setting="null";

	    date = sDateFormat.format(new java.util.Date());  
	    another.plugin_creat_time=date;
	    
		models.add(another);
		
		dao.initPlugin(models, db);
		super.onCreateTableOver(db);
	}

}
