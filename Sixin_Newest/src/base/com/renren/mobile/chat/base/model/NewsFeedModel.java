package com.renren.mobile.chat.base.model;

import java.io.Serializable;

import com.core.orm.ORM;
import com.data.xml.XMLMapping;
import com.data.xml.XMLType;
import com.renren.mobile.chat.base.util.XMLAttributeMapping;
import com.renren.mobile.chat.base.util.XMLTurnType;
import com.renren.mobile.chat.database.NewsFeed_Column;


/**
 * @author dingwei.chen
 * @说明 新鲜事模型
 * */
public class NewsFeedModel implements Serializable{
	
	@ORM(mappingColumn=NewsFeed_Column.FEED_ID)
	@XMLAttributeMapping(attributeName="id",turnType=XMLTurnType.LONG)
	@XMLMapping(Type=XMLType.ATTRIBUTE,Name="id")
	public long mFeedId = -1;//消息的ID号
	
	@ORM(mappingColumn=NewsFeed_Column.FEED_TYPE)
	@XMLAttributeMapping(attributeName="type",turnType=XMLTurnType.INT)
	@XMLMapping(Type=XMLType.ATTRIBUTE,Name="type")
	public int mFeedType = 0;//消息的类型
	
	@ORM(mappingColumn=NewsFeed_Column.FEED_DESCRIPTION)
	@XMLAttributeMapping(attributeName="description",turnType=XMLTurnType.STRING)
	@XMLMapping(Type=XMLType.ATTRIBUTE,Name="description")
	public String mFeedDescription = null;//消息描述
	
	@ORM(mappingColumn=NewsFeed_Column.FEED_PLACENAME)
	@XMLAttributeMapping(attributeName="placename",turnType=XMLTurnType.STRING)
	@XMLMapping(Type=XMLType.ATTRIBUTE,Name="placename")
	public String mFeedPlaceName = null;//地点
	
	@ORM(mappingColumn=NewsFeed_Column.FEED_SOURCE_ID)
	@XMLAttributeMapping(attributeName="source_id",turnType=XMLTurnType.STRING)
	@XMLMapping(Type=XMLType.ATTRIBUTE,Name="source_id")
	public String mFeedSourceId = null;//内容实体Id
	
	@ORM(mappingColumn=NewsFeed_Column.FEED_ORIGINAL_NAME)
	@XMLAttributeMapping(attributeName="original_name",turnType=XMLTurnType.STRING)
	@XMLMapping(Type=XMLType.ATTRIBUTE,Name="original_name")
	public String mFeedOriginal_Name = null;//源作者名字
	
	@ORM(mappingColumn=NewsFeed_Column.FEED_IMAGE1_URL_TINY)
	@XMLAttributeMapping(attributeName="photo1_url_s",turnType=XMLTurnType.STRING)
	@XMLMapping(Type=XMLType.ATTRIBUTE,Name="photo1_url_s")
	public String mImage1_Url_Tiny=null;//预览小图
	
	@ORM(mappingColumn=NewsFeed_Column.FEED_IMAGE1_URL_MAIN)
	@XMLAttributeMapping(attributeName="photo1_url_m",turnType=XMLTurnType.STRING)
	@XMLMapping(Type=XMLType.ATTRIBUTE,Name="photo1_url_m")
	public String mImage1_Url_Main=null;//预览大图
	
	@ORM(mappingColumn=NewsFeed_Column.FEED_IMAGE1_URL_LARGE)
	@XMLAttributeMapping(attributeName="photo1_url_l",turnType=XMLTurnType.STRING)
	@XMLMapping(Type=XMLType.ATTRIBUTE,Name="photo1_url_l")
	public String mImage1_Url_Large=null;//原始图
	
	@ORM(mappingColumn=NewsFeed_Column.FEED_ORIGINAL_STATUS)
	@XMLAttributeMapping(attributeName="original_descrip",turnType=XMLTurnType.STRING)
	//好吧~协议这么规定~别问我为何定这么恶心的名字~
	@XMLMapping(Type=XMLType.ATTRIBUTE,Name="original_descrip")
	public String mFeedOriginal_Descip=null;//源状态
	
	
	@ORM(mappingColumn=NewsFeed_Column.FEED_IMAGE2_URL_TINY)
	@XMLAttributeMapping(attributeName="photo2_url_s",turnType=XMLTurnType.STRING)
	@XMLMapping(Type=XMLType.ATTRIBUTE,Name="photo2_url_s")
	public String mImage2_Url_Tiny=null;//预览小图1
	
	@ORM(mappingColumn=NewsFeed_Column.FEED_IMAGE2_URL_MAIN)
	@XMLAttributeMapping(attributeName="photo2_url_m",turnType=XMLTurnType.STRING)
	@XMLMapping(Type=XMLType.ATTRIBUTE,Name="photo2_url_m")
	public String mImage2_Url_Main=null;//预览大图1
	
	@ORM(mappingColumn=NewsFeed_Column.FEED_IMAGE2_URL_LARGE)
	@XMLAttributeMapping(attributeName="photo2_url_l",turnType=XMLTurnType.STRING)
	@XMLMapping(Type=XMLType.ATTRIBUTE,Name="photo2_url_l")
	public String mImage2_Url_Large=null;//原始图1
	
	@ORM(mappingColumn=NewsFeed_Column.FEED_REPLY_USERNAME)
	@XMLAttributeMapping(attributeName="user_name",turnType=XMLTurnType.STRING)
	@XMLMapping(Type=XMLType.ATTRIBUTE,Name="user_name")
	public String mFeed_Reply_UserName=null;//回复用户名
	
	@ORM(mappingColumn=NewsFeed_Column.FEED_REPLY_CONTENT)
	@XMLAttributeMapping(attributeName="reply_content",turnType=XMLTurnType.STRING)
	@XMLMapping(Type=XMLType.ATTRIBUTE,Name="reply_content")
	public String mFeed_Reply_Content=null;//回复内容
	
	@ORM(mappingColumn=NewsFeed_Column.FEED_VIDEO)
	@XMLAttributeMapping(attributeName="video",turnType=XMLTurnType.STRING)
	@XMLMapping(Type=XMLType.ATTRIBUTE,Name="video")
	public String mFeed_Video=null;//回复内容
	
	@ORM(mappingColumn=NewsFeed_Column.FEED_FORWARD_LINE)
	@XMLAttributeMapping(attributeName="forward_line",turnType=XMLTurnType.STRING)
	@XMLMapping(Type=XMLType.ATTRIBUTE,Name="forward_line")
	public String mFeed_Show_Forward=null;//来自转发
	
	@ORM(mappingColumn=NewsFeed_Column.FEED_SUMMARY)
	@XMLAttributeMapping(attributeName="summary",turnType=XMLTurnType.STRING)
	@XMLMapping(Type=XMLType.ATTRIBUTE,Name="summary")
	public String mFeed_Summary=null;//回复内容
	
	
	
	@ORM(mappingColumn=NewsFeed_Column.FEED_USER_NAME)
	public String mFeedUserName = null;
	
	public long mFeedUserId = -1l;
	
	
	
}
