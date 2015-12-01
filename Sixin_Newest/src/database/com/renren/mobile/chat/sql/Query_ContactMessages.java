package com.renren.mobile.chat.sql;

import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;

import com.core.database.BaseDAO;
import com.core.database.Query;
import com.core.orm.ORMUtil;
import com.renren.mobile.chat.ui.contact.ContactMessageModel;


public class Query_ContactMessages extends Query{

	public Query_ContactMessages(BaseDAO dao) {super(dao);}

	@Override
	public Object warpData(Cursor c) {
		List<ContactMessageModel> data = new ArrayList<ContactMessageModel>();
		if(c.getCount()>0){
			for(c.moveToFirst();!c.isAfterLast();c.moveToNext()){
				ContactMessageModel model = new ContactMessageModel();
				ORMUtil.getInstance().ormQuery(ContactMessageModel.class, model, c);
				data.add(model);
			}
		}
		return data;
	}

}
