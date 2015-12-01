package com.renren.mobile.chat.actions.plugin;

import android.text.TextUtils;
import com.common.actions.ActionNotMatchException;
import com.common.messagecenter.base.Utils;
import com.core.util.Base64;
import com.data.xmpp.Iq;
import com.renren.mobile.chat.actions.iq.Action_Iq;
import plugin.DBBasedPluginManager;

/**
 * Created with IntelliJ IDEA.
 * User: xiaobo.yuan
 * Date: 9/28/12
 * Time: 1:03 AM
 */
public class PluginIq extends Action_Iq{
    @Override
    public void processAction(Iq node, long id) {
        String str = new String(Base64.decode(node.mZNode.mValue));
		String namespace = node.mZNode.mXmlns;
		Utils.l("namespace=" + namespace);
		Utils.l("str=" + str);
        DBBasedPluginManager manager = new DBBasedPluginManager();
        manager.routeMessageWithNamespace(namespace, node.getFromId(), node.mZNode.mValue);
    }

    @Override
    public void checkActionType(Iq node) throws ActionNotMatchException {
        addCheckCase(node.mZNode != null).
		addCheckCase(!TextUtils.isEmpty(node.mZNode.mXmlns)).
		addCheckCase(!"http://recommendation.sixin.com".equals(node.mZNode.mXmlns));
    }
}
