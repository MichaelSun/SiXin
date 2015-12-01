package com.renren.mobile.chat.actions.presence;

import com.data.xmpp.Presence;
import com.data.xmpp.XMPPNodeRoot.ROOT_TYPE;
import com.renren.mobile.chat.actions.ActionDispatcher;

public class DestroyRoom extends Action_Presence {

	@Override
	public void processAction(Presence node,long id) {
		// TODO Auto-generated method stub
		ActionDispatcher.CALLBACK.onRecive_destory_room(node.getFromId());
	}

	@Override
	public void checkActionType(Presence node) {
		this.addCheckCase(node.mType.equals(ROOT_TYPE.UNAVAILABLE))
		.addCheckCase(node.mXNode.mItemNode.size()==1)
		.addCheckCase(node.mXNode.mItemNode.get(0).mAffiliation.equals("none"))
		.addCheckCase(node.mXNode.mDestory!=null);
		
	}

}
