package com.renren.mobile.chat.ui.plugins;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import plugin.DBBasedPluginManager;
import plugin.PluginHelper;
import plugin.PluginInfo;
import plugin.base.Plugin;
import plugin.database.PluginDaoFactoryImpl;
import plugin.database.Plugin_Model;
import plugin.database.dao.PluginBaseDAO;
import plugin.database.dao.PluginBaseDAOObserver;
import plugin.plugins.v0.PluginV0;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.common.mcs.INetRequest;
import com.common.mcs.INetResponse;
import com.common.mcs.McsServiceProvider;
import com.core.json.JsonObject;
import com.core.json.JsonValue;
import com.renren.mobile.chat.R;
import com.renren.mobile.chat.RenrenChatApplication;
import com.renren.mobile.chat.base.util.ImageUtil;
import com.renren.mobile.chat.common.ResponseError;
import com.renren.mobile.chat.ui.contact.feed.ChatFeedModel;
import com.renren.mobile.chat.ui.contact.feed.FeedCallback;
import com.renren.mobile.chat.ui.contact.feed.FeedCallbackSource;
import com.renren.mobile.chat.ui.contact.feed.ObserverImpl;

/**
 * @author yanfei.wu 已安装插件的列表界面
 * at 2012-8-15
 */
	
public class PluginView implements PluginBaseDAOObserver,FeedCallback{
	
	public static Context context;
	
	private static LayoutInflater inflater;
	
	private LinearLayout pluginLayout;
	
	private ScrollView mScrollView;
	
	private LinearLayout noPluginLayout;
	
	private LinearLayout mRootLayout;

	public ScrollView mPluginList;
	
	private DBBasedPluginManager pluginManager = new DBBasedPluginManager();
	
	private PluginBaseDAO pluginBaseDAO = PluginDaoFactoryImpl.getInstance().buildDAO(PluginBaseDAO.class);
	
	private ArrayList<PluginInfo> installPlugins = new ArrayList<PluginInfo>();

    static{
        DBBasedPluginManager.init(new PluginContainer());
    }
	
	/*为了设置插件未读消息数而建立的内部类*/
	private class CountHolder{
		public TextView requestNUM;
		public RelativeLayout requestNumLayout;
		public CountHolder(TextView count,RelativeLayout layout){
			this.requestNUM = count;
			this.requestNumLayout = layout;
		}
	}
	
	/*为了添加插件时方便，而建立的内部类*/
	private class PluginHolder{
		public TextView pluginName;
		public TextView pluginDesc;
		public ImageView pluginIcon;
		public RelativeLayout requestNumLayout;
		public TextView requestNUM;
		public void setData(PluginInfo info){
			pluginName.setText(info.name());
			pluginDesc.setText(info.desc());
			pluginIcon.setBackgroundResource(PluginHelper.getIcon(info.id(), PluginHelper.ICON_SMALL));
		}
	}
	
	/*存储界面中所有插件消息区域的View，在需要时进行更新*/
	private HashMap<String, CountHolder> countViewMap = new HashMap<String, PluginView.CountHolder>();
	
	
	/**
	 * 进入插件列表界面的对外接口
	 * @param con Context对象，用来初始化View
	 * @return 返回展示插件列表的View
	 */
	public  View getView(Context con){
		context = con;
		inflater = LayoutInflater.from(con);
		pluginBaseDAO.registorObserver(this);
		mRootLayout = (LinearLayout)inflater.inflate(R.layout.plugin_installed_list, null);
		noPluginLayout = (LinearLayout)mRootLayout.findViewById(R.id.plugin_no_data);
		pluginLayout = (LinearLayout)mRootLayout.findViewById(R.id.append_plugin_layout);
		mScrollView = (ScrollView)mRootLayout.findViewById(R.id.scroll_list);
		ObserverImpl.getInstance().addCallback(this);
		getData();
		initView();
		return mRootLayout;
	}
	
	/**
	 * 初始化界面
	 */
	public void initView(){
		/*
		 * 为达到UI设计中诡异的奇偶变化，根据插件数量设计了两个添加插件到界面的方法
		 */
		if(installPlugins.size()==1 || installPlugins.size()==2){
			for(PluginInfo model : installPlugins){
				addPluginView(model);
			}
		}else if(installPlugins.size()%2==0){
			for(int i =0;i<installPlugins.size();i=i+2){
				addPluginView(installPlugins.get(i),installPlugins.get(i+1));
			}
		}else if (installPlugins.size()%2==1){
			addPluginView(installPlugins.get(0));
			for(int i =1;i<installPlugins.size();i=i+2){
				addPluginView(installPlugins.get(i),installPlugins.get(i+1));
			}
		}
	}
	
