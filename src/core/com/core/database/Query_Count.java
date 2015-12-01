package com.core.database;

import android.database.Cursor;

/**
 * @author dingwei.chen
 * @说明 查询记录总数
 * */
class Query_Count extends Query{

	public Query_Count(BaseDAO dao) {
		super(dao);
	}

	@Override
	public Object warpData(Cursor c) {
		
		int count = 0;
		if(c.getCount()!=0){
			c.moveToFirst();
			count = c.getInt(c.getColumnIndex(DatabaseColumn.COUNT));
		}
		return count;
	}

}
