package com.renren.mobile.chat.ui.setting;

import java.util.List;

import plugin.DBBasedPluginManager;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.common.manager.LoginManager;
import com.common.manager.LoginManager.BindInfoObserver;
import com.common.manager.LoginManager.LoginInfo;
import com.common.mcs.INetRequest;
import com.common.mcs.INetResponse;
import com.common.mcs.McsServiceProvider;
import com.core.json.JsonObject;
import com.core.json.JsonValue;
import com.core.util.SystemService;
import com.core.util.ViewMapUtil;
import com.core.util.ViewMapping;
import com.renren.mobile.account.LoginControlCenter;
import com.renren.mobile.chat.R;
import com.renren.mobile.chat.RenrenChatApplication;
import com.renren.mobile.chat.activity.RenRenChatActivity;
import com.renren.mobile.chat.base.model.ChatBaseItem;
import com.renren.mobile.chat.base.model.ChatBaseItem.MESSAGE_ISGROUP;
import com.renren.mobile.chat.base.util.SystemUtil;
import com.renren.mobile.chat.common.ResponseError;
import com.renren.mobile.chat.model.warpper.ChatMessageFactory;
import com.renren.mobile.chat.model.warpper.ChatMessageWarpper;
import com.renren.mobile.chat.ui.BaseScreen;
import com.renren.mobile.chat.ui.account.BindRenrenAccountScreen;
import com.renren.mobile.chat.ui.contact.C_ContactsData;
import com.renren.mobile.chat.ui.contact.ContactBaseModel;
import com.renren.mobile.chat.ui.contact.ContactModel;
import com.renren.mobile.chat.ui.contact.IDownloadContactListener;
import com.renren.mobile.chat.ui.guide.WelcomeActivity;
import com.renren.mobile.chat.ui.setting.SettingDataManager.PhotoUploadSuccessListener;
import com.renren.mobile.chat.view.GeneralSettingLayout;

/**
 * @author xiangchao.fan
 * @description setting screen begin with sixin3.0
 */
public class F_SettingScreen extends BaseScreen implements PhotoUploadSuccessListener,BindInfoObserver{

	public static final long SIXIN_XIAO_MI_SHU = 90001L;
	long old,newt;
	public class ViewHolder {
		@ViewMapping(ID = R.id.self_setting)
		public GeneralSettingLayout mSelfSetting;

		@ViewMapping(ID = R.id.system_setting)
		public GeneralSettingLayout mSystemSetting;

		@ViewMapping(ID = R.id.bind_renren)
		public GeneralSettingLayout mBindRenren;

		@ViewMapping(ID = R.id.blacklist_setting)
		public GeneralSettingLayout mBlackListSetting;

		@ViewMapping(ID = R.id.private_setting)
		public GeneralSettingLayout mPrivateSetting;

		@ViewMapping(ID = R.id.newuser_guide)
		public GeneralSettingLayout mNewuserGuide;

		@ViewMapping(ID = R.id.about_sixin)
		public GeneralSettingLayout mAboutSixin;

		@ViewMapping(ID = R.id.user_feedback)
		public GeneralSettingLayout mUserFeedback;

		@ViewMapping(ID = R.id.evaluate_sixin)
		public GeneralSettingLayout mEvaluateSixin;

		@ViewMapping(ID = R.id.setting_logout)
		public Button mSettingLogout;
		
		@ViewMapping(ID=R.id.setting_logout_loading)
		public View mLoading;
		
		@ViewMapping(ID=R.id.loading_text)
		public TextView mLoadingText;
	}

	private View mSettingView;
	private ViewHolder mHolder;
	
	private LoginInfo mLoginfo;

	private Dialog mToFeedBackDialog;
	
	public F_SettingScreen(Activity activity) {
		super(activity);
		mSettingView = SystemService.sInflaterManager.inflate(
				R.layout.f_setting, null);
		setContent(mSettingView);

		SettingDataManager.getInstance().registerPhotoUploadSuccessListener(this);
		BindRenrenAccountScreen.registerObserver(this);
		initTitle();
		initView();
		initEvent();
	}

