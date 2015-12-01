package com.renren.mobile.chat.actions.presence;

import com.data.xmpp.Check;
import com.data.xmpp.Presence;
import com.renren.mobile.chat.actions.models.CheckRoomInfoModel;

/**
 * @author dingwei.chen1988@gmail.com
 * 加入房间
 * */
public class CheckRoomInfo extends Action_Presence{

	@Override
	public void processAction(Presence node,long id) {
//		CALLBACK.onRecive_create_room(roomInfo);	
//		RoomDAO dao = DAOFactoryImpl.getInstance().buildDAO(RoomDAO.class);
//		dao.insert_RoomInfo(roomInfo);
//		RoomInfoModelWarpper r = dao.query_RoomInfoFromDB(roomInfo.mRoomId);
//		SystemUtil.log("cdw", "query = "+r);
		Check check;
		CheckRoomInfoModel checkRoomInfoModel = new CheckRoomInfoModel();
		if(node.mXNode!=null && node.mXNode.mCheckNodes!=null && node.mXNode.mCheckNodes.size()>0){
			int size = node.mXNode.mCheckNodes.size();
			for(int i=0;i<size;i++){
				check = node.mXNode.mCheckNodes.get(i);
//				CALLBACK.onRecive_check_room(check.getRoomId(),check.getUpdate(),check.getIsMember());	
				checkRoomInfoModel.addCheckInfo(check);
			}
			this.onSuccessCallback(id, checkRoomInfoModel);
		}
	}

	@Override
	public void checkActionType(Presence node) {
		this.addCheckCase(node.mId!=null)
		.addCheckCase(node.mXNode.mCheckNodes.size()>0);
	}

}
