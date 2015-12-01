package com.renren.mobile.chat.database;

import com.core.database.Column;
import com.core.database.DatabaseTypeConstant;

public interface ContactMessage_Column{
	@Column(defineType=DatabaseTypeConstant.INT+" "+DatabaseTypeConstant.PRIMARY)
	public static final String _ID = "_id";
	
	@Column(defineType=DatabaseTypeConstant.TEXT)
	public final String FROM="from_domain";
	
	@Column(defineType=DatabaseTypeConstant.TEXT)
	public final String TO="to_domain";
	
	@Column(defineType=DatabaseTypeConstant.INT)
	public final String GID="gid";
	
	@Column(defineType=DatabaseTypeConstant.TEXT)
	public final String DOMAIN="domain";
	
	@Column(defineType=DatabaseTypeConstant.INT)
	public final String FROM_TYPE="from_type";
	
	@Column(defineType=DatabaseTypeConstant.TEXT)
	public final String FROM_TEXT="from_text";
	
	@Column(defineType=DatabaseTypeConstant.TEXT)
	public final String NAME="name";
	
	@Column(defineType=DatabaseTypeConstant.TEXT)
	public final String HEAD_URL="head_url";
	
	@Column(defineType=DatabaseTypeConstant.INT)
	public final String TYPE="type";
	
	@Column(defineType=DatabaseTypeConstant.TEXT)
	public final String BODY="body";
	
	@Column(defineType=DatabaseTypeConstant.TEXT)
	public final String NATIVE_ID = "native_id";
	
	@Column(defineType=DatabaseTypeConstant.INT)
	public final String READED = "readed";
	
	@Column(defineType=DatabaseTypeConstant.INT)
	public final String ADDED = "has_add";
	
}
