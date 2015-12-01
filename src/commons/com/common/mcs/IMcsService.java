package com.common.mcs;

import java.util.Map;

import com.common.contactscontract.ContactsContractModel;

/**
 * @author dingwei.chen
 * @说明 MCS调用接口
 * @see com.common.mcs.HttpMcsService
 * */
public interface IMcsService {
	
	public String getClientInfo();
	
//	public void setSecretKey(String key);
	
//	public String getSecretKey();
	
//	public void setSessionKey(String key);
	
//	public String getSessionKey();
	
	/*获取注册验证码*/
	public void getCaptcha(String user, String action, INetResponse response);
	
	/*验证注册验证码*/
	public void checkCaptcha(String user, String captcha, INetResponse response);
	
	/*获取绑定换绑验证码*/
	public void getCaptchaBinding(String account, String action, INetResponse response);
	
	/*绑定手机号或邮箱*/
	public void bindAccount(String account, String captcha, String passwordToken, INetResponse response);
	
	/*登录*/
	public void login(String account,String passwordMd5,  int captcha_needed, String captcha, long session,INetResponse response);
	
	/*登录 用私信帐号登陆*/
	public void loginSiXin(String account,String passwordMd5,long session,INetResponse response);
	
	/*登录 用人人帐号登陆*/
	public void loginRenRen(String account,String passwordMd5,long session,INetResponse response);
	
	/*登录 用私信帐号登陆*/
	public void loginSiXin(String account,String passwordMd5,long session,int captcha_needed, String captcha,INetResponse response);
	
	/*登录 用人人帐号登陆*/
	public void loginRenRen(String account,String passwordMd5,long session,int captcha_needed, String captcha,INetResponse response);
	
	/*获取新鲜事*/
	public void getFeedList(int page, int page_size,String types,INetResponse response);
	
	/*获取新鲜事*/
	public void getFeedList( long[] id,INetResponse response);
	
	/*获得图片*/
	public void getImage(String url,INetResponse response);
	
	
	public void postPhoto(byte[] imgData, int type, int htf, int from, String statistic, String aid, String description, String placeData, INetResponse response);
	/*获得语音*/
	public void getVoice(String url,INetResponse response);
	
	/*得到好友*/
	public INetRequest getFriends(int page,int pagesize,boolean batchRun,INetResponse response);
	
	/*得到组*/
	public INetRequest getGroups(boolean batchRun,INetResponse response);

	/*获得好友请求*/
	public void getFriendsRequests( int page, int page_size, int exclude_list, int del_news,int htf,INetResponse response);
	
	/*上传图片*///byte[] imgData, int toId, INetResponse response
	public void postPhoto(byte[] imgData, long toId, INetResponse response);
	
	public void postPNGPhoto(byte[] imgData, long toId, INetResponse response);

	/*上传语音*///byte[] imgData, int toId, INetResponse response
	public void postVoice(long toId, String vid, int seqid, String mode, int playTime, byte[] voiceData, INetResponse response);
	
	/*上传崩溃信息*/
	public void postLog(String stackInfo,String exMessage,INetResponse response);
	
	/*设置关注*/
	public void postFocusFriend(boolean isFoucs,long userId,INetResponse response);
	/*得到好友的在线状态*/
	public void getFriendsOnlineStatus(INetResponse response);
	/*得到好友的在线状态*/
	public void getFriendOnlineStatus(long userId,INetResponse response);
	/*得到好友信息*/
	public INetRequest getFriendInfo(int type, long uid, boolean batchRun , INetResponse response);
	
	public void batchRun(INetRequest[] requests);
	
	/**
	 * 绑定人人账号
	 * @param account 账号
	 * @param psw 密码
	 * @param sessionkey
	 * @param response
	 */
	public void bindRenrenCount(String account, String psw, INetResponse response);
	
	/**
	 * 解绑人人账号
	 * @param renrenUid 人人账号
	 * @param sessionkey
	 * @param response
	 */
	public void unBindRenrenCount(String renrenUid, INetResponse response);
	
	
	public INetRequest getFriendRequests(int type, INetResponse response,boolean batch);
	
	/**
	 * 后台运行统计
	 * @param response
	 */
	public void backgroundRunStatistics(INetResponse response);
	
	/**
	 * @description 上传统计数据
	 * @param localStatistics 本地统计数据
	 * @param response
	 */
	public void uploadStatistics(String localStatistics, INetResponse response);
	
	/**
	 * 
	 * @param type
	 *            1：激活统计 2：联网统计 uniqid: 设备的唯一标识，一般手机取IMEI
	 * @return
	 */
	public INetRequest activeClient(String imei, int type, INetResponse response, boolean batchRun);

