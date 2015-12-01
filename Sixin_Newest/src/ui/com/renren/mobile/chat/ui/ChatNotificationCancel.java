package com.renren.mobile.chat.ui;

import android.app.Activity;
import android.os.Bundle;

import com.common.statistics.BackgroundUtils;
import com.common.statistics.Htf;
import com.renren.mobile.chat.actions.models.RoomInfoModelWarpper;
import com.renren.mobile.chat.activity.RenRenChatActivity;
import com.renren.mobile.chat.base.model.ChatBaseItem.MESSAGE_ISGROUP;
import com.renren.mobile.chat.common.AwokeUtil;
import com.renren.mobile.chat.model.warpper.ChatMessageWarpper;
import com.renren.mobile.chat.model.warpper.ChatMessageWarpper_Image;
import com.renren.mobile.chat.ui.contact.C_ContactsData;
import com.renren.mobile.chat.ui.contact.ContactBaseModel;
import com.renren.mobile.chat.ui.contact.ContactModel;
import com.renren.mobile.chat.ui.contact.ContactModelFactory;
import com.renren.mobile.chat.ui.contact.RoomInfosData;
import com.renren.mobile.chat.ui.notification.MessageNotificationManager;
import com.renren.mobile.chat.ui.notification.MessageNotificationModel;


public class ChatNotificationCancel extends Activity {

	public MessageNotificationModel messageNotificationModel;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		if (!MessageNotificationManager.getInstance().getMessageNotificationModel().isMutil() && MessageNotificationManager.getInstance().getMessageNotificationModel().getCount()>0) {
			messageNotificationModel = MessageNotificationManager.getInstance().getMessageNotificationModel();
			//统计唤醒次数与类型
			/***************************************************************************/
			if(messageNotificationModel.getUnReadMessageList().size() > 1)
				BackgroundUtils.getInstance().dealAppRunState(Htf.MULTI_MESSAGE, false);
			else{
				ChatMessageWarpper message = messageNotificationModel.getUnReadMessageList().get(0);
				if(message instanceof ChatMessageWarpper_Image && ((ChatMessageWarpper_Image)message).isBrush()){
					BackgroundUtils.getInstance().dealAppRunState(Htf.BRUSH_MESSAGE, false);
				}else{
					int type = messageNotificationModel.getUnReadMessageList().get(0).mMessageType;
					String awokeWay = AwokeUtil.getAwokeWay(type);
					BackgroundUtils.getInstance().dealAppRunState(awokeWay, false);
				}
			}
			/***************************************************************************/
			
			
			int isGroupMessage = messageNotificationModel.getIsGroupMessage(1);
			if(isGroupMessage == MESSAGE_ISGROUP.IS_SINGLE){
//				ContactModel contactModel = new ContactModel();
				ContactBaseModel contactBaseModel = C_ContactsData.getContact(messageNotificationModel.getMessageUserId(1), messageNotificationModel.getMessageDomain(1));
//				contactModel.mUserId = messageNotificationModel.getMessageUserId(1);
//				contactModel.mContactName = messageNotificationModel.getMessageUserName(1);
//				contactModel.mRelation = ContactBaseModel.Relationship.CONTACT;
//				contactModel.mHeadUrl = messageNotificationModel.getHeadUrl(1);
//				contactModel = C_ContactsData.getInstance().getSiXinContact(contactModel.mUserId,null);
				if(contactBaseModel == null){
					contactBaseModel = ContactModelFactory.createContactModel(messageNotificationModel.getMessageDomain(1)); 
					contactBaseModel.mUserId = messageNotificationModel.getMessageUserId(1);
					contactBaseModel.mContactName = messageNotificationModel.getMessageUserName(1);
					contactBaseModel.name = messageNotificationModel.getMessageUserName(1);
				}
				contactBaseModel.mDomain = messageNotificationModel.getMessageDomain(1);
				
				RenRenChatActivity.show(this, contactBaseModel);
			}else {
				long groupId = messageNotificationModel.getGroupId(1);
				RoomInfoModelWarpper infoModelWarpper = RoomInfosData.getInstance().getRoomInfo(groupId);
				if(infoModelWarpper == null){
					RoomInfosData.getInstance().loadRoomInfoListFromDB();
					infoModelWarpper = RoomInfosData.getInstance().getRoomInfo(groupId);
					if(infoModelWarpper == null){
						infoModelWarpper = RoomInfosData.getInstance().createUnknowRoom(groupId);
					}
				}
				RenRenChatActivity.show(this, infoModelWarpper);
			}
			
			
			finish();
			
			
		} else {
			finish();
			// TODO跳转到会话页面
			MainFragmentActivity.show(this,MainFragmentActivity.Tab.SIXIN);
			
			//统计唤醒次数与类型
			/***************************************************************************/
			BackgroundUtils.getInstance().dealAppRunState(Htf.MULTI_MESSAGE, false);
			/***************************************************************************/
		}
	}
	
	@Override
	protected void onStart() {
		super.onStart();
	}
	
	protected void onStop() {
		super.onStop();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
}
