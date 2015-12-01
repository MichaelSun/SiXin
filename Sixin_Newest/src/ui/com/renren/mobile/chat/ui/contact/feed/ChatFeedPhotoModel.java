package com.renren.mobile.chat.ui.contact.feed;

import java.io.Serializable;
import java.util.ArrayList;

import com.core.json.JsonArray;
import com.core.json.JsonObject;

public class ChatFeedPhotoModel extends ChatFeedModel implements Serializable {

	private static final long serialVersionUID = 4812358003126695938L;

	private int mPhotoNum;

	private ArrayList<Object> mChatFeedPhotoContent = new ArrayList<Object>();

	public ChatFeedPhotoModel(JsonObject obj) {
		super(obj);
		JsonArray array = obj.getJsonArray("attachement_list");
		if (array != null) {
			mPhotoNum = array.size();
			JsonObject[] objs = new JsonObject[array.size()];
			array.copyInto(objs);
			for (JsonObject o : objs) {
				ChatFeedPhotoContent c = new ChatFeedPhotoContent(o);
				if (c != null) {
					mChatFeedPhotoContent.add(c);
				}
			}
		}
	}

	@Override
	public ArrayList<Object> getChatFeedAttachmentContent() {
		return mChatFeedPhotoContent;
	}

	public int getPhotoNum() {
		return mPhotoNum;
	}

}
