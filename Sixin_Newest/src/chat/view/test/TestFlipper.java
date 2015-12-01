package view.test;

import com.renren.mobile.chat.base.util.SystemUtil;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View.OnTouchListener;
import android.widget.ViewFlipper;

public class TestFlipper extends ViewFlipper {

	public TestFlipper(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		super.onLayout(changed, left, top, right, bottom);
		SystemUtil.log("flipper", "onLayout "+top+","+bottom);
		Rect r = new Rect();
		this.getHitRect(r);
		SystemUtil.log("flipper", "onLayout "+r);
//		System.out.println(r);
	};
	
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		SystemUtil.log("flipper", "dispatchTouchEvent");
		SystemUtil.log("flipper", "mOnTouchListener = "+mOnTouchListener);
		SystemUtil.log("flipper", ""+this);
		if(mOnTouchListener==null){
			
			return super.dispatchTouchEvent(ev);
		}
		return mOnTouchListener.onTouch(this, ev);
	}
	OnTouchListener mOnTouchListener = null;
	 public void setOnTouchListener1(OnTouchListener l) {
	        mOnTouchListener = l;
	    }
	
}
