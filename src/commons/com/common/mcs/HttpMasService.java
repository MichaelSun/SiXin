package com.common.mcs;

import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import com.common.app.AbstractRenrenApplication;
import com.common.contactscontract.ContactsContractModel;
import com.common.manager.LoginManager;
import com.common.statistics.Htf;
import com.core.json.JsonObject;
import com.core.util.CommonUtil;
import com.core.util.SystemService;

import java.util.Map;

/**
 * @author dingwei.chen(update by yuchao.zhang 复制于HttpMasService)
 * @MCS 网络业务
 */
public final class HttpMasService extends AbstractHttpMcsService {

    private static HttpMasService sInstance = new HttpMasService();

    private HttpMasService() {
    }

    public static HttpMasService getInstance() {
        return sInstance;
    }

    @Override
    public void login(String account, String passwordMd5, int captcha_needed, String captcha, long session,
                      INetResponse response) {
        TelephonyManager tm = SystemService.sTelephonyManager;
        JsonObject bundle = obtainBaseRequestBundle(false);
        // 使用 3G 服务器的 api_key
        bundle.put("api_key", AbstractHttpMcsService.MCS_API_KEY);
        bundle.put("call_id", System.currentTimeMillis());
        bundle.put("client_info", getClientInfo());
        bundle.put("v", "1.0");
        bundle.put("format", "JSON");
        bundle.put("user", account);
        bundle.put("password", passwordMd5);
        bundle.put("captcha_needed", 1);
        bundle.put("captcha", captcha);
        bundle.put("uniq_id", tm.getDeviceId());
        bundle.remove("session_key");
//		bundle.put(INetRequest.gzip_key, INetRequest.gzip_value);
        bundle.put("sig", getSigForLogin(bundle));
        INetRequest request = obtainINetRequest("/user/login");
        request.setData(bundle);
        request.setResponse(response);
        request.setSecretKey(AbstractHttpMcsService.LOCAL_SECRET_KEY);
        request.setCurrentSession(session);
        HttpProviderWrapper.getInstance().addRequest(request);
    }

    /*登录 用私信帐号登陆*/
    @Override
    public void loginSiXin(String account, String passwordMd5, long session,
                           INetResponse response) {
        CommonUtil.log("sunnyykn", "loginSixin");
        TelephonyManager tm = SystemService.sTelephonyManager;
        JsonObject bundle = obtainBaseRequestBundle(false);
        // 使用 3G 服务器的 api_key
        bundle.put("api_key", AbstractHttpMcsService.MCS_API_KEY);
        bundle.put("call_id", System.currentTimeMillis());
        bundle.put("client_info", getClientInfo());
        bundle.put("v", "1.0");
        bundle.put("format", "JSON");
        bundle.put("user", account);
        bundle.put("password", passwordMd5);
        bundle.put("uniq_id", tm.getDeviceId());
        bundle.remove("session_key");
//		bundle.put(INetRequest.gzip_key, INetRequest.gzip_value);
        bundle.put("sig", getSigForLogin(bundle));
        INetRequest request = obtainINetRequest("/user/login");
        request.setData(bundle);
        request.setResponse(response);
        request.setSecretKey(AbstractHttpMcsService.LOCAL_SECRET_KEY);
        request.setCurrentSession(session);
        HttpProviderWrapper.getInstance().addRequest(request);
    }

    /*登录 用人人帐号登陆*/
    @Override
    public void loginRenRen(String account, String passwordMd5, long session,
                            INetResponse response) {
        CommonUtil.log("sunnyykn", "loginRenren");
        CommonUtil.log("sunnyykn", "api_key:" + AbstractHttpMcsService.MCS_API_KEY);
        TelephonyManager tm = SystemService.sTelephonyManager;
        JsonObject bundle = obtainBaseRequestBundle(false);
        bundle.put("api_key", AbstractHttpMcsService.MCS_API_KEY);
        bundle.put("call_id", System.currentTimeMillis());
        bundle.put("client_info", getClientInfo());
        bundle.put("v", "1.0");
        bundle.put("format", "JSON");
        bundle.put("user", account);
        bundle.put("password", passwordMd5);
        bundle.put("uniq_id", tm.getDeviceId());
        bundle.remove("session_key");
//		bundle.put(INetRequest.gzip_key, INetRequest.gzip_value);
        bundle.put("sig", getSigForLogin(bundle));
        INetRequest request = obtainINetRequest("/user/login/renren");
        request.setData(bundle);
        request.setResponse(response);
        request.setSecretKey(AbstractHttpMcsService.LOCAL_SECRET_KEY);
        request.setCurrentSession(session);
        HttpProviderWrapper.getInstance().addRequest(request);
    }
    
    /*登录 用私信帐号登陆*/
    @Override
    public void loginSiXin(String account, String passwordMd5, long session, int captcha_needed, String captcha,
                           INetResponse response) {
        CommonUtil.log("sunnyykn", "loginSixin");
        TelephonyManager tm = SystemService.sTelephonyManager;
        JsonObject bundle = obtainBaseRequestBundle(false);
        // 使用 3G 服务器的 api_key
        bundle.put("api_key", AbstractHttpMcsService.MCS_API_KEY);
        bundle.put("call_id", System.currentTimeMillis());
        bundle.put("client_info", getClientInfo());
        bundle.put("v", "1.0");
        bundle.put("format", "JSON");
        bundle.put("user", account);
        bundle.put("password", passwordMd5);
        bundle.put("captcha_needed", captcha_needed);
        bundle.put("captcha", captcha);
        
        bundle.put("uniq_id", tm.getDeviceId());
        bundle.remove("session_key");
//		bundle.put(INetRequest.gzip_key, INetRequest.gzip_value);
        bundle.put("sig", getSigForLogin(bundle));
        INetRequest request = obtainINetRequest("/user/login");
        request.setData(bundle);
        request.setResponse(response);
        request.setSecretKey(AbstractHttpMcsService.LOCAL_SECRET_KEY);
        request.setCurrentSession(session);
        HttpProviderWrapper.getInstance().addRequest(request);
    }

    /*登录 用人人帐号登陆*/
    @Override
    public void loginRenRen(String account, String passwordMd5, long session, int captcha_needed, String captcha,
                            INetResponse response) {
        CommonUtil.log("sunnyykn", "loginRenren");
        TelephonyManager tm = SystemService.sTelephonyManager;
        JsonObject bundle = obtainBaseRequestBundle(false);
        // 使用 3G 服务器的 api_key
        bundle.put("api_key", AbstractHttpMcsService.MCS_API_KEY);
        bundle.put("call_id", System.currentTimeMillis());
        bundle.put("client_info", getClientInfo());
        bundle.put("v", "1.0");
        bundle.put("format", "JSON");
        bundle.put("user", account);
        bundle.put("password", passwordMd5);
        bundle.put("captcha_needed", captcha_needed);
        bundle.put("captcha", captcha);
        
        bundle.put("uniq_id", tm.getDeviceId());
        bundle.remove("session_key");
//		bundle.put(INetRequest.gzip_key, INetRequest.gzip_value);
        bundle.put("sig", getSigForLogin(bundle));
        INetRequest request = obtainINetRequest("/user/login/renren");
        request.setData(bundle);
        request.setResponse(response);
        request.setSecretKey(AbstractHttpMcsService.LOCAL_SECRET_KEY);
        request.setCurrentSession(session);
        HttpProviderWrapper.getInstance().addRequest(request);
    }

    /*获取注册验证码*/
    @Override
    public void getCaptcha(String user, String action, INetResponse response) {
        JsonObject bundle = obtainBaseRequestBundle(false);
        bundle.put("user", user);
        bundle.put("action", "GET");

        bundle.put("api_key", AbstractHttpMcsService.MCS_API_KEY);
        bundle.put("call_id", System.currentTimeMillis());
        bundle.put("sig", getSigForLogin(bundle));
        this.sendRequest("/user/register/captcha", bundle, response);
    }

