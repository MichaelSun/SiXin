package com.renren.mobile.chat.ui.contact.feed;

import plugin.DBBasedPluginManager;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.common.manager.LoginManager;
import com.common.manager.LoginManager.LoginInfo;
import com.renren.mobile.chat.RenrenChatApplication;
import com.renren.mobile.chat.ui.BaseActivity;
import com.renren.mobile.chat.ui.account.BindRenrenAccountActivity;

public class ChatFeedActivity extends BaseActivity {

	private static final int BIND_RENREN = 102;
	/**
	 * 新鲜事列表左右两边距离屏幕边缘的距离
	 */
	public static final int FEED_LIST_LEFT_PADDING = 0;
	public static int FEED_SINGLE_PHOTO_WIDTH = 0;
	public static int SCREEN_WIDTH = 0;
	public static int SCREEN_HEIGHT = 0;
	ChatFeedScreen mChatFeedScreen;
	public static int mPluginId = -1;
	public static boolean needRefresh = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if(!new DBBasedPluginManager()
		.isPluginWithPluginIdInstalled(DBBasedPluginManager.PLUGIN_ID_ATTETION)){
			//如果没有安装插件，或者卸载了，进入不了此界面~
			this.finish();
		}
		LoginInfo mLoginInfo = LoginManager.getInstance().getLoginInfo();
		if (mLoginInfo.mBindInfoRenren == null
				|| (mLoginInfo.mBindInfoRenren != null && TextUtils
						.isEmpty(mLoginInfo.mBindInfoRenren.mBindId))) {
			Intent bindIntent = new Intent(ChatFeedActivity.this,
					BindRenrenAccountActivity.class);
			bindIntent.putExtra(BindRenrenAccountActivity.COME_FROM,
					BindRenrenAccountActivity.BIND_FEED);
			startActivityForResult(bindIntent, BIND_RENREN);
			mChatFeedScreen = new ChatFeedScreen(this, false);
		} else {
			mChatFeedScreen = new ChatFeedScreen(this);
		}
		setContentView(mChatFeedScreen.getScreenView());
		Intent intent = getIntent();
		if (intent != null) {
			String pluginId = intent.getStringExtra("plugin_id");
			if (!TextUtils.isEmpty(pluginId)) {
				mPluginId = Integer.valueOf(pluginId);
			}
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case BIND_RENREN:
			if (resultCode == RESULT_OK) {
				mChatFeedScreen.mChatFeedHolder.mLoading
						.setVisibility(View.VISIBLE);
				mChatFeedScreen.refresh();
			} else {
				ChatFeedActivity.this.finish();
			}
			break;

		default:
			break;
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		if(!new DBBasedPluginManager()
		.isPluginWithPluginIdInstalled(DBBasedPluginManager.PLUGIN_ID_ATTETION)){
			//如果没有安装插件，或者卸载了，进入不了此界面~
			this.finish();
		}
		mChatFeedScreen.onResume();

	}

	@Override
	protected void onStop() {
		RenrenChatApplication.feedIndex = 3;
		super.onStop();
	}

}
