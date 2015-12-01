package com.renren.mobile.chat.ui.contact.feed;

import java.io.Serializable;
import java.util.ArrayList;

import com.core.json.JsonObject;

public class ChatFeedTextModel extends ChatFeedModel implements Serializable {

	private static final long serialVersionUID = 3402823921167553869L;
	private ArrayList<Object> chatFeedReblogContent;

	public ChatFeedTextModel(JsonObject objs) {
		super(objs);
		JsonObject objs_reblog = objs.getJsonObject(FEED_COLUMN_STATUS_FORWARD);
		if (objs_reblog != null) {
			mIsReblog = true;
			ChatFeedReblogContent c = new ChatFeedReblogContent(objs_reblog);
			if (c != null) {
				chatFeedReblogContent = new ArrayList<Object>();
				chatFeedReblogContent.add(c);
			}
		}
	}

	@Override
	public ArrayList<Object> getChatFeedAttachmentContent() {
		return chatFeedReblogContent;
	}
}
