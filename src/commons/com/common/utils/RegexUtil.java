package com.common.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * @author kaining.yang
 * 正则工具类
 *
 */

public class RegexUtil {
	/*
	public static boolean isMobilePhone(String MobileNo) { 
		Pattern p = Pattern.compile("^[1][3458]([\\d]{9})$");
		Matcher m = p.matcher(MobileNo);
		return m.find(); 
	}
	
	public static boolean isEmailAddress(String EmailAddress) {
		Pattern p = Pattern.compile("^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+((\\.[a-zA-Z0-9_-]{2,3}){1,2})$");
		Matcher m = p.matcher(EmailAddress);
		return m.find(); 
	}
	
	public static boolean isCaptcha(String Captcha) {
		Pattern p = Pattern.compile("[\\d]{6}");
		Matcher m = p.matcher(Captcha);
		return m.find(); 
	}
	*/
	
	private static boolean isRegexTrue(String text, String regex) {
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(text);
		return m.find(); 
	}
	

	public static boolean isMobilePhone(String MobileNo) { 
		// return isRegexTrue(MobileNo, "^[1][3458]([\\d]{9})$");
		return isRegexTrue(MobileNo, "^1(3|4|5|8)[0-9]{9}$");
	}
	// new: ^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$
	// old: ^\\s*\\w+(?:\\.{0,1}[\\w-]+)*@[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)*\\.[a-zA-Z]+\\s*$
	public static boolean isEmailAddress(String EmailAddress) {
		return isRegexTrue(EmailAddress, "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$");
	}
	
	public static boolean isCaptcha(String Captcha) {
		return isRegexTrue(Captcha, "[\\d]{6}");
	}
	
	//验证输入的密码是否是为6-20位，由数字、字母(区分大小写)组成
	public static boolean isPassword(String password){
		return isRegexTrue(password, "^[A-Za-z0-9]{6,20}$");
	}
	
}
