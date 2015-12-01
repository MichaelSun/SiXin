package com.renren.mobile.chat.activity;

import android.view.View;
import com.renren.mobile.chat.R;
import com.renren.mobile.chat.activity.NativePlugin.NativePlugin_Brush;
import com.renren.mobile.chat.activity.NativePlugin.NativePlugin_SelectPhoto;
import com.renren.mobile.chat.activity.NativePlugin.NativePlugin_TakePhoto;
import plugin.DBBasedPluginManager;
import plugin.LocalPlugin;
import plugin.base.Plugin;
import view.plugin.PluginGroup.AbstractPluginAdapter;
import view.plugin.PluginItemView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author dingwei.chen
 * */
public final class PluginAdapter extends AbstractPluginAdapter {

	private static PluginAdapter sInstance = null;
	private RenRenChatActivity mContext = null;
	DBBasedPluginManager mManager = new DBBasedPluginManager();
	List<NativePlugin> mPlugins = new ArrayList<NativePlugin>();
	private PluginAdapter(RenRenChatActivity activity){
		mContext = activity;
		this.initPlugins();
		List<Integer> list = mManager.getInstalledPluginIdList();
		for(Integer i:list){
			Plugin p = mManager.getPlugin(i, true);
			if("毛笔字".equals(p.getPluginInfo().name())){
				mPlugins.add(new NativePlugin_Brush(R.drawable.plugin_brush,mContext,(LocalPlugin)p));
			}
		}
		
	}
	
	public static PluginAdapter obtain(RenRenChatActivity activity){
		if(sInstance == null){
			sInstance = new PluginAdapter(activity);
			return sInstance;
		}
		for(NativePlugin p:sInstance.mPlugins){
			p.updateActivity(activity);
		}
		return sInstance;
	}
	
	
	
	
	
	private void initPlugins(){
		mPlugins.add(new NativePlugin_TakePhoto(R.drawable.plugin_takephoto, "拍照",mContext));
		mPlugins.add(new NativePlugin_SelectPhoto(R.drawable.plugin_selectphoto, "相册",mContext));
//        mPlugins.add(new NativePlugin.NativePlugin_3D(R.drawable.plugin_circle, "3D", mContext));
//        mPlugins.add(new NativePlugin.NativePlugin_Bip(R.drawable.plugin_circle, "Bip", mContext));
	}

	
	
	@Override
	public int getCount() {
		return mPlugins.size();
	}

	@Override
	public View getView(int position) {
		NativePlugin model = mPlugins.get(position);
		PluginItemView view = new PluginItemView(mContext);
		view.setPluginSource(model.getIconId(), model.getPluginInfo().name());
		view.setOnClickListener(model.mClick);
		return view;
	}

}
