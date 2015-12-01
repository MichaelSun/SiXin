package com.core.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class ImageUtil {

	
	//通过字节码来创建图片
	public final static Bitmap createImageByBytes(byte[] bytes){
		Bitmap img = null;
		try {
			img = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
		} catch (Exception e) {
			recycle(img);
			img = null;
		} catch (OutOfMemoryError e) {
			recycle(img);
			img = null;
		}
		return img;
	}
	public static void recycle(Bitmap bitmap){
		if(bitmap!=null&& !bitmap.isRecycled()){
			bitmap.recycle();
		}
			
	}
   
	
}
