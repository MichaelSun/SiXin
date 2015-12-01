package com.sixin.widgets.layout.scalable;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import com.sixin.widgets.toolkits.ViewGroupInterceptor;

public class ScalableFrameLayout extends FrameLayout implements IScalable {

    private IScalable            mScalable         = new IScalableImpl(this);
    private ViewGroupInterceptor mGroupInterceptor = (ViewGroupInterceptor) mScalable;

    public ScalableFrameLayout(Context context) {
        this(context, null, 0);
    }

    public ScalableFrameLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScalableFrameLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mGroupInterceptor.postInterceptMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        mGroupInterceptor.preInterceptDispatchDraw(canvas);
        super.dispatchDraw(canvas);
        mGroupInterceptor.postInterceptDispatchDraw(canvas);
    }

    @Override
    public void setTransformType(int rotate) {
        mScalable.setTransformType(rotate);
    }

    @Override
    public void startScale() {
        mScalable.startScale();
    }

    @Override
    public void startExpand() {
        mScalable.startExpand();
    }

    @Override
    public void transform(float scale) {
        mScalable.transform(scale);
    }

    @Override
    public float getScale() {
        return mScalable.getScale();
    }

    @Override
    public void setOnScaleListener(OnScaleListener listener) {
        mScalable.setOnScaleListener(listener);
    }

    @Override
    public void setOnExpandListener(OnExpandListener listener) {
        mScalable.setOnExpandListener(listener);
    }
}
