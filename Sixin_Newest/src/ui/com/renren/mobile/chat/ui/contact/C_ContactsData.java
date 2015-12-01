package com.renren.mobile.chat.ui.contact;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import android.os.Handler;
import android.text.TextUtils;

import com.common.manager.BindInfo;
import com.common.manager.LoginManager;
import com.common.mcs.INetRequest;
import com.common.mcs.INetResponse;
import com.common.mcs.McsServiceProvider;
import com.common.network.DomainUrl;
import com.core.json.JsonObject;
import com.core.json.JsonValue;
import com.renren.mobile.chat.R;
import com.renren.mobile.chat.RenrenChatApplication;
import com.renren.mobile.chat.actions.models.RoomInfoModelWarpper;
import com.renren.mobile.chat.base.model.ChatBaseItem.MESSAGE_ISGROUP;
import com.renren.mobile.chat.base.util.SystemUtil;
import com.renren.mobile.chat.base.views.NotSynImageView;
import com.renren.mobile.chat.common.ResponseError;
import com.renren.mobile.chat.dao.ContactCommonDAO;
import com.renren.mobile.chat.dao.ContactsDAO;
import com.renren.mobile.chat.dao.DAOFactoryImpl;
import com.renren.mobile.chat.dao.IContactsDAO.OnContactsAndRoomDownloadListener;
import com.renren.mobile.chat.dao.IContactsDAO.OnContactsDownloadListener;
import com.renren.mobile.chat.dao.ThirdContactsDAO;
import com.renren.mobile.chat.dao.ThirdContactsDAO.DownloadThirdListener;
import com.renren.mobile.chat.ui.PullUpdateTouchListener;
import com.renren.mobile.chat.ui.chatsession.ChatSessionDataModel;
import com.renren.mobile.chat.ui.contact.RoomInfosData.RoomInfosDataObserver;
import com.renren.mobile.chat.ui.contact.utils.ContactResrouceUtils;

/**
 * @说明 通讯录数据
 * */
public final class C_ContactsData implements RoomInfosDataObserver {

	public static boolean enable_fasle_data = false;
	private static C_ContactsData sInstance =null;
	private static final Handler HADLER = RenrenChatApplication.sHandler;
	private int[] mAll_Indexs = new int[C_LetterBar.TAB_INDEX.length];
	private int[] mFilter_Indexs = new int[C_LetterBar.TAB_INDEX.length];
	private int[] mSiXin_Indexs = new int[C_LetterBar.TAB_INDEX.length];
	private int[] mRenRen_Indexs = new int[C_LetterBar.TAB_INDEX.length];

	/** 排序过的所有联系人 */
	private List<ContactModel> mSortContacts_All = new ArrayList<ContactModel>();
	/** 排序后的常用联系人联系人 */
	private List<ContactModel> mSortContactsCommon = new ArrayList<ContactModel>();
	/** 搜索联系人 */
    private List<ContactModel> mSortContacts_Search = new ArrayList<ContactModel>();
     
    
    private List<ContactModel> mSixinContacts = new  ArrayList<ContactModel>();
    private List<ContactModel> mSortGroupContacts = new ArrayList<ContactModel>();
    private List<ThirdContactModel> mRenRenContacts = new  ArrayList<ThirdContactModel>();
    
    private SpecialContactModel mRecommendContact;
    private SpecialContactModel mRenRenContact ;
    
    /** 通过查找联系人搜索得来的联系人 */
	private List<ContactModel> mContacts_Find = new ArrayList<ContactModel>();

    
	private ArrayList<NewContactsDataObserver> mNewObserverList = new ArrayList<C_ContactsData.NewContactsDataObserver>();
	private ContactsDAO mContactsDAO = null;
	private ThirdContactsDAO mThirdContactsDAO;
	private ContactCommonDAO mCommonDAO;
	private HashMap<Long, List<IDownloadContactListener>> contactCache = new  HashMap<Long, List<IDownloadContactListener>>();
	

	public static interface TYPE {
		int ALL_CONTACTS = 0; //   全部联系人
		int COMMON_CONTACTS = 1;// 常用联系人
		int SEARCH_CONTACTS = 2;// 搜索结果
		/** 被过滤后的联系人 */
		int FILTER_CONTACTS = 3;
		/**人人联系人 */
		int RENREN_CONTACTS = 4;
		/**私信联系人 */
		int SIXIN_CONTACTS  = 5;
		/** 私信群组联系人*/
		int SIXIN_GROUP = 6;
		int FIND_CONTACTS = 7; //查找联系人的结果
	}
	
	private C_ContactsData() {
		mContactsDAO = DAOFactoryImpl.getInstance().buildDAO(ContactsDAO.class);
		mThirdContactsDAO = DAOFactoryImpl.getInstance().buildDAO(ThirdContactsDAO.class);
		mCommonDAO = DAOFactoryImpl.getInstance().buildDAO(ContactCommonDAO.class);
		loadSiXinContacts();
	}
	
	public synchronized static C_ContactsData getInstance() {
		if(sInstance==null){
			sInstance = new C_ContactsData();
			RoomInfosData.getInstance().registorObserver(sInstance);
		}
		return sInstance;
	}
	
	/***
	 * 通过domain与uid获取联系人model
	 * **/
	public static ContactBaseModel getContact(long uid,String domain){
		if(TextUtils.isEmpty(domain)||uid==0 || uid==-1){
			if(SystemUtil.mDebug){
				SystemUtil.traces();
			}
			return null;
		}
		if(domain.equals(DomainUrl.SIXIN_DOMAIN)||domain.equals(DomainUrl.SIXIN_OLD_DOMAIN)||domain.equals(DomainUrl.SIXIN_RECOMMENDATION_DOMAIN)||domain.endsWith(DomainUrl.SIXIN_DOMAIN)){
			return C_ContactsData.getInstance()
					.getSiXinContact(
							uid,null);
		}else{
			return C_ContactsData.getInstance()
					.getRenRenContactBySiXinID(
							uid,null);
		}
	}
	
	
	
	/**加载常用联系人 */
	public void loadCommonContacts(){
		if(SystemUtil.mDebug){
			SystemUtil.logd("常用联系人size="+mSortContactsCommon.size());
		}
		
		if(mSortContactsCommon.size()>0){
			if(SystemUtil.mDebug){
				SystemUtil.logd("从内存中加载常用联系人:");
			}
			this.notifyNewObserver(NewContactsDataObserver.DATA_STATE_OK,NewContactsDataObserver.TYPE_COMMON);
			return ;
		}
		//end 内存中已经存在  不用重复加载
		
		int size = mCommonDAO.querySumCount() ;
		if(SystemUtil.mDebug){
			SystemUtil.logd("数据库人人常用联系人 size="+size);
		}
		if (size > 0) {//从数据库中加载联系人
			if(SystemUtil.mDebug){
				SystemUtil.logd("数据库中常用联系人");
			}
			List<CommonContactModel> tmp = mCommonDAO.query_AllContact();
			if(tmp!=null){
				List<ContactModel> unsorted =  new ArrayList<ContactModel>();
				for (CommonContactModel commonContactModel : tmp) {
					ContactModel cModel = getSiXinContact(commonContactModel.getmUserId());
					if(cModel!=null){
						unsorted.add(cModel);
						if(SystemUtil.mDebug){
							SystemUtil.logd("数据库中常用联系人  name="+cModel.getmContactName()+"#id="+cModel.getmUserId());
						}
					}
				}
				if(SystemUtil.mDebug){
					SystemUtil.logd("实际获取到的常用联系人 size="+unsorted.size());
				}
				if(unsorted.size()>0){
//					final ArrayList<ContactModel>  sortedContactList = ContactResrouceUtils
//							.combineResrouceOrderd(unsorted);
//					if(SystemUtil.mDebug){
//						SystemUtil.logd("排序后到的常用联系人 size="+sortedContactList.size());
//					}
					synchronized (mSortContactsCommon) {	
						mSortContactsCommon.clear();
						mSortContactsCommon.addAll(unsorted);
						if(SystemUtil.mDebug){
							SystemUtil.logd("添加后的常用联系人 size="+mSortContactsCommon.size());
						}
					}
				}
			}
			this.notifyNewObserver(NewContactsDataObserver.DATA_STATE_OK,NewContactsDataObserver.TYPE_COMMON);
		} 
	}
	
