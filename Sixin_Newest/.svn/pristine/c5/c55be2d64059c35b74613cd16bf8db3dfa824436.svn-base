package com.renren.mobile.chat.cache;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import android.os.Environment;
import android.text.TextUtils;

import com.common.utils.Methods;
import com.renren.mobile.chat.RenrenChatApplication;

public class ImageUtils {
	public static final String SIXIN_HEAD_IMG_CACHE_DIR = "/sixin/ImageCache/";
	public static final String SIXIN_IMAGE = "/sixin/Photo/";
	public static final String SIXIN_SAVE_IMAGE_TO_LOCAL = "/sixin_img/";
	
	/**
	 * 将头像保存到本地
	 * */
	public static boolean saveHeadImg(byte[] data, String url){
		String urlHash = String.valueOf(url.hashCode());
		if (RenrenChatApplication.getmContext() == null) {
			return false;
		}
		String fileName = null;
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			String sdFilePath = Environment.getExternalStorageDirectory() + SIXIN_HEAD_IMG_CACHE_DIR;
			fileName = sdFilePath + urlHash;
			File filePath = new File(sdFilePath);
			if (!filePath.exists()) {
				filePath.mkdirs();
			}
			File file = new File(fileName);
			if (!file.exists()) {
				try {
					file.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
					return false;
				}
			}
			return Methods.toFile(file, data);
			
		} else {
			fileName = RenrenChatApplication.getmContext().getFilesDir().getAbsolutePath() + "/" + urlHash;
			return false;
		}
	}
	
	
	/**
	 * 从本地缓存中读取图片
	 * */
	public static byte[] getHeadImg(String url){
		if(TextUtils.isEmpty(url)){
			return null;
		}
		String urlHash = String.valueOf(url.hashCode());
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			String sdFilePath = Environment.getExternalStorageDirectory() + SIXIN_HEAD_IMG_CACHE_DIR;
			String fileName = sdFilePath + urlHash;
			//Logd.error("fileName="+fileName);
			FileInputStream fio = null;
			try {
				File filePath = new File(fileName);
				if(filePath.exists()){
					fio = new FileInputStream(filePath);
					byte[] imgData = Methods.toByteArray(fio);
					return imgData;
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if(null != fio) {
					Methods.closeQuietly(fio);
				}
			}
		}
		return null;
	}

}
