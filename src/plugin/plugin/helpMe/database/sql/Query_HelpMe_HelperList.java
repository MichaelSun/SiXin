package plugin.helpMe.database.sql;

import java.util.ArrayList;
import java.util.List;
import plugin.helpMe.database.HelpMe_Helper_Model;
import android.database.Cursor;
import com.core.database.BaseDAO;
import com.core.database.Query;
import com.core.orm.ORMUtil;

public class Query_HelpMe_HelperList extends Query{

	public Query_HelpMe_HelperList(BaseDAO dao) {
		super(dao);
	}

	@Override
	public Object warpData(Cursor c) {
		if (c!=null && c.getCount()>0) {
			List<HelpMe_Helper_Model> modelList = new ArrayList<HelpMe_Helper_Model>();
			for(c.moveToFirst();!c.isAfterLast();c.moveToNext()){
				HelpMe_Helper_Model model = new HelpMe_Helper_Model();
				ORMUtil.getInstance().ormQuery(HelpMe_Helper_Model.class, model, c);
				modelList.add(model);
			}
			return modelList;
		}
		return null;
	}

}
