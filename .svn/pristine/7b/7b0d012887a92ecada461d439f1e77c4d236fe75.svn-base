package com.sixin.widgets.toolkits;

import android.graphics.Canvas;
import android.view.MotionEvent;

/**
 * Interceptor methods to call in a custom view's methods. Extends this if you want more interception.
 * <p/>
 * post...() means to be called at the end of the target method
 * <p/>
 * pre...() means to be called at the beginning of the target method
 * <p/>
 * no prefix means to replace the super call.
 */
public abstract class ViewGroupInterceptor {

    public abstract void postInterceptMeasure(int widthMeasureSpec, int heightMeasureSpec);

    public abstract void postInterceptLayout(boolean changed, int l, int t, int r, int b);

    public abstract void preInterceptDispatchDraw(Canvas canvas);

    public abstract void postInterceptDispatchDraw(Canvas canvas);

    public abstract boolean preInterceptDispatchTouchEvent(MotionEvent ev);

    public abstract boolean interceptInterceptTouchEvent(MotionEvent ev);

    public abstract boolean interceptTouch(MotionEvent ev);
}