    /*验证注册验证码*/
    @Override
    public void checkCaptcha(String user, String captcha, INetResponse response) {
        JsonObject bundle = obtainBaseRequestBundle(false);
        bundle.put("user", user);
        bundle.put("captcha", captcha);

        bundle.put("api_key", AbstractHttpMcsService.MCS_API_KEY);
        bundle.put("call_id", System.currentTimeMillis());
        bundle.put("sig", getSigForLogin(bundle));
        this.sendRequest("/user/register/captcha", bundle, response);
    }

    /*获取绑定换绑验证码*/
    @Override
    public void getCaptchaBinding(String account, String action, INetResponse response) {
        JsonObject bundle = obtainBaseRequestBundle(false);
        bundle.put("account", account);
        bundle.put("action", action);
        bundle.put("session_key", LoginManager.getInstance().getSsssionKey());

        bundle.put("api_key", AbstractHttpMcsService.MCS_API_KEY);
        bundle.put("call_id", System.currentTimeMillis());
        bundle.put("sig", getSig(bundle));
        this.sendRequest("/user/binding/account/captcha", bundle, response);
    }

    // 绑定邮箱或手机号
    @Override
    public void bindAccount(String account, String captcha, String passwordToken, INetResponse response) {
        JsonObject bundle = obtainBaseRequestBundle(false);
        bundle.put("account", account);
        bundle.put("captcha", captcha);
        bundle.put("session_key",LoginManager.getInstance().getSsssionKey());
        bundle.put("password_token", passwordToken);

        bundle.put("api_key", AbstractHttpMcsService.MCS_API_KEY);
        bundle.put("call_id", System.currentTimeMillis());
        bundle.put("sig", getSig(bundle));
        this.sendRequest("/user/binding/account", bundle, response);
    }

    @Override
    public void getFeedList(int page, int page_size, String types,
                            INetResponse response) {
        JsonObject bundle = obtainBaseRequestBundle(false);
        bundle.put("v", "1.0").put("call_id", System.currentTimeMillis()).put("action", "GET").put("page", page).put("page_size", page_size)
                .put("focus", 1).put("type", "502,701,709");
//				.put(INetRequest.gzip_key, INetRequest.gzip_value);
        this.sendRequest("/feed/feeds", bundle, response);
        CommonUtil.log("cdw", "getFeedList");
    }

    @Override
    public void getFeedList(long[] id, INetResponse response) {
        String temp = "" + id[0];
        for (int num = 1; num < id.length; num++) {
            temp += "," + id[num];
        }
        JsonObject bundle = obtainBaseRequestBundle(false);
        bundle.put("v", "1.0").put("fids", temp)
                .put(INetRequest.gzip_key, INetRequest.gzip_value);
        sendRequest("/feed/getByIds", bundle, response);
    }

    @Override
    public void getImage(String url, INetResponse response) {
        INetRequest request = this.obtainINetRequest(url,
                INetRequest.TYPE_HTTP_GET_IMG, response);
        request.setPriority(INetRequest.PRIORITY_LOW_PRIORITY);
        request.setSecretKey(LoginManager.getInstance().getSecretKey());
        HttpProviderWrapper.getInstance().addRequest(request);
    }

    @Override
    public void getVoice(String url, INetResponse response) {
        INetRequest request = obtainINetRequest(url,
                INetRequest.TYPE_HTTP_GET_VOICE, response);
        request.setSecretKey(LoginManager.getInstance().getSecretKey());
        HttpProviderWrapper.getInstance().addRequest(request);
    }

    @Override
    public INetRequest getFriends(int page, int page_size, boolean batchRun, INetResponse response) {
        final JsonObject bundle = obtainBaseRequestBundle(false);
        if (batchRun) {
            bundle.put("method", "friends.getOnlineFriends");
        }
        bundle.put("v", "1.0");
        bundle.put("hasGender", 1);// 包括性别
        bundle.put("hasBirthday", 1);// 包括生日
        if ((page_size != -1) || (page == -1)) {
            bundle.put("page", page);
            bundle.put("page_size", page_size);
        }
        bundle.put("hasLargeHeadUrl", 1);// 包括大头像
        bundle.put("hasMainHeadUrl", 1);// 包括大头像
        bundle.put("useShortUrl", 1);// 使用短连接
        bundle.put("hasFocused", 1);// 请求特别关注
        INetRequest request = obtainINetRequest("");
        if (!batchRun) {
            bundle.put(INetRequest.gzip_key, INetRequest.gzip_value);
            this.sendRequest("/friends/getOnlineFriends", bundle, response);
        } else {
            request = obtainINetRequest("");
            request.setData(bundle);
            request.setResponse(response);
        }
        return request;
    }

    @Override
    public INetRequest getGroups(boolean batchRun, INetResponse response) {
        INetRequest request = null;
        JsonObject bundle = obtainBaseRequestBundle(batchRun);
        if (batchRun) {
            bundle.put("method", "talk.getGroupList");
        }
        return request;
    }

    @Override
    public void getFriendsRequests(int page, int page_size, int exclude_list,
                                   int del_news, int htf, INetResponse response) {
        JsonObject bundle = obtainBaseRequestBundle(false);
        if ((page != -1) && (page_size != -1)) {
            bundle.put("page", page);
            bundle.put("page_size", page_size);
        }
        bundle.put("del_news", del_news);
        bundle.put("htf", htf);//统计需求
        bundle.put("exclude_list", exclude_list);
        bundle.put(INetRequest.gzip_key, INetRequest.gzip_value);
        sendRequest("/friends/getRequests", bundle, response);
    }

    @Override
    public void postPhoto(byte[] imgData, long toId, INetResponse response) {
        JsonObject bundle = obtainBaseRequestBundle(false);
        INetRequest request = obtainINetRequest("/file/photo");
        bundle.put("data", buildUploadPhotoData(toId, imgData));
        request.setData(bundle);
        request.setResponse(response);
        // 使用 3G 服务器的 secret_key
        request.setSecretKey(LoginManager.getInstance().getSecretKey());
        request.setType(INetRequest.TYPE_HTTP_POST_IMG);
        HttpProviderWrapper.getInstance().addRequest(request);
    }

    @Override
    public void postVoice(long toId, String vid, int seqid, String mode, int playTime, byte[] voiceData, INetResponse response) {
        CommonUtil.log("VoiceUpload", "send Voice Request");
        JsonObject bundle = obtainBaseRequestBundle(false);
        INetRequest request = obtainINetRequest("/file/common_file");
        bundle.put("data", buildUploadAudioData(toId, vid, seqid, mode, playTime, voiceData));
        request.setData(bundle);
        request.setResponse(response);
        // 使用 3G 服务器的 secret_key
        request.setSecretKey(LoginManager.getInstance().getSecretKey());
        request.setType(INetRequest.TYPE_HTTP_POST_BIN_File);
        HttpProviderWrapper.getInstance().addRequest(request);
    }

    @Override
    public void backgroundRunStatistics(INetResponse response) {
		JsonObject bundle = obtainBaseRequestBundle(false);
		bundle.put("v", "1.0");
		bundle.put("format", "json");
		
		bundle.put("sig", getSig(bundle));
		
		sendRequest("/client/maintain", bundle, response);
    }

    @Override
    public void commonLog(String bundle, INetResponse response,long uid) {
        JsonObject bundle_commlog = obtainBaseRequestBundle(false);

        bundle_commlog.put("v", "1.0");
        bundle_commlog.put("format", "json");
        bundle_commlog.put("context", bundle);
        bundle_commlog.put("user_id", uid);
//		bundle.put("fromId", String.valueOf(AbstractRenrenApplication.FROM));
//		bundle.put("terminal", "android");
//		bundle.put("version", AbstractRenrenApplication.VERSION_NAME);

        bundle_commlog.put("sig", getSigForLogin(bundle_commlog));
        
        sendRequest("/client/common_log", bundle_commlog, response);
    }

