package com.renren.mobile.chat.actions.message;

import com.common.actions.ActionNotMatchException;
import com.data.xmpp.Message;
import plugin.DBBasedPluginManager;
import plugin.base.Plugin;


/**
 * @author 宁长胜 2012-7-27
 *
 */
public class PluginRoute extends Action_Message{

	Plugin plugin;
	DBBasedPluginManager pluginManager;

	@Override
	public void processAction(Message node, long id) {
		pluginManager = new DBBasedPluginManager();
		String namespace = node.mXNode.mPrefix;
		int pluginId = pluginManager.getPluginIdWithNamespace(namespace);
		plugin = pluginManager.getPlugin(pluginId, false);
//		TestPluginActivity.show(RenrenChatApplication.getCurrentActivity(), pluginId);
		//plugin.setMessage(node); 给插件传递消息，方法尚未实现，下一步进行实现
	}

	@Override
	public void checkActionType(Message node) throws ActionNotMatchException {
		//此检测条件与其他检测有冲突，需要和服务器协商后对此检测方法进行修正
		this.addCheckCase(node.mXNode.mPrefix!=null);
	}

}
