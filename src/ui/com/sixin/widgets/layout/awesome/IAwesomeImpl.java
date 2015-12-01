package com.sixin.widgets.layout.awesome;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import com.common.R;
import com.sixin.widgets.toolkits.ViewGroupInterceptor;

/**
 * The implementation of Awesome interface and the interceptions for host view methods.
 */
class IAwesomeImpl extends ViewGroupInterceptor implements IAwesome {

    private static final String TAG = "IAwesomeImpl";

    private final static int MSG_ANIMATE_LEFT = -100;
    private final static int MSG_ANIMATE_RIGHT = -101;
    private final static int MSG_ANIMATE_TOP = -102;
    private final static int MSG_ANIMATE_BOTTOM = -103;
    private final static int MSG_ANIMATE_LEFT_OPEN = -104;
    private final static int MSG_ANIMATE_RIGHT_OPEN = -105;
    private final static int MSG_ANIMATE_TOP_OPEN = -106;
    private final static int MSG_ANIMATE_BOTTOM_OPEN = -107;

    private final static int TRACK_DIRECTION_LEFT_MASK = 0x10 << 2;
    private final static int TRACK_DIRECTION_RIGHT_MASK = 0x10 << 1;
    private final static int TRACK_DIRECTION_HORIZONTAL_MASK = 0x10;
    private final static int TRACK_DIRECTION_TOP_MASK = 0x01 << 2;
    private final static int TRACK_DIRECTION_BOTTOM_MASK = 0x01 << 1;
    private final static int TRACK_DIRECTION_VERTICAL_MASK = 0x01;

    private final static int TAP_THRESHOLD = 25;

    private Context mContext;
    private View mHost;

    private float mLeftOffset;
    private float mRightOffset;
    private float mTopOffset;
    private float mBottomOffset;

    private final int mInterceptTouchEventThreshold;

    private boolean mLeftTapBack;
    private boolean mRightTapBack;
    private boolean mTopTapBack;
    private boolean mBottomTapBack;

    private int mTrackDirection = 0x00;

    private int mPositionState;

    private Rect mLeftFrameForTap = new Rect();
    private Rect mTopFrameForTap = new Rect();
    private Rect mRightFrameForTap = new Rect();
    private Rect mBottomFrameForTap = new Rect();

    /* We store the host's properties to avoid too many get...() methods calling. */
    private int mHostWidth;
    private int mHostHeight;

    /* When layout we store the initial values of mHost.getLeft() and mHost.getTop() because
     * after offset vertically or horizontally, these value will be updated. */
    private int mHostLayoutLeft;
    private int mHostLayoutTop;

    private int mLastDownX;
    private int mLastDownY;
    private int mLastMoveX;
    private int mLastMoveY;
    private boolean mLastMoveXBeenSet;
    private boolean mLastMoveYBeenSet;

    private AnimationHandler mHandler;
    private Animator mAnimator;
    private Tracker mTracker;

    private OnLeftAnimationListener mOnLeftAnimationListener;
    private OnRightAnimationListener mOnRightAnimationListener;
    private OnTopAnimationListener mOnTopAnimationListener;
    private OnBottomAnimationListener mOnBottomAnimationListener;
    private OnOpenAnimationListener mOnOpenAnimationListener;


    IAwesomeImpl(View host) {
        mHost = host;
        mContext = mHost.getContext();
        mHandler = new AnimationHandler();
        mAnimator = new Animator();
        mTracker = new Tracker();
        mPositionState = STATE_EXPAND;

        float density = mContext.getResources().getDisplayMetrics().density;
        mInterceptTouchEventThreshold = (int) (TAP_THRESHOLD * density + 0.5);

    }

    /**
     * Load xml attributes
     *
     * @param attrs the attributes set from xml
     */
    @Override
    public void loadAttrs(AttributeSet attrs) {
        TypedArray a = mContext.obtainStyledAttributes(attrs, R.styleable.Awesome);

        mLeftOffset = a.getDimension(R.styleable.Awesome_left_offset, -1f);
        mRightOffset = a.getDimension(R.styleable.Awesome_right_offset, -1f);
        mTopOffset = a.getDimension(R.styleable.Awesome_top_offset, -1f);
        mBottomOffset = a.getDimension(R.styleable.Awesome_bottom_offset, -1f);

//        Log.d(TAG, "@loadAttrs " + mTopOffset);

        parseTrack(a);
        parseTapBackArea(a);

        a.recycle();

        mHost.setClickable(true);
    }

    private void parseTrack(TypedArray a) {
        final String track = a.getString(R.styleable.Awesome_track);
        if (track != null && track.length() > 0) {
            final String[] tracks = track.split("\\|");
            for (String s : tracks) {
                if (mLeftOffset != -1 && mRightOffset != -1 &&
                        HORIZONTAL.equals(s) && (mTrackDirection & 0xF0) == 0) {
                    mTrackDirection |= TRACK_DIRECTION_HORIZONTAL_MASK;
                } else if ((mRightOffset != -1) && RIGHT.equals(s) && (mTrackDirection & 0xF0) == 0) {
                    mTrackDirection |= TRACK_DIRECTION_RIGHT_MASK;
                } else if ((mLeftOffset != -1) && LEFT.equals(s) && (mTrackDirection & 0xF0) == 0) {
                    mTrackDirection |= TRACK_DIRECTION_LEFT_MASK;
                } else if (mTopOffset != -1 && mBottomOffset != -1 &&
                        VERTICAL.equals(s) && (mTrackDirection & 0x0F) == 0) {
                    mTrackDirection |= TRACK_DIRECTION_VERTICAL_MASK;
                } else if (mTopOffset != -1 && TOP.equals(s) && (mTrackDirection & 0x0F) == 0) {
                    mTrackDirection |= TRACK_DIRECTION_TOP_MASK;
                } else if (mBottomOffset != -1 && BOTTOM.equals(s) && (mTrackDirection & 0x0F) == 0) {
                    mTrackDirection |= TRACK_DIRECTION_BOTTOM_MASK;
                }
            }

        }
    }

