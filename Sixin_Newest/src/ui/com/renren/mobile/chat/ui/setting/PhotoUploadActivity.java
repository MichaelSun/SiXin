package com.renren.mobile.chat.ui.setting;

import java.io.ByteArrayOutputStream;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.common.manager.LoginManager;
import com.common.manager.LoginManager.LoginInfo;
import com.common.manager.PhotoUploadManager;
import com.common.mcs.INetRequest;
import com.common.mcs.INetResponse;
import com.common.mcs.McsServiceProvider;
import com.core.json.JsonObject;
import com.core.json.JsonValue;
import com.core.util.SystemService;
import com.core.util.ViewMapUtil;
import com.core.util.ViewMapping;
import com.renren.mobile.account.LoginControlCenter;
import com.renren.mobile.account.LoginfoModel;
import com.renren.mobile.chat.R;
import com.renren.mobile.chat.RenrenChatApplication;
import com.renren.mobile.chat.base.util.SystemUtil;
import com.renren.mobile.chat.common.ResponseError;
import com.renren.mobile.chat.ui.BaseActivity;
import com.renren.mobile.chat.ui.imageviewer.ImageViewActivity;
import com.sixin.widgets.HeadEditImageView;

/**
 * @description 上传头像
 */
public class PhotoUploadActivity extends BaseActivity{
    
    public class ViewHolder{
    	/** title */
    	@ViewMapping(ID=R.id.photo_upload_title_bar)
    	public View mTitleBar;
    	
    	@ViewMapping(ID=R.id.photo_upload_title_left_button)
    	public Button mTitleLeftButton;
    	
    	@ViewMapping(ID=R.id.photo_upload_title_right_button)
    	public Button mTitleRightButton;
    	
    	
    	/** operation */
    	@ViewMapping(ID=R.id.photo_upload_operation_bar)
    	public View mOperationBar;

    	@ViewMapping(ID=R.id.photo_upload_turn_left)
    	public ImageView mTurnLeft;
    	
    	@ViewMapping(ID=R.id.photo_upload_turn_right)
    	public ImageView mTurnRight;
    	
    	
    	/** show image view */
    	@ViewMapping(ID=R.id.photo_upload_default_image_loading)
    	public View mDefaultImageLoading;

    	@ViewMapping(ID=R.id.photo_upload_image_view)
    	public HeadEditImageView mImageView;
    	
    	
    	/** cut domain */
    	@ViewMapping(ID=R.id.cut_domain)
    	public View mCutDomain;
    }
    
    public static interface MODE_TYPE{
	    int MODE_FULL_SCREEN = 0;
	    int MODE_SHOW_OPERATION_BAR = 1;
    }
    /** 控件holder */
    private ViewHolder mHolder;

    /** 位图 */
    private Bitmap mBitmap;
   
    /** 位图旋转方位 */
    private int mOrientaion = 0;
    
    /** 当前模式 */
    private int mMode;

    /** 用来上传的数组*/
    private byte[] bytes;
    
    /** 当前屏幕分辨率*/
    private float mDensity;
    
    /** 选取框的DP值*/
    private int selectedFrameDp = 268;

    /** 登录数据*/
    private LoginInfo mLoginInfo;
    
    private int mRequestCode;
    
    private boolean isUploading = false;
    
    /** 上传头像接口response */
	INetResponse uploadResponse = new INetResponse() {
		@Override
		public void response(INetRequest req, JsonValue obj) {
			boolean resultFlag = false;
			if (obj != null && obj instanceof JsonObject) {
				final JsonObject map = (JsonObject) obj;
				SystemUtil.f_log("map:" + map.toJsonString());

				if (ResponseError.noError(req, map, true)) {
					String mediumUrl = map.getString("medium_url");
					String largeUrl = map.getString("large_url");
					String original_Url = map.getString("original_url");

					if (!TextUtils.isEmpty(mediumUrl)
							&& !TextUtils.isEmpty(largeUrl)
							&& !TextUtils.isEmpty(original_Url)) {
						resultFlag = true;

						mLoginInfo.mMediumUrl = mediumUrl;
						mLoginInfo.mLargeUrl = largeUrl;
						mLoginInfo.mOriginal_Url = original_Url;

						// update database
						LoginfoModel model = new LoginfoModel();
						model.parse(mLoginInfo);
						LoginControlCenter.getInstance().updateUserData(model);

						// update page
						SettingDataManager.getInstance()
								.notifyAllPhotoUploadSuccessListeners();

						RenrenChatApplication.HANDLER.post(new Runnable() {

							@Override
							public void run() {
								mDialog.dismiss();
								SystemUtil.toast(getResources().getString(
										R.string.PhotoUploadActivity_2));

								//ImageViewActivity.show(PhotoUploadActivity.this);

								finish();
							}
						});
					}
				}
				if (!resultFlag) {
					RenrenChatApplication.HANDLER.post(new Runnable() {

						@Override
						public void run() {
							mDialog.dismiss();
							SystemUtil.toast(getResources().getString(
									R.string.PhotoUploadActivity_3));
						}
					});
				}
			}
		}
	};
    
