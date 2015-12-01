package com.renren.mobile.chat.ui.contact.feed;

import java.io.Serializable;

import com.core.json.JsonObject;

/**
 * @author liuchao
 */

public class ChatFeedCommentContent implements Serializable {

	private static final long serialVersionUID = -8030374518222938296L;

	private String mContent;

	private String mHeadUrl;

	private long mId;

	private String mUserName;

	private long mTime;

	private long mUserId;

	public ChatFeedCommentContent(JsonObject objs) {
		this.mContent = objs
				.getString(ChatFeedModel.FEED_COLUMN_COMMENT_CONTENT);
		this.mHeadUrl = objs
				.getString(ChatFeedModel.FEED_COLUMN_COMMENT_HEADURL);
		this.mId = objs.getNum(ChatFeedModel.FEED_COLUMN_COMMENT_ID);
		this.mUserName = objs
				.getString(ChatFeedModel.FEED_COLUMN_COMMENT_USERNAME);
		this.mTime = objs.getNum(ChatFeedModel.FEED_COLUMN_COMMENT_TIME);
		this.mUserId = objs.getNum(ChatFeedModel.FEED_COLUMN_COMMENT_USERID);
	}

	public String getContent() {
		return mContent;
	}

	public void setContent(String content) {
		this.mContent = content;
	}

	public String getHeadUrl() {
		return mHeadUrl;
	}

	public void setHeadUrl(String headUrl) {
		this.mHeadUrl = headUrl;
	}

	public long getId() {
		return mId;
	}

	public void setId(long id) {
		this.mId = id;
	}

	public String getUserName() {
		return mUserName;
	}

	public void setUserName(String userName) {
		this.mUserName = userName;
	}

	public long getTime() {
		return mTime;
	}

	public void setTime(long time) {
		this.mTime = time;
	}

	public long getUserId() {
		return mUserId;
	}

	public void setUserId(long userId) {
		this.mUserId = userId;
	}

}
