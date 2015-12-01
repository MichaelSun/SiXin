package com.renren.mobile.chat.util;
import java.io.File;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import android.view.View.OnClickListener;
import com.common.manager.MessageManager.OnSendTextListener.SEND_TEXT_STATE;
import com.core.voice.PlayerThread;
import com.core.voice.PlayerThread.PlayRequest;
import com.renren.mobile.chat.base.GlobalValue;
import com.renren.mobile.chat.base.inter.Subject;
import com.renren.mobile.chat.base.model.ChatBaseItem;
import com.renren.mobile.chat.base.util.SystemUtil;
import com.renren.mobile.chat.model.warpper.ChatMessageWarpper_Voice;
/**
 * @author dingwei.chen
 * @说明 声音消息点击事件
 * */
import com.renren.mobile.chat.R;
import com.renren.mobile.chat.RenrenChatApplication;
public class VoiceOnClickListenner implements OnClickListener {
	ChatMessageWarpper_Voice mChatMessage = null;
	public VoiceOnClickListenner(ChatMessageWarpper_Voice chatmessage){
		mChatMessage = chatmessage;
	}
	@Override
	public void onClick(View v) {
			if(mChatMessage.mSendTextState ==SEND_TEXT_STATE.SEND_PREPARE 
					&& 
				mChatMessage.mComefrom == ChatBaseItem.MESSAGE_COMEFROM.OUT_TO_LOCAL){
				SystemUtil.toast(RenrenChatApplication.getmContext().getResources().getString(R.string.VoiceOnClickListenner_java_1));		//VoiceOnClickListenner_java_1=正在下载...; 
				VoiceDownloadThread.getInstance().stopToAutoPlay(mChatMessage);
				return;
			}
			File file = new File(mChatMessage.mMessageContent);
			if(		file.exists() 
					&& file.length()>0 ){
				final PlayRequest request = new PlayRequest();
				request.mAbsVoiceFileName = mChatMessage.mMessageContent;
				request.mPlayListenner = mChatMessage;
				if (mChatMessage.mMessageState!=Subject.COMMAND.COMMAND_PLAY_VOICE_OVER) {
					VoiceDownloadThread.getInstance().stopToAutoPlay(mChatMessage);
				}else{
					VoiceDownloadThread.getInstance().stopToAutoPlay();
				}
				int state = mChatMessage.mMessageState;
				if(PlayerThread.getInstance().forceToPlay(request) && state==Subject.COMMAND.COMMAND_DOWNLOAD_VOICE_OVER){
					PlayerThread.getInstance().onAddPlay(request);
				};
			}else{
				/*外部发过来的错误处理*/
				if(mChatMessage.mComefrom==ChatBaseItem.MESSAGE_COMEFROM.OUT_TO_LOCAL){
					if(GlobalValue.getInstance().getCurrentActivity()!=null
							&& mChatMessage.mMessageState==Subject.COMMAND.COMMAND_DOWNLOAD_VOICE_ERROR){
						AlertDialog.Builder builder = 
								new AlertDialog.Builder(GlobalValue.getInstance().getCurrentActivity());
						AlertDialog dialog = builder.setMessage(RenrenChatApplication.getmContext().getResources().getString(R.string.VoiceOnClickListenner_java_2))		//VoiceOnClickListenner_java_2=是否重新下载; 
							.setPositiveButton(RenrenChatApplication.getmContext().getResources().getString(R.string.VoiceOnClickListenner_java_3),new DialogInterface.OnClickListener(){		//VoiceOnClickListenner_java_3=确定; 
								public void onClick(DialogInterface dialog,
										int which) {
									mChatMessage.download(true);
								}
							}).setNegativeButton(RenrenChatApplication.getmContext().getResources().getString(R.string.ChatMessageWarpper_FlashEmotion_java_4), null).create();		//ChatMessageWarpper_FlashEmotion_java_4=取消; 
						dialog.show();
					}else{
						mChatMessage.download(true);
					}
				}else {
					SystemUtil.toast(RenrenChatApplication.getmContext().getResources().getString(R.string.VoiceOnClickListenner_java_4));		//VoiceOnClickListenner_java_4=本地音频文件丢失; 
				}
			}
	}
}
