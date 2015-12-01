package com.renren.mobile.chat.actions.requests;

import com.common.network.AbstractNotSynRequest;
import com.common.network.DomainUrl;
import com.common.network.NetRequestListener;
import com.data.action.ACTION_TYPE;
import com.data.xmpp.Item;
import com.data.xmpp.Item.AFFILIATION;
import com.data.xmpp.Query;
import com.data.xmpp.XMPPNode;
import com.data.xmpp.XMPPNodeFactory;
import com.data.xmpp.XMPPNodeRoot.ROOT_TYPE;
import com.renren.mobile.chat.actions.models.RoomInfoModelWarpper;

public class Iq_QueryRoomInfo extends AbstractNotSynRequest<RoomInfoModelWarpper>{
	long mRoomId = -1;
	long mFromId = -1;
	
	public Iq_QueryRoomInfo(long fromId,long roomId){
		this.mRoomId = roomId;
		this.mFromId = fromId;
	}
	@Override
	public String getSendNetMessage() {
		XMPPNode root = XMPPNodeFactory.getInstance()
		.obtainRootNode(ACTION_TYPE.IQ.TypeName,
						this.mFromId+"@"+DomainUrl.SIXIN_DOMAIN, 
						this.mRoomId+"@"+DomainUrl.MUC_URL+"/"+this.mFromId, 
						getId());
		root.addAttribute("type", ROOT_TYPE.GET);
		Query query = new Query();
		query.addAttribute("xmlns", DomainUrl.XMLN_USER);
		root.addChildNode(query);
		Item item = new Item();
		item.addAttribute("affiliation", AFFILIATION.member);
		return root.toXMLString();
	}

}
