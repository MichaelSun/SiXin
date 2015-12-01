package com.common.emotion.view;
/**
 * 同时支持顺序切换和随机切换的接口
 * @author zhenning.yang
 *
 */
public interface ISuperOnFling2ContentChangeListener extends IOnFling2ContentChangeListener {
	public void onShowPointer(int index);
}
