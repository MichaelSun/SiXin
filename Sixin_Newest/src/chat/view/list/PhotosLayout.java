package view.list;

import com.renren.mobile.chat.R;
import com.renren.mobile.chat.base.util.SystemUtil;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class PhotosLayout extends LinearLayout{

	View mPhoto1;
	View mPhoto2;
	
	public PhotosLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	@Override
	protected void onFinishInflate() {
		mPhoto1 = this.findViewById(R.id.video_group);
		mPhoto2 = this.findViewById(R.id.photo2);
	};
	

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int width = getMeasuredWidth();
		SystemUtil.log("layoutphoto", "get measure = "+MeasureSpec.getSize(heightMeasureSpec));
		this.setMeasuredDimension(width,( width>>1));
		width = ((width-2)>>1);
		mPhoto1.measure(MeasureSpec.EXACTLY|width, MeasureSpec.EXACTLY|width);
		mPhoto2.measure(MeasureSpec.EXACTLY|width, MeasureSpec.EXACTLY|width);
	}
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		SystemUtil.log("layoutphoto", "layout width = "+this.getWidth());
		mPhoto1.layout(0, 0, mPhoto1.getMeasuredWidth(), mPhoto1.getMeasuredHeight());
		mPhoto2.layout(r-mPhoto2.getMeasuredWidth(), t, r, t+mPhoto2.getMeasuredHeight());
	}
}
