package com.renren.mobile.chat.ui;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import android.app.Activity;
import android.graphics.Matrix;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.common.utils.Bip;
import com.core.util.AbstractApplication;
import com.core.util.DateFormat;
import com.core.util.DipUtil;
import com.renren.mobile.chat.R;
import com.sixin.widgets.RotateImageView;

public abstract class PullUpdateTouchListener implements OnTouchListener {
	
	public abstract class GetTextUtils{
		public final static int pull_to_refresh_pull_label = 1;
		public final static int pull_to_refresh_release_label = 2;
		public final static int pull_to_refresh_refreshing_label = 3;
		public final static int pull_to_refresh_ok_label = 4;
		public final static int pull_to_refresh_last_update_time = 5;
	}
	
	protected static final String								TAG	= "PullUpdateTouchListener ";
	private static WeakReference<PullUpdateTouchListener>		instance;

	public static void reset() {
		PullUpdateTouchListener l = instance.get();
		if (l != null) {
			l.resetHeader();
		}
	}

	private static final int				UP						= 1;
	private static final int				DOWN					= 2;
	private static final int				UPDATING				= 3;
	private static final int				IDLE					= 4;
	private static final int				REFRESHED				= 5;
	private static final SimpleDateFormat	format					= new SimpleDateFormat("HH:mm");

	private Handler							mHandler;
	private RelativeLayout					mPullUpdateView;
	private TextView						mText;
	private TextView						mLastUpdateTime;
	private ProgressBar						mProgressBar;
	private View							mImageView;
	private RotateImageView					mCircleImage;

	private boolean							isComplete				= false;
	private boolean							isFirstGetHead			= false;
	private boolean							isStartMoving			= false;
	private boolean							isRefreshCompleteSend	= false;

	private final ArrayList<Float>			events					= new ArrayList<Float>(3);
	private final onRefreshCompleteDelayed	completeDelayed			= new onRefreshCompleteDelayed();
	private final RotateAnimation			mFlipAnimation;
	private final RotateAnimation			mReverseFlipAnimation;
	private final double					scale;
	private final ChangeText				textChanger				= new ChangeText();

	private final ViewFling					resetHeaderRunnable;
	private final ViewFling					onRefreshCompleteRunnable;
	private final ViewFling					prepareForRefreshRunnable;
	private final ViewFling					beginRefreshRunnable;

	private int								mCorrentState;
	private int								mViewHeight;
	private int								mOriginPaddingTop;
	private double							mLastMotionY;
	private int								offset					= 10;
	private int 							screenHeight;
	public long								LastUpdateTime			= 0L;
	private Matrix							mMatrix;
	private static String					updateText				= null;
	private static String					timeFormat1				= null;
	private static String					timeFormat2				= null;
	private static String					timeFormat3				= null;
	private static String					timeFormat4				= null;
	
	public Runnable resetRunnable = null;

	public abstract boolean isHead(); // 判断是否到达顶部

	public abstract void afterDo(); // 刷新完成后执行

	public abstract void refresh(); // 刷新需要的操作
	
	public PullUpdateTouchListener(Activity mActivity, int rId) {
		this((RelativeLayout) mActivity.findViewById(rId));
	}

	public PullUpdateTouchListener(View mLayout, int rId) {
		this((RelativeLayout) mLayout.findViewById(rId));
	}

	public PullUpdateTouchListener(LayoutInflater inflater) {
		this((RelativeLayout) inflater.inflate(R.layout.pull_update_header, null));
	}

	public PullUpdateTouchListener(RelativeLayout relativeLayout) {
		super();
		instance = new WeakReference<PullUpdateTouchListener>(this);
		mMatrix = new Matrix();
		this.mHandler = new Handler();
		scale = relativeLayout.getContext().getResources().getDisplayMetrics().density;
		screenHeight = relativeLayout.getContext().getResources().getDisplayMetrics().heightPixels;
		offset *= scale;
		mPullUpdateView = relativeLayout;
		mImageView = mPullUpdateView.findViewById(R.id.pull_to_refresh_image);
		mCircleImage = (RotateImageView) mPullUpdateView.findViewById(R.id.pull_to_refresh_circle);
		mProgressBar = (ProgressBar) mPullUpdateView.findViewById(R.id.pull_to_refresh_progress);

		mText = (TextView) mPullUpdateView.findViewById(R.id.frame_layout).findViewById(R.id.text_layout)
				.findViewById(R.id.pull_to_refresh_text);
		mLastUpdateTime = (TextView) mPullUpdateView.findViewById(R.id.frame_layout).findViewById(R.id.text_layout)
				.findViewById(R.id.last_update_time);

//		mPullUpdateView.measure(0, 0);
		mViewHeight = DipUtil.calcFromDip(42);
		mCorrentState = IDLE;
		
		mFlipAnimation = new RotateAnimation(0, -180, RotateAnimation.RELATIVE_TO_SELF, 0.5f,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		mFlipAnimation.setInterpolator(new LinearInterpolator());
		mFlipAnimation.setDuration(250);
		mFlipAnimation.setFillAfter(true);
		mReverseFlipAnimation = new RotateAnimation(-180, -360, RotateAnimation.RELATIVE_TO_SELF, 0.5f,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		mReverseFlipAnimation.setInterpolator(new LinearInterpolator());
		mReverseFlipAnimation.setDuration(250);
		mReverseFlipAnimation.setFillAfter(true);
		
		mPullUpdateView.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return true;
			}
		});
		
