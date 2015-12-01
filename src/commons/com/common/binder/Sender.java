package com.common.binder;

import java.util.LinkedList;
import java.util.List;

import com.common.binder.MessageState.SEND_STATE;
import com.common.manager.ConnectionManager;
import com.common.messagecenter.base.IMessage;

/*发送器*/
final class Sender{
	RemoteServiceBinder mBinder = null;
	private static Sender sInstance = new Sender();
	private List<MessageState> mMessageStates = new LinkedList<MessageState>();
	private Sender(){}
	public static Sender getInstance(){
		return sInstance;
	}
	public void setBinder(RemoteServiceBinder binder){
			mBinder = binder;
	}
	
	public void send(long key, String message){
		if(message == null){
			MessageState state = this.obtainMessageState(key, SEND_STATE.SEND_ERROR);
			state.mAttachMsg = "发送消息不能为空";
			this.notifySendState(state);
			return;
		}
		if(!ConnectionManager.getInstance().isConnected()){
			MessageState state = this.obtainMessageState(key, SEND_STATE.SEND_ERROR);
//			state.mAttachMsg = "未连接";
			this.notifySendState(state);
			return;
		}
			
		if(key!=-1){
			ConnectionManager.getInstance().sendMessage(new SendMessageCallback(key, message));
		}
	}
	class SendMessageCallback implements IMessage{

		private long mKey = -1L;
		private String mContent =null;
		public SendMessageCallback(long key,String content){
			this.mKey = key;
			this.mContent = content;
		}
		@Override
		public void onSendSuccess(String result) {
			MessageState state = obtainMessageState(mKey, SEND_STATE.SEND_OVER);
			notifySendState(state);
		}
		@Override
		public void onSendFailed(String result) {
			MessageState state =  obtainMessageState(mKey, SEND_STATE.SEND_OVER);
			state.mAttachMsg = result;
			notifySendState(state);
		}

		@Override
		public String getContent() {
			return this.mContent;
		}
	}
	
	
	private MessageState obtainMessageState(long key,int state) {
		MessageState  messageState = new MessageState(key, 0,state);
		return messageState;
	}
	protected void notifySendState(MessageState state){
		synchronized (mMessageStates) {
			mMessageStates.add(state);
			mMessageStates.notify();
		}
	}
	public MessageState readSendState(){
		synchronized (mMessageStates) {
			while(true){
				if(mMessageStates.size()>0){
					return mMessageStates.remove(0);
				}else{
					try {
						mMessageStates.wait();
					} catch (Exception e) {}
				}
			}
		}
	}
	
}
