package com.renren.mobile.chat.ui.contact;
import android.content.Intent;
import android.text.TextUtils;

import com.common.manager.LoginManager;
import com.common.manager.LoginManager.BindInfoObserver;
import com.common.manager.LoginManager.LoginInfo;
import com.renren.mobile.chat.R;
import com.renren.mobile.chat.RenrenChatApplication;
import com.renren.mobile.chat.base.util.SystemUtil;
import com.renren.mobile.chat.base.views.NotSynImageView;
import com.renren.mobile.chat.ui.MainFragmentActivity;
import com.renren.mobile.chat.ui.account.BindRenrenAccountScreen;
import com.renren.mobile.chat.ui.contact.C_ContactsData.NewContactsDataObserver;
public final class SpecialContactModel extends ContactModel implements BindInfoObserver{
	
	private static final long serialVersionUID = 9037844524341707396L;
	private int unReadCount = 0;
	private String namesString ;
	public SpecialContactModel(byte type){
		super(type);
		mAlephString = null;
		switch (type) {
		case Contact_group_type.SPECIAL_RECOMMEND: //推荐
			String message=RenrenChatApplication.getmContext().getResources().getString(R.string.ContactModel_java_14);
			setmContactName(message);
			ContactMessageData.getInstance().loadDada();
			unReadCount=ContactMessageData.getInstance().getUnReadCount();
			unReadCountChanged(unReadCount);
			if(unReadCount>0){
				namesString = ContactMessageData.getInstance().getUnReadNames();
			}
//			if(SystemUtil.mDebug){
//				unReadCount = 10;
//				namesString = "jkdfoa,到家了,放假啦";
//			}
//			initRecommend();
			this.setmSmallHeadUrl(NotSynImageView.RECOMMEND_CONTACT);
			break;
		case Contact_group_type.SPECIAL_RENREN: //绑定第三方（人人）
			BindRenrenAccountScreen.registerObserver(this);
			init();
			this.setmSmallHeadUrl(NotSynImageView.RENREN_CONTACT);
			
			break;
		default:
			break;
		}
	}
	
//	private void initRecommend() {
//		
//	}

	public boolean isBindedRenRen(){
		LoginInfo loginInfo=LoginManager.getInstance().getLoginInfo();
		if(loginInfo!=null&&loginInfo.mBindInfoRenren!=null){
		//	loginInfo.mBindInfoRenren.mBindId
	        if(SystemUtil.mDebug){
	        	SystemUtil.logd("id="+loginInfo.mBindInfoRenren.mBindId+"#name="+loginInfo.mBindInfoRenren.mBindName);
			}
			return !TextUtils.isEmpty(loginInfo.mBindInfoRenren.mBindId);
		}
		//return !TextUtils.isEmpty(loginInfo.mBindId);
		return false;
	}
	
	public boolean isBindedPhone(){
		LoginInfo loginInfo=LoginManager.getInstance().getLoginInfo();
		if(loginInfo!=null&&loginInfo.mBindInfoMobile!=null){
		//	loginInfo.mBindInfoRenren.mBindId
	        if(SystemUtil.mDebug){
	        	SystemUtil.logd("id="+loginInfo.mBindInfoMobile.mBindId+"#name="+loginInfo.mBindInfoRenren.mBindName);
			}
			return !TextUtils.isEmpty(loginInfo.mBindInfoMobile.mBindId);
		}
		//return !TextUtils.isEmpty(loginInfo.mBindId);
		return false;
	}
	
	public String getBindedPhone(){
		LoginInfo loginInfo=LoginManager.getInstance().getLoginInfo();
		if(loginInfo!=null&&loginInfo.mBindInfoMobile!=null){
		//	loginInfo.mBindInfoRenren.mBindId
	        if(SystemUtil.mDebug){
	        	SystemUtil.logd("id="+loginInfo.mBindInfoMobile.mBindId+"#name="+loginInfo.mBindInfoRenren.mBindName);
			}
			if(!TextUtils.isEmpty(loginInfo.mBindInfoMobile.mBindId)){
				return loginInfo.mBindInfoMobile.mBindId;
				
			}
		}
		return "";
	}
	
//	public void unBindRenRen(){
//		
//	}
	
	public String getBindedRenRenAccount(){
		LoginInfo loginInfo=LoginManager.getInstance().getLoginInfo();
		if(loginInfo!=null&&loginInfo.mBindInfoRenren!=null){
		//	loginInfo.mBindInfoRenren.mBindId
	        if(SystemUtil.mDebug){
	        	SystemUtil.logd("id="+loginInfo.mBindInfoRenren.mBindId+"#name="+loginInfo.mBindInfoRenren.mBindName);
			}
			
			return loginInfo.mBindInfoRenren.mBindId;
		}
		return "";
	}
	
