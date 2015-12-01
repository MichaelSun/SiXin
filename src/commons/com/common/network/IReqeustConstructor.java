package com.common.network;

import com.common.manager.MessageManager.OnSendTextListener;
import com.common.network.requests.Message_SendGroupChat;
import com.common.network.requests.Message_SendSynMessage;
import com.data.action.Actions;

public interface IReqeustConstructor {

	@Request(action=Actions.MESSAGE$_SINGLE_CHAT,request=Message_SendSynMessage.class)	
	AbstractSynRequest sendSynMessage(long fromId,long roomId,OnSendTextListener message);
	
	@Request(action=Actions.MESSAGE$_SINGLE_CHAT,request=Message_SendSynMessage.class)	
	AbstractSynRequest sendSynMessage(long fromId,long roomId, String domain, OnSendTextListener message);
	
	@Request(action=Actions.MESSAGE$_SINGLE_CHAT,request=Message_SendSynMessage.class)	
	AbstractSynRequest sendSynMessage(long fromId,long roomId, String toDomain, String fromDomain, OnSendTextListener message);

	@Request(action=Actions.MESSAGE$_GROUP_CHAT,request=Message_SendGroupChat.class)	
	AbstractNotSynRequest<NULL> sendGroupMessage(String fromName,long fromId,long roomId,OnSendTextListener node);
	
	
}
