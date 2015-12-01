package plugin.ui;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import plugin.DBBasedPluginManager;
import plugin.LocalPlugin;
import plugin.PluginHelper;
import plugin.PluginInfo;
import plugin.PluginManagerObserver;
import plugin.base.PluginManager;
import plugin.database.PluginDaoFactoryImpl;
import plugin.database.Plugin_Model;
import plugin.database.dao.PluginBaseDAO;
import plugin.database.dao.PluginBaseDAOObserver;
import plugin.plugins.v0.PluginV0;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.common.R;
import com.common.manager.DataManager;
import com.core.util.CommonUtil;

public class PluginSettingActivity extends Activity implements PluginBaseDAOObserver,PluginManagerObserver{
	
	public static final String EXTRA_STRING = "ID";
	
	private DBBasedPluginManager mPluginManager;
	private PluginBaseDAO pluginBaseDAO = PluginDaoFactoryImpl.getInstance().buildDAO(PluginBaseDAO.class);
	private PluginV0 mPlugin;
	private int pluginId;
	private ProgressDialog mDialog;
	
	private boolean mReminderFlag;
	private boolean mPushSetFlag = false;
	
	public static interface Switcher{
		public final static Boolean REMIND_DEFAULT = false;
		public final static Boolean REMIND_ON = true;
	}
	
	/**
	 * 静态方法启动插件设置界面
	 * @param context 上下文
	 * @param pluginId 插件唯一标示 pluginId
	 * */
	public static void show(Context context, int pluginId) {
		Intent intent = new Intent(context,PluginSettingActivity.class);
		intent.putExtra(EXTRA_STRING, pluginId);
		context.startActivity(intent);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.plugin_info);
		initPlugin();	
	}
	
	private void initPlugin(){
		Intent intent = getIntent();
		pluginId = intent.getIntExtra(EXTRA_STRING, -1);
		mPluginManager = new DBBasedPluginManager();
		pluginBaseDAO.registorObserver(this);
		mPluginManager.registorObserver(this);
		
		// judge if exists or not
		mPlugin = (PluginV0) mPluginManager.getPlugin(pluginId, true);
		if (mPlugin != null) {
			// then judge if installed to initialize the custom setting view
			boolean installed = mPluginManager.isPluginWithPluginIdInstalled(pluginId);
			mReminderFlag = mPluginManager.isPluginPushOpen(pluginId);
			initViews(installed);
		} else {
			Toast.makeText(this, getResources().getString(R.string.plugin_unknow), Toast.LENGTH_LONG).show();
			Timer timer = new Timer();
			timer.schedule(new TimerTask(){

				@Override
				public void run() {
					finish();
				}}, 2000);
			
		}
	}
	
	/** title */
	public Button mBackButton;
	public TextView mTitle;

	
	/**
	 * 界面一些属性
	 * */
	public ImageView mPluginIcon;
	public TextView mPluginName;
	public ImageView mPluginOnOffIcon;
	public TextView mPluginOnOffStatus;
	public TextView mPluginDescription;
	
	public Button mClearHistory;
	public RelativeLayout mPushToggle;
	public TextView mPushStatus;
	public ImageView mPushToggleIcon;
	public ProgressBar mProgressBar;
	
	public Button mPluginInstall;
	public Button mPluginUninstall;
	
	private void initViews(boolean installed) {
		mDialog = new ProgressDialog(this);
		mDialog.setMessage(getResources().getString(R.string.plugin_setting));
		
		mBackButton = (Button)findViewById(R.id.title_left_button);
		mBackButton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				finish();
			}});
		mTitle = (TextView) findViewById(R.id.title_middle_name);
		
		mPluginName = (TextView) findViewById(R.id.plugin_name);
		mPluginIcon = (ImageView) findViewById(R.id.plugin_icon);
		mPluginOnOffIcon = (ImageView) findViewById(R.id.onoff_icon);
		mPluginOnOffStatus = (TextView) findViewById(R.id.onoff_status);
		mPluginDescription = (TextView) findViewById(R.id.plugin_desc);
		mPluginInstall = (Button) findViewById(R.id.plugin_install);
		mPluginUninstall = (Button) findViewById(R.id.plugin_uninstall);
		mClearHistory = (Button) findViewById(R.id.clear_plugin_history);
		
		mPushToggle = (RelativeLayout) findViewById(R.id.push_toggle_layout);
		mPushToggleIcon = (ImageView) findViewById(R.id.push_toggle);
		mPushStatus = (TextView) findViewById(R.id.push_status);
		mPushStatus.setText(mReminderFlag ? getResources().getString(R.string.plugin_notify_on) : getResources().getString(R.string.plugin_notify_off));
		mPushToggleIcon.setImageResource(mReminderFlag ? R.drawable.round_open : R.drawable.round_closed);
		
		PluginInfo info = mPlugin.getPluginInfo();
//		mTitle.setText(info.name());
		mPluginName.setText(info.name());
		mPluginIcon.setImageResource(PluginHelper.getIcon(info.id(), PluginHelper.ICON_BIG));
//		DataManager.getInstance().getImage(info.icon(), new PluginIconListener(mPluginIcon,PluginIconListener.ICON_BIG));
		mPluginDescription.setText(info.desc());
		if(installed) {
			mPluginOnOffIcon.setImageResource(R.drawable.plugin_on);
			mPluginOnOffStatus.setText(getResources().getString(R.string.plugin_installed));
			mPluginUninstall.setVisibility(View.VISIBLE);	
			mClearHistory.setVisibility(View.VISIBLE);
			mPushToggle.setVisibility(View.VISIBLE);
			
		} else {
			mPluginOnOffIcon.setImageResource(R.drawable.plugin_off);
			mPluginOnOffStatus.setText(getResources().getString(R.string.plugin_notinstalled));	
			mPluginInstall.setVisibility(View.VISIBLE);
		}
		
		mPluginUninstall.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				createConfirmDialog(getResources().getString(R.string.plugin_confirm_uninstall),unstallListener).show();
