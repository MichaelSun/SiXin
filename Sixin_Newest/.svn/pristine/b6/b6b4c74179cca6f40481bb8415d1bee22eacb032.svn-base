package com.renren.mobile.chat.actions.message;

import com.common.actions.ActionNotMatchException;
import com.common.utils.Methods;
import com.data.xmpp.Message;
import com.renren.mobile.chat.actions.ActionDispatcher;
/**
 * @author dingwei.chen1988@gmail.com
 * @说明 单人聊天业务
 * */
public class Update_Subject_Message extends Action_Message {

	@Override
	public void processAction(Message node,long id) {
			ActionDispatcher.CALLBACK.onRecive_update_subject(node.getFromId(),node.mSubjectNode.getVersion(),Methods.htmlDecoder(node.mSubjectNode.mValue));
	}

	@Override
	public void checkActionType(Message node) throws ActionNotMatchException{
		this.addCheckCase(node.mSubjectNode!=null);
	}
	

}