	private void initTitle() {
		this.getTitle().setTitleMiddle(mActivity.getResources().getString(R.string.F_SettingScreen_1));
		this.getTitle().setTitleButtonLeftBackVisibility(false);
		//this.getTitle().setTitleFunctionButtonBackground(FUNCTION_BUTTON_TYPE.SETTING);
	}

	private void initView() {
		mHolder = new ViewHolder();
		ViewMapUtil.getUtil().viewMapping(mHolder, mSettingView);

		mToFeedBackDialog = SettingDataManager.getInstance().createLoadingDialog(mActivity, mActivity.getResources().getString(R.string.F_SettingScreen_17));
		// self setting
		mHolder.mSelfSetting.mHolder.mHead.setVisibility(View.VISIBLE);
		mHolder.mSelfSetting.mHolder.mSettingName.setText(mActivity.getResources().getString(R.string.F_SettingScreen_2));
		mHolder.mSelfSetting.mHolder.mSettingDescription
				.setText(mActivity.getResources().getString(R.string.F_SettingScreen_3));
		mHolder.mSelfSetting.mHolder.mHead.clear();
		mHolder.mSelfSetting.mHolder.mHead
				.addUrl(LoginManager.getInstance().getLoginInfo().mMediumUrl);
		
		// system setting
		mHolder.mSystemSetting.mHolder.mSettingName.setText(mActivity.getResources().getString(R.string.F_SettingScreen_4));
		mHolder.mSystemSetting.mHolder.mSettingDescription
				.setText(mActivity.getResources().getString(R.string.F_SettingScreen_5));

		// bind renren
		mHolder.mBindRenren.mHolder.mSettingIcon.setVisibility(View.VISIBLE);
		mHolder.mBindRenren.mHolder.mSettingIcon.setBackgroundResource(R.drawable.renren_logo_setting);
		mHolder.mBindRenren.mHolder.mSettingName.setText(mActivity.getResources().getString(R.string.F_SettingScreen_6));
		mHolder.mBindRenren.mHolder.mSettingDescription
				.setText(mActivity.getResources().getString(R.string.F_SettingScreen_7));
		updateRenrenBind();

		// blacklist setting
		mHolder.mBlackListSetting.mHolder.mSettingName.setText(mActivity.getResources().getString(R.string.F_SettingScreen_8));
		mHolder.mBlackListSetting.mHolder.mSettingDescription
				.setText(mActivity.getResources().getString(R.string.F_SettingScreen_9));

		// private setting
		mHolder.mPrivateSetting.mHolder.mSettingName.setText(mActivity.getResources().getString(R.string.F_SettingScreen_10));
		mHolder.mPrivateSetting.mHolder.mSettingDescription
				.setText(mActivity.getResources().getString(R.string.F_SettingScreen_11));

		// newuser guide
		mHolder.mNewuserGuide.mHolder.mSettingName.setText(mActivity.getResources().getString(R.string.F_SettingScreen_12));
		mHolder.mNewuserGuide.mHolder.mSettingDescription
				.setVisibility(View.GONE);

		// about sixin
		mHolder.mAboutSixin.mHolder.mSettingName.setText(mActivity.getResources().getString(R.string.F_SettingScreen_13));
		mHolder.mAboutSixin.mHolder.mSettingDescription
				.setVisibility(View.GONE);

		// user feedback
		mHolder.mUserFeedback.mHolder.mSettingName.setText(mActivity.getResources().getString(R.string.F_SettingScreen_14));
		mHolder.mUserFeedback.mHolder.mSettingDescription
				.setVisibility(View.GONE);

		// evaluate sixin
		mHolder.mEvaluateSixin.mHolder.mSettingName.setText(mActivity.getResources().getString(R.string.F_SettingScreen_15));
		mHolder.mEvaluateSixin.mHolder.mSettingDescription
				.setVisibility(View.GONE);
		
		/**
		 * 人人信息绑定回调~
		 * @author liuchao
		 */
//		BindRenrenAccountScreen.setRenrenInfoOnChangeCallback(this);
		
	}

