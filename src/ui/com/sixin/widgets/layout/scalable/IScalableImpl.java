package com.sixin.widgets.layout.scalable;

import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.View;
import com.sixin.widgets.toolkits.ViewGroupInterceptor;

public class IScalableImpl extends ViewGroupInterceptor implements IScalable {
    public static final float SCALE = 1f;

    private static final String TAG = "IScalableImpl";

    private static GradientDrawable SHADE;

    private static final int SHADE_HEIGHT      = 20; // dips
    private static final int DEGREE            = 15;
    private static final int DEPTH_CONSTANT    = 25;
    private static final int HORIZONTAL_OFFSET = 15;

    private static final int MSG_EXPAND = 0xF9;
    private static final int MSG_SCALE  = 0xFA;

    private static final int FRAME_ANIMATION_DURATION = 1000 / 60;

    private int   mState;
    private int   mType;
    private float mScale;

    private final int mDepthConstant;
    private final int mHorizontalOffset;

    private final View mHost;

    private final Animator        mAnimator;
    private final AnimatorHandler mHandler;
    private final Camera          mCamera;
    private final Matrix          mMatrix;

    private long mCurrentAnimationTime;
    private long mLastAnimationTime;

    private int mCenterX;
    private int mCenterY;

    private OnScaleListener  mOnScaleListener;
    private OnExpandListener mOnExpandListener;


    IScalableImpl(View host) {
        mHost = host;
        mHost.setClickable(true);
        mState = STATE_EXPAND;
        mType = NO_ROTATE;
        mScale = 0;
        mAnimator = new Animator();
        mHandler = new AnimatorHandler();
        mCamera = new Camera();
        mMatrix = new Matrix();

        final float density = mHost.getResources().getDisplayMetrics().density;
        mDepthConstant = (int) (density * DEPTH_CONSTANT + 0.5f);
        mHorizontalOffset = (int) (density * HORIZONTAL_OFFSET + 0.5f);
    }

    @Override
    public float getScale() {
        return mScale;
    }

    @Override
    public void setOnExpandListener(OnExpandListener listener) {
        mOnExpandListener = listener;
    }

    @Override
    public void setOnScaleListener(OnScaleListener listener) {
        mOnScaleListener = listener;
    }

// --------------------- Interface IScalable ---------------------

    @Override
    public void setTransformType(int type) {
        mType = type;
    }

    @Override
    public void startScale() {
        if (mState != STATE_SCALED && !mAnimator.animating) {
            mAnimator.animateScale(0f);
        }
    }

    @Override
    public void startExpand() {
        if (!mAnimator.animating) {
            mAnimator.animateExpand(mScale);
        }
    }

    @Override
    public void transform(float scale) {
        if (scale <= SCALE && scale >= 0f) {
            mScale = scale;
        } else if (scale > SCALE) {
            mScale = SCALE;
        } else if (scale < 0f) {
            mScale = 0;
        }
        mHost.invalidate();
    }

    @Override
    public boolean interceptInterceptTouchEvent(MotionEvent ev) {
        return false;
    }

    @Override
    public boolean interceptTouch(MotionEvent ev) {
        return false;
    }


    @Override
    public void postInterceptLayout(boolean changed, int l, int t, int r, int b) {

    }

    @Override
    public void postInterceptMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mCenterX = mHost.getMeasuredWidth() / 2;
        mCenterY = mHost.getMeasuredHeight() / 2;

