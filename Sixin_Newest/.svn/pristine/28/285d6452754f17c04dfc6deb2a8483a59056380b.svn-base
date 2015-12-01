package com.renren.mobile.chat.ui.contact;

import android.content.Context;
import android.graphics.Canvas;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ListView;

import com.core.util.SystemService;


/**
 * @author dingwei.chen1988@gmail.com
 * */
public final class C_ContactsListView extends ListView{

	//private String mHeaderAlpheText = null;
	//private C_AlpheBarView mBarView = null;
//	public static final Paint PAINT = new Paint();
//	static{
//		PAINT.setAntiAlias(true);//抗锯齿
//		PAINT.setAlpha(100);
//	}
	
	private boolean mIsDrawAlpheBar=true;
	
	public C_ContactsListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.setVerticalFadingEdgeEnabled(false);
//		mBarView = new C_AlpheBarView(getContext());
//		mBarView.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
//		mBarView.setVisibility(View.VISIBLE);
	}
	
	@Override
	public void draw(Canvas canvas) {
		super.draw(canvas);
		//Logd.log("mIsDrawAlpheBar="+mIsDrawAlpheBar+"#getChildCount()="+getChildCount());
		if(mIsDrawAlpheBar&&this.getChildCount()>1){
			canvas.save();
			this.drawAlpheBar(canvas);
			canvas.restore();
		}
	};
	
	private final void drawAlpheBar(Canvas canvas){
		View firstView = this.getChildAt(0);
		View nextView = this.getChildAt(1);
		int nextTop = nextView.getTop();
		if(firstView.getTag()!=null ){ 
			C_ContactsItemHolder holder = (C_ContactsItemHolder)firstView.getTag();
			if(!TextUtils.isEmpty(holder.mAlpheBarText.getText())){
				C_ContactsItemHolder nextholder = (C_ContactsItemHolder)nextView.getTag();
				if(nextholder.mAlpheBar.getVisibility()!=View.GONE){//推送代码
					int barheight = holder.mShadowAlpheBar.getHeight();
					if(barheight > nextTop){
						int translateY = nextTop-barheight;
						double f = ((translateY+0.0)/barheight);
						holder.mShadowAlpheBar.setAlpha((int)(255*f));
						canvas.translate(0, nextTop-barheight);
					}
				}
				//Logd.error("firstView="+holder.mAlpheBarText.getText()+"#name="+holder.mUserNameView.getText());
				holder.mShadowAlpheBar.draw(canvas);
				holder.mShadowAlpheBar.setAlpha(255);
			}
		}
	}
	
	public final boolean onTouchEvent(MotionEvent ev) {
		if(ev.getAction()==MotionEvent.ACTION_DOWN){
			if(getWindowToken()!=null){
				SystemService.sInputMethodManager
					.hideSoftInputFromWindow(getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
			}
		}
		return super.onTouchEvent(ev);
	}
	
	/**
	 * 是否显示最上面的字母条，搜索时候是
	 * @param draw
	 */
	public final void setDrawAlpheBar(boolean draw){
		mIsDrawAlpheBar=draw;
		//Logd.error("draw draw="+draw);
		//Logd.traces();
	}
	
	
}
