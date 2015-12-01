package com.common.manager;

import com.common.mcs.INetReponseAdapter;
import com.common.mcs.INetRequest;
import com.common.mcs.INetResponse;
import com.common.mcs.McsServiceProvider;
import com.core.json.JsonArray;
import com.core.json.JsonObject;

/**
 * @author dingwei.chen
 * @description 新鲜事管理
 * */
public final class FeedManager {

	private static FeedManager sInstance = new FeedManager();
	private FeedManager(){}
	public static FeedManager getInstance(){
		return sInstance;
	}
	public void getFeedList(int page, int page_size,String types,INetResponse response){
		McsServiceProvider.getProvider().getFeedList(page, page_size, types, response);
	}
	/*获取新鲜事*/
	public void getFeedList( long[] ids,INetResponse response){
		McsServiceProvider.getProvider().getFeedList(ids, response);
	}
	
	
	
//	public static interface FeedListener {
//		public void onGetFeed(JsonObject[] array);
//		public void onError();
//	}
//	public static class FeedResponse extends INetReponseAdapter{
//		FeedListener mListener = null;
//		public FeedResponse(FeedListener listener) {
//			mListener = listener;
//		}
//		
//		@Override
//		public void onSuccess(INetRequest req, JsonObject data) {
//			JsonArray array = data.getJsonArray("feed_list");
//			JsonObject[] arrays = new JsonObject[array.size()];
//			array.copyInto(arrays);
//			if(mListener!=null){
//				mListener.onGetFeed(arrays);
//			}
//		}
//		@Override
//		public void onError(INetRequest req, JsonObject data) {
//			if(mListener!=null){
//				mListener.onError();
//			}
//		}
//	}
}
