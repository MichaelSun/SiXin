package com.common.app;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.os.MessageQueue;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Printer;

import com.common.R;
import com.common.binder.LocalBinderPool;
import com.common.manager.LoginManager;
import com.common.manager.LoginManager.LoginInfo;
import com.common.manager.LoginManager.LoginListener;
import com.common.utils.ClazzUtils;
import com.core.util.AbstractApplication;
import com.core.util.CommonUtil;
import com.core.util.SystemService;

/**
 * 
 * */
public abstract class AbstractRenrenApplication extends AbstractApplication implements LoginListener{

	/**
	 * 应用程序配置
	 * */
	public static String FROM;
	public static String PUBLIC_DATE;
	public static String SOFT_ID;
	public static String APP_ID;
	public static String VERSION_NAME;
	public static String SUB_PROPERTY;
	public static String API_KEY;
	public static String SCERET_KEY;
	public static String TICKET;
	public static long USER_ID;
	public static Bitmap DEFAULT_HEAD_BITMAP = null;
//	public static BitmapDrawable DEFAULT_HEAD = null;
	public static String SESSION_ID;
	public static String VERSION;
	public static boolean sIsLogin;
	
	/**
	 * 客户端在后头驻留期间，访问任意接口，都要增加该参数
	 */
	public static int IS_AUTO = IS_AUTO_VALUE.BACKGROUND;//统计使用
	public static interface IS_AUTO_VALUE{
		int FOREGROUND = 0;
		int BACKGROUND = 1;
	}
	
	public static String SCREEN = null;
	public static String PACKAGE_NAME = null;
	public static DisplayMetrics metric =  null; //add by jia.xia
	
	@Override
	public void initAfterOnCreate() {
		SystemService.sPackageManager = this.getPackageManager();
//		DEFAULT_HEAD = (BitmapDrawable) getAppContext().getResources().getDrawable(R.drawable.default_head_image);
		DEFAULT_HEAD_BITMAP = BitmapFactory.decodeResource(getAppContext().getResources(), R.drawable.default_head_image);
//		DEFAULT_HEAD_BITMAP = DEFAULT_HEAD.getBitmap();
		metric = new DisplayMetrics();
		SystemService.sWindowsManager.getDefaultDisplay().getMetrics(metric);
		SCREEN =  "" + metric.widthPixels + "*"+ metric.heightPixels;
		try {
			PackageInfo info = SystemService.sPackageManager.getPackageInfo(getPackageName(), 0);
			VERSION_NAME = info.versionName;
		} catch (NameNotFoundException e) {}
		LoginManager.getInstance().registorLoginListener(this);
		
		String packageName = getApplicationContext().getPackageName();
		if(TextUtils.isEmpty(packageName)){
			PACKAGE_NAME = getPackageName();
		}else {
			PACKAGE_NAME = packageName;
		}
	}
	public abstract String getPackageName();
	public void onLogin(LoginInfo info){
		USER_ID = info.mUserId;
		sIsLogin = true;
		
	}
	public void onLogout(){
		try {
			if(LocalBinderPool.getInstance().isContainBinder())
				LocalBinderPool.getInstance().obtainBinder().disConnect();
			LoginManager.getInstance().clean();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		USER_ID = -1;
		sIsLogin = false;
	}
	public static void initPrinter(){
		HANDLER.getLooper().setMessageLogging(new LogPrinter());
	}
	
	
	public static void printQueue(){
		try {
			
			MessageQueue queue = (MessageQueue)ClazzUtils.getField("mQueue", Handler.class, HANDLER);
			Message mCurrent = (Message)ClazzUtils.getField("mMessages", MessageQueue.class, queue);
			CommonUtil.log("queue", "---"+mCurrent.getCallback());
			CommonUtil.waitTime(30);
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}
	public static class LogPrinter implements Printer{

		private String className= null;
		private long time = 0l;
		String msgCode = null;
		@Override
		public void println(String x) {
			x = x.trim();
			if(x.startsWith(">>>")){
				time = System.currentTimeMillis();
				String[] ss = x.split("[:]");
				msgCode = ss[1];
				ss = ss[0].split("[}]");
				className = ss[1];
				className=className.substring(className.lastIndexOf(".")+1);
			}else{
				CommonUtil.log("queuem", className+" pro "+msgCode+" :"+(System.currentTimeMillis()-time)+"ms");
			}
			
			
			
		}
	}
	
	
}
