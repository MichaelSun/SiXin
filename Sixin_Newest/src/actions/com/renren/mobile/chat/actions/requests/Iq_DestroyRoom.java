package com.renren.mobile.chat.actions.requests;

import com.common.network.AbstractNotSynRequest;
import com.common.network.DomainUrl;
import com.common.network.NULL;
import com.common.network.NetRequestListener;
import com.data.action.ACTION_TYPE;
import com.data.xmpp.Destroy;
import com.data.xmpp.Query;
import com.data.xmpp.XMPPNode;
import com.data.xmpp.XMPPNodeFactory;
import com.data.xmpp.XMPPNodeRoot.ROOT_TYPE;

public class Iq_DestroyRoom extends AbstractNotSynRequest<NULL>{

	long mRoomId = -1;
	long mFromId = -1;
	
	public Iq_DestroyRoom(long fromId,long roomId){
		this.mRoomId = roomId;
		this.mFromId = fromId;
	}
	
	@Override
	public String getSendNetMessage() {
		XMPPNode root = XMPPNodeFactory.getInstance()
		.obtainRootNode(ACTION_TYPE.IQ.TypeName,
						this.mFromId+"@"+DomainUrl.SIXIN_DOMAIN, 
						this.mRoomId+"@"+DomainUrl.MUC_URL, 
						getId());
		root.addAttribute("type", ROOT_TYPE.SET);
		Query query = new Query();
		query.addAttribute("xmlns", DomainUrl.XMLN_OWNER);
		root.addChildNode(query);
		query.addChildNode(new Destroy());
		return root.toXMLString();
	}

}
