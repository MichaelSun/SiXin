package com.renren.mobile.chat.ui.account;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.DialogInterface.OnCancelListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.SmsMessage;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.common.manager.LoginManager;
import com.common.manager.LoginManager.LoginInfo;
import com.common.mcs.INetReponseAdapter;
import com.common.mcs.INetRequest;
import com.common.mcs.McsServiceProvider;
import com.common.utils.RegexUtil;
import com.core.json.JsonObject;
import com.core.util.CommonUtil;
import com.renren.mobile.account.LoginControlCenter;
import com.renren.mobile.account.LoginfoModel;
import com.renren.mobile.chat.R;
import com.renren.mobile.chat.base.util.SystemUtil;
import com.renren.mobile.chat.ui.BaseActivity;
import com.renren.mobile.chat.ui.guide.WelcomeActivity;
import com.renren.mobile.chat.ui.setting.SettingForwardActivity;
import com.renren.mobile.chat.view.BaseTitleLayout;
/**
 * 注册
 * @author kaining.yang
 */
public class RegisterActivity extends BaseActivity {
	
	private static final int LOCK_TIME = 9;
	
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
	private int mFlag;
	
	/**是否正常跳转 */
	private boolean normal;
	
	private TextView mGuidance;
	
	private TextView mHint;
	
	private EditText mPhoneOrEmail;
	
	private EditText mCaptcha;
	
	private Button mBtnCaptcha;
	
	private Button mBtnSubmit;
	
	private Handler mCountHandler;
	
	private CountThread mCountThread;
	
