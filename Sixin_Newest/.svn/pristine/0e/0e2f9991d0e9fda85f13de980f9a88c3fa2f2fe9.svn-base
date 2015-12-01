package com.renren.mobile.chat.actions.message;

import com.common.actions.ActionNotMatchException;
import com.data.xmpp.Message;
import com.renren.mobile.chat.actions.ActionDispatcher;
import com.renren.mobile.chat.base.model.StateMessageModel;
/**
 * @author dingwei.chen1988@gmail.com
 * @说明 单人聊天业务
 * */
public class ActionStateUpdate extends Action_Message {

	@Override
	public void processAction(Message node,long id) {
		StateMessageModel stateMessage = new StateMessageModel(
				node.mFromName, 
				(int)node.getToId(),
				(int)node.getFromId(), 
				node.mBody.mAction);
//			C_ChatDataParser.getInstance().onReciveStateMessage(stateMessage);
			ActionDispatcher.CALLBACK.onRecive(stateMessage);
	}

	@Override
	public void checkActionType(Message node) throws ActionNotMatchException{
		this.addCheckCase(node.mBody.mType.equals("action"));
	}
	

}
