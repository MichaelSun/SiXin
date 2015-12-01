package com.common.emotion.sql;

import android.database.Cursor;

import com.common.emotion.model.EmotionPackageModel;
import com.core.database.BaseDAO;
import com.core.database.Query;
import com.core.orm.ORMUtil;

public class Query_EmotionPackage extends Query {

	public Query_EmotionPackage(BaseDAO dao) {
		super(dao);
	}

	@Override
	public Object warpData(Cursor c) {
		if(c!=null&&c.getCount()>0){
			EmotionPackageModel model = new EmotionPackageModel();
			c.moveToFirst();
			ORMUtil.getInstance().ormQuery(EmotionPackageModel.class, model, c);
			return model;
		}
		return null;
	}

}
