package com.renren.mobile.chat.ui.account;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.common.manager.LoginManager;
import com.common.utils.RegexUtil;
import com.core.util.CommonUtil;
import com.core.util.Md5;
import com.renren.mobile.account.LoginControlCenter;
import com.renren.mobile.chat.R;
import com.renren.mobile.chat.RenrenChatApplication;
import com.renren.mobile.chat.base.util.SystemUtil;
import com.renren.mobile.chat.dao.ContactsDAO;
import com.renren.mobile.chat.dao.DAOFactoryImpl;
import com.renren.mobile.chat.service.ChatService;
import com.renren.mobile.chat.ui.BaseActivity;
import com.renren.mobile.chat.ui.account.LoginSixinActivity.LoginStatusListener;
import com.renren.mobile.chat.ui.chatsession.ChatSessionHelper;
import com.renren.mobile.chat.ui.contact.C_ContactsData;
import com.renren.mobile.chat.ui.contact.ContactMessageData;
import com.renren.mobile.chat.ui.contact.RoomInfosData;
import com.renren.mobile.chat.util.ChatDataHelper;
import com.renren.mobile.chat.view.BaseTitleLayout;

/**
 * 
 * @author kaining.yang
 *
 */
public class RegisterInfoActivity extends BaseActivity {

	private ImageView mAvatar;
	
	private TextView mHint;
	
	private EditText mRealname;
	
	private EditText mPassword;
	
	private EditText mConfirm;
	
	private LinearLayout mBtnFemale;
	
	private LinearLayout mBtnMale;
	
	private ImageView mImgFemale;
	
	private ImageView mImgMale;
	
	private TextView mTvFemale;
	
	private TextView mTvMale;
	
	private Button mSubmit;
	
	private Dialog mDialog;
	
	private LinearLayout mTitle;
	
	private BaseTitleLayout mBaseTitleLayout;
	
	private LoginStatusListener mLoginStatusListener = null; 
	
	private Context mContext;
	
	/**
	 * 性别选择标志 
	 * 0：女
	 * 1：男
	 * -1：未选择
	 */
	private int mSexFlag = -1;
	
	/**
	 * 注册资料
	 */
	private String username;
	
	private String captcha;
	
	private String password;
	
	private String confirm;
	
	private int sex;
	
