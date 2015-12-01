package com.renren.mobile.chat.dao;

import java.util.List;

import android.content.ContentValues;

import com.core.database.BaseDAO;
import com.core.database.BaseDBTable;
import com.core.database.Query;
import com.core.orm.ORMUtil;
import com.renren.mobile.chat.base.util.SystemUtil;
import com.renren.mobile.chat.database.ContactCommon_Column;
import com.renren.mobile.chat.sql.Query_CommonContacts;
import com.renren.mobile.chat.ui.contact.CommonContactModel;


public class ContactCommonDAO extends BaseDAO{
	/*查询所有的通讯录好友*/
	Query mQuery_All = new Query_CommonContacts(this);

	public ContactCommonDAO(BaseDBTable table) {super(table);}
	
	public List<CommonContactModel> query_AllContact() {
		
		String desc = ContactCommon_Column._ID + " DESC";
		String limitString  =  "16" ;
		
		@SuppressWarnings({ "unchecked" })
		List<CommonContactModel> list =  mQuery_All.query(null, null, null, desc, limitString,  List.class);
		if(list!=null){
			SystemUtil.errord("list====================="+list.size());
		}else{
			SystemUtil.errord("list=====================dfsafasdfsafdasf");
		}
		return list;
	}

	
	public void delete_All(){
		if(SystemUtil.mDebug){
			SystemUtil.errord("删除所有常用联系人");
		}
		mDelete.delete(null);
	}
	
	public void delete_contact_by_userId(long id){
		String whereString =ContactCommon_Column.USER_ID+" = "+id;
		mDelete.delete(whereString);
	}
	
	public void insert_Contact(CommonContactModel contactModel) {
		ContentValues values = new ContentValues();
		ORMUtil.getInstance().ormInsert(CommonContactModel.class, contactModel, values);
		values.remove(ContactCommon_Column._ID);
		if(SystemUtil.mDebug){
			SystemUtil.logd("values="+values.toString());
		}
		mInsert.insert(values);
	}
}
