package com.common.binder;

import com.core.util.CommonUtil;

public class LocalBinderPool {

	private LocalBinderPool(){}
	PollBinder mBinder = null;
	OnPushBinderListener mListener = null;
	private static final byte[] LOCK = new byte[0];
	private static LocalBinderPool sInstance = new LocalBinderPool();
	public static LocalBinderPool getInstance(){
		return sInstance;
	}
	public void push(PollBinder binder){
		synchronized (LOCK) {
			mBinder = binder;
			if(mListener!=null){
				mListener.onPushBinder(mBinder);
			}
			LOCK.notifyAll();
		}
	}
	
	public PollBinder obtainBinder(){
		synchronized (LOCK) {
			if(mBinder==null){
				try {
					LOCK.wait();
				} catch (InterruptedException e) {}
			}
			return mBinder;
		}
	}
	
	public boolean isContainBinder(){
		return mBinder!=null;
	}
	
	public static interface OnPushBinderListener{
		public void onPushBinder(PollBinder binder);
	}
	public void registorOnPushBinderListener(OnPushBinderListener listener){
		synchronized (LOCK) {
			mListener = listener;
			if(mBinder!=null){
				mListener.onPushBinder(mBinder);
			}
		}
	}
}
