package com.renren.mobile.chat.base.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.Button;


/**
 * @author dingwei.chen
 * @说明 可移动button
 * */
public class MoveButton extends Button{

	static AnimationSet sLeftDissmissAnim = null;
	static AnimationSet sRightDissmissAnim = null;
	static AnimationSet sLeftShowAnim = null;
	static AnimationSet sRightShowAnim = null;
	static{
		sLeftDissmissAnim = new AnimationSet(false);
		Animation left_tran = new TranslateAnimation(0f, -50f, 0, 0);
		sLeftDissmissAnim.addAnimation(left_tran);
		Animation alpha_to_dissmiss = new AlphaAnimation(1f,0);
		Animation alpha_to_show = new AlphaAnimation(0f,1);
		sLeftDissmissAnim.addAnimation(alpha_to_dissmiss);
		sLeftDissmissAnim.setDuration(200);
		
		sLeftShowAnim = new AnimationSet(false);
		left_tran = new TranslateAnimation(50f, 0f, 0, 0);
		sLeftShowAnim.addAnimation(left_tran);
		sLeftShowAnim.addAnimation(alpha_to_show);
		sLeftShowAnim.setDuration(200);
		
		sRightShowAnim = new AnimationSet(false);
		Animation right_tran = new TranslateAnimation(-50f, 0f, 0, 0);
		sRightShowAnim.addAnimation(right_tran);
		sRightShowAnim.addAnimation(alpha_to_show);
		sRightShowAnim.setDuration(200);
		
		sRightDissmissAnim = new AnimationSet(false);
		right_tran = new TranslateAnimation(0f, 50f, 0, 0);
		sRightDissmissAnim.addAnimation(right_tran);
		sRightDissmissAnim.addAnimation(alpha_to_dissmiss);
	}
	
	
	
	public MoveButton(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public enum DIRECTION{
		LEFT,
		RIGHT
	}
	
	/**
	 * @param dir 代表的是控件移动朝向
	 * */
	public void moveToDissmiss(DIRECTION dir){
		switch(dir){
		case LEFT:
			this.startAnimation(sLeftDissmissAnim);
			;break;
		case RIGHT:
			this.startAnimation(sRightDissmissAnim);
			;break;
		}
		this.setVisibility(View.GONE);
	}
	/**
	 * @param dir 代表的是控件移动朝向
	 * */
	public void moveToShow(DIRECTION dir){
		switch(dir){
		case LEFT:
			this.startAnimation(sLeftShowAnim);
			;break;
		case RIGHT:
			this.startAnimation(sRightShowAnim);
			;break;
		}
		this.setVisibility(View.VISIBLE);
	}
	
}
