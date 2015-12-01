package com.renren.mobile.chat.actions.requests;

import java.util.List;

import com.common.network.AbstractNotSynRequest;
import com.common.network.IReqeustConstructor;
import com.common.network.NULL;
import com.common.network.Request;
import com.data.action.Actions;
import com.renren.mobile.chat.actions.models.AllRoomInfoModel;
import com.renren.mobile.chat.actions.models.CheckRoomInfoModel;
import com.renren.mobile.chat.actions.models.CreateRoomModel;
import com.renren.mobile.chat.actions.models.RoomInfoModelWarpper;

/**
 * @author dingwei.chen1988@gmail.com
 * @说明 网络请求池接口 必须保证请求参数和构造器参数一致
 * */
public interface RequestConstructor extends IReqeustConstructor{
	
	@Request(action=Actions.PRESENCE$_CREATE_ROOM,request=Presence_CreateRoom.class)		
	AbstractNotSynRequest<CreateRoomModel> createRoom(long fromId,List<Long> inviteIds);
	
	@Request(action=Actions.PRESENCE$_INVITE,request=Message_Invite.class)	
	AbstractNotSynRequest<RoomInfoModelWarpper> invite(long fromId,long roomId,List<Long> invites);
	
	@Request(action=Actions.MESSAGE$_UPDATE_SUBJECT,request=Message_UpdateRoomSubject.class)	
	AbstractNotSynRequest<NULL> updateRoomSubject(long fromid,long roomid,String newSubjectName);
	
	@Request(action=Actions.IQ$_DELETE_MEMBER,request=Iq_DeleteMember.class)	
	AbstractNotSynRequest<NULL> deleteMember(long fromId,long roomId,List<Long> deleteIds);
	
	@Request(action=Actions.IQ$_DESTROY_ROOM,request=Iq_DestroyRoom.class)	
	AbstractNotSynRequest<NULL> destroyRoom(long fromId,long roomId);
	
	@Request(action=Actions.IQ$_QUERY_ROOM_FROM_CONTACTS,request=Iq_QueryRoomFromContact.class)	
	AbstractNotSynRequest<AllRoomInfoModel> queryRoomFromContacts(long fromId);
	
	@Request(action=Actions.IQ$_QUERY_ROOMINFO,request=Iq_QueryRoomInfo.class)	
	AbstractNotSynRequest<RoomInfoModelWarpper> queryRoomInfo(long fromId,long roomId);
	
	@Request(action=Actions.IQ$_SAVE_ROOM_ID,request=Iq_SaveRoomId.class)	
	AbstractNotSynRequest<NULL> saveRoomInfo(long fromId,long roomId);
	
	@Request(action=Actions.IQ$_DELETE_ROOM_ID,request=Iq_DeleteRoomId.class)	
	AbstractNotSynRequest<NULL> deleteRoomInfoFromContact(long fromId,long roomId);
	
	
	@Request(action=Actions.PRESENCE$_CREATE_ROOM,request=Presence_UploadTimestamp.class)	
	AbstractNotSynRequest<CheckRoomInfoModel> uploadTimestamp(long fromId,List<Long> checkIds,List<String> versions);
	
	@Request(action=Actions.PRESENCE$_CREATE_ROOM,request=Presence_LeaveRoom.class)	
	AbstractNotSynRequest<NULL> leaveRoom(long fromId,long roomId);
}
