package com.renren.mobile.chat.database;

import com.core.database.Column;
import com.core.database.DatabaseTypeConstant;


/**
 * "head_url":
 * {
 *    "medium_url":"http:\/\/www.qiqu5.com\/wp-content\/uploads\/2010\/03\/119.jpg",
*"     original_rul":"http:\/\/t3.baidu.com\/it\/u=295144277,3149792241&fm=51&gp=0.jpg",
*"     large_url":"http:\/\/t2.baidu.com\/it\/u=3925358291,1529856573&fm=52&gp=0.jpg"
*   },
*"first_name":"",
*"school":[{"name":"学校"}],
*"name":"张朋朋",
*"employer":[{"name":"公司"}],
*"birth_day":"1-1-1",
*"last_name":"",
*"gender":0,
*"user_id":0,
*"thirdPartyExpandInfo":
     {
        "third_party_id":"105909705",
        "third_party_type":"renren",
        "third_party_page":"http:\/\/www.renren.com\/105909705\/profile"}
     },
 * */
public interface Renren_Contact_Column {

	@Column(defineType=DatabaseTypeConstant.INT+" "+DatabaseTypeConstant.PRIMARY)
	public static final String _ID = "_id";
	
	@Column(defineType=DatabaseTypeConstant.INT)
	public static final String USER_ID = Contact_Column.USER_ID; //用户的人人id
	
	@Column(defineType=DatabaseTypeConstant.TEXT)
	public static final String USER_NAME = Contact_Column.USER_NAME;//用户的姓名
	
	@Column(defineType=DatabaseTypeConstant.TEXT)
	public static final String MAIN_PAGE = "main_page";//主页
	
	@Column(defineType=DatabaseTypeConstant.TEXT)
	public static final String HEAD_MEDIUM_URL = Contact_Column.HEAD_MEDIUM;//主页
	
	@Column(defineType=DatabaseTypeConstant.TEXT)
	public static final String HEAD_ORIGINAL_URL = Contact_Column.HEAD_ORIGINAL;//主页
	
	@Column(defineType=DatabaseTypeConstant.TEXT)
	public static final String HEAD_LARGE_URL = Contact_Column.HEAD_LARGE;//主页
	
	@Column(defineType=DatabaseTypeConstant.TEXT)
	public static final String SCHOOL = Contact_Column.SCHOOL; 
	
	@Column(defineType=DatabaseTypeConstant.TEXT)
	public static final String EMPLOYER = Contact_Column.EMPLOYER;
	
	@Column(defineType=DatabaseTypeConstant.TEXT)
	public static final String ALEPH = Contact_Column.ALEPH; //姓名首字母
	
	@Column(defineType=DatabaseTypeConstant.TEXT)
	public static final String BIRTH = Contact_Column.BIRTH; 
	
	@Column(defineType=DatabaseTypeConstant.TEXT)
	public static final String GENDER = Contact_Column.GENDER; 
	
	@Column(defineType=DatabaseTypeConstant.INT)
	public static final String RENREN_ID = "renren_id"; //用户的人人id
		
	@Column(defineType=DatabaseTypeConstant.TEXT)
	public static final String USER_NAME_PINYIN = Contact_Column.USER_NAME_PINYIN;//用户姓名的拼音
	
	@Column(defineType=DatabaseTypeConstant.TEXT)
	public static final String XIN_PIN_YIN = Contact_Column.XIN_PIN_YIN;
	
	@Column(defineType=DatabaseTypeConstant.INT)
	public static final String RELATION = Contact_Column.RELATION; //所属组的
	
	@Column(defineType=DatabaseTypeConstant.TEXT)
	public static final String DOMAIN = Contact_Column.DOMAIN;	//用户所在域
	
	@Column(defineType=DatabaseTypeConstant.INT)
	public static final String IS_ATTENTION = "is_attention"; //是否是特别关注好友
	
	@Column(defineType=DatabaseTypeConstant.INT)
	public static final String IS_FRIEND = "is_friend"; //是否是特别关注好友
	
	
}
