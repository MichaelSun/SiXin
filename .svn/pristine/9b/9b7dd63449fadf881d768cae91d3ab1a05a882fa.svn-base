package com.common.actions;

import com.data.xmpp.XMPPNodeRoot;

/**
 * @author dingwei.chen1988@gmail.com
 * @说明 成功回调:不在意回执
 * */
public class Action_SendSuccess extends Action{

	@Override
	public void processAction(XMPPNodeRoot node, long id) {
//		this.onSuccessCallback(key, null);
		this.onSuccessCallback(id);
	}

	@Override
	public void checkActionType(XMPPNodeRoot node) throws ActionNotMatchException {
		this.addCheckCase(node.mId!=null);
	}

}