    private void parseTapBackArea(TypedArray a) {
        final String tapBackArea = a.getString(R.styleable.Awesome_tap_back_area);
        if (tapBackArea != null && tapBackArea.length() > 0) {
            final String[] taps = tapBackArea.split("\\|");
            for (String s : taps) {
//                Log.d(TAG, "@loadAttrs tap area " + s);
                if (LEFT.equals(s) && mLeftOffset != -1) {
                    mLeftTapBack = true;
                } else if (RIGHT.equals(s) && mRightOffset != -1) {
                    mRightTapBack = true;
                } else if (TOP.equals(s) && mTopOffset != -1) {
                    mTopTapBack = true;
                } else if (BOTTOM.equals(s) && mBottomOffset != -1) {
                    mBottomTapBack = true;
                } else {
//                    Log.d(TAG, "@loadAttrs tap_back_area value illegal");
                }
            }
        }
    }

    /**
     * The offset value when flip to mLeft.
     *
     * @return the mLeft offset
     */
    @Override
    public int getLeftOffset() {
        return (int) mLeftOffset;
    }

    /**
     * The offset value when flip to right.
     *
     * @return the mTop offset
     */
    @Override
    public int getRightOffset() {
        return (int) mRightOffset;
    }

    /**
     * The mTop value when flip to mTop.
     *
     * @return the mTop offset
     */
    @Override
    public int getTopOffset() {
        return (int) mTopOffset;
    }

    /**
     * The bottom value when flip to bottom
     *
     * @return the bottom offset
     */
    @Override
    public int getBottomOffset() {
        return (int) mBottomOffset;
    }

    /**
     * tap left offset area to flip back.
     *
     * @return true if allow tap back
     */
    @Override
    public boolean getLeftTapBack() {
        return mLeftTapBack;
    }

    /**
     * tap right offset area to flip back.
     *
     * @return true if allow tap back
     */
    @Override
    public boolean getRightTapBack() {
        return mRightTapBack;
    }

    /**
     * tap top offset area to flip back.
     *
     * @return true if allow tap back
     */
    @Override
    public boolean getTopTapBack() {
        return mTopTapBack;
    }

    /**
     * tap bottom offset area to flip back.
     *
     * @return true if allow tap back
     */
    @Override
    public boolean getBottomTapBack() {
        return mBottomTapBack;
    }

    /**
     * Set left offset area could tap back.
     *
     * @param tapBack tap back
     */
    @Override
    public void setLeftTapBack(boolean tapBack) {
        mLeftTapBack = tapBack;
    }

    /**
     * Set right offset area could tap back.
     *
     * @param tapBack tap back
     */
    @Override
    public void setRightTapBack(boolean tapBack) {
        mRightTapBack = tapBack;
    }

    /**
     * Set top offset area could tap back.
     *
     * @param tapBack tap back
     */
    @Override
    public void setTopTapBack(boolean tapBack) {
        mTopTapBack = tapBack;
    }

    /**
     * Set bottom offset area could tap back.
     *
     * @param tapBack tap back
     */
    @Override
    public void setBottomTapBack(boolean tapBack) {
        mBottomTapBack = tapBack;
    }

    /**
     * Flip left immediately, without animation.
     */
    @Override
    public void left() {
        if (canLeft()) {
            mHost.offsetLeftAndRight((int) (mLeftOffset - mHostWidth - mHost.getLeft()));
            ((View) mHost.getParent()).invalidate();
        }
    }

    /**
     * Flip right immediately, without animation.
     */
    @Override
    public void right() {
        if (canRight()) {
            mHost.offsetLeftAndRight((int) (mHostWidth - mRightOffset - mHost.getLeft()));
            ((View) mHost.getParent()).invalidate();
        }
    }

    /**
     * Flip top immediately, without animation.
     */
    @Override
    public void top() {
        if (canTop()) {
            mHost.offsetTopAndBottom((int) (mTopOffset - mHostHeight - mHost.getTop()));
            ((View) mHost.getParent()).invalidate();
        }
    }

    /**
     * Flip bottom immediately, without animation.
     */
    @Override
    public void bottom() {
        if (canBottom()) {
            mHost.offsetTopAndBottom((int) (mHostHeight - mBottomOffset - mHost.getTop()));
            ((View) mHost.getParent()).invalidate();
        }
    }

    /**
     * Open host view when flipped.
     */
    @Override
    public void open() {
        mHost.offsetLeftAndRight(mHost.getLeft());
        mHost.offsetTopAndBottom(mHost.getTop());
        ((View) mHost.getParent()).invalidate();
    }

    /**
     * Animation version of flipping
     */
    @Override
    public void animateLeft() {
        if (canLeft()) mAnimator.animateLeft(-mAnimator.velocity);
    }

    /**
     * Animation version of flipping
     */
    @Override
    public void animateRight() {
        if (canRight()) mAnimator.animateRight(mAnimator.velocity);
    }

    /**
     * Animation version of flipping
     */
    @Override
    public void animateTop() {
        if (canTop()) mAnimator.animateTop(-mAnimator.velocity);
    }

