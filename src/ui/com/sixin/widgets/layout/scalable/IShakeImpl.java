package com.sixin.widgets.layout.scalable;

import android.graphics.Canvas;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.View;
import com.sixin.widgets.toolkits.ViewGroupInterceptor;

public class IShakeImpl extends ViewGroupInterceptor implements IShakable {
    private static final String TAG = "IShakeImpl";

    private static final int MSG_ANIMATE_SHAKE = 1000;

    private static final int FRAME_ANIMATION_DURATION = 1000 / 60; // ms

    private static final XYVelocity[] SHAKE_VELOCITIES = {
            new XYVelocity(-951, 309), new XYVelocity(1000, 0),
            new XYVelocity(-587, -809), new XYVelocity(309, -951),
            new XYVelocity(951, 309), new XYVelocity(-587, 809),
            new XYVelocity(951, -309)
    };

    private static final Point[] SHAKE_POINTS = {
            new Point(-95, 30), new Point(95, 30), new Point(-58, -80),
            new Point(0, 100), new Point(58, -80), new Point(-95, 30)
    };

    private final XYVelocity[] mShakeVelocities;

    private boolean mShaking;

    private View mHost;

    private int mShakeXPosition;
    private int mShakeYPosition;

    private ShakeAnimator mShakeAnimator;
    private ShakeHandler  mShakeHandler;

    private static class XYVelocity {
        int xVelocity;
        int yVelocity;

        XYVelocity(int x, int y) {
            xVelocity = x;
            yVelocity = y;
        }
    }

    private static class Point {
        int x;
        int y;

        Point(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    public IShakeImpl(View host) {
        mHost = host;

        mShakeAnimator = new ShakeAnimator();
        mShakeHandler = new ShakeHandler();

        mShakeVelocities = new XYVelocity[SHAKE_VELOCITIES.length];

        final float density = mHost.getResources().getDisplayMetrics().density;
        for (int i = 0, count = SHAKE_VELOCITIES.length; i < count; i++) {
            mShakeVelocities[i] =
                    new XYVelocity((int) (SHAKE_VELOCITIES[i].xVelocity * density + 0.5f),
                            (int) (SHAKE_VELOCITIES[i].yVelocity + density + 0.5f));
        }
    }

    private class ShakeAnimator {
        boolean animating;
        long    lastAnimationTime;
        long    currentAnimatingTime;

        int animatingXVelocity;
        int animatingYVelocity;
        int animatingXPosition;
        int animatingYPosition;

        int index;

        private void animate() {
            index = 0;
            animatingXVelocity = SHAKE_VELOCITIES[index].xVelocity;
            animatingYVelocity = SHAKE_VELOCITIES[index].yVelocity;
            animatingXPosition = 0;
            animatingYPosition = 0;
            final long now = SystemClock.uptimeMillis();
            lastAnimationTime = now;
            currentAnimatingTime = now + FRAME_ANIMATION_DURATION;
            mShakeHandler.sendMessageAtTime(mShakeHandler.obtainMessage(MSG_ANIMATE_SHAKE), currentAnimatingTime);
        }

        private void compute() {
            final long now = SystemClock.uptimeMillis();
            final float t = (now - lastAnimationTime) / 1000f;
            animatingXPosition += t * animatingXVelocity;
            animatingYPosition += t * animatingYVelocity;
            lastAnimationTime = now;
            currentAnimatingTime = now + FRAME_ANIMATION_DURATION;
            mShakeHandler.sendMessageAtTime(mShakeHandler.obtainMessage(MSG_ANIMATE_SHAKE), currentAnimatingTime);
        }
    }

    private class ShakeHandler extends Handler {
        @Override
        public void dispatchMessage(Message msg) {
            if (msg.what == MSG_ANIMATE_SHAKE) {
                mShakeAnimator.compute();
            }
        }
    }

    @Override
    public void startShake(int duration) {
        if (mShaking) {
            return;
        }
    }

    @Override
    public void stopShake() {
        if (!mShaking) {
            return;
        }
    }

    @Override
    public void postInterceptMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    }

    @Override
    public void postInterceptLayout(boolean changed, int l, int t, int r, int b) {
    }

    @Override
    public void preInterceptDispatchDraw(Canvas canvas) {
        canvas.save();
        canvas.translate(mShakeXPosition, mShakeYPosition);
    }

    @Override
    public void postInterceptDispatchDraw(Canvas canvas) {
        canvas.restore();
    }

    @Override
    public boolean preInterceptDispatchTouchEvent(MotionEvent ev) {
        return false;
    }

    @Override
    public boolean interceptInterceptTouchEvent(MotionEvent ev) {
        return false;
    }

    @Override
    public boolean interceptTouch(MotionEvent ev) {
        return false;
    }
}
