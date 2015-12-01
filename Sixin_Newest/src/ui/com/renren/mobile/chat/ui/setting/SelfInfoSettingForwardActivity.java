package com.renren.mobile.chat.ui.setting;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.renren.mobile.chat.ui.BaseActivity;
import com.renren.mobile.chat.ui.BaseScreen;

/**
 * @author xiangchao.fan
 * @description 从个人设置页面跳转到子页面
 */
public class SelfInfoSettingForwardActivity extends BaseActivity{

	public static final String SELF_FORWARD_SCREEN_TYPE = "SelfForwardScreenType";
	
	public static interface SelfForwardScreenType{
		public static final int CHANGE_PWD_SCREEN = 0;
		public static final int SELECT_COLLEGE_SCREEN = 1;
	}
	
	private int mSelfForwardScreenType;
	
	private BaseScreen screen;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mSelfForwardScreenType = getIntent().getIntExtra(SELF_FORWARD_SCREEN_TYPE, -1);
		
		switch(mSelfForwardScreenType){
		case SelfForwardScreenType.CHANGE_PWD_SCREEN:
			screen = new ChangePwdScreen(this);
			this.setContentView(screen.getScreenView());
			break;
		case SelfForwardScreenType.SELECT_COLLEGE_SCREEN:
			screen = new SelectCollegeScreen(this);
			this.setContentView(screen.getScreenView());
			break;
		}
		
	}
	
	public static void show(Context context, int screenType){
		Intent intent = new Intent(context, SelfInfoSettingForwardActivity.class);
		intent.putExtra(SELF_FORWARD_SCREEN_TYPE, screenType);
		context.startActivity(intent);
	}
	
	/** startActivityForResult */
	public static void show(Context context, int screenType, int requestCode){
		Intent intent = new Intent(context, SelfInfoSettingForwardActivity.class);
		intent.putExtra(SELF_FORWARD_SCREEN_TYPE, screenType);
		((Activity) context).startActivityForResult(intent, requestCode);
	}
	
}
