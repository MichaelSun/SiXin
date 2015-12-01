package com.common.actions;

import com.common.network.AbstractNotSynRequest;
import com.common.network.NetRequestsPool;
import com.data.action.Actions;
import com.data.xmpp.XMPPNodeRoot;

/**
 * @author dingwei.chen1988@gmail.com
 * @说明 业务处理模型
 * */
public abstract class Action {

	protected boolean mIsSelfAction = true;
	public Actions mAction = null;
	protected AbstractNotSynRequest mCallbackRequest = null;
//	public OnPollDataReciveCallback CALLBACK = null;
	
	
	public abstract void processAction(XMPPNodeRoot node,long id);
	
	public abstract void checkActionType(XMPPNodeRoot node)  throws ActionNotMatchException;
	
	/*开启业务*/
	public void beginAction(){}
	/*提交业务*/
	public void commitAction(){}
	
	public Action addCheckCase(boolean flag) throws ActionNotMatchException{
		if(flag){
			return this;
		}else{
			throw new ActionNotMatchException();
		}
	}
	public void setActions(Actions action){
		this.mAction = action;
	}
	public void setCallback(AbstractNotSynRequest request){
		this.mCallbackRequest = request;
	}
	public  void onSuccessCallback(long key,Object object){
		NetRequestsPool.getInstance().callDataCallbackNotSyn(key, object);
	}
	public void onErrorCallback(long id,int errorCode,String errorMsg){
		NetRequestsPool.getInstance().callErrorNotSyn(id, errorCode, errorMsg);
	}
	public void onSuccessCallback(long id){
		NetRequestsPool.getInstance().callSuccessNotSyn(id);
	}
}
