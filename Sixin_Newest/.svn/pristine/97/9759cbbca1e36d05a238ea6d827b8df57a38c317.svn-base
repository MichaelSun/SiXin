package com.renren.mobile.chat.ui.account;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.common.manager.LoginManager;
import com.common.manager.RegisterManager;
import com.common.mcs.INetReponseAdapter;
import com.common.mcs.INetRequest;
import com.common.mcs.McsServiceProvider;
import com.common.utils.RegexUtil;
import com.core.json.JsonObject;
import com.core.util.CommonUtil;
import com.renren.mobile.chat.R;
import com.renren.mobile.chat.RenrenChatApplication;
import com.renren.mobile.chat.base.util.SystemUtil;
import com.renren.mobile.chat.common.ResponseError;
import com.renren.mobile.chat.ui.BaseActivity;
import com.renren.mobile.chat.ui.contact.ContactOptionActivity;
import com.renren.mobile.chat.ui.guide.WelcomeActivity;
import com.renren.mobile.chat.ui.setting.SettingForwardActivity;
import com.renren.mobile.chat.view.BaseTitleLayout;
import com.renren.mobile.chat.webview.RenRenWapUrlFactory;
import com.renren.mobile.chat.webview.RenRenWebView;

public class RegisterAccountActivity extends BaseActivity {
	
	public static final String FLAG_FINISH = "flag_finish";

	public static final String ACCOUNT = "account";
	
	public static final String FLAG_TYPE = "flag_type";
	
	public static final int FLAG_FIND_SECRET = 0;
	
	public static final int FLAG_REGISTER = 1;
	
	public static final int FLAG_BIND_EMAIL = 2;
	
	public static final int FLAG_BIND_PHONE = 3;
	
	/**
	 * @author kaining.yang
	 * 0：找回密码 验证帐号
	 * 1：注册私信
	 * 2：绑定邮箱
	 * 3：绑定手机号
	 */
	private int mFlagType;
	
	private boolean mFlagFinish;
	
	private TextView mGuidance;
	
	private EditText mPhoneOrEmail;
	
	private Button mBtnSubmit;
	
	private TextView mHint;
	
	private CheckBox mAgree;
	
	private LinearLayout mCheckArea;
	
	private TextView mProtocol;
	
	private String strUser;
	
	private LinearLayout mTitle;
	
	private Dialog mDialog;
	
	private Context mContext;
	
	private BaseTitleLayout mBaseTitleLayout;
	
