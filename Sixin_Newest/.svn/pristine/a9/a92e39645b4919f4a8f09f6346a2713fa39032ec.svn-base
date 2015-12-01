package com.renren.mobile.chat.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Window;

import com.renren.mobile.chat.RenrenChatApplication;
import com.renren.mobile.chat.ui.notification.FeedNotificationManager;
import com.renren.mobile.chat.ui.notification.MessageNotificationManager;

public class UpdateActivity extends BaseActivity {
	private Context context;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE); 
		context = UpdateActivity.this;
		RenrenChatApplication.pushStack(UpdateActivity.this);
		int flag = this.getIntent().getIntExtra(MainFragmentActivity.UPDATE_INFO, 0);
		switch (flag) {
		case MainFragmentActivity.MUST_UPDATE:
			RenrenChatApplication.currentIndex = 0;
			RenrenChatApplication.feedIndex = 1;
			
		
			String title = this.getIntent().getStringExtra("title");
			String info = this.getIntent().getStringExtra("info");
			String leftKey = this.getIntent().getStringExtra("leftKey");
			final String url = this.getIntent().getStringExtra("url");
			AlertDialog dialog = new AlertDialog.Builder(context).setTitle(title).setMessage(info).setPositiveButton(leftKey, new DialogInterface.OnClickListener() {
			
					@Override
					public void onClick(DialogInterface dialog, int which) {
						MessageNotificationManager.getInstance().clearAllNotification(context);
						FeedNotificationManager.getInstance().clearAllNotification();
						MessageNotificationManager.getInstance().getMessageNotificationModel().clearUnReadMessageList();
						if (url != null && !url.equals("")) {
							Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
							context.startActivity(intent);
						}
						RenrenChatApplication.clearStack();
					}
				}).show();
				
				dialog.setOnCancelListener(new OnCancelListener() {
					
					@Override
					public void onCancel(DialogInterface dialog) {
								// 退出程序
							MessageNotificationManager.getInstance().clearAllNotification(context);
							FeedNotificationManager.getInstance().clearAllNotification();
							MessageNotificationManager.getInstance().getMessageNotificationModel().clearUnReadMessageList();
							RenrenChatApplication.clearStack();
					}
				});
				FeedNotificationManager.getInstance().clearAllNotification();
				FeedNotificationManager.getInstance().clearChatFeedList();
				MessageNotificationManager.getInstance().getMessageNotificationModel().clearUnReadMessageList();
				MessageNotificationManager.getInstance().clearAllNotification(context);
				
			break;
		case MainFragmentActivity.SELECT_UPDATE:
			String title1 = this.getIntent().getStringExtra("title");
			String info1 = this.getIntent().getStringExtra("info");
			String leftKey1 = this.getIntent().getStringExtra("leftKey");
			String rightKey1 = this.getIntent().getStringExtra("rightKey");
			final String url_ = this.getIntent().getStringExtra("url");
			AlertDialog dialog1 = new AlertDialog.Builder(context).setTitle(title1).setMessage(info1).setPositiveButton(leftKey1, new DialogInterface.OnClickListener() {
			
							@Override
							public void onClick(DialogInterface dialog, int which) {

								if (url_ != null && !url_.equals("")) {
									Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url_));
									context.startActivity(intent);
								}
								finish();
							}
						}).setNegativeButton(rightKey1, new OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								finish();
							}
						}).show();
			
			dialog1.setOnCancelListener(new OnCancelListener() {
				
				@Override
				public void onCancel(DialogInterface dialog) {
					finish();
				}
			});
			break;

		default:
			break;
		}
	}
	@Override
	protected void onDestroy() {
		RenrenChatApplication.popStack(UpdateActivity.this);
		super.onDestroy();
	}
	
	

}
