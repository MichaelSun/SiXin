package com.common.emotion.view;

import com.common.R;
import com.core.util.CommonUtil;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ViewFlipper;

/**
 * 实现纵向滑动的ViewFlipper
 * 
 * @author zhenning.yang
 * 
 */
public class EmotionViewFlipper extends ViewFlipper implements
		ISuperOnFling2ContentChangeListener {

	public String tag = "flipper";
	/**
	 * flipper是否支持循环
	 */
	public static boolean isLoop = false;
	public EmotionViewFlipper(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public EmotionViewFlipper(Context context) {
		super(context);
	}

	private ISuperOnFling2ContentChangeListener mListener;
	/**
	 * 设置纵向的导航条
	 * @param listener
	 */
	public void setOnIndicatorListener(ISuperOnFling2ContentChangeListener listener) {
		mListener = listener;
	}

	@Override
	public void onShowNextPointer() {
		setInAnimation(getContext(), R.anim.trans_in);
		setOutAnimation(getContext(), R.anim.trans_out);
		if(isLoop){
			showPrevious();
			mListener.onShowPreviousPointer();
		}else{
			if(getDisplayedChild()>0){
				showPrevious();
				mListener.onShowPreviousPointer();
			}else{
				CommonUtil.log(tag,"flipper can not loop");
			}
				
		}
	}

	@Override
	public void onShowPreviousPointer() {
		setInAnimation(getContext(), R.anim.trans_in_up);
		setOutAnimation(getContext(), R.anim.trans_out_up);
		if(isLoop){
			showNext();
			mListener.onShowNextPointer();
		}else{
			if(getDisplayedChild()<getChildCount()-1){
				showNext();
				mListener.onShowNextPointer();
			}else{
				CommonUtil.log(tag,"flipper can not loop");
			}
		}
	}

	@Override
	public void onShowPointer(int index) {
		// TODO Auto-generated method stub
		setInAnimation(getContext(), android.R.anim.fade_in);
		setOutAnimation(getContext(), android.R.anim.fade_out);
		setDisplayedChild(index);
		mListener.onShowPointer(index);
	}
}
