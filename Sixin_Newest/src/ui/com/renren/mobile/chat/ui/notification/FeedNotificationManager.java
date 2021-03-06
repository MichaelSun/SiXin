package com.renren.mobile.chat.ui.notification;
import java.util.ArrayList;
import java.util.List;

import plugin.DBBasedPluginManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Observable;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.util.Log;

import com.common.emotion.emotion.EmotionString;
import com.common.network.DomainUrl;
import com.common.statistics.BackgroundUtils;
import com.renren.mobile.chat.R;
import com.renren.mobile.chat.RenrenChatApplication;
import com.renren.mobile.chat.activity.RenRenChatActivity;
import com.renren.mobile.chat.base.inter.NEWSFEED_TYPE;
import com.renren.mobile.chat.base.util.SystemUtil;
import com.renren.mobile.chat.common.RRSharedPreferences;
import com.renren.mobile.chat.newsfeed.NewsFeedFactory;
import com.renren.mobile.chat.newsfeed.NewsFeedWarpper;
import com.renren.mobile.chat.ui.FeedNotificationCancel;
import com.renren.mobile.chat.ui.MainFragmentActivity;
import com.renren.mobile.chat.ui.contact.C_ContactsData;
import com.renren.mobile.chat.ui.contact.ContactBaseModel;
import com.renren.mobile.chat.ui.contact.ContactModel;
import com.renren.mobile.chat.ui.contact.ContactModelFactory;
import com.renren.mobile.chat.ui.contact.IDownloadContactListener;
import com.renren.mobile.chat.ui.contact.feed.ChatFeedDataManager;
import com.renren.mobile.chat.ui.contact.feed.ChatFeedModel;
import com.renren.mobile.chat.ui.contact.feed.ChatFeedPhotoModel;
import com.renren.mobile.chat.ui.contact.feed.FeedCallback;
import com.renren.mobile.chat.ui.contact.feed.FeedCallbackSource;
import com.renren.mobile.chat.ui.contact.feed.ObserverImpl;
import com.renren.mobile.chat.ui.setting.SettingDataManager;
import com.renren.mobile.chat.util.ObservableImpl;
/**
 * 
 * @author rubin.dong@renren-inc.com
 * 新鲜事Notification
 *
 */
