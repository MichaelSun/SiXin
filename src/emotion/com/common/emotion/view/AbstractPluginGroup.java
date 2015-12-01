package com.common.emotion.view;

import java.util.LinkedList;
import java.util.List;

import com.core.util.CommonUtil;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;

/**
 * 横向滑动的控件
 * @author dingwei.chen
 * */
public abstract class AbstractPluginGroup extends ViewGroup {

	protected Scroller mScroller;
	private VelocityTracker mVelocityTracker = null;
	private static final float ABS_MIN_VELOCITY_X = 300.0F;
	private static final float ABS_MIN_VELOCITY_Y = 200.0F;
	protected int mWidth = 0;
	PAGER_STATE mState = PAGER_STATE.STOP;
	boolean mIsNextPage = true;
	private float mStartX = 0;
	private float mStartY = 0;
	private float mOffsetX = 0;
	// 每个页面的数量
	public static final int PAGE_MAX_NUMBER = 20;
	public static final int LINE_MAX_NUMBER = 3;
	protected List<PluginPager> mPagers = new LinkedList<PluginPager>();
	protected List<PluginPager> mRecyclePagers = new LinkedList<PluginPager>();
	private static final int DEFUALT_BACK_COLOR = 0XFFeaeaea;
	private int mLineHeight = 0;
	Rect mRect = new Rect();
	private String tag = "pgroup";
	/**
	 * 方向锁
	 */
	private DIR_LOCK mLock = DIR_LOCK.UNLOCK;
	
	public AbstractPluginGroup(Context context, AttributeSet attrs) {
		super(context, attrs);
		mScroller = new Scroller(context);
		this.setOnClickListener(null);
		this.setBackgroundColor(DEFUALT_BACK_COLOR);
//		sLineBitmap = PluginPager.getBitmap(context, sLineBitmap,
//				R.drawable.linex);
//		mLineHeight = sLineBitmap.getHeight();
	}

	/**
	 * 每一个页面的列数
	 */
	protected int mXCount = 4;//default value
	/**
	 * 每一个页面的行数
	 */
	protected int mYCount = 2;//default value
	/**
	 * 每一个页面的总数
	 */
	protected int mMaxCount = 8;//default value
	
	protected int mLeft = 6;    //default value
	
	protected int mTop = 0;      //default value
	
	protected int mRight = 18;    //default value
	
	protected int mBottom = 18;   //default value
	
	protected int mOffest ; 
	/**
	 * 设施页面的行数和列数，一定要在setAdapter之前调用！
	 * @param xCount 列数
	 * @param yCount 行数
	 */
	public void setPageParameter(int xCount,int yCount){
		this.mMaxCount = xCount*yCount;
		this.mYCount = yCount;
		this.mXCount = xCount;
	}
	
	public void setPagerPadding(int left,int top,int right,int bottom,int offest){
		this.mLeft = left;
		this.mTop = top;
		this.mRight = right;
		this.mBottom = bottom;
		this.mOffest = offest;
	}
	
	public static enum DIR {
		LEFT, RIGHT, UP, DOWN
	}

	public static enum PAGER_STATE {
		MOVE, STOP
	}

