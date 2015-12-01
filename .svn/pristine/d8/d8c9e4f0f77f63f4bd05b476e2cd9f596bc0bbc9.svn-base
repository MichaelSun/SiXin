package com.common.emotion.emotion;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.text.style.ImageSpan;


public class EmotionSpan extends ImageSpan {

	private Bitmap mBitmap = null;
	private Drawable mDrawable = null;
	public EmotionSpan(Bitmap b) {
		super(b, ImageSpan.ALIGN_BOTTOM);
		this.mBitmap = b;
	}
	public EmotionSpan(Drawable b) {
		super(b, ImageSpan.ALIGN_BOTTOM);
		this.mDrawable = b;
	}
	public EmotionSpan copy(){
		if(this.mBitmap!=null){
			return new EmotionSpan(mBitmap);
		}else if(this.mDrawable!=null){
			return new EmotionSpan(mDrawable);
		}
		return null;
		
	}

}
