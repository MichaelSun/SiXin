package com.renren.mobile.chat.database;

import com.core.database.Column;
import com.core.database.DatabaseTypeConstant;


/**
 * @author dingwei.chen1988@gmail.com
 * */
public interface Room_Column {

	@Column(defineType=DatabaseTypeConstant.INT+" "+DatabaseTypeConstant.PRIMARY)
	public static final String _ID = "_id";
	
	@Column(defineType=DatabaseTypeConstant.INT)
	public static final String ROOM_ID = "room_id"; //组的id号
	
	@Column(defineType=DatabaseTypeConstant.INT)
	public static final String ROOM_MEMBER_NUMBER = "group_member_number"; //组成员个数
	
	@Column(defineType=DatabaseTypeConstant.TEXT)
	public static final String ROOM_SUBJECT = "room_subject";//组名
	
	@Column(defineType=DatabaseTypeConstant.TEXT)
	public static final String ROOM_MEMBER_IDS = "room_member_ids"; //组成员ID号字串
	
	@Column(defineType=DatabaseTypeConstant.TEXT)
	public static final String ROOM_MEMBER_HEADURLS = "room_member_headurls"; //组成员HEADURL
	
	@Column(defineType=DatabaseTypeConstant.INT)
	public static final String ROOM_MASTER_ID = "room_master_id"; //群主ID号
	
	@Column(defineType=DatabaseTypeConstant.INT)
	public static final String LOCAL_USER_ID = "local_user_id";//当前用户ID号
	
	@Column(defineType=DatabaseTypeConstant.INT)
	public static final String ROOM_VERSION = "room_version";//群版本号
	
	@Column(defineType=DatabaseTypeConstant.INT)
	public static final String ROOM_ISDISABLE = "room_isdisable";//是否被销毁
	
	@Column(defineType=DatabaseTypeConstant.INT)
	public static final String ROOM_IS_CONTACT = "room_is_contact";//是否被销毁
}