	/**
	 * 匹配通讯录
	 * 
	 * @param contacts
	 *            通讯录数据
	 * @param response
	 *            回调
	 */
	public void matchContacts(boolean isSkip, ContactsContractModel[] contacts, INetResponse response);
	
	/**
	 * 申请加为好友
	 */
	public INetRequest m_friendsRequest(long uid, String content, INetResponse response, boolean batchRun);
	
	/**
	 * 
	 * @param uid
	 * @param content
	 * @param response
	 * @param batchRun
	 * @param HTF
	 * @return
	 */
	public INetRequest m_friendsRequest(long uid, String content, INetResponse response, boolean batchRun, String HTF);

	/**
	 * 获取好友请求列表
	 * 
	 * @param page
	 * @param batchRun
	 * @param page
	 * @param page_size
	 * @author rubin.dong@renren-inc.com
	 * @return
	 */
	public INetRequest getFriendsRequest(INetResponse response, int page, int page_size, int exclude_list, int del_news, boolean batchRun);
	
	/**
	 * 接受好友请求
	 * 
	 * @param uid
	 * @param response
	 * @param batchRun
	 * @author rubin.dong@renren-inc.com
	 * @return
	 */
	public INetRequest acceptFriendRequest(long uid, final INetResponse response, boolean batchRun);
	
	/**
	 * 忽略好友请求
	 * 
	 * @param uid
	 * @param response
	 * @param batchRun
	 * @author rubin.dong@renren-inc.com
	 * @return
	 */
	public INetRequest denyFriendRequest(long uid, final INetResponse response, boolean batchRun);
	
	/**
	 * 完善用户信息
	 * 
	 * @param response
	 * @param userName
	 *            用户名 (长度小于6汉字或12英文)
	 * @param userGender
	 *            性别(男生|女生)
	 * @param year
	 *            生日(年) （1900至今）
	 * @param month
	 *            生日(月)
	 * @param day
	 *            生日(日)
	 * @param stage
	 *            用户当前身份（已经工作，30| 大学，20| 中学，10| 其他，90）
	 * @param batchRun
	 * @return
	 */
	public INetRequest supplyUserInfo(final INetResponse response, String userName, String userGender, int year, int month, int day, int stage, boolean batchRun);
	
	/**
	 * 获取特别关注好友
	 * @param response
	 */
	public void getFocusFriends(INetResponse response);
	
	/**
	 * @author add by xiangchao.fan
	 * @description 判断用户userId是否为某公共主页pageId的粉丝
	 * @param userId
	 * @param pageId
	 * @param response
	 */
	public void isFansOfPage(long userId, long pageId, INetResponse response);
	
	/**
	 * @author add by xiangchao.fan
	 * @description 成为某公共主页pageId的粉丝
	 * @param pageId
	 * @param response
	 */
	public void becomeFansOfPage(int pageId, INetResponse response);
	
	/**
	 * 分享私信
	 * 
	 * @param response
	 * @param batchRun
	 * @return
	 */
	public INetRequest m_statusSet(INetResponse response, boolean batchRun);
	
	/**
	 * 添加特别关注
	 * @param response
	 * @param uid
	 */
	public void addFocusFriend(INetResponse response,long uid);
	
	/**
	 * 取消特别关注
	 * @param response
	 * @param uid
	 */
	public void delFocusFriend(INetResponse response,long uid);
	
//	/**
//	 * 获取在线的好友 接口 2012-2-16
//	 * 
//	 * @param uid
//	 * @param response
//	 * @param page
//	 *            页数 1
//	 * @param page_size
//	 *            页面大小 2000
//	 * @return
//	 */
//	public INetRequest getOnlineFriendList(INetResponse response, int page, int page_size,boolean batchRun);
	
	/**
	 * 获取群组联系人
	 * @param response
	 * @param batchRun
	 * @return
	 */
	public INetRequest getGroupContact(INetResponse response, boolean batchRun);
	
	/**
	 * 获取用户在线状态
	 * 返回  在线用户的id
	 * @param response
	 * @return
	 */
	public INetRequest getOnlineFriendListSimple(INetResponse response);
	
	public INetRequest getFriendInfo(int type, Long uid, INetResponse response, boolean batchRun);
	
	/**
	 * 获取某一个人的在线状态
	 * **/
	public INetRequest getOnlineStatusByUserid(INetResponse response, long id);
	
