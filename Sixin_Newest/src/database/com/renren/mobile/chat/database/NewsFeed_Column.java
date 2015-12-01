package com.renren.mobile.chat.database;

import com.core.database.Column;
import com.core.database.DatabaseColumn;
import com.core.database.DatabaseTypeConstant;


/**
 * @author dingwei.chen
 * @说明  新鲜事数据库的列字段
 * @注:不采用数据表的级联存储,采用数据表的冗余存储
 * */
public interface NewsFeed_Column extends DatabaseColumn{
	
	@Column(defineType=DatabaseTypeConstant.INT+" "+DatabaseTypeConstant.PRIMARY)
	public static final String _ID = "_id";				//新鲜事ID号
	
	@Column(defineType=DatabaseTypeConstant.INT)
	public static final String FEED_ID = "feed_id";				//服务器下发新鲜事ID号
	
	@Column(defineType=DatabaseTypeConstant.INT)
	public static final String FEED_TYPE = "feed_type";//新鲜事类型
	
	@Column(defineType=DatabaseTypeConstant.TEXT)
	public static final String FEED_DESCRIPTION= "feed_description";//新鲜事文本内容
	
	@Column(defineType=DatabaseTypeConstant.TEXT)
	public static final String FEED_PLACENAME= "feed_placename";//发生地点
	
	@Column(defineType=DatabaseTypeConstant.INT)
	public static final String FEED_SOURCE_ID= "feed_sourceid";//新鲜事内容实体Id号
	
	@Column(defineType=DatabaseTypeConstant.TEXT)
	public static final String FEED_USER_NAME = "feed_user_name";//新鲜事产生者名字
	
	@Column(defineType=DatabaseTypeConstant.TEXT)
	public static final String FEED_ORIGINAL_NAME = "feed_origin_name";//源作者名字
	
	@Column(defineType=DatabaseTypeConstant.TEXT)
	public static final String FEED_ORIGINAL_STATUS = "feed_origin_status";//源状态
	
	@Column(defineType=DatabaseTypeConstant.TEXT)
	public static final String FEED_IMAGE1_URL_TINY = "feed_image1_url_tiny";//图片1预览小图
	
	@Column(defineType=DatabaseTypeConstant.TEXT)
	public static final String FEED_IMAGE1_URL_MAIN = "feed_image1_url_main";//图片1预览大图
	
	@Column(defineType=DatabaseTypeConstant.TEXT)
	public static final String FEED_IMAGE1_URL_LARGE = "feed_image1_url_large";//图片1原图
	
	@Column(defineType=DatabaseTypeConstant.TEXT)
	public static final String FEED_IMAGE1_DIGEST = "feed_image1_digest";//图片1描述
	
	
	@Column(defineType=DatabaseTypeConstant.TEXT)
	public static final String FEED_IMAGE2_URL_TINY = "feed_image2_url_tiny";//图片2预览小图
	
	@Column(defineType=DatabaseTypeConstant.TEXT)
	public static final String FEED_IMAGE2_URL_MAIN = "feed_image2_url_main";//图片2预览大图
	
	@Column(defineType=DatabaseTypeConstant.TEXT)
	public static final String FEED_IMAGE2_URL_LARGE = "feed_image2_url_large";//图片2原图
	
	@Column(defineType=DatabaseTypeConstant.TEXT)
	public static final String FEED_REPLY_USERNAME = "feed_reply_username";//回复用户名
	
	@Column(defineType=DatabaseTypeConstant.TEXT)
	public static final String FEED_REPLY_CONTENT = "feed_reply_content";//回复内容
	
	@Column(defineType=DatabaseTypeConstant.TEXT)
	public static final String FEED_FORWARD_LINE = "feed_isshow_forward_line";// "false" false,"true" true
	
	@Column(defineType=DatabaseTypeConstant.TEXT)
	public static final String FEED_SUMMARY = "feed_summary";// 0 false,1 true
	
	
	@Column(defineType=DatabaseTypeConstant.TEXT)
	public static final String FEED_VIDEO = "feed_isvideo";// 0 false,1 true
	
}
