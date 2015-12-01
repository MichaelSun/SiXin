package com.renren.mobile.chat.ui.contact.feed;

import java.io.Serializable;

import com.core.json.JsonObject;

public class ChatFeedPhotoContent implements Serializable {

	private static final long serialVersionUID = -4492428004125095166L;

	private long ownerId; // 源状态拥有者id

	private long mediaId = 0; // 媒体内容的id

	private String type; // 类型

	private String url; // 缩略图url

	private String mainUrl; // 大缩略图url

	private String largeUrl;

	private String digest; // 描述

	public ChatFeedPhotoContent(JsonObject objs) {
		this.ownerId = objs.getNum(ChatFeedModel.FEED_COLUMN_OWNER_ID);
		this.mediaId = objs.getNum(ChatFeedModel.FEED_COLUMN_MEDIA_ID);
		this.type = objs.getString(ChatFeedModel.FEED_COLUMN_TYPE);
		this.url = objs.getString(ChatFeedModel.FEED_COLUMN_PHOTO_URL);
		this.mainUrl = objs.getString(ChatFeedModel.FEED_COLUMN_PHOTO_MAIN_URL);
		this.largeUrl = objs
				.getString(ChatFeedModel.FEED_COLUMN_PHOTO_LARGE_URL);
		this.digest = objs.getString(ChatFeedModel.FEED_COLUMN_PHOTO_DIGEST);
	}

	public long getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(long ownerId) {
		this.ownerId = ownerId;
	}

	public long getMediaId() {
		return mediaId;
	}

	public void setMediaId(long mediaId) {
		this.mediaId = mediaId;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getMainUrl() {
		return mainUrl;
	}

	public void setMainUrl(String mainUrl) {
		this.mainUrl = mainUrl;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDigest() {
		return digest;
	}

	public void setDigest(String digest) {
		this.digest = digest;
	}

	public String getLargeUrl() {
		return largeUrl;
	}

	public void setLargeUrl(String largeUrl) {
		this.largeUrl = largeUrl;
	}

}
