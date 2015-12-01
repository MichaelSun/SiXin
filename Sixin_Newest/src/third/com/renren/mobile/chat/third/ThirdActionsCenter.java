package com.renren.mobile.chat.third;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.Intent;

import com.renren.mobile.chat.RenrenChatApplication;


/**
 * @author dingwei.chen
 * @说明 第三方业务中心
 * */
public final class ThirdActionsCenter {
	
	public static interface Actions
	{
		/*可处理第三方业务数量*/
		int ACTION_SIZE = 2;
		/*个人聊天接口*/
		String ACTION_CHAT 		= ".ACTION_CHAT";
		/*会话页面*/
		String ACTION_SESSION = ".ACTION_SESSION";
	}
	public static interface THIRD_APP_PARMAS{
		String APP_NAME = "app_name";
		String ACTION_NAME = "action_name";
	}
	
	private static ThirdActionsCenter sInstance = new ThirdActionsCenter();
	/*业务表*/
	private Map<String, ThirdAction> mActionTable 
					= new HashMap<String, ThirdAction>(Actions.ACTION_SIZE);
	
	private ThirdActionsCenter(){
		this.registorActions();
	}
	/*业务注册*/
	private void registorActions(){
		mActionTable.put(RenrenChatApplication.PACKAGE_NAME + Actions.ACTION_SESSION, new ThirdAction_ChatSession());
		mActionTable.put(RenrenChatApplication.PACKAGE_NAME + Actions.ACTION_CHAT, new ThirdAction_Chat());
	}
	
	public static ThirdActionsCenter getInstance(){
		return sInstance;
	}
	private String mAction_Name = null;
	private String mApp_Name = null;
	
	/**
	 * @author dingwei.chen
	 * @说明 业务分发
	 * */
	public void dispatchAction(Context context,String actionName,Intent intent){
		ThirdAction action = mActionTable.get(actionName);
		if(action!=null){
			mAction_Name = intent.getStringExtra(THIRD_APP_PARMAS.ACTION_NAME);
			mApp_Name = intent.getStringExtra(THIRD_APP_PARMAS.APP_NAME);
			action.process(context, actionName, intent,this);
		}
	}
	
	public void sendThirdBroadcast(){
		if(mAction_Name!=null){
			Intent intent = new Intent(mAction_Name);
			if(mApp_Name!=null){
				intent.putExtra(THIRD_APP_PARMAS.APP_NAME, mApp_Name);
			}
			RenrenChatApplication.mContext.sendBroadcast(intent);
			this.clearAppParams();
		}
	}
	
	public void clearAppParams(){
		this.mApp_Name = null;
		this.mAction_Name = null;
	}
	
}
