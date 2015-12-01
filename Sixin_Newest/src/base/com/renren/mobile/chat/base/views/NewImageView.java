package com.renren.mobile.chat.base.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.os.Handler;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;

import com.common.manager.LoginManager;
import com.renren.mobile.chat.base.GlobalValue;
import com.renren.mobile.chat.base.util.FileUtil;
import com.renren.mobile.chat.base.util.ImagePool;
import com.renren.mobile.chat.base.util.ImagePool.OnDownloadImageListenner;
import com.renren.mobile.chat.base.views.NotSynImageView.DownloadImageAbleListener;

/**
 * @author dingwei.chen
 * */
public class NewImageView  extends ImageView implements OnDownloadImageListenner{
	
	private static Handler sHandler;
	private static ImagePool sImagePool;
	private String mImageURL = null;
	private String mSaveFilePath = null;
	private static final String pathDir = Environment.getExternalStorageDirectory() + "/sixin/" + LoginManager.getInstance().getLoginInfo().mUserId + "/NewsPhotos/";
	
	static{
		sImagePool = ImagePool.getInstance();
		if (!FileUtil.getInstance().isExistFile(pathDir)) {
			FileUtil.getInstance().createFile(pathDir);
		}
	}
	
	public NewImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		if(sHandler==null){
			sHandler = new Handler();
		}
	}
	
	public void setURL(final String url,final Bitmap defalutBitmap){
		String filePath = Environment.getExternalStorageDirectory() + "/sixin/" + LoginManager.getInstance().getLoginInfo().mUserId + "/NewsPhotos/" + url.hashCode();
		setURL(url,filePath,defalutBitmap,mListener!=null?mListener.enable():true);
	}
	
	@Override
	protected void onDraw(android.graphics.Canvas canvas) {
			Drawable d = this.getDrawable();
			Bitmap b = null;
			if(d!=null){
				BitmapDrawable bd = (BitmapDrawable)d;
				if(bd.getBitmap() == null || bd.getBitmap().isRecycled()){
					if(mSaveFilePath==null){
						return;
					}else{
						b = ImagePool.getInstance().getBitmapFromLocal(mSaveFilePath);
						if(b!=null&&!b.isRecycled()){
							this.setImageBitmap(b,mSaveFilePath);
						}else{
							return;
						}
					}
				}
			}
			super.onDraw(canvas);
	};
	
	
	private void setURL(String url,String localAbsFilePath,Bitmap defaultBitmap,boolean isRead){
		mImageURL = url;
		mSaveFilePath = localAbsFilePath;
		//图片过大是压缩图片，减少内存
//		BitmapFactory.Options newOpts = new BitmapFactory.Options();
//		newOpts.inJustDecodeBounds = true;
//		BitmapFactory.decodeFile(localAbsFilePath, newOpts);
//		if(newOpts.outWidth > IMAGE_MAX_WIDTH && newOpts.outHeight > IMAGE_MAX_WIDTH){
//			
//		}
//		if(!onDownloading){
			Bitmap bitmap = sImagePool.getBitmapFromLocal(localAbsFilePath,isRead);
			if(bitmap!=null){
				this.setImageBitmap(bitmap);
			}else{
				if(url!=null){
					downloadImage();
				}
			}
//		}
	}
	
	
	
	
	
	public void setImageBitmap(Bitmap bitmap,String path){
		this.setImageBitmap(bitmap);
		this.mSaveFilePath = path;
	}
	
	
	/**
	 * 通过url下载图片
	 * */
	private void downloadImage() {
//		onDownloading = true;
		ImagePool.DownloadImageRequest request = 
				new ImagePool.DownloadImageRequest(mImageURL,this,mSaveFilePath);
		sImagePool.downloadImage(request);
		
	}

	@Override
	public void onDownloadStart() {}

	@Override
	public void onDownloadSuccess(final String url,final Bitmap bitmap) {
		sHandler.post(new Runnable() {
			public void run() {
				if(mImageURL.equals(url)){
					setImageBitmap(bitmap);
//					onDownloading = false;
				}
			}
		});
	}

	@Override
	public void onDownloadError() {
//		onDownloading = false;
	}

	public void setSize(int number){
		number = GlobalValue.getInstance().calcFromDip(number);
		LayoutParams params = this.getLayoutParams();
		params.width = number;
		params.height = number ; 
		this.setLayoutParams(params);
	}
	DownloadImageAbleListener mListener;
	public void setDownloadAbleListener(DownloadImageAbleListener listener) {
		mListener = listener;
	}

	public boolean onDownloadEnable() {
		if (mListener != null) {
			return mListener.enable();
		}
		return true;
	}
	
	/**
	 * 主动调用图片回收，用于Feed列表~
	 */
	public void recycle(){
		if(!TextUtils.isEmpty(mSaveFilePath)){
			sImagePool.recycleSingleImage(mSaveFilePath);
		}
	}
	
}
