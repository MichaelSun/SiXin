package com.renren.mobile.chat.ui.contact;
import com.core.orm.ORM;
import com.renren.mobile.chat.database.ContactMessage_Column;

public class ContactMessageModel {
	
	@ORM(mappingColumn = ContactMessage_Column.NATIVE_ID)
	public  long nativeId;
	

	@ORM(mappingColumn = ContactMessage_Column.FROM)
	public  String from="";
	
	@ORM(mappingColumn = ContactMessage_Column.TO)
	public  String to="";
	
	@ORM(mappingColumn = ContactMessage_Column.GID)
	public  long gid;
	
	@ORM(mappingColumn = ContactMessage_Column.DOMAIN)
	public  String domain="";
	
	@ORM(mappingColumn = ContactMessage_Column.FROM_TYPE)
	public  int from_type;
	
	@ORM(mappingColumn = ContactMessage_Column.FROM_TEXT)
	public  String from_text="";
	
	@ORM(mappingColumn = ContactMessage_Column.NAME)
	public  String name="";
	
	@ORM(mappingColumn = ContactMessage_Column.HEAD_URL)
	public  String head_url="";
	
	@ORM(mappingColumn = ContactMessage_Column.TYPE)
	public  int type;
	
	@ORM(mappingColumn = ContactMessage_Column.BODY)
	public  String body="";
	
	@ORM(mappingColumn = ContactMessage_Column.READED)
	public  int readed = READ_NO;
	
	@ORM(mappingColumn = ContactMessage_Column.ADDED)
	public  int added = ADD_NO;
	
	
	public static byte READ_YES = 1;
	public static byte READ_NO = 0;
	
	public static byte ADD_YES = 1;
	public static byte ADD_NO = 0;

	/**
	 * 0：被系统自动互相添加为联系人
        1：推荐联系人（双方尚未添加对方）
        2：被对方添加为联系人的提醒（自己尚未添加对方）
        3：发送方请求把接受方加为发送方的联系人（双方尚未添加对方）
	 */
	

	/**
	 * A,B通讯录录或sns中互有联系方式   系统默认将它们加为联系人
	 */
	public static final byte TYPE_ADD = 0;
	/**A 通讯录中有B，但B中没有A， 系统发送相互推荐加为联系人 */
	public static final byte TYPE_RECOMMEND = 1;
	/** A发送加B为联系人，B验证为关，系统向A发送添加的联系人B，并向B发送一个特殊对话*/
	public static final byte TYPE_ADDED_BY_OTHER = 2;
	/**A发送加B为联系人，但B验证为开，A发送验证请求，系统给B发送好友请求 */
	public static final byte TYPE_REQUEST = 3;
	
	/**
	 * 0通讯录；2人人；3Facebook -
	 */
	public static final byte FROM_TYPE_ADDRESS = 0;
	public static final byte FROM_TYPE_RENREN = 2;
	public static final byte FROM_TYPE_FACEBOOK = 3;
	

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public long getGid() {
		return gid;
	}

	public void setGid(long gid) {
		this.gid = gid;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public int getFrom_type() {
		return from_type;
	}

	public void setFrom_type(int from_type) {
		this.from_type = from_type;
	}

	public String getFrom_text() {
		return from_text;
	}

	public void setFrom_text(String from_text) {
		this.from_text = from_text;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getHead_url() {
		return head_url;
	}

	public void setHead_url(String head_url) {
		this.head_url = head_url;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}
	
	public long getNativeId() {
		return nativeId;
	}

	public void setNativeId(long nativeId) {
		this.nativeId = nativeId;
	}
	
	public int getReaded() {
		return readed;
	}

	public void setReaded(int readed) {
		this.readed = readed;
	}


	public int getAdded() {
		return added;
	}

	public void setAdded(int added) {
		this.added = added;
	}

	
}
