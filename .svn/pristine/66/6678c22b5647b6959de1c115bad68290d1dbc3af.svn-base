package com.common.emotion.view;

import com.common.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

/**
 * 横向导航点
 * @author zhenning.yang
 *
 */
public class PagePointView  implements IDoubleStateIndicator {

	private static final int CHECK_SOURCE_ID = R.drawable.emotion_point_check;
	private static final int UNCHECK_SOURCE_ID = R.drawable.emotion_point_uncheck;
    private LayoutInflater mInflater = null;
	private View mView = null;
	private ImageView mImageView = null;
	public PagePointView(Context context) {
		mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mView  = mInflater.inflate(R.layout.emotion_plugin_point_item,null );
		mImageView = (ImageView)mView.findViewById(R.id.emotion_plugin_imageview);
		//mView.setLayoutParams(new LinearLayout.LayoutParams(CommonUtil.calcFromDip(18), CommonUtil.calcFromDip(5)));
		//mView.setPadding(0, 10, 0, 0);
		this.unCheck();
	}

	public View getView(){
		return mView;
	}
	
	public void setScaleType(ImageView.ScaleType type){
		this.mImageView.setScaleType(type);
	}
	
	@Override
	public void check(int position) {
		mImageView.setImageResource(CHECK_SOURCE_ID);
	}

	@Override
	public void unCheck() {
		mImageView.setImageResource(UNCHECK_SOURCE_ID);
	}

}
