package com.renren.mobile.chat.command;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Map;

import plugin.DBBasedPluginManager;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.os.Bundle;

import com.common.manager.LoginManager;
import com.core.util.DeviceUtil;
import com.renren.mobile.chat.R;
import com.renren.mobile.chat.RenrenChatApplication;
import com.renren.mobile.chat.activity.RenRenChatActivity;
import com.renren.mobile.chat.base.GlobalValue;
import com.renren.mobile.chat.base.model.ChatBaseItem;
import com.renren.mobile.chat.base.util.ImageUtil;
import com.renren.mobile.chat.base.util.SystemUtil;
import com.renren.mobile.chat.model.warpper.ChatMessageWarpper_Image;
import com.renren.mobile.chat.model.warpper.ChatMessageWarpper_Image.IS_BRUSH;
import com.renren.mobile.chat.ui.imageviewer.ImageViewActivity;
import com.renren.mobile.chat.ui.plugins.BrushPlugin;
import com.renren.mobile.chat.util.ChatDataHelper;
import com.renren.mobile.chat.util.ChatMessageSender;

public class Command_Brush extends Command {
	
	public static interface NEED_RETURN_DATA{
		String RESULT_CODE_INT="RESULT_CODE_INT";//结果码
	}
	
	public static String BRUSN_IMAGE_IDENTIFIER = "_brush_png";
	public static String BRUSH_PLUGIN_ID = "plugin_id";
	
	String mTmpFile = null;
	RenRenChatActivity mChatActivity = null;
	public Command_Brush(RenRenChatActivity activity) {
		mChatActivity = activity;
	}

	@Override
	public void onStartCommand() {
//		if (!DeviceUtil.getInstance().isSDCardHasEnoughSpace()) {
//			DeviceUtil.getInstance().toastNotEnoughSpace();
//			return;
//		}
//
//		Intent intent = new Intent(mChatActivity, ImageViewActivity.class);
//		intent.putExtra(ImageViewActivity.NEED_PARAM.REQUEST_CODE, 
//				ImageViewActivity.VIEW_BURSH);
//		mTmpFile = GlobalValue.getInstance().getUserPhotos(mChatActivity.mToChatUser.getUId())+
//				"renren_"+ String.valueOf(System.currentTimeMillis() + BRUSN_IMAGE_IDENTIFIER); 
//		intent.putExtra(ImageViewActivity.NEED_PARAM.LARGE_LOCAL_URI, mTmpFile);		
//		mChatActivity.startActivityForResult(intent, ImageViewActivity.VIEW_BURSH);
	}
	
	public void onStartCommand(Bundle bundle) {
		if (!DeviceUtil.getInstance().isSDCardHasEnoughSpace()) {
			DeviceUtil.getInstance().toastNotEnoughSpace();
			return;
		}	
		Intent intent = new Intent(mChatActivity, ImageViewActivity.class);
		intent.putExtra(ImageViewActivity.NEED_PARAM.REQUEST_CODE, 
						BrushPlugin.REQUEST_CODE_BRUSH_PLUGIN);
		mTmpFile = GlobalValue.getInstance().getUserPhotos(mChatActivity.mToChatUser.getUId())+
				"renren_"+ String.valueOf(System.currentTimeMillis() + BRUSN_IMAGE_IDENTIFIER); 
		
		String pluginId = null;
		if(bundle != null){
			pluginId = bundle.getString(BRUSH_PLUGIN_ID);
			intent.putExtra(BRUSH_PLUGIN_ID, pluginId);
		}
		System.gc();
		DBBasedPluginManager manager = new DBBasedPluginManager();
    	BrushPlugin p = (BrushPlugin)manager.getPlugin(Integer.parseInt(pluginId), true);
		p.start(mChatActivity, new Bundle());
		System.gc();
	}

	@Override
	public void onEndCommand(Map<String, Object> returnData) {
//		int resultCode = (Integer) returnData.get(NEED_RETURN_DATA.RESULT_CODE_INT);
//		switch(resultCode){
//		case Activity.RESULT_OK:
//			sendMessage(mTmpFile);
//		}
	}
	
