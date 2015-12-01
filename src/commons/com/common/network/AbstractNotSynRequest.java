package com.common.network;



/**
 * @author dingwei.chen1988@gmail.com
 * */
public abstract class AbstractNotSynRequest<T> implements NetRequestListener<T>{

	
	protected long mKey = -1L;
	
	@Override
	public long getKey() {
		return mKey;
	}

	@Override
	public void setKey(long key) {
		this.mKey = key;
	}

	@Override
	public void onNetError(int errorCode,String errorMsg) {
		if(mCallback!=null){
			mCallback.onError(errorCode,errorMsg);
		}
	}

	@Override
	public void onNetSuccess() {
		if(mCallback!=null){
			mCallback.onSuccess();
		}
	}

	@Override
	public long getNetTimeOutTime() {
		// TODO Auto-generated method stub
		return 59000;
	}

	@Override
	public abstract String getSendNetMessage();

	@Override
	public boolean isSyn() {
		return false;
	}

	@Override
	public void onSuccessRecive(T data) {
		if(mCallback!=null){
			mCallback.onSuccessRecive(data);//String
		}
		
	}
	protected String getId(){
		if(this.mKey!=-1){
			return this.mKey+"";
		}
		return null;
	}
	OnDataCallback mCallback = null;
	public AbstractNotSynRequest<T> setCallback(OnDataCallback<T> callback){
		mCallback = callback;
		return this;
	}
	
	
	public static interface OnDataCallback<E>{
		public void onSuccess();
		public void onSuccessRecive(E data);
		public void onError(int errorCode,String errorMsg);
	}
	
}