    @Override
    public void uploadStatistics(String localStatistics, INetResponse response) {
		JsonObject bundle = obtainBaseRequestBundle(false);

		bundle.put("v", "1.0");
		bundle.put("format", "json");
		bundle.put("context", localStatistics);
		
//		Log.v("fxch", "=======================uploadStatistics interface:" + localStatistics);
//		bundle.put("fromId", String.valueOf(AbstractRenrenApplication.FROM));
//		bundle.put("terminal", "android");
//		bundle.put("version", AbstractRenrenApplication.VERSION_NAME);
		
		bundle.put("sig", getSigForLogin(bundle));
		
		bundle.put("user_id", LoginManager.getInstance().getLoginInfo().mUserId);
		
		sendRequest("/client/action", bundle, response);
    }

    @Override
    public INetRequest activeClient(String imei, int type,
                                    INetResponse response, boolean batchRun) {
        JsonObject bundle = obtainBaseRequestBundle(batchRun);
//		bundle.put("method", "phoneclient.activeClient");
        bundle.put("v", "1.0");
        bundle.put("format", "json");
//		bundle.put("uniq_id", imei);
//		JsonArray dataArray = new JsonArray();
//		JsonObject dataJson = new JsonObject();
//		dataArray.add(dataJson);
//		dataJson.put("type", type);
//		dataJson.put("num", 1);
//		bundle.put("data", dataArray.toJsonString());
//		bundle.remove("session_key");

        bundle.put("sig", getSigForLogin(bundle));
        
        INetRequest request = obtainINetRequest("");
        request.setSecretKey(LoginManager.getInstance().getSecretKey());
        if (batchRun) {
            request.setData(bundle);
            request.setResponse(response);
            return request;
        } else {
            sendRequest("/client/active", bundle, response);
            return null;
        }
    }

    @Override
    public void matchContacts(boolean isSkip, ContactsContractModel[] contacts, INetResponse response) {
        JsonObject bundle = obtainBaseRequestBundle(false);

        String data = null;
		if (!isSkip)
			data = ContactsContractModel.generateContactsJsonString(contacts,
					LoginManager.getInstance().getSsssionKey());
		bundle.put("contacts", data);
		bundle.put("v", "1.0");
		bundle.put("format", "json");
		// bundle.put("friend_type_switch", "1");
		bundle.put("session_key", LoginManager.getInstance().getSsssionKey());
        sendRequest("/contacts/sychnorize", bundle, response);
    }

    @Override
    public INetRequest m_friendsRequest(long uid, String content,
                                        INetResponse response, boolean batchRun) {
        JsonObject bundle = obtainBaseRequestBundle(batchRun);

//		bundle.put("method", "friends.request");
        bundle.put("uid", uid);
        if (content != null) {
            bundle.put("content", content);
        }
        bundle.put("v", "1.0");
        bundle.put("format", "json");
        // if (!batchRun) {
        // bundle.put(INetRequest.gzip_key, INetRequest.gzip_value);
        // }
        INetRequest request = obtainINetRequest("");
        request.setSecretKey(LoginManager.getInstance().getSecretKey());
        if (batchRun) {
            request.setData(bundle);
            request.setResponse(response);
            return request;
        } else {
            sendRequest("/friends/request", bundle, response);
            return null;
        }
    }

    @Override
    public INetRequest m_friendsRequest(long uid, String content,
                                        INetResponse response, boolean batchRun, String HTF) {
        JsonObject bundle = obtainBaseRequestBundle(batchRun);


        bundle.put("misc", getMISCInfo(HTF, null));


//		bundle.put("method", "friends.request");
        bundle.put("uid", uid);
        if (content != null) {
            bundle.put("content", content);
        }
        bundle.put("v", "1.0");
        bundle.put("format", "json");
        // if (!batchRun) {
        // bundle.put(INetRequest.gzip_key, INetRequest.gzip_value);
        // }
        INetRequest request = obtainINetRequest("");
        request.setSecretKey(LoginManager.getInstance().getSecretKey());
        if (batchRun) {
            request.setData(bundle);
            request.setResponse(response);
            return request;
        } else {
            sendRequest("/friends/request", bundle, response);
            return null;
        }
    }

    @Override
    public INetRequest getFriendsRequest(INetResponse response, int page,
                                         int page_size, int exclude_list, int del_news, boolean batchRun) {
        JsonObject bundle = obtainBaseRequestBundle(false);
        bundle.put("method", "friends.getRequests");
        bundle.put("v", "1.0");
        bundle.put("page", page);
        bundle.put("page_size", page_size);
        bundle.put("del_news", del_news);
        bundle.put("htf", Htf.REQUEST_BUTTON);
        bundle.put("exclude_list", exclude_list);
        if (!batchRun) {
            bundle.put(INetRequest.gzip_key, INetRequest.gzip_value);
        }

        INetRequest request = obtainINetRequest("");
        request.setSecretKey(LoginManager.getInstance().getSecretKey());
        if (batchRun) {
            request.setData(bundle);
            request.setResponse(response);
            return request;
        } else {
            sendRequest("", bundle, response);
            return null;
        }
    }

    @Override
    public INetRequest acceptFriendRequest(long uid, INetResponse response,
                                           boolean batchRun) {
        JsonObject bundle = obtainBaseRequestBundle(false);
//		bundle.put("method", "friends.accept");
        bundle.put("v", "1.0");
        bundle.put("user_id", uid);
        bundle.put("htf", Htf.REQUEST_RECIVIED);

        INetRequest request = obtainINetRequest("");
        request.setSecretKey(LoginManager.getInstance().getSecretKey());
        if (batchRun) {
            request.setData(bundle);
            request.setResponse(response);
            return request;
        } else {
            sendRequest("/friends/accept", bundle, response);
            return null;
        }
    }

    @Override
    public INetRequest denyFriendRequest(long uid, INetResponse response,
                                         boolean batchRun) {
        JsonObject bundle = obtainBaseRequestBundle(false);
//		bundle.put("method", "friends.deny");
        bundle.put("v", "1.0");
        bundle.put("user_id", uid);

        INetRequest request = obtainINetRequest("");
        request.setSecretKey(LoginManager.getInstance().getSecretKey());
        if (batchRun) {
            request.setData(bundle);
            request.setResponse(response);
            return request;
        } else {
            sendRequest("/friends/deny", bundle, response);
            return null;
        }
    }

    @Override
    public INetRequest supplyUserInfo(INetResponse response, String userName,
                                      String userGender, int year, int month, int day, int stage,
                                      boolean batchRun) {
        JsonObject bundle = obtainBaseRequestBundle(false);
//		bundle.put("method", "user.fillInfo");
        bundle.put("v", "1.0");
        bundle.put("name", userName);
        bundle.put("gender", userGender);
        bundle.put("year", year);
        bundle.put("month", month);
        bundle.put("day", day);
        bundle.put("stage", stage);

        INetRequest request = obtainINetRequest("");
        request.setSecretKey(LoginManager.getInstance().getSecretKey());
        if (batchRun) {
            request.setData(bundle);
            request.setResponse(response);
            return request;
        } else {
            sendRequest("/user/fillInfo", bundle, response);
            return null;
        }
    }

    @Override
    public void getFocusFriends(INetResponse response) {
        JsonObject bundle = obtainBaseRequestBundle(false);
        bundle.put("v", "1.0");
        bundle.put("format", "json");
        sendRequest("/friends/getFocusFriends", bundle, response);
    }

    @Override
    public void isFansOfPage(long userId, long pageId, INetResponse response) {
        JsonObject bundle = obtainBaseRequestBundle(false);

        bundle.put("v", "1.0");
        bundle.put("format", "json");
        bundle.put("user_id", userId);
        bundle.put("page_id", pageId);

        sendRequest("/page/isFans", bundle, response);
    }

    @Override
    public void becomeFansOfPage(int pageId, INetResponse response) {
        JsonObject bundle = obtainBaseRequestBundle(false);

        bundle.put("v", "1.0");
        bundle.put("format", "json");
        bundle.put("page_id", pageId);

        sendRequest("/page/becomeFan", bundle, response);
    }

