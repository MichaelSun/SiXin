package com.renren.mobile.chat.sql;

import java.util.ArrayList;

import android.database.Cursor;

import com.core.database.BaseDAO;
import com.core.database.Query;
import com.core.orm.ORMUtil;
import com.renren.mobile.account.LoginfoModel;

public class Query_Account extends Query{

	public Query_Account(BaseDAO dao) {
		super(dao);
	}

	/*@Override
	public Object warpData(Cursor c) {
		LoginfoModel model = null;
		if(c.getCount()>0){
			model = new LoginfoModel();
			c.moveToFirst();
			ORMUtil.getInstance().ormQuery(LoginfoModel.class, model, c);
		}
		return model;
	}*/
	
	/**
	 * @author kaining.yang
	 * 表中存储所有登陆过的账户信息
	 */
	@Override
	public Object warpData(Cursor c) {
		ArrayList<LoginfoModel> mModleList = new ArrayList<LoginfoModel>();
		if (c.getCount() > 0) {
			c.moveToFirst();
		}
		while (!c.isAfterLast()) {
			LoginfoModel model = new LoginfoModel();
			ORMUtil.getInstance().ormQuery(LoginfoModel.class, model, c);
			mModleList.add(model);
			c.moveToNext();
		}
		return mModleList;
	}

}