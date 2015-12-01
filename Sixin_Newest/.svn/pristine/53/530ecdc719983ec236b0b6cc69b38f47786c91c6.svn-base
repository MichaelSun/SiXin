package com.renren.mobile.chat.ui.account;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.common.manager.LoginManager;
import com.renren.mobile.account.LoginControlCenter;
import com.renren.mobile.account.LoginfoModel;
import com.renren.mobile.chat.R;
import com.renren.mobile.chat.RenrenChatApplication;
import com.renren.mobile.chat.base.util.SystemUtil;
import com.renren.mobile.chat.base.views.NotSynImageView;
import com.renren.mobile.chat.ui.BaseActivity;
import com.renren.mobile.chat.ui.MainFragmentActivity;
import com.renren.mobile.chat.ui.account.LoginSixinActivity.LoginStatusListener;
import com.renren.mobile.chat.ui.guide.WelcomeActivity;
import com.renren.mobile.chat.view.BaseTitleLayout;

public class RenrenAuthorizeActivity extends BaseActivity {
	
	private LinearLayout mTitle;
	
	private BaseTitleLayout mBaseTitleLayout;
	
	private NotSynImageView mAvatar;
	
	private TextView mUserInfo;
	
	private Button mLoginAuthorize;
	
	private Button mLoginOther;
	
	private LoginfoModel mLoginInfo;
	
	private long mCurrentSession;
	
	private LoginStatusListener mLoginStatusListener = null; 
	
	private Dialog mDialog;

	private LoginfoModel mLoginInfoModel;
	
	private Context mContext;
	
	private String Username;
	
