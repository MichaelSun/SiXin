package com.renren.mobile.chat.actions.iq;

import com.common.actions.ActionNotMatchException;
import com.data.xmpp.Iq;
import com.renren.mobile.chat.actions.models.RoomInfoModelWarpper;

public class Query_RoomInfo extends Action_Iq{

	@Override
	public void processAction(Iq node, long id) {
		// TODO Auto-generated method stub
		RoomInfoModelWarpper m = new RoomInfoModelWarpper();
		m.mRoomId = node.getFromId();
		m.mVersion = node.mQueryNode.getVersion();
		m.parse(node.mQueryNode);
		this.onSuccessCallback(id, m);
	}

	@Override
	public void checkActionType(Iq node) throws ActionNotMatchException {
		this.addCheckCase(node.mType.equals("result"))
		.addCheckCase(node.mQueryNode!=null)
		.addCheckCase(node.mQueryNode.mSubjectNode!=null);
	}


}
