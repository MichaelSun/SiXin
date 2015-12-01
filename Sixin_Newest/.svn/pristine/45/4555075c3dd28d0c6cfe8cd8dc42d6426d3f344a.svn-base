package com.renren.mobile.chat.ui.contact.feed;

import java.io.Serializable;

import com.core.json.JsonObject;

public class ChatFeedReblogContent implements Serializable {

	private static final long serialVersionUID = 8322229812608467994L;

	protected String ownerName;

	protected long ownerId;

	protected long id;

	protected String status;

	public ChatFeedReblogContent(JsonObject objs) {
		ownerName = objs.getString(ChatFeedModel.FEED_COLUMN_OWNER_NAME);
		ownerId = objs.getNum(ChatFeedModel.FEED_COLUMN_OWNER_ID);
		id = objs.getNum(ChatFeedModel.FEED_COLUMN_REBLOGID);
		status = objs.getString(ChatFeedModel.FEED_COLUMN_STATUS);
	}

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	public long getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(long ownerId) {
		this.ownerId = ownerId;
	}

	public long getId() {
		return id;
	}

	public void setId(long Id) {
		this.id = Id;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
