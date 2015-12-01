package com.renren.mobile.chat.actions.presence;

import com.common.utils.Methods;
import com.data.xmpp.Presence;
import com.renren.mobile.chat.actions.models.CreateRoomModel;

public class CreateRoom extends Action_Presence {

	@Override
	public void processAction(Presence node,long id) {
		CreateRoomModel model = new CreateRoomModel();
		model.mRoomId = node.getFromId();
		model.mSubject = Methods.htmlDecoder(node.mXNode.mSubjectNode.mValue);
		model.mVersion = node.mXNode.getVersion();
		this.onSuccessCallback(id, model);
	}

	@Override
	public void checkActionType(Presence node) throws NullPointerException {
		this.addCheckCase(node.mId!=null)
			.addCheckCase(node.mXNode.mSubjectNode.mValue!=null);
	}

}