	public synchronized void commonContactsChanged(ChatSessionDataModel model){
		if(SystemUtil.mDebug){
			SystemUtil.logd("常用联系人发生变化重新获取");
		}
		if(model==null){
			if(SystemUtil.mDebug){
				SystemUtil.logd("%>_<%......");
			}
			return ;
		}
		if(SystemUtil.mDebug){
			SystemUtil.logd("isgroup="+model.mIsGroup);
		}
		if(model.mIsGroup == MESSAGE_ISGROUP.IS_SINGLE){
			 ContactModel cm = this.getSiXinContact(model.getmToId());
			 if(cm!=null){
				 if((cm.getmRelation()&ContactBaseModel.Relationship.IS_CONTACT)== ContactBaseModel.Relationship.IS_CONTACT){
					// boolean exits = false;
					 for (ContactModel model2 : mSortContactsCommon) {
						if(model2.getmUserId()==cm.getmUserId()){
							//exits =true;
							mSortContactsCommon.remove(model2);
							mCommonDAO.delete_contact_by_userId(model2.getmUserId());
							break;
						}
					}
					//if(!exits)
					{
						CommonContactModel ccm = new CommonContactModel(); 
						ccm.setmUserId(cm.getmUserId());
						ccm.setmDomain(cm.getmDomain());
						int size=mSortContactsCommon.size();
						if(mSortContactsCommon.size()>=16){
							for(int i=size-1;i>=15;i--){
								if(SystemUtil.mDebug){
									SystemUtil.logd("超过16个了 删除 name="+mSortContactsCommon.get(i).getmContactName()+"#id"+mSortContactsCommon.get(i).getmUserId());
								}
								mCommonDAO.delete_contact_by_userId(mSortContactsCommon.get(i).getmUserId());
								mSortContactsCommon.remove(i);
							}
						}
						mCommonDAO.delete_contact_by_userId(ccm.getmUserId());
						mCommonDAO.insert_Contact(ccm);
						mSortContactsCommon.add(0,cm);
//						synchronized (mSortContactsCommon) {
//							mSortContactsCommon.add(0,cm);
////							final ArrayList<ContactModel>  sortedContactList = ContactResrouceUtils
////									.combineResrouceOrderd(mSortContactsCommon);
//						//	mSortContactsCommon.clear();
//							//mSortContactsCommon.addAll(sortedContactList);
//						}
						this.notifyNewObserver(NewContactsDataObserver.DATA_STATE_OK,NewContactsDataObserver.TYPE_COMMON);
					}
					
				 }
			 }else {
				 if(SystemUtil.mDebug){
					 SystemUtil.logd("为啥就没这个人呢");
				 }
			}
		}
		
//		synchronized (mSortContactsCommon) {
//			mSortContactsCommon.clear();
//			loadCommonContacts();
//		}
	}
	
	/** */
	public void loadRenRenContact(PullUpdateTouchListener listener){
		
		if(SystemUtil.mDebug){
			SystemUtil.traces();
		}
		
		//内存中已经存在  不用重复加载
		if(mRenRenContacts.size()>0){
			if(SystemUtil.mDebug){
				SystemUtil.logd("从内存中加载联系人:");
			}
			this.notifyNewObserver(NewContactsDataObserver.DATA_STATE_OK,NewContactsDataObserver.TYPE_RENREN);
			return ;
		}
		//end 内存中已经存在  不用重复加载
		
		int size = mThirdContactsDAO.querySumCount() ;
		if(SystemUtil.mDebug){
			SystemUtil.logd("数据库人人联系人 size="+size);
		}
		if (size > 0) {//从数据库中加载联系人
			if(SystemUtil.mDebug){
				SystemUtil.logd("数据库中有联系人");
			}
			refreshRenRenContacts(null);
		} else {//从网络中加载联系人
			if(SystemUtil.mDebug){
				SystemUtil.logd("333网络加载联系人");
			}
			if(ThirdContactsActivity.isFirstGetContactsFormNet()){
				if(SystemUtil.mDebug){
					SystemUtil.logd("正在加载中。。。。");
				}
				this.notifyNewObserver(NewContactsDataObserver.DATA_STATE_OK,NewContactsDataObserver.TYPE_RENREN);
				return;
			}
			ThirdContactsActivity.setFirstGetContactsFormNet(true);
			this.notifyNewObserver(NewContactsDataObserver.DATA_STATE_OK,NewContactsDataObserver.TYPE_RENREN);
			this.query_third_contacts_fromNet(null);
		}
	}
	
	
	/**加载全部联系人 */
	public  void loadAllContacts(PullUpdateTouchListener listener){
		
		if(SystemUtil.mDebug){
			SystemUtil.traces();
			SystemUtil.logd("加载全部联系人");
		}
		
		//内存中已经存在  不用重复加载
		if(mSortContacts_All.size()>0){
			if(SystemUtil.mDebug){
				SystemUtil.logd("从内存中加载联系人");
			}
			this.notifyNewObserver(NewContactsDataObserver.DATA_STATE_OK,NewContactsDataObserver.TYPE_SIXIN);
			return ;
		}
		//end 内存中已经存在  不用重复加载
		
		loadSiXinContacts();
		
		if(mSixinContacts.size()==0){
			if(SystemUtil.mDebug){
				SystemUtil.logd("网络加载联系人");
			}
			mergeAllContacts(false);
			if(C_ContactScreen.isFirstGetContactsFormNet()){
				if(SystemUtil.mDebug){
					SystemUtil.logd("已经开始加载了  网络加载联系人  不用重复加载");
				}
				this.notifyNewObserver(NewContactsDataObserver.DATA_STATE_OK,NewContactsDataObserver.TYPE_SIXIN);
				return ;
			}
			C_ContactScreen.setFirstGetContactsFormNet(true);
			this.notifyNewObserver(NewContactsDataObserver.DATA_STATE_OK,NewContactsDataObserver.TYPE_SIXIN);
			query_Contacts_FromNet(null);
		}else{
			refreshAllContacts();
		}
		
//		if (mContactsDAO.querySumCount() > 0) {//从数据库中加载联系人
//			if(SystemUtil.mDebug){
//				SystemUtil.logd("数据库中有联系人");
//			}
//			refreshAllContacts(null);
//		} else {//从网络中加载联系人
//			if(SystemUtil.mDebug){
//				SystemUtil.logd("网络加载联系人");
//			}
//			mergeAllContacts(false);
//			C_ContactScreen.setFirstGetContactsFormNet(true);
//			this.notifyNewObserver(NewContactsDataObserver.DATA_STATE_OK,NewContactsDataObserver.TYPE_SIXIN);
//			query_Contacts_FromNet(null);
//		}
		
	}
	
	/**加载私信联系人*/
	public  void loadSiXinContacts(){
		if(mSixinContacts.size()>0){
			if(SystemUtil.mDebug){
				SystemUtil.logd("内存中有私心联系人");
			}
			return ;
		}
		if (mContactsDAO.querySumCount() > 0) {//从数据库中加载联系人
			if(SystemUtil.mDebug){
				SystemUtil.logd("数据库中有联系人");
			}
			List<ContactModel> tmp = mContactsDAO.query_AllContact();
			if(tmp!=null){
				mSixinContacts.addAll(tmp);	
				//Arrays.fill(mSiXin_Indexs, -1);
				if(mSixinContacts.size()>0){
					resetIndex(mSixinContacts,mSiXin_Indexs);
				}
			}else{
				if(SystemUtil.mDebug){
					SystemUtil.errord("这个地方不应该为空  请看一下其他地方是否有数据库异常问题  而导致没有查到数据");
				}
			}
		}
	}
	
	/**
	 * 通过读取本地数据库获取所有联系人列表  包括私信和群组
	 * **/
	public final void refreshAllContacts() {	
		if(SystemUtil.mDebug){
			SystemUtil.errord("刷新所有联系人列表");
		}
		//加载私信群组
		querySiXinGroups();
		//合并到全部联系人中
		mergeAllContacts(true);
		//this.notifyObserver(ContactsDataObserver.DATA_STATE_OK);
		this.notifyNewObserver(NewContactsDataObserver.DATA_STATE_OK,NewContactsDataObserver.TYPE_SIXIN);
	}
	
	public void nofifyRoomChanged(){
		querySiXinGroups();
		mergeAllContacts(true);
		this.notifyNewObserver(NewContactsDataObserver.DATA_STATE_OK,NewContactsDataObserver.TYPE_GROUP);
	}
	
//	private final void refreshSiXinContacts(){
//		Arrays.fill(mSiXin_Indexs, -1);
//		if(mSixinContacts.size()>0){
//			resetIndex(mSixinContacts,mSiXin_Indexs);
//		}
//	}
	