    /**
     * Animation version of flipping
     */
    @Override
    public void animateBottom() {
        if (canBottom()) mAnimator.animateBottom(mAnimator.velocity);
    }

    /**
     * Animation version of flipping
     */
    @Override
    public void animateOpen() {
        switch (mPositionState) {
            case STATE_COLLAPSE_LEFT:
                mAnimator.animateLeftOpen(mAnimator.velocity);
                break;
            case STATE_COLLAPSE_RIGHT:
                mAnimator.animateRightOpen(-mAnimator.velocity);
                break;
            case STATE_COLLAPSE_TOP:
                mAnimator.animateTopOpen(mAnimator.velocity);
                break;
            case STATE_COLLAPSE_BOTTOM:
                mAnimator.animateBottomOpen(-mAnimator.velocity);
                break;
            default:
                break;
        }
    }

    /**
     * Get flipping state
     *
     * @return the state
     */
    @Override
    public int getState() {
        return mPositionState;
    }

    @Override
    public void setLeftAnimationListener(OnLeftAnimationListener listener) {
        mOnLeftAnimationListener = listener;
    }

    @Override
    public void setRightAnimationListener(OnRightAnimationListener listener) {
        mOnRightAnimationListener = listener;
    }

    @Override
    public void setTopAnimationListener(OnTopAnimationListener listener) {
        mOnTopAnimationListener = listener;
    }

    @Override
    public void setBottomAnimationListener(OnBottomAnimationListener listener) {
        mOnBottomAnimationListener = listener;
    }

    @Override
    public void setOpenAnimationListener(OnOpenAnimationListener listener) {
        mOnOpenAnimationListener = listener;
    }


