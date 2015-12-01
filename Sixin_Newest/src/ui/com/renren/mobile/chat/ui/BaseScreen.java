package com.renren.mobile.chat.ui;

import android.app.Activity;
import android.support.v4.app.FragmentActivity;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.core.util.SystemService;
import com.renren.mobile.chat.R;
import com.renren.mobile.chat.RenrenChatApplication;
import com.renren.mobile.chat.view.BaseTitleLayout;

/**
 * 继承此类可覆写方法如下： onShow() refresh() returnTop() clearCache() createMenu()
 * optionsMenu() onKeyDown()
 * 
 * @author tian.wang
 * */
public class BaseScreen {
	protected Activity mActivity;
	private LinearLayout mScreenRootLayout;
	private BaseTitleLayout mTitle;
	private RelativeLayout mRelativeLayout;
	protected LayoutInflater mInflater;
	protected MenuInflater mMenuInflater;
	protected boolean mIsFromThird = false;

	public void setIsFromThird(boolean isFromThird) {
		this.mIsFromThird = isFromThird;
		BaseTitleLayout title = getTitle();
		title.setTitleButtonLeftOtherVisibility(isFromThird);
		title.setTitleButtonLeftOtherRenren();
		if(isFromThird){
			title.setTitleButtonLeftOtherListener(new View.OnClickListener() {
				public void onClick(View v) {
					RenrenChatApplication.onThirdCallExit();
					mActivity.finish();
				}
			});
		}
	}
	
	public BaseScreen(Activity activity) {
		this.mActivity = activity;
		this.mTitle = new BaseTitleLayout(this.mActivity);
		mInflater = LayoutInflater.from(this.mActivity);
		mMenuInflater = mActivity.getMenuInflater();
		mRelativeLayout = new RelativeLayout(this.mActivity);
		mScreenRootLayout = (LinearLayout) mInflater.inflate(R.layout.screen,
				null);
		mScreenRootLayout.addView(mTitle.getView(),
				new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.FILL_PARENT,
						LinearLayout.LayoutParams.WRAP_CONTENT, 0));
		
		if(FragmentActivity.class.isInstance(activity)){
			mTitle.setTitleButtonLeftBackVisibility(false);
		}
	}

	/**
	 * 将screen的内容区包装在RelativeLayout内，这样做的目的主要是解决listview中的adapter刷新的时候不重绘父类 *
	 * 使用者只需要将除title以外的显示view或者layout作为参数传递给此函数就OK了。。
	 * 
	 * @author daye.wang
	 * */
	public void setListView(ListView view) {
		mRelativeLayout.addView(view, new RelativeLayout.LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		mScreenRootLayout.addView(mRelativeLayout,
				new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT,
						LayoutParams.FILL_PARENT, 1));
	}

	/**
	 * 如果添加到screen中的view不是listview，可直接添加到根目录中
	 * 
	 * @author daye.wang
	 * */
	public void setContent(View view) {
		FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
				FrameLayout.LayoutParams.MATCH_PARENT,
				FrameLayout.LayoutParams.MATCH_PARENT);
		
		FrameLayout frameLayout = new FrameLayout(view.getContext());
		View v = new View(view.getContext());
		v.setBackgroundResource(R.drawable.title_shadow);
		
		frameLayout.setLayoutParams(layoutParams);
		frameLayout.addView(view, layoutParams);
		frameLayout.addView(v, new LayoutParams(LayoutParams.MATCH_PARENT, (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, mActivity.getResources().getDisplayMetrics())));
		
		mScreenRootLayout.addView(frameLayout, new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.FILL_PARENT, 1));
	}

	/**
	 * 获取screen对应的title，title的具体操作在BaseTitleLayout类中
	 * 
	 * @author daye.wang
	 * */
	public BaseTitleLayout getTitle() {
		return mTitle;
	}

	/**
	 * 获取screen对应的布局主Layout
	 * 
	 * @author daye.wang
	 * */
	public ViewGroup getScreenView() {
		return mScreenRootLayout;
	}

	/************************************* 继承此类需要覆盖的方法 *****************************************************/
	/**
	 * 展现界面 注意区分第一次展现与加载数据后展现
	 * 
	 * @author daye.wang
	 * */
	public void onShow() {

	}

	/**
	 * 刷新
	 * 
	 * @author daye.wang
	 * */
	public void refresh() {

	}

	/**
	 * 返回列表顶部
	 * 
	 * @author daye.wang
	 * */
	public void returnTop() {

	}

	/**
	 * 清空缓存数据
	 * 
	 * @author daye.wang
	 * */
	public void clearCache() {

	}

	/**
	 * 创建menu菜单
	 * 
	 * @author daye.wang
	 * */
	public void createMenu(Menu menu) {
	}

	/**
	 * 处理menu菜单事件
	 * 
	 * @author daye.wang
	 * */
	public void optionsMenu(MenuItem item) {

	}

	/**
	 * 处理按键事件
	 * */
	public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
		return false;
	}

	/****************************************************************************************************************/

	public void clear() {
		clearCache();
		mActivity = null;
		if (null != mRelativeLayout) {
			mRelativeLayout.removeAllViews();
			mScreenRootLayout.removeAllViews();
			mRelativeLayout = null;
			mScreenRootLayout = null;
		}
		this.mTitle = null;
	};

	public void finish() {
		mActivity.finish();
	}

	/**
	 * @author liuchao 当界面重新出现时，调用此方法
	 */
	public void onResume() {

	}

	/**
	 * @author liuchao 当界面被覆盖时，调用此方法
	 */
	public void onPause() {

	}

	/**
	 * @author xiangchao.fan 设置无标题栏
	 */
	public void noTitle() {
		mScreenRootLayout.removeView(mTitle.getView());
	}

	public void setProgressBarVisible(final boolean flag) {
		RenrenChatApplication.sHandler.post(new Runnable() {
			public void run() {
				mTitle.setTitleRefreshButtonVisibility(flag);
			}
		});
	}

	public void hideInputMethod(View view) {
		if (view != null && view.getWindowToken() != null) {
			SystemService.sInputMethodManager.hideSoftInputFromWindow(
					view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}

}
