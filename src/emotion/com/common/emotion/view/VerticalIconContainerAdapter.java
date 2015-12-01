package com.common.emotion.view;

import java.util.LinkedList;
import java.util.List;

import android.view.View;

import com.common.app.AbstractRenrenApplication;
/**
 * VerticalIconContainer的适配器。
 * @author zhenning.yang
 * @version 1.0，目前没有加入notify的功能，另外缓存设计也不完善，后期需要修改.
 */
public abstract class VerticalIconContainerAdapter {
	private List<VTabIconImageView> mList = new LinkedList<VTabIconImageView>();
	public VerticalIconContainerAdapter(){
		for(int i = 0;i<getCount();i++){
			if(getTabIconImageView(i) == null){  //资源不正确的容错处理，但UI上就难看了。
				mList.add((VTabIconImageView) new View(AbstractRenrenApplication.getAppContext()));
				continue;
			}
			mList.add(getTabIconImageView(i));
		}
	}
	public  VTabIconImageView getView(int position){
		
		return mList.get(position);
	}
	public abstract int getCount();
	protected abstract VTabIconImageView getTabIconImageView(int position);
}
