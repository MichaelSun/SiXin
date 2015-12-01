package com.renren.mobile.chat.ui.contact.mutichat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.renren.mobile.chat.ui.BaseActivity;

/**
 * @author xiangchao.fan
 * 设置群聊名称Activity
 */
public class MultiChatNameSettingActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		long groupId = getIntent().getLongExtra("groupId", 0);
		MultiChatNameSettingScreen multiChatNameSettingScreen = new MultiChatNameSettingScreen(this, groupId);
		setContentView(multiChatNameSettingScreen.getScreenView());
	}
	
	@Override
	protected void onResume() {
		super.onResume();
	}
	
	public static void show(Context context, long groupId){
		Intent intent = new Intent(context, MultiChatNameSettingActivity.class);
		intent.putExtra("groupId", groupId);
		context.startActivity(intent);
	}
}