//				
			}});
		mPluginInstall.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				showDialog(getResources().getString(R.string.plugin_installing));
				mPluginManager.installPluginWithPluginId(pluginId);
//				
			}}); 
		mClearHistory.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				createConfirmDialog(getResources().getString(R.string.plugin_clear_history),clearHistory).show();
			}});
		mPushToggle.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {	
				mPushSetFlag = true;
				onPushToggled();
			}
		});
		
		if (installed) {
			initCustomSettingView();
		}
	}
	
	private void initCustomSettingView() {
		LinearLayout container = (LinearLayout) findViewById(R.id.content);
		container.setVisibility(View.VISIBLE);
		mPlugin.initSettingView(container);
	}
	
	/**
	 * change when the button is clicked
	 * TODO
	 * */
	private synchronized void onPushToggled(){
		if (!mReminderFlag) {
			mReminderFlag = true;
			mPushStatus.setText(getResources().getString(R.string.plugin_notify_on));
			mPushToggleIcon.setImageResource(R.drawable.round_open);
		} else {
			mReminderFlag = false;
			mPushStatus.setText(getResources().getString(R.string.plugin_notify_off));
			mPushToggleIcon.setImageResource(R.drawable.round_closed);
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(mPushSetFlag){
			mPluginManager.settingPluginPush(pluginId, mReminderFlag);
			mPushSetFlag = false;
		}
		
	}

	private void onInstall(boolean success) {
		if(success) {	
//			Handler handler = new Handler();
//			handler.post(new Runnable(){
//
//				@Override
//				public void run() {
					mPluginOnOffIcon.setImageResource(R.drawable.plugin_on);
					mPluginOnOffStatus.setText(getResources().getString(R.string.plugin_installed));
					mPluginUninstall.setVisibility(View.VISIBLE);
					mPushToggle.setVisibility(View.VISIBLE);
					mPluginInstall.setVisibility(View.GONE);
					mClearHistory.setVisibility(View.VISIBLE);
					initCustomSettingView();
					dismissDialog();
//				}});
			
		} else {
			Toast.makeText(this, getResources().getString(R.string.plugin_install_failed), Toast.LENGTH_SHORT).show();
			dismissDialog();
		}
	}
	
	private void onUninstall(boolean success) {
		if(success) {
			
//			Handler handler = new Handler();
//			handler.post(new Runnable(){
//
//				@Override
//				public void run() {
					mPluginOnOffIcon.setImageResource(R.drawable.plugin_off);
					mPluginOnOffStatus.setText(getResources().getString(R.string.plugin_notinstalled));	
					mPushToggle.setVisibility(View.GONE);
					mPluginInstall.setVisibility(View.VISIBLE);
					mPluginUninstall.setVisibility(View.GONE);
					mClearHistory.setVisibility(View.GONE);
					
					LinearLayout container = (LinearLayout) findViewById(R.id.content);
					container.removeAllViews();
					container.setVisibility(View.VISIBLE);
					dismissDialog();
//				}});
			
			
		} else {
			Toast.makeText(this, getResources().getString(R.string.plugin_uninstall_failed), Toast.LENGTH_SHORT).show();
			dismissDialog();
		}
	}
	
	private void dismissDialog() {
		if(mDialog != null && mDialog.isShowing()) {
			mDialog.dismiss();
		}
	}
	
	private void showDialog(String dialogMessage){
		if(mDialog!=null){
			mDialog.setMessage(dialogMessage);
			mDialog.show();
		}
		
	}
	
	private Dialog createConfirmDialog(String dialogTitle,DialogInterface.OnClickListener clicked){
		AlertDialog.Builder buider = new AlertDialog.Builder(this);
		buider.setTitle(dialogTitle);
		buider.setPositiveButton(getResources().getString(R.string.plugin_sure),clicked);
		buider.setNegativeButton(getResources().getString(R.string.plugin_cancel), new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
        return buider.create();
		
	}
	
	DialogInterface.OnClickListener unstallListener = new DialogInterface.OnClickListener() {
		
		@Override
		public void onClick(DialogInterface dialog, int which) {
			dialog.dismiss();
			showDialog(getResources().getString(R.string.plugin_uninstalling));
			mPluginManager.uninstallPluginWithPluginId(pluginId);
		}
	};
	
	DialogInterface.OnClickListener clearHistory = new DialogInterface.OnClickListener() {
		
		@Override
		public void onClick(DialogInterface dialog, int which) {
			dialog.dismiss();
			showDialog(getResources().getString(R.string.plugin_clearing));
			mPlugin.clearHistory();
			dismissDialog();
		}
	};


	@Override
	public void onInsert(List<Plugin_Model> models) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDelete(int pluginId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onUpdate(List<Plugin_Model> models) {
		// TODO Auto-generated method stub
		if(models.size()==1 ){
			if(models.get(0).plugin_status==1){
				onInstall(true);
			}else if(models.get(0).plugin_status==0){
				onUninstall(true);
			}
		}
		
	}

	@Override
	public void onFailed() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPluginInstalledOver(boolean flagResult) {
		// TODO Auto-generated method stub
		onInstall(false);
	}

	@Override
	public void onPluginUninstalledOver(boolean flagResult) {
		// TODO Auto-generated method stub
		onUninstall(false);
	}

	@Override
	public void onSuccess() {
		// TODO Auto-generated method stub
		
	}
	
}
