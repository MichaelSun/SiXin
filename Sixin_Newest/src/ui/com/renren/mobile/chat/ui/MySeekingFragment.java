package com.renren.mobile.chat.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.renren.mobile.chat.RenrenChatApplication;


import com.renren.mobile.chat.ui.setting.F_SettingScreen;
import com.renren.mobile.chat.ui.setting.SettingDataManager;
import com.renren.mobile.chat.ui.setting.SettingDataManager.PhotoUploadSuccessListener;

public class MySeekingFragment extends BaseFragment {
	public MySeekingFragment() {
		super();
	}
//	private FindFriendScreen mScreen;
//
//	public FindFriendScreen getScreen() {
//		return mScreen;
//	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (mScreen == null) {
			mScreen = new F_SettingScreen(getActivity());
			mScreen.setIsFromThird(this.mIsFromThird);
			((F_SettingScreen)mScreen).onShow();
		} else {
			((ViewGroup) mScreen.getScreenView().getParent()).removeView(mScreen.getScreenView());
		}
		setRequestCount();//刚登陆客户端时 FindFriendScreen很有可能 还没创建，所以创建好后 手动调一次更新
		return mScreen.getScreenView();
	}
	public void setRequestCount(){
//		if(mScreen != null){
//			((FindFriendScreen)mScreen).setRequestCount(RenrenChatApplication.mNewFriendsRequestCount); 
//		}
	}
	
	public void setRefresh(){
//		if(mScreen != null){
//			((FindFriendScreen)mScreen).setRefresh();
//		}
	}
	
	@Override
	public void onDestroy() {
		((F_SettingScreen)mScreen).unRegisterPhoteUploadSuccessListener();
		super.onDestroy();
	}
	
}