	public void onEndCommand(Intent data){
		String originalPath = null;
		SystemUtil.log("jason","onEndCommand ");
		if(data != null){
			SystemUtil.log("jason","data not null command");
			originalPath = (String) data.getStringExtra(BrushPlugin.STRING_EXTRA_BRUSH_PLUGIN);
			SystemUtil.log("jason","originalPath "+originalPath +" to rename "+mTmpFile);
			if(originalPath != null && originalPath.trim().length() > 0){
//				File temp = new File(originalPath);
//				temp.setExecutable(true);
//				temp.renameTo(new File(mTmpFile));
				try {
					this.copyFile(originalPath, mTmpFile);
					SystemUtil.log("jason", "length = "+new File(mTmpFile).length());
				} catch (Exception e) {
					SystemUtil.toastTest("send error");
					return ;
				}
				this.sendMessage(mTmpFile);
			}
		}
		
	}
	public void copyFile(String src,String desc) throws Exception{
		FileInputStream fis = new FileInputStream(src);
		FileOutputStream fos = new FileOutputStream(desc);
		byte[] data  = new byte[1024];
		int readLength = -1;
		while((readLength=fis.read(data))!=-1){
			fos.write(data, 0, readLength);
		}
		fis.close();
		fos.close();
	}
	
	
	
//	private void storePNGImageToFile(Bitmap bitmap, String path, boolean isThumb){
//		SystemUtil.log("jason","storePNGImageToFile");
//		String imgPath = null;
//		if (path.endsWith(".png")) {
//			imgPath = path.substring(0, path.length() - 4);
//		} else {
//			imgPath = path;
//		}
//		if (bitmap == null || bitmap.isRecycled()) {
//			SystemUtil.log("jason","return");
//			return ;
//		}
//		File file = new File(imgPath);
//		FileOutputStream accessFile = null;
//		try {
//			accessFile = new FileOutputStream(file);
//			bitmap.compress(Bitmap.CompressFormat.PNG, 80, accessFile);
//			accessFile.flush();
//			accessFile.close();
//			File temp = new File(imgPath);
//			SystemUtil.log("jason","temp exist : " + temp.exists());
//		} catch (Exception e) {
//			SystemUtil.toast(RenrenChatApplication.getmContext().getResources().getString(R.string.ImageViewActivity_java_5));		//ImageViewActivity_java_5=图片文件存储失败; 
//			return ;
//		}
//		if (!isThumb) {
//			Options op = new Options();
//			op.inSampleSize = ImageViewActivity.getSampleSize(bitmap.getWidth(), bitmap.getHeight(), 100);
//			Bitmap thumb = BitmapFactory.decodeFile(file.toString(), op);
//			storePNGImageToFile(thumb, imgPath + "_small", true);
//			thumb.recycle();
//		}
//	}
	
	public void sendMessage(String path){
		ChatMessageWarpper_Image message =new ChatMessageWarpper_Image();
		message.mLocalUserId = LoginManager.getInstance().getLoginInfo().mUserId;
		message.mToChatUserId = mChatActivity.mToChatUser.getUId();
		message.mMessageContent = path;
		SystemUtil.log("jason"," mMessageContent:"+path);
		message.mUserName = mChatActivity.mToChatUser.getName();
		message.mComefrom = ChatBaseItem.MESSAGE_COMEFROM.LOCAL_TO_OUT;
		message.mIsGroupMessage = mChatActivity.mGroup.Value;
		message.mGroupId = message.mToChatUserId;
		message.mHeadUrl = mChatActivity.mToChatUser.getHeadUrl();
		message.mIsBrushPen = IS_BRUSH.YES;
		message.mDomain = mChatActivity.mChatListAdapter.getDomain();
		final String imgPath = path;
		File file =  new File(imgPath);
		byte[] bytes = new byte[(int)file.length()];
		FileInputStream fis;
		try {
			fis = new FileInputStream(file);
			fis.read(bytes);
			fis.close();
		} catch (Exception e) {}
		SystemUtil.log("jason"," upload file length = :"+file.length());
		if(file.length()==0){
			SystemUtil.toastTest("send error");
			return;
		}
		ChatDataHelper.getInstance()
						.insertToTheDatabase(message);
		ChatMessageSender.getInstance().uploadPNGData(message, bytes);		
	}
}