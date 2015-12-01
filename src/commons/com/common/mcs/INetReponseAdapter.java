package com.common.mcs;

import com.common.utils.Methods;
import com.core.json.JsonObject;
import com.core.json.JsonValue;


/**
 * @author dingwei.chen
 * */
public class INetReponseAdapter implements INetResponse{

	protected INetResponse mInnerAdapter = null;
	
	public INetReponseAdapter(){}
	
	public INetReponseAdapter(INetReponseAdapter innerAdapter){
		this.mInnerAdapter = innerAdapter;
	}
	
	@Override
	public void response(INetRequest req, JsonValue obj) {
		try {
			this.onResponseBegin();
			JsonObject data = (JsonObject)obj;
			if(Methods.checkNoError(req, data)){
				this.onSuccess(req, data);
				
			}else{
				this.onError(req, data);
			}
		} catch (Exception e) {
			this.onResponseException(e);
		}finally{
			this.onResponseOver();
		}
	}
	public void onSuccess(INetRequest req, JsonObject data){
		
	}
	public void onError(INetRequest req, JsonObject data){
		
	}
	
	protected void onResponseBegin(){}
	protected void onResponseOver(){}
	protected void onResponseException(Exception e){}
}