	/**
	 * 设定用来从网络获取插件ICON的方法，当前版本未曾用到，暂留，Mark!
	 * @param icon 需要设置Icon的ImageView
	 * @param url 图片资源的url
	 */
	private void downloadImage(final ImageView icon, String url) {
		INetResponse response = new INetResponse() {
			public void response(final INetRequest req, final JsonValue result) {
				if (result instanceof JsonObject) {
					final JsonObject obj = (JsonObject) result;
					RenrenChatApplication.mHandler.post(new Runnable() {
						@Override
						public void run() {
							if (ResponseError.noError(req, obj, false)) {
								byte[] imgData = obj.getBytes(IMG_DATA);
								if (imgData != null) {
									Bitmap bm = ImageUtil.createImageByBytes(imgData);
									if (null != bm) {
										icon.setImageBitmap(bm);
									} else {
										icon.setImageResource(R.drawable.cy_bangbang_icon);
									}
								}else{
									icon.setImageResource(R.drawable.cy_bangbang_icon);
								}
								
							} 
							
						}
					});
				}
			}
		};
		McsServiceProvider.getProvider().getImage(url, response);
	}
	
	/**
	 * 从数据库取回已安装插件的信息
	 */
	public void getData(){
		List<Integer> idList = pluginManager.getInstalledPluginIdList();
		countViewMap.clear();
		installPlugins.clear();
		if(idList!=null && idList.size()!=0){
			
			for(int id : idList){
				Plugin plugin = pluginManager.getPlugin(id, true);
				if(plugin!=null){
					PluginInfo info = plugin.getPluginInfo();
					if(info!=null && !"毛笔字".equals(info.name())){
						installPlugins.add(info);
					}
				}
				
			}
			
		}
		
		/*根据取得的信息是否为空，设置空白提醒界面是否显示*/
		if(installPlugins.isEmpty()){
			noPluginLayout.setVisibility(View.VISIBLE);
			mScrollView.setVisibility(View.GONE);
		}else{
			noPluginLayout.setVisibility(View.GONE);
			mScrollView.setVisibility(View.VISIBLE);
		}
		
	}


