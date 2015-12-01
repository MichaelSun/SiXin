package com.renren.mobile.chat.dao;

import java.util.LinkedList;
import java.util.List;

import android.content.ContentValues;

import com.common.manager.LoginManager;
import com.core.database.BaseDAO;
import com.core.database.BaseDBTable;
import com.core.database.Query;
import com.core.orm.ORMUtil;
import com.renren.mobile.chat.RenrenChatApplication;
import com.renren.mobile.chat.actions.models.SynHistoryRecordModel;
import com.renren.mobile.chat.base.util.SystemUtil;
import com.renren.mobile.chat.database.SynHistoryRecord_Column;
import com.renren.mobile.chat.sql.QuerySynHistoryRecord;

public class SynHistoryRecordDAO extends BaseDAO{

    Query mQuerySynHistoryInfo = new QuerySynHistoryRecord(this);

    private List<SynHistoryRecordDAOObserver> mObservers = new LinkedList<SynHistoryRecordDAOObserver>();

	public SynHistoryRecordDAO(BaseDBTable table) {
		super(table);
	}

    /**
     * @author xiaoguang.zhang
     * 插入一条
     * */
    public  void  insertSynHitoryRecordInfo(SynHistoryRecordModel model){
        ContentValues values = new ContentValues();
        ORMUtil.getInstance().ormInsert(model.getClass(), model, values);
        mInsert.insert(values);
        onInsert(model);
    }

    public void onInsert(final SynHistoryRecordModel message){
        RenrenChatApplication.sHandler.post(new Runnable() {
            public void run() {
                for(SynHistoryRecordDAOObserver observer : mObservers){
                    observer.onInsert(message);
                }
            }
        });

    }

    public void onDelete(final String columnName,final long _id){
        RenrenChatApplication.sHandler.post(new Runnable() {
            public void run() {
                for(SynHistoryRecordDAOObserver observer :mObservers){
                    observer.onDelete(columnName,_id);
                }
            }
        });
    }

    public void onUpdate(final String column,final long _id,final ContentValues values){
        RenrenChatApplication.sHandler.post(new Runnable() {
            public void run() {
                for(SynHistoryRecordDAOObserver observer :mObservers){
                    observer.onUpdate(column, _id, values);
                }
            }
        });
    }

    public  SynHistoryRecordModel querySynHitoryRecordInfo(long toChatId){
        String[] where = {SynHistoryRecord_Column.TO_CHAT_ID + " = " + toChatId,
                SynHistoryRecord_Column.LOCAL_ID + " = " + LoginManager.getInstance().mLoginInfo.mUserId};
        SynHistoryRecordModel message = (SynHistoryRecordModel) this.query1(mQuerySynHistoryInfo, null, where, null, null, null);
        return message;
    }


    public  void updateSynHitoryRecordInfo(long toChatId,SynHistoryRecordModel synHistoryRecordInfo){
        ContentValues values = new ContentValues(4);
        values.put(SynHistoryRecord_Column.TO_CHAT_ID, toChatId);
        values.put(SynHistoryRecord_Column.LAST_SYN_TIME, synHistoryRecordInfo.mLastSynTime);
        String where = SynHistoryRecord_Column.TO_CHAT_ID + " = " + toChatId;
        int successId = mUpdate.update(values, where);
        if(successId <= 0) {
            insertSynHitoryRecordInfo(synHistoryRecordInfo);
        	onInsert(synHistoryRecordInfo);
        	 
        } else {
            onUpdate(SynHistoryRecord_Column.TO_CHAT_ID, toChatId, values);
        }
        
    }

    public  void deleteSynHitoryRecordInfo(long toChatId){
        String where = SynHistoryRecord_Column.TO_CHAT_ID + " = " + toChatId;
        mDelete.delete(where);
        onDelete(SynHistoryRecord_Column.TO_CHAT_ID, toChatId);
    }

    public void deleteAll() {
        this.delete2(null);
        this.onDelete(SynHistoryRecord_Column.TO_CHAT_ID, 0);
    }

    /**
     * 删除所有房间
     */
    public void deleteAllSynHitoryRecords(){
        mDelete.delete(null);
    }

    public void registorObserver(SynHistoryRecordDAOObserver observer){
        if(!this.mObservers.contains(observer)){
            this.mObservers.add(observer);
        }
    }

    public void unregistorObserver(SynHistoryRecordDAOObserver observer){
        this.mObservers.remove(observer);
    }

}
