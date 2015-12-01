package com.renren.mobile.chat.cache;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import com.common.utils.Methods;

import android.graphics.Bitmap;
import android.widget.BaseAdapter;


/**
 * 头像的缓存类
 * **/
public class HeadImagePool {
	public static final int MAX_HEAD_PHOTO_NUM = 100;
	private HashMap<String, Bitmap> mHeadPool = new HashMap<String, Bitmap>();
	private HashSet<String> mRequestPhotoList = new HashSet<String>();
	public boolean mIsDownLoad = true;

	public boolean isSet = true;
	

	BaseAdapter mAdapter;
	private static HeadImagePool mImagePool = new HeadImagePool();

	public static HeadImagePool getInstance() {
		return mImagePool;
	}

	public static Bitmap getRoundedCornerBitmap(Bitmap bitmap) {
		return bitmap;
	}

	public synchronized boolean isRequestting(String url) {
		return mRequestPhotoList.contains(url);
	}

	public synchronized void addRequest(String url) {
		mRequestPhotoList.add(url);
	}

	public synchronized void removeRequest(String url) {
		mRequestPhotoList.remove(url);
	}

	public void putHeadImg(String url, Bitmap bitmap) {
		mHeadPool.put(url, bitmap);
	}

	/**
	 * 从缓存中读取图片
	 * */
	public Bitmap getImageFromCache(String url) {
		Bitmap img = mHeadPool.get(url);
		if (img != null && img.isRecycled()) {
			mHeadPool.remove(url);
			img = null;
		}
		// 内存中的缓存
		if (img != null) {
			return img;
		}
		img = getImageFromLocalCache(url);
		// 本地图片缓存
		if (img != null) {
			putImageToMemory(url, img);
			return img;
		}
		return null;
	}

	/**
	 * 将最近使用的图片放到内存缓存中
	 * */
	public synchronized void putImageToMemory(String key, Bitmap bitmap) {

		if (mHeadPool.size() > MAX_HEAD_PHOTO_NUM) {
			//Logd.error("mHeadpool clear");
			mHeadPool.clear();
			mRequestPhotoList.clear();
		}
		mHeadPool.put(key, bitmap);
	}

	/**
	 * 读取本地图片缓存，使用id请求的图片需要拼接后的url
	 */
	public static Bitmap getImageFromLocalCache(String url) {
		// String imageName = String.valueOf(url.hashCode());
		byte[] imgData = ImageUtils.getHeadImg(url);
		if (imgData != null) {
			final Bitmap img = Methods.createImage(imgData);
			if (img != null) {
				return img;
			}
		}
		return null;
	}

	public void clear() {
		for (Map.Entry<String, Bitmap> entry : mHeadPool.entrySet()) {
			if (entry.getValue() != null && !entry.getValue().isRecycled())
				entry.getValue().recycle();
		}
		mHeadPool.clear();
		mRequestPhotoList.clear();
	}

}
