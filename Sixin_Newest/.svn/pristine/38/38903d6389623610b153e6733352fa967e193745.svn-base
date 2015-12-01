package com.renren.mobile.chat.common;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.TypedValue;

import com.common.utils.Methods;
import com.renren.mobile.chat.RenrenChatApplication;

/**
 * 
 * @author eason lee
 * @date 2.9.2012
 * @说明：bitmap 图片的工具类
 */
public class BitmapUtil {
	/**
	 * @说明：在bitmiap 上绘制一个字符串 并且产生一个新的bitmap
	 * @param SrcBmp
	 * @param alphe
	 * @return
	 */
	//TODO:继续封装
	public static Bitmap creatAlpheBitMap(Bitmap SrcBmp , String alphe ,float x,float y){
		if( SrcBmp == null )   
	       {   
	           return null;   
	       }  
		int w = SrcBmp.getWidth();   
	    int h = SrcBmp.getHeight();   
	    Bitmap newb = Bitmap.createBitmap( w, h, Config.ARGB_8888 );//创建一个新的和SRC长度宽度一样的位图  
	    Canvas cv = new Canvas( newb );   
	    //draw src into   
	    cv.drawBitmap( SrcBmp, 0, 0, null );//在 0，0坐标开始画入src   
	    
	    Paint paint = new Paint();
	    paint.setTextSize(Methods.getTextSizeOnPaint(16));
	    paint.setColor(Color.parseColor("#fffffefe"));
	    paint.setTypeface(Typeface.DEFAULT_BOLD);
	    paint.setAntiAlias(true);
	    Rect rect = new Rect();  
	    paint.getTextBounds(alphe, 0, 1, rect);  
	    
	    int alpheWidth = rect.width();  
	    int alpheHeight = rect.height(); 
	    
	    float alphePx = paint.measureText(alphe);
//	    int x_dip = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 15, RenrenChatApplication.mContext.getResources().getDisplayMetrics());//37
		int y_dip = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 18,  RenrenChatApplication.mContext.getResources().getDisplayMetrics()); //32
//		Log.i("lys","x:"+);
//	    cv.drawText(alphe,x_dip-alphePx/2, y_dip, paint);
	    cv.drawText(alphe,(float)(w - alphePx - Methods.getTextSizeOnPaint(3))/2, y_dip, paint);
	    
	    
	    cv.save( Canvas.ALL_SAVE_FLAG );//保存   
	    //store   
	    cv.restore();//存储   
	    return newb;   
	}
	
	/**  
	    * @说明：生成水印图片。在原来的bitmap上绘制一张水印bitmap。
	    * 
	    * @param src the bitmap object you want proecss 
	    * @param watermark the water mark above the src 
	    * @return return a bitmap object ,if paramter's length is 0,return null 
	    */  
	   public static Bitmap creatAlpheBitMap( Bitmap src, Bitmap watermark ,float left,float top)   
	   {   
	       if( src == null )   
	       {   
	           return null;   
	       }   

	       int w = src.getWidth();   
	       int h = src.getHeight();   
	       int ww = watermark.getWidth();   
	       int wh = watermark.getHeight();   
	       //create the new blank bitmap   
	       Bitmap newb = Bitmap.createBitmap( w, h, Config.ARGB_8888 );//创建一个新的和SRC长度宽度一样的位图   空图
	       Canvas cv = new Canvas( newb );   
	       //draw src into   
	       cv.drawBitmap( src, 0, 0, null );//在 0，0坐标开始画入src   
	       //draw watermark into   
	       cv.drawBitmap( watermark, left, top, null );//在src的右下角画入水印   
	       //save all clip   
	       cv.save( Canvas.ALL_SAVE_FLAG );//保存   
	       //store   
	       cv.restore();//存储   
	       return newb;   
	   } 

}
