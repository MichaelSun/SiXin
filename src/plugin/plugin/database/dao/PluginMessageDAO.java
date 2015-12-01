package plugin.database.dao;

import android.content.ContentValues;
import com.core.database.*;
import com.core.orm.ORMUtil;
import plugin.database.Plugin_Message_Column;
import plugin.database.Plugin_Message_Model;
import plugin.database.sql.Query_Plugin_Message;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: tiger
 * Date: 9/18/12
 * Time: 2:49 PM
 */
public class PluginMessageDAO extends BaseDAO{

    private Insert mInsert = new Insert(this);
	private Delete mDelete = new Delete(this);
	private Update mUpdate = new Update(this);
	private Query mQuery_Plugin_message = new Query_Plugin_Message(this);

    public PluginMessageDAO(BaseDBTable table) {
        super(table);
    }

   public void insertMessage(Plugin_Message_Model model){
       ContentValues values = new ContentValues();
       ORMUtil.getInstance().ormInsert(Plugin_Message_Model.class, model, values);
       mInsert.insert(values);
   }

   public void insertMessages(List<Plugin_Message_Model> models){
       if (models != null && !models.isEmpty()) {
			for (Plugin_Message_Model model : models) {
				this.insertMessage(model);
			}
		}
   }

    public long deleteMessagesByPluginId(int pluginId){
        String whereString = Plugin_Message_Column.PLUGIN_ID + " = " + pluginId;
		long num = mDelete.delete(whereString);
		this.closeDataBase();
		return num;
    }

    public List<Plugin_Message_Model> queryMessagesByPluginId(int pluginId){
        String[] whereString = {
            Plugin_Message_Column.PLUGIN_ID + " = " + pluginId
        };
        @SuppressWarnings("unchecked")
        List<Plugin_Message_Model> list = (List<Plugin_Message_Model>) this.query1(mQuery_Plugin_message, null, whereString, null, null, null);
        return list;
    }
}
