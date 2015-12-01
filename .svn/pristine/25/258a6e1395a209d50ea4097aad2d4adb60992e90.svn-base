package com.sixin.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.InflateException;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.common.R;

/**
 * 这控件提供了同时显示一张图片和文字的功能。
 * 文字可以被设置在图片的上下左右，可以设置图片和文字之间的间距（spacing）。
 *
 * @author ccl
 */
public class ImageTextView extends ViewGroup {
    private static final String TAG = "ImageTextView";

    /**
     * value of XML attribute position
     * <p/>
     * Text will display on the left of the image.
     */
    public static final String POSITION_LEFT = "left";

    /**
     * value of XML attribute position
     * <p/>
     * Text will display on the top of the image.
     */
    public static final String POSITION_TOP = "top";

    /**
     * value of XML attribute position
     * <p/>
     * Text will display on the bottom of the image.
     */
    public static final String POSITION_BOTTOM = "bottom";

    /**
     * value of XML attribute position
     * <p/>
     * Text will display on the right of the image
     * <p/>
     * <b>This is the default value of position</b>
     */
    public static final String POSITION_RIGHT = "right";

    /**
     * value of XML attribute text_gravity and image_gravity
     * <p/>
     * If set this value to text_gravity, the text will display on the center of this ImageTextView component
     * horizontally or vertically based on the value of position.
     * <p/>
     * If position is left or right, text will be vertically centered, otherwise, it will be horizontal centered.
     * <p/>
     * The image_gravity has the same situation.
     * <p/>
     * <b>This is the default value of gravity</b>
     */
    public static final String GRAVITY_CENTER = "center";

    /**
     * value of XML attribute text_gravity and image_gravity
     * <p/>
     * Here, head means left in horizontal and top in vertical.
     */
    public static final String GRAVITY_HEAD = "head";

    /**
     * value of XML attribute text_gravity and image_gravity
     * <p/>
     * tail means right in horizontal and bottom in vertical.
     */
    public static final String GRAVITY_TAIL = "tail";

    private String mTextGravity;
    private String mImageGravity;
    private String mPosition;
    private float mImageWidth;
    private float mImageHeight;
    private float mSpacing;

    private ImageView mImage;
    private TextView mText;

    public ImageTextView(Context context) {
        this(context, null, 0);
    }