	/**
	 * 获取feed列表 接口 2012-4-09
	 * 
	 * @return
	 */
	public INetRequest getFeedByIds(INetResponse response, long[] id);
	
	/**
	 * 获取特别关注好友数目 接口 2012-5-06
	 * 
	 * @return
	 */
	public INetRequest getFocusFriendsNUM(INetResponse response);
	
	/**
	 * 检查更新
	 * 
	 * @param type
	 *            0:手动检查更新，1:自动检查更新
	 * @param response
	 * @param batchRun
	 * @author kuangxiaoyue
	 * @return
	 */
	public INetRequest getUpdateInfo(int type, INetResponse response, String lastTag, final boolean batchRun);

	/**
	 * 获取授权列表
	 * @param permissions 逗号分割的授权列表
	 * @param response 
	 * @param batch
	 * @return
	 */
	public INetRequest getPermissions(String[] permissions, INetResponse response, boolean batch);
		
	/**
	 * 上传Access Token 用于访问用户授权的资源，如果系统中不存在该UID，则为该UID生成一个私信帐号，并绑定该UID
	 * @param third_party 第三方帐号系统，列如Facebook
	 * @param access_token 访问用户资源所必须的Token
	 * @param expires 单位为秒的过期时间
	 * @param user_id 第三方系统用户ID
	 * @param email 第三方系统用户Email
	 * @param response
	 * @param batch
	 * @return
	 */
	public INetRequest uploadAccessToken(String third_party, String access_token, long expires, String user_id, String email, INetResponse response, boolean batch);
	
	
	/**
	 * 获取第三方授权登陆的个人信息
	 * @param access_token
	 * @param response
	 * @param batch
	 * @return
	 */
	public INetRequest getOauthPersonInfo(String access_token, INetResponse response, boolean batch);
	
	/**
	 * 第三方授权账户注销登陆信息
	 * @param response
	 * @param batch
	 * @return
	 */
	public INetRequest thirdOauthLogout(INetResponse response, boolean batch);
	
	/**
	 * 当用户access token失效的情况下，客户端重新上传该用户的access token
	 * @param session_key 第三方帐号类型：facebook，建议小写
	 * @param access_token
	 * @param expires 单位为秒的过期时间(facebook为60天)
	 * @param response
	 * @param batch
	 * @return
	 */
	public INetRequest refreshAccessToken(String session_key, String access_token, long expires, INetResponse response, boolean batch);
	
	/**
	 * 得到第三方账号的好友列表
	 * @param response
	 * @param id
	 * @param externalAccountType
	 * @param externalAccountId
	 * @param limit
	 * @param offset
	 * @return
	 */
	public INetRequest getThirdFriendsList(INetResponse response,String id,String externalAccountType,String externalAccountId,int limit,int offset);
	
	public INetRequest getThirdFriendsInfo(INetResponse response,String id,String externalAccountType,String externalAccountId,int limit,int offset);
	
	public INetRequest getSixinInfo(INetResponse response,String sinxId);
	
	
	/**
	 * 获取登录信息，用于票失效
	 * */
	public void getLoginInfo(INetResponse response);
	
	/**
	 * 获取表情
	 */
	public void getEmotionFromNet(String packageId,String emotionId,INetResponse response,boolean batch);
	
	/**
	 * 获取验证码
	 * @param number 邮箱或者手机号
	 */
	public void getVerifycode(String number, INetResponse response);
	
	/**
	 * 验证验证码是否正确
	 * @param number 邮箱或者手机号
	 * @param verifycode
	 * @param response
	 */
	public void submitWithVerifycode(String number, String verifycode, INetResponse response);
	
	/**
	 * 国内版使用
	 * 注册，需name、密码、验证码:
	 * @param number 邮箱或者手机号
	 * @param verifycode
	 * @param captcha
	 * @param response
	 */
	public void register(String number, String pwd, String captcha, int gender, String name, INetResponse response);
	 
	/**
	 * 国际版使用
	 * 注册，需firstName,lastName、密码、验证码
	 * @param number 邮箱或者手机号
	 * @param verifycode
	 * @param captcha
	 * @param response
	 */
	public void register(String number, String pwd, String captcha, int gender, String firstName, String lastName, INetResponse response);
	
	/**
	 * 获取私信联系人列表
	 * @param response
	 * @param page
	 * @param page_size
	 * @param batchRun
	 * @return
	 */
	public INetRequest getContactList(INetResponse response, int page,
			int page_size, boolean batchRun);
	
	/**
	 * 获取私信联系人列表(包含群组)
	 * @param response
	 * @param page
	 * @param page_size
	 * @param batchRun
	 * @return
	 */
	public INetRequest getContactListIncludeGroup(INetResponse response, int page,
			int page_size, boolean batchRun);
	
