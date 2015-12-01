package com.renren.mobile.chat.newsfeed;

import com.common.utils.Methods;
import com.data.xmpp.Message;
import com.data.xmpp.childs.Message_Feed;

/**
 * @author dingwei.chen
 * @说明 
 * 			生成新鲜事模型
 * */
public final class NewsFeedFactory {
	private static NewsFeedFactory sInstance = new NewsFeedFactory();
	private NewsFeedFactory(){}
	public static NewsFeedFactory getInstance(){
		return sInstance;
	}
	
	public NewsFeedWarpper obtainNewsFeedModel(Message message){
		NewsFeedWarpper newsfeed = new NewsFeedWarpper();
		Message_Feed feed = message.mFeedNode;
		newsfeed.mFeedId 			= feed.getFeedId();
		newsfeed.mFeedType 			= feed.getFeedType();
		newsfeed.mFeedDescription 	= Methods.htmlDecoder(feed.mDescription);
		newsfeed.mFeedPlaceName		= feed.mPlaceName;
		newsfeed.mFeedSourceId		= feed.mSouceId;
		newsfeed.mFeedOriginal_Name = feed.mOriginal_Name;
		newsfeed.mImage1_Url_Tiny 	= feed.mPhoto1_1;
		newsfeed.mImage1_Url_Main 	= feed.mPhoto1_2;
		newsfeed.mImage1_Url_Large 	= feed.mPhoto1_3;
		newsfeed.mImage2_Url_Tiny 	= feed.mPhoto2_1;
		newsfeed.mImage2_Url_Main 	= feed.mPhoto2_2;
		newsfeed.mImage2_Url_Large 	= feed.mPhoto2_3;
		newsfeed.mFeedOriginal_Descip 	= feed.mOriginal_Descrip;
		newsfeed.mFeed_Reply_UserName 	= feed.mUserName;
		newsfeed.mFeed_Reply_Content 	= feed.mReplyContent;
		newsfeed.mFeed_Video 		= feed.mVideo;
		newsfeed.mFeed_Show_Forward = feed.mForward_Line;
		newsfeed.mFeed_Video 		= feed.mVideo;
		newsfeed.mFeed_Summary 		= feed.mSummary;
		return newsfeed;
	}
	
//	public NewsFeedWarpper obtainNewsFeedModel(int type){
//		NewsFeedWarpper newsfeed = null;
//		switch (type) {
//		case NEWSFEED_TYPE.FEED_STATUS_UPDATE:
//			newsfeed = new  NewsFeedWarpper_Status();break;
//		case NEWSFEED_TYPE.FEED_PHOTO_PUBLISH_ONE:
//			newsfeed = new  NewsFeedWarpper_PublishOnePhoto();break;
//		case NEWSFEED_TYPE.FEED_PHOTO_PUBLISH_MORE:
//			newsfeed = new  NewsFeedWarpper_PublishMorePhotos();break;
//		default:
//			break;
//		}
//		return newsfeed;
//	}
	
	public NewsFeedWarpper obtainNewsFeedModel(){
		return new NewsFeedWarpper();
	}
	
	
}
