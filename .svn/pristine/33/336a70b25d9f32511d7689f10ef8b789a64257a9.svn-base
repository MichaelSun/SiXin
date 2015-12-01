package com.common.binder;

import java.util.concurrent.LinkedBlockingQueue;

import com.common.binder.LocalSendThread.Entry;
import com.common.binder.MessageState.SEND_STATE;
import com.core.util.CommonUtil;

/**
 * @author dingwei.chen
 * */
public class LocalSendThread extends LocalThread implements ISender {

	OnSendMessageListener mListener = null;
	LinkedBlockingQueue<Entry> mPrepareToSendMessage = new LinkedBlockingQueue<Entry>();
	Entry mCurrentEntry = null;
	GetSendStateThread mGetStateThread = new GetSendStateThread();

	private LocalSendThread() {
		this.start();
		mGetStateThread.start();
	}

	private static LocalSendThread sInstance = new LocalSendThread();

	public static LocalSendThread getInstance() {
		return sInstance;
	}

	@Override
	public void loop() throws Exception {
		if (mPrepareToSendMessage.size() > 0) {
			if (LocalBinderPool.getInstance().isContainBinder()) {
				PollBinder binder = LocalBinderPool.getInstance()
						.obtainBinder();
				mCurrentEntry = mPrepareToSendMessage.poll();
				binder.send(mCurrentEntry.key, mCurrentEntry.message);
			} else {
				CommonUtil.error("cdw", "没有绑定binder");
				throw new Exception();
			}
		} else {
			synchronized (mPrepareToSendMessage) {
				mPrepareToSendMessage.wait();
			}
		}
	}

	@Override
	public void onException(Exception e) {
		if (mCurrentEntry != null) {
			MessageState state = new MessageState(mCurrentEntry.key,
					SEND_STATE.SEND_ERROR, "本地发送异常");
			onCallback(state);
		}
	}

	@Override
	public void send(long key, String message) {
		synchronized (mPrepareToSendMessage) {
			Entry entry = new Entry(key, message);
			mPrepareToSendMessage.add(entry);
			mPrepareToSendMessage.notify();
		}
	}

	public void setOnSendMessageListener(OnSendMessageListener listener) {
		mListener = listener;
	}

	public void onCallback(MessageState state) {
		if (mListener != null && state != null) {
			this.mListener.onMessageState(state);
		}
	}

	static class Entry {
		long key;
		String message;

		public Entry(long key, String message) {
			this.key = key;
			this.message = message;
		}
	}

	private class GetSendStateThread extends LocalThread {

		@Override
		public void loop() throws Exception {
			PollBinder binder = LocalBinderPool.getInstance().obtainBinder();
			MessageState state = binder.getSendState();
			onCallback(state);
		}

		@Override
		public void onException(Exception e) {
		}

	}

}
