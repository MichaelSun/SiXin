package com.renren.mobile.chat.ui.imageviewer;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Media;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.common.manager.BindInfo;
import com.common.manager.LoginManager;
import com.common.manager.PhotoUploadManager;
import com.common.mcs.INetProgressResponse;
import com.common.mcs.INetRequest;
import com.common.mcs.INetResponse;
import com.common.mcs.McsServiceProvider;
import com.common.utils.Methods;
import com.core.json.JsonObject;
import com.core.json.JsonValue;
import com.core.util.DeviceUtil;
import com.renren.mobile.chat.R;
import com.renren.mobile.chat.RenrenChatApplication;
import com.renren.mobile.chat.base.util.FileUtil;
import com.renren.mobile.chat.base.util.ImageUtil;
import com.renren.mobile.chat.base.util.SystemUtil;
import com.renren.mobile.chat.cache.ImageUtils;
import com.renren.mobile.chat.common.ResponseError;
import com.renren.mobile.chat.ui.BaseActivity;
import com.renren.mobile.chat.ui.account.BindRenrenAccountActivity;
import com.renren.mobile.chat.ui.groupchat.D_SelectGroupChatContactActivity;
import com.renren.mobile.chat.ui.setting.PhotoUploadActivity;
import com.renren.mobile.chat.ui.setting.SettingDataManager;
public class ImageViewActivity extends BaseActivity implements IModeSwitchable {
    public static final int TOKEN = ImageViewActivity.class.hashCode();
    public static final int mNotificationId = "sixin_android".hashCode();
	public static final int TAKE_PHOTO = 0;
	public static final int SELECT_PHOTO = 1;
	public static final int VIEW_LARGE_IMAGE = 2;
	public static final int VIEW_LARGE_HEAD = 3;
	public static final int VIEW_LOCAL_TO_OUT_LARGE_IMAGE = 4;
	public static final int VIEW_OUT_TO_LOCAL_LARGE_IMAGE = 5;
	private NotificationManager mNotificationManager;
	private Notification mNotificationUploading;
	private Notification mNotificationSucceed;
	private Notification mNotificationFailed;
	private int mRequestCode;
	private Button titleLeftButton;
	private Button titleRightButton;
	private View mTitleBar;
	private View mOperationBar;
	private View mDefaultImageLoading;
    private int orientation = 0;
    private int mOrientaion = 0;
    private File mTmpFile;
    private Bitmap mBitmap;
    private Bitmap mBitmapSend = null;
    private int mBitmapRotateCount = 0;
    private String mFilePath;
    private String mFileUrl;
    private String mPluginId;
    private Uri mFileUri;
    private int mMode;
    private static final int MODE_FULL_SCREEN = 0;
    private static final int MODE_SHOW_OPERATION_BAR = 1;
    public static interface NEED_PARAM {
		String REQUEST_CODE = "REQUEST_CODE_INT"; 
		String SMALL_LOCAL_URI = "SMALL_LOCAL_URI";
		String LARGE_LOCAL_URI = "LARGE_LOCAL_URI";
		String SMALL_PORTRAIT_URL = "SMALL_PORTRAIT";
		String LARGE_PORTRAIT_URL = "LARGE_PORTRAIT";
		
		String DEFAULT_SHOW = "default_show";
	}
	
