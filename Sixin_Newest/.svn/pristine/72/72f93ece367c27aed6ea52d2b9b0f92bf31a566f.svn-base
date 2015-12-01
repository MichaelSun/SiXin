package com.renren.mobile.chat.ui;


import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.common.statistics.BackgroundUtils;
import com.renren.mobile.chat.common.AwokeUtil;
import com.renren.mobile.chat.newsfeed.NewsFeedWarpper;
import com.renren.mobile.chat.ui.contact.ContactBaseModel;
import com.renren.mobile.chat.ui.contact.feed.ChatFeedActivity;
import com.renren.mobile.chat.ui.contact.feed.ChatFeedModel;
import com.renren.mobile.chat.ui.contact.feed.FeedCallbackSource;
import com.renren.mobile.chat.ui.notification.FeedNotificationManager;


public class FeedNotificationCancel extends Activity{

	public NewsFeedWarpper message;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		ContactBaseModel contactInChat = (ContactBaseModel) getIntent().getSerializableExtra("ContactInChat"); 
		if(TextUtils.isEmpty(contactInChat.name)){
			contactInChat.name = contactInChat.mContactName;
		}
		message = (NewsFeedWarpper)getIntent().getSerializableExtra("NewsFeedWarpper");
		//统计唤醒次数与类型
		/***************************************************************************/
		int type = message.mFeedType;
		String awokeWay = AwokeUtil.getAwokeWay(type);
		BackgroundUtils.getInstance().dealAppRunState(awokeWay, false);
		/***************************************************************************/
		
		ChatFeedModel chatFeed = (ChatFeedModel)getIntent().getSerializableExtra("ChatFeedModel");
		List<ChatFeedModel> delFeedList = new ArrayList<ChatFeedModel>();
		List<ChatFeedModel> feedList = FeedNotificationManager.getInstance().getUnReadChatFeedList();
		for(ChatFeedModel chatFeedModel : feedList){
			if(chatFeedModel.getId() == chatFeed.getId()){
				delFeedList.add(chatFeedModel);
			}
		}
		feedList.removeAll(delFeedList);
		
		finish();
		
//		RenRenChatActivity.show(this,contactInChat,message);
		Intent intent = new Intent(FeedNotificationCancel.this,ChatFeedActivity.class);
		startActivity(intent);
		
		FeedCallbackSource mCallbackSource = FeedNotificationManager.getInstance().getFeedCallbackSource();
		if(mCallbackSource != null){
			mCallbackSource.getAllFeed();
		}
	}
	
	@Override
	protected void onStart() {
		
	}
	
	@Override
	protected void onStop() {
	}

}
