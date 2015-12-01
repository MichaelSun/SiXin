package com.common.network;



/**
 * @author dingwei.chen
 * @说明 同步请求
 * update by dingwei.chen 9-7
 * */
public abstract class AbstractSynRequest implements NetRequestListener<NULL>{

	private long mKey = -1;
	
	@Override
	public long getKey() {
		return mKey;
	}

	@Override
	public void setKey(long key) {
		mKey = key;
	}

	@Override
	public abstract void onNetError(int errorCode,String errorMsg);

	@Override
	public abstract void onNetSuccess() ;

	@Override
	public long getNetTimeOutTime() {
		return 59*1000;
	}

	@Override
	public abstract String getSendNetMessage();

	@Override
	public boolean isSyn() {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public void onSuccessRecive(NULL data) {}
	
	protected String getId(){
		if(this.mKey!=-1){
			return this.mKey+"";
		}
		return null;
	}

}
