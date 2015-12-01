package com.renren.mobile.chat.ui.contact;

import java.util.HashMap;
import com.renren.mobile.chat.base.util.SystemUtil;
import com.renren.mobile.chat.dao.ContactsDAO;
import com.renren.mobile.chat.dao.DAOFactoryImpl;

/**
 * 联系人cache，供查询陌生人所用
 * */
public class StrangerContactCache {
	private static StrangerContactCache mStrangerContactCache = new StrangerContactCache();
	private HashMap<Long, ContactModel> mContactCacheMap = new HashMap<Long, ContactModel>();
	private final int  MAX_CONTACT_COUNT = 100;
	

	private StrangerContactCache() {
//		List<ContactModel> models = C_ContactsData.getInstance().StrangerContacts();
//		clear();
//		for (ContactModel contactModel : models) {
//			mContactCacheMap.put(contactModel.getmUserId(), contactModel);
//			if(SystemUtil.mDebug){
//				SystemUtil.logd("加载陌生人 = "+contactModel.getmContactName());
//			}
//		}
	}

	public static StrangerContactCache getInstance() {
//		if(mStrangerContactCache == null){
//			mStrangerContactCache = new StrangerContactCache();
//		}
		return mStrangerContactCache;
		
	}
	
	/**
	 * 清空联系人缓存列表
	 * **/
	public void clear(){
		mContactCacheMap.clear();
	}
	
	
	/**
	 * 通过用户id获取联系人信息
	 * 只有当C_ContactData中不存在userId对应的联系人信息时才调用此方法
	 * */
	public ContactModel getStrangerContactByUserId(long userId) {
		ContactModel contactInfo = null;
		if (this.mContactCacheMap.containsKey(Long.valueOf(userId))) {
			contactInfo = this.mContactCacheMap.get(Long.valueOf(userId));
			if(SystemUtil.mDebug){
				SystemUtil.logd("from 陌生人  缓存 name="+contactInfo.getmContactName()+"#id="+contactInfo.getmUserId());
			}
			return contactInfo;
		}
		ContactsDAO contactDao =DAOFactoryImpl.getInstance().buildDAO(ContactsDAO.class);
		contactInfo =  contactDao.query_Contact_ByUserId(userId);
		if (contactInfo != null) {
			if((contactInfo.getmRelation()&ContactBaseModel.Relationship.IS_CONTACT) != ContactBaseModel.Relationship.IS_CONTACT){
				this.putStrangerContact(userId, contactInfo);
				if(SystemUtil.mDebug){
					SystemUtil.logd("from  数据库中 陌生人 name="+contactInfo.getmContactName()+"#id="+contactInfo.getmUserId());
				}
			}else{
				if(SystemUtil.mDebug){
					SystemUtil.logd("from  数据库中 联系人 name="+contactInfo.getmContactName()+"#id="+contactInfo.getmUserId());
				}	
			}
			return contactInfo;
		}
		return contactInfo;
	}

	/**
	 * 将联系人信息放入到缓存中去
	 * */
	public void putStrangerContact(long uid, ContactModel contact) {
		if (contact != null) {
			if(SystemUtil.mDebug){
				SystemUtil.logd("将联系人信息放到缓存中:uid="+uid+"#name="+contact.getName());
			}
			if(this.mContactCacheMap.containsKey(uid)){
				mContactCacheMap.remove(uid);
				mContactCacheMap.put(uid, contact);
				
			}else{
				if(this.mContactCacheMap.size() >= MAX_CONTACT_COUNT){
					this.mContactCacheMap.clear();
				}
				this.mContactCacheMap.put(uid, contact);
			}
			
		}
	}

}
