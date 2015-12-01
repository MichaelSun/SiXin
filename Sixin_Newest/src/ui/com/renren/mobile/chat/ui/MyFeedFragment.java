package com.renren.mobile.chat.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.renren.mobile.chat.base.util.SystemUtil;
import com.renren.mobile.chat.ui.contact.feed.ChatFeedScreen;
import com.renren.mobile.chat.ui.plugins.PluginScreen;

public class MyFeedFragment extends BaseFragment {

	public MyFeedFragment() {
		super();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (mScreen == null) {
			mScreen = new PluginScreen(getActivity());
			mScreen.setIsFromThird(this.mIsFromThird);
		} else {
			((ViewGroup) mScreen.getScreenView().getParent()).removeView(mScreen.getScreenView());
		}
		return mScreen.getScreenView();
	}

	@Override
	public void onDestroy() {
		((PluginScreen)mScreen).clear();
		super.onDestroy();
	}
	
	public void onResume(){
		super.onResume();
		if(mScreen!=null){
//			mScreen.refresh();
			((PluginScreen)mScreen).setRequestCount();
		}
	}
	
	public void setRequestCount(){
		if(mScreen!=null){
			((PluginScreen)mScreen).setRequestCount();
		}
		 
	}
}
