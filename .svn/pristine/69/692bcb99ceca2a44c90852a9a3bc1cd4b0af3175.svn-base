package com.common.manager;

import java.util.List;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.common.binder.LocalBinderPool;
import com.common.binder.LocalBinderPool.OnPushBinderListener;
import com.common.binder.LocalSendThread;
import com.common.binder.MessageState;
import com.common.binder.PollBinder;
import com.common.network.IReqeustConstructor;
import com.common.network.NULL;
import com.common.network.NetRequestListener;
import com.common.network.NetRequestsPool;
import com.common.network.RequestConstructorDicProxy;
import com.core.util.CommonUtil;
import com.data.xmpp.XMPPNode;

/**
 * @author dingwei.chen
 * @description 消息管理
 * */
public class MessageManager implements OnPushBinderListener{

	private static MessageManager sInstance = new MessageManager();
	private MessageManager(){
		LocalBinderPool.getInstance().registorOnPushBinderListener(this);
	}
	public static MessageManager getInstance(){
		return sInstance;
	}
	public void sendMessage(NetRequestListener request){
		if(request!=null){
			NetRequestsPool.getInstance().sendNetRequest(request);
		}
	}
	public void sendMessage(String message){
		if(message!=null){
			LocalSendThread.getInstance().send(-1, message);
		}
	}
	/**
	 * @author dingwei.chen
	 * @说明 旧接口采用的是同步方式发送
	 * */
//	public<T extends IReqeustConstructor> void sendSingleMessage(long fromId,long toId,OnSendTextListener listener,Class<T> clazz){
//		NetRequestListener request = RequestConstructorDicProxy.getInstance(clazz).sendSynMessage(fromId, toId, listener);
//		this.sendMessage(request);
//	}
//	
	public<T extends IReqeustConstructor> void sendSingleMessage(long fromId,long toId, String domain,OnSendTextListener listener,Class<T> clazz){
		NetRequestListener request = RequestConstructorDicProxy.getInstance(clazz).sendSynMessage(fromId, toId, domain, listener);
		this.sendMessage(request);
	}
//	
//	public<T extends IReqeustConstructor> void sendSingleMessage(OnSendTextListener listener,Class<T> clazz){
//		NetRequestListener request = RequestConstructorDicProxy.getInstance(clazz).sendSynMessage(listener.getFromId(), listener.getToId(), listener);
//		this.sendMessage(request);
//	}
	
	/**
	 * @author dingwei.chen
	 * @说明 新接口采用的是异步方式发送
	 * */
	public <T extends IReqeustConstructor> void sendGroupMessage(String fromName,long fromId,long roomId,OnSendTextListener listener,Class<T> clazz){
		NetRequestListener<NULL> request = RequestConstructorDicProxy.getInstance(clazz).sendGroupMessage(fromName, fromId, roomId, listener);
		this.sendMessage(request);
	}
//	public void setOnRecieveMessageListener(OnReceiveListener listener){
//		LocalReceiveThread.getInstance().setListener(listener);
//	}
	
	/**
	 * @author dingwei.chen
	 * @说明 发送聊天报文监听器(现行私信版本)
	 * */
	public static interface OnSendTextListener {
		public void onSendTextPrepare();//发送文本准备(未进行网络请求)
		public void onSendTextSuccess();//发送文本成功
		public void onSendTextError();//发送文本失败
		public boolean hasNewsFeed();
		public long getFromId();
		public long getToId();
		public List<XMPPNode>  getNetPackage();
		public static interface SEND_TEXT_STATE{
				int SEND_PREPARE = MessageState.SEND_STATE.SEND_ERROR;
				int SEND_OVER =  MessageState.SEND_STATE.SEND_OVER;
		}
	}

	@Override
	public void onPushBinder(PollBinder binder) {
		LocalSendThread.getInstance();
	}
	private boolean mIsBinder = false;
	public <T extends Context>void startService(Context context,Class<T> serviceClazz){
		if(mIsBinder && LocalBinderPool.getInstance().isContainBinder()){
			context.startService(new Intent(context, serviceClazz));
			return ;
		}
		mIsBinder = true;
		Intent i = new Intent(context, serviceClazz);
		context.bindService(i, new ServiceConnection() {
			
			@Override
			public void onServiceDisconnected(ComponentName arg0) {}
			@Override
			public void onServiceConnected(ComponentName arg0, IBinder binder) {
				LocalBinderPool.getInstance().push(PollBinder.Stub.asInterface(binder));
			}
		},  Context.BIND_AUTO_CREATE);
	}
	
}
