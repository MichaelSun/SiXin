package com.common.utils;


import android.telephony.NeighboringCellInfo;

/**
 * 为了解决1.6中无法获取info.lac的问题
 * @author he.cao
 *
 */
public class CellInfoUtil {
	
	public static final int getLac(NeighboringCellInfo info) {
		if (Methods.fitApiLevel(5)) {
			return info.getLac();
		} else {
			return 0;
		}
	}
	

}
