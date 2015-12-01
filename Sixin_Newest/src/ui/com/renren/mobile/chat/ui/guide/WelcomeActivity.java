package com.renren.mobile.chat.ui.guide;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.LinearLayout;
import android.widget.ViewFlipper;

import com.core.util.ViewMapUtil;
import com.core.util.ViewMapping;
import com.renren.mobile.chat.R;
import com.renren.mobile.chat.RenrenChatApplication;
import com.renren.mobile.chat.ui.BaseActivity;
import com.renren.mobile.chat.ui.account.LoginSixinActivity;
import com.renren.mobile.chat.ui.account.RegisterAccountActivity;

/**
 * 欢迎界面，有三个点击事件
 * @author kaining.yang
 */
public class WelcomeActivity extends BaseActivity implements OnClickListener {
	
	private final static String IS_INTRODUCE = "is_introduce";
	
	public class ViewHolder {
		@ViewMapping(ID=R.id.ykn_welcome_layout)
		public LinearLayout mLayout;
		
		@ViewMapping(ID=R.id.ykn_welcome_flipper)
		public ViewFlipper mFlipper;
		
		@ViewMapping(ID=R.id.ykn_welcome_login_sixin)
		public LinearLayout mLoginSixin;
		
		@ViewMapping(ID=R.id.ykn_welcome_login_renren)
		public LinearLayout mLoginRenren;
		
		@ViewMapping(ID=R.id.ykn_welcome_login_register)
		public LinearLayout mRegister;
		
		@ViewMapping(ID=R.id.bottom)
		public LinearLayout mBottom;
		
		@ViewMapping(ID=R.id.image_view)
		public MeasureableView mImageView;
		
		@ViewMapping(ID=R.id.text01)
		public LinearLayout mText01;
		
		@ViewMapping(ID=R.id.text02)
		public LinearLayout mText02;
		
		@ViewMapping(ID=R.id.text03)
		public LinearLayout mText03;
	}
	
	private ViewHolder mViewHolder;
	
	private GestureDetector mGestureDetector;
	
	private int width;
	
	private int height;
	
