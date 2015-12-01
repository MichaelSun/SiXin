package com.renren.mobile.chat.actions.requests;

import com.common.network.AbstractNotSynRequest;
import com.common.network.DomainUrl;
import com.common.network.NetRequestListener;
import com.data.action.ACTION_TYPE;
import com.data.xmpp.Invite;
import com.data.xmpp.X;
import com.data.xmpp.XMPPNode;
import com.data.xmpp.XMPPNodeFactory;
import com.renren.mobile.chat.actions.models.CreateRoomModel;

/**
 * @author dingwei.chen1988@gmail.com
 * @
 * */
public class Presence_CreateRoom extends AbstractNotSynRequest<CreateRoomModel>{

	Iterable<Long> mInviteIds = null;
	long mFromId = -1;
	
	public Presence_CreateRoom(long fromId,Iterable<Long> inviteIds){
		this.mFromId = fromId;
		mInviteIds = inviteIds;
	}
	

	@Override
	public String getSendNetMessage() {
		XMPPNode root = XMPPNodeFactory.getInstance()
							.obtainRootNode(ACTION_TYPE.PRESENCE.TypeName,
											this.mFromId+"@"+DomainUrl.SIXIN_DOMAIN, 
											DomainUrl.MUC_URL, 
											getId());
		X x = new X();
		x.addAttribute("xmlns", DomainUrl.XMLN);
		root.addChildNode(x);
		if(mInviteIds!=null){
			for(Long id :mInviteIds){
				Invite invite = new Invite();
				invite.addAttribute("to", id);
				x.addChildNode(invite);
			}
		}
		return root.toXMLString();
	}
}
