package com.renren.mobile.chat.database;

import com.core.database.Column;
import com.core.database.DatabaseTypeConstant;


/**
 * @author dingwei.chen
 * @说明 通讯录数据库的列字段
 * @注:不采用数据表的级联存储,采用数据表的冗余存储
 * */
public interface Contact_Column {

	@Column(defineType=DatabaseTypeConstant.INT+" "+DatabaseTypeConstant.PRIMARY)
	public static final String _ID = "_id";
	
	@Column(defineType=DatabaseTypeConstant.INT)
	public static final String USER_ID = "user_id"; //用户的人人id
	
	@Column(defineType=DatabaseTypeConstant.TEXT)
	public static final String USER_NAME = "user_name";//用户的姓名
	
	@Column(defineType=DatabaseTypeConstant.TEXT)
	public static final String USER_NAME_PINYIN = "user_name_pinyin";//用户姓名的拼音
	
	@Column(defineType=DatabaseTypeConstant.TEXT)
	public static final String HEAD_MEDIUM= "head_medium";//用户的头像 url
	
	@Column(defineType=DatabaseTypeConstant.TEXT)
	public static final String HEAD_LARGE = "head_large";//用户的大头像url
	
	@Column(defineType=DatabaseTypeConstant.INT) 
	public static final String  HEAD_ORIGINAL= "head_original";
	
	@Column(defineType=DatabaseTypeConstant.TEXT)
	public static final String ALEPH = "aleph"; //姓名首字母
	
//	@Column(defineType=DatabaseTypeConstant.INT)
//	public static final String NAME_LENGTH = "name_length";  //名字长度
	
	@Column(defineType=DatabaseTypeConstant.TEXT)
	public static final String GENDER = "gender"; 
	
	@Column(defineType=DatabaseTypeConstant.TEXT)
	public static final String BIRTH = "birth"; 
	
	@Column(defineType=DatabaseTypeConstant.TEXT)
	public static final String XIN_PIN_YIN = "xing_pinyin"; 
	
	@Column(defineType=DatabaseTypeConstant.TEXT)
	public static final String SCHOOL = "school"; 
	
	@Column(defineType=DatabaseTypeConstant.TEXT)
	public static final String EMPLOYER = "employer"; 
	
	
	@Column(defineType=DatabaseTypeConstant.INT)
	public static final String ATTACH_GROUP_NUMBER = "attach_group_number"; //所属组的
	
	@Column(defineType=DatabaseTypeConstant.INT)
	public static final String RELATION = "relation"; //所属组的

	
	@Column(defineType=DatabaseTypeConstant.TEXT)
	public static final String BIND_INFO = "bind_info"; //所属组的
	
	@Column(defineType=DatabaseTypeConstant.TEXT)
	public static final String DOMAIN = "domain";	//用户所在域
	
	@Column(defineType=DatabaseTypeConstant.INT)
	public static final String COMPLETE = "complete";	//用户所在域
	
//	@Column(defineType=DatabaseTypeConstant.INT)
//	public static final String IS_COMMON = "is_common";	//用户所在域

}
