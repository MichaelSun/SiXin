package com.common.manager;

import com.common.mcs.INetReponseAdapter;
import com.common.mcs.INetRequest;
import com.common.mcs.INetResponse;
import com.common.mcs.McsServiceProvider;
import com.core.json.JsonArray;
import com.core.json.JsonObject;

public final class FriendManager {

	private static FriendManager sInstance = new FriendManager();
	private  FriendManager(){}
	public static FriendManager getInstance(){
		return sInstance;
	}
	
	
	public INetRequest getAllFriends(boolean batchRun,INetResponse response){
		return McsServiceProvider.getProvider().getFriends(-1, -1, batchRun, response);
	}
	public INetRequest getFriends(int page,int pagesize,boolean batchRun,INetResponse response){
		return McsServiceProvider.getProvider().getFriends(page, pagesize, batchRun, response);
	}
	public void getFriendsRequests(int page, int page_size, int exclude_list, int del_news,int htf,INetResponse response){
		McsServiceProvider.getProvider().getFriendsRequests(page, page_size, exclude_list, del_news, htf, response);
	}
	
	
	
	public static interface FriendListener {
		public void onGetFriends(String headPrefix,JsonObject[] friends);
		public void onGetError(JsonObject data);
	}
	public static class FriendResponse extends INetReponseAdapter{
		FriendListener mListener = null;
		public FriendResponse(FriendListener listener){
			mListener = listener;
		}
		
		@Override
		public void onSuccess(INetRequest req, JsonObject data) {
			String prefix = data.getString("prefix_url");
			JsonArray array = data.getJsonArray("friend_list");
			JsonObject[] objs = null;
			if (array != null) {
				objs = new JsonObject[array.size()];
				array.copyInto(objs);
			}
			if(mListener!=null){
				mListener.onGetFriends(prefix, objs);
			}
		}
		@Override
		public void onError(INetRequest req, JsonObject data) {
			if(mListener!=null){
				mListener.onGetError(data);
			}
		}
	}
	
}
