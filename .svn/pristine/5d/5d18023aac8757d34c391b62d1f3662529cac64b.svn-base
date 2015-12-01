package plugin.helpMe.database;

import plugin.helpMe.database.dao.HelpMeHelpBaseDAO;
import android.database.sqlite.SQLiteDatabase;

import com.core.database.BaseDB;
import com.core.database.BaseDBTable;
import com.core.database.Table;

/**
 * @author changsheng.ning
 */
@Table(
		TableName = HelpMe_Help_Table.TABLE_NAME,
		TableColumns = HelpMe_Help_Column.class,
		DAO = HelpMeHelpBaseDAO.class

)
public class HelpMe_Help_Table extends BaseDBTable {

	public static final String TABLE_NAME = "helpme_help";
	
	public HelpMe_Help_Table(BaseDB database) {
		super(database);
	}
	
	@Override
	protected void onCreateTableOver(SQLiteDatabase db) {
		// HelpMe_Help表创建完毕后的回调方法
		super.onCreateTableOver(db);
	}

}
