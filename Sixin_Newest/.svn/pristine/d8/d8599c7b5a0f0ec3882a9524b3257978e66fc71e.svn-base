package com.renren.mobile.chat.ui.contact.feed;

import com.core.json.JsonObject;
import com.renren.mobile.chat.base.inter.NEWSFEED_TYPE;

public class ChatFeedModelFactory {

	public static ChatFeedModel createFeedModel(JsonObject objs) {
		int type = (int) objs.getNum("type");
		ChatFeedModel chatFeedModel = null;
		switch (type) {
		case NEWSFEED_TYPE.FEED_STATUS_UPDATE:
			chatFeedModel = new ChatFeedTextModel(objs);
			break;
		case NEWSFEED_TYPE.FEED_PHOTO_PUBLISH_ONE:
		case NEWSFEED_TYPE.FEED_PHOTO_PUBLISH_MORE:
			chatFeedModel = new ChatFeedPhotoModel(objs);
			break;
		case NEWSFEED_TYPE.FEED_PAGE_STATUS_UPDATE:
			// TODO 新的feed类型处理~

			break;
		default:
			break;
		}
		return chatFeedModel;

	}

}
