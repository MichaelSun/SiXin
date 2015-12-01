package com.renren.mobile.chat.ui;

import android.app.Activity;
import android.content.ContentResolver;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;

import com.common.binder.LocalBinderPool;
import com.common.statistics.BackgroundUtils;
import com.common.statistics.Htf;
import com.core.util.SystemService;
import com.renren.mobile.chat.RenrenChatApplication;
import com.renren.mobile.chat.common.RRSharedPreferences;
import com.renren.mobile.chat.service.ChatService;

public class BaseActivity extends Activity {

	protected ContentResolver mContentResolver;
	protected RRSharedPreferences rRSharedPreferences;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ChatService.start();
		//?update by dingwei.chen start?//
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//? r:引起崩溃?//
		//?update by dingwei.chen end?//
		if(RenrenChatApplication.from == 0) {
			RenrenChatApplication.init();
		}
		setDensity();
		mContentResolver = getContentResolver();
		if(rRSharedPreferences==null){
			rRSharedPreferences = new RRSharedPreferences(this);	
		}
		RenrenChatApplication.pushStack(this);
	}

	@Override
	protected void onStart() {
		BackgroundUtils.getInstance().dealAppRunState(Htf.INITIATIVE_RUN, true);
		if(LocalBinderPool.getInstance().isContainBinder()){
			try {
				LocalBinderPool.getInstance().obtainBinder().changeAppGround(true);
			} catch (RemoteException ignore) {}
		}
		super.onStart();
	}



	@Override
	protected void onPause() {
		if(LocalBinderPool.getInstance().isContainBinder()){
			try {
				LocalBinderPool.getInstance().obtainBinder().changeAppGround(BackgroundUtils.getInstance().isAppOnForeground());
			} catch (RemoteException ignore) {}
		}
		super.onPause();
	}

	@Override
	protected void onStop() {
		BackgroundUtils.getInstance().dealAppRunState();
		super.onStop();
	}
	
	

	@Override
	protected void onDestroy() {
		RenrenChatApplication.popStack(this);
		super.onDestroy();
	}

	@Override
	protected void onUserLeaveHint() {
		rRSharedPreferences.putBooleanValue("home", true);
		super.onUserLeaveHint();
	}

	public void setDensity() {
		DisplayMetrics metric = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metric);
		RenrenChatApplication.density = metric.density; // 屏幕密度（0.75 / 1.0 /
														// 1.5）
		RenrenChatApplication.screenResolution = metric.widthPixels
				* metric.heightPixels;
		RenrenChatApplication.screen = "" + metric.widthPixels + "*"
				+ metric.heightPixels;

	}
	
	public void hideInputMethod(){
		SystemService.sInputMethodManager
		.hideSoftInputFromWindow(this.getWindow().getDecorView().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
	}
	
	public void showInputMethod(View view){
		SystemService.sInputMethodManager
		.showSoftInput(view, InputMethodManager.SHOW_FORCED);
	}
	
}
