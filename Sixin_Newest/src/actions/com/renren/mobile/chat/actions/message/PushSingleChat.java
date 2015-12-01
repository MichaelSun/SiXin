package com.renren.mobile.chat.actions.message;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;

import com.common.actions.ActionNotMatchException;
import com.common.manager.LoginManager;
import com.common.statistics.BackgroundUtils;
import com.common.utils.Bip;
import com.common.utils.RRSharedPreferences;
import com.core.util.CommonUtil;
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
import com.renren.mobile.chat.ui.setting.SettingDataManager;
import com.renren.mobile.chat.util.CurrentChatSetting;
/**
 * @author dingwei.chen1988@gmail.com
 * @说明 单人聊天业务
 * */
public class PushSingleChat extends Action_Message {

	List<ChatMessageWarpper> messages = null;
	RRSharedPreferences rrsp = new RRSharedPreferences(RenrenChatApplication.getAppContext());
	@Override
	public void processAction(Message node,long id) {
		SystemUtil.log("handle", "push single start");
		SettingDataManager.getInstance().obtainSwitchState();
		
		if (SettingDataManager.getInstance().mSoundState && 
				(mIsRunForeground || SettingDataManager.getInstance().mPushState)) {
			Activity activity = GlobalValue.getCurrentActivity();
			if (activity instanceof RenRenChatActivity && ((RenRenChatActivity)activity).mToChatUser.getUId() != node.getFromId()){
				Bip.bipIncomingPush();
			}else if(activity instanceof MainFragmentActivity && ((MainFragmentActivity)activity).mPager.getCurrentItem() == MainFragmentActivity.Tab.SIXIN){
				Bip.bipIncomingPush();
			}else if(!mIsRunForeground){
				Bip.bipIncomingPush();
			}
		}
		ChatMessageWarpper message = ChatMessageFactory.getInstance().obtainMessage(node.mBody.getType());
		message.mIsGroupMessage = ChatBaseItem.MESSAGE_ISGROUP.IS_SINGLE;
		this.basicParser(message, node);
		message.swapDataFromXML(node);
		if(node.isContainFeed()){//解析新鲜事
			NewsFeedWarpper item = NewsFeedFactory.getInstance().obtainNewsFeedModel(node);
			message.setNewsFeedModel(item);
		}else{
		}
		messages.add(message);
	}
	private void basicParser(ChatMessageWarpper message,Message m){
		message.mUserName = m.getFromName();
		message.mLocalUserId = m.getToId();
		message.mToChatUserId = m.getFromId();
		message.mGroupId = message.mToChatUserId;
		message.mComefrom= ChatBaseItem.MESSAGE_COMEFROM.OUT_TO_LOCAL;
		message.mMessageKey = m.getMsgKey();
		message.mDomain = m.getFromDomain();
		if(m.isOffline()){
			message.mMessageReceiveTime = m.getTime();
			message.mIsOffline = true;
		}
		if(m.isSyn()){
			message.mMessageReceiveTime = m.getTime();
			message.mIsSyn = true;
		}
		if(message.mToChatUserId==LoginManager.getInstance().getLoginInfo().mUserId){
			long id = message.mToChatUserId;
			message.mToChatUserId = message.mLocalUserId;
			message.mLocalUserId = id;
			message.mGroupId = message.mToChatUserId;
			message.mComefrom = ChatBaseItem.MESSAGE_COMEFROM.LOCAL_TO_OUT;
		}
	
	}
	@Override
	public void checkActionType(Message node) throws ActionNotMatchException{
		this.addCheckCase(node.mType.equals("chat"))
			.addCheckCase(!node.mBody.mType.equals("action"));
	}
	boolean mIsRunForeground;
	@Override
	public void beginAction() {
		messages = new ArrayList<ChatMessageWarpper>();
		mIsRunForeground = BackgroundUtils.getInstance().isAppOnForeground();
	};
	
	@Override
	public void commitAction() {
		ActionDispatcher.CALLBACK.onRecive(messages);
		SystemUtil.log("handle", "push single commit");
	}

}
