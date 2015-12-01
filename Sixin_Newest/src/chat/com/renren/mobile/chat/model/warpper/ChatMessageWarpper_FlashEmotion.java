package com.renren.mobile.chat.model.warpper;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import android.graphics.Bitmap;

import com.common.emotion.emotion.ScrollingLock;
import com.common.emotion.manager.Emotion;
import com.common.emotion.manager.EmotionManager;
import com.common.emotion.manager.IEmotionManager.OnEmotionParserCallback;
import com.common.utils.Methods;
import com.core.util.CommonUtil;
import com.data.xmpp.Message;
import com.renren.mobile.chat.R;
import com.renren.mobile.chat.RenrenChatApplication;
import com.renren.mobile.chat.base.inter.Subject;
import com.renren.mobile.chat.base.model.ChatBaseItem;
import com.renren.mobile.chat.base.util.SystemUtil;
import com.renren.mobile.chat.views.ItemLongClickDialogProxy.LONGCLICK_COMMAND;
public class ChatMessageWarpper_FlashEmotion extends ChatMessageWarpper {
	public ChatMessageWarpper_FlashEmotion(){
		this.mMessageType = ChatBaseItem.MESSAGE_TYPE.FLASH;
	}
	public int mIndex = 0;
	boolean mIsFlash = true;
	@Override
	public List<OnLongClickCommandMapping> getOnClickCommands() {
		List<OnLongClickCommandMapping> list = new LinkedList<ChatMessageWarpper.OnLongClickCommandMapping>();
		if(this.mMessageState == Subject.COMMAND.COMMAND_MESSAGE_ERROR ){
			list.add(new OnLongClickCommandMapping(RenrenChatApplication.getmContext().getResources().getString(R.string.ChatMessageWarpper_FlashEmotion_java_1),LONGCLICK_COMMAND.RESEND));		//ChatMessageWarpper_FlashEmotion_java_1=重新发送; 
		}
		list.add(new OnLongClickCommandMapping(RenrenChatApplication.getmContext().getResources().getString(R.string.ChatMessageWarpper_FlashEmotion_java_2),LONGCLICK_COMMAND.DELETE));		//ChatMessageWarpper_FlashEmotion_java_2=删除炫酷表情消息; 
		list.add(new OnLongClickCommandMapping(RenrenChatApplication.getmContext().getResources().getString(R.string.ChatMessageWarpper_FlashEmotion_java_3),LONGCLICK_COMMAND.FORWARD));		//ChatMessageWarpper_FlashEmotion_java_3=转发; 
		list.add(new OnLongClickCommandMapping(RenrenChatApplication.getmContext().getResources().getString(R.string.ChatMessageWarpper_FlashEmotion_java_4),LONGCLICK_COMMAND.CANCEL));		//ChatMessageWarpper_FlashEmotion_java_4=取消; 
		return list;
	}
	@Override
	public String getDescribe() {
		return RenrenChatApplication.getmContext().getResources().getString(R.string.ChatMessageWarpper_FlashEmotion_java_5);		//ChatMessageWarpper_FlashEmotion_java_5=[炫酷表情]; 
	}
	
	UpdateImageAction mUpdateAction = new UpdateImageAction();
	class UpdateImageAction implements Runnable{
		public Bitmap mBitmap = null;
		public void updateBitmap(Bitmap bitmap){
			this.mBitmap = bitmap;
		}
		@Override
		public void run() {
			// TODO Auto-generated method stub
			if(mObserver!=null){
				Map<String,Object> data = obtainData(mBitmap);
				mObserver.update(
							Subject.COMMAND.COMMAND_UPDATE_FLASH_IMAGE,
							data);
			}
		}
		
	}
	
	
	
	Map<String,Object> mData = new HashMap<String, Object>();
	
	private Map<String,Object> obtainData(Bitmap bitmap){
		mData.put(Subject.DATA.COMMAND_UPDATE_IMAGE, bitmap);
		return mData;
	}
//	public static final OnEmotionParserCallbackImpl CALLBACK = new OnEmotionParserCallbackImpl();
	public static class OnEmotionParserCallbackImpl implements OnEmotionParserCallback{

		ChatMessageWarpper_FlashEmotion mModel = null;
		public OnEmotionParserCallbackImpl(ChatMessageWarpper_FlashEmotion model){
			this.mModel = model;
		}
		public void updateChatMessageWarpper_FlashEmotion(ChatMessageWarpper_FlashEmotion model){
			mModel = model;
		}
		
		@Override
		public void onEmotionSuccess(Emotion emotion) {
			if (emotion != null) {
				if(ScrollingLock.isScrolling){
					mModel.mIndex = 0;
				}
				if(emotion.mEmotionList.size()<=mModel.mIndex){
					return;
				}
				final Bitmap bitmap = emotion.mEmotionList.get(mModel.mIndex);
				
				mModel.mIndex++;
				mModel.mIndex = mModel.mIndex % emotion.mFrameSize;
				mModel.mUpdateAction.updateBitmap(bitmap);
				mModel.post(mModel.mUpdateAction);
				mModel.mIsFlash = true;
			}
		}

		@Override
		public void onEmotionError(Bitmap bitmap) {
			mModel.mUpdateAction.updateBitmap(bitmap);
			mModel.post(mModel.mUpdateAction);
			mModel.mIsFlash = false;
		}

		@Override
		public void onEmotionLoading(Bitmap bitmap) {
			mModel.mIsFlash =false;
			mModel.mUpdateAction.updateBitmap(bitmap);
			mModel.post(mModel.mUpdateAction);
		}
		
	}
	
	
	
	@Override
	public void reflash() {
//		CALLBACK.updateChatMessageWarpper_FlashEmotion(this);
		EmotionManager.getInstance().registerCoolEmotionParserCallBack(new OnEmotionParserCallbackImpl(this));
		EmotionManager.getInstance().getCoolEmotion(mMessageContent);
	}
	@Override
	public boolean isReflash() {
		return mIsFlash;
	}
	@Override
	public void download(boolean isForceDownload) {}
	@Override
	public void onErrorCode() {
		this.addErrorCode(Subject.COMMAND.COMMAND_MESSAGE_ERROR);
	}
	@Override
	public boolean isHideBackground() {
		return super.isHideBackground()||!hasNewsFeed();
	}
	@Override
	public void swapDataFromXML(Message message) {
		this.mMessageContent = Methods.htmlDecoder(message.mBody.mTextNode.mValue);
	}
}
