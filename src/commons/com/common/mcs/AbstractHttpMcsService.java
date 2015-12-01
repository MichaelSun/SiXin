package com.common.mcs;

import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import com.common.R;
import com.common.app.AbstractRenrenApplication;
import com.common.app.AbstractRenrenApplication.IS_AUTO_VALUE;
import com.common.manager.LoginManager;
import com.common.utils.Config;
import com.common.utils.LanguageSettingUtil;
import com.common.utils.Methods;
import com.core.json.JsonArray;
import com.core.json.JsonObject;
import com.core.json.JsonValue;
import com.core.util.*;

import java.io.ByteArrayOutputStream;
import java.net.URLEncoder;
import java.util.Vector;

abstract class AbstractHttpMcsService implements IMcsService{

	/*3G 服务器的API KEY*/
	protected final static String MCS_API_KEY = AbstractRenrenApplication.API_KEY;
	/*本地Secret key*/
	// login返回成功后被付值为"运行時secret key".
	protected final static String LOCAL_SECRET_KEY =  AbstractRenrenApplication.SCERET_KEY;
	
//	public static String sRuntimeSecretKey = null;
//	public static String sRuntimeSessionKey = null;
	
	/**
	 * 获取登录请求的sig
	 * */
	protected final static String getSigForLogin(JsonObject data) {
		String[] keys = data.getKeys();
		StringBuilder sb = new StringBuilder();
		Vector<String> vecSig = new Vector<String>();
		for (String key : keys) {
			String val = data.getJsonValue(key).toString();
			sb.append(key).append('=').append(URLEncoder.encode(val)).append('&');
			if (val.length() > 50) {
				val = val.substring(0, 50);
			}
			vecSig.add(key + "=" + val);
		}
		String[] ss = new String[vecSig.size()];
		vecSig.copyInto(ss);
		return getSig(ss, LOCAL_SECRET_KEY);
	}
	
	/**
	 * 每个接口都必须要携带的统计信息
	 * 
	 * @return
	 */
	public final  String getClientInfo() {
		String imei = "";
		String mac = "";
		int mcc = -1; 
		int mnc = -1;
		if (AbstractRenrenApplication.getAppContext() != null) {
			TelephonyManager tm = SystemService.sTelephonyManager;
			
			mcc = AbstractRenrenApplication.getAppContext().getResources().getConfiguration().mcc;
			mnc = AbstractRenrenApplication.getAppContext().getResources().getConfiguration().mnc; 
			if (tm != null && !TextUtils.isEmpty(imei)) {

			} else {
				WifiManager wifi = SystemService.sWifiManager;
				if (wifi != null) {
					WifiInfo info = wifi.getConnectionInfo();
					if (info != null) {
						mac = info.getMacAddress();
						imei = mac;
					}
				}
			}
		}

		JsonObject clientInfo = new JsonObject();
		clientInfo.put("screen", AbstractRenrenApplication.SCREEN);
		clientInfo.put("os", Build.VERSION.SDK + "_" + Build.VERSION.RELEASE);
		clientInfo.put("model", Build.MODEL);
		clientInfo.put("from", AbstractRenrenApplication.FROM);
		String[] langStrs = LanguageSettingUtil.getInstance().getLanguage().split("[_]");
		clientInfo.put("language", String.format("%s_%s", langStrs[0].toLowerCase(), langStrs[1].toUpperCase()));
		clientInfo.put("uniqid", imei);
		clientInfo.put("mac", mac);
		clientInfo.put("version", AbstractRenrenApplication.VERSION_NAME);
		if(mcc != 0){
			clientInfo.put("other", String.format("%03d", mcc) + String .format("%02d", mnc) + ",");
		}
		return clientInfo.toJsonString();
	}
	
	protected static String getSig(JsonObject data) {
		String[] keys = data.getKeys();
		StringBuilder sb = new StringBuilder();
		Vector<String> vecSig = new Vector<String>();
		for (String key : keys) {
			String val = data.getJsonValue(key).toString();
			sb.append(key).append('=').append(URLEncoder.encode(val)).append('&');
			if (val.length() > 50) {
				val = val.substring(0, 50);
			}
			vecSig.add(key + "=" + val);
		}
		String[] ss = new String[vecSig.size()];
		vecSig.copyInto(ss);
		return getSig(ss, LoginManager.getInstance().getSecretKey());
	}
	
