package com.renren.mobile.chat.crash;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Environment;

import com.common.mcs.INetRequest;
import com.common.mcs.INetResponse;
import com.common.mcs.McsServiceProvider;
import com.common.statistics.BackgroundUtils;
import com.common.statistics.Htf;
import com.common.utils.Methods;
import com.core.json.JsonValue;
import com.renren.mobile.chat.RenrenChatApplication;
import com.renren.mobile.chat.R;
import com.renren.mobile.chat.base.util.SystemUtil;
public class CrashHandler implements UncaughtExceptionHandler {
	public static final String TAG = "CrashHandler";
	private Thread.UncaughtExceptionHandler mDefaultHandler;
	private static CrashHandler INSTANCE = new CrashHandler();
	private Context mContext;
	private Map<String, String> infos = new HashMap<String, String>();
	private DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm");
	/** 保证只有一个CrashHandler实例 */
	private CrashHandler() {
	}
	/** 获取CrashHandler实例 ,单例模式 */
	public static CrashHandler getInstance() {
		return INSTANCE;
	}
	/**
	 * 初始化
	 * 
	 * @param context
	 */
	public void init(Context context) {
		mContext = context;
		mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
		Thread.setDefaultUncaughtExceptionHandler(this);
	}
	/**
	 * 当UncaughtException发生时会转入该函数来处理
	 */
	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		SystemUtil.f_log("crash handler");
		
		// crash statistics
		BackgroundUtils.getInstance().multiChatStatistics(Htf.COLLAPSE);
		
		handleException(ex);
		if (mDefaultHandler != null && mDefaultHandler != this) {
			// 调用系统的处理异常方法
			mDefaultHandler.uncaughtException(thread, ex);
		} else {
			RenrenChatApplication.clearStack();
			RenrenChatApplication.HANDLER.postDelayed(new Runnable() {
				
				@Override
				public void run() {
					System.exit(1);
				}
			}, 3000);
//			try {
//				Thread.sleep(3000);
//			} catch (InterruptedException e) {
//			}
		}
	}
	/**
	 * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成.
	 * 
	 * @param ex
	 * @return true:如果处理了该异常信息;否则返回false.
	 */
	private boolean handleException(final Throwable ex) {
		if (ex == null) {
			return false;
		}
		SystemUtil.toast(RenrenChatApplication.getmContext().getResources().getString(R.string.CrashHandler_java_1));		//CrashHandler_java_1=很抱歉,程序出现异常,即将退出; 
		eventLog(ex.toString(),getInformation(ex));
		collectDeviceInfo(mContext);
		saveCrashInfo2File(ex);
		/*add by dingwei.chen 如果不加这句会影响测试*/
		return true;
	}
	private void eventLog(String ex,String sb) {
		INetResponse response = new INetResponse() {
			
			@Override
			public void response(INetRequest req, JsonValue obj) {
				SystemUtil.f_log("crash hander response:" + obj.toJsonString());
			}
		};
		McsServiceProvider.getProvider().postLog(sb, ex, response);
	}
	/**
	 * 收集设备参数信息
	 * 
	 * @param ctx
	 */
	public void collectDeviceInfo(Context ctx) {
		try {
			PackageManager pm = ctx.getPackageManager();
			PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(), PackageManager.GET_ACTIVITIES);
			if (pi != null) {
				String versionName = pi.versionName == null ? "null" : pi.versionName;
				String versionCode = pi.versionCode + "";
				infos.put("versionName", versionName);
				infos.put("versionCode", versionCode);
			}
		} catch (NameNotFoundException e) {
		}
		Field[] fields = Build.class.getDeclaredFields();
		for (Field field : fields) {
			try {
				field.setAccessible(true);
				infos.put(field.getName(), field.get(null).toString());
			} catch (Exception e) {
			}
		}
	}
	/**
	 * 保存错误信息到文件中
	 * 
	 * @param ex
	 * @return 返回文件名称,便于将文件传送到服务器
	 */
	private String saveCrashInfo2File(Throwable ex) {
		StringBuffer sb = new StringBuffer();
		for (Map.Entry<String, String> entry : infos.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();
			sb.append(key + "=" + value + "\n");
		}
		Writer writer = new StringWriter();
		PrintWriter printWriter = new PrintWriter(writer);
		ex.printStackTrace(printWriter);
		Throwable cause = ex.getCause();
		while (cause != null) {
			cause.printStackTrace(printWriter);
			cause = cause.getCause();
		}
		printWriter.close();
		String result = writer.toString();
		sb.append(result);
		try {
			String time = formatter.format(new Date());
			String fileName = "crash-" + time + ".log";
			if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
				String path = "/sdcard/crash/";
				File dir = new File(path);
				if (!dir.exists()) {
					dir.mkdirs();
				}
				FileOutputStream fos = new FileOutputStream(path + fileName);
				fos.write(sb.toString().getBytes());
				fos.close();
			}
			return fileName;
		} catch (Exception e) {
		}
		return null;
	}
	private String getInformation(Throwable ex){
		StringBuffer sb = new StringBuffer();
		for (Map.Entry<String, String> entry : infos.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();
			sb.append(key + "=" + value + "\n");
		}
		Writer writer = new StringWriter();
		PrintWriter printWriter = new PrintWriter(writer);
		ex.printStackTrace(printWriter);
		Throwable cause = ex.getCause();
		while (cause != null) {
			cause.printStackTrace(printWriter);
			cause = cause.getCause();
		}
		printWriter.close();
		String result = writer.toString();
		sb.append(result);
		return sb.toString();
	}
}
