package com.renren.mobile.chat.actions.presence;

import com.common.manager.LoginManager;
import com.data.xmpp.Item;
import com.data.xmpp.Item.AFFILIATION;
import com.data.xmpp.Presence;
import com.renren.mobile.chat.actions.ActionDispatcher;
import com.renren.mobile.chat.actions.models.NotifyInviteModel;
import com.renren.mobile.chat.actions.models.RoomInfoModelWarpper;
import com.renren.mobile.chat.ui.contact.ContactModel;

/**
 * @author dingwei.chen1988@gmail.com
 * 加入房间
 * */
public class PresenceRoom extends Action_Presence{

	@Override
	public void processAction(Presence node,long id) {
		RoomInfoModelWarpper roomInfo = new RoomInfoModelWarpper();
		roomInfo.mRoomId = node.getFromId();
		roomInfo.mSubject = node.mXNode.mSubjectNode.mValue;
		roomInfo.mVersion = node.mXNode.getVersion();
		roomInfo.mLocalUserId = LoginManager.getInstance().getLoginInfo().mUserId;
		for(Item item:node.mXNode.mItemNode){
			ContactModel contactModel = new ContactModel();
			contactModel.mUserId= item.getJid();
			contactModel.mSmallHeadUrl = node.mXNode.mPrefix+item.mHeadUrl;
			contactModel.mContactName = item.mName;
			if(contactModel.mUserId != LoginManager.getInstance().getLoginInfo().mUserId){
				roomInfo.addMember(contactModel);
			}
			if(item.getAffiliaction()==AFFILIATION.owner.Value){
				roomInfo.mOwner = contactModel.mUserId;
			}
		}
		roomInfo.generateIds();
		if(node.mXNode.mInvitesNode!=null){
			NotifyInviteModel notifyInviteModel = new NotifyInviteModel();
			notifyInviteModel.parse(node.mXNode);
			if(node.mXNode.mInvitesNode.mToType.equals("invitee")){
				RoomInfoModelWarpper roomInfoModelWarpper = new RoomInfoModelWarpper();
				roomInfoModelWarpper.mRoomId = node.getFromId();
				roomInfoModelWarpper.parse(node.mXNode);
				ActionDispatcher.CALLBACK.onRecive_invite_to_new_member(roomInfoModelWarpper );
			}else if(node.mXNode.mInvitesNode.mToType.equals("existing")){
				ActionDispatcher.CALLBACK.onRecive_invite_to_old_member(roomInfo.mRoomId, notifyInviteModel );
			}
			
			
		}else{
			ActionDispatcher.CALLBACK.onRecive_create_room(roomInfo);	
		}
//		RoomDAO dao = DAOFactoryImpl.getInstance().buildDAO(RoomDAO.class);
//		dao.insert_RoomInfo(roomInfo);
//		RoomInfoModelWarpper r = dao.query_RoomInfoFromDB(roomInfo.mRoomId);
//		SystemUtil.log("cdw", "query = "+r);
		
	}

	@Override
	public void checkActionType(Presence node) {
		this.addCheckCase(node.mXNode.mPrefix!=null)
		.addCheckCase(node.mXNode.mItemNode.size()>0);
	}

}
