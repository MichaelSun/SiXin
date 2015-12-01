package plugin.database;

import plugin.database.dao.UserPluginBaseDAO;
import android.database.sqlite.SQLiteDatabase;

import com.core.database.BaseDB;
import com.core.database.BaseDBTable;
import com.core.database.Table;


/**
 * @author changsheng.ning
 */
@Table(
		TableName = User_Plugin_Table.TABLE_NAME,
		TableColumns = User_Plugin_Column.class,
		DAO = UserPluginBaseDAO.class

)
public class User_Plugin_Table extends BaseDBTable {
	
	public static final String TABLE_NAME = "user_plugin";
	
	public User_Plugin_Table(BaseDB database) {
		super(database);
	}
	
	@Override
	protected void onCreateTableOver(SQLiteDatabase db) {
		// User_Plugin表创建完毕后的回调方法
		super.onCreateTableOver(db);
	}
}
