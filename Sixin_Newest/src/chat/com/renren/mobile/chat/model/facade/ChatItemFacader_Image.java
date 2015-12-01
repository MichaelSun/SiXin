package com.renren.mobile.chat.model.facade;

import java.io.File;

import android.graphics.Bitmap;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.FrameLayout;

import com.common.manager.MessageManager.OnSendTextListener.SEND_TEXT_STATE;
import com.renren.mobile.chat.base.GlobalValue;
import com.renren.mobile.chat.base.inter.Subject;
import com.renren.mobile.chat.base.model.ChatBaseItem;
import com.renren.mobile.chat.base.util.ImagePool;
import com.renren.mobile.chat.base.util.SystemUtil;
import com.renren.mobile.chat.holder.ChatItemHolder;
import com.renren.mobile.chat.model.warpper.ChatMessageWarpper;
import com.renren.mobile.chat.model.warpper.ChatMessageWarpper_Image;
import com.renren.mobile.chat.util.BaseChatListAdapter.OnItemLongClickCallback;
import com.renren.mobile.chat.util.ImageItemOnClickListenner;


/**
 * @author dingwei.chen
 * @说明 图片条目装饰器
 * */
public class ChatItemFacader_Image extends ChatItemFacader{
	
	@Override
	public void facade(ChatItemHolder holder, ChatMessageWarpper chatmessage,OnItemLongClickCallback callback) {
		this.process(holder, (ChatMessageWarpper_Image)chatmessage,callback);
	}
	
	private void process(ChatItemHolder holder,final  ChatMessageWarpper_Image chatmessage,final OnItemLongClickCallback callback){
		holder.mMessage_Image_ImageView.setVisibility(View.VISIBLE);
		holder.mMessage_Image_ImageView.setIsBrush(chatmessage.isBrush());
		switch(chatmessage.mComefrom){
			case ChatBaseItem.MESSAGE_COMEFROM.LOCAL_TO_OUT:
				this.processLocalToOut(holder, chatmessage);
				break;
			case ChatBaseItem.MESSAGE_COMEFROM.OUT_TO_LOCAL:
				this.processOutToLocal(holder, chatmessage);
				break;
		}
		holder.update(chatmessage.mMessageState, null);
		if(chatmessage.mSendTextState==SEND_TEXT_STATE.SEND_PREPARE){
			holder.mMessage_Image_ImageView.setLayerVisible(chatmessage.mComefrom==ChatBaseItem.MESSAGE_COMEFROM.LOCAL_TO_OUT);
		}else{
			holder.mMessage_Image_ImageView.setLayerVisible(false);
		}
		if(chatmessage.mComefrom == ChatBaseItem.MESSAGE_COMEFROM.OUT_TO_LOCAL){
			holder.mMessage_Error_ImageView.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					chatmessage.download(true);
				}
			});
		}
		holder.mMessage_Image_ImageView.setOnClickListener(new ImageItemOnClickListenner(chatmessage));
		holder.mMessage_Image_ImageView.setOnLongClickListener(new OnLongClickListener() {
			public boolean onLongClick(View v) {
				callback.onItemLongClick(chatmessage);
				return false;
			}
		});
	}
	
	
	
	
	
	
	/*处理发送来的消息*/
	private void processOutToLocal(ChatItemHolder holder, ChatMessageWarpper_Image chatmessage){
		final String imgPath = chatmessage.mMessageContent+(chatmessage.isBrush()?"":"_small");
		File file = new File(imgPath);
		if(file.exists()){
			Bitmap bitmap = ImagePool.getInstance()
					.getBitmapFromLocal(file.getAbsolutePath());
			if(bitmap!=null){
				holder.mMessage_Image_ImageView.setImageBitmap(bitmap,file.getAbsolutePath());
			}else{
				holder.mMessage_Image_ImageView
											.setImageBitmap(GlobalValue.getInstance().getDefualtBitmap(true));
			}
		}else{/*文件不存在*/
			holder.mMessage_Image_ImageView.setImageBitmap(GlobalValue.getInstance().getDefualtBitmap(true));
			if(chatmessage.mMessageState!=Subject.COMMAND.COMMAND_DOWNLOAD_IMAGE_ERROR){
				chatmessage.download(false);
			}else{
				holder.mMessage_Error_ImageView.setVisibility(View.VISIBLE);
				return;
			}
			holder.mMessage_Error_ImageView.setVisibility(View.GONE);
		}
	}
	/*处理发送出去的消息*/
	private void processLocalToOut(ChatItemHolder holder, ChatMessageWarpper_Image chatmessage){
		final String imgPath = chatmessage.mMessageContent+(chatmessage.isBrush()?"":"_small");
		if(new File(imgPath).exists()){
			holder.mMessage_Image_ImageView.setImagePath(imgPath,false);
		}else{
			chatmessage.download(false);
		}
	}
	@Override
	public View getFacadeView(ChatItemHolder holder) {
		return holder.mMessage_ImageMessage_LinearLayout;
	}
	

}