	private final void refreshRenRenContacts(List<ThirdContactModel> contacts){
		synchronized (mRenRenContacts) {	
			mRenRenContacts.clear();
			Arrays.fill(mRenRen_Indexs, -1);
			if(contacts==null){
				List<ThirdContactModel> tmp = mThirdContactsDAO.query_AllContact();
				if(tmp!=null){
					mRenRenContacts.addAll(tmp);	
				}else{
					if(SystemUtil.mDebug){
						SystemUtil.errord("这个地方不应该为空  请看一下其他地方是否有数据库异常问题  而导致没有查到数据");
					}
				}
			}else{
				mRenRenContacts.addAll(contacts);
			}
			if ( mRenRenContacts.size() > 0) {
				ThirdContactModel first = mRenRenContacts.get(0);
				char preAleph = first.mAleph;
				first.mIsShowAlephLabel_InAll = true;
				this.setIndex(preAleph, 0, mRenRen_Indexs);
				int index=0;
				for (ThirdContactModel m : mRenRenContacts) {
					if (first != m) {
						if (m.mAleph == preAleph) {
							m.mIsShowAlephLabel_InAll = false;
						} else {
//							if(SystemUtil.mDebug){
//								SystemUtil.logd("显示字母条="+m.getName()+"#"+m.getmUserId());
//							}
							m.mIsShowAlephLabel_InAll = true;
							preAleph = m.mAleph;
							this.setIndex(preAleph, index, mRenRen_Indexs);
						}
					}
					index++;
				}
			}
			this.notifyNewObserver(NewContactsDataObserver.DATA_STATE_OK,NewContactsDataObserver.TYPE_RENREN);
		}
	}
	
	/**获取群组联系人 必须保证已经获取了联系人 */
	private final void querySiXinGroups(){
		if(RoomInfosData.getInstance().getRoomsList() == null || RoomInfosData.getInstance().getRoomsList().size()==0){
			RoomInfosData.getInstance().loadRoomInfoListFromDB();
		}
		ArrayList<RoomInfoModelWarpper> roomInfoModelWarppers = RoomInfosData.getInstance().getRoomsList();
		if(roomInfoModelWarppers==null){
			return;
		}
		if(SystemUtil.mDebug){
			SystemUtil.logd("roomInfoModelWarppers.size()="+roomInfoModelWarppers.size());
		}
	   
		mSortGroupContacts.clear();
		GroupContactModel gcm;
		//这个地方要判断是否添加到了通讯录中
		for (RoomInfoModelWarpper roomInfoModelWarpper : roomInfoModelWarppers) {
			if(SystemUtil.mDebug){
				SystemUtil.errord("fdfdf="+roomInfoModelWarpper.mDisable+"#"+roomInfoModelWarpper.mRoomIsContact+"#name="+roomInfoModelWarpper.getName());
			}
			if(roomInfoModelWarpper.mDisable==1 || roomInfoModelWarpper.mRoomIsContact!=1){//1表示已经废弃   第二个1表示是通讯录联系人
				continue;
			}
			gcm= new GroupContactModel();
			if(roomInfoModelWarpper.mMembers.size() == 0){
				gcm.setDefaultUrl(NotSynImageView.GROUP_DEFUALT_HEAD_URL);
			}else{
				if(SystemUtil.mDebug){
					SystemUtil.errord("mmevbers size="+roomInfoModelWarpper.mMembers.size());
				}
				//Logd.error("mmevbers size="+roomInfoModelWarpper.mMembers.size());
				for (ContactModel cm : roomInfoModelWarpper.mMembers) {
		    		gcm.addHeadUrl(cm.getHeadUrl());
		    		//gcm.addId(cm.getUId());
		    		if(SystemUtil.mDebug){
		    			SystemUtil.logd("name="+cm.getmContactName()+"#id="+cm.getmUserId()+"#demo="+cm.getDomain());
		    		}
				}
			}
			//Logd.error("end seturl");
			gcm.setGroupId(roomInfoModelWarpper.getUId());
			gcm.setGroupName(roomInfoModelWarpper.getName());
			mSortGroupContacts.add(gcm);
		}
	}
	

	public final void mergeAllContacts(boolean hasData){ 
		synchronized (mSortContacts_All) {
			mSortContacts_All.clear();
			Arrays.fill(mAll_Indexs, -1);
			this.setIndex(C_LetterBar.CHAR_SEARCH, C_LetterBar.INDEX_SEARCH_CHAR , mAll_Indexs);

			if(hasData){
				int start = mSortGroupContacts.size();
				
				int size=mSixinContacts.size();
				
				int specialNum = 3;
				
				if ( size > 0) {
					ContactModel first = mSixinContacts.get(0);
					char preAleph = first.mAleph;
					first.mIsShowAlephLabel_InAll = true;
					this.setIndex(preAleph, start+specialNum, mAll_Indexs);
					
					int index=0;
					for (ContactModel m : mSixinContacts) {
						if (first != m) {
							if (m.mAleph == preAleph) {
								m.mIsShowAlephLabel_InAll = false;
							} else {
//								if(SystemUtil.mDebug){
//									SystemUtil.logd("显示字母条="+m.getName()+"#"+m.getmUserId());
//								}
								m.mIsShowAlephLabel_InAll = true;
								preAleph = m.mAleph;
								this.setIndex(preAleph, index+start+specialNum, mAll_Indexs);
							}
						}
						index++;
					}
				}
				
				mSortContacts_All.addAll(mSixinContacts);
				
				if(start>0){
					mSortContacts_All.addAll(0,mSortGroupContacts);	
					mSortContacts_All.get(0).mIsShowAlephLabel_InAll=true;
					this.setIndex(C_LetterBar.CHAR_GROUP, C_LetterBar.INDEX_GROUP_CHAR , mAll_Indexs);
				}
			}
			if(mRenRenContact==null){
				mRenRenContact = new SpecialContactModel(ContactModel.Contact_group_type.SPECIAL_RENREN);
			}
			mSortContacts_All.add(0,mRenRenContact);
			if(mRecommendContact==null){
				mRecommendContact = new SpecialContactModel(ContactModel.Contact_group_type.SPECIAL_RECOMMEND);
			}
			mSortContacts_All.add(0,mRecommendContact);
		}
	}
	
	
	
	private final void resetIndex(List<ContactModel> contacts, int[] indexs){
		Arrays.fill(indexs, -1);
		this.setIndex(C_LetterBar.CHAR_SEARCH, C_LetterBar.INDEX_SEARCH_CHAR , indexs);
		int start = 1;
		if(contacts.size()>0){
			ContactModel first = contacts.get(0);
			char preAleph = first.mAleph;
			first.mIsShowAlephLabel_InAll = true;
			this.setIndex(preAleph, start, indexs);
			int index = 0;
			for (ContactModel m : contacts) {
				if (first != m) {
					if (m.mAleph == preAleph) {
						m.mIsShowAlephLabel_InAll = false;
					} else {
						
						m.mIsShowAlephLabel_InAll = true;
						preAleph = m.mAleph;
						this.setIndex(preAleph, index+start,indexs);
						//Log.v("asas", "preAleph = " + preAleph);
						//Log.v("asas", "indexs = " + indexs);
					}
				}
				index++;
			}
		}	
		
	}
	
    public final  void query_third_contacts_fromNet(PullUpdateTouchListener listener){
    	//mRenRenContact 
    	ThirdContactsActivity.setFirstGetContactsFormNet(true);
    	mThirdContactsDAO.getContactsRequest(new OnDownloadThirdContacts(listener),"renren", mRenRenContact.getBindedRenRenAccount());
	}
    
	class OnDownloadThirdContacts implements DownloadThirdListener{
		
		PullUpdateTouchListener listener;
		public OnDownloadThirdContacts(PullUpdateTouchListener listener){
			this.listener = listener;
		}
		@Override
		public void onSuccess(List<ThirdContactModel> noSortList) {
	        if(SystemUtil.mDebug){
	        	SystemUtil.errord("Onsucess -------第三方联系人-------size="+noSortList.size());
			}
			
			//排序
			final ArrayList<ThirdContactModel>  sortedContactList = ContactResrouceUtils
					.combineOrderd(noSortList);
	        if(SystemUtil.mDebug){
	        	SystemUtil.errord("after sort -------size="+noSortList.size());
			}
			
			RenrenChatApplication.mHandler.post(new Runnable() {
				@Override
				public void run() {
					if(listener!=null){
						if(SystemUtil.mDebug){
							SystemUtil.logd("第三方联系人 结束下拉刷新");
						}
						listener.onRefreshComplete();
					}
					ThirdContactsActivity.setFirstGetContactsFormNet(false);
					refreshRenRenContacts(sortedContactList);
				}
			});
			//从数据库中删除
			mThirdContactsDAO.delete_AllContacts();
			//插入已经排序后的联系人
			mThirdContactsDAO.insert_Contacts(sortedContactList);
			
		}

