package com.common.utils;

import org.json.JSONException;
import org.json.JSONObject;

import android.telephony.CellLocation;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;

public class CdmaUtil {
	public static JSONObject getCdmaInfo(TelephonyManager telephoneManager) {
		JSONObject connectedCellInfo = new JSONObject();

		CellLocation sm = telephoneManager.getCellLocation();
		if (sm instanceof CdmaCellLocation) {
			CdmaCellLocation cdmaCellInfo = ((CdmaCellLocation) telephoneManager.getCellLocation());
			try {
				String imei = telephoneManager.getDeviceId();
				String imsi = telephoneManager.getSubscriberId();
				String carrier = telephoneManager.getSimState() == TelephonyManager.SIM_STATE_READY ? telephoneManager.getSimOperator() : "";
				connectedCellInfo.put("imei", imei == null ? "" : imei);
				connectedCellInfo.put("imsi", imsi == null ? "" : imsi);
				connectedCellInfo.put("radio_type", "cdma");
				connectedCellInfo.put("carrier", carrier == null ? "" : carrier);
				connectedCellInfo.put("bid", String.valueOf(cdmaCellInfo.getBaseStationId()));
				connectedCellInfo.put("sid", String.valueOf(cdmaCellInfo.getSystemId()));
				connectedCellInfo.put("nid", String.valueOf(cdmaCellInfo.getNetworkId()));				
				double latDouble = (double)cdmaCellInfo.getBaseStationLatitude()  /14400;  
				double lonDouble = (double)cdmaCellInfo.getBaseStationLongitude() /14400;
//				Methods.showToast("lat:"+latDouble+"lon:"+lonDouble, false);
				long lat=(long) (latDouble*1000000);
				long lon=(long) (lonDouble*1000000);
//				Methods.showToast("lat:"+lat+"lon:"+lon, false);
				connectedCellInfo.put("base_station_latitude", String.valueOf(lat));
				connectedCellInfo.put("base_station_longitude", String.valueOf(lon));
				connectedCellInfo.put("time", String.valueOf(System.currentTimeMillis()));
				
				
				// Configuration con = getResources().getConfiguration();
				// if (con != null) {
				// connectedCellInfo.put("home_mobile_conutry_code",
				// String.valueOf(con.mcc));
				// connectedCellInfo.put("home_mobile_network_code",
				// String.valueOf(con.mnc));
				// }
				// locationPut("cell_tower_connected_info", connectedCellInfo);

				// "bid":"32945"//cell id
				// "sid":"1382"//系统识别码
				// "nid":"2"//网络标识码
				// "base_station_latitude":"39959830"
				// "base_station_longitude":"116440437"
				// "time":"51595352"//获取基站信息的时间点
				return connectedCellInfo;
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// cellLocation = true;	
			
		}
		return null;
	}
}
