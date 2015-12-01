package com.renren.mobile.chat.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.renren.mobile.chat.R;
import com.renren.mobile.chat.base.GlobalValue;

public class BaseHeaderLayout {
	
	private Context mContext;
	
	private LinearLayout mFeedHeader = null;
	
//	private FrameLayout mFeedHeaderLayout = null;

	private RelativeLayout mFeedHeaderNewFeedLayout = null;

	private TextView mFeedHeaderNewFeedNotice = null;
	
	//默认的顶部bar高度
	public static final int FEED_HEADER_HEIGHT = 39;
	
	public BaseHeaderLayout(Context context) {
		this.mContext = context;
		LayoutInflater mInflater = LayoutInflater.from(this.mContext);
		mFeedHeader = (LinearLayout) mInflater.inflate(R.layout.lc_feed_header, null);
		init();
	}
	
	private void init(){
//		mFeedHeaderLayout = (FrameLayout) mFeedHeader.findViewById(R.id.lc_feed_header_layout);
//		mFeedHeaderLayout.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, GlobalValue.getInstance()
//				.calcFromDip(FEED_HEADER_HEIGHT)));
		mFeedHeaderNewFeedLayout = (RelativeLayout) mFeedHeader.findViewById(R.id.lc_feed_header_layout);
		mFeedHeaderNewFeedNotice = (TextView) mFeedHeader.findViewById(R.id.lc_feed_header_notice);
	}
	
	public ViewGroup getView(){
		return mFeedHeader;
	}
	
	public void setHeaderText(String temp){
		mFeedHeaderNewFeedNotice.setText(temp);
	}
	
//	public void setHeaderLoadingText(String temp){
//		mFeedHeaderNewFeedLoadingText.setText(temp);
//	}
	
//	public void setFeedHeaderLoading(boolean state){
//		if(state){
//			mFeedHeaderNewFeedLayout.setVisibility(View.INVISIBLE);
//			mFeedHeaderLoadLayout.setVisibility(View.VISIBLE);
//		}else{
//			mFeedHeaderNewFeedLayout.setVisibility(View.VISIBLE);
//			mFeedHeaderLoadLayout.setVisibility(View.INVISIBLE);
//		}
//	}
	
//	public boolean isFeedLoading(){
//		return mFeedHeaderLoadLayout.getVisibility()==View.VISIBLE;
//	}
	
	public void setVisibility(boolean visibility){
		if(visibility){
			this.mFeedHeaderNewFeedLayout.setVisibility(View.VISIBLE);
		}else{
//			setFeedHeaderLoading(false);
			this.mFeedHeaderNewFeedLayout.setVisibility(View.GONE);
		}
	}
	
	public boolean getVisibility(){
		return this.mFeedHeaderNewFeedLayout.getVisibility() == View.VISIBLE;
	}
	
	public void setOnClickListener(OnClickListener onClickListener){
		this.mFeedHeaderNewFeedLayout.setOnClickListener(onClickListener);
	}

}
