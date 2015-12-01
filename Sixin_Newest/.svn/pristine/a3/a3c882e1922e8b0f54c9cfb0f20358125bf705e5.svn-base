package com.renren.mobile.chat.command;

import java.io.File;
import java.io.FileInputStream;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;

import com.common.manager.LoginManager;
import com.core.util.DeviceUtil;
import com.renren.mobile.chat.activity.RenRenChatActivity;
import com.renren.mobile.chat.base.GlobalValue;
import com.renren.mobile.chat.base.model.ChatBaseItem;
import com.renren.mobile.chat.model.warpper.ChatMessageWarpper_Image;
import com.renren.mobile.chat.ui.imageviewer.ImageViewActivity;
import com.renren.mobile.chat.util.ChatDataHelper;
import com.renren.mobile.chat.util.ChatMessageSender;


/**
 * @author dingwei.chen
 * @说明 照相的命令
 * */
public class Command_TakePhoto extends Command {

	public static interface NEED_RETURN_DATA{
		String RESULT_CODE_INT="RESULT_CODE_INT";//请求码
	}
	
	
	String mTmpFile = null;
	RenRenChatActivity mChatActivity = null;
	public Command_TakePhoto(RenRenChatActivity chatActivity) {
		mChatActivity = chatActivity;
	}

	@Override
	public void onStartCommand() {
		if(!DeviceUtil.getInstance().isSDCardHasEnoughSpace()){
			DeviceUtil.getInstance().toastNotEnoughSpace();
			return;
		}
		Intent intent = new Intent(mChatActivity,ImageViewActivity.class);
		mTmpFile = GlobalValue.getInstance().getUserPhotos(mChatActivity.mToChatUser.getUId())+
				"renren_"+ String.valueOf(System.currentTimeMillis()); 
		intent.putExtra(ImageViewActivity.NEED_PARAM.LARGE_LOCAL_URI, mTmpFile);
		intent.putExtra(ImageViewActivity.NEED_PARAM.REQUEST_CODE, ImageViewActivity.TAKE_PHOTO);
		mChatActivity.startActivityForResult(intent, ImageViewActivity.TAKE_PHOTO);
	}
	@Override
	public void onEndCommand(Map<String, Object> returnData) {
		int resultCode = (Integer)returnData.get(NEED_RETURN_DATA.RESULT_CODE_INT);
		switch(resultCode){
		case Activity.RESULT_OK:
			ChatMessageWarpper_Image message =new ChatMessageWarpper_Image();
			message.mLocalUserId = LoginManager.getInstance().getLoginInfo().mUserId;
			message.mMessageContent = mTmpFile;
			message.parseUserInfo( mChatActivity.mToChatUser);
			message.mIsGroupMessage = mChatActivity.mGroup.Value;
			message.mGroupId = message.mToChatUserId;
			message.mComefrom = ChatBaseItem.MESSAGE_COMEFROM.LOCAL_TO_OUT;
			message.mDomain = mChatActivity.mChatListAdapter.getDomain();
			String imgPath = mTmpFile;
			File file =  new File(imgPath);
			byte[] bytes = new byte[(int)file.length()];
			FileInputStream fis;
			try {
				fis = new FileInputStream(file);
				fis.read(bytes);
				fis.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			ChatDataHelper.getInstance().insertToTheDatabase(message);
			ChatMessageSender.getInstance().uploadData(message, bytes);
			
			break;
		case Activity.RESULT_CANCELED:
			break;
		}
	}

}
