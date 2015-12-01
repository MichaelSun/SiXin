package view.voice;

import com.core.util.DipUtil;
import com.renren.mobile.chat.R;
import com.renren.mobile.chat.base.util.SystemUtil;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * @author dingwei.chen
 * 国内
 * */
public class VoiceView extends LinearLayout implements android.view.View.OnTouchListener{

	private static final Paint PAINT = new Paint();
	private float mSweepAngle = 360.0f;
	private int mWidth = 0;
	private static final float START_ANGLE = -90F;
	private  int LINE_COLOR = 0xff31b5f4;
	private  int ALERT_COLOR = 0xffef5c30;
	private  int BACK_COLOR = 0xd84a5154;
	private int mStartX = 150;
	private int mStartY = 300;
	private int mLineWidth = 10;
	private int mColor = LINE_COLOR;
	TextView mTimeTextView = null;
	View mAlertImage = null;
	View mRubbish = null;
	View mVoiceToShortView = null;
	View mVoiceSpeaker = null;
	View mVoiceRoot = null;
	LayoutInflater mInflater = null;
	HeaderLayout mHeaderLayout = null;
	private static final boolean DEBUG = true;
	private boolean mIsAbandon = false;
	private boolean mIsOver = false;
	private  String TEXT_CANCEL_SEND = "松开取消发送";
	private  String TEXT_RECORD_LESS = "录制时间太短";
	private  String TEXT_RECORD_OVER = "语音录制即将结束";
	private  String TEXT_MOVE_TO_CANCEL = "滑到这里取消发送";
	public static final int MAX_TIME = 60;
	public static final int ALERT_TIME = 55;
	private boolean mIsAlert = false;
	MotionEvent mMotionUp = null;
	private boolean mIsShow = false;
	View[] mVoices = new View[4];
	private boolean mIsShowVolumns = true;
	
