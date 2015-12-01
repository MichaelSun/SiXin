package com.renren.mobile.chat.base.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

/**
 * @author dingwei.chen
 * */
public class AnimationLinearLayout extends LinearLayout{

	public AnimationLinearLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	public void resize(int height){
		android.view.ViewGroup.LayoutParams params = this.getLayoutParams();
		if(params!=null){
			params.height = height;
			this.setLayoutParams(params);
		}
		
	}
	public void endResize(int height){
		this.setVisibility(View.VISIBLE);
		this.resize(height);
	}
	public int getMHeight(){
		this.measure(android.view.ViewGroup.LayoutParams.WRAP_CONTENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		return this.getMeasuredHeight();
	}
	
	
}
