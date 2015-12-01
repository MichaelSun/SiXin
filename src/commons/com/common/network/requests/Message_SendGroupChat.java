package com.common.network.requests;

import com.common.manager.LoginManager;
import com.common.manager.MessageManager.OnSendTextListener;
import com.common.network.AbstractNotSynRequest;
import com.common.network.DomainUrl;
import com.common.network.NULL;
import com.common.network.NetRequestListener;
import com.core.util.CommonUtil;
import com.data.action.ACTION_TYPE;
import com.data.xmpp.XMPPNode;
import com.data.xmpp.XMPPNodeFactory;
import com.data.xmpp.XMPPNodeRoot;

/**
 * @author dingwei.chen1988@gmail.com
 * */
public class Message_SendGroupChat extends AbstractNotSynRequest<NULL>{
	
	private long mFromId = -1L;
	private long mRoomId = -1L;
	private String mFromName = null;
	private String mDomain = null;
	private OnSendTextListener mMessage = null;
	
	
	public Message_SendGroupChat(String fromName,long fromId,long roomId,OnSendTextListener listener){
		this(fromName, fromId, roomId, null, listener);
	}
	
	public Message_SendGroupChat(String fromName,long fromId,long roomId, String domain,OnSendTextListener listener){
		mFromName = fromName;
		this.mFromId = fromId;
		this.mRoomId = roomId;
		this.mMessage = listener;
		this.mDomain = domain;
	}
	
	@Override
	public String getSendNetMessage() {
		XMPPNodeRoot root = XMPPNodeFactory.getInstance().obtainRootNode(ACTION_TYPE.MESSAGE.TypeName,
				this.mFromId+"@"+ (mDomain == null ? LoginManager.getInstance().getLoginInfo().mDomainName : mDomain), 
				mRoomId+"@"+DomainUrl.MUC_URL, 
				getId());
		root.addAttribute("type", XMPPNodeRoot.ROOT_TYPE.GROUP_CHAT);
		root.addAttribute("fname", mFromName);
		if(mMessage.hasNewsFeed()){
			root.addAttribute("feed", "true");
		}
		if(mMessage.getNetPackage()!=null){
			for(XMPPNode node:mMessage.getNetPackage()){
				root.addChildNode(node);
			}
		}
		return root.toXMLString();
	}

	@Override
	public void onNetSuccess() {
		this.mMessage.onSendTextSuccess();
	}
	@Override
	public void onNetError(int errorCode,String errorMsg) {
		this.mMessage.onSendTextError();
		CommonUtil.toast(errorMsg);
	}
	
}
