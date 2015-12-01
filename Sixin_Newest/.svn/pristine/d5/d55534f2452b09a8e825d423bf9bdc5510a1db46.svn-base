package com.renren.mobile.chat.actions;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import com.common.actions.Action;
import com.core.util.CommonUtil;
import com.data.action.Actions;
import com.data.action.VERSION;
import com.data.xmpp.XMPPNodeRoot;
import com.renren.mobile.chat.C_PollDataCallbackImpl;
import com.renren.mobile.chat.OnPollDataReciveCallback;
import com.renren.mobile.chat.actions.iq.Query_RoomInfo;
import com.renren.mobile.chat.actions.iq.Query_RoomInfoFromContact;
import com.renren.mobile.chat.actions.message.ActionStateUpdate;
import com.renren.mobile.chat.actions.message.InviteToRoom;
import com.renren.mobile.chat.actions.message.OfflineMessage;
import com.renren.mobile.chat.actions.message.PushFeed;
import com.renren.mobile.chat.actions.message.PushGroupMessage;
import com.renren.mobile.chat.actions.message.PushSingleChat;
import com.renren.mobile.chat.actions.message.PushSoftInfoMessage;
import com.renren.mobile.chat.actions.message.Update_Subject_Message;
import com.renren.mobile.chat.actions.plugin.PluginIq;
import com.renren.mobile.chat.actions.plugin.PluginMessage;
import com.renren.mobile.chat.actions.plugin.PluginPresence;
import com.renren.mobile.chat.actions.presence.CheckRoomInfo;
import com.renren.mobile.chat.actions.presence.CreateRoom;
import com.renren.mobile.chat.actions.presence.DeletedFromRoom;
import com.renren.mobile.chat.actions.presence.DestroyRoom;
import com.renren.mobile.chat.actions.presence.NofiyWhoDeletedFromRoom;
import com.renren.mobile.chat.actions.presence.PresenceRoom;
import com.renren.mobile.chat.actions.message.PluginRoute;
import com.renren.mobile.chat.base.util.SystemUtil;
import com.renren.mobile.chat.ui.contact.ContactMessage;
/**
 * @author dingwei.chen1988@gmail.com
 * @说明 业务分发器
 * @注：
 * 	         业务分为3个大线,11个中线,30个小线;大线以节点类型划分,中线以业务用例划分,小线以反馈类型划分
 * @大线:大线作为业务分类作为业务缓冲池,存于变量：
 * 		{@link AbstractActionDispatcher#mActions_Message}   Message节点业务
 * 		{@link AbstractActionDispatcher#mActions_Presence}  Presence节点业务
 * 		{@link AbstractActionDispatcher#mActions_Iq} 		Iq节点业务
 * */
import com.renren.mobile.chat.R;
import com.renren.mobile.chat.RenrenChatApplication;
public final class ActionDispatcher extends com.common.actions.AbstractActionDispatcher {
	private static ActionDispatcher sDispatcher = new ActionDispatcher();
	public static OnPollDataReciveCallback CALLBACK = new C_PollDataCallbackImpl();
	private ActionDispatcher(){}
	public static ActionDispatcher getInstance(){
		return sDispatcher;
	}
	/*初始化message节点业务*/
	private void initActions_Message(){
		/*将插件路由的Action进行注册*/
		put(mActions_Message,Actions.MESSAGE$_PLUGIN_ROUTE,	new PluginRoute());
		put(mActions_Message,Actions.MESSAGE$_SINGLE_CHAT, 	new PushSingleChat());
		put(mActions_Message,Actions.MESSAGE$_ACTION, 		new ActionStateUpdate());
		put(mActions_Message,Actions.MESSAGE$_PUSH_FEED, 	new PushFeed());
		put(mActions_Message,Actions.MESSAGE$_GROUP_CHAT, 	new PushGroupMessage());
		put(mActions_Message,Actions.MESSAGE$_PUSH_SOFTINFO,new PushSoftInfoMessage());
		put(mActions_Message,Actions.MESSAGE$_UPDATE_SUBJECT,new Update_Subject_Message());
		put(mActions_Message,Actions.MESSAGE$_OFFLINE, 		new OfflineMessage());
		put(mActions_Message,Actions.MESSAGE$_RECOMMAND,	new ContactMessage());
		put(mActions_Message,Actions.MESSAGE$_PLUGIN,		new PluginMessage());

	}
	/*初始化presence节点业务*/
	private void initActions_Presence(){
		put(mActions_Presence,Actions.PRESENCE$_CREATE_ROOM, 		new CreateRoom());
		put(mActions_Presence,Actions.PRESENCE$_PRESENCE_ROOM, 		new PresenceRoom());
		put(mActions_Presence,Actions.PRESENCE$_DELETED_FROM_ROOM, 	new DeletedFromRoom());
		put(mActions_Presence,Actions.PRESENCE$_NOTIFY_DELETE_ROOM, new NofiyWhoDeletedFromRoom());
		put(mActions_Presence,Actions.PRESENCE$_DESTROY_ROOM, 		new DestroyRoom());
		put(mActions_Presence,Actions.PRESENCE$_INVITE, 			new InviteToRoom());
		put(mActions_Presence,Actions.PRESENCE$_CHECK, 		new CheckRoomInfo());
        put(mActions_Presence,Actions.PRESENCE$_PLUGIN,		new PluginPresence());
	}
	/*初始化iq节点业务*/
	private void initActions_Iq(){
		put(mActions_Iq,Actions.IQ$_QUERY_ROOM_FROM_CONTACTS,new Query_RoomInfoFromContact());
		put(mActions_Iq,Actions.IQ$_QUERY_ROOMINFO, 		 new Query_RoomInfo());
        put(mActions_Iq,Actions.IQ$_PLUGIN,		new PluginIq());
	}
	protected void put(Map<Actions,Action> map,Actions action,Action actionModel){
		actionModel.setActions(action);
		map.put(action, actionModel);
	}
	public synchronized void dispatchAction(XMPPNodeRoot root){
		Map<Actions,Action> pool = super.getActionsPool(root.getActionType());
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
						id = root.getId();
						action.beginAction();
						action.processAction(root,id);
						action.commitAction();
						action.onSuccessCallback(id);
						SystemUtil.log("handle", "handle action = "+action.getClass().getSimpleName());
						return;
					}
				} catch (Exception e) {
					SystemUtil.log("handle", "process exception = "+e+":"+SystemUtil.printStackElements(e.getStackTrace()));
				}
			}
		}else{
		}
		if(checkAction(mAction_Success, root)){
			id = root.getId();
			mAction_Success.processAction(root, id);
			return;
		}
	}
	/*批处理业务*/
	public synchronized <T extends XMPPNodeRoot>  void batchDispatchAction(List<T> roots){
		if(roots.size()==0){
			return;
		}
		XMPPNodeRoot root0 = roots.get(0);
		Map<Actions,Action> pool = super.getActionsPool(root0.getActionType());
		Collection<Action> set = pool.values();
		for(Action action:set){
			if(checkAction(action, root0)){
				try {
					SystemUtil.log("syn", "action = "+action);
					action.beginAction();
					for(XMPPNodeRoot r : roots){
						long id = r.getId();
						action.processAction(r,id);
					}
					action.commitAction();
				} catch (Exception e) {
					SystemUtil.log("syn", "process exception = "+e+":"+SystemUtil.printStackElements(e.getStackTrace()));
				}
				
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
	@Override
	protected void initActions() {
		this.initActions_Message();
		this.initActions_Presence();
		this.initActions_Iq();
	}
}
