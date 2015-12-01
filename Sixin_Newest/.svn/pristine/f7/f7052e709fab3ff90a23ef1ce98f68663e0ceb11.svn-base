package com.renren.mobile.chat.ui.setting;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.renren.mobile.chat.ui.BaseActivity;
import com.renren.mobile.chat.ui.BaseScreen;
import com.renren.mobile.chat.ui.account.BindRenrenAccountActivity;

/**
 * @author xiangchao.fan
 * @description 从设置页面跳转到子页面
 */
public class SettingForwardActivity extends BaseActivity{

	public static final String FORWARD_SCREEN_TYPE = "ForwardScreenType";
	
	public static interface ForwardScreenType {
		public static final int SELFINFO_SETTING_SCREEN = 0;
		public static final int SYSTEM_SETTING_SCREEN = 1;
		public static final int BIND_RENREN_ACCOUNT_SCREEN = 2;
		public static final int BLACKLIST_SCREEN = 3;
		public static final int PRIVATE_SETTING_SCREEN = 4;
	}
	
	private int mForwardScreenType;
	
	private BaseScreen screen;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mForwardScreenType = getIntent().getIntExtra(FORWARD_SCREEN_TYPE, -1);
		
		switch(mForwardScreenType){
		case ForwardScreenType.SELFINFO_SETTING_SCREEN:
			screen = new SelfInfoSettingScreen(this);
			setContentView(screen.getScreenView());
			break;
		case ForwardScreenType.SYSTEM_SETTING_SCREEN:
			screen = new SystemSettingScreen(this);
			this.setContentView(screen.getScreenView());
			break;
		case ForwardScreenType.BIND_RENREN_ACCOUNT_SCREEN:
			Intent intent = new Intent(SettingForwardActivity.this,BindRenrenAccountActivity.class);
			this.startActivity(intent);
			this.finish();
//			screen = new BindRenrenAccountScreen(this);
//			this.setContentView(screen.getScreenView());
			break;
		case ForwardScreenType.BLACKLIST_SCREEN:
			screen = new BlacklistScreen(this);
			this.setContentView(screen.getScreenView());
			break;
		case ForwardScreenType.PRIVATE_SETTING_SCREEN:
			screen = new PrivateSettingScreen(this);
			this.setContentView(screen.getScreenView());
			break;
		}
		
	}
	
	public static void show(Context context, int screenType) {
		Intent intent = new Intent(context, SettingForwardActivity.class);
		intent.putExtra(FORWARD_SCREEN_TYPE, screenType);
		context.startActivity(intent);
	}

	@Override
	protected void onDestroy() {
		if(screen instanceof SelfInfoSettingScreen){
			if(!SettingDataManager.getInstance().isSelfInfoHasUpload()){
				SettingDataManager.getInstance().toUploadSelfInfo();
			}
			((SelfInfoSettingScreen) screen).unRegisterPhoteUploadSuccessListener();
		}else if(screen instanceof PrivateSettingScreen){
			if(!SettingDataManager.getInstance().isPrivateInfoHasUpload()){
				SettingDataManager.getInstance().toUploadPrivateInfo();
			}
		}
		super.onDestroy();
	}
	
	@Override
	protected void onResume() {
		if(screen instanceof SelfInfoSettingScreen){
			//((SelfInfoSettingScreen) screen).updateUI_Photo();
		}
		if(screen instanceof BlacklistScreen){
			screen.refresh();
		}
		super.onStart();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(screen instanceof SelfInfoSettingScreen){
			//((SelfInfoSettingScreen) screen).mHeadPhoto.onActivityResult(requestCode, resultCode, data);
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
}
