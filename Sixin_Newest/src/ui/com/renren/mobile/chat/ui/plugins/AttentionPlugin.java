package com.renren.mobile.chat.ui.plugins;


import plugin.LocalPlugin;
import plugin.PluginInfo;
import plugin.base.Container;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import com.renren.mobile.chat.ui.contact.feed.ChatFeedActivity;
import com.renren.mobile.chat.ui.contact.feed.ChatFeedAdapter;
import com.renren.mobile.chat.ui.contact.feed.ChatFeedDataManager;
import com.renren.mobile.chat.ui.contact.feed.ObserverImpl;
import com.renren.mobile.chat.ui.plugins.AttentionSettingView.AttentionDownLoadCompletedListener;


public class AttentionPlugin extends LocalPlugin implements AttentionDownLoadCompletedListener{

	public AttentionPlugin(Container container, PluginInfo pluginInfo) {
		super(container, pluginInfo);
	}

	private ViewGroup mRootView;

	@Override
	public boolean clearHistory() {
		// TODO Auto-generated method stub
		ChatFeedAdapter.mDataList.clear();
		ChatFeedActivity.needRefresh = true;
		return true;
	}

	@Override
	public String getIcon(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getNotificationCount() {
		return ObserverImpl.getInstance().getFeedSize();
	}

	@Override
	public void initSettingView(ViewGroup arg0) {
		mRootView = arg0;
		AttentionSettingView attentionSettingView = new AttentionSettingView();
		arg0.addView(attentionSettingView.getView());
		attentionSettingView.setOnAttentionDownLoadCompletedListener(this);
	}

	@Override
	public void onMessageUsingIndex(long arg0, String arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onMessageUsingUID(long arg0, String arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void start(Context context, Bundle bundle) {
		// TODO Auto-generated method stub
		Intent intent = new Intent(context,ChatFeedActivity.class);
		if(bundle != null){
			intent.putExtra("plugin_id",bundle.getString("plugin_id"));
		}
		context.startActivity(intent);
	}

    @Override
	public void onCompleted(View view) {
		mRootView.removeAllViews();
		mRootView.addView(view);
	}

}
