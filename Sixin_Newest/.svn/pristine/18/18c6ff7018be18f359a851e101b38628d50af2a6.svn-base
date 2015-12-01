package com.renren.mobile.chat.actions.message;

import java.util.LinkedList;
import java.util.List;

import plugin.DBBasedPluginManager;
import android.text.TextUtils;

import com.common.actions.ActionNotMatchException;
import com.common.manager.LoginManager;
import com.data.xmpp.Message;
import com.renren.mobile.chat.actions.ActionDispatcher;

/**
 * @author dingwei.chen1988@gmail.com
 * @说明 单人聊天业务
 * */
public class PushFeed extends Action_Message {

	private List<Long> mFeeds = new LinkedList<Long>();

	@Override
	public void processAction(Message node, long id) {
		if (new DBBasedPluginManager()
				.isPluginWithPluginIdInstalled(DBBasedPluginManager.PLUGIN_ID_ATTETION)
				&& (LoginManager.getInstance().getLoginInfo().mBindInfoRenren != null && !TextUtils
						.isEmpty(LoginManager.getInstance().getLoginInfo().mBindInfoRenren.mBindId))) {
			long feedid = node.mFeedNode.getFeedId();
			if (feedid != -1) {
				mFeeds.add(feedid);
			}
		}

	}

	@Override
	public void beginAction() {
		mFeeds.clear();
	};

	@Override
	public void commitAction() {
		// C_ChatDataParser.getInstance().onReciveFeeds(mFeeds);
		if (mFeeds.size() > 0) {
			long[] feedArr = new long[mFeeds.size()];
			int index = 0;
			for (Long l : mFeeds) {
				feedArr[index++] = l;
			}
			ActionDispatcher.CALLBACK.onRecive(feedArr);
		}
	};

	@Override
	public void checkActionType(Message node) throws ActionNotMatchException {
		this.addCheckCase(node.mType.equals("normal"));
	}

}
