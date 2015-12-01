package plugin.database;

import android.content.Context;
import com.core.database.BaseDB;
import com.core.database.Database;

@Database(
		tables = { 
				/**
				 * 
				 * @author changsheng.ning
				 *  插件表
				 */
				Plugin_Table.class,
				/**
				 * 
				 * @author changsheng.ning
				 *  用户插件订购表
				 */
				User_Plugin_Table.class,
                /**
                 * @author xiaobo.yuan
                 */
                Plugin_Message_Table.class
		}
)
public class Plugin_DB extends BaseDB {
	/**插件数据库*/
	public static final String PLUGIN_DB_NAME = "plugin.db";
	public static final int PLUGIN_DB_VERSION = 10;
	public Plugin_DB(Context context) {
		super(context, PLUGIN_DB_NAME, PLUGIN_DB_VERSION, PluginDaoFactoryImpl.getInstance());
	}

}
