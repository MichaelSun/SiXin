package com.sixin.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.common.R;


/**
 * Description:This is a component View,it has 2 ImageViews in it,one is used for main image,one is used for assistant
 * image,you can use this view at somewhere like contact,simply show head and online status at same time.This view also
 * support fade in animation when first time be created or change image(enable_animation = true).
 *
 * @autor Chengran Sun
 */
public class StatusImageView extends ViewGroup {

    private static final String TAG = "StatusImageView";

    //main image layout coordinate
    private int mMainImage_l;
    private int mMainImage_t;
    private int mMainImage_r;
    private int mMainImage_b;

    //status image layout coordinate
    private int mStatusImage_l;
    private int mStatusImage_t;
    private int mStatusImage_r;
    private int mStatusImage_b;


    /**
     * the offset in X that relative to the attached corner
     */
    private int mExtendOffset_X;
    /**
     * the offset in Y that relative to the attached corner
     */
    private int mExtendOffset_Y;
    /**
     * the point where status image attach to main image
     */
    private String mAttachedCorner;
    private ImageView mMainImage;
    private ImageView mStatusImage;
    private float mMainImageWidth;
    private float mMainImageHeight;
    private float mStatusImageWidth;
    private float mStatusImageHeight;

    private boolean mEnableAnimation;

    private static final int MSG_ANIMATE_FADE_IN_MAIN = -100;
    private static final int MSG_ANIMATE_FADE_IN_STATUS = -101;

    private Animator mAnimator;
    private AnimationHandler mHandler;


    /**
     * locate status image on the top left corner
     */
    public static final String LT_CORNER = "lt_corner";
    /**
     * locate status image on the bottom left corner
     */
    public static final String LB_CONNER = "lb_corner";
    /**
     * locate status image on the top right corner
     */
    public static final String RT_CONNER = "rt_corner";
    /**
     * locate status image on the bottom right corner
     */
    public static final String RB_CONNER = "rb_corner";

    public StatusImageView(Context context) {
        this(context, null, 0);
    }

    public StatusImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        mMainImage = new ImageView(context, attrs, defStyle);

//        mMainImage.setBackgroundColor(Color.RED);

        mStatusImage = new ImageView(context, attrs, defStyle);

//        mStatusImage.setBackgroundColor(Color.BLUE);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.StatusImageView);

        mAttachedCorner = ta.getString(R.styleable.StatusImageView_attached_corner);

        Drawable mainDrawable = ta.getDrawable(R.styleable.StatusImageView_main_src);
        Drawable statusDrawable = ta.getDrawable(R.styleable.StatusImageView_status_src);
        if (mainDrawable != null) {
            mMainImage.setImageDrawable(mainDrawable);
        }

        if (statusDrawable != null) {
            mStatusImage.setImageDrawable(statusDrawable);
        }


        mMainImageWidth = ta.getDimension(R.styleable.StatusImageView_main_image_width, 0.0f);
        mMainImageHeight = ta.getDimension(R.styleable.StatusImageView_main_image_height, 0.0f);
        mStatusImageWidth = ta.getDimension(R.styleable.StatusImageView_status_image_width, 0.0f);
        mStatusImageHeight = ta.getDimension(R.styleable.StatusImageView_status_image_height, 0.0f);

        mExtendOffset_X = (int) ta.getDimension(R.styleable.StatusImageView_extend_offset_X, 0.0f);
        mExtendOffset_Y = (int) ta.getDimension(R.styleable.StatusImageView_extend_offset_Y, 0.0f);

        mEnableAnimation = ta.getBoolean(R.styleable.StatusImageView_enable_animation, true);


        ta.recycle();

        mAnimator = new Animator();
        mHandler = new AnimationHandler();

        addView(mMainImage);
        addView(mStatusImage);

        mMainImage.setScaleType(ImageView.ScaleType.FIT_XY);
        mStatusImage.setScaleType(ImageView.ScaleType.FIT_XY);