    @Override
    public INetRequest m_statusSet(INetResponse response, boolean batchRun) {
        JsonObject bundle = obtainBaseRequestBundle(batchRun);
//		bundle.put("method", "share.publishLink");
        bundle.put("desc", "");
        bundle.put("thumb_url", "http://fmn.rrimg.com/fmn060/20120628/1950/original_6wNB_2a6600001075118e.jpg");
//		bundle.put("url", "http://3g.renren.com/ep.do?c=9504901");
        bundle.put("url", "http://mobile.renren.com/home?psf=42053");
        bundle.put("title", "新版私信可以群聊了！和好友们畅聊一“夏”吧！立即下载！");
        bundle.put("comment", "");
        bundle.put("from", AbstractRenrenApplication.FROM);
        bundle.put("v", "1.0");
        bundle.put("format", "json");
        bundle.put("htf", Htf.RAPID_PUB_ACTIVITY);

        INetRequest request = obtainINetRequest("");
        request.setSecretKey(LoginManager.getInstance().getSecretKey());
        if (batchRun) {
            request.setData(bundle);
            request.setResponse(response);
            return request;
        } else {
            sendRequest("/share/publishLink", bundle, response);
            return null;
        }
    }


    @Override
    public void postLog(String stackInfo, String exMessage,
                        INetResponse response) {
        JsonObject bundle = obtainBaseRequestBundle(false);
        bundle.put("v", "1.0");
        bundle.put("format", "json");
        bundle.put("event_type", "10000");
        bundle.put("exception_type", exMessage);
        bundle.put("stack_info", stackInfo);
        
        bundle.put("sig", getSigForLogin(bundle));
        bundle.put("user_id", LoginManager.getInstance().getLoginInfo().mUserId);
        
        this.sendRequest("/client/event", bundle, response);
    }

    @Override
    public void postFocusFriend(boolean isFoucs, long userId,
                                INetResponse response) {
        JsonObject bundle = this.obtainBaseRequestBundle(false);
        bundle.put("uid", userId);
        if (isFoucs) {
            this.sendRequest("/friends/addFocusFriend", bundle, response);
        } else {
            this.sendRequest("/friends/delFocusFriend", bundle, response);
        }

    }

    @Override
    public void getFriendsOnlineStatus(INetResponse response) {
/*temp comment
		JsonObject json = this.obtainBaseRequestBundle(false);
		this.sendRequest("/friends/getOnlineFriendsStatus", json, response);
*/
    }

    @Override
    public INetRequest getFriendInfo(int type, long uid, boolean batchRun,
                                     INetResponse response) {
        JsonObject bundle = this.obtainBaseRequestBundle(batchRun);
        bundle.put("v", "1.0");
        bundle.put("uid", uid);
        bundle.put("type", type);
        if (!batchRun) {
            bundle.put(INetRequest.gzip_key, INetRequest.gzip_value);
        }
        if (batchRun) {
            INetRequest request = this.obtainINetRequest("/profile/getInfo");
            request.setData(bundle);
            return request;
        } else {
            this.sendRequest("/profile/getInfo", bundle, response);
            return null;
        }
    }

    @Override
    public void getFriendOnlineStatus(long userId, INetResponse response) {
        JsonObject bundle = this.obtainBaseRequestBundle(false);
        bundle.put("id_list", String.valueOf(userId));
        this.sendRequest("/friends/isOnline", bundle, response);
    }

    @Override
    public void addFocusFriend(INetResponse response, long uid) {
        JsonObject bundle = obtainBaseRequestBundle(false);
        bundle.put("v", "1.0");
        bundle.put("focus_uid", uid);
        sendRequest("/friends/focus_friends/renren", bundle, response);
    }

    @Override
    public void delFocusFriend(INetResponse response, long uid) {
        JsonObject bundle = obtainBaseRequestBundle(false);
        bundle.put("v", "1.0");
        bundle.put("focus_uid", uid);
        bundle.put("action", "DELETE");
        sendRequest("/friends/focus_friends/renren", bundle, response);
    }

//	@Override
//	public INetRequest getOnlineFriendList(INetResponse response, int page,
//			int page_size, boolean batchRun) {
//		//Logd.log("batchRun="+batchRun);
//		JsonObject bundle = obtainBaseRequestBundle(batchRun);
//		if(batchRun){
//			//bundle.put("method", "friends.getOnlineFriends");
//			//contcat/contactlist
//			bundle.put("method", "contact.contactlist");
//		}
//		
////		bundle.put("v", "1.0");
////		bundle.put("page", page);
////		bundle.put("hasGender", 1);// 包括性别
////		bundle.put("hasBirthday", 1);// 包括生日
////		bundle.put("page_size", page_size);
////		bundle.put("hasLargeHeadUrl", 1);// 包括大头像
////		bundle.put("hasMainHeadUrl", 1);// 包括大头像
////		bundle.put("useShortUrl", 1);// 使用短连接
////		bundle.put("hasFocused", 1);// 请求特别关注
//		
//		
//		bundle.put("offset",page);
//		bundle.put("limit",page_size);
//		bundle.put("profile_type",1023);
//		bundle.put("action", "GET");
//
//		INetRequest request = obtainINetRequest("");
//		request.setSecretKey(sRuntimeSecretKey);
//		if (batchRun) {
//			request.setData(bundle);
//			request.setResponse(response);
//			return request;
//		} else {
//			//bundle.put(INetRequest.gzip_key, INetRequest.gzip_value);
//			sendRequest("/contact/contactlist", bundle, response);
//			return null;
//		}
//	}

    @Override
    public INetRequest getGroupContact(INetResponse response, boolean batchRun) {
        JsonObject bundle = obtainBaseRequestBundle(false);
        if (batchRun) {
            bundle.put("method", "talk.getGroupList");
        }
        bundle.put("v", "1.0");
        bundle.put("sig", "");
        bundle.put("v ", "1.0");
        bundle.put("page", 1);
        bundle.put("page_size", 100);
        bundle.put("head_url_switch", 2);
        bundle.put("member_detail", 1 + 2 + 4 + 8 + 16 + 64 + 128 + 256);

        INetRequest request = obtainINetRequest("");
        request.setSecretKey(LoginManager.getInstance().getSecretKey());
        if (batchRun) {
            request.setData(bundle);
            request.setResponse(response);
            return request;
        } else {
            bundle.put(INetRequest.gzip_key, INetRequest.gzip_value);
            sendRequest("/talk/getGroupList", bundle, response);
            return null;
        }
    }

    @Override
    public INetRequest getOnlineFriendListSimple(INetResponse response) {
/*temp comment
		JsonObject bundle = obtainBaseRequestBundle(false);
		bundle.put("v", "1.0");
//		bundle.put(INetRequest.gzip_key, INetRequest.gzip_value);
		sendRequest("/friends/getOnlineFriendsStatus", bundle, response);
*/
        return null;
    }

    @Override
    public INetRequest getFriendInfo(int type, Long uid, INetResponse response,
                                     boolean batchRun) {
        JsonObject bundle = obtainBaseRequestBundle(false);
//		bundle.put("method", "profile.getInfo");
        bundle.put("v", "1.0");
        bundle.put("uid", uid);
        bundle.put("type", type);
        if (!batchRun) {
            bundle.put(INetRequest.gzip_key, INetRequest.gzip_value);
        }

        INetRequest request = obtainINetRequest("");
        request.setSecretKey(LoginManager.getInstance().getSecretKey());
        if (batchRun) {
            request.setData(bundle);
            request.setResponse(response);
            return request;
        } else {
            sendRequest("/profile/getInfo", bundle, response);
            return null;
        }
    }