	private String realname;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.ykn_register_info);
		mContext = this;
		
		initViews();
		
		// 注册
		mSubmit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				register();
			}
		});
	}
	
	private void register() {
		// 读取EditView中的信息
		realname = mRealname.getText().toString();
		sex = mSexFlag;
		password = mPassword.getText().toString();
		confirm = mConfirm.getText().toString();
		// 检测注册信息是否合法
		if (realname == null || TextUtils.isEmpty(realname)) {
			CommonUtil.toast(R.string.ykn_register_info_inputname);
			return;
		}
		
		if (sex == -1) {
			CommonUtil.toast(R.string.ykn_register_info_inputsex);
			return;
		}
		
		if (password == null || TextUtils.isEmpty(password)) {
			CommonUtil.toast(R.string.ykn_register_info_inputpassword1);
			return;
		}
		
		if (!RegexUtil.isPassword(password)) {
			CommonUtil.toast(R.string.ykn_reset_error1);
			return;
		}
		
		if (confirm == null || TextUtils.isEmpty(confirm)) {
			CommonUtil.toast(R.string.ykn_register_info_inputpassword2);
			return;
		}
		
		if (!password.equals(confirm)) {
			CommonUtil.toast(R.string.ykn_register_info_inputpassword3);
			return;
		}
		
		
		// 检测账户信息均okay后发送注册请求
		if (!mDialog.isShowing()) {
			mDialog.show();
		}
		disableSubmit();
		
		mLoginStatusListener = new LoginStatusListener() {
			
			@Override
			public void onLoginSuccess(int fill_stage, long session) {
				
				SystemUtil.logykn("登陆成功！");		
				undisableSubmit();
				RenrenChatApplication.sIsSingleLoginError = false;
				if(TextUtils.isEmpty(LoginManager.getInstance().getLoginInfo().mLastAccount)) {
					// 从数据库读取
					if (LoginControlCenter.getInstance().loadLastLoginUserData() != null) {
						LoginManager.getInstance().getLoginInfo().mLastAccount = LoginControlCenter.getInstance().loadLastLoginUserData().mAccount;
					}
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
				/*if(!TextUtils.isEmpty(LoginManager.getInstance().getLoginInfo().mLastAccount)){
					if(!LoginManager.getInstance().getLoginInfo().mLastAccount.equals(LoginManager.getInstance().getLoginInfo().mAccount)){
						LoginControlCenter.getInstance().onLoginClear();
					}
				}*/
				ChatService.start();
				UploadContactActivity.showByRegister(RegisterInfoActivity.this);
				//MainFragmentActivity.show(RegisterInfoActivity.this, MainFragmentActivity.INDEX_CONTACT);
				
				// 同时销毁前两个页面
				RegisterInfoActivity.this.finish();
				// 注册成功销毁填写帐号页面
				RenrenChatApplication.removeActivity(RegisterAccountActivity.sActivity);
				
				if (mDialog.isShowing()) {
					mDialog.dismiss();
				}
			}
			
			@Override
			public void onLoginFailed(long error_code, String error_msg, long session) {
				SystemUtil.logykn("error_code:" + error_code);
				SystemUtil.logykn("error_msg:" + error_msg);
				
				if (mDialog.isShowing()) {
					mDialog.dismiss();
				}
				undisableSubmit();
			}
			
			@Override
			public void onLoginResponse() {
				if (mDialog.isShowing()) {
					mDialog.dismiss();
				}
				undisableSubmit();
			}
		};
		
		LoginControlCenter.getInstance().register(
				username,
				Md5.toMD5(password), 
				captcha, 
				sex, 
				realname, 
				mLoginStatusListener
				);
		// McsServiceProvider.getProvider().register(username,  Md5.toMD5(password), captcha, sex, realname, new responseRegister());
	}
	
	/*class responseRegister extends INetReponseAdapter{
    	
    	public responseRegister() {
    		
    	}
		
		@Override
		public void onSuccess(INetRequest req, JsonObject data) {
			SystemUtil.log("sunnyykn", "注册成功:"+data);
			SystemUtil.toast("注册成功！");
			
			if (mDialog.isShowing()) {
				mDialog.dismiss();
			}
			
			RegisterInfoActivity.this.finish();
			// 注册成功销毁填写帐号页面
			RenrenChatApplication.removeActivity(RegisterAccountActivity.sActivity);
		}
		
		@Override
		public void onError(INetRequest req, JsonObject data) {
			SystemUtil.log("sunnyykn", "错误信息:"+data);
			SystemUtil.toast("注册失败！");
			long error_code = data.getNum("error_code");
			String error_msg = data.getString("error_msg");
			
			if (mDialog.isShowing()) {
				mDialog.dismiss();
			}
		}
		
	}*/
	
	private void initViews() {
		username = getIntent().getExtras().getString("username");
		captcha = getIntent().getExtras().getString("captcha");
		
		mTitle = (LinearLayout) this.findViewById(R.id.title);
		mAvatar = (ImageView) this.findViewById(R.id.avatar);
		mHint = (TextView) this.findViewById(R.id.tv_hint);
		mRealname = (EditText) this.findViewById(R.id.et_realname);
		mPassword = (EditText) this.findViewById(R.id.et_password);
		mConfirm = (EditText) this.findViewById(R.id.et_confirm);
		mSubmit = (Button) this.findViewById(R.id.btn_submit);
		mBtnMale = (LinearLayout) this.findViewById(R.id.btn_male);
		mBtnFemale = (LinearLayout) this.findViewById(R.id.btn_female);
		mImgMale = (ImageView) this.findViewById(R.id.male);
		mImgFemale = (ImageView) this.findViewById(R.id.female);
		mTvMale = (TextView) this.findViewById(R.id.tv_male);
		mTvFemale = (TextView) this.findViewById(R.id.tv_female);
		
		mDialog = new Dialog(this, R.style.sendRequestDialog);
		mDialog.setContentView(R.layout.ykn_dialog);
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
		
		// title
    	mBaseTitleLayout.setTitleMiddle(getString(R.string.ykn_register_info_title));
    	mBaseTitleLayout.getTitleLeft().setOnClickListener(new OnClickListener() {
    			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
    	
    	/**
    	 * sex buttons 
    	 * 空：-1
    	 * 女：0
    	 * 男：1
    	 */
    	mBtnMale.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mSexFlag = 1;
				mTvMale.setTextColor(mContext.getResources().getColor(R.color.Color_C));
				mImgMale.setBackgroundResource(R.drawable.ykn_male_pressed);
				mBtnMale.setBackgroundResource(R.drawable.ykn_button_gender_pressed_male);
				
				mTvFemale.setTextColor(mContext.getResources().getColor(R.color.Color_B));
				mImgFemale.setBackgroundResource(R.drawable.ykn_female_normal);
				mBtnFemale.setBackgroundResource(R.drawable.ykn_button_gender_normal);
			}
		});
    	
    	mBtnFemale.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mSexFlag = 0;
				mTvFemale.setTextColor(mContext.getResources().getColor(R.color.Color_C));
				mImgFemale.setBackgroundResource(R.drawable.ykn_female_pressed);
				mBtnFemale.setBackgroundResource(R.drawable.ykn_button_gender_pressed_female);
				
				mTvMale.setTextColor(mContext.getResources().getColor(R.color.Color_B));
				mImgMale.setBackgroundResource(R.drawable.ykn_male_normal);
				mBtnMale.setBackgroundResource(R.drawable.ykn_button_gender_normal);
			}
		});
		
	}
	
	private void disableSubmit() {
		mSubmit.setClickable(false);
		mSubmit.setTextColor(mContext.getResources().getColor(R.color.Color_D));
		mSubmit.setBackgroundResource(R.drawable.ykn_button_disable);
	}
	
	private void undisableSubmit() {
		RenrenChatApplication.sHandler.post(new Runnable() {
			
			@Override
			public void run() {
				mSubmit.setClickable(true);
				mSubmit.setTextColor(mContext.getResources().getColor(R.color.Color_C));
				mSubmit.setBackgroundResource(R.drawable.ykn_button_background_blue);
			}
		});
		
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
}
