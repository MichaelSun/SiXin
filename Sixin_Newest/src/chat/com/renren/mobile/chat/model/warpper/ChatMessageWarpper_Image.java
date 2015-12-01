package com.renren.mobile.chat.model.warpper;
import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.graphics.Bitmap;

import com.common.manager.DataManager.Uploadable_Image;
import com.core.orm.ORM;
import com.data.xmpp.Message;
import com.renren.mobile.chat.R;
import com.renren.mobile.chat.RenrenChatApplication;
import com.renren.mobile.chat.base.GlobalValue;
import com.renren.mobile.chat.base.inter.Playable.PLAY_STATE;
import com.renren.mobile.chat.base.inter.Subject;
import com.renren.mobile.chat.base.model.ChatBaseItem;
import com.renren.mobile.chat.base.util.FileUtil;
import com.renren.mobile.chat.base.util.ImagePool;
import com.renren.mobile.chat.base.util.ImagePool.DownloadImageRequest;
import com.renren.mobile.chat.base.util.ImagePool.OnDownloadImageListenner;
import com.renren.mobile.chat.base.util.SystemUtil;
import com.renren.mobile.chat.database.ChatHistory_Column;
import com.renren.mobile.chat.util.ChatDataHelper;
import com.renren.mobile.chat.util.ChatMessageSender;
import com.renren.mobile.chat.views.ItemLongClickDialogProxy.LONGCLICK_COMMAND;
public class ChatMessageWarpper_Image extends ChatMessageWarpper implements 
		OnDownloadImageListenner,
		Uploadable_Image{
	public ChatMessageWarpper_Image(){
		this.mMessageType =ChatBaseItem.MESSAGE_TYPE.IMAGE;
	}
	/**
	 * 图片数据
	 * */
	public String mMineType;
	@ORM(mappingColumn=ChatHistory_Column.TINY_URL)
	public String mTinyUrl;
	@ORM(mappingColumn=ChatHistory_Column.MAIN_URL)
	public String mMainUrl;
	@ORM(mappingColumn=ChatHistory_Column.LARGE_URL)
	public String mLargeUrl;
	@ORM(mappingColumn=ChatHistory_Column.IS_BRUSH)
	public int mIsBrushPen = IS_BRUSH.NO;
	
	public static interface IS_BRUSH{
		public int YES = 1;
		public int NO = 0;
	}
	
	
	
	
	
	public boolean mIsDownLoad = false;
	public int mPlayState = PLAY_STATE.STOP;
	public long mUploadDataNumber = 0l;
	public long mUploadSumDataLength = 0l;
	public long mUploadSumDataPercent = 0L;
	public long mDownloadDataNumber = 0l;
	public long mDownloadSumDataLength = 0l;
	public long mDownloadSumDataPercent = 0L;
	Map<String,Object> mUpload_Or_Download_Data = null;
	@Override
	public void download(boolean isForceDownload){
//		if(this.mComefrom == ChatBaseItem.MESSAGE_COMEFROM.LOCAL_TO_OUT){
//			return;
//		}
		if((			this.mSendTextState!=SEND_TEXT_STATE.SEND_PREPARE
				&&  this.mMessageState!=Subject.COMMAND.COMMAND_DOWNLOAD_IMAGE_ERROR)
					||	isForceDownload
		){
			String url = this.isBrush()?this.mLargeUrl:this.mTinyUrl;
			SystemUtil.log("onmea", "down load =========== "+isBrush()+","+url);
			DownloadImageRequest request = new DownloadImageRequest(
												url,
												this, 
												isBrush()?this.mMessageContent:this.mMessageContent+"_small");
			request.mIsCompress = false;
			ImagePool.getInstance().downloadImage(request);
		}
	}
	@Override
	public void resend() {
		if(this.mSendTextState == SEND_TEXT_STATE.SEND_PREPARE){
			return;
		}
		if(this.mMessageState==COMMAND.COMMAND_UPLOAD_IMAGE_ERROR){
			this.mMessageState=COMMAND.COMMAND_MESSAGE_OVER;
			try {
				ChatMessageSender.getInstance().uploadData(
						this, 
						FileUtil.getInstance().readBytes(new File(mMessageContent.split(".jpg")[0])));
			} catch (Exception e) {
				SystemUtil.toast(RenrenChatApplication.getAppContext().getResources().getString(R.string.ChatMessageWarpper_Image_java_1));		//ChatMessageWarpper_Image_java_1=文件未找到; 
			}
		}else{
			this.mMessageState=COMMAND.COMMAND_MESSAGE_OVER;
			ChatMessageSender.getInstance().sendMessageToNet(this, false);
		}
	}
	@Override
	public List<OnLongClickCommandMapping> getOnClickCommands() {
		List<OnLongClickCommandMapping> list = new LinkedList<ChatMessageWarpper.OnLongClickCommandMapping>();
		if(this.mMessageState == Subject.COMMAND.COMMAND_MESSAGE_ERROR || this.mMessageState == Subject.COMMAND.COMMAND_UPLOAD_IMAGE_ERROR ){
			list.add(new OnLongClickCommandMapping(RenrenChatApplication.getmContext().getResources().getString(R.string.ChatMessageWarpper_FlashEmotion_java_1),LONGCLICK_COMMAND.RESEND));		//ChatMessageWarpper_FlashEmotion_java_1=重新发送; 
		}
		if(this.mMessageState == Subject.COMMAND.COMMAND_DOWNLOAD_IMAGE_ERROR){
			list.add(new OnLongClickCommandMapping(RenrenChatApplication.getmContext().getResources().getString(R.string.ChatMessageWarpper_Image_java_2),LONGCLICK_COMMAND.REDOWNLOAD));		//ChatMessageWarpper_Image_java_2=重新下载; 
		}
		list.add(new OnLongClickCommandMapping(RenrenChatApplication.getmContext().getResources().getString(R.string.ChatMessageWarpper_Image_java_3),LONGCLICK_COMMAND.DELETE));		//ChatMessageWarpper_Image_java_3=删除图片消息; 
		list.add(new OnLongClickCommandMapping(RenrenChatApplication.getmContext().getResources().getString(R.string.ChatMessageWarpper_FlashEmotion_java_3),LONGCLICK_COMMAND.FORWARD));		//ChatMessageWarpper_FlashEmotion_java_3=转发; 
		list.add(new OnLongClickCommandMapping(RenrenChatApplication.getmContext().getResources().getString(R.string.ChatMessageWarpper_FlashEmotion_java_4),LONGCLICK_COMMAND.CANCEL));		//ChatMessageWarpper_FlashEmotion_java_4=取消; 
		return list;
	}
	@Override
	public String getDescribe() {
		return RenrenChatApplication.getmContext().getResources().getString(R.string.ChatMessageWarpper_Image_java_4);		//ChatMessageWarpper_Image_java_4=[图片]; 
	}
	/*----------------------下载回调-------------------------*/
	@Override
	public void onDownloadStart() {
		this.mSendTextState = SEND_TEXT_STATE.SEND_PREPARE;
		this.updateChatMessageState(Subject.COMMAND.COMMAND_DOWNLOAD_IMAGE_OVER,false);
		this.observerUpdate(Subject.COMMAND.COMMAND_IMAGE_DOWNLOADING, null);
		this.observerUpdate(Subject.COMMAND.COMMAND_MESSAGE_SEND_SHOW_LOADING, null);
		this.observerUpdate(Subject.COMMAND.COMMAND_MESSAGE_SEND_HIDE_IMAGE_LAYER, null);
	}
	@Override
	public void onDownloadSuccess(String imageUrl,Bitmap bitmap) {
		this.mSendTextState = SEND_TEXT_STATE.SEND_OVER;
		this.updateChatMessageState(Subject.COMMAND.COMMAND_DOWNLOAD_IMAGE_OVER,false);
		Map<String,Object> data = new HashMap<String, Object>(1);
		data.put(Subject.DATA.COMMAND_IMAGE_DOWNLOAD_OVER,bitmap);
		this.observerUpdate(Subject.COMMAND.COMMAND_DOWNLOAD_IMAGE_OVER, data);
		this.observerUpdate(Subject.COMMAND.COMMAND_MESSAGE_SEND_HIDE_LOADING, null);
	}
	@Override
	public void onDownloadError() {
		this.mSendTextState = SEND_TEXT_STATE.SEND_OVER;
		this.updateChatMessageState(Subject.COMMAND.COMMAND_DOWNLOAD_IMAGE_ERROR,false);
		this.observerUpdate(Subject.COMMAND.COMMAND_DOWNLOAD_IMAGE_ERROR, null);
		this.observerUpdate(Subject.COMMAND.COMMAND_MESSAGE_SEND_HIDE_LOADING, null);
	}
	/*----------------------上传回调-------------------------*/
	private void onUploadSuccess() {
		this.observerUpdate(Subject.COMMAND.COMMAND_UPLOAD_IMAGE_OVER, null);
		ChatMessageSender.getInstance()			
				.sendMessageToNet(ChatMessageWarpper_Image.this,false);
	}
	@Override
	public void onUploadError() {
		this.updateChatMessageState(Subject.COMMAND.COMMAND_UPLOAD_IMAGE_ERROR,false);
		this.mSendTextState = SEND_TEXT_STATE.SEND_OVER;
		this.observerUpdate(Subject.COMMAND.COMMAND_UPLOAD_IMAGE_ERROR, null);
		this.observerUpdate(Subject.COMMAND.COMMAND_MESSAGE_SEND_HIDE_LOADING, null);
	}
	@Override
	public void onUploadStart() {
		this.updateChatMessageState(Subject.COMMAND.COMMAND_UPLOAD_IMAGE_OVER,false);
		this.observerUpdate(Subject.COMMAND.COMMAND_IMAGE_UPLOADING, null);
		this.observerUpdate(Subject.COMMAND.COMMAND_MESSAGE_SEND_SHOW_IMAGE_LAYER, null);
		this.observerUpdate(Subject.COMMAND.COMMAND_MESSAGE_SEND_SHOW_LOADING, null);
		this.observerUpdate(Subject.COMMAND.COMMAND_UPLOAD_IMAGE_OVER, null);
		this.mSendTextState = SEND_TEXT_STATE.SEND_PREPARE;
	}
	@Override
	public void onSendTextSuccess() {
		super.onSendTextSuccess();
		this.observerUpdate(Subject.COMMAND.COMMAND_MESSAGE_SEND_HIDE_IMAGE_LAYER, null);
		this.mSendTextState = SEND_TEXT_STATE.SEND_OVER;
	}
	@Override
	public void onSendTextError() {
		super.onSendTextError();
		this.observerUpdate(Subject.COMMAND.COMMAND_MESSAGE_SEND_HIDE_IMAGE_LAYER, null);
		this.mSendTextState = SEND_TEXT_STATE.SEND_OVER;
	}
	@Override
	public void onErrorCode() {
		this.addErrorCode(Subject.COMMAND.COMMAND_MESSAGE_ERROR)
			  .addErrorCode(Subject.COMMAND.COMMAND_UPLOAD_IMAGE_ERROR)
		      .addErrorCode(Subject.COMMAND.COMMAND_DOWNLOAD_IMAGE_ERROR);
	}
	@Override
	public void swapDataFromXML(Message message) {
		this.mMineType = message.mBody.mImageNode.mMineType;
		this.mTinyUrl = message.mBody.mImageNode.mTiny;
		this.mMainUrl = message.mBody.mImageNode.mMain;
		this.mLargeUrl = message.mBody.mImageNode.mLarge;
		this.mIsBrushPen = message.mBody.mImageNode.isBrushImage()?IS_BRUSH.YES:IS_BRUSH.NO;
		String dir = GlobalValue.getInstance().getUserPhotos_Tiny(this.mToChatUserId);
		this.mMessageContent = dir+FileUtil.getInstance().getFileNameFromURL(this.mTinyUrl);
	}
	@Override
	public long getToId() {
		return this.mToChatUserId;
	}
	@Override
	public void onUploadOver(String tinyUrl, String mainUrl, String largeUrl) {
		ContentValues values = new ContentValues(4);
		this.mTinyUrl = tinyUrl;
		this.mMainUrl = mainUrl;
		this.mLargeUrl =largeUrl;
		values.put(ChatHistory_Column.MAIN_URL, this.mMainUrl);
		values.put(ChatHistory_Column.TINY_URL, this.mTinyUrl);
		values.put(ChatHistory_Column.LARGE_URL,this.mLargeUrl);
		values.put(ChatHistory_Column.MESSAGE_STATE, Subject.COMMAND.COMMAND_UPLOAD_IMAGE_OVER);
		ChatDataHelper.getInstance().update(this, values);
		this.onUploadSuccess();
	}
	public boolean isBrush(){
		return this.mIsBrushPen==IS_BRUSH.YES;
	}
	@Override
	public boolean isShowCover() {
		// TODO Auto-generated method stub
		return true;
	}
}
