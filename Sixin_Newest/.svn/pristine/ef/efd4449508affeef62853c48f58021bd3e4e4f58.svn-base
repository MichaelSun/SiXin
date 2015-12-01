package com.renren.mobile.chat.dao;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import android.content.ContentValues;
import android.util.Log;

import com.common.mcs.INetReponseAdapter;
import com.common.mcs.INetRequest;
import com.common.mcs.McsServiceProvider;
import com.core.database.BaseDAO;
import com.core.database.BaseDBTable;
import com.core.database.Query;
import com.core.json.JsonArray;
import com.core.json.JsonObject;
import com.core.orm.ORMUtil;
import com.renren.mobile.chat.actions.models.RoomInfoModelWarpper;
import com.renren.mobile.chat.base.model.OnlineStatusModel;
import com.renren.mobile.chat.base.util.SystemUtil;
import com.renren.mobile.chat.database.Contact_Column;
import com.renren.mobile.chat.sql.Query_AllContacts;
import com.renren.mobile.chat.sql.Query_Contact;
import com.renren.mobile.chat.ui.contact.C_ContactsData;
import com.renren.mobile.chat.ui.contact.ContactModel;

import com.renren.mobile.chat.ui.contact.utils.ContactResrouceUtils;
public class ContactsDAO extends BaseDAO implements IContactsDAO{
	/*查询所有的通讯录好友*/
	Query mQuery_AllContacts = new Query_AllContacts(this);
	/*查询联系人*/
	Query mQuery_Contact = new Query_Contact(this);
	public ContactsDAO(BaseDBTable table) {super(table);}
	@Override
	public List<ContactModel> query_AllContact() {
		String where = Contact_Column.RELATION +" = "+ContactModel.Relationship.CONTACT+" OR "+Contact_Column.RELATION +" = "+ContactModel.Relationship.BLACK_LIST_CONTACT;
		List<ContactModel> list =  mQuery_AllContacts.query(null, where, null, null, null,  List.class);
		ArrayList<ContactModel>  sortedContactList = null;
		if(list!=null){
			if(SystemUtil.mDebug){
				  SystemUtil.errord("size="+list.size());
			}
		    sortedContactList = ContactResrouceUtils.combineResrouceOrderd(list);
		    list.clear();
		    list = null;
		}
		return sortedContactList;
	}
	
	public List<ContactModel> StrangerContacts() {
		String where = Contact_Column.RELATION +" = "+ContactModel.Relationship.STRANGER+" OR "+Contact_Column.RELATION +" = "+ContactModel.Relationship.BLACK_LIST_STRANGER;
		List<ContactModel> list =  mQuery_AllContacts.query(null, where, null, null, null,  List.class);
		return list;
	}
	
	@Override
	public void insert_Contacts(List<ContactModel> contactList) {
	//	SystemUtil.errord("dfdfdf");
	//	SystemUtil.traces();
		this.beginTransaction();
		synchronized (contactList) {
			for(ContactModel m:contactList){
				this.insert_Contact(m);
			}
		}
		this.commit();
	}
	@Override
	public void delete_AllFirends(){
		String whereString =Contact_Column.RELATION+" = "+ContactModel.Relationship.CONTACT +" OR "+Contact_Column.RELATION +" = "+ContactModel.Relationship.BLACK_LIST_CONTACT;
		mDelete.delete(whereString);
	}
	
	public void delete_All(){
		if(SystemUtil.mDebug){
			SystemUtil.errord("删除所有私信联系人");
		}
		mDelete.delete(null);
	}
	
	public void delete_contact_by_userId(long id){
		String whereString =Contact_Column.USER_ID+" = "+id;
		mDelete.delete(whereString);
	}
	
