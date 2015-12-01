package com.renren.mobile.chat.base.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.widget.ImageView;

public class HeadImageView extends ImageView{

	private String mUrl = null;
	
	public HeadImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public void setUrl(String url,Bitmap defaultBitmap){
//		ImagePool.getInstance().getHeadFromLocal(bitmapPath)
	}
	
}
