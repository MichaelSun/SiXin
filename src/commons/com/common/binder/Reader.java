//package com.common.binder;
//
//import java.util.LinkedList;
//import java.util.List;
//
//public class Reader {
//	
//	private List<String> mMessageList = new LinkedList<String>();
//	private static final String REPLACE_TEXT = new String(new char[]{(char)10});
//	private static Reader sInstance = new Reader();
//	private Reader(){}
//	RemoteServiceBinder mBinder = null;
//	public void setBinder(RemoteServiceBinder binder){
//		synchronized (mMessageList) {
//			mBinder = binder;
//		}
//	}
//	public static Reader getInstance(){
//		return sInstance;
//	}
//	public void onRecieveMessage(String message){
//		synchronized (mMessageList) {
//			if(message!=null){
//				message = message.replaceAll(REPLACE_TEXT, "");
//				mMessageList.add(message);
//				if(mBinder!=null){
//					mMessageList.notify();
//				}
//			}
//		}
//	}
//	public String readMessage(){
//		synchronized (mMessageList) {
//			while(true){
//				if(mMessageList.size()>0){
//					return mMessageList.remove(0);
//				}else{
//					try {
//						mMessageList.wait();
//					} catch (InterruptedException e) {}
//				}
//			}
//		}
//	}
//	public void clear(){
//		synchronized (mMessageList) {
//			mMessageList.clear();
//		}
//	}
//	
//}
