package com.renren.mobile.chat.ui.contact;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.core.util.ViewMapUtil;
import com.core.util.ViewMapping;
import com.renren.mobile.chat.R;

public class C_AlpheBarView extends FrameLayout{

	@ViewMapping(ID=R.id.cdw_alphe_viewgroup)
	public ViewGroup mAlpheTextGroup = null;
	
	@ViewMapping(ID=R.id.cdw_alphe_text)
	public TextView mAlpheText = null;
	
	
	public C_AlpheBarView(Context context){
		super(context);
		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.cdw_aleph_bar, this);
		ViewMapUtil.getUtil().viewMapping(this, this);
	}
	
	public C_AlpheBarView(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.cdw_aleph_bar, this);
		ViewMapUtil.getUtil().viewMapping(this, this);
	}
	public void setAlpha(int alpha){
		this.setAlpha(this.getBackground(), alpha);
		this.setAlpha(mAlpheTextGroup.getBackground(), alpha);
	}
	public void setAlpha(Drawable d,int alpha){
		if(d!=null){
			d.setAlpha(alpha);
		}
	}
	public void setText(String text){
		mAlpheText.setText(text);
		
	}
	
//	/**
//	 * 标题栏为多汉字时
//	 */
//	public void setTextViewLength(){
//		mAlpheText.setWidth(141);
//	}
	
//	public void setTextViewLength(int w){
//		mAlpheText.setWidth(w);
//	}
	
	public void setTextViewLength(int w){
		mAlpheTextGroup.getLayoutParams().width=w;
	}

}
