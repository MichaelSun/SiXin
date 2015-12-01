package com.common.emotion.sql;

import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;

import com.common.emotion.model.EmotionThemeModel;
import com.core.database.BaseDAO;
import com.core.database.Query;
import com.core.orm.ORMUtil;

public class Query_EmotionThemeList extends Query {

	public Query_EmotionThemeList(BaseDAO dao) {
		super(dao);
	}

	@Override
	public Object warpData(Cursor c) {
		List<EmotionThemeModel> modelList = null;
		if(c!=null&&c.getCount()>0){
			modelList = new ArrayList<EmotionThemeModel>();
			c.moveToFirst();
			while(!c.isAfterLast()){
				EmotionThemeModel model = new EmotionThemeModel();
				ORMUtil.getInstance().ormQuery(EmotionThemeModel.class, model, c);
				modelList.add(model);
				c.moveToNext();
			}
		}
		return modelList;
	}


}