        // construction of shade drawable.
        if (SHADE == null) {
            SHADE = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM,
                    new int[]{0x66000000, 0x00000000});
            final float density = mHost.getResources().getDisplayMetrics().density;
            final int height = (int) (SHADE_HEIGHT * density + 0.5f);
            SHADE.setBounds(0, 0, mHost.getMeasuredWidth(), height);
        }
    }

    @Override
    public void preInterceptDispatchDraw(Canvas canvas) {
        final int centerX = mCenterX;
        final int centerY = mCenterY;
        canvas.save();
        mCamera.save();
        switch (mType) {
            case LEFT_ROTATE:
                mCamera.rotateY(Math.min(0, (0.7f - mScale) * DEGREE));
                mCamera.translate(-mScale * mHorizontalOffset, 0f, Math.min(mDepthConstant, mScale * mDepthConstant));
                break;
            case NO_ROTATE:
                mCamera.translate(0.0f, 0.0f, Math.min(mDepthConstant, mScale * mDepthConstant));
                break;
            case RIGHT_ROTATE:
                mCamera.rotateY(Math.max(0, (-0.7f + mScale) * DEGREE));
                mCamera.translate(mScale * mHorizontalOffset, 0f, Math.min(mDepthConstant, mScale * mDepthConstant));
                break;
            default:
                break;
        }
        mCamera.getMatrix(mMatrix);
        mMatrix.preTranslate(-centerX, -centerY);
        mMatrix.postTranslate(centerX, centerY);
        mCamera.restore();
        canvas.concat(mMatrix);
    }

    @Override
    public void postInterceptDispatchDraw(Canvas canvas) {
        if (mScale != 0) {
            SHADE.draw(canvas);
        }
        canvas.restore();
    }

    @Override
    public boolean preInterceptDispatchTouchEvent(MotionEvent ev) {
        // 动画的时候，不处理任何触摸事件
        return mAnimator.animating;
    }

    private class AnimatorHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_EXPAND:
                    mAnimator.computeExpand();
                    break;
                case MSG_SCALE:
                    mAnimator.computeScale();
                    break;
                default:
                    break;
            }
        }
    }

    private class Animator {
        static final String TAG = "IScalableImpl$Animator";

        static final float VELOCITY = 3.8f;

        float   velocity;
        float   animatingPosition;
        float   animatingVelocity;
        boolean animating;

        Animator() {
            velocity = VELOCITY;
        }

        void compute() {
            final long now = SystemClock.uptimeMillis();
            final float t = (now - mLastAnimationTime) / 1000f;
            mLastAnimationTime = now;
            mCurrentAnimationTime = mLastAnimationTime;
            animatingPosition += animatingVelocity * t;
        }

        void computeScale() {
            compute();
            if (animatingPosition >= SCALE) {
                mScale = SCALE;
                mState = STATE_SCALED;
                animating = false;
                mType = NO_ROTATE;

                OnScaleListener listener = mOnScaleListener;
                if (listener != null) {
                    listener.onScaled();
                }
            } else {
                mScale = animatingPosition;
                mCurrentAnimationTime += FRAME_ANIMATION_DURATION;
                mHandler.sendMessageAtTime(mHandler.obtainMessage(MSG_SCALE), mCurrentAnimationTime);
            }
            mHost.invalidate();
        }

        void computeExpand() {
            compute();
            if (animatingPosition <= 0) {
                mScale = 0;
                mState = STATE_EXPAND;
                animating = false;
                mType = NO_ROTATE;

                OnExpandListener listener = mOnExpandListener;
                if (listener != null) {
                    listener.onExpanded();
                }
            } else {
                mScale = animatingPosition;
                mCurrentAnimationTime += FRAME_ANIMATION_DURATION;
                mHandler.sendMessageAtTime(mHandler.obtainMessage(MSG_EXPAND), mCurrentAnimationTime);
            }
            mHost.invalidate();
        }

        void animateScale(float scale) {
//            Log.d(TAG, "@animateScale");
            animatingPosition = scale;
            animatingVelocity = velocity;
            animating = true;
            final long now = SystemClock.uptimeMillis();
            mLastAnimationTime = now;
            mCurrentAnimationTime = now + FRAME_ANIMATION_DURATION;
            mHandler.sendMessageAtTime(mHandler.obtainMessage(MSG_SCALE), mCurrentAnimationTime);
        }

        void animateExpand(float scale) {
//            Log.d(TAG, "@animateExpand");
            animatingPosition = scale;
            animatingVelocity = -velocity;
            animating = true;
            final long now = SystemClock.uptimeMillis();
            mLastAnimationTime = now;
            mCurrentAnimationTime = now + FRAME_ANIMATION_DURATION;
            mHandler.sendMessageAtTime(mHandler.obtainMessage(MSG_EXPAND), mCurrentAnimationTime);
        }
    }
}
