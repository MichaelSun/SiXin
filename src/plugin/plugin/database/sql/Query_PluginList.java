package plugin.database.sql;

import java.util.ArrayList;
import java.util.List;
import plugin.database.Plugin_Model;
import android.database.Cursor;
import com.core.database.BaseDAO;
import com.core.database.Query;
import com.core.orm.ORMUtil;

public class Query_PluginList extends Query {

	public Query_PluginList(BaseDAO dao) {
		super(dao);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Object warpData(Cursor c) {
		if(c!=null&&c.getCount()>0){
			List<Plugin_Model> PluginList = new ArrayList<Plugin_Model>();
			for(c.moveToFirst();!c.isAfterLast();c.moveToNext()){
				Plugin_Model plugin_Model = new Plugin_Model();
				ORMUtil.getInstance().ormQuery(Plugin_Model.class, plugin_Model, c);
				PluginList.add(plugin_Model);
			}
			return PluginList;
		}
		return null;
	}
	
}
