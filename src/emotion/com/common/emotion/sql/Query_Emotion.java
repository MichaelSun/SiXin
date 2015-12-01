package com.common.emotion.sql;

import android.database.Cursor;

import com.common.emotion.model.EmotionBaseModel;
import com.core.database.BaseDAO;
import com.core.database.Query;
import com.core.orm.ORMUtil;

public class Query_Emotion extends Query {

	public Query_Emotion(BaseDAO dao) {
		super(dao);
	}

	@Override
	public Object warpData(Cursor c) {
		if(c!=null&&c.getCount()>0){
			EmotionBaseModel model = new EmotionBaseModel();
			c.moveToFirst();
			ORMUtil.getInstance().ormQuery(EmotionBaseModel.class, model, c);
			return model;
		}
		return null;
	}

}