    @Override
    public INetRequest getOnlineStatusByUserid(INetResponse response, long id) {
        JsonObject bundle = obtainBaseRequestBundle(false);
//		bundle.put("method", "friends.getOnlineFriends");
        bundle.put("v", "1.0");
        bundle.put("id_list", String.valueOf(id));
        sendRequest("/friends/isOnline", bundle, response);
        return null;
    }

    @Override
    public INetRequest getFeedByIds(INetResponse response, long[] id) {
        JsonObject bundle = obtainBaseRequestBundle(false);
//		bundle.put("method", "friends.getOnlineFriends");
        bundle.put("v", "1.0");
        bundle.put("call_id", System.currentTimeMillis());
        bundle.put("action", "GET");
        String temp = "" + id[0];
        for (int num = 1; num < id.length; num++) {
            temp += "," + id[num];
        }
        bundle.put("fids", temp);
        sendRequest("/feed/feeds", bundle, response);
        return null;
    }

    @Override
    public INetRequest getFocusFriendsNUM(INetResponse response) {
        JsonObject bundle = obtainBaseRequestBundle(false);
        bundle.put("action", "GET");
        sendRequest("/friends/focus_friends/renren", bundle, response);
        return null;
    }

    @Override
    public INetRequest getUpdateInfo(int type, INetResponse response,
                                     String lastTag, boolean batchRun) {
        JsonObject bundle = obtainBaseRequestBundle(false);
//		bundle.put("method", "phoneclient.getUpdateInfo");
//        bundle.put("api_key", MCS_API_KEY);
        bundle.put("v", "1.0"); 
        bundle.put("call_id", System.currentTimeMillis());
//        bundle.put("sig", getSig(bundle));
//        bundle.put("session_key", LoginManager.getInstance().getSsssionKey());
        bundle.put("name", 2);// 2代表人人chat
        bundle.put("property", 5);
        bundle.put("subproperty", 0);
        bundle.put("version", AbstractRenrenApplication.VERSION_NAME);
        bundle.put("channelId", AbstractRenrenApplication.FROM);
        bundle.put("ua", "");
        bundle.put("os", Build.VERSION.SDK + "_" + Build.VERSION.RELEASE);
        bundle.put("pubdate", AbstractRenrenApplication.PUBLIC_DATE);
        bundle.put("up", type);
        bundle.put("format", "json");
//        if (lastTag != null && !lastTag.equals("")) {
//            bundle.put("lastTag", lastTag);
//        }
        bundle.put("action", "GET");
        sendRequest("/phoneclient/update_info", bundle, response);
//		if (batchRun) {
//			return request;
//		} else {
//			
        return null;
//		}
    }

    @Override
    public void postPhoto(byte[] imgData, int type, int htf, int from,
                          String statistic, String aid, String description, String placeData,
                          INetResponse response) {
        JsonObject bundle = obtainBaseRequestBundle(false);
        INetRequest request = obtainINetRequest("/share/photo");
//        bundle.put("data", buildUploadPhotoData(aid, type + "", htf + "", from + "", statistic, description, placeData, imgData));
        bundle.put("data", buildUploadPhotoData(imgData));
        bundle.put("session_key", LoginManager.getInstance().getSsssionKey());
        request.setData(bundle);
        request.setResponse(response);
        // 使用 3G 服务器的 secret_key
        request.setSecretKey(LoginManager.getInstance().getSecretKey());
        request.setType(INetRequest.TYPE_HTTP_POST_IMG);
        HttpProviderWrapper.getInstance().addRequest(request);
    }

    @Override
    public INetRequest getFriendRequests(int type, INetResponse response, boolean batch) {

        JsonObject bundle = this.obtainBaseRequestBundle(false);
        bundle.put("method", "news.getCount");
        bundle.put("v", "1.1");// 升级为V1.1主要是为了用子类型取代原来的类型
        bundle.put("format", "JSON"/* TODO */);
        bundle.put("type", type);
        bundle.put("update_timestamp", 1);// 更新push消息时间戳
        INetRequest request = null;
        if (batch) {
            request = this.obtainRequest();
            request.setData(bundle);
            request.setResponse(response);
            return request;
        }
        return request;
    }

    @Override
    public INetRequest getPermissions(String[] permissions, INetResponse response,
                                      boolean batch) {
        JsonObject bundle = obtainBaseRequestBundle(false);
        bundle.put("v", "1.0");
        if (permissions.length > 0) {
            bundle.put("permission_list", TextUtils.join(",", permissions));
        }
        bundle.put("action", "GET");
        bundle.put("sig", getSigForLogin(bundle));
//		bundle.put(INetRequest.gzip_key, INetRequest.gzip_value);
        sendRequest("/oauth/facebook/permissions", bundle, response);

        return null;
    }

    @Override
    public INetRequest uploadAccessToken(String third_party,
                                         String access_token, long expires, String user_id, String email,
                                         INetResponse response, boolean batch) {
        JsonObject bundle = obtainBaseRequestBundle(false);
        bundle.put("v", "1.0");
        bundle.put("access_token", access_token);
        bundle.put("expires", expires);
        bundle.put("sig", getSigForLogin(bundle));
//        bundle.put(INetRequest.gzip_key, INetRequest.gzip_value);

        sendRequest("/user/login/facebook", bundle, response);
        return null;
    }

    @Override
    public void getLoginInfo(INetResponse response) {
        JsonObject bundle = obtainBaseRequestBundle(false);
        bundle.put("v", "1.0");
        bundle.put("format", "JSON");
        bundle.put(INetRequest.gzip_key, INetRequest.gzip_value);

        sendRequest("/user/login", bundle, response);
    }

    /**
     * 获取历史聊天记录
     */
    @Override
    public void getHistoryMessages(boolean multi, long toChatUserId, INetResponse response) {
        // TODO Auto-generated method stub
        JsonObject bundle = obtainBaseRequestBundle(false);
        bundle.put("v", "1.0");
        bundle.put("from_id", toChatUserId);
        bundle.put("action", "GET");
        bundle.put("page_size", 300);
        if(multi) {
            bundle.put("multi", 1);
        }
        sendRequest("/talk/history_message", bundle, response);
    }

    @Override
    public void clearServerHistoryMessages(boolean multi, long toChatUserId, INetResponse response) {
        JsonObject bundle = obtainBaseRequestBundle(false);
        bundle.put("v", "1.0");
        bundle.put("from_id", toChatUserId);
        bundle.put("action", "DELETE");
        if(multi) {
            bundle.put("multi", 1);
        }
        sendRequest("/talk/history_message", bundle, response);
    }

    /**
     * 获取网络图片(基本是单个请求)
     */
    @Override
    public void getEmotionFromNet(String packageId, String emotionId,
                                  INetResponse response, boolean batch) {
        // TODO Auto-generated method stub
        JsonObject bundle = obtainBaseRequestBundle(false);
        bundle.put("v", "1.0");
        bundle.put("format", "JSON");
        //bundle.put(INetRequest.gzip_key, INetRequest.gzip_value);
        bundle.put("action", "GET");
        //sendRequest("",bundle,response,1);
        sendRequest("/emoticon/"+packageId+"/"+emotionId, bundle, response);
    }

    @Override
    public INetRequest getOauthPersonInfo(String access_token,
                                          INetResponse response, boolean batch) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public INetRequest thirdOauthLogout(INetResponse response, boolean batch) {
        // TODO Auto-generated method stub
        return null;
    }


    /**
     * 得到第三方账号的好友列表
     *
     * @param response
     * @param id                  用户私信ID
     * @param externalAccountType 第三方帐号类型
     * @param externalAccountId   第三方帐号ID
     * @param limit               每页最多纪录数
     * @param offset              偏移量
     * @return
     */
    @Override
    public INetRequest getThirdFriendsList(INetResponse response, String id, String externalAccountType, String externalAccountId, int limit, int offset) {
        JsonObject bundle = obtainBaseRequestBundle(false);
        bundle.put("v", "1.0");
        bundle.put("profile_type", 1023);
        bundle.put("action", "GET");
        sendRequest("/friends/" + externalAccountType, bundle, response);
        return null;
    }