	@Override
	public ContactModel query_Contact_ByUserId(long userId) {
		String where = Contact_Column.USER_ID +" = "+userId;
		ContactModel tmpContactModel= mQuery_Contact.query(null, where, null, null, null, ContactModel.class);
		if(tmpContactModel != null){
			tmpContactModel.name = tmpContactModel.mContactName;
		}
		return tmpContactModel;
	}
	
//	private void update_Contact_Attention_DB(long userId, int isAttention){
//		ContentValues values = new ContentValues();
//		String where = Contact_Column.USER_ID +" = "+userId;
//		values.put(Contact_Column.IS_ATTENTION, isAttention);
//		mUpdate.update(values, where);
//	}
	
	public void updateContact(long uid, byte blackListContact) {
		if(SystemUtil.mDebug){
			SystemUtil.errord("update uid="+uid);
		}
		//SystemUtil.traces();
		ContentValues values = new ContentValues();
		String where = Contact_Column.USER_ID +" = "+uid;
		values.put(Contact_Column.RELATION, blackListContact);
		mUpdate.update(values, where);
	}
	
	@Override
	public void insert_Contact(ContactModel contactModel) {
		ContentValues values = new ContentValues();
		
		ORMUtil.getInstance().ormInsert(ContactModel.class, contactModel, values);
		
	//	SystemUtil.errord("quanpin="+contactModel.getQuanPin()+"#"+values.get("xing_pinyin")+"#");
	//	SystemUtil.errord("quanpin="+contactModel.mNamePinyin_String+"#"+values.get("user_name_pinyin"));
		mInsert.insert(values);
	}
	@Override
	public void update_GroupNumber(List<Long> userid_set, int addGroupNumber) {
		for(long id:userid_set){
			this.update_GroupNumber(id, addGroupNumber);
		}
	}
	/**
	 * @param addGroupNumber 联系人对应的组的个数(-1~+1)
	 * */
	@Override
	public void update_GroupNumber(long userId, int addGroupNumber) {
		String where = Contact_Column.USER_ID +" = "+userId;
		ContactModel m = this.query_Contact_ByUserId(userId);
		ContentValues values = new ContentValues(1);
		values.put(Contact_Column.ATTACH_GROUP_NUMBER,m.mAttachGroupNumber+addGroupNumber);
		mUpdate.update(values, where);
	}
//	/**
//	 * 获取网络上的所有的联系人 包括在线状态
//	 * */
//	@Override
//	public void query_Contacts_FromNet(OnContactsDownloadListener listener){
//	}
	/**
	 * 获取网络上的所有的联系人 
	 * */
	public final INetRequest getContactsRequest(OnContactsAndRoomDownloadListener listener){
		return  McsServiceProvider.getProvider().getContactList(new Contacts_Response(listener), 1, 2000,false);
	}
	
	
	public final void getFindContactsRequest(OnContactsDownloadListener listener,String key){
		McsServiceProvider.getProvider().searchContacts(key, new Find_Contacts_Response(listener));
	}
	
