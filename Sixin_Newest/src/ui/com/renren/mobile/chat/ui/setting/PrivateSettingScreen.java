package com.renren.mobile.chat.ui.setting;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.ProgressDialog;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

import com.common.manager.LoginManager;
import com.common.manager.LoginManager.LoginInfo;
import com.common.mcs.INetRequest;
import com.common.mcs.INetResponse;
import com.common.mcs.McsServiceProvider;
import com.core.json.JsonObject;
import com.core.json.JsonValue;
import com.core.util.SystemService;
import com.core.util.ViewMapUtil;
import com.core.util.ViewMapping;
import com.renren.mobile.chat.R;
import com.renren.mobile.chat.RenrenChatApplication;
import com.renren.mobile.chat.base.util.SystemUtil;
import com.renren.mobile.chat.common.ResponseError;
import com.renren.mobile.chat.ui.BaseScreen;
import com.renren.mobile.chat.view.GeneralSettingLayout;

public class PrivateSettingScreen extends BaseScreen{

	public class ViewHolder{
		@ViewMapping(ID=R.id.verification_setting)
		public GeneralSettingLayout mVirificationSetting;
		
		@ViewMapping(ID=R.id.search_by_sixin_id)
		public GeneralSettingLayout mSearchBySixinID;
		
		@ViewMapping(ID=R.id.search_by_mobile)
		public GeneralSettingLayout mSearchByMobile;
		
		@ViewMapping(ID=R.id.search_by_name)
		public GeneralSettingLayout mSearchByName;

	}

	private ViewHolder mHolder;
	private View mPrivateSettingView;
	
	private ProgressDialog mDialog;
	
	private LoginInfo mLoginfo;
	private SettingDataManager mManager;
	
	public PrivateSettingScreen(Activity activity) {
		super(activity);
		mPrivateSettingView = SystemService.sInflaterManager.inflate(R.layout.f_private_setting, null);
		setContent(mPrivateSettingView);
		
		mLoginfo = LoginManager.getInstance().getLoginInfo();
		mManager = SettingDataManager.getInstance();
		
		initTitle();
		initView();
		initEvent();
		
		if(!mManager.isPrivateInfoHasUpload()){
			mManager.obtainPrivateSwitchState();
			initSwitchState();
		}else if(mLoginfo.mPrivate == -1){
			getPrivacy();
		}else{
			mManager.parsePrivateSwitchState(mLoginfo.mPrivate);
			initSwitchState();
		}
	}
	
