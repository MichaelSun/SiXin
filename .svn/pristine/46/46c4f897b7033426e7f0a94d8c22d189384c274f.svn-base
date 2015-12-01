package com.common.manager;

import android.graphics.Bitmap;

import com.common.mcs.INetReponseAdapter;
import com.common.mcs.INetRequest;
import com.common.mcs.INetResponse;
import com.common.mcs.McsServiceProvider;
import com.core.json.JsonObject;
import com.core.util.ImageUtil;
import com.core.util.CommonUtil;

/**
 * @author dingwei.chen
 * @description 数据管理器 用来上传下载数据
 * */
public final class DataManager {
	private static DataManager sInstance = new DataManager();
	private DataManager(){}
	public static DataManager getInstance(){
		return sInstance;
	}
	
	/*获得图片*/
	public void getImage(String url,INetResponse response){
		McsServiceProvider.getProvider().getImage(url, response);
	}
	/*获得图片*/
	public void getImage(String url,ImageListener listener){
		McsServiceProvider.getProvider().getImage(url, new ImageResponse(listener));
	}
	public static interface ImageListener extends DataListener{
		public void onGetBitmap(Bitmap bitmap);
	}
	public static interface DataListener{
		public void onGetData(byte[] data);
		public void onGetError();
	}
	
	public static class ImageResponse extends INetReponseAdapter{
		public ImageListener mListener =null;
		public ImageResponse(ImageListener listener){
			this.mListener = listener;
		}
		@Override
		public void onSuccess(INetRequest req, JsonObject data) {
			byte[] imgData = data.getBytes(IMG_DATA);
			if(this.mListener!=null){
				this.mListener.onGetData(imgData);
			}
			Bitmap bitmap = ImageUtil.createImageByBytes(imgData);
			if(this.mListener!=null){
				this.mListener.onGetBitmap(bitmap);
			}
		}
		@Override
		public void onError(INetRequest req, JsonObject data) {
			if(this.mListener!=null){
				this.mListener.onGetError();
			}
		}
	}
	
	
	/*获得语音*/
	public void getVoice(String url,INetResponse response){
		McsServiceProvider.getProvider().getVoice(url, response);
	}
	/*获得语音*/
	public void getVoice(String url,DataListener listener){
		McsServiceProvider.getProvider().getVoice(url, new VoiceResponse(listener));
	}
	public static class VoiceResponse extends INetReponseAdapter{
		public DataListener mListener =null;
		public VoiceResponse(DataListener listener){
			this.mListener = listener;
		}
		@Override
		public void onSuccess(INetRequest req, JsonObject data) {
			byte[] voiceData = data.getBytes(VOICE_DATA);
			if(this.mListener!=null){
				this.mListener.onGetData(voiceData);
			}
		}
		@Override
		public void onError(INetRequest req, JsonObject data) {
			if(this.mListener!=null){
				this.mListener.onGetError();
			}
		}
	}
	
	
	public void postVoice(long toId, String vid, int seqid, String mode, int playTime, byte[] voiceData, INetResponse response){
		McsServiceProvider.getProvider().postVoice(toId, vid, seqid, mode, playTime, voiceData, response);
	}
	public void postVoice(long toId, int playTime, byte[] voiceData, INetResponse response){
		this.postVoice(toId, Uploadable_Voice.VID, Uploadable_Voice.SEQ_ID, Uploadable_Voice.Mode.END, playTime, voiceData, response);
	}
	public void postVoice(byte[] voiceData,Uploadable_Voice callback){
		callback.onUploadStart();
		this.postVoice(callback.getToId(), callback.getPlayTime(), voiceData, new UploadResponse_Voice(callback));
	}
	
	
	public void postImage(byte[] imgData, long toId, INetResponse response){
		McsServiceProvider.getProvider().postPhoto(imgData, toId, response);
	}
	
	public void postPNGImage(byte[] imgData, long toId, INetResponse response){
		McsServiceProvider.getProvider().postPNGPhoto(imgData, toId, response);
	}
	
	public void postPNGImage(byte[] imgData, Uploadable_Image image){
		image.onUploadStart();
		this.postPNGImage(imgData, image.getToId(), new UploadResponse_Image(image));
	}
	
	public void postImage(byte[] imgData,Uploadable_Image image){
		image.onUploadStart();
		this.postImage(imgData, image.getToId(), new UploadResponse_Image(image));
	}
	
	/*--------------------------------------可上传接口---------------------------------------*/
	/**
	 * @author dingwei.chen
	 * */
	public interface Uploadable{
		public long getToId();
		public void onUploadStart();
		public void onUploadError();
	}
	public static abstract class UploadResponse<T extends Uploadable> extends INetReponseAdapter{

		private T mListenner = null;
		public UploadResponse(T listenner){
			mListenner = listenner;
		}
		@Override
		public void onSuccess(INetRequest req,JsonObject data) {
			this.uploadSuccess(req, data,mListenner);
		};
		@Override
		public void onError(INetRequest req, JsonObject data) {
			this.uploadError(req, data,mListenner);
			if(mListenner!=null){
				mListenner.onUploadError();
			}
			
		};
		public void onUploadStart(){
			if(mListenner!=null){
				mListenner.onUploadStart();
			}
		}
		/*上传失败回调*/
		public abstract void uploadError(INetRequest req, JsonObject data,T listener);
		/*上传成功回调*/
		public abstract void uploadSuccess(INetRequest req, JsonObject data,T listener);
	}
	
	public static interface Uploadable_Image extends Uploadable{
		public void onUploadOver(String tinyUrl,String mainUrl,String largeUrl);
	}
	public static interface Uploadable_Voice extends Uploadable{
		public static interface Mode{
			public static final String SEGMENT="segment" ;//表示非结尾片段
			public static final String END="end";//表示结尾 片段
		}
		public int SEQ_ID = 1;
		public String VID = "1";
		public int getPlayTime();
		public void onUploadOver(String voiceId,String voiceUrl);
		
	}
	
	public static class UploadResponse_Voice extends UploadResponse<Uploadable_Voice>{

		public UploadResponse_Voice(Uploadable_Voice listenner) {
			super(listenner);
		}
		@Override
		public void uploadError(INetRequest req, JsonObject data,Uploadable_Voice listener) {
            CommonUtil.log("VoiceUpload", "Voice error data = "+data.toString());
            listener.onUploadError();
       }
		@Override
		public void uploadSuccess(INetRequest req, JsonObject data,Uploadable_Voice listener) {
            CommonUtil.log("VoiceUpload", "Voice data = "+data.toString());
			if(listener!=null){
				String voiceUrl =  data.getString("file_url");
				String vid =  "0";
				listener.onUploadOver(vid,voiceUrl);
			}
		}
	}
	public static class UploadResponse_Image extends UploadResponse<Uploadable_Image>{

		public UploadResponse_Image(Uploadable_Image listenner) {
			super(listenner);
		}
		@Override
		public void uploadError(INetRequest req, JsonObject data,Uploadable_Image listener) {
			if(listener!=null){
				listener.onUploadError();
			}
		}

		@Override
		public void uploadSuccess(INetRequest req, JsonObject data,Uploadable_Image listener) {
			if(listener!=null){
				CommonUtil.log("image", "data = "+data);
				String tinyUrl = data.getString("medium_url");
				String largeUrl = data.getString("original_url");
				String mainUrl = data.getString("large_url");
				listener.onUploadOver(tinyUrl, mainUrl, largeUrl);
			}
		}
	}
	
}
