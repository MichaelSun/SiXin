package com.renren.mobile.chat.actions.presence;

import com.common.actions.Action;
import com.common.actions.ActionNotMatchException;
import com.data.xmpp.Presence;
import com.data.xmpp.XMPPNodeRoot;

public abstract class Action_Presence extends Action{

	@Override
	public void processAction(XMPPNodeRoot node,long id) {
		this.processAction((Presence)node,id);
	}
	@Override
	public void checkActionType(XMPPNodeRoot node) throws ActionNotMatchException {
		this.checkActionType((Presence)node);
	};
	public abstract void processAction(Presence node,long id) ;
	public abstract void checkActionType(Presence node) throws ActionNotMatchException ;

}
