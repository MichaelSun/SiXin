package com.common.utils;

import com.core.util.CommonUtil;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

/**
 * 注册的工具类:获取用户的手机号和Gmail邮箱
 * 
 * @author shichao.song
 * 
 */
public class RegisterUtil {
	public static Context context;
	// 手机号
	public static String phoneNumber = null;
	// 默认取gmail的邮箱账号
	public static String gmailAccount = null;

	/**
	 * 得到用户的手机号
	 * 
	 * @author shichao.song
	 */
	public static String getPhoneNumber() {
		// 创建电话管理
		TelephonyManager mTelephonyManager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		// 获取手机号码
		String number = mTelephonyManager.getLine1Number();
		// 去掉手机号的前3位“+86”
		if (!TextUtils.isEmpty(number)) {
			if (number.startsWith("+86") && number.contains("+86")) {
				number = number.subSequence(3, number.length()).toString();
			}

			return number;
		}

		return null;
	}

	/**
	 * 返回当前手机绑定的Gmail账户
	 * 
	 * @author shichao.song
	 */
	public static String getGmailAccount() {
		AccountManager accountManager = AccountManager.get(context);
		Account[] accounts = accountManager.getAccountsByType("com.google");
		for (int i = 0; i < accounts.length; i++) {
			Account account = accounts[i];
			gmailAccount = account.toString().trim();
			CommonUtil.log("tag", "RegisterUtil getGmailAccount() gmail = "
					+ gmailAccount);
		}

		if (accounts.length > 0) {
			Account account = accounts[0];
			gmailAccount = account.toString().trim();
			// 得到的account的格式如下：
			// Account {name=shichaosong@gmail.com, type=com.google}
			// gmailAccount.split("=")[1].split(",")[0]
			// gmailAccount.subSequence(14, gmailAccount.length()-18)
			return gmailAccount.split("=")[1].split(",")[0];

		}

		return null;
	}

	/**
	 * test main
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		// 获取手机号和邮箱账户
		phoneNumber = getPhoneNumber();
		gmailAccount = getGmailAccount();

	}

}
