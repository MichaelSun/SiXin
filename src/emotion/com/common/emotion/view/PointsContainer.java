package com.common.emotion.view;

import java.util.LinkedList;
import java.util.List;

import com.core.util.CommonUtil;


import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * 导航点的容器，也可以在纵向的二级tab中使用
 * @author zhenning.yang
 *
 */
public class PointsContainer extends LinearLayout implements IOnFling2ContentChangeListener {
	private String tag = "flipper";
	private List<IDoubleStateIndicator> mPointList = new LinkedList<IDoubleStateIndicator>();
	private int currentIndex = 0;
	private int mCount = 0;
	public PointsContainer(Context context) {
		super(context);
	}
	public PointsContainer(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	public PointsContainer(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	public void addPoints(int mPageCount,int leftMargin,int rightMargin,int topMargin,int bottomMargin,int width,int type){
		mCount = mPageCount;
		if(type==0){
			//setPadding(0, CommonUtil.calcFromDip(5), CommonUtil.calcFromDip(10), CommonUtil.calcFromDip(5));
			for(int i  = 0;i<mPageCount;i++){
				PagePointView point = new PagePointView(getContext());
				point.setScaleType(ImageView.ScaleType.CENTER_CROP);
				addView(point.getView());
				mPointList.add(point);
				if(i==0){
					point.check(0);
				}
//				LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)point.getLayoutParams();
//				params.leftMargin = leftMargin;
//				params.rightMargin = rightMargin;
//				params.topMargin = topMargin;
//				params.bottomMargin = bottomMargin;
//				point.setLayoutParams(params);
			}
		}else{
			for(int i  = 0;i<mPageCount;i++){
				VerticalPointView point = new VerticalPointView(getContext());
				addView(point);
				mPointList.add(point);
				if(i==0){
					point.check(0);
				}
				LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)point.getLayoutParams();
				params.leftMargin = leftMargin;
				params.rightMargin = rightMargin;
				params.topMargin = topMargin;
				params.bottomMargin = bottomMargin;
				point.setLayoutParams(params);
			}
		}
	}
	public void addPoints(int mPageCount){
		addPoints(mPageCount,CommonUtil.calcFromDip(5),CommonUtil.calcFromDip(5),0,0,18,0);
	}
	@Override
	public void onShowNextPointer() {
		// TODO Auto-generated method stub
		mPointList.get(currentIndex).unCheck();
		currentIndex++;
		if(currentIndex>mCount-1){
			currentIndex=0;
		}
		mPointList.get(currentIndex).check(currentIndex);
		postInvalidate();
	}
	@Override
	public void onShowPreviousPointer() {
		// TODO Auto-generated method stub
		mPointList.get(currentIndex).unCheck();
		currentIndex--;
		if(currentIndex<0){
			currentIndex=mCount-1;
		}
		mPointList.get(currentIndex).check(currentIndex);
		postInvalidate();
	}
}