    private Dialog mDialog;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //初始化holder
        mHolder = new ViewHolder();
        View view = SystemService.sInflaterManager.inflate(R.layout.f_photo_upload, null);
        setContentView(view);
        ViewMapUtil.getUtil().viewMapping(mHolder, view);
        
		mDialog = SettingDataManager.getInstance().createLoadingDialog(this,
				getResources().getString(R.string.PhotoUploadActivity_1), Color.WHITE);

        mLoginInfo = LoginManager.getInstance().getLoginInfo();
        
        getDensity();
        
        setMode(MODE_TYPE.MODE_SHOW_OPERATION_BAR);
        
        setupClickEvents();

        setImage(getIntent().getData());
        
    }
    
    /** 取得当前屏幕分辨率 */
    private void getDensity(){
        //DisplayMetrics dm = new DisplayMetrics();
        //getWindow().getWindowManager().getDefaultDisplay().getMetrics(dm);
        //SystemService.sWindowsManager.getDefaultDisplay().getMetrics(dm);
        mDensity = RenrenChatApplication.metric.density;
    }
    
    /**
     * 显示图片
     */
    private void setImage(final Uri uri) {
    	
    	int screenWidth = getWindowManager().getDefaultDisplay().getWidth();
        int screenHeight = getWindowManager().getDefaultDisplay().getHeight();
        mHolder.mImageView.setScreenWidth(screenWidth);
        mHolder.mImageView.setScreenHeight(screenHeight);
        
        float marginLeft = (screenWidth - mDensity * selectedFrameDp)/2;
        float marginRight = (screenWidth - mDensity * selectedFrameDp)/2;
        float marginTop = (screenHeight - mDensity * selectedFrameDp)/2;
        float marginBottom = (screenHeight - mDensity * selectedFrameDp)/2;
        mHolder.mImageView.setMarginLeft(marginLeft);
        mHolder.mImageView.setMarginRight(marginRight);
        mHolder.mImageView.setMarginTop(marginTop);
        mHolder.mImageView.setMarginBottom(marginBottom);
        
        mHolder.mImageView.setDensity(mDensity);
        
        mHolder.mDefaultImageLoading.setVisibility(View.VISIBLE);
        
		HeadEditImageView.isLoadingBitmap = true;
        new Thread(new Runnable() {
            @Override
			public void run() {
				if (uri != null) {
//					mBitmap = PhotoUploadManager.getInstance().getBitmap(
//							PhotoUploadActivity.this, uri);
					SystemUtil
					.f_log("uri:" + uri.getPath());
					mBitmap = PhotoUploadManager.getInstance().getBitmap(PhotoUploadActivity.this, uri);
					SystemUtil
							.f_log("mBitmap == null:" + (mBitmap == null));
					if (mBitmap == null || !isBitmapCanUse()) {
						SystemUtil.toast(getResources().getString(R.string.PhotoUploadActivity_7));
						finish();
					} else {
						try{
							bytes = Bitmap2Bytes(mBitmap);
							setImageBitmap();
						}catch(Exception e){
							SystemUtil.f_log("e:" + e.toString());
							SystemUtil.toast(getResources().getString(R.string.PhotoUploadActivity_9));
							finish();
						}
					}
				}
			}
        }).start();
 
    }
    
    private void setImageBitmap(){
    	
    	RenrenChatApplication.HANDLER.post(new Runnable() {
			@Override
			public void run() {
				//mHolder.mCutDomain.setVisibility(View.VISIBLE);
		    	mHolder.mDefaultImageLoading.setVisibility(View.GONE);
		    	HeadEditImageView.isLoadingBitmap = false;
		    	mHolder.mImageView.setImageWidth(mBitmap.getWidth());
		    	mHolder.mImageView.setImageHeight(mBitmap.getHeight());
		    	mHolder.mImageView.setImageBitmap(mBitmap);
		    	mHolder.mImageView.setBytes(bytes);
			}
		});
    	
    }
 
    private boolean isBitmapCanUse() {
        SystemUtil.f_log("mDensity = " + mDensity + " selectedFrameDp = " + selectedFrameDp);
        return (mBitmap.getWidth() >= selectedFrameDp * mDensity) && (mBitmap.getHeight() >= selectedFrameDp * mDensity);
    }

    private void setupClickEvents() {
        mHolder.mTitleLeftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	ImageViewActivity.show(PhotoUploadActivity.this);
				finish();
            }
        });
        mHolder.mTitleRightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	// TODO finishDialog();
            	mDialog.show();
            	new Thread(new Runnable() {
					
					@Override
					public void run() {
						uploadHeadPhoto();
					}
				}).start();
            }
        });

        mHolder.mTurnLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	mHolder.mImageView.turnLeft();
            }
        });
        mHolder.mTurnRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	mHolder.mImageView.turnRight();
            }
        });
    }

    private byte[] Bitmap2Bytes(Bitmap bm) {
    	ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try{

            mBitmap.compress(Bitmap.CompressFormat.PNG, 80, outputStream);
            byte[] bytes = outputStream.toByteArray();
            outputStream.flush();
            outputStream.close();
            return bytes;
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    protected void onDestroy() {
        if (mBitmap != null && !mBitmap.isRecycled()) {
            mBitmap.recycle();
            mBitmap = null;
        }
        if (mHolder.mImageView.getBitmap() != null && !mHolder.mImageView.getBitmap().isRecycled()) {
        	mHolder.mImageView.setBitmapNull();
        }
        if(PhotoUploadManager.getInstance().bitmap != null && !PhotoUploadManager.getInstance().bitmap.isRecycled()){

            PhotoUploadManager.getInstance().bitmap.recycle();
            PhotoUploadManager.getInstance().bitmap = null;
        }
        bytes = null;
        PhotoUploadManager.getInstance().bytes = null;
        System.gc();
        super.onDestroy();
    }

    public void switchMode() {
        setMode(MODE_TYPE.MODE_FULL_SCREEN + MODE_TYPE.MODE_SHOW_OPERATION_BAR - mMode);
    }

    private void setMode(int mode) {
        if (mode == MODE_TYPE.MODE_FULL_SCREEN) {
            mHolder.mTitleBar.setVisibility(View.INVISIBLE);
            mHolder.mOperationBar.setVisibility(View.INVISIBLE);
        } else if (mode == MODE_TYPE.MODE_SHOW_OPERATION_BAR) {
        	mHolder.mTitleBar.setVisibility(View.VISIBLE);
        	mHolder.mOperationBar.setVisibility(View.VISIBLE);
        } else {
            throw new IllegalArgumentException("the mode is invalid " + mode);
        }
        mMode = mode;
    }
    
    /** 修改头像完成确认 */
    private void finishDialog(){
    	AlertDialog.Builder builder = new AlertDialog.Builder(PhotoUploadActivity.this);
    	builder.setTitle(getResources().getString(R.string.PhotoUploadActivity_6));
        builder.setItems(R.array.upload_photo, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0://使用
                    	uploadHeadPhoto();
                        break;
                    case 1://
                    	SettingDataManager.getInstance().selectDialog(PhotoUploadActivity.this);
                        break;
                    case 2://取消
                        break;
                }
            }
        });
        builder.show();
    }
    
    private void uploadHeadPhoto(){
    	float distanceX = mHolder.mImageView.getMoveHorizontal();
        float distanceY = mHolder.mImageView.getMoveVertical();
        float frameMarinLeft = (mHolder.mImageView.getScreenWidth() - selectedFrameDp * mDensity) / 2;
        float frameMarginTop = (mHolder.mImageView.getScreenHeight() - selectedFrameDp * mDensity) / 2;
        if(distanceX > frameMarinLeft){
            distanceX = frameMarinLeft;
        }
        if(distanceY > frameMarginTop){
            distanceY = frameMarginTop;
        }
        float frameSize = selectedFrameDp * mDensity;

        int left = (int) ( (frameMarinLeft - distanceX) / (mHolder.mImageView.getScale()) );
        int top = (int) ( (frameMarginTop - distanceY) / (mHolder.mImageView.getScale()) );
        int width = (int) ( frameSize / (mHolder.mImageView.getScale()) );
        int height = (int) ( frameSize / (mHolder.mImageView.getScale()) );
        bytes = mHolder.mImageView.getBytes();//旋转之后bytes变化
        if(bytes != null && bytes.length != 0){
        	McsServiceProvider.getProvider().uploadHeadPhoto(uploadResponse, left, top, width, height, bytes);
        }
    }
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
    	if(keyCode == KeyEvent.KEYCODE_BACK){
    		ImageViewActivity.show(PhotoUploadActivity.this);
    		finish();
    		return true;
    	}
    	return super.onKeyDown(keyCode, event);
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	switch (requestCode) {
			
		// add by xiangchao.fan
		/* 拍照上传头像 */
		case PhotoUploadManager.REQUEST_CODE_HEAD_TAKE_PHOTO:
			setImage(data.getData());
			break;
		/* 本地上传头像 */
		case PhotoUploadManager.REQUEST_CODE_HEAD_CHOOSE_FROM_GALLERY:
			Uri selectedImage = data.getData();
			String[] filePathColumn = { MediaStore.Images.Media.DATA };
			Cursor cursor = getContentResolver().query(selectedImage,
					filePathColumn, null, null, null);
			cursor.moveToFirst();
			int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
			String picturePath = cursor.getString(columnIndex);
			cursor.close();
			
			setImage(Uri.parse(picturePath));
			break;
		}
    	super.onActivityResult(requestCode, resultCode, data);
    }
}
