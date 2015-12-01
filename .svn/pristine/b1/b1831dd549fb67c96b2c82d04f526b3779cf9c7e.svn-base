package plugin.database.sql;

import plugin.database.Plugin_Model;
import android.database.Cursor;
import com.core.database.BaseDAO;
import com.core.database.Query;
import com.core.orm.ORMUtil;

public class Query_Plugin extends Query {

	public Query_Plugin(BaseDAO dao) {
		super(dao);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Object warpData(Cursor c) {
		if (c!=null && c.getCount()>0) {
			Plugin_Model model = new Plugin_Model();
			c.moveToFirst();
			ORMUtil.getInstance().ormQuery(Plugin_Model.class, model, c);
			return model;
		}
		return null;
	}

}
