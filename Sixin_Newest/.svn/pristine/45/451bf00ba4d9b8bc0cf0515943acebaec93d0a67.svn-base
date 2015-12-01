package com.renren.mobile.chat.actions.requests;

import com.common.network.AbstractNotSynRequest;
import com.common.network.DomainUrl;
import com.common.network.NULL;
import com.common.network.NetRequestListener;
import com.data.action.ACTION_TYPE;
import com.data.xmpp.Contact;
import com.data.xmpp.Query;
import com.data.xmpp.XMPPNode;
import com.data.xmpp.XMPPNodeFactory;
import com.data.xmpp.XMPPNodeRoot.ROOT_TYPE;

/**
 * @author dingwei.chen
 * @说明 删除房间ID
 * */
public class Iq_DeleteRoomId extends AbstractNotSynRequest<NULL>{
	
	long mRoomId;
	long mFromId;
	public Iq_DeleteRoomId(long fromId,long roomId){
		mFromId = fromId;
		mRoomId = roomId;
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
		query.addAttribute("xmlns", DomainUrl.XMLN_USER);
		root.addChildNode(query);
		Contact contact = new Contact();
		contact.addAttribute("type", "delete");
		query.addChildNode(contact);
		return root.toXMLString();
	}

}
