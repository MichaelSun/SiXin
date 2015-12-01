package com.renren.mobile.chat.ui.account;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;

import com.core.util.CommonUtil;
import com.renren.mobile.chat.base.util.SystemUtil;
import com.renren.mobile.chat.ui.BaseActivity;

public class BindInfoActivity extends BaseActivity {
	
	public static final String ACCOUNT = "account";
	
	private BindInfoScreen bindinfoScreen;
	
    public static void show(Context context) {
    	Intent intent =new Intent(context,BindInfoActivity.class);
    	context.startActivity(intent);
    }
    
    public static void show(Context context, String account, int bindtype) {
    	Intent intent =new Intent(context,BindInfoActivity.class);
    	intent.putExtra(RegisterActivity.FLAG_TYPE, bindtype);
    	intent.putExtra(ACCOUNT, account);
    	context.startActivity(intent);
    }
    
    public static void show(Context context, String account, int bindtype, boolean normal) {
    	Intent intent =new Intent(context,BindInfoActivity.class);
    	intent.putExtra(RegisterActivity.FLAG_TYPE, bindtype);
    	intent.putExtra(BindInfoScreen.FLAG_NORMAL, normal);
    	intent.putExtra(ACCOUNT, account);
    	context.startActivity(intent);
    }
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		bindinfoScreen = new BindInfoScreen(this);
		setContentView(bindinfoScreen.getScreenView());
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			SystemUtil.logykn("捕捉到返回按钮事件！");
			bindinfoScreen.jumpBack();
			return true;
		} else {
			SystemUtil.logykn("未捕捉到返回按钮事件！");
			return super.onKeyDown(keyCode, event);
		}
	}

}