	public static enum DIR_LOCK{
		UNLOCK,VERTICAL,HORIZONAL
	}
	
	
	@Override
	public void draw(Canvas canvas) {
		super.draw(canvas);
	};
	
	

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		this.measureChild(widthMeasureSpec, heightMeasureSpec);
	}
	protected void measureChild(int widthMeasureSpec, int heightMeasureSpec){
		int c = this.getChildCount();
		int k = 0;
		while(k<c){
			View view = this.getChildAt(k);
			view.measure(widthMeasureSpec, heightMeasureSpec);
			k++;
		}
	}

	@Override
	public boolean onTouchEvent(android.view.MotionEvent event) {
		if (this.mState == PAGER_STATE.MOVE) {
			this.mScroller.abortAnimation();
			this.postInvalidate();
			return false;
		}
		int currentPage = this.getCurrentPage();
		int countPage = this.getPageCount();

		if (mVelocityTracker == null) {
			mVelocityTracker = VelocityTracker.obtain();
		}
		mVelocityTracker.addMovement(event);
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			this.onDown(event.getX(), event.getY());
			this.mStartX = event.getX();
			this.mStartY = event.getY();
			break;
		case MotionEvent.ACTION_MOVE:
			if(mLock==DIR_LOCK.UNLOCK){
				CommonUtil.log(tag, "prepare to add lock");
				//如果无锁就加锁
				float deltaX = Math.abs(event.getX()-mStartX);
				float deltaY = Math.abs(event.getY()-mStartY);
				if(Math.max(deltaX, deltaY)>20){//20是最小的偏移，以防止某些型号的手机过于灵敏
					if(deltaX>deltaY){
						CommonUtil.log(tag,"add horizontal lock  deltaX:"+deltaX);
						mLock = DIR_LOCK.HORIZONAL;
					}else{
						CommonUtil.log(tag,"add vertical lock");
						mLock = DIR_LOCK.VERTICAL;
					}
				}
			}else if(mLock == DIR_LOCK.HORIZONAL){
				CommonUtil.log(tag,"running under horizontal lock");
				this.onMove(event.getX(), event.getY());
			}
			break;
		case MotionEvent.ACTION_UP:
			mVelocityTracker.computeCurrentVelocity(1000);
			float vX = mVelocityTracker.getXVelocity();
			float vY = mVelocityTracker.getYVelocity();
			DIR vXDirect = vX > 0 ? DIR.RIGHT : DIR.LEFT;
			DIR vYDirect = vY > 0 ? DIR.DOWN : DIR.UP;
			// 纵向速率大于横向速率
			// 这里的最小纵向速率标准要相对横向最小速率小
			if (Math.abs(vY) > Math.abs(vX)&&Math.abs(vY)>ABS_MIN_VELOCITY_Y&&mLock==DIR_LOCK.VERTICAL) {
				CommonUtil.log(tag,"vY:"+vY+"  vX"+vX);
				switch (vYDirect) {
				case UP:
					CommonUtil.log(tag, "up");
					vListener.onShowPreviousPointer();
					break;
				case DOWN:
					CommonUtil.log(tag, "down");
					vListener.onShowNextPointer();
					break;
				default:
					break;
				}
			}else{
				this.mOffsetX = event.getX() - mStartX;
				// 横向滑动速度大于最小的Fling速度，触发Fling
				if (Math.abs(vX) > ABS_MIN_VELOCITY_X) {
					switch (vXDirect) {
					case LEFT:
						if (currentPage + 1 < countPage) {
							this.mIsNextPage = true;
							CommonUtil.log(tag,"next");
							this.onMoveNextPage(this.getScrollX());
						} else {
							this.onBackOldPosition(this.getScrollX(), DIR.LEFT,
									mOffsetX);
						}
						break;
					case RIGHT:
						if (currentPage > 0) {
							this.mIsNextPage = false;
							CommonUtil.log(tag,"previous");
							this.onMovePrePage(this.getScrollX());
						} else {
							this.onBackOldPosition(this.getScrollX(), DIR.LEFT,
									mOffsetX);
						}
						break;
					default:
						break;
					}
					mState = PAGER_STATE.MOVE;
					this.postInvalidate();
				} else {
					// 处理先慢后快非正常滑动的问题
					DIR offsetDir = mOffsetX > 0 ? DIR.RIGHT : DIR.LEFT;
					int min = this.onGetMinOffsetWidth();
					CommonUtil.log(tag,"--------startX:"+mStartX);
					CommonUtil.log(tag,"min = " + min + "--->" + mOffsetX);
					if (Math.abs(mOffsetX) >= min) {
						switch (offsetDir) {
						case LEFT:
							if (currentPage + 1 < countPage) {
								this.mIsNextPage = true;
								CommonUtil.log(tag,"else next l");
								this.onMoveNextPage(this.getScrollX());
							} else {
								this.onBackOldPosition(this.getScrollX(), DIR.LEFT,
										mOffsetX);
							}
							break;
						case RIGHT:
							if (currentPage > 0) {
								this.mIsNextPage = false;
								CommonUtil.log(tag,"else next r");
								this.onMovePrePage(this.getScrollX());
							} else {
								this.onBackOldPosition(this.getScrollX(), DIR.LEFT,
										mOffsetX);
							}
							break;
						default:
							break;
						}
						mState = PAGER_STATE.MOVE;
						this.postInvalidate();
					} else {
						// return old state
						this.onBackOldPosition(getScrollX(), offsetDir, mOffsetX);
					}
				}
			}
			
			this.onUp(event.getX(), event.getY());
			this.onEndDrag();
			break;
		}
		return super.onTouchEvent(event);
	};

	/**
	 * @param dir
	 *            :offset direct
	 * */
	protected abstract void onBackOldPosition(float x, DIR dir,
			float fingerMoveOffset);

	protected void move(int start, int offset) {
		this.mScroller.startScroll(start, 0, offset, 0);
		this.mState = PAGER_STATE.MOVE;
		this.postInvalidate();
	}

	protected abstract void onDown(float x, float y);

	protected abstract void onMove(float x, float y);

	protected abstract void onUp(float x, float y);

	protected abstract void onMovePrePage(float x);

	protected abstract void onMoveNextPage(float x);

	protected abstract void onMoveOver(boolean isNextPage);

	protected abstract int onGetMinOffsetWidth();

	protected abstract int getCurrentPage();

	protected abstract int getPageCount();

	@Override
	public void computeScroll() {
		if (mState == PAGER_STATE.STOP) {
			return;
		}
		if (!mScroller.isFinished() && mScroller.computeScrollOffset()) {
			this.scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
			this.postInvalidate();
		} else {
			mScroller.abortAnimation();
			this.scrollTo(mScroller.getFinalX(), mScroller.getFinalY());
			if (mState == PAGER_STATE.MOVE) {
				this.onMoveOver(mIsNextPage);
				mState = PAGER_STATE.STOP;
				this.postInvalidate();
			}
		}
	};

	protected void onEndDrag() {
		mLock = DIR_LOCK.UNLOCK;
		if (this.mVelocityTracker != null) {
			this.mVelocityTracker.clear();
			this.mVelocityTracker.recycle();
			this.mVelocityTracker = null;
		}
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {

		int count = this.getChildCount();
		int index = 0;
		int pagerWidth = this.getWidth();
		mWidth = pagerWidth;
		int offsetHeight = mLineHeight;
		while (index < count) {
			View v = this.getChildAt(index);
			l = index * pagerWidth;
			v.layout(l, 0, l + pagerWidth, getHeight() - offsetHeight);
			index++;
		}
	}

	private View mTarget = null;

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		int x = (int) ev.getX();
		int y = (int) ev.getY();
		if (ev.getAction() == MotionEvent.ACTION_UP) {
			if (mTarget != null) {
				Rect r = new Rect();
				mTarget.getHitRect(r);
				if (r.contains(x, y)) {
					mTarget.dispatchTouchEvent(ev);
				} else {
					int oldAction = ev.getAction();
					ev.setAction(MotionEvent.ACTION_CANCEL);
					mTarget.dispatchTouchEvent(ev);
					ev.setAction(oldAction);
				}
				mTarget = null;
			}
		} else if (ev.getAction() == MotionEvent.ACTION_DOWN) {
			this.mStartX = ev.getX();
			this.mStartY = ev.getY();
			ViewGroup group = (ViewGroup) this.getChildAt(getCurrentPage());
			int count = group.getChildCount();
			int index = 0;
			Rect r = new Rect();
			while (index < count) {
				mTarget = group.getChildAt(index);
				mTarget.getHitRect(r);
				if (r.contains(x, y)) {
					mTarget.dispatchTouchEvent(ev);
					break;
				}
				index++;
			}
			if (index == count) {
				mTarget = null;
			}
		}
		this.onTouchEvent(ev);
		return true;
	}

	@Override
	public void addView(View child) {
		if (!(child instanceof ViewGroup)) {
			throw new RuntimeException("add view must be subclass of ViewGroup");
		}
		super.addView(child);
	}

	public void addPager(PluginPager pager) {
		this.addView(pager);
		this.mPagers.add(pager);
	}

	protected void recycle() {
		this.removeAllViews();
		this.mRecyclePagers.addAll(this.mPagers);
		for (PluginPager p : this.mPagers) {
			p.removeAllViews();
			p.layout(0, 0, 0, 0);
		}
		this.mPagers.clear();
	}

	protected void obtainPagers(int size) {
		int k = 0;
		while (k < size && mRecyclePagers.size() > 0) {
			addPager(mRecyclePagers.remove(0));
			k++;
		}
		if (k != size) {
			while (k < size) {
				//构造一个页面
				addPager(new PluginPager(getContext(),mXCount,mYCount,mLeft,mTop,mRight,mBottom,mOffest));
				k++;
			}
		}
		mRecyclePagers.clear();
	}

	OnPagerListenner mPagerListener = null;

	public void setOnPagerListenner(OnPagerListenner listener) {
		mPagerListener = listener;
	}

	public static interface OnPagerListenner {
		public void onPageChange(int page);

		public void onPageSizeChange(int pageSize);
	}

	protected void onPageSizeChange() {
		if (mPagerListener != null) {
			mPagerListener.onPageSizeChange(getPageCount());
		}
	}

	protected void onPageChange() {
		if (mPagerListener != null) {
			mPagerListener.onPageChange(getCurrentPage());
		}
	}

	// 横纵接口相关方法
	protected IOnFling2ContentChangeListener vListener;
	protected IOnFling2ContentChangeListener hListener;
	/**
	 * 设置横向的导航条
	 * @param listener
	 */
	public void setOnHorizontalFlingListener(IOnFling2ContentChangeListener listener) {
		this.hListener = listener;
	}

	/**
	 * 设置纵向的Flipper，一旦纵向滑动，首先调用Flipper切换内容，然后由Flipper控制纵向的导航条
	 * @param listener
	 */
	public void setOnVerticalFlingListener(IOnFling2ContentChangeListener listener) {
		this.vListener = listener;
	}

}
