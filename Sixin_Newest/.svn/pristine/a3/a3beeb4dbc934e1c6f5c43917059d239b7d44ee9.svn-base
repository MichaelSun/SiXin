package com.renren.mobile.chat.model.facade;

import android.view.View;
import android.view.View.OnLongClickListener;

import com.common.emotion.emotion.EmotionString;
import com.common.emotion.emotion.EmotionStringRef;
import com.renren.mobile.chat.holder.ChatItemHolder;
import com.renren.mobile.chat.model.warpper.ChatMessageWarpper;
import com.renren.mobile.chat.model.warpper.ChatMessageWarpper_Text;
import com.renren.mobile.chat.util.BaseChatListAdapter.OnItemLongClickCallback;


/**
 * @author dingwei.chen
 * @说明 文本条目装饰器
 * */
public class ChatItemFacader_Text extends ChatItemFacader{

	@Override
	public void facade(ChatItemHolder holder, ChatMessageWarpper chatmessage,OnItemLongClickCallback callback) {
		this.process(holder, (ChatMessageWarpper_Text)chatmessage,callback);
	}
	
	public void process(final ChatItemHolder holder,final ChatMessageWarpper_Text chatmessage,final OnItemLongClickCallback callback){
		EmotionString emotionstring = EmotionStringRef.getInstacne().get(chatmessage.mMessageContent);
		if(emotionstring == null){
			emotionstring = new EmotionString(chatmessage.mMessageContent);
			EmotionStringRef.getInstacne().put(chatmessage.mMessageContent, emotionstring);
		}

		holder.mMessage_TextView.setText(emotionstring);
		holder.update(chatmessage.mMessageState, null);
		holder.mMessage_TextView.setOnLongClickListener(new OnLongClickListener() {
			public boolean onLongClick(View v) {
				callback.onItemLongClick(chatmessage);
				return false;
			}
		});
	}

	@Override
	public View getFacadeView(ChatItemHolder holder) {
		return holder.mMessage_TextMessage_LinearLayout;
	}

}
