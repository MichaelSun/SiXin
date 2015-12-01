package com.renren.mobile.chat.actions.requests;

import com.common.network.AbstractNotSynRequest;
import com.common.network.DomainUrl;
import com.common.network.NetRequestListener;
import com.data.action.ACTION_TYPE;
import com.data.xmpp.Contact;
import com.data.xmpp.Query;
import com.data.xmpp.XMPPNode;
import com.data.xmpp.XMPPNodeFactory;
import com.data.xmpp.XMPPNodeRoot.ROOT_TYPE;
import com.renren.mobile.chat.actions.models.AllRoomInfoModel;

public class Iq_QueryRoomFromContact extends AbstractNotSynRequest<AllRoomInfoModel>{

	private long mFromId;
	public Iq_QueryRoomFromContact(long fromId){
		this.mFromId = fromId;
	}
	
	@Override
	public String getSendNetMessage() {
		XMPPNode root = XMPPNodeFactory.getInstance()
		.obtainRootNode(ACTION_TYPE.IQ.TypeName,
						this.mFromId+"@"+DomainUrl.SIXIN_DOMAIN, 
						DomainUrl.MUC_URL, 
						getId());
		root.addAttribute("type", ROOT_TYPE.GET);
		Query query = new Query();
		root.addChildNode(query);
		query.addAttribute("xmlns", DomainUrl.XMLN_USER);
		query.addChildNode(new Contact());
		return root.toXMLString();
	}
}