		@Override
		public void onError() { 
			RenrenChatApplication.mHandler.post(new Runnable() {
				@Override
				public void run() {
					SystemUtil.errord("error");
					if(listener!=null){
						if(SystemUtil.mDebug){
							SystemUtil.logd("第三方联系人 结束下拉刷新");
						}
						listener.onRefreshComplete();
					}
					ThirdContactsActivity.setFirstGetContactsFormNet(false);
					notifyNewObserver(NewContactsDataObserver.DATA_STATE_ERROR,NewContactsDataObserver.TYPE_RENREN);
				}
			});
			
		}
	}
	
	/**
	 * 通过网络获取所有联系人列表
	 * **/
	public final void query_Contacts_FromNet(PullUpdateTouchListener listener) {
		if(SystemUtil.mDebug){
			SystemUtil.errord("query_Contacts_FromNet");	
		}
		mContactsDAO.getContactsRequest(new OnDownloadAllContactsStatus(listener));
	}
	//全部联系人下载接口 //////////////////////////////////////////////////////////////////////////////////////////////
	class OnDownloadAllContactsStatus implements OnContactsAndRoomDownloadListener{
		PullUpdateTouchListener listener;
		public OnDownloadAllContactsStatus(PullUpdateTouchListener listener){
			this.listener = listener;
		}
		@Override
		public long getUid() {
			return LoginManager.getInstance().getLoginInfo().mUserId;
		}

		@Override
		public void onSuccess(List<ContactModel> noSortList,List<RoomInfoModelWarpper> roomlist) {
	        if(SystemUtil.mDebug){
				SystemUtil.errord("Onsucess -------联系人-------size="+noSortList.size());
			}
			//排序
			final ArrayList<ContactModel>  sortedContactList = ContactResrouceUtils
					.combineResrouceOrderd(noSortList);
			
			
			//从数据库中删除
			mContactsDAO.delete_AllFirends();
			//插入已经排序后的联系人
			mContactsDAO.insert_Contacts(sortedContactList);
			
	        if(SystemUtil.mDebug){
	        	SystemUtil.errord("结束插入联系人  下载的群组联系人："+roomlist.size());
			}
			
			if(roomlist.size()>0){//将网络群组加入数据库{
				RoomInfosData instance=RoomInfosData.getInstance();
				ArrayList<RoomInfoModelWarpper>  rooms=instance.getRoomsList();
			    for (RoomInfoModelWarpper rimw : rooms) {  
					for(RoomInfoModelWarpper group : roomlist){
						if(rimw.getUId() == group.getRoomId()){
							group.setDisabled(rimw.mDisable);
						}
					}
				}
			    
			    for(RoomInfoModelWarpper group : roomlist){
			    	group.mLocalUserId = LoginManager.getInstance().getLoginInfo().mUserId;
			    	group.updateToDB();
			    }
			    
			    for (RoomInfoModelWarpper rimw : roomlist) {
					for (ContactModel contactModel : rimw.mMembers) {
						if((contactModel.mRelation&ContactModel.Relationship.IS_CONTACT) != ContactModel.Relationship.IS_CONTACT){ //<TODO> 从服务器获取
							contactModel.setmComplete(ContactModel.COMPLETE_NO);
							addContact(contactModel);
						}
					}
				}
			}
			
			RenrenChatApplication.mHandler.post(new Runnable() {
				@Override
				public void run() {
					if(listener!=null){
				        if(SystemUtil.mDebug){
				        	SystemUtil.logd("结束下拉刷新");
						}
						listener.onRefreshComplete();
					}
					C_ContactScreen.setFirstGetContactsFormNet(false);
					mSixinContacts.clear();
					mSixinContacts.addAll(sortedContactList);
					resetIndex(mSixinContacts,mSiXin_Indexs);
					refreshAllContacts();
					RoomInfosData.getInstance().notifyObserver(RoomInfosDataObserver.DATA_STATE_OK,-1);
					//mOldRefreshStatusTime = System.currentTimeMillis();
				}
			});
			
			
			
			
		}

		@Override
		public void onError() { 
	        if(SystemUtil.mDebug){
	        	SystemUtil.errord("error---------");
			}
			RenrenChatApplication.mHandler.post(new Runnable() {
				@Override
				public void run() {
					C_ContactScreen.setFirstGetContactsFormNet(false);
					if(listener!=null){
						listener.onRefreshComplete();
					}
					SystemUtil.toast(R.string.contact_refresh_error);
					notifyNewObserver(NewContactsDataObserver.DATA_STATE_ERROR,NewContactsDataObserver.TYPE_SIXIN);
				}
			});
		}

		@Override
		public boolean check(long uid) {
			return uid == LoginManager.getInstance().getLoginInfo().mUserId;
		}	
	}
	
	//end全部联系人下载接口/////////////////////////////////////////////////////////////////////////////////////////////////
	
  /**
	 * 搜索网络联系人
	 * **/
	public final void query_FindContacts_FromNet(String key) {
		if(SystemUtil.mDebug){
			SystemUtil.errord("find screen from net");
		}
		mContactsDAO.getFindContactsRequest(new OnDownloadContacts(),key);
	}
	
	class OnDownloadContacts implements OnContactsDownloadListener{
		public OnDownloadContacts(){}
		@Override
		public long getUid() {
			return LoginManager.getInstance().getLoginInfo().mUserId;
		}

		@Override
		public void onSuccess(List<ContactModel> noSortList) {
			//SystemUtil.toast("下载联系人完成");
			//SystemUtil.waitTime(30000L);
	        if(SystemUtil.mDebug){
				SystemUtil.errord("Onsucess -------查找联系人-------size="+noSortList.size());
			}
			//排序
			final ArrayList<ContactModel>  sortedContactList = ContactResrouceUtils
					.combineResrouceOrderd(noSortList);
			mContacts_Find.clear();
			for (ContactModel contactModel : sortedContactList) {
				if(contactModel.getmUserId() == RenrenChatApplication.USER_ID){
					sortedContactList.remove(contactModel);
					break;
				}
			}
			mContacts_Find.addAll(sortedContactList);
			RenrenChatApplication.mHandler.post(new Runnable() {
				@Override
				public void run() {
					notifyNewObserver(NewContactsDataObserver.DATA_STATE_OK,NewContactsDataObserver.TYPE_FIND);
				}
			});
		}

		@Override
		public void onError() { 
			 if(SystemUtil.mDebug){
				SystemUtil.errord("Onsucess查找联系人失败");
			}
			 mContacts_Find.clear();
			RenrenChatApplication.mHandler.post(new Runnable() {
				@Override
				public void run() {
					notifyNewObserver(NewContactsDataObserver.DATA_STATE_ERROR,NewContactsDataObserver.TYPE_FIND);
				}
			});
		}

		@Override
		public boolean check(long uid) {
			return uid == LoginManager.getInstance().getLoginInfo().mUserId;
		}	
	}
	
	
	public void addBlackList(long uid, int mIsFriend) {
		if(mIsFriend == ContactModel.Relationship.CONTACT){//联系人加黑名单
	        if(SystemUtil.mDebug){
	        	SystemUtil.logd("联系人加黑名单");	
			}
			mContactsDAO.updateContact(uid,ContactModel.Relationship.BLACK_LIST_CONTACT);
			notifyNewObserver(NewContactsDataObserver.DATA_STATE_OK,NewContactsDataObserver.TYPE_SIXIN);
			
		}else if (mIsFriend == ContactModel.Relationship.STRANGER){//陌生人加黑名单
	        if(SystemUtil.mDebug){
	        	SystemUtil.logd("陌生人加黑名单");	
			}
			mContactsDAO.updateContact(uid,ContactModel.Relationship.BLACK_LIST_STRANGER);
		}		
	}
	
	public void removeBalckList(long uid,int mIsFriend){
		if(mIsFriend == ContactModel.Relationship.BLACK_LIST_CONTACT){//移除联系人黑名单
			mContactsDAO.updateContact(uid,ContactModel.Relationship.CONTACT);
			notifyNewObserver(NewContactsDataObserver.DATA_STATE_OK,NewContactsDataObserver.TYPE_SIXIN);
		}else if(mIsFriend == ContactModel.Relationship.BLACK_LIST_STRANGER){//移除陌生人黑名单
			mContactsDAO.updateContact(uid,ContactModel.Relationship.STRANGER);
		}
		
	}
	
