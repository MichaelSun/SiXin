package com.common.emotion.view;

import com.common.R;

import android.content.Context;
import android.graphics.Color;
import android.widget.ImageView;

public class VerticalPointView extends ImageView implements IDoubleStateIndicator{
	private static final int CHECK_SOURCE_ID = R.drawable.emotion_point_check;
	private static final int UNCHECK_SOURCE_ID = R.drawable.emotion_point_uncheck;
	public VerticalPointView(Context context) {
		super(context);
		this.unCheck();
	}
	@Override
	public void check(int position){
		this.setImageResource(CHECK_SOURCE_ID);
		this.setBackgroundColor(Color.RED);
	}
	@Override
	public void unCheck(){
		this.setImageResource(UNCHECK_SOURCE_ID);
		this.setBackgroundColor(Color.GRAY);
	}

}