		updateText = getString(R.string.pull_to_refresh_last_update_time);
		timeFormat1 = getString(R.string.pull_to_refresh_format_1);
		timeFormat2 = getString(R.string.pull_to_refresh_format_2);
		timeFormat3 = getString(R.string.pull_to_refresh_format_3);
		timeFormat4 = getString(R.string.pull_to_refresh_format_4);

		resetHeaderRunnable = new ViewFling(getHideHight(), 10, 10) {
			@Override
			void elseDo() {
				changeCircle();
				mCorrentState = IDLE;
				mImageView.clearAnimation();
				mProgressBar.setVisibility(View.GONE);
				mOriginPaddingTop = mPullUpdateView.getPaddingTop();
				if(resetRunnable != null){
					resetRunnable.run();
				}
			}
		};
		onRefreshCompleteRunnable = new ViewFling(getHideHight(), 10, 10) {
			@Override
			void elseDo() {
				Bip.bipContactsRefresh();
				mProgressBar.setVisibility(View.INVISIBLE);
				mCorrentState = IDLE;
				mProgressBar.setVisibility(View.GONE);
				mImageView.setVisibility(View.GONE);
				mImageView.setBackgroundResource(R.drawable.pull_update_header_arrow_down);
				isComplete = false;
				afterDo();
				resetHeader();
			}
		};
		prepareForRefreshRunnable = new ViewFling(0, 10, 10) {
			@Override
			void elseDo() {
//				mPullUpdateView.setPadding(mPullUpdateView.getPaddingLeft(), 0, mPullUpdateView.getPaddingRight(),
//				mPullUpdateView.getPaddingBottom());
				changeCircle();
//				mImageView.setBackgroundResource(R.drawable.pull_update_header_eye);
				mProgressBar.setVisibility(View.VISIBLE);
				mText.setText(getString(R.string.pull_to_refresh_refreshing_label));
				refresh();
			}
		};
		beginRefreshRunnable = new ViewFling(0, 20, 10, false) {
			@Override
			void elseDo() {
				changeCircle();
//				mImageView.setBackgroundResource(R.drawable.pull_update_header_eye);
				mProgressBar.setVisibility(View.VISIBLE);
				mText.setText(getString(R.string.pull_to_refresh_refreshing_label));
				refresh();
			}
		};
	}
	
	private void changeCircle(){
		switch (mCorrentState) {
		case REFRESHED:
		case UPDATING:
			mCircleImage.setVisibility(View.INVISIBLE);
			mImageView.setBackgroundDrawable(null);
			mImageView.setVisibility(View.GONE);
			break;
		default:
			mCircleImage.setVisibility(View.VISIBLE);
			mImageView.setVisibility(View.VISIBLE);
			break;
		}
	}

	public RelativeLayout getPullUpdateView() {
		return mPullUpdateView;
	}

	public static boolean hasCancelUpdate(PullUpdateTouchListener listener) {
		return false;
	}

	private int getHideHight() {
		if (mViewHeight == 0) {
			return (int)(-100*scale);
		}
		return (int) (0 - mViewHeight * 1.5);
	}

    public void initView(View... views) {
        mPullUpdateView.setVisibility(View.VISIBLE);
        mPullUpdateView.setPadding(mPullUpdateView.getPaddingLeft(), getHideHight(), mPullUpdateView.getPaddingRight(),
                mPullUpdateView.getPaddingBottom());
        mOriginPaddingTop = mPullUpdateView.getPaddingTop();
        if (views != null && views.length > 0) {
            for (View v : views) {
                if (v instanceof ListView) {
                    ((ListView) v).setCacheColorHint(0);
                }
                v.setOnTouchListener(this);
//                v.setLongClickable(true);
            }
        }
    }

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if (isComplete || mCorrentState == UPDATING || mCorrentState == REFRESHED) { // 假如下拉刷新有显示内容、当前状态是更新中
			if (isRefreshCompleteSend && isComplete) { // 假如下拉刷新有显示内容，还没来得及收回，但用户触摸了屏幕
				mHandler.removeCallbacks(completeDelayed);
				mHandler.post(onRefreshCompleteRunnable);
				isRefreshCompleteSend = false;
			}
			return false;
		}

		final int action = event.getAction();
		if (!isStartMoving && (action == MotionEvent.ACTION_MOVE || action == MotionEvent.ACTION_DOWN)) {
			isStartMoving = true;
			isFirstGetHead = isHead();
			mLastMotionY = event.getY();
		}
		int eventSize = events.size();
		if (eventSize < 2) {
			if (action == MotionEvent.ACTION_UP) {
				events.clear();
			} else if ((eventSize >= 1 && events.get(eventSize - 1) != event.getRawY()) || eventSize < 2) {
				events.add(event.getRawY());
			}
			return false;
		}
		if(events != null && events.size() > 1 && events.get(0).equals(events.get(1))){//add by yahui.wu
			events.clear();
			return false;
		}
		if (action == MotionEvent.ACTION_UP) {
			isStartMoving = false;
			events.clear();
			if (mCorrentState == DOWN || mPullUpdateView.getBottom() >= offset + mViewHeight) {
				mCorrentState = UPDATING; // 开始刷新
				mText.setText(getString(R.string.pull_to_refresh_refreshing_label));
				if (mPullUpdateView.getPaddingTop() < 0){
					mPullUpdateView.setPadding(mPullUpdateView.getPaddingLeft(), 0, mPullUpdateView.getPaddingRight(),
							mPullUpdateView.getPaddingBottom()); //note by yahui.wu
				}
				prepareForRefreshRunnable.beforeDo();
				mHandler.post(prepareForRefreshRunnable);
			} else { // 在下拉刷新状态下松手
				if (mPullUpdateView.getPaddingTop() < 0){
					mPullUpdateView.setPadding(mPullUpdateView.getPaddingLeft(), getHideHight(), mPullUpdateView.getPaddingRight(),
							mPullUpdateView.getPaddingBottom()); //note by yahui.wu
				}
				resetHeader();
			}
			return false;
		}

		if (!isFirstGetHead || events.size() < 2) { // 用户动作没有采样够 || 当前位置尚未到头
			return false;
		}
		if (events.size() >= 2 && events.get(0) > events.get(1)) { // 采样足够，用户的手向上动
			return false;
		}

		if (mImageView.getVisibility() != View.VISIBLE) {
			mImageView.setVisibility(View.VISIBLE);
			mImageView.setBackgroundResource(R.drawable.pull_update_header_arrow_down);
		}
		if (mPullUpdateView.getVisibility() != View.VISIBLE) {
			mPullUpdateView.setVisibility(View.VISIBLE);	
		}
		
		if (mPullUpdateView.getBottom() >= mViewHeight + offset && mCorrentState != DOWN) {
			mCorrentState = DOWN;
			if (!textChanger.isInUse) {
				mText.setText(getString(R.string.pull_to_refresh_release_label));
			}
			mImageView.clearAnimation();
			mImageView.startAnimation(mFlipAnimation);
			Bip.bipContactsScrollDown();
		}
		if (mPullUpdateView.getBottom() < mViewHeight + offset && (mCorrentState == DOWN || mCorrentState == IDLE)) {
			if (!textChanger.isInUse) {
				mText.setText(getString(R.string.pull_to_refresh_pull_label));
			}
			mImageView.clearAnimation();
			if (mCorrentState == DOWN){
				mImageView.startAnimation(mReverseFlipAnimation);
				Bip.bipContactsScrollUp();
			}
			else {
				updateTimeDisplay();
				textChanger.check();
				textChanger.updateText(R.string.pull_to_refresh_pull_label);
			}
			mCorrentState = UP;
		}

		if (action == MotionEvent.ACTION_MOVE) {
			if (resetHeaderRunnable.isSend()) {
				mHandler.removeCallbacks(resetHeaderRunnable);
			}
			double topPadding = (event.getY() - mLastMotionY) / 1.7;
			int newTopPadding = (int) (mOriginPaddingTop + topPadding + 0.5);
			if (newTopPadding >= getHideHight()) {
				if(newTopPadding < 100*scale){//控制下拉显示的高度
					mPullUpdateView.setPadding(mPullUpdateView.getPaddingLeft(), newTopPadding,
							mPullUpdateView.getPaddingRight(), mPullUpdateView.getPaddingBottom());
					mCircleImage.setDegree(newTopPadding); //TODO 稍后修改
				}
			} else {
				mLastMotionY = event.getY();
			}
		}

		if (mPullUpdateView.getBottom() > 0) { // 当下拉刷新露出来的时候就停止事件的传递
			return true;
		}
		return isStartMoving;
	}

	private void resetHeader() {
		mCorrentState = IDLE;
		if (mPullUpdateView.getBottom() <= 0) {
			resetHeaderRunnable.elseDo();
			mPullUpdateView.setPadding(mPullUpdateView.getPaddingLeft(), getHideHight(),
					mPullUpdateView.getPaddingRight(), mPullUpdateView.getPaddingBottom());
		}
		changeCircle();
		mHandler.post(resetHeaderRunnable);
	}

	public void onRefreshComplete() {
		if (mCorrentState != UPDATING)
			return;
		mCorrentState = REFRESHED;
		changeCircle();
		mText.setText(getString(R.string.pull_to_refresh_ok_label));
		mHandler.post(onRefreshCompleteRunnable);
	}

	public void onRefreshComplete(CharSequence info, int time) {
		if (mCorrentState != UPDATING)
			return;
		mCorrentState = REFRESHED;
		changeCircle();
		mText.setText(info);
		isComplete = true;
		isRefreshCompleteSend = true;
		mHandler.postDelayed(completeDelayed, time);
	}

	// 当有特殊情况下，在下拉刷新中需要显示的内容
	public void onRefreshComplete(CharSequence info) {
		onRefreshComplete(info, 2000);
	}

	class onRefreshCompleteDelayed implements Runnable {

		@Override
		public void run() {
			mHandler.post(onRefreshCompleteRunnable);
		}
	}

	abstract private class ViewFling implements Runnable {
		private int					finalPaddingTop;		// mPullUpdateView最后的paddingTop
		private int					perFling;				// 每个时间周期内滚动的像素
		private int					perTime;				// 每个时间周期的长度
		private boolean				isSend		= false;
		private boolean				isShower	= false;	// 是否变慢
		private boolean				isOtherAlg	= false;
		private boolean				isDown		= true;
		private int					originPadding;
		private int					totalTime;
		private double				argK		= 0.8;
		private int					time		= 0;
		private FunctionInterface	function;

		public boolean isSend() {
			return isSend;
		}

		public ViewFling(int finalPaddingTop, int perFling, int perTime) {
			this(finalPaddingTop, perFling, perTime, true);
		}

		public ViewFling(int finalPaddingTop, int perFling, int perTime, boolean isDown) {
			super();
			this.finalPaddingTop = finalPaddingTop;
			this.perFling = (int) (perFling * scale);
			this.perTime = perTime;
			this.isDown = isDown;
		}

		@Override
		public void run() {
			isSend = true;
			int originPaddingTop = mPullUpdateView.getPaddingTop();
			if (isDown ? (originPaddingTop > finalPaddingTop) : (originPaddingTop < finalPaddingTop)) {
				if (!isShower && originPaddingTop < finalPaddingTop + 8 * scale) {
					isShower = true;
					function = new FunctionSlower();
				}
				int motion = (isOtherAlg ? (function.getY(time, originPaddingTop) != 0 ? function.getY(time,
						originPaddingTop) : 1) : perFling);
				motion = isDown ? motion : (0 - motion);
				motion = Math.max(0, motion);//控制motion不能为负 add by yahui.wu 防止偶尔铺满整个屏幕
				mPullUpdateView.setPadding(mPullUpdateView.getPaddingLeft(), originPaddingTop - motion,
						mPullUpdateView.getPaddingRight(), mPullUpdateView.getPaddingBottom());
				time += perTime;
				if(originPaddingTop > screenHeight){//防止无限循环卡死
					clear();
					return;
				}
				mHandler.postDelayed(this, perTime);
			} else {
				elseDo();
				clear();
				
			}
		}
		public void clear(){
			mHandler.removeCallbacks(this); // 防止内存溢出
			isSend = false;
			isOtherAlg = false;
			function = null;
			time = 0;
			isShower = false;
		}

		public void beforeDo() {
			isOtherAlg = true;
			originPadding = mPullUpdateView.getPaddingTop();
			totalTime = (int) (argK * perTime * (originPadding - finalPaddingTop) / (scale * perFling));
			function = new Function(totalTime, finalPaddingTop, originPadding);
		}

		abstract void elseDo(); // 动画完成后的操作
	}

	interface FunctionInterface {
		int getY(int time, int originPadding);
	}

	class Function implements FunctionInterface {
		private double	a, b, c;	// 方程参数 y = a*x^2 + b*x + c;

		public Function(double a1, double b1, double c1) {
			double k = (c1 - b1) / (a1 * a1);
			this.a = k;
			this.b = 0 - 2 * a1 * k;
			this.c = a1 * a1 * k + b1;
		}

		@Override
		public int getY(int x, int originPadding) {
			return (int) (originPadding - (a * x * x + b * x + c));
		}
	}

	public class FunctionSlower implements FunctionInterface {

		public FunctionSlower() {}

		@Override
		public int getY(int x, int originPadding) {
			return (int) (scale + 0.5);
		}
	}

	public void beginRefresh() {
		mCorrentState = UPDATING; // 开始刷新
		updateTimeDisplay();
		mPullUpdateView.setPadding(mPullUpdateView.getPaddingLeft(), 0, mPullUpdateView.getPaddingRight(),
				mPullUpdateView.getPaddingBottom());
		mHandler.post(beginRefreshRunnable);
	}

	public static final String SIMPLE_DATE_PATTERN = "MM/dd/yyyy HH:mm";
	
	private void updateTimeDisplay() {
		if (System.currentTimeMillis() - LastUpdateTime <= 5 * 60 * 1000) {
			int tmp = (int) ((System.currentTimeMillis() - LastUpdateTime) / 1000);
			int sec = tmp % 60;
			int min = tmp / 60;
			String lastTime = null;
			if (sec != 0 && min != 0)
				lastTime = String.format(timeFormat1, min, sec);
			else if (tmp / 60 != 0)
				lastTime = String.format(timeFormat2, min);
			else if (tmp % 60 != 0 && tmp != 0)
				lastTime = String.format(timeFormat3, sec);

			mLastUpdateTime.setText(TextUtils.isEmpty(lastTime) ? timeFormat4 : updateText + lastTime);
		} else if (LastUpdateTime != 0) {
			mLastUpdateTime.setText(updateText + DateFormat.getNowStr(LastUpdateTime));
		} else {
			mLastUpdateTime.setText(format.format(new Date()));
		}
	}
	
    public static boolean isEn(){
        if(null == Locale.getDefault().getLanguage())
            return false;
        return "en".equals(Locale.getDefault().getLanguage());
    }

	protected final class ChangeText {
		static final int	ISUSEUPDATETEXT	= 50500;
		static final int	UPDATETIMES		= 50501;
		static final int	UPDATETEXTBEGIN	= 50502;
		static final int	UPDATETEXTEND	= 50599;

		int					mUpdateTimes	= -1;
		int					mUpdateIndex;
		boolean				mIsUseUpdateText;
		boolean				mIsNoEnd;
		boolean				isInited		= false;
		boolean				isInUse			= false;

		public void check() {
			if (isInited)
				return;
			mIsUseUpdateText = "1".equals(errorMessage(ISUSEUPDATETEXT));

			if (mIsUseUpdateText && mUpdateTimes < 0) {
			    String updateTimes = errorMessage(UPDATETIMES);
				if(updateTimes == null || TextUtils.isEmpty(updateTimes)){
					mUpdateTimes = -1;
				}else{
					mUpdateTimes = Integer.parseInt(updateTimes);
				}
				mIsNoEnd = mUpdateTimes == 0;
				mUpdateIndex = UPDATETEXTBEGIN;
			} else {
				mUpdateTimes = -1;
			}
			isInited = true;
		}

		public void updateText(int textId) {
			String text = null;
			if (mIsUseUpdateText && (mIsNoEnd || mUpdateTimes > 0)) {
				text = errorMessage(mUpdateIndex);
				if (TextUtils.isEmpty(text) || mUpdateTimes == UPDATETEXTEND) {
					mUpdateIndex = UPDATETEXTBEGIN;
					if (!mIsNoEnd)
						--mUpdateTimes;
					if (TextUtils.isEmpty(text)) {
						text = errorMessage(mUpdateIndex);
					}
				} else {
					++mUpdateIndex;
				}
			}
			if (TextUtils.isEmpty(text)) {
				mText.setText(getString(textId));
			} else {
				isInUse = true;
				mText.setText(text);
			}
		}

		private String errorMessage(int code) {
//			String result = ErrorMessageUtils.getMessage(mPullUpdateView.getContext(), code);
//			return result;
			return "";
		}
	}

	public static String getString(int stringId) {
		return AbstractApplication.getAppContext().getString(stringId);
	}

}