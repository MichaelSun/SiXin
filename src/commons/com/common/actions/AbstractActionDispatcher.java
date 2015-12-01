package com.common.actions;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.data.action.ACTION_TYPE;
import com.data.action.Actions;
import com.data.action.VERSION;
import com.data.xmpp.XMPPNodeRoot;

public abstract class AbstractActionDispatcher {
	
	
	/*Message业务*/
	protected Map<Actions,Action> mActions_Message = new HashMap<Actions, Action>();
	/*Presence业务*/
	protected Map<Actions,Action> mActions_Presence = new HashMap<Actions, Action>();
	/*Iq业务*/
	protected Map<Actions,Action> mActions_Iq = new HashMap<Actions, Action>();
	protected Action mAction_Error = new Action_Error();
	protected Action mAction_Success = new Action_SendSuccess();
	
	
	/*获得业务处理池*/
	protected Map<Actions,Action> getActionsPool(ACTION_TYPE type){
		switch (type) {
			case MESSAGE:
				return mActions_Message;
			case PRESENCE:
				return mActions_Presence;
			default:
				return mActions_Iq;
		}
	}
	
	protected AbstractActionDispatcher addAction(ACTION_TYPE type,Actions actionName,Action action){
		this.getActionsPool(type).put(actionName, action);
		return this;
	}
	
	
	protected AbstractActionDispatcher(){
		this.initActions();
	}
	protected abstract void initActions();
//	private void initActions(){
//		this.initActions_Message();
//		this.initActions_Presence();
//		this.initActions_Iq();
//	}
////	
//	/*初始化message节点业务*/
//	private void initActions_Message(){
//		put(mActions_Message,Actions.MESSAGE$_SINGLE_CHAT, 			new PushSingleChat());
//		put(mActions_Message,Actions.MESSAGE$_ACTION, 				new ActionStateUpdate());
//		put(mActions_Message,Actions.MESSAGE$_PUSH_FEED, 			new PushFeed());
//		put(mActions_Message,Actions.MESSAGE$_GROUP_CHAT, 			new PushGroupMessage());
//		put(mActions_Message,Actions.MESSAGE$_PUSH_SOFTINFO,		new PushSoftInfoMessage());
//		put(mActions_Message,Actions.MESSAGE$_UPDATE_SUBJECT,		new Update_Subject_Message());
//	}
//	/*初始化presence节点业务*/
//	private void initActions_Presence(){
//		put(mActions_Presence,Actions.PRESENCE$_CREATE_ROOM, 		new CreateRoom());
//		put(mActions_Presence,Actions.PRESENCE$_PRESENCE_ROOM, 		new PresenceRoom());
//		put(mActions_Presence,Actions.PRESENCE$_DELETED_FROM_ROOM, 	new DeletedFromRoom());
//		put(mActions_Presence,Actions.PRESENCE$_NOTIFY_DELETE_ROOM, new NofiyWhoDeletedFromRoom());
//		put(mActions_Presence,Actions.PRESENCE$_DESTROY_ROOM, 		new DestroyRoom());
//		put(mActions_Presence,Actions.PRESENCE$_INVITE, 			new InviteToRoom());
//		put(mActions_Presence,Actions.PRESENCE$_CHECK, 				new CheckRoomInfo());
//	}
//	/*初始化iq节点业务*/
//	private void initActions_Iq(){
//		put(mActions_Iq,Actions.IQ$_QUERY_ROOM_FROM_CONTACTS,		new Query_RoomInfoFromContact());
//		put(mActions_Iq,Actions.IQ$_QUERY_ROOMINFO, 		 		new Query_RoomInfo());
//		
//	}
//	
	private void put(Map<Actions,Action> map,Actions action,Action actionModel){
		actionModel.setActions(action);
		map.put(action, actionModel);
	}
//	
	public synchronized void dispatchAction(XMPPNodeRoot root){
		Map<Actions,Action> pool = getActionsPool(root.getActionType());
		Collection<Action> set = pool.values();
		long id = -1;
		if(checkAction(mAction_Error, root)){
			id = root.getId();
			mAction_Error.processAction(root, id);
			return;
		}
		if(set!=null && set.size()>0){
			for(Action action:set){
				try {
					if(checkAction(action, root)){
						if(action.mAction.mVersion == VERSION.NEW_VERSION){
							id = root.getId();
						}
						action.beginAction();
						action.processAction(root,id);
						action.commitAction();
						action.onSuccessCallback(id);
						return;
					}
					
				} catch (Exception e) {}
			}
		}else{
		}
		if(checkAction(mAction_Success, root)){
			id = root.getId();
			mAction_Success.processAction(root, id);
			return;
		}
//		SystemUtil.log("cdw", "结束业务");
	}
	/*批处理业务*/
	public synchronized <T extends XMPPNodeRoot>  void batchDispatchAction(List<T> roots){
		if(roots.size()==0){
			return;
		}
		XMPPNodeRoot root0 = roots.get(0);
		Map<Actions,Action> pool = getActionsPool(root0.getActionType());
		Collection<Action> set = pool.values();
		Action a = null;
		for(Action action:set){
			if(checkAction(action, root0)){
				a = action;
				action.beginAction();
				for(XMPPNodeRoot r : roots){
					long id = -1;
					if(action.mAction.mVersion == VERSION.NEW_VERSION){
						id = r.getId();
					}
					action.processAction(r,id);
					
				}
				action.commitAction();
				break;
			}
		}
	}
	private boolean checkAction(Action action,XMPPNodeRoot node){
		try {
			action.checkActionType(node);
		} catch (Exception e) {
			return false;
		}
		return true;
	}
	
	
}
