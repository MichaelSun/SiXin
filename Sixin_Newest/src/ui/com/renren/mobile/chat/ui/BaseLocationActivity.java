package com.renren.mobile.chat.ui;

import org.json.JSONObject;

import android.content.Context;
import android.os.Handler;

import com.core.json.JsonObject;
import com.core.json.JsonParser;
import com.core.json.JsonValue;
import com.renren.mobile.chat.lbs.ILocationListener;
import com.renren.mobile.chat.lbs.LocationThread;

public class BaseLocationActivity extends BaseActivity {
	protected  ILocationListener mLocationListener;
	public final int LATLON_DEFAULT = 255*1000000;
	protected LocationThread locationThread;
	
	public void getLocaiton(Context context, ILocationListener locationListener) {
		mLocationListener = locationListener;
		locationThread = new LocationThread(context, locationandler);
		locationThread.chekPosition();
		
		//如果本地缓存中游数据，则直接调用mLocationListener.onLocateSuccess()方法。
	}
	
	protected Handler locationandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case LocationThread.RESULT_READY:
			case LocationThread.RESULT_TIMEOUT:
				
				String latlonString = ((JSONObject)msg.obj).toString();
				JsonObject latlon = null;
				if(latlonString != null) {
					JsonValue value = JsonParser.parse(latlonString);
					if(value instanceof JsonObject) {
						latlon = (JsonObject) value;
					}
				}
				mLocationListener.onLocateSuccess(LATLON_DEFAULT, LATLON_DEFAULT, latlon);
				break;
			case LocationThread.RESULT_FAIL:
				mLocationListener.onLocateFail();
				break;
			default:
				break;
			}
		};
	};

}
