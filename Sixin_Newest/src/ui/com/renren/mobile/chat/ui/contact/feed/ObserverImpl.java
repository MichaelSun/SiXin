package com.renren.mobile.chat.ui.contact.feed;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import plugin.DBBasedPluginManager;

import com.common.mcs.INetRequest;
import com.common.mcs.INetResponse;
import com.common.mcs.McsServiceProvider;
import com.common.statistics.BackgroundUtils;
import com.common.utils.Bip;
import com.core.json.JsonArray;
import com.core.json.JsonObject;
import com.core.json.JsonValue;
import com.renren.mobile.chat.activity.RenRenChatActivity;
import com.renren.mobile.chat.base.GlobalValue;
import com.renren.mobile.chat.common.ResponseError;
import com.renren.mobile.chat.ui.MainFragmentActivity;
import com.renren.mobile.chat.ui.setting.SettingDataManager;
import com.renren.mobile.chat.util.ObservableImpl;

/**
 * 
 * @author cdw && liuchou
 * 
 */

public class ObserverImpl implements Observer, FeedCallbackSource {

	private static ObserverImpl sInstance = new ObserverImpl();
	private LinkedList<FeedCallback> mCallbacks = new LinkedList<FeedCallback>();
	private LinkedHashMap<Long, ChatFeedModel> mNewFeedMap = new LinkedHashMap<Long, ChatFeedModel>();

	private ObserverImpl() {
		ObservableImpl.getInstance().addObserver(this);
	}

	public static ObserverImpl getInstance() {
		return sInstance;
	}

	public void onCallback(List<ChatFeedModel> newFeed) {
		// 声音
		if (new DBBasedPluginManager()
				.isPluginWithPluginIdInstalled(DBBasedPluginManager.PLUGIN_ID_ATTETION)) {
			SettingDataManager.getInstance().obtainSwitchState();
			if (SettingDataManager.getInstance().mSoundState && (BackgroundUtils.getInstance().isAppOnForeground() || SettingDataManager.getInstance().mPushState)) {
				if (RenRenChatActivity.class.isInstance(GlobalValue.getCurrentActivity())){
					Bip.bipIncomingPush();
				}else if(MainFragmentActivity.class.isInstance(GlobalValue.getCurrentActivity()) 
						&& ((MainFragmentActivity)GlobalValue.getCurrentActivity()).mPager.getCurrentItem() == MainFragmentActivity.Tab.SIXIN){
					Bip.bipIncomingPush();
				}else if(!BackgroundUtils.getInstance().isAppOnForeground()){
					Bip.bipIncomingPush();
				}
			}
		}
		for (ChatFeedModel c : newFeed) {
			mNewFeedMap.put(c.getId(), c);
		}
		for (FeedCallback c : mCallbacks) {
			c.onCallback(newFeed);
		}
	}

	public void addCallback(FeedCallback callback) {
		if (!mCallbacks.contains(callback)) {
			mCallbacks.add(callback);
			callback.registorFeedCallbackSource(this);
		}
	}

	public void removeCallback(FeedCallback callback) {
		if (this.mCallbacks.remove(callback)) {
			callback.registorFeedCallbackSource(null);
		}
	}

	@Override
	public void update(Observable observable, Object data) {
		/*
		 * 新的新鲜事的观察者调用
		 */
		long[] id = (long[]) data;
		INetResponse response = new INetResponse() {
			@Override
			public void response(INetRequest req, JsonValue obj) {
				if (obj != null && obj instanceof JsonObject) {
					final JsonObject map = (JsonObject) obj;
					if (ResponseError.noError(req, map, false)) {
						JsonArray array = map.getJsonArray("feed_list");
						if (array != null) {
							ArrayList<ChatFeedModel> newFeed = new ArrayList<ChatFeedModel>();
							JsonObject[] objs = new JsonObject[array.size()];
							array.copyInto(objs);
							for (JsonObject o : objs) {
								ChatFeedModel c = ChatFeedModelFactory
										.createFeedModel(o);
								if (c != null) {
									newFeed.add(c);
								}
							}
							if (newFeed.size() > 0) {
								onCallback(newFeed);
							}
							for (ChatFeedModel c : newFeed) {
								mNewFeedMap.put(c.getId(), c);
							}
						}
					} else {
					}
				}
			}
		};
		McsServiceProvider.getProvider().getFeedByIds(response, id);
	}

	// @Override
	// public ArrayList<ChatFeedModel> getAllSkimedFeed() {
	// ArrayList<ChatFeedModel> feedList = new ArrayList<ChatFeedModel>();
	// for(ChatFeedModel c: mNewFeedMap.values()){
	// feedList.add(c);
	// }
	// delAllSkimedFeed();
	// return feedList;
	// }

	// public void delAllSkimedFeed(){
	//
	// LinkedHashMap<Long, ChatFeedModel> mNewFeedMapTemp = new
	// LinkedHashMap<Long, ChatFeedModel>(mNewFeedMap);
	// for(ChatFeedModel c: mNewFeedMap.values()){
	// mNewFeedMapTemp.remove(c.getId());
	// }
	// mNewFeedMap = mNewFeedMapTemp;
	// }

	// @Override
	// public ArrayList<ChatFeedModel> getAllUnSkimFeed() {
	// ArrayList<ChatFeedModel> feedList = new ArrayList<ChatFeedModel>();
	// for(ChatFeedModel c: mNewFeedMap.values()){
	// if(!c.isIsSkimed()){
	// feedList.add(c);
	// }
	// }
	// return feedList;
	// }
	// @Override
	// public void setAllFeedSkim() {
	// for(ChatFeedModel c: mNewFeedMap.values()){
	// c.setIsSkimed(true);
	// }
	// }
	@Override
	public void clearFeedList() {
		mNewFeedMap.clear();
	}

	@Override
	public ArrayList<ChatFeedModel> getAllFeed() {
		ArrayList<ChatFeedModel> feedList = new ArrayList<ChatFeedModel>();
		for (ChatFeedModel c : mNewFeedMap.values()) {
			feedList.add(c);
		}
		return feedList;
	}

	public int getFeedSize() {
		return mNewFeedMap.size();
	}

}
