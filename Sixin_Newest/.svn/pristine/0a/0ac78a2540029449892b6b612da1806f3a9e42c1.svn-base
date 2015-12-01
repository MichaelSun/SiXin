package com.renren.mobile.chat.sql;

import android.database.Cursor;
import com.core.database.BaseDAO;
import com.core.database.Query;
import com.core.orm.ORMUtil;
import com.renren.mobile.chat.actions.models.SynHistoryRecordModel;

/**
 * QuerySynHistoryRecord
 * @author : xiaoguang.zhang
 * Date: 12-8-31
 * Time: 下午7:49
 * @说明 : 用来搜索同步聊天的最后时间的表
 */
public class QuerySynHistoryRecord extends Query {

    public QuerySynHistoryRecord(BaseDAO dao) {
        super(dao);
    }

    @Override
    public Object warpData(Cursor c) {

        SynHistoryRecordModel message = new SynHistoryRecordModel();
        if(c.moveToFirst()){
            ORMUtil.getInstance().ormQuery(SynHistoryRecordModel.class, message, c);//取出
        }
        return message;
    }
}
