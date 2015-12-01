package com.renren.mobile.chat.actions.requests;

import java.util.List;

import com.common.network.AbstractNotSynRequest;
import com.common.network.DomainUrl;
import com.common.network.NetRequestListener;
import com.data.action.ACTION_TYPE;
import com.data.xmpp.Check;
import com.data.xmpp.X;
import com.data.xmpp.XMPPNodeFactory;
import com.data.xmpp.XMPPNodeRoot;
import com.renren.mobile.chat.actions.models.TimestempModel;

public class Presence_UploadTimestamp extends AbstractNotSynRequest<TimestempModel> {
	long mFromId ;
	List<Long> mCheckIds;
	List<Integer> mVersions;
	public Presence_UploadTimestamp(long fromId,List<Long> checkIds,List<Integer> versions){
		mFromId = fromId;
		mCheckIds = checkIds;
		mVersions = versions;
	}
	
	
	@Override
	public String getSendNetMessage() {
		XMPPNodeRoot root = XMPPNodeFactory.getInstance().obtainRootNode(ACTION_TYPE.PRESENCE.TypeName,
				this.mFromId+"@"+DomainUrl.SIXIN_DOMAIN, 
				DomainUrl.MUC_URL, 
				getId());
		X x = new X();
		root.addChildNode(x);
		x.addAttribute("xmlns", DomainUrl.XMLN);
		int index = 0 ;
		for(Long l:mCheckIds){
			Check check = new Check();
			check.addAttribute("to", l);
			check.addAttribute("version", mVersions.get(index++));
			x.addChildNode(check);
		}
		return root.toXMLString();
	}

}
