package com.renren.mobile.chat.actions.message;

import com.common.actions.Action;
import com.common.actions.ActionNotMatchException;
import com.data.xmpp.Message;
import com.data.xmpp.XMPPNodeRoot;

//
public abstract class Action_Message extends Action{
	@Override
	public void processAction(XMPPNodeRoot node,long id) {
		this.processAction((Message)node,id);
	}
	@Override
	public void checkActionType(XMPPNodeRoot node) throws ActionNotMatchException {
		 this.checkActionType((Message)node);
	};
	public abstract void processAction(Message node,long id);
	public abstract void checkActionType(Message node)  throws ActionNotMatchException ;
}
