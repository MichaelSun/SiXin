package com.renren.mobile.chat.base;

import android.view.View;

/**
 * @author dingwei.chen
 * 基础持有类
 * @说明 这个类是为了增加视图的索引速度和方便管理视图用的,
	 * 在使用视图的时候建议增加一个静态接口来管理资源配置
	 * 外界使用的时候都直接调用mRootView来生成视图
 * */
public class BaseHolder {

	public View mRootView = null;
	
	public View getRootView(){
		return this.mRootView;
	}
}