    /**
     * 获取联系人列表详细信息
     */
    @Override
    public INetRequest getThirdFriendsInfo(INetResponse response, String siXinid,
                                           String externalAccountType, String thirdId, int limit,
                                           int offset) {
        JsonObject bundle = obtainBaseRequestBundle(false);
        bundle.put("v", "1.0");
        bundle.put("profile_type", 1023);
        bundle.put("action", "GET");
        if(TextUtils.isEmpty(siXinid)){ //用第三方ID
//        	SystemUtil.log_common("dfjkasfj===renrenid="+siXinid);
        	 sendRequest("/friends/" + externalAccountType + "/" + thirdId, bundle, response);
        }else{//用私信id
        	bundle.put("id_type", 1);
        	//SystemUtil.log_common("dfjkasfj===siixid="+siXinid);
        	sendRequest("/friends/" + externalAccountType + "/" + siXinid, bundle, response);
        }
       
        return null;
    }
    
    public INetRequest getSixinInfo(INetResponse response,String sinxId){
    	JsonObject bundle = obtainBaseRequestBundle(false);
        bundle.put("v", "1.0");
        bundle.put("profile_type", 1023);
        bundle.put("action", "GET");
       sendRequest("/contact/contactlist/" + sinxId, bundle, response);
       
        return null;
    }

    /**
     * 获取联系人列表接口
     */
    @Override
    public INetRequest getContactList(INetResponse response, int page,
                                      int page_size, boolean batchRun) {
        JsonObject bundle = obtainBaseRequestBundle(false);
        bundle.put("v", "1.0");
        bundle.put("limit", page_size); //每页最多纪录数
        bundle.put("offset", 0);// 偏移量
        bundle.put("profile_type", "1023");//想要得到的个人资料标识
//		bundle.put("head_url_type", 1);// 头像标识
        bundle.put("action", "GET");//值设置为GET
//		bundle.put(INetRequest.gzip_key, INetRequest.gzip_value);
        sendRequest("/contact/contactlist/contacts_talkgroups", bundle, response);
        return null;
    }
    
    /**
     * 获取全部联系人（包含群组）列表接口
     */
    @Override
    public INetRequest getContactListIncludeGroup(INetResponse response, int page,
                                      int page_size, boolean batchRun) {
        JsonObject bundle = obtainBaseRequestBundle(false);
        bundle.put("v", "1.0");
        bundle.put("limit", page_size); //每页最多纪录数
        bundle.put("offset", 0);// 偏移量
        bundle.put("profile_type", "1023");//想要得到的个人资料标识
//		bundle.put("head_url_type", 1);// 头像标识
        bundle.put("action", "GET");//值设置为GET
//		bundle.put(INetRequest.gzip_key, INetRequest.gzip_value);
        sendRequest("/contact/contactlist/contacts_talkgroups", bundle, response);
        return null;
    }


    @Override
    public void getVerifycode(String number, INetResponse response) {
        JsonObject bundle = obtainBaseRequestBundle(false);
        bundle.put("v", "1.0");
        bundle.put("user", number);
        bundle.put("action", "GET");
        bundle.put("sig", getSigForLogin(bundle));
//	        bundle.put(INetRequest.gzip_key, INetRequest.gzip_value);

        sendRequest("/user/register/captcha", bundle, response);
    }

    @Override
    public void submitWithVerifycode(String number, String verifycode,
                                     INetResponse response) {
        JsonObject bundle = obtainBaseRequestBundle(false);
        bundle.put("v", "1.0");
        bundle.put("user", number);
        bundle.put("captcha", verifycode);
        bundle.put("sig", getSigForLogin(bundle));
//	        bundle.put(INetRequest.gzip_key, INetRequest.gzip_value);

        sendRequest("/user/register/captcha", bundle, response);
    }

    @Override
    public void register(String number, String pwd, String captcha, int gender, String firstName, String lastName,
                         INetResponse response) {
        JsonObject bundle = obtainBaseRequestBundle(false);
        bundle.put("v", "1.0");
        bundle.put("user", number);
        bundle.put("password", pwd);
        bundle.put("captcha", captcha);
        bundle.put("gender", gender);  
        bundle.put("first_name", firstName);
        bundle.put("last_name", lastName);
        bundle.put("sig", getSigForLogin(bundle));
//	        bundle.put(INetRequest.gzip_key, INetRequest.gzip_value);
        sendRequest("/user/register", bundle, response);
    }
    
    @Override
    public void register(String number, String pwd, String captcha, int gender, String name,  
                         INetResponse response) {
        JsonObject bundle = obtainBaseRequestBundle(false);
        bundle.put("v", "1.0");
        bundle.put("user", number);
        bundle.put("password", pwd);
        bundle.put("captcha", captcha);
        bundle.put("gender", gender);  
        bundle.put("name", name); 
        bundle.put("sig", getSigForLogin(bundle));
//	        bundle.put(INetRequest.gzip_key, INetRequest.gzip_value);
        sendRequest("/user/register", bundle, response);
    }


    /**
     * 添加删除联系人
     *
     * @param response
     * @param contactUid
     * @param isADD      true为添加，false为删除
     * @return
     */
    @Override
    public INetRequest optContact(INetResponse response, String contactUid, boolean isADD) {
        JsonObject bundle = obtainBaseRequestBundle(false);
        bundle.put("v", "1.0");
        if (isADD) {
            bundle.put("contact_uid", contactUid);// 联系人id
            sendRequest("/contact/contactlist", bundle, response);
        } else {
            bundle.put("action", "DELETE");//设置删除action
            sendRequest("/contact/contactlist/" + contactUid, bundle, response);
        }
        return null;
    }
    
    public INetRequest addContact(INetResponse response, String contactUid,int verification_needed,String verification) {
        JsonObject bundle = obtainBaseRequestBundle(false);
        bundle.put("v", "1.0");
        bundle.put("contact_uid", contactUid);// 联系人id
        if(verification_needed == 1){
        	  bundle.put("verification_needed", verification_needed);
        	  bundle.put("verification", verification);
        }
        sendRequest("/contact/contactlist", bundle, response);
        return null;
    }
    
    
    public INetRequest delContact(INetResponse response, String contactUid) {
        JsonObject bundle = obtainBaseRequestBundle(false);
        bundle.put("v", "1.0");
        bundle.put("action", "DELETE");//设置删除action
        sendRequest("/contact/contactlist/" + contactUid, bundle, response);
        return null;
    }
    
    
    

    /**
     * 获取联系人数量
     *
     * @param response
     * @return
     */
    public INetRequest getContactNumber(INetResponse response) {
        JsonObject bundle = obtainBaseRequestBundle(false);
        bundle.put("v", "1.0");
        bundle.put("action", "GET");
        sendRequest("/contact/contactlist/number", bundle, response);
        return null;
    }

    /**
     * 添加删除黑名单
     *
     * @param response
     * @param contactUid
     * @param isADD      true为添加，false为删除
     * @return
     */
    public INetRequest optBlacklist(INetResponse response, String contactUid, boolean isADD) {
        JsonObject bundle = obtainBaseRequestBundle(false);
        bundle.put("v", "1.0");
        if (isADD) {
            bundle.put("contact_uid", contactUid);// 联系人id
            sendRequest("/contact/blacklist", bundle, response);
        } else {
            bundle.put("action", "DELETE");//设置删除action
            sendRequest("/contact/blacklist/" + contactUid, bundle, response);
        }
        return null;
    }

    /**
     * 获取黑名单列表
     *
     * @param response
     * @param limit
     * @param offset
     * @param batchRun
     * @return
     */
    public INetRequest getBlacklist(INetResponse response, int limit,
                                    int offset, boolean batchRun) {
        JsonObject bundle = obtainBaseRequestBundle(false);
        bundle.put("v", "1.0");
        bundle.put("limit", limit); //每页最多纪录数
        bundle.put("offset", offset);// 偏移量
        bundle.put("profile_type", 1023);//想要得到的个人资料标识
        bundle.put("action", "GET");//值设置为GET
        sendRequest("/contact/blacklist", bundle, response);
        return null;
    }