    public ImageTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ImageTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ImageTextView);

        mTextGravity = a.getString(R.styleable.ImageTextView_text_gravity);
        if (mTextGravity == null) {
            mTextGravity = GRAVITY_CENTER;
        }

        mImageGravity = a.getString(R.styleable.ImageTextView_image_gravity);
        if (mImageGravity == null) {
            mImageGravity = GRAVITY_CENTER;
        }

        mPosition = a.getString(R.styleable.ImageTextView_position);
        if (mPosition == null) {
            mPosition = POSITION_RIGHT;
        }

        mSpacing = a.getDimension(R.styleable.ImageTextView_spacing, 0f);
        mImageWidth = a.getDimension(R.styleable.ImageTextView_image_width, 0f);
        mImageHeight = a.getDimension(R.styleable.ImageTextView_image_height, 0f);

        a.recycle();

        mText = new TextView(context, attrs, defStyle);
        mImage = new ImageView(context, attrs, defStyle);

        addView(mImage);
        addView(mText);
    }

    public void setPosition(String position) {
        mPosition = position;
        requestLayout();
        invalidate();
    }

    public String getPosition() {
        return mPosition;
    }

    public void setTextGravity(String gravity) {
        mTextGravity = gravity;
        requestLayout();
        invalidate();
    }

    public String getTextGravity() {
        return mTextGravity;
    }

    public void setImageGravity(String gravity) {
        mImageGravity = gravity;
        requestLayout();
        invalidate();
    }

    public String getImageGravity() {
        return mImageGravity;
    }

    public void setText(CharSequence text) {
        mText.setText(text);
    }

    public CharSequence getText() {
        return mText.getText();
    }

    public void setTextSize(float size) {
        mText.setTextSize(size);
    }

    public float getTextSize() {
        return mText.getTextSize();
    }

    public void setImageResource(int resID) {
        mImage.setImageResource(resID);
    }

    public void setImageBitmap(Bitmap bitmap) {
        mImage.setImageBitmap(bitmap);
    }

    public void setImageDrawable(Drawable drawable) {
        mImage.setImageDrawable(drawable);
    }

    public Drawable getDrawable() {
        return mImage.getDrawable();
    }

    public void setImageScaleType(ImageView.ScaleType scaleType) {
        mImage.setScaleType(scaleType);
    }

    public ImageView.ScaleType getImageScaleType() {
        return mImage.getScaleType();
    }


    private int horizontalPadding() {
        return getPaddingLeft() + getPaddingRight();
    }

    private int verticalPadding() {
        return getPaddingTop() + getPaddingBottom();
    }

    private boolean mNeedLayout = true;

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int widthSize = widthMeasureSpec & ~(0x3 << 30);
        final int widthMode = widthMeasureSpec & (0x3 << 30);
        final int heightSize = heightMeasureSpec & ~(0x3 << 30);
        final int heightMode = heightMeasureSpec & (0x3 << 30);

        boolean horizontal = (POSITION_LEFT.equals(mPosition) || POSITION_RIGHT.equals(mPosition));

        if (widthMode == MeasureSpec.UNSPECIFIED || heightMode == MeasureSpec.UNSPECIFIED) {
            throw new InflateException("MeasureSpec.UNSPECIFIED is not support.");
        }

        if (mImageHeight > 0 && mImageWidth > 0 &&
                mImageHeight < heightSize - verticalPadding() - (horizontal ? 0 : mSpacing) &&
                mImageWidth < widthSize - horizontalPadding() - (horizontal ? mSpacing : 0)) {
            //the image's size must not larger than the given size. and if the size is 0, we won't measure it.
            measureChild(mImage, (int) (mImageWidth + widthMode), (int) (mImageHeight + heightMode));
        } else {
            mNeedLayout = false;
        }

        if (horizontal) {
            int textWidth = (int) (widthSize - horizontalPadding() - mSpacing - mImageWidth);
            measureChild(mText, textWidth + widthMode, heightSize + heightMode);
        } else {
            int textHeight = (int) (heightSize - verticalPadding() - mSpacing - mImageHeight);
            measureChild(mText, widthSize + widthMode, textHeight + heightMode);
        }

        int width = 0;
        int height = 0;

        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else if (widthMode == MeasureSpec.AT_MOST) {
            width = (int) (mImage.getMeasuredWidth() + mText.getMeasuredWidth() + horizontalPadding() +
                    (horizontal ? mSpacing : 0));
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else if (heightMode == MeasureSpec.AT_MOST) {
            height = (int) (mImage.getMeasuredHeight() + mText.getMeasuredHeight() + verticalPadding() +
                    (horizontal ? 0 : mSpacing));
        }

        setMeasuredDimension(width, height);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        final int width = r - l;
        final int height = b - t;
        final int imageWidth = mImage.getMeasuredWidth();
        final int imageHeight = mImage.getMeasuredHeight();
        final int textWidth = mText.getMeasuredWidth();
        final int textHeight = mText.getMeasuredHeight();
        int left = getPaddingLeft();
        int top = getPaddingTop();
        int right = r - getPaddingRight();
        int bottom = b - getPaddingBottom();

        /*added by chengran sun for better performing*/
        int widthNoPadding = width - getPaddingLeft() - getPaddingRight();
        int heightNoPadding = height - getPaddingTop() - getPaddingBottom();

        if (POSITION_LEFT.equals(mPosition)) {
            if (GRAVITY_CENTER.equals(mTextGravity)) {
                mText.layout(left, (heightNoPadding - textHeight) / 2,
                        left += textWidth, (heightNoPadding + textHeight) / 2);
            } else if (GRAVITY_HEAD.equals(mTextGravity)) {
                mText.layout(left, top, left += textWidth, top + textHeight);
            } else if (GRAVITY_TAIL.equals(mTextGravity)) {
                mText.layout(left, bottom - textHeight, left += textWidth, bottom);
            }

            if (!mNeedLayout) {
                return;
            }

            left += mSpacing;

            if (GRAVITY_CENTER.equals(mImageGravity)) {
                mImage.layout(left, (heightNoPadding - imageHeight) / 2,
                        left += imageWidth, (heightNoPadding + imageHeight) / 2);
            } else if (GRAVITY_HEAD.equals(mImageGravity)) {
                mImage.layout(left, top, left += imageWidth, top + imageHeight);
            } else if (GRAVITY_TAIL.equals(mImageGravity)) {
                mImage.layout(left, bottom - imageHeight, left += imageWidth, bottom);
            }
        } else if (POSITION_RIGHT.equals(mPosition)) {
            if (GRAVITY_CENTER.equals(mImageGravity)) {
                mImage.layout(left, (heightNoPadding - imageHeight) / 2,
                        left += imageWidth, (heightNoPadding + imageHeight) / 2);
            } else if (GRAVITY_HEAD.equals(mImageGravity)) {
                mImage.layout(left, top, left += imageWidth, top + imageHeight);
            } else if (GRAVITY_TAIL.equals(mImageGravity)) {
                mImage.layout(left, bottom - imageHeight, left += imageWidth, bottom);
            }

            if (!mNeedLayout) {
                return;
            }

            left += mSpacing;

            if (GRAVITY_CENTER.equals(mTextGravity)) {
                mText.layout(left, (heightNoPadding - textHeight) / 2,
                        left += textWidth, (heightNoPadding + textHeight) / 2);
            } else if (GRAVITY_HEAD.equals(mTextGravity)) {
                mText.layout(left, top, left += textWidth, top + textHeight);
            } else if (GRAVITY_TAIL.equals(mTextGravity)) {
                mText.layout(left, bottom - textHeight, left += textWidth, bottom);
            }
        } else if (POSITION_TOP.equals(mPosition)) {
            if (GRAVITY_CENTER.equals(mTextGravity)) {
                mText.layout((widthNoPadding - textWidth) / 2, top,
                        (widthNoPadding + textWidth) / 2, top += textHeight);
            } else if (GRAVITY_HEAD.equals(mTextGravity)) {
                mText.layout(left, top, left + textWidth, top += textHeight);
            } else if (GRAVITY_TAIL.equals(mTextGravity)) {
                mText.layout(right - textWidth, top, right, top += textHeight);
            }

            if (!mNeedLayout) {
                return;
            }

            top += mSpacing;

            if (GRAVITY_CENTER.equals(mImageGravity)) {
                mImage.layout((widthNoPadding - imageWidth) / 2, top,
                        (widthNoPadding + imageWidth) / 2, top += imageHeight);
            } else if (GRAVITY_HEAD.equals(mImageGravity)) {
                mImage.layout(left, top, left + imageWidth, top += imageHeight);
            } else if (GRAVITY_TAIL.equals(mImageGravity)) {
                mImage.layout(right - imageWidth, top, right, top += imageHeight);
            }
        } else if (POSITION_BOTTOM.equals(mPosition)) {
            if (GRAVITY_CENTER.equals(mImageGravity)) {
                mImage.layout((widthNoPadding - imageWidth) / 2, top,
                        (widthNoPadding + imageWidth) / 2, top += imageHeight);
            } else if (GRAVITY_HEAD.equals(mImageGravity)) {
                mImage.layout(left, top, left + imageWidth, top += imageHeight);
            } else if (GRAVITY_TAIL.equals(mImageGravity)) {
                mImage.layout(right - imageWidth, top, right, top += imageHeight);
            }

            if (!mNeedLayout) {
                return;
            }

            top += mSpacing;

            if (GRAVITY_CENTER.equals(mTextGravity)) {
                mText.layout((widthNoPadding - textWidth) / 2, top,
                        (widthNoPadding + textWidth) / 2, top += textHeight);
            } else if (GRAVITY_HEAD.equals(mTextGravity)) {
                mText.layout(left, top, left + textWidth, top += textHeight);
            } else if (GRAVITY_TAIL.equals(mTextGravity)) {
                mText.layout(right - textWidth, top, right, top += textHeight);
            }
        }
    }
}
