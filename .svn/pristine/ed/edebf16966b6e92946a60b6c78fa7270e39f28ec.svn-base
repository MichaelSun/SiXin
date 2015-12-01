package plugin.helpMe.database;

import plugin.helpMe.database.HelpMe_Help_Table;
import plugin.helpMe.database.HelpMe_Helper_Table;
import plugin.helpMe.database.HelpMe_University_Table;
import android.content.Context;
import com.core.database.BaseDB;
import com.core.database.Database;

@Database(
		tables = {
				/**
				 * 
				 * @author changsheng.ning
				 *  师兄帮帮我插件的求助表
				 */
				HelpMe_Help_Table.class,
				/**
				 * 
				 * @author changsheng.ning
				 *  师兄帮帮我插件的援助表
				 */
				HelpMe_Helper_Table.class,
				/**
				 * 
				 * @author changsheng.ning
				 *  师兄帮帮我插件的学校表
				 */
				HelpMe_University_Table.class
		}
)
public class HelpMe_DB extends BaseDB {

	public static final String PLUGIN_DB_NAME = "plugin_helpme.db";
	public static final int PLUGIN_DB_VERSION = 1;
	
	public HelpMe_DB(Context context) {
		super(context, PLUGIN_DB_NAME, PLUGIN_DB_VERSION, HelpMeDaoFactoryImpl.getInstance());
	}

}
