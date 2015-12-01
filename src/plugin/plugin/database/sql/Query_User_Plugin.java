package plugin.database.sql;

import plugin.database.User_Plugin_Model;
import android.database.Cursor;
import com.core.database.BaseDAO;
import com.core.database.Query;
import com.core.orm.ORMUtil;

public class Query_User_Plugin extends Query{

	public Query_User_Plugin(BaseDAO dao) {
		super(dao);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Object warpData(Cursor c) {
		if (c!=null && c.getCount()>0) {
			User_Plugin_Model model = new User_Plugin_Model();  //暂时指定插件类型为local插件
			c.moveToFirst();
			ORMUtil.getInstance().ormQuery(User_Plugin_Model.class, model, c);
			return model;
		}
		return null;
	}

}
