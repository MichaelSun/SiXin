package com.renren.mobile.chat.ui.account;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.SmsMessage;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.common.manager.BindInfo;
import com.common.manager.LoginManager;
import com.common.manager.LoginManager.LoginInfo;
import com.common.manager.RegisterManager;
import com.common.mcs.INetReponseAdapter;
import com.common.mcs.INetRequest;
import com.common.mcs.McsServiceProvider;
import com.common.utils.RegexUtil;
import com.core.json.JsonObject;
import com.core.util.CommonUtil;
import com.renren.mobile.account.LoginControlCenter;
import com.renren.mobile.account.LoginfoModel;
import com.renren.mobile.chat.R;
import com.renren.mobile.chat.RenrenChatApplication;
import com.renren.mobile.chat.base.util.SystemUtil;
import com.renren.mobile.chat.common.ResponseError;
import com.renren.mobile.chat.ui.BaseActivity;
import com.renren.mobile.chat.ui.contact.ContactOptionActivity;
import com.renren.mobile.chat.ui.setting.SettingForwardActivity;
import com.renren.mobile.chat.view.BaseTitleLayout;

public class RegisterCaptchaActivity extends BaseActivity {

	/**
	 * @author kaining.yang
	 * 0：找回密码 验证帐号
	 * 1：注册私信
	 * 2：绑定邮箱
	 * 3：绑定手机号
	 */
	private int mFlag;
	
	private boolean mFlagFromContact;
	
	private TextView mGuidance;
	
	private EditText mCaptcha;
	
	private Button mBtnSubmit;
	
	private String strUser;
	
	private String strCaptcha;
	
	private LinearLayout mTitle;
	
	private Dialog mDialog;
	
	private Context mContext;
	
	private BaseTitleLayout mBaseTitleLayout;
	
	private InterceptSMSMessage mReceiver = new InterceptSMSMessage();
	
