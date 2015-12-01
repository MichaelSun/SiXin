package com.renren.mobile.chat.ui.contact;

import java.util.ArrayList;
import java.util.List;
import com.renren.mobile.chat.base.util.SystemUtil;
import com.renren.mobile.chat.dao.ContactMessageDAO;
import com.renren.mobile.chat.dao.DAOFactoryImpl;

/**
 * @说明 通讯录数据
 * */
public final class ContactMessageData  {
	
	/** 排序过的所有联系人 */
	private List<ContactMessageModel> mData = new ArrayList<ContactMessageModel>();

	private ContactMessageDAO mContactMessageDAO ;
	private static ContactMessageData instance = new ContactMessageData();
	
	private List<ContactMessageDataObserver> mNewObserverList = new ArrayList<ContactMessageDataObserver>();
	
	//private boolean isLoaded =  false;
	private ContactMessageData (){
		mContactMessageDAO = DAOFactoryImpl.getInstance().buildDAO(ContactMessageDAO.class);
	}
	
	public  static ContactMessageData getInstance(){
		return instance;
	}
	
	public void notifyData(ContactMessageModel model){
		mContactMessageDAO.insert_Contact(model); 
		mData.add(0,model);
		SpecialContactModel specialContactModel = C_ContactsData.getInstance().getRecommendContact();
		if(specialContactModel!=null){
			specialContactModel.addUnReadCount();
		}
		notifyNewObserver(ContactMessageDataObserver.DATA_STATE_OK,ContactMessageDataObserver.TYPE_ADD);
	}
	
	public void loadDada(){
		//内存中已经存在  不用重复加载
		if(mData.size()>0){
			SystemUtil.logd("从内存加载推荐联系人消息:");
			notifyNewObserver(ContactMessageDataObserver.DATA_STATE_OK,ContactMessageDataObserver.TYPE_UPDATE);
			return ;
		}
		//end 内存中已经存在  不用重复加载
		if (mContactMessageDAO.querySumCount() > 0) {//从数据库中加载联系人
			if(SystemUtil.mDebug){
				SystemUtil.logd("数据库中有联系人");
			}
			List<ContactMessageModel> tmpContactModels=mContactMessageDAO.query_AllContactMessage();
			if(tmpContactModels!=null){
				mData.addAll(tmpContactModels);
			}
			//isLoaded = true;
		} 
		
		if(SystemUtil.mDebug){
			if(mData.size()==0){
				//createFalseData();
			}
		}
		notifyNewObserver(ContactMessageDataObserver.DATA_STATE_OK,ContactMessageDataObserver.TYPE_UPDATE);
	}
	
	public ContactMessageModel getModelByNativeId(long nativeID){
		for (ContactMessageModel model : mData) {
			if(nativeID == model.getNativeId()){
				return model;
			}
		}
		return null;
	}
	
	public List<ContactMessageModel> getData(){
		return mData;
	}
	
	public void clear(){
		mData.clear();
		//isLoaded = false;
	}
	
    public void delete(long id) {
    	mContactMessageDAO.delete_contact_by_userId(id);
    	for(ContactMessageModel model:mData){
    		if(model.getNativeId() == id){
    			mData.remove(model);
    			break;
    		}
    	}
    	notifyNewObserver(ContactMessageDataObserver.DATA_STATE_OK,ContactMessageDataObserver.TYPE_DEL);
	}
	
	public void delete_All(){
		mData.clear();
		mContactMessageDAO.delete_All();
	}
	
	public int getUnReadCount() {
		int num=0;
		for (ContactMessageModel model : mData) {
			if(model.getReaded()== ContactMessageModel.READ_NO){
				num++;
			}
		}
		return num;
	}

	public String getUnReadNames() {
		int num=1;
		StringBuilder sb = new StringBuilder();
		for (ContactMessageModel model : mData) {
			if(model.getReaded()== ContactMessageModel.READ_NO){
				if(num<=3){
					if(num!=1){
						sb.append(',');
					}
					sb.append(model.getName());
					num++;
				}
				
			}
		}
		return sb.toString();
	}

	
	/**
	 * 根据联系人类型   实现对联系人数据变更的监听
	 * **/
	public static interface ContactMessageDataObserver {
		/** 数据正常更新 */
		byte DATA_STATE_OK = 1;
		/** 数据更新中出现异常*/
		byte DATA_STATE_ERROR = 0;
		
		byte TYPE_ADD = 0;
		byte TYPE_DEL = 1;
		byte TYPE_UPDATE = 3;
		
		public void notifyDataUpdate(byte state,byte type);
	}
	
	public synchronized void notifyNewObserver(byte state,byte type) {
		if(SystemUtil.mDebug){
			SystemUtil.logd("notify contact message");
		}
		for (ContactMessageDataObserver observer : mNewObserverList) {
			observer.notifyDataUpdate(state,type);
		}
	}
	
	public synchronized void registorNewObserver(ContactMessageDataObserver observer) {
		if(observer != null){
			mNewObserverList.add(observer);
		}
	}
	
