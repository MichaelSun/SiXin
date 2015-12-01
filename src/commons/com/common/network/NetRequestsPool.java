package com.common.network;

import com.common.binder.LocalSendThread;
import com.common.binder.MessageState;
import com.common.binder.MessageState.SEND_STATE;

/**
 * @author dingwei.chen1988@gmail.com
 * @说明 网络请求池 (超时监控)
 * */
public final class NetRequestsPool extends AbstractRequestsPool {

	private static NetRequestsPool sPool = new NetRequestsPool();
	public static NetRequestsPool getInstance(){return sPool;}
	
	private NetRequestsPool(){
		LocalSendThread.getInstance().setOnSendMessageListener(this);
	} 
	/*添加一个网络请求*/
	public void sendNetRequest(NetRequestListener request){
		request.setKey(System.currentTimeMillis());
		this.addToRequestPool(request);
		this.addToCheckTimeOutQueue(request);//添加的超时检测队列
		
	}
	private void addToRequestPool(NetRequestListener request){
		if(request.getKey()!=-1){
			synchronized (mRequestPool) {
				mRequestPool.put(request.getKey(), request);
			}
		}
		LocalSendThread.getInstance().send(request.getKey(), request.getSendNetMessage());
	}
	
	public void callSuccessNotSyn(long key){
		onSuccess(key);
	}
	public void callErrorNotSyn(Long key,int errorCode,String errorMsg){
		onError(key, errorCode, errorMsg);
	}
	
	/*异步回调*/
	public void callDataCallbackNotSyn(long key,Object object){
		synchronized (this.mRequestPool) {
			NetRequestListener listener = this.mRequestPool.get(key);
			if(listener!=null){
				listener.onSuccessRecive(object);
			}
		}
	
	}
	@Override
	public void onMessageState(MessageState state) {
		NetRequestListener request = getRequestFromPool(state.mKey);
		if(request!=null){
			switch(state.mState){
				case SEND_STATE.SEND_OVER:
					if(request.isSyn() && state.mIsSyn){
						onSuccess(state.mKey);//同步发送成功
					}
					break;
				case SEND_STATE.SEND_ERROR:
					onError(state.mKey,state.mErrorCode,state.mAttachMsg);//发送失败
					break;
			}
		}
	}

	@Override
	protected void notifyTimeOutError(long key) {
		this.onError(key,-99,"网络超时");
	}
	
}
