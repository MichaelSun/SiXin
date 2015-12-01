package com.renren.mobile.chat.ui.photo;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore.Images.Media;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.common.manager.BindInfo;
import com.common.manager.LoginManager;
import com.common.mcs.INetProgressResponse;
import com.common.mcs.INetRequest;
import com.common.mcs.McsServiceProvider;
import com.core.json.JsonObject;
import com.core.json.JsonValue;
import com.core.util.DeviceUtil;
import com.renren.mobile.chat.R;
import com.renren.mobile.chat.RenrenChatApplication;
import com.renren.mobile.chat.base.util.SystemUtil;
import com.renren.mobile.chat.cache.ImageUtils;
import com.renren.mobile.chat.ui.BaseActivity;
import com.renren.mobile.chat.ui.account.BindRenrenAccountActivity;
import com.renren.mobile.chat.ui.groupchat.D_SelectGroupChatContactActivity;
import com.renren.mobile.chat.ui.imageviewer.ImageViewActivity;

/**
 * @author kaining.yang
 */
public class PhotoNew extends BaseActivity {
	
	public static final int mNotificationId = "sixin_android".hashCode();
	
	private NotificationManager mNotificationManager;
	
	private Notification mNotificationUploading;
	
	private Notification mNotificationSucceed;
	
	private Notification mNotificationFailed;
	
	private PhotoGalleryNew mPhotoGallery;

	private ProgressBar mLoadingPhoto;

	private boolean mIsShowInfo = false;

	private Boolean[] mIsDownLoadFailed;

	private String[] mImageLargeUrls;
	
	private int mImageNum;

	private ViewGroup mTitle;

	private PhotoNewAdapter mAdapter;

	protected static float screenWidth = 0.0f;

    protected static float screenHeight = 0.0f;

	private int curPosition = 0;

	public static boolean isImgOnScroll = false;

	private Button mBack;
	
	private Button mMore;
	
	private TextView mTitleText;
	
	private ListView popupMenu;

	private PopupWindow popupWindow;

	private int mPopupMenuWidth;

    private boolean isImgOnMove = false;
    
