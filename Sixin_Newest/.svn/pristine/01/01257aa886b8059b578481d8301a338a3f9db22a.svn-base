package com.renren.mobile.chat.ui;

import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import com.common.binder.LocalBinderPool;
import com.common.emotion.manager.EmotionManager;
import com.common.statistics.LocalStatisticsManager;
import com.renren.mobile.account.LoginControlCenter;
import com.renren.mobile.chat.R;
import com.renren.mobile.chat.RenrenChatApplication;
import com.renren.mobile.chat.common.RRSharedPreferences;
import com.renren.mobile.chat.crash.CrashHandler;
import com.renren.mobile.chat.ui.notification.MessageNotificationManager;

/**
 * @author kaining.yang
 */
public final class FirstActivity extends BaseActivity implements Runnable{
    
    /**
     * 第一次登录需要显示欢迎界面，显示欢迎界面的时间
     */
    private int waitTime = 2 * 1000;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	
    	//激活统计
    	LocalStatisticsManager.getInstance().sendActivityRequest();
    	//LocalStatisticsManager.getInstance().activityClient();
    	MessageNotificationManager.getInstance().clearSingleLoginNotification(this);
    	
    	RenrenChatApplication.setThird(false);
    	// crash log catch
    	CrashHandler.getInstance().init(FirstActivity.this);
    	
		try {
			if(LocalBinderPool.getInstance().isContainBinder() && LocalBinderPool.getInstance().obtainBinder().isServiceRunning()){
				waitTime = 0;
				run();
			}else{
				getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN); 
				setContentView(R.layout.ykn_first_layout);
				new Handler().postDelayed(this, waitTime);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 每次进入都要显示欢迎界面
		// add by jia.xia(加载表情数据库)
		EmotionManager.getInstance().loadEmotionPackage();
    }
	
	public void run() {
		LoginControlCenter.getInstance().pageJump(FirstActivity.this,new RRSharedPreferences(FirstActivity.this));
		finish();
	}
}