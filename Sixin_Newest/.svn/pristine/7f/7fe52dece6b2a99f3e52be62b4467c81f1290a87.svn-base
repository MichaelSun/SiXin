package com.renren.mobile.chat.contact;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.text.TextUtils;

import com.common.manager.LoginManager;
import com.core.json.JsonObject;
import com.core.json.JsonParser;
import com.renren.mobile.chat.common.RRInternalStorage;

public class ContactUtils {
	/**
	 * 13,14,15,18开头的11位数字
	 */
	public static final Pattern phonePattern = Pattern.compile("^1(3|4|5|8)[0-9]{9}$");

	/**
	 * 处理手机号
	 * 
	 * @param phoneNumber
	 *            需要处理的手机号
	 * @return String 处理过后的手机号
	 * */
	public static String treatmentPhoneNumber(String phoneNumber) {
		if (TextUtils.isEmpty(phoneNumber)) {
			return null;
		}
		phoneNumber = phoneNumber.trim();
		String temp = phoneNumber.replaceAll("-", "");

		String afterTreatmentPhoneNumber = null;
		if (TextUtils.isEmpty(phoneNumber)) {
			return null;
		}
		if (phoneNumber.startsWith("+86") && phoneNumber.length() == 14) {
			afterTreatmentPhoneNumber = phoneNumber.substring(3, phoneNumber.length());
		} else if (phoneNumber.startsWith("179511") && phoneNumber.length() == 16) {
			afterTreatmentPhoneNumber = phoneNumber.substring(5, phoneNumber.length());
		} else if (temp.length() == 11) {
			afterTreatmentPhoneNumber = temp;
		}
		if (afterTreatmentPhoneNumber != null) {
			Matcher m = phonePattern.matcher(afterTreatmentPhoneNumber);
			if (m.find()) {
				return afterTreatmentPhoneNumber;
			}
		}
		return null;
	}


	/**
	 * 将手机号转换为id（long类型）
	 * 
	 * */
	public static long transformPhoneToId(String phoneNum) {
		if (!TextUtils.isEmpty(phoneNum)) {
			if (phoneNum.length() <= 11) {
				return Long.valueOf(phoneNum);
			} else {
				return Long.valueOf(phoneNum.substring(phoneNum.length() - 11));
			}
		}
		return 0;
	}
	
	/**
	 * 设置同步标志位 是否通整合过联系人
	 * @param isSynced
	 */
	public static void setIsSynced(boolean isSynced){
	    JsonObject jo = new JsonObject();
	    jo.put(LoginManager.getInstance().getLoginInfo().mUserId + " isSynced", isSynced);
	    RRInternalStorage.saveString2File("isSynced",jo.toJsonString());
	}
	/**
	 * 获取同步标志位
	 * @return 是否整合过联系人
	 */
	public static boolean getIsSynced(){
	    JsonObject jo = new JsonObject();
	    
	    String info = RRInternalStorage.getStringFromFile("isSynced");
	    
	    if(TextUtils.isEmpty(info)){
		return false;
	    }else{
		jo=(JsonObject)JsonParser.parse(info);
		if(jo.getBool("isSynced")==true){
		    return true;
		}else{
		    return false;
		}
	    }
	}
}
