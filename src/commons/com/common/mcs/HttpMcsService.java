//package com.common.mcs;
//
//import android.os.Build;
//import android.telephony.TelephonyManager;
//import android.text.TextUtils;
//import com.common.app.AbstractRenrenApplication;
//import com.common.contactscontract.ContactsContractModel;
//import com.common.statistics.Htf;
//import com.core.json.JsonArray;
//import com.core.json.JsonObject;
//import com.core.util.CommonUtil;
//import com.core.util.SystemService;
//
//import java.util.Map;
//
///**
// * @author dingwei.chen
// * @MCS 网络业务
// * */
//final class HttpMcsService extends AbstractHttpMcsService {
//
//	private static HttpMcsService sInstance = new HttpMcsService();
//
//	private HttpMcsService() {
//	}
//
//	public static HttpMcsService getInstance() {
//		return sInstance;
//	}
//
//	@Override
//	public void login(String account, String passwordMd5, int captcha_needed, String captcha, long session,
//			INetResponse response) {
//		TelephonyManager tm = SystemService.sTelephonyManager;
//		JsonObject bundle = new JsonObject();
//		// 使用 3G 服务器的 api_key
//		bundle.put("api_key", AbstractHttpMcsService.MCS_API_KEY);
//		bundle.put("call_id", System.currentTimeMillis());
//		bundle.put("client_info", getClientInfo());
//		bundle.put("v", "1.0");
//		bundle.put("format", "JSON");
//		bundle.put("user", account);
//		bundle.put("password", passwordMd5);
//		bundle.put("uniq_id", tm.getDeviceId());
//		bundle.remove("session_key");
//		bundle.put(INetRequest.gzip_key, INetRequest.gzip_value);
//		bundle.put("sig", getSigForLogin(bundle));
//		INetRequest request = obtainINetRequest("/client/login");
//		request.setData(bundle);
//		request.setResponse(response);
//		request.setSecretKey(AbstractHttpMcsService.LOCAL_SECRET_KEY);
//		request.setCurrentSession(session);
//		HttpProviderWrapper.getInstance().addRequest(request);
//	}
//
//	@Override
//	public void getFeedList(int page, int page_size, String types,
//			INetResponse response) {
//		JsonObject bundle = obtainBaseRequestBundle(false);
//		bundle.put("v", "1.0").put("call_id", System.currentTimeMillis()).put("action", "GET").put("page", page).put("page_size", page_size)
//				.put("focus", 1).put("type", "502,701,709");
////				.put(INetRequest.gzip_key, INetRequest.gzip_value);
//		this.sendRequest("/feed/feeds", bundle, response);
//		CommonUtil.log("cdw", "getFeedList");
//	}
//
//	@Override
//	public void getFeedList(long[] id, INetResponse response) {
//		String temp = "" + id[0];
//		for (int num = 1; num < id.length; num++) {
//			temp += "," + id[num];
//		}
//		JsonObject bundle = obtainBaseRequestBundle(false);
//		bundle.put("v", "1.0").put("fids", temp).put("call_id", System.currentTimeMillis()).put("action", "GET")
//				.put(INetRequest.gzip_key, INetRequest.gzip_value);
//		sendRequest("/feed/getByIds", bundle, response);
//	}
//
//	@Override
//	public void getImage(String url, INetResponse response) {
//		INetRequest request = this.obtainINetRequest(url,
//				INetRequest.TYPE_HTTP_GET_IMG, response);
//		request.setPriority(INetRequest.PRIORITY_LOW_PRIORITY);
//		request.setSecretKey(sRuntimeSecretKey);
//		HttpProviderWrapper.getInstance().addRequest(request);
//	}
//
//	@Override
//	public void getVoice(String url, INetResponse response) {
//		INetRequest request = obtainINetRequest(url,
//				INetRequest.TYPE_HTTP_GET_VOICE, response);
//		request.setSecretKey(sRuntimeSecretKey);
//		HttpProviderWrapper.getInstance().addRequest(request);
//	}
//
//	@Override
//	public INetRequest getFriends(int page, int page_size,boolean batchRun,INetResponse response) {
//		final JsonObject bundle = obtainBaseRequestBundle(false);
//		if(batchRun){
//			bundle.put("method", "friends.getOnlineFriends");	
//		}
//		bundle.put("v", "1.0");
//		bundle.put("hasGender", 1);// 包括性别
//		bundle.put("hasBirthday", 1);// 包括生日
//		if((page_size!=-1)||(page==-1)){
//			bundle.put("page", page);
//			bundle.put("page_size", page_size);
//		}
//		bundle.put("hasLargeHeadUrl", 1);// 包括大头像
//		bundle.put("hasMainHeadUrl", 1);// 包括大头像
//		bundle.put("useShortUrl", 1);// 使用短连接
//		bundle.put("hasFocused", 1);// 请求特别关注
//		INetRequest request = obtainINetRequest("");
//		if (!batchRun) {
//			bundle.put(INetRequest.gzip_key, INetRequest.gzip_value);
//			this.sendRequest("/friends/getOnlineFriends", bundle, response);
//		}else{
//			request = obtainINetRequest("");
//			request.setData(bundle);
//			request.setResponse(response);
//		}
//		return request;
//	}
//
//	@Override
//	public INetRequest getGroups(boolean batchRun, INetResponse response) {
//		INetRequest request = null;
//		JsonObject bundle = obtainBaseRequestBundle(batchRun);
//		if(batchRun){
//			bundle.put("method", "talk.getGroupList");	
//		}
//		return request;
//	}
//
//	@Override
//	public void getFriendsRequests(int page, int page_size, int exclude_list,
//			int del_news, int htf, INetResponse response) {
//		JsonObject bundle = obtainBaseRequestBundle(false);
//		if((page!=-1)&&(page_size!=-1)){
//			bundle.put("page", page);
//			bundle.put("page_size", page_size);
//		}
//		bundle.put("del_news", del_news);
//		bundle.put("htf", htf);//统计需求
//		bundle.put("exclude_list", exclude_list);
//		bundle.put(INetRequest.gzip_key, INetRequest.gzip_value);
//		sendRequest("/friends/getRequests", bundle, response);
//	}
//
//	@Override
//	public void postPhoto(byte[] imgData, long toId, INetResponse response) {
//		JsonObject bundle = new JsonObject();
//		INetRequest request = obtainINetRequest("");
//		bundle.put("data", buildUploadPhotoData(toId, imgData));
//		request.setData(bundle);
//		request.setResponse(response);
//		// 使用 3G 服务器的 secret_key
//		request.setSecretKey(sRuntimeSecretKey);
//		request.setType(INetRequest.TYPE_HTTP_POST_IMG);
//		HttpProviderWrapper.getInstance().addRequest(request);
//	}
//
//	@Override
//	public void postVoice(long toId, String vid, int seqid, String mode, int playTime, byte[] voiceData, INetResponse response) {
//		JsonObject bundle = new JsonObject();
//		bundle.put("data", buildUploadAudioData(toId, vid, seqid, mode, playTime, voiceData));
//		INetRequest request = obtainRequest();
//		request.setData(bundle);
//		request.setResponse(response);
//		// 使用 3G 服务器的 secret_key
//		request.setSecretKey(sRuntimeSecretKey);
//		request.setType(INetRequest.TYPE_HTTP_POST_BIN_File);
//		HttpProviderWrapper.getInstance().addRequest(request);
//	}
//	
//	@Override
//	public void backgroundRunStatistics(INetResponse response) {
//		JsonObject bundle = obtainBaseRequestBundle(false);
//		bundle.put("v", "1.0");
//		bundle.put("format", "json");
//		bundle.put("client_info", getClientInfo());
//		sendRequest("/client/maintain", bundle, response);
//	}
//
//	@Override
//	public void uploadStatistics(String localStatistics, INetResponse response) {
//		JsonObject bundle = obtainBaseRequestBundle(false);
//
//		bundle.put("v", "1.0");
//		bundle.put("format", "json");
//		bundle.put("data", localStatistics);
//		bundle.put("fromId", String.valueOf(AbstractRenrenApplication.FROM));
//		bundle.put("terminal", "android");
//		bundle.put("version", AbstractRenrenApplication.VERSION_NAME);
//		
//		sendRequest("/phoneclient/actionLog", bundle, response);
//	}
//
//	@Override
//	public INetRequest activeClient(String imei, int type,
//			INetResponse response, boolean batchRun) {
//		JsonObject bundle = obtainBaseRequestBundle(batchRun);
////		bundle.put("method", "phoneclient.activeClient");
//		bundle.put("v", "1.0");
//		bundle.put("format", "json");
//		bundle.put("uniq_id", imei);
//		JsonArray dataArray = new JsonArray();
//		JsonObject dataJson = new JsonObject();
//		dataArray.add(dataJson);
//		dataJson.put("type", type);
//		dataJson.put("num", 1);
//		bundle.put("data", dataArray.toJsonString());
//		bundle.remove("session_key");
//		
//		INetRequest request = obtainINetRequest("");
//		request.setSecretKey(sRuntimeSecretKey);
//		if (batchRun) {
//			request.setData(bundle);
//			request.setResponse(response);
//			return request;
//		} else {
//			sendRequest("/phoneclient/activeClient", bundle, response);
//			return null;
//		}
//	}
//
//	@Override
//	public void matchContacts(boolean isSkip, ContactsContractModel[] contacts, INetResponse response) {
//		JsonObject bundle = obtainBaseRequestBundle(false);
//		
//		String data = null;
//		if(!isSkip)
//			data = ContactsContractModel.generateContactsJsonString(contacts, sRuntimeSessionKey);
//		bundle.put("data", data);
//		bundle.put("v", "1.0");
//		bundle.put("format", "json");
//		bundle.put("friend_type_switch", "1");
//		
//		sendRequest("/contact/getFriends", bundle, response);
//	}
//
//	@Override
//	public INetRequest m_friendsRequest(long uid, String content,
//			INetResponse response, boolean batchRun) {
//		JsonObject bundle = obtainBaseRequestBundle(batchRun);
//
////		bundle.put("method", "friends.request");
//		bundle.put("uid", uid);
//		if (content != null) {
//			bundle.put("content", content);
//		}
//		bundle.put("v", "1.0");
//		bundle.put("format", "json");
//		// if (!batchRun) {
//		// bundle.put(INetRequest.gzip_key, INetRequest.gzip_value);
//		// }
//		INetRequest request = obtainINetRequest("");
//		request.setSecretKey(sRuntimeSecretKey);
//		if (batchRun) {
//			request.setData(bundle);
//			request.setResponse(response);
//			return request;
//		} else {
//			sendRequest("/friends/request", bundle, response);
//			return null;
//		}
//	}
//	
//	@Override
//	public INetRequest m_friendsRequest(long uid, String content,
//			INetResponse response, boolean batchRun, String HTF) {
//		JsonObject bundle = obtainBaseRequestBundle(batchRun);
//		
//		
//		bundle.put("misc", getMISCInfo(HTF, null));
//		
//
////		bundle.put("method", "friends.request");
//		bundle.put("uid", uid);
//		if (content != null) {
//			bundle.put("content", content);
//		}
//		bundle.put("v", "1.0");
//		bundle.put("format", "json");
//		// if (!batchRun) {
//		// bundle.put(INetRequest.gzip_key, INetRequest.gzip_value);
//		// }
//		INetRequest request = obtainINetRequest("");
//		request.setSecretKey(sRuntimeSecretKey);
//		if (batchRun) {
//			request.setData(bundle);
//			request.setResponse(response);
//			return request;
//		} else {
//			sendRequest("/friends/request", bundle, response);
//			return null;
//		}
//	}
//
//	@Override
//	public INetRequest getFriendsRequest(INetResponse response, int page,
//			int page_size, int exclude_list, int del_news, boolean batchRun) {
//		JsonObject bundle = obtainBaseRequestBundle(false);
//		bundle.put("method", "friends.getRequests");
//		bundle.put("v", "1.0");
//		bundle.put("page", page);
//		bundle.put("page_size", page_size);
//		bundle.put("del_news", del_news);
//		bundle.put("htf", Htf.REQUEST_BUTTON);
//		bundle.put("exclude_list", exclude_list);
//		if (!batchRun) {
//			bundle.put(INetRequest.gzip_key, INetRequest.gzip_value);
//		}
//		
//		INetRequest request = obtainINetRequest("");
//		request.setSecretKey(sRuntimeSecretKey);
//		if (batchRun) {
//			request.setData(bundle);
//			request.setResponse(response);
//			return request;
//		} else {
//			sendRequest("", bundle, response);
//			return null;
//		}
//	}
//
//	@Override
//	public INetRequest acceptFriendRequest(long uid, INetResponse response,
//			boolean batchRun) {
//		JsonObject bundle = obtainBaseRequestBundle(false);
////		bundle.put("method", "friends.accept");
//		bundle.put("v", "1.0");
//		bundle.put("user_id", uid);
//		bundle.put("htf", Htf.REQUEST_RECIVIED);
//		
//		INetRequest request = obtainINetRequest("");
//		request.setSecretKey(sRuntimeSecretKey);
//		if (batchRun) {
//			request.setData(bundle);
//			request.setResponse(response);
//			return request;
//		} else {
//			sendRequest("/friends/accept", bundle, response);
//			return null;
//		}
//	}
//
//	@Override
//	public INetRequest denyFriendRequest(long uid, INetResponse response,
//			boolean batchRun) {
//		JsonObject bundle = obtainBaseRequestBundle(false);
////		bundle.put("method", "friends.deny");
//		bundle.put("v", "1.0");
//		bundle.put("user_id", uid);
//		
//		INetRequest request = obtainINetRequest("");
//		request.setSecretKey(sRuntimeSecretKey);
//		if (batchRun) {
//			request.setData(bundle);
//			request.setResponse(response);
//			return request;
//		} else {
//			sendRequest("/friends/deny", bundle, response);
//			return null;
//		}
//	}
//
//	@Override
//	public INetRequest supplyUserInfo(INetResponse response, String userName,
//			String userGender, int year, int month, int day, int stage,
//			boolean batchRun) {
//		JsonObject bundle = obtainBaseRequestBundle(false);
////		bundle.put("method", "user.fillInfo");
//		bundle.put("v", "1.0");
//		bundle.put("name", userName);
//		bundle.put("gender", userGender);
//		bundle.put("year", year);
//		bundle.put("month", month);
//		bundle.put("day", day);
//		bundle.put("stage", stage);
//		
//		INetRequest request = obtainINetRequest("");
//		request.setSecretKey(sRuntimeSecretKey);
//		if (batchRun) {
//			request.setData(bundle);
//			request.setResponse(response);
//			return request;
//		} else {
//			sendRequest("/user/fillInfo", bundle, response);
//			return null;
//		}
//	}
//
//	@Override
//	public void getFocusFriends(INetResponse response) {
//		JsonObject bundle = obtainBaseRequestBundle(false);
//		bundle.put("v", "1.0");
//		bundle.put("format", "json");
//		sendRequest("/friends/getFocusFriends", bundle, response);
//	}
//
//	@Override
//	public void isFansOfPage(long userId, long pageId, INetResponse response) {
//		JsonObject bundle = obtainBaseRequestBundle(false);
//
//		bundle.put("v", "1.0");
//		bundle.put("format", "json");
//		bundle.put("user_id", userId);
//		bundle.put("page_id", pageId);
//		
//		sendRequest("/page/isFans", bundle, response);
//	}
//
//	@Override
//	public void becomeFansOfPage(int pageId, INetResponse response) {
//		JsonObject bundle = obtainBaseRequestBundle(false);
//
//		bundle.put("v", "1.0");
//		bundle.put("format", "json");
//		bundle.put("page_id", pageId);
//		
//		sendRequest("/page/becomeFan", bundle, response);
//	}
//
//	@Override
//	public INetRequest m_statusSet(INetResponse response, boolean batchRun) {
//		JsonObject bundle = obtainBaseRequestBundle(batchRun);
////		bundle.put("method", "share.publishLink");
//		bundle.put("desc", "");
//		bundle.put("thumb_url", "http://fmn.rrimg.com/fmn060/20120628/1950/original_6wNB_2a6600001075118e.jpg");
////		bundle.put("url", "http://3g.renren.com/ep.do?c=9504901");
//		bundle.put("url", "http://mobile.renren.com/home?psf=42053");
//		bundle.put("title", "新版私信可以群聊了！和好友们畅聊一“夏”吧！立即下载！");
//		bundle.put("comment", "");
//		bundle.put("from", AbstractRenrenApplication.FROM);
//		bundle.put("v", "1.0");
//		bundle.put("format", "json");
//		bundle.put("htf", Htf.RAPID_PUB_ACTIVITY);
//
//		INetRequest request = obtainINetRequest("");
//		request.setSecretKey(sRuntimeSecretKey);
//		if (batchRun) {
//			request.setData(bundle);
//			request.setResponse(response);
//			return request;
//		} else {
//			sendRequest("/share/publishLink", bundle, response);
//			return null;
//		}
//	}
//
//	
//	
//	
//
//
//	@Override
//	public void postLog(String stackInfo, String exMessage,
//			INetResponse response) {
//		JsonObject bundle = obtainBaseRequestBundle(false);
//		bundle.put("v", "1.0");
//		bundle.put("format", "json");
//		bundle.put("event_type", "10000");
//		bundle.put("exception_type",exMessage);
//		bundle.put("stack_info",stackInfo);
//		bundle.put("client_info", getClientInfo());
//		this.sendRequest("/phoneclient/eventLog", bundle, response);
//	}
//
//	@Override
//	public void postFocusFriend(boolean isFoucs, long userId,
//			INetResponse response) {
//		JsonObject bundle = this.obtainBaseRequestBundle(false);
//		bundle.put("uid", userId);
//		if(isFoucs){
//			this.sendRequest("/friends/addFocusFriend", bundle, response);
//		}else{
//			this.sendRequest("/friends/delFocusFriend", bundle, response);
//		}
//		
//	}
//
//	@Override
//	public void getFriendsOnlineStatus(INetResponse response) {
//		JsonObject json = this.obtainBaseRequestBundle(false);
//		this.sendRequest("/friends/getOnlineFriendsStatus", json, response);
//	}
//
//	@Override
//	public INetRequest getFriendInfo(int type, long uid, boolean batchRun,
//			INetResponse response) {
//		JsonObject bundle = this.obtainBaseRequestBundle(batchRun);
//		bundle.put("v", "1.0");
//		bundle.put("uid", uid);
//		bundle.put("type", type);
//		if (!batchRun) {
//			bundle.put(INetRequest.gzip_key, INetRequest.gzip_value);
//		}
//		if (batchRun) {
//			INetRequest request = this.obtainINetRequest("/profile/getInfo");
//			request.setData(bundle);
//			return request;
//		} else {
//			this.sendRequest("/profile/getInfo", bundle, response);
//			return null;
//		}
//	}
//
//	@Override
//	public void getFriendOnlineStatus(long userId, INetResponse response) {
//		JsonObject bundle = this.obtainBaseRequestBundle(false);
//		bundle.put("id_list", String.valueOf(userId));
//		this.sendRequest("/friends/isOnline", bundle, response);
//	}
//
//    @Override
//    public void addFocusFriend(INetResponse response, long uid) {
//        JsonObject bundle = obtainBaseRequestBundle(false);
//        bundle.put("v", "1.0");
//        bundle.put("focus_uid", uid);
//        sendRequest("/friends/focus_friends/renren", bundle, response);
//    }
//
//    @Override
//    public void delFocusFriend(INetResponse response, long uid) {
//        JsonObject bundle = obtainBaseRequestBundle(false);
//        bundle.put("v", "1.0");
//        bundle.put("focus_uid", uid);
//        bundle.put("action", "DELETE");
//        sendRequest("/friends/focus_friends/renren", bundle, response);
//    }
//
////	@Override
////	public INetRequest getOnlineFriendList(INetResponse response, int page,
////			int page_size, boolean batchRun) {
////		JsonObject bundle = obtainBaseRequestBundle(false);
////		if(batchRun){
////			bundle.put("method", "friends.getOnlineFriends");	
////		}
////		bundle.put("v", "1.0");
////		bundle.put("page", page);
////		bundle.put("hasGender", 1);// 包括性别
////		bundle.put("hasBirthday", 1);// 包括生日
////		bundle.put("page_size", page_size);
////		bundle.put("hasLargeHeadUrl", 1);// 包括大头像
////		bundle.put("hasMainHeadUrl", 1);// 包括大头像
////		bundle.put("useShortUrl", 1);// 使用短连接
////		bundle.put("hasFocused", 1);// 请求特别关注
////		
////		INetRequest request = obtainINetRequest("");
////		request.setSecretKey(sRuntimeSecretKey);
////		if (batchRun) {
////			request.setData(bundle);
////			request.setResponse(response);
////			return request;
////		} else {
////			bundle.put(INetRequest.gzip_key, INetRequest.gzip_value);
////			sendRequest("/friends/getOnlineFriends", bundle, response);
////			return null;
////		}
////	}
//
//	@Override
//	public INetRequest getGroupContact(INetResponse response, boolean batchRun) {
//		JsonObject bundle = obtainBaseRequestBundle(false);
//		if(batchRun){
//			bundle.put("method", "talk.getGroupList");	
//		}
//		bundle.put("v", "1.0");
//		bundle.put("sig","");
//		bundle.put("v ", "1.0");
//	    bundle.put("page",1);
//	    bundle.put("page_size",100);
//	    bundle.put("head_url_switch", 2);
//	    bundle.put("member_detail", 1+2+4+8+16+64+128+256);
//	    
//		INetRequest request = obtainINetRequest("");
//		request.setSecretKey(sRuntimeSecretKey);
//		if (batchRun) {
//			request.setData(bundle);
//			request.setResponse(response);
//			return request;
//		} else {
//			bundle.put(INetRequest.gzip_key, INetRequest.gzip_value);
//			sendRequest("/talk/getGroupList", bundle, response);
//			return null;
//		}
//	}
//
//	@Override
//	public INetRequest getOnlineFriendListSimple(INetResponse response) {
//		JsonObject bundle = obtainBaseRequestBundle(false);
//		bundle.put("v", "1.0");
////		bundle.put(INetRequest.gzip_key, INetRequest.gzip_value);
//		sendRequest("/friends/getOnlineFriendsStatus", bundle, response);
//		return null;
//	}
//
//	@Override
//	public INetRequest getFriendInfo(int type, Long uid, INetResponse response,
//			boolean batchRun) {
//		JsonObject bundle = obtainBaseRequestBundle(false);
////		bundle.put("method", "profile.getInfo");
//		bundle.put("v", "1.0");
//		bundle.put("uid", uid);
//		bundle.put("type", type);
//		if (!batchRun) {
//			bundle.put(INetRequest.gzip_key, INetRequest.gzip_value);
//		}
//		
//		INetRequest request = obtainINetRequest("");
//		request.setSecretKey(sRuntimeSecretKey);
//		if (batchRun) {
//			request.setData(bundle);
//			request.setResponse(response);
//			return request;
//		} else {
//			sendRequest("/profile/getInfo", bundle, response);
//			return null;
//		}
//	}
//
//	@Override
//	public INetRequest getOnlineStatusByUserid(INetResponse response, long id) {
//		JsonObject bundle = obtainBaseRequestBundle(false);
////		bundle.put("method", "friends.getOnlineFriends");
//		bundle.put("v", "1.0");
//		bundle.put("id_list", String.valueOf(id));
//		sendRequest("/friends/isOnline", bundle, response);
//		return null;
//	}
//
//	@Override
//	public INetRequest getFeedByIds(INetResponse response, long[] id) {
//		JsonObject bundle = obtainBaseRequestBundle(false);
////		bundle.put("method", "friends.getOnlineFriends");
//        bundle.put("v", "1.0");
//        bundle.put("call_id", System.currentTimeMillis());
//        bundle.put("action", "GET");
//        String temp = "" + id[0];
//        for (int num = 1; num < id.length; num++) {
//            temp += "," + id[num];
//        }
//        bundle.put("fids", temp);
//        sendRequest("/feed/feeds", bundle, response);
//		return null;
//	}
//
//	@Override
//	public INetRequest getFocusFriendsNUM(INetResponse response) {
//		JsonObject bundle = obtainBaseRequestBundle(false);
//        bundle.put("action", "GET");
//        sendRequest("/friends/focus_friends/renren", bundle, response);
//		return null;
//	}
//
//	@Override
//	public INetRequest getUpdateInfo(int type, INetResponse response,
//			String lastTag, boolean batchRun) {
//		JsonObject bundle = obtainBaseRequestBundle(false);
////		bundle.put("method", "phoneclient.getUpdateInfo");
//		bundle.put("v", "1.0");
//		bundle.put("version", AbstractRenrenApplication.VERSION_NAME);
//		bundle.put("up", type);
//		bundle.put("name", 2);// 2代表人人chat
//		bundle.put("property", 5);
//		bundle.put("subproperty", 0);
//		bundle.put("channelId", AbstractRenrenApplication.FROM);
//		bundle.put("ua", "");
//		bundle.put("os", Build.VERSION.SDK + "_" + Build.VERSION.RELEASE);
//		bundle.put("pubdate", AbstractRenrenApplication.PUBLIC_DATE);
//		if (lastTag != null && !lastTag.equals("")) {
//			bundle.put("lastTag", lastTag);
//		}
//		sendRequest("/phoneclient/getUpdateInfo", bundle, response);
////		if (batchRun) {
////			return request;
////		} else {
////			
//			return null;
////		}
//	}
//
//	@Override
//	public void postPhoto(byte[] imgData, int type, int htf, int from,
//			String statistic, String aid, String description, String placeData,
//			INetResponse response) {
//		JsonObject bundle = new JsonObject();
//		INetRequest request = obtainINetRequest("");
//		bundle.put("data", buildUploadPhotoData(aid, type+"", htf+"", from+"", statistic, description, placeData, imgData));
//		request.setData(bundle);
//		request.setResponse(response);
//		// 使用 3G 服务器的 secret_key
//		request.setSecretKey(sRuntimeSecretKey);
//		request.setType(INetRequest.TYPE_HTTP_POST_IMG);
//		HttpProviderWrapper.getInstance().addRequest(request);
//	}
//
//	@Override
//	public INetRequest getFriendRequests(int type, INetResponse response,boolean batch) {
//		
//		JsonObject bundle = this.obtainBaseRequestBundle(false);
//		bundle.put("method", "news.getCount");
//		bundle.put("v", "1.1");// 升级为V1.1主要是为了用子类型取代原来的类型
//		bundle.put("format", "JSON"/* TODO */);
//		bundle.put("type", type);
//		bundle.put("update_timestamp", 1);// 更新push消息时间戳
//		INetRequest request = null;
//		if(batch){
//			request = this.obtainRequest();
//			request.setData(bundle);
//			request.setResponse(response);
//			return request;
//		}
//		return request;
//	}
//
//	@Override
//	public INetRequest getPermissions(String[] permissions, INetResponse response,
//			boolean batch) {
//		JsonObject bundle = obtainBaseRequestBundle(false);
//		bundle.put("v", "1.0");
//		if (permissions.length > 0) {
//	        bundle.put("permission_list", TextUtils.join(",", permissions));
//	    }
//        bundle.put("third_party_type", "FACEBOOK");
////		bundle.put(INetRequest.gzip_key, INetRequest.gzip_value);
//		sendRequest("/oauth/permissions/get", bundle, response);
//		
//		return null;
//	}
//
//	@Override
//	public INetRequest uploadAccessToken(String third_party,
//			String access_token, long expires, String user_id, String email,
//			INetResponse response, boolean batch) {
//        JsonObject bundle = obtainBaseRequestBundle(false);
//        bundle.put("v", "1.0");
//        bundle.put("access_token", "BAABrw4sCDz0BAGfEJZCj8X7bVuoaOKywmhZBFVTmRwJtpM4ytIe3SeG0eaUx4JQolFDB9fs51R8x65GQvXVwqZCEbiJJZAJNPF3SVdhBZAdexmlQZCFCeqYgW4GNRUfe7PmzOUNPdo8AZDZD");
//        bundle.put("thrid_party_type", "FACEBOOK");
//        bundle.put("expires", "1349680311363");
////        bundle.put(INetRequest.gzip_key, INetRequest.gzip_value);
//
//        sendRequest("/user/login/third_party", bundle, response);
//		return null;
//	}
//
//	@Override
//	public void getLoginInfo(INetResponse response) {
//		JsonObject bundle = obtainBaseRequestBundle(false);
//		bundle.put("v", "1.0");
//		bundle.put("format", "JSON");
//		bundle.put(INetRequest.gzip_key, INetRequest.gzip_value);
//		
//		sendRequest("/user/login", bundle, response);
//	}
//
//	/**
//	 * 获取网络图片(基本是单个请求)
//	 */
//	@Override
//	public void getEmotionFromNet(String packageId, String emotionId,
//			INetResponse response,boolean batch) {
//		// TODO Auto-generated method stub
//		JsonObject bundle = obtainBaseRequestBundle(false);
//        bundle.put("v", "1.0");
//        bundle.put("format", "JSON");
//        //bundle.put(INetRequest.gzip_key, INetRequest.gzip_value);
//        bundle.put("action", "GET");
//        //sendRequest("",bundle,response,1);
//        sendRequest("/emoticon/"+packageId+"/"+emotionId, bundle, response);
//	}
//
//	@Override
//	public INetRequest getOauthPersonInfo(String access_token,
//			INetResponse response, boolean batch) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public INetRequest thirdOauthLogout(INetResponse response, boolean batch) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//
//	@Override
//	public INetRequest getContactList(INetResponse response, int page,
//			int page_size, boolean batchRun) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//	
//	@Override
//	public INetRequest getContactListIncludeGroup(INetResponse response, int page,
//			int page_size, boolean batchRun) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public INetRequest getThirdFriendsList(INetResponse response, String id,
//			String externalAccountType, String externalAccountId, int limit,
//			int offset) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public INetRequest optContact(INetResponse response, String contactUid,
//			boolean isADD) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public INetRequest getContactNumber(INetResponse response) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public INetRequest optBlacklist(INetResponse response, String contactUid,
//			boolean isADD) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public INetRequest getBlacklist(INetResponse response, int limit,
//			int offset, boolean batchRun) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public INetRequest getBlacklistNumber(INetResponse response) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public void getVerifycode(String number, INetResponse response) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void submitWithVerifycode(String number, String verifycode,
//			INetResponse response) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void register(String number, String pwd, String captcha, int gender, String firstName, String lastName,
//			INetResponse response) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public INetRequest refreshAccessToken(String session_key,
//			String access_token, long expires, INetResponse response,
//			boolean batch) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public void getCaptcha(String user, String action, INetResponse response) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void checkCaptcha(String user, String captcha, INetResponse response) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void loginSiXin(String account, String passwordMd5, long session,
//			INetResponse response) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void loginRenRen(String account, String passwordMd5, long session,
//			INetResponse response) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	/**
//	 * 获取历史聊天记录
//	 * */
//	@Override
//	public void getHistoryMessages(boolean multi, long toChatUserId, INetResponse response) {
//		// TODO Auto-generated method stub
//		JsonObject bundle = obtainBaseRequestBundle(false);
//		bundle.put("v", "1.0");
//		bundle.put("from_id", toChatUserId);
//		bundle.put("action", "GET");
//        bundle.put("page_size", 300);
//        if(multi) {
//            bundle.put("multi", 1);
//        }
//		sendRequest("/talk/history_message", bundle, response);
//	}
//
//    @Override
//    public void clearServerHistoryMessages(boolean multi, long toChatUserId, INetResponse response) {
//        JsonObject bundle = obtainBaseRequestBundle(false);
//        bundle.put("v", "1.0");
//        bundle.put("from_id", toChatUserId);
//        bundle.put("action", "DELETE");
//        if(multi) {
//            bundle.put("multi", 1);
//        }
//        sendRequest("/talk/history_message", bundle, response);
//    }
//
//    @Override
//	public void getForgetPwdIdentifyCode(String number, INetResponse response) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void identifyForgetPwdCode(String number, String verifycode,
//			INetResponse response) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void modifyForgetPwd(String number, String newPwd, String captcha,
//			INetResponse response) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public INetRequest getProfile(INetResponse response, String userId) {
//		JsonObject bundle = obtainBaseRequestBundle(false);
//		bundle.put("action", "GET");
////		bundle.put("profile_type", profile_type);
//		sendRequest("/user/profile/"+ userId,bundle, response);
//		return null;
//	}
//
//	@Override
//	public void bindThirdParty(String access_token, long expires,
//			String session_key, INetResponse response) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void removeThireParty(String session_key, String third_party_type,
//			String third_party_uid, INetResponse response) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public INetRequest getThirdFriendsInfo(INetResponse response, String id,
//			String externalAccountType, String externalAccountId, int limit,
//			int offset) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public INetRequest bindAccount(INetResponse response, String account,String captcha, String passwordToken) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public INetRequest getPrivacy(INetResponse response) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//	
//	@Override
//	public void queryAllPluginInfos(INetResponse response, Integer offset,
//			Integer limits) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void querySinglePluginInfo(INetResponse response, Integer plugin_id) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void installPlugin(INetResponse response, Integer plugin_id) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void uninstallPlugin(INetResponse response, Integer plugin_id) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void settingPluginPush(INetResponse response, Integer plugin_id,
//			boolean pushflag) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void queryUserPlugins(INetResponse response, String user_id,
//    		Integer offset, Integer limits, Integer type, Integer hasDetailOrNot) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public INetRequest modifyPassword(INetResponse response, String oldMd5,
//			String newMd5, String sessionId) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//	
//	@Override
//	public void searchContacts(String text, INetResponse response) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public INetRequest getGroupContactInfo(INetResponse response,
//			boolean batchRun) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public INetRequest putPrivacy(INetResponse response,
//			boolean verification_required, boolean id_searchable,
//			boolean name_searchable, boolean phone_searchable) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public void getCaptchaBinding(String account, String action, INetResponse response) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void bindAccount(String account, String captcha, String passwordToken, INetResponse response) {
//		
//	}
//
//	@Override
//	public void bindRenrenCount(String count, String psw,
//			INetResponse response) {
//		JsonObject bundle = obtainBaseRequestBundle(false);
//		bundle.put("renren_account", count);
//		bundle.put("renren_password", psw);
//		sendRequest("/user/binding/renren", bundle, response);
//	}
//
//	@Override
//	public void unBindRenrenCount(String renrenUid,
//			INetResponse response) {
//		JsonObject bundle = obtainBaseRequestBundle(false);
//		bundle.put("action", "DELETE");
//		sendRequest("/user/binding/renren/" + renrenUid, bundle, response);
//	}
//
//	@Override
//	public INetRequest logout(INetResponse response, String sessionId) {
//		return null;
//	}
//
//    @Override
//	public INetRequest putProfile(INetResponse response, Map<String, String> map) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public void searchColleges(String text, INetResponse response) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public INetRequest getBlacklist(INetResponse response) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//    @Override
//    public void uploadHeadPhoto(INetResponse response, int x, int y, int width, int height, byte[] data) {
//
//        JsonObject bundle = obtainBaseRequestBundle(false);
//        bundle.put("action", "POST");
////        bundle.put("data", buildUploadHeadData(0, data));
//        bundle.put("x", x);
//        bundle.put("y", y);
//        bundle.put("width", width);
//        bundle.put("height", height);
//        sendRequest("file/head_photo", bundle, response);
//    }
//
//    @Override
//    public void uploadPhoto(INetResponse response, byte[] data){
//
//        JsonObject bundle = obtainBaseRequestBundle(false);
////        bundle.put("data", buildUploadHeadData(0, data));
//        sendRequest("file/photo", bundle, response);
//    }
//
//    @Override
//    public void uploadFile(INetResponse response, byte[] data){
//
//        JsonObject bundle = obtainBaseRequestBundle(false);
//        bundle.put("action", "POST");
////        bundle.put("data", buildUploadHeadData(0, data));
//        sendRequest("file/common_file", bundle, response);
//    }
//
//    @Override
//    public void uploadPhoto() {
//
//    }
//
//	@Override
//	public void getAccountCaptcha(INetResponse response, String email) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void contactFriends(ContactsContractModel[] contacts,
//			INetResponse response) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void postPNGPhoto(byte[] imgData, long toId, INetResponse response) {
//		JsonObject bundle = new JsonObject();
//		INetRequest request = obtainINetRequest("");
//		bundle.put("data", buildUploadPNGPhotoData(toId, imgData));
//		request.setData(bundle);
//		request.setResponse(response);
//		// 使用 3G 服务器的 secret_key
//		request.setSecretKey(sRuntimeSecretKey);
//		request.setType(INetRequest.TYPE_HTTP_POST_IMG);
//		HttpProviderWrapper.getInstance().addRequest(request);
//	}
//
//	@Override
//	public void register(String number, String pwd, String captcha, int gender,
//			String name, INetResponse response) {
//		// TODO Auto-generated method stub
//		
//	} 
//	@Override
//	public void uploadLanguage(INetResponse response, String language) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void getPasswordToken(INetResponse response, String passwordMd5) {
//		// TODO Auto-generated method stub
//		
//	}
//
//    @Override
//    public void shareLinkToFacebook(INetResponse response){
//
//        JsonObject bundle = obtainBaseRequestBundle(false);
//        sendRequest("/share/link/facebook", bundle, response);
//    }
//	@Override
//	public INetRequest getSixinInfo(INetResponse response, String sinxId) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public void loginSiXin(String account, String passwordMd5, long session,
//			int captcha_needed, String captcha, INetResponse response) {
//		
//	}
//
//	@Override
//	public void loginRenRen(String account, String passwordMd5, long session,
//			int captcha_needed, String captcha, INetResponse response) {
//		
//	}
//
//	@Override
//	public INetRequest addContact(INetResponse response, String contactUid,
//			int verification_needed, String verification) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public INetRequest delContact(INetResponse response, String contactUid) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public void getUpdateInformation(INetResponse response, int up, int name,
//			int from, int pubDate, String version, String language) {
//		// TODO Auto-generated method stub
//		
//	}
//
//}
