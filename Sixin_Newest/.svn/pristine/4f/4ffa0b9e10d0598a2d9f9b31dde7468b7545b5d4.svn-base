package com.renren.mobile.chat.ui.contact;

import android.app.Activity;
import android.content.Context;
import android.os.Binder;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * 
 * @author yisong.li@renren-inc.com
 * @date 5.12.2011
 * 单独封装OnTouchListener 以便后期业务功能拓展  使得LIstView控件于业务功能需求低耦合
 *
 */

public class ContactOnTouchListener implements OnTouchListener {
	
	private EditText searchEditText;
	private Context mContext;
	public MotionEvent mMotionEvent;
	
	public ContactOnTouchListener(EditText searchEditText,
			Context mContext) {
		super();
		this.searchEditText = searchEditText;
		this.mContext = mContext;
		
	}

	

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			
			break;

		
		}
		
		//隐藏键盘
		Binder binder = (Binder) searchEditText.getWindowToken();            
		InputMethodManager inputManager = (InputMethodManager) mContext.getSystemService(Activity.INPUT_METHOD_SERVICE);
		inputManager.hideSoftInputFromWindow(binder, InputMethodManager.HIDE_NOT_ALWAYS);
		return false;
	}

}
