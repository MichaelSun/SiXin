package com.renren.mobile.chat.ui.contact.feed;

import java.io.Serializable;
import java.util.ArrayList;

import com.core.json.JsonArray;
import com.core.json.JsonObject;

public abstract class ChatFeedModel implements Serializable {

	private static final long serialVersionUID = -8817804798472844549L;

	public static final String FEED_COLUMN_ID = "id";

	public static final String FEED_COLUMN_HEAD_URL = "head_url";

	public static final String FEED_COLUMN_USER_NAME = "user_name";

	public static final String FEED_COLUMN_TITLE = "title";

	public static final String FEED_COLUMN_TIME = "time";

	public static final String FEED_COLUMN_SOURCE_ID = "source_id";

	public static final String FEED_COLUMN_PREFIX = "prefix";

	public static final String FEED_COLUMN_USER_ID = "user_id";

	public static final String FEED_COLUMN_TYPE = "type";

	public static final String FEED_COLUMN_OWNER_ID = "owner_id";

	public static final String FEED_COLUMN_MEDIA_ID = "media_id";

	public static final String FEED_COLUMN_PHOTO_URL = "url";

	public static final String FEED_COLUMN_PHOTO_MAIN_URL = "main_url";

	public static final String FEED_COLUMN_PHOTO_LARGE_URL = "large_url";

	public static final String FEED_COLUMN_PHOTO_DIGEST = "digest";

	public static final String FEED_COLUMN_STATUS_FORWARD = "status_forward";

	public static final String FEED_COLUMN_OWNER_NAME = "owner_name";

	public static final String FEED_COLUMN_REBLOG_OWNER_ID = "owner_id";

	public static final String FEED_COLUMN_REBLOGID = "id";

	public static final String FEED_COLUMN_STATUS = "status";

	public static final String FEED_COLUMN_PLACE = "place";

	public static final String FEED_COLUMN_PLACE_NAME = "pname";

	public static final String FEED_COLUMN_PLACE_ID = "id";

	public static final String FEED_COLUMN_PLACE_PID = "pid";

	public static final String FEED_COLUMN_PLACE_LONGITUDE = "longitude";

	public static final String FEED_COLUMN_PLACE_LATITUDE = "latitude";

	public static final String FEED_COLUMN_COMMENT = "comment_list";

	public static final String FEED_COLUMN_COMMENT_CONTENT = "content";

	public static final String FEED_COLUMN_COMMENT_HEADURL = "head_url";

	public static final String FEED_COLUMN_COMMENT_ID = "id";

	public static final String FEED_COLUMN_COMMENT_USERNAME = "user_name";

	public static final String FEED_COLUMN_COMMENT_TIME = "time";

	public static final String FEED_COLUMN_COMMENT_USERID = "user_id";

	protected boolean mIsReblog = false;

	protected boolean mHasLocation = false;

	private long mId; // 新鲜事id

	private String mHeadUrl; // 头像url

	private String mUserName; // 用户姓名

	private String mTitle; // 内容

	private long mTime; // 时间

	private long mSourceId; // 分享源内容id

	private String mPrefix; // 新鲜事的前缀 例如：上传照片至

	private long mUserId; // 新鲜事用户的id

	private int mType; // 新鲜事类型

	// private boolean mIsSkimed = false;

	protected ChatFeedLocationContent mChatFeedLocationContent;

	protected ArrayList<ChatFeedCommentContent> mChatFeedCommentContent;

	public ChatFeedModel(JsonObject objs) {
		this.mId = objs.getNum(FEED_COLUMN_ID);
		this.mHeadUrl = objs.getString(FEED_COLUMN_HEAD_URL);
		this.mUserName = objs.getString(FEED_COLUMN_USER_NAME);
		this.mTitle = objs.getString(FEED_COLUMN_TITLE);

		this.mTime = objs.getNum(FEED_COLUMN_TIME);
		this.mSourceId = objs.getNum(FEED_COLUMN_SOURCE_ID);
		this.mPrefix = objs.getString(FEED_COLUMN_PREFIX);
		this.mUserId = objs.getNum(FEED_COLUMN_USER_ID);
		this.mType = (int) objs.getNum(FEED_COLUMN_TYPE);
		initLocationAndComment(objs);
	}

	public void initLocationAndComment(JsonObject objs) {
		JsonObject objs_location = objs.getJsonObject(FEED_COLUMN_PLACE);
		if (objs_location != null) {
			mHasLocation = true;
			mChatFeedLocationContent = new ChatFeedLocationContent(
					objs_location);
		}
		JsonArray array_comment = objs.getJsonArray(FEED_COLUMN_COMMENT);
		if (array_comment != null) {
			mChatFeedCommentContent = new ArrayList<ChatFeedCommentContent>();
			JsonObject[] objs_comments = new JsonObject[array_comment.size()];
			array_comment.copyInto(objs_comments);
			for (JsonObject o : objs_comments) {
				ChatFeedCommentContent c = new ChatFeedCommentContent(o);
				if (c != null) {
					mChatFeedCommentContent.add(c);
				}
			}
		}
	}

	@Override
	public String toString() {
		return "id =" + mId + ";" + "headUrl =" + mHeadUrl + ";" + "userName ="
				+ mUserName + ";" + "title =" + mTitle + ";" + "time =" + mTime
				+ ";" + "sourceId =" + mSourceId + ";" + "prefix =" + mPrefix
				+ ";" + "userId =" + mUserId + ";" + "type =" + mType + ";";
	}

	public long getId() {
		return mId;
	}

	public void setId(long id) {
		this.mId = id;
	}

	public String getHeadUrl() {
		return mHeadUrl;
	}

	public void setHeadUrl(String headUrl) {
		this.mHeadUrl = headUrl;
	}

	public String getUserName() {
		return mUserName;
	}

	public void setUserName(String userName) {
		this.mUserName = userName;
	}

	public String getTitle() {
		return mTitle;
	}

	public void setTitle(String title) {
		this.mTitle = title;
	}

	public long getTime() {
		return mTime;
	}

	public void setTime(long time) {
		this.mTime = time;
	}

	public long getSourceId() {
		return mSourceId;
	}

	public void setSourceId(long sourceId) {
		this.mSourceId = sourceId;
	}

	public String getPrefix() {
		return mPrefix;
	}

	public void setPrefix(String prefix) {
		this.mPrefix = prefix;
	}

	public long getUserId() {
		return mUserId;
	}

	public void setUserId(long userId) {
		this.mUserId = userId;
	}

	public int getType() {
		return mType;
	}

	public void setType(int type) {
		this.mType = type;
	}

	public boolean ismIsReblog() {
		return mIsReblog;
	}

	public void setmIsReblog(boolean mIsReblog) {
		this.mIsReblog = mIsReblog;
	}

	public boolean isHasLocation() {
		return mHasLocation;
	}

	public abstract ArrayList<Object> getChatFeedAttachmentContent();

	public ChatFeedLocationContent getChatFeedLocation() {
		return mChatFeedLocationContent;
	}

	public ArrayList<ChatFeedCommentContent> getChatFeedCommentContent() {
		return mChatFeedCommentContent;
	}

	/**
	 * 新鲜事插件无需在使用此字段 public boolean isIsSkimed() { return mIsSkimed; }
	 * 
	 * public void setIsSkimed(boolean mIsSkimed) { this.mIsSkimed = mIsSkimed;
	 * }
	 */

}
