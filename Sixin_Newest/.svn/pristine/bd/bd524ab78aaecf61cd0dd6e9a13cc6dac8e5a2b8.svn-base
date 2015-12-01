package com.renren.mobile.chat.model.facade;

import android.view.View;

import com.common.manager.MessageManager.OnSendTextListener.SEND_TEXT_STATE;
import com.renren.mobile.chat.base.inter.Playable.PLAY_STATE;
import com.renren.mobile.chat.base.inter.Subject.COMMAND;
import com.renren.mobile.chat.base.model.ChatBaseItem;
import com.renren.mobile.chat.holder.ChatItemHolder;
import com.renren.mobile.chat.model.warpper.ChatMessageWarpper;
import com.renren.mobile.chat.model.warpper.ChatMessageWarpper_Voice;
import com.renren.mobile.chat.util.BaseChatListAdapter.OnItemLongClickCallback;
import com.renren.mobile.chat.util.VoiceOnClickListenner;


/**
 * @author dingwei.chen
 * @说明 文本条目装饰器
 * */
public class ChatItemFacader_Voice extends ChatItemFacader{

	@Override
	public void facade(ChatItemHolder holder, ChatMessageWarpper chatmessage,OnItemLongClickCallback callback) {
		this.process(holder, (ChatMessageWarpper_Voice)chatmessage);
	}
	private void process(ChatItemHolder holder,final ChatMessageWarpper_Voice chatmessage){
		
		VoiceOnClickListenner click =new VoiceOnClickListenner(chatmessage);
		holder.mMessage_Content_ViewGroup.setOnClickListener(click);
		
		holder.update(chatmessage.mMessageState, chatmessage.createResetData());
		if(chatmessage.mComefrom == ChatBaseItem.MESSAGE_COMEFROM.OUT_TO_LOCAL){
			holder.mMessage_Error_ImageView.setOnClickListener(click);
		}
		if(chatmessage.mPlayState == PLAY_STATE.STOP){//不为播放状态
			holder.updateVoice(chatmessage.mMessageState, chatmessage.createResetData());
		}else if(chatmessage.mPlayState == PLAY_STATE.PLAYING){//播放状态
			chatmessage.reflash();
		}
		if(chatmessage.mComefrom == ChatBaseItem.MESSAGE_COMEFROM.OUT_TO_LOCAL){
			if(chatmessage.mMessageState == COMMAND.COMMAND_DOWNLOAD_VOICE_OVER
			&& chatmessage.mSendTextState== SEND_TEXT_STATE.SEND_OVER){
				holder.mMessage_Voice_Unlisten_ImageView.setVisibility(View.VISIBLE);
			}else{
				holder.mMessage_Voice_Unlisten_ImageView.setVisibility(View.GONE);
			}
		}
		holder.mMessage_VoiceMessage_LinearLayout.setTime(holder,chatmessage.mPlayTime,!chatmessage.hasNewsFeed());//是否自适应
	}

	@Override
	public View getFacadeView(ChatItemHolder holder) {
		// TODO Auto-generated method stub
		return holder.mMessage_VoiceMessage_LinearLayout;
	}
}
