package com.renren.mobile.chat.ui.account;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.common.manager.LoginManager;
import com.common.mcs.INetRequest;
import com.common.mcs.INetResponse;
import com.common.mcs.McsServiceProvider;
import com.common.utils.Methods;
import com.common.utils.RegexUtil;
import com.core.json.JsonObject;
import com.core.json.JsonValue;
import com.core.util.Md5;
import com.renren.mobile.account.LoginControlCenter;
import com.renren.mobile.chat.R;
import com.renren.mobile.chat.RenrenChatApplication;
import com.renren.mobile.chat.base.util.SystemUtil;
import com.renren.mobile.chat.common.ResponseError;
import com.renren.mobile.chat.ui.BaseActivity;
import com.renren.mobile.chat.ui.MainFragmentActivity;
import com.renren.mobile.chat.ui.guide.WelcomeActivity;
import com.renren.mobile.chat.view.BaseTitleLayout;

/**
 * @author kaining.yang
 */

public class LoginSixinActivity extends BaseActivity {
	
	public static final String LOGIN_ACCOUNT = "login_account";
	
	public static final String LOGIN_PASSWORD = "login_password";
	
	public static final String LOGIN_TYPE = "login_type";
	
	public static final int LOGIN_SIXIN = 0;
	
	public static final int LOGIN_RENREN = 1;
	
	public static final int LOGIN_REGISTERED = 2; // 用户注册时发现用户名已注册，直接调到登陆页面
	
	public static final int LOGIN_THIRD = 3; // 官方互调的一个特殊情况
	
	public static final int CAPTCHA_NEEDED = 1;
	
	public static final int CAPTCHA_NOT_NEEDED = 0;
	
	private int mFlag;
	
	private TextView tvLogin;
	
	private FrameLayout mUsernameLayout;
	
	private AutoCompleteTextView mUsername;
	
	private EditText mPassword;
	
	private ImageView mUsernameDel;
	
	private ImageView mPasswordDel;

	private LinearLayout mCaptchaLayout;
	
	private EditText mCaptcha;
	
	private ImageView mCaptchaImg;
	
	private Button mLogin;
	
	private LinearLayout mForget;
	
	private TextView mHint;
	
	private LinearLayout mTitle;
	
	private BaseTitleLayout mBaseTitleLayout;
	
	private long mCurrentSession;
	
	private LoginStatusListener mLoginStatusListener = null; 
	
	private Dialog mDialog;
	
	private String mUserAccount;
	
	private String mUserPassword;
	
