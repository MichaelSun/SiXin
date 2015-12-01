package com.renren.mobile.chat.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.renren.mobile.chat.ui.chatsession.ChatSessionScreen;

public class MySessionFragment extends BaseFragment {

//	private ChatSessionScreen mScreen;

	public MySessionFragment() {
		super();
	}

	/**
	 * 返回Screen
	 */
//	public ChatSessionScreen getScreen() {
//		return mScreen;
//	}
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onStart() {
		super.onStart();
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (mScreen == null) {
			try {
				mScreen = new ChatSessionScreen(getActivity());
				mScreen.setIsFromThird(this.mIsFromThird);
				mScreen.onShow();
			} catch (Exception e) {
			}
			
		} else {
			((ViewGroup) mScreen.getScreenView().getParent()).removeView(mScreen.getScreenView());
		}
		return mScreen.getScreenView();
	}

	@Override
	public void onDestroyView() {
		
		super.onDestroyView();
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		if(mScreen != null)
			mScreen.clearCache();
		super.onDestroy();
	}
	
	
	
}
