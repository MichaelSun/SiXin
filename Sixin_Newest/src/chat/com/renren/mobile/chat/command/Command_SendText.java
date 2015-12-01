package com.renren.mobile.chat.command;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.common.manager.LoginManager;
import com.common.messagecenter.base.Utils;
import com.common.utils.Bip;
import com.renren.mobile.chat.R;
import com.renren.mobile.chat.RenrenChatApplication;
import com.renren.mobile.chat.activity.RenRenChatActivity;
import com.renren.mobile.chat.activity.RenRenChatActivity.CanTalkable.GROUP;
import com.renren.mobile.chat.base.model.ChatBaseItem;
import com.renren.mobile.chat.base.model.StateMessageModel;
import com.renren.mobile.chat.base.util.SystemUtil;
import com.renren.mobile.chat.model.warpper.ChatMessageWarpper_Text;
import com.renren.mobile.chat.ui.setting.SettingDataManager;
import com.renren.mobile.chat.util.ChatMessageSender;
import com.renren.mobile.chat.util.StateMessageSender;


/**
 * @author dingwei.chen
 * 发送网络文本消息命令
 * */
public class Command_SendText extends Command {

	RenRenChatActivity mChatActivity = null;
	
	
	
	public Command_SendText(RenRenChatActivity chatActivity) {
		mChatActivity = chatActivity;
	}
	private static int k = 0;
	private ChatMessageWarpper_Text createTesetmessage(){
		ChatMessageWarpper_Text message = new ChatMessageWarpper_Text();
		message.mLocalUserId = LoginManager.getInstance().getLoginInfo().mUserId;
		message.parseUserInfo(mChatActivity.mToChatUser);
		message.mMessageContent = mChatActivity.mHolder.mRoot_InputBar.getText()+""+(k++);
		message.mComefrom= ChatBaseItem.MESSAGE_COMEFROM.LOCAL_TO_OUT;
		message.mDomain = mChatActivity.mChatListAdapter.getDomain();
		return message;
	}
	
	private static final int N = 50;
	@Override
	public void onStartCommand() {
		SettingDataManager.getInstance().obtainSwitchState();
		if(SettingDataManager.getInstance().mSoundState)
			Bip.bipSendMessage();
		if(checkNotEmptyInput()){
//			int s = 0;
//			while(s<N){
//				ChatMessageSender.getInstance().sendMessageToNet(createTesetmessage(), true);
//				s++;
//			}
//			mChatActivity.mHolder.mRoot_ChatList.onLoadDataOver();
			ChatMessageSender.getInstance().sendMessageToNet(createTextMessage(), true);
//			mChatActivity.mChatListAdapter.addAndNotifyCallback(new ChatMessageWarpper_SoftInfo());
			if(mChatActivity.mToChatUser.isGroup()==GROUP.CONTACT_MODEL.Value){
				StateMessageSender.getInstance().send(
						mChatActivity.mToChatUser.getName(),
						mChatActivity.mToChatUser.getLocalUId(),
						mChatActivity.mToChatUser.getUId(), 
						mChatActivity.mToChatUser.getDomain(), 
						StateMessageModel.STATE_TYPE.CANCELED);
			}
			if(mIsClearText){
				clearInputText();//清空输入框
			}
		}else{
			SystemUtil.toast(RenrenChatApplication.getmContext().getResources().getString(R.string.Command_SendText_java_1));		//Command_SendText_java_1=不能输入空串;
		}
	}

	public boolean checkNotEmptyInput(){
		String text = replaceBlank(mChatActivity.mHolder.mRoot_InputBar.getText().toString());
		return  text!=null&&!text.equals("");
	}
	
	private String replaceBlank(String temp){
		Pattern p = Pattern.compile("\\s*|\n");
		Matcher m = p.matcher(temp);
		String after = m.replaceAll("");
		return after;
	}
	public void clearInputText(){
		mChatActivity.mHolder.mRoot_InputBar.mView_TextEdit.setNotNotify();
		mChatActivity.mHolder.mRoot_InputBar.setText("");
	}

	@Override
	public void onEndCommand(Map<String, Object> returnData) {}

	
	public ChatMessageWarpper_Text createTextMessage(){
		ChatMessageWarpper_Text message = new ChatMessageWarpper_Text();
		
		/*? test dingwei.chen start?*/
		{
//			message.mToChatUserId = mChatActivity.mToChatUser.mUserId;
//			message.mLocalUserId = RenrenChatApplication.user_id;
//			message.mMessageContent = mChatActivity.mHolder
//										.mRoot_Input_Text_EmotionEditText
//										.getText().toString();
//			message.mComefrom= ChatBaseItem.MESSAGE_COMEFROM.OUT_TO_LOCAL;
		}
		
		/*? test dinwei.chen end?*/
		message.mLocalUserId = LoginManager.getInstance().getLoginInfo().mUserId;
		message.parseUserInfo(mChatActivity.mToChatUser);
		message.mMessageContent = (mChatActivity.mHolder.mRoot_InputBar.getText()+"").trim();
		message.mComefrom= ChatBaseItem.MESSAGE_COMEFROM.LOCAL_TO_OUT;
		message.mDomain = mChatActivity.mChatListAdapter.getDomain();
		Utils.l("Domain--->" + message.mDomain);
		return message;
	}
	
	
}
