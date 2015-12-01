package com.renren.mobile.chat.sql;

import android.database.Cursor;

import com.core.database.BaseDAO;
import com.core.database.Query;
import com.core.orm.ORMUtil;
import com.renren.mobile.chat.ui.contact.ContactModel;

public class Query_ContactMessage extends Query {

	public Query_ContactMessage(BaseDAO dao) {
		super(dao);
	}

	@Override
	public Object warpData(Cursor c) {
		// TODO Auto-generated method stub
		if(c.getCount()>0){
			ContactModel model = new ContactModel();
			c.moveToFirst();
			ORMUtil.getInstance().ormQuery(ContactModel.class, model, c);
			return model;
		}
		return null;
	}

}
