package com.renren.mobile.chat.ui.contact;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;
import android.widget.ListView;

import com.renren.mobile.chat.contact.TitleMapCache;
/**
 * 
 * @author eason Lee
 *
 */
public class ContactListView extends ListView{
	
	//使用静态变量 保证数据处理的统一性
	private static final int MAX_ALPHA = 255;   //0完全透明  255完全不透明
	public static int positionTitleInScreem;
	public Paint mPaint;
	int titledy;
	private boolean doDrawFloatTitle = true;
	
	Bitmap charactorBitmap;
	int width;
	public ContactListView(Context context) {
		super(context);
		width = this.getWidth();
		positionTitleInScreem = 0;
		mPaint = new Paint();
		this.setVerticalFadingEdgeEnabled(false); //Define whether the vertical edges should be faded when this view is scrolled vertically
	}
	
	
	/**
	 * 用与开启悬浮标题
	 * */
	public void showFloatTitles() {
		doDrawFloatTitle = true;
	}
	
	
	/**
	 * 用于关闭悬浮标题
	 * */
	public void hideFloatTitles() {
		doDrawFloatTitle = false;
	}
	
	
	public static void reset(){
		positionTitleInScreem=0;
		
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.ListView#dispatchDraw(android.graphics.Canvas)
	 */

	@Override
	protected void dispatchDraw(Canvas canvas) {
		super.dispatchDraw(canvas);
		if(!doDrawFloatTitle) 
			return;
		canvas.save();

		
		
		if(this.getChildAt(0)!=null){    //解决搜索为空的情况	（没有条目）
		int positionInTop = this.getPositionForView(this.getChildAt(0));	  //置顶元素的position	
		
		String titlekey = "";   
		View mHeadView;
		titledy = 0;      //YYYYYYYY
		
		mPaint.setAlpha(MAX_ALPHA);
		ContactModel cm = (ContactModel) getAdapter().getItem(positionInTop);
		//在线联系人列表的长度，包含标题
		char aleph = Character.toUpperCase(cm.mAleph);
		if(!Character.isLetter(aleph)) 
			aleph = '#';
		titlekey = aleph+"";
		
			if (positionTitleInScreem > positionInTop && positionTitleInScreem - positionInTop < getCount()) { //如果置顶条目的position < 屏幕中是title条目的position      （=====屏幕有下一个分组====）
				mHeadView = getChildAt(positionTitleInScreem - positionInTop);
				if(mHeadView!=null && mHeadView.getTop()<=mHeadView.getHeight()) {
					titledy-=(mHeadView.getHeight()-mHeadView.getTop());   //形成推进效果(越来越小)   Y
					int alpha = (int) (MAX_ALPHA*(mHeadView.getTop()*1.0f/mHeadView.getHeight()));  //颜色渐变效果  越推进越透明
					mPaint.setAlpha(alpha);
				}
			}else{
				titledy = 0;
			}
			charactorBitmap = TitleMapCache.getTitleByString(titlekey, getWidth());
			canvas.drawBitmap(charactorBitmap, 0, titledy, mPaint);   // 绘制原来在顶部的title	  //titlemap只在这里会用到
		}		
		canvas.restore();		
	}
}