    @Override
    public INetRequest getBlacklist(INetResponse response) {
        JsonObject bundle = obtainBaseRequestBundle(false);
        bundle.put("v", "1.0");
        bundle.put("profile_type", 1023);//想要得到的个人资料标识
        bundle.put("action", "GET");//值设置为GET
        sendRequest("/contact/blacklist", bundle, response);
        return null;
    }

    /**
     * 获取黑名单数量
     *
     * @param response
     * @return
     */
    public INetRequest getBlacklistNumber(INetResponse response) {
        JsonObject bundle = obtainBaseRequestBundle(false);
        bundle.put("v", "1.0");
        bundle.put("action", "GET");
        sendRequest("/contact/blacklist/number", bundle, response);
        return null;
    }

    @Override
    public INetRequest refreshAccessToken(String session_key, String access_token, long expires, INetResponse response, boolean batch) {
        JsonObject bundle = obtainBaseRequestBundle(false);
        bundle.put("v", "1.0");
        bundle.put("session_key", session_key);
        bundle.put("action", "PUT");
        bundle.put("access_token", access_token);
        bundle.put("expires", expires);
        sendRequest("/oauth/facebook/access_token", bundle, response);
        return null;
    }

    // 插件系统
    @Override
    public void queryAllPluginInfos(INetResponse response, Integer offset, Integer limits) {
        JsonObject bundle = obtainBaseRequestBundle(false);
        bundle.put("action", "GET");
        if (null != offset) {
            bundle.put("offset", offset);
        }
        if (null != limits) {
            bundle.put("limits", limits);
        }
        sendRequest("/user/plugins", bundle, response);
    }

    @Override
    public void querySinglePluginInfo(INetResponse response, Integer plugin_id) {
    	JsonObject bundle = obtainBaseRequestBundle(false);
        bundle.put("action", "GET");
        if (null == plugin_id) {
			return;
		}
    	sendRequest("/user/plugins/" + plugin_id, bundle, response);
    }
    
    @Override
    public void installPlugin(INetResponse response, Integer plugin_id) {
        JsonObject bundle = obtainBaseRequestBundle(false);
//        bundle.put("action", "POST");
        if (null == plugin_id) {
			return;
		}
        bundle.put("plugin_id", plugin_id);
        sendRequest("/user/plugins", bundle, response);
    }
    
    @Override
    public void uninstallPlugin(INetResponse response, Integer plugin_id) {
        JsonObject bundle = obtainBaseRequestBundle(false);
        bundle.put("action", "DELETE");
        if (null == plugin_id) {
			return;
		}
        sendRequest("/user/plugins/" + plugin_id, bundle, response);
    }

    @Override
    public void settingPluginPush(INetResponse response, Integer plugin_id, boolean pushflag) {
        JsonObject bundle = obtainBaseRequestBundle(false);
        bundle.put("action", "PUT");
        if (null != plugin_id) {
            bundle.put("plugin_id", plugin_id);
        }
        bundle.put("push", pushflag ? 1 : 0);
        sendRequest("/user/plugins/setting", bundle, response);
    }

    @Override
    public void queryUserPlugins(INetResponse response, String user_id,
    		Integer offset, Integer limits, Integer type, Integer hasDetailOrNot) {
        JsonObject bundle = obtainBaseRequestBundle(false);
        bundle.put("action", "GET");
        if (null == user_id) {
			return;
		}
        if (null != offset) {
            bundle.put("offset", offset);
        }
        if (null != limits) {
            bundle.put("limits", limits);
        }
        if (null != type) {
            bundle.put("type", type);
        }
        if (null != hasDetailOrNot) {
            bundle.put("detail", hasDetailOrNot);
        }
        sendRequest("/user/" + user_id + "/plugins", bundle, response);
    }

    @Override
    public void getForgetPwdIdentifyCode(String number, INetResponse response) {
        JsonObject bundle = obtainBaseRequestBundle(false);
        bundle.put("action", "GET");
        bundle.put("user", number);
        bundle.put("sig", getSigForLogin(bundle));
        sendRequest("/user/password/captcha", bundle, response);
    }

    @Override
    public void identifyForgetPwdCode(String number, String verifycode,
                                      INetResponse response) {
        JsonObject bundle = obtainBaseRequestBundle(false);
        bundle.put("captcha", verifycode);
        bundle.put("user", number);
        bundle.put("sig", getSigForLogin(bundle));
        sendRequest("/user/password/captcha", bundle, response);
    }

    @Override
    public void modifyForgetPwd(String number, String newPwd, String captcha,
                                INetResponse response) {
        JsonObject bundle = obtainBaseRequestBundle(false);
        bundle.put("captcha", captcha);
        bundle.put("action", "PUT");
        bundle.put("user", number);
        bundle.put("password_new", newPwd);
        bundle.put("sig", getSigForLogin(bundle));
        sendRequest("/user/password/forget", bundle, response);
    }

    @Override
    public INetRequest getProfile(INetResponse response, String userId) {
        JsonObject bundle = obtainBaseRequestBundle(false);
        bundle.put("action", "GET");
		bundle.put("profile_type", 1023);
        sendRequest("/user/profile/" + userId, bundle, response);
        return null;
    }

    @Override
    public void bindThirdParty(String access_token, long expires,
                               String session_key, INetResponse response) {
        JsonObject bundle = obtainBaseRequestBundle(false);
        bundle.put("access_token", access_token);
        bundle.put("expires", expires);
        bundle.put("session_key", session_key);
        sendRequest("/user/binding/facebook", bundle, response);
    }

    @Override
    public void removeThireParty(String session_key, String third_party_type,
                                 String third_party_uid, INetResponse response) {
        JsonObject bundle = obtainBaseRequestBundle(false);
        bundle.put("action", "DELETE");
        bundle.put("session_key", session_key);
        sendRequest("/user/binding/" + third_party_type + "/" + third_party_uid, bundle, response);
    }

    @Override
        public INetRequest bindAccount(INetResponse response, String account, String captcha, String passwordToken) {
            JsonObject bundle = obtainBaseRequestBundle(false);
            bundle.put("account", account);
            bundle.put("captcha", captcha);
            bundle.put("password_token", passwordToken);
            sendRequest("/user/binding/account", bundle, response);
            return null;
        }

    @Override
    public INetRequest getPrivacy(INetResponse response) {
        JsonObject bundle = obtainBaseRequestBundle(false);
        bundle.put("action", "GET");
        sendRequest("/user/privacy", bundle, response);
        return null;
    }

    @Override
    public INetRequest putPrivacy(INetResponse response,
                                  boolean verification_required, boolean id_searchable,
                                  boolean name_searchable, boolean phone_searchable) {
        JsonObject bundle = obtainBaseRequestBundle(false);
        bundle.put("action", "PUT");
        bundle.put("verification_required", verification_required);
        bundle.put("id_searchable", id_searchable);
        bundle.put("name_searchable", name_searchable);
        bundle.put("phone_searchable", phone_searchable);
        sendRequest("/user/privacy", bundle, response);
        return null;
    }

    @Override
    public INetRequest modifyPassword(INetResponse response, String oldMd5,
                                      String newMd5, String sessionId) {
        JsonObject bundle = obtainBaseRequestBundle(false);
        bundle.put("action", "PUT");
        bundle.put("password_old", oldMd5);
        bundle.put("password_new", newMd5);
        bundle.put("session_id", sessionId);
        sendRequest("/user/password", bundle, response);
        return null;
    }

    @Override
    public void searchContacts(String text, INetResponse response) {
        // TODO Auto-generated method stub
        JsonObject bundle = obtainBaseRequestBundle(false);
        bundle.put("v", "1.0");
        bundle.put("q", text);
        bundle.put("type", "user");
        bundle.put("action", "GET");
        sendRequest("/search", bundle, response);
    }

