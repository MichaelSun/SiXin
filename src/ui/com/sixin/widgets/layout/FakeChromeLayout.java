package com.sixin.widgets.layout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class FakeChromeLayout extends ViewGroup {
    private static final String TAG = "FakeChromeLayout";

    private List<View> mViews;
    private View mActive;

    public FakeChromeLayout(Context context) {
        this(context, null, 0);
    }

    public FakeChromeLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FakeChromeLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void addView(View child) {
        super.addView(child);
        mViews.add(child);
        if (mActive == null) {
            mActive = child;
        }
    }

    @Override
    public void removeView(View view) {
        super.removeView(view);
        mViews.remove(view);
    }

    @Override
    public void removeAllViews() {
        super.removeAllViews();
        mViews.clear();
    }

    private void setActive(View view) {
        if (mViews.contains(view)) {
            mActive = view;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        final int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        for (View v : mViews) {
            if (v.getLayoutParams() != null) {
                measureChild(v, widthMeasureSpec, heightMeasureSpec);
            }
        }

        setMeasuredDimension(widthSize, heightSize);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        final int width = r - l;
        final int height = b - t;

        if (mActive != null) {
            final int count = mViews.size();
            final int i = mViews.indexOf(mActive);

            View pre = i == 0 ? mViews.get(count - 1) : mViews.get(i - 1);
            pre.layout(l - width, t, l, b);

            View next = mViews.get((i + 1) % count);
            next.layout(r, t, r + width, b);

            mActive.layout(l, t, r, b);
        }
    }


}
