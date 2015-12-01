package com.common.network;

import java.lang.Thread.State;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.common.binder.ISender.OnSendMessageListener;

abstract class AbstractRequestsPool  implements OnSendMessageListener{
	
	private List<NetRequestCheckTimeoutWarpper> mRequestTimeoutCheckQueue = new LinkedList<NetRequestCheckTimeoutWarpper>();
	private WatchTimeOutThread mCheckTimeOutThread = new WatchTimeOutThread();
	protected Map<Long,NetRequestListener> mRequestPool = new HashMap<Long,NetRequestListener>();
	protected void addToCheckTimeOutQueue(NetRequestListener request){
		synchronized (mRequestTimeoutCheckQueue) {
			if(mCheckTimeOutThread.getState()==State.NEW){
				mCheckTimeOutThread.start();
			}
			this.addToRequestQueue(new NetRequestCheckTimeoutWarpper(request));//监控队列
			mRequestTimeoutCheckQueue.notify();
		}
	}
	private void addToRequestQueue(NetRequestCheckTimeoutWarpper request){
		mRequestTimeoutCheckQueue.add(request);
	}
	private static class NetRequestCheckTimeoutWarpper{
		public NetRequestListener mNetRequest = null;
		public long mTimeOutTime = -1L;
		public NetRequestCheckTimeoutWarpper(NetRequestListener request){
			mNetRequest = request;
			mTimeOutTime = request.getNetTimeOutTime();
		}
	}
	
	/*超时检测线程*/
	private class WatchTimeOutThread extends Thread{
		private byte[] LOCK = new byte[0];
		private NetRequestCheckTimeoutWarpper mCurrentRequest = null;
		public void removeKey(long key){
			synchronized (mRequestTimeoutCheckQueue) {
				if(mCurrentRequest!=null&&mCurrentRequest.mNetRequest.getKey()==key){
					synchronized (LOCK) {
						LOCK.notify();
					}
					return;
				}
				NetRequestCheckTimeoutWarpper r = null;
				for(NetRequestCheckTimeoutWarpper w:mRequestTimeoutCheckQueue){
					if(w.mNetRequest.getKey()==key){
						r = w;
					}
				}
				if(r!=null){
					mRequestTimeoutCheckQueue.remove(r);
				}
			}
		}
		
		@Override
		public void run() {
			while(true){
				NetRequestCheckTimeoutWarpper request = null;
				synchronized (mRequestTimeoutCheckQueue) {
					if(mRequestTimeoutCheckQueue.size()>0){
						request = mRequestTimeoutCheckQueue.remove(0);
					}else{
						try {
							mRequestTimeoutCheckQueue.wait();
						} catch (InterruptedException e){}
					}
				}
				if(request!=null){
					synchronized (LOCK) {
						mCurrentRequest = request;
						long wait_time = request.mTimeOutTime;
						if(wait_time > 0){
							try {
								LOCK.wait(wait_time);
							} catch (InterruptedException e) {}
						}
						notifyTimeOutError(request.mNetRequest.getKey());
					}
				}
			}
		}
	}
	protected NetRequestListener getRequestFromPool(Long key){
		synchronized (mRequestPool) {
			return mRequestPool.get(key);
		}
	}
	protected void onError(Long key,int errorCode,String errorMsg){
		synchronized (mRequestPool) {
			NetRequestListener r = mRequestPool.get(key);
			if(r!=null){
				r.onNetError(errorCode,errorMsg);
			}
			mCheckTimeOutThread.removeKey(key);
			this.removeFromPool(key);
		}
	}
	protected void removeFromPool(Long key){
		synchronized (mRequestPool) {
			mRequestPool.remove(key);
		}
	}
	protected abstract void notifyTimeOutError(long key);
	public static interface OnSendListener{
		public void onSendSuccess(long key);
		public void onSendError(long key);
		public void onSendPrepare(Set<Long> sendSet);
	}
	OnSendListener mSendListener  = null;
	public void registorOnSendListener(OnSendListener listener){
		mSendListener = listener;
		mSendListener.onSendPrepare(mRequestPool.keySet());
	}
	protected void onSuccess(Long key){
		synchronized (mRequestPool) {
			NetRequestListener r = mRequestPool.get(key);
			if(r!=null){
				r.onNetSuccess();
			}
			mCheckTimeOutThread.removeKey(key);
			this.removeFromPool(key);
		}
	}
}
