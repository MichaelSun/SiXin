package com.renren.mobile.chat.ui.account;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.common.manager.LoginManager;
import com.common.mcs.INetReponseAdapter;
import com.common.mcs.INetRequest;
import com.common.mcs.McsServiceProvider;
import com.core.json.JsonObject;
import com.core.util.Md5;
import com.core.util.SystemService;
import com.renren.mobile.chat.R;
import com.renren.mobile.chat.RenrenChatApplication;
import com.renren.mobile.chat.base.util.SystemUtil;
import com.renren.mobile.chat.common.ResponseError;
import com.renren.mobile.chat.ui.BaseScreen;
import com.renren.mobile.chat.ui.contact.ContactOptionActivity;
import com.renren.mobile.chat.ui.setting.SettingForwardActivity;

public class BindInfoScreen extends BaseScreen {
	
	private TextView mText;
	
	private EditText mBindContent;
	
	private TextView mHint;
	
	private Button mBindConfirmButton;
	
	private String mAccount;
	
	private Dialog mDialog;
	
	private int mBindType; // Email or Mobile
	
	public static final String FLAG_NORMAL = "flag_normal";
	/**是否正常跳转 */
	private boolean normal;
	
	public BindInfoScreen(Activity activity) {
		super(activity);
		normal = activity.getIntent().getBooleanExtra(FLAG_NORMAL, false);
		initView();
		initTitle();
	}
	
	/**
	 * Title设置
	 */
	public void initTitle() {
		switch (mBindType) {
		case RegisterAccountActivity.FLAG_BIND_EMAIL:
			this.getTitle().getTitleLeft().setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					jumpBack();
				}
			});
			this.getTitle().setTitleMiddle(mActivity.getResources().getString(R.string.ykn_bindinfo_email_title));
			break;
		case RegisterAccountActivity.FLAG_BIND_PHONE:
			this.getTitle().getTitleLeft().setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					jumpBack();
				}
			});
			this.getTitle().setTitleMiddle(mActivity.getResources().getString(R.string.ykn_bindinfo_phone_title));
			break;
		}
	}
	
	public void jumpBack() {
		SystemUtil.logykn("返回跳转事件！");
		if(!normal){
			SettingForwardActivity.show(mActivity, SettingForwardActivity.ForwardScreenType.SELFINFO_SETTING_SCREEN);
			mActivity.finish();
		} else {
			ContactOptionActivity.show(mActivity);
			mActivity.finish();
		}
	}
	
	/*private void showConfirmDialog() {
		final EditText password = new EditText(mActivity);
		password.setHint(R.string.ykn_bindinfo_password);
		password.setSingleLine(true);
		password.setTransformationMethod(PasswordTransformationMethod.getInstance());
		AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
		builder.setMessage(mActivity.getString(R.string.ykn_account_message));
		builder.setView(password);
		builder.setNegativeButton(R.string.ykn_bindinfo_cancel, null);
		builder.setPositiveButton(R.string.ykn_bindinfo_confirm, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (Md5.toMD5(password.getText().toString()).equals(LoginManager.getInstance().getLoginInfo().mPassword)) {
					switchBindinfo();
				} else {
					Toast.makeText(mActivity, mActivity.getString(R.string.ykn_bindinfo_error), Toast.LENGTH_SHORT).show();
				}
			}

		});
		builder.show();
	}*/
	
	// 比对服务器密码
	private void showConfirmDialog() {
		final EditText password = new EditText(mActivity);
		password.setHint(R.string.ykn_bindinfo_password);
		password.setSingleLine(true);
		password.setTransformationMethod(PasswordTransformationMethod.getInstance());
		AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
		builder.setMessage(mActivity.getString(R.string.ykn_account_message1));
		builder.setView(password);
		builder.setNegativeButton(R.string.ykn_bindinfo_cancel, null);
		builder.setPositiveButton(R.string.ykn_bindinfo_confirm, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				showSubmitDialog();
				McsServiceProvider.getProvider().getPasswordToken(new responseGetPasswordToken(), Md5.toMD5(password.getText().toString()));
			}

		});
		builder.show();
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
	
	class responseGetPasswordToken extends INetReponseAdapter {
		
		@Override
		public void onSuccess(INetRequest req, JsonObject data) {
			SystemUtil.logykn("password_token：" + data.toString());
			SystemUtil.logykn("获取密码令牌成功");
			//CommonUtil.toast("获取密码令牌成功！");
			
			LoginManager.getInstance().getLoginInfo().mPasswordToken = data.getString("password_token");
			SystemUtil.logykn("password_token：" + LoginManager.getInstance().getLoginInfo().mPasswordToken);
			
			dismissSubmitDialog();
			
			// jump
			switchBindinfo();
		}
		
		@Override
		public void onError(INetRequest req, JsonObject data) {
			SystemUtil.logykn("password_token：" + data.toString());
			SystemUtil.logykn("获取密码令牌失败");
			//CommonUtil.toast("获取密码令牌失败！");
			ResponseError.showError(data);
			
			dismissSubmitDialog();
			
			RenrenChatApplication.sHandler.post(new Runnable() {
				
				@Override
				public void run() {
					showConfirmDialog();
				}
			});
		}
	}
	
	private void switchBindinfo() {
		switch (mBindType) {
		case RegisterAccountActivity.FLAG_BIND_EMAIL:
			RegisterAccountActivity.show(mActivity, RegisterAccountActivity.FLAG_BIND_EMAIL);
			break;

		case RegisterAccountActivity.FLAG_BIND_PHONE:
			RegisterAccountActivity.show(mActivity, RegisterAccountActivity.FLAG_BIND_PHONE, normal);
			break;
		}
		mActivity.finish();
	}
	
	public void initView() {
		mAccount = mActivity.getIntent().getExtras().getString(BindInfoActivity.ACCOUNT);
		mBindType = mActivity.getIntent().getExtras().getInt(RegisterAccountActivity.FLAG_TYPE);
		
		mDialog = new Dialog(mActivity, R.style.sendRequestDialog);
		mDialog.setContentView(R.layout.ykn_dialog);
		mDialog.setOnCancelListener(new OnCancelListener() {
			
			@Override
			public void onCancel(DialogInterface dialog) {
				
			}
		});
		
		View view = SystemService.sInflaterManager.inflate(R.layout.bind_info_layout, null);
		mText = (TextView) view.findViewById(R.id.bind_info_type);
		mBindContent = (EditText) view.findViewById(R.id.bind_info_content);
		mHint = (TextView) view.findViewById(R.id.bind_info_info);
		mBindConfirmButton = (Button) view.findViewById(R.id.bind_info_confirm);
		this.setContent(view);
		
		mBindContent.setText(mAccount);
		
		mBindConfirmButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showConfirmDialog();
			}
		});
		
		switch (mBindType) {
		case RegisterAccountActivity.FLAG_BIND_EMAIL:
			mText.setText(mActivity.getString(R.string.ykn_binfinfo_email_guidance));
			mHint.setText(mActivity.getString(R.string.ykn_bindinfo_email_hint));
			mBindConfirmButton.setText(mActivity.getString(R.string.ykn_bindinfo_email_button));
			break;

		case RegisterAccountActivity.FLAG_BIND_PHONE:
			mText.setText(mActivity.getString(R.string.ykn_bindinfo_phone_guidance));
			mHint.setText(mActivity.getString(R.string.ykn_bindinfo_phone_hint));
			mBindConfirmButton.setText(mActivity.getString(R.string.ykn_bindinfo_phone_button));
			break;
		}
	}
}
