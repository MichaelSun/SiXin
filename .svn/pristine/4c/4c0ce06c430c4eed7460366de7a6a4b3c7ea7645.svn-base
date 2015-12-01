package plugin.helpMe.database.sql;

import java.util.ArrayList;
import java.util.List;
import plugin.helpMe.database.HelpMe_Help_Model;
import android.database.Cursor;
import com.core.database.BaseDAO;
import com.core.database.Query;
import com.core.orm.ORMUtil;

public class Query_HelpMe_HelpList extends Query {

	public Query_HelpMe_HelpList(BaseDAO dao) {
		super(dao);
	}

	@Override
	public Object warpData(Cursor c) {
		if (c!=null && c.getCount()>0) {
			List<HelpMe_Help_Model> modelList = new ArrayList<HelpMe_Help_Model>();
			for(c.moveToFirst();!c.isAfterLast();c.moveToNext()){
				HelpMe_Help_Model model = new HelpMe_Help_Model();
				ORMUtil.getInstance().ormQuery(HelpMe_Help_Model.class, model, c);
				modelList.add(model);
			}
			return modelList;
		}
		return null;
	}

}
