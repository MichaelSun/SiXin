package com.sixin.widgets.layout.toast;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.common.R;
import com.sixin.widgets.layout.awesome.AwesomeFrameLayout;
import com.sixin.widgets.layout.awesome.IAwesome;

import java.util.Timer;
import java.util.TimerTask;

public class ToastLayout extends FrameLayout implements IToast {

    private AwesomeFrameLayout mToast;
    private TextView mTextView;
    private OnVisibilityChangedListener mOnVisibilityChangedListener;

    public ToastLayout(Context context) {
        this(context, null, 0);
    }

    public ToastLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ToastLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onFinishInflate() {
        mTextView = (TextView) findViewById(R.id.text);

        mToast = (AwesomeFrameLayout) View.inflate(getContext(), R.layout.toast_widget, null);

        addView(mToast);

        mToast.top();

        mToast.setTopAnimationListener(new IAwesome.OnTopAnimationListener() {
            @Override
            public void onTopAnimationStart() {
            }

            @Override
            public void onTopAnimationEnd() {
                final OnVisibilityChangedListener listener = mOnVisibilityChangedListener;
                if (listener != null) {
                    listener.onHidden();
                }
            }
        });

        mToast.setOpenAnimationListener(new IAwesome.OnOpenAnimationListener() {
            @Override
            public void onOpenAnimationStart() {
            }

            @Override
            public void onOpenAnimationEnd() {
                final OnVisibilityChangedListener listener = mOnVisibilityChangedListener;
                if (listener != null) {
                    listener.onShown();
                }
            }
        });
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
    }

    @Override
    public void show(long ms) {
        mToast.animateOpen();
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                mToast.animateTop();
            }
        }, ms);
    }

    @Override
    public void show() {
        mToast.animateOpen();
    }

    @Override
    public void hide() {
        mToast.animateTop();
    }

    @Override
    public void setText(String text) {
        mTextView.setText(text);
    }

    @Override
    public String getText() {
        return mTextView.getText().toString();
    }

    @Override
    public void setOnVisibilityChangedListener(OnVisibilityChangedListener listener) {
        mOnVisibilityChangedListener = listener;
    }
}
