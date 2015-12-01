package com.common.statistics;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.atomic.AtomicBoolean;

import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import com.common.manager.LoginManager;
import com.common.mcs.INetRequest;
import com.common.mcs.INetResponse;
import com.common.mcs.McsServiceProvider;
import com.common.utils.DeviceUtil;
import com.common.utils.Methods;
import com.core.json.JsonObject;
import com.core.json.JsonParser;
import com.core.json.JsonValue;
import com.core.util.AbstractApplication;
import com.core.util.SystemService;

/**
 * 本地统计管理类
 * **/


public class LocalStatisticsManager {
	
	private long interval_time = 3*60*60*1000;
	
	public SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	/**
	 * 切换前台统计常量
	 */
	public static final String LOCAL_STORE_NAME = "statistics";
	public static final String LAST_UPLOAD_STATISTICS_TIME = "lastUploadStatisticsTime";
	/**
	 * 后台统计常量
	 */
	public static final String LOCAL_STORE_BACKGROUND_NAME = "local_background_statistics";
	public static final String LAST_UPLOAD_BACKGROUND_STATISTICS_TIME = "lastUploadBackgroundStatisticsTime";
	
	//public static final String LOG_NAME = "fxch";
	
	/**
	 * 本地存储
	 */
	private SharedPreferences statisticsSP;
	
	/**
	 * 本地统计Json对象
	 */
	public JsonObject localStatistics;
	
	/**
	 * 上次上传时间
	 */
	public long lastUploadStatisticsTime = 0;
	
	/**
	 * 后台上次上传时间
	 */
	public long LastUploadBackgroundStatisticsTime = 0;
	
	/**
	 * "上传中"标志位
	 */
	public AtomicBoolean uploading= new AtomicBoolean(false);

	/**
	 * 后台"上传中"标志位
	 */
	public AtomicBoolean background_uploading= new AtomicBoolean(false);
	
	/**
	 * 单例
	 */
	private static LocalStatisticsManager localStatisticsManager = new LocalStatisticsManager();
	
	/**
	 * 构造函数
	 */
	private LocalStatisticsManager(){
		//实例化前台统计sharedPreferences对象
		statisticsSP = AbstractApplication
				.getAppContext()
				.getSharedPreferences(
						"statisticsSP",
						AbstractApplication.getAppContext().MODE_WORLD_READABLE
								| AbstractApplication.getAppContext().MODE_WORLD_WRITEABLE);
		;
		
		localStatistics = new JsonObject();
	}
	
	/**
	 * 获取单例对象
	 * @return
	 */
	public static LocalStatisticsManager getInstance(){
		return localStatisticsManager;
	}
	
	/*************************************后台统计********************************************************/
	/**
	 * 上传后台运行统计数据-供调用
	 */
	public void uploadBackgroundRunStatistics(){
		
		//获取上次上传时间
		long tempTime = statisticsSP.getLong(LAST_UPLOAD_BACKGROUND_STATISTICS_TIME, 0);

		if(tempTime >= 0){
			LastUploadBackgroundStatisticsTime = tempTime;
		}
		long currentTime = System.currentTimeMillis();
		
		if((currentTime - LastUploadBackgroundStatisticsTime) > interval_time && !background_uploading.get() && !LoginManager.getInstance().isLogout()){
			//上传后台统计数据
			backgroundRunStatistics(currentTime);
		}
	}
	
	/**
	 * 后台运行统计
	 */
	private synchronized void backgroundRunStatistics(final long time){
		background_uploading.set(true);
		McsServiceProvider.getProvider().backgroundRunStatistics( new INetResponse() {
			@Override
			public void response(INetRequest req, JsonValue obj) {
				if (obj != null && obj instanceof JsonObject) {
					final JsonObject jObj = (JsonObject) obj;
					if (Methods.checkNoError(req, jObj)) {
						int result = (int) jObj.getNum("result");
						if (result == 1) {
							//如果上传成功，将当前时间处理后赋值给LastUploadBackgroundStatisticsTime并存储到preferences
							//设置上次上传时间
							Calendar cal = Calendar.getInstance();
							cal.setTimeInMillis(time);
//							cal.set(Calendar.HOUR_OF_DAY, 0);
//							cal.set(Calendar.MINUTE, 0);
//							cal.set(Calendar.SECOND, 0);
							LastUploadBackgroundStatisticsTime = cal.getTimeInMillis();
							
							SharedPreferences.Editor editor = statisticsSP.edit();
							editor.putLong(LAST_UPLOAD_BACKGROUND_STATISTICS_TIME, LastUploadBackgroundStatisticsTime);
							editor.commit();
						}
					}
				}
				background_uploading.set(false);
			}
		});
	}
	/*************************************后台统计********************************************************/
	
	
	/*************************************前台统计********************************************************/
	/**
	 * 记录次数
	 * @param key 
	 */
	private synchronized void put(String key){
		long i = localStatistics.getNum(key);
		localStatistics.put(key, i+1);
	}
	
	/**
	 * 群聊新增统计
	 * @param key
	 */
	public synchronized void computeMultiChatStatistics(String key){
		
		//获取上次统计数据
		String record = statisticsSP.getString(LOCAL_STORE_NAME, null);

		if (null != record) {
			localStatistics = (JsonObject) JsonParser
					.parse(record);
		}
		//统计
		put(key);
		
		SharedPreferences.Editor editor = statisticsSP.edit();
		editor.putString(LOCAL_STORE_NAME, localStatistics.toJsonString());
		editor.commit();
		
		//上传统计数据
		toUploadStatistics();
	}
	
