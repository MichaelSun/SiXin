package com.renren.mobile.chat.ui.contact.feed;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;

import com.renren.mobile.chat.ui.contact.attention.AttentionAddActiviy;

public class ChatFeedEventManager {

	private ChatFeedScreen chatFeedScreen;

	public ChatFeedEventManager(ChatFeedScreen chatFeedScreen) {
		this.chatFeedScreen = chatFeedScreen;
		registorEvents();
	}

	public void registorEvents() {
		chatFeedScreen.mChatFeedHolder.mFeedNoAttentionBtn
				.setOnClickListener(FeedNoAttentionBtnOnClickListener);
		chatFeedScreen.mChatFeedHolder.mBaseHeaderLayout
				.setOnClickListener(FeedHeaderNewFeedListener);
		chatFeedScreen.mChatFeedHolder.mBaseFooterLayout
				.setOnClickListener(FeedFooterMoreOnClickListener);
	}

	private OnClickListener FeedHeaderNewFeedListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			chatFeedScreen.mChatFeedHolder.hideAllNoticeView();
			chatFeedScreen.mChatFeedDataManager.clearNewFeedNotice();
		}
	};

	private OnClickListener FeedFooterMoreOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			/**
			 * 加载更多
			 */
			if (chatFeedScreen.mAdapter.getCount() < ChatFeedDataManager.CHAT_FEED_MAX_NUM) {
				chatFeedScreen.mChatFeedHolder.mBaseFooterLayout
						.setFeedFooterLoading(true);
				chatFeedScreen.mChatFeedDataManager.getFeedList(false);
			}
		}
	};

	public OnClickListener FeedNoAttentionBtnOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			/**
			 * 没有关注好友，点击以后的事件
			 */
			Intent intent = new Intent();
			intent.setClass(chatFeedScreen.context, AttentionAddActiviy.class);
			intent.putExtra("refresh", true);
			chatFeedScreen.context.startActivity(intent);
		}
	};

}
