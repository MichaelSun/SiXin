package com.renren.mobile.chat.actions.message;

import android.text.TextUtils;

import com.common.actions.ActionNotMatchException;
import com.common.messagecenter.base.Utils;
import com.data.xmpp.Message;

public class RecommendationMessage extends Action_Message {

	@Override
	public void processAction(Message node, long id) {
		Utils.l("");
	}

	@Override
	public void checkActionType(Message node) throws ActionNotMatchException {
		Utils.l("" + node.toXMLString());
		addCheckCase(node.mZNode != null).
		addCheckCase(!TextUtils.isEmpty(node.mZNode.mXmlns)).
		addCheckCase("http://recommendation.sixin.com".equals(node.mZNode.mXmlns)).addCheckCase(node.mZNode.mProfile == null);
	}

}
