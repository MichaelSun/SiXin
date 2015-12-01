package com.renren.mobile.chat.dao;

import java.util.List;

import com.renren.mobile.chat.actions.models.RoomInfoModelWarpper;
import com.renren.mobile.chat.ui.contact.ContactModel;

public interface IContactsDAO {

	/*查询所有的联系人信息*/
	public List<ContactModel> query_AllContact();
	
	/*插入联系人列表*/
	public void insert_Contacts(List<ContactModel> contactList);
	/*插入联系人列表*/
	public void insert_Contact(ContactModel contactModel);
	/*删除所有联系人*/
	public void delete_AllFirends();
	/*查询联系人*/
	public ContactModel query_Contact_ByUserId(long userId);
	
	/*修改*/
	//public void update_Contact_Attention(ContactModel model,boolean isAttention);
	
	/*修改*/
	//public void update_Contact_Attention_FromNet(ContactModel model,boolean isAttention);
	
	/*批量更新特别关注*/
	//public void update_Contacts_Attention(List<Long> userid_set,int isAttention);
	
	/*添加组附属*/
	public void update_GroupNumber(List<Long> userid_set,int addGroupNumber);
	
	/*添加组附属*/
	public void update_GroupNumber(long userId,int addGroupNumber);
	
	
	
	
	
	public static interface OnContactsAndRoomDownloadListener{
		public long getUid();
		public void onSuccess(List<ContactModel> noSortList,List<RoomInfoModelWarpper> roomList);
		public void onError();
		public boolean check(long uid);
	}
	
	public static interface OnContactsDownloadListener{
		public long getUid();
		public void onSuccess(List<ContactModel> noSortList);
		public void onError();
		public boolean check(long uid);
	}
}
