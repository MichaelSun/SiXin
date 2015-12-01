package com.renren.mobile.chat.actions.models;

import java.util.ArrayList;

import com.common.utils.Methods;
import com.data.xmpp.Item;
import com.data.xmpp.X;
import com.renren.mobile.chat.ui.contact.ContactModel;

public class NotifyInviteModel {
	public int mVersion;
	public long mRoomid;
	public String mSubject;
	public ArrayList<Long> mIdSet = new ArrayList<Long>();
	public ArrayList<ContactModel> mMembers = new ArrayList<ContactModel>();
	public String mPrefix;
	public void parse(X node){
		ContactModel contactModel;
		this.mVersion = node.getVersion();
		this.mSubject = Methods.htmlDecoder(node.mSubjectNode.mValue);
		this.mPrefix = node.mPrefix;
		for (Item item : node.mItemNode) {
			contactModel = this.parse(item);
			this.mIdSet.add(contactModel.mUserId);
			this.mMembers.add(contactModel);
		}
	}
	
	/**
	 * 将item节点解析成ContactModel
	 * **/
	public ContactModel parse(Item item) {
		ContactModel contactModel = new ContactModel();
		contactModel.mUserId = item.getJid();
		contactModel.mSmallHeadUrl = mPrefix+item.mHeadUrl;
		contactModel.mContactName = item.mName;
		return contactModel;
	}

}
