package com.common.messagecenter.base;

import android.content.Context;

import com.common.messagecenter.base.RefKeeper.Iter;

/**
 * @author yang-chen
 */
public interface IConnectionManager {
	void disConnect();

	void start(Context context);

	public static final RefKeeper<IGetConnectionState> sGetConnectionState = new RefKeeper<IGetConnectionState>();

	final static Iter<IGetConnectionState> sOnConnectSuccessIter = new Iter<IGetConnectionState>() {

		@Override
		public boolean iter(IGetConnectionState t) {
			t.onConnectSuccess();
			return false;
		}

	};

	final static Iter<IGetConnectionState> sOnConnectFailedIter = new Iter<IGetConnectionState>() {

		@Override
		public boolean iter(IGetConnectionState t) {
			t.onConnectFailed();
			return false;
		}

	};

	final static Iter<IGetConnectionState> sOnBeginReconnectIter = new Iter<IGetConnectionState>() {

		@Override
		public boolean iter(IGetConnectionState t) {
			t.onBeginReonnect();
			return false;
		}

	};

	final static Iter<IGetConnectionState> sOnEndReonnectIter = new Iter<IGetConnectionState>() {

		@Override
		public boolean iter(IGetConnectionState t) {
			t.onEndReonnect();
			return false;
		}

	};
	
	final static Iter<IGetConnectionState> sOnRecvOfflineIter = new Iter<IGetConnectionState>() {
		
		@Override
		public boolean iter(IGetConnectionState t) {
			t.onRecvOffLineMessage();
			return false;
		}
		
	};

}
