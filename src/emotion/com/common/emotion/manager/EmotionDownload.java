package com.common.emotion.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.common.R;
import com.common.emotion.manager.IEmotionManager.OnEmotionParserCallback;
import com.common.emotion.model.EmotionBaseModel;
import com.common.manager.DataManager;
import com.common.mcs.INetReponseAdapter;
import com.common.mcs.INetRequest;
import com.common.mcs.McsServiceProvider;
import com.core.json.JsonObject;
import com.core.util.AbstractApplication;
import com.core.util.CommonUtil;

/**
 * 下载表情控制类
 * @author jiaxia
 *
 */
public class EmotionDownload {

	private static EmotionDownload eDownLoad = new EmotionDownload();
	private HashMap<Integer, OnEmotionParserCallback>requestPool = new HashMap<Integer, OnEmotionParserCallback>();
	
	private static final int MAX_POOL = 24;
	private static final String TAG = "EmotionDownload";
	//private OnEmotionParserCallback mParserCallback = null;
	private EmotionDownload(){
		
	}
	
	public static EmotionDownload getInstance(){
		
		return eDownLoad;
	}
	
	/**
	 * 
	 * @author jiaxia 下载多个表情单元的监听器接口
	 */
	public static interface DownloadEmotionListener {

		
		public void onSuccess(List<EmotionBaseModel> tempList);

		public void onError(String errorMsg);
	}
	/**
	 * 下载多个表情
	 */
		public void downloadEmotion(String package_id,String[] emotion_ids,OnEmotionParserCallback parserlistener) {
			long start = System.currentTimeMillis();
			String emotionIds = createEmotionIds(emotion_ids);
			CommonUtil.log(TAG, "downloadEmotion:"+package_id+"||"+emotionIds);
			int request = (package_id+emotionIds).hashCode();
			if(!requestPool.containsKey(request)){   //将请求放入请求池中
				McsServiceProvider.getProvider().getEmotionFromNet(package_id, emotionIds,
						new DownloadEmotionResponse(package_id,emotionIds), false);
				CommonUtil.log(TAG, "add hash:"+request);
				addRequest(request,parserlistener);
				
			}
			
		}
		/**
		 * 加入请求池
		 * @param requestHashcode
		 */
		public void addRequest(int requestHashcode,OnEmotionParserCallback callback){
			requestPool.put(requestHashcode, callback);
			CommonUtil.log(TAG, "add pool size:"+requestPool.size());
		}
		/**
		 * 移除请求
		 * @param requestHashcode
		 */
		public void removeRequest(int requestHashcode){
			
			if(requestPool.containsKey(requestHashcode)){
				requestPool.remove(requestHashcode);
			}
			CommonUtil.log(TAG, "remove pool size:"+requestPool.size());
		}
		/**
		 *  获取解析监听器
		 * @param key
		 * @return
		 */
		public OnEmotionParserCallback getParserCallBack(int key){
			if(requestPool.containsKey(key)){
				return requestPool.get(key);
			}
			return null;
		}
		
		public void downloadEmotionBitmap(EmotionDownloadBitmap listener){
		  DataManager.getInstance().getImage(listener.getUrl(), listener);
		}
		/**
		 public static byte[] readInputStream(InputStream inStream) throws Exception{  
		        ByteArrayOutputStream outStream = new ByteArrayOutputStream();  
		        //创建一个Buffer字符串  
		        byte[] buffer = new byte[1024];  
		        //每次读取的字符串长度，如果为-1，代表全部读取完毕  
		        int len = 0;  
		        //使用一个输入流从buffer里把数据读取出来  
		        while( (len=inStream.read(buffer)) != -1 ){  
		            //用输出流往buffer里写入数据，中间参数代表从哪个位置开始读，len代表读取的长度  
		            outStream.write(buffer, 0, len);  
		        }  
		        //关闭输入流  
		        inStream.close();  
		        //把outStream里的数据写入内存  
		        return outStream.toByteArray();  
		    }  
*/
	   private String createEmotionIds(String[] ids){
		   if(ids == null||ids.length == 0)
			   return "";
		   
		   StringBuffer sb = new StringBuffer();
		   for(int k = 0;k<ids.length;k++){
			   sb.append(ids[k]);
			   sb.append(",");
		   }
		   return sb.substring(0, sb.length()-1);
	   }
	
	/**
	 * 
	 * @author jiaxia 下载表情的Response
	 */
	class DownloadEmotionResponse extends INetReponseAdapter {

        String pid  ;
        String emotionids;
		 int key ;
		
		public DownloadEmotionResponse(String pid,String emotionids) {
			this.pid = pid;
			this.emotionids = emotionids;
			this.key = ( pid+emotionids).hashCode();
		}

		@Override
		public void onSuccess(INetRequest req, JsonObject data) {
			CommonUtil.log(TAG, data.toJsonString());
			List<EmotionBaseModel>lists = EmotionJsonParser.jsonParserNetEmotionFactory(data);
		  //  mEmotionListener.onSuccess(lists);\
			OnEmotionParserCallback mParserCallback = null;
			mParserCallback = getParserCallBack(key);
			if (lists != null&&lists.size()>0) { // 成功则进行图片下载
				for (EmotionBaseModel model : lists) {
					EmotionDownloadBitmap mDownloadBitmap = new EmotionDownloadBitmap(
							model, mParserCallback,key);
					EmotionDownload.getInstance().downloadEmotionBitmap(
							mDownloadBitmap);
				}

			}
			else{  //失败则告诉业务层失败
				if(mParserCallback!=null){
					try{
						Bitmap b = BitmapFactory.decodeResource(AbstractApplication.getAppContext().getResources(), R.drawable.emotion_default);
						mParserCallback.onEmotionError(b);
					}catch(OutOfMemoryError error){
					}
				}
				removeRequest(key);	//移除请求
			}
		}

		@Override
		public void onError(INetRequest req, JsonObject data) {
			CommonUtil.log(TAG, data.toJsonString());
			OnEmotionParserCallback parser = getParserCallBack(key);
			if(parser!=null){
				Bitmap b = BitmapFactory.decodeResource(AbstractApplication.getAppContext().getResources(), R.drawable.emotion_default);
				parser.onEmotionError(b);
			}
		   removeRequest(key);
			
		}

	}
}
