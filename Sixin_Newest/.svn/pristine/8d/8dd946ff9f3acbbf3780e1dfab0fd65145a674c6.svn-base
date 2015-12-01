package com.renren.mobile.chat.newsfeed;

import java.io.Serializable;
import java.util.ArrayList;

import com.data.xml.XOMUtil;
import com.data.xmpp.XMPPNode;
import com.data.xmpp.childs.Message_Feed;
import com.renren.mobile.chat.base.inter.NEWSFEED_TYPE;
import com.renren.mobile.chat.base.model.NewsFeedModel;
import com.renren.mobile.chat.base.util.SystemUtil;
import com.renren.mobile.chat.ui.contact.feed.ChatFeedLocationContent;
import com.renren.mobile.chat.ui.contact.feed.ChatFeedModel;
import com.renren.mobile.chat.ui.contact.feed.ChatFeedPhotoContent;
import com.renren.mobile.chat.ui.contact.feed.ChatFeedReblogContent;

/**
 * @author dingwei.chen
 * 新鲜事模型包装类
 * */
public class NewsFeedWarpper extends NewsFeedModel implements Serializable{

	
	public XMPPNode getFeedNode(){
		SystemUtil.log("load", "feed load");
		Message_Feed feed = new Message_Feed();
		XOMUtil.getInstance().OXMapping(feed, this);
		return feed;
	}
	
	public void adapter(ChatFeedModel model){
		this.mFeedId = model.getId();
		this.mFeedSourceId = model.getSourceId()+"";
		this.mFeedDescription = model.getTitle();
		this.mFeedUserName = model.getUserName();
		this.mFeedType = model.getType();
		this.mFeedUserId = model.getUserId();
		ChatFeedLocationContent placeContent = model.getChatFeedLocation();
		if(placeContent!=null){
			this.mFeedPlaceName = placeContent.getPlaceName();
		}
		ArrayList<Object> attachment = model.getChatFeedAttachmentContent();
		this.mFeed_Show_Forward = model.ismIsReblog()?"true":"flase";
		if(attachment!=null){
			switch(model.getType()){
			case NEWSFEED_TYPE.FEED_PHOTO_PUBLISH_ONE:
			case NEWSFEED_TYPE.FEED_PHOTO_PUBLISH_MORE:
				{
					if(attachment.size()==1){
						ChatFeedPhotoContent photoContent  = (ChatFeedPhotoContent)attachment.get(0);
						this.mImage1_Url_Tiny 		= photoContent.getUrl();
						this.mImage1_Url_Main 	= photoContent.getMainUrl();
						if(this.mImage1_Url_Main==null){
							this.mImage1_Url_Main  = this.mImage1_Url_Tiny ;
						}
						this.mImage1_Url_Large 	= photoContent.getLargeUrl();
						if(photoContent.getDigest()!=null){
							this.mFeedDescription = photoContent.getDigest();
							if(this.mFeedDescription.trim().length()==0){
								this.mFeedDescription = null;
							}
						}else{
							this.mFeedDescription = null;
						}
						
					}else if(attachment.size()>1){
						ChatFeedPhotoContent photoContent  = (ChatFeedPhotoContent)attachment.get(0);
						this.mImage1_Url_Tiny 		= photoContent.getUrl();
						this.mImage1_Url_Main 	= photoContent.getMainUrl();
						this.mImage1_Url_Large 	= photoContent.getLargeUrl();
						photoContent  = (ChatFeedPhotoContent)attachment.get(1);
						this.mImage2_Url_Tiny = photoContent.getUrl();
						this.mImage2_Url_Main = photoContent.getMainUrl();
						this.mImage2_Url_Large = photoContent.getLargeUrl();
					}
				}break;
			case NEWSFEED_TYPE.FEED_STATUS_UPDATE:
				{
					if(attachment.size()>0){
							ChatFeedReblogContent content = (ChatFeedReblogContent)attachment.get(0);
							this.mFeedOriginal_Name = content.getOwnerName();
							this.mFeedOriginal_Descip = content.getStatus();
					}
				}break;
			}
		
			
			
		}
		
		
		
		
	}

	
}
