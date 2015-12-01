package com.renren.mobile.chat.ui.setting;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.common.manager.LoginManager;
import com.common.manager.LoginManager.LoginInfo;
import com.common.mcs.INetRequest;
import com.common.mcs.INetResponse;
import com.common.mcs.McsServiceProvider;
import com.common.utils.RegexUtil;
import com.core.json.JsonObject;
import com.core.json.JsonValue;
import com.core.util.Md5;
import com.core.util.SystemService;
import com.core.util.ViewMapUtil;
import com.core.util.ViewMapping;
import com.renren.mobile.account.LoginControlCenter;
import com.renren.mobile.account.LoginfoModel;
import com.renren.mobile.chat.R;
import com.renren.mobile.chat.RenrenChatApplication;
import com.renren.mobile.chat.base.util.SystemUtil;
import com.renren.mobile.chat.common.ResponseError;
import com.renren.mobile.chat.ui.BaseScreen;

/**
 * @author xiangchao.fan
 * @description change pwd page screen
 */
public class ChangePwdScreen extends BaseScreen {

	public class ViewHolder{
		@ViewMapping(ID=R.id.old_pwd)
		public EditText mOldPwd;
		
		@ViewMapping(ID=R.id.new_pwd)
		public EditText mNewPwd;
		
		@ViewMapping(ID=R.id.check_new_pwd)
		public EditText mCheckNewPwd;
		
		@ViewMapping(ID=R.id.btn_change_pwd)
		public Button mConfirm;
		
		@ViewMapping(ID=R.id.loading_change_pwd)
		public View mLoading;
		
		@ViewMapping(ID=R.id.loading_text)
		public TextView mLoadingText;
		
	}
	
	private View mChangePwdView;
	private ViewHolder mHolder;
	
	private String mOldPwd;
	private String mNewPwd;
	private String mCheckPwd;
	
	private Dialog mDialog;
	
	public ChangePwdScreen(Activity activity) {
		super(activity);
		mChangePwdView = SystemService.sInflaterManager.inflate(R.layout.f_change_pwd, null);
		setContent(mChangePwdView);
		
		SystemService.sInputMethodManager.showSoftInput(mChangePwdView, 0);
		
		initTitle();
		initView();
		initEvent();
	}
	
	private void initTitle() {
		this.getTitle().setTitleMiddle(mActivity.getResources().getString(R.string.ChangePwdScreen_1));
		this.getTitle().getTitleLeft().setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
	
	private void initView(){
		mHolder = new ViewHolder();
		ViewMapUtil.getUtil().viewMapping(mHolder, mChangePwdView);
		
		mDialog = SettingDataManager.getInstance().createLoadingDialog(mActivity, mActivity.getResources().getString(R.string.f_change_pwd_5));
		
		//mHolder.mLoadingText.setText(mActivity.getResources().getString(R.string.f_change_pwd_5));
	}
	
	public void initEvent(){
		mHolder.mConfirm.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				SystemService.sInputMethodManager.hideSoftInputFromInputMethod(mHolder.mConfirm.getWindowToken(), 0);
				mOldPwd = mHolder.mOldPwd.getText().toString();
				mNewPwd = mHolder.mNewPwd.getText().toString();
				mCheckPwd = mHolder.mCheckNewPwd.getText().toString();
				
				SystemUtil.f_log("Md5.toMD5(mOldPwd).equals(LoginManager.getInstance().getLoginInfo().mPassword):" + Md5.toMD5(mOldPwd).equals(LoginManager.getInstance().getLoginInfo().mPassword));
				if(TextUtils.isEmpty(mOldPwd.trim())){// || !Md5.toMD5(mOldPwd).equals(LoginManager.getInstance().getLoginInfo().mPassword)){
					SystemUtil.toast(mActivity.getResources().getString(R.string.ChangePwdScreen_3));
					return;
				}else if(!RegexUtil.isPassword(mOldPwd)){
					SystemUtil.toast(mActivity.getResources().getString(R.string.ChangePwdScreen_2));
					return;
				}else if(TextUtils.isEmpty(mNewPwd.trim())){
					SystemUtil.toast(mActivity.getResources().getString(R.string.ChangePwdScreen_7));
					return;
				}else if(!RegexUtil.isPassword(mNewPwd)){
					SystemUtil.toast(mActivity.getResources().getString(R.string.ChangePwdScreen_2));
					return;
				}else if(TextUtils.isEmpty(mCheckPwd.trim())){
					SystemUtil.toast(mActivity.getResources().getString(R.string.ChangePwdScreen_7));
					return;
				}else if (!mCheckPwd.equals(mNewPwd)) {
					SystemUtil.f_log("mHolder.mCheckNewPwd:" + mCheckPwd.equals(mNewPwd));
					SystemUtil.toast(mActivity.getResources().getString(R.string.ChangePwdScreen_4));
					return;
				}
				//mHolder.mLoading.setVisibility(View.VISIBLE);
				mHolder.mConfirm.setEnabled(false);//不可点击
				mDialog.show();
				//Md5.toMD5(s);
				McsServiceProvider.getProvider().modifyPassword(response,
						Md5.toMD5(mOldPwd),
						Md5.toMD5(mNewPwd),
						LoginManager.getInstance().getSsssionKey());
			}
		});
	}
	
	INetResponse response = new INetResponse() {
		
		@Override
		public void response(final INetRequest req, JsonValue obj) {
			if (obj != null && obj instanceof JsonObject) {
				final JsonObject map = (JsonObject) obj;
				RenrenChatApplication.mHandler.post(new Runnable() {
					@Override
					public void run() {
						if (ResponseError.noError(req, map,true)) {
							SystemUtil.f_log("" + map.toJsonString());
							String sessionKey = map.getString("session_key");
							if(sessionKey != null){
								AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
								builder.setTitle("修改密码成功"); 
								builder.setPositiveButton(mActivity
												.getResources()
												.getString(
														R.string.VoiceOnClickListenner_java_3,null),
										new DialogInterface.OnClickListener() {
											@Override
											public void onClick(DialogInterface dialog,
													int which) {
												finish();
											}
										});
								builder.create().show();
//								McsServiceProvider.getProvider().setSessionKey(sessionKey);	
								// update database
								updatePwd(sessionKey);
								mHolder.mLoading.setVisibility(View.GONE);
							}
						}
						//mHolder.mLoading.setVisibility(View.GONE);
						mHolder.mConfirm.setEnabled(true);//可点击
						mDialog.dismiss();
					}
				});
			}
		}
	};

	/** update local database user pwd */
	private void updatePwd(String sessionKey){
		LoginInfo info = LoginManager.getInstance().getLoginInfo();
		LoginfoModel Loginfo = new LoginfoModel();
		
		info.mPassword = Md5.toMD5(mNewPwd);
		info.mSessionKey = sessionKey;
		Loginfo.parse(info);
		LoginControlCenter.getInstance().updateUserData(Loginfo);
	}

}
