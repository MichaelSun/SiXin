package com.renren.mobile.chat.third;

import android.content.Context;
import android.content.Intent;

import com.renren.mobile.chat.RenrenChatApplication;
import com.renren.mobile.chat.base.util.SystemUtil;
import com.renren.mobile.chat.ui.contact.C_ContactsData;
import com.renren.mobile.chat.ui.contact.ContactBaseModel;
import com.renren.mobile.chat.ui.contact.ContactModel;
import com.sixin.proxy.UserInfo;

public class ThirdAction_Chat extends ThirdAction {

	/*@Override
	protected void processAction(Context context, Intent intent,ThirdActionsCenter center) {
		UserInfo userinfo = new UserInfo(intent);
		ContactModel contact_user = new ContactModel();
		contact_user.mUserId = userinfo.mUserId;
		contact_user.mContactName = userinfo.mUserName;
		contact_user.mHeadUrl = userinfo.mHeadUrl;
		//contact_user.mOnlinestatus = -1;
		RenrenChatApplication.sToChatContactInChat = contact_user;
	}*/
	
	// kaining.yang
	@Override
	protected void processAction(Context context, Intent intent, ThirdActionsCenter center) {
		UserInfo userinfo = new UserInfo(intent);
		
		SystemUtil.logykn("userinfo.mUserId", String.valueOf(userinfo.mUserId));
		SystemUtil.logykn("userinfo.mUserName", userinfo.mUserName);
		
		ContactBaseModel contact_user = new ContactModel();
		contact_user = C_ContactsData.getInstance().getContactByRenRenID(userinfo.mUserId, null);
		
		if (null != contact_user) {
			SystemUtil.logykn("contact_user.mUserId", String.valueOf(contact_user.mUserId));
			SystemUtil.logykn("contact_user.mContactName", contact_user.mContactName);
		}
		
		RenrenChatApplication.sToChatContactInChat = contact_user;
		mIsChat = true;
	}
}
