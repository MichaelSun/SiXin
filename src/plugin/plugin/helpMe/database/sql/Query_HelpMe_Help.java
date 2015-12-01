package plugin.helpMe.database.sql;

import plugin.helpMe.database.HelpMe_Help_Model;
import android.database.Cursor;
import com.core.database.BaseDAO;
import com.core.database.Query;
import com.core.orm.ORMUtil;

public class Query_HelpMe_Help extends Query {

	public Query_HelpMe_Help(BaseDAO dao) {
		super(dao);
	}

	@Override
	public Object warpData(Cursor c) {
		if (c!=null && c.getCount()>0) {
			HelpMe_Help_Model model = new HelpMe_Help_Model();
			c.moveToFirst();
			ORMUtil.getInstance().ormQuery(HelpMe_Help_Model.class, model, c);
			return model;
		}
		return null;
	}

}
