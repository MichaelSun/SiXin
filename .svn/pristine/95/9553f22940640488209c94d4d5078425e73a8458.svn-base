package com.sixin.widgets;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.common.R;

public class RotateImageView extends View {

	private float mDegree = 0f;
	
	public RotateImageView(Context context) {
		super(context);
	}

	public RotateImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public RotateImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mDegree = 0f;
	}

	public void setDegree(double degree) {
		mDegree = (float) degree;
		invalidate();
	}

	public float getDegree() {
		return mDegree;
	}

	public void setDegree(float degree, float x, float y) {
		mDegree = degree;
		invalidate();
	}

	@Override
	public void draw(Canvas canvas) {
		canvas.save();
		canvas.rotate(mDegree, getMeasuredWidth() / 2, getMeasuredHeight() / 2);
		super.draw(canvas);
		canvas.restore();
	}
}