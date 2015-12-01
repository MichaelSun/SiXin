package plugin.helpMe.database.sql;

import java.util.ArrayList;
import java.util.List;
import plugin.helpMe.database.HelpMe_University_Model;
import android.database.Cursor;
import com.core.database.BaseDAO;
import com.core.database.Query;
import com.core.orm.ORMUtil;

public class Query_HelpMe_UniversityList extends Query {

	public Query_HelpMe_UniversityList(BaseDAO dao) {
		super(dao);
	}

	@Override
	public Object warpData(Cursor c) {
		if (c!=null && c.getCount()>0) {
			List<HelpMe_University_Model> modelList = new ArrayList<HelpMe_University_Model>();
			for(c.moveToFirst();!c.isAfterLast();c.moveToNext()){
				HelpMe_University_Model model = new HelpMe_University_Model();
				ORMUtil.getInstance().ormQuery(HelpMe_University_Model.class, model, c);
				modelList.add(model);
			}
			
			return modelList;
		}
		return null;
	}
}
