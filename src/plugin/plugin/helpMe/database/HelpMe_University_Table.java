package plugin.helpMe.database;

import plugin.helpMe.database.dao.HelpMeUniversityBaseDAO;
import android.database.sqlite.SQLiteDatabase;

import com.core.database.BaseDB;
import com.core.database.BaseDBTable;
import com.core.database.Table;

/**
 * @author changsheng.ning
 */
@Table(
		TableName = HelpMe_University_Table.TABLE_NAME,
		TableColumns = HelpMe_University_Column.class,
		DAO = HelpMeUniversityBaseDAO.class

)
public class HelpMe_University_Table extends BaseDBTable {

	public static final String TABLE_NAME = "helpme_university";
	
	public HelpMe_University_Table(BaseDB database) {
		super(database);
	}
	
	@Override
	protected void onCreateTableOver(SQLiteDatabase db) {
		// HelpMe_University表创建完毕后的回调方法
		super.onCreateTableOver(db);
	}
	
}
