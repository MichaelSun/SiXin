package com.renren.mobile.chat.sql;

import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;

import com.core.database.BaseDAO;
import com.core.database.Query;
import com.core.orm.ORMUtil;
import com.renren.mobile.chat.base.util.SystemUtil;
import com.renren.mobile.chat.ui.contact.CommonContactModel;

public class Query_CommonContacts extends Query{

	public Query_CommonContacts(BaseDAO dao) {super(dao);}

	@Override
	public Object warpData(Cursor c) {
		List<CommonContactModel> data = new ArrayList<CommonContactModel>();
		SystemUtil.errord("size===="+c.getCount());
		if(c.getCount()>0){
			for(c.moveToFirst();!c.isAfterLast();c.moveToNext()){
				CommonContactModel model = new CommonContactModel();
				ORMUtil.getInstance().ormQuery(CommonContactModel.class, model, c);
				data.add(model);
			}
		}
		return data;
	}
}
