package com.renren.mobile.chat.actions.requests;

import java.util.List;

import com.common.network.AbstractNotSynRequest;
import com.common.network.DomainUrl;
import com.common.network.NULL;
import com.common.network.NetRequestListener;
import com.data.action.ACTION_TYPE;
import com.data.xmpp.Item;
import com.data.xmpp.Item.AFFILIATION;
import com.data.xmpp.Query;
import com.data.xmpp.XMPPNode;
import com.data.xmpp.XMPPNodeFactory;
import com.data.xmpp.XMPPNodeRoot.ROOT_TYPE;

public class Iq_DeleteMember extends AbstractNotSynRequest<NULL> {

	private long mFromId = -1L;
	private long mRoomId = -1L;
	private List<Long> mDeleteIds;
	
	public Iq_DeleteMember(long fromId,long roomId,List<Long> deleteIds){
		mFromId = fromId;
		mRoomId = roomId;
		this.mDeleteIds = deleteIds;
	}
	
	
	@Override
	public String getSendNetMessage() {
		XMPPNode root = XMPPNodeFactory.getInstance()
		.obtainRootNode(ACTION_TYPE.IQ.TypeName,
						this.mFromId+"@"+DomainUrl.SIXIN_DOMAIN, 
						this.mRoomId+"@"+DomainUrl.MUC_URL, 
						getId());
		Query query = new Query();
		query.addAttribute("xmlns", DomainUrl.XMLN_OWNER);
		root.addChildNode(query);
		root.addAttribute("type", ROOT_TYPE.SET);
		for(Long id:mDeleteIds){
			Item item = new Item();
			item.addAttribute("nick", id);
			item.addAttribute("affiliation", AFFILIATION.outcast);
			query.addChildNode(item);
		}
		return root.toXMLString();
	}

}
