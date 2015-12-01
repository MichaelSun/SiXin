package com.common.emotion.sql;

import java.util.ArrayList;
import java.util.List;
import android.database.Cursor;

import com.common.emotion.model.EmotionPackageModel;
import com.core.database.BaseDAO;
import com.core.database.Query;
import com.core.orm.ORMUtil;

public class Query_EmotionPackageList extends Query {

	public Query_EmotionPackageList(BaseDAO dao) {
		super(dao);
	}

	@Override
	public Object warpData(Cursor c) {
		if(c!=null&&c.getCount()>0){
			List<EmotionPackageModel> emotionlist = new ArrayList<EmotionPackageModel>();
			for(c.moveToFirst();!c.isAfterLast();c.moveToNext()){
				EmotionPackageModel model = new EmotionPackageModel();
				ORMUtil.getInstance().ormQuery(EmotionPackageModel.class, model, c);
				emotionlist.add(model);
			}
			
			return emotionlist;
		}
		return null;
	}

}
