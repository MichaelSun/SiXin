package com.renren.mobile.chat.ui.setting;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.common.mcs.HttpProviderWrapper;
import com.common.mcs.INetRequest;
import com.common.mcs.INetResponse;
import com.common.mcs.McsServiceProvider;
import com.common.utils.LanguageSettingUtil;
import com.common.utils.RRSharedPreferences;
import com.core.json.JsonObject;
import com.core.json.JsonValue;
import com.renren.mobile.chat.R;
import com.renren.mobile.chat.RenrenChatApplication;
import com.renren.mobile.chat.common.ResponseError;
import com.renren.mobile.chat.ui.BaseActivity;
import com.renren.mobile.chat.ui.notification.FeedNotificationManager;
import com.renren.mobile.chat.ui.notification.MessageNotificationManager;
public class AboutChatActivity extends BaseActivity {
	private AboutChatScreen boutChatScreen;
	private View myView;
	private TextView version_textView;
	private Button version_button;
	private Context context;
	private ProgressDialog dialog;
	private INetResponse response;
	private RRSharedPreferences rRSharedPreferences;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE); 
		boutChatScreen = new AboutChatScreen(this);
		myView = boutChatScreen.getScreenView();
		setContentView(myView);
		rRSharedPreferences = new RRSharedPreferences(AboutChatActivity.this);
		RenrenChatApplication.pushStack(this);
		version_textView = (TextView)myView.findViewById(R.id.version_textView);
		version_button = (Button)myView.findViewById(R.id.version_button);
		String versionName = RenrenChatApplication.versionName;
		version_textView.setText(RenrenChatApplication.getmContext().getResources().getString(R.string.AboutChatActivity_java_1) + versionName);		//AboutChatActivity_java_1=版本号：; 
		context = AboutChatActivity.this;
		updateInfo();
		version_button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.show();
				McsServiceProvider.getProvider().getUpdateInformation(
						response,
						0,
						2,
						Integer.parseInt(RenrenChatApplication.FROM),
						Integer.parseInt(RenrenChatApplication.PUBLIC_DATE),
						RenrenChatApplication.VERSION,
						LanguageSettingUtil.getInstance().getLanguageStr(
								LanguageSettingUtil.getInstance()
										.getLanguageType()));// .getUpdateInfo(0,
																// response, "",
																// false);
			}
		});
	}
	private void updateInfo() {
		dialog = new ProgressDialog(context);
		dialog.setMessage(RenrenChatApplication.getmContext().getResources().getString(R.string.AboutChatActivity_java_2));		//AboutChatActivity_java_2=正在检查更新; 
		dialog.setOnCancelListener(new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				HttpProviderWrapper.getInstance().stop();
			}
		});
		response = new INetResponse() {
			@Override
			public void response(final INetRequest req, final JsonValue obj) {
				if (obj instanceof JsonObject) {
					AboutChatActivity.this.runOnUiThread(new Runnable() {
						public void run() {
							dialog.dismiss();
							JsonObject map = (JsonObject) obj;
							if (ResponseError.noError(req, map)) {
								int type = (int) map.getNum("type");
								String info = map.getString("info");
								final String url = map.getString("url");
								String leftKey = RenrenChatApplication.getmContext().getResources().getString(R.string.VoiceOnClickListenner_java_3);		//VoiceOnClickListenner_java_3=确定; 
								String rightKey = RenrenChatApplication.getmContext().getResources().getString(R.string.ChatMessageWarpper_FlashEmotion_java_4);		//ChatMessageWarpper_FlashEmotion_java_4=取消; 
								String title = "";
								JsonObject object = map.getJsonObject("configInfo");
								if (object != null) {
									leftKey = object.getString("leftKey");
									if (leftKey == null || leftKey.equals("")) {
										leftKey = RenrenChatApplication.getmContext().getResources().getString(R.string.VoiceOnClickListenner_java_3);		//VoiceOnClickListenner_java_3=确定; 
									}
									rightKey = object.getString("rightKey");
									if (rightKey == null || rightKey.equals("")) {
										rightKey = RenrenChatApplication.getmContext().getResources().getString(R.string.ChatMessageWarpper_FlashEmotion_java_4);		//ChatMessageWarpper_FlashEmotion_java_4=取消; 
									}
									title = object.getString("title");
								}
								switch (type) {
								case 0: 
									Toast.makeText(context, info, 1).show();
									break;
								case 1: 
									mustUpdate(info,url,leftKey,title);
									break;
								case 2: 
									selectUpdate(info,url,leftKey, rightKey,title);
									break;
								}
							}
						}
					});
				}
			}
		};
	}
	private void mustUpdate(String info, final String url,
			String leftKey, String title) {
		rRSharedPreferences.putBooleanValue("update_must", true);
		rRSharedPreferences.putStringValue("version_update", RenrenChatApplication.versionName);
		rRSharedPreferences.putStringValue("title", title);
		rRSharedPreferences.putStringValue("info", info);
		rRSharedPreferences.putStringValue("leftKey", leftKey);
		rRSharedPreferences.putStringValue("url", url);
		RenrenChatApplication.currentIndex = 0;
		RenrenChatApplication.feedIndex = 1;
		FeedNotificationManager.getInstance().clearAllNotification();
		FeedNotificationManager.getInstance().clearChatFeedList();
		MessageNotificationManager.getInstance().getMessageNotificationModel().clearUnReadMessageList();
		MessageNotificationManager.getInstance().clearAllNotification(context);
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
						MessageNotificationManager.getInstance().clearAllNotification(context);
						FeedNotificationManager.getInstance().clearAllNotification();
						MessageNotificationManager.getInstance().getMessageNotificationModel().clearUnReadMessageList();
						RenrenChatApplication.clearStack();
				}
			});
	}
	private void selectUpdate(String info, final String url, String leftKey,
			String rightKey, String title) {
		rRSharedPreferences.putBooleanValue("update_select", true);
		AlertDialog dialog = new AlertDialog.Builder(context).setTitle(title).setMessage(info).setPositiveButton(leftKey, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (url != null && !url.equals("")) {
					Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
					context.startActivity(intent);
				}
			}
		}).setNegativeButton(rightKey, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
			}
		}).show();
		dialog.setOnCancelListener(new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
			}
		});
	}
	@Override
	protected void onDestroy() {
		RenrenChatApplication.popStack(this);
		super.onDestroy();
	}
}