	public static Activity sActivity = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.setContentView(R.layout.ykn_register_account);
		mContext = this;
		mFlagFinish = getIntent().getBooleanExtra(FLAG_FINISH, false);
		if(sActivity != null) RenrenChatApplication.removeActivity(sActivity);
		sActivity = this;
		RenrenChatApplication.pushStack(this);
		initViews();
	}
	
	public static void show(Context context, int flagtype){
    	Intent intent = new Intent(context, RegisterAccountActivity.class);
    	intent.putExtra(FLAG_TYPE, flagtype);
    	context.startActivity(intent);
    }
	
	public static void show(Context context, int flagtype, boolean flagfinish) {
		Intent intent = new Intent(context, RegisterAccountActivity.class);
		intent.putExtra(FLAG_TYPE, flagtype);
		intent.putExtra(FLAG_FINISH, flagfinish);
    	context.startActivity(intent);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		RenrenChatApplication.popStack(this);
	}

	private void initViews() {
		mTitle = (LinearLayout) this.findViewById(R.id.title);
    	mPhoneOrEmail = (EditText) this.findViewById(R.id.et_phone_or_email);
    	mBtnSubmit = (Button) this.findViewById(R.id.btn_submit);
    	mGuidance = (TextView) this.findViewById(R.id.tv_guidance);
    	mAgree = (CheckBox) this.findViewById(R.id.agree);
    	mCheckArea = (LinearLayout) this.findViewById(R.id.check_area);
    	mProtocol = (TextView) this.findViewById(R.id.protocol);
    	mHint = (TextView) this.findViewById(R.id.tv_hint);
    	
    	mProtocol.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				RenRenWebView.show(RegisterAccountActivity.this, "", getString(R.string.ykn_account_hint2), RenRenWapUrlFactory.URL_TYPE.REGISTER_PROTOCAL);
			}
		});
    	
    	mDialog = new Dialog(this, R.style.sendRequestDialog);
		mDialog.setContentView(R.layout.ykn_dialog);
		TextView mDialogText = (TextView) mDialog.findViewById(R.id.message);
		mDialogText.setText(R.string.ykn_submitting);
		mDialog.setOnCancelListener(new OnCancelListener() {
			
			@Override
			public void onCancel(DialogInterface dialog) {
				
			}
		});
    	
    	// title
    	mBaseTitleLayout = new BaseTitleLayout(this);
    	mTitle.addView(mBaseTitleLayout.getView(), 
    			new LinearLayout.LayoutParams(
    					LayoutParams.FILL_PARENT,
    					LayoutParams.FILL_PARENT, 1));
    	// title back
    	mBaseTitleLayout.getTitleLeft().setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				jumpBack();
			}
		});
    	
    	mFlagType = getIntent().getExtras().getInt(FLAG_TYPE, FLAG_REGISTER);
    	
    	switch (mFlagType) {
		case FLAG_FIND_SECRET:
			initViewsFindSecret();
			break;
		case FLAG_REGISTER:
			initViewsRegister();
			break;
		case FLAG_BIND_EMAIL:
			initViewsBindEmail();
			break;
		case FLAG_BIND_PHONE:
			initViewsBindPhone();
			break;
		default:
			
			break;
		}
    	
		// 获取验证码
		mBtnSubmit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				strUser = mPhoneOrEmail.getText().toString();
				// 是否同意隐私协议
				if (mAgree.isChecked()) {
					getCaptcha();
				} else {
					showDialog();
				}
			}
		});
		
		mCheckArea.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (mAgree.isChecked()) {
					mAgree.setChecked(false);
				} else {
					mAgree.setChecked(true);
				}
			}
		});
    }
	
	private void showDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(RegisterAccountActivity.this);
		builder.setMessage(getString(R.string.ykn_account_message));
		builder.setNegativeButton(R.string.ykn_account_confirm, null);
		builder.show();
	}
	
	private void disableSubmit() {
		mBtnSubmit.setClickable(false);
		mBtnSubmit.setTextColor(mContext.getResources().getColor(R.color.Color_D));
		mBtnSubmit.setBackgroundResource(R.drawable.ykn_button_disable);
	}
	
	private void undisableSubmit() {
		RenrenChatApplication.sHandler.post(new Runnable() {
			
			@Override
			public void run() {
				mBtnSubmit.setClickable(true);
				mBtnSubmit.setTextColor(mContext.getResources().getColor(R.color.Color_C));
				mBtnSubmit.setBackgroundResource(R.drawable.ykn_button_background_blue);
			}
		});
	}
	
	private void initViewsFindSecret() {
		mGuidance.setText(R.string.ykn_account_findsecret_guidance);
    	mPhoneOrEmail.setHint(R.string.ykn_account_register_edittext);
    	
    	// title
    	mBaseTitleLayout.setTitleMiddle(getString(R.string.ykn_account_register_title));
    }
    
    private void initViewsRegister() {
    	mCheckArea.setVisibility(View.VISIBLE);
    	mGuidance.setText(R.string.ykn_account_register_guidance);
    	mPhoneOrEmail.setHint(R.string.ykn_account_register_edittext);
    	
    	// title
    	mBaseTitleLayout.setTitleMiddle(getString(R.string.ykn_account_register_title));
    }
    
    private void initViewsBindEmail() {
    	mHint.setVisibility(View.VISIBLE);
    	mHint.setText(R.string.ykn_bindinfo_email_hint);
    	mGuidance.setText(R.string.ykn_account_email_guidance);
    	mPhoneOrEmail.setHint(R.string.ykn_account_email_edittext);
    	
    	// title
    	mBaseTitleLayout.setTitleMiddle(getString(R.string.ykn_account_email_title));
    }
    
    private void initViewsBindPhone() {
    	mHint.setVisibility(View.VISIBLE);
    	mHint.setText(R.string.ykn_bindinfo_phone_hint);
    	mGuidance.setText(R.string.ykn_account_phone_guidance);
    	mPhoneOrEmail.setHint(R.string.ykn_account_phone_edittext);
    	
    	// title
    	mBaseTitleLayout.setTitleMiddle(getString(R.string.ykn_account_phone_title));
    }
	
	// 返回跳转页面
	private void jumpBack() {
		switch (mFlagType) {
		case FLAG_REGISTER:
			WelcomeActivity.show(RegisterAccountActivity.this);
			RegisterAccountActivity.this.finish();
			break;
		case FLAG_BIND_PHONE:
			/*if (mFlagFinish) {
				ContactOptionActivity.show(RegisterAccountActivity.this);
				RegisterAccountActivity.this.finish();
				return;
			}*/
			if (TextUtils.isEmpty(LoginManager.getInstance().getLoginInfo().mBindInfoMobile.mBindId)) {
				if (mFlagFinish) {
					ContactOptionActivity.show(RegisterAccountActivity.this);
					RegisterAccountActivity.this.finish();
				} else {
					SettingForwardActivity.show(RegisterAccountActivity.this, SettingForwardActivity.ForwardScreenType.SELFINFO_SETTING_SCREEN);
					RegisterAccountActivity.this.finish();
				}
			} else {
				BindInfoActivity.show(this, LoginManager.getInstance().getLoginInfo().mBindInfoMobile.mBindId, FLAG_BIND_PHONE, mFlagFinish);
				RegisterAccountActivity.this.finish();
			}
			break;
		case FLAG_BIND_EMAIL:
			/*if (mFlagFinish) {
				ContactOptionActivity.show(RegisterAccountActivity.this);
				RegisterAccountActivity.this.finish();
				return;
			}*/
			if (TextUtils.isEmpty(LoginManager.getInstance().getLoginInfo().mBindInfoEmail.mBindId)) {
				if (mFlagFinish) {
					ContactOptionActivity.show(RegisterAccountActivity.this);
					RegisterAccountActivity.this.finish();
				} else {
					SettingForwardActivity.show(RegisterAccountActivity.this, SettingForwardActivity.ForwardScreenType.SELFINFO_SETTING_SCREEN);
					RegisterAccountActivity.this.finish();
				}
			} else {
				BindInfoActivity.show(this, LoginManager.getInstance().getLoginInfo().mBindInfoEmail.mBindId, FLAG_BIND_EMAIL, mFlagFinish);
				RegisterAccountActivity.this.finish();
			}
			// SettingForwardActivity.show(RegisterAccountActivity.this, SettingForwardActivity.ForwardScreenType.SELFINFO_SETTING_SCREEN);
			// RegisterAccountActivity.this.finish();
			break;
		case FLAG_FIND_SECRET:
			RegisterAccountActivity.this.finish();
			break;
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			SystemUtil.logykn("捕捉到返回按钮事件！");
			jumpBack();
		}
		return true;
	}
	
	/**
	 * @author kaining.yang
	 * 不同页面的业务逻辑在这里，主要为调不同的请求
	 * 获取验证码
	 */
	private void getCaptcha() {
		
		if (TextUtils.isEmpty(strUser)) {
			CommonUtil.toast(R.string.ykn_account_toast1);
			return;
		}
		
		// 发送获取验证码请求前要用正则检查 帐号 是否合法（手机号、邮箱）
		switch (mFlagType) {
		case FLAG_FIND_SECRET:
			if (RegexUtil.isMobilePhone(strUser) || RegexUtil.isEmailAddress(strUser)) {
				showSubmitDialog();
				disableSubmit();
				RegisterManager.getRegisterManager().getForgetPwdIdentifyCode(strUser.toLowerCase(), new responseForgetCaptcha());
				break;
			} else {
				// 注册帐号不合法
				CommonUtil.toast(R.string.ykn_account_toast2);
				break;
			}
		case FLAG_REGISTER:
			if (RegexUtil.isMobilePhone(strUser) || RegexUtil.isEmailAddress(strUser)) {
				showSubmitDialog();
				disableSubmit();
				McsServiceProvider.getProvider().getCaptcha(strUser.toLowerCase(), "GET", new responseRegisterCaptcha());
				break;
			} else {
				// 注册帐号不合法
				CommonUtil.toast(R.string.ykn_account_toast2);
				break;
			}
		case FLAG_BIND_EMAIL: // 绑定邮箱和手机号接口一致
			if (RegexUtil.isEmailAddress(strUser)) {
				showSubmitDialog();
				disableSubmit();
				McsServiceProvider.getProvider().getCaptchaBinding(strUser.toLowerCase(), "GET", new responseBindingCaptcha());
				break;
			} else {
				// 注册帐号不合法
				CommonUtil.toast(R.string.ykn_account_toast3);
				break;
			}
		case FLAG_BIND_PHONE:
			if (RegexUtil.isMobilePhone(strUser)) {
				showSubmitDialog();
				disableSubmit();
				McsServiceProvider.getProvider().getCaptchaBinding(strUser.toLowerCase(), "GET", new responseBindingCaptcha());
				break;
			} else {
				// 注册帐号不合法
				CommonUtil.toast(R.string.ykn_account_toast4);
				break;
			}
    	}
		
	}
	
	private void showSubmitDialog() {
		if (!mDialog.isShowing()) {
			mDialog.show();
		}
	}
	
	private void dismissSubmitDialog() {
		if (mDialog.isShowing()) {
			mDialog.dismiss();
		}
	}
	
	class responseForgetCaptcha extends INetReponseAdapter{
    	
    	public responseForgetCaptcha() {
    		
    	}
		
		@Override
		public void onSuccess(INetRequest req, JsonObject data) {
			SystemUtil.log("sunnyykn", "获取验证码成功:"+data);
			//CommonUtil.toast(R.string.ykn_account_toast5);
			SystemUtil.log("sunnyykn", "验证码:"+data);
			
			dismissSubmitDialog();
			undisableSubmit();
			
			RegisterCaptchaActivity.show(RegisterAccountActivity.this, FLAG_FIND_SECRET, strUser);
		}
		
		@Override
		public void onError(INetRequest req, JsonObject data) {
			SystemUtil.log("sunnyykn", "获取验证码失败信息:"+data);
			int error_code = (int) data.getNum("error_code");
			if (12005==error_code ){
				CommonUtil.toast(R.string.ykn_account_toast7);
			}else{
				//CommonUtil.toast(R.string.ykn_account_toast6);
				ResponseError.showError(data);
			}
			dismissSubmitDialog();
			undisableSubmit();
		}
	}
	
	class responseRegisterCaptcha extends INetReponseAdapter{
    	
    	public responseRegisterCaptcha() {
    		
    	}
		
		@Override
		public void onSuccess(INetRequest req, JsonObject data) {
			SystemUtil.log("sunnyykn", "获取验证码成功:"+data);
			//CommonUtil.toast(R.string.ykn_account_toast5);
			SystemUtil.log("sunnyykn", "验证码:"+data);
			
			dismissSubmitDialog();
			undisableSubmit();
			
			RegisterCaptchaActivity.show(RegisterAccountActivity.this, FLAG_REGISTER, strUser);
		}
		
		@Override
		public void onError(INetRequest req, JsonObject data) {
			
			dismissSubmitDialog();
			undisableSubmit();
			
			if (data.getNum("error_code") == 12004) {
				LoginSixinActivity.show(RegisterAccountActivity.this, LoginSixinActivity.LOGIN_REGISTERED, strUser);
				// RegisterAccountActivity.this.finish();
			} else {
				SystemUtil.log("sunnyykn", "获取验证码失败信息:"+data);
				//CommonUtil.toast(R.string.ykn_account_toast6);
				ResponseError.showError(data);
			}
		}
	}
	
	class responseBindingCaptcha extends INetReponseAdapter {
		
		@Override
		public void onSuccess(INetRequest req, JsonObject data) {
			SystemUtil.log("sunnyykn", "获取验证码成功:"+data);
			//CommonUtil.toast(R.string.ykn_account_toast5);
			SystemUtil.log("sunnyykn", "验证码:"+data);
			
			dismissSubmitDialog();
			undisableSubmit();
			
			RegisterCaptchaActivity.show(RegisterAccountActivity.this, mFlagType, strUser, mFlagFinish);
		}
		
		@Override
		public void onError(INetRequest req, JsonObject data) {
			SystemUtil.log("sunnyykn", "获取验证码失败信息:"+data);
			//CommonUtil.toast(R.string.ykn_account_toast6);
			ResponseError.showError(data);
			dismissSubmitDialog();
			undisableSubmit();
		}
	}
}
