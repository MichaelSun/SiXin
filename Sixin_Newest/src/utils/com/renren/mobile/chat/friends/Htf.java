package com.renren.mobile.chat.friends;

/**
 * 
 * @author rubin.dong@renren-inc.com
 *
 */

public class Htf {
	/*宫格页面拍照发布*/
	public static final int UPLOAD_PHOTO_FROM_DESKTOP_TAKE=361;	
	
	/*宫格页面相册发布*/
	public static final int UPLOAD_PHOTO_FROM_DESKTOP_PICKUP=362;	
	
	/*个人主页拍照发布*/
	public static final int UPLOAD_PHOTO_FROM_PROFILE_TAKE=363;	
	
	/*个人主页相册发布*/
	public static final int UPLOAD_PHOTO_FROM_PROFILE_PICKUP=364;	
	
	/*POI页拍照发布*/
	public static final int UPLOAD_PHOTO_FROM_POI_TAKE=365;	
	
	/*POI页相册发布*/
	public static final int UPLOAD_PHOTO_FROM_POI_PICKUP=366;
	
    /*宫格页面点击报到请求POIlist*/
	public static final int POI_LIST_FROM_DESKTOP=380;
	
	  /*位置首页点击报到请求POIlist*/
	public static final int POI_LIST_FROM_PLACES=381;	
	
	
	
	 /*POI页面点击报到请求POIlist*/
	public static final int POI_LIST_FROM_POI=382;	
	
	 /*UGC（状态、照片）点击位置请求POI list*/
	public static final int POI_LIST_FROM_UGC=383;
	
	
	public static final int POI_LIST_REFRESH_CHECKIN=410;
	
	public static final int POI_LIST_REFRESH_UGC=411;
	
	/*从htf=380进入POI页面*/
	public static final int POI_FROM_POI_LIST_FROM_DESKTOP=390;	
	
	/*从htf=381进入POI页面*/
	public static final int POI_FROM_POI_LIST_FROM_PLACES=391;	
	
	/*从htf=382进入POI页面*/
	public static final int POI_FROM_POI_LIST_FROM_POI=392;	
	
	/*从所有新鲜事点击进入POI页面*/
	public static final int POI_FROM_NEWSFEED=393;
	
	public static final int POI_FROM_PHOTO_COMMENT=427;
	
	public static final int POI_FROM_ACTIVITY=394;

    /*从htf=390点击立即报到发布成功数 */
	public static final int CHECK_IN_POI_FROM_POI_LIST_FROM_DESKTOP=400;
	
	/*从htf=391点击立即报到发布成功数 */
	public static final int CHECK_IN_POI_FROM_POI_LIST_FROM_PLACES=401;

	/*从htf=392点击立即报到发布成功数 */
	public static final int CHECK_IN_POI_FROM_POI_LIST_FROM_POI=402;
	
	/*从htf=393点击立即报到发布成功数 */
	public static final int CHECK_IN_POI_FROM_NEWSFEED=403;
	
	public static final int CHECK_IN_POI_FROM_ACTIVITY=404;
	
	public static final int HTF_EMPTY=0;
	
	public static final int POI_LIST_REFRESH=0;
	
	
	public static final int RAPID_PUB_ACTIVITY=422;
	
	public static final int POI_LIST_SEARCH=424;
	
	public static final int NEARBY_ACTIVITY_LIST_FROM_POI=426;
	
	public static final int PAGE_BUTTON=432;
	
	public static final int REQUEST_BUTTON=433;

	/* 推荐好友界面接收请求以及添加好友请求数*/
	public static final int REQUEST_RECIVIED = 525;
	public static final int REQUEST_ADDFRIENDS = 526;
	
	/*新用户引导中的同步通讯录操作 htf=581 已加入*/
    public static final int SYNC_CONTACTS_FROM_NEWUSER_GUIDE = 581;
	/*同步通讯录的提醒带来的用户上传通讯录操作 htf=582已加入*/
    public static final int SYNC_CONTACTS_FROM_SYN_NOTIFY = 582;
	/*好友列表中的通讯录按钮触发上传通讯录的操作 htf=583已加入*/
    public static final int SYNC_CONTACTS_FROM_FRIENDS_LIST = 583;
    
    /*同步通讯录  加好友    htf=593已加入*/
    public static final int ADD_FRIENDS_FROM_SYNC_CONTACTS = 593;
	/*最近来访 加好友    htf=590*/
	public static final int ADD_FRIENDS_FROM_RECENT_VISIT = 590;
	/*好友搜索  加好友    htf=591*/
    public static final int ADD_FRIENDS_FROM_FRIEND_SEARCH = 591;
	/*宫格搜索  加好友     htf=592 */
    public static final int ADD_FRIENDS_FROM_MAINPAGE_SEARCH = 592;
	/*聊天中的联系人列表 加好友  htf=594*/
    public static final int ADD_FRIENDS_FROM_CHAT = 594;
	/*好友的好友列表  加好友    htf=595*/
    public static final int ADD_FRIENDS_FROM_OTHERS_FRIENDLIST = 595;
	/*个人主页    加好友    htf=596*/
    public static final int ADD_FRIENDS_FROM_OTHERS_RECENT_VISITOR = 596;
    /*聊天界面通讯簿加好友    htf=601已加入*/
    public static final int ADD_FRIENDS_FROM_CHAT_ACTIVITY = 601;
    /*下载语音文件失败 htf=611已加入*/
    public static final int DOWNLOAD_AUDIO_FILE_ERROR=611;
    /*通过Widget报到*/
    public static final int POI_LIST_FROM_WIDGET = 622;
    /*通过Widget获取新鲜事自动 */
    public static final int AUTOMATIC_NEWSFEED_LIST_FROM_WIDGET = 623;
    /*通过Widget获取新鲜事手动*/
	public static final int MANUAL_NEWSFEED_LIST_FROM_WIDGET = 624;
    
}