    private class AnimationHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            if (!mAnimator.animating) {
                return;
            }
            switch (msg.what) {
                case MSG_ANIMATE_LEFT:
                    mAnimator.computeLeftAnimation();
                    break;
                case MSG_ANIMATE_RIGHT:
                    mAnimator.computeRightAnimation();
                    break;
                case MSG_ANIMATE_TOP:
                    mAnimator.computeTopAnimation();
                    break;
                case MSG_ANIMATE_BOTTOM:
                    mAnimator.computeBottomAnimation();
                    break;
                case MSG_ANIMATE_LEFT_OPEN:
                    mAnimator.computeLeftOpenAnimation();
                    break;
                case MSG_ANIMATE_RIGHT_OPEN:
                    mAnimator.computeRightOpenAnimation();
                    break;
                case MSG_ANIMATE_TOP_OPEN:
                    mAnimator.computeTopOpenAnimation();
                    break;
                case MSG_ANIMATE_BOTTOM_OPEN:
                    mAnimator.computeBottomOpenAnimation();
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * The offset will be reset after every layout, so we do an additional offset when layout based on
     * the position state.
     */
    private void offset() {
        switch (mPositionState) {
            case STATE_EXPAND:
                mHost.offsetLeftAndRight(-mHost.getLeft());
                mHost.offsetTopAndBottom(-mHost.getTop());
                ((View) mHost.getParent()).invalidate();
                break;
            case STATE_COLLAPSE_LEFT:
                mHost.offsetLeftAndRight((int) (mLeftOffset - mHostWidth - mHost.getLeft()));
                ((View) mHost.getParent()).invalidate();
                break;
            case STATE_COLLAPSE_TOP:
                mHost.offsetTopAndBottom((int) (mTopOffset - mHostHeight - mHost.getTop()));
                ((View) mHost.getParent()).invalidate();
                break;
            case STATE_COLLAPSE_RIGHT:
                mHost.offsetLeftAndRight((int) (mHostWidth - mRightOffset - mHost.getLeft()));
                ((View) mHost.getParent()).invalidate();
                break;
            case STATE_COLLAPSE_BOTTOM:
                mHost.offsetTopAndBottom((int) (mHostHeight - mBottomOffset - mHost.getTop()));
                ((View) mHost.getParent()).invalidate();
                break;
        }

    }

    private boolean canLeft() {
        return mLeftOffset != -1 && mPositionState == STATE_EXPAND;
    }

    private boolean canTop() {
        return mTopOffset != -1 && mPositionState == STATE_EXPAND;
    }

    private boolean canBottom() {
        return mBottomOffset != -1 && mPositionState == STATE_EXPAND;
    }

    private boolean canRight() {
        return mRightOffset != -1 && mPositionState == STATE_EXPAND;
    }

    /**
     * Tracker can handle the dragging of the host view.
     */
    private class Tracker {
        static final int VELOCITY_UNIT = 500;
        static final float MIN_VELOCITY = 1000;

        VelocityTracker velocityTracker;

        boolean tracking;
        int direction;
        final int velocityUnit;
        final int minVelocity;

        Tracker() {
            float density = mContext.getResources().getDisplayMetrics().density;
            velocityUnit = (int) (VELOCITY_UNIT * density + 0.5f);
            minVelocity = (int) (MIN_VELOCITY * density + 0.5f);
        }

        void prepareTracking() {
            velocityTracker = VelocityTracker.obtain();
            tracking = true;
        }

        void stopTracking() {
            fling();
            tracking = false;
            if (velocityTracker != null) {
                velocityTracker.recycle();
                velocityTracker = null;
            }
        }

        boolean prepareLeftTrack() {
            if (mPositionState != STATE_EXPAND && mPositionState != STATE_COLLAPSE_LEFT) return false;
            prepareTracking();
            direction = TRACK_DIRECTION_LEFT_MASK;
            return true;
        }

        boolean prepareTopTrack() {
            if (mPositionState != STATE_EXPAND && mPositionState != STATE_COLLAPSE_TOP) return false;
            prepareTracking();
            direction = TRACK_DIRECTION_TOP_MASK;
            return true;
        }

        boolean prepareRightTrack() {
            if (mPositionState != STATE_EXPAND && mPositionState != STATE_COLLAPSE_RIGHT) return false;
            prepareTracking();
            direction = TRACK_DIRECTION_RIGHT_MASK;
            return true;
        }

        boolean prepareBottomTrack() {
            if (mPositionState != STATE_EXPAND && mPositionState != STATE_COLLAPSE_BOTTOM) return false;
            prepareTracking();
            direction = TRACK_DIRECTION_BOTTOM_MASK;
            return true;
        }

        boolean prepareHorizontalTrack() {
            prepareTracking();
            direction = TRACK_DIRECTION_HORIZONTAL_MASK;
            return true;
        }

        boolean prepareVerticalTrack() {
            prepareTracking();
            direction = TRACK_DIRECTION_VERTICAL_MASK;
            return true;
        }

        void move(int xOffset, int yOffset) {
            if (!tracking) return;
//            Log.d(TAG, String.format("@move xOffset %d yOffset %d", xOffset, yOffset));
            final int left = (int) (mHost.getLeft() - xOffset);
            final int top = (int) (mHost.getTop() - yOffset);
//            Log.d(TAG, String.format("@move left %d top %d", left, top));
            switch (direction) {
                case TRACK_DIRECTION_LEFT_MASK:
//                    Log.d(TAG, "@move left");
                    if (left > mLeftOffset - mHostWidth && left < 0) {
                        mHost.offsetLeftAndRight(-xOffset);
                        ((View) mHost.getParent()).invalidate();
                    }
                    break;
                case TRACK_DIRECTION_RIGHT_MASK:
//                    Log.d(TAG, "@move right");
                    if (left < mHostWidth - mRightOffset && left > 0) {
                        mHost.offsetLeftAndRight(-xOffset);
                        ((View) mHost.getParent()).invalidate();
                    }
                    break;
                case TRACK_DIRECTION_TOP_MASK:
//                    Log.d(TAG, "@move top");
                    if (top > mTopOffset - mHostHeight && top < 0) {
                        mHost.offsetTopAndBottom(-yOffset);
                        ((View) mHost.getParent()).invalidate();
                    }
                    break;
                case TRACK_DIRECTION_BOTTOM_MASK:
//                    Log.d(TAG, "@move bottom");
                    if (top < mHostHeight - mBottomOffset && top > 0) {
                        mHost.offsetTopAndBottom(-yOffset);
                        ((View) mHost.getParent()).invalidate();
                    }
                    break;
                case TRACK_DIRECTION_HORIZONTAL_MASK:
//                    Log.d(TAG, "@move horizontal");
                    if (left > mLeftOffset - mHostWidth && left < mHostWidth - mRightOffset) {
                        mHost.offsetLeftAndRight(-xOffset);
                        ((View) mHost.getParent()).invalidate();
                    }
                    break;
                case TRACK_DIRECTION_VERTICAL_MASK:
//                    Log.d(TAG, "@move vertical");
                    if (top > mTopOffset - mHostHeight && top < mHostHeight - mBottomOffset) {
                        mHost.offsetTopAndBottom(-yOffset);
                        ((View) mHost.getParent()).invalidate();
                    }
                    break;
                default:
                    break;
            }
        }

        /**
         * 拖动结束之后，我们会根据速度和位置把View动画到合适的位置。
         */
        private void fling() {
            velocityTracker.computeCurrentVelocity(velocityUnit);
            float xVelocity = velocityTracker.getXVelocity();
            float yVelocity = velocityTracker.getYVelocity();

//            Log.d(TAG, "@fling x " + xVelocity);
//            Log.d(TAG, "@fling y " + yVelocity);

            if (xVelocity < 0) {
                xVelocity = Math.min(xVelocity, -minVelocity);
            } else {
                xVelocity = Math.max(xVelocity, minVelocity);
            }

            if (yVelocity < 0) {
                yVelocity = Math.min(yVelocity, -minVelocity);
            } else {
                yVelocity = Math.max(yVelocity, minVelocity);
            }

            switch (direction) {
                case TRACK_DIRECTION_HORIZONTAL_MASK:
                    horizontalFling(xVelocity);
                    break;
                case TRACK_DIRECTION_LEFT_MASK:
                    leftFling(xVelocity);
                    break;
                case TRACK_DIRECTION_RIGHT_MASK:
                    rightFling(xVelocity);
                    break;
                case TRACK_DIRECTION_TOP_MASK:
                    topFling(yVelocity);
                    break;
                case TRACK_DIRECTION_BOTTOM_MASK:
                    bottomFling(yVelocity);
                    break;
                case TRACK_DIRECTION_VERTICAL_MASK:
                    verticalFling(yVelocity);
                    break;
                default:
                    break;
            }
        }

        private void verticalFling(float velocity) {
            final int top = mHost.getTop();
            if (top <= 0 && top >= mTopOffset - mHostHeight) {
                if (velocity < 0) {
                    mAnimator.animateTop(velocity);
                } else {
                    mAnimator.animateTopOpen(velocity);
                }
            } else if (top >= 0 && top <= mHostHeight - mBottomOffset) {
                if (velocity < 0) {
                    mAnimator.animateBottomOpen(velocity);
                } else {
                    mAnimator.animateBottom(velocity);
                }
            }
        }

        private void bottomFling(float velocity) {
            if (velocity > 0) {
                mAnimator.animateBottom(velocity);
            } else {
                mAnimator.animateBottomOpen(velocity);
            }
        }

        private void horizontalFling(float velocity) {
//            Log.d(TAG, "@horizontalFling");
            final int left = mHost.getLeft();
            if (left <= 0 && left >= mLeftOffset - mHostWidth) {
                if (velocity < 0) {
                    mAnimator.animateLeft(velocity);
                } else {
                    mAnimator.animateLeftOpen(velocity);
                }
            } else if (left >= 0 && left <= mHostWidth - mRightOffset) {
                if (velocity < 0) {
                    mAnimator.animateRightOpen(velocity);
                } else {
                    mAnimator.animateRight(velocity);
                }
            }
        }

        private void leftFling(float velocity) {
//            Log.d(TAG, "@leftFling");
            if (velocity < 0) {
//                Log.d(TAG, "@leftFling animateLeft " + velocity);
                mAnimator.animateLeft(velocity);
            } else {
//                Log.d(TAG, "@leftFling animateLeftOpen " + velocity);
                mAnimator.animateLeftOpen(velocity);
            }
        }

        private void rightFling(float velocity) {
//            Log.d(TAG, "@rightFling");
            if (velocity < 0) {
                mAnimator.animateRightOpen(velocity);
            } else {
                mAnimator.animateRight(velocity);
            }
        }

        private void topFling(float velocity) {
//            Log.d(TAG, "@topFling");
            if (velocity < 0) {
                mAnimator.animateTop(velocity);
            } else {
                mAnimator.animateTopOpen(velocity);
            }
        }
    }

    private class Animator {
        static final String TAG = "IAwesomeImpl$Animator";

        static final int FRAME_ANIMATION_DURATION = 1000 / 60;

        static final int VELOCITY = 1500;
        static final int END_VELOCITY = 500;
        static final int MIN_VELOCITY = 300;
        static final int ACCELERATION = 3000;

        final float velocity;
        final float minVelocity;
        final float endVelocity;
        final float acceleration;

        float animatingDelta;
        float animatingVelocity;
        float animatingAcceleration;
        long lastAnimationTime;
        long currentAnimationTime;
        boolean animating;

        Animator() {
            final float density = mContext.getResources().getDisplayMetrics().density;
            velocity = VELOCITY * density;
            minVelocity = MIN_VELOCITY * density;
            acceleration = ACCELERATION * density;
            endVelocity = END_VELOCITY * density;
        }

        private void compute() {
            final float v = animatingVelocity;
            final long now = SystemClock.uptimeMillis();
            final float t = (now - lastAnimationTime) / 1000f;
            animatingDelta = v * t + animatingAcceleration * t * t * 0.5f;
            animatingVelocity = v + animatingAcceleration * t;
            lastAnimationTime = now;
            currentAnimationTime += FRAME_ANIMATION_DURATION;

            //Log.d(TAG, "@compute position " + animatingDelta + " velocity " + animatingVelocity);
        }

        float computeAcceleration(float d, float velocity) {
            if (Math.abs(d) < 100) return 0;
            float endV = velocity < 0 ? -endVelocity : endVelocity;
            return (endV - velocity) * ((velocity + 0.5f * (endV - velocity)) / d);
        }

        void computeLeftAnimation() {
            compute();
            if (mHost.getLeft() + animatingDelta <= mLeftOffset - mHostWidth) {
                final OnLeftAnimationListener listener = mOnLeftAnimationListener;
                if (listener != null) listener.onLeftAnimationEnd();
                animating = false;
                mPositionState = STATE_COLLAPSE_LEFT;
                offset();
            } else {
                mHost.offsetLeftAndRight((int) animatingDelta);
                ((View) mHost.getParent()).invalidate();
                mHandler.sendMessageAtTime(mHandler.obtainMessage(MSG_ANIMATE_LEFT), currentAnimationTime);
            }
        }

        void computeRightAnimation() {
            compute();
            if (mHost.getLeft() + animatingDelta >= mHostWidth - mRightOffset) {
                final OnRightAnimationListener listener = mOnRightAnimationListener;
                if (listener != null) listener.onRightAnimationEnd();
                animating = false;
                mPositionState = STATE_COLLAPSE_RIGHT;
                offset();
            } else {
                mHost.offsetLeftAndRight((int) animatingDelta);
                ((View) mHost.getParent()).invalidate();
                mHandler.sendMessageAtTime(mHandler.obtainMessage(MSG_ANIMATE_RIGHT), currentAnimationTime);
            }
        }

        void computeTopAnimation() {
            compute();
            if (mHost.getTop() + animatingDelta <= mTopOffset - mHostHeight) {
                final OnTopAnimationListener listener = mOnTopAnimationListener;
                if (listener != null) listener.onTopAnimationEnd();
                animating = false;
                mPositionState = STATE_COLLAPSE_TOP;
                offset();
            } else {
                mHost.offsetTopAndBottom((int) animatingDelta);
                ((View) mHost.getParent()).invalidate();
                mHandler.sendMessageAtTime(mHandler.obtainMessage(MSG_ANIMATE_TOP), currentAnimationTime);
            }
        }

        void computeBottomAnimation() {
            compute();
            if (mHost.getTop() + animatingDelta >= mHostHeight - mBottomOffset) {
                final OnBottomAnimationListener listener = mOnBottomAnimationListener;
                if (listener != null) listener.onBottomAnimationEnd();
                animating = false;
                mPositionState = STATE_COLLAPSE_BOTTOM;
                offset();
            } else {
                mHost.offsetTopAndBottom((int) animatingDelta);
                ((View) mHost.getParent()).invalidate();
                mHandler.sendMessageAtTime(mHandler.obtainMessage(MSG_ANIMATE_BOTTOM), currentAnimationTime);
            }
        }

        void computeLeftOpenAnimation() {
            compute();
            if (mHost.getLeft() + animatingDelta >= 0) {
                final OnOpenAnimationListener listener = mOnOpenAnimationListener;
                if (listener != null) {
                    listener.onOpenAnimationEnd();
                }
                animating = false;
                mPositionState = STATE_EXPAND;
                offset();
            } else {
                mHost.offsetLeftAndRight((int) animatingDelta);
                ((View) mHost.getParent()).invalidate();
                mHandler.sendMessageAtTime(mHandler.obtainMessage(MSG_ANIMATE_LEFT_OPEN), currentAnimationTime);
            }
        }

        void computeRightOpenAnimation() {
            compute();
            if (mHost.getLeft() + animatingDelta <= 0) {
                final OnOpenAnimationListener listener = mOnOpenAnimationListener;
                if (listener != null) listener.onOpenAnimationEnd();
                animating = false;
                mPositionState = STATE_EXPAND;
                offset();
            } else {
                mHost.offsetLeftAndRight((int) animatingDelta);
                ((View) mHost.getParent()).invalidate();
                mHandler.sendMessageAtTime(mHandler.obtainMessage(MSG_ANIMATE_RIGHT_OPEN), currentAnimationTime);
            }
        }

        void computeTopOpenAnimation() {
            compute();
            if (mHost.getTop() + animatingDelta >= 0) {
                final OnOpenAnimationListener listener = mOnOpenAnimationListener;
                if (listener != null) listener.onOpenAnimationEnd();
                animating = false;
                mPositionState = STATE_EXPAND;
                offset();
            } else {
                mHost.offsetTopAndBottom((int) animatingDelta);
                ((View) mHost.getParent()).invalidate();
                mHandler.sendMessageAtTime(mHandler.obtainMessage(MSG_ANIMATE_TOP_OPEN), currentAnimationTime);
            }
        }

        void computeBottomOpenAnimation() {
            compute();
            if (mHost.getTop() + animatingDelta <= 0) {
                final OnOpenAnimationListener listener = mOnOpenAnimationListener;
                if (listener != null) listener.onOpenAnimationEnd();
                animating = false;
                mPositionState = STATE_EXPAND;
                offset();
            } else {
                mHost.offsetTopAndBottom((int) animatingDelta);
                ((View) mHost.getParent()).invalidate();
                mHandler.sendMessageAtTime(mHandler.obtainMessage(MSG_ANIMATE_BOTTOM_OPEN), currentAnimationTime);
            }
        }

        void animateLeftOpen(float velocity) {
            final OnOpenAnimationListener listener = mOnOpenAnimationListener;
            if (listener != null) {
                listener.onOpenAnimationStart();
            }
            animating = true;
            currentAnimationTime = SystemClock.uptimeMillis();
            lastAnimationTime = currentAnimationTime;
            currentAnimationTime += FRAME_ANIMATION_DURATION;
            animatingVelocity = velocity;
            animatingAcceleration = computeAcceleration(-mHost.getLeft(), velocity);
            mHandler.removeMessages(MSG_ANIMATE_LEFT_OPEN);
            mHandler.sendMessageAtTime(mHandler.obtainMessage(MSG_ANIMATE_LEFT_OPEN), currentAnimationTime);
        }

        void animateRightOpen(float velocity) {
            final OnOpenAnimationListener listener = mOnOpenAnimationListener;
            if (listener != null) {
                listener.onOpenAnimationStart();
            }
            animating = true;
            currentAnimationTime = SystemClock.uptimeMillis();
            lastAnimationTime = currentAnimationTime;
            currentAnimationTime += FRAME_ANIMATION_DURATION;
            animatingVelocity = velocity;
            animatingAcceleration = computeAcceleration(-mHost.getLeft(), velocity);
            mHandler.removeMessages(MSG_ANIMATE_RIGHT_OPEN);
            mHandler.sendMessageAtTime(mHandler.obtainMessage(MSG_ANIMATE_RIGHT_OPEN), currentAnimationTime);
        }

        void animateTopOpen(float velocity) {
            animating = true;
            currentAnimationTime = SystemClock.uptimeMillis();
            lastAnimationTime = currentAnimationTime;
            currentAnimationTime += FRAME_ANIMATION_DURATION;
            animatingVelocity = velocity;
            animatingAcceleration = computeAcceleration(-mHost.getTop(), velocity);
            mHandler.removeMessages(MSG_ANIMATE_TOP_OPEN);
            mHandler.sendMessageAtTime(mHandler.obtainMessage(MSG_ANIMATE_TOP_OPEN), currentAnimationTime);
        }

        void animateBottomOpen(float velocity) {
            animating = true;
            currentAnimationTime = SystemClock.uptimeMillis();
            lastAnimationTime = currentAnimationTime;
            currentAnimationTime += FRAME_ANIMATION_DURATION;
            animatingVelocity = velocity;
            animatingAcceleration = computeAcceleration(-mHost.getTop(), velocity);
            mHandler.removeMessages(MSG_ANIMATE_BOTTOM_OPEN);
            mHandler.sendMessageAtTime(mHandler.obtainMessage(MSG_ANIMATE_BOTTOM_OPEN), currentAnimationTime);
        }

        void animateLeft(float velocity) {
            final OnLeftAnimationListener listener = mOnLeftAnimationListener;
            if (listener != null) {
                listener.onLeftAnimationStart();
            }
            animating = true;
            currentAnimationTime = SystemClock.uptimeMillis();
            lastAnimationTime = currentAnimationTime;
            currentAnimationTime += FRAME_ANIMATION_DURATION;
            animatingVelocity = velocity;
            animatingAcceleration = computeAcceleration(mLeftOffset - mHostWidth - mHost.getLeft(), velocity);
            mHandler.sendMessageAtTime(mHandler.obtainMessage(MSG_ANIMATE_LEFT), currentAnimationTime);
        }

        void animateRight(float velocity) {
            final OnRightAnimationListener listener = mOnRightAnimationListener;
            if (listener != null) {
                listener.onRightAnimationStart();
            }
            animating = true;
            currentAnimationTime = SystemClock.uptimeMillis();
            lastAnimationTime = currentAnimationTime;
            currentAnimationTime += FRAME_ANIMATION_DURATION;
            animatingVelocity = velocity;
            animatingAcceleration = computeAcceleration(mHostWidth - mRightOffset - mHost.getLeft(), velocity);
            mHandler.sendMessageAtTime(mHandler.obtainMessage(MSG_ANIMATE_RIGHT), currentAnimationTime);
        }

        void animateTop(float velocity) {
            final OnTopAnimationListener listener = mOnTopAnimationListener;
            if (listener != null) {
                listener.onTopAnimationStart();
            }
            animating = true;
            currentAnimationTime = SystemClock.uptimeMillis();
            lastAnimationTime = currentAnimationTime;
            currentAnimationTime += FRAME_ANIMATION_DURATION;
            animatingVelocity = velocity;
            animatingAcceleration = computeAcceleration(mTopOffset - mHostHeight - mHost.getLeft(), velocity);
            mHandler.sendMessageAtTime(mHandler.obtainMessage(MSG_ANIMATE_TOP), currentAnimationTime);
        }

        void animateBottom(float velocity) {
            final OnBottomAnimationListener listener = mOnBottomAnimationListener;
            if (listener != null) {
                listener.onBottomAnimationStart();
            }
            animating = true;
            currentAnimationTime = SystemClock.uptimeMillis();
            lastAnimationTime = currentAnimationTime;
            currentAnimationTime += FRAME_ANIMATION_DURATION;
            animatingVelocity = velocity;
            animatingAcceleration = computeAcceleration(-mBottomOffset + mHostHeight, velocity);
            mHandler.sendMessageAtTime(mHandler.obtainMessage(MSG_ANIMATE_BOTTOM), currentAnimationTime);
        }
    }

    /**
     * call this method at the end of the onMeasure method body or after the setMeasuredDimension() call.
     *
     * @param widthMeasureSpec  MeasureSpec of width
     * @param heightMeasureSpec MeasureSpec of height
     */
    @Override
    public void postInterceptMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int width = mHost.getMeasuredWidth();
        final int height = mHost.getMeasuredHeight();
//        Log.d(TAG, String.format("@postInterceptMeasure width %d height %d", width, height));

        //check the offsets' sizes are not larger than the view's dimension
        assert width >= mLeftOffset : "mLeft offset should not be larger than the view's width";
        assert width >= mRightOffset : "right offset should not be larger than the view's width";
        assert height >= mTopOffset : "mTop offset should not be larger than the view's height";
        assert height >= mBottomOffset : "bottom offset should not be larger than the view's height";
    }

