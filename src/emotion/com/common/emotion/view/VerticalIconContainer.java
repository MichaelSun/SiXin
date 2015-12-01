package com.common.emotion.view;


import com.common.app.AbstractRenrenApplication;
import com.core.util.CommonUtil;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

/**
 * 二级tab纵向导航Icon的容器
 * 
 * @author zhenning.yang
 * 
 */
public class VerticalIconContainer extends LinearLayout implements
		ISuperOnFling2ContentChangeListener {
	private String tag = "v";
	private VerticalIconContainerAdapter mAdapter = null;
	private int currentIndex = 0;
	private int mCount = 0;
	private static final int mCountDefault = 6;
	private  ISuperOnFling2ContentChangeListener mFlipperListener;
	public VerticalIconContainer(Context context) {
		super(context);
	}

	public VerticalIconContainer(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public VerticalIconContainer(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	/**
	 * 给纵向导航条设置Flipper，以实现点击事件滚动Flipper
	 * @param listener
	 */
	public void setSuperOnFling2ContentChangeListener(ISuperOnFling2ContentChangeListener listener){
		mFlipperListener = listener;
	}
	
	public void setAdapter(VerticalIconContainerAdapter adapter) {
		this.mAdapter = adapter;
		if (mAdapter != null) {
			mCount = mAdapter.getCount();
			CommonUtil.log("Icon", "mCount c:"+mCount);
			boolean isLess = false; // 看是否少于默认个数
			if(mCount < mCountDefault){//当少于默认个数，则在前面添加2个坑。（有待改进）
				isLess = true;
				addNullView(2);
			}
			//CommonUtil.log("Icon", "mCount: 2");
			for (int i = 0; i < mCount; i++) {
				VTabIconImageView v = mAdapter.getView(i);
				addView(v);
				CommonUtil.log(tag,"add view");
				if (i == 0) {
					v.check(0);
				}
				final int index = i;
				v.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						CommonUtil.log(tag," vertical Icon is clicked"+index);
						mFlipperListener.onShowPointer(index);
					}
				});
				LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) v
						.getLayoutParams();
				
				if(isLess == false){  //当不少于默认个数，则均分。
					params.weight = (float)1.0/mCount;
				}else{  //不均分
					//需设定其大小，否则无法均分
					params.width = CommonUtil.calcFromDip(32);
					params.height =CommonUtil.calcFromDip(32);
					params.weight = mCountDefault;

				}
				
				v.setLayoutParams(params);
			}
			if(mCount+2 < mCountDefault){
				addNullView(mCountDefault-(mCount+2));
			}
			//CommonUtil.log("Icon", "mCount: 3");
		}
	}
/**
 * 占坑的VIew
 * @param count（需要的个数）
 */
	private void addNullView(int count){
		
		for(int k = 0 ;k<count;k++){
			
			VTabIconImageView v1 = new VTabIconImageView(AbstractRenrenApplication.getAppContext());
			addView(v1);
			LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) v1
					.getLayoutParams();
		//	params.height = android.view.ViewGroup.LayoutParams.FILL_PARENT;
			//需设定其大小，否则无法均分
			params.width = CommonUtil.calcFromDip(32);
			params.height =CommonUtil.calcFromDip(32);
			params.weight = mCountDefault;
			v1.setLayoutParams(params);
		}
	}
	
	@Override
	public void onShowNextPointer() {
		mAdapter.getView(currentIndex).unCheck();
		currentIndex++;
		// 如果越界，这是一个可以循环的Container
		if (currentIndex > mCount - 1) {
			currentIndex = 0;
		}
		mAdapter.getView(currentIndex).check(currentIndex);
		CommonUtil.log(tag, "PointsContainer onFlingLeft");
	}

	@Override
	public void onShowPreviousPointer() {
		mAdapter.getView(currentIndex).unCheck();
		currentIndex--;
		// 如果越界，这是一个可以循环的Container
		if (currentIndex < 0) {
			currentIndex = mCount - 1;
		}
		mAdapter.getView(currentIndex).check(currentIndex);
		CommonUtil.log(tag, "PointsContainer onFlingRight");
	}

	@Override
	public void onShowPointer(int index) {
		// check指定位置的Icon
		mAdapter.getView(currentIndex).unCheck();
		currentIndex = index;
		mAdapter.getView(currentIndex).check(currentIndex);
		CommonUtil.log(tag,"PointsContainer onShowPointer");
	}
}
