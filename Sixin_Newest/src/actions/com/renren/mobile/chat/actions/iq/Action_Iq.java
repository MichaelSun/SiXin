package com.renren.mobile.chat.actions.iq;

import com.common.actions.Action;
import com.common.actions.ActionNotMatchException;
import com.data.xmpp.Iq;
import com.data.xmpp.XMPPNodeRoot;

public abstract class Action_Iq extends Action{

	@Override
	public void processAction(XMPPNodeRoot node,long id) {
		this.processAction((Iq)node,id);
	}
	@Override
	public void checkActionType(XMPPNodeRoot node) throws ActionNotMatchException {
		this.checkActionType((Iq)node);
	};
	
	public abstract void processAction(Iq node,long id);
	public abstract void checkActionType(Iq node)  throws ActionNotMatchException ;

}