    /**
     * call this method at the end of the onLayout method body
     *
     * @param changed if layout changed
     * @param l       left side
     * @param t       top side
     * @param r       right side
     * @param b       bottom size
     */
    @Override
    public void postInterceptLayout(boolean changed, int l, int t, int r, int b) {
        mHostWidth = mHost.getWidth();
        mHostHeight = mHost.getHeight();
        mHostLayoutLeft = mHost.getLeft();
        mHostLayoutTop = mHost.getTop();

        if (changed) {
            if (mLeftOffset != -1) {
                mLeftFrameForTap.set((int) (r - mLeftOffset), t, r, b);
            }

            if (mTopOffset != -1) {
                mTopFrameForTap.set(l, (int) (b - mTopOffset), r, b);
            }

            if (mRightOffset != -1) {
                mRightFrameForTap.set(l, t, (int) (l + mRightOffset), b);
            }

            if (mBottomOffset != -1) {
                mBottomFrameForTap.set(l, t, r, (int) (t + mBottomOffset));
            }
        }

        switch (mPositionState) {
            case STATE_COLLAPSE_BOTTOM:
                bottom();
                break;
            case STATE_COLLAPSE_LEFT:
                left();
                break;
            case STATE_COLLAPSE_TOP:
                top();
                break;
            case STATE_COLLAPSE_RIGHT:
                right();
                break;
            case STATE_EXPAND:
                open();
                break;
            default:
                break;
        }
    }

