package com.renren.mobile.chat.actions.message;

import com.common.actions.ActionNotMatchException;
import com.common.messagecenter.base.IGetConnectionState;
import com.common.messagecenter.base.Utils;
import com.data.xmpp.Message;

public class OfflineMessage extends Action_Message {
	
	public static IGetConnectionState connectionState = null;
	public static boolean sIsRecvOfflineMessage = false;

	@Override
	public void processAction(Message node, long id) {
		Utils.l("................");
		sIsRecvOfflineMessage = true;
		if(connectionState != null){
			connectionState.onRecvOffLineMessage();
		}
	} 

	@Override
	public void checkActionType(Message node) throws ActionNotMatchException {
		addCheckCase("EOOM".equals(node.mType));
	}

}
