package com.renren.mobile.chat.actions.iq;

import java.util.List;

import com.common.actions.ActionNotMatchException;
import com.data.xmpp.Iq;
import com.data.xmpp.Item;
import com.data.xmpp.XMPPNodeRoot.ROOT_TYPE;
import com.renren.mobile.chat.actions.models.AllRoomInfoModel;
import com.renren.mobile.chat.actions.models.RoomInfoModelWarpper;

public class Query_RoomInfoFromContact extends Action_Iq {

	@Override
	public void processAction(Iq node, long id) {
		List<Item> items = node.mQueryNode.mContactNode.mItems;
		AllRoomInfoModel model = new AllRoomInfoModel();
		for(Item item:items){
			RoomInfoModelWarpper m = new RoomInfoModelWarpper();
			m.mRoomId = item.parseLong(item.mRoomId);
			m.mSubject = item.mRoomName;
			m.addMembers(item.mItems);
			model.mRooms.add(m);
		}
		this.onSuccessCallback(id, model);
	}

	@Override
	public void checkActionType(Iq node) throws ActionNotMatchException {
		this.addCheckCase(node.mType.equals(ROOT_TYPE.RESULT));
		this.addCheckCase(node.mQueryNode.mContactNode!=null);
	}

}