	private void initTitle() {
		this.getTitle().setTitleMiddle(mActivity.getResources().getString(R.string.PrivateSettingScreen_1));
		this.getTitle().getTitleLeft().setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
	
	private void initView(){
		mHolder = new ViewHolder();
		ViewMapUtil.getUtil().viewMapping(mHolder, mPrivateSettingView);
		
		mDialog = new ProgressDialog(mActivity);
		
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		
		// virification setting
		mHolder.mVirificationSetting.mHolder.mSettingName.setText(mActivity.getResources().getString(R.string.PrivateSettingScreen_2));
		mHolder.mVirificationSetting.mHolder.mSettingName.setLayoutParams(params);
		mHolder.mVirificationSetting.mHolder.mSettingDescription.setVisibility(View.GONE);
		mHolder.mVirificationSetting.mHolder.mStateLayout.setVisibility(View.VISIBLE);
		mHolder.mVirificationSetting.mHolder.mNextPageIcon.setVisibility(View.GONE);
		mHolder.mVirificationSetting.setEnabled(false);
		
		// SearchBySixinID setting
		mHolder.mSearchBySixinID.mHolder.mSettingName.setText(mActivity.getResources().getString(R.string.PrivateSettingScreen_3));
		mHolder.mSearchBySixinID.mHolder.mSettingName.setLayoutParams(params);
		mHolder.mSearchBySixinID.mHolder.mSettingDescription.setVisibility(View.GONE);
		mHolder.mSearchBySixinID.mHolder.mStateLayout.setVisibility(View.VISIBLE);
		mHolder.mSearchBySixinID.mHolder.mNextPageIcon.setVisibility(View.GONE);
		mHolder.mSearchBySixinID.setEnabled(false);
		
		// searchByMobile
		mHolder.mSearchByMobile.mHolder.mSettingName.setText(mActivity.getResources().getString(R.string.PrivateSettingScreen_4));
		mHolder.mSearchByMobile.mHolder.mSettingName.setLayoutParams(params);
		mHolder.mSearchByMobile.mHolder.mSettingDescription.setVisibility(View.GONE);
		mHolder.mSearchByMobile.mHolder.mStateLayout.setVisibility(View.VISIBLE);
		mHolder.mSearchByMobile.mHolder.mNextPageIcon.setVisibility(View.GONE);
		mHolder.mSearchByMobile.setEnabled(false);
		
		// searchByName setting
		mHolder.mSearchByName.mHolder.mSettingName.setText(mActivity.getResources().getString(R.string.PrivateSettingScreen_5));
		mHolder.mSearchByName.mHolder.mSettingName.setLayoutParams(params);
		mHolder.mSearchByName.mHolder.mSettingDescription.setVisibility(View.GONE);
		mHolder.mSearchByName.mHolder.mStateLayout.setVisibility(View.VISIBLE);
		mHolder.mSearchByName.mHolder.mNextPageIcon.setVisibility(View.GONE);
		mHolder.mSearchByName.setEnabled(false);
		
	}
	
	public void initEvent(){
		// virification setting
		mHolder.mVirificationSetting.mHolder.mStateLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				setVirificationWhenClick();
			}
		});
		
		// SearchBySixinID setting
		mHolder.mSearchBySixinID.mHolder.mStateLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				setSearchByIDStateWhenClick();
			}
		});
		
		// searchByMobile
		mHolder.mSearchByMobile.mHolder.mStateLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				setSearchByPhoneStateWhenClick();
			}
		});
		
		// searchByName setting
		mHolder.mSearchByName.mHolder.mStateLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				setSearchByNameStateWhenClick();
			}
		});
	}
	
	private void initSwitchState() {
		if (mManager.getVerificationState()) {
			mHolder.mVirificationSetting.mHolder.mSwitch
					.setBackgroundResource(R.drawable.switch_open);
			mHolder.mVirificationSetting.mHolder.mState.setText(mActivity
					.getResources().getString(R.string.f_setting_item_1));
		} else {
			mHolder.mVirificationSetting.mHolder.mSwitch
					.setBackgroundResource(R.drawable.switch_closed);
			mHolder.mVirificationSetting.mHolder.mState.setText(mActivity
					.getResources().getString(R.string.f_setting_item_2));
		}

		if (mManager.getIdSearchableState()) {
			mHolder.mSearchBySixinID.mHolder.mSwitch
					.setBackgroundResource(R.drawable.switch_open);
			mHolder.mSearchBySixinID.mHolder.mState.setText(mActivity
					.getResources().getString(R.string.f_setting_item_1));
		} else {
			mHolder.mSearchBySixinID.mHolder.mSwitch
					.setBackgroundResource(R.drawable.switch_closed);
			mHolder.mSearchBySixinID.mHolder.mState.setText(mActivity
					.getResources().getString(R.string.f_setting_item_2));
		}

		if (mManager.getNameSearchable()) {
			mHolder.mSearchByName.mHolder.mSwitch
					.setBackgroundResource(R.drawable.switch_open);
			mHolder.mSearchByName.mHolder.mState.setText(mActivity.getResources()
					.getString(R.string.f_setting_item_1));
		} else {
			mHolder.mSearchByName.mHolder.mSwitch
					.setBackgroundResource(R.drawable.switch_closed);
			mHolder.mSearchByName.mHolder.mState.setText(mActivity.getResources()
					.getString(R.string.f_setting_item_2));
		}
		
		if (mManager.getPhoneSearchable()) {
			mHolder.mSearchByMobile.mHolder.mSwitch
					.setBackgroundResource(R.drawable.switch_open);
			mHolder.mSearchByMobile.mHolder.mState.setText(mActivity.getResources()
					.getString(R.string.f_setting_item_1));
		} else {
			mHolder.mSearchByMobile.mHolder.mSwitch
					.setBackgroundResource(R.drawable.switch_closed);
			mHolder.mSearchByMobile.mHolder.mState.setText(mActivity.getResources()
					.getString(R.string.f_setting_item_2));
		}
	}
	
	private void setVirificationWhenClick() {
		if (!mManager.getVerificationState()) {
			mManager.setVerificationState(true);
			mHolder.mVirificationSetting.mHolder.mSwitch
					.setBackgroundResource(R.drawable.switch_open);
			mHolder.mVirificationSetting.mHolder.mState.setText(mActivity
					.getResources().getString(R.string.f_setting_item_1));
		} else {
			mManager.setVerificationState(false);
			mHolder.mVirificationSetting.mHolder.mSwitch
					.setBackgroundResource(R.drawable.switch_closed);
			mHolder.mVirificationSetting.mHolder.mState.setText(mActivity
					.getResources().getString(R.string.f_setting_item_2));
		}
	}
	
	private void setSearchByIDStateWhenClick() {
		if (!mManager.getIdSearchableState()) {
			mManager.setIdSearchableState(true);
			mHolder.mSearchBySixinID.mHolder.mSwitch
					.setBackgroundResource(R.drawable.switch_open);
			mHolder.mSearchBySixinID.mHolder.mState.setText(mActivity
					.getResources().getString(R.string.f_setting_item_1));
		} else {
			mManager.setIdSearchableState(false);
			mHolder.mSearchBySixinID.mHolder.mSwitch
					.setBackgroundResource(R.drawable.switch_closed);
			mHolder.mSearchBySixinID.mHolder.mState.setText(mActivity
					.getResources().getString(R.string.f_setting_item_2));
		}
	}
	
	private void setSearchByNameStateWhenClick() {

		if (!mManager.getNameSearchable()) {
			mManager.setNameSearchableState(true);
			mHolder.mSearchByName.mHolder.mSwitch
					.setBackgroundResource(R.drawable.switch_open);
			mHolder.mSearchByName.mHolder.mState.setText(mActivity.getResources()
					.getString(R.string.f_setting_item_1));
		} else {
			mManager.setNameSearchableState(false);
			mHolder.mSearchByName.mHolder.mSwitch
					.setBackgroundResource(R.drawable.switch_closed);
			mHolder.mSearchByName.mHolder.mState.setText(mActivity.getResources()
					.getString(R.string.f_setting_item_2));
		}
	}
	
	private void setSearchByPhoneStateWhenClick() {

		if (!mManager.getPhoneSearchable()) {
			mManager.setPhoneSearchableState(true);
			mHolder.mSearchByMobile.mHolder.mSwitch
					.setBackgroundResource(R.drawable.switch_open);
			mHolder.mSearchByMobile.mHolder.mState.setText(mActivity.getResources()
					.getString(R.string.f_setting_item_1));
		} else {
			mManager.setPhoneSearchableState(false);
			mHolder.mSearchByMobile.mHolder.mSwitch
					.setBackgroundResource(R.drawable.switch_closed);
			mHolder.mSearchByMobile.mHolder.mState.setText(mActivity.getResources()
					.getString(R.string.f_setting_item_2));
		}
	}
	
	public void getPrivacy(){

		showDialog(mActivity.getResources().getString(R.string.PrivateSettingScreen_6));
		// 获取用户profile
		McsServiceProvider.getProvider().getPrivacy(new INetResponse() {
			
			@Override
			public void response(final INetRequest req, JsonValue obj) {
				if (obj != null && obj instanceof JsonObject) {
					final JsonObject map = (JsonObject) obj;
					SystemUtil.f_log("map:" + map.toJsonString());
					RenrenChatApplication.mHandler.post(new Runnable() {
						@Override
						public void run() {
							if (ResponseError.noError(req, map,false)) {
								boolean verificationRequired = map.getBool("verification_required");
								boolean idSearchable = map.getBool("id_searchable");
								boolean nameSearchable = map.getBool("name_searchable");
								boolean phoneSearchable = map.getBool("phone_searchable");
								
								mManager.obtainPirvateSwitchState(verificationRequired, idSearchable, phoneSearchable, nameSearchable);
								
								initSwitchState();
								SystemUtil.f_log("mVerificationRequired:" + verificationRequired);
								SystemUtil.f_log("mIdSearchable:" + idSearchable);
								SystemUtil.f_log("mNameSearchable:" + nameSearchable);
								SystemUtil.f_log("mPhoneSearchable:" + phoneSearchable);
							}
							dismissDialog();
						}
					});
				}
			}
		});
		
	}
	
	private void showDialog(String text) {
		mDialog.setMessage(text);
		mDialog.show();
	}
	private void dismissDialog(){
		if(mDialog.isShowing()){
			mDialog.dismiss();
		}
	}

}
