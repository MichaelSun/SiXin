package com.renren.mobile.chat.sql;

import android.database.Cursor;

import com.core.database.BaseDAO;
import com.core.database.Query;
import com.core.orm.ORMUtil;
import com.renren.mobile.chat.ui.contact.ThirdContactModel;

public class Query_Renren_Contact extends Query {

	public Query_Renren_Contact(BaseDAO dao) {
		super(dao);
	}

	@Override
	public Object warpData(Cursor c) {
		// TODO Auto-generated method stub
		if(c.getCount()>0){
			ThirdContactModel model = new ThirdContactModel();
			c.moveToFirst();
			ORMUtil.getInstance().ormQuery(ThirdContactModel.class, model, c);
			return model;
		}
		return null;
	}

}
