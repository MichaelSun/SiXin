package com.renren.mobile.chat.actions.plugin;

import android.text.TextUtils;

import com.common.actions.ActionNotMatchException;
import com.common.messagecenter.base.Utils;
import com.core.util.Base64;
import com.data.xmpp.Message;
import com.renren.mobile.chat.actions.message.Action_Message;
import plugin.DBBasedPluginManager;


public class PluginMessage extends Action_Message {

	@Override
	public void processAction(Message node, long id) {
		String str = new String(Base64.decode(node.mZNode.mValue));
		String namespace = node.mZNode.mXmlns;
		Utils.l("namespace=" + namespace);
		Utils.l("str=" + str);
        DBBasedPluginManager manager = new DBBasedPluginManager();
        manager.routeMessageWithNamespace(namespace, node.getFromId(), node.mZNode.mValue);
	}

	@Override
	public void checkActionType(Message node) throws ActionNotMatchException {
		addCheckCase(node.mZNode != null).
		addCheckCase(!TextUtils.isEmpty(node.mZNode.mXmlns)).
		addCheckCase(!"http://recommendation.sixin.com".equals(node.mZNode.mXmlns));
	}

}
