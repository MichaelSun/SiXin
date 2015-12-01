package com.renren.mobile.chat.dao;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;

import com.common.mcs.INetReponseAdapter;
import com.common.mcs.INetRequest;
import com.common.mcs.McsServiceProvider;
import com.core.database.BaseDAO;
import com.core.database.BaseDBTable;
import com.core.database.Query;
import com.core.json.JsonObject;
import com.core.orm.ORMUtil;
import com.renren.mobile.chat.base.util.SystemUtil;
import com.renren.mobile.chat.database.Renren_Contact_Column;
import com.renren.mobile.chat.sql.Query_RenRen_Contacts;
import com.renren.mobile.chat.sql.Query_Renren_Contact;
import com.renren.mobile.chat.ui.contact.ThirdContactModel;
import com.renren.mobile.chat.ui.contact.utils.ContactResrouceUtils;
public class ThirdContactsDAO extends BaseDAO{

	Query mQuery_AllContacts = new Query_RenRen_Contacts(this);
	/*查询联系人*/
	Query mQuery_Contact = new Query_Renren_Contact(this);
	
	public ThirdContactsDAO(BaseDBTable table) {
		super(table);
	}
	
	public final INetRequest getContactsRequest(DownloadThirdListener listener,String externalAccountType,String externalAccountId){
		return  McsServiceProvider.getProvider().getThirdFriendsList(new Contacts_Response(listener),"",externalAccountType,externalAccountId, 2000, 1);
	}
	
	public class Contacts_Response extends INetReponseAdapter{
		long mUserId = -99l;
		DownloadThirdListener mlisListener = null;
		public Contacts_Response(DownloadThirdListener listener){
			mlisListener = listener;
		}
		@Override
		public void onSuccess(INetRequest req, JsonObject data) {
	        if(SystemUtil.mDebug){
	        	SystemUtil.logd("get all contacts="+data.toJsonString());
			}
			
			List<ThirdContactModel> contactList = ThirdContactModel.newParseContactModels(data);
			mlisListener.onSuccess(contactList);
		}
		
		@Override
		public void onError(INetRequest req, JsonObject data) {
	        if(SystemUtil.mDebug){
	        	SystemUtil.traces();
				SystemUtil.logd("get all contacts reroe="+data.toJsonString());
			}
			mlisListener.onError();
		}
	}
	
	public interface DownloadThirdListener{
		public void onSuccess(List<ThirdContactModel> noSortList);
		public void onError();
	}
	
	public void delete_AllContacts(){
		if (SystemUtil.mDebug) {
			SystemUtil.logd("删除所有人人联系人");
		}
		mDelete.delete(null);
	}
	
	public void insert_Contacts(List<ThirdContactModel> contactList) {
		this.beginTransaction();
		synchronized (contactList) {
			for(ThirdContactModel m:contactList){
				this.insert_Contact(m);
			}
		}
		this.commit();
	}
	
	public void insert_Contact(ThirdContactModel contactModel) {
		ContentValues values = new ContentValues();
		//SystemUtil.logd("111name="+contactModel.getmContactName()+"#"+contactModel.getmBirth());
		ORMUtil.getInstance().ormInsert(ThirdContactModel.class, contactModel, values);
		//SystemUtil.errord("test="+values.toString());
		mInsert.insert(values);
	}
	
	public List<ThirdContactModel> query_AllContact() {
		List<ThirdContactModel> list =  mQuery_AllContacts.query(null, null, null, null, null,  List.class);
		//SystemUtil.logd("list="+(list==null));
		
		ArrayList<ThirdContactModel>  sortedContactList = null;
		if(list != null){
			sortedContactList= ContactResrouceUtils
					.combineOrderd(list);
		}
		return sortedContactList;
	}
	
	public void update_contact_attention_by_userId(long userId, int attention){
		ContentValues values = new ContentValues();
		values.put(Renren_Contact_Column.IS_ATTENTION, attention);
		String where = Renren_Contact_Column.USER_ID +" = "+userId;
		mUpdate.update(values, where);
	}
	
	
	public ThirdContactModel query_Contact_BySiXinId(long userId) {
		String where = Renren_Contact_Column.USER_ID +" = "+userId;
		ThirdContactModel tmpContactModel= mQuery_Contact.query(null, where, null, null, null, ThirdContactModel.class);
		if(tmpContactModel != null){
			tmpContactModel.name = tmpContactModel.mContactName;
		}
		return tmpContactModel;
	}
	
	public ThirdContactModel query_Contact_ByRenRenId(long userId) {
		String where = Renren_Contact_Column.RENREN_ID +" = "+userId;
		ThirdContactModel tmpContactModel= mQuery_Contact.query(null, where, null, null, null, ThirdContactModel.class);
		if(tmpContactModel != null){
			tmpContactModel.name = tmpContactModel.mContactName;
		}
		return tmpContactModel;
	}
	
	public List<ThirdContactModel> query_Attention_Contact(){
		String where = Renren_Contact_Column.IS_ATTENTION +" = "+1;
		return mQuery_AllContacts.query(null, where, null, null, null, List.class);
	}
	
	public void delete_contact_by_userId(long id){
		String whereString =Renren_Contact_Column.USER_ID+" = "+id;
		mDelete.delete(whereString);
	}
	
	@Override
	public int querySumCount(){
		String whereString =Renren_Contact_Column.IS_FRIEND+" = "+ThirdContactModel.IS_FRIEND_YES; 
		return super.querySumCount(whereString);
	}
}