	/** TODO true情况下显示上传头像的选项*/ 
	private boolean defaultShow = false;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFormat(PixelFormat.RGBA_8888);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.ykn_view_image);
        RenrenChatApplication.pushStack(ImageViewActivity.this);
        
        defaultShow = getIntent().getBooleanExtra(NEED_PARAM.DEFAULT_SHOW, false);
        
        mBitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.default_image_loading);
        mBitmapSend = BitmapFactory.decodeResource(this.getResources(), R.drawable.default_image_loading);
        mTitleBar = findViewById(R.id.title_bar);
        mOperationBar = findViewById(R.id.operation_bar);
        mDefaultImageLoading = findViewById(R.id.default_image_loading);
        titleLeftButton = (Button) findViewById(R.id.title_left_button);
        titleRightButton = (Button) findViewById(R.id.title_right_button);
        mNotificationManager = (NotificationManager) getSystemService(this.NOTIFICATION_SERVICE);
        initImageViewCase();
        setMode(MODE_FULL_SCREEN);
        setupFullImageGallery();
        setupTitle();
        setupClickEvents();
    }
    private void isLoadingImage(boolean isLoading) {
    	if (isLoading) {
    		mDefaultImageLoading.setVisibility(View.VISIBLE);
    	} else {
    		mDefaultImageLoading.setVisibility(View.GONE);
    	}
    }
    private void initImageViewCase() {
		mRequestCode = this.getIntent().getIntExtra(NEED_PARAM.REQUEST_CODE, 0);
		SystemUtil.f_log("mRequestCode:" + mRequestCode);
		switch (mRequestCode) {
		case TAKE_PHOTO:
			mFilePath = this.getIntent().getStringExtra(NEED_PARAM.LARGE_LOCAL_URI);
			takePhoto(mFilePath);
			break;
		case SELECT_PHOTO:
			mFilePath = this.getIntent().getStringExtra(NEED_PARAM.LARGE_LOCAL_URI);
			selectPhoto();
			break;
		case VIEW_LARGE_IMAGE:
			mFileUrl = this.getIntent().getStringExtra("url");
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
			break;
		case VIEW_LARGE_HEAD:
			mFileUrl = this.getIntent().getStringExtra(NEED_PARAM.LARGE_PORTRAIT_URL);
			SystemUtil.f_log("mFileUrl:" + mFileUrl);
			if(TextUtils.isEmpty(mFileUrl)){
				break;
			}
			String sdFilePath = Environment.getExternalStorageDirectory() + ImageUtils.SIXIN_HEAD_IMG_CACHE_DIR;
			mFilePath  = sdFilePath + mFileUrl.hashCode();
			if (!FileUtil.getInstance().isExistFile(sdFilePath)) {
				FileUtil.getInstance().createFile(sdFilePath);
			}
			File fileLargeHead = new File(mFilePath);
			if (fileLargeHead.isFile()) {
			} else if (fileLargeHead.isDirectory()) {
			}
			if (!fileLargeHead.exists()) {
				try {
					fileLargeHead.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			isLoadingImage(true);
			downloadBitmap(mFilePath, mFileUrl);
			break;
		case VIEW_OUT_TO_LOCAL_LARGE_IMAGE:
			String imgLargeUrl = this.getIntent().getStringExtra(NEED_PARAM.LARGE_PORTRAIT_URL);
			String imgLargePath = this.getIntent().getStringExtra(NEED_PARAM.LARGE_LOCAL_URI);
			mFileUrl = imgLargeUrl;
			SystemUtil.logykn("大图", imgLargeUrl);
			if (imgLargePath.endsWith(".jpg")) {
				mFilePath = imgLargePath.substring(0, imgLargePath.length() - 4);
			} else {
				mFilePath = imgLargePath;
			}
			isLoadingImage(true);
			downloadBitmap(mFilePath, mFileUrl);
			break;
		case VIEW_LOCAL_TO_OUT_LARGE_IMAGE:
			String path = this.getIntent().getStringExtra(NEED_PARAM.LARGE_LOCAL_URI);
			if (path.endsWith(".jpg")) {
				mFilePath = path.substring(0, path.length() - 4);
			} else {
				mFilePath = path;
			}
			 try {
                ExifInterface exif = new ExifInterface(mFilePath);
                if (exif != null) {
                    orientation = (int) Shared.exifOrientationToDegrees(exif.getAttributeInt(
                            ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL));
                }
            } catch (Exception ex) {}
            mBitmap = ImageLoader.getBitmapFromFile(mFilePath, null); 
	        break;
		}
    }
    
	private void takePhoto(String filepath) {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		mTmpFile = new File(filepath);
		Uri mImageCaptureUri = Uri.fromFile(mTmpFile);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
		startActivityForResult(intent, TAKE_PHOTO);
	}
    private void selectPhoto() {
		Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		intent.setType("image/*");
		startActivityForResult(intent, SELECT_PHOTO);
	}
    
    private void setupTitle() {
    	LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(74,34);
    	
    	switch (mRequestCode) {
    	case TAKE_PHOTO:
    	case SELECT_PHOTO:
    		setTitleButton();
    		break;
    	case PhotoUploadManager.REQUEST_CODE_HEAD_TAKE_PHOTO:
    		setTitleButton();
    		break;
    	default:
    		break;
    	}
    }
    
    private void setTitleButton(){
		RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(
				getResources().getDimensionPixelSize(
						R.dimen.imageview_title_button_width), getResources()
						.getDimensionPixelSize(
								R.dimen.imageview_title_button_height));
		RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(
				getResources().getDimensionPixelSize(
						R.dimen.imageview_title_button_width), getResources()
						.getDimensionPixelSize(
								R.dimen.imageview_title_button_height));
		
    	titleLeftButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.ykn_imageviewer_title_button_bg));
		params1.leftMargin = getResources().getDimensionPixelSize(R.dimen.imageview_title_button_margin);
		params1.addRule(RelativeLayout.CENTER_VERTICAL);
		params1.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		titleLeftButton.setLayoutParams(params1);
		titleRightButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.ykn_imageviewer_title_button_bg));
		params2.addRule(RelativeLayout.CENTER_VERTICAL);
		params2.rightMargin = getResources().getDimensionPixelSize(R.dimen.imageview_title_button_margin);
		params2.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		titleRightButton.setLayoutParams(params2);
		titleLeftButton.setText(RenrenChatApplication.getmContext().getResources().getString(R.string.ChatMessageWarpper_FlashEmotion_java_4));		//ChatMessageWarpper_FlashEmotion_java_4=取消; 
		titleRightButton.setText(RenrenChatApplication.getmContext().getResources().getString(R.string.cdw_chat_main_input_text_send_btn_text));		//cdw_chat_main_input_text_send_btn_text=发送; 
    }
    
    private void setupClickEvents() {
    	titleLeftButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
    	titleRightButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				switch (mRequestCode) {
				case TAKE_PHOTO:
					storeImageToFile(mBitmapSend, mFilePath, false);
					ImageViewActivity.this.setResult(RESULT_OK);
					finish();
					break;
				case SELECT_PHOTO:
					storeImageToFile(mBitmapSend, mFilePath, false);
					ImageViewActivity.this.setResult(RESULT_OK);
					finish();
					break;
				default:
					if(defaultShow){// add by xiangchao.fan
						SettingDataManager.getInstance().selectDialog(ImageViewActivity.this);
						break;
					}
					AlertDialog.Builder builder = new AlertDialog.Builder(ImageViewActivity.this);
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
									mBitmapSend = ImageUtil.image_Compression(mFilePath, RenrenChatApplication.screenResolution);
									storeImageToFile(mBitmapSend, mFilePath, false);
									/**MultiChatForwardActivity.show(ImageViewActivity.this, null, mFilePath);*/
									D_SelectGroupChatContactActivity.show(ImageViewActivity.this, mFilePath, true);
									
									break;
								case 3:
									break;
							}
						}
					});
					builder.show();
					break;
				}
			}
		});
    	findViewById(R.id.turn_left).setOnClickListener(new OnClickListener() {
    		Bitmap tempBitmap = null;
			@Override
			public void onClick(View v) {
				mBitmapRotateCount ++;
				/*mOrientaion -= 90;
				mFullImageGallery.rotateToLeft();*/
				switch (mRequestCode) {
		    	case TAKE_PHOTO:
		    	case SELECT_PHOTO:
		    		mOrientaion -= 90;
		    		tempBitmap = mBitmapSend;
		    		mBitmapSend = Util.rotate(mBitmapSend, -90);
		    		if (null != tempBitmap) {
		    			tempBitmap.recycle();
		    			tempBitmap = null;
		    		}
		    		mFullImageGallery.rotateToLeft();
		    		break;
		    	default:
		    		mOrientaion -= 90;
		    		tempBitmap = mBitmapSend;
		    		mBitmapSend = Util.rotate(mBitmap, mOrientaion);
		    		/*if (null != tempBitmap) {
		    			tempBitmap.recycle();
		    			tempBitmap = null;
		    		}*/
		    		mFullImageGallery.rotateToLeft();
		    		break;
				}
			}
		});
    	findViewById(R.id.turn_right).setOnClickListener(new OnClickListener() {
    		Bitmap tempBitmap = null;
			@Override
			public void onClick(View v) {
				mBitmapRotateCount ++;
				/*mOrientaion += 90;
				mFullImageGallery.rotateToRight();*/
				switch (mRequestCode) {
		    	case TAKE_PHOTO:
		    	case SELECT_PHOTO:
		    		mOrientaion += 90;
		    		tempBitmap = mBitmapSend;
		    		mBitmapSend = Util.rotate(mBitmapSend, 90);
		    		if (null != tempBitmap) {
		    			tempBitmap.recycle();
		    			tempBitmap = null;
		    		}
		    		mFullImageGallery.rotateToRight();
		    		break;
		    	default:
		    		mOrientaion += 90;
		    		tempBitmap = mBitmapSend;
		    		mBitmapSend = Util.rotate(mBitmap, mOrientaion);
		    		/*if (null != tempBitmap) {
		    			tempBitmap.recycle();
		    			tempBitmap = null;
		    		}*/
		    		mFullImageGallery.rotateToRight();
		    		break;
				}
			}
		});
    	/**
    	findViewById(R.id.zoom_out).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mFullImageGallery.zoomOut();
			}
		});
    	findViewById(R.id.zoom_in).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mFullImageGallery.zoomIn();
			}
		});*/
    }
    private void confirmShareDialog() {
    	AlertDialog.Builder builder = new AlertDialog.Builder(ImageViewActivity.this);
    	builder.setTitle(RenrenChatApplication.getmContext().getResources().getString(R.string.ImageViewActivity_java_2));		    //ImageViewActivity_java_2=分享到人人网; 
    	builder.setMessage(RenrenChatApplication.getmContext().getResources().getString(R.string.ImageViewActivity_java_3));		//ImageViewActivity_java_3=上传图片到人人网手机相册; 
    	builder.setPositiveButton(RenrenChatApplication.getmContext().getResources().getString(R.string.MultiChatForwardScreen_java_6), new DialogInterface.OnClickListener() {		//MultiChatForwardScreen_java_6=确认; 
			@Override
			public void onClick(DialogInterface dialog, int which) {
				ByteArrayOutputStream os = new ByteArrayOutputStream();
				Bitmap bitmap;
				if (mBitmapRotateCount > 0) {
					bitmap = mBitmapSend;
				} else {
					bitmap = mBitmap;
				}
				if (bitmap != null) {
					// TODO add by xiangchao.fan
					BindInfo bindInfoRenren = LoginManager.getInstance().getLoginInfo().mBindInfoRenren;
					if(bindInfoRenren == null ||(bindInfoRenren != null && TextUtils.isEmpty(bindInfoRenren.mBindId))){
						Intent intent = new Intent(ImageViewActivity.this,BindRenrenAccountActivity.class);
						intent.putExtra(BindRenrenAccountActivity.COME_FROM, BindRenrenAccountActivity.BIND_PHOTO);
						ImageViewActivity.this.startActivity(intent);
						SystemUtil.toast(RenrenChatApplication.getmContext().getResources().getString(R.string.ImageViewActivity_java_20));
					}else{
						notifyUploading();
						bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
						McsServiceProvider.getProvider().postPhoto(os.toByteArray(), 0, 0, 0, "", "", "", "", response);
					}
				} else {
					Toast.makeText(ImageViewActivity.this, RenrenChatApplication.getmContext().getResources().getString(R.string.ImageViewActivity_java_4), Toast.LENGTH_SHORT);		//ImageViewActivity_java_4=分享失败; 
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
    private void setupFullImageGallery() {
    	if (null == mBitmap) {
    		mBitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.default_image_loading);
    		mBitmapSend = BitmapFactory.decodeResource(this.getResources(), R.drawable.default_image_loading);
    	}
        mFullImageGallery = (ImageNavigatorView) findViewById(R.id.full_image_gallery);
        mFullImageGallery.setAdapter(new FullImageAdapter(this));
        mFullImageGallery.setSelection(0);
        mFullImageGallery.setHostActivity(this);
        if(!TextUtils.isEmpty(mFilePath) && (mFilePath.contains("brush") || mFilePath.contains(".png"))) {
        	this.mFullImageGallery.changeBackgroundColorTo(Color.WHITE);
        }
    }
    private ImageNavigatorView mFullImageGallery;
    private class FullImageAdapter extends BaseAdapter {
        private Context mContext;
        public FullImageAdapter(Context context) {
            mContext = context;
        }
        @Override
        public int getCount() {
            if (mBitmap != null) {
                return 1;
            }
            return 0;
        }
        @Override
        public Object getItem(int position) {
            if (mBitmap != null) {
                return mBitmap;
            }
            return null;
        }
        @Override
        public long getItemId(int position) {
            return position;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = new MultiTouchView(mContext);
            }
            MultiTouchView i = (MultiTouchView) convertView;
            i.setLayoutParams(new Gallery.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            i.setBitmap(mBitmap, orientation);
            return i;
        }
    }
    protected void onDestroy() {
        if (mFullImageGallery != null) {
            mFullImageGallery.recycle();
        }
        if (mBitmap != null && !mBitmap.isRecycled()) {
            mBitmap.recycle();
            mBitmap = null;
        }
        if (mBitmapSend != null && !mBitmapSend.isRecycled()) {
        	mBitmapSend.recycle();
        	mBitmapSend =null;
        }
    	RenrenChatApplication.popStack(ImageViewActivity.this);
        super.onDestroy();
    }
    public void switchMode() {
        setMode(MODE_FULL_SCREEN + MODE_SHOW_OPERATION_BAR - mMode);
    }
    private void setMode(int mode) {
        if (mode == MODE_FULL_SCREEN) {
        	mTitleBar.setVisibility(View.INVISIBLE);
            mOperationBar.setVisibility(View.INVISIBLE);
        } else if (mode == MODE_SHOW_OPERATION_BAR) {
        	mTitleBar.setVisibility(View.VISIBLE);
            mOperationBar.setVisibility(View.VISIBLE);
        } else {
            throw new IllegalArgumentException("the mode is invalid " + mode);
        }
        mMode = mode;
    }
//    @Override
//    public void onOptionsMenuClosed(Menu menu) {
//        switchMode();
//        super.onOptionsMenuClosed(menu);
//    }
//    @Override
//    public boolean onPrepareOptionsMenu(Menu menu) {
//        switchMode();
//        return super.onPrepareOptionsMenu(menu);
//    }
    /**
	 * Save image to SD card.
	 */
	/*private void storeImageToFile(Bitmap bitmap, String path, boolean isThumb) {
		String imgPath = null;
		if (path.endsWith(".jpg")) {
			imgPath = path.substring(0, path.length() - 4);
		} else {
			imgPath = path;
		}
		if (bitmap == null || bitmap.isRecycled()) {
			return;
		}
		int count = 15; 
		File file = null;
		FileOutputStream accessFile = null;
		do {
			file = new File(imgPath);
			count--;
		} while (!file.exists() && count > 0);
		ByteArrayOutputStream steam = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, 100, steam);
		byte[] buffer = steam.toByteArray();
		if (!isThumb) {
			Options op = new Options();
			op.inSampleSize = getSampleSize(bitmap.getWidth(), bitmap.getHeight(), 100);
			Bitmap thumb = BitmapFactory.decodeByteArray(steam.toByteArray(), 0, steam.toByteArray().length, op);
			// thumb);
			storeImageToFile(thumb, imgPath + "_small", true);
		}
		try {
			accessFile = new FileOutputStream(file);
			accessFile.write(buffer);
			accessFile.flush();
		} catch (Exception e) {
		}
		try {
			accessFile.close();
			steam.close();
		} catch (IOException e) {
		}
		int i = buffer.length / 1024;
	}*/
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
    /**
	 * Store image to SD card.
	 */
	public static void storeImageToFile(Bitmap bitmap, String path, boolean isThumb) {
		String imgPath = null;
		if (path.endsWith(".jpg")) {
			imgPath = path.substring(0, path.length() - 4);
		} else {
			imgPath = path;
		}
		if (bitmap == null || bitmap.isRecycled()) {
			return ;
		}
		File file = new File(imgPath);
		FileOutputStream accessFile = null;
		try {
			accessFile = new FileOutputStream(file);
			if(path.contains("brush") || path.contains("_brush_png")) {
				bitmap.compress(Bitmap.CompressFormat.PNG, 100, accessFile);
			}else{
				bitmap.compress(Bitmap.CompressFormat.JPEG, 100, accessFile);
			}
			accessFile.flush();
			accessFile.close();
		} catch (Exception e) {
			SystemUtil.toast(RenrenChatApplication.getmContext().getResources().getString(R.string.ImageViewActivity_java_5));		//ImageViewActivity_java_5=图片文件存储失败; 
			return ;
		}
		if (!isThumb) {
			Options op = new Options();
			op.inSampleSize = getSampleSize(bitmap.getWidth(), bitmap.getHeight(), 100);
			Bitmap thumb = BitmapFactory.decodeFile(file.toString(), op);
			storeImageToFile(thumb, imgPath + "_small", true);
			thumb.recycle();
		}
	}
	
	private void storePNGImageToFile(Bitmap bitmap, String path, boolean isThumb) {
		String imgPath = null;
		if (path.endsWith(".png")) {
			imgPath = path.substring(0, path.length() - 4);
		} else {
			imgPath = path;
		}
		if (bitmap == null || bitmap.isRecycled()) {
			return ;
		}
		File file = new File(imgPath);
		FileOutputStream accessFile = null;
		try {
			accessFile = new FileOutputStream(file);
			bitmap.compress(Bitmap.CompressFormat.PNG, 100, accessFile);
			accessFile.flush();
			accessFile.close();
		} catch (Exception e) {
			SystemUtil.toast(RenrenChatApplication.getmContext().getResources().getString(R.string.ImageViewActivity_java_5));		//ImageViewActivity_java_5=图片文件存储失败; 
			return ;
		}
		if (!isThumb) {
			Options op = new Options();
			op.inSampleSize = getSampleSize(bitmap.getWidth(), bitmap.getHeight(), 100);
			Bitmap thumb = BitmapFactory.decodeFile(file.toString(), op);
			storePNGImageToFile(thumb, imgPath + "_small", true);
			thumb.recycle();
		}
	}
	/**
	 * 通过指定的默认值，获取缩放比例
	 * */
	public static int getSampleSize(int width, int heigth, int defaultSize) {
		int realityDefaultSize = Methods.dip2px(RenrenChatApplication.getAppContext(), defaultSize);
		int maxEdageSize;
		if (width <= realityDefaultSize && heigth <= realityDefaultSize) {
			return 1;
		} else {
			if (width > heigth) {
				maxEdageSize = width;
			} else {
				maxEdageSize = heigth;
			}
			return maxEdageSize / realityDefaultSize;
		}
	}
	private void notifyUploading() {
		mNotificationUploading = new Notification(R.drawable.notification_upload, RenrenChatApplication.getmContext().getResources().getString(R.string.ImageViewActivity_java_6), System.currentTimeMillis());		//ImageViewActivity_java_6=正在上传照片...; 
		Intent intent = new Intent();
		mNotificationUploading.setLatestEventInfo(ImageViewActivity.this, RenrenChatApplication.getmContext().getResources().getString(R.string.app_name), RenrenChatApplication.getmContext().getResources().getString(R.string.ImageViewActivity_java_7), PendingIntent.getActivity(ImageViewActivity.this, 0, intent, 0));		//app_name=私信; //ImageViewActivity_java_7=照片上传中...; 
    	mNotificationManager.notify(mNotificationId, mNotificationUploading);
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
					mNotificationSucceed.setLatestEventInfo(ImageViewActivity.this, RenrenChatApplication.getmContext().getResources().getString(R.string.app_name), RenrenChatApplication.getmContext().getResources().getString(R.string.ImageViewActivity_java_9), PendingIntent.getActivity(ImageViewActivity.this, 0, intent, 0));		//app_name=私信; //ImageViewActivity_java_9=图片已上传到人人网; 
					mNotificationSucceed.flags = Notification.FLAG_AUTO_CANCEL;
			    	mNotificationManager.notify(mNotificationId - 1, mNotificationSucceed);
				} else {
					mNotificationManager.cancel(mNotificationId);
					mNotificationFailed = new Notification(R.drawable.notification_fail, RenrenChatApplication.getmContext().getResources().getString(R.string.ImageViewActivity_java_10), System.currentTimeMillis());		//ImageViewActivity_java_10=上传失败; 
					Intent intent = new Intent();
					mNotificationFailed.setLatestEventInfo(ImageViewActivity.this, RenrenChatApplication.getmContext().getResources().getString(R.string.app_name), RenrenChatApplication.getmContext().getResources().getString(R.string.ImageViewActivity_java_11), PendingIntent.getActivity(ImageViewActivity.this, 0, intent, 0));		//app_name=私信; //ImageViewActivity_java_11=图片上传失败; 
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
				saveBitmapToLocal(mBitmap, fileName);
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
		if (mFileUrl != null) {
			return String.valueOf(mFileUrl.hashCode());
		} else
			return String.valueOf(mFilePath.hashCode());
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
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, accessFile);
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
				mBitmap = ImageLoader.getBitmapFromFile(filepath, null);
				/*mBitmap = ImageUtil.image_Compression(filepath,
						RenrenChatApplication.screenResolution);*/
				isLoadingImage(false);
				if (null == mBitmap) {
					SystemUtil.toast(RenrenChatApplication.getmContext().getResources().getString(R.string.ImageViewActivity_java_17));		//ImageViewActivity_java_17=图片无法显示; 
					return;
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
									mBitmap = ImageUtil.createImageByBytes(imgData);
								}
								if (DeviceUtil.getInstance().isMountSDCard()) {
									if (DeviceUtil.getInstance().isSDCardHasEnoughSpace() && null != mBitmap) {
										saveBitmap(mBitmap, savePath ,savePath.endsWith(".png"));
									} else {
									}
								} else {
								}
							} 
							if (null == mBitmap) {
							} {
								setupFullImageGallery();
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
	private int saveBitmap(Bitmap bitmap, String path ,boolean isPng) {
		if(bitmap==null){
			return -1;
		}
		File file = new File(path);
		FileOutputStream accessFile = null;
		try {
			accessFile = new FileOutputStream(file);
			if(isPng)
				bitmap.compress(Bitmap.CompressFormat.PNG, 100, accessFile);
			else
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
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		mRequestCode = requestCode;
		if ((requestCode == PhotoUploadManager.REQUEST_CODE_HEAD_TAKE_PHOTO || requestCode == PhotoUploadManager.REQUEST_CODE_HEAD_CHOOSE_FROM_GALLERY)
				&& resultCode == RESULT_CANCELED) {
			return;
		}
		if (resultCode == RESULT_CANCELED) {
			finish();
			return;
		}
		switch (requestCode) {
		case TAKE_PHOTO:
			String filePath = mTmpFile.toString();
			DisplayMetrics dm = new DisplayMetrics();
		    getWindowManager().getDefaultDisplay().getMetrics(dm);
		    AsyncTask<Object, String, Bitmap> task = new AsyncTask<Object, String, Bitmap>(){
		    	@Override
		    	protected void onPreExecute() {
		    		isLoadingImage(true);
		    		super.onPreExecute();
		    	}
				@Override
				protected void onPostExecute(Bitmap result) {
					if (null != result) {
						setupFullImageGallery();
					}
					isLoadingImage(false);
					super.onPostExecute(result);
				}
				@Override
				protected Bitmap doInBackground(Object... params) {
					mBitmap = ImageUtil.image_Compression(String.valueOf(params[0]), (DisplayMetrics)params[1]);
					mBitmapSend = ImageUtil.image_Compression(String.valueOf(params[0]), (DisplayMetrics)params[1]);
					storeImageToFile(mBitmapSend, mFilePath, false);
					return mBitmap;
				}
		    };
		    task.execute(filePath, dm);
		    switchMode();
			break;			
		case SELECT_PHOTO:
			String tmpPath = null;
			if (data != null) {
				Cursor cursor = null;
				try {
					Uri originalUri = data.getData(); 
					
					String[] proj = { MediaStore.Images.Media.DATA };
					cursor = managedQuery(originalUri, proj, null, null, null);
					if (cursor != null) {
						int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
						cursor.moveToFirst();
						tmpPath = cursor.getString(column_index);
					} else {
						tmpPath = originalUri.getPath();
					}
				} catch (Exception e) {
					finish();
					Toast.makeText(ImageViewActivity.this, RenrenChatApplication.getmContext().getResources().getString(R.string.ImageViewActivity_java_19) + e, 1).show();		//ImageViewActivity_java_19=图片选择失败！; 
				} finally {
					if (cursor != null)
						cursor.close();
				}
				if (tmpPath != null && tmpPath.trim().length()>0) {
					File temp = new File(tmpPath);
					SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss");
					if (temp.exists() && temp.length() > 0) {
						mBitmap = ImageUtil.image_Compression(tmpPath, RenrenChatApplication.screenResolution);
						mBitmapSend = ImageUtil.image_Compression(tmpPath, RenrenChatApplication.screenResolution);
						storeImageToFile(mBitmapSend, mFilePath, false);
						if(mBitmap == null){
							finish();
						}
					} else {
						finish();
					}
				} else {
					finish();
				}
			}
			if (null != mBitmap) {
				setupFullImageGallery();
			}
			switchMode();
			break;
			
		// add by xiangchao.fan
		/* 拍照上传头像 */
		case PhotoUploadManager.REQUEST_CODE_HEAD_TAKE_PHOTO:
			Intent intent = new Intent(this, PhotoUploadActivity.class);
			intent.setData(PhotoUploadManager.getInstance().mUri);
			//startActivityForResult(intent, PhotoUploadManager.REQUEST_CODE_CUT);
			startActivity(intent);
			finish();
			break;
		/* 本地上传头像 */
		case PhotoUploadManager.REQUEST_CODE_HEAD_CHOOSE_FROM_GALLERY:
			intent = new Intent(this, PhotoUploadActivity.class);
			
			Uri selectedImage = data.getData();
//			String[] filePathColumn = { MediaStore.Images.Media.DATA };
//			Cursor cursor = getContentResolver().query(selectedImage,
//					filePathColumn, null, null, null);
//			cursor.moveToFirst();
//			int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
//			String picturePath = cursor.getString(columnIndex);
//			cursor.close();
//
//			
//			intent.setData(Uri.parse(picturePath));
			intent.setData(selectedImage);
			//startActivityForResult(intent, PhotoUploadManager.REQUEST_CODE_CUT);
			startActivity(intent);
			finish();
			break;
		/* 剪切图片返回 */
		/*case PhotoUploadManager.REQUEST_CODE_CUT:
			filePath = "/sixin/" + LoginManager.getInstance().getLoginInfo().mUserId + "/";
			String file = "head_" + System.currentTimeMillis();
			ContentValues values = PhotoUploadManager.getInstance()
					.getContentValues(data, filePath, file, ".jpg",
							"Image from head");
			this.getContentResolver().insert(
					MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
			Uri uri = PhotoUploadManager.getInstance().getUri(filePath,
					file, ".jpg");
			break;*/
		}
	}
	/**
	 * @description 跳转到查看大头像界面 或 从上传头像界面返回查看大图界面 add by xiangchao.fan
	 * @param context
	 */
	public static void show(Context context){
		Intent intent = new Intent(context,ImageViewActivity.class);
		intent.putExtra(ImageViewActivity.NEED_PARAM.REQUEST_CODE, ImageViewActivity.VIEW_LARGE_HEAD);
		SystemUtil.f_log("large:" + LoginManager.getInstance().getLoginInfo().mLargeUrl);
		//if(!TextUtils.isEmpty(LoginManager.getInstance().getLoginInfo().mLargeUrl)){
			//intent.putExtra(ImageViewActivity.NEED_PARAM.LARGE_PORTRAIT_URL, LoginManager.getInstance().getLoginInfo().mLargeUrl);
		//}else{
			intent.putExtra(ImageViewActivity.NEED_PARAM.LARGE_PORTRAIT_URL, LoginManager.getInstance().getLoginInfo().mOriginal_Url);
		//}
		//intent.putExtra(ImageViewActivity.NEED_PARAM.LARGE_PORTRAIT_URL, mLoginInfo.mLargeUrl);
		intent.putExtra(ImageViewActivity.NEED_PARAM.DEFAULT_SHOW, true);
		context.startActivity(intent);
	}

}
