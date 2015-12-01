package com.renren.mobile.chat.ui.account;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;

import com.common.manager.ContactManager;
import com.common.manager.ContactManager.ContactListener;
import com.common.manager.ContactManager.ContactResponse;
import com.core.json.JsonArray;
import com.core.json.JsonObject;
import com.renren.mobile.chat.R;
import com.renren.mobile.chat.RenrenChatApplication;
import com.renren.mobile.chat.base.util.SystemUtil;
import com.renren.mobile.chat.ui.BaseActivity;
import com.renren.mobile.chat.ui.MainFragmentActivity;
import com.renren.mobile.chat.view.BaseTitleLayout;
import com.renren.mobile.chat.view.BaseTitleLayout.FUNCTION_BUTTON_TYPE;

public class UploadContactActivity extends BaseActivity implements ContactListener {
	
	public static final String SHOW_BY_CONTACT = "show_by_contact";
	
	public static final String SHOW_BY_REGISTER = "show_by_register";
	
	private LinearLayout mTitle;
	
	private Dialog mDialog;
	
	private Context mContext;
	
	private BaseTitleLayout mBaseTitleLayout;
	
	private Button mSubmit;
	
	private boolean mFlagContact = false;
	
	private boolean mFlagRegister = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.setContentView(R.layout.ykn_upload_contact);
		mContext = this;
		mFlagContact = getIntent().getBooleanExtra(SHOW_BY_CONTACT, false);
		mFlagRegister = getIntent().getBooleanExtra(SHOW_BY_REGISTER, false);
		initViews();
	}
	
	private void initViews() {
		mSubmit = (Button) findViewById(R.id.btn_submit);
		mTitle = (LinearLayout) findViewById(R.id.title);
		
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
    	
    	mBaseTitleLayout.setTitleMiddle(getString(R.string.ykn_uploadcontact_title));
    	// title back
    	mBaseTitleLayout.getTitleLeft().setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				UploadContactActivity.this.finish();
			}
		});
    	
    	if (mFlagContact) {
    		mBaseTitleLayout.getTitleLeft().setVisibility(View.VISIBLE);
    	} else {
    		mBaseTitleLayout.getTitleLeft().setVisibility(View.GONE);
    	}
    	
    	if (mFlagRegister) {
    		mBaseTitleLayout.setButtonString(mBaseTitleLayout.getRightOtherBtn(), R.string.ykn_uploadcontact_title_right);
        	mBaseTitleLayout.setRightButtonBackground(FUNCTION_BUTTON_TYPE.SEND);
        	mBaseTitleLayout.getRightOtherBtn().setOnClickListener(new OnClickListener() {
    			
    			@Override
    			public void onClick(View v) {
    				MainFragmentActivity.show(UploadContactActivity.this, MainFragmentActivity.Tab.CONTRACT);
    				UploadContactActivity.this.finish();
    			}
    		});
    	}
    	
    	mSubmit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showSubmitDialog();
				ContactResponse response = new ContactResponse(UploadContactActivity.this);
				ContactManager.getInstance().matchContacts(false, response);
				
				RenrenChatApplication.getInstance().sHandler.postDelayed(new Runnable() {
					
					@Override
					public void run() {
						try {
							Thread.sleep(2000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						} finally {
							jumpBack();
						}
						
					}
				}, 2000);
			}
		});
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			SystemUtil.logykn("捕捉到返回按钮事件！");
			jumpBack();
		}
		return true;
	}
	
	private void jumpBack() {
		dismissSubmitDialog();
		if (mFlagRegister) {
			MainFragmentActivity.show(UploadContactActivity.this, MainFragmentActivity.Tab.CONTRACT);
		}
		UploadContactActivity.this.finish();
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

	public static void show(Context context) {
		Intent intent = new Intent(context, UploadContactActivity.class);
		context.startActivity(intent);
	}
	
	public static void showByRegister(Context context) {
		Intent intent = new Intent(context, UploadContactActivity.class);
		intent.putExtra(SHOW_BY_REGISTER, true);
		context.startActivity(intent);
	}
	
	public static void showByContact(Context context) {
		Intent intent = new Intent(context, UploadContactActivity.class);
		intent.putExtra(SHOW_BY_CONTACT, true);
		context.startActivity(intent);
	}

	@Override
	public void onReceiveFull(JsonArray friends) {
		
	}

	@Override
	public void onReceiveEmpty() {
		
	}


	@Override
	public void onReceiveError(JsonObject data) {
		
	}
	
}