	public void initEvent() {

		// to self info page
		mHolder.mSelfSetting.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// Intent intent = new Intent(mActivity,
				// SelfInfoSettingActivity.class);
				// mActivity.startActivity(intent);
				SettingForwardActivity
						.show(mActivity,
								SettingForwardActivity.ForwardScreenType.SELFINFO_SETTING_SCREEN);
			}
		});

		// to system setting page
		mHolder.mSystemSetting.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// Intent intent = new Intent(mActivity,
				// SystemSettingActivity.class);
				// mActivity.startActivity(intent);
				SettingForwardActivity
						.show(mActivity,
								SettingForwardActivity.ForwardScreenType.SYSTEM_SETTING_SCREEN);
			}
		});

		// bind renren
		mHolder.mBindRenren.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// Intent intent = new Intent(mActivity,
				// BindRenrenAccountActivity.class);
				// mActivity.startActivity(intent);
				SettingForwardActivity
						.show(mActivity,
								SettingForwardActivity.ForwardScreenType.BIND_RENREN_ACCOUNT_SCREEN);
			}
		});

		// blacklist setting
		mHolder.mBlackListSetting.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
//				Intent intent = new Intent(mActivity, BlacklistActivtiy.class);
//				mActivity.startActivity(intent);
				SettingForwardActivity
				.show(mActivity,
						SettingForwardActivity.ForwardScreenType.BLACKLIST_SCREEN);
			}
		});

		// private setting
		mHolder.mPrivateSetting.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
