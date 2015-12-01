package plugin.helpMe.database.sql;

import plugin.helpMe.database.HelpMe_Helper_Model;
import android.database.Cursor;
import com.core.database.BaseDAO;
import com.core.database.Query;
import com.core.orm.ORMUtil;

public class Query_HelpMe_Helper extends Query {

	public Query_HelpMe_Helper(BaseDAO dao) {
		super(dao);
	}

	@Override
	public Object warpData(Cursor c) {
		if (c!=null && c.getCount()>0) {
			HelpMe_Helper_Model model = new HelpMe_Helper_Model();
			c.moveToFirst();
			ORMUtil.getInstance().ormQuery(HelpMe_Helper_Model.class, model, c);
			return model;
		}
		return null;
	}

}
