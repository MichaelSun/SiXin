package com.renren.mobile.chat.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.renren.mobile.chat.R;

public class BaseNodataLayout {
	
	private RelativeLayout mNodataLayout;
	
	private ImageView mNodataImg;
	
	private TextView mNodataText;
	
	private Context mContext;
	
	public BaseNodataLayout(Context context) {
		this.mContext = context;
		LayoutInflater mInflater = LayoutInflater.from(this.mContext);
		mNodataLayout = (RelativeLayout) mInflater.inflate(R.layout.lc_nodata_layout, null);
		init();
	}
	
	private void init(){
		mNodataImg = (ImageView) mNodataLayout.findViewById(R.id.lc_nodata_img);
		mNodataText = (TextView) mNodataLayout.findViewById(R.id.lc_nodata_text);
	}
	
	public void setNodataImg(int resId){
		this.mNodataImg.setImageResource(resId);
		this.mNodataImg.setBackgroundResource(0);
	}
	
	public void setNodataText(String text){
		this.mNodataText.setText(text);
	}
	
	public ViewGroup getView(){
		return mNodataLayout;
	}
	
	public void setVisibility(boolean visibility){
		if(visibility){
			this.mNodataLayout.setVisibility(View.VISIBLE);
		}else{
			this.mNodataLayout.setVisibility(View.GONE);
		}
	}
	
	public boolean getVisibility(){
		boolean visibility = true;
		if(this.mNodataLayout.getVisibility() == View.VISIBLE){
			visibility = true;
		}else{
			visibility = false;
		}
		return visibility;
	}

}