	public synchronized void unRegistorNewObserver(ContactMessageDataObserver observer) {
		if(observer!=null){
			mNewObserverList.remove(observer);
		}
	}
	
//	private void  createFalseData() {
//		SystemUtil.logd("createFalseData--------");
//		ContactMessageModel model = new ContactMessageModel();
//		model.setType(ContactMessageModel.TYPE_REQUEST);
//		model.setGid(1240077940);
//		model.setDomain("talk.sixin.com");
//		//model.setFrom_type(1);
//		model.setFrom_text("好友请求");
//		model.setName("李星辰");
//		model.setHead_url("http://hdn.xnimg.cn/photos/hdn321/20110512/2120/tiny_qORI_123268m019118.jpg");
//		model.setBody("我是星辰呀，加我好友呀！");
//		model.setNativeId(System.currentTimeMillis());
//		mData.add(model);
//		mContactMessageDAO.insert_Contact(model);   
//		
//		model = new ContactMessageModel();
//		model.setType(ContactMessageModel.TYPE_ADD);
//		model.setGid(1240077940);
//		model.setDomain("talk.sixin.com");
//		model.setFrom_type(ContactMessageModel.FROM_TYPE_ADDRESS);
//		model.setFrom_text("通讯录好友");
//		model.setName("李星辰");
//		model.setHead_url("http://hdn.xnimg.cn/photos/hdn321/20110512/2120/tiny_qORI_123268m019118.jpg");
//		model.setBody("李星辰已经将你加为他的联系人");
//		model.setNativeId(System.currentTimeMillis());
//		mData.add(model);
//		mContactMessageDAO.insert_Contact(model);   
//		
//		model = new ContactMessageModel();
//		model.setType(ContactMessageModel.TYPE_ADD);
//		model.setGid(1240077940);
//		model.setDomain("talk.sixin.com");
//		model.setFrom_type(ContactMessageModel.FROM_TYPE_RENREN);
//		model.setFrom_text("人人好友");
//		model.setName("李星辰");
//		model.setHead_url("http://hdn.xnimg.cn/photos/hdn321/20110512/2120/tiny_qORI_123268m019118.jpg");
//		model.setBody("李星辰已经将你加为他的联系人"); 
//		model.setNativeId(System.currentTimeMillis());
//		mData.add(model);
//		mContactMessageDAO.insert_Contact(model);   
//		
//		model = new ContactMessageModel();
//		model.setType(ContactMessageModel.TYPE_ADD);
//		model.setGid(1240077940);
//		model.setDomain("talk.sixin.com");
//		model.setFrom_type(ContactMessageModel.FROM_TYPE_FACEBOOK);
//		model.setFrom_text("Facebook好友");
//		model.setName("李星辰");
//		model.setHead_url("http://hdn.xnimg.cn/photos/hdn321/20110512/2120/tiny_qORI_123268m019118.jpg");
//		model.setBody("李星辰已经将你加为他的联系人");
//		model.setNativeId(System.currentTimeMillis());
//		mData.add(model);
//		mContactMessageDAO.insert_Contact(model);   
//		
//		
//		model = new ContactMessageModel();
//		model.setType(ContactMessageModel.TYPE_RECOMMEND);
//		model.setGid(1240077940);
//		model.setDomain("talk.sixin.com");
//		model.setFrom_type(1);
//		model.setFrom_text("通讯录联系人");
//		model.setName("李星辰");
//		model.setHead_url("http://hdn.xnimg.cn/photos/hdn321/20110512/2120/tiny_qORI_123268m019118.jpg");
//		model.setBody("你的通讯录中有他的联系人");
//		model.setNativeId(System.currentTimeMillis());
//		mData.add(model);
//		mContactMessageDAO.insert_Contact(model);   
//		
//		model = new ContactMessageModel();
//		model.setType(ContactMessageModel.TYPE_RECOMMEND);
//		model.setGid(1240077940);
//		model.setDomain("talk.sixin.com");
//		model.setFrom_type(1);
//		model.setFrom_text("通讯录联系人");
//		model.setName("李星辰");
//		model.setHead_url("http://hdn.xnimg.cn/photos/hdn321/20110512/2120/tiny_qORI_123268m019118.jpg");
//		model.setBody("TA的通讯录中有你");
//		model.setNativeId(System.currentTimeMillis());
//		mData.add(model);
//		mContactMessageDAO.insert_Contact(model);   
//	}

	public void clearUnReadCount() {
		if(SystemUtil.mDebug){
			SystemUtil.logd("clearUnReadCount");
		}
		mContactMessageDAO.clearUnReadCount();
		for (ContactMessageModel model : mData) {
			model.setReaded(ContactMessageModel.READ_YES);
		}
		SpecialContactModel specialContactModel = C_ContactsData.getInstance().getRecommendContact();
		if(specialContactModel!=null){
			specialContactModel.clearUnReadCount();
		}
	}

	public void addSucessd(long nativeID) {
		if(SystemUtil.mDebug){
			SystemUtil.logd("成功添加好友 nativeID="+nativeID);
		}
		for (ContactMessageModel model : mData) {
			if(nativeID == model.getNativeId()){
				model.setAdded(ContactMessageModel.ADD_YES);
				break;
			}
		}
		notifyNewObserver(ContactMessageDataObserver.DATA_STATE_OK,ContactMessageDataObserver.TYPE_UPDATE);
		updateAdded(nativeID,ContactMessageModel.ADD_YES);
	}	
	
	public void updateAdded(long nativeID,byte state){
		mContactMessageDAO.updateAdded(nativeID,state);
	}
	
}
