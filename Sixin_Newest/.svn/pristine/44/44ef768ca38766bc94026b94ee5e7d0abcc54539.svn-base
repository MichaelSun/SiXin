package com.renren.mobile.chat.ui.contact.feed;

import java.io.IOException;
import java.util.List;
import java.util.Observable;

import android.content.Intent;
import android.view.View;

import com.common.mcs.INetRequest;
import com.common.mcs.INetResponse;
import com.common.mcs.McsServiceProvider;
import com.core.json.JsonArray;
import com.core.json.JsonObject;
import com.core.json.JsonValue;
import com.renren.mobile.chat.RenrenChatApplication;
import com.renren.mobile.chat.common.ResponseError;
import com.renren.mobile.chat.ui.MainFragmentActivity;

/**
 * @author liuchao
 */

public class ChatFeedDataManager extends Observable implements FeedCallback {

	public ChatFeedScreen mChatFeedScreen;

	public static final int CHAT_FEED_MAX_NUM = 160;

	public static final int CHAT_FEED_PAGESIZE = 20;

	public static final int CHAT_FEED_DEFAULT_PAGE = 1;

	public static int CHAT_FEED_CURRENT_PAGE = 1;

	public static final int CHAT_FEED_NODATA = 0;

	private boolean mChatAttention = true;

	// 用户标示每次请求新鲜事是否达到CHAT_FEED_PAGESIZE，如果少于此数目，表示没有更多新鲜事~
	public static boolean hasMoreData = false;

	// public static ArrayList<ChatFeedModel> mNewFeed = new
	// ArrayList<ChatFeedModel>();

	public ChatFeedDataManager(ChatFeedScreen chatFeedScreen) {
		this.mChatFeedScreen = chatFeedScreen;
		ObserverImpl.getInstance().addCallback(this);
	}

	public void getFeedList(final boolean isRefresh) {
		this.getFeedList(isRefresh, 0);
	}

	/**
	 * 获取所有的新鲜事列表
	 * **/
	public void getFeedList(final boolean isRefresh, final int pageNum) {

		INetResponse response = new INetResponse() {
			// long userId = RenrenChatApplication.user_id;
			@Override
			public void response(INetRequest req, JsonValue obj) {
				// if(userId!=RenrenChatApplication.user_id){
				// return;
				// }
				if (obj != null && obj instanceof JsonObject) {
					final JsonObject map = (JsonObject) obj;
					if (ResponseError.noError(req, map, true)) {
						JsonArray array = map.getJsonArray("feed_list");
						if (array != null) {
							try {
								mChatFeedScreen.mAdapter
										.reset(array, isRefresh);
							} catch (IOException e) {
								e.printStackTrace();
							}
							if (array.size() < CHAT_FEED_PAGESIZE) {
								hasMoreData = false;
							}
						} else {
							checkAttentionFriendsNum(isRefresh);
							hasMoreData = false;
						}
					} else {

						if (mChatFeedScreen.mAdapter.getCount() == 0) {
							mChatFeedScreen.mChatFeedHolder.showNodataView();
						}
					}
				}

				RenrenChatApplication.sHandler.post(new Runnable() {
					@Override
					public void run() {
						// mChatFeedScreen.getTitle().setTitleRefreshProgressVisibility(false);
						mChatFeedScreen.mChatFeedHolder.mBaseFooterLayout
								.setFeedFooterLoading(false);
						mChatFeedScreen.mChatFeedHolder.mLoading
								.setVisibility(View.INVISIBLE);
						mChatFeedScreen.firstCome = false;
						// 好友新鲜事多于最大数目时，隐藏底部更多栏~
						if (mChatFeedScreen.mAdapter.getCount() == CHAT_FEED_MAX_NUM) {
							mChatFeedScreen.mChatFeedHolder.mBaseFooterLayout
									.setVisibility(false);// .removeFooterView(chatFeedScreen.mChatFeedHolder.mFeedFooterLayout);
						} else {
							if (mChatFeedScreen.mAdapter.getCount() != CHAT_FEED_NODATA) {
								mChatFeedScreen.mChatFeedHolder
										.hideAllNoticeView();
								if (hasMoreData) {
									mChatFeedScreen.mChatFeedHolder.mBaseFooterLayout
											.setVisibility(true);
								} else {
									mChatFeedScreen.mChatFeedHolder.mBaseFooterLayout
											.setVisibility(false);
								}
							}
						}
						// 如果是刷新，就跳到列表头部
						if (isRefresh) {
							mChatFeedScreen.mChatFeedHolder.mBaseHeaderLayout
									.setVisibility(false);
							if (mChatFeedScreen.mAdapter.getCount() > 0) {
								mChatFeedScreen.mChatFeedHolder.mFeedListView
										.setSelection(0);
							}
							if (RenrenChatApplication.isMainFragementActivity == true) {
								Intent intent = new Intent(
										MainFragmentActivity.REFRESH_NEW_FEED_RECEIVER_ACTION);
								RenrenChatApplication.mContext
										.sendBroadcast(intent);
							}
							ChatFeedScreenHolder.mTouchListener
									.onRefreshComplete();
							ChatFeedScreenHolder.mTouchListener.LastUpdateTime = System
									.currentTimeMillis();
						} else {
							mChatFeedScreen.mChatFeedHolder.mBaseFooterLayout
									.setFeedFooterLoading(false);
						}
					}
				});

			}
		};
		if (isRefresh) {
			CHAT_FEED_CURRENT_PAGE = CHAT_FEED_DEFAULT_PAGE;
		} else {
			CHAT_FEED_CURRENT_PAGE++;
		}
		if (pageNum != 0) {
			McsServiceProvider.getProvider().getFeedList(pageNum,
					CHAT_FEED_PAGESIZE, null, response);
		} else {
			McsServiceProvider.getProvider().getFeedList(
					CHAT_FEED_CURRENT_PAGE, CHAT_FEED_PAGESIZE, null, response);
		}
		hasMoreData = true;
	}