	/**
	 * 添加删除联系人
	 * @param response
	 * @param contactUid
	 * @param isADD true为添加，false为删除
	 * @return
	 */
	public  INetRequest optContact(INetResponse response, String contactUid,boolean isADD);
	
	public INetRequest addContact(INetResponse response, String contactUid,int verification_needed,String verification) ;
	public INetRequest delContact(INetResponse response, String contactUid);
	

	/**
	  * 获取联系人数量
	  * @param response
	  * @return
	  */
	public  INetRequest getContactNumber(INetResponse response);
	
	/**
	 * 添加删除黑名单
	 * @param response
	 * @param contactUid
	 * @param isADD true为添加，false为删除
	 * @return
	 */
	public  INetRequest optBlacklist(INetResponse response, String contactUid,boolean isADD);
	/**
	 * 获取黑名单列表
	 * @param response
	 * @param limit
	 * @param offset
	 * @param batchRun
	 * @return
	 */
	public INetRequest getBlacklist(INetResponse response, int limit,
			int offset, boolean batchRun);
	
	/**
	 * 获取全部黑名单列表
	 * @param response
	 * @return
	 */
	public INetRequest getBlacklist(INetResponse response);
	
	/**
	  * 获取黑名单数量
	  * @param response
	  * @return
	  */
	public  INetRequest getBlacklistNumber(INetResponse response);
	
	/**
	 * 获取历史聊天记录
	 * */
	public void getHistoryMessages(boolean multi, long toChatUserId, INetResponse response);

    /**
     * 获取历史聊天记录
     * */
    public void clearServerHistoryMessages(boolean multi, long toChatUserId, INetResponse response);
	
	/**
	 * 获取忘记密码的验证码
	 * @param number
	 * @param response
	 */
	public void getForgetPwdIdentifyCode(String number, INetResponse response);
	
	/**
	 * 验证验证码是否正确
	 * @param number
	 * @param verifycode
	 * @param response
	 */
	public void identifyForgetPwdCode(String number, String verifycode, INetResponse response);
	
	/**
	 * 忘记密码后，通过验证码修改密码
	 * @param number
	 * @param newPwd
	 * @param captcha
	 * @param response
	 */
	public void modifyForgetPwd(String number, String newPwd, String captcha, INetResponse response);
	
	/**
	  * 获取用户资料
	  * @param response
	  * @param uerId
	  * @param profile_types
	  * @return
	  */
	public  INetRequest getProfile(INetResponse response,String userId);
	
	/**
	  * 更新用户资料
	  * @param response
	  * @param map 需要更改的个人资料的map
	  * @return
	  */
	public  INetRequest putProfile(INetResponse response, Map<String, String> map);
	
	/**
	 * 用私信账号登陆后，绑定第三方账户
	 * @param access_token
	 * @param expires
	 * @param session_key
	 */
	public void bindThirdParty(String access_token, long expires, String session_key, INetResponse response);
	
	/**
	 * 解绑第三方账号
	 * @param session_key 当前登陆私信用户的session_key
	 * @param third_party_type 第三方账户类型，如：facebook,twitter
	 * @param third_party_uid 第三方账户id
	 * @param response
	 */
	public void removeThireParty(String session_key, String third_party_type, String third_party_uid, INetResponse response);
	
	/**
	 * 搜索联系人
	 * */
	public void searchContacts(String text, INetResponse response);

	/**
	 * 通讯录找人
	 * */
	public void contactFriends(ContactsContractModel[] contacts,
			INetResponse response);

	
	/**
	 * 绑定Email后者手机号
	 * @param response
	 * @param account Email或者手机号
	 * @param captcha 验证码
	 * @return
	 */
	public INetRequest bindAccount(INetResponse response, String account, String captcha, String passwordToken);

	/**
	 * 获取群组联系人(新接口)
	 * @param response
	 * @param batchRun
	 * @return
	 */
	public INetRequest getGroupContactInfo(INetResponse response, boolean batchRun);

	/**
	 * 获取用户隐私信息
	 * @param response
	 * @return
	 */
	public INetRequest getPrivacy(INetResponse response);
	
