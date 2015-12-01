package com.renren.mobile.chat.friends;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.LinkedList;
//import java.util.Map;
//import java.util.Stack;
//
//import android.app.Activity;
//import android.content.Context;
//import android.content.SharedPreferences;
//import android.content.pm.ApplicationInfo;
//import android.content.pm.PackageInfo;
//import android.content.pm.PackageManager;
//import android.content.pm.PackageManager.NameNotFoundException;
//import android.graphics.Bitmap;
//import android.graphics.drawable.BitmapDrawable;
//import android.graphics.drawable.Drawable;
//import android.preference.PreferenceManager;
//import android.util.DisplayMetrics;
//import android.widget.Toast;
//
//import com.renren.mobile.chat.R;
//import com.renren.mobile.chat.cache.NewImagePool;
//import com.renren.mobile.chat.common.Methods;
//import com.renren.mobile.chat.json.JsonObject;
//import com.renren.mobile.chat.net.ServiceProvider;
//import com.renren.mobile.chat.ui.BaseActivity;
//import com.renren.mobile.chat.ui.chat.ChatNotificationDataModel;
//
///**
// * 
// * @author rubin.dong@renren-inc.com
// *
// */
//
//public class Variables {
//	
//	public static interface Settings {
//		/**
//		 * 图片请求线程数
//		 */
//		int IMG_THREAD_NUM = 3;
//		
//		/**
//		 * 文本请求线程数
//		 */
//		int TEXT_THREAD_NUM = 3;
//	
//		final public String PREF = "MY_PREF";
//	}
//
//	/**非法经纬度**/
//	final public static long LATLONDEFAULT=255000000;
//
//	public static float density;// 屏幕密度（0.75 / 1.0 / 1.5）
//	public static int screenResolution; //屏幕分辨率
//	public static int screenWidth;
//	public static long user_id;
//	public static String user_name;
//	public static String account;
//	public static String password;
//	public static Context context;
//	public static String ua="";
//	public static Toast gToast;
//	public static boolean firstRun = false;
//	public static int noticeCount;
//	public static int friendsRequestCount = 0;
//	public static String versionName;
//	public static String subproperty;
//	public static int from;
//	public static int pubdate;
//	public static int softid;
//	public static String apikey;
//	public static String secretkey;
//	public static int appid;
//	public static String ticket;
//	public static boolean showToast;
//	public static long startTime=0;
//	public static final int mNotificationId="renren_android_1.6".hashCode();
//	public static int mPhotoUploadSucceedCount=0;
//	public static int mPhotoUploadFailCount=0;
//	public static int mPhotoUploadCount=0;
//	public static Object lockObject=new Object();
//	public static Bitmap headImageDefault=null;
//	public static int locateType=0;
//	public static int deflectType=1;
//	public static boolean backgroundRun=false;
//	public static JsonObject userInfo;
//	public static String lastAccount="";
//	public static String screen = "";
//	public static boolean showNewsFeedPhoto = true;
//	public static Map<String, Drawable> emonticonsMap=new HashMap<String, Drawable>();
//	public static long  albumId=-1L;
//	public static String albumTitle="";
//	public static long current_chat_uid=0;
//	//add by dingwei.chen
//	public static String sSessionId = "";
//	public static ChatNotificationDataModel chatNoitificationData = new ChatNotificationDataModel();
//	public static LinkedList<Object> sessionList=new LinkedList<Object>();
//	public static Boolean isChatSession=false;
//	public static Boolean isChatActivity = false;
//	public static long rid=0;
//	public static long login_count = 0;
//	public static long currenSession;
//	public static Activity activity;
//	public static String mFileName = "";
//	//=.=!
//	public static boolean isCanVoice;
//	
//	ArrayList list;
//	LinkedList l;
////	public static LocationService testService;
//	
////	public static ArrayList<NewsFeedDataItem> newsfeedArrayList = new ArrayList<NewsFeedDataItem>();
//	public static HashMap<Long, String> chatUuids = new HashMap<Long, String>();
//	
//	//add by dingwei.chen
////	public static Map<Long,ChatListAdapter> sChatAdapter = new HashMap<Long, ChatListAdapter>();
//	public static long sNow_Chat_UserId = 0;//当前聊天窗口的用户ID
//	
//	
//	public static void clear() {
//		user_id = 0;
//		user_name = "";
//		account = "";
//		password = "";
//		ticket = "";
//		ServiceProvider.session_key = "";
//		chatUuids.clear();
////		newsfeedArrayList.clear();
//	}
//	
//	public static Stack<com.renren.mobile.chat.ui.BaseActivity> activityStack = new Stack<com.renren.mobile.chat.ui.BaseActivity>();
//
//	public static void init(Context con) {
//
//		String packageName = con.getPackageName();
//		PackageManager pm = con.getPackageManager();
//		try {
//			context = con;
//			BitmapDrawable imageDrawable = (BitmapDrawable)context.getResources().getDrawable(R.drawable.widget_default_head);
//			Bitmap image = imageDrawable.getBitmap();
//			headImageDefault = NewImagePool.getRoundedCornerBitmap(image);
//			from = Integer.parseInt(context.getResources().getString(R.string.from));
//			pubdate = Integer.parseInt(context.getResources().getString(R.string.pubdate));
//			softid = Integer.parseInt(context.getResources().getString(R.string.softid));
//			appid = Integer.parseInt(context.getResources().getString(R.string.appid));
//			apikey = context.getResources().getString(R.string.apikey);
//			secretkey = context.getResources().getString(R.string.secretkey);
//			subproperty = context.getResources().getString(R.string.subproperty);
//			PackageInfo info = pm.getPackageInfo(packageName, 0);
//			versionName = info.versionName ;
//			SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
//			showNewsFeedPhoto = prefs.getBoolean("bt_show_newsfeed_photo", true);
//			ApplicationInfo appInfo = pm.getApplicationInfo(context.getPackageName(), PackageManager.GET_UNINSTALLED_PACKAGES);
//			Methods.logMode = appInfo != null && (appInfo.flags & ApplicationInfo.FLAG_DEBUGGABLE) == 0;
//		} catch (NameNotFoundException e) {
//			e.printStackTrace();
//		}
//	}
//	
//	public static void setDensity(BaseActivity activity) {
//
//		if (density != 0 || activity == null) {
//			return;
//		}
//		DisplayMetrics metric = new DisplayMetrics();
//
//		activity.getWindowManager().getDefaultDisplay().getMetrics(metric);
//		density = metric.density; // 屏幕密度（0.75 / 1.0 / 1.5）
//		screenResolution = metric.widthPixels * metric.heightPixels;
//		screenWidth = metric.widthPixels;
//		screen = "" + metric.widthPixels + "*" + metric.heightPixels;
//
//	}
//
//	
//}