    /**
     * This method must be invoked at the beginning of the host view's method dispatchDraw().
     *
     * @param canvas the canvas.
     */
    @Override
    public void preInterceptDispatchDraw(Canvas canvas) {
    }

    /**
     * This method must be invoked at the end of the host view's method dispatchDraw().
     *
     * @param canvas the canvas
     */
    @Override
    public void postInterceptDispatchDraw(Canvas canvas) {
    }

    /**
     * This method must be invoked at the beginning of the host view's method dispatchTouchEvent().
     *
     * @param ev the event.
     * @return true if consumed, the host's super.dispatchTouchEvent method will not be invoked
     */
    @Override
    public boolean preInterceptDispatchTouchEvent(MotionEvent ev) {
        return false;
    }

    /**
     * This method must be invoked at the beginning of the host view's method onInterceptTouchEvent().
     *
     * @param ev the event.
     * @return true if intercepted
     */
    @Override
    public boolean interceptInterceptTouchEvent(MotionEvent ev) {
        ev.offsetLocation(-mHostLayoutLeft, -mHostLayoutTop);
        final int action = ev.getAction() & MotionEvent.ACTION_MASK;
        final int x = (int) ev.getX();
        final int y = (int) ev.getY();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
//                Log.d(TAG, "@preInterceptDispatchTouchEvent event down");
                mLastDownX = x;
                mLastDownY = y;
                break;
            case MotionEvent.ACTION_MOVE:
//                Log.d(TAG, "@interceptInterceptTouchEvent");

                if ((mTrackDirection & 0xF0) > 0 &&
                        (x < mLastDownX - mInterceptTouchEventThreshold || x > mLastDownX +
                                mInterceptTouchEventThreshold)) {
                    switch (mTrackDirection & 0xF0) {
                        case TRACK_DIRECTION_LEFT_MASK:
                            return mTracker.prepareLeftTrack();
                        case TRACK_DIRECTION_RIGHT_MASK:
                            return mTracker.prepareRightTrack();
                        case TRACK_DIRECTION_HORIZONTAL_MASK:
                            return mTracker.prepareHorizontalTrack();
                        default:
                            break;
                    }
                } else if ((mTrackDirection & 0x0F) > 0 &&
                        (y < mLastDownY - mInterceptTouchEventThreshold || y > mLastDownY +
                                mInterceptTouchEventThreshold)) {
                    switch (mTrackDirection & 0x0F) {
                        case TRACK_DIRECTION_TOP_MASK:
                            return mTracker.prepareTopTrack();
                        case TRACK_DIRECTION_BOTTOM_MASK:
                            return mTracker.prepareBottomTrack();
                        case TRACK_DIRECTION_VERTICAL_MASK:
                            return mTracker.prepareVerticalTrack();
                        default:
                            break;
                    }
                }

                return false;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
//                Log.d(TAG, "@interceptInterceptTouchEvent up cancel");
//                Log.d(TAG, String.format("@interceptInterceptTouchEvent x %d y %d", x, y));
                if (mLastDownX - mInterceptTouchEventThreshold < x &&
                        x < mLastDownX + mInterceptTouchEventThreshold &&
                        mLastDownY - mInterceptTouchEventThreshold < y &&
                        y < mLastDownY + mInterceptTouchEventThreshold) {
                    if (mLeftTapBack && mLeftFrameForTap.contains(x, y) &&
                            mPositionState == STATE_COLLAPSE_LEFT) {
//                        Log.d(TAG, "@preInterceptDispatchTouchEvent mLeft open");
                        mAnimator.animateLeftOpen(mAnimator.velocity);
                    } else if (mTopTapBack && mTopFrameForTap.contains(x, y) &&
                            mPositionState == STATE_COLLAPSE_TOP) {
//                        Log.d(TAG, "@preInterceptDispatchTouchEvent mTop open");
                        mAnimator.animateTopOpen(mAnimator.velocity);
                    } else if (mRightTapBack && mRightFrameForTap.contains(x, y) &&
                            mPositionState == STATE_COLLAPSE_RIGHT) {
//                        Log.d(TAG, "@preInterceptDispatchTouchEvent right open");
                        mAnimator.animateRightOpen(-mAnimator.velocity);
                    } else if (mBottomTapBack && mBottomFrameForTap.contains(x, y) &&
                            mPositionState == STATE_COLLAPSE_BOTTOM) {
//                        Log.d(TAG, "@preInterceptDispatchTouchEvent bottom open");
                        mAnimator.animateBottomOpen(-mAnimator.velocity);
                    } else {
                        return false;
                    }
                    return true;
                }
            default:
                break;
        }
        return false;
    }

    /**
     * This method must be invoked at the beginning of the host view's method onTouchEvent().
     *
     * @param ev the event.
     * @return true if handled
     */
    @Override
    public boolean interceptTouch(MotionEvent ev) {
        final int x = (int) ev.getX();
        final int y = (int) ev.getY();
//        Log.d(TAG, String.format("@interceptTouch x %d, y %d", x, y));
        final int action = ev.getAction() & MotionEvent.ACTION_MASK;

        switch (action) {
            case MotionEvent.ACTION_MOVE:
                if (mTracker.tracking) {
                    if (!mLastMoveXBeenSet) {
                        if (x > mLastDownX) {
                            mLastMoveX = mLastDownX + mInterceptTouchEventThreshold;
                            mLastMoveXBeenSet = true;
                        } else {
                            mLastMoveX = mLastDownX - mInterceptTouchEventThreshold;
                            mLastMoveXBeenSet = true;
                        }
                    }

                    if (!mLastMoveYBeenSet) {
                        if (y > mLastDownY) {
                            mLastMoveY = mLastDownY + mInterceptTouchEventThreshold;
                            mLastMoveYBeenSet = true;
                        } else {
                            mLastMoveY = mLastDownY - mInterceptTouchEventThreshold;
                            mLastMoveYBeenSet = true;
                        }
                    }

                    mTracker.move(mLastMoveX - x, mLastMoveY - y);
                    ev.offsetLocation(mHost.getLeft(), mHost.getTop());
                    mTracker.velocityTracker.addMovement(ev);
                }
                break;
            case MotionEvent.ACTION_UP:
//                Log.d(TAG, "@interceptTouch stop tracking and do fling");
                if (mTracker.tracking) {
                    mTracker.stopTracking();
                    mLastMoveXBeenSet = false;
                    mLastMoveYBeenSet = false;
                    return true;
                }
            default:
                return false;
        }
        return true;
    }

}
