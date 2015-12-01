package com.renren.mobile.chat.model.facade;

import android.graphics.Bitmap;
import android.view.View;

import com.common.emotion.manager.Emotion;
import com.common.emotion.manager.EmotionManager;
import com.common.emotion.manager.IEmotionManager.OnEmotionParserCallback;
import com.renren.mobile.chat.R;
import com.renren.mobile.chat.RenrenChatApplication;
import com.renren.mobile.chat.base.util.SystemUtil;
import com.renren.mobile.chat.holder.ChatItemHolder;
import com.renren.mobile.chat.model.warpper.ChatMessageWarpper;
import com.renren.mobile.chat.model.warpper.ChatMessageWarpper_FlashEmotion;
import com.renren.mobile.chat.util.BaseChatListAdapter.OnItemLongClickCallback;


/**
 * @author dingwei.chen
 * @说明 文本条目装饰器
 * */
public class ChatItemFacader_FlashEmotion extends ChatItemFacader{

	@Override
	public void facade(ChatItemHolder holder, ChatMessageWarpper chatmessage,OnItemLongClickCallback callback) {
		this.process(holder, (ChatMessageWarpper_FlashEmotion)chatmessage);
	}
	
	public void process(final ChatItemHolder holder,final  ChatMessageWarpper_FlashEmotion chatmessage){
		OnEmotionParserCallback callback = new OnEmotionParserCallback() {

			@Override
			public void onEmotionSuccess(Emotion emotion) {
				holder.mMessage_Flash_TextView.setVisibility(View.GONE);
				holder.mMessage_Flash_ImageView.setVisibility(View.VISIBLE);
				Bitmap bitmap = emotion.next();
				holder.mMessage_Flash_ImageView.setImageBitmap(bitmap);
			}

			@Override
			public void onEmotionError(Bitmap bitmap) {
				holder.mMessage_Flash_TextView.setVisibility(View.VISIBLE);
				holder.mMessage_Flash_ImageView.setVisibility(View.GONE);
				holder.mMessage_Flash_TextView
						.setText(RenrenChatApplication.getmContext().getResources()
								.getString(R.string.ChatItemFacader_FlashEmotion_java_1)
								+chatmessage.mMessageContent);
			}

			@Override
			public void onEmotionLoading(Bitmap bitmap) {
				// TODO Auto-generated method stub
				holder.mMessage_Flash_TextView.setVisibility(View.GONE);
				holder.mMessage_Flash_ImageView.setVisibility(View.VISIBLE);
				holder.mMessage_Flash_ImageView.setImageBitmap(bitmap);
			}
			
			
		};
		EmotionManager.getInstance().registerCoolEmotionParserCallBack(callback);
		EmotionManager.getInstance().getCoolEmotion(chatmessage.mMessageContent);
	}

	@Override
	public View getFacadeView(ChatItemHolder holder) {
		// TODO Auto-generated method stub
		return holder.mMessage_FlashMessage_LinearLayout;
	}
	
	
	
}
