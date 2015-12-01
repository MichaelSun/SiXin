package com.renren.mobile.chat.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.renren.mobile.chat.base.util.SystemUtil;
import com.renren.mobile.chat.ui.contact.C_ContactScreen;

public class MyContactFragment extends BaseFragment {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (mScreen == null) {
			mScreen = new C_ContactScreen(getActivity());
			mScreen.setIsFromThird(this.mIsFromThird);
			mScreen.onShow();
		} else {
			((ViewGroup) mScreen.getScreenView().getParent()).removeView(mScreen.getScreenView());
		}
		return mScreen.getScreenView();
	}

	@Override
	public void onDestroy() {
		if(mScreen!=null){
			((C_ContactScreen)mScreen).clearCache();
		}
		
		super.onDestroy();
	}
	
	public void onPageScrollStateChanged(){
		if(mScreen != null){
			((C_ContactScreen)mScreen).onPageScrollStateChanged();
		}
	}
	
	public void onVisible(){
		if(mScreen != null){
			((C_ContactScreen)mScreen).onVisible();
		}
	}
}
