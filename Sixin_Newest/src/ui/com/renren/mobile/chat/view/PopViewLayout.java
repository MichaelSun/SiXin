package com.renren.mobile.chat.view;

import android.content.Context;
import android.content.res.Configuration;
import android.util.AttributeSet;
import android.widget.TableLayout;

public class PopViewLayout extends TableLayout {
	CustomPopupWindow pop;
	public PopViewLayout(Context context,AttributeSet attr) {
		super(context, attr);
	}
	@Override
	protected void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
		pop.dismiss();
	}
	public CustomPopupWindow getPop() {
		return pop;
	}
	public void setPop(CustomPopupWindow pop) {
		this.pop = pop;
	}
	
}
