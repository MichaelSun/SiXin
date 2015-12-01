package com.renren.mobile.chat.actions.presence;

import com.data.xmpp.Presence;
import com.data.xmpp.XMPPNodeRoot.ROOT_TYPE;
import com.renren.mobile.chat.actions.ActionDispatcher;
import com.renren.mobile.chat.actions.models.NotifyDeleteModel;

public class NofiyWhoDeletedFromRoom extends Action_Presence{

	@Override
	public void processAction(Presence node,long id) {
		NotifyDeleteModel notifyDeleteModel = new NotifyDeleteModel();
		notifyDeleteModel.parse(node.mXNode);
		// TODO Auto-generated method stub
		ActionDispatcher.CALLBACK.onRecive_delete_to_other(node.getFromId(), notifyDeleteModel );
	}

	@Override
	public void checkActionType(Presence node) {
		this.addCheckCase(node.mType.equals(ROOT_TYPE.UNAVAILABLE))
		.addCheckCase(node.mXNode.mItemNode.size()>0)
		.addCheckCase(node.mXNode.mItemNode.get(0).mActor==null)
		.addCheckCase(node.mXNode.mStatusNodes.size()==0)
		.addCheckCase(node.mXNode.mDestory==null);
	}

}
