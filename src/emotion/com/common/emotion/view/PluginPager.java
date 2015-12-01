package com.common.emotion.view;

import com.common.R;
import com.core.util.CommonUtil;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * 一个格子页面
 * @author dingwei.chen
 * */
public class PluginPager extends ViewGroup{

	public static final int DEFAULT_PADDING = 10;
	public static final Paint PAINT = new Paint();
	private int mOffsetX1 = 0;
	private int mOffsetX2 = 0;
	private int mOffsetX3;
	private int mOffsetX4;
	private int mPadding = DEFAULT_PADDING;
	private int mOffsetY1 = 0;
	private int mOffsetY2 = 0;
	private int mStepX = 0;
	private int mStepY = 0;
	private int mItemWidth = 0;
	private int mItemHight = 0;
	Rect mRect = null;
	private static final int DEFUALT_BACK_COLOR = 0X00eaeaea;
	
	private int mXCount = 8;
	private int mYCount = 2;
	private int mLeft ;
	private int mTop;
	private int mRight;
	private int mBottom;
	private int mOffest;
	
	public PluginPager(Context context){
		super(context);
		this.init(context);
	}
	/**
	 * 带有行数和列数的构造方法
	 * @param context
	 * @param xCount 列数
	 * @param yCount 行数
	 */
	public PluginPager(Context context,int xCount,int yCount,int left,int top,int right,int bottom,int offest){
		this(context);
		this.mXCount = xCount;
		this.mYCount = yCount;
		this.mLeft = left;
		this.mTop = top;
		this.mRight = right;
		this.mBottom = bottom;
		this.mOffest = offest;
	}
	
	private void init(Context context){
//		sLineX   = getBitmap(context,sLineX, R.drawable.linex);
//		sLineY   = getBitmap(context,sLineY, R.drawable.liney);
//		sCircle  = getBitmap(context,sCircle, R.drawable.circle);
		PAINT.setAntiAlias(true);
		PAINT.setColor(Color.RED);
		//this.setBackgroundColor(DEFUALT_BACK_COLOR);
		this.setDrawingCacheEnabled(false);
	}
	public static Bitmap getBitmap(Context context,Bitmap bitmap,int id){
		if(bitmap==null||bitmap.isRecycled()){
			return BitmapFactory.decodeResource(context.getResources(), id);
		}
		return bitmap;
	}
	
	public PluginPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.init(context);
	}
	@Override
	public void draw(Canvas canvas) {
		super.draw(canvas);
	};
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		this.measureChild(widthMeasureSpec, heightMeasureSpec);
	}
	protected void measureChild(int widthMeasureSpec, int heightMeasureSpec){
		int c = this.getChildCount();
		int k = 0;
		while(k<c){
			View view = this.getChildAt(k);
			view.measure(widthMeasureSpec, heightMeasureSpec);
			k++;
		}
	}

	/*
	//绘制横向线
//	private void drawLineX(Canvas canvas){
//		int height = sLineX.getHeight();
//		int top = ((getHeight()-height)>>1);
//		Rect rect = this.obtainRect(0, top, 0, top+height);
//		rect.left = mPadding;
//		rect.right = mOffsetX1;
//		canvas.drawBitmap(sLineX, null, rect,PAINT);
//		rect.left = mOffsetX2;
//		rect.right = mOffsetX3;
//		canvas.drawBitmap(sLineX, null, rect,PAINT);
//		rect.left = mOffsetX4;
//		rect.right = getWidth()-mPadding;
//		canvas.drawBitmap(sLineX, null, rect,PAINT);
//	}
	
	
	
	//绘制纵向线
//	private void drawLineY(Canvas canvas){
//		int width = sLineY.getWidth();
//		int width_step = this.getWidth()/3;
//		int left = (width_step-(width>>1));
//		int left2 = ((width_step<<1)-(width>>1));
//		Rect rect = this.obtainRect(left, mPadding, left+width, mOffsetY1);
//		canvas.drawBitmap(sLineY, null, rect,PAINT);
//		rect.left = left2;
//		rect.right = left2+width;
//		canvas.drawBitmap(sLineY, null, rect,PAINT);
//		
//		rect.left = left;
//		rect.right = left+width;
//		rect.top = mOffsetY2;
//		rect.bottom = getHeight()-mPadding;
//		canvas.drawBitmap(sLineY, null, rect,PAINT);
//		rect.left = left2;
//		rect.right = left2+width;
//		canvas.drawBitmap(sLineY, null, rect,PAINT);
//	}
	
//	private Rect obtainRect(int left, int top, int right, int bottom){
//		if(mRect==null){
//			mRect = new Rect();
//		}
//		mRect.left = left;
//		mRect.top = top;
//		mRect.right = right;
//		mRect.bottom = bottom;
//		return mRect;
//	}
	
	//绘制圆点
//	private void drawCircles(Canvas canvas){
//		int top = (this.getHeight() - sCircle.getHeight())>>1;
//		mOffsetY1 = top-mPadding;
//		mOffsetY2 = top+sCircle.getHeight()+mPadding;
//		int width = sCircle.getWidth();
//		int width_step = this.getWidth()/3;
//		int left = (width_step-(sCircle.getWidth()>>1));
//		mOffsetX1 = left-mPadding; 
//		mOffsetX2 = left+width +mPadding;
//		canvas.drawBitmap(sCircle, left, top, PAINT);
//		left = ((width_step<<1)-(sCircle.getWidth()>>1));
//		mOffsetX3 = left-mPadding;
//		canvas.drawBitmap(sCircle, left, top, PAINT);
//		mOffsetX4 = left+width+mPadding;
//	}
*/
	
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		int count = this.getChildCount();
		if(count>0){
			int index = 0;
			
			//一个格子的宽和高
			mStepX = (this.getWidth()-mLeft-mRight)/mXCount;
			mStepY = (this.getHeight()-mTop-mBottom)/mYCount;
		//	CommonUtil.log("pager", "x: "+this.getWidth()+"|"+mStepX +"|"+this.getHeight()+" Y:"+mStepY);
			mItemWidth = Math.min(mStepX, mStepY);
			mItemHight = mItemWidth;
		//	CommonUtil.log("pager", "itemWidth: "+mItemWidth);
			//一个元素的宽度
//			mItemWidth = (mStepX-6);
//			mItemHight = (mStepY-6);
			//item相对于格子的偏移
			int off_x = (mStepX-mItemWidth)>>1;
			//item相对于格子的纵向偏移
			int off_y = (mStepY-mItemHight)>>1;
			while(index<count){
				View view = this.getChildAt(index);
				//item所在格子的横纵坐标
				int ioffX = index%mXCount;//0,1,2
				int ioffY = (index)/mXCount;
				//item横纵坐标
				int offX = (mStepX*ioffX+off_x+mLeft);
				int offY = (mStepY*ioffY+off_y+mTop);
//				CommonUtil.log("pager", "offx: "+offX+"  offy:"+offY);
				view.layout(offX, offY, offX+mItemWidth, offY+mItemHight);
				index ++;
			}
		}
	}

}
