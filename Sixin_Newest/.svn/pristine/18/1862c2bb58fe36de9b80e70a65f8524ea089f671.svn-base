package com.renren.mobile.chat.actions.message;

import com.common.actions.ActionNotMatchException;
import com.common.network.DomainUrl;
import com.common.network.NetRequestListener;
import com.data.xmpp.Presence;
import com.renren.mobile.chat.actions.models.RoomInfoModelWarpper;
import com.renren.mobile.chat.actions.presence.Action_Presence;

/**
 * @author dingwei.chen1988@gmail.com
 * @说明 被邀请进入房间
 * */
public class InviteToRoom extends Action_Presence{

	@Override
	public void processAction(Presence node, long id) {
		RoomInfoModelWarpper model = new RoomInfoModelWarpper();
		this.onSuccessCallback(id, model);
	}

	@Override
	public void checkActionType(Presence node) throws ActionNotMatchException {
		this.addCheckCase(node.mXNode.mPrefix!=null)
			.addCheckCase(node.mXNode.getVersion()!=-1)
			.addCheckCase(node.mXNode.mXmlns.equals(DomainUrl.XMLN_USER))
			.addCheckCase(node.mXNode.mItemNode!=null);
		
	}

}
