package com.renren.mobile.chat.lbs;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.Context;
import android.content.res.Configuration;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.telephony.CellLocation;
import android.telephony.NeighboringCellInfo;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import com.common.utils.CdmaUtil;
import com.common.utils.CellInfoUtil;
import com.common.utils.Methods;
import com.core.util.SystemService;
import com.renren.mobile.chat.R;
import com.renren.mobile.chat.RenrenChatApplication;
public class LocationThread extends Thread {
	private Context mContext;
	private TelephonyManager mTelephoneManager;
	private LocationManager mLocationManager;
	private WifiManager mWifiManager;
	private LocationListener mGpsLocationListener, mNetWorkLocationListener;
	private Handler mResultHandler;
	private JSONObject mLocationData;
	private static ArrayList<JSONObject> gpsLocationArray = new ArrayList<JSONObject>();
	private static ArrayList<JSONObject> networkLocationArray = new ArrayList<JSONObject>();
	private int mTarget = 0;
	private int mComplete = 0;
	private int mSucess = 0;
	public static final int TIME_OUT = 20;
	public static final int LOCATION_ARRAY_MAX_LENGTH = 3;
	public static final int LOCATE_UPDATE_MIN_TIME = 0;
	public static final int LOCATE_UPDATE_MIN_DISTANCE = 10;
	public static final int TYPE_GSM = 1;
	public static final int TYPE_WIFI = 2;
	public static final int TYPE_GPS = 4;
	public static final int TYPE_NETWORK = 8;
	public static final int TYPE_TIMEOUT = 16;
	public static final int TYPE_CANCEL = 64;
	public static final int TYPE_USE_CACHE = 128;
	public static final int FAIL_GSM = -1;
	public static final int FAIL_WIFI = -2;
	public static final int FAIL_GPS = -4;
	public static final int FAIL_NETWORK = -8;
	public static final int RESULT_FAIL = 0;
	public static final int RESULT_READY = 1;
	public static final int RESULT_CANCEL = 2;
	public static final int RESULT_TIMEOUT = 6;
	public LocationThread(Context context,Handler handler) {
		mResultHandler = handler;
		mContext = context;
		mLocationData = new JSONObject();
		mLocationManager = SystemService.sLocationManager;
		mTelephoneManager = SystemService.sTelephonyManager;
		mWifiManager = SystemService.sWifiManager;
	}
	public void chekPosition(){
		this.start();
	}
	@Override
	public void run() {
		checkPosition("0", "0", "0", "0", TIME_OUT, LOCATE_UPDATE_MIN_TIME, LOCATE_UPDATE_MIN_DISTANCE);
	}
	/**
	 * 获取位置信息
	 * 
	 * @param gps
	 *            是否使用gps模块
	 * @param cell
	 *            是否收集基站信息
	 * @param wifi
	 *            是否收集wifi信息
	 * @param googleApi
	 *            是否使用network模块
	 * @param timeout
	 *            定位超时时间
	 * @param minitime
	 *            locationProvider的最小时间间隔
	 * @param meter
	 *            locationProvider的最小距离间隔
	 * 
	 * @author tian.wang
	 * **/
	public void checkPosition(String gps, String cell, String wifi, String googleApi, int timeout, int minitime, int meter) {
		locationPut("os_type", android.os.Build.MODEL + ";" + android.os.Build.VERSION.SDK + ";" + android.os.Build.VERSION.RELEASE);
		mComplete = 0;
		mSucess = 0;
		mTarget = 0;
		if ("0".equals(gps)) {
			mTarget += TYPE_GPS;
		}
		if ("0".equals(cell)) {
			mTarget += TYPE_GSM;
		}
		if ("0".equals(wifi)) {
			mTarget += TYPE_WIFI;
		}
		if ("0".equals(googleApi)) {
			mTarget += TYPE_NETWORK;
		}
		if ((mTarget & TYPE_GSM) == TYPE_GSM) {
			getGSMInfo();
		}
		if ((mTarget & TYPE_GPS) == TYPE_GPS) {
			getGPSinfo(minitime, meter);
		}
		if ((mTarget & TYPE_WIFI) == TYPE_WIFI) {
			getWifiInfo();
		}
		if ((mTarget & TYPE_NETWORK) == TYPE_NETWORK) {
			LocationHandler.postAtTime(new Runnable() {
				@Override
				public void run() {
					getNetWorkinfo(0, 0);
				}
			}, SystemClock.uptimeMillis() + 1 * 1000);
		}
		if (mTarget == 0) {
			LocationHandler.sendEmptyMessage(TYPE_CANCEL);
		}
	}
	private Handler LocationHandler = new Handler() {
		public void handleMessage(Message msg) {
			mSucess = 0;
			switch (msg.what) {
			case TYPE_GPS:
				mSucess += TYPE_GPS;
				mComplete += TYPE_GPS;
				completeTask();
				break;
			case TYPE_GSM:
				mSucess += TYPE_GSM;
				mComplete += TYPE_GSM;
				break;
			case TYPE_WIFI:
				mSucess += TYPE_WIFI;
				mComplete += TYPE_WIFI;
				break;
			case TYPE_NETWORK:
				mSucess += TYPE_NETWORK;
				mComplete += TYPE_NETWORK;
				completeTask();
				break;
			case TYPE_TIMEOUT:
				mComplete += TYPE_TIMEOUT;
				mSucess += TYPE_TIMEOUT;
				completeTask();
				break;
			case TYPE_CANCEL:
				mComplete += TYPE_CANCEL;
				completeTask();
				break;
			case FAIL_GPS:
				mComplete += TYPE_GPS;
				break;
			case FAIL_NETWORK:
				mComplete += TYPE_NETWORK;
				break;
			case FAIL_WIFI:
				mComplete += TYPE_WIFI;
				break;
			case FAIL_GSM:
				mComplete += TYPE_GSM;
				break;
			}
		}
	};
	private void completeTask() {
		StringBuffer sb = new StringBuffer();
		int result = 0;
		if ((mSucess & TYPE_GPS) == TYPE_GPS) {
			sb.append(RenrenChatApplication.getmContext().getResources().getString(R.string.LocationThread_java_1));		//LocationThread_java_1=GPS 成功; 
			result += TYPE_GPS;
		} else {
			sb.append(RenrenChatApplication.getmContext().getResources().getString(R.string.LocationThread_java_2));		//LocationThread_java_2=GPS 失败; 
		}
		if ((mSucess & TYPE_GSM) == TYPE_GSM) {
			sb.append(RenrenChatApplication.getmContext().getResources().getString(R.string.LocationThread_java_3));		//LocationThread_java_3=gsm 成功; 
			result += TYPE_GSM;
		} else {
			sb.append(RenrenChatApplication.getmContext().getResources().getString(R.string.LocationThread_java_4));		//LocationThread_java_4=gsm 失败; 
		}
		if ((mSucess & TYPE_WIFI) == TYPE_WIFI) {
			sb.append(RenrenChatApplication.getmContext().getResources().getString(R.string.LocationThread_java_5));		//LocationThread_java_5=wifi 成功; 
			result += TYPE_WIFI;
		} else {
			sb.append(RenrenChatApplication.getmContext().getResources().getString(R.string.LocationThread_java_6));		//LocationThread_java_6=wifi 失败; 
		}
		if ((mSucess & TYPE_NETWORK) == TYPE_NETWORK) {
			sb.append(RenrenChatApplication.getmContext().getResources().getString(R.string.LocationThread_java_7));		//LocationThread_java_7=network 成功; 
			result += TYPE_NETWORK;
		} else {
			sb.append(RenrenChatApplication.getmContext().getResources().getString(R.string.LocationThread_java_8));		//LocationThread_java_8=network 失败; 
		}
		if ((mSucess & TYPE_USE_CACHE) == TYPE_USE_CACHE) {
			sb.append("use cache");
			result += TYPE_USE_CACHE;
		}
		result = RESULT_FAIL;
		if (mSucess != 0) {
			if ((mSucess & TYPE_TIMEOUT) == TYPE_TIMEOUT) {
				result = RESULT_TIMEOUT;
			} else {
				result = RESULT_READY;
			}
		} else {
			if ((mComplete & TYPE_CANCEL) == TYPE_CANCEL) {
				result = RESULT_CANCEL;
			} else if ((mComplete & TYPE_TIMEOUT) == TYPE_TIMEOUT) {
				result = RESULT_FAIL;
			}
			if ((mComplete & TYPE_GSM) == TYPE_GSM) {
				result = RESULT_READY;
			}
			if ((mComplete & TYPE_WIFI) == TYPE_WIFI) {
				result = RESULT_READY;
			}
		}
		closeLocation(result);
	}
	/**
	 * 关闭定位模块，并且向页面发送定为结果
	 * */
	public void closeLocation(int result) {
		if (mLocationManager != null) {
			if (mNetWorkLocationListener != null) {
				mLocationManager.removeUpdates(mNetWorkLocationListener);
				mNetWorkLocationListener = null;
			}
			if (mGpsLocationListener != null) {
				mLocationManager.removeUpdates(mGpsLocationListener);
				mGpsLocationListener = null;
			}
		}
		Message message = mResultHandler.obtainMessage();
		message.obj = this.mLocationData;
		message.what = result;
		message.sendToTarget();
	}
	/**
	 * 向最终的位置json串中写入获取的数据
	 * 
	 * @author tian.wang
	 * */
	private synchronized boolean locationPut(String key, Object value) {
		try {
			if (mLocationData != null) {
				mLocationData.put(key, value);
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	/**
	 * 获取用户连接基站的信息
	 * 
	 * "cell_tower_connected_info": {
	 * 
	 * "imei": "354059021137664",
	 * Identity)是国际移动设备身份码的缩写，国际移动装备辨识码，是由15位数字组成的RenrenChatApplication.getmContext().getResources().getString(R.string.LocationThread_java_9)，它与每台手机一一对应，而且该码是全世界唯一的。		//LocationThread_java_9=电子串号; 
	 * 
	 * "imsi": "460022105507870",
	 * 国际移动用户识别码（IMSI：InternationalMobileSubscriberIdentificationNumber
	 * ）是区别移动用户的标志，储存在SIM卡中，可用于区别移动用户的有效信息。
	 * 
	 * "radio_type": "gsm", 
	 * 
	 * "carrier": RenrenChatApplication.getmContext().getResources().getString(R.string.LocationThread_java_10),		//LocationThread_java_10=中国移动; 
	 * 
	 * "cell_id": "2512", 
	 * 
	 * "location_area_code": "415", 
	 * 
	 * "home_mobile_conutry_code": "310",
	 * 
	 * "home_mobile_network_code": "410",
	 * 
	 * "time": "51595352"
	 * 
	 * },
	 * 
	 */
	private void getGSMInfo() {
		if (mTelephoneManager == null) {
			LocationHandler.sendEmptyMessage(FAIL_GSM);
			return;
		}
		boolean isGsmInfoSucceed = false;
		boolean isNeighborInfoSucceed = false;
		CellLocation cellLocation = mTelephoneManager.getCellLocation();
		if (cellLocation != null) {
			if (cellLocation instanceof GsmCellLocation) {
				JSONObject connectedCellInfo = new JSONObject();
				GsmCellLocation gsmCellInfo = ((GsmCellLocation) mTelephoneManager.getCellLocation());
				try {
					String imei = mTelephoneManager.getDeviceId();
					String imsi = mTelephoneManager.getSubscriberId();
					String carrier = mTelephoneManager.getSimState() == TelephonyManager.SIM_STATE_READY ? mTelephoneManager.getSimOperator() : "";
					connectedCellInfo.put("imei", imei == null ? "" : imei);
					connectedCellInfo.put("imsi", imsi == null ? "" : imsi);
					connectedCellInfo.put("radio_type", "gsm");
					connectedCellInfo.put("carrier", carrier == null ? "" : carrier);
					connectedCellInfo.put("cell_id", String.valueOf(gsmCellInfo.getCid()));
					if (Methods.fitApiLevel(5)) {
						connectedCellInfo.put("location_area_code", String.valueOf(gsmCellInfo.getLac()));
					}
					connectedCellInfo.put("time", String.valueOf(System.currentTimeMillis()));
					Configuration con = mContext.getResources().getConfiguration();
					if (con != null) {
						connectedCellInfo.put("home_mobile_conutry_code", String.valueOf(con.mcc));
						connectedCellInfo.put("home_mobile_network_code", String.valueOf(con.mnc));
					}
					locationPut("cell_tower_connected_info", connectedCellInfo);
					locationPut("mobile_code", 1);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			} else {
				JSONObject connectedCdmaCellInfo = null;
				if (Integer.parseInt(Build.VERSION.SDK) >= 5) {
					connectedCdmaCellInfo = CdmaUtil.getCdmaInfo(mTelephoneManager);
					if (connectedCdmaCellInfo != null) {
						Configuration con = mContext.getResources().getConfiguration();
						if (con != null) {
							try {
								connectedCdmaCellInfo.put("home_mobile_conutry_code", String.valueOf(con.mcc));
								connectedCdmaCellInfo.put("home_mobile_network_code", String.valueOf(con.mnc));
							} catch (JSONException e) {
								e.printStackTrace();
							}
						}
						locationPut("cdma_cell_tower_connected_info", connectedCdmaCellInfo);
						locationPut("mobile_code", 2);
					}
				}
			}
			isGsmInfoSucceed = true;
		} else {
			isGsmInfoSucceed = false;
		}
		List<NeighboringCellInfo> neighborsList = mTelephoneManager.getNeighboringCellInfo();
		if (neighborsList != null) {
			int count = neighborsList.size();
			if (count > 10) {
				count = 10;
			}
			if (count > 0) {
				JSONArray neighborArray = new JSONArray();
				try {
					NeighboringCellInfo info;
					for (int i = 0; i < count; i++) {
						info = neighborsList.get(i);
						if (info != null) {
							JSONObject neighborObj = new JSONObject();
							neighborObj.put("cell_id", String.valueOf(info.getCid()));
							if (Methods.fitApiLevel(5)) {
								neighborObj.put("location_area_code", String.valueOf(CellInfoUtil.getLac(info)));
							}
							neighborObj.put("signal_strength", String.valueOf(info.getRssi()));
							neighborArray.put(neighborObj);
						}
					}
					JSONObject neighborInfoObj = new JSONObject();
					neighborInfoObj.put("time", String.valueOf(System.currentTimeMillis()));
					neighborInfoObj.put("cell_tower_list", neighborArray);
					locationPut("cell_tower_neighbors_info", neighborInfoObj);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			isNeighborInfoSucceed = true;
		} else {
			isNeighborInfoSucceed = false;
		}
		if (isGsmInfoSucceed || isNeighborInfoSucceed) {
			LocationHandler.sendEmptyMessage(TYPE_GSM);
		} else {
			LocationHandler.sendEmptyMessage(FAIL_GSM);
		}
	}
	/**
	 * 获取连接的wifi信息
	 * 
	 * "wifi_mac":"00:23:76:08:21:e7" WIFI MAC 地址 
	 * "wifi_time":"51595352" wifi定位时间 
	 * "wifi_ssid":"WeiZhong" wifi 名称 
	 * "wifi_bssid":"00:1f:a3:65:7f:00" wifi 接入点地址
	 * 
	 * @author tian.wang
	 */
	private void getWifiInfo() {
		if (mWifiManager == null) {
			LocationHandler.sendEmptyMessage(FAIL_WIFI);
			return;
		}
		boolean isWifiSucceed = false;
		WifiInfo connectWifiInfo = mWifiManager.getConnectionInfo();
		JSONObject connectedWifiInfo = new JSONObject();
		if (connectWifiInfo != null) {
			try {
				connectedWifiInfo.put("mac_address", connectWifiInfo.getMacAddress());
				connectedWifiInfo.put("wifi_ssid", connectWifiInfo.getSSID());
				connectedWifiInfo.put("wifi_bssid", connectWifiInfo.getBSSID());
				connectedWifiInfo.put("wifi_ip_address", String.valueOf(connectWifiInfo.getIpAddress()));
				connectedWifiInfo.put("time", String.valueOf(System.currentTimeMillis()));
			} catch (JSONException e) {
				e.printStackTrace();
			}
			isWifiSucceed = true;
		} else {
			isWifiSucceed = false;
		}
		locationPut("wifi_tower_connected_info", connectedWifiInfo);
		getNeighborsWifiInfo(isWifiSucceed);
	}
	/**
	 * 获取周围wifi信息
	 * 
	 * @author tian.wang
	 * */
	public void getNeighborsWifiInfo(boolean isWifiConneted) {
		List<ScanResult> mWifiList = mWifiManager.getScanResults();
		boolean wifiNeighborSucceed = false;
		if (mWifiList != null) {
			int size = mWifiList.size();
			if (size > 10) {
				size = 10;
			}
			if (size > 0) {
				JSONObject wifiNeighborInfo = new JSONObject();
				JSONArray wifiArray = new JSONArray();
				try {
					wifiNeighborInfo.put("time", String.valueOf(System.currentTimeMillis()));
					for (int i = 0; i < size; i++) {
						ScanResult sr = (mWifiList.get(i));
						JSONObject wifiObj = new JSONObject();
						try {
							wifiObj.put("wifi_ssid", String.valueOf(sr.SSID));
							wifiObj.put("wifi_bssid", String.valueOf(sr.BSSID));
							wifiObj.put("signal_strength", String.valueOf(sr.level));
						} catch (JSONException e) {
							e.printStackTrace();
						}
						wifiArray.put(wifiObj);
						wifiNeighborInfo.put("wifi_tower_list", wifiArray);
					}
					locationPut("wifi_tower_neighbors_info", wifiNeighborInfo);
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
			}
			wifiNeighborSucceed = true;
		} else {
			wifiNeighborSucceed = false;
		}
		if (isWifiConneted || wifiNeighborSucceed) {
			LocationHandler.sendEmptyMessage(TYPE_WIFI);
		} else {
			LocationHandler.sendEmptyMessage(FAIL_WIFI);
		}
	}
	/**
	 * 通过network模块获取位置信息
	 * 
	 * @author tian.wang
	 * */
	private void getNetWorkinfo(int minitime, int meter) {
		if (checkNetAvailable()) {
			if (isProviderAvailable(LocationManager.NETWORK_PROVIDER)) {
				if (mNetWorkLocationListener == null) {
					mNetWorkLocationListener = new LocationListener() {
						public void onLocationChanged(Location location) {
							double lat = location.getLatitude();
							double log = location.getLongitude();
							JSONObject networkJSONObj = new JSONObject();
							try {
								networkJSONObj.put("network_lat", String.valueOf((long) (1000000 * Double.valueOf(lat))));
								networkJSONObj.put("network_lon", String.valueOf((long) (1000000 * Double.valueOf(log))));
								networkJSONObj.put("network_time", String.valueOf(System.currentTimeMillis()));
								if (location.hasAccuracy()) {
									networkJSONObj.put("network_accuracy", String.valueOf(location.getAccuracy()));
								} else {
									networkJSONObj.put("network_accuracy", "-1");
								}
							} catch (JSONException e) {
								e.printStackTrace();
							}
							putNetworkLocation(networkJSONObj);
							LocationHandler.sendEmptyMessage(TYPE_NETWORK);
						}
						public void onProviderDisabled(String provider) {
						}
						public void onProviderEnabled(String provider) {
						}
						public void onStatusChanged(String provider, int status, Bundle extras) {
						}
					};
				}
				mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, minitime * 1000, meter, mNetWorkLocationListener);
			} else {
				LocationHandler.sendEmptyMessage(FAIL_NETWORK);
			}
		} else {
			LocationHandler.sendEmptyMessage(FAIL_NETWORK);
		}
	}
	private void getGPSinfo(int minitime, int meter) {
		if (isProviderAvailable(LocationManager.GPS_PROVIDER)) {
			if (mGpsLocationListener == null) {
				mGpsLocationListener = new LocationListener() {
					public void onLocationChanged(Location location) {
						double lat = location.getLatitude();
						double log = location.getLongitude();
						JSONObject gpsJSONObj = new JSONObject();
						try {
							gpsJSONObj.put("gps_lat", String.valueOf((long) (1000000 * Double.valueOf(lat))));
							gpsJSONObj.put("gps_lon", String.valueOf((long) (1000000 * Double.valueOf(log))));
							gpsJSONObj.put("gps_time", String.valueOf(System.currentTimeMillis()));
							if (location.hasAccuracy()) {
								gpsJSONObj.put("gps_accuracy", String.valueOf(location.getAccuracy()));
							} else {
								gpsJSONObj.put("gps_accuracy", "-1");
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
						putGpsLocation(gpsJSONObj);
						LocationHandler.sendEmptyMessage(TYPE_GPS);
					}
					public void onProviderDisabled(String provider) {
					}
					public void onProviderEnabled(String provider) {
					}
					public void onStatusChanged(String provider, int status, Bundle extras) {
					}
				};
			}
			mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, minitime * 1000, meter, mGpsLocationListener);
		} else {
			LocationHandler.sendEmptyMessage(FAIL_GPS);
		}
	}
	/**
	 * 检查网路是否可用
	 * */
	public static boolean checkNetAvailable() {
		ConnectivityManager manager = SystemService.sConnectivityManager;
		NetworkInfo netWrokInfo = manager.getActiveNetworkInfo();
		if (netWrokInfo == null || !netWrokInfo.isAvailable()) {
			return false;
		} else {
			return true;
		}
	}
	/**
	 * 检查location provider是否可用
	 * */
	private synchronized boolean isProviderAvailable(String provider) {
		if (mLocationManager != null) {
			try {
				List<String> list = mLocationManager.getAllProviders();
				if (list != null && list.contains(provider)) {
					return mLocationManager.isProviderEnabled(provider);
				} else {
					return false;
				}
			} catch (Exception e) {
				return false;
			}
		}
		return false;
	}
	private boolean putGpsLocation(JSONObject gpsLocationObj) {
		try {
			if (gpsLocationArray.size() >= LOCATION_ARRAY_MAX_LENGTH) {
				gpsLocationArray.remove(0);
			}
			gpsLocationArray.add(gpsLocationObj);
			JSONArray gpsJsonArray = new JSONArray();
			for (JSONObject obj : gpsLocationArray) {
				gpsJsonArray.put(obj);
			}
			locationPut("gps_location", gpsJsonArray);
		} catch (Exception e) {
			return false;
		}
		return true;
	}
	private boolean putNetworkLocation(JSONObject networkLocationObj) {
		try {
			if (networkLocationArray.size() >= LOCATION_ARRAY_MAX_LENGTH) {
				networkLocationArray.remove(0);
			}
			networkLocationArray.add(networkLocationObj);
			JSONArray networkJsonArray = new JSONArray();
			for (JSONObject obj : networkLocationArray) {
				networkJsonArray.put(obj);
			}
			locationPut("network_location", networkJsonArray);
		} catch (Exception e) {
			return false;
		}
		return true;
	}
}
