package com.renren.mobile.chat.ui.account;

import com.renren.mobile.chat.ui.BaseActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class BindRenrenAccountActivity extends BaseActivity{

	public static int BIND_STATUS = -1;
	public static final int BIND_CONTANT = 0;
	public static final int BIND_SETTING = 1;
	public static final int BIND_FEED = 2;
	public static final int BIND_PLUGIN = 3;
	public static final int BIND_PHOTO = 4;
	public static final String COME_FROM = "COME_FROM";
	private BindRenrenAccountScreen mBindRenrenAccountScreen;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mBindRenrenAccountScreen = new BindRenrenAccountScreen(this);
		setContentView(mBindRenrenAccountScreen.getScreenView());
		initIntent(getIntent());
	}
	
	private void initIntent(Intent intent){
		if(intent != null){
			BIND_STATUS = intent.getIntExtra(COME_FROM, -1);
		}
	}

}