//				Intent intent = new Intent(mActivity,
//						PrivateSettingActivity.class);
//				mActivity.startActivity(intent);
				SettingForwardActivity
				.show(mActivity,
						SettingForwardActivity.ForwardScreenType.PRIVATE_SETTING_SCREEN);
			}
		});

		// newuser guide
		mHolder.mNewuserGuide.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				//FunctionGuideFragmentActivity.show(mActivity);
				WelcomeActivity.showIntroduce(mActivity);
			}
		});

		// to about sixin
		mHolder.mAboutSixin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mActivity, AboutChatActivity.class);
				mActivity.startActivity(intent);
			}
		});

		// user feedback
		final ChatMessageWarpper message = ChatMessageFactory.getInstance().obtainMessage(ChatBaseItem.MESSAGE_TYPE.TEXT);
		message.mLocalUserId = LoginManager.getInstance().getLoginInfo().mUserId;
		message.mMessageContent = mActivity.getResources().getString(R.string.F_SettingScreen_20);
		message.mComefrom = ChatBaseItem.MESSAGE_COMEFROM.OUT_TO_LOCAL;
		message.mIsGroupMessage = MESSAGE_ISGROUP.IS_SINGLE;
		
		mHolder.mUserFeedback.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(!mToFeedBackDialog.isShowing()){
					mToFeedBackDialog.show();
				}
				mHolder.mLoadingText.setText(mActivity.getResources().getString(R.string.F_SettingScreen_17));
				// TODO
				ContactModel model =C_ContactsData.getInstance().getSiXinContact(SIXIN_XIAO_MI_SHU, new IDownloadContactListener() {
					
					@Override
					public void onSussess(ContactBaseModel model) {
						SystemUtil.f_log("getSiXinContact success from net");
						if(mToFeedBackDialog.isShowing()){
							mToFeedBackDialog.dismiss();
						}
						message.parseUserInfo(model);
						RenRenChatActivity.show(mActivity, model, message);
					}
					
					@Override
					public void onError() {
						SystemUtil.f_log("getSiXinContact error from net");
						if(mToFeedBackDialog.isShowing()){
							mToFeedBackDialog.dismiss();
						}
					}
					
					@Override
					public void onDowloadOver() {
					}
				});
				if(model != null){
					SystemUtil.f_log("getSiXinContact success from local");
//					message.parseUserInfo(model);
//					RenRenChatActivity.show(mActivity, model, message);
					RenRenChatActivity.show(mActivity, model);
					if(mToFeedBackDialog.isShowing()){
						mToFeedBackDialog.dismiss();
					}
				}else{
					SystemUtil.f_log("model == null");
				}
			}
		});

		// to evaluate sixin
		mHolder.mEvaluateSixin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (isMarketInstalled()) {
					mActivity.startActivity(new Intent(
							Intent.ACTION_VIEW,
							Uri.parse("market://details?id=" + RenrenChatApplication.getmContext().getPackageName())));
				} else {
					SystemUtil.toast(mActivity.getResources().getString(R.string.F_SettingScreen_16));
				}
			}
		});

		// logout
		mHolder.mSettingLogout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				createLogoutConfirmDialog().show();
				DBBasedPluginManager.pluginLoadedFlag = true;
			}
		});
	}

	/**
	 * whether install market
	 * 
	 * @return
	 */
	boolean isMarketInstalled() {
		Intent market = new Intent(Intent.ACTION_VIEW,
				Uri.parse("market://search?q=dummy"));
		PackageManager manager = mActivity.getPackageManager();
		List<ResolveInfo> list = manager.queryIntentActivities(market, 0);
		if (list.size() > 0)
			return true;
		else
			return false;
	}
	
	
	
	public void updateRenrenBind(){
		mLoginfo = LoginManager.getInstance().getLoginInfo();
		if(mLoginfo.mBindInfoRenren != null && !TextUtils.isEmpty(mLoginfo.mBindInfoRenren.mBindId)){
			mHolder.mBindRenren.mHolder.mAssociateContent.setVisibility(View.VISIBLE);
			mHolder.mBindRenren.mHolder.mAssociateContent.setText(mLoginfo.mBindInfoRenren.mBindName);
		}else{
			mHolder.mBindRenren.mHolder.mAssociateContent.setVisibility(View.GONE);
			mHolder.mBindRenren.mHolder.mAssociateContent.setText("");
		}
	}

	/** logout dialog */
	private Dialog createLogoutConfirmDialog(){
		AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
		builder.setTitle(mActivity
				.getResources()
				.getString(R.string.SettingScreen_java_1)); 
		builder.setPositiveButton(mActivity
						.getResources()
						.getString(
								R.string.VoiceOnClickListenner_java_3),
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog,
							int which) {
						mHolder.mSettingLogout.setEnabled(false);
						logout();
					}
				});
		builder.setNegativeButton(mActivity
						.getResources()
						.getString(
								R.string.ChatMessageWarpper_FlashEmotion_java_4),
				null); 
		return builder.create();
	}

	/** 退出登陆 method */
	private void logout(){
		LoginManager.getInstance().logout();
		LoginControlCenter.getInstance().gotoLogin(
				mActivity);
		LoginControlCenter.getInstance().logout(mActivity);
		McsServiceProvider.getProvider().logout(response, LoginManager.getInstance().getSsssionKey());
		finish();
	}
	
	/** 退出登陆 response */
	INetResponse response = new INetResponse() {
		
		@Override
		public void response(final INetRequest req, JsonValue obj) {
			if (obj != null && obj instanceof JsonObject) {
				final JsonObject map = (JsonObject) obj;
				if (ResponseError.noError(req, map,false)) {
					SystemUtil.f_log("logout" + map.toJsonString());
				}
			}
		}
	};
	
	@Override
	public void updateUI_Photo() {
		RenrenChatApplication.mHandler.post(new Runnable() {
			@Override
			public void run() {
				mHolder.mSelfSetting.mHolder.mHead.clear();
				mHolder.mSelfSetting.mHolder.mHead
						.addUrl(LoginManager.getInstance().getLoginInfo().mMediumUrl);
			}
		});
	}
	
	public void unRegisterPhoteUploadSuccessListener(){
		SettingDataManager.getInstance().unRegisterPhotoUploadSuccessListener(this);
		BindRenrenAccountScreen.deleteObserver(this);
	}

	@Override
	public void update() {
		RenrenChatApplication.mHandler.post(new Runnable() {
			@Override
			public void run() {
				updateRenrenBind();
			}
		});
	}
}
