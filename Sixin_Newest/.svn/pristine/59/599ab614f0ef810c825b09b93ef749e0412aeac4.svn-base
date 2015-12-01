package com.renren.mobile.chat.ui;

import com.core.util.CommonUtil;
import com.renren.mobile.chat.base.util.SystemUtil;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class BaseFragment extends Fragment{
	
	public BaseFragment(){
	}
	
	protected BaseScreen mScreen;
	public boolean mIsFromThird = false;
	/**
	 * 返回Screen
	 */
	public BaseScreen getScreen() {
		return mScreen;
	}
	
	public void show(){
		if(mScreen!=null){
			mScreen.onShow();
		}
	}

	public void setIsFromThird(boolean isFromThird){
		mIsFromThird = isFromThird;
		if(mScreen!=null){
			mScreen.setIsFromThird(isFromThird);
		}
	}
	
	/**
	 * @author liuchao
	 * 实现自己的onResume方法
	 */
	public void onViewResume(){
		if(mScreen != null){
			mScreen.onResume();
		}
	}
	/**
	 * @author liuchao
	 * 实现自己的onPause方法
	 */
	public void onViewPause() {
		if(mScreen != null){
			mScreen.onPause();
		}
	}

	@Override
	public void onResume() {
		onViewResume();
		super.onResume();
	}
	
	@Override
	protected void finalize() throws Throwable {
		CommonUtil.log("final", "finalize:" + getClass().getSimpleName());
		super.finalize();
	}
}