	public void updateIsFriend(long uid,byte friend){
		if(SystemUtil.mDebug){
			SystemUtil.logd("改变联系人的关系 relationship="+Integer.toBinaryString(friend)+"#uid="+uid+"#"+(friend&ContactBaseModel.Relationship.IS_CONTACT));
		}
		if((friend&ContactBaseModel.Relationship.IS_CONTACT)!=ContactBaseModel.Relationship.IS_CONTACT){
			boolean exist = false;
			for (ContactModel model : mSixinContacts) {
				if(model.getmUserId() == uid){
					mSixinContacts.remove(model);
					exist = true;
					if(SystemUtil.mDebug){
						SystemUtil.logd("改变联系人的关系  从列表中删除这个哥们="+model.getmContactName());
					}
					break;
				}
			}
			if(exist){
				C_ContactsData.getInstance().refreshAllContacts();
			}
		}
		mContactsDAO.updateContact(uid,friend);
	}
	
	public SpecialContactModel getRecommendContact(){
		return mRecommendContact;
	}
	
	public SpecialContactModel getBindRenRenContact(){
		return mRenRenContact;
	}
	
	
	/**
	 * 根据联系人类型   实现对联系人数据变更的监听
	 * **/
	public static interface NewContactsDataObserver {
		/** 数据正常更新 */
		byte DATA_STATE_OK = 1;
		/** 数据更新中出现异常*/
		byte DATA_STATE_ERROR = 0;
		
		//监听数据的类型 监听多个可以将类型与在一起
		byte TYPE_RENREN = 1;
		byte TYPE_SIXIN = 2;
		byte TYPE_GROUP = 4;
		byte TYPE_SPECIAL = 8;
		byte TYPE_COMMON =16;
		byte TYPE_SEARCH = 32;
		byte TYPE_FIND = 64;
		
		public void notifyDataUpdate(byte state,byte type);
		public byte getType();
	}
	
	public synchronized void registorNewObserver(NewContactsDataObserver observer) {
		if(observer != null){
			mNewObserverList.add(observer);
		}
	}
	
	public synchronized void unRegistorNewObserver(NewContactsDataObserver observer) {
		if(observer!=null){
			mNewObserverList.remove(observer);
		}
	}

	public synchronized void notifyNewObserver(byte state,byte type) {
        if(SystemUtil.mDebug){
        	SystemUtil.traces();
		}
		int i=0;
		for (NewContactsDataObserver observer : mNewObserverList) {
	        if(SystemUtil.mDebug){
	        	SystemUtil.logd("type="+i+"#"+Integer.toBinaryString(type)+"#"+Integer.toBinaryString(observer.getType()&type)+"#"+Integer.toBinaryString(observer.getType()));
			}
			i++;
			if ((observer.getType()&type) == type) {
				observer.notifyDataUpdate(state,type);
			}
		}
	}
		
	
//	/**
//	 * 从数据库中加载群组联系人l
//	 */
//	public void query_group_contacts_from_DB(){
//		if(RoomInfosData.getInstance().getRoomsList() == null || RoomInfosData.getInstance().getRoomsList().size()==0){
//			RoomInfosData.getInstance().loadRoomInfoListFromDB();
//		}
//		ArrayList<RoomInfoModelWarpper> roomInfoModelWarppers = RoomInfosData.getInstance().getRoomsList();
//		if(roomInfoModelWarppers==null){
//			return;
//		}
//	    //Logd.log("roomInfoModelWarppers.size()="+roomInfoModelWarppers.size());
//		mSortGroupContacts.clear();
//		GroupContactModel gcm;
//		//这个地方要判断是否添加到了通讯录中
//		for (RoomInfoModelWarpper roomInfoModelWarpper : roomInfoModelWarppers) {
//			//Logd.error("fdfdf="+roomInfoModelWarpper.mDisable+"#"+roomInfoModelWarpper.mRoomIsContact+"#name="+roomInfoModelWarpper.getName());
//			if(roomInfoModelWarpper.mDisable==1 || roomInfoModelWarpper.mRoomIsContact!=1){//1表示已经废弃   第二个1表示是通讯录联系人
//				continue;
//			}
//			gcm= new GroupContactModel();
//			if(roomInfoModelWarpper.mMembers.size() == 0){
//				gcm.setDefaultUrl(NotSynImageView.GROUP_DEFUALT_HEAD_URL);
//			}else{
//				//Logd.error("mmevbers size="+roomInfoModelWarpper.mMembers.size());
//				for (ContactModel cm : roomInfoModelWarpper.mMembers) {
//		    		gcm.addHeadUrl(cm.getHeadUrl());
//		    		//gcm.addId(cm.getUId());
//				}
//			}
//			//Logd.error("end seturl");
//			gcm.setGroupId(roomInfoModelWarpper.getUId());
//			gcm.setGroupName(roomInfoModelWarpper.getName());
//			mSortGroupContacts.add(gcm);
//		}
//	}

	
	public void addGroupContact(long roomId){
		if(SystemUtil.mDebug){
		    SystemUtil.logd("保存为群组联系人 roomid="+roomId);
		}
		//Logd.log("addGroupContact----------------------");
		 RoomInfoModelWarpper rimw=RoomInfosData.getInstance().getRoomInfo(roomId);
		 if(rimw==null){
			 return;
		 }
		 for (ContactModel cm : mSortGroupContacts) {
			 if(cm.getmUserId()==roomId){ //重复了
				 //Logd.log("重复了");
				 return;
			 }
		 }
		 GroupContactModel gcm;
		 if(rimw.mDisable!=1&&rimw.mRoomIsContact==1){//没有被废弃 而且已经添加到了通讯录中
			//Logd.log("dfdffdfdfdfdfdf");
			//Logd.log("addGroupContact rimw.mMembers size="+rimw.mMembers.size()); 
	    	gcm= new GroupContactModel();
	    	if(rimw.mMembers.size() == 0){
				gcm.setDefaultUrl(NotSynImageView.GROUP_DEFUALT_HEAD_URL);
			}else{
				for (ContactModel cm : rimw.mMembers) {
					gcm.addHeadUrl(cm.getHeadUrl());
				}
	    	}
			gcm.setGroupId(rimw.getUId());
			gcm.setGroupName(rimw.getName());
			mSortGroupContacts.add(gcm);
			mergeAllContacts(true);
			this.notifyNewObserver(NewContactsDataObserver.DATA_STATE_OK, NewContactsDataObserver.TYPE_GROUP);
		  }else{
			  //Logd.log("error");
		  }
	}
	
	
	public void deleteGroupContact(long roomId){
		if (SystemUtil.mDebug) {
			SystemUtil.logd("删除组roomId="+roomId);
		}
		//Logd.error("deleteGroupContact roomId="+roomId);
		int index=-1;
		int size=mSortGroupContacts.size();
		for (int i = 0; i < size; i++) {
			if(mSortGroupContacts.get(i).getUId()==roomId){
				index=i;
				break;
			}
		}
		if(index>=0){
			mSortGroupContacts.remove(index);	
			mergeAllContacts(true);
			this.notifyNewObserver(NewContactsDataObserver.DATA_STATE_OK, NewContactsDataObserver.TYPE_GROUP);
		}
	}

	public synchronized void addToSearchListAndNotify(List<ContactModel> models) {
		mSortContacts_Search.clear();
		//mSortContactsCommon.clear();
		mSortContacts_Search.addAll(models);
		if (models.size() > 0) {
			ContactModel first = mSortContacts_Search.get(0);
			char preAleph = first.mAleph;
			first.mIsShowAlephLabel_InSearch = true;
			for (ContactModel m : mSortContacts_Search) {
				if (first != m) {
					if (m.mAleph == preAleph) {
						m.mIsShowAlephLabel_InSearch = false;
					} else {
						m.mIsShowAlephLabel_InSearch = true;
						preAleph = m.mAleph;
					}
				}
			}
		}
		this.notifyNewObserver(NewContactsDataObserver.DATA_STATE_OK, NewContactsDataObserver.TYPE_SEARCH);
		//this.notifyObserver(ContactsDataObserver.DATA_STATE_OK);
	}

