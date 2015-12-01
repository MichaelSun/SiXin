package com.renren.mobile.chat.command;

import java.io.File;
import java.io.FileInputStream;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;

import com.common.manager.LoginManager;
import com.renren.mobile.chat.activity.RenRenChatActivity;
import com.renren.mobile.chat.base.GlobalValue;
import com.renren.mobile.chat.base.model.ChatBaseItem;
import com.renren.mobile.chat.base.util.SystemUtil;
import com.renren.mobile.chat.model.warpper.ChatMessageWarpper_Image;
import com.renren.mobile.chat.ui.imageviewer.ImageViewActivity;
import com.renren.mobile.chat.util.ChatDataHelper;
import com.renren.mobile.chat.util.ChatMessageSender;
/**
 * @author dingwei.chen
 * */
public class Command_SelectPhoto extends Command{
	
	public static interface NEED_RETURN_DATA{
		String RESULT_CODE_INT="RESULT_CODE_INT";//结果码
	}
	
	RenRenChatActivity mChatActivity = null;
	String mTmpFile = null;
	public Command_SelectPhoto(RenRenChatActivity chatActivity) {
		mChatActivity = chatActivity;
	}

	@Override
	public void onStartCommand() {
		Intent intent = new Intent(mChatActivity,ImageViewActivity.class);
		intent.putExtra(ImageViewActivity.NEED_PARAM.REQUEST_CODE, ImageViewActivity.SELECT_PHOTO);
		mTmpFile = GlobalValue.getInstance().getUserPhotos(mChatActivity.mToChatUser.getUId())+
				"renren_"+ String.valueOf(System.currentTimeMillis()); 
		intent.putExtra(ImageViewActivity.NEED_PARAM.LARGE_LOCAL_URI, mTmpFile);
		mChatActivity.startActivityForResult(intent, ImageViewActivity.SELECT_PHOTO);
	}

	@Override
	public void onEndCommand(Map<String, Object> returnData) {
		int resultCode = (Integer)returnData.get(NEED_RETURN_DATA.RESULT_CODE_INT);
		switch(resultCode){
		case Activity.RESULT_OK:
			this.sendMessage(mTmpFile);
			break;
		}
	}
	public void sendMessage(String path){
		ChatMessageWarpper_Image message =new ChatMessageWarpper_Image();
		message.mLocalUserId = LoginManager.getInstance().getLoginInfo().mUserId;
		message.mToChatUserId = mChatActivity.mToChatUser.getUId();
		message.mMessageContent = path;
		message.mUserName = mChatActivity.mToChatUser.getName();
		message.mComefrom = ChatBaseItem.MESSAGE_COMEFROM.LOCAL_TO_OUT;
		message.mIsGroupMessage = mChatActivity.mGroup.Value;
		message.mGroupId = message.mToChatUserId;
		message.mHeadUrl = mChatActivity.mToChatUser.getHeadUrl();
		message.mDomain = mChatActivity.mChatListAdapter.getDomain();
		String imgPath = path;
		File file =  new File(imgPath);
		byte[] bytes = new byte[(int)file.length()];
		FileInputStream fis;
		try {
			fis = new FileInputStream(file);
			fis.read(bytes);
			fis.close();
		} catch (Exception e) {}
		if(bytes.length==0){
			SystemUtil.toast("发送图片失败");
			return;
		}
		ChatDataHelper.getInstance()
						.insertToTheDatabase(message);
		ChatMessageSender.getInstance()
						.uploadData(message, bytes);
	}
	
	

}