	/**
	 * 统计
	 * @param awakeWay 唤醒方式（在isActive为false的情况下）
	 * @param isActive 是否是主动运行
	 */
	public synchronized void computeStatistics(String awokeWay, boolean isActive){
		
		//获取上次统计数据
		String record = statisticsSP.getString(LOCAL_STORE_NAME, null);
		
		if (null != record) {
			localStatistics = (JsonObject) JsonParser
					.parse(record);
		}
		
		//统计
		if (isActive) {
			put(Htf.INITIATIVE_RUN);
		} else {
			put(awokeWay);
		}
		SharedPreferences.Editor editor = statisticsSP.edit();
		editor.putString(LOCAL_STORE_NAME, localStatistics.toJsonString());
		editor.commit();
		
		//上传统计数据
		toUploadStatistics();
	}
	
	/**
	 * 上传push和主动运行统计数据-供调用
	 */
	private void toUploadStatistics(){
		
		//获取上次上传时间
		long tempTime = statisticsSP.getLong(LAST_UPLOAD_STATISTICS_TIME, 0);

		if(tempTime >= 0){
			lastUploadStatisticsTime = tempTime;
		}
		long currentTime = System.currentTimeMillis();
		
		if((currentTime - lastUploadStatisticsTime) > interval_time && !uploading.get()){
			//上传统计数据
			uploadStatistics(currentTime);
		}
	}
	
	/**
	 * 统计方法
	 */
	private synchronized void uploadStatistics(final long time){
		uploading.set(true);
		
		//上传回调接口实现
		INetResponse response = new INetResponse() {
			
			@Override
			public void response(INetRequest req, JsonValue obj) {
				if (obj != null && obj instanceof JsonObject) {
					final JsonObject map = (JsonObject) obj;
					if (Methods.checkNoError(req, map)) {
						//如果上传成功，将当前时间处理后赋值给lastUploadStatisticsTime并存储到preferences
						//设置上次上传时间
						Calendar cal = Calendar.getInstance();
						cal.setTimeInMillis(time);
//						cal.set(Calendar.HOUR_OF_DAY, 0);
//						cal.set(Calendar.MINUTE, 0);
//						cal.set(Calendar.SECOND, 0);
						lastUploadStatisticsTime = cal.getTimeInMillis();
						
						SharedPreferences.Editor editor = statisticsSP.edit();
						editor.putLong(LAST_UPLOAD_STATISTICS_TIME, lastUploadStatisticsTime);
						editor.commit();
						
						//清空数据
						clear();
						editor.remove(LOCAL_STORE_NAME);
						editor.commit();
					}
				}
				uploading.set(false);
			}
		};
		
		if(null != statisticsSP.getString(LOCAL_STORE_NAME, null) )
			McsServiceProvider.getProvider().uploadStatistics(statisticsSP.getString(LOCAL_STORE_NAME, null), response);
	}
	
	/**
	 * 本地数据统计清零
	 */
	private synchronized void clear(){
		localStatistics.clear();
	}
	/*************************************前台统计********************************************************/
	
	/**
	 * 联网统计，只要装上客户端就要统计
	 * */
	public void activityClient() {
//		boolean netwrok_connection = statisticsSP.getBoolean("netwrok_connection", false);
//		if (!netwrok_connection) {
//			try {
//				ConnectivityManager connectivityManager = SystemService.sConnectivityManager;
//				NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
//
//				if (activeNetInfo != null) {
//					TelephonyManager tm = SystemService.sTelephonyManager;
//					INetResponse response = new INetResponse() {
//						
//						@Override
//						public void response(INetRequest req, JsonValue obj) {
//							int result = (int) ((JsonObject) obj).getNum("result");
//							if (result == 1) {} else {
//								SharedPreferences.Editor editor = statisticsSP.edit();
//								editor.putBoolean("netwrok_connection", false);
//								editor.commit();
//							}
//
//						}
//					};
//
//					SharedPreferences.Editor editor = statisticsSP.edit();
//					editor.putBoolean("netwrok_connection", true);
//					editor.commit();
//
//					String imei = tm.getDeviceId();
//					if (tm != null && !TextUtils.isEmpty(imei)) {
//
//					} else {
//						imei = DeviceUtil.getInstance().getLocalMacAddress(AbstractApplication.getAppContext());
//					}
//					McsServiceProvider.getProvider().activeClient(imei, 2, response, false);
//				}
//			}
//			catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
	}


	/**
	 * 激活统计
	 * */
	public void sendActivityRequest() {
		
		boolean mActivateFlag = statisticsSP.getBoolean("activate_statistics", false);
		if (mActivateFlag) {
			return;
		}
		TelephonyManager tm = SystemService.sTelephonyManager;
		INetResponse response = new INetResponse() {
			@Override
			public void response(INetRequest req, JsonValue obj) {
				if (obj instanceof JsonObject) {
					int result = (int) ((JsonObject) obj).getNum("result");
					if (result == 1) {
					}else{
						SharedPreferences.Editor editor = statisticsSP.edit();
						editor.putBoolean("activate_statistics", false);
						editor.commit();
					}
				}
			}
		};
		
		SharedPreferences.Editor editor = statisticsSP.edit();
		editor.putBoolean("activate_statistics", true);
		editor.commit();
		
		String imei = tm.getDeviceId();
		if (tm != null && !TextUtils.isEmpty(imei)) {

		} else {
			imei = getLocalMacAddress();
		}
		McsServiceProvider.getProvider().activeClient(imei, 1, response, false);

	}
	
	public static String getLocalMacAddress() {
		String mac = "000000";
		WifiManager wifi = SystemService.sWifiManager;
		if (wifi != null) {
			WifiInfo info = wifi.getConnectionInfo();
			if (info != null) {
				mac = info.getMacAddress();
			}
		}
		return mac;
	}

}
