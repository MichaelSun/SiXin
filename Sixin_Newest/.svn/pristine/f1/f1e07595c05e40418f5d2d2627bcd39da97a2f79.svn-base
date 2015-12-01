package view.test;

import com.renren.mobile.chat.R;
import com.renren.mobile.chat.base.util.SystemUtil;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

public class TestFrameLayout extends LinearLayout {

	public TestFrameLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		// TODO Auto-generated method stub
		super.onLayout(changed, left, top, right, bottom);
		SystemUtil.log("framelayout", "frame = "+left+","+right);
		
	}
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		View v = this.findViewById(R.id.cdw_chat_listview_item_loading);
		if(v!=null){
			SystemUtil.log("tt1", "----->"+v.getMeasuredWidth());
		}
	}
	@Override
	protected void measureChildWithMargins(View child,
			int parentWidthMeasureSpec, int widthUsed,
			int parentHeightMeasureSpec, int heightUsed) {
		// TODO Auto-generated method stub
//		SystemUtil.log("tt1", "measureChildWithMargins="+MeasureSpec.getSize(parentWidthMeasureSpec));
		 final MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
//		 final int childWidthMeasureSpec = getChildMeasureSpec(parentWidthMeasureSpec,
//	                this.getPaddingLeft() + this.getPaddingRight()  + lp.leftMargin + lp.rightMargin
//	                        + widthUsed, lp.width);
////		SystemUtil.log("tt1", "measureChildWithMargins="+MeasureSpec.getSize(parentWidthMeasureSpec));
		 SystemUtil.log("tt1", lp.width+","+ this.getPaddingLeft()+","+this.getPaddingRight()+","+widthUsed+","+lp.leftMargin+","+lp.rightMargin);
//		child.measure(parentWidthMeasureSpec, parentHeightMeasureSpec);
		super.measureChildWithMargins(child, parentWidthMeasureSpec, widthUsed, parentHeightMeasureSpec, heightUsed);
	}
	
	
	
}
