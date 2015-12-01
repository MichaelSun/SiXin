package com.renren.mobile.chat;

import java.util.List;

import com.renren.mobile.chat.actions.models.NotifyDeleteModel;
import com.renren.mobile.chat.actions.models.NotifyInviteModel;
import com.renren.mobile.chat.actions.models.RoomInfoModelWarpper;
import com.renren.mobile.chat.base.model.StateMessageModel;
import com.renren.mobile.chat.model.warpper.ChatMessageWarpper;

/**
 * @author dingwei.chen
 * @说明 轮询数据回调
 * */
public interface OnPollDataReciveCallback {
	public void onRecive(List<ChatMessageWarpper> messages);
	public void onRecive(StateMessageModel stateModel);
	public void onRecive(long[] feeds);
	
	//创建room，发送给被添加的用户
	public void onRecive_create_room(RoomInfoModelWarpper roomInfoModel);
	//邀请用户，发送给原有成员
	public void onRecive_invite_to_old_member(long roomId,NotifyInviteModel notifyInviteModel);
	//邀请用户，发送给新成员
	public void onRecive_invite_to_new_member(RoomInfoModelWarpper roomInfoModelWarpper);
	//删除用户，发送给被删除的用户
	public void onRecive_delete_to_member(long roomId);
	//删除用户，发送给群内的其他用户
	public void onRecive_delete_to_other(long roomId,NotifyDeleteModel notifyDeleteModel);
	//离开房间
	public void onRecive_leave_room(long roomId,String subject,int version,long userId);
	//销毁房间
	public void onRecive_destory_room(long roomId);
	
	public void onRecive_update_subject(long roomId,int version,String subject);
}
