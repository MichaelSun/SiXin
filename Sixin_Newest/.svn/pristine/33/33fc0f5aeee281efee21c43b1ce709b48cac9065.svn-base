package com.renren.mobile.chat.dao;

import java.util.List;

import android.content.ContentValues;

import com.core.database.BaseDAO;
import com.core.database.BaseDBTable;
import com.core.database.Query;
import com.core.orm.ORMUtil;
import com.renren.mobile.chat.base.util.SystemUtil;
import com.renren.mobile.chat.database.ContactMessage_Column;
import com.renren.mobile.chat.sql.Query_ContactMessage;
import com.renren.mobile.chat.sql.Query_ContactMessages;
import com.renren.mobile.chat.ui.contact.ContactMessageModel;
public class ContactMessageDAO extends BaseDAO {
	
    Query mQuery_All = new Query_ContactMessages(this);
    Query mQuery = new Query_ContactMessage(this);
	
	public ContactMessageDAO(BaseDBTable table) {
		super(table);
	}

	public List<ContactMessageModel> query_AllContactMessage() {
		@SuppressWarnings("unchecked")
		List<ContactMessageModel> list =  mQuery_All.query(null, null, null, null, null,  List.class);
		return list;
	}
	
	public void insert_Contacts(List<ContactMessageModel> contactList) {
		synchronized (contactList) {
			for(ContactMessageModel m:contactList){
				this.insert_Contact(m);
			}
		}
	}
	
	public void insert_Contact(ContactMessageModel contactModel) {
		ContentValues values = new ContentValues();
		ORMUtil.getInstance().ormInsert(ContactMessageModel.class, contactModel, values);
		mInsert.insert(values);
	}
	
	public void delete_contact_by_userId(long nativeID){
		String whereString =ContactMessage_Column.NATIVE_ID+" = "+nativeID;
		mDelete.delete(whereString);
	}
	
	public ContactMessageModel query_Contact_ByUserId(long gid) {
		String where = ContactMessage_Column.GID +" = "+gid;
		ContactMessageModel tmpContactMessageModel= mQuery.query(null, where, null, null, null, ContactMessageModel.class);
		return tmpContactMessageModel;
	}
	
//	public void updateContact(ContactMessageModel mode) {
//		ContentValues values = new ContentValues();
//		String where = ContactMessage_Column.GID +" = "+mode.getGid();
//		ORMUtil.getInstance().ormInsert(ContactMessageModel.class, mode, values);
//		mUpdate.update(values, where);
//	}
	
	public void delete_All(){
		if (SystemUtil.mDebug) {
			SystemUtil.logd("删除所有联系人推荐消息");
		}
		mDelete.delete(null);
	}

	public void clearUnReadCount() {
		ContentValues values = new ContentValues();
		values.put(ContactMessage_Column.READED,ContactMessageModel.READ_YES);
		mUpdate.update(values, null);
	}

	public void updateAdded(long nativeID, byte state) {
		ContentValues values = new ContentValues();
		String whereString =ContactMessage_Column.NATIVE_ID+" = "+nativeID;
		values.put(ContactMessage_Column.ADDED,state);
		mUpdate.update(values, whereString);
	}

//	public void delete(ContactMessageModel item) {
////		String whereString =ContactMessage_Column.GID+" = "+gid;
////		mDelete.delete(whereString);
//	}
}
