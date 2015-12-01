package com.renren.mobile.chat.ui.contact.attention;


import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Window;

import com.renren.mobile.chat.RenrenChatApplication;
import com.renren.mobile.chat.ui.BaseActivity;
import com.renren.mobile.chat.ui.contact.C_ContactsData;
import com.renren.mobile.chat.ui.contact.ContactModel;
import com.renren.mobile.chat.ui.contact.feed.ChatFeedScreen;

/**
 * 
 * @author eason Lee
 * @version v2.0
 * @注意  目前实现基本功能  但此包需要重构  应该可以复用
 * @date 4.10.2012
 *
 */
public class AttentionAddActiviy extends BaseActivity {
	AttentionAddScreen mScreen ;
	boolean refresh;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		RenrenChatApplication.pushStack(AttentionAddActiviy.this);
		mScreen = new AttentionAddScreen(this);
		setContentView(mScreen.getScreenView());
		refresh = getIntent().getBooleanExtra("refresh", false);
    }
    public static void show(Context context){
    	Intent intent =new Intent(context,AttentionAddActiviy.class);
    	context.startActivity(intent);
    }
    
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
//			FocusCache.makeGridViewTypeOneToBottom();
			this.finish();
		} else if (keyCode == KeyEvent.KEYCODE_VOLUME_UP || keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
			return super.onKeyDown(keyCode, event);
		} else if (keyCode == KeyEvent.KEYCODE_MENU) {
			return super.onKeyDown(keyCode, event);
		}

		return true;
	}
	
	
	@Override
	protected void onPause() {
		mScreen.onPause();
		super.onPause();
	}
	
	@Override
	protected void onDestroy() {
		RenrenChatApplication.popStack(AttentionAddActiviy.this);
		List<ContactModel> contactList = C_ContactsData.getInstance().get(C_ContactsData.TYPE.ALL_CONTACTS);
		for(int i=0; i<contactList.size(); i++){
			contactList.get(i).unregitor();
		}
		super.onDestroy();
	}
    
}
