package com.renren.mobile.chat.ui.plugins;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;

import com.renren.mobile.chat.R;
import com.renren.mobile.chat.RenrenChatApplication;
import com.renren.mobile.chat.ui.BaseScreen;
import com.renren.mobile.chat.ui.contact.feed.ChatFeedAdapter;
import com.renren.mobile.chat.ui.contact.feed.ChatFeedModel;
import com.renren.mobile.chat.ui.contact.feed.ObserverImpl;
import com.renren.mobile.chat.view.BaseTitleLayout;
import com.renren.mobile.chat.view.BaseTitleLayout.FUNCTION_BUTTON_TYPE;

/** 
 * 
 * @author yanfei.wu
 * @version 2012-8-18
 */

public class PluginScreen extends BaseScreen {
	
	public Context context;
	public PluginView pluginView = new PluginView();

	public PluginScreen(Activity activity) {
		super(activity);
		context = activity;
		initTitle();
		initView();
	}
	
	public void initView(){
		setContent(pluginView.getView(context));
	}
	@Override
	public void clear() {
		super.clear();
		pluginView.clear();
	};
	

	private void initTitle() {
		this.getTitle().setTitleMiddle(RenrenChatApplication.getmContext().getResources().getString(R.string.y_main_layout_9));		//FindFriendScreen_java_2=工具; 
		BaseTitleLayout title = getTitle();
		//title.setTitleButtonLeftBackVisibility(false);
		title.setTitleFunctionButtonBackground(FUNCTION_BUTTON_TYPE.PLUGIN);
		title.setTitleFunctionButtonListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context, PluginAllActivity.class);
				context.startActivity(intent);
			}
		});
	}
	
	/**
	 * 更新界面
	 */
	public void updateUI(){
		pluginView.updateUI();
	}
	
	@Override
	public void refresh(){
		updateUI();
	}
	
	public void setRequestCount(){
		pluginView.setRequestCount();
	}
}
