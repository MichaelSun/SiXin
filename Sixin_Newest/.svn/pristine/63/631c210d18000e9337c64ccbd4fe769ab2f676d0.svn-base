package com.renren.mobile.chat.ui.photo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashSet;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.os.Handler;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.common.manager.LoginManager;
import com.common.mcs.INetRequest;
import com.common.mcs.INetResponse;
import com.common.mcs.McsServiceProvider;
import com.core.json.JsonObject;
import com.core.json.JsonValue;
import com.core.util.DeviceUtil;
import com.renren.mobile.chat.R;
import com.renren.mobile.chat.RenrenChatApplication;
import com.renren.mobile.chat.base.util.FileUtil;
import com.renren.mobile.chat.base.util.ImageUtil;
import com.renren.mobile.chat.base.util.SystemUtil;
import com.renren.mobile.chat.common.ResponseError;
import com.renren.mobile.chat.ui.imageviewer.ImageLoader;

/**
 * Created on December 23,2010
 * 
 * @author kuangxiaoyue
 * @version 1.0
 */
public class PhotoNewAdapter extends BaseAdapter {

	public static final int IMAGE_SMALL = 320 * 480;

	public static final int IMAGE_LARGE = 480 * 800;
	
	public HashSet<String> mRequestPhotoList = new HashSet<String>();

	private Context mContext;
	
	private String[] mImageHeadUrls;

	private String[] mImageLargeUrls;
	
	private Boolean[] mIsDownLoadFailed;

	private LayoutInflater mInflater;

	private Display mDisplay;

	private Handler mHandler;

	private View mAppearView;

	private Bitmap mCurrPhoto;

	private RenrenPhotoImageView view;
	
	private ProgressBar mLoading;
	
	public Bitmap mLargeBitmap;
	
	public String mFileUrl;
	
	private String mFilePath;

	public PhotoNewAdapter(Context context, String[] imageHeadUrls, String[] imageLargeUrls, Display display, Boolean[] mIsDownLoadFailed) {
		mContext = context;
		mDisplay = display;
		mImageHeadUrls = imageHeadUrls;
		mImageLargeUrls = imageLargeUrls;
		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mHandler = new Handler();
		this.mIsDownLoadFailed = mIsDownLoadFailed;
	}
	
	public PhotoNewAdapter(Context context, String[] imageLargeUrls, Display display, Boolean[] mIsDownLoadFailed) {
		mContext = context;
		mDisplay = display;
		mImageLargeUrls = imageLargeUrls;
		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mHandler = new Handler();
		this.mIsDownLoadFailed = mIsDownLoadFailed;
	}

	@Override
	public int getCount() {
		return mImageLargeUrls.length;
	}

	@Override
	public Object getItem(int position) {
		return mImageLargeUrls[position];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public View getAppearView() {
		return mAppearView;
	}

	public Bitmap getCurrPhoto() {
		return mCurrPhoto;
	}

	public RenrenPhotoImageView getView() {
		return view;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = createPhotoView(position);
			mAppearView = convertView;
			return convertView;
		} else {
			FrameLayout view = (FrameLayout) convertView;
			return view;
		}
	}

	private View createPhotoView(int position) {
		if (mImageLargeUrls == null || mImageLargeUrls.length == 0) {
			return null;
		}

		FrameLayout layout = (FrameLayout) mInflater.inflate(R.layout.v5_0_1_photo_new_adapter, null);
		view = (RenrenPhotoImageView) layout.findViewById(R.id.imageview_photo_new);
		view.setMinimumWidth(mDisplay.getWidth());
		view.setMinimumHeight(mDisplay.getHeight());
		mLoading = (ProgressBar) layout.findViewById(R.id.progressbar_loading_photo_adapter);

		mFileUrl = mImageLargeUrls[position]; //ServiceProvider.createImageUrlByUrl(mImageLargeUrls[position], getDownloadPhotoType());
		
		// kaining.yang
		
		mFilePath = Environment.getExternalStorageDirectory() + "/sixin/" + LoginManager.getInstance().getLoginInfo().mUserId + "/NewsPhotos/" + mFileUrl.hashCode();
		String pathDir = Environment.getExternalStorageDirectory() + "/sixin/" + LoginManager.getInstance().getLoginInfo().mUserId + "/NewsPhotos/";
		if (!FileUtil.getInstance().isExistFile(pathDir)) {
			FileUtil.getInstance().createFile(pathDir);
		}
		File file = new File(mFilePath);
		if (file.isFile()) {
		} else if (file.isDirectory()) {
		}
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (file.isFile()) {
		} else if (file.isDirectory()) {
		}
		isLoadingImage(true);
		downloadBitmap(mFilePath, mFileUrl);
		
		/*if (largeImg != null) {
			mCurrPhoto = largeImg;
            view.setImageHeight(largeImg.getHeight());
            view.setImageWidth(largeImg.getWidth());
			view.setImageBitmap(largeImg);
			if (!PhotoNew.isImgOnScroll) {
				notifyDataSetChanged();
			}
			preLoadPhoto(position - 1);
			nextLoadPhoto(position + 1);
		} else {
			mCurrPhoto = null;
			loading.setVisibility(View.VISIBLE);
			Bitmap headImg = ImagePool.loadImageCache(mImageHeadUrls[position]);
			if (headImg != null) {
                view.setImageHeight(headImg.getHeight());
                view.setImageWidth(headImg.getWidth());
				view.setImageBitmap(headImg);
			}
            loadPhotoImage(view, loading, position);
			preLoadPhoto(position - 1);
			nextLoadPhoto(position + 1);
		}*/
		return layout;
	}
	
