package com.common.emotion.sql;

import android.database.Cursor;

import com.common.emotion.model.EmotionThemeModel;
import com.core.database.BaseDAO;
import com.core.database.Query;
import com.core.orm.ORMUtil;

public class Query_EmotionTheme extends Query {

	public Query_EmotionTheme(BaseDAO dao) {
		super(dao);
		
	}

	@Override
	public Object warpData(Cursor c) {
		EmotionThemeModel model = null;
		
		if(c!=null&&c.getCount()>0){
			c.moveToFirst();
			model = new EmotionThemeModel();
			ORMUtil.getInstance().ormQuery(EmotionThemeModel.class, model, c);
		}
		return model;
	}

}
