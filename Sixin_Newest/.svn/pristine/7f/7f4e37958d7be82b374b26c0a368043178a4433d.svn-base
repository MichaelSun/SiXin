package com.renren.mobile.chat.actions.message;

import android.text.TextUtils;

import com.common.actions.ActionNotMatchException;
import com.common.messagecenter.base.Utils;
import com.core.util.Base64;
import com.data.xmpp.Message;

public class RecommandAddMessage extends Action_Message {

	@Override
	public void processAction(Message node, long id) {
		Utils.l("");
		node.mZNode.mProfile.mValue = new String(Base64.decode(node.mZNode.mProfile.mValue));
	}

	@Override
	public void checkActionType(Message node) throws ActionNotMatchException {
		Utils.l("" + node.toXMLString());
		addCheckCase(node.mZNode != null).
		addCheckCase(!TextUtils.isEmpty(node.mZNode.mXmlns)).
		addCheckCase("http://recommendation.sixin.com".equals(node.mZNode.mXmlns)).
		addCheckCase(node.mZNode.mProfile != null).
		addCheckCase("add".equals(node.mZNode.mProfile.mType));
	}

}

