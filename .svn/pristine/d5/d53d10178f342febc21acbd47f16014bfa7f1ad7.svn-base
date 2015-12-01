package com.common.emotion.sql;

import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;

import com.common.emotion.model.EmotionBaseModel;
import com.core.database.BaseDAO;
import com.core.database.Query;
import com.core.orm.ORMUtil;

public class Query_EmotionList extends Query {

	public Query_EmotionList(BaseDAO dao) {
		super(dao);
	}

	@Override
	public Object warpData(Cursor c) {
		
		if(c!=null&&c.getCount()>0){
			List<EmotionBaseModel> emotionlist = new ArrayList<EmotionBaseModel>();
			for(c.moveToFirst();!c.isAfterLast();c.moveToNext()){
				EmotionBaseModel model = new EmotionBaseModel();
				ORMUtil.getInstance().ormQuery(EmotionBaseModel.class, model, c);
				emotionlist.add(model);
			}
			return emotionlist;
		}
		return null;
	}

}
