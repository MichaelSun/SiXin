package plugin.helpMe.database;

import plugin.helpMe.database.dao.HelpMeHelperBaseDAO;
import android.database.sqlite.SQLiteDatabase;

import com.core.database.BaseDB;
import com.core.database.BaseDBTable;
import com.core.database.Table;

/**
 * @author changsheng.ning
 */
@Table(
		TableName = HelpMe_Helper_Table.TABLE_NAME,
		TableColumns = HelpMe_Helper_Column.class,
		DAO = HelpMeHelperBaseDAO.class

)
public class HelpMe_Helper_Table extends BaseDBTable {

	public static final String TABLE_NAME = "helpme_helper";
	
	public HelpMe_Helper_Table(BaseDB database) {
		super(database);
	}
	
	@Override
	protected void onCreateTableOver(SQLiteDatabase db) {
		// HelpMe_Helper表创建完毕后的回调方法
		super.onCreateTableOver(db);
	}
	
}
