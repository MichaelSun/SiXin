package com.common.utils;


public interface SearchAble {
	public String getName();
	public String getQuanPin();
	public void setQuanPin(String str);
	public char[][] getNamePinyin();
	public void setNamePinyin(char[][] namepinyin);
	/**name中的第index位置被匹配了 */
	public void nameSearched(int index);
}