	private String Userpassword;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.ykn_renren_authorize);
		mContext = this;
		
		initViews();
		
		mLoginAuthorize.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				login();
			}
		});
		
		mLoginOther.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				WelcomeActivity.show(RenrenAuthorizeActivity.this);
				RenrenAuthorizeActivity.this.finish();
			}
		});
		
	}
	
	private void initViews() {
		mTitle = (LinearLayout) findViewById(R.id.title);
		mAvatar = (NotSynImageView) findViewById(R.id.avatar);
		mUserInfo = (TextView) findViewById(R.id.user_info);
		mLoginAuthorize = (Button) findViewById(R.id.login_authorize);
		mLoginOther = (Button) findViewById(R.id.login_other);
		
		// title
    	mBaseTitleLayout = new BaseTitleLayout(this);
    	mTitle.addView(mBaseTitleLayout.getView(), 
    			new LinearLayout.LayoutParams(
    					LayoutParams.FILL_PARENT,
    					LayoutParams.FILL_PARENT, 1));
    	mBaseTitleLayout.setTitleMiddle(getString(R.string.ykn_renren_authorize_title));
    	mBaseTitleLayout.setTitleMiddleIcon(R.drawable.ykn_renren_logo);
    	
    	mBaseTitleLayout.getTitleLeft().setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// 测试
				// UploadContactActivity.show(RenrenAuthorizeActivity.this);
				
				// 测试
				/*
				String[] imageLargeUrls = {
						"http://fmn.rrimg.com/fmn061/20120825/1830/original_Uxwj_399400004633118e.jpg",
						"http://fmn.rrimg.com/fmn061/20120824/1630/original_VgbZ_774a00000eca118f.jpg",
						"http://fmn.rrimg.com/fmn056/20120824/1440/original_3YsT_3fb200000a31118d.jpg",
						"http://fmn.rrimg.com/fmn061/20120825/1830/original_Uxwj_399400004633118e.jpg",
						"http://fmn.rrimg.com/fmn061/20120824/1630/original_VgbZ_774a00000eca118f.jpg",
						"http://fmn.rrimg.com/fmn061/20120825/1830/original_Uxwj_399400004633118e.jpg",
						"http://fmn.rrimg.com/fmn061/20120824/1630/original_VgbZ_774a00000eca118f.jpg",
						"http://fmn.rrimg.com/fmn056/20120824/1440/original_3YsT_3fb200000a31118d.jpg"
						};
				PhotoNew.show(RenrenAuthorizeActivity.this, imageLargeUrls);
				*/
			}
		});
    	mBaseTitleLayout.getTitleLeft().setVisibility(View.INVISIBLE);
    	
    	mDialog = new Dialog(this, R.style.logindialog);
		mDialog.setContentView(R.layout.y_login_dialog);
		mDialog.setOnCancelListener(new OnCancelListener() {
			public void onCancel(DialogInterface dialog) {
				mCurrentSession=0l;
			}
		});
    	
    	mUserInfo.setText(LoginManager.getInstance().getLoginInfo().mUserName);
    	SystemUtil.logykn("user_name:" + LoginManager.getInstance().getLoginInfo().mUserName);
    	mAvatar.clear();
    	mAvatar.addUrl(LoginManager.getInstance().getLoginInfo().mHeadUrl);
    	SystemUtil.logykn("head_url:" + LoginManager.getInstance().getLoginInfo().mHeadUrl);
	}
	
	public static void show(Context context) {
		Intent intent = new Intent(context, RenrenAuthorizeActivity.class);
		context.startActivity(intent);
	}
	
	private void dismissDialog() {
		if(mDialog.isShowing()){
			mDialog.dismiss();
		}
	}
	
	private void showDialog() {
		if(!mDialog.isShowing()){
			mDialog.show();
		}
	}
	
	private void login() {
				
		mLoginStatusListener = new LoginStatusListener() {
			
			@Override
			public void onLoginSuccess(int fill_stage,long session) {
				
				SystemUtil.logykn("授权登陆成功！");		
				if(session != mCurrentSession){
					return ;
				}
				if(TextUtils.isEmpty(LoginManager.getInstance().getLoginInfo().mLastAccount)) {
					// 从数据库读取
					if (LoginControlCenter.getInstance().loadLastLoginUserData() != null) {
						LoginManager.getInstance().getLoginInfo().mLastAccount = LoginControlCenter.getInstance().loadLastLoginUserData().mAccount;
					}
				}
				if(SystemUtil.mDebug){
					SystemUtil.errord("lastAccount="+LoginManager.getInstance().getLoginInfo().mLastAccount+"#now="+LoginManager.getInstance().getLoginInfo().mAccount);
				}
				if(!TextUtils.isEmpty(LoginManager.getInstance().getLoginInfo().mLastAccount)){
					if(!LoginManager.getInstance().getLoginInfo().mLastAccount.equals(LoginManager.getInstance().getLoginInfo().mAccount)){
						LoginControlCenter.getInstance().onLoginClear();
					}
				}
				RenrenChatApplication.sIsSingleLoginError = false;
				MainFragmentActivity.show(mContext, MainFragmentActivity.Tab.CONTRACT);
				RenrenAuthorizeActivity.this.finish();
				dismissDialog();
			}
			
			@Override
			public void onLoginFailed(long error_code, String error_msg, long session) {
				SystemUtil.logykn("error_code:" + error_code);
				SystemUtil.logykn("error_msg:" + error_msg);
				final int code=(int) error_code;
				dismissDialog();
				RenrenChatApplication.mHandler.post(new Runnable() {
					@Override
					public void run() {
						switch (code) {		
						case 10003:
							final String username = LoginManager.getInstance().getLoginInfo().mAccount.trim();
							AlertDialog.Builder builder = new AlertDialog.Builder(RenrenAuthorizeActivity.this);
							builder.setMessage(R.string.ykn_login_dialog_message);
							builder.setPositiveButton(R.string.ykn_confirm,
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											LoginSixinActivity
													.show(RenrenAuthorizeActivity.this,
															LoginSixinActivity.LOGIN_SIXIN,
															username);
											RenrenAuthorizeActivity.this
													.finish();
										}
									});
							builder.create().show();
							break;
						case 10004:
							SystemUtil.logykn("用户", Username);
							SystemUtil.logykn("密码", Userpassword);
							LoginSixinActivity
									.show(RenrenAuthorizeActivity.this,
											LoginSixinActivity.LOGIN_RENREN,
											Username,
											Userpassword);
							RenrenAuthorizeActivity.this.finish();
							break;
					    default: 
							break;
			            }	
					}
				});
			}
			
			@Override
			public void onLoginResponse() {
				dismissDialog();
			}
		};
		
		SystemUtil.logykn("account:" + LoginManager.getInstance().getLoginInfo().mAccount);
		SystemUtil.logykn("password:" + LoginManager.getInstance().getLoginInfo().mPassword);
		Username = LoginManager.getInstance().getLoginInfo().mAccount;
		Userpassword = LoginManager.getInstance().getLoginInfo().mPassword;
		
		
		/*if (TextUtils.isEmpty(LoginManager.getInstance().getLoginInfo().mAccount)) {
			Toast.makeText(mContext, RenrenChatApplication.getmContext().getResources().getString(R.string.Login_java_3), Toast.LENGTH_LONG).show();		//Login_java_3=请输入登录帐号; 
			return;
		}
		
		if (TextUtils.isEmpty(LoginManager.getInstance().getLoginInfo().mPassword)) {
			Toast.makeText(mContext, RenrenChatApplication.getmContext().getResources().getString(R.string.Login_java_4), Toast.LENGTH_LONG).show();		//Login_java_4=请输入密码; 
			return;
			
		}*/
		
		showDialog();
		mCurrentSession = System.currentTimeMillis();
		/**
		 * kaining.yang
		 */
		LoginControlCenter.getInstance().login(
				LoginManager.LOGIN_RENREN,
				LoginManager.getInstance().getLoginInfo().mAccount,
				LoginManager.getInstance().getLoginInfo().mPassword, 
				null,
				mContext, 
				mLoginStatusListener,
				mCurrentSession);
		
	}
}