	private void setIndex(char indexChar, int listIndex, int[] array) {
		if (indexChar < 91 && indexChar > 64) {
			indexChar += 32;
		}
		int index = indexChar - 'a';
		if (index < 26 && index > -1) {
			array[index+C_LetterBar.INDEX_A] = listIndex;
			return ;
		} 
		
		if(indexChar == C_LetterBar.CHAR_GROUP){
			array[C_LetterBar.INDEX_GROUP_CHAR] = listIndex;
		}else if(indexChar == C_LetterBar.CHAR_OTHER){
			array[C_LetterBar.INDEX_OTHER_CHAR] = listIndex;
		}else if(indexChar == C_LetterBar.CHAR_SEARCH){
			array[C_LetterBar.INDEX_SEARCH_CHAR] = listIndex;
		}
	}

	public int getIndex(char indexChar, int [] array) {
		if(array==null){
			return -1;
		}
		if (indexChar < 91 && indexChar > 64) {
			indexChar += 32;
		}
		
		int index = indexChar - 'a';
		SystemUtil.logd("getIndex="+index);
		//SystemUtil.logd("ary",array);
		if (index < 26 && index > -1) {
			SystemUtil.logd("char="+array[index+C_LetterBar.INDEX_A]);
			return array[index+C_LetterBar.INDEX_A];
		} 
		
		if (indexChar == C_LetterBar.CHAR_OTHER) {
			return array[C_LetterBar.INDEX_OTHER_CHAR];
		}else if(indexChar == C_LetterBar.CHAR_GROUP){
			return array[C_LetterBar.INDEX_GROUP_CHAR];
		}else if(indexChar == C_LetterBar.CHAR_SEARCH){
			return array[C_LetterBar.INDEX_SEARCH_CHAR];
		}
		return -1;
	}
	public int getIndex(char indexChar, int type) {
		return getIndex(indexChar,getIndexs(type));
	}
	public int [] getIndexs(int type){
		switch (type) {
		case TYPE.ALL_CONTACTS:
			return mAll_Indexs;
		case TYPE.SIXIN_CONTACTS:
			return mSiXin_Indexs;
		case TYPE.FILTER_CONTACTS:
			return mFilter_Indexs;
		case TYPE.RENREN_CONTACTS:
			return mRenRen_Indexs;
		}
		return null;
	}

	/**
	 * 获取联系人列表
	 * 
	 * @param type
	 *            列表类型
	 * @see C_ContactsData.TYPE
	 * **/
	public synchronized List<ContactModel> get(int type) {
		switch (type) {
		case TYPE.COMMON_CONTACTS:
			return mSortContactsCommon;
		case TYPE.SEARCH_CONTACTS:
			return mSortContacts_Search;
		case TYPE.SIXIN_CONTACTS:
			return mSixinContacts;
		case TYPE.ALL_CONTACTS:
			return mSortContacts_All;
		case TYPE.SIXIN_GROUP:
			return mSortGroupContacts;
		case TYPE.FIND_CONTACTS:
			return mContacts_Find;
		}
		return null;
	}
	
	public synchronized List<ThirdContactModel> getRenRenContacts() {
		return mRenRenContacts;
	}
	
	/**
	 * 查询特别关注好友
	 * @return
	 */
	public List<ThirdContactModel> getRenrenAttentionContacts(){
		List<ThirdContactModel> temp = new LinkedList<ThirdContactModel>();
		for(ThirdContactModel thirdContactModel:mRenRenContacts){
			if(thirdContactModel.getmIsAttention() == ThirdContactModel.ATTENTION.IS_ATTENTION){
				temp.add(thirdContactModel);
			}
		}
		return temp;
	}
	
	/**
	 * 查询单个特别关注好友~
	 */
	
	public ThirdContactModel getRenrenAttentionContact(long userId){
		for(ThirdContactModel thirdContactModel:mRenRenContacts){
			if(thirdContactModel.mUserId == userId){
				return thirdContactModel;
			}
		}
		return null;
	}
	
	/**
	 * 设置某个联系人为特别关注
	 * @return
	 */
	public void setRenrenAttentionState(long id, int state){
		for(ThirdContactModel thirdContactModel:mRenRenContacts){
			if(thirdContactModel.mUserId == id){
				thirdContactModel.setmIsAttention(state);
			}
		}
	}

	public synchronized ArrayList<ContactModel> getTotalList() {
		return (ArrayList<ContactModel>) mSortContacts_All;
	}

	/**
	 * 获取联系人列表的长度
	 * 
	 * @param type
	 *            列表类型
	 * @see C_ContactsData.TYPE
	 * **/
	public synchronized int getSize(int type) {
		List<ContactModel> list = this.get(type);
		int size=list.size();
		return size;
	}


	public  synchronized void addContact(ContactModel model) {
		if(model==null){
			return ;
		}
		if(SystemUtil.mDebug){
			SystemUtil.errord("addContact name="+model.getmContactName()+"#"+model.getmUserId()+"#relaton="+Integer.toBinaryString(+model.getmRelation()));
		}
		if((model.getmRelation()&ContactBaseModel.Relationship.IS_CONTACT) != ContactBaseModel.Relationship.IS_CONTACT){
			StrangerContactCache.getInstance().putStrangerContact(model.getmUserId(),
					model);
		}
		
		mContactsDAO.delete_contact_by_userId(model.getmUserId());
		mContactsDAO.insert_Contact(model);// 将此联系人增加到数据库中
		
		if((model.getmRelation()&ContactBaseModel.Relationship.IS_CONTACT)==ContactBaseModel.Relationship.IS_CONTACT){
			if(SystemUtil.mDebug){
				SystemUtil.logd("新增联系人#id="+model.getmUserId()+"#name="+model.getmContactName()+"#relationship="+Integer.toBinaryString(+model.getmRelation()));
			}
			boolean exist = false;
			for (ContactModel tmp : mSixinContacts) {
				if(tmp.getmUserId()==model.getmUserId()){
					exist = true;
				}
			}
			if(!exist){
				if(SystemUtil.mDebug){
					SystemUtil.logd("重新更新联系人列表 name="+model.getmContactName()+"#id="+model.getmUserId());
				}
				List<ContactModel> tmplist = new ArrayList<ContactModel>();
				tmplist.addAll(mSixinContacts);
				tmplist.add(model);
				List<ContactModel> sorted= ContactResrouceUtils.combineResrouceOrderd(tmplist);
				mSixinContacts.clear();
				mSixinContacts.addAll(sorted);
				C_ContactsData.getInstance().refreshAllContacts();
			}else {
				if(SystemUtil.mDebug){
					SystemUtil.logd("联系人已经存在列表里面 不用重复更新");
				}
			}
		}
	}
	
	public  void addRenrenContact(ThirdContactModel model) {
		if(SystemUtil.mDebug){
			SystemUtil.logd("添加第三方联系人  name="+model.getName()+"#id="+model.getmUserId()+"#relation="+model.getmRelation());
		}
		model.setmIsFriend(ThirdContactModel.IS_FRIEND_NO);
		mThirdContactsDAO.delete_contact_by_userId(model.getmUserId());
		mThirdContactsDAO.insert_Contact(model);
	}
	
	
	public synchronized void deleteSinxiContact(long uid){
		int len = mSixinContacts.size();
		for (int i = 0; i < len; i++) {
			if(mSixinContacts.get(i).getUId()== uid){
				mSixinContacts.remove(i);
				break;
			}
		}
	}
	
	
	/**根据人人id返回联系人  优先返回私信联系人    如果没有返回人人联系人 */
	public ContactBaseModel getContactByRenRenID(long uid, IDownloadContactListener listener){
		if(SystemUtil.mDebug){
			SystemUtil.logd("获取私信或人人   人人id="+uid);
		}
		ThirdContactModel model = getRenRenContactByRenRenID(uid);
		if(model!=null){
			ContactModel cm = getSiXinContact(model.getmUserId());
			if(cm!=null){
				return cm;
			}else{
				return model;
			}
		}else{
			ContactModel cm = getSiXinContactByRenRenID(uid);
			if(cm!=null){
				return cm;
			}
			if(!isLoading(uid,listener)){
				if(SystemUtil.mDebug){
					SystemUtil.logd("从网络加载 单个联系人");
					SystemUtil.traces();
				}
				getRenRenContactInfoFromNet(uid,false);	
			}
			return null;
		}
	}
	
