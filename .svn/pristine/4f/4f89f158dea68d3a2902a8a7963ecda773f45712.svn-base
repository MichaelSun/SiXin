package com.common.manager;

import com.common.contactscontract.ContactsContractMethods;
import com.common.contactscontract.ContactsContractModel;
import com.common.mcs.INetReponseAdapter;
import com.common.mcs.INetRequest;
import com.common.mcs.INetResponse;
import com.common.mcs.McsServiceProvider;
import com.core.json.JsonArray;
import com.core.json.JsonObject;
import com.core.util.CommonUtil;

/**
 * 通讯录匹配管理
 * 
 */
public class ContactManager {

	private static ContactManager sInstance = new ContactManager();
	
	private ContactManager(){
	}
	
	public static ContactManager getInstance() {
		return sInstance;
	}
	
	/**
	 * 获取本地通讯录，
	 * @return Contact[] 封装的联系人数组
	 * */
	public ContactsContractModel[] getLocalContacts () {
		return ContactsContractMethods.getInstance().loadAllContacts();
	}
	
	/**
	 * 获取推荐好友
	 * */
	public void matchContacts (final boolean isSkip, final INetResponse response) {
		new Thread(new Runnable(){
			@Override
			public void run() {
				ContactsContractModel[] contacts = null;
				if(!isSkip) {
					contacts = ContactsContractMethods.getInstance().loadAllContacts();
					CommonUtil.log("sunnyykn", "通讯录:" + contacts.toString());
					if (contacts == null || contacts.length == 0) {
						McsServiceProvider.getProvider().matchContacts(false, contacts, response);
						return;
					}
				}
				McsServiceProvider.getProvider().matchContacts(isSkip, contacts, response);
			}
		}).start();
	}
	
	public static class ContactResponse extends INetReponseAdapter {
		ContactListener mListener = null;
		public ContactResponse (ContactListener listener){
			mListener = listener;
		}
		
		@Override
		public void onSuccess(INetRequest req, JsonObject data) {
			CommonUtil.log("sunnyykn", "通讯录上传成功:" + data.toString());
			JsonArray array = data.getJsonArray("friend_list");
			if (array != null) {
				if (mListener != null) {
					mListener.onReceiveFull(array);
				}
			} else {
				if (mListener != null) 
					mListener.onReceiveEmpty();
			}
		}
		
		@Override
		public void onError(INetRequest req, JsonObject data) {
			CommonUtil.log("sunnyykn", "通讯录上传失败:" + data.toString());
			int error_code = (int) data.getNum("error_code");
			String error_msg = data.getString("error_msg");
			
			if (mListener != null) {
				mListener.onReceiveError(data);
			}
			if(error_code == 0){
				return ;
			}
			switch (error_code) {
			case -99:
				CommonUtil.toast("请栓查您的网络，稍后进行重试");
				break;
			default:
				CommonUtil.toast(error_msg);
			}
		}
	}
	
	public static interface ContactListener {
		public void onReceiveFull(JsonArray friends);
		public void onReceiveEmpty();
		public void onReceiveError(JsonObject data);
	}

}