	public CaptchaHandler mCaptchaHandler = new CaptchaHandler();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.setContentView(R.layout.ykn_register_captcha);
		RenrenChatApplication.pushStack(this);
		mContext = this;
		mFlagFromContact = getIntent().getBooleanExtra(RegisterAccountActivity.FLAG_FINISH, false);
		initViews();
		initCaptchaReceiver();
	}
	
	public static void show(Context context, int flagtype, String account){
    	Intent intent =new Intent(context, RegisterCaptchaActivity.class);
    	intent.putExtra(RegisterAccountActivity.FLAG_TYPE, flagtype);
    	intent.putExtra(RegisterAccountActivity.ACCOUNT, account);
    	context.startActivity(intent);
    }
	
	public static void show(Context context, int flagtype, String account, boolean fromcontact){
    	Intent intent =new Intent(context, RegisterCaptchaActivity.class);
    	intent.putExtra(RegisterAccountActivity.FLAG_TYPE, flagtype);
    	intent.putExtra(RegisterAccountActivity.ACCOUNT, account);
    	intent.putExtra(RegisterAccountActivity.FLAG_FINISH, fromcontact);
    	context.startActivity(intent);
    }
	
	private void initCaptchaReceiver() {
		IntentFilter filter = new IntentFilter();  
		filter.setPriority(Integer.MAX_VALUE);
		filter.addAction("android.provider.Telephony.SMS_RECEIVED");
		registerReceiver(mReceiver, filter);
		Log.d("sunnyykn", "receiver registered");
    }
    
    @Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(mReceiver);
    	Log.d("sunnyykn", "receiver unregistered");
    	RenrenChatApplication.popStack(this);
	}
	
	private void initViews() {
		mFlag = getIntent().getExtras().getInt(RegisterAccountActivity.FLAG_TYPE, RegisterAccountActivity.FLAG_REGISTER);
    	strUser = getIntent().getExtras().getString(RegisterAccountActivity.ACCOUNT);
		
		mTitle = (LinearLayout) this.findViewById(R.id.title);
    	mCaptcha = (EditText) this.findViewById(R.id.et_captcha);
    	mBtnSubmit = (Button) this.findViewById(R.id.btn_submit);
    	mGuidance = (TextView) this.findViewById(R.id.tv_guidance);
    	
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
    	// title back
    	mBaseTitleLayout.getTitleLeft().setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//jumpBack();
				RegisterCaptchaActivity.this.finish();
			}
		});
    	// title
    	mBaseTitleLayout.setTitleMiddle(getString(R.string.ykn_captcha_title));
    	
    	switch (mFlag) {
		case RegisterAccountActivity.FLAG_FIND_SECRET:
			initViewsFindSecret();
			break;
		case RegisterAccountActivity.FLAG_REGISTER:
			initViewsRegister();
			break;
		case RegisterAccountActivity.FLAG_BIND_EMAIL:
			initViewsBindEmail();
			break;
		case RegisterAccountActivity.FLAG_BIND_PHONE:
			initViewsBindPhone();
			break;
		default:
			
			break;
		}
    	
		// 验证验证码
		mBtnSubmit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				strCaptcha = mCaptcha.getText().toString();
				if(TextUtils.isEmpty(strCaptcha)){
					CommonUtil.toast(R.string.ykn_captcah_toast1);
				}else{
				checkCaptcha();}
			}
		});
    }
	
	/**
	 * @author kaining.yang
	 * 不同页面的业务逻辑在这里，主要为调不同的请求
	 * 校对验证码
	 */
	private void checkCaptcha() {
		showSubmitDialog();
		disableSubmit();
		switch (mFlag) {
		case RegisterAccountActivity.FLAG_FIND_SECRET:
			RegisterManager.getRegisterManager().identifyForgetPwdCode(strUser, strCaptcha, new responseForgetCheck());
			break;
		case RegisterAccountActivity.FLAG_REGISTER:
			McsServiceProvider.getProvider().checkCaptcha(strUser, strCaptcha ,new responseRegisterCheck());
			break;
		case RegisterAccountActivity.FLAG_BIND_EMAIL:
			McsServiceProvider.getProvider().bindAccount(strUser, strCaptcha, LoginManager.getInstance().getLoginInfo().mPasswordToken, new responseBindingCheck());
			break;
		case RegisterAccountActivity.FLAG_BIND_PHONE:
			McsServiceProvider.getProvider().bindAccount(strUser, strCaptcha, LoginManager.getInstance().getLoginInfo().mPasswordToken, new responseBindingCheck());
			break;
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
    
	class responseForgetCheck extends INetReponseAdapter {
    	
    	public responseForgetCheck() {
    		
    	}
		
		@Override
		public void onSuccess(INetRequest req, JsonObject data) {
			SystemUtil.log("sunnyykn", "验证验证码成功:"+data);
			//CommonUtil.toast("验证验证码成功！");
			
			// 验证成功后跳转到 重置密码 页面
			ResetPasswordActivity.show(mContext, strUser, strCaptcha);
			RegisterCaptchaActivity.this.finish();
			// RenrenChatApplication.popStack(RegisterAccountActivity.sActivity);
			
			/*RenrenChatApplication.sHandler.post(new Runnable() {
				
				@Override
				public void run() {
					RenrenChatApplication.popStack(RegisterAccountActivity.sActivity);
				}
			});*/
			
			dismissSubmitDialog();
			undisableSubmit();
		}
		
		@Override
		public void onError(INetRequest req, JsonObject data) {
			SystemUtil.log("sunnyykn", "验证验证码失败信息:"+data);
			//CommonUtil.toast("验证验证码失败！");
			ResponseError.showError(data);
			
			dismissSubmitDialog();
			undisableSubmit();
		}
	}
	
	class responseRegisterCheck extends INetReponseAdapter {
    	
    	public responseRegisterCheck() {
    		
    	}
		
		@Override
		public void onSuccess(INetRequest req, JsonObject data) {
			SystemUtil.log("sunnyykn", "验证验证码成功:"+data);
			//CommonUtil.toast("验证验证码成功！");
			
			// 验证成功后跳转到完善资料并注册页面RegisterInfoActivity
			Intent intent = new Intent(RegisterCaptchaActivity.this, RegisterInfoActivity.class);
			intent.putExtra("username", strUser);
			intent.putExtra("captcha", strCaptcha);
			RegisterCaptchaActivity.this.startActivity(intent);
			RegisterCaptchaActivity.this.finish();
			
			dismissSubmitDialog();
			undisableSubmit();
		}
		
		@Override
		public void onError(INetRequest req, JsonObject data) {
			SystemUtil.log("sunnyykn", "验证验证码失败信息:"+data);
			//CommonUtil.toast("验证验证码失败！");
			ResponseError.showError(data);
			
			dismissSubmitDialog();
			undisableSubmit();
		}
	}
	
	class responseBindingCheck extends INetReponseAdapter {
		
		@Override
		public void onSuccess(INetRequest req, JsonObject data) {
			SystemUtil.logykn("返回数据：" + data.toString());
			SystemUtil.logykn("绑定成功");
			//CommonUtil.toast("绑定成功！");
			
			// 更新LoginInfo 和数据库
			updateUserData();
			
			dismissSubmitDialog();
			undisableSubmit();
			
			// 跳转到绑定手机和邮箱页面
			if (mFlagFromContact) {
				// 跳转到联系人选项
				ContactOptionActivity.show(RegisterCaptchaActivity.this);
				RegisterCaptchaActivity.this.finish();
			} else {
				SettingForwardActivity.show(RegisterCaptchaActivity.this, SettingForwardActivity.ForwardScreenType.SELFINFO_SETTING_SCREEN);
				RegisterCaptchaActivity.this.finish();
			}
			
			// 绑定成功销毁填写帐号页面
			RenrenChatApplication.removeActivity(RegisterAccountActivity.sActivity);
		}
		
		@Override
		public void onError(INetRequest req, JsonObject data) {
			SystemUtil.logykn("返回数据：" + data.toString());
			SystemUtil.logykn("绑定失败");
			//CommonUtil.toast("绑定失败！");
			ResponseError.showError(data);
			
			dismissSubmitDialog();
			undisableSubmit();
		}
		
	}

	private void updateUserData() {
		LoginInfo info = LoginManager.getInstance().getLoginInfo();
		LoginfoModel loginfo = new LoginfoModel();
		
		switch (mFlag) {

		case RegisterAccountActivity.FLAG_BIND_PHONE:
			if (info.mBindInfoMobile == null) info.mBindInfoMobile = new BindInfo();
			info.mBindInfoMobile.mBindId = strUser;
			info.mBindInfoMobile.mBindType = "mobile";
			info.mBindInfoMobile.mBindName = "";
			info.mBindInfoMobile.mBindPage = "";
			loginfo.parse(info);
			LoginManager.getInstance().setLoginfo(info);
			LoginControlCenter.getInstance().updateUserData(loginfo);
			break;

		case RegisterAccountActivity.FLAG_BIND_EMAIL:
			if (info.mBindInfoEmail == null) info.mBindInfoEmail = new BindInfo();
			info.mBindInfoEmail.mBindId = strUser;
			info.mBindInfoEmail.mBindType = "email";
			info.mBindInfoEmail.mBindName = "";
			info.mBindInfoEmail.mBindPage = "";
			loginfo.parse(info);
			LoginManager.getInstance().setLoginfo(info); 				// 更新内存
			LoginControlCenter.getInstance().updateUserData(loginfo);	// 更新数据库
			break;
		}
	}
	
	// 初始化页面文案
	private void initViewsFindSecret() {
		initViewsRegister();
    }
    
    private void initViewsRegister() {
    	// 由正则判断手机号 或 邮箱
    	if (RegexUtil.isEmailAddress(strUser)) {
    		initViewsBindEmail();
    	} else {
    		initViewsBindPhone();
    	}
    }
    
    private void initViewsBindEmail() {
    	StringBuffer guidance = new StringBuffer();
    	guidance.append(getString(R.string.ykn_captcha_email_guidance1));
    	guidance.append(strUser);
    	guidance.append("\n");
    	guidance.append(getString(R.string.ykn_captcha_email_guidance2));
    	mGuidance.setText(guidance.toString());
    }
    
    private void initViewsBindPhone() {
    	StringBuffer guidance = new StringBuffer();
    	guidance.append(getString(R.string.ykn_captcha_phone_guidance1));
    	guidance.append(strUser);
    	guidance.append("\n");
    	guidance.append(getString(R.string.ykn_captcha_phone_guidance2));
    	mGuidance.setText(guidance.toString());
    }
	
	
	class CaptchaHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			String captcha = (String) msg.obj;
			if (captcha != null) {
				mCaptcha.setText(captcha);
			}
			super.handleMessage(msg);
		}
    }
    
    /**
     * @author kaining.yang
     * 拦截短信验证码 自动提取短信中的验证码填充到表单中
     */
    class InterceptSMSMessage extends BroadcastReceiver {
    	/**
    	 * The defination of the SMSACTION
    	 */
    	private final String SMSACTION = "android.provider.Telephony.SMS_RECEIVED";
    	
    	/**
    	 * 短信内容
    	 */
    	StringBuilder mSmsContent = new StringBuilder();	
    	/**
    	 * 短信发件人号码
    	 */
    	StringBuilder mSmsNumber = new StringBuilder();
    	
    	/**
    	 * 截获的验证码
    	 */
    	String verifyCode;

    	@Override
    	public void onReceive(Context context, Intent intent) {
    		 
    		if (intent.getAction().equals(SMSACTION)) {
    			// 接收由Intent传来的数据 
    			Bundle bundle = intent.getExtras();
    			if (bundle != null) {
    				// pdus为 android内置短信参数 identifier , 通过bundle.get("")返回一包含pdus的对象 
    				Object[] myOBJpdus = (Object[]) bundle.get("pdus");
    				// 构建短信对象array,并依据收到的对象长度来创建array的大小 
    				SmsMessage[] messages = new SmsMessage[myOBJpdus.length];
    				for (int i = 0; i < myOBJpdus.length; i++) {
    					messages[i] = SmsMessage
    							.createFromPdu((byte[]) myOBJpdus[i]);
    				}
    				for (SmsMessage currentMessage : messages) {
    					mSmsContent.append(currentMessage.getDisplayMessageBody());
    					mSmsNumber.append(currentMessage
    							.getDisplayOriginatingAddress());
    				}
    				String smsBody = mSmsContent.toString();
    				String smsNumber = mSmsNumber.toString();
    				if (smsNumber.contains("+86")) {
    					smsNumber = smsNumber.substring(3);
    				}
    				Log.d("sunnyykn", "手机号：" + smsNumber);
    				Log.d("sunnyykn", "短信：" + smsBody);
    				
    				// 确认该短信内容是否满足过滤条件 
    				boolean flags_filter = false;
    				if (smsNumber.equals("10690086")) {
    					// 屏蔽人人发来的短信
    					flags_filter = true; 
    					// 解析smsBody，获取其中的验证码，赋值给verifyCode
    					char[] ch = smsBody.toCharArray();
    					verifyCode = "";
    					for (int i = 0; i < ch.length; i++) {
    						if (Character.isDigit(ch[i]))
    							verifyCode += ch[i];
    					}
    					
    					Log.d("sunnyykn", "验证码" + smsBody);
    					Log.d("sunnyykn", "验证码" + verifyCode);
    					
    				}
    				// 第三步:取消
    				//if (flags_filter) {
    				if (true) {
    					this.abortBroadcast();
    					
    					Message msg = new Message();
    					msg.obj = verifyCode;
    					//Log.d("sunnyykn", "msg:" + msg.toString());
    					//Log.d("sunnyykn", "obj:" + msg.obj.toString());
    					mCaptchaHandler.sendMessage(msg);
    				} 
    			} 
    		} 
    	}
    }
}
