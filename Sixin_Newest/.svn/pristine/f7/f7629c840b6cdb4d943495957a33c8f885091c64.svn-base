package com.renren.mobile.chat.ui.plugins;

import com.renren.godlikepen.ui.ScrawlMainActivity;

import android.os.Bundle;
import plugin.LocalPlugin;
import plugin.base.Container;
import plugin.PluginInfo;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;


/**
 * @author xingchen.li  2012-08-16
 * 
 * 临时测试 调用  
 * {@link plugin.DBBasedPluginManager#getPlugin(String, boolean)}获取插件
 * 
 * 传入参数 String:    插件JID    {@link #JID_BRUSH_PLUGIN}
 * 		   boolean:   创建                  true or false 不影响          
 * */

public class BrushPlugin extends LocalPlugin {
	
	/**
	 * 调用毛笔字使用的插件JID
	 * */
	public static final String JID_BRUSH_PLUGIN = "brush";
	
	/**
	 * 调用毛笔字使用的请求码
	 * */
	public static final int REQUEST_CODE_BRUSH_PLUGIN = 200;
	/**
	 * 获取毛笔字路径的extra key
	 * */
	public static final String STRING_EXTRA_BRUSH_PLUGIN = "path";
	
	public BrushPlugin(Container container, PluginInfo pluginInfo) {
		super(container, pluginInfo);
	}
	/**
	 * 启动毛笔字插件
	 * 
	 * 该插件返回毛笔字的图片路径,使用时需要复写调用类的 onActivityResult方法
	 * requestCode为 {@link #REQUEST_CODE_BRUSH_PLUGIN}
	 * 
	 * 在参数data中获取毛笔字输入结果(png图片)的路径 ,从data中取出路径
	 * StringExtra 为 {@link #STRING_EXTRA_BRUSH_PLUGIN},
	 * 
	 * @param Context context 传入上下文
	 * */
    @Override
    public void start(Context context, Bundle bundle) {
        Intent intent=new Intent(context,ScrawlMainActivity.class);
        ((Activity) context).startActivityForResult(intent, REQUEST_CODE_BRUSH_PLUGIN);
    }
	
	@Override
	public boolean clearHistory() {
		return false;
	}
	/**
	 * TODO 
	 * 暂时未设置图标，测试请自行建立
	 * */
	@Override
	public String getIcon(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

}