	private void checkAttentionFriendsNum(final boolean isRefreshCheck) {
		INetResponse response = new INetResponse() {
			@Override
			public void response(INetRequest req, JsonValue obj) {
				if (obj != null && obj instanceof JsonObject) {
					final JsonObject map = (JsonObject) obj;
					if (ResponseError.noError(req, map, false)) {
						long count = map.getNum("count");
						if (count > 0) {
							mChatAttention = true;
						} else {
							mChatAttention = false;
						}
					} else {
						mChatAttention = false;
					}
				}
				RenrenChatApplication.sHandler.post(new Runnable() {

					@Override
					public void run() {
						if (mChatAttention) {
							if (isRefreshCheck) {
								mChatFeedScreen.mAdapter.clear();
								mChatFeedScreen.mAdapter.updateList();
								mChatFeedScreen.mChatFeedHolder
										.showNodataView();
							}
						} else {
							if (isRefreshCheck) {
								mChatFeedScreen.mChatFeedHolder
										.showNoAttentionView();
								mChatFeedScreen.mAdapter.clear();
								mChatFeedScreen.mAdapter.updateList();
							}
						}
					}

				});
			}
		};
		McsServiceProvider.getProvider().getFocusFriendsNUM(response);
	}

	//
	//
	// private void addNewFeedNotice(final JsonArray data){
	// RenrenChatApplication.sHandler.post(new Runnable() {
	// @Override
	// public void run() {
	// if(!mChatFeedScreen.mChatFeedHolder.mBaseHeaderLayout.getVisibility()){
	// mChatFeedScreen.mChatFeedHolder.mBaseHeaderLayout.setVisibility(true);
	// }
	// JsonObject[] objs = new JsonObject[data.size()];
	// data.copyInto(objs);
	// for (JsonObject o : objs) {
	// ChatFeedModel c = ChatFeedModelFactory.createFeedModel(o);
	// if(c != null){
	// mNewFeed.add(c);
	// }
	// }
	// updateNoticeUI();
	// }
	// });
	// }

	public void clearNewFeedNotice() {
		mChatFeedScreen.mAdapter.addListFront(ObserverImpl.getInstance()
				.getAllFeed());
		ObserverImpl.getInstance().clearFeedList();
		mFeedSource.clearFeedList();
		updateNoticeUI();
	}

	/**
	 * 更新通知信息
	 */
	public void updateNoticeUI() {
		int num = ObserverImpl.getInstance().getFeedSize();
		if (num == CHAT_FEED_NODATA) {
			// 不在刷新的时候才隐藏状态提示
			mChatFeedScreen.mChatFeedHolder.mBaseHeaderLayout
					.setVisibility(false);
		} else {
			if (!mChatFeedScreen.mChatFeedHolder.mBaseHeaderLayout
					.getVisibility()) {
				mChatFeedScreen.mChatFeedHolder.mBaseHeaderLayout
						.setVisibility(true);
			}
			mChatFeedScreen.mChatFeedHolder.mBaseHeaderLayout.setHeaderText(num
					+ "条新鲜事");
		}
//		RenrenChatApplication.mFeedUnReadCount = num;
//		if (RenrenChatApplication.isMainFragementActivity == true) {
//			Intent intent = new Intent(
//					MainFragmentActivity.REFRESH_NEW_FEED_RECEIVER_ACTION);
//			RenrenChatApplication.mContext.sendBroadcast(intent);
//		}
	}

	/**
	 * 重新进入ChatFeedScreen界面的时候调用
	 */
	public void reLoad() {
		mChatFeedScreen.mAdapter.updateList();
		updateNoticeUI();
	}

	/**
	 * 当有新鲜事推送时被调用
	 * 
	 * @param newFeed
	 *            新鲜事列表
	 */
	@Override
	public void onCallback(final List<ChatFeedModel> newFeed) {
		RenrenChatApplication.sHandler.post(new Runnable() {
			@Override
			public void run() {
				// for (ChatFeedModel chatFeedModel1 : newFeed) {
				// if (!hasItemFeed(mNewFeed, chatFeedModel1)) {
				// if (mNewFeed.size() < CHAT_FEED_MAX_NUM) {
				// mNewFeed.addAll(newFeed);
				// }
				// }
				// }
				// if (newFeed.size() > CHAT_FEED_NODATA) {
				// if (!mChatFeedScreen.mChatFeedHolder.mBaseHeaderLayout
				// .getVisibility()) {
				// mChatFeedScreen.mChatFeedHolder.mBaseHeaderLayout
				// .setVisibility(true);
				// }
				// }
				updateNoticeUI();
			}
		});
	}

	/**
	 * 去重使用
	 */
	public static boolean hasItemFeed(List<ChatFeedModel> list,
			ChatFeedModel item) {
		boolean has = false;
		for (ChatFeedModel chatFeedModel : list) {
			if (item.getId() == chatFeedModel.getId()) {
				has = true;
			}
		}
		return has;
	}

	FeedCallbackSource mFeedSource = null;

	@Override
	public void registorFeedCallbackSource(FeedCallbackSource source) {
		mFeedSource = source;
	}
}
