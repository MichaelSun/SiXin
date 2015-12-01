package com.renren.mobile.chat.ui.guide;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.renren.mobile.chat.base.util.SystemUtil;

public class MeasureableView extends ImageView {
		
	Context mContext;
	
	public MeasureableView(Context context) {
		super(context);
	}
	
	public MeasureableView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		SystemUtil.logykn("mContext:" + mContext.toString());
	}
	
	public MeasureableView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		((WelcomeActivity) mContext).update();
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
	}
}