	Handler mHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			
			int time = getTime();
			int angle = getAngle();
			boolean isShowToCancel =  isToCancel();
			if(time>ALERT_TIME){
				mIsShowVolumns = false;
				mColor = ALERT_COLOR;
				mAlertImage.setVisibility(View.VISIBLE);
				if(!mIsAbandon){
					mTimeTextView.setVisibility(View.VISIBLE);
				}
				mHeaderLayout.setVisibility(View.VISIBLE);
				if(!mIsAbandon){
					mHeaderLayout.mTextView.setText(TEXT_RECORD_OVER);
				}
				mVoiceSpeaker.setVisibility(View.GONE);
				mIsAlert = true;
				if(time>=MAX_TIME){
					time = MAX_TIME;
					angle = 360;
				}
			}else{
				mIsShowVolumns = true;
				mColor = LINE_COLOR;
				mAlertImage.setVisibility(View.GONE);
				if(!mIsAbandon && mIsShow){
					mVoiceSpeaker.setVisibility(View.VISIBLE);
					if(isShowToCancel){
						removeCallbacks(mDissmissHeader);
						mHeaderLayout.mTextView.setText(TEXT_MOVE_TO_CANCEL);
						mIsAlert=true;
						mHeaderLayout.setVisibility(View.VISIBLE);
						postDelayed(mDissmissHeader, 2000);
					}
				}
				mTimeTextView.setVisibility(View.GONE);
			}
			setTime(time);
			setSweepAngle(angle);
			postInvalidate();
		};
	};
	
	Runnable mDissmissHeader =new Runnable() {
		public void run() {
			if(!mIsAbandon){
				mHeaderLayout.setVisibility(View.GONE);
				mIsAlert=false;
			}
		}
	};
	
	
	boolean isToCancel(){
		if(this.mListener!=null){
			return this.mListener.onIsShowMoveToCancel();
		}
		return false;
	}
	
	
	private static final int MESSAGE_DRAW = 0;
	private int mHeightOffset = 0;
	public VoiceView(Context context, AttributeSet attrs) {
		super(context, attrs);
		TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.VoiceView);
		BACK_COLOR = array.getColor(R.styleable.VoiceView_background_color, 0xd84a5154);
		LINE_COLOR = array.getColor(R.styleable.VoiceView_line_color, 0xff31b5f4);
		ALERT_COLOR = array.getColor(R.styleable.VoiceView_alert_line_color, 0xffef5c30);
		TEXT_CANCEL_SEND = array.getString(R.styleable.VoiceView_cancel_send);
		TEXT_RECORD_LESS = array.getString(R.styleable.VoiceView_record_less);
		TEXT_RECORD_OVER = array.getString(R.styleable.VoiceView_record_over);
		TEXT_MOVE_TO_CANCEL = array.getString(R.styleable.VoiceView_move_to_cancel);
		array.recycle();
		PAINT.setAntiAlias(true);
		this.setBackgroundColor(0x00000000);
		mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mInflater.inflate(R.layout.voice_view, this);
		mTimeTextView = (TextView)this.findViewById(R.id.voice_time);
		mAlertImage = this.findViewById(R.id.back_image);
		mVoiceRoot = this.findViewById(R.id.voice_root);
		mRubbish = this.findViewById(R.id.bin);
		mVoiceSpeaker = this.findViewById(R.id.voice_speaker);
		mVoiceToShortView = this.findViewById(R.id.voice_to_short);
		this.setOnClickListener(null);
		mHeaderLayout = new HeaderLayout(context);
		this.addView(mHeaderLayout);
		mHeaderLayout.measure(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		mHeightOffset = mHeaderLayout.getMeasuredHeight();
		mHeaderLayout.setVisibility(View.GONE);
		mVoices[0] = this.findViewById(R.id.voice1);
		mVoices[1] = this.findViewById(R.id.voice2);
		mVoices[2] = this.findViewById(R.id.voice3);
		mVoices[3] = this.findViewById(R.id.voice4);
	}
	
	public void setVolumn(int volumn){
		for(View v:mVoices){
			v.setVisibility(View.GONE);
		}
		if(mIsShowVolumns&&!mIsAbandon){
			if(volumn<0){
				volumn = 0;
			}
			if(volumn>=4){
				volumn=3;
			}
			mVoices[volumn].setVisibility(View.VISIBLE);
		}
		
	}
	
	
	
	
	
	
	@Override
	public void draw(Canvas canvas) {
//		this.mStartX = this.getScrollX()+mLineWidth;
//		this.mStartY = this.getScrollY()+mLineWidth;
//		mWidth = Math.min(getHeight()-mHeightOffset, getWidth());
//		RectF circle_Rect = this.getRectF((mWidth-(mLineWidth<<1)));
		this.drawCircle(canvas);
		this.drawBackground(canvas);
		super.draw(canvas);
		if(this.getVisibility()==View.VISIBLE && !mIsOver){
			Message m = mHandler.obtainMessage(MESSAGE_DRAW);
			mHandler.sendMessageDelayed(m,30);
		}
	}
	public void pop(){
		this.reset();
		this.setVisibility(View.VISIBLE);
		ScaleAnimation mScaleAnim = new ScaleAnimation(
				0, 
				1.1f, 
				0, 
				1.1f,
				Animation.RELATIVE_TO_SELF, 0.5f,
				Animation.RELATIVE_TO_SELF, 0.5f);
		mScaleAnim.setDuration(200L);
		this.startAnimation(mScaleAnim);
		SystemUtil.log("voiceupload", "VoiceView.pop()");
	}
	public void dismiss(){
		this.setVisibility(View.INVISIBLE);
		mHandler.removeMessages(MESSAGE_DRAW);
		ScaleAnimation mScaleAnim = new ScaleAnimation(
				1, 
				0f, 
				1, 
				0f,
				Animation.RELATIVE_TO_SELF, 0.5f,
				Animation.RELATIVE_TO_SELF, 0.5f);
		mScaleAnim.setDuration(200L);
		this.startAnimation(mScaleAnim);
		this.removeCallbacks(mDissmissHeader);
		SystemUtil.log("voiceupload", "VoiceView.dismiss()");
	}
	
	
