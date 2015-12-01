package com.common.utils;


import android.content.Context;
import android.util.TypedValue;

public class ViewUtils {

	/**
	 * 将int数值转化为dip
	 * context建议通过getApplicationContext()传入
	 * */
	public static int TransIntToDip(int dimens, Context context) {
		int dip = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dimens, context.getResources().getDisplayMetrics());
		return dip;
	}

}
