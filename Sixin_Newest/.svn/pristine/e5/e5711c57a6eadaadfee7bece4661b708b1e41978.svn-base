package com.renren.mobile.chat.ui.setting;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;

import com.common.utils.FileUtil;
import com.core.util.SystemService;
import com.core.util.ViewMapUtil;
import com.core.util.ViewMapping;
import com.renren.mobile.chat.R;
import com.renren.mobile.chat.RenrenChatApplication;
import com.renren.mobile.chat.base.util.SystemUtil;
import com.renren.mobile.chat.cache.ImageUtils;
import com.renren.mobile.chat.ui.BaseScreen;
import com.renren.mobile.chat.ui.chatsession.ChatSessionManager;
import com.renren.mobile.chat.view.GeneralSettingLayout;

/**
 * @author xiangchao.fan
 * @description system setting page screen
 */
public class SystemSettingScreen extends BaseScreen {

	public class ViewHolder {
		@ViewMapping(ID = R.id.sound_remind)
		public GeneralSettingLayout mSoundRemind;

		@ViewMapping(ID = R.id.vibration_remind)
		public GeneralSettingLayout mVibrationRemind;

		@ViewMapping(ID = R.id.push_remind)
		public GeneralSettingLayout mPushRemind;

		@ViewMapping(ID = R.id.language_setting)
		public GeneralSettingLayout mLanguageSetting;

		@ViewMapping(ID = R.id.clear_cache)
		public GeneralSettingLayout mClearCache;
	}

	private View mSystemSettingView;
	private ViewHolder mHolder;
	
	private SettingDataManager mDataManager;
	
	private Dialog mDialog;
	private Dialog mLoadingDialog;
	
	public SystemSettingScreen(Activity activity) {
		super(activity);
		mSystemSettingView = SystemService.sInflaterManager.inflate(
				R.layout.f_system_setting, null);
		setContent(mSystemSettingView);

		mDataManager = SettingDataManager.getInstance();
		
		mDataManager.obtainSwitchState();
		
		initTitle();
		initView();
		initEvent();
	}

	private void initTitle() {
		this.getTitle().setTitleMiddle(mActivity.getResources().getString(R.string.SystemSettingScreen_1));
		this.getTitle().getTitleLeft()
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						finish();
					}
				});
	}

	private void initView() {
		mHolder = new ViewHolder();
		ViewMapUtil.getUtil().viewMapping(mHolder, mSystemSettingView);

		mDialog = createConfirmDialog();
		mLoadingDialog = mDataManager.createLoadingDialog(mActivity, mActivity.getResources().getString(R.string.F_SettingScreen_17));
		
		// sound setting
		mHolder.mSoundRemind.mHolder.mSettingName.setText(mActivity.getResources().getString(R.string.SystemSettingScreen_2));
		mHolder.mSoundRemind.mHolder.mSettingDescription
				.setVisibility(View.GONE);
		mHolder.mSoundRemind.mHolder.mStateLayout.setVisibility(View.VISIBLE);
		mHolder.mSoundRemind.mHolder.mNextPageIcon.setVisibility(View.GONE);
		mHolder.mSoundRemind.setEnabled(false);

		// vibration setting
		mHolder.mVibrationRemind.mHolder.mSettingName.setText(mActivity.getResources().getString(R.string.SystemSettingScreen_3));
		mHolder.mVibrationRemind.mHolder.mSettingDescription
				.setVisibility(View.GONE);
		mHolder.mVibrationRemind.mHolder.mStateLayout.setVisibility(View.VISIBLE);
		mHolder.mVibrationRemind.mHolder.mNextPageIcon.setVisibility(View.GONE);
		mHolder.mVibrationRemind.setEnabled(false);
		
		// push
		mHolder.mPushRemind.mHolder.mSettingName.setText(mActivity.getResources().getString(R.string.SystemSettingScreen_4));
		mHolder.mPushRemind.mHolder.mSettingDescription
				.setVisibility(View.GONE);
		mHolder.mPushRemind.mHolder.mStateLayout.setVisibility(View.VISIBLE);
		mHolder.mPushRemind.mHolder.mNextPageIcon.setVisibility(View.GONE);
		mHolder.mPushRemind.setEnabled(false);

		// language setting
		mHolder.mLanguageSetting.mHolder.mSettingName.setText(mActivity.getResources().getString(R.string.SystemSettingScreen_5));
		mHolder.mLanguageSetting.mHolder.mSettingDescription
				.setVisibility(View.GONE);

		// cache setting
		mHolder.mClearCache.mHolder.mSettingName.setText(mActivity.getResources().getString(R.string.SystemSettingScreen_6));
		mHolder.mClearCache.mHolder.mSettingDescription
				.setVisibility(View.GONE);
		mHolder.mClearCache.mHolder.mNextPageIcon.setVisibility(View.GONE);
		
		initSwitchState();
		
	}
	
	private void initEvent() {
		// sound setting
		mHolder.mSoundRemind.mHolder.mStateLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				setSoundStateWhenClick();
			}
		});
		
		// vibration setting
		mHolder.mVibrationRemind.mHolder.mStateLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				setVibrateStateWhenClick();
			}
		});
		
		// push setting
		mHolder.mPushRemind.mHolder.mStateLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				setPushStateWhenClick();
			}
		});
		
		// language setting
		mHolder.mLanguageSetting.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				SystemSettingForwardActivity
						.show(mActivity,
								SystemSettingForwardActivity.SystemForwardScreenType.LANGUAGE_SETTING_SCREEN);
			}
		});

		// cache setting
		mHolder.mClearCache.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