	/**
	 * 上传用户隐私信息
	 * @param response
	 * @param verification_required (可选)是否需要验证
	 * @param id_searchable (可选)是否可以通过私信ID搜索到
	 * @param name_searchable (可选)是否可以通过姓名搜索到
	 * @param phone_searchable (可选)是否可以通过手机号搜索到
	 * @return
	 */
	public INetRequest putPrivacy(INetResponse response, boolean verification_required, boolean id_searchable, boolean name_searchable, boolean phone_searchable);
	
	
	/**
	 * 查询系统已开通的插件列表
	 * @param response
	 * @param offset (可选)页数(不传默认为0)
	 * @param limits (可选)每页记录数每页记录数(不传默认为最大整数)
	 * @return
	 */
	public void queryAllPluginInfos(INetResponse response, Integer offset, Integer limits);
	
	/**
	 * 查询单个插件信息
	 * @param response
	 * @param plugin_id 根据此参数返回所查询插件详情
	 * @return
	 */
	public void querySinglePluginInfo(INetResponse response, Integer plugin_id);
	
	/**
	 * 根据插件的plugin_id在服务器生成订购关系（安装某一插件后调用此接口）
	 * @param response
	 * @param plugin_id 所要订购的插件plugin_id
	 * @return
	 */
	public void installPlugin(INetResponse response, Integer plugin_id);
	
	/**
	 * 根据插件的plugin_id在服务器解除订购关系（卸载某一插件后调用此接口）
	 * @param response
	 * @param plugin_id 所要卸载的插件plugin_id
	 * @return
	 */
	public void uninstallPlugin(INetResponse response, Integer plugin_id);
	
	/**
	 * 配置指定插件或者全部插件是否推送信息
	 * @param response
	 * @param plugin_id (可选?)根据此参数配置指定插件是否推送消息; 此参数不存在,则对全部插件进行配置？
	 * @param pushflag true:push, false:不push
	 * @return
	 */
	public void settingPluginPush(INetResponse response, Integer plugin_id, boolean pushflag);
	
	/**
	 * 查询用户已经开启的插件列表
	 * @param response
	 * @param user_id 当前用户的uid
	 * @param offset (可选)页数(不传默认为0)
	 * @param limits (可选)每页记录数每页记录数(不传默认为最大整数)
	 * @return
	 */
	public void queryUserPlugins(INetResponse response, String user_id,
    		Integer offset, Integer limits, Integer type, Integer hasDetailOrNot);
	
	/**
	 * 修改密码
	 * @param response
	 * @param oldMd5 用户旧密码的32位md5小写
	 * @param newMd5 用户新密码的32位md5小写
	 * @param sessionId talk的sessionId
	 * @return
	 */
	public INetRequest modifyPassword(INetResponse response, String oldMd5, String newMd5, String sessionId);
	
	/**
	 * 注销
	 * @param response
	 * @param sessionId talk的sessionId
	 * @return
	 */
	public INetRequest logout(INetResponse response, String sessionId);
	
	/**
	 * add by xiangchao.fan
	 * 搜索college
	 * */
	public void searchColleges(String text, INetResponse response);

    /**
     * 上传头像
     * @param response
     */
    public void uploadHeadPhoto(INetResponse response, int x, int y, int width, int height, byte[] data);

    /**
     * 上传图片
     * @param response
     */
    public void uploadPhoto(INetResponse response, byte[] data);

    /**
     * 上传文件
     * @param response
     */
    public void uploadFile(INetResponse response, byte[] data);
    
    /**
     * 获取绑定验证码接口(待定)
     * @param response
     * @param email 邮箱的名称
     */
    public void getAccountCaptcha(INetResponse response, String email);
    
    /**
     * 上传语言变更
     * @param response
     * @param language 当前语言 zh_CN/ en_US
     */
    public void uploadLanguage(INetResponse response, String language);
    
    /**
     * 获取密码验证的token，用于换绑邮箱
     * @param response
     * @param passwordMd5 用户输入密码的小写32位md5值
     */
    public void getPasswordToken(INetResponse response, String passwordMd5);

    /**
     * 分享链接到Facebook
     * @param response
     */
    public void shareLinkToFacebook(INetResponse response);

    /**
     * 获取升级信息
     * @param response
     * @param up   	0:手动检查更新,1:自动检查更新
     * @param name 	1:国际版私信,2:国内版私信
     * @param from 	渠道号
     * @param pubDate 	发布日期,格式：20090909
     * @param version 	版本,格式：3.2.1:第一个号码为大版本号,第二个号为中版本号,第三个为小版本号
     * @param language 	语言: zh_TW/zh_CN/ en_US
     */
    public void getUpdateInformation(INetResponse response, int up, int name, int from, int pubDate, String version, String language);

    /**
     * 客户端的通用统计接口
     */
    public void commonLog(String bundle, INetResponse response,long user_id);
}