	/**
	 * 计算sig
	 * */
	protected static String getSig(String[] ss, String secretKey) {
		for (int i = 0; i < ss.length; i++) {
			for (int j = ss.length - 1; j > i; j--) {
				if (ss[j].compareTo(ss[j - 1]) < 0) {
					String temp = ss[j];
					ss[j] = ss[j - 1];
					ss[j - 1] = temp;
				}
			}
		}
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < ss.length; i++) {
			sb.append(ss[i]);
		}
		sb.append(secretKey);
		return Md5.toMD5(sb.toString());
	}
	protected static final INetRequest obtainINetRequest(String interfaceNameRestPath){
		INetRequest request = new HttpRequestWrapper();
		request.setUrl(Config.CURRENT_SERVER_URI+interfaceNameRestPath);
		return request;
	}
	protected static final INetRequest obtainRequest(){
		INetRequest request = new HttpRequestWrapper();
		request.setUrl(Config.CURRENT_SERVER_URI);
		return request;
	}
	protected static final INetRequest obtainINetRequest(String url,int type,INetResponse response){
		INetRequest request = new HttpRequestWrapper();
		request.setUrl(url);
		request.setType(type);
		request.setResponse(response);
		return request;
	}
	
	
	protected JsonObject obtainBaseRequestBundle(boolean batchRun){
		JsonObject bundle = new JsonObject();
		bundle.put("api_key", MCS_API_KEY);
		bundle.put("call_id", System.currentTimeMillis());
		// 使用批调用的请求不包含session_key, session_key统一由batch.run方法提供.
		if (!batchRun) {
			if (LoginManager.getInstance().getSecretKey() == null) {
//				Data.loadUserInfo(RenrenChatApplication.mContext);
			}
//			if(m_secretKey==null){
//				//?update by dingwei.chen?
//				m_secretKey = RenrenChatApplication.secretkey;
//			}
			if(LoginManager.getInstance().getSsssionKey()!=null){
				bundle.put("session_key", LoginManager.getInstance().getSsssionKey());
			}
		}
		bundle.put("client_info", getClientInfo());
		if(!"".equals(getMISCInfo())){
			bundle.put("misc", getMISCInfo());
		}
		
		//is_auto
		if(AbstractRenrenApplication.IS_AUTO == IS_AUTO_VALUE.BACKGROUND){
			bundle.put("is_auto", AbstractRenrenApplication.IS_AUTO);
		}
		/* *********************************************************/
		
		return bundle;
	}
	/**
	 * 每个接口都必须要携带的统计信息
	 * @description 
	 * 		misc字段定义
	 *         HTF：标志来源页面（即从哪个页面进入当前页面的）
	 *         离线or在线：0为在线，1为离线
	 *         联网状态：1对应wifi；2对应非wifi
	 * 		格式要求：最后一个字段后不加逗号。
	 * @return misc字符串
	 */
	protected static String getMISCInfo(){
		StringBuilder misc = new StringBuilder(",,");
		WifiManager wifi = SystemService.sWifiManager;
		if (wifi != null) {
			WifiInfo info = wifi.getConnectionInfo();
			if (info != null) {
				misc.append("1");
			}else{
				misc.append("2");
			}
		}
		else{
			misc.append("2");
		}
		return misc.toString();
	}
	
	/**
	 * 每个接口都必须要携带的统计信息
	 * @description 
	 * 		misc字段定义
	 *         HTF：标志来源页面（即从哪个页面进入当前页面的）
	 *         离线or在线：0为在线，1为离线
	 *         联网状态：1对应wifi；2对应非wifi
	 * 		格式要求：最后一个字段后不加逗号。
	 * @param HTF
	 * @param onlineState 在线状态
	 * @return misc字符串
	 */
	protected static String getMISCInfo(String HTF, String onlineState){

		StringBuilder misc = new StringBuilder("");
		if(null != HTF){
			misc.append(HTF);
		}
		misc.append(",");
		if(null != onlineState){
			misc.append(onlineState);
		}
		misc.append(",");

		WifiManager wifi = SystemService.sWifiManager;
		if (wifi != null) {
			WifiInfo info = wifi.getConnectionInfo();
			if (info != null) {
				misc.append("1");
			}else{
				misc.append("2");
			}
		}
		else{
			misc.append("2");
		}
		return misc.toString();
	}
	
