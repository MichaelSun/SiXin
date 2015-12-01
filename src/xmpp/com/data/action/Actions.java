package com.data.action;

public enum Actions {

	
	
	MESSAGE$_SINGLE_CHAT(VERSION.OLD_VERSION),//单人聊天业务
	MESSAGE$_PRIVATE_CHAT(VERSION.OLD_VERSION), //单人私密聊天业务
	MESSAGE$_PUSH_FEED(VERSION.OLD_VERSION),//新鲜事推送
	MESSAGE$_ACTION(VERSION.OLD_VERSION),//状态消息业务
	MESSAGE$_GROUP_CHAT(VERSION.NEW_VERSION),//群聊业务
	MESSAGE$_PUSH_SOFTINFO(VERSION.NEW_VERSION),//弱提示
	MESSAGE$_UPDATE_SUBJECT(VERSION.NEW_VERSION),//变更主题业务
	MESSAGE$_OFFLINE(VERSION.NEW_VERSION),
	MESSAGE$_RECOMMAND(VERSION.NEW_VERSION),
	MESSAGE$_PLUGIN(VERSION.NEW_VERSION),
	
	/*插件demo添加的Action，用于路由插件*/
	MESSAGE$_PLUGIN_ROUTE(VERSION.NEW_VERSION),//插件路由
	/*插件demo添加的Action，用于路由插件*/
    MESSAGE$_PUSH_SINGLE_LOGIN(VERSION.NEW_VERSION),//单点登陆业务
    MESSAGE$_OFFLINE_MESSAGE(VERSION.NEW_VERSION),//离线消息接收
	
	PRESENCE$_CREATE_ROOM(VERSION.NEW_VERSION),//创建房间
	PRESENCE$_PRESENCE_ROOM(VERSION.NEW_VERSION),//加入房间
	PRESENCE$_DELETED_FROM_ROOM(VERSION.NEW_VERSION),//被删除
	PRESENCE$_NOTIFY_DELETE_ROOM(VERSION.NEW_VERSION),//删除用户通知
	PRESENCE$_UPLOAD_TIMESTAMP(VERSION.NEW_VERSION),//上传时间戳
	PRESENCE$_LEAVE_ROOM(VERSION.NEW_VERSION),//创建房间
	PRESENCE$_DESTROY_ROOM(VERSION.NEW_VERSION),//销毁房间
	PRESENCE$_INVITE(VERSION.NEW_VERSION),//邀请业务
	PRESENCE$_CHECK(VERSION.NEW_VERSION),//检查业务
    PRESENCE$_PLUGIN(VERSION.NEW_VERSION),
	
	IQ$_QUERY_ROOM_FROM_CONTACTS(VERSION.NEW_VERSION),
	IQ$_QUERY_ROOMINFO(VERSION.NEW_VERSION),
	IQ$_SAVE_ROOM_ID(VERSION.NEW_VERSION),
	IQ$_DELETE_ROOM_ID(VERSION.NEW_VERSION),
	IQ$_DESTROY_ROOM(VERSION.NEW_VERSION),//创建房间
	IQ$_DELETE_MEMBER(VERSION.NEW_VERSION),//删除成员
    IQ$_PLUGIN(VERSION.NEW_VERSION),
	
	;public int mVersion = VERSION.NEW_VERSION;
	Actions(int version){
		this.mVersion = version;
	}
}
