package com.renren.mobile.chat.ui.account;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.WindowManager;

import com.renren.mobile.account.LoginControlCenter;
import com.renren.mobile.chat.R;
import com.renren.mobile.chat.RenrenChatApplication;
import com.renren.mobile.chat.ui.BaseActivity;

public class SingleLoginActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		showDialog(0, getIntent().getExtras());
	}

	@Override
	protected Dialog onCreateDialog(int id, Bundle args) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.login_error_title);
		builder.setMessage(args.getString("msg"));
		builder.setCancelable(false);  
		builder.setPositiveButton(R.string.VoiceOnClickListenner_java_3,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						LoginControlCenter.getInstance().logout(
								RenrenChatApplication.getmContext());
						LoginSixinActivity.show(SingleLoginActivity.this, LoginSixinActivity.LOGIN_SIXIN);
						dialog.dismiss();
						SingleLoginActivity.this.finish();
					}
				});
		return builder.create();
	}

	@Override
	protected void onPrepareDialog(int id, Dialog dialog) {

		dialog.setOnKeyListener(new android.content.DialogInterface.OnKeyListener(){  
		    @Override  
		    public boolean onKey(DialogInterface dialog, int keyCode,KeyEvent event) {  
		    	dialog.dismiss();
		        return onKeyDown(keyCode, event);  
		    }  
		});
		super.onPrepareDialog(id, dialog);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_HOME:
		case KeyEvent.KEYCODE_BACK:
		case KeyEvent.KEYCODE_CALL:
		case KeyEvent.KEYCODE_SYM:
		case KeyEvent.KEYCODE_VOLUME_DOWN:
		case KeyEvent.KEYCODE_VOLUME_UP:
		case KeyEvent.KEYCODE_STAR:
			LoginControlCenter.getInstance().logout(
					RenrenChatApplication.getmContext());
			SingleLoginActivity.this.finish();
			LoginSixinActivity.show(this, LoginSixinActivity.LOGIN_SIXIN);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onAttachedToWindow() {
		this.getWindow().setType(WindowManager.LayoutParams.TYPE_KEYGUARD);
		super.onAttachedToWindow();
	}
}
