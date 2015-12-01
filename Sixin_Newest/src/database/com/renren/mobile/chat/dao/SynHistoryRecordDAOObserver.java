package com.renren.mobile.chat.dao;

import com.renren.mobile.chat.actions.models.SynHistoryRecordModel;

import android.content.ContentValues;

/**
 * SynHistoryRecordDAOObserver
 * @author : xiaoguang.zhang
 * Date: 12-9-3
 * Time: 上午11:32
 * @说明 : 对同步聊天记录的数据库表的增删改做监听
 */
public interface SynHistoryRecordDAOObserver {
    public void onInsert(SynHistoryRecordModel message);
    public void onDelete(String columnName,long _id);
    public void onUpdate(String column,long _id,ContentValues values);
}
