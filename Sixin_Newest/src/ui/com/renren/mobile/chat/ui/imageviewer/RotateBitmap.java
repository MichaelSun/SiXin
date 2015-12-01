package com.renren.mobile.chat.ui.imageviewer;

import android.graphics.Bitmap;
import android.graphics.Matrix;

public class RotateBitmap {
	
    private Bitmap mBitmap;

    private int mRotation;

    private boolean mAutoRecycle = true;

    public RotateBitmap(Bitmap bitmap) {
        this(bitmap, 0, true);
    }

    public RotateBitmap(Bitmap bitmap, boolean autoRecycle) {
        this(bitmap, 0, autoRecycle);
    }

    public RotateBitmap(Bitmap bitmap, int rotation, boolean autoRecycle) {
        setBitmap(bitmap, autoRecycle);
        setRotation(rotation % 360);
    }

    public void recycle() {
        if (mAutoRecycle && mBitmap != null && !mBitmap.isRecycled()) {
            mBitmap.recycle();
        }
        mBitmap = null;
    }

    public void setRotation(int rotation) {
        mRotation = rotation;
    }

    public int getRotation() {
        return mRotation;
    }

    public Bitmap getBitmap() {
        return mBitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        setBitmap(bitmap, true);
    }

    public void setBitmap(Bitmap bitmap, boolean autoRecycle) {
        if (mBitmap != bitmap) {
            recycle();
            mBitmap = bitmap;
        }
        mWidth = bitmap == null ? 0 : bitmap.getWidth();
        mHeight = bitmap == null ? 0 : bitmap.getHeight();
        mAutoRecycle = autoRecycle;
    }

    private int mWidth;

    private int mHeight;

    public void setBitmap(Bitmap bitmap, int width, int height, boolean autoRecycle) {
        if (mBitmap != bitmap) {
            recycle();
            mBitmap = bitmap;
        }

        mWidth = width;
        mHeight = height;
        mAutoRecycle = autoRecycle;
    }

    public Matrix getRotateMatrix() {
        // By default this is an identity matrix.
        Matrix matrix = new Matrix();
        int intrinsicWidth = mBitmap.getWidth();
        int intrinsicHeight = mBitmap.getHeight();
        int cx = intrinsicWidth / 2;
        int cy = intrinsicHeight / 2;

        // handle orientation
        if (mRotation != 0) {
            // We want to do the rotation at origin, but since the bounding
            // rectangle will be changed after rotation, so the delta values
            // are based on old & new width/height respectively.
            matrix.preTranslate(-cx, -cy);
            matrix.postRotate(mRotation);
            if (isOrientationChanged()) {
                matrix.postTranslate(cy, cx);
            } else {
                matrix.postTranslate(cx, cy);
            }
        }
        // handle zoom factor
        if (isOrientationChanged()) {
            matrix.postScale((mHeight + 0F) / intrinsicHeight, (mWidth + 0F) / intrinsicWidth);
        } else {
            matrix.postScale((mWidth + 0F) / intrinsicWidth, (mHeight + 0F) / intrinsicHeight);
        }

        return matrix;
    }

    public boolean isOrientationChanged() {
        return (mRotation / 90) % 2 != 0;
    }

    public int getHeight() {
        return isOrientationChanged() ? mWidth : mHeight;
    }

    public int getWidth() {
        return isOrientationChanged() ? mHeight : mWidth;
    }

}
