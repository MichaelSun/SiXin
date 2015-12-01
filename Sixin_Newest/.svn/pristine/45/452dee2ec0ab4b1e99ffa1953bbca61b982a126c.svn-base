package com.renren.mobile.chat.actions.message;

import java.util.ArrayList;
import java.util.List;

import com.common.actions.ActionNotMatchException;
import com.common.manager.LoginManager;
import com.common.network.DomainUrl;
import com.common.network.NetRequestListener;
import com.common.statistics.BackgroundUtils;
import com.common.utils.Bip;
import com.common.utils.RRSharedPreferences;
import com.data.xmpp.Message;
import com.renren.mobile.chat.RenrenChatApplication;
import com.renren.mobile.chat.actions.ActionDispatcher;
import com.renren.mobile.chat.activity.RenRenChatActivity;
import com.renren.mobile.chat.base.GlobalValue;
import com.renren.mobile.chat.base.model.ChatBaseItem;
import com.renren.mobile.chat.base.util.SystemUtil;
import com.renren.mobile.chat.model.warpper.ChatMessageFactory;
import com.renren.mobile.chat.model.warpper.ChatMessageWarpper;
import com.renren.mobile.chat.newsfeed.NewsFeedFactory;
import com.renren.mobile.chat.newsfeed.NewsFeedWarpper;
import com.renren.mobile.chat.ui.MainFragmentActivity;
import com.renren.mobile.chat.ui.contact.C_ContactsData;
import com.renren.mobile.chat.ui.contact.ContactModel;
import com.renren.mobile.chat.ui.contact.RoomInfosData;
import com.renren.mobile.chat.ui.setting.SettingDataManager;
import com.renren.mobile.chat.util.CurrentChatSetting;

/**
 * @author dingwei.chen1988@gmail.com
 * @说明 群消息推送
 * */
public class PushGroupMessage extends Action_Message {

	List<ChatMessageWarpper> messages = null;
	RRSharedPreferences rrsp = new RRSharedPreferences(RenrenChatApplication.getAppContext());
	
	@Override
	public void processAction(Message node, long id) {
		
		ChatMessageWarpper message = ChatMessageFactory.getInstance().obtainMessage(node.mBody.getType());
		this.basicParser(message, node);
		message.swapDataFromXML(node);
		message.mIsGroupMessage = ChatBaseItem.MESSAGE_ISGROUP.IS_GROUP;
		if(node.isContainFeed()){//解析新鲜事
			NewsFeedWarpper item = NewsFeedFactory.getInstance().obtainNewsFeedModel(node);
			message.setNewsFeedModel(item);
		}
		int roomVersion = node.mBody.getVersion();
		long roomId = message.mGroupId;
		
		SettingDataManager.getInstance().obtainSwitchState();
		boolean whetherRemind = true;
		if (SettingDataManager.getInstance().mSoundState 
		&& whetherRemind 
		&&(mIsRunForeground || SettingDataManager.getInstance().mPushState)) {
			if (RenRenChatActivity.class.isInstance(GlobalValue.getCurrentActivity()) 
			 && ((RenRenChatActivity)GlobalValue.getCurrentActivity()).mToChatUser.getUId() != roomId){
				Bip.bipIncomingPush();
			}else if(MainFragmentActivity.class.isInstance(GlobalValue.getCurrentActivity()) 
					&& ((MainFragmentActivity)GlobalValue.getCurrentActivity()).mPager.getCurrentItem() == MainFragmentActivity.Tab.SIXIN){
				Bip.bipIncomingPush();
			}else if(!mIsRunForeground){
				Bip.bipIncomingPush();
			}
		}
		RoomInfosData.getInstance().updateRoomInfoFromMessage(roomId,roomVersion);
		messages.add(message);
	}

	private void basicParser(ChatMessageWarpper message, Message m) {
		message.mMessageKey = m.getMsgKey();
		message.mVersion = m.mBody.getVersion();
		if (m.isOffline()) {
			message.mMessageReceiveTime = m.getTime();
			message.mIsOffline = true;
		}
		if(m.isSyn()){
			message.mMessageReceiveTime = m.getTime();
			message.mIsSyn = true;
		}
		if(m.mFrom.contains(DomainUrl.MUC_URL)){//message from room
			message.mComefrom = ChatBaseItem.MESSAGE_COMEFROM.OUT_TO_LOCAL;
			message.mLocalUserId = m.getToId();
			message.mGroupId = m.getFromId();
			message.mToChatUserId = m.getSendId();
			ContactModel model = C_ContactsData.getInstance().getSiXinContact(message.mToChatUserId, null);
			if(model!=null){
				message.mHeadUrl = model.getmHeadUrl();
				message.mUserName  = model.mContactName;
			}
			if (message.mUserName == null) {
				message.mUserName = m.mFromName;
			}
		}else{//message from local
			message.mComefrom = ChatBaseItem.MESSAGE_COMEFROM.LOCAL_TO_OUT;
			message.mLocalUserId = m.getFromId();
			message.mGroupId = m.getToId();
			message.mToChatUserId = message.mGroupId;
		}
	}

	@Override
	public void checkActionType(Message node) throws ActionNotMatchException {
		this.addCheckCase(node.mFrom.contains(DomainUrl.MUC_URL)||node.mTo.contains(DomainUrl.MUC_URL))
				.addCheckCase(node.mType.equals("groupchat"))
				.addCheckCase(node.mBody != null);
	}
	long time = 0;
	boolean mIsRunForeground = false;
	@Override
	public void beginAction() {
		messages = new ArrayList<ChatMessageWarpper>();
		time = System.currentTimeMillis();
		mIsRunForeground = BackgroundUtils.getInstance().isAppOnForeground();
	};

	@Override
	public void commitAction() {
//		SystemUtil.log("syn", "commit group = "+messages.size());
		SystemUtil.log("syn", "after over action = "+(System.currentTimeMillis()-time));
		ActionDispatcher.CALLBACK.onRecive(messages);
	}

//	private void playVoice(){
//		SettingDataManager.getInstance().obtainSwitchState();
//		RRSharedPreferences rrsp = new RRSharedPreferences(RenrenChatApplication.getAppContext());
//		boolean whetherRemind = rrsp.getBooleanValue(String.valueOf(roomId), true);
//		if (SettingDataManager.getInstance().mSoundState && whetherRemind &&
//				(BackgroundUtils.getInstance().isAppOnForeground() || SettingDataManager.getInstance().mPushState)) {
//			if (RenRenChatActivity.class.isInstance(GlobalValue.getCurrentActivity()) && ((RenRenChatActivity)GlobalValue.getCurrentActivity()).mToChatUser.getUId() != roomId){
//				Bip.bipIncomingPush();
//			}else if(MainFragmentActivity.class.isInstance(GlobalValue.getCurrentActivity()) 
//					&& ((MainFragmentActivity)GlobalValue.getCurrentActivity()).mPager.getCurrentItem() == MainFragmentActivity.Tab.SIXIN){
//				Bip.bipIncomingPush();
//			}else if(!BackgroundUtils.getInstance().isAppOnForeground()){
//				Bip.bipIncomingPush();
//			}
//		}
//	}
	
	
	
}