import com.renren.mobile.chat.R;
import com.renren.mobile.chat.RenrenChatApplication;
public class FeedNotificationManager implements FeedCallback{
	private static FeedNotificationManager mFeedNoiticationManager = new FeedNotificationManager();
	private Notification mNoitification; 
	private NotificationManager mNoitificationManager;
	private HandlerThread mFeedHandlerThread;
	private FeedHandler mFeedHandler;
	private RRSharedPreferences rRSharedPreferences;
	public static final int FEED_NOTIFICATION_ID = 6362024;
	private List<ChatFeedModel> feedList = new ArrayList<ChatFeedModel>();
	private FeedNotificationManager() {
		initFeedHanlderThread();
		ObserverImpl.getInstance().addCallback(this);
	}
	private void initFeedHanlderThread() {
		mFeedHandlerThread = new HandlerThread("handler feed thread", Process.THREAD_PRIORITY_BACKGROUND);
		mFeedHandlerThread.start();
		mFeedHandler = new FeedHandler(mFeedHandlerThread.getLooper(), RenrenChatApplication.mHandler);
	}
	public void handleNewFeed(List<ChatFeedModel> feedList) {
		if (mFeedHandlerThread == null || !mFeedHandlerThread.isAlive()) {
			initFeedHanlderThread();
		}
		Message message = mFeedHandler.obtainMessage();
		message.obj = feedList;
		message.sendToTarget();
	}
	public static FeedNotificationManager getInstance() {
		return mFeedNoiticationManager;
	}
	/*
	 * 发送feedNotification 
	 */
	public void sendFeedNotification(Context context, boolean needRoll,ChatFeedModel chatFeed) {
		if(new DBBasedPluginManager().isPluginWithPluginIdInstalled(DBBasedPluginManager.PLUGIN_ID_ATTETION) 
				&& new DBBasedPluginManager().isPluginPushOpen(DBBasedPluginManager.PLUGIN_ID_ATTETION)){
			mNoitificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
			rRSharedPreferences = new RRSharedPreferences(context);
			mNoitification = new Notification();
			if (feedList.size() > 0) {
				if (needRoll) {
					mNoitification.ledARGB = 0xFF0000ff;
					mNoitification.ledOnMS = 200;
					mNoitification.ledOffMS = 200;
					mNoitificationManager.cancel(FEED_NOTIFICATION_ID);
					int flag = SettingDataManager.getInstance().mRemindState;
					/*
					 * 设置声音 震动,详见SettingScreen
					 * @see com.renren.mobile.chat.ui.setting.SettingScreen
					 */
					//如果设置中开启震动 或者 声音加震动  都为Notification属性设置成震动 
					if(flag == 2 || flag == 3){
						mNoitification.defaults = 2;
					}
					if(chatFeed.getType() == NEWSFEED_TYPE.FEED_STATUS_UPDATE){
						mNoitification.tickerText = chatFeed.getUserName() + " : "+RenrenChatApplication.getmContext().getResources().getString(R.string.FeedNotificationManager_java_1);		//FeedNotificationManager_java_1=发布了一条状态; 
					}else if(chatFeed.getType() == NEWSFEED_TYPE.FEED_PHOTO_PUBLISH_ONE){
						mNoitification.tickerText = chatFeed.getUserName() + " : "+RenrenChatApplication.getmContext().getResources().getString(R.string.FeedNotificationManager_java_2);		//FeedNotificationManager_java_2=发布了一张图片; 
					}else if(chatFeed.getType() == NEWSFEED_TYPE.FEED_PHOTO_PUBLISH_MORE){
						mNoitification.tickerText = chatFeed.getUserName() + " : "+RenrenChatApplication.getmContext().getResources().getString(R.string.FeedNotificationManager_java_3);		//FeedNotificationManager_java_3=发布了多张图片; 
					}
				}
				mNoitification.icon = R.drawable.notification_chat;
				mNoitification.when = chatFeed.getTime();
				mNoitification.flags = Notification.FLAG_AUTO_CANCEL;
				mNoitification.number = ObserverImpl.getInstance().getFeedSize();
				Intent cancelIntent = new Intent(context, FeedNotificationCancel.class);
				Bundle bundle = new Bundle();
//				ContactModel contactInChat = new ContactModel();
//				contactInChat.mUserId = chatFeed.getUserId();
//				contactInChat.mContactName = chatFeed.getUserName();
//				contactInChat.mRelation = 0;
//				contactInChat.mHeadUrl = chatFeed.getHeadUrl();
//				ContactModel contactModel = C_ContactsData.getInstance().getSiXinContact(contactInChat.mUserId,null);
//				if(contactModel != null){
//					contactInChat.mLargeHeadUrl = contactModel.mLargeHeadUrl;
//					contactInChat.mBirth = contactModel.mBirth;
//					contactInChat.mGender = contactModel.mGender;
//				//	contactInChat.mOnlinestatus = contactModel.mOnlinestatus;
//				}
				
				
				
				ContactBaseModel contactmodel = C_ContactsData.getInstance().getContact(chatFeed.getUserId(), DomainUrl.RENREN_SIXIN_DOMAIN);
				if (contactmodel == null) {
					contactmodel = ContactModelFactory.createContactModel(DomainUrl.RENREN_SIXIN_DOMAIN);
					contactmodel.setmContactName(chatFeed.getUserName());
					contactmodel.mUserId = chatFeed.getUserId();
				}
				NewsFeedWarpper message = NewsFeedFactory.getInstance().obtainNewsFeedModel();
				message.adapter(chatFeed);
				bundle.putSerializable("ContactInChat", contactmodel);
				bundle.putSerializable("ChatFeedModel", chatFeed);
				bundle.putSerializable("NewsFeedWarpper", message);
				cancelIntent.putExtras(bundle);
				cancelIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				cancelIntent.setAction("" + System.currentTimeMillis());
				PendingIntent pendingIntent =  PendingIntent.getActivity(context, 0, cancelIntent, PendingIntent.FLAG_UPDATE_CURRENT);
				if(chatFeed.getType() == NEWSFEED_TYPE.FEED_STATUS_UPDATE){
					mNoitification.setLatestEventInfo(context,ObserverImpl.getInstance().getFeedSize() + "条未读新鲜事" ,chatFeed.getUserName() + RenrenChatApplication.getmContext().getResources().getString(R.string.FeedNotificationManager_java_4),pendingIntent);		//FeedNotificationManager_java_4=发布了状态; 
				}else if(chatFeed.getType() == NEWSFEED_TYPE.FEED_PHOTO_PUBLISH_ONE){
					mNoitification.setLatestEventInfo(context, ObserverImpl.getInstance().getFeedSize()  + "条未读新鲜事",chatFeed.getUserName() + RenrenChatApplication.getmContext().getResources().getString(R.string.FeedNotificationManager_java_2),pendingIntent);		//FeedNotificationManager_java_2=发布了一张图片; 
				}else if(chatFeed.getType() == NEWSFEED_TYPE.FEED_PHOTO_PUBLISH_MORE){
					ChatFeedPhotoModel chatFeedPhoto = (ChatFeedPhotoModel)chatFeed;
					mNoitification.setLatestEventInfo(context,ObserverImpl.getInstance().getFeedSize()  + "条未读新鲜事"  ,chatFeed.getUserName() + RenrenChatApplication.getmContext().getResources().getString(R.string.FeedNotificationManager_java_3),pendingIntent);		//FeedNotificationManager_java_3=发布了多张图片; 
				}
				mNoitificationManager.notify(FEED_NOTIFICATION_ID, mNoitification);
			} else {
				mNoitificationManager.cancel(FEED_NOTIFICATION_ID);
			}
		}
	}
	/*
	 * 离开feed列表页面 调用该方法
	 */
	public void sendFeedNotification(){
		if(mCallbackSource != null){
			ArrayList<ChatFeedModel> chatFeedList = mCallbackSource.getAllFeed();
			feedList = chatFeedList;
		}
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						if(feedList != null && feedList.size()>0){
							for(ChatFeedModel chatFeed : feedList){
								sendFeedNotification(RenrenChatApplication.mContext, false, chatFeed);
							}
						}
					} catch (Exception e) {}
				}
			}).start();
	}
	private final class FeedHandler extends Handler{
		public FeedHandler(Looper looper, Handler handler) {
			super(looper);
		}
		@Override
		public void handleMessage(Message msg) {
			List<ChatFeedModel>	tempFeedList = (List<ChatFeedModel>)msg.obj;
				for (ChatFeedModel chatFeed : tempFeedList) {
					if(!ChatFeedDataManager.hasItemFeed(feedList,chatFeed)){
						feedList.add(chatFeed);
					}
				}
				
				if(RenrenChatApplication.feedIndex != 1){
					for(ChatFeedModel chatFeed : tempFeedList){
						sendFeedNotification(RenrenChatApplication.mContext, true,chatFeed);
					}
				}
			/**
			 * tab的数目展示放在ChatFeedScreen
			 */
		}
	}
	public synchronized FeedCallbackSource getFeedCallbackSource(){
		return mCallbackSource;
	}
	public synchronized List<ChatFeedModel> getUnReadChatFeedList(){
		return feedList;
	}
	public synchronized void clearChatFeedList(){
		if(feedList != null && feedList.size()>0){
			feedList.clear();
		}
	}
	/*
	 * clear所有Notification根据Feed id
	 */
	public synchronized void clearAllNotification() {
		mNoitificationManager = (NotificationManager) RenrenChatApplication.mContext.getSystemService(Context.NOTIFICATION_SERVICE);
		mNoitificationManager.cancel(FEED_NOTIFICATION_ID);
//		new Thread() {
//			@Override
//			public void run() {
//				try {
//					if (feedList != null && feedList.size() > 0) {
//						for (ChatFeedModel chatFeed : feedList) {
//							if (mNoitificationManager != null) {
//								mNoitificationManager.cancel((int) chatFeed
//										.getId());
//							}
//						}
//					}
//				} catch (Exception e) {
//				}
//			};
//		}.start();
	}
	@Override
	public void onCallback(List<ChatFeedModel> newFeed) {
		this.handleNewFeed(newFeed);
		if (RenrenChatApplication.isMainFragementActivity == true) {
			Intent intent = new Intent(
					MainFragmentActivity.REFRESH_NEW_FEED_RECEIVER_ACTION);
			RenrenChatApplication.mContext.sendBroadcast(intent);
		}
	}
	FeedCallbackSource mCallbackSource = null;
	@Override
	public void registorFeedCallbackSource(FeedCallbackSource source) {
		mCallbackSource = source;
	}
}