	//private static Activity sActivity = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/*if(sActivity != null) RenrenChatApplication.removeActivity(sActivity);
		sActivity = this;*/
		RenrenChatApplication.pushStack(this);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.ykn_login_sixin);
		mFlag = getIntent().getExtras().getInt(LOGIN_TYPE, LOGIN_SIXIN);
		mUserAccount = getIntent().getStringExtra(LOGIN_ACCOUNT);
		mUserPassword = getIntent().getStringExtra(LOGIN_PASSWORD);
		initViews();
	}
	
	public static void show(Context context, int loginType) {
		show(context, loginType, "", "");
	}
	
	public static void show(Context context, int loginType, String account) {
		show(context, loginType, account, "");
	}
	
	public static void show(Context context, int loginType, String account, String password) {
		Intent intent = new Intent(context, LoginSixinActivity.class);
		intent.putExtra(LOGIN_TYPE, loginType);
		intent.putExtra(LOGIN_ACCOUNT, account);
		intent.putExtra(LOGIN_PASSWORD, password);
		context.startActivity(intent);
	}
	
	private void initViews() {
		mTitle = (LinearLayout) this.findViewById(R.id.title);
		tvLogin = (TextView) findViewById(R.id.tv_login);
		mUsernameLayout = (FrameLayout) findViewById(R.id.username_layout);
		mUsername = (AutoCompleteTextView) findViewById(R.id.et_username);
		mPassword = (EditText) findViewById(R.id.et_password);
		
		// del
		mUsernameDel = (ImageView) findViewById(R.id.iv_username_del_icon);
		mPasswordDel = (ImageView) findViewById(R.id.iv_password_del_icon);
		
		mCaptchaLayout = (LinearLayout) findViewById(R.id.captcha);
		mCaptcha = (EditText) findViewById(R.id.et_captcha);
		mCaptchaImg = (ImageView) findViewById(R.id.img_captcha);
		mLogin = (Button) findViewById(R.id.btn_login);
		mForget = (LinearLayout) findViewById(R.id.tv_forget_secret);
		mHint = (TextView) findViewById(R.id.hint);
		
		refreshCaptcha();
		
		// 设置验证码点击事件
		mCaptchaImg.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				refreshCaptcha();
				mCaptchaImg.setVisibility(View.INVISIBLE);
			}
		});
		
		// title
    	mBaseTitleLayout = new BaseTitleLayout(this);
    	mTitle.addView(mBaseTitleLayout.getView(), 
    			new LinearLayout.LayoutParams(
    					LayoutParams.FILL_PARENT,
    					LayoutParams.FILL_PARENT, 1));
		
		switch (mFlag) {
		case LOGIN_SIXIN:
			initViewSixin();
			break;

		case LOGIN_RENREN:
			initViewRenren();
			break;
			
		case LOGIN_REGISTERED:
			initViewRegister();
			break;
			
		case LOGIN_THIRD:
			initViewThird();
			break;
		}
		mUsername.addTextChangedListener(new AccountWatcher());
		mPassword.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				if (TextUtils.isEmpty(mPassword.getText().toString())) {
					mPasswordDel.setVisibility(View.GONE);
				}else{
					mPasswordDel.setVisibility(View.VISIBLE);
				}
			}
		});
		mUsernameDel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mUsername.setText("");
				mPassword.setText("");
			}
		});
		mPasswordDel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mPassword.setText("");
			}
		});
		initUI();
		login();
	}
	
	private void initViewSixin() {
		tvLogin.setVisibility(View.GONE);
		mUsername.setHint(R.string.ykn_login_sixin_username);
		mPassword.setHint(R.string.ykn_login_sixin_password);
		mLogin.setText(R.string.ykn_login_sixin_login);
		
		initUserAccountAndPassword();
		
		// title
    	mBaseTitleLayout.setTitleMiddle(getString(R.string.ykn_login_sixin_title));
    	mBaseTitleLayout.getTitleLeft().setOnClickListener(new OnClickListener() {
    			
			@Override
			public void onClick(View v) {
				WelcomeActivity.show(LoginSixinActivity.this);
				LoginSixinActivity.this.finish();
			}
		});
		
	}
	
	private void initViewRenren() {
		tvLogin.setVisibility(View.GONE);
		//mHint.setVisibility(View.VISIBLE);
		//mHint.setText(R.string.ykn_login_renren_hint);
		mUsername.setHint(R.string.ykn_login_renren_username);
		mPassword.setHint(R.string.ykn_login_sixin_password);
		mLogin.setText(R.string.ykn_login_sixin_login);
		mForget.setVisibility(View.GONE);
		
		initUserAccountAndPassword();
		
		// title
    	mBaseTitleLayout.setTitleMiddle(getString(R.string.ykn_login_renren_title));
    	mBaseTitleLayout.getMidTitleLayout().setOrientation(LinearLayout.HORIZONTAL);
    	ImageView logo = new ImageView(LoginSixinActivity.this);
    	logo.setImageResource(R.drawable.ykn_renren_logo);
    	mBaseTitleLayout.getMidTitleLayout().addView(logo, 0);
    	mBaseTitleLayout.getTitleLeft().setOnClickListener(new OnClickListener() {
    			
			@Override
			public void onClick(View v) {
				WelcomeActivity.show(LoginSixinActivity.this);
				LoginSixinActivity.this.finish();
			}
		});
	}
	
	private void initViewRegister() {
		StringBuffer guidance = new StringBuffer();
		if (RegexUtil.isMobilePhone(mUserAccount)) {
			guidance.append(getString(R.string.ykn_login_sixin_registered_guidance1));
		} else {
			guidance.append(getString(R.string.ykn_login_sixin_registered_guidance2));
		}
		guidance.append(mUserAccount);
		guidance.append(getString(R.string.ykn_login_sixin_registered_guidance3));
		
		tvLogin.setText(guidance.toString());
		tvLogin.setVisibility(View.VISIBLE);
		mUsernameLayout.setVisibility(View.GONE);
		mPassword.setHint(R.string.ykn_login_sixin_password);
		mLogin.setText(R.string.ykn_login_sixin_login);
		mForget.setVisibility(View.GONE);
		
		//initUserAccountAndPassword();
		
		// title
    	mBaseTitleLayout.setTitleMiddle(getString(R.string.ykn_login_sixin_title));
    	mBaseTitleLayout.getTitleLeft().setOnClickListener(new OnClickListener() {
    			
			@Override
			public void onClick(View v) {
				LoginSixinActivity.this.finish();
			}
		});
	}
	
	private void initViewThird() {
		tvLogin.setVisibility(View.GONE);
		mHint.setVisibility(View.VISIBLE);
		mUsername.setHint(R.string.ykn_login_sixin_username);
		mPassword.setHint(R.string.ykn_login_sixin_password);
		mLogin.setText(R.string.ykn_login_sixin_login);
		
		initUserAccountAndPassword();
		
		// title
    	mBaseTitleLayout.setTitleMiddle(getString(R.string.ykn_login_sixin_title));
    	mBaseTitleLayout.setTitleButtonLeftOtherVisibility(true);
    	mBaseTitleLayout.setTitleButtonLeftOtherRenren();
    	mBaseTitleLayout.setTitleButtonLeftOtherListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				RenrenChatApplication.onThirdCallExit();
				LoginSixinActivity.this.finish();
			}
		});
	}
	
	private void initUserAccountAndPassword() {
		if (!TextUtils.isEmpty(mUserAccount)) {
			mUsername.setText(mUserAccount);
			mUsernameDel.setVisibility(View.VISIBLE);
		}
		SystemUtil.logykn("密码", mUserPassword);
		if (!TextUtils.isEmpty(mUserPassword)) {
			mPassword.setText(mUserPassword);
			
			mUsernameDel.setVisibility(View.GONE);
			mPasswordDel.setVisibility(View.GONE);
			
			mUsername.setEnabled(false);
			mPassword.setEnabled(false);
		}
	}
	
	private void refreshCaptcha() {
		if (LoginManager.getInstance().getLoginInfo().mCaptchaNeeded == CAPTCHA_NEEDED) {
			mCaptchaLayout.setVisibility(View.VISIBLE);
			setImageView(mCaptchaImg, LoginManager.getInstance().getLoginInfo().mCaptchaUrl);
		}
		
		/*
		mCaptchaImg.clear();
		mCaptchaImg.setIsLayout(false);
		mCaptchaImg.addUrl(LoginManager.getInstance().getLoginInfo().mCaptchaUrl);
		SystemUtil.logykn"mCaptchaUrl:" + LoginManager.getInstance().getLoginInfo().mCaptchaUrl);
		*/
		
		// 测试 跳转到大图浏览看验证码
		/*Intent intent = new Intent(LoginSixinActivity.this, ImageViewActivity.class);
		intent.putExtra(ImageViewActivity.NEED_PARAM.REQUEST_CODE,
				ImageViewActivity.VIEW_LARGE_IMAGE);
		intent.putExtra("url", LoginManager.getInstance().getLoginInfo().mCaptchaUrl);
		startActivity(intent);*/
	}
	
	private String getUsername() {
		String username = "";
		switch (mFlag) {
		case LOGIN_SIXIN:
		case LOGIN_RENREN:
			username = mUsername.getText().toString().trim();
			break;
		case LOGIN_REGISTERED:
			username = mUserAccount;
			break;
		case LOGIN_THIRD:
			username = mUserAccount;
			break;
		}
		return username;
	}
	
	/*private void disableLogin() {
		mLogin.setClickable(false);
	}
	
	private void undisableLogin() {
		mLogin.setClickable(true);
	}*/
	
	private String getPassword() {
		if (!TextUtils.isEmpty(mUserPassword)) {
			return mUserPassword;
		} else {
			if (TextUtils.isEmpty(mPassword.getText().toString().trim())) {
				return "";
			} else {
				return Md5.toMD5(mPassword.getText().toString().trim());
			}
		}
	}
	
	private void initUI() {
		mLogin.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				mCurrentSession = System.currentTimeMillis();
				LoginManager.getInstance().getLoginInfo().mAccount = getUsername();
				LoginManager.getInstance().getLoginInfo().mPassword = getPassword();
				if (TextUtils.isEmpty(LoginManager.getInstance().getLoginInfo().mAccount)) {
					Toast.makeText(LoginSixinActivity.this, RenrenChatApplication.getmContext().getResources().getString(R.string.Login_java_1), Toast.LENGTH_LONG).show(); 
					return;
				}
				if (mFlag == LOGIN_SIXIN) {
					if (!RegexUtil.isMobilePhone(LoginManager.getInstance().getLoginInfo().mAccount) && !RegexUtil.isEmailAddress(LoginManager.getInstance().getLoginInfo().mAccount)) {
						Toast.makeText(LoginSixinActivity.this, RenrenChatApplication.getmContext().getResources().getString(R.string.Login_java_3), Toast.LENGTH_LONG).show(); 
						return;
					}
				}
				if (TextUtils.isEmpty(LoginManager.getInstance().getLoginInfo().mPassword)) {
					Toast.makeText(LoginSixinActivity.this, RenrenChatApplication.getmContext().getResources().getString(R.string.Login_java_2), Toast.LENGTH_LONG).show();	
					return;
				}
				showDialog();
				//disableLogin();
				SystemUtil.logykn("登陆验证码", mCaptcha.getText().toString());
				/**
				 * kaining.yang
				 */
				if (mFlag != LOGIN_RENREN) {
					LoginControlCenter.getInstance().login(
							LOGIN_SIXIN,
							LoginManager.getInstance().getLoginInfo().mAccount.toLowerCase(),
							LoginManager.getInstance().getLoginInfo().mPassword, 
							mCaptcha.getText().toString(),
							LoginSixinActivity.this, 
							mLoginStatusListener,
							mCurrentSession);
				} else {
					LoginControlCenter.getInstance().login(
							LOGIN_RENREN,
							LoginManager.getInstance().getLoginInfo().mAccount.toLowerCase(),
							LoginManager.getInstance().getLoginInfo().mPassword, 
							mCaptcha.getText().toString(),
							LoginSixinActivity.this, 
							mLoginStatusListener,
							mCurrentSession);
				}
				
			}
		});
		
		mDialog = new Dialog(this, R.style.logindialog);
		mDialog.setContentView(R.layout.y_login_dialog);
		mDialog.setOnCancelListener(new OnCancelListener() {
			public void onCancel(DialogInterface dialog) {
				mCurrentSession=0l;
			}
		});
		
		mForget.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				RegisterAccountActivity.show(LoginSixinActivity.this, RegisterAccountActivity.FLAG_FIND_SECRET);
			}
		});
	}
	
	private void showDialog() {
		if(!mDialog.isShowing()){
			mDialog.show();
		}
	}
	
	private void dismissDialog() {
		if(mDialog.isShowing()){
			mDialog.dismiss();
		}
	}
	
	/** 
	 * 下载验证码并加载到imageview里 
	 * */
	private void setImageView(final ImageView imageView, final String url) {

		INetResponse response = new INetResponse() {
			public void response(INetRequest req, JsonValue obj) {

				if (obj instanceof JsonObject) {
					JsonObject map = (JsonObject) obj;
					if (ResponseError.noError(req, map)) {
						byte[] imgData = map.getBytes(IMG_DATA);
						if (imgData != null) {
							final Bitmap img = Methods.createImage(imgData);
							if (img != null) {
								runOnUiThread(new Runnable() {

									@Override
									public void run() {
										imageView.setImageBitmap(img);
										imageView.setVisibility(View.VISIBLE);
									}
								});
							}
						}
					}
				}
			}
		};
		if (url != null) {
			McsServiceProvider.getProvider().getImage(url, response);
		}
	}
	
	private void login() {
		if(TextUtils.isEmpty(LoginManager.getInstance().getLoginInfo().mLastAccount)) {
			// 从数据库读取
			if (LoginControlCenter.getInstance().loadLastLoginUserData() != null) {
				LoginManager.getInstance().getLoginInfo().mLastAccount = LoginControlCenter.getInstance().loadLastLoginUserData().mAccount;
			}
		}
		if(TextUtils.isEmpty(mUserAccount) && LoginManager.getInstance().getLoginInfo().mLastAccount != null){
			mUsername.setText(LoginManager.getInstance().getLoginInfo().mLastAccount);
			mUsername.setSelection(LoginManager.getInstance().getLoginInfo().mLastAccount.length());
		}
		ArrayList<String> allUserNames = LoginControlCenter.getInstance().getAllAccounts();
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.auto_complete_textview, allUserNames);
		mUsername.setAdapter(adapter);
		mLoginStatusListener = new LoginStatusListener() {
			
			@Override
			public void onLoginSuccess(int fill_stage,long session) {
				
				SystemUtil.logykn("登陆成功！");		
				if(session != mCurrentSession) {
					return ;
				}
				RenrenChatApplication.sIsSingleLoginError = false;
				if(SystemUtil.mDebug){
					SystemUtil.logykn("lastAccount="+LoginManager.getInstance().getLoginInfo().mLastAccount+"#now="+LoginManager.getInstance().getLoginInfo().mAccount);
				}
				if(!TextUtils.isEmpty(LoginManager.getInstance().getLoginInfo().mLastAccount)){
					
					if(!LoginManager.getInstance().getLoginInfo().mLastAccount.equals(LoginManager.getInstance().getLoginInfo().mAccount)){
						if(SystemUtil.mDebug){
							SystemUtil.logykn("账户不一致  清空上一个账户的所有信息");
							SystemUtil.logykn("lastAccount="+LoginManager.getInstance().getLoginInfo().mLastAccount+"#now="+LoginManager.getInstance().getLoginInfo().mAccount);
						}
						LoginControlCenter.getInstance().onLoginClear();
					}else{
						if(SystemUtil.mDebug){
							SystemUtil.logykn("和上一个账号相同");
							SystemUtil.logykn("lastAccount="+LoginManager.getInstance().getLoginInfo().mLastAccount+"#now="+LoginManager.getInstance().getLoginInfo().mAccount);
						}
					}
				}
				MainFragmentActivity.show(LoginSixinActivity.this, MainFragmentActivity.Tab.CONTRACT);
				dismissDialog();
				//undisableLogin();
				LoginSixinActivity.this.finish();
			}
			
			@Override
			public void onLoginFailed(long error_code, String error_msg, long session) {
				
				SystemUtil.logykn("error_code:" + error_code);
				SystemUtil.logykn("error_msg:" + error_msg);
				
				SystemUtil.logykn("登陆失败！");	
				if(session != mCurrentSession) {
					return ;
				}
				
				final int code = (int) error_code;
				dismissDialog();
				//undisableLogin();
				RenrenChatApplication.mHandler.post(new Runnable() {
					@Override
					public void run() {
						switch (code) {		
						case 10003:
							final String username = mUsername.getText().toString().trim();
							AlertDialog.Builder builder = new AlertDialog.Builder(LoginSixinActivity.this);
							builder.setMessage(R.string.ykn_login_dialog_message);
							builder.setNegativeButton(R.string.ykn_confirm,
									
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											LoginSixinActivity.show(
													LoginSixinActivity.this,
													LOGIN_SIXIN, username);
											LoginSixinActivity.this.finish();
										}
									});
							builder.setPositiveButton(R.string.ykn_cancel,
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											mUsername.setText("");
										}
									});
							builder.create().show();
							refreshCaptcha();
							break;
						case 10002:
							if(LOGIN_REGISTERED == mFlag) {
								SystemUtil.toast(R.string.ykn_login_toast1);
							}else{
								SystemUtil.toast(R.string.ykn_login_toast2);
							}
							refreshCaptcha();
						    break;
						case 10001:
							SystemUtil.toast(R.string.ykn_login_toast2);
							refreshCaptcha();
							break;
					    default: 
							refreshCaptcha();
							break;
			            }	
					}
				});
			}
			
			@Override
			public void onLoginResponse() {
				dismissDialog();
				//undisableLogin();
			}
		};
	}
	
	/**
	 * 处理登陆结果的接口
	 * 
	 * @author he.cao
	 * 
	 */
	public interface LoginStatusListener {
		/**
		 * 登陆请求返回
		 */
		public void onLoginResponse();
		/**
		 * 登陆成功
		 */
		public void onLoginSuccess(int fill_stage,long session);
		/**
		 * 登陆失败
		 */
		public void onLoginFailed(long error_code, String error_msg, long session);
	}
	/**
	 * 隐藏输入法
	 */
	private void hideKeyboard() {
		((InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE))
				.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
	}
	
	@Override
	protected void onDestroy() {
		/*if(sActivity==this){
			sActivity = null;
		}
		RenrenChatApplication.popStack(this);*/
		dismissDialog();
		super.onDestroy();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		mCurrentSession = 0;
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			switch (mFlag) {
			case LOGIN_REGISTERED:
				LoginSixinActivity.this.finish();
				break;
			default:
				WelcomeActivity.show(LoginSixinActivity.this);
				LoginSixinActivity.this.finish();
				break;
			}
		}
		return super.onKeyDown(keyCode, event);
	}
	
	class AccountWatcher implements TextWatcher {

		@Override
		public void afterTextChanged(Editable s) {
			
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			mCaptchaLayout.setVisibility(View.GONE);
			if (TextUtils.isEmpty(mUsername.getText().toString())) {
				mPassword.setText("");
				mUsernameDel.setVisibility(View.GONE);
			}else{
				mUsernameDel.setVisibility(View.VISIBLE);
			}
		}
	}
}

