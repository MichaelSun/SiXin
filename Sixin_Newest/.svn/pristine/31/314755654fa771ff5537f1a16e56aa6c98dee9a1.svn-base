package com.renren.mobile.chat.ui.contact;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import android.util.Log;

import com.common.manager.LoginManager;
import com.common.network.AbstractNotSynRequest;
import com.common.network.AbstractNotSynRequest.OnDataCallback;
import com.common.network.NULL;
import com.renren.mobile.chat.R;
import com.renren.mobile.chat.RenrenChatApplication;
import com.renren.mobile.chat.actions.models.CheckRoomInfoModel;
import com.renren.mobile.chat.actions.models.NotifyDeleteModel;
import com.renren.mobile.chat.actions.models.NotifyInviteModel;
import com.renren.mobile.chat.actions.models.RoomBaseInfoModel;
import com.renren.mobile.chat.actions.models.RoomBaseInfoModel.ROOM_ENABLE;
import com.renren.mobile.chat.actions.models.RoomInfoModelWarpper;
import com.renren.mobile.chat.actions.presence.Action_Presence;
import com.renren.mobile.chat.actions.requests.RequestConstructorProxy;
import com.renren.mobile.chat.base.util.SystemUtil;
import com.renren.mobile.chat.dao.ContactsDAO;
import com.renren.mobile.chat.dao.DAOFactoryImpl;
import com.renren.mobile.chat.dao.RoomDAO;
import com.renren.mobile.chat.util.ChatMessageSender;
/**
 * @author tian.wang
 * @说明 
 * */