	private void initPhotoView() {
		mCurrPhoto = mLargeBitmap;
        view.setImageHeight(mLargeBitmap.getHeight());
        view.setImageWidth(mLargeBitmap.getWidth());
		view.setImageBitmap(mLargeBitmap);
		if (!PhotoNew.isImgOnScroll) {
			notifyDataSetChanged();
		}
	}
	
	private void isLoadingImage(boolean loading) {
		if (loading) {
			mLoading.setVisibility(View.VISIBLE);
		} else {
			mLoading.setVisibility(View.GONE);
		}
	}
	
	/**
	 * if the image we browsing is not downloaded, we will download it right now
	 * @param filepath
	 * @param fileurl
	 */
	private void downloadBitmap(String filepath, String fileurl) {
		if (filepath != null) {
			File file = new File(filepath);
			if (file.isDirectory()) {
				return;
			}
			if (!file.exists() || file.length() == 0) {
				if (TextUtils.isEmpty(fileurl)) {
					SystemUtil.toast(RenrenChatApplication.getmContext().getResources().getString(R.string.ImageViewActivity_java_16));		//ImageViewActivity_java_16=图片预览错误; 
					return;
				}
				downloadImage(fileurl, filepath);
			} else {
				mLargeBitmap = ImageLoader.getBitmapFromFile(filepath, null);
				/*mBitmap = ImageUtil.image_Compression(filepath,
						RenrenChatApplication.screenResolution);*/
				isLoadingImage(false);
				
				if (null == mLargeBitmap) {
					SystemUtil.toast(RenrenChatApplication.getmContext().getResources().getString(R.string.ImageViewActivity_java_17));		//ImageViewActivity_java_17=图片无法显示; 
					return;
				} else {
					initPhotoView();
				}
			}
		}
	}
	/**
	 * sending download request
	 * @param url
	 * @param savePath
	 */
	protected void downloadImage(String url, final String savePath) {
		INetResponse response = new INetResponse() {
			public void response(final INetRequest req, final JsonValue result) {
				if (result instanceof JsonObject) {
					final JsonObject obj = (JsonObject) result;
					RenrenChatApplication.mHandler.post(new Runnable() {
						@Override
						public void run() {
							isLoadingImage(false);
							if (ResponseError.noError(req, obj, false)) {
								byte[] imgData = obj.getBytes(IMG_DATA);
								if (imgData != null) {
									/*mBitmap = ImageUtil
											.image_Compression(
													imgData,
													RenrenChatApplication.screenResolution);*/
									mLargeBitmap = ImageUtil.createImageByBytes(imgData);
								}
								if (DeviceUtil.getInstance().isMountSDCard()) {
									if (DeviceUtil.getInstance().isSDCardHasEnoughSpace() && null != mLargeBitmap) {
										saveBitmap(mLargeBitmap, savePath);
									} else {
									}
								} else {
								}
							} 
							if (null == mLargeBitmap) {
							} else {
								initPhotoView();
							}
						}
					});
				}
			}
		};
		McsServiceProvider.getProvider().getImage(url, response);
	}
	
	/**
	 * when the picture we intend to browsing is downloaded 
	 * auto save it to local sd card
	 */
	private int saveBitmap(Bitmap bitmap, String path) {
		if(bitmap==null){
			return -1;
		}
		File file = new File(path);
		FileOutputStream accessFile = null;
		try {
			accessFile = new FileOutputStream(file);
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, accessFile);
			accessFile.flush();
		} catch (Exception e) {
			SystemUtil.toast(RenrenChatApplication.getmContext().getResources().getString(R.string.ImageViewActivity_java_18));		//ImageViewActivity_java_18=保存失败; 
			return -1;
		}
		try {
			accessFile.close();
		} catch (IOException e) {
		}
		return 0;
	}

	private int getDownloadPhotoType() {
		
		if (RenrenChatApplication.screenResolution < IMAGE_SMALL) {
			return 5;
		} else if (RenrenChatApplication.screenResolution < IMAGE_LARGE) {
			return 6;
		} else {
			return 7;
		}
	}
	
	public void recycle(){
		if(mLargeBitmap != null){
			mLargeBitmap.recycle();
			mLargeBitmap = null;
		}
	}
}
