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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.common.manager.RegisterManager;
import com.common.mcs.INetReponseAdapter;
import com.common.mcs.INetRequest;
import com.common.utils.RegexUtil;
import com.core.json.JsonObject;
import com.core.util.Md5;
import com.core.util.CommonUtil;
import com.renren.mobile.chat.R;
import com.renren.mobile.chat.RenrenChatApplication;
import com.renren.mobile.chat.base.util.SystemUtil;
import com.renren.mobile.chat.ui.BaseActivity;
import com.renren.mobile.chat.view.BaseTitleLayout;

public class ResetPasswordActivity extends BaseActivity {

	private EditText mPassword;
	
	private EditText mConfirm;
	
	private Button mSubmit;
	
	private LinearLayout mTitle;
	
	private BaseTitleLayout mBaseTitleLayout;
	
	private Dialog mDialog;
	
	private Context mContext;
	
	private String strUser;
	
	private String strCaptcha;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.setContentView(R.layout.ykn_reset_password);
		mContext = this;
		strUser = getIntent().getExtras().getString("user");
		strCaptcha = getIntent().getExtras().getString("captcha");
		initViews();
		initTitle();
		
	}
	
	public static void show(Context context, String user, String captcha) {
		Intent intent = new Intent(context, ResetPasswordActivity.class);
		intent.putExtra("user", user);
		intent.putExtra("captcha", captcha);
		context.startActivity(intent);
	}
	
	private void initTitle() {
		// title
    	mBaseTitleLayout = new BaseTitleLayout(this);
    	mTitle.addView(mBaseTitleLayout.getView(), 
    			new LinearLayout.LayoutParams(
    					LayoutParams.FILL_PARENT,
    					LayoutParams.FILL_PARENT, 1));
    	
    	mBaseTitleLayout.setTitleMiddle(getString(R.string.ykn_reset_title));
    	
    	// title back
    	mBaseTitleLayout.getTitleLeft().setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ResetPasswordActivity.this.finish();
			}
		});
	}
	
	private void initViews() {
		mTitle = (LinearLayout) findViewById(R.id.title);
		mPassword = (EditText) findViewById(R.id.new_password);
		mConfirm = (EditText) findViewById(R.id.new_password_confirm);
		mSubmit = (Button) findViewById(R.id.btn_submit);
		
		mSubmit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				onSubmit();
			}
		});
		
		mDialog = new Dialog(this, R.style.sendRequestDialog);
		mDialog.setContentView(R.layout.ykn_dialog);
		mDialog.setOnCancelListener(new OnCancelListener() {
			
			@Override
			public void onCancel(DialogInterface dialog) {
				
			}
		});
	}
	
	private void onSubmit() {
		if (TextUtils.isEmpty(mPassword.getText().toString().trim()) || !RegexUtil.isPassword(mPassword.getText().toString().trim())) {
			Toast.makeText(mContext, getString(R.string.ykn_reset_error1), Toast.LENGTH_SHORT).show();
		} else if (TextUtils.isEmpty(mConfirm.getText().toString().trim())) {
			Toast.makeText(mContext, getString(R.string.ykn_reset_error1), Toast.LENGTH_SHORT).show();
		} else if (!mPassword.getText().toString().trim().equals(mConfirm.getText().toString().trim())) {
			Toast.makeText(mContext, getString(R.string.ykn_reset_error2), Toast.LENGTH_SHORT).show();
		} else {
			// 提交
			showSubmitDialog();
			RegisterManager.getRegisterManager().modifyForgetPwd(strUser, Md5.toMD5(mPassword.getText().toString().trim()), strCaptcha, new responseChangePassword());
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

	private void showSuccessDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(ResetPasswordActivity.this);
		builder.setMessage(R.string.ykn_reset_message);
		builder.setNegativeButton(R.string.ykn_reset_login, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				jumpPage();
			}
		});
		builder.show();
	}
	
	private void jumpPage() {
		// 验证成功后跳转到 私信登陆 页面
		LoginSixinActivity.show(mContext, LoginSixinActivity.LOGIN_SIXIN, strUser);
		ResetPasswordActivity.this.finish();
		// 注册成功销毁填写帐号页面
		RenrenChatApplication.removeActivity(RegisterAccountActivity.sActivity);
	}
	
	class responseChangePassword extends INetReponseAdapter {
    	
    	public responseChangePassword() {
    		
    	}
		
		@Override
		public void onSuccess(INetRequest req, JsonObject data) {
			SystemUtil.log("sunnyykn", "重置密码成功:"+data);
			CommonUtil.toast("重置密码成功！");
			
			dismissSubmitDialog();
			
			RenrenChatApplication.sHandler.post(new Runnable() {
				
				@Override
				public void run() {
					showSuccessDialog();
				}
			});
			// showSuccessDialog();
		}
		
		@Override
		public void onError(INetRequest req, JsonObject data) {
			SystemUtil.log("sunnyykn", "重置密码失败信息:"+data);
			CommonUtil.toast("重置密码失败！");
			
			dismissSubmitDialog();
		}
	}
	
}
