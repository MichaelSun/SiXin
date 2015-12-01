/*
 * (#)StringUtil.java 1.0 2012-3-19 2012-3-19 GMT+08:00
 */
package com.renren.mobile.chat.common;


public final class TextualUtil {
	public static final int NotALetter = -1;
	public static final int UpperLetter = 0;
	public static final int LowerLetter = 1;
	public static int checkLetter(char c) {
		if(c>='A' && c<='Z') {
			return UpperLetter;
		}
		if(c>='a' && c<= 'z') {
			return LowerLetter;
		}
		return NotALetter;
	}
	public static boolean isLetter(char c) {
		return checkLetter(c) != NotALetter;
	}
}