	/**
	 * 添加具体插件
	 * 
	 * @param info 封装好的插件信息的对象
	 */
	private void addPluginView(final PluginInfo info){
		
			final PluginV0 plugin = (PluginV0)pluginManager.getPlugin(Integer.parseInt(info.id()), true);
			
			FrameLayout item = (FrameLayout)inflater.inflate(R.layout.plugin_bigger_area_row, mRootLayout, false);
			
			PluginHolder holder = new PluginHolder();
			
			holder.pluginName = (TextView) item.findViewById(R.id.plugin_title);
			holder.pluginIcon = (ImageView) item.findViewById(R.id.plugin_icon);
			holder.pluginDesc = (TextView)item.findViewById(R.id.plugin_desc);
			holder.requestNumLayout = (RelativeLayout)item.findViewById(R.id.message_num_layout);
			holder.requestNUM = (TextView)item.findViewById(R.id.plugin_message_textview);
			
			/*设置插件消息区域的显示与否*/
			countViewMap.put(info.id(), new CountHolder(holder.requestNUM, holder.requestNumLayout));
			if(info!=null && plugin!=null){
				
				if(plugin.getNotificationCount()!=0){
					holder.requestNumLayout.setVisibility(View.VISIBLE);
					holder.requestNUM.setText(String.valueOf(plugin.getNotificationCount()));
				}else{
					holder.requestNumLayout.setVisibility(View.GONE);
				}
				holder.setData(info);
			}
			
			/*设置每个插件项的上边空白间距*/
//			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);  
//			lp.setMargins(0,(int) ((int)10*RenrenChatApplication.density), 0, 0); 
//			item.setLayoutParams(lp);
			
			pluginLayout.addView(item);
			
			/*设置进入插件的点击事件，调用插件的start(()方法，具体实现由插件自身完成*/
			item.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					//进入插件
					Bundle bundle = new Bundle();
					bundle.putString("plugin_id", info.id());
					plugin.start(context, bundle);
				}
			});
		
		
	}
	
	/**
	 * 添加具体的插件，一次性添加两个插件
	 * @param info_1，添加的第一个插件的pluginInfo
	 * @param info_2, 添加的第二个插件的pluginInfo
	 */
	private void addPluginView(final PluginInfo info_1,final PluginInfo info_2){
		final PluginV0 plugin_1 = (PluginV0)pluginManager.getPlugin(Integer.parseInt(info_1.id()), true);
		final PluginV0 plugin_2 = (PluginV0)pluginManager.getPlugin(Integer.parseInt(info_2.id()), true);
		
		
		LinearLayout item = (LinearLayout)inflater.inflate(R.layout.plugin_smaller_area_row, mRootLayout, false);
		
		PluginHolder holder_1 = new PluginHolder();
		PluginHolder holder_2 = new PluginHolder();
		
		RelativeLayout pluginLayout_1 = (RelativeLayout)item.findViewById(R.id.one_plugin_area);
		RelativeLayout pluginLayout_2 = (RelativeLayout)item.findViewById(R.id.another_plugin_area);
		
		holder_1.pluginName = (TextView) item.findViewById(R.id.plugin_title);
		holder_1.pluginIcon = (ImageView) item.findViewById(R.id.plugin_icon);
		holder_1.pluginDesc = (TextView)item.findViewById(R.id.plugin_desc);
		holder_1.requestNumLayout = (RelativeLayout)item.findViewById(R.id.message_num_layout);
		holder_1.requestNUM = (TextView)item.findViewById(R.id.plugin_message_textview);
		
		holder_2.pluginName = (TextView) item.findViewById(R.id.another_plugin_title);
		holder_2.pluginIcon = (ImageView) item.findViewById(R.id.another_plugin_icon);
		holder_2.pluginDesc = (TextView)item.findViewById(R.id.another_plugin_desc);
		holder_2.requestNumLayout = (RelativeLayout)item.findViewById(R.id.message_num_layout_2);
		holder_2.requestNUM = (TextView)item.findViewById(R.id.another_plugin_message_textview);
		
		countViewMap.put(info_1.id(), new CountHolder(holder_1.requestNUM, holder_1.requestNumLayout));
		countViewMap.put(info_2.id(), new CountHolder(holder_2.requestNUM, holder_2.requestNumLayout));
		
		//控制插件消息数提醒的Icon是否显示，以及设置消息数
		if(info_1!=null && info_2!=null && plugin_1!=null && plugin_2!=null){
			
			if(plugin_1.getNotificationCount()!=0 && plugin_2.getNotificationCount()!=0){
				holder_1.requestNumLayout.setVisibility(View.VISIBLE);
				holder_2.requestNumLayout.setVisibility(View.VISIBLE);
				holder_1.requestNUM.setText(String.valueOf(plugin_1.getNotificationCount()));
				holder_2.requestNUM.setText(String.valueOf(plugin_2.getNotificationCount()));
			}else{
				holder_1.requestNumLayout.setVisibility(View.GONE);
				holder_2.requestNumLayout.setVisibility(View.GONE);
			}
			holder_1.setData(info_1);
			holder_2.setData(info_2);
		}
		
		
//		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);  
//		lp.setMargins(0, (int) (15*RenrenChatApplication.density), 0, 0); 
//		item.setLayoutParams(lp);
		
		pluginLayout.addView(item);
		pluginLayout_1.setBackgroundResource(R.drawable.plugin_default_background);
		pluginLayout_2.setBackgroundResource(R.drawable.plugin_default_background);
		
		/*设置进入插件的点击事件*/
		pluginLayout_1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//进入插件并传递插件的plugin id
				Bundle bundle = new Bundle();
				bundle.putString("plugin_id", info_1.id());
				plugin_1.start(context, bundle);
			}
		});
		
		pluginLayout_2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//进入插件并传递插件的plugin id
				Bundle bundle = new Bundle();
				bundle.putString("plugin_id", info_2.id());
				plugin_2.start(context, bundle);
			}
		});
	}
	
	
	@Override
	public void onDelete(int pluginId) {
		updateUI();
	}

	@Override
	public void onInsert(List<Plugin_Model> models ) {
		updateUI();
	}
	
	@Override
	public void onUpdate(List<Plugin_Model> models) {
		updateUI();
	}
	
	/**
	 * 需要更新插件列表界面时，调用此方法
	 */
	public void updateUI(){
		pluginLayout.removeAllViews();
		getData();
		initView();
	}
	
	/**
	 * 需要刷新插件的未读消息数量时，调用此方法
	 */
	public void setRequestCount(){
		Iterator iterator = countViewMap.entrySet().iterator(); 
		while (iterator.hasNext()) { 
			Entry entry = (Entry) iterator.next(); 
			String pluginId = (String)entry.getKey(); 
			CountHolder holder = (CountHolder)entry.getValue();
			PluginV0 plugin = (PluginV0)pluginManager.getPlugin(Integer.valueOf(pluginId), true);
			if(plugin!=null){
				int i = plugin.getNotificationCount();
				if(i!=0){
					holder.requestNumLayout.setVisibility(View.VISIBLE);
					if(i>99){
						holder.requestNUM.setText("99+");
					}else{
						holder.requestNUM.setText(String.valueOf(i));
					}
					
				}else{
					holder.requestNumLayout.setVisibility(View.GONE);
				}
			}
		} 
	}

	/**
	 * 为满足人人关注插件的未读消息提示而实现的接口方法，但不太适用以后其他插件
	 */
	@Override
	public void onCallback(List<ChatFeedModel> newFeed) {
		RenrenChatApplication.sHandler.post(new Runnable() {
			@Override
			public void run() {
					setRequestCount();
			}
		});
	}

	/**
	 * 为满足人人关注插件的未读消息提示而实现的接口方法，但不太适用以后其他插件
	 */
	@Override
	public void registorFeedCallbackSource(FeedCallbackSource source) {
		
	}
	public void clear(){
		pluginBaseDAO.unregistorObserver(this);
		ObserverImpl.getInstance().removeCallback(this);
	}
}
