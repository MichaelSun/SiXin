package com.renren.mobile.chat.contact;

import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import com.renren.mobile.chat.R;
import com.renren.mobile.chat.RenrenChatApplication;
import com.renren.mobile.chat.common.BitmapUtil;

public class TitleMapCache {
	private static Map<String,SoftReference<Bitmap>> map = new HashMap<String, SoftReference<Bitmap>>();
	private static Bitmap backgroundBitmap = BitmapFactory.decodeResource(RenrenChatApplication.mContext.getResources(), R.drawable.s_aleph_bg); //TODO:换成背景图
	private static Bitmap charBg = BitmapFactory.decodeResource(RenrenChatApplication.mContext.getResources(), R.drawable.s_aleph_char_bg);//字符的背景
	
	public static synchronized void clear() {
		for(Map.Entry<String,SoftReference<Bitmap>> entry:map.entrySet()){ 
			if(entry.getValue()!=null&&entry.getValue().get()!=null&&!entry.getValue().get().isRecycled())
		     entry.getValue().get().recycle();
		}  
		map.clear();
	}
	
	public static synchronized Bitmap getTitleByString(String tag, int width) {
		Bitmap ret = null;
		if(null != tag) {
			SoftReference<Bitmap> ref;
			ref = map.get(tag);
			if(null == ref || null == ref.get()) {
				if(width != backgroundBitmap.getWidth()) {
					backgroundBitmap = Bitmap.createScaledBitmap(backgroundBitmap, width, backgroundBitmap.getHeight(), false);
				}
				ret = backgroundBitmap.copy(backgroundBitmap.getConfig(), true);
				Bitmap initial = BitmapUtil.creatAlpheBitMap(charBg, tag, 0, 0);
				Canvas canvas = new Canvas(ret);
				canvas.drawBitmap(initial, 0, 0, null);
				map.put(tag, new SoftReference<Bitmap>(ret));
			}
			else
				ret = ref.get();
		}
		return ret;
	}
}
