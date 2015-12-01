package com.renren.mobile.chat.ui.plugins;

import java.util.ArrayList;
import java.util.List;

import plugin.DBBasedPluginManager;
import plugin.PluginInfo;
import plugin.PluginManagerObserver;
import plugin.base.Plugin;
import plugin.database.PluginDaoFactoryImpl;
import plugin.database.Plugin_Model;
import plugin.database.dao.PluginBaseDAO;
import plugin.database.dao.PluginBaseDAOObserver;
import plugin.ui.PluginSettingActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.core.util.CommonUtil;
import com.renren.mobile.chat.R;
import com.renren.mobile.chat.RenrenChatApplication;
import com.renren.mobile.chat.ui.BaseActivity;

/**
 * @author yanfei.wu 插件管理的界面 at 2012-8-15
 */
public class PluginAllActivity extends BaseActivity implements PluginManagerObserver,
		PluginBaseDAOObserver {

	/**
	 * 插件界面的一些控件
	 */
	private LayoutInflater inflater;

	private LinearLayout mRoot;
	
	private ScrollView mScrollView;
	
	private LinearLayout noPluginLayout;

	private LinearLayout mInstallList,mUninstallList;
	
	private TextView mTitle;

	private Button mLeftButton,mTitleRefreshButton;
	private ProgressBar mRefreshProgress;

	private TextView installTXT, uninstallTXT;
	
	 /* 判断是已安装还是未安装的标志*/
	private final int INSTALLED_PLUGIN = 1;
	private final int UNINSTALLED_PLUGIN = 2;

	private DBBasedPluginManager pluginManager = new DBBasedPluginManager();
	private PluginBaseDAO pluginBaseDAO = PluginDaoFactoryImpl.getInstance().buildDAO(PluginBaseDAO.class);
	
	 /* 存储从数据库中取得的插件信息*/
	private ArrayList<PluginInfo> installPlugins = new ArrayList<PluginInfo>();
	private ArrayList<PluginInfo> uninstallPlugins = new ArrayList<PluginInfo>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.plugin_all_list);
		mScrollView = (ScrollView)findViewById(R.id.scroll_list);
		noPluginLayout = (LinearLayout)findViewById(R.id.plugin_no_data);
		inflater = getLayoutInflater();
		getData();
		initView();
	}

	/**
	 * 初始化界面
	 */
	private void initView() {
		pluginBaseDAO.registorObserver(this);
		pluginManager.registorObserver(this);

		mRoot = (LinearLayout) findViewById(R.id.plugin_all_list);
		mInstallList = (LinearLayout) findViewById(R.id.install_plugin_layout);
		mUninstallList = (LinearLayout) findViewById(R.id.uninstall_plugin_layout);
		installTXT = (TextView) findViewById(R.id.install_txt);
		uninstallTXT = (TextView) findViewById(R.id.uninstall_txt);

		mTitle = (TextView) findViewById(R.id.title_mid_layout_text);
		mTitle.setText(RenrenChatApplication.getmContext().getResources().getString(R.string.plugin_all_title));
		mTitleRefreshButton = (Button)findViewById(R.id.title_right_refresh_button);
		mRefreshProgress = (ProgressBar)findViewById(R.id.title_right_refresh_progress);
		setRefreshProgressVisibility(false);
		
		 /* 刷新，从网络取得最新的插件信息并更新数据库，相应地触发观察者变化界面*/
		mTitleRefreshButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				setRefreshProgressVisibility(true);
				pluginManager.updatePluginsFromServer();
				
			}
		});
		mLeftButton = (Button) findViewById(R.id.title_left_button);
		mLeftButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				PluginAllActivity.this.finish();
			}
		});
		
		/*根据两个List里面存储的内容往界面里添加显示每个插件的View，该思路太暴力，有待改进*/
		if(installPlugins.isEmpty() && uninstallPlugins.isEmpty()){
			noPluginLayout.setVisibility(View.VISIBLE);
			mScrollView.setVisibility(View.GONE);
		}else{
			noPluginLayout.setVisibility(View.GONE);
			mScrollView.setVisibility(View.VISIBLE);
			
			if (!installPlugins.isEmpty()) {
				installTXT.setVisibility(View.VISIBLE);
				for (int i =0; i<installPlugins.size();i++) {
					PluginInfo tem = installPlugins.get(i);
					addPluginView(tem, INSTALLED_PLUGIN,i);
				}
			} else {
				 installTXT.setVisibility(View.GONE);
			}

			if (!uninstallPlugins.isEmpty()) {
				uninstallTXT.setVisibility(View.VISIBLE);
				for (int i=0; i<uninstallPlugins.size();i++) {
					PluginInfo tem = uninstallPlugins.get(i);
					addPluginView(tem, UNINSTALLED_PLUGIN,i);
				}
			} else {
				 uninstallTXT.setVisibility(View.GONE);
			}
		}

		

	}

	/**
	 * 从数据库取得插件相关信息
	 */
	private void getData() {
		
		List<Integer> install  = pluginManager.getInstalledPluginIdList();
		List<Integer> unstall = pluginManager.getUnInstalledPluginIdList();

		installPlugins.clear();
		uninstallPlugins.clear();
		if (install!=null && install.size()!=0) {
			for (int tem : install) {
				Plugin plugin = pluginManager.getPlugin(tem, true);
				if(plugin!=null){
					PluginInfo info = plugin.getPluginInfo();
					if(plugin!=null && info!=null && !"毛笔字".equals(info.name())){
						installPlugins.add(info);
					}
				}
				
			}
		}
		if (unstall!=null && unstall.size()!=0) {
			for (int tem : unstall) {
				Plugin plugin = pluginManager.getPlugin(tem, true);
				if(plugin!=null){
					PluginInfo info = plugin.getPluginInfo();
					if(plugin!=null && info!=null && !"毛笔字".equals(info.name())){
						uninstallPlugins.add(info);
					}
				}
				
			}
		}
		

	}


	/**
	 * 添加具体插件
	 * 
	 * @param model
	 *            封装插件信息的model对象
	 * @param type
	 *            标记插件是否已安装的标志
	 * @param index
	 *            插件在列表的索引，主要用来判断采用何种背景图
	 */
	private void addPluginView(final PluginInfo model,int type,int index) {
		
			final View item = inflater.inflate(R.layout.added_plugin_item, mRoot, false);

			TextView pluginName = (TextView)item.findViewById(R.id.plugin_name);
			pluginName.setText(model.name());
			
			/*设置进入插件设置的点击事件*/
			item.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
						PluginSettingActivity.show(PluginAllActivity.this, Integer.parseInt(model.id()));
					
				}
			});
			
			/*根据插件在列表的位置，选择不同的背景图进行设置*/
			int itemsCount = type == INSTALLED_PLUGIN ? installPlugins.size() : uninstallPlugins.size();
			if(itemsCount==1){
				item.setBackgroundResource(R.drawable.single_bg_selector);
			}else{
				if(index==0){
					item.setBackgroundResource(R.drawable.top_bg_selector);
				}else{
					item.setBackgroundResource(index == (itemsCount-1)?R.drawable.bottom_bg_selector:R.drawable.middle_bg_selector);
				}
			}
			
			LinearLayout listLayout = type == INSTALLED_PLUGIN ? mInstallList
					: mUninstallList;

			listLayout.addView(item);
		
	}

	
	@Override
	public void onInsert(List<Plugin_Model> models) {
		updateUI();
	}

	@Override
	public void onDelete(int pluginId) {
		updateUI();
	}

	@Override
	public void onUpdate(List<Plugin_Model> models) {
		updateUI();

	}

	/**
	 * 需要更新插件管理界面时，调用此方法
	 */
	public void updateUI() {
		mInstallList.removeAllViews();
		mUninstallList.removeAllViews();
		getData();
		initView();
	}

	@Override
	public void onPluginInstalledOver(boolean arg0) {
		
	}

	@Override
	public void onPluginUninstalledOver(boolean arg0) {
		
	}
	
	@Override
	public void onFailed() {
		setRefreshProgressVisibility(false);
	}
	
	@Override
	public void onSuccess() {
		Toast.makeText(this, RenrenChatApplication.getmContext().getResources().getString(R.string.plugin_refresh_completed), Toast.LENGTH_SHORT).show();
		setRefreshProgressVisibility(false);
	}

	/*
	 * 为求方便，设置Title右边刷新和Progressbar的可见性变化的辅助方法
	 */
	public void setRefreshProgressVisibility(boolean visibility) {
		if (visibility) {
			mRefreshProgress.setVisibility(View.VISIBLE);
			mTitleRefreshButton.setVisibility(View.GONE);
		} else {
			mTitleRefreshButton.setVisibility(View.VISIBLE);
			mRefreshProgress.setVisibility(View.GONE);
		}
	}
	
}
