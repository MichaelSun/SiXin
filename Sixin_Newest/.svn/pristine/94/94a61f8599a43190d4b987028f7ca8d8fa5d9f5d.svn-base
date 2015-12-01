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

import com.renren.mobile.chat.R;
import com.renren.mobile.chat.base.GlobalValue;

public class BaseFooterLayout {
	
	private Context mContext;
	
	private FrameLayout mFeedFooterLayout = null;
	
	private LinearLayout mFeedFooter = null;

	private RelativeLayout mFeedFooterMoreLayout = null;

	private RelativeLayout mFeedFooterLoadLayout = null;
	
	public static final int FEED_HFOOTER_HEIGHT = 46;

	public BaseFooterLayout(Context context) {
		this.mContext = context;
		LayoutInflater mInflater = LayoutInflater.from(this.mContext);
		mFeedFooter = (LinearLayout) mInflater.inflate(R.layout.lc_feed_footer, null);
		init();
	}
	
	private void init(){
		mFeedFooterLayout = (FrameLayout) mFeedFooter.findViewById(R.id.lc_feed_footer_layout);
		mFeedFooterLayout.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, GlobalValue.getInstance()
				.calcFromDip(FEED_HFOOTER_HEIGHT)));
		mFeedFooterMoreLayout = (RelativeLayout) mFeedFooter.findViewById(R.id.lc_feed_footer_more);
		mFeedFooterLoadLayout = (RelativeLayout) mFeedFooter.findViewById(R.id.lc_feed_footer_load);
	}
	
	public ViewGroup getView(){
		return mFeedFooter;
	}
	
	public void setFeedFooterLoading(boolean state){
		if(state){
			mFeedFooterMoreLayout.setVisibility(View.INVISIBLE);
			mFeedFooterLoadLayout.setVisibility(View.VISIBLE);
		}else{
			mFeedFooterMoreLayout.setVisibility(View.VISIBLE);
			mFeedFooterLoadLayout.setVisibility(View.INVISIBLE);
		}
	}
	
	public void setVisibility(boolean visibility){
		if(visibility){
			this.mFeedFooterLayout.setVisibility(View.VISIBLE);
		}else{
			this.mFeedFooterLayout.setVisibility(View.GONE);
		}
	}
	
	public boolean getVisibility(){
		return this.mFeedFooterLayout.getVisibility() == View.VISIBLE;
	}
	
	public void setOnClickListener(OnClickListener onClickListener){
		this.mFeedFooterMoreLayout.setOnClickListener(onClickListener);
	}
	
}
