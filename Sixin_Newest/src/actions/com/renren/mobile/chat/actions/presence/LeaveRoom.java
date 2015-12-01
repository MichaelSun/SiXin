package com.renren.mobile.chat.actions.presence;

import android.util.Log;

import com.common.utils.Methods;
import com.data.xmpp.Presence;
import com.data.xmpp.XMPPNodeRoot.ROOT_TYPE;
import com.renren.mobile.chat.actions.ActionDispatcher;

public class LeaveRoom extends Action_Presence {

	@Override
	public void processAction(Presence node,long id) {
		Log.v("aac", "LeaveRoom");
		ActionDispatcher.CALLBACK.onRecive_leave_room(node.getFromId(), Methods.htmlDecoder(node.mXNode.mSubjectNode.mValue),node.mXNode.getVersion(), node.mXNode.mItemNode.get(0).getJid());
	}

	@Override
	public void checkActionType(Presence node) {
		this.addCheckCase(node.mType.equals(ROOT_TYPE.UNAVAILABLE))
		.addCheckCase(node.mXNode.mItemNode.size()==1)
		.addCheckCase(node.mXNode.mItemNode.get(0).mAffiliation.equals("none"))
		.addCheckCase(node.mXNode.mItemNode.get(0).mActor==null);
	}

}