//	boolean isContain(int x,int y){
//		mHeaderLayout.mTextView.setVisibility(View.VISIBLE);
//		Rect hitRect = new Rect();
//		mVoiceRoot.getGlobalVisibleRect(hitRect);
//		boolean flag =  hitRect.contains(x, y);
//		if(!flag){
//			return flag;
//		}else{
//			int r = hitRect.width()>>1;
//			int ox = hitRect.centerX();
//			int oy = hitRect.centerY();
//			return Math.pow(r, 2)>(Math.pow(x-ox, 2)+Math.pow(y-oy, 2));
//		}
//	}
	
	Rect mRect = new Rect();
	public Rect obtainRect(){
		return mRect;
	}
	
    /**
     * 感应区域为与录音圆圈同心的正方形，边长为直径的1.5倍
     */
    boolean isContain(int x, int y) {
        mHeaderLayout.mTextView.setVisibility(View.VISIBLE);
        Rect hitRect = this.obtainRect();
        mVoiceRoot.getGlobalVisibleRect(hitRect);
        int ox = hitRect.centerX();
        int oy = hitRect.centerY();
        int r = (hitRect.width()+(hitRect.width() >> 1))>>1;
        SystemUtil.log("contain", "begin = "+hitRect);
        hitRect.left = ox-r;
        hitRect.top = oy -r;
        hitRect.bottom = oy +r;
        hitRect.right = ox +r;
        SystemUtil.log("contain", "after = "+hitRect);
        return hitRect.contains(x, y);
        
    }
	
	public void setTime(int time){
		mTimeTextView.setText(time+"s");
		mTimeTextView.requestLayout();
	}
	
	private void drawBackground(Canvas canvas){
		
//		float left = rect.left+mLineWidth;
//		float top = rect.top+mLineWidth;
//		RectF dst = new RectF(left, top, left+width, top+width);
		Rect r = new Rect();
		mVoiceRoot.getHitRect(r);
		r.top = r.top+mLineWidth;
		r.left = r.left+mLineWidth;
		r.bottom = r.bottom-mLineWidth;
		r.right = r.right-mLineWidth;
		PAINT.setColor(BACK_COLOR);
		PAINT.setAlpha(150);
		canvas.drawCircle(r.centerX(), r.centerY(),(r.width()/2), PAINT);
		PAINT.setAlpha(255);
	}
	
	
	private RectF getRectF(int width){
		int w =  this.getCircleWidth();
		int x = mStartX+((getWidth()-w)>>1);
		int y =  mStartY+mHeightOffset;
		return new RectF(x, y,x+ width, y+width);
	}
	private void drawCircle(Canvas canvas){
		PAINT.setStyle(Style.STROKE);
		int dip=DipUtil.calcFromDip(3);;
		PAINT.setStrokeWidth(dip);
		PAINT.setColor(mColor);
		Path path = new Path();
		Rect rect = new Rect();
		this.mVoiceRoot.getHitRect(rect);
		RectF rectf = new RectF(rect);
		path.addArc(rectf, START_ANGLE, mSweepAngle);
		canvas.drawPath(path, PAINT);
		path.close();
		PAINT.setStyle(Style.FILL);
//		canvas.drawCircle(rect.centerX(), rect.top, (mLineWidth>>1), PAINT);//start point
//		float r = (rect.width()/2);
//		double x = r + r*Math.sin(Math.PI*mSweepAngle/180)+rect.left;
//		double y = r - r*Math.cos(Math.PI*mSweepAngle/180)+rect.top;
//		canvas.drawCircle((int)x, (int)y, (mLineWidth>>1), PAINT);//start point
	}
	public void setSweepAngle(float angle){
		this.mSweepAngle = angle;
	}
	int mH;
	int mW;
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		mH = 0;
		int count = this.getChildCount();
		if(count>0){
			int k = 0;
			while(k<count){
				View view = this.getChildAt(k);
				view.measure(MeasureSpec.AT_MOST|getWidth(),MeasureSpec.AT_MOST|getHeight());
				if(view!=mHeaderLayout){
					if(mH<view.getMeasuredHeight()){
						mH = view.getMeasuredHeight();
					}
				}
				k++;
			}
			
		}
		int h = mHeaderLayout.getMeasuredHeight();
		if((h+mH)<MeasureSpec.getSize(heightMeasureSpec)){
			 setMeasuredDimension(getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec),
		                getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec));
		}else{
			 setMeasuredDimension(h+mH,h+mH);
		}
	};
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		int width = mVoiceRoot.getMeasuredWidth();
		int height = mVoiceRoot.getMeasuredHeight();
		l = (getWidth()-width)>>1;
		t = ((getHeight()-height)>>1);
		mVoiceRoot.layout(l, t, l+width, t+height);
		width = mHeaderLayout.getMeasuredWidth();
		height = mHeaderLayout.getMeasuredHeight();
		l = (getWidth()-width)>>1;
		this.mHeaderLayout.layout(l, t-height-mLineWidth, l+width, t-mLineWidth);
	}
	
	int getCircleWidth(){
		return DipUtil.calcFromDip(200);
	}
	public void popView(){
		this.pop();
		this.onStart();
	}
	OnTouchListener mTouchListener;
	public void setOnPreTouchListener(OnTouchListener listener){
		mTouchListener = listener;
	}
	
	
	public void hide(){
		SystemUtil.log("hide", "hide1");
		if(mIsShow){
			SystemUtil.log("hide", "hide2");
			this.onTouch(null,obtainEvent());
		}
	}
	
	
	
	private void cloneEvent(MotionEvent event){
		if(this.mMotionUp ==null){
			mMotionUp = MotionEvent.obtain(event);
			mMotionUp.setAction(MotionEvent.ACTION_UP);
		}
	}
	private MotionEvent obtainEvent(){
		return mMotionUp;
	}
	long mPreClickTime = 0L;
	private static final long MIN_OFFSET_TIME = 1500L;
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		SystemUtil.log("touch", "ontouch");
		this.cloneEvent(event);
		if(mTouchListener!=null && event.getAction() == MotionEvent.ACTION_DOWN){
			if((System.currentTimeMillis()-mPreClickTime)<MIN_OFFSET_TIME){
				SystemUtil.toast("请休息一会儿");
				this.mIsShow = false;
				return false;
			}
			if(mTouchListener.onTouch(v, event)){
				this.mIsShow = true;
				return false;
			}
		}
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			SystemUtil.log("recordb", "action_down");
			if((System.currentTimeMillis()-mPreClickTime)<MIN_OFFSET_TIME){
				SystemUtil.toast("请休息一会儿");
				this.mIsShow = false;
				return false;
			}
			popView();
			this.mIsShow = true;
			break;
		case MotionEvent.ACTION_MOVE:
			
			int x = (int)event.getRawX();
			int y = (int)event.getRawY();
			Rect r = new Rect();
			mVoiceRoot.getGlobalVisibleRect(r);
			SystemUtil.log("recordb", "action_move "+x+","+y+","+r+"\r\n"+mVoiceRoot.getScrollX()+","+mVoiceRoot.getScrollY());
			if(isContain(x, y)){
				this.mTimeTextView.setVisibility(View.GONE);
				this.mVoiceSpeaker.setVisibility(View.GONE);
				this.mRubbish.setVisibility(View.VISIBLE);
				mHeaderLayout.setVisibility(View.VISIBLE);
				mHeaderLayout.mTextView.setText(TEXT_CANCEL_SEND);
				if(mIsAlert==true) mIsAlert=false;
				this.mIsAbandon = true;
			}else{
				this.mRubbish.setVisibility(View.GONE);
				if(!mIsAlert){
					mHeaderLayout.setVisibility(View.GONE);
				}
				this.mIsAbandon = false;
			}
			break;
		case MotionEvent.ACTION_UP:
			SystemUtil.log("recordb", "action_up");
			if(!this.mIsShow){
				this.dismiss();
				return false;
			}
			mPreClickTime = System.currentTimeMillis();
			this.mIsOver = true;
			this.mIsShow = false;
			SystemUtil.log("hide", "up");
			if(!isLessTime() || mIsAbandon){
				this.mVoiceToShortView.setVisibility(View.GONE);
				this.dismiss();
				onVoiceEnd(!mIsAbandon);
			}else{
				this.mTimeTextView.setVisibility(View.GONE);
				this.mVoiceToShortView.setVisibility(View.VISIBLE);
				this.mHeaderLayout.mTextView.setText(TEXT_RECORD_LESS);
				this.mHeaderLayout.setVisibility(View.VISIBLE);
				this.mIsShowVolumns = false;
				this.setVolumn(0);
				mVoiceSpeaker.setVisibility(View.GONE);
				mHandler.removeMessages(MESSAGE_DRAW);
				this.removeCallbacks(mDismissRunnable);
				this.postDelayed(mDismissRunnable, 500);
			}
			break;
		default:
			break;
		}
		return false;
	}
	
	public Runnable mDismissRunnable = new Runnable() {
		@Override
		public void run() {
			dismiss();
			onVoiceEnd(false);
		}
	};
	
	
	
	public void monitorView(View view){
		view.setOnTouchListener(this);
	}
	public static interface MonitorListener{
		public void onVoiceStart();
		public int onGetTime();
		public int onGetAngle();
		public boolean onIsLessMinTime();
		public void onVoiceEnd(boolean isSuccess);
		public boolean onIsShowMoveToCancel();
	}
	public void onVoiceEnd(boolean isSuccess){
		if(mListener!=null){
			mListener.onVoiceEnd(isSuccess);
		}
	}
	
	public boolean isLessTime(){
		if(mListener!=null){
			return mListener.onIsLessMinTime();
		}
		return false;
	}
	
	public void onStart(){
		if(mListener!=null){
			mListener.onVoiceStart();
		}
	}
	public int getTime(){
		if(mListener!=null){
			return mListener.onGetTime();
		}
		return 0;
	}
	public int getAngle(){
		if(mListener!=null){
			return mListener.onGetAngle();
		}
		return 0;
	}
	
	
	MonitorListener mListener = null;
	public void setMonitorListener(MonitorListener listener){
		mListener = listener;
	}
	public void reset(){
		mColor = LINE_COLOR;
		this.setTime(0);
		mTimeTextView.setVisibility(View.GONE);
		mRubbish.setVisibility(View.GONE);
		mAlertImage.setVisibility(View.GONE);
		mHeaderLayout.mTextView.setText(TEXT_CANCEL_SEND);
		mHeaderLayout.setVisibility(View.GONE);
		mVoiceSpeaker.setVisibility(View.VISIBLE);
		this.mVoiceToShortView.setVisibility(View.GONE);
		mIsAbandon = false;
		mIsOver = false;
		mIsAlert = false;
		this.setSweepAngle(0);
		this.mIsShowVolumns = true;
	}
	
	
	class HeaderLayout extends LinearLayout{

		public TextView mTextView = null;
		public HeaderLayout(Context context) {
			super(context);
			mInflater.inflate(R.layout.voice_pop_message, this);
			mTextView = (TextView)this.findViewById(R.id.pop_message);
		}
		
	}
	
}
