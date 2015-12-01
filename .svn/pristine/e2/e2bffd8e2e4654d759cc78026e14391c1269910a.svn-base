package com.sixin.widgets.layout.awesome;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;
import com.sixin.widgets.toolkits.ViewGroupInterceptor;

public class AwesomeFrameLayout extends FrameLayout implements IAwesome {
    private static final String TAG = "AwesomeFrameLayout";

    private IAwesome mAwesomeDelegate;
    private ViewGroupInterceptor mGroupInterceptor;

    public AwesomeFrameLayout(Context context) {
        this(context, null, 0);
    }

    public AwesomeFrameLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AwesomeFrameLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        mAwesomeDelegate = new IAwesomeImpl(this);
        loadAttrs(attrs);

        setAlwaysDrawnWithCacheEnabled(false);
        mGroupInterceptor = (ViewGroupInterceptor) mAwesomeDelegate;
    }

    @Override
    public void loadAttrs(AttributeSet attrs) {
        mAwesomeDelegate.loadAttrs(attrs);
    }

    @Override
    public int getLeftOffset() {
        return mAwesomeDelegate.getLeftOffset();
    }

    @Override
    public int getRightOffset() {
        return mAwesomeDelegate.getRightOffset();
    }

    @Override
    public int getTopOffset() {
        return mAwesomeDelegate.getTopOffset();
    }

    @Override
    public int getBottomOffset() {
        return mAwesomeDelegate.getBottomOffset();
    }

    @Override
    public boolean getLeftTapBack() {
        return mAwesomeDelegate.getLeftTapBack();
    }

    @Override
    public boolean getRightTapBack() {
        return mAwesomeDelegate.getRightTapBack();
    }

    @Override
    public boolean getTopTapBack() {
        return mAwesomeDelegate.getTopTapBack();
    }

    @Override
    public boolean getBottomTapBack() {
        return mAwesomeDelegate.getBottomTapBack();
    }

    @Override
    public void setLeftTapBack(boolean tapBack) {
        mAwesomeDelegate.setLeftTapBack(tapBack);
    }

    @Override
    public void setRightTapBack(boolean tapBack) {
        mAwesomeDelegate.setRightTapBack(tapBack);
    }

    @Override
    public void setTopTapBack(boolean tapBack) {
        mAwesomeDelegate.setTopTapBack(tapBack);
    }

    @Override
    public void setBottomTapBack(boolean tapBack) {
        mAwesomeDelegate.setBottomTapBack(tapBack);
    }

    @Override
    public void left() {
    }

    @Override
    public void right() {
    }

    @Override
    public void top() {
    }

    @Override
    public void bottom() {
    }

    @Override
    public void open() {
    }

    @Override
    public void animateLeft() {
        mAwesomeDelegate.animateLeft();
    }

    @Override
    public void animateRight() {
        mAwesomeDelegate.animateRight();
    }

    @Override
    public void animateTop() {
        mAwesomeDelegate.animateTop();
    }

    @Override
    public void animateBottom() {
        mAwesomeDelegate.animateBottom();
    }

    @Override
    public void animateOpen() {
        mAwesomeDelegate.animateOpen();
    }

    @Override
    public int getState() {
        return mAwesomeDelegate.getState();
    }

    @Override
    public void setLeftAnimationListener(OnLeftAnimationListener listener) {
        mAwesomeDelegate.setLeftAnimationListener(listener);
    }

    @Override
    public void setRightAnimationListener(OnRightAnimationListener listener) {
        mAwesomeDelegate.setRightAnimationListener(listener);
    }

    @Override
    public void setTopAnimationListener(OnTopAnimationListener listener) {
        mAwesomeDelegate.setTopAnimationListener(listener);
    }

    @Override
    public void setBottomAnimationListener(OnBottomAnimationListener listener) {
        mAwesomeDelegate.setBottomAnimationListener(listener);
    }

    @Override
    public void setOpenAnimationListener(OnOpenAnimationListener listener) {
        mAwesomeDelegate.setOpenAnimationListener(listener);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mGroupInterceptor.postInterceptMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        mGroupInterceptor.postInterceptLayout(changed, l, t, r, b);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        mGroupInterceptor.preInterceptDispatchDraw(canvas);
        super.dispatchDraw(canvas);
        mGroupInterceptor.postInterceptDispatchDraw(canvas);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return mGroupInterceptor.preInterceptDispatchTouchEvent(ev) || super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return mGroupInterceptor.interceptInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mGroupInterceptor.interceptTouch(event);
    }
}
