package com.renren.mobile.chat.actions.requests;

import com.common.network.AbstractNotSynRequest;
import com.common.network.DomainUrl;
import com.common.network.NULL;
import com.common.network.NetRequestListener;
import com.data.action.ACTION_TYPE;
import com.data.xmpp.XMPPNode;
import com.data.xmpp.XMPPNodeFactory;
import com.data.xmpp.XMPPNodeRoot.ROOT_TYPE;

public class Presence_LeaveRoom extends AbstractNotSynRequest<NULL>{

	private long mFromId;
	private long mRoomId;
	public Presence_LeaveRoom(long fromId,long roomId){
		this.mFromId = fromId;
		this.mRoomId = roomId;
	}
	
	@Override
	public String getSendNetMessage() {
		XMPPNode root = XMPPNodeFactory.getInstance()
		.obtainRootNode(ACTION_TYPE.PRESENCE.TypeName,
						this.mFromId+"@"+DomainUrl.SIXIN_DOMAIN, 
						this.mRoomId+"@"+DomainUrl.MUC_URL+"/"+this.mFromId, 
						getId());
		root.addAttribute("type", ROOT_TYPE.UNAVAILABLE);
		return root.toXMLString();
	}

}
