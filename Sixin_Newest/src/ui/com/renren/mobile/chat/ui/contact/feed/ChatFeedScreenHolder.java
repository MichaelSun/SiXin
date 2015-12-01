package com.renren.mobile.chat.ui.contact.feed;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.common.utils.Methods;
import com.renren.mobile.chat.R;
import com.renren.mobile.chat.RenrenChatApplication;
import com.renren.mobile.chat.ui.PullUpdateTouchListener;
import com.renren.mobile.chat.view.BaseFooterLayout;
import com.renren.mobile.chat.view.BaseHeaderLayout;
import com.renren.mobile.chat.view.BaseNodataLayout;

public class ChatFeedScreenHolder {
	public ListView mFeedListView = null;
	public BaseHeaderLayout mBaseHeaderLayout = null;
	public BaseFooterLayout mBaseFooterLayout = null;
	public BaseNodataLayout mFeedNoData = null;
	public BaseNodataLayout mNoNetWorkView;
	public LinearLayout mFeedNoAttention;
	public Button mFeedNoAttentionBtn;
	public View mLoading;
	public static PullUpdateTouchListener mTouchListener;
	private static final int LIST_ITEM_DIVIDER_HEIGHT = 18;

	public ChatFeedScreenHolder(Context context) {
		this.initViews(context);
	}

	private void initViews(Context context) {
		LayoutInflater inflater = LayoutInflater.from(context);
		mFeedNoAttention = (LinearLayout) inflater.inflate(
				R.layout.lc_feed_noattention, null);
		mFeedNoAttention.setVisibility(View.INVISIBLE);
		mFeedNoAttentionBtn = (Button) mFeedNoAttention
				.findViewById(R.id.lc_feed_noattention_btn);
		mNoNetWorkView = new BaseNodataLayout(context);
		mNoNetWorkView
				.setNodataText(RenrenChatApplication.getmContext()
						.getResources()
						.getString(R.string.ChatFeedScreenHolder_java_1)); // ChatFeedScreenHolder_java_1=网络连接失败，请稍后重试;
		mNoNetWorkView.setNodataImg(R.drawable.connection_fail_logo);
		mNoNetWorkView.setVisibility(false);
		mLoading = inflater.inflate(R.layout.loading_dialog, null);
		mLoading.setVisibility(View.INVISIBLE);
		mBaseFooterLayout = new BaseFooterLayout(context);
		mBaseHeaderLayout = new BaseHeaderLayout(context);
		mBaseFooterLayout.setVisibility(false);
		mBaseHeaderLayout.setVisibility(false);
		mFeedNoData = new BaseNodataLayout(context);
		mFeedNoData
				.setNodataText(RenrenChatApplication.getmContext()
						.getResources()
						.getString(R.string.ChatFeedScreenHolder_java_2)); // ChatFeedScreenHolder_java_2=暂无数据;
		mFeedNoData.setNodataImg(R.drawable.lc_nodata_img);
		mFeedNoData.setVisibility(false);
		mFeedListView = new ListView(context);
		// mFeedListView.setItemsCanFocus(true);
		// mFeedListView.setAddStatesFromChildren(true);
		// mFeedListView.setFocusableInTouchMode(true);
		mFeedListView.setVerticalFadingEdgeEnabled(false);
		mFeedListView.setCacheColorHint(android.R.color.transparent);
		mFeedListView
				.setDivider(new ColorDrawable(android.R.color.transparent));
		mFeedListView.setDividerHeight(Methods.dip2px(context,
				LIST_ITEM_DIVIDER_HEIGHT));
		mFeedListView.setOnItemClickListener(null);
		mFeedListView.setScrollingCacheEnabled(false);
		mFeedListView.setDrawingCacheEnabled(false);
		mFeedListView.setAlwaysDrawnWithCacheEnabled(false);
		mFeedListView.setWillNotCacheDrawing(true);
		mFeedListView.setSelector(R.drawable.lc_feed_item_bg_color);
	}

	public void hideAllNoticeView() {
		RenrenChatApplication.sHandler.post(new Runnable() {
			@Override
			public void run() {
				if (mFeedNoData.getVisibility()) {
					mFeedNoData.setVisibility(false);
				}
				if (mFeedNoAttention.getVisibility() == View.VISIBLE) {
					mFeedNoAttention.setVisibility(View.GONE);
				}
				if (mNoNetWorkView.getVisibility()) {
					mNoNetWorkView.setVisibility(false);
				}
			}
		});
	}

	public void showNodataView() {
		RenrenChatApplication.sHandler.post(new Runnable() {
			@Override
			public void run() {
				mFeedNoData.setVisibility(true);
				if (mFeedNoAttention.getVisibility() == View.VISIBLE) {
					mFeedNoAttention.setVisibility(View.GONE);
				}
				if (mNoNetWorkView.getVisibility()) {
					mNoNetWorkView.setVisibility(false);
				}
			}
		});
	}

	public void showNoAttentionView() {
		RenrenChatApplication.sHandler.post(new Runnable() {
			@Override
			public void run() {
				mFeedNoAttention.setVisibility(View.VISIBLE);
				if (mFeedNoData.getVisibility()) {
					mFeedNoData.setVisibility(false);
				}
				if (mNoNetWorkView.getVisibility()) {
					mNoNetWorkView.setVisibility(false);
				}
			}
		});
	}

	public void showNoNetWorkView() {
		RenrenChatApplication.sHandler.post(new Runnable() {
			@Override
			public void run() {
				mNoNetWorkView.setVisibility(true);
				if (mFeedNoData.getVisibility()) {
					mFeedNoData.setVisibility(false);
				}
				if (mFeedNoAttention.getVisibility() == View.VISIBLE) {
					mFeedNoAttention.setVisibility(View.GONE);
				}
			}
		});
	}
}
