package com.renren.mobile.chat.actions.requests;

import java.util.List;

import com.common.network.AbstractNotSynRequest;
import com.common.network.DomainUrl;
import com.common.network.NULL;
import com.common.network.NetRequestListener;
import com.data.action.ACTION_TYPE;
import com.data.xmpp.Invite;
import com.data.xmpp.X;
import com.data.xmpp.XMPPNodeFactory;
import com.data.xmpp.XMPPNodeRoot;

/**
 * @author dingwei.chen1988@gmail.com
 * @邀请请求
 * */
public class Message_Invite extends AbstractNotSynRequest<NULL>{

	List<Long> mInvites = null;
	long mRoomId = -1;
	long mFromId = -1;
	public Message_Invite(long fromId,long roomId,List<Long> invites){
		this.mFromId = fromId;
		this.mRoomId = roomId;
		mInvites = invites;
	}
	
	@Override
	public void onSuccessRecive(NULL data) {}

	@Override
	public String getSendNetMessage() {
		XMPPNodeRoot root = XMPPNodeFactory.getInstance().obtainRootNode(ACTION_TYPE.MESSAGE.TypeName,
										this.mFromId+"@"+DomainUrl.SIXIN_DOMAIN, 
										mRoomId+"@"+DomainUrl.MUC_URL, 
										getId());
		X x = new X();
		x.addAttribute("xmlns", DomainUrl.XMLN_USER);
		root.addChildNode(x);
		for(Long id:mInvites){
			Invite invite = new Invite();
			invite.addAttribute("to", id);
			x.addChildNode(invite);
		}
		return root.toXMLString();
	}
	
}
