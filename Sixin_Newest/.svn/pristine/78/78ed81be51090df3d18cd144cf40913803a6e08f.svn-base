package com.renren.mobile.chat.third;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.common.manager.LoginManager;
import com.renren.mobile.account.LoginControlCenter;
import com.renren.mobile.account.LoginfoModel;
import com.renren.mobile.chat.RenrenChatApplication;
import com.renren.mobile.chat.base.util.SystemUtil;
import com.renren.mobile.chat.ui.account.ThirdWelcomeActivity;
import com.renren.mobile.chat.ui.guide.WelcomeActivity;
import com.sixin.proxy.UserInfo;

/**
 * @author dingwei.chen
 * @说明  第三方业务处理单元
 * */
public abstract class ThirdAction {
	
	public String ACTION_PARAMS_NAME = "ACTION_PARAMS_NAME";
	
	public boolean mIsDebug = true;
	
	public boolean mIsChat = false;
	
	/*// kaining.yang
	public void process(Context context,String actionName,Intent intent,ThirdActionsCenter center) {
		LoginControlCenter.getInstance().setIsFirstInstall(context, false);
		// 获取登录信息
		LoginfoModel mLoginInfo = LoginControlCenter.getInstance().parseLoginInfo(intent);
		this.processAction(context, intent, center);
		RenrenChatApplication.setThird(true);
		
		// 官方互调信息
		SystemUtil.logykn("官方互调 account", mLoginInfo.mAccount);
		SystemUtil.logykn("官方互调 password", mLoginInfo.mPassword);
		
		// 登录信息验证
		if (LoginManager.getInstance().getLoginInfo().mBindInfoRenren.mBindId.equals(String.valueOf(mLoginInfo.mUserId))) {
			// 官方人人帐号 和 本地私信帐号绑定的人人帐号相同
			// 并且为已登陆状态
			RenrenChatApplication.clearStack(); // 清空历史用户栈
			ThirdWelcomeActivity.show(context, true, true);
		} else {
			// 不同账号要清空联系人
			try {
				boolean delete = true; // 是否要清空数据
				if (LoginManager.getInstance().getLoginInfo().mUserId == 0 && LoginManager.getInstance().getLoginInfo().mAccount != null && mLoginInfo.mAccount != null) {
					if (LoginManager.getInstance().getLoginInfo().mAccount.equals(mLoginInfo.mAccount)) {
						//账户相同  表示只是私信账户被注销了  不用在清空数据
						delete = false;
					}
				}
				if (delete) {
					//删除群组
					RoomInfosData.getInstance().delete_all_rooms();
					//删除聊天记录
					ChatDataHelper.getInstance().deleteAll();
					ChatSessionHelper.getInstance().deleteAll();
					ContactsDAO contactDao = DAOFactoryImpl.getInstance().buildDAO(ContactsDAO.class);
					contactDao.delete_All();
					C_ContactsData.getInstance().deleteAllCommonContact();
					C_ContactsData.getInstance().deleteAllRenRenContact();
					ContactMessageData.getInstance().delete_All();
					LoginControlCenter.getInstance().logout(context); // 注销用户

				}
				C_ContactsData.getInstance().clear(); // 内存中的数据要清空的  因为在线联系人必须重新获取
			} catch (Exception e) {
				e.printStackTrace();
			}
			LoginControlCenter.getInstance().saveUserData(mLoginInfo); // 保存新用户数据
			RenrenChatApplication.clearStack(); // 清空历史用户栈
			// LoginControlCenter.getInstance().pageJump(context, new RRSharedPreferences(context));
			ThirdWelcomeActivity.show(context, true, false, mLoginInfo.mAccount, mLoginInfo.mPassword);
		}
	}*/
	
	// kaining.yang
	public void process(Context context,String actionName,Intent intent,ThirdActionsCenter center) {
		LoginControlCenter.getInstance().setIsFirstInstall(context, false);
		// 获取登录信息
		UserInfo userinfo = new UserInfo(intent);
		LoginfoModel mLoginInfo = LoginControlCenter.getInstance().parseLoginInfo(intent);
		this.processAction(context, intent, center);
		RenrenChatApplication.setThird(true);
		
		// 官方互调信息
		SystemUtil.logykn("官方互调 account", mLoginInfo.mAccount);
		SystemUtil.logykn("官方互调 password", mLoginInfo.mPassword);
		// RenrenChatApplication.clearStack(); // 清空历史用户栈
		
		//
		SystemUtil.logykn("官方互调判断：");
		/*SystemUtil.logykn(LoginManager.getInstance().getLoginInfo().mBindInfoRenren.mBindId);
		SystemUtil.logykn(String.valueOf(mLoginInfo.mUserId));
		SystemUtil.logykn(mLoginInfo.mAccount);
		SystemUtil.logykn(LoginManager.getInstance().getLoginInfo().mAccount);*/
		// 登录信息验证
		if (LoginManager.getInstance().getLoginInfo().mBindInfoRenren.mBindId.equals(String.valueOf(mLoginInfo.mUserId)) ||
				(!TextUtils.isEmpty(LoginManager.getInstance().getLoginInfo().mAccount) && 
				mLoginInfo.mAccount.equals(LoginManager.getInstance().getLoginInfo().mAccount) && 
				LoginManager.getInstance().getLoginInfo().mIsLogin)) {
			SystemUtil.logykn("不需要登陆");
			/*SystemUtil.logykn(LoginManager.getInstance().getLoginInfo().mBindInfoRenren.mBindId);
			SystemUtil.logykn(String.valueOf(mLoginInfo.mUserId));
			SystemUtil.logykn(mLoginInfo.mAccount);
			SystemUtil.logykn(LoginManager.getInstance().getLoginInfo().mAccount);*/
			// SystemUtil.logykn(LoginManager.getInstance().getLoginInfo().toString());
			// 不需要登陆
			// 1.官方人人帐号 和 本地私信帐号绑定的人人帐号相同 
			// 2.官方人人帐号 和 私信帐号相同（用人人帐号单独注册私信的用户）
			// 并且为已登陆状态
			ThirdWelcomeActivity.show(context, userinfo.mUserId, true, true, mIsChat);
		} else {
			// 需要登陆
			// 不同账号要清空联系人
			boolean delete = true; // 是否要清空数据
			if (LoginManager.getInstance().getLoginInfo().mUserId == 0 && LoginManager.getInstance().getLoginInfo().mAccount != null && mLoginInfo.mAccount != null) {
				if (LoginManager.getInstance().getLoginInfo().mAccount.equals(mLoginInfo.mAccount)) {
					//账户相同  表示只是私信账户被注销了  不用在清空数据
					delete = false;
				}
			}
			boolean islogouted = true;
			LoginfoModel info = LoginControlCenter.getInstance().loadAutoLoginUserData();
			if (info != null) {
				// 目前有已登陆私信帐号，但已登陆私信账户和人人官方帐号不同
				if (info.mAutoLogin == 1) {
					islogouted = false;
				}
			}
			
			SystemUtil.logykn("官方互调，需要登陆");
			SystemUtil.logykn("islogouted", islogouted + "");
			ThirdWelcomeActivity.show(context, userinfo.mUserId, true, false, islogouted, mIsChat, delete, mLoginInfo.mAccount, mLoginInfo.mPassword);
		}
	}
	
	/*子类进行业务处理*/
	protected abstract void processAction(Context context, Intent intent, ThirdActionsCenter center) ;
	
}
