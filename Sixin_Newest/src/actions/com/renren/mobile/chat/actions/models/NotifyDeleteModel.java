package com.renren.mobile.chat.actions.models;

import java.util.ArrayList;

import com.common.utils.Methods;
import com.data.xmpp.Item;
import com.data.xmpp.X;

public class NotifyDeleteModel {
	public int mVersion;
	public long mRoomid;
	public String mSubject;
	public ArrayList<Long> mIdSet = new ArrayList<Long>();
	
	
	public void parse(X node){
		this.mVersion = node.getVersion();
		this.mSubject = Methods.htmlDecoder(node.mSubjectNode.mValue);
		for (Item item : node.mItemNode) {
			if(item.getJid()!= -1){
				this.mIdSet.add(item.getJid());
			}else if(item.getNickId()!= -1){
				this.mIdSet.add(item.getNickId());
			}
			
		}
	}
}
