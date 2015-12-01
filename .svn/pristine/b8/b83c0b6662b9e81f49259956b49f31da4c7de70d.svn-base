package com.common.actions;

import com.data.xmpp.XMPPNodeRoot;

/**
 * @author dingwei.chen1988@gmail.com
 * @说明 错误回调业务
 * */
public class Action_Error extends Action{

	@Override
	public void processAction(XMPPNodeRoot node, long id) {
		int code =  0;
		try {
			code = Integer.parseInt(node.mErrorNode.mCode);
		} catch (Exception e) {}
		this.onErrorCallback(id,code,node.mErrorNode.mMsg);
	}

	@Override
	public void checkActionType(XMPPNodeRoot node) throws ActionNotMatchException {
		this.addCheckCase(node.mType!=null)
		.addCheckCase(node.mType.equals("error")||node.mErrorNode!=null);
	}

}
