package com.renren.mobile.chat.ui.setting;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.renren.mobile.chat.ui.BaseActivity;
import com.renren.mobile.chat.ui.BaseScreen;

/**
 * @author xiangchao.fan
 * @description 从系统设置页面跳转到子页面
 */
public class SystemSettingForwardActivity extends BaseActivity{

	public static final String SYSTEM_FORWARD_SCREEN_TYPE = "SystemForwardScreenType";
	
	public static interface SystemForwardScreenType{
		public static final int LANGUAGE_SETTING_SCREEN = 0;
	}
	
	private int mSystemForwardScreenType;
	
	private BaseScreen screen;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mSystemForwardScreenType = getIntent().getIntExtra(SYSTEM_FORWARD_SCREEN_TYPE, -1);
		
		switch(mSystemForwardScreenType){
		case SystemForwardScreenType.LANGUAGE_SETTING_SCREEN:
			screen = new LanguageSettingScreen(this);
			this.setContentView(screen.getScreenView());
			break;
		}
		
	}
	
	public static void show(Context context, int screenType){
		Intent intent = new Intent(context, SystemSettingForwardActivity.class);
		intent.putExtra(SYSTEM_FORWARD_SCREEN_TYPE, screenType);
		context.startActivity(intent);
	}
}
