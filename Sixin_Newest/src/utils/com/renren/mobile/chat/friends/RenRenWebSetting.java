package com.renren.mobile.chat.friends;

import android.webkit.WebSettings;

public class RenRenWebSetting {
	/**
	 * 不要删除这个类啦，这样做是有目的的啦。。。
	 * */
	public static void setWebSettingsAppCacheEnabled(WebSettings webSetting) {
		webSetting.setAppCacheEnabled(true);
	}

}
