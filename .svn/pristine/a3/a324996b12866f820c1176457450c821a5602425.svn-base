package plugin.database.sql;

import android.database.Cursor;
import com.core.database.BaseDAO;
import com.core.database.Query;
import com.core.orm.ORMUtil;
import plugin.database.Plugin_Message_Model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: xiaobo.yuan
 * Date: 9/18/12
 * Time: 3:30 PM
 */
public class Query_Plugin_Message extends Query {

    public Query_Plugin_Message(BaseDAO dao) {
        super(dao);
    }

    @Override
    public Object warpData(Cursor c) {
        if(c != null && c.getCount()>0){
            List<Plugin_Message_Model> pluginMessageList = new ArrayList<Plugin_Message_Model>();
            for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()){
                Plugin_Message_Model model = new Plugin_Message_Model();
                ORMUtil.getInstance().ormQuery(Plugin_Message_Model.class, model, c);
                pluginMessageList.add(model);
            }
            return pluginMessageList;

        }
        return null;
    }
}