    @Override
    public INetRequest getGroupContactInfo(INetResponse response,
                                           boolean batchRun) {
        // TODO Auto-generated method stub
        JsonObject bundle = obtainBaseRequestBundle(false);
        bundle.put("v", "1.0");
        bundle.put("action", "GET");
//	    bundle.put("page",1);
//	    bundle.put("page_size",100);
//	    bundle.put("head_url_switch", 2);
//	    bundle.put("member_detail", 1+2+4+8+16+64+128+256);

        sendRequest("/talk/group_list", bundle, response);
        return null;
    }

    @Override
    public void bindRenrenCount(String count, String psw,
                                INetResponse response) {
        JsonObject bundle = obtainBaseRequestBundle(false);
        bundle.put("renren_account", count);
        bundle.put("renren_password", psw);
        sendRequest("/user/binding/renren", bundle, response);
    }

    @Override
    public void unBindRenrenCount(String renrenUid,
                                  INetResponse response) {
        JsonObject bundle = obtainBaseRequestBundle(false);
        bundle.put("action", "DELETE");
        sendRequest("/user/binding/renren/" + renrenUid, bundle, response);
    }

    @Override
    public INetRequest logout(INetResponse response, String sessionId) {
        JsonObject bundle = obtainBaseRequestBundle(false);
        bundle.put("session_id", sessionId);
        sendRequest("/user/logout", bundle, response);
        return null;
    }

    @Override
    public INetRequest putProfile(INetResponse response, Map<String, String> map) {
        JsonObject bundle = obtainBaseRequestBundle(false);
        bundle.put("action", "PUT");
        for (String s : map.keySet()) {
            String value = map.get(s);
            if(s.equals("gender")){
               bundle.put(s, Integer.parseInt(value));
            }else{
               bundle.put(s, value);
            }
        }
        sendRequest("/user/profile", bundle, response);
        return null;
    }

    @Override
    public void searchColleges(String text, INetResponse response) {
        JsonObject bundle = obtainBaseRequestBundle(false);
        bundle.put("action", "GET");
        bundle.put("v", "1.0");
        bundle.put("q", text);
        bundle.put("type", "school");
        sendRequest("/search", bundle, response);
    }


    @Override
    public void uploadHeadPhoto(INetResponse response, int x, int y, int width, int height, byte[] data) {

        JsonObject bundle = obtainBaseRequestBundle(false);
        INetRequest request = obtainINetRequest("/file/head_photo");
        bundle.put("data", buildUploadHeadData(data, x, y ,width, height));
        request.setData(bundle);
        request.setResponse(response);
        // 使用 3G 服务器的 secret_key
        request.setSecretKey(LoginManager.getInstance().getSecretKey());
        request.setType(INetRequest.TYPE_HTTP_POST_IMG);
        HttpProviderWrapper.getInstance().addRequest(request);
    }

    @Override
    public void uploadPhoto(INetResponse response, byte[] data) {

        JsonObject bundle = obtainBaseRequestBundle(false);
        INetRequest request = obtainINetRequest("/file/photo");
        bundle.put("data", buildUploadPhotoData(data));
        request.setData(bundle);
        request.setResponse(response);
        // 使用 3G 服务器的 secret_key
        request.setSecretKey(LoginManager.getInstance().getSecretKey());
        request.setType(INetRequest.TYPE_HTTP_POST_IMG);
        HttpProviderWrapper.getInstance().addRequest(request);
    }

    @Override
    public void uploadFile(INetResponse response, byte[] data) {

        JsonObject bundle = obtainBaseRequestBundle(false);
        INetRequest request = obtainINetRequest("/file/common_file");
        bundle.put("action", "POST");
//        bundle.put("data", buildUploadHeadData(0, data));
        request.setData(bundle);
        request.setResponse(response);
        // 使用 3G 服务器的 secret_key
        request.setSecretKey(LoginManager.getInstance().getSecretKey());
        request.setType(INetRequest.TYPE_HTTP_POST_BIN_File);
        HttpProviderWrapper.getInstance().addRequest(request);
    }

    @Override
    public void uploadPhoto() {

    }

    @Override
    public void getAccountCaptcha(INetResponse response, String email) {
        JsonObject bundle = obtainBaseRequestBundle(false);
        bundle.put("action", "GET");
        bundle.put("account", email);
        sendRequest("/user/binding/account/captcha", bundle, response);
    }


    private JsonObject m_buildRequestBundle(boolean batchRun) {
        JsonObject bundle = obtainBaseRequestBundle(false);
        bundle.put("api_key", AbstractHttpMcsService.MCS_API_KEY);
        bundle.put("call_id", System.currentTimeMillis());
        bundle.put("secret_Key", LoginManager.getInstance().getSecretKey());
        if (!batchRun) {
            bundle.put("session_key", LoginManager.getInstance().getSsssionKey());
        }
        bundle.put("client_info", super.getClientInfo());
        /* ******************************************************** */
        if (!"".equals(getMISCInfo())) {
            bundle.put("misc", getMISCInfo());
        }
        if (AbstractRenrenApplication.IS_AUTO == 1) {
            bundle.put("is_auto", AbstractRenrenApplication.IS_AUTO);
        }
        /* ******************************************************** */
        return bundle;
    }

    //上传通讯录，通讯录找人
    @Override
    public void contactFriends(ContactsContractModel[] contacts,
                               INetResponse response) {
        JsonObject bundle = m_buildRequestBundle(false);
        String data = ContactsContractModel.generateContactsJsonString(
                contacts, LoginManager.getInstance().getSsssionKey());
        bundle.put("data", data);
        bundle.put("v", "1.0");
        bundle.put("format", "json");
        // INetRequest request =
        // m_buildRequest(test_server+"/contact/synchronize", bundle, response);
        // m_sendRequest(request);
        sendRequest("/contacts/sychnorize", bundle, response);
    }

	@Override
	public void postPNGPhoto(byte[] imgData, long toId, INetResponse response) {
		JsonObject bundle = obtainBaseRequestBundle(false);
        INetRequest request = obtainINetRequest("/file/photo");
        bundle.put("data", buildUploadPNGPhotoData(toId, imgData));
        request.setData(bundle);
        request.setResponse(response);
        // 使用 3G 服务器的 secret_key
        request.setSecretKey(LoginManager.getInstance().getSecretKey());
        request.setType(INetRequest.TYPE_HTTP_POST_IMG);
        HttpProviderWrapper.getInstance().addRequest(request);
	}

	@Override
	public void uploadLanguage(INetResponse response, String language) {
		JsonObject bundle = m_buildRequestBundle(false);
		bundle.put("action", "PUT");
		bundle.put("language", language);
		sendRequest("/client/language", bundle, response);
	}

	@Override
	public void getPasswordToken(INetResponse response, String passwordMd5) {
		 JsonObject bundle = m_buildRequestBundle(false);
		 bundle.put("action", "GET");
		 bundle.put("password", passwordMd5);
		 sendRequest("/user/password/token", bundle, response);
	}

    @Override
    public void shareLinkToFacebook(INetResponse response){

        JsonObject bundle = m_buildRequestBundle(false);
        sendRequest("/share/link/facebook", bundle, response);
    }

	@Override
	public void getUpdateInformation(INetResponse response, int up, int name, int from, int pubDate, String version, String language) {
		JsonObject bundle = obtainBaseRequestBundle(false);
		bundle.put("action", "GET");
        bundle.put("version", version);
        bundle.put("up", up);
        bundle.put("name", name);
        bundle.put("property", 5);
        bundle.put("subproperty", 0);
        bundle.put("channelId", from);
        bundle.put("ua", "");
        bundle.put("os", Build.VERSION.SDK + "_" + Build.VERSION.RELEASE);
        bundle.put("pubdate", pubDate);
        bundle.put("language", language);
        sendRequest("/phoneclient/update_info", bundle, response);
	}


}