	private boolean mFlag;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ykn_welcome_activity);
		RenrenChatApplication.mWelcomeActivity = this;
		mFlag = getIntent().getBooleanExtra(IS_INTRODUCE, false);
		mViewHolder = new ViewHolder();
		ViewMapUtil.getUtil().viewMapping(mViewHolder, this.getWindow().getDecorView());
		if (mFlag) {
			mViewHolder.mLoginRenren.setVisibility(View.GONE);
			mViewHolder.mBottom.setVisibility(View.GONE);
		}
		
		mViewHolder.mFlipper.startFlipping();
		mViewHolder.mLoginSixin.setOnClickListener(this);
		mViewHolder.mLoginRenren.setOnClickListener(this);
		mViewHolder.mRegister.setOnClickListener(this);
		mViewHolder.mLayout.setClickable(true);
		mViewHolder.mLayout.setOnTouchListener(new OnTouchListener() { 
            @Override 
            public boolean onTouch(View v, MotionEvent event) { 
            	//SystemUtil.logykn("touch event:" + event);
            	mGestureDetector.onTouchEvent(event);
                return false; 
            } 
        }); 
		
		mGestureDetector = new GestureDetector(this, new OnGestureListener() {
			
			@Override
			public boolean onSingleTapUp(MotionEvent e) {
				return false;
			}
			
			@Override
			public void onShowPress(MotionEvent e) {
				
			}
			
			@Override
			public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
					float distanceY) {
				return false;
			}
			
			@Override
			public void onLongPress(MotionEvent e) {
				
			}
			
			@Override
			public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
					float velocityY) {
				//用户按下屏幕，快速移动后松开（就是在屏幕上滑动）  
                //e1:第一个ACTION_DOWN事件（手指按下的那一点）  
                //e2:最后一个ACTION_MOVE事件 （手指松开的那一点）  
                //velocityX:手指在x轴移动的速度 单位：像素/秒  
                //velocityY:手指在y轴移动的速度 单位：像素/秒 
				
				//SystemUtil.logykn("MotionEvent e1:" + e1.getX());
				//SystemUtil.logykn("MotionEvent e2:" + e2.getX());
				//SystemUtil.logykn("velocityX:" + velocityX);
				//SystemUtil.logykn("velocityY:" + velocityY);

                int x = (int) (e2.getX() - e1.getX()); 
                //SystemUtil.logykn("x:" + x);
                if(x>0){ 
                	mViewHolder.mFlipper.showPrevious();                      
                }else{ 
                	mViewHolder.mFlipper.showNext();                  
                } 
                return true; 
			}
			
			@Override
			public boolean onDown(MotionEvent e) {
				//SystemUtil.logykn("touch event onDown:" + e);
				//用户轻触屏幕。（单击） 
				return true;
			}
		});
	}

	@Override
	public void onClick(View v) {
		
		Intent intent;
		
		switch (v.getId()) {
		case R.id.ykn_welcome_login_sixin:
			// 用私信账号登陆
			intent = new Intent(WelcomeActivity.this,
					LoginSixinActivity.class);
			intent.putExtra(LoginSixinActivity.LOGIN_TYPE, LoginSixinActivity.LOGIN_SIXIN);
			WelcomeActivity.this.startActivity(intent);
			WelcomeActivity.this.finish();
			break;
		case R.id.ykn_welcome_login_renren:
			// 用人人账号登陆
			intent = new Intent(WelcomeActivity.this,
					LoginSixinActivity.class);
			intent.putExtra(LoginSixinActivity.LOGIN_TYPE, LoginSixinActivity.LOGIN_RENREN);
			WelcomeActivity.this.startActivity(intent);
			WelcomeActivity.this.finish();
			break;
		case R.id.ykn_welcome_login_register:
			// 注册账号
			// mViewHolder.mFlipper.showNext();
			intent = new Intent(WelcomeActivity.this, RegisterAccountActivity.class);
			intent.putExtra(RegisterAccountActivity.FLAG_TYPE, RegisterAccountActivity.FLAG_REGISTER);
			WelcomeActivity.this.startActivity(intent);
			WelcomeActivity.this.finish();
			break;
		default:
			break;
		}
		
	}
	
	public static void show(Context context) {
		Intent intent = new Intent(context, WelcomeActivity.class);
		context.startActivity(intent);
	}
	
	public static void showIntroduce(Context context) {
		Intent intent = new Intent(context, WelcomeActivity.class);
		intent.putExtra(IS_INTRODUCE, true);
		context.startActivity(intent);
	}
	
	/*
	private Rect mFrame = new Rect();

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		mViewHolder.mLayout.getHitRect(mFrame);
		
		final int x = (int) ev.getX();
		final int y = (int) ev.getY();
		SystemUtil.logykn("@dispatchTouchEvent " + mViewHolder.mLayout.getMeasuredWidth());
		SystemUtil.logykn("@dispatchTouchEvent " + mViewHolder.mLayout.getMeasuredHeight());
		SystemUtil.logykn("@dispatchTouchEvent " + x + " " + y);
		SystemUtil.logykn("@dispatchTouchEvent " + mFrame.toShortString());
		if (mFrame.contains(x, y)) {
			return mViewHolder.mLayout.dispatchTouchEvent(ev);
		} else {
			return super.dispatchTouchEvent(ev);
		}
	}
	*/
	
	public void update() {
		width = mViewHolder.mImageView.getWidth();
		height = mViewHolder.mImageView.getHeight();
		//SystemUtil.logykn("width:" + width);
		//SystemUtil.logykn("height:" + height);
		mViewHolder.mText01.setPadding(72 * width / 480, 408 * height / 543, 0, 0);
		mViewHolder.mText02.setPadding(42 * width / 480, 210 * height / 543, 0, 0);
		mViewHolder.mText03.setPadding(33 * width / 480, 411 * height / 543, 0, 0);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		mViewHolder = null;
		RenrenChatApplication.mWelcomeActivity = null;
	}
	
}
