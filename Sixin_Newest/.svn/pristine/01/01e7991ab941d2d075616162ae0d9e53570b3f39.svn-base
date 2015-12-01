package com.renren.mobile.chat.dao;

import java.util.ArrayList;

import android.content.ContentValues;

import com.common.manager.LoginManager;
import com.core.database.BaseDAO;
import com.core.database.BaseDBTable;
import com.core.database.Query;
import com.core.orm.ORMUtil;
import com.renren.mobile.chat.actions.models.RoomInfoModelWarpper;
import com.renren.mobile.chat.database.Room_Column;
import com.renren.mobile.chat.sql.Query_RoomInfo;
import com.renren.mobile.chat.sql.Query_RoomInfo_List;

/**
 * @author dingwei.chen
 * */
public class RoomDAO extends BaseDAO{

	Query mQuery_RoomInfo = new Query_RoomInfo(this);
	Query mQuery_RoomInfo_List= new Query_RoomInfo_List(this);
	
	public RoomDAO(BaseDBTable table) {
		super(table);
	}
	

	/**
	 * @author dingwei.chen
	 * 插入一条群信息
	 * */
	public  void  insert_RoomInfo(RoomInfoModelWarpper model){
		ContentValues values = new ContentValues();
		ORMUtil.getInstance().ormInsert(model.getClass(), model, values);
		mInsert.insert(values);
	}
	
	public  RoomInfoModelWarpper query_RoomInfoFromDB(long roomId){
		String where = Room_Column.ROOM_ID+" = "+roomId + " and "+Room_Column.LOCAL_USER_ID+" = "+LoginManager.getInstance().getLoginInfo().mUserId;
		RoomInfoModelWarpper message = mQuery_RoomInfo.query(null, where, null, null, null, RoomInfoModelWarpper.class);
		return message;
	}
	
	/**
	 * 查询群信息列表
	 * **/
	public  ArrayList<RoomInfoModelWarpper> query_RoomInfo_List(){
		String where = Room_Column.LOCAL_USER_ID+" = "+LoginManager.getInstance().getLoginInfo().mUserId;
		return  mQuery_RoomInfo_List.query(null, where, null, null, null,ArrayList.class);
	}
	
	
	/**
	 * @author dingwei.chen
	 * */
	public void insert_RoomInfoToContact(RoomInfoModelWarpper model,long fromId){
		
	}
	
	public  void update_RoomInfo(long fromId,long roomId,RoomInfoModelWarpper roominfo){
		String where = Room_Column.ROOM_ID +" = "+roomId+" and "+Room_Column.LOCAL_USER_ID+" = "+fromId;
		mDelete.delete(where);
		this.insert_RoomInfo(roominfo);
	}
	
	public  void delete_RoomInfo(long fromId,long roomId){
		String where = Room_Column.ROOM_ID +" = "+roomId+" and "+Room_Column.LOCAL_USER_ID+" = "+fromId;
		mDelete.delete(where);
	}
	
	/**
	 * 删除所有房间
	 */
	public void delete_all_rooms(){
		mDelete.delete(null);
	}
	
	
	
	
}
