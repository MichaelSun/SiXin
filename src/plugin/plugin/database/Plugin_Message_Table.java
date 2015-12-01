package plugin.database;

import android.database.sqlite.SQLiteDatabase;
import com.core.database.BaseDB;
import com.core.database.BaseDBTable;
import com.core.database.Table;
import plugin.database.dao.PluginMessageDAO;

/**
 * Created with IntelliJ IDEA.
 * User: xiaobo.yuan
 * Date: 9/18/12
 * Time: 2:44 PM
 */
@Table(
        TableName = Plugin_Message_Table.TABLE_NAME,
        TableColumns = Plugin_Message_Column.class,
        DAO = PluginMessageDAO.class

)
public class Plugin_Message_Table extends BaseDBTable{

    public static final String TABLE_NAME = "plugin_message";

    public Plugin_Message_Table(BaseDB database) {
        super(database);
    }

    @Override
    protected void onCreateTableOver(SQLiteDatabase db) {
        super.onCreateTableOver(db);
    }
}
