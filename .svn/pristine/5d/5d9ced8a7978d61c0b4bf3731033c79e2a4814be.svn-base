package com.common.emotion.manager;

import com.common.mcs.INetReponseAdapter;
import com.common.mcs.INetRequest;
import com.core.json.JsonObject;

/**
 * 下载表情包控制类
 * @author jiaxia
 *
 */
public class EmotionPackageDownload {

	private static EmotionPackageDownload epDownload = new EmotionPackageDownload();
	
	private EmotionPackageDownload(){
		
	}
	
	public static EmotionPackageDownload getInstance(){
		return epDownload;
	}
	/**
	 * 
	 * @param url   下载整包的URL地址
	 * @param listener   下载所用到的监听器
	 */
	public void downloadEmotionPackage(String url,DownloadEmotionPackageListener listener){
		
	}
	/**
	 * 
	 * @author jiaxia 下载表情包的监听器接口
	 */
	public static interface DownloadEmotionPackageListener {

		
	}

	
	
	
	/**
	 * 
	 * @author jiaxia 下载表情包的Response
	 */
	class DownloadEmotionPackageResponse extends INetReponseAdapter {

		DownloadEmotionPackageListener mEmotionPackageListener = null;

		public DownloadEmotionPackageResponse(
				DownloadEmotionPackageListener listener) {
			mEmotionPackageListener = listener;
		}

		@Override
		public void onSuccess(INetRequest req, JsonObject data) {

		}

		@Override
		public void onError(INetRequest req, JsonObject data) {

		}

	}
}