//	@Override
//	public void setSecretKey(String key) {
//		CommonUtil.log("cdw", "set setSecretKey = "+key);
////		sRuntimeSecretKey = key;
//	}
	
//	@Override
//	public String getSecretKey() {
//		// TODO Auto-generated method stub
//		return sRuntimeSecretKey;
//	}
	
//	@Override
//	public void setSessionKey(String key) {
//		CommonUtil.log("cdw", "set setSessionKey = "+key);
//		sRuntimeSessionKey = key;
//	}
//	
//	@Override
//	public String getSessionKey() {
//		// TODO Auto-generated method stub
//		return sRuntimeSessionKey;
//	}
	
	protected void sendTestPluginRequest(String interfaceNameRest, INetResponse response) {
		INetRequest request = new HttpRequestWrapper();
		request.setUrl("http://10.9.18.79:8080/plugins/sxbbm" + interfaceNameRest);
		CommonUtil.log("lee"," requestUrl is : " + request.getUrl());
		request.setResponse(response);
		HttpProviderWrapper.getInstance().addRequest(request);
	}
	
	protected void sendRequest(String interfaceNameRest,JsonObject bundle,INetResponse response){
		INetRequest request = obtainINetRequest(interfaceNameRest);
		request.setData(bundle);
		CommonUtil.log("cdw", "发送参数为:"+bundle);
		request.setResponse(response);
		HttpProviderWrapper.getInstance().addRequest(request);
	}
	/**
	 * 构建照片上传的Multipart数据
	 */
	protected byte[] buildUploadPhotoData(long toId, byte[] imgData) {

		byte[] ret = null;
		try {
			String[] props = { "api_key", "call_id", "client_info", "format", "session_key", "toid", "v", "sig" };
			String[] values = { MCS_API_KEY, String.valueOf(System.currentTimeMillis()), getClientInfo(), "json", LoginManager.getInstance().getSsssionKey(), String.valueOf(toId), "1.0", "" };

			String[] params = new String[props.length - 1];
			for (int i = 0; i < params.length; i++) {
				if (values[i] != null && values[i].length() > 50) {
					params[i] = props[i] + "=" + values[i].substring(0, 50);
				} else {
					params[i] = props[i] + "=" + values[i];
				}

			}
			values[values.length - 1] = getSig(params, LoginManager.getInstance().getSecretKey());
			String BOUNDARY = "FlPm4LpSXsE"; // separate line
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < props.length; i++) { // send each property
				sb = sb.append("--");
				sb = sb.append(BOUNDARY);
				sb = sb.append("\r\n");
				sb = sb.append("Content-Disposition: form-data; name=\"" + props[i] + "\"\r\n\r\n");
				CommonUtil.log("lee","Disposition: " + props[i]+" = " + values[i]);
				sb = sb.append(values[i]);
				sb = sb.append("\r\n");
			}
			sb = sb.append("--");
			sb = sb.append(BOUNDARY);
			sb = sb.append("\r\n");
			sb = sb.append("Content-Disposition: form-data;name=\"data\";filename=\"" + DateFormat.now2() + ".jpg\"\r\n");
			sb = sb.append("Content-Type: image/jpg\r\n\r\n");
			CommonUtil.log("lee","Disposition: " + sb.toString());
			byte[] begin_data = sb.toString().getBytes("UTF-8");
			byte[] end_data = ("\r\n--" + BOUNDARY + "--\r\n").getBytes("UTF-8");
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			baos.write(begin_data);
			baos.write(imgData);
			baos.write(end_data);
			ret = baos.toByteArray();
			baos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}
	
	/**
	 * 构建照片上传的Multipart数据
	 */
	protected byte[] buildUploadPNGPhotoData(long toId, byte[] imgData) {

		byte[] ret = null;
		try {
			String[] props = { "api_key", "call_id", "client_info", "format", "session_key", "toid", "v", "sig" };
			String[] values = { MCS_API_KEY, String.valueOf(System.currentTimeMillis()), getClientInfo(), "json", LoginManager.getInstance().getSsssionKey(), String.valueOf(toId), "1.0", "" };
			String[] params = new String[props.length - 1];
			for (int i = 0; i < params.length; i++) {
				if (values[i] != null && values[i].length() > 50) {
					params[i] = props[i] + "=" + values[i].substring(0, 50);
				} else {
					params[i] = props[i] + "=" + values[i];
				}

			}
			values[values.length - 1] = getSig(params, LoginManager.getInstance().getSecretKey());
			String BOUNDARY = "FlPm4LpSXsE"; // separate line
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < props.length; i++) { // send each property
				sb = sb.append("--");
				sb = sb.append(BOUNDARY);
				sb = sb.append("\r\n");
				sb = sb.append("Content-Disposition: form-data; name=\"" + props[i] + "\"\r\n\r\n");
				CommonUtil.log("lee","Disposition: " + props[i]+" = " + values[i]);
				sb = sb.append(values[i]);
				sb = sb.append("\r\n");
			}
			sb = sb.append("--");
			sb = sb.append(BOUNDARY);
			sb = sb.append("\r\n");
			sb = sb.append("Content-Disposition: form-data;name=\"data\";filename=\"" + DateFormat.now2() + ".png\"\r\n");
			sb = sb.append("Content-Type: image/png\r\n\r\n");
			CommonUtil.log("lee","Disposition: " + sb.toString());
			byte[] begin_data = sb.toString().getBytes("UTF-8");
			byte[] end_data = ("\r\n--" + BOUNDARY + "--\r\n").getBytes("UTF-8");
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			baos.write(begin_data);
			baos.write(imgData);
			baos.write(end_data);
			ret = baos.toByteArray();
			baos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}
    /**
     * 构建头像上传的Multipart数据
     */
    protected byte[] buildUploadHeadData(byte[] imgData, int x, int y , int width, int height) {

        byte[] ret = null;
        try {
            String[] props = { "api_key", "call_id", "client_info", "format", "session_key",  "v",
                    "action","x","y","width","height","name","sig" };
            String[] values = { MCS_API_KEY, String.valueOf(System.currentTimeMillis()), getClientInfo(), "json",LoginManager.getInstance().getSsssionKey(),  "1.0",
                    "POST",String.valueOf(x),String.valueOf(y),
                    String.valueOf(width),String.valueOf(height),"tall", "" };


            String[] params = new String[props.length - 1];
            for (int i = 0; i < params.length; i++) {
                if (values[i] != null && values[i].length() > 50) {
                    params[i] = props[i] + "=" + values[i].substring(0, 50);
                } else {
                    params[i] = props[i] + "=" + values[i];
                }

            }
            values[values.length - 1] = getSig(params, LoginManager.getInstance().getSecretKey());
            String BOUNDARY = "FlPm4LpSXsE"; // separate line
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < props.length; i++) { // send each property
                sb = sb.append("--");
                sb = sb.append(BOUNDARY);
                sb = sb.append("\r\n");
                sb = sb.append("Content-Disposition: form-data; name=\"" + props[i] + "\"\r\n\r\n");
                sb = sb.append(values[i]);
                sb = sb.append("\r\n");
            }
            sb = sb.append("--");
            sb = sb.append(BOUNDARY);
            sb = sb.append("\r\n");
            sb = sb.append("Content-Disposition: form-data;name=\"data\";filename=\"" + DateFormat.now2() + ".jpg\"\r\n");
            sb = sb.append("Content-Type: image/jpg\r\n\r\n");
            byte[] begin_data = sb.toString().getBytes("UTF-8");
            byte[] end_data = ("\r\n--" + BOUNDARY + "--\r\n").getBytes("UTF-8");
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            baos.write(begin_data);
            baos.write(imgData);
            baos.write(end_data);
            ret = baos.toByteArray();
            baos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    /**
     * 构建图片上传的Multipart数据
     */
    protected byte[] buildUploadPhotoData(byte[] imgData) {

        byte[] ret = null;
        try {
            String[] props = { "api_key", "call_id", "client_info", "format", "session_key",  "v","action","name","sig" };
            String[] values = { MCS_API_KEY, String.valueOf(System.currentTimeMillis()), getClientInfo(), "json",LoginManager.getInstance().getSsssionKey(),  "1.0","POST","tall", "" };


            String[] params = new String[props.length - 1];
            for (int i = 0; i < params.length; i++) {
                if (values[i] != null && values[i].length() > 50) {
                    params[i] = props[i] + "=" + values[i].substring(0, 50);
                } else {
                    params[i] = props[i] + "=" + values[i];
                }

            }
            values[values.length - 1] = getSig(params, LoginManager.getInstance().getSecretKey());
            String BOUNDARY = "FlPm4LpSXsE"; // separate line
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < props.length; i++) { // send each property
                sb = sb.append("--");
                sb = sb.append(BOUNDARY);
                sb = sb.append("\r\n");
                sb = sb.append("Content-Disposition: form-data; name=\"" + props[i] + "\"\r\n\r\n");
                sb = sb.append(values[i]);
                sb = sb.append("\r\n");
            }
            sb = sb.append("--");
            sb = sb.append(BOUNDARY);
            sb = sb.append("\r\n");
            sb = sb.append("Content-Disposition: form-data;name=\"data\";filename=\"" + DateFormat.now2() + ".jpg\"\r\n");
            sb = sb.append("Content-Type: image/jpg\r\n\r\n");
            byte[] begin_data = sb.toString().getBytes("UTF-8");
            byte[] end_data = ("\r\n--" + BOUNDARY + "--\r\n").getBytes("UTF-8");
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            baos.write(begin_data);
            baos.write(imgData);
            baos.write(end_data);
            ret = baos.toByteArray();
            baos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }
	
	/**
	 * @author dingwei.chen
	 * 
	 * @说明 构建照片上传的Multipart数据
	 * 
	 * @param vid
	 *            string 语音组id
	 * @param seqid
	 *            int 切割语音的序列号
	 * @param mode
	 *            * string 传输模式segment|end，segment为分割语音模式，end为结束标识
	 * */
	protected byte[] buildUploadAudioData(long toId, String vid, int seqid, String mode, int playTime, byte[] voiceData) {

		byte[] ret = null;
		try {
			String[] props = { "api_key", "call_id", "client_info","format",  "mode", "playtime", "seqid",
                    "session_key", "toid", "v", "vid", "action", "sig" };
			String[] values = { MCS_API_KEY, String.valueOf(System.currentTimeMillis()),getClientInfo(), "json", mode, String.valueOf(playTime), String.valueOf(seqid),
					LoginManager.getInstance().getSsssionKey(),String.valueOf(toId), "1.0", String.valueOf(vid), "POST", "" };
			String[] params = new String[props.length - 1];
			for (int i = 0; i < params.length; i++) {
				if (values[i] != null && values[i].length() > 50) {
					params[i] = props[i] + "=" + values[i].substring(0, 50);
				} else {
					params[i] = props[i] + "=" + values[i];
				}

			}
			values[values.length - 1] = getSig(params, LoginManager.getInstance().getSecretKey());
			String BOUNDARY = "FlPm4LpSXsE"; // separate line
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < props.length; i++) { // send each property
				sb = sb.append("--");
				sb = sb.append(BOUNDARY);
				sb = sb.append("\r\n");
				sb = sb.append("Content-Disposition: form-data; name=\"" + props[i] + "\"\r\n\r\n");
				sb = sb.append(values[i]);
				sb = sb.append("\r\n");
			}
			sb = sb.append("--");
			sb = sb.append(BOUNDARY);
			sb = sb.append("\r\n");
			sb = sb.append("Content-Disposition: form-data;name=\"data\";filename=\"test.spx" + "\"\r\n");
			sb = sb.append("Content-Type: application/octet-stream\r\n\r\n");
			byte[] begin_data = sb.toString().getBytes("UTF-8");
			byte[] end_data = ("\r\n--" + BOUNDARY + "--\r\n").getBytes("UTF-8");
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			baos.write(begin_data);
			baos.write(voiceData);
			baos.write(end_data);
			ret = baos.toByteArray();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}
	
	public void batchRun(final INetRequest[] requests) {

		if (requests == null)
			throw new NullPointerException();
		if (requests.length == 0)
			return;

		JsonArray ja = new JsonArray();
		for (INetRequest request : requests) {
			if (request != null) {
				String param = request.getParamsString();
				ja.add(param);
			}
		}
		String param = ja.toJsonString();
		JsonObject bundle = this.obtainBaseRequestBundle(false);
		bundle.put("method_feed", param);
		bundle.put("v", "1.0");
		bundle.put("format", "json");

		bundle.put(INetRequest.gzip_key, INetRequest.gzip_value);

		INetResponse response4BatchRun = new INetResponse() {

			@Override
			public void response(INetRequest req, JsonValue jv) {

				// A failing response.
				if (jv instanceof JsonObject) {
					JsonObject ret = (JsonObject) jv;

					assert !Methods.checkNoError(req, ret);

					for (INetRequest request : requests) {
					    if (request == null)
					        continue;
						INetResponse response = request.getResponse();
						if (response == null)
							continue;
						response.response(request, ret);
					}

					// A successful response.
				} else if (jv instanceof JsonArray) {
					JsonArray ja = (JsonArray) jv;
					for (int i = 0; i < ja.size(); i++) {
						INetRequest request = requests[i];
						String method = request.getMethod();
						JsonValue val = ((JsonObject) ja.get(i)).getJsonValue(method);
						request.getResponse().response(request, val);
					}
				}
			}
		};
		this.sendRequest("/batch/run", bundle, response4BatchRun);
	}
	
	public byte[] buildUploadPhotoData(String aid, String type, String htf, String from, String statistic, String description, String placeData, byte[] imgData) {

		byte[] ret = null;
		try {
			String[] props = { "aid", "api_key", "call_id", "caption", "client_info", "statistic", "format", "htf", "method", "place_data", "session_key", "upload_type", "v", "from", "sig" };
			String[] values = { aid, MCS_API_KEY, String.valueOf(System.currentTimeMillis()), description, getClientInfo(), statistic, "json", htf, "photos.uploadbin", placeData, LoginManager.getInstance().getSsssionKey(), type, "1.0", from, "" };

			String[] params = new String[props.length - 1];
			for (int i = 0; i < params.length; i++) {
				if (values[i] != null && values[i].length() > 50) {
					params[i] = props[i] + "=" + values[i].substring(0, 50);
				} else {
					params[i] = props[i] + "=" + values[i];
				}

			}
			values[values.length - 1] = getSig(params, LoginManager.getInstance().getSecretKey());
			String BOUNDARY = "FlPm4LpSXsE"; // separate line
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < props.length; i++) { // send each property
				sb = sb.append("--");
				sb = sb.append(BOUNDARY);
				sb = sb.append("\r\n");
				sb = sb.append("Content-Disposition: form-data; name=\"" + props[i] + "\"\r\n\r\n");
				sb = sb.append(values[i]);
				sb = sb.append("\r\n");
			}
			sb = sb.append("--");
			sb = sb.append(BOUNDARY);
			sb = sb.append("\r\n");
			sb = sb.append("Content-Disposition: form-data;name=\"data\";filename=\"" + DateFormat.now2() + ".jpg\"\r\n");
			sb = sb.append("Content-Type: image/jpg\r\n\r\n");
			byte[] begin_data = sb.toString().getBytes("UTF-8");
			byte[] end_data = ("\r\n--" + BOUNDARY + "--\r\n").getBytes("UTF-8");
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			baos.write(begin_data);
			baos.write(imgData);
			baos.write(end_data);
			ret = baos.toByteArray();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}


    public abstract void uploadPhoto();
}