    private GestureDetector gestureScanner;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.ykn_photo_new);
		init();
		mTitle = (RelativeLayout) findViewById(R.id.title_bar);
		onConfigurationChanged(this.getResources().getConfiguration());
	}

	protected void onResume() {
		super.onResume();
        if(screenWidth == 0.0f || screenHeight == 0.0f) {
            onConfigurationChanged(this.getResources().getConfiguration());
        }
        resetAdapter(mAdapter);
	}

	public static void show(Context activity, String[] imageLargeUrls) {

		Intent intent = new Intent(activity, PhotoNew.class);
		Bundle extras = new Bundle();
		extras.putStringArray("image_large_urls", imageLargeUrls);
		intent.putExtras(extras);
		activity.startActivity(intent);
	}
	
	public static void show(Context activity, String[] imageLargeUrls, int imageNum) {

		Intent intent = new Intent(activity, PhotoNew.class);
		Bundle extras = new Bundle();
		extras.putStringArray("image_large_urls", imageLargeUrls);
		extras.putInt("image_num", imageNum);
		intent.putExtras(extras);
		activity.startActivity(intent);
	}

	protected void getExtras() {
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			mImageLargeUrls = extras.getStringArray("image_large_urls");
			mImageNum = extras.getInt("image_num");
		}
	}

	private void init() {
		getExtras();
		initLayout();
		initAdapter();
	}
	
	private void initAdapter() {
		setAdapter(mImageNum - 1);
	}
	
	private void setAdapter(int position) {

		if (mPhotoGallery == null || mImageLargeUrls == null || mImageLargeUrls.length == 0) {
			return;
		}
        mIsDownLoadFailed = new Boolean[mImageLargeUrls.length];
        for(int i = 0; i < mIsDownLoadFailed.length; i++) {
            mIsDownLoadFailed[i] = false;
        }
        if(mAdapter == null) {
            Display display = getWindowManager().getDefaultDisplay();

            mAdapter = new PhotoNewAdapter(PhotoNew.this, mImageLargeUrls, display, mIsDownLoadFailed);
            mPhotoGallery.setAdapter(mAdapter);
        }
        mPhotoGallery.setSelection(position);
	}
	
	private class MySimpleGesture extends GestureDetector.SimpleOnGestureListener {
        RenrenPhotoImageView imageView;

        // 按两下的第二下Touch down时触发
        @Override
        public boolean onDoubleTap(MotionEvent e) {

            View layout = mPhotoGallery.getSelectedView();
            View view = (ImageView) layout.findViewById(R.id.imageview_photo_new);
            if (view instanceof RenrenPhotoImageView) {
                imageView = (RenrenPhotoImageView) view;
                if (imageView.getScale() > imageView.getScaleRate()) {
                    imageView.zoomTo(imageView.getScaleRate(), e.getX(), e.getY(), 200f);
                    // imageView.layoutToCenter();
                } else {
                    imageView.zoomTo(imageView.getScaleRate() * 4, e.getX(), e.getY(), 200f);
                }

            } else {

            }
            // return super.onDoubleTap(e);
            return true;
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {

        	if (mIsShowInfo) {
				mIsShowInfo = false;
				mTitle.setVisibility(View.GONE);
			} else {
				mIsShowInfo = true;
				mTitle.setVisibility(View.VISIBLE);
			}
            return true;
        }

    }

	private void initLayout() {
		gestureScanner = new GestureDetector(new MySimpleGesture());
		mNotificationManager = (NotificationManager) getSystemService(this.NOTIFICATION_SERVICE);
		mLoadingPhoto = (ProgressBar) findViewById(R.id.progressbar_loading_photo);
		mTitleText = (TextView) findViewById(R.id.title_text);
		mBack = (Button) findViewById(R.id.title_left_button);
		mBack.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		mMore = (Button) findViewById(R.id.title_right_button);
		mMore.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				AlertDialog.Builder builder = new AlertDialog.Builder(PhotoNew.this);
				builder.setItems(R.array.browsing_choices, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						switch (which) {
							case 0:
								saveBitmapToLocal();
								break;
							case 1:
								confirmShareDialog();
								break;
							case 2:
								if(!IsSdCardFeasible()){
									SystemUtil.toast(RenrenChatApplication.getmContext().getResources().getString(R.string.ImageViewActivity_java_1));		//ImageViewActivity_java_1=SD卡不可用; 
									break;
								}
								if (mAdapter == null) {
									SystemUtil.toast(R.string.PhotoNew_java_5);
									return;
								}
								Bitmap displayPhoto = mAdapter.getCurrPhoto();
								if (displayPhoto == null) {
									SystemUtil.toast(R.string.PhotoNew_java_5);
									return;
								}
								String mFilePath = Environment.getExternalStorageDirectory() + "/sixin/" + LoginManager.getInstance().getLoginInfo().mUserId + "/NewsPhotos/" + System.currentTimeMillis();
								//displayPhoto = ImageUtil.image_Compression(mFilePath, RenrenChatApplication.screenResolution);
								ImageViewActivity.storeImageToFile(displayPhoto, mFilePath, false);
								/**MultiChatForwardActivity.show(ImageViewActivity.this, null, mFilePath);*/
								D_SelectGroupChatContactActivity.show(PhotoNew.this, mFilePath, true);
								break;
							case 3:
								break;
						}
					}
				});
				builder.show();
			}
		});

		mPhotoGallery = (PhotoGalleryNew) findViewById(R.id.photo_new_gallery);
		mPhotoGallery.setHorizontalFadingEdgeEnabled(false);
		mPhotoGallery.setCallbackDuringFling(false);
        mPhotoGallery.requestFocus();
		mPhotoGallery.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, final int position, long id) {

                final Handler handler = new Handler() {
                    public void handleMessage(Message ms) {
                        super.handleMessage(ms);
                        if(ms.what == 1) {
                            if (mImageLargeUrls != null && mImageLargeUrls.length > 0) {
                                String length = String.valueOf(mImageLargeUrls.length);
                                mTitleText.setText(String.valueOf(position + 1) + " / " + length);
                            }
                        }
                    }
                };

                new Thread(new Runnable() {
                    public void run() {
                        try{
                            try {
                                while (isImgOnMove) {

                                }
                                Thread.sleep(200);
                                handler.sendEmptyMessage(1);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        } catch (Exception e) {

                        }
                    }
                }).start();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});
		/*mPhotoGallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

				if (mIsShowInfo) {
					mIsShowInfo = false;
					mTitle.setVisibility(View.GONE);
				} else {
					mIsShowInfo = true;
					mTitle.setVisibility(View.VISIBLE);
				}

			}
		});*/
        mPhotoGallery.setOnTouchListener(new View.OnTouchListener() {
            float baseValue;
            float originalScale;
            RenrenPhotoImageView imageView;
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
            	gestureScanner.onTouchEvent(motionEvent);
                View layout = mPhotoGallery.getSelectedView();
                if(layout == null) {
                    return true;
                }
                View img = (ImageView) layout.findViewById(R.id.imageview_photo_new);
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if(img != null) {
                            baseValue = 0;
                            originalScale = ((RenrenPhotoImageView)img).getScale();
                        }
                        curPosition = getCurrPhotoPosition();
                        isImgOnScroll = true;
                        isImgOnMove = true;
                        return false;
                    case MotionEvent.ACTION_POINTER_DOWN:// 多点缩放
                        if(img != null && img instanceof RenrenPhotoImageView && mAdapter.getCurrPhoto() != null) {
                            baseValue = 0;
                            originalScale = ((RenrenPhotoImageView)img).getScale();
                            if (motionEvent.getPointerCount() == 2) {
                                float x = motionEvent.getX(0) - motionEvent.getX(1);
                                float y = motionEvent.getY(0) - motionEvent.getY(1);
                                float value = (float) Math.sqrt(x * x + y * y);// 计算两点的距离
                                if (baseValue == 0) {
                                    baseValue = value;
                                }
                                return true;
                            }
                        }
                        isImgOnMove = true;
                    case MotionEvent.ACTION_MOVE:
                        if (img != null && motionEvent.getPointerCount() == 2 && img instanceof RenrenPhotoImageView
                                && mAdapter.getCurrPhoto() != null) {
                            float x = motionEvent.getX(0) - motionEvent.getX(1);
                            float y = motionEvent.getY(0) - motionEvent.getY(1);
                            float value = (float) Math.sqrt(x * x + y * y);// 计算两点的距离
                            if (baseValue == 0) {
                                baseValue = value;
                            } else {
                                float scale = value / baseValue;// 当前两点间的距离除以手指落下时两点间的距离就是需要缩放的比例。
                                ((RenrenPhotoImageView)img).zoomTo(originalScale * scale, x + motionEvent.getX(1), y + motionEvent.getY(1));
                            }
                            return true;
                        }

                    case MotionEvent.ACTION_UP:
                        isImgOnMove = false;
                    case MotionEvent.ACTION_POINTER_UP:
                        isImgOnMove = false;
                }
                return false;
            }
        });

	}
	
	private int getCurrPhotoPosition() {
		if (mPhotoGallery != null) {
			return mPhotoGallery.getSelectedItemPosition();
		}
		return 0;
	}

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
        }
        DisplayMetrics metric = new DisplayMetrics();

        getWindowManager().getDefaultDisplay().getMetrics(metric);
        screenWidth = metric.widthPixels;
        screenHeight = metric.heightPixels;
        resetAllValues();
    }



	@Override
	public void onLowMemory() {
		super.onLowMemory();
	}

	@Override
	protected void onDestroy() {
        screenHeight = 0.0f;
        screenWidth = 0.0f;
        mAdapter.recycle();
		super.onDestroy();

	}

	private void resetAdapter(BaseAdapter mAdapter) {

		if (mAdapter != null) {
			resetAllValues();
			mAdapter.notifyDataSetChanged();
		}
	}

	private void resetAllValues() {
		curPosition = 0;
		isImgOnScroll = false;
        isImgOnMove = false;
	}
	
	/*
	 * 保存到本地的方法
	 */
	private void photo_save(){
		try {
			if (mAdapter == null) {
				SystemUtil.toast(R.string.PhotoNew_java_5);
				return;
			}
			Bitmap displayPhoto = mAdapter.getCurrPhoto();
			if (displayPhoto == null) {
				SystemUtil.toast(R.string.PhotoNew_java_5);
				return;
			}
			ContentValues values = new ContentValues();
			String sdFilePath = Environment.getExternalStorageDirectory() + "/Renren/Image/";
			File filePath = new File(sdFilePath);
			if (!filePath.exists()) {
				filePath.mkdir();
			}
			File tmpFile = new File(filePath, "renren_" + String.valueOf(System.currentTimeMillis()) + ".png");
			if (tmpFile != null) {
				FileOutputStream fo = new FileOutputStream(tmpFile);

				displayPhoto.compress(Bitmap.CompressFormat.PNG, 100, fo);
				fo.close();
				values.put(Media.DATA, tmpFile.getPath());
				values.put(Media.DESCRIPTION, "Image from renren_android");
				values.put(Media.MIME_TYPE, "image/jpeg");
				getContentResolver().insert(Media.EXTERNAL_CONTENT_URI, values);
			}
			SystemUtil.toast(R.string.PhotoNew_java_6);
		} catch (Exception e) {
			SystemUtil.toast(R.string.PhotoNew_java_7);
			e.printStackTrace();
		}
	}

	/**
	 *  when the users browsing pictures, they could press the
	 *  save button to save picture to local
	 */
	public void saveBitmapToLocal() {
		String fileName = null;
		if (DeviceUtil.getInstance().isMountSDCard()) {
			String sdFilePath = Environment.getExternalStorageDirectory()
					+ ImageUtils.SIXIN_SAVE_IMAGE_TO_LOCAL;
			fileName = sdFilePath + getFileName() + ".jpg";
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
				}
			}
			if (DeviceUtil.getInstance().isSDCardHasEnoughSpace()) {
				saveBitmapToLocal(mAdapter.mLargeBitmap, fileName);
			}else{
				SystemUtil.toast(RenrenChatApplication.getmContext().getResources().getString(R.string.ImageViewActivity_java_12));		//ImageViewActivity_java_12=存储卡已满，无法保存图片。; 
			}
		} else {
			Toast.makeText(this, RenrenChatApplication.getmContext().getResources().getString(R.string.ImageViewActivity_java_13), 1).show();		//ImageViewActivity_java_13=存储卡被移除，无法保存图片。; 
		}
	}
	
	/**
	 * achieve hash code value to name the picture
	 */
	public String getFileName() {
		if (mAdapter.mFileUrl != null) {
			return String.valueOf(mAdapter.mFileUrl.hashCode());
		} else
			return String.valueOf(mAdapter.mFileUrl.hashCode());
	}
	/**
	 * saveBitmap to SD card
	 */
	private void saveBitmapToLocal(Bitmap bitmap, String path) {
		if (bitmap == null) {
			return;
		}
		File file = new File(path);
		FileOutputStream accessFile = null;
		try {
			accessFile = new FileOutputStream(file);
			bitmap.compress(Bitmap.CompressFormat.JPEG, 50, accessFile);
			accessFile.flush();
		} catch (Exception e) {
			SystemUtil.toast(RenrenChatApplication.getmContext().getResources().getString(R.string.ImageViewActivity_java_14));		//ImageViewActivity_java_14=创建文件失败; 
			return ;
		}
		try {
			accessFile.close();
		} catch (IOException e) {}
		try {
			ContentValues values = new ContentValues();
			values.put(Media.DATA, path);
			values.put(Media.DESCRIPTION, "Image from renren_android");
			values.put(Media.MIME_TYPE, "image/jpeg");
			this.getContentResolver().insert(Media.EXTERNAL_CONTENT_URI, values);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Toast.makeText(this, RenrenChatApplication.getmContext().getResources().getString(R.string.ImageViewActivity_java_15) + path, 1).show();		//ImageViewActivity_java_15=图片已保存至; 
	}
	
	private void confirmShareDialog() {
    	AlertDialog.Builder builder = new AlertDialog.Builder(PhotoNew.this);
    	builder.setTitle(RenrenChatApplication.getmContext().getResources().getString(R.string.ImageViewActivity_java_2));		    //ImageViewActivity_java_2=分享到人人网; 
    	builder.setMessage(RenrenChatApplication.getmContext().getResources().getString(R.string.ImageViewActivity_java_3));		//ImageViewActivity_java_3=上传图片到人人网手机相册; 
    	builder.setPositiveButton(RenrenChatApplication.getmContext().getResources().getString(R.string.MultiChatForwardScreen_java_6), new DialogInterface.OnClickListener() {		//MultiChatForwardScreen_java_6=确认; 
			@Override
			public void onClick(DialogInterface dialog, int which) {
				ByteArrayOutputStream os = new ByteArrayOutputStream();
				Bitmap bitmap;
				bitmap = mAdapter.mLargeBitmap;
				if (bitmap != null) {
					BindInfo bindInfoRenren = LoginManager.getInstance().getLoginInfo().mBindInfoRenren;
					if(bindInfoRenren == null ||(bindInfoRenren != null && TextUtils.isEmpty(bindInfoRenren.mBindId))){
						Intent intent = new Intent(PhotoNew.this,BindRenrenAccountActivity.class);
						intent.putExtra(BindRenrenAccountActivity.COME_FROM, BindRenrenAccountActivity.BIND_PHOTO);
						PhotoNew.this.startActivity(intent);
						SystemUtil.toast(RenrenChatApplication.getmContext().getResources().getString(R.string.ImageViewActivity_java_20));
					}else{
						notifyUploading();
						bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
						McsServiceProvider.getProvider().postPhoto(os.toByteArray(), 0, 0, 0, "", "", "", "", response);
					}
				} else {
					Toast.makeText(PhotoNew.this, RenrenChatApplication.getmContext().getResources().getString(R.string.ImageViewActivity_java_4), Toast.LENGTH_SHORT);		//ImageViewActivity_java_4=分享失败; 
				}
			}
		});
		builder.setNegativeButton(RenrenChatApplication.getmContext().getResources().getString(R.string.ChatMessageWarpper_FlashEmotion_java_4), new DialogInterface.OnClickListener() {		//ChatMessageWarpper_FlashEmotion_java_4=取消; 
			@Override
			public void onClick(DialogInterface dialog, int which) {
			}
		});
		builder.show();
    }
	
	/**
     * sdcard是否可读写   
     * @return boolean
     * @author xiangchao.fan
     */
    public boolean IsSdCardFeasible() {  
        try {  
            return Environment.getExternalStorageState().equals(  
                    Environment.MEDIA_MOUNTED);  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        return false;  
    } 
	
	private INetProgressResponse response = new INetProgressResponse() {
		
		@Override
		public void response(INetRequest req, JsonValue obj) {
			if (obj instanceof JsonObject) {
				JsonObject map = (JsonObject) obj;
				
				int error_code = (int) map.getNum("error_code");
				String error_msg = map.getString("error_msg");
				
				SystemUtil.logykn("share renren error_code:" + error_code);
				SystemUtil.logykn("share renren error_message:" + error_msg);
				if (error_code == 0) {
					mNotificationManager.cancel(mNotificationId);
					mNotificationSucceed = new Notification(R.drawable.notification_succeed, RenrenChatApplication.getmContext().getResources().getString(R.string.ImageViewActivity_java_8), System.currentTimeMillis());		//ImageViewActivity_java_8=上传成功; 
					Intent intent = new Intent();
					mNotificationSucceed.setLatestEventInfo(PhotoNew.this, RenrenChatApplication.getmContext().getResources().getString(R.string.app_name), RenrenChatApplication.getmContext().getResources().getString(R.string.ImageViewActivity_java_9), PendingIntent.getActivity(PhotoNew.this, 0, intent, 0));		//app_name=私信; //ImageViewActivity_java_9=图片已上传到人人网; 
					mNotificationSucceed.flags = Notification.FLAG_AUTO_CANCEL;
			    	mNotificationManager.notify(mNotificationId - 1, mNotificationSucceed);
				} else {
					mNotificationManager.cancel(mNotificationId);
					mNotificationFailed = new Notification(R.drawable.notification_fail, RenrenChatApplication.getmContext().getResources().getString(R.string.ImageViewActivity_java_10), System.currentTimeMillis());		//ImageViewActivity_java_10=上传失败; 
					Intent intent = new Intent();
					mNotificationFailed.setLatestEventInfo(PhotoNew.this, RenrenChatApplication.getmContext().getResources().getString(R.string.app_name), RenrenChatApplication.getmContext().getResources().getString(R.string.ImageViewActivity_java_11), PendingIntent.getActivity(PhotoNew.this, 0, intent, 0));		//app_name=私信; //ImageViewActivity_java_11=图片上传失败; 
					mNotificationFailed.flags = Notification.FLAG_AUTO_CANCEL;
			    	mNotificationManager.notify(mNotificationId - 2, mNotificationFailed);
				}
			}
		}
		
		@Override
		public void uploadContentLength(
				long bytes) {
		}
		
		@Override
		public void upload(int bytes) {
		}
		
		@Override
		public void downloadContentLength(
				long bytes) {
		}
		
		@Override
		public void download(int bytes) {
		}
	};
	
	private void notifyUploading() {
		mNotificationUploading = new Notification(R.drawable.notification_upload, RenrenChatApplication.getmContext().getResources().getString(R.string.ImageViewActivity_java_6), System.currentTimeMillis());		//ImageViewActivity_java_6=正在上传照片...; 
		Intent intent = new Intent();
		mNotificationUploading.setLatestEventInfo(PhotoNew.this, RenrenChatApplication.getmContext().getResources().getString(R.string.app_name), RenrenChatApplication.getmContext().getResources().getString(R.string.ImageViewActivity_java_7), PendingIntent.getActivity(PhotoNew.this, 0, intent, 0));		//app_name=私信; //ImageViewActivity_java_7=照片上传中...; 
    	mNotificationManager.notify(mNotificationId, mNotificationUploading);
	}
    
    /**
	 * 获取照片列表／单张照片信息
	 * 
	 * @param aid
	 *            相册id，(照片id和相册id至少指定一个） 只指定aid时返回相册中的照片列表，page和count参数有效
	 * @param pid
	 *            照片id， (照片id和相册id至少指定一个） 指定pid后返回指定相片的详细信息
	 * @param uid
	 *            相册的所有者的用户id
	 * @param page
	 *            支持分页，指定页号，页号从1开始。缺省返回第一页数据
	 * @param count
	 *            支持分页，指定每页记录数，缺省为每页10条记录。
	 * @param all
	 *            为1时表示取所有的照片信息
	 * @param password
	 *            相册的访问密码。
	 * @param response
	 * @author kuangxiaoyue
	 */
	/*public static INetRequest getPhotos3G(long aid, long pid, long uid, int page, int count, int all, String password, INetResponse response,boolean batchRun) {

		JsonObject sm = m_buildRequestBundle(false);
		sm.put("v", "1.0");
		sm.put("uid", uid);
		if (aid != 0) {
			sm.put("aid", aid);
		}
		if (pid != 0) {
			sm.put("pid", pid);
		}
		if (page != 0) {
			sm.put("page", page);
		}
		if (count != 0) {
			sm.put("page_size", count);
		}
		if (all != 0) {
			sm.put("all", all);
		}
		sm.put("with_lbs", 1);
		sm.put(INetRequest.gzip_key, INetRequest.gzip_value);
		if (password != null && !password.equals("")) {
			sm.put("password", password);
		}
		 String url;
         if (!batchRun) {
                 sm.put(INetRequest.gzip_key, INetRequest.gzip_value);
                 url = m_test_apiUrl + "/photos/get";
         } else {
                 sm.put("method", "photos.get");
                 url = m_test_apiUrl;
         }
		// Log.d("kxy", "---> photos.get request: " + sm.toString());
		//m_sendRequest(m_test_apiUrl + "/photos/get", sm, response);
         INetRequest request = m_buildRequest(url, sm, response);
         if (batchRun) {
                 return request;
         } else {
                 m_sendRequest(request);
                 return null;
         }
	}*/
	
	/**
	 * 构造一个网絡请求的基本数据bundle, 填入api_key, call_id, session_key.
	 * 
	 * @param batchRun
	 *            是否使用批调用. 使用批调用的请求不包含session_key, session_key统一由batch.run方法提供.
	 */
	/*private static JsonObject m_buildRequestBundle(boolean batchRun) {

		JsonObject bundle = new JsonObject();

		// 使用 3G 服务器的 api_key
		bundle.put("api_key", m_apiKey);
		bundle.put("call_id", System.currentTimeMillis());
		// 使用批调用的请求不包含session_key, session_key统一由batch.run方法提供.
		if (!batchRun) {
            if (!TextUtils.isEmpty(m_sessionKey)) {

                bundle.put("session_key", m_sessionKey);
            }
        }
		bundle.put("client_info", getClientInfo());
		return bundle;
	}*/
}
