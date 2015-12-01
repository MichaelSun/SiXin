package com.renren.mobile.chat.actions.requests;

import com.common.network.AbstractNotSynRequest;
import com.common.network.DomainUrl;
import com.common.network.NULL;
import com.common.network.NetRequestListener;
import com.common.utils.Methods;
import com.data.action.ACTION_TYPE;
import com.data.xmpp.Subject;
import com.data.xmpp.XMPPNodeFactory;
import com.data.xmpp.XMPPNodeRoot;

public class Message_UpdateRoomSubject extends AbstractNotSynRequest<NULL>{

	public long mFromId;
	public long mRoomId;
	public String mNewSubjectName;
	public Message_UpdateRoomSubject(long fromid,long roomid,String newSubjectName){
		this.mFromId = fromid;
		this.mRoomId = roomid;
		this.mNewSubjectName = newSubjectName;
	}
	
	@Override
	public String getSendNetMessage() {
		XMPPNodeRoot root = XMPPNodeFactory.getInstance().obtainRootNode(ACTION_TYPE.MESSAGE.TypeName,
				this.mFromId+"@"+DomainUrl.SIXIN_DOMAIN, 
				mRoomId+"@"+DomainUrl.MUC_URL, 
				getId());
		Subject subject = new Subject();
		subject.mValue = Methods.htmlEncoder(mNewSubjectName);
		root.addChildNode(subject);
		return root.toXMLString();
	}

}
