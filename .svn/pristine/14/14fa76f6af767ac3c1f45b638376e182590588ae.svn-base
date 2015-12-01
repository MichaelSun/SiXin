package plugin.helpMe.database.sql;

import plugin.helpMe.database.HelpMe_University_Model;
import android.database.Cursor;
import com.core.database.BaseDAO;
import com.core.database.Query;
import com.core.orm.ORMUtil;

public class Query_HelpMe_University extends Query {

	public Query_HelpMe_University(BaseDAO dao) {
		super(dao);
	}

	@Override
	public Object warpData(Cursor c) {
		if (c!=null && c.getCount()>0) {
			HelpMe_University_Model model = new HelpMe_University_Model();
			c.moveToFirst();
			ORMUtil.getInstance().ormQuery(HelpMe_University_Model.class, model, c);
			return model;
		}
		return null;
	}

}