public class RoomInfosData {
	private static RoomInfosData mRoomInfosData = null;
	public ArrayList<RoomInfoModelWarpper> mRoomsList = new ArrayList<RoomInfoModelWarpper>();
	private ArrayList<RoomInfosDataObserver> mRoomInfosDataObserverList= new ArrayList<RoomInfosDataObserver>();
	private RoomDAO mRoomDao = DAOFactoryImpl.getInstance().buildDAO(RoomDAO.class);
	ContactsDAO mContactsDAO;
	private RoomInfosData(){
		mContactsDAO = DAOFactoryImpl.getInstance().buildDAO(ContactsDAO.class);
	}
	public static synchronized RoomInfosData getInstance(){
		if(mRoomInfosData == null){
			mRoomInfosData = new RoomInfosData();
			mRoomInfosData.loadRoomInfoListFromDB();
		}
		return mRoomInfosData;
	}
	public synchronized void registorObserver(RoomInfosDataObserver observer) {
		if(observer != null){
			mRoomInfosDataObserverList.add(observer);
		}
	}
	public synchronized void unRegistorObserver(RoomInfosDataObserver observer) {
		if(observer!=null){
			mRoomInfosDataObserverList.remove(observer);
		}
	}
	public synchronized void recycleAllObserver(){}
	public synchronized void notifyObserver(byte state,long roomid) {
		for (RoomInfosDataObserver observer : mRoomInfosDataObserverList) {
			if (observer != null) {
				observer.notifyRoomInfoDataUpdate(state,roomid);
			}
		}
	}
	/**
	 * 注意同步问题，此处的同步不能解决根本问题
	 * **/
	public synchronized ArrayList<RoomInfoModelWarpper> getRoomsList(){
		return mRoomsList;
	}
	/**
	 * 加载本地DB存储的群信息
	 * **/
	public synchronized void loadRoomInfoListFromDB(){
		ArrayList<RoomInfoModelWarpper> roomList = mRoomDao.query_RoomInfo_List();
		this.mRoomsList.clear();
		for (RoomInfoModelWarpper roomInfoModelWarpper : roomList) {
			this.mRoomsList.add(roomInfoModelWarpper);
		}
	}
	/**
	 * 在数据库中新增加一个群组信息
	 * **/
	public synchronized void addRoomInfoToDB(RoomInfoModelWarpper roomInfoModel){
		roomInfoModel.updateToDB();
	}
	/**
	 * 新增加一个已经创建的群组信息
	 * **/
	public synchronized void addRoomInfo(RoomInfoModelWarpper roomInfoModel){
		RoomInfoModelWarpper roomInfo = getRoomInfo(roomInfoModel.mRoomId);
		if(roomInfo!= null){
			roomInfoModel.mRoomIsContact = roomInfo.mRoomIsContact;
			mRoomsList.remove(roomInfo);
			mRoomsList.add(roomInfoModel);
			roomInfoModel.updateToDB();
			notifyObserver(RoomInfosDataObserver.DATA_STATE_OK,roomInfo.mRoomId);
			return ;
		}
		ArrayList<ContactModel> strangerContactList = new ArrayList<ContactModel>();
		for (ContactModel contactModel : roomInfoModel.mMembers) {
			if(contactModel.mUserId!= LoginManager.getInstance().getLoginInfo().mUserId &&C_ContactsData.getInstance().getSiXinContact(contactModel.mUserId,null) == null){
				contactModel.mRelation = ContactModel.Relationship.STRANGER;
				strangerContactList.add(contactModel);
			}else{
				contactModel.mRelation = ContactModel.Relationship.STRANGER;
			}
		}
		if(strangerContactList.size()>0){
			mContactsDAO.insert_Contacts(strangerContactList);
		}
		mRoomsList.add(roomInfoModel);
		addRoomInfoToDB(roomInfoModel);
		notifyObserver(RoomInfosDataObserver.DATA_STATE_OK,roomInfoModel.mRoomId);
	}
	/**
	 * 被通知群组内邀请某人加入群，老成员更新群信息
	 * **/
	public synchronized void updateRoomInfoByNotifyInvite(long roomId,NotifyInviteModel notifyInviteModel){
		RoomInfoModelWarpper roomInfo = getRoomInfo(roomId);
		if(roomInfo == null){
			getRoomInfoFromNet(roomId);
		}else if(roomInfo.mVersion != notifyInviteModel.mVersion - 1){
		}else{
			roomInfo.mVersion = notifyInviteModel.mVersion;
			roomInfo.mSubject = notifyInviteModel.mSubject;
			ArrayList<ContactModel> strangerContactList = new ArrayList<ContactModel>();
			for (ContactModel contactModel : notifyInviteModel.mMembers) {
				if(!roomInfo.isMember(contactModel.mUserId)){
					roomInfo.addMember(contactModel);
				}
				if(contactModel.mUserId!= LoginManager.getInstance().getLoginInfo().mUserId &&C_ContactsData.getInstance().getSiXinContact(contactModel.mUserId,null) == null){
					contactModel.mRelation = ContactModel.Relationship.STRANGER;
					strangerContactList.add(contactModel);
				}else{
					contactModel.mRelation = ContactModel.Relationship.CONTACT;
				}
			}
			if(strangerContactList.size()>0){
				mContactsDAO.insert_Contacts(strangerContactList);
			}
			roomInfo.updateToDB();
			notifyObserver(RoomInfosDataObserver.DATA_STATE_OK,roomId);
		}
	}
	/**
	 * 被通知自己被删除
	 * **/
	public synchronized void updateRoomInfoByNotifyDeleted(long roomId){
		RoomInfoModelWarpper roomInfo = getRoomInfo(roomId);
		if(roomInfo!=null){
			C_ContactsData.getInstance().deleteGroupContact(roomId);
			roomInfo.leaveRoom();
			roomInfo.updateToDB();
			notifyObserver(RoomInfosDataObserver.DATA_STATE_OK,roomId);
		}
	}
	/**
	 * 被通知别人被删除
	 * **/
	public synchronized void updateRoomInfoByNotifyDeleteMember(long roomId, NotifyDeleteModel notifyDeleteModel){
		RoomInfoModelWarpper roomInfo = getRoomInfo(roomId);
		if(roomInfo == null){
			getRoomInfoFromNet(roomId);
		}else if(roomInfo.mVersion != notifyDeleteModel.mVersion - 1){
		}else{
			for (Long id : notifyDeleteModel.mIdSet) {
				roomInfo.deleteMember(id);
			};
			roomInfo.mVersion = notifyDeleteModel.mVersion;
			roomInfo.mSubject = notifyDeleteModel.mSubject;
			roomInfo.generateIds();
			roomInfo.updateToDB();
			notifyObserver(RoomInfosDataObserver.DATA_STATE_OK,roomId);
		}
	}
	/**
	 * 被通知某人离开某个群
	 * **/
	public synchronized void updateRoomInfoByNotifyMemberLeave(long roomId,String subject,int version,long userId){
		RoomInfoModelWarpper roomInfo = getRoomInfo(roomId);
		if(roomInfo == null){
			getRoomInfoFromNet(roomId);
		}else if(roomInfo.mVersion != version - 1){
		}else{
			roomInfo.mVersion = version;
			roomInfo.mSubject = subject;
			roomInfo.deleteMember(userId);
			roomInfo.updateToDB();
			notifyObserver(RoomInfosDataObserver.DATA_STATE_OK,roomId);
		}
	}
	/**
	 * 被通知某个群被销毁
	 * **/
	public synchronized void updateRoomInfoByNotifyRoomDestory(long roomId){
		RoomInfoModelWarpper roomInfo = getRoomInfo(roomId);
		if(roomInfo!=null){
			C_ContactsData.getInstance().deleteGroupContact(roomId);
			roomInfo.dismissRoom();
			roomInfo.updateToDB();
			notifyObserver(RoomInfosDataObserver.DATA_STATE_OK,roomId);
		}
	}
	/**
	 * 新增加一个本地创建的群组
	 * **/
	public synchronized void addRoomInfo(long roomId,List<Long> idList,int version,String subject){
		RoomInfoModelWarpper roomInfoModel = new RoomInfoModelWarpper();
		StringBuilder sb = new StringBuilder();
		roomInfoModel.mRoomId = roomId;
		roomInfoModel.mSubject = subject;
		roomInfoModel.mVersion = version;
		roomInfoModel.mOwner = LoginManager.getInstance().getLoginInfo().mUserId;
		roomInfoModel.mLocalUserId = LoginManager.getInstance().getLoginInfo().mUserId;
		ContactModel contactModel;
		for (Long id : idList) {
			if(id.longValue()!=LoginManager.getInstance().getLoginInfo().mUserId){
				contactModel = C_ContactsData.getInstance().getSiXinContact(id,null);
				if(contactModel != null){
					roomInfoModel.mMembers.add(contactModel);
					sb.append(id).append(RoomBaseInfoModel.SPLIT);
				}
			}
		}
		roomInfoModel.generateIds();
		mRoomsList.add(roomInfoModel);
		notifyObserver(RoomInfosDataObserver.DATA_STATE_OK,roomId);
		addRoomInfoToDB(roomInfoModel);
	}
	/**
	 * 在数据库中新删除一个群组信息
	 * **/
	public synchronized void  deleteRoomInfoToDB(RoomInfoModelWarpper roomInfoModel){
		mRoomDao.delete_RoomInfo(LoginManager.getInstance().getLoginInfo().mUserId, roomInfoModel.mRoomId);
	}
	public synchronized void updateRoomSubject(long roomId,String subject){
		RoomInfoModelWarpper roomInfo = getRoomInfo(roomId);
		if(roomInfo != null){
			roomInfo.updateSbject(subject);
			roomInfo.mVersion = roomInfo.mVersion+1;
			roomInfo.updateToDB();
			notifyObserver(RoomInfosDataObserver.DATA_STATE_OK,roomId);
		}
	}
	/**
	 * 更新一个群组
	 * **/
	private synchronized void  updateRoomInfo(RoomInfoModelWarpper roomInfoModel){
		Log.v("gaga", "updateRoomInfo");
		RoomInfoModelWarpper roomInfo = getRoomInfo(roomInfoModel.mRoomId);
		if(roomInfo.mVersion == roomInfoModel.mVersion){
			return ;
		}
		if(roomInfo !=null ){
			roomInfoModel.mRoomIsContact = roomInfo.mRoomIsContact;
			mRoomsList.remove(roomInfo);
			mRoomsList.add(roomInfoModel);
		}
		notifyObserver(RoomInfosDataObserver.DATA_STATE_OK,roomInfoModel.mRoomId);
		roomInfoModel.updateToDB();
	}
	/**
	 * 通过群组id获取群组信息
	 * **/
	public synchronized RoomInfoModelWarpper getRoomInfo(long roomId){
		for(RoomInfoModelWarpper roomInfo : mRoomsList){
			if(roomInfo.mRoomId == roomId){
				return roomInfo;
			}
		}
		return null;
	}
	/**
	 * 将群加入到联系人列表中
	 * **/
	public synchronized void saveRoomToContact(long roomId){
		RoomInfoModelWarpper roomInfo = getRoomInfo(roomId);
		if(roomInfo != null){
			roomInfo.saveToContact();
			roomInfo.updateToDB();
			C_ContactsData.getInstance().addGroupContact(roomId);
		}
	}
	/**
	 * 将联系人从群中删除
	 * **/
	private synchronized void deleteRoomFromContactOnLocal(long roomId){
		RoomInfoModelWarpper roomInfo = getRoomInfo(roomId);
		if(roomInfo != null){
			roomInfo.deleteToContact();
			roomInfo.updateToDB();
			C_ContactsData.getInstance().deleteGroupContact(roomId);
		}
	}
    public void deleteRoomFromContactOnNet(final long roomId){
    	AbstractNotSynRequest<NULL> abstractNotSynRequest = RequestConstructorProxy.getInstance().deleteRoomInfoFromContact(LoginManager.getInstance().getLoginInfo().mUserId, roomId);
    	abstractNotSynRequest.setCallback(new OnDataCallback<NULL>() {
			@Override
			public void onSuccess() {
				RenrenChatApplication.mHandler.post(new Runnable() {
					@Override
					public void run() {
						SystemUtil.toast(RenrenChatApplication.getmContext().getResources().getString(R.string.RoomInfosData_java_1));		//RoomInfosData_java_1=删除联系人成功; 
						RoomInfosData.getInstance().deleteRoomFromContactOnLocal(roomId);
					}
				});
			}
			@Override
			public void onSuccessRecive(NULL data) {
			}
			@Override
			public void onError(int errorCode,final String errorMsg) {
				RenrenChatApplication.mHandler.post(new Runnable() {
					@Override
					public void run() {
						SystemUtil.toast(errorMsg);
					}
				});
			}
		});
    	ChatMessageSender.getInstance().sendRequestToNet(abstractNotSynRequest);
    }
	/**
	 * 解散群,如果此群存在联系人列表中，同时要从联系人列表中删除
	 * **/
	public synchronized void deleteRoom(long roomId){
		RoomInfoModelWarpper roomInfo = getRoomInfo(roomId);
		if(roomInfo != null){
			roomInfo.dismissRoom();
			int size = mRoomsList.size();
			for(int i=0;i<size;i++){
				if(mRoomsList.get(i).mRoomId == roomInfo.mRoomId){
					mRoomsList.remove(i);
					break;
				}
			}
			mRoomDao.delete_RoomInfo(LoginManager.getInstance().getLoginInfo().mUserId, roomInfo.mRoomId);
			C_ContactsData.getInstance().deleteGroupContact(roomId);
		}
	}
	/**
	 * 通过presence来更新群信息
	 * **/
	public synchronized void updateRoomInfoFromPrecense(Action_Presence presence){
		/**
		 * 1 获取presence的群id以及版本号version
		 * 
		 * 2 判断此id的群组信息是否存在？如果存在则进入步骤3，如果不存在，进入步骤4
		 * 
		 * 3 判断此本地的群组信息与presence中的群组信息版本是否相差为1，如果是，则直接使用presence对群组信息进行更新，并提升相应的版本号信息，如果不是，则进入步骤4
		 * 
		 * 4从网络上获取此群组的详细信息，进行更新
		 * **/
	}
	/**
	 * 通过Message来更新群信息
	 * **/
	public synchronized void updateRoomInfoFromMessage(long roomId,int version){
		RoomInfoModelWarpper roomInfo = getRoomInfo(roomId);
		if(roomInfo !=null && version == 0){
			roomInfo.dismissRoom();
			roomInfo.updateToDB();
		}
		if(roomInfo != null&&roomInfo.mVersion == version){
		}else{
			getRoomInfoFromNet(roomId);
		}		
	}
	private HashSet<Long> mRoomInfoRequestSet = new HashSet<Long>();
	/**
	 * 从网络中更新群组信息
	 * **/
	public synchronized void getRoomInfoFromNet(final long roomId){
		Log.v("gaga", "11111111111");
		if(mRoomInfoRequestSet.contains(roomId)){
			Log.v("gaga", "2222222222222");
			return;
		}else{
			Log.v("gaga", "333333333333333");
			mRoomInfoRequestSet.add(roomId);
			AbstractNotSynRequest<RoomInfoModelWarpper> xx = RequestConstructorProxy.getInstance().queryRoomInfo(LoginManager.getInstance().getLoginInfo().mUserId, roomId);
			xx.setCallback(new OnDataCallback<RoomInfoModelWarpper>(){
				@Override
				public void onSuccess() {
					mRoomInfoRequestSet.remove(roomId);
				}
				@Override
				public void onSuccessRecive(RoomInfoModelWarpper roomInfoModel) {
					Log.v("gaga", "onSuccessRecive");
					if(getRoomInfo(roomInfoModel.mRoomId) != null){
						updateRoomInfo(roomInfoModel);
					}else{
						addRoomInfo(roomInfoModel);
					}
					mRoomInfoRequestSet.remove(roomId);
				}
				@Override
				public void onError(int errorCode,String errorMsg) {
					Log.v("gaga", "onError");
					if(errorCode == 610 || errorCode ==100){
						RoomInfoModelWarpper roomInfo = getRoomInfo(roomId);
						if(roomInfo != null){
							roomInfo.mDisable = ROOM_ENABLE.DISABLE.Value;
							roomInfo.updateToDB();
						}else{
							roomInfo = new RoomInfoModelWarpper();
							roomInfo.mSubject = RenrenChatApplication.getmContext().getResources().getString(R.string.GroupContactModel_java_2);		//GroupContactModel_java_2=群聊; 
							roomInfo.mDisable = ROOM_ENABLE.DISABLE.Value;
							roomInfo.mRoomId = roomId;
							roomInfo.mLocalUserId = LoginManager.getInstance().getLoginInfo().mUserId;
							addRoomInfo(roomInfo);
						}
					}
					mRoomInfoRequestSet.remove(roomId);
				}});
			ChatMessageSender.getInstance().sendRequestToNet(xx);
		}
	}
	public RoomInfoModelWarpper createUnknowRoom(long roomId){
		RoomInfoModelWarpper roomInfo = new RoomInfoModelWarpper();
		roomInfo.mSubject = RenrenChatApplication.getmContext().getResources().getString(R.string.GroupContactModel_java_2);		//GroupContactModel_java_2=群聊; 
		roomInfo.mDisable = ROOM_ENABLE.DISABLE.Value;
		roomInfo.mRoomId = roomId;
		roomInfo.mLocalUserId = LoginManager.getInstance().getLoginInfo().mUserId;
		return roomInfo;
	}
	/**
	 * 判断是否需要更新此群组信息
	 * **/
	public synchronized void judgeRoomInfoIsNeedToUpdate(long roomId){
	}
	/**
	 * 判断是否需要更新此群组信息
	 * **/
	public synchronized void checkRoomInfo(long roomId,int version){
		List<Long> checkIds = new ArrayList<Long>();
		List<String> versions = new ArrayList<String>();
		checkIds.add(roomId);
		versions.add(String.valueOf(version));
		AbstractNotSynRequest<CheckRoomInfoModel> xx = RequestConstructorProxy.getInstance().uploadTimestamp(LoginManager.getInstance().getLoginInfo().mUserId, checkIds, versions);
		xx.setCallback(new OnDataCallback<CheckRoomInfoModel>(){
			@Override
			public void onSuccess() {
			}
			@Override
			public void onSuccessRecive(CheckRoomInfoModel roomInfoModel) {
				if(roomInfoModel.getFristIsmember()&&roomInfoModel.getFristUpdate()){
					getRoomInfoFromNet(roomInfoModel.getFristId());
				}
			}
			@Override
			public void onError(int errorCode,String errorMsg) {
			}});
		RoomInfoModelWarpper roomInfo = getRoomInfo(roomId);
		if(roomInfo!=null && roomInfo.mDisable != ROOM_ENABLE.DISABLE.Value){
			ChatMessageSender.getInstance().sendRequestToNet(xx);	
		}
	}
	/**
	 * 继承次接口，并且将该实现类注册当前类中，可实现对联系人数据变更的监听
	 * **/
	public static interface RoomInfosDataObserver {
		/** 数据正常更新 */
		public static byte DATA_STATE_OK = 1;
		/** 数据更新中出现异常*/
		public static byte DATA_STATE_ERROR = 0;
		public void notifyRoomInfoDataUpdate(byte state,long roomId);
	}
	public void clear(){
		mRoomsList.clear();
	}
	public void delete_all_rooms(){
		mRoomDao.delete_all_rooms();
		clear();
		notifyObserver(RoomInfosDataObserver.DATA_STATE_OK,-1);
	}
}
