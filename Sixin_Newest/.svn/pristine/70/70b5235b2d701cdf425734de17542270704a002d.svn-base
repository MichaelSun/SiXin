package com.renren.mobile.chat.views;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import com.renren.mobile.chat.model.warpper.ChatMessageWarpper;
import com.renren.mobile.chat.R;
import com.renren.mobile.chat.RenrenChatApplication;
public class ResendDialog {
	AlertDialog mDialog = null;
	ChatMessageWarpper mMessage = null;
	public ResendDialog(Context context) {
		Builder builder =new  AlertDialog.Builder(context);
		builder.setTitle(RenrenChatApplication.getmContext().getResources().getString(R.string.ResendDialog_java_1));		//ResendDialog_java_1=重新发送消息; 
		builder.setPositiveButton(RenrenChatApplication.getmContext().getResources().getString(R.string.VoiceOnClickListenner_java_3), new OnClickListener() {		//VoiceOnClickListenner_java_3=确定; 
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if(mMessage!=null){
					mMessage.resend();
				}
				mDialog.dismiss();
			}
		});
		builder.setNegativeButton(RenrenChatApplication.getmContext().getResources().getString(R.string.ChatMessageWarpper_FlashEmotion_java_4), new OnClickListener() {		//ChatMessageWarpper_FlashEmotion_java_4=取消; 
			@Override
			public void onClick(DialogInterface dialog, int which) {
				mDialog.dismiss();
			}
		});
		mDialog = builder.create();
	}
	public void update(ChatMessageWarpper message){
		this.mMessage = message;
		mDialog.show();
	}
}
