package com.renren.mobile.chat.ui;



import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;

import com.common.utils.SearchAble;
import com.core.orm.ORM;
import com.renren.mobile.chat.common.PinyinUtils;
import com.renren.mobile.chat.database.Contact_Column;


public  class BaseSearchItem  implements SearchAble{
	

	public String name;

	public boolean[] isDye;// 每个字的涂色标志位

	public boolean isHaveDyed;// 是否被涂色

	public boolean isMatched;// 是否被匹配过（mapsearch防止二次涂色用）

	public char[][] mNamePinyin; // 搜索时用的索引
	
	public char mAleph;
	
	public boolean mIsShowAlephLabel_InAll = false;
	public boolean mIsShowAlephLabel_Choose = false;
	public boolean mIsShowAlephLabel_InOnline = false;
	public boolean mIsShowAlephLabel_InSearch = false;
	
	//@ORM(mappingColumn = Contact_Column.NAME_LENGTH)
	//public int mNameLength;
	
//	public int getmNameLength(String name) {
//		return PinyinUtils.nameLength(name);
//	}

//	public void setmNameLength(int mNameLength) {
//		this.mNameLength = mNameLength;
//	}

//	public String getmNamePinyin_String() {
//		return mNamePinyin_String;
//	}

//	public void setmNamePinyin_String(String mNamePinyin_String) {
//		this.mNamePinyin_String = mNamePinyin_String;
//	}

	/**姓的全拼 */
	@ORM(mappingColumn = Contact_Column.XIN_PIN_YIN)
	public String quanPin  = "";
	
	@ORM(mappingColumn = Contact_Column.USER_NAME_PINYIN)
	public String mNamePinyin_String  = "";
	
	public boolean isDye(int i) {
		if(isDye!=null&&i<isDye.length){
			return this.isDye[i];
		}
		return false;
	}

	public boolean isInitDye() {
		if (this.isDye == null)
			return false;
		else
			return true;
	}
	public int getDyeNum() {
		return this.isDye.length;
	}
	public void setDye(int i, boolean isDye) {
		this.isDye[i] = isDye;
		this.setHaveDyed(true);
	}
	
	public void nameSearched(int i){
		setDye(i, true);
	}
	public boolean isSearched(){
		return isHaveDyed();
	}
	

	/**
	 * 还原涂色标示位
	 */
	public void bleach() {
		for (int i = 0; i < this.isDye.length; i++) {
			this.isDye[i] = false;
		}
		this.isHaveDyed = false;
		this.isMatched = false;
	}
	
	/**
	 * 初始化涂色数组，数组长度和名字的长度相同
	 */
	public void initDye(int nameLenth){
		isDye = new boolean[nameLenth];
		setHaveDyed(false);
	}
	
	public static ForegroundColorSpan span = new ForegroundColorSpan(0x99cc6600); 
	public SpannableStringBuilder getDyeContactName(){
		SpannableStringBuilder style = new SpannableStringBuilder(name);
		if (this.isInitDye() && this.isHaveDyed()) {
			for (int i = 0; i+1 <= name.length(); i++) {
				if (this.getName().charAt(i) != ' ' && this.getName().charAt(i) != '　') {
					if (this.isDye(i)) {
						style.setSpan(new ForegroundColorSpan(0x99cc6600), i, i + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
					} else {
						style.delete(i, i);
					}
				}
			}
		}
		return style;
	}
	

	public void setNamePinyin(char[][] namePinyin) {
		this.mNamePinyin = namePinyin;
		this.mNamePinyin_String = PinyinUtils.array2String(namePinyin);
	}

	public char[][] getNamePinyin() {
		return mNamePinyin;
	}

	public void setHaveDyed(boolean isHaveDyed) {
		this.isHaveDyed = isHaveDyed;
	}

	public boolean isHaveDyed() {
		return isHaveDyed;
	}

	public String getName() {
		if (TextUtils.isEmpty(name)) {
			return "#";
		}
		return name;
	}
	
	public void setName(String name){
		this.name = name;
	}
	

	public void setQuanPin(String index) {
		quanPin = index;
	}

	public String getQuanPin() {
		return quanPin;
	}


	public void setMatched(boolean b) {
		isMatched=b;
	}

	public boolean isMatched() {
		return isMatched;
	}

	public char getAleph() {
		return mAleph;
	}

	
}
