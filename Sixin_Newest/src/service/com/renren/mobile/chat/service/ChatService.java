package com.renren.mobile.chat.service;

import com.common.app.AbstractRenrenApplication;
import com.common.binder.AbstractRemoteService;
import com.common.manager.LoginManager;
import com.common.manager.MessageManager;
import com.renren.mobile.chat.base.util.SystemUtil;

public class ChatService extends AbstractRemoteService {
	
	public static void start() {
		if (!LoginManager.getInstance().isLogout()) {
			MessageManager.getInstance().startService(
					AbstractRenrenApplication.getAppContext(),
					ChatService.class);
		}
	}
}