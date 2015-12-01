package com.renren.mobile.chat.webview;
import java.net.URLDecoder;
import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.common.manager.LoginManager;
import com.common.manager.LoginManager.LoginStatusListener;
import com.common.mcs.McsServiceProvider;
import com.common.utils.Methods;
import com.core.json.JsonObject;
import com.renren.mobile.chat.R;
import com.renren.mobile.chat.RenrenChatApplication;
import com.renren.mobile.chat.base.util.SystemUtil;
import com.renren.mobile.chat.friends.FlashChecker;
import com.renren.mobile.chat.friends.RenRenWebPluginSetting;
import com.renren.mobile.chat.friends.RenRenWebSetting;
import com.renren.mobile.chat.ui.BaseActivity;
import com.renren.mobile.chat.ui.BaseScreen;
import com.renren.mobile.chat.view.BaseTitleLayout;
public class RenRenWebView extends BaseActivity {
	/** Called when the activity is first created. */
	protected WebView web;
	private static int minimumFontSize = 8;
	private static int minimumLogicalFontSize = 8;
	private static int defaultFontSize = 16;
	private static int defaultFixedFontSize = 13;
	private Button title_left, title_right;
	private Button bottom_left, bottom_right;
	private ProgressBar bar;
	private int loadingTimeout = 30;
	private int progress;
	private stoploading stopload;
	private boolean needDecode=true;
	private RelativeLayout  bottom;
	private BaseTitleLayout mTitle;
	private BaseScreen mainScreen;
	private RelativeLayout r1;
	private String url;
	private String titleMiddle;
	private int urlType;
	private long uid;
	int count = 0;
	class stoploading implements Runnable {
		public void run() {
			if (progress <= 10) {
			}
			if (bar != null) {
				bar.setVisibility(View.GONE);
			}
		}
	}
	public class RenRenWebViewClient extends WebViewClient {
		@Override
		public boolean shouldOverrideUrlLoading(final WebView view, final String url) {
			if (url.startsWith("tel:")) {
				Uri uri = Uri.parse(url);
				Intent it = new Intent(Intent.ACTION_DIAL, uri);
				startActivity(it);
			}
			else if (url.indexOf(".apk") != -1) {
				Uri uri = Uri.parse(url);
				Intent it = new Intent(Intent.ACTION_VIEW, uri);
				startActivity(it);
			}else{
				WebView.enablePlatformNotifications();
				view.loadUrl(url);
				progress = 0;
				bar.setVisibility(View.VISIBLE);
				loadTimeout.postAtTime(stopload, android.os.SystemClock.uptimeMillis()
						+ loadingTimeout * 1000);
			}
			return true;
		}
	}
	public class RenRenWebChromeClient extends WebChromeClient {
		@Override
		public void onProgressChanged(WebView view, int newProgress) {
			super.onProgressChanged(view, newProgress);
			if (newProgress >= 80) {
				loadTimeout.removeCallbacks(stopload);
				if (bar != null && newProgress >= 100) {
					bar.setVisibility(View.GONE);
					mTitle.setTitleRefreshProgressVisibility(false);
				}
				setNavigationState();
			} else {
				if (bar != null) {
					bar.setVisibility(View.VISIBLE);
				}
			}
			progress = newProgress;
			if (bar != null) {
				bar.setProgress(newProgress);
			}
		}
		@Override
		public void onReceivedTitle(WebView view, String title) {
			super.onReceivedTitle(view, title);
		}
	}
	private Handler loadTimeout = new Handler() {
		public void handleMessage(Message msg) {
		}
	};
	protected void onDestroy() {
		manageCookie();
		web.clearCache(true);
		web.setVisibility(View.GONE);
		web.destroy();
		super.onDestroy();
	};
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}
	/*
	 * titleLeft titleBar左按钮文案
	 * 
	 * titleMiddle titleBar标题文案
	 * 
	 * url 需要加载的url
	 */
	public static void show(Context context, String titleLeft,
			String titleMiddle, int urlType) {
		Intent intent = new Intent(context, RenRenWebView.class);
		intent.putExtra("titleLeft", titleLeft);
		intent.putExtra("titleMiddle", titleMiddle);
		intent.putExtra("url_type", urlType);
		context.startActivity(intent);
	}
	
	public static void show(Context context, String titleLeft,
			String titleMiddle, String url) {
		Intent intent = new Intent(context, RenRenWebView.class);
		intent.putExtra("titleLeft", titleLeft);
		intent.putExtra("titleMiddle", titleMiddle);
		intent.putExtra("url_type", RenRenWapUrlFactory.URL_TYPE.NO_URL);
		intent.putExtra("url", url);
		context.startActivity(intent);
	}
	/**
	 * 跳转到个人新鲜事webview
	 * @param context
	 * @param uid
	 * @param titleLeft
	 * @param titleMiddle
	 * @param urlType
	 */
	public static void show(Context context, long uid, String titleLeft,
			String titleMiddle, int urlType) {
		Intent intent = new Intent(context, RenRenWebView.class);
		intent.putExtra("uid", uid);
		intent.putExtra("titleLeft", titleLeft);
		intent.putExtra("titleMiddle", titleMiddle);
		intent.putExtra("url_type", urlType);
		context.startActivity(intent);
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		stopload = new stoploading();
		initView();
		manageCookie();
		getData();
		onConfigurationChanged(this.getResources().getConfiguration());
	}
	
	public static CookieManager sCookieManager = null;
	
	private void manageCookie() {
		CookieSyncManager csm = CookieSyncManager.createInstance(web.getContext());
		sCookieManager = CookieManager.getInstance();
		sCookieManager.setAcceptCookie(true); 
		csm.sync();
	}
	
	public BaseScreen getScreen() {
		return mainScreen;
	}
	private void initView() {
		mainScreen = new BaseScreen(this);
		r1 = (RelativeLayout) getLayoutInflater().inflate(R.layout.web_main,
				null);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				ViewGroup.LayoutParams.FILL_PARENT,
				ViewGroup.LayoutParams.FILL_PARENT,(float) 1.0);
		mainScreen.getScreenView().addView(r1, lp);
		setContentView(mainScreen.getScreenView());
		web = (WebView) findViewById(R.id.webview);
		web.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
		setWebView();
		bottom = (RelativeLayout) findViewById(R.id.bottom);
		title_left = new Button(this);
		title_right = new Button(this);
		title_right.setBackgroundResource(R.drawable.title_refresh_bg);
		bottom_right = (Button) findViewById(R.id.right);
		bottom_left = (Button) findViewById(R.id.left);
		bar = (ProgressBar) findViewById(R.id.progress_bar);
		hookTitleLeft();
		hookTitleRight();
		hookBottomLeft();
		hookBottomRight();
		bar.setVisibility(View.VISIBLE);
		WebView.enablePlatformNotifications();
		bottom_left.setEnabled(false);
		bottom_left.setBackgroundResource(R.drawable.button_back_selector);
		bottom_right.setEnabled(false);
		bottom_right.setBackgroundResource(R.drawable.button_forward_selector);
		mTitle = mainScreen.getTitle();
		mTitle.setTitleButtonLeftBackVisibility(true);
		mTitle.setTitleRefreshButtonVisibility(true);
		Button menu  = mTitle.getTitleRightRefreshButton();
		mTitle.setTitleButtonLeftBackListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		menu.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mTitle.setTitleRefreshProgressVisibility(true);
				if (web != null) {
					web.reload();
				}
			}
		});
	}
	private void getData() {
		Intent intent = getIntent();
		if (intent != null) {
			this.uid = intent.getLongExtra("uid", 0);
			this.titleMiddle = intent.getStringExtra("titleMiddle");
			this.needDecode=intent.getBooleanExtra("needDecode", true);
			urlType = intent.getIntExtra("url_type", -1);
			if(urlType == RenRenWapUrlFactory.URL_TYPE.NO_URL){
				url = getIntent().getStringExtra("url");
			}else if(urlType != -1){
				if(this.needDecode){
					this.url = URLDecoder.decode(RenRenWapUrlFactory.getUrl(urlType,this.uid));
				}else{
					this.url = RenRenWapUrlFactory.getUrl(urlType,this.uid);
				}				
			}else{
				Toast.makeText(this, RenrenChatApplication.getmContext().getResources().getString(R.string.RenRenWebView_java_1), Toast.LENGTH_SHORT).show();		//RenRenWebView_java_1=您访问的资源不存在; 
				this.finish();
				return ;
			}
		}
		setView();
	}
	/*
	 * 可能导致Flash无法顺利播放的原因之一：
	 * frameworks/base/media/libstagefright/OMXCodec.cpp中
	 * 2110 CHECK_EQ(
	 * 2111   countBuffersWeOwn(mPortBuffers[kPortIndexInput]),
	 * 2112   mPortBuffers[kPortIndexInput].size());
	 */
	private void setView() {
		if (!TextUtils.isEmpty(url)) {
			if (url.endsWith(".swf")) {
				List<String> warnings = new LinkedList<String>();
				FlashChecker flashChecker = new FlashChecker(this);
				if (flashChecker.test(FlashChecker.LOW_ANDROID_VERSION)) {
					warnings.add(RenrenChatApplication.getmContext().getResources().getString(R.string.RenRenWebView_java_2));		//RenRenWebView_java_2=您的rom版本过低，rom版本达到2.2以上才能正常观看视频; 
				}
				if (flashChecker.test(FlashChecker.FLASH_PLAYER_NOT_FOUND)) {
					warnings.add(RenrenChatApplication.getmContext().getResources().getString(R.string.RenRenWebView_java_3));		//RenRenWebView_java_3=您没有安装Adobe Flash Player，无法正常播放网页视频，请安装后重试; 
				}
				if (flashChecker.test(FlashChecker.LOW_SYSTEM_MEMORY)) {
					warnings.add(RenrenChatApplication.getmContext().getResources().getString(R.string.RenRenWebView_java_4));		//RenRenWebView_java_4=您的手机当前内存过低，建议您关掉一些其他应用或重启手机后重试; 
				}
				if (flashChecker.test(FlashChecker.CHECK_SYSTEM_MEMORY_FAILED)) {
					warnings.add(RenrenChatApplication.getmContext().getResources().getString(R.string.RenRenWebView_java_5));		//RenRenWebView_java_5=检测操作系统内存大小失败，请重启手机后重试; 
				}
				if (flashChecker.test(FlashChecker.LOW_ARM_VERSION)) {
					warnings.add(RenrenChatApplication.getmContext().getResources().getString(R.string.RenRenWebView_java_6));		//RenRenWebView_java_6=很抱歉，您的手机不支持播放网页视频; 
				}
				if (flashChecker.test(FlashChecker.CHECK_ARM_VERSION_FAILED)) {
					warnings.add(RenrenChatApplication.getmContext().getResources().getString(R.string.RenRenWebView_java_6));		//RenRenWebView_java_6=很抱歉，您的手机不支持播放网页视频; 
				}
				if(!warnings.isEmpty()) {
					SystemUtil.toast(warnings.get(0));
				}
				web.getSettings().setUseWideViewPort(false);
				web.setInitialScale(150);
				bottom.setVisibility(View.GONE);
			}
			web.loadUrl(url);
		}
		title_left.setText("");
		if (!TextUtils.isEmpty(titleMiddle)) {
			mTitle.setTitleMiddle(titleMiddle);
		}
	}
	private void hookTitleLeft() {
		title_left.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				RenRenWebView.this.finish();
			}
		});
	}
	private void hookTitleRight() {
		title_right.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (web != null) {
					web.reload();
				}
			}
		});
	}
	private void setNavigationState(){
		if(web != null && web.canGoBack()){
			bottom_left.setEnabled(true);
			bottom_left.setBackgroundResource(R.drawable.button_back_selector);
		}else{
			bottom_left.setEnabled(false);
			bottom_left.setBackgroundResource(R.drawable.button_back_disable);
		}
		if(web != null && web.canGoForward()){
			bottom_right.setEnabled(true);
			bottom_right.setBackgroundResource(R.drawable.button_forward_selector);
		}else{
			bottom_right.setEnabled(false);
			bottom_right.setBackgroundResource(R.drawable.button_forward_disable);
		}
	}
	private void hookBottomRight() {
		bottom_right.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (web != null && web.canGoForward()) {
					web.goForward();
					setNavigationState();
				}
			}
		});
	}
	private void hookBottomLeft() {
		bottom_left.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (web != null && web.canGoBack()) {
					web.goBack();
					setNavigationState();
				}
			}
		});
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK) ) {			
			if(web.canGoBack()){
				web.goBack();
				return true;
			}else{
			}
			finish();
			return true;
		}else{
			return super.onKeyDown(keyCode, event);
		}
	}
	public void setWebView() {
		WebSettings settings = web.getSettings();
		settings.setLoadsImagesAutomatically(true);
		settings.setPluginsEnabled(true);
		settings.setDomStorageEnabled(true);
		settings.setSaveFormData(false);
//		settings.setLightTouchEnabled(true);  
		settings.setSavePassword(false);
		settings.setJavaScriptEnabled(true);
		settings.setSupportZoom(true);
		settings.setCacheMode(WebSettings.LOAD_NORMAL);
//		settings.setLightTouchEnabled(true);  
		settings.setNavDump(true);
		settings.setMinimumFontSize(minimumFontSize);
		settings.setMinimumLogicalFontSize(minimumLogicalFontSize);
		settings.setDefaultFontSize(defaultFontSize);
		settings.setDefaultFixedFontSize(defaultFixedFontSize);
		settings.setTextSize(WebSettings.TextSize.NORMAL);
		settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
		if (Build.VERSION.SDK_INT >= 7) {
			RenRenWebSetting.setWebSettingsAppCacheEnabled(settings);
		}
		if (Build.VERSION.SDK_INT >= 8) {
			RenRenWebPluginSetting.setWebSettingsAppPluginState(settings);
		}
		settings.setAllowFileAccess(true);
		settings.setBuiltInZoomControls(true);
		web.setWebViewClient(new RenRenWebViewClient());
		web.setWebChromeClient(new RenRenWebChromeClient());
	}
	@Override
	public void onLowMemory() {
		web.clearCache(true);
		super.onLowMemory();
	}
}
