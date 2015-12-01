package com.common.manager;

import com.common.mcs.INetResponse;
import com.common.mcs.McsServiceProvider;

public class BlacklistManager {
	/**
	 * 黑名单管理类
	 * 
	 * @author zhen.yao 2012-8-15
	 */
	private static BlacklistManager mManager;

	public BlacklistManager() {

	}

	public static BlacklistManager getBlacklistManager() {
		if (mManager == null) {
			mManager = new BlacklistManager();
		}
		return mManager;
	}

	/**
	 * 添加删除黑名单
	 * 
	 * @param response
	 * @param contactUid
	 * @param isADD  true为添加，false为删除
	 */
	public void optBlacklist(INetResponse response, String contactUid,
			boolean isAdd) {
		McsServiceProvider.getProvider().optBlacklist(response, contactUid,
				isAdd);
	}

	/**
	 * 获取黑名单列表
	 * 
	 * @param response
	 * @param limit
	 * @param offset
	 * @param batchRun
	 */
	public void getBlacklist(INetResponse response, int limit, int offset,
			boolean batchRun) {
		McsServiceProvider.getProvider().getBlacklist(response, limit, offset,
				batchRun);
	}

	/**
	 * 获取黑名单数量
	 * 
	 * @param response
	 * @return
	 */
	public void getBlacklistNumber(INetResponse response) {
		McsServiceProvider.getProvider().getBlacklistNumber(response);
	}
}