   private ContactModel getSiXinContactByRenRenID(long renrenid){
	   try {
		   for (ContactModel model : mSixinContacts) {
			   BindInfo info = model.getBindInfo(ContactModel.BIND_TYPE_RENREN);
			   if(info!=null){
				   if(SystemUtil.mDebug){
						SystemUtil.errord("name ="+model.getmContactName()+"#id="+model.getmUserId()+"#bindid="+info.getmBindId()+"#renrenid="+renrenid);
					}
				   if(Long.parseLong(info.getmBindId())== renrenid){
					   if(SystemUtil.mDebug){
							SystemUtil.logd("来自私信缓存");
						}
					   return model;
				   }
			   }
		   }
	  } catch (Exception e) {
		 if(SystemUtil.mDebug){
				SystemUtil.errord("excepion=="+e.toString());
				e.printStackTrace();
		 }
	  }
	  return null;
   }
	
	private boolean isLoading(long uid,IDownloadContactListener listener){
		if(contactCache.containsKey(uid)){
			List<IDownloadContactListener> tmplist=contactCache.get(uid);
			if(tmplist!=null){
				if(listener!=null){
					tmplist.add(listener);
				}
			}
			return true;
		}else{
			List<IDownloadContactListener> tmplist= new ArrayList<IDownloadContactListener>();
			if(listener!=null){
				tmplist.add(listener);	
			}
			contactCache.put(uid, tmplist);
			return false;
		}
	}
	
	private void endLoad(boolean succeed,ContactBaseModel model,long uid){
		if(SystemUtil.mDebug){
			SystemUtil.mark();
		}
		
		//加入数据库 以及陌生人缓存
		//两种情况回去下载联系人  1.本地没有用户的资料   2.本地用户资料不全  
		if(succeed){
			if(model instanceof ThirdContactModel){
				//Log.v("ff","===============================end load 1");
				addRenrenContact((ThirdContactModel)model);
			}else if(model instanceof ContactModel){
				//Log.v("ff","===============================end load 2");
				addContact((ContactModel)model);
			}
		}
		List<IDownloadContactListener> tmplist = contactCache.get(uid);
		if(tmplist!=null){
			for (IDownloadContactListener iDownloadContactListener : tmplist) {
				if(succeed){
					if(SystemUtil.mDebug){
						SystemUtil.mark("onsussess");
					}
					iDownloadContactListener.onSussess(model);
				}else{
					if(SystemUtil.mDebug){
						SystemUtil.mark("error");
					}
					iDownloadContactListener.onError();
				}
			}
		}
		
		contactCache.remove(uid);
		
	}
	
	private ContactModel getSiXinContact(long uid){
		
		if(SystemUtil.mDebug){
			SystemUtil.logd("uid="+uid);
			SystemUtil.traces();
		}
		
		for (ContactModel m : mSixinContacts) {
			if (m.mUserId == uid) {
				if(SystemUtil.mDebug){
					SystemUtil.logd("from sixin 缓存 name="+m.getmContactName()+"#ID="+m.getmUserId());
				}
				//Log.v("ff","=====================11111111111111111");
				return m;
			}
		}
		
		for (ContactModel m : mContacts_Find) {
			if (m.mUserId == uid) {
				if(SystemUtil.mDebug){
					SystemUtil.logd("from 搜索 name="+m.getmContactName()+"#ID="+m.getmUserId());
				}
				return m;
			}
		}
		
		ContactModel  contactModel = StrangerContactCache.getInstance().getStrangerContactByUserId(uid);
		if(contactModel!=null){
			//Log.v("ff","=====================2222222222222222");
			return contactModel;
		}
		
		
		//Log.v("ff","=====================333333333");
		
		return null;
	}
	
	public ContactModel getSiXinContact(long uid,IDownloadContactListener listener) {		
		ContactModel tmp = getSiXinContact(uid);
		if(tmp!=null){
//			if(!tmp.isCompleted()){
//				if(SystemUtil.mDebug){
//					SystemUtil.logd("有用户资料 但不完善  开始从网络请求");
//				}
//				getSixinContactInfoFromNet(uid,listener);
//			}
			return tmp;
		}
		getSixinContactInfoFromNet(uid,listener);
		
		return null;
	}
	
	
	/**获取完整的联系人,如果发现用户资料不完整会从网络上获取，目前只用于联系人详情 */
	public ContactModel getWholeSiXinContact(long uid,IDownloadContactListener listener) {		
		ContactModel tmp = getSiXinContact(uid);
		if(tmp!=null){
			if(!tmp.isCompleted()){
				if(SystemUtil.mDebug){
					SystemUtil.logd("有用户资料 但不完善  开始从网络请求 name="+tmp.getmContactName()+"#ID="+tmp.getmUserId());
				}
				getSixinContactInfoFromNet(uid,listener);
			}
			return tmp;
		}
		getSixinContactInfoFromNet(uid,listener);
		
		return null;
	}

	
	
	private ThirdContactModel getRenRenContactBySiXinID(long uid){
		
		for (ThirdContactModel m : mRenRenContacts) {
//			SystemUtil.logd("muid="+m.mUserId+"#uid="+uid+"#name="+m.getmContactName());
			if (m.mUserId == uid) {
				if(SystemUtil.mDebug){
					SystemUtil.logd("from renren 缓存  name="+m.getmContactName()+"#id="+m.getmUserId());
				}
				return m;
			}
		}
		
		ThirdContactModel contactModel = mThirdContactsDAO.query_Contact_BySiXinId(uid);
		if(contactModel!=null){
			if(SystemUtil.mDebug){
				SystemUtil.logd("from renren 数据库 name="+contactModel.getmContactName()+"#id="+contactModel.getmUserId());
			}
			return contactModel;
		}
		
		return null;
	}
	
 private ThirdContactModel getRenRenContactByRenRenID(long uid){
		
		for (ThirdContactModel m : mRenRenContacts) {
//			SystemUtil.logd("muid="+m.mUserId+"#uid="+uid+"#name="+m.getmContactName());
			if (m.getmRenRenId() == uid) {
				if(SystemUtil.mDebug){
					SystemUtil.logd("from renren 缓存name="+m.getmContactName()+"#id="+m.getmUserId());
				}
				return m;
			}
		}
		
		ThirdContactModel contactModel = mThirdContactsDAO.query_Contact_ByRenRenId(uid);
		if(contactModel!=null){
			if(SystemUtil.mDebug){
				SystemUtil.logd("from renren 数据库 name="+contactModel.getmContactName()+"#id="+contactModel.getmUserId());
			}
			return contactModel;
		}
		
		return null;
	}
	
	
	
	public ThirdContactModel getRenRenContactBySiXinID(long uid,IDownloadContactListener listener) {
		
		ThirdContactModel tmp = getRenRenContactBySiXinID(uid);
		if(tmp!=null){
			return tmp;
		}
		
		if(!isLoading(uid,listener)){
			if(SystemUtil.mDebug){
				SystemUtil.logd("从网络加载 单个联系人uid="+uid);
				SystemUtil.traces();
			}
			getRenRenContactInfoFromNet(uid,true);	
		}
		return tmp;
	}
	
  public ThirdContactModel getRenRenContactByRenRenID(long uid,IDownloadContactListener listener) {
		ThirdContactModel tmp = getRenRenContactByRenRenID(uid);
		if(tmp!=null){
			return tmp;
		}
		
		if(!isLoading(uid,listener)){
			if(SystemUtil.mDebug){
				SystemUtil.logd("从网络加载 单个联系人 uid="+uid);
				SystemUtil.traces();
			}
			getRenRenContactInfoFromNet(uid,false);	
		}
		return tmp;
	}
	
	/**
	 * 从网络获取单个联系人的详细信息
	 * **/
	private  void getSixinContactInfoFromNet(final long uid) {
		if(mSixinContacts.size()==0){
			if (mContactsDAO.querySumCount() == 0){
				if(!C_ContactScreen.isFirstGetContactsFormNet()){
					if(SystemUtil.mDebug){
						SystemUtil.logd("从网络加载联系人  本地没有联系人数据  开始从网络上加载 uid="+uid);
					}
					query_Contacts_FromNet(null);

				}
			}
		}
		INetResponse response = new INetResponse() {
			public void response(final INetRequest req, final JsonValue result) {

				if (result instanceof JsonObject) {
					final JsonObject obj = (JsonObject) result;
					if(SystemUtil.mDebug){
						SystemUtil.logd("下载的陌生人22：uid="+uid+"#"+obj.toJsonString());
					}
					RenrenChatApplication.mHandler.post(new Runnable() {
						@Override
						public void run() {
							if (ResponseError.noError(req, obj,false)) {
								ContactModel contactModel = ContactModel
										.newParseContactModel(obj);
								if(SystemUtil.mDebug){
									if(contactModel!=null){
										SystemUtil.errord("陌生人名字："+contactModel.getmContactName()+"#id="+contactModel.getUId());
									}else{
										SystemUtil.errord("oooooooooooo");
									}
								}
								//addContactAndNotify(contactModel);
								//listener.onSussess();
								endLoad(true,contactModel,uid);
							} else {
								if(SystemUtil.mDebug){
									SystemUtil.errord("error");
								}
								endLoad(false,null,uid);
							}
						}
					});

				}
			}
		};
		McsServiceProvider.getProvider().getSixinInfo(response,uid+"");
	}
	