//				SystemSettingForwardActivity
//				.show(mActivity,
//						SystemSettingForwardActivity.SystemForwardScreenType.CLIEAR_CACHE_SCREEN);
				mDialog.show();
			}
		});
	}
	
	private void initSwitchState() {
		if (mDataManager.mSoundState) {
			mHolder.mSoundRemind.mHolder.mSwitch
					.setBackgroundResource(R.drawable.switch_open);
			mHolder.mSoundRemind.mHolder.mState.setText(mActivity
					.getResources().getString(R.string.f_setting_item_1));
		} else {
			mHolder.mSoundRemind.mHolder.mSwitch
					.setBackgroundResource(R.drawable.switch_closed);
			mHolder.mSoundRemind.mHolder.mState.setText(mActivity
					.getResources().getString(R.string.f_setting_item_2));
		}

		if (mDataManager.mVibrateState) {
			mHolder.mVibrationRemind.mHolder.mSwitch
					.setBackgroundResource(R.drawable.switch_open);
			mHolder.mVibrationRemind.mHolder.mState.setText(mActivity
					.getResources().getString(R.string.f_setting_item_1));
		} else {
			mHolder.mVibrationRemind.mHolder.mSwitch
					.setBackgroundResource(R.drawable.switch_closed);
			mHolder.mVibrationRemind.mHolder.mState.setText(mActivity
					.getResources().getString(R.string.f_setting_item_2));
		}

		if (mDataManager.mPushState) {
			mHolder.mPushRemind.mHolder.mSwitch
					.setBackgroundResource(R.drawable.switch_open);
			mHolder.mPushRemind.mHolder.mState.setText(mActivity.getResources()
					.getString(R.string.f_setting_item_1));
		} else {
			mHolder.mPushRemind.mHolder.mSwitch
					.setBackgroundResource(R.drawable.switch_closed);
			mHolder.mPushRemind.mHolder.mState.setText(mActivity.getResources()
					.getString(R.string.f_setting_item_2));
		}
	}
	
	private void setSoundStateWhenClick() {
		if (!mDataManager.mSoundState) {
			mDataManager.setSoundState(true);
			mHolder.mSoundRemind.mHolder.mSwitch
					.setBackgroundResource(R.drawable.switch_open);
			mHolder.mSoundRemind.mHolder.mState.setText(mActivity
					.getResources().getString(R.string.f_setting_item_1));
		} else {
			mDataManager.setSoundState(false);
			mHolder.mSoundRemind.mHolder.mSwitch
					.setBackgroundResource(R.drawable.switch_closed);
			mHolder.mSoundRemind.mHolder.mState.setText(mActivity
					.getResources().getString(R.string.f_setting_item_2));
		}
	}
	
	private void setVibrateStateWhenClick() {
		if (!mDataManager.mVibrateState) {
			mDataManager.setVibrateState(true);
			mHolder.mVibrationRemind.mHolder.mSwitch
					.setBackgroundResource(R.drawable.switch_open);
			mHolder.mVibrationRemind.mHolder.mState.setText(mActivity
					.getResources().getString(R.string.f_setting_item_1));
		} else {
			mDataManager.setVibrateState(false);
			mHolder.mVibrationRemind.mHolder.mSwitch
					.setBackgroundResource(R.drawable.switch_closed);
			mHolder.mVibrationRemind.mHolder.mState.setText(mActivity
					.getResources().getString(R.string.f_setting_item_2));
		}
	}
	
	private void setPushStateWhenClick() {

		if (!mDataManager.mPushState) {
			mDataManager.setPushState(true);
			mHolder.mPushRemind.mHolder.mSwitch
					.setBackgroundResource(R.drawable.switch_open);
			mHolder.mPushRemind.mHolder.mState.setText(mActivity.getResources()
					.getString(R.string.f_setting_item_1));
		} else {
			mDataManager.setPushState(false);
			mHolder.mPushRemind.mHolder.mSwitch
					.setBackgroundResource(R.drawable.switch_closed);
			mHolder.mPushRemind.mHolder.mState.setText(mActivity.getResources()
					.getString(R.string.f_setting_item_2));
		}
	}
	
	private Dialog createConfirmDialog(){
		AlertDialog.Builder buider = new AlertDialog.Builder(mActivity);
		buider.setTitle(mActivity.getResources()
				.getString(R.string.SystemSettingScreen_7));
		buider.setPositiveButton(mActivity.getResources()
				.getString(R.string.SystemSettingScreen_8), new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				mLoadingDialog.show();
    			new Thread(new ProcessRunnable()).start();
			}
		});
		buider.setNegativeButton(mActivity.getResources()
				.getString(R.string.SystemSettingScreen_9), new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
        return buider.create();
		
	}

	public class ProcessRunnable implements Runnable{

		@Override
		public void run() {
			
			String fileFolder = Environment.getExternalStorageDirectory() + "/sixin/";
			SystemUtil.f_log("fileFolder:" + fileFolder);
			//SettingDataManager.getInstance().delAllFile(fileFolder);
			FileUtil.getInstance().delFolder(fileFolder, ImageUtils.SIXIN_HEAD_IMG_CACHE_DIR);
			
			SystemUtil.f_log("delete fileFolder:");
			
			// 清除聊天记录 TODO
			ChatSessionManager.getInstance().clearAllRecord();
			
			SystemUtil.f_log("delete chat:");
			
			RenrenChatApplication.HANDLER.post(new Runnable() {
				
				@Override
				public void run() {
					SystemUtil.toast("清除成功");
					mLoadingDialog.dismiss();
				}
			});
		}
	}
}