	/**
	 * 
{
   "count":1,
   "contact_list":[
      {
         "profile_info":{
            "user_id":100001392,
            "domain_name":"talk.sixin.com",
            "name":"1822中文",
            "first_name":"",
            "last_name":"",
            "gender":1,
            "birth_display":"0-0-0",
            "birth_day":{
               "year":0,
               "month":0,
               "day":0
            },
            "profile_image":{
               "medium_url":"http://www.qiqu5.com/wp-content/uploads/2010/03/119.jpg",
               "large_url":"http://t2.baidu.com/it/u=3925358291,1529856573&fm=52&gp=0.jpg",
               "original_url":"http://t3.baidu.com/it/u=295144277,3149792241&fm=51&gp=0.jpg"
            },
            "school":[
               {
                  "name":""
               }
            ],
            "employer":[
               {
                  "name":""
               }
            ],
            "relationship_type":5
         },
         "bind_info":[
            {
               "bind_id":"18233333333",
               "type":"mobile",
               "name":"",
               "page":""
            }
         ]
      }
   ],
   "group_count":2,
   "group_list":[
      {
         "id":10618,
         "name":"李小宾_女、汪涵",
         "creator":384139006,
         "create_time":1340076121676,
         "update_time":0,
         "version":1,
         "member_list":[
            {
               "profile_info":{
                  "user_id":2,
                  "domain_name":"talk.sixin.com",
                  "name":"汪涵",
                  "first_name":"",
                  "last_name":"",
                  "gender":0,
                  "birth_day":{
                     "year":0,
                     "month":0,
                     "day":0
                  },
                  "profile_image":{
                     "medium_url":"http://www.qiqu5.com/wp-content/uploads/2010/03/119.jpg",
                     "large_url":"http://t2.baidu.com/it/u=3925358291,1529856573&fm=52&gp=0.jpg",
                     "original_url":"http://t3.baidu.com/it/u=295144277,3149792241&fm=51&gp=0.jpg"
                  },
                  "relationship_type":5
               }
            },
            {
               "profile_info":{
                  "user_id":384139006,
                  "domain_name":"talk.sixin.com",
                  "name":"李小宾_女",
                  "first_name":"",
                  "last_name":"",
                  "gender":0,
                  "birthday":{
                     "year":2011,
                     "month":6,
                     "day":30
                  },
                  "profile_image":{
                     "medium_url":"http://www.qiqu5.com/wp-content/uploads/2010/03/119.jpg",
                     "large_url":"http://t2.baidu.com/it/u=3925358291,1529856573&fm=52&gp=0.jpg",
                     "original_url":"http://t3.baidu.com/it/u=295144277,3149792241&fm=51&gp=0.jpg"
                  },
                  "relationship_type":5
               }
            }
         ]
      },
      {
         "id":10617,
         "name":"李小宾_女、汪涵",
         "creator":384139006,
         "create_time":1340076121624,
         "update_time":0,
         "version":1,
         "member_list":[
            {
               "profile_info":{
                  "user_id":2,
                  "name":"汪涵",
                  "domain_name":"talk.sixin.com",
                  "first_name":"",
                  "last_name":"",
                  "gender":0,
                  "birth_day":{
                     "year":0,
                     "month":0,
                     "day":0
                  },
                  "profile_image":{
                     "medium_url":"http://www.qiqu5.com/wp-content/uploads/2010/03/119.jpg",
                     "large_url":"http://t2.baidu.com/it/u=3925358291,1529856573&fm=52&gp=0.jpg",
                     "original_url":"http://t3.baidu.com/it/u=295144277,3149792241&fm=51&gp=0.jpg"
                  },
                  "relationship_type":5
               }
            },
            {
               "profile_info":{
                  "user_id":384139006,
                  "domain_name":"talk.sixin.com",
                  "name":"李小宾_女",
                  "first_name":"",
                  "last_name":"",
                  "gender":0,
                  "birthday":{
                     "year":2011,
                     "month":6,
                     "day":30
                  },
                  "profile_image":{
                     "medium_url":"http://www.qiqu5.com/wp-content/uploads/2010/03/119.jpg",
                     "large_url":"http://t2.baidu.com/it/u=3925358291,1529856573&fm=52&gp=0.jpg",
                     "original_url":"http://t3.baidu.com/it/u=295144277,3149792241&fm=51&gp=0.jpg"
                  },
                  "relationship_type":5
               }
            }
         ]
      }
   ]
}
}
	 *
	 */
	public class Contacts_Response extends INetReponseAdapter{
		long mUserId = -99l;
		OnContactsAndRoomDownloadListener mlisListener = null;
		public Contacts_Response(OnContactsAndRoomDownloadListener listener){
			mUserId = listener.getUid();
			mlisListener = listener;
		}
		@Override
		public void onSuccess(INetRequest req, JsonObject data) {
	        if(SystemUtil.mDebug){
	        	SystemUtil.logd("get all contacts22222222222="+data.toJsonString()); //
			}
	        Log.v("kfc","=====================Contacts_Response onSuccess================================");
	        List<ContactModel> contactList;
	        List<RoomInfoModelWarpper> roomList;
//	        try {
//	        	JsonObject contacts = data.getJsonObject("contacts");
				JsonArray array = data.getJsonArray("contact_list");
				contactList = ContactModel.newParseContactModels(array);
				
//				JsonObject groups = data.getJsonObject("groups");
				array = data.getJsonArray("group_list");
				roomList = RoomInfoModelWarpper.newParseGroupContactModels(array);
		        if(SystemUtil.mDebug){
		        	SystemUtil.mark();
				}
//			} catch (Exception e) {
//				if(SystemUtil.mDebug){
//					SystemUtil.errord(e.toString());
//					e.printStackTrace();
//				}
//				mlisListener.onError();
//				return ;
//			}
			mlisListener.onSuccess(contactList,roomList);
		}
		@Override
		public void onError(INetRequest req, JsonObject data) {
			Log.v("kfc","=====================Contacts_Response onError================================");
			//Logd.traces();
	        if(SystemUtil.mDebug){
	        	SystemUtil.errord("get all contacts reroe="+data.toJsonString());
			}
			
			mlisListener.onError();
		}
	}
	
	
	public class Find_Contacts_Response extends INetReponseAdapter{
		