	private  void  getSixinContactInfoFromNet(final long uid,IDownloadContactListener listener){
		if(!isLoading(uid,listener)){
			if(SystemUtil.mDebug){
				SystemUtil.errord("从网络加载 单个联系人 uid="+uid);
				SystemUtil.traces();
			}
			getSixinContactInfoFromNet(uid);	
		}else{
			if(SystemUtil.mDebug){
				SystemUtil.logd("已经下载了 uid="+uid);
			}
		}
	}
	
	/**
	 * 从网络获取单个联系人的详细信息
	 * **/
	private  void getRenRenContactInfoFromNet(final long uid,boolean isSiXinId) {
		INetResponse response = new INetResponse() {
			public void response(final INetRequest req, final JsonValue result) {
				if (result instanceof JsonObject) {
					final JsonObject obj = (JsonObject) result;
					if(SystemUtil.mDebug){
						SystemUtil.logd("下载的陌生人：uid="+uid+"#"+obj.toJsonString());
					}
					RenrenChatApplication.mHandler.post(new Runnable() {
						@Override
						public void run() {
							//listener.onDowloadOver();
							if (ResponseError.noError(req, obj,false)) {
								ThirdContactModel contactModel = ThirdContactModel
										.newParseContactModel(obj);
								if(SystemUtil.mDebug){
									if(contactModel!=null){
										SystemUtil.errord("陌生人名字："+contactModel.getmContactName()+"#id="+contactModel.getmUserId());	
									}else{
										SystemUtil.errord("oooooooooooo");
									}
								}
								//addContactAndNotify(contactModel);
								//listener.onSussess();
								endLoad(true,contactModel,uid);
								
							} else {
								//listener.onError();
								endLoad(false,null,uid);
								if(SystemUtil.mDebug){
									SystemUtil.errord("99999");
								}
							}
						}
					});

				}
			}
		};
		if(isSiXinId){
			McsServiceProvider.getProvider().getThirdFriendsInfo(response,uid+"","renren","",0,0);	
		}else{
			//<cf TODO> 根据人人id获取联系人
			McsServiceProvider.getProvider().getThirdFriendsInfo(response,"","renren",String.valueOf(uid),0,0);	
		}
		
	}

	/**
	 * 获取特定类型的联系人列表中特定位置的联系人信息
	 * 
	 * @param type
	 *            列表类型
	 * @see C_ContactsData.TYPE
	 * 
	 * @param position
	 *            位置
	 * **/
	public synchronized ContactModel getItem(int type, int position) {
		List<ContactModel> list = this.get(type);
		if (position < list.size()) {
			return list.get(position);
		}
		return null;
	}


	/**
	 * 清空所有列表
	 * **/
	public synchronized void clear() {
		
		if(SystemUtil.mDebug){
			SystemUtil.errord("情况数据缓存");
		}
		//Logd.log("clear");
		mSortContacts_All.clear();
		mSortContactsCommon.clear();
		mSortContacts_Search.clear();
		mSortGroupContacts.clear();
	//	mObserverList.clear();
		mNewObserverList.clear();
		mRenRenContacts.clear();
		mSixinContacts.clear();
		if(mRecommendContact!=null){
			mRecommendContact.clear();
			mRecommendContact = null;
		}
		if(mRenRenContact!=null){
			mRenRenContact.clear();
			mRenRenContact = null;
		}
		//isLoadedContact = false;
	}

	/**
	 * 设置模型标题栏展示标志位
	 */
	public void resetListFlag(List<ContactModel> list) {
		if (list.size() > 0) {
			ContactModel first = list.get(0);
			String preAlephStr = first.mAlephString;
			first.mIsShowAlephLabel_InOnline = true;
			for (ContactModel m : list) {
				if (first != m) {
					if (m.mAlephString.equalsIgnoreCase(preAlephStr)) {
						m.mIsShowAlephLabel_InOnline = false;
					} else {
						m.mIsShowAlephLabel_InOnline = true;
						preAlephStr = m.mAlephString;
					}
				}
			}
		}
	}
	
	public List<ContactModel> updateIndex(List<ContactModel> result){
		Arrays.fill(mFilter_Indexs, -1);
		if (result.size() > 0) {
			ContactModel first = result.get(0);
			char preAleph = first.mAleph;
			first.mIsShowAlephLabel_Choose = true;
			//Logd.error("first="+first.getName()+"#preAleph="+preAleph);
			this.setIndex(preAleph,0, mFilter_Indexs);
			int index = 0;
			for (ContactModel m : result) {
				if (first != m) {
					if (m.mAleph == preAleph) {
						m.mIsShowAlephLabel_Choose = false;
					} else {
						m.mIsShowAlephLabel_Choose = true;
						preAleph = m.mAleph;
						this.setIndex(preAleph, index, mFilter_Indexs);
						//Logd.error("index="+index+"#name="+m.getName());
					}
				}
				index++;
			}
		}
		//Logd.log("mFilter_Indexs=",mFilter_Indexs);
		return result;
	}
	

	@Override
	public void notifyRoomInfoDataUpdate(byte state,final long roomid) {
		if(SystemUtil.mDebug){
			SystemUtil.logd("state="+state+"#roomid="+roomid);
		}
		if(state == RoomInfosDataObserver.DATA_STATE_OK){
			for(ContactModel m:mSortGroupContacts){
				if(m.mUserId==roomid){
					HADLER.post(new Runnable() {
						public void run() {
							deleteGroupContact(roomid);
							addGroupContact(roomid);
						}
					});
					return;
					
				}
			}
		}
	}

	/**
	 * add by xiangchao.fan
	 * @return
	 */
	public List<ContactModel> getmSortGroupContacts() {
		return mSortGroupContacts;
	}
	
	
	public List<ContactModel> getFilterContacts(int type,List<ContactModel> filter){
		//Logd.error("getFilterContacts type="+type);
		//Logd.traces();
		//Log.v("cacaca", "type = " + type);
		if(type == TYPE.FILTER_CONTACTS){
			type = TYPE.SIXIN_CONTACTS;
		}
		List<ContactModel> all=get(type);
		List<ContactModel> result=new ArrayList<ContactModel>();
		boolean add;
		for (ContactModel contactModel : all) {
			if(contactModel.getGroupType()== ContactModel.Contact_group_type.SINGLE){
				add=true;
				for (ContactModel cm : filter) {
					if(contactModel.getmUserId()==cm.getmUserId()){
						add=false;
						break;
					}
				}
				if(add){
					result.add(contactModel);
				}
			}
		}
		Arrays.fill(mFilter_Indexs, -1);
		if (result.size() > 0) {
			this.setIndex(C_LetterBar.CHAR_SEARCH, C_LetterBar.INDEX_SEARCH_CHAR , mFilter_Indexs);
			int start = 1;
			ContactModel first = result.get(0);
			char preAleph = first.mAleph;
			first.mIsShowAlephLabel_Choose = true;
			//Logd.error("first="+first.getName()+"#preAleph="+preAleph);
			this.setIndex(preAleph,start, mFilter_Indexs);
			int index = 0;
			for (ContactModel m : result) {
				if (first != m) {
					if (m.mAleph == preAleph) {
						m.mIsShowAlephLabel_Choose = false;
					} else {
						m.mIsShowAlephLabel_Choose = true;
						preAleph = m.mAleph;
						this.setIndex(preAleph, index+start, mFilter_Indexs);
						//Log.v("aac", "preAleph = " + preAleph);
						//Log.v("aac", "index+start = " + (index+start));
					}
				}
				index++;
			}
		}
		//Logd.log("mFilter_Indexs=",mFilter_Indexs);
		return result;
	}

	public void deleteAllRenRenContact() {
		mRenRenContacts.clear();
		mThirdContactsDAO.delete_AllContacts();
	}
	
	public void deleteAllCommonContact(){
		mSortContactsCommon.clear();
		mCommonDAO.delete_All();
	}

	public List<ContactModel> StrangerContacts() {
		return mContactsDAO.StrangerContacts();
		
	}

	public List<ContactModel> getFindContacts() {
		return mContacts_Find;
	}


}
