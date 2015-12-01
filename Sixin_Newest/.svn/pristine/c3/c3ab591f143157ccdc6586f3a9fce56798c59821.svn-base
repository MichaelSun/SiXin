package view.test;

import com.renren.mobile.chat.base.util.SystemUtil;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ProgressBar;

public class TestProgressBar extends ProgressBar {

	public TestProgressBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	@Override
	protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		SystemUtil.log("tt1", "TestProgressBar onMeasure W = "+MeasureSpec.getSize(widthMeasureSpec));
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		SystemUtil.log("tt1", "TestProgressBar onMeasurewidth = "+this.getMeasuredWidth());
//		int size = MeasureSpec.getSize(widthMeasureSpec);
//		this.setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.getSize(heightMeasureSpec));
	};
	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		// TODO Auto-generated method stub
		super.onLayout(changed, left, top, right, bottom);
		SystemUtil.log("tt1", "getwidth = "+(right-left)+","+this.getMeasuredWidth());
	}
	

}
