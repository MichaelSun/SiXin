package com.renren.mobile.chat.dao;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import android.graphics.Bitmap;

import com.common.mcs.INetReponseAdapter;
import com.common.mcs.INetRequest;
import com.common.mcs.McsServiceProvider;
import com.common.utils.Methods;
import com.core.json.JsonObject;
import com.core.util.CommonUtil;
import com.renren.mobile.chat.base.util.ImageUtil;
import com.renren.mobile.chat.base.util.SystemUtil;
import com.renren.mobile.chat.base.views.NotSynImageView.CanDownloadImageAble;
import com.renren.mobile.chat.cache.ImageUtils;

public class ImageDAO {

	
	public static Map<String,List<CanDownloadImageAble>> REQUEST_POOL = new HashMap<String,List<CanDownloadImageAble>>();
	
	public synchronized Bitmap downloadBitmap(String url,CanDownloadImageAble listener){
		Bitmap bitmap = getImageFromLocalCache(url);
		if(bitmap!=null){
			listener.onImageRecive(url,bitmap);
			return bitmap;
		}
		if(REQUEST_POOL.containsKey(url)){
				List<CanDownloadImageAble> list = REQUEST_POOL.get(url);
				if(!list.contains(listener)){
					list.add(listener);
				}
				return null;
		}else{
				List<CanDownloadImageAble> list = new LinkedList<CanDownloadImageAble>();
				list.add(listener);
				REQUEST_POOL.put(url, list);
		}
		McsServiceProvider.getProvider().getImage(url, new ImageDownloadResponse(listener));
		return null;
	}
	public synchronized void notifyBitmapDownloadOver(String url,Bitmap bitmap){
		List<CanDownloadImageAble> list = REQUEST_POOL.get(url);
		for(CanDownloadImageAble m:list){
			m.onImageRecive(url, bitmap);//发生死锁位置
		}
		REQUEST_POOL.remove(url);
	}
	
	
	public class ImageDownloadResponse extends INetReponseAdapter {
		
		public CanDownloadImageAble mListener;
		public ImageDownloadResponse(CanDownloadImageAble listener){
			this.mListener = listener;
		}
		
		@Override
		public void onSuccess(INetRequest req, JsonObject data) {
			try {
				byte[] imgData = data.getBytes(IMG_DATA);
				if (imgData != null) {
					ImageUtils.saveHeadImg(imgData, mListener.getUrl());
					Bitmap img = ImageUtil.createImageByBytes(imgData);
					if(img!=null){
						notifyBitmapDownloadOver(mListener.getUrl(), img);
					}
				}
			} catch (Exception e) {}
		}
		@Override
		public void onError(INetRequest req, JsonObject data) {
			synchronized (REQUEST_POOL) {
				REQUEST_POOL.remove(mListener.getUrl());
			}
		}
	}
	
	
	
	
	/**
	 * 读取本地图片缓存，使用id请求的图片需要拼接后的url
	 */
	public static Bitmap getImageFromLocalCache(String url) {
		byte[] imgData = ImageUtils.getHeadImg(url);
		if (imgData != null) {
			final Bitmap img = Methods.createImage(imgData);
			imgData = null;
			if (img != null) {
				return img;
			}
		}
		return null;
	}
	
//	public static interface OnImageDownloadListener{
//		public void onImageRecive(String url,Bitmap bitmap);
//		
//	}
	
}
