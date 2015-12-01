package com.renren.mobile.chat.lbs;

import com.core.json.JsonObject;


public interface ILocationListener {
	/**
	 * 定位成功 有可能lon和lat都是0
	 */
	public abstract void onLocateSuccess (long lat, long lon, JsonObject locateObj);
	
	/**
	 * 取消定位
	 */
	public abstract void onLocateCancel ();
	
	/**
	 * 定位失败
	 */
	public abstract void onLocateFail ();

}