        if (mEnableAnimation) {
            mAnimator.fadeInMainImage();
            mAnimator.fadeInStatusImage();
        }

//        this.setBackgroundColor(Color.GREEN);
    }


    public StatusImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public void setMainImageResource(int mainImageResource) {
        if (mEnableAnimation) {
            mHandler.removeMessages(MSG_ANIMATE_FADE_IN_MAIN);
            ;
            mAnimator.fadeInMainImage();
        }
        mMainImage.setImageResource(mainImageResource);
    }

    public void setMainImageBitmap(Bitmap mainImageBitmap) {
        if (mEnableAnimation) {
            mHandler.removeMessages(MSG_ANIMATE_FADE_IN_MAIN);
            ;
            mAnimator.fadeInMainImage();
        }
        mMainImage.setImageBitmap(mainImageBitmap);
    }

    public void setMainImageDrawable(Drawable mainImageDrawable) {
        if (mEnableAnimation) {
            mHandler.removeMessages(MSG_ANIMATE_FADE_IN_MAIN);
            ;
            mAnimator.fadeInMainImage();
        }
        mMainImage.setImageDrawable(mainImageDrawable);
    }

    public void setStatusImageResource(int statusImageResource) {
        if (mEnableAnimation) {
            mHandler.removeMessages(MSG_ANIMATE_FADE_IN_STATUS);
            ;
            mAnimator.fadeInStatusImage();
        }
        mStatusImage.setImageResource(statusImageResource);
    }

    public void setStatusImageBitmap(Bitmap statusImageBitmap) {
        if (mEnableAnimation) {
            mHandler.removeMessages(MSG_ANIMATE_FADE_IN_STATUS);
            ;
            mAnimator.fadeInStatusImage();
        }
        mStatusImage.setImageBitmap(statusImageBitmap);
    }

    public void setStatusImageDrawable(Drawable statusImageDrawable) {
        if (mEnableAnimation) {
            mHandler.removeMessages(MSG_ANIMATE_FADE_IN_STATUS);
            ;
            mAnimator.fadeInStatusImage();
        }
        mStatusImage.setImageDrawable(statusImageDrawable);
    }

    public void setStatusImageScaleType(ImageView.ScaleType statusImageScaleType) {
        mStatusImage.setScaleType(statusImageScaleType);
    }

    public void setMainImageScaleType(ImageView.ScaleType mainImageScaleType) {
        mMainImage.setScaleType(mainImageScaleType);
    }

    public ImageView.ScaleType getStatusImageScaleType() {
        return mStatusImage.getScaleType();
    }

    public ImageView.ScaleType getMainImageScaleType() {
        return mMainImage.getScaleType();
    }

    public Drawable getStatusImageDrawable() {
        return mStatusImage.getDrawable();
    }

    public Drawable getMainImageDrawable() {
        return mMainImage.getDrawable();
    }

    public float getStatusImageHeight() {
        return mStatusImageHeight;
    }

    public void setStatusImageHeight(float mStatusImageHeight) {
        this.mStatusImageHeight = mStatusImageHeight;
        requestLayout();
        invalidate();
    }

    public float getStatusImageWidth() {
        return mStatusImageWidth;
    }

    public void setStatusImageWidth(float mStatusImageWidth) {
        this.mStatusImageWidth = mStatusImageWidth;
        requestLayout();
        invalidate();
    }

    public float getMainImageHeight() {
        return mMainImageHeight;
    }

    public void setMainImageHeight(float mMainImageHeight) {
        this.mMainImageHeight = mMainImageHeight;
        requestLayout();
        invalidate();
    }

    public float getMainImageWidth() {
        return mMainImageWidth;
    }

    public void setMainImageWidth(float mMainImageWidth) {
        this.mMainImageWidth = mMainImageWidth;
        requestLayout();
        invalidate();
    }

    public String getAttachedCorner() {
        return mAttachedCorner;
    }

    public void setAttachedCorner(String mAttachedCorner) {
        this.mAttachedCorner = mAttachedCorner;
        requestLayout();
        invalidate();
    }

    public int getExtendOffset_X() {
        return mExtendOffset_X;
    }

    public void setExtendOffset_X(int ExtendOffset_X) {
        this.mExtendOffset_X = ExtendOffset_X;
        requestLayout();
        invalidate();
    }

    public int getExtendOffset_Y() {
        return mExtendOffset_Y;
    }

    public void setExtendOffset_Y(int ExtendOffset_Y) {
        this.mExtendOffset_Y = ExtendOffset_Y;
        requestLayout();
        invalidate();
    }

    public boolean isEnableAnimation() {
        return mEnableAnimation;
    }

    public void setEnableAnimation(boolean mEnableAnimation) {
        this.mEnableAnimation = mEnableAnimation;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int statusImageWidth = mStatusImage.getMeasuredWidth();
        int statusImageHeight = mStatusImage.getMeasuredHeight();
        int offset_X = statusImageWidth >> 1;
        int offset_Y = statusImageHeight >> 1;
        int width = r - l;
        int height = b - t;

        mMainImage_l = offset_X;
        mMainImage_t = offset_Y;
        mMainImage_r = width - offset_X;
        mMainImage_b = height - offset_Y;

        mMainImage.layout(mMainImage_l, mMainImage_t, mMainImage_r, mMainImage_b);

        if (LT_CORNER.equals(mAttachedCorner)) {

            if (mExtendOffset_X > 0 && mExtendOffset_Y > 0 &&
                    mExtendOffset_X <= width - statusImageWidth &&
                    mExtendOffset_Y <= height - statusImageHeight) {
                mStatusImage_l = mExtendOffset_X;
                mStatusImage_t = mExtendOffset_Y;
                mStatusImage_r = statusImageWidth + mExtendOffset_X;
                mStatusImage_b = statusImageHeight + mExtendOffset_Y;

            } else {
                mStatusImage_l = 0;
                mStatusImage_t = 0;
                mStatusImage_r = statusImageWidth;
                mStatusImage_b = statusImageHeight;

            }

            mStatusImage.layout(mStatusImage_l, mStatusImage_t,
                    mStatusImage_r, mStatusImage_b);

        } else if (LB_CONNER.equals(mAttachedCorner)) {

            if (mExtendOffset_X > 0 && mExtendOffset_Y < 0 &&
                    mExtendOffset_X <= width - statusImageWidth &&
                    mExtendOffset_Y >= statusImageHeight - height) {
                mStatusImage_l = mExtendOffset_X;
                mStatusImage_t = height - statusImageHeight + mExtendOffset_Y;
                mStatusImage_r = statusImageWidth + mExtendOffset_X;
                mStatusImage_b = height + mExtendOffset_Y;

            } else {
                mStatusImage_l = 0;
                mStatusImage_t = height - statusImageHeight;
                mStatusImage_r = statusImageWidth;
                mStatusImage_b = height;

            }

            mStatusImage.layout(mStatusImage_l, mStatusImage_t,
                    mStatusImage_r, mStatusImage_b);

        } else if (RT_CONNER.equals(mAttachedCorner)) {

            if (mExtendOffset_X < 0 && mExtendOffset_Y > 0 &&
                    mExtendOffset_X >= statusImageWidth - width &&
                    mExtendOffset_Y <= height - statusImageHeight) {
                mStatusImage_l = width - statusImageWidth + mExtendOffset_X;
                mStatusImage_t = mExtendOffset_Y;
                mStatusImage_r = width + mExtendOffset_X;
                mStatusImage_b = statusImageHeight + mExtendOffset_Y;

            } else {
                mStatusImage_l = width - statusImageWidth;
                mStatusImage_t = 0;
                mStatusImage_r = width;
                mStatusImage_b = statusImageHeight;

            }

            mStatusImage.layout(mStatusImage_l, mStatusImage_t,
                    mStatusImage_r, mStatusImage_b);

        } else if (RB_CONNER.equals(mAttachedCorner)) {

            if (mExtendOffset_X < 0 && mExtendOffset_Y < 0 &&
                    mExtendOffset_X >= statusImageWidth - width &&
                    mExtendOffset_Y >= statusImageHeight - height) {
                mStatusImage_l = width - statusImageWidth + mExtendOffset_X;
                mStatusImage_t = height - statusImageHeight + mExtendOffset_Y;
                mStatusImage_r = width + mExtendOffset_X;
                mStatusImage_b = height + mExtendOffset_Y;

            } else {
                mStatusImage_l = width - statusImageWidth;
                mStatusImage_t = height - statusImageHeight;
                mStatusImage_r = width;
                mStatusImage_b = height;

            }
            mStatusImage.layout(mStatusImage_l, mStatusImage_t,
                    mStatusImage_r, mStatusImage_b);

        } else {
            //no defined or illegal value in xml,default locate on the bottom right corner.

            if (mExtendOffset_X < 0 && mExtendOffset_Y < 0 &&
                    mExtendOffset_X >= statusImageWidth - width &&
                    mExtendOffset_Y >= statusImageHeight - height) {
                mStatusImage_l = width - statusImageWidth + mExtendOffset_X;
                mStatusImage_t = height - statusImageHeight + mExtendOffset_Y;
                mStatusImage_r = width + mExtendOffset_X;
                mStatusImage_b = height + mExtendOffset_Y;

            } else {
                mStatusImage_l = width - statusImageWidth;
                mStatusImage_t = height - statusImageHeight;
                mStatusImage_r = width;
                mStatusImage_b = height;

            }
            mStatusImage.layout(mStatusImage_l, mStatusImage_t,
                    mStatusImage_r, mStatusImage_b);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        int statusImageWidthHalf = (int) mStatusImageWidth >> 1;
        int statusImageHeightHalf = (int) mStatusImageHeight >> 1;

        if (mMainImageWidth > 0 && mStatusImageWidth > 0
                && mMainImageWidth + statusImageWidthHalf <= widthSize
                && mMainImageHeight > 0 && mStatusImageHeight > 0
                && mMainImageHeight + statusImageHeightHalf <= heightSize) {
            measureChild(mMainImage, (int) mMainImageWidth + widthMode,
                    (int) mMainImageHeight + heightMode);
            measureChild(mStatusImage, (int) mStatusImageWidth + widthMode,
                    (int) mStatusImageHeight + heightMode);

        } else {
            measureChild(mMainImage, widthMeasureSpec, heightMeasureSpec);
            measureChild(mStatusImage, widthMeasureSpec, heightMeasureSpec);
        }

        int width = 0, height = 0;
        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else if (widthMode == MeasureSpec.AT_MOST) {
            width = mMainImage.getMeasuredWidth() + (mStatusImage.getMeasuredWidth() >> 1);
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else if (heightMode == MeasureSpec.AT_MOST) {
            height = mMainImage.getMeasuredHeight() + (mStatusImage.getMeasuredHeight() >> 1);
        }
        setMeasuredDimension(width, height);
    }

    private class Animator {

        private static final String TAG = "Animator";

        private long mMainTime;
        private long mStatusTime;
        private int mFadeInAlpha4Main = 0;
        private int mFadeInAlpha4Status = 0;

        private static final int MAX_ALPHA = 255;
        private static final int ALPHA_VELOCITY = 5;
        private static final int ANIMATION_FRAME_DURATION = 1000 / 60;

        @SuppressWarnings({"deprecation"})
        private void fadeInStatusImage() {

            mStatusTime = SystemClock.uptimeMillis() + ANIMATION_FRAME_DURATION;
            mFadeInAlpha4Status += ALPHA_VELOCITY;

            mStatusImage.setAlpha(mFadeInAlpha4Status < MAX_ALPHA ? mFadeInAlpha4Status : MAX_ALPHA);

            if (mFadeInAlpha4Status < MAX_ALPHA) {
                mHandler.sendMessageAtTime(mHandler.obtainMessage(MSG_ANIMATE_FADE_IN_STATUS), mStatusTime);
            } else {
                mFadeInAlpha4Status = 0;
                mHandler.removeMessages(MSG_ANIMATE_FADE_IN_STATUS);
            }

            invalidate();

        }

        @SuppressWarnings({"deprecation"})
        private void fadeInMainImage() {

            mMainTime = SystemClock.uptimeMillis() + ANIMATION_FRAME_DURATION;
            mFadeInAlpha4Main += ALPHA_VELOCITY;

            mMainImage.setAlpha(mFadeInAlpha4Main < MAX_ALPHA ? mFadeInAlpha4Main : MAX_ALPHA);

            if (mFadeInAlpha4Main < MAX_ALPHA) {
                mHandler.sendMessageAtTime(mHandler.obtainMessage(MSG_ANIMATE_FADE_IN_MAIN), mMainTime);
            } else {
                mFadeInAlpha4Main = 0;
                mHandler.removeMessages(MSG_ANIMATE_FADE_IN_MAIN);
            }

            invalidate();

        }

    }

    private class AnimationHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch ((msg.what)) {
                case MSG_ANIMATE_FADE_IN_MAIN:
                    mAnimator.fadeInMainImage();
                    break;
                case MSG_ANIMATE_FADE_IN_STATUS:
                    mAnimator.fadeInStatusImage();
                    break;
            }
        }
    }
}
