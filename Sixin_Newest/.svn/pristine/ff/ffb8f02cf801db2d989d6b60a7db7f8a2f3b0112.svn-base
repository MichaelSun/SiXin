package view.test;

import com.renren.mobile.chat.base.util.SystemUtil;

import android.content.Context;
import android.graphics.Canvas;
import android.view.View;
import android.widget.ImageView;

public class TestImageView extends ImageView {

	public TestImageView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	@Override
	public void draw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.draw(canvas);
		SystemUtil.log("testimage", "imagedraw");
	}

	@Override
	protected void dispatchDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		SystemUtil.log("testimage", "dispatchDraw");
		super.dispatchDraw(canvas);
	}
	
	
	@Override
	public void setVisibility(int visibility) {
		// TODO Auto-generated method stub
		if(visibility!=View.VISIBLE){
			SystemUtil.log("log", "set gone ");
		}else{
			SystemUtil.log("log", "set visible ");
		}
		super.setVisibility(visibility);
	}
}
