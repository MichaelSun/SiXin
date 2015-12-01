package com.renren.mobile.chat.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.EditText;

import com.renren.mobile.chat.R;


public class SearchEditText extends EditText {
	private Listener listener;
	private BitmapDrawable bdDefault;//editText右边的image默认背景
	private BitmapDrawable bdHover;//editText右边的image按下时候的背景
	private Bitmap bmDefault;
	private Bitmap bmHover;
	private int backgroudDeafult;
	private int backgroudHover;
	Context context;
	
	public SearchEditText( Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		TypedArray params = context.obtainStyledAttributes(attrs,R.styleable.MyEditText);
		backgroudDeafult = params.getResourceId(R.styleable.MyEditText_img_background_default, 0);
		backgroudHover= params.getResourceId(R.styleable.MyEditText_img_background_hover, 0);
		bdDefault = (BitmapDrawable)getResources().getDrawable(backgroudDeafult);
		bmDefault=bdDefault.getBitmap();
		bdDefault.setBounds(0, 0, bmDefault.getWidth(),bmDefault.getHeight());
		bdHover = (BitmapDrawable)getResources().getDrawable(backgroudHover);
		bmHover=bdHover.getBitmap();
		bdHover.setBounds(0, 0, bmHover.getWidth(),bmHover.getHeight());
//		this.setHint("请输入搜索内容");
		this.setHintTextColor(Color.GRAY);
//		this.setCompoundDrawables(null, null, bdDefault, null);
		params.recycle();
		

		this.setListener(new Listener() {
			public void click() {
				// TODO Auto-generated method stub
				setText("");
				removeIcon();
			}
		});
	}
	public void showIcon(){
		this.setCompoundDrawables(null, null, bdDefault, null);
	}
	public void removeIcon(){
		this.setCompoundDrawables(null, null, null, null);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
	
	public void setListener(Listener l){
		listener = l;
	}
	
	
	
	
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
//		float x1 = event.getRawX();// 获得相对于屏幕中的位置
//		float y1 = event.getRawY();

		float x = event.getX();// 获得相对于父view的位置
//		float y = event.getY();
		
		float x2 = this.getWidth() - 40;
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			if (x > x2 && x < this.getWidth()) {
				if(listener != null)
					listener.click();
				return true;
			}
			else{
				return super.onTouchEvent(event);
			}
		case MotionEvent.ACTION_UP:
			if (x > x2 && x < this.getWidth()) {
				return true;
			}else{
				return super.onTouchEvent(event);
			}
		}
		return true;
	}
}
