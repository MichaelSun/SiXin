package com.common.network.requests;

import plugin.plugins.v0.ChatClientResponse;

import android.os.Bundle;

import com.common.network.AbstractNotSynRequest;

public class Message_SendByPlugin extends AbstractNotSynRequest {
	
	private String mNetPackage;
	private ChatClientResponse mResponse;
	
	public Message_SendByPlugin(String netPackage){
		mNetPackage = netPackage;
	}
	
	public Message_SendByPlugin(ChatClientResponse response, String netPackage){
		mNetPackage = netPackage;
		mResponse = response;
	}	
	
	@Override
	public void onNetError(int errorCode,String errorMsg) {		
		if( null != mResponse) {
			Bundle bundle = new Bundle();
			bundle.putInt("errorCode", errorCode);
			bundle.putString("errorMsg", errorMsg);
			mResponse.response(bundle);
		}	
	}

	@Override
	public void onNetSuccess() {
		if( null != mResponse) {
			Bundle bundle = new Bundle();
			bundle.putString("success","ok");
			mResponse.response(bundle);	
		}
	}
	
	@Override
	public String getSendNetMessage() {
		return mNetPackage;
	}
}