	private int mCount;
	
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
		this.setContentView(R.layout.ykn_register);
		mContext = this;
		initViews();
		initCaptchaReceiver();
		mCountHandler = new Handler();
		mCountThread = new CountThread();
    }
    
    public static void show(Context context, int flagtype){
    	Intent intent =new Intent(context,RegisterActivity.class);
    	intent.putExtra(FLAG_TYPE, flagtype);
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
    	unregisterReceiver(mReceiver);
    	Log.d("sunnyykn", "receiver unregistered");
		super.onDestroy();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			jumpBack();
		}
		return super.onKeyDown(keyCode, event);
	}

	private void initViews() {
		mTitle = (LinearLayout) this.findViewById(R.id.title);
    	mPhoneOrEmail = (EditText) this.findViewById(R.id.et_phone_or_email);
    	mCaptcha = (EditText) this.findViewById(R.id.et_captcha);
    	mBtnCaptcha = (Button) this.findViewById(R.id.btn_get_captcha);
    	mBtnSubmit = (Button) this.findViewById(R.id.btn_check_in);
    	mGuidance = (TextView) this.findViewById(R.id.tv_guidance);
    	mHint = (TextView) this.findViewById(R.id.tv_hint);
    	
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
				jumpBack();
			}
		});
    	
    	mFlag = getIntent().getExtras().getInt(FLAG_TYPE, FLAG_REGISTER);
    	
    	switch (mFlag) {
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
		mBtnCaptcha.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				strUser = mPhoneOrEmail.getText().toString();
				getCaptcha();
			}
		});
		
		// 验证验证码
		mBtnSubmit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				strCaptcha = mCaptcha.getText().toString();
				strUser = mPhoneOrEmail.getText().toString();
				checkCaptcha();
			}
		});
    }
	
	private void jumpBack() {
		switch (mFlag) {
		case FLAG_REGISTER:
			WelcomeActivity.show(RegisterActivity.this);
			RegisterActivity.this.finish();
			break;
		case FLAG_BIND_PHONE:
			
			break;
		case FLAG_BIND_EMAIL:
			
			break;
		case FLAG_FIND_SECRET:
			
			break;
		}
	}
	
	/**
	 * @author kaining.yang
	 * 不同页面的业务逻辑在这里，主要为调不同的请求
	 * 获取验证码
	 */
	private void getCaptcha() {
		
		// 发送获取验证码请求前要用正则检查 帐号 是否合法（手机号、邮箱）
		switch (mFlag) {
		case FLAG_FIND_SECRET:
			break;
		case FLAG_REGISTER:
			if (RegexUtil.isMobilePhone(strUser) || RegexUtil.isEmailAddress(strUser)) {
				onClickCaptcha();
				McsServiceProvider.getProvider().getCaptcha(strUser, "GET", new responseRegisterCaptcha());
				break;
			} else {
				// 注册帐号不合法
				CommonUtil.toast("请填写正确格式的手机号或邮箱！");
				break;
			}
		case FLAG_BIND_EMAIL: // 绑定邮箱和手机号接口一致
			if (RegexUtil.isEmailAddress(strUser)) {
				onClickCaptcha();
				McsServiceProvider.getProvider().getCaptchaBinding(strUser, "GET", new responseBindingCaptcha());
				break;
			} else {
				// 注册帐号不合法
				CommonUtil.toast("请填写正确格式的邮箱！");
				break;
			}
		case FLAG_BIND_PHONE:
			if (RegexUtil.isMobilePhone(strUser)) {
				onClickCaptcha();
				McsServiceProvider.getProvider().getCaptchaBinding(strUser, "GET", new responseBindingCaptcha());
				break;
			} else {
				// 注册帐号不合法
				CommonUtil.toast("请填写正确格式的手机号！");
				break;
			}
    	}
		
	}
	
	class responseRegisterCaptcha extends INetReponseAdapter{
    	
    	public responseRegisterCaptcha() {
    		
    	}
		
		@Override
		public void onSuccess(INetRequest req, JsonObject data) {
			SystemUtil.log("sunnyykn", "获取验证码成功:"+data);
			CommonUtil.toast("获取验证码成功！");
			SystemUtil.log("sunnyykn", "验证码:"+data);
		}
		
		@Override
		public void onError(INetRequest req, JsonObject data) {
			SystemUtil.log("sunnyykn", "获取验证码失败信息:"+data);
			CommonUtil.toast("获取验证码失败！");
		}
	}
	
	class responseBindingCaptcha extends INetReponseAdapter {
		
		@Override
		public void onSuccess(INetRequest req, JsonObject data) {
			SystemUtil.log("sunnyykn", "获取验证码成功:"+data);
			CommonUtil.toast("获取验证码成功！");
			SystemUtil.log("sunnyykn", "验证码:"+data);
		}
		
		@Override
		public void onError(INetRequest req, JsonObject data) {
			SystemUtil.log("sunnyykn", "获取验证码失败信息:"+data);
			CommonUtil.toast("获取验证码失败！");
		}
		
	}
	
	
	/**
	 * @author kaining.yang
	 * 不同页面的业务逻辑在这里，主要为调不同的请求
	 * 校对验证码
	 */
	private void checkCaptcha() {
		if (!mDialog.isShowing()) {
			mDialog.show();
		}
		switch (mFlag) {
		case FLAG_FIND_SECRET:
			break;
		case FLAG_REGISTER:
			McsServiceProvider.getProvider().checkCaptcha(strUser, strCaptcha, new responseRegisterCheck());
			break;
		case FLAG_BIND_EMAIL:
			//McsServiceProvider.getProvider().bindAccount(strUser, strCaptcha, new responseBindingCheck());
			break;
		case FLAG_BIND_PHONE:
			//McsServiceProvider.getProvider().bindAccount(strUser, strCaptcha, new responseBindingCheck());
			break;
    	}
	}
    
    class responseRegisterCheck extends INetReponseAdapter {
    	
    	public responseRegisterCheck() {
    		
    	}
		
		@Override
		public void onSuccess(INetRequest req, JsonObject data) {
			SystemUtil.log("sunnyykn", "验证验证码成功:"+data);
			CommonUtil.toast("验证验证码成功！");
			
			// 验证成功后跳转到完善资料并注册页面RegisterInfoActivity
			Intent intent = new Intent(RegisterActivity.this, RegisterInfoActivity.class);
			intent.putExtra("username", strUser);
			intent.putExtra("captcha", strCaptcha);
			RegisterActivity.this.startActivity(intent);
			RegisterActivity.this.finish();
			
			
			if (mDialog.isShowing()) {
				mDialog.dismiss();
			}
		}
		
		@Override
		public void onError(INetRequest req, JsonObject data) {
			SystemUtil.log("sunnyykn", "验证验证码失败信息:"+data);
			CommonUtil.toast("验证验证码失败！");
			long error_code = data.getNum("error_code");
			String error_msg = data.getString("error_msg");
			
			if (mDialog.isShowing()) {
				mDialog.dismiss();
			}
		}
		
	}
	
	class responseBindingCheck extends INetReponseAdapter {
		
		@Override
		public void onSuccess(INetRequest req, JsonObject data) {
			SystemUtil.logykn("返回数据：" + data.toString());
			SystemUtil.logykn("绑定成功");
			CommonUtil.toast("绑定成功！");
			
			// 更新LoginInfo 和数据库
			updateUserData();
			
			if (mDialog.isShowing()) {
				mDialog.dismiss();
			}
			
			// 跳转到绑定手机和邮箱页面
			SettingForwardActivity.show(RegisterActivity.this, SettingForwardActivity.ForwardScreenType.SELFINFO_SETTING_SCREEN);
			RegisterActivity.this.finish();
		}
		
		@Override
		public void onError(INetRequest req, JsonObject data) {
			SystemUtil.logykn("返回数据：" + data.toString());
			SystemUtil.logykn("绑定失败");
			CommonUtil.toast("绑定失败！");
			
			if (mDialog.isShowing()) {
				mDialog.dismiss();
			}
		}
		
	}

	private void updateUserData() {
		LoginInfo info = LoginManager.getInstance().getLoginInfo();
		LoginfoModel loginfo = new LoginfoModel();
		
		switch (mFlag) {

		case FLAG_BIND_PHONE:
			info.mBindInfoMobile.mBindId = strUser;
			info.mBindInfoMobile.mBindType = "mobile";
			info.mBindInfoMobile.mBindName = "";
			info.mBindInfoMobile.mBindPage = "";
			loginfo.parse(info);
			LoginManager.getInstance().setLoginfo(info);
			LoginControlCenter.getInstance().updateUserData(loginfo);
			break;

		case FLAG_BIND_EMAIL:
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
    
    private void initViewsFindSecret() {
    	mHint.setVisibility(View.VISIBLE);
    	mGuidance.setText(R.string.ykn_register_find_secret_guidance);
    	mPhoneOrEmail.setHint(R.string.ykn_register_edittext);
    	mBtnSubmit.setText(R.string.ykn_register_submit);
    	
    	// title
    	mBaseTitleLayout.setTitleMiddle(getString(R.string.ykn_register_find_secret_title));
    }
    
    private void initViewsRegister() {
    	mHint.setVisibility(View.GONE);
    	mGuidance.setText(R.string.ykn_register_guidance);
    	mPhoneOrEmail.setHint(R.string.ykn_register_edittext);
    	mBtnSubmit.setText(R.string.ykn_register_submit);
    	
    	// title
    	mBaseTitleLayout.setTitleMiddle(getString(R.string.ykn_register_title));
    }
    
    private void initViewsBindEmail() {
    	mHint.setVisibility(View.GONE);
    	mGuidance.setText(R.string.ykn_register_bind_email_guidance);
    	mPhoneOrEmail.setHint(R.string.ykn_register_bind_email_edittext);
    	mBtnSubmit.setText(R.string.ykn_register_bind_email_submit);
    	
    	// title
    	mBaseTitleLayout.setTitleMiddle(getString(R.string.ykn_register_bind_email_title));
    }
    
    private void initViewsBindPhone() {
    	mHint.setVisibility(View.GONE);
    	mGuidance.setText(R.string.ykn_register_bind_phone_guidance);
    	mPhoneOrEmail.setHint(R.string.ykn_register_bind_phone_edittext);
    	mBtnSubmit.setText(R.string.ykn_register_bind_phone_submit);
    	
    	// title
    	mBaseTitleLayout.setTitleMiddle(getString(R.string.ykn_register_bind_phone_title));
    }
    
    
    /**
     * 控制”获取验证码“倒计时按钮
     */
    private void onClickCaptcha() {
    	mCount = LOCK_TIME;
		mBtnCaptcha.setBackgroundResource(R.drawable.ykn_button_grey);
		mBtnCaptcha.setClickable(false);
		mPhoneOrEmail.setClickable(false);
		mCountThread.run();
    }
    

    class CountThread implements Runnable {

		@Override
		public void run() {
			if (mCount > 0) {
        		mBtnCaptcha.setText(getString(R.string.ykn_register_button_pressed) + "(" + mCount + ")");
        		mCount --;
        		mCountHandler.postDelayed(mCountThread, 1000);
        	} else if (mCount == 0) {
        		mBtnCaptcha.setText(getString(R.string.ykn_register_button_normal));
        		mBtnCaptcha.setBackgroundResource(R.drawable.ykn_button_background_green);
        		mBtnCaptcha.setClickable(true);
        		mPhoneOrEmail.setClickable(true);
        	}
		}
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
    				if (smsNumber.equals("106900867741")) {
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
    					Log.d("sunnyykn", "msg:" + msg.toString());
    					Log.d("sunnyykn", "obj:" + msg.obj.toString());
    					mCaptchaHandler.sendMessage(msg);
    					
    				} 
    			} 
    		} 
    	}
    }
}

