package com.common.network.requests;

import android.text.TextUtils;
import android.util.Log;

import com.common.manager.LoginManager;
import com.common.manager.MessageManager.OnSendTextListener;
import com.common.messagecenter.base.Utils;
import com.common.network.AbstractSynRequest;
import com.common.network.DomainUrl;
import com.common.network.NetRequestListener;
import com.data.action.ACTION_TYPE;
import com.data.xmpp.XMPPNode;
import com.data.xmpp.XMPPNodeFactory;
import com.data.xmpp.XMPPNodeRoot;

/**
 * @author dingwei.chen
 * @说明 单聊请求
 * */
public class Message_SendSynMessage extends AbstractSynRequest{

	
	private OnSendTextListener mMessage = null;
	private long mFromId = -1L;
	private long mToId = -1L;
	private String mToDomain = null;
	private String mFromDomain = null;
	
	public Message_SendSynMessage(long fromId,long toId, String domain, OnSendTextListener message){
		this(fromId,toId,domain, null, message);
	}
	
	public Message_SendSynMessage(long fromId,long toId, String domain, String fromDomain, OnSendTextListener message){
		this.mFromId = fromId;
		this.mToId = toId;
		mMessage = message;
		mToDomain = domain;
		mFromDomain = fromDomain;
	}
	
	public Message_SendSynMessage(long fromId,long toId,OnSendTextListener message){
		this(fromId,toId,null, message);
	}
	
	@Override
	public void onNetError(int errorCode,String errorMsg) {
		mMessage.onSendTextError();
	}

	@Override
	public void onNetSuccess() {
		mMessage.onSendTextSuccess();
	}

	@Override
	public String getSendNetMessage() {
		XMPPNodeRoot root = XMPPNodeFactory.getInstance().obtainRootNode(ACTION_TYPE.MESSAGE.TypeName,
				this.mFromId+"@"+(TextUtils.isEmpty(mFromDomain) ? LoginManager.getInstance().getLoginInfo().mDomainName : mFromDomain), 
				this.mToId+"@"+(TextUtils.isEmpty(mToDomain) ? DomainUrl.SIXIN_DOMAIN : mToDomain),
				getId());
		root.addAttribute("type", XMPPNodeRoot.ROOT_TYPE.CHAT);
//		X xnode = new X();
//		xnode.addAttribute("name", "xxx");
//		root.addChildNode(xnode);
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

}