		OnContactsDownloadListener mlisListener = null;
		public Find_Contacts_Response(OnContactsDownloadListener listener){
			mlisListener = listener;
		}
		
		@Override
		public void onSuccess(INetRequest req, JsonObject data) {
	        if(SystemUtil.mDebug){
	        	SystemUtil.logd("get all find contacts ="+data.toJsonString()); 
			}
	        List<ContactModel> contactList;
			JsonArray array = data.getJsonArray("contact_list");
			contactList = ContactModel.newParseContactModels(array);
	        if(SystemUtil.mDebug){
	        	SystemUtil.mark();
			}
			mlisListener.onSuccess(contactList);
		}
		
		@Override
		public void onError(INetRequest req, JsonObject data) {
			//Logd.traces();
	        if(SystemUtil.mDebug){
	        	SystemUtil.errord("find contacts reroe="+data.toJsonString());
			}
			mlisListener.onError();
		}
	}
	
	public static interface OnContactsOnlineStatusListener{
		public void onDownloadOnlinesStatus(List<OnlineStatusModel> onlineStatusSet);
	}
	public void query_ContactsOnlineStatus(OnContactsOnlineStatusListener listener){
		if(listener!=null){
			McsServiceProvider.getProvider().getOnlineFriendListSimple(new OnlineStatus_Response(listener));
		}
	}
	static class OnlineStatus_Response extends INetReponseAdapter{
		OnContactsOnlineStatusListener mListener = null;
		public OnlineStatus_Response(OnContactsOnlineStatusListener listener){
			mListener = listener;
		}
		@Override
		public void onSuccess(INetRequest req, JsonObject data) {
			JsonArray jArray = data.getJsonArray("status_list");
			if (jArray != null) {
				JsonObject[] objs = new JsonObject[jArray.size()];
				jArray.copyInto(objs);
				List<OnlineStatusModel> list = new LinkedList<OnlineStatusModel>();
				for (JsonObject jObject : objs) {
					String sourceStatusStr = jObject.getString("s");
					if(sourceStatusStr!=null){
						try {
							String[] strs = sourceStatusStr.split(":");
							long userid = Long.parseLong(strs[0]);
							int status = Integer.parseInt(strs[1]);
							list.add(new OnlineStatusModel(userid, status));
						} catch (Exception e) {}
					}
				}
				if(list.size()>0 && mListener!=null){
					mListener.onDownloadOnlinesStatus(list);
				}
			}
		}
	}

	public static interface DataObserver{
		public void notifyUpdate(String columnName,boolean isSuccess);
		public void registorSubject(DataSubject subject);
	}
	public static interface DataSubject{
		public void unregitor();
	}
	
	@Override
	public int querySumCount(){
		String whereString =Contact_Column.RELATION+" = "+ContactModel.Relationship.CONTACT +" OR "+Contact_Column.RELATION +" = "+ContactModel.Relationship.BLACK_LIST_CONTACT; 
		return super.querySumCount(whereString);
	}
	
}