	public String getBindedRenRenName(){
		LoginInfo loginInfo=LoginManager.getInstance().getLoginInfo();
		if(loginInfo!=null&&loginInfo.mBindInfoRenren!=null){
		//	loginInfo.mBindInfoRenren.mBindId
	        if(SystemUtil.mDebug){
	        	SystemUtil.logd("id="+loginInfo.mBindInfoRenren.mBindId+"#name="+loginInfo.mBindInfoRenren.mBindName);
			}
			if(!TextUtils.isEmpty(loginInfo.mBindInfoRenren.mBindName)){
				return loginInfo.mBindInfoRenren.mBindName;
				
			}
		}
		return "";
	}
//	
//	public void unBindPhone(){
//		
//	}
	
//	public void bindPhone(){
//		if (isBindedPhone()) {
//			BindInfoActivity.show(RenrenChatApplication.getmContext(), getBindedPhone(), RegisterAccountActivity.FLAG_BIND_PHONE);
//		} else {
//			RegisterAccountActivity.show(RenrenChatApplication.getmContext(), RegisterAccountActivity.FLAG_BIND_PHONE);
//		}
//	}
//	
//	public void bindrenren(){
//		Intent i;
//		if(isBindedRenRen()){
//		   i = new Intent(RenrenChatApplication.getmContext(), ThirdContactsActivity.class);
//		}else{
//			i = new Intent(RenrenChatApplication.getmContext(), BindRenrenAccountActivity.class);
//			i.putExtra(BindRenrenAccountActivity.COME_FROM, BindRenrenAccountActivity.BIND_CONTANT);
//		}
//		RenrenChatApplication.getmContext().startActivity(i);
//	}

	public String getThirdType() {
//		String tmp = LoginManager.getInstance().getLoginInfo().mBindType;
//		SystemUtil.logd("type="+tmp);
//		return tmp;
		return "";
	}

	public String getThirdAccount() {
//		String tmp= LoginManager.getInstance().getLoginInfo().mBindId;
//		SystemUtil.logd("account="+tmp);
//		return tmp;
		return "";
	}
	
	private void init(){
		if(isBindedRenRen()){
			String bindRenRen=RenrenChatApplication.getmContext().getResources().getString(R.string.ContactModel_java_13);
			setmContactName(bindRenRen);
		}else{
			String unBindRenRen=RenrenChatApplication.getmContext().getResources().getString(R.string.ContactModel_java_12);
			setmContactName(unBindRenRen);
		}
	}

	@Override
	public void update() {
		init();
		C_ContactsData.getInstance().notifyNewObserver(NewContactsDataObserver.DATA_STATE_OK, NewContactsDataObserver.TYPE_SPECIAL);
	}

	public void clear() {
		BindRenrenAccountScreen.deleteObserver(this);
	}

	public int getUnReadedCount() {
		return unReadCount;
	}
	
	public String getUnReadedNames() {
		return namesString;
	}

	public void addUnReadCount() {
		unReadCount++;
		unReadCountChanged(unReadCount);
		namesString = ContactMessageData.getInstance().getUnReadNames();
		C_ContactsData.getInstance().notifyNewObserver(NewContactsDataObserver.DATA_STATE_OK, NewContactsDataObserver.TYPE_SPECIAL);
	}

	public void clearUnReadCount() {
		unReadCount=0;
		unReadCountChanged(0);
		namesString = "";
		C_ContactsData.getInstance().notifyNewObserver(NewContactsDataObserver.DATA_STATE_OK, NewContactsDataObserver.TYPE_SPECIAL);
	}
	
	private void unReadCountChanged(int count){
		if(SystemUtil.mDebug){
			SystemUtil.logd("推荐联系人数量发送变化  count="+count+"#"+RenrenChatApplication.isMainFragementActivity);
		}
		//if(RenrenChatApplication.isMainFragementActivity)
		{
			Intent intent = new Intent(MainFragmentActivity.REFRESH_RED_DOT_ACTION);
			intent.putExtra("unread_count", count);
			intent.putExtra("tab", MainFragmentActivity.Tab.CONTRACT);
			RenrenChatApplication.getAppContext().sendBroadcast(intent);
			if(SystemUtil.mDebug){
				SystemUtil.logd("联系人推荐发送请求count="+count);
			}
		}
	}

//	@Override
//	public void onBindRenren() {
//		update();
//	}
	
//	private void update(){
//		if(isBindedRenRen()){
//			String bindRenRen=RenrenChatApplication.getmContext().getResources().getString(R.string.ContactModel_java_13);
//			setmContactName(bindRenRen);
//		}else{
//			String unBindRenRen=RenrenChatApplication.getmContext().getResources().getString(R.string.ContactModel_java_12);
//			setmContactName(unBindRenRen);
//		}
//	}
	
}
