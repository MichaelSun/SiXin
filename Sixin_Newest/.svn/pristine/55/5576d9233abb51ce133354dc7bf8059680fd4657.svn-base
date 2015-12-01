package com.renren.mobile.chat.ui.contact;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.common.utils.PinyinSearch;
import com.common.utils.SearchAble;
import com.core.util.SystemService;
import com.core.util.ViewMapUtil;
import com.renren.mobile.chat.R;
import com.renren.mobile.chat.RenrenChatApplication;
import com.renren.mobile.chat.base.util.SystemUtil;
import com.renren.mobile.chat.ui.BaseScreen;
import com.renren.mobile.chat.ui.ILoveSixinActivity;
import com.renren.mobile.chat.ui.MainFragmentActivity;
import com.renren.mobile.chat.ui.PullUpdateTouchListener;
import com.renren.mobile.chat.ui.contact.C_ContactsData.NewContactsDataObserver;
import com.renren.mobile.chat.ui.contact.C_LetterBar.OnLetterSelectListener;
import com.renren.mobile.chat.ui.groupchat.D_ContactTitleSelectHolder;
import com.renren.mobile.chat.view.BaseTitleLayout;
import com.renren.mobile.chat.view.BaseTitleLayout.FUNCTION_BUTTON_TYPE;
public final class C_ContactScreen extends BaseScreen implements OnLetterSelectListener,TextWatcher, OnScrollListener, NewContactsDataObserver{
	
	C_ContactsScreenHolder mHolder = new C_ContactsScreenHolder();
	int mDataType = -1;
	
	C_ContactsAdapter mAdapter_All;
	C_ContactsAdapter mAdapter_Common;
	C_ContactsAdapter mAdapter_Search;
	
//	/**显示联系人列表 */
//	private static final byte VISIABLE_CONTACT_LIST = 0;
//	private static final byte VISIABLE_NO_DATA_COMMON = 1;
//	private static final byte VISIABLE_NO_DATA_ALL=2;
//	private static final byte VISIABLE_ON_LOADING=3;
	//private static final byte VISIABLE_OFF_LINE=4;
	
	/** 正在从网络加载 联系人  */
	private static boolean mGetContactsFormNet = false;
	///**true:数据更新正常，即网络正常，false数据更新异常,这里认为都是网络异常 */
	private static boolean mLineError = false;
	
	/**查询结果 */
	List<SearchAble> results = null;
	
	private boolean isSelectAll;
//	TextView mLeftText ;
//	ImageView mLeftImage ;
//	TextView mRightText ;
//	ImageView mRightImage;
	
	View head;
	boolean hasAddHead;
	View allContactsBn2; 
	View commonContactsBn2;
	
	View mainView;
	
	private PullUpdateTouchListener touchListener;
	
	
	private int search_laylout_pading_left = 0;
	private int search_laylout_pading_top = 0;
	private int search_laylout_pading_right_general = 0;
	private int search_laylout_pading_right_special = 0;
	private int search_laylout_pading_bottom = 0;
	
	
	private D_ContactTitleSelectHolder contactTitleSelectHolder = new D_ContactTitleSelectHolder();
	
	public C_ContactScreen(Activity activity) {
		super(activity);
		initData();
		initView();
		initTitle();
	}
	
	public void onShow() {
        if(SystemUtil.mDebug){
        	SystemUtil.mark();
		}
		C_ContactsData.getInstance().loadAllContacts(touchListener);
		C_ContactsData.getInstance().loadCommonContacts();
		if(SystemUtil.mDebug){
			SystemUtil.logd("hasAddHead="+hasAddHead);
		}
		if(!hasAddHead){
			hasAddHead = true;
			mHolder.mContactsListView.addHeaderView(head);
			if(SystemUtil.mDebug){
				SystemUtil.errord("add head");
			}
		}
		mHolder.mContactsListView.setAdapter(mAdapter_All);
		//mAdapter_Common.setData(C_ContactsData.getInstance().get(C_ContactsData.TYPE.COMMON_CONTACTS));
		//mAdapter_All.setData(C_ContactsData.getInstance().get(C_ContactsData.TYPE.ALL_CONTACTS));
	};
	
	private final void changeVisiable(){
		if(mDataType == C_ContactsData.TYPE.ALL_CONTACTS){
			if(SystemUtil.mDebug){
				SystemUtil.logd("sixin size="+C_ContactsData.getInstance().getSize(C_ContactsData.TYPE.SIXIN_CONTACTS));
			}
			
			if(C_ContactsData.getInstance().getSize(C_ContactsData.TYPE.SIXIN_CONTACTS)==0){//没有私信联系人
				if (isFirstGetContactsFormNet()) {//正在联网
					if(SystemUtil.mDebug){
						SystemUtil.mark();
					}
					mHolder.mOnLoading.setVisibility(View.VISIBLE);
					mHolder.mNoData.setVisibility(View.GONE);
					
//					mHolder.mOnLoading.setVisibility(View.GONE);
//					mHolder.mNoDataText.setText(RenrenChatApplication.getmContext().getResources().getString(R.string.C_ContactScreen_java_7));		//C_ContactScreen_java_7=快去多多添加好友; 
//					//mHolder.mNoDataText1.setText(RenrenChatApplication.getmContext().getResources().getString(R.string.C_ContactScreen_java_8));		//C_ContactScreen_java_8=对方验证后你们就可以聊天了; 
//					mHolder.mTipImage.setBackgroundResource(R.drawable.contact_no_data);
//					mHolder.mNoData.setVisibility(View.VISIBLE);
					
				} else if(mLineError){//上次网络更新失败
					if(SystemUtil.mDebug){
						SystemUtil.mark();
					}
					mHolder.mOnLoading.setVisibility(View.GONE);
					mHolder.mTipImage.setBackgroundResource(R.drawable.connection_fail_logo);
					mHolder.mNoDataText.setText(RenrenChatApplication.getmContext().getResources().getString(R.string.C_ContactScreen_java_9));		//C_ContactScreen_java_9=网络连接失败，请稍后再试; 
					mHolder.mNoDataText1.setText("");
					mHolder.mNoData.setVisibility(View.VISIBLE);
				}else{
					if(SystemUtil.mDebug){
						SystemUtil.mark();
					}
					mHolder.mOnLoading.setVisibility(View.GONE);
					mHolder.mNoDataText.setText(RenrenChatApplication.getmContext().getResources().getString(R.string.C_ContactScreen_java_7));		//C_ContactScreen_java_7=快去多多添加好友; 
					//mHolder.mNoDataText1.setText(RenrenChatApplication.getmContext().getResources().getString(R.string.C_ContactScreen_java_8));		//C_ContactScreen_java_8=对方验证后你们就可以聊天了; 
					mHolder.mTipImage.setBackgroundResource(R.drawable.contact_no_data);
					mHolder.mNoData.setVisibility(View.VISIBLE);
				}
			}else{//有联系人数据
				if (isFirstGetContactsFormNet()){//正在联网
					if(SystemUtil.mDebug){
						SystemUtil.mark();
					}
					//setVisiable(VISIABLE_ON_LOADING);
					mHolder.mOnLoading.setVisibility(View.GONE);
					mHolder.mNoData.setVisibility(View.GONE);
				}else {//没有联网
					if(SystemUtil.mDebug){
						SystemUtil.mark();
					}
					//setVisiable(VISIABLE_CONTACT_LIST);	
					mHolder.mOnLoading.setVisibility(View.GONE);
					mHolder.mNoData.setVisibility(View.GONE);
				}
			}
		}else if(mDataType == C_ContactsData.TYPE.COMMON_CONTACTS){
			if(C_ContactsData.getInstance().getSize(C_ContactsData.TYPE.COMMON_CONTACTS)==0){
				if (isFirstGetContactsFormNet()) {//正在联网
					if(SystemUtil.mDebug){
						SystemUtil.mark();
					}
					//setVisiable(VISIABLE_ON_LOADING);
					mHolder.mOnLoading.setVisibility(View.VISIBLE);
					mHolder.mNoData.setVisibility(View.GONE);
				} else if(mLineError){//上次网络更新失败
					if(SystemUtil.mDebug){
						SystemUtil.mark();
					}
					mHolder.mOnLoading.setVisibility(View.GONE);
					mHolder.mTipImage.setBackgroundResource(R.drawable.connection_fail_logo);
					mHolder.mNoDataText.setText(RenrenChatApplication.getmContext().getResources().getString(R.string.C_ContactScreen_java_9));		//C_ContactScreen_java_9=网络连接失败，请稍后再试; 
					mHolder.mNoDataText1.setText("");
					mHolder.mNoData.setVisibility(View.VISIBLE);
				}else{//没有联网
					//setVisiable(VISIABLE_NO_DATA_COMMON);
					if(SystemUtil.mDebug){
						SystemUtil.mark();
					}
					mHolder.mOnLoading.setVisibility(View.GONE);
					mHolder.mNoDataText.setText(RenrenChatApplication.getmContext().getResources().getString(R.string.C_ContactScreen_java_6)); 		//C_ContactScreen_java_6=暂无在线联系人; 
					mHolder.mNoDataText1.setText("");
					mHolder.mTipImage.setBackgroundResource(R.drawable.contact_no_data);
					mHolder.mNoData.setVisibility(View.VISIBLE);
				}
			}else{//有常用联系人
				if (isFirstGetContactsFormNet()){
					if(SystemUtil.mDebug){
						SystemUtil.mark();
					}
					//setVisiable(VISIABLE_ON_LOADING);
					mHolder.mOnLoading.setVisibility(View.GONE);
					mHolder.mNoData.setVisibility(View.GONE);
				}else{
					if(SystemUtil.mDebug){
						SystemUtil.mark();
					}
					//setVisiable(VISIABLE_CONTACT_LIST);
					mHolder.mOnLoading.setVisibility(View.GONE);
					mHolder.mNoData.setVisibility(View.GONE);
				}
			}
		}else
		{
			if(SystemUtil.mDebug){
				SystemUtil.mark();
			}
			//全部搜索界面
//			if(mIsOnline){
//				setVisiable(VISIABLE_CONTACT_LIST);
//			}else{
//				if(!isSelectAll){
//					mHolder.mSearchEditText.setText("");
//				}else{
//					if (isFirstGetContactsFormNet()){
//						setVisiable(VISIABLE_ON_LOADING);
//					}else{
//						setVisiable(VISIABLE_CONTACT_LIST);
//					}
//				}
//			}
		}
	}
	
	/**
	 * 常用   全部    全部搜索 三种种界面
	 * 主动刷新  第一次加载
	 * 四种界面下三种不同刷新的情况都要考虑到 
	 */
	@Override
	public void notifyDataUpdate(final byte state,final byte type) {
		
	//	setFirstGetContactsFormNet(false);
		RenrenChatApplication.sHandler.post(new Runnable() {
			@Override
			public void run() {
				if(SystemUtil.mDebug){
					SystemUtil.errord("state="+state+"#type="+Integer.toBinaryString(type));
				}
				mLineError=(state!=NewContactsDataObserver.DATA_STATE_OK);
				changeVisiable();
//				if((type&NewContactsDataObserver.TYPE_SEARCH) == NewContactsDataObserver.TYPE_SEARCH){
//					if(SystemUtil.mDebug){
//						SystemUtil.logd("update search");
//					}
//					mAdapter_Search.setData(C_ContactsData.getInstance().get(C_ContactsData.TYPE.SEARCH_CONTACTS));
//				}
				
				if((type&NewContactsDataObserver.TYPE_COMMON) == NewContactsDataObserver.TYPE_COMMON){
					if(SystemUtil.mDebug){
						SystemUtil.logd("updata 常用");
					}
					mAdapter_Common.setData(C_ContactsData.getInstance().get(C_ContactsData.TYPE.COMMON_CONTACTS));
				}
				
				if(((type&NewContactsDataObserver.TYPE_SIXIN) == NewContactsDataObserver.TYPE_SIXIN)||
						((type&NewContactsDataObserver.TYPE_GROUP) == NewContactsDataObserver.TYPE_GROUP)||
						((type&NewContactsDataObserver.TYPE_SPECIAL) == NewContactsDataObserver.TYPE_SPECIAL)){
					if(SystemUtil.mDebug){
						SystemUtil.logd("updata all");
					}
					mHolder.mLetterBar.initView(C_ContactsData.getInstance().getIndexs(C_ContactsData.TYPE.ALL_CONTACTS));
					mAdapter_All.notifyData(C_ContactsData.getInstance().get(C_ContactsData.TYPE.ALL_CONTACTS));
				}
			}
		});
	}
	
	
	@Override
	public void onSelect(char c) {
		hideInputMethod(mainView);
		if(SystemUtil.mDebug){
			SystemUtil.errord("c="+c);
		}
		//mHolder.mContactsListView.setSelection(3);
		int index = getProximalIndex(c);
        if(SystemUtil.mDebug){
        	SystemUtil.errord("c="+c+"#index="+((index>>8)&0xffffff));
		}
		if(index!=-1){
			mHolder.mPopText.setVisibility(View.VISIBLE);
			c=(char) (index&0xff);
			index=(index>>8)&0xffffff;
			mHolder.mPopText.setText(c+"");
			mHolder.mContactsListView.setSelection(index);
		}else{
			mHolder.mPopText.setVisibility(View.GONE);
		}
	}
	
	
	@Override
	public void onUnSelect() {
		if(SystemUtil.mDebug){
			SystemUtil.mark();
		}
		mHolder.mPopText.setVisibility(View.GONE);
		mHolder.mLetterBar.getBackground().setAlpha(0);
	}
		
	private final int getProximalIndex(char c){
		SystemUtil.log("c="+c+"#mDataType="+mDataType);
		if(mDataType!=C_ContactsData.TYPE.ALL_CONTACTS){
			onUnSelect();
		}
		int index = C_ContactsData.getInstance().getIndex(c, C_ContactsData.TYPE.ALL_CONTACTS);
		if(SystemUtil.mDebug){
			SystemUtil.errord("index="+index);
		}
		if(index!=-1){
			return (index<<8)|c;
		}
//		char tmpchar=c;  
//		char A='A';
//		char Z='Z';
//		if(c == C_LetterBar.CHAR_OTHER){
//			c=(char) (Z+1);
//		}else if(c == C_LetterBar.CHAR_GROUP){
//			c=(char) (A-1);
//		}
//		for(char i=1;i<C_LetterBar.TAB_INDEX.length;i++){
//			tmpchar=(char) (c-i);
//			if(tmpchar>=A&&tmpchar<=Z){
//				index = C_ContactsData.getInstance().getIndex(tmpchar, C_ContactsData.TYPE.ALL_CONTACTS);
//				if(index!=-1){
//					return (index<<8)|tmpchar;
//				}
//			}
//			tmpchar=(char) (c+i);
//			if(tmpchar>=A&&tmpchar<=Z){
//				index = C_ContactsData.getInstance().getIndex(tmpchar, C_ContactsData.TYPE.ALL_CONTACTS);
//				if(index!=-1){
//					return (index<<8)|tmpchar;
//				}
//			}
//		}
		return -1;
	}
	
	/*检索*/
	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		mHolder.mNoSearchData.setVisibility(View.GONE);
		String searchKey = s+"";
		String loveSixin=s.toString();
		if(loveSixin.equals("i love sixin")){
			Intent intent = new Intent(mActivity, ILoveSixinActivity.class);
			mActivity.startActivity(intent);
		}
		if(searchKey.trim().length()==0){
			ContactModel.bleachList(results);
			if(this.mDataType==C_ContactsData.TYPE.SEARCH_CONTACTS){
				if(isSelectAll){
					allContactsBn2.performClick();
				}else{
					commonContactsBn2.performClick();
				}
				if (mHolder.mLetterBar != null && mHolder.mLetterBar.getVisibility() == View.GONE) {
					mHolder.mLetterBar.setVisibility(View.VISIBLE);
				}
			}else{
				if (mHolder.mLetterBar != null && mHolder.mLetterBar.getVisibility() == View.INVISIBLE) {
					mHolder.mLetterBar.setVisibility(View.GONE);
				}
			}
			
			mHolder.mImageDel.setVisibility(View.GONE);
			return;
		}else{
			if (mHolder.mLetterBar != null && mHolder.mLetterBar.getVisibility() != View.GONE) {
				mHolder.mLetterBar.setVisibility(View.GONE);
			}
			if(SystemUtil.mDebug){
				SystemUtil.logd("right="+search_laylout_pading_right_special+"#top="+search_laylout_pading_top);
			}
			mHolder.mSearchFrameLayout.setPadding(search_laylout_pading_left,search_laylout_pading_top,search_laylout_pading_right_general,search_laylout_pading_bottom);
			mHolder.mImageDel.setVisibility(View.VISIBLE);
			mHolder.mContactsListView.setAdapter(mAdapter_Search);
			this.mDataType = C_ContactsData.TYPE.SEARCH_CONTACTS;
			mHolder.mContactsListView.setDrawAlpheBar(false);
			if(searchKey.length() >= 20){
				SystemUtil.toast(RenrenChatApplication.getmContext().getResources().getString(R.string.D_AttentionScreen_java_2));		//D_AttentionScreen_java_2=最多输入20个字; 
			}
			mHolder.mSearchEditText.requestFocus();
		}
		
		List<SearchAble> beforeSearch = new ArrayList<SearchAble>();
		List<ContactModel> tmpModels = null;
		if(isSelectAll){
			tmpModels = mAdapter_All.getData();
			//tmpModels=C_ContactsData.getInstance().get(C_ContactsData.TYPE.ALL_CONTACTS);
		}else{
			tmpModels = mAdapter_Common.getData();
			//tmpModels=C_ContactsData.getInstance().get(C_ContactsData.TYPE.COMMON_CONTACTS);
		}
		
		for (SearchAble contactModel : tmpModels) {
			if(contactModel instanceof GroupContactModel){
				continue;
			}
			beforeSearch.add(contactModel);
		}
		
		ContactModel.bleachList(results);
		PinyinSearch.mapSearch(searchKey,beforeSearch, results);
		results=beforeSearch;
		if(beforeSearch.size()==0){
			mHolder.mNoSearchData.setText(mActivity.getText(R.string.contact_search_nodata));
			mHolder.mNoSearchData.setVisibility(View.VISIBLE);
		}
		tmpModels=new ArrayList<ContactModel>();
		for (SearchAble search : beforeSearch) {
			tmpModels.add((ContactModel) search);
		}
		
		//C_ContactsData.getInstance().addToSearchListAndNotify(tmpModels);
		
		mAdapter_Search.setData(tmpModels);
	}
		
	/*没用的方法*/
	@Override
	public void afterTextChanged(Editable s) {}
	
	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,int after) {}
	
	@Override
	public void clearCache() {
		C_ContactsData.getInstance().unRegistorNewObserver(this);
	}
	/**
	 * MainFramentActivity中的ViewPager滑动时调用
	 */
	public void onPageScrollStateChanged(){
        if(SystemUtil.mDebug){
        	SystemUtil.logd("999999999999999999999999999999");
		}
		if (mHolder.mPopText != null && mHolder.mLetterBar!=null) {
			onUnSelect();
		}
		if(mHolder.mNoSearchData!=null){
					}
	}
	
	public void onVisible(){
//		if(!C_ContactsData.getInstance().hasGroup()){
//			C_ContactsData.getInstance().loadContacts(true);
//			notifyDataUpdate(ContactsDataObserver.DATA_STATE_OK);
//		}
	}
	
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		boolean isScrolling=false;
		if(scrollState==AbsListView.OnScrollListener.SCROLL_STATE_IDLE){
			isScrolling=false;
		}else{
			isScrolling=true;
			hideInputMethod(view);
		}
		if(this.mDataType==C_ContactsData.TYPE.ALL_CONTACTS){
			mAdapter_All.setScrollState(isScrolling);
		}else if(this.mDataType==C_ContactsData.TYPE.COMMON_CONTACTS){
			mAdapter_Common.setScrollState(isScrolling);
		}else{
			mAdapter_Search.setScrollState(isScrolling);
		}
	}
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
	}
	public  static final void setFirstGetContactsFormNet(boolean isFirst){
		mGetContactsFormNet=isFirst;
	}
	public  static final boolean isFirstGetContactsFormNet(){
		return mGetContactsFormNet;
	} 
	
	
	private void initData(){
		C_ContactsData.getInstance().registorNewObserver(this);
		mAdapter_All = new C_ContactsAdapter(mActivity, C_ContactsData.TYPE.ALL_CONTACTS);
		mAdapter_Common = new C_ContactsAdapter(mActivity, C_ContactsData.TYPE.COMMON_CONTACTS);
		mAdapter_Search = new C_ContactsAdapter(mActivity, C_ContactsData.TYPE.SEARCH_CONTACTS);
		search_laylout_pading_left = (int)mActivity.getResources().getDimension(R.dimen.search_layout_left);
		search_laylout_pading_top = (int)mActivity.getResources().getDimension(R.dimen.search_layout_top);
		search_laylout_pading_right_general = (int)mActivity.getResources().getDimension(R.dimen.search_layout_right_general);
		search_laylout_pading_right_special = (int)mActivity.getResources().getDimension(R.dimen.search_layout_right_special);
		search_laylout_pading_bottom = (int)mActivity.getResources().getDimension(R.dimen.search_layout_bottom);
		if(SystemUtil.mDebug){
			SystemUtil.logd("l="+search_laylout_pading_left+"#t="+search_laylout_pading_top+"#r="+search_laylout_pading_right_general+"#"+search_laylout_pading_right_special+"#b="+search_laylout_pading_bottom);
		}
	}
	
	private void initView(){
		mainView = SystemService.sInflaterManager.inflate(R.layout.contact_main, null);
		ViewMapUtil.getUtil().viewMapping(mHolder, mainView);
		head = SystemService.sInflaterManager.inflate(
				R.layout.contact_search_edit_text, null);
		//mHolder.mContactsListView.addHeaderView(head);
	//	int right = mActivity.getResources().getDimension(R.dimen.search_layout_right_special);
		mHolder.mSearchFrameLayout = (FrameLayout) head.findViewById(R.id.search_layout);
////		int right = (int)mActivity.getResources().getDimension(R.dimen.search_layout_right_general);
//		if(SystemUtil.mDebug){
//			SystemUtil.logd("right="+search_laylout_pading_right_special+"#top="+search_laylout_pading_top);
//		}
//		mHolder.mSearchFrameLayout.setPadding(search_laylout_pading_left,search_laylout_pading_top,search_laylout_pading_right_special,search_laylout_pading_bottom);
		mHolder.mSearchEditText = (EditText) head.findViewById(R.id.search_edit);
		mHolder.mSearchEditText.setHint(mActivity.getText(R.string.contact_search_hint));
		mHolder.mSearchEditText.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (KeyEvent.KEYCODE_ENTER == keyCode) {
					return true;
				}
				return false; 
			}
		});
		
		mHolder.mImageDel = (ImageView) head.findViewById(R.id.search_del);
		mHolder.mImageDel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mHolder.mSearchEditText.setText("");
				SystemService.sInputMethodManager.toggleSoftInput(0, 0);
			}
		});
		
		mHolder.mContactsListView.setDivider(null);
		mHolder.mContactsListView.setOnScrollListener(this);
		mHolder.mContactsListView.setItemsCanFocus(true);
		mHolder.mContactsListView.setAddStatesFromChildren(true);
		mHolder.mContactsListView.setFocusableInTouchMode(true);
		mHolder.mContactsListView.setVerticalFadingEdgeEnabled(false);
		mHolder.mContactsListView.setCacheColorHint(0);
		mHolder.mContactsListView.setScrollingCacheEnabled(false);
		mHolder.mContactsListView.setDrawingCacheEnabled(false);
		mHolder.mContactsListView.setAlwaysDrawnWithCacheEnabled(false);
		mHolder.mContactsListView.setWillNotCacheDrawing(true);
		mHolder.mLetterBar.initView(null);
		mHolder.mLetterBar.setOnLetterSelectListener(this);
		mHolder.mSearchEditText.addTextChangedListener(this);
		
		// 下拉刷新
		touchListener = new PullUpdateTouchListener(SystemService.sInflaterManager) {
			
			@Override
			public void refresh() {
				//touchListener.onRefreshComplete();
				//query_Contacts_FromNet
				//setFirstGetContactsFormNet(true);
				C_ContactScreen.setFirstGetContactsFormNet(true);
				changeVisiable();
				C_ContactsData.getInstance().query_Contacts_FromNet(touchListener);
//				RenrenChatApplication.mHandler.post(new Runnable() {
//					@Override
//					public void run() {
//						
//						//mHolder.mContactsListView.invalidate();
//						//mainView.postInvalidate();
//					//	mainView.invalidate();
//						
//					}
//				});
			}
			
			@Override
			public boolean isHead() {
				ListView listview = mHolder.mContactsListView;
				if(mDataType == C_ContactsData.TYPE.ALL_CONTACTS){
					if(mAdapter_All.getCount()>0){
						return listview.getFirstVisiblePosition() == 0 && Math.abs(listview.getChildAt(0).getTop()) <= 10 ;
					}
					return true;
				}else if (mDataType == C_ContactsData.TYPE.COMMON_CONTACTS){
//					if(mAdapter_Common.getCount()>0){
//						return listview.getFirstVisiblePosition() == 0 && Math.abs(listview.getChildAt(0).getTop()) <= 10 ;
//					}
					return false;
				}else{
//					if(mAdapter_Search.getCount()>0){
//						return listview.getFirstVisiblePosition() == 0 && Math.abs(listview.getChildAt(0).getTop()) <= 10 ;
//					}
					return false;
				}
			}
			
			@Override
			public void afterDo() {
				LastUpdateTime = System.currentTimeMillis();
			}
		};
		touchListener.initView(mHolder.mContactsListView, mHolder.mFrameLayout);
		mHolder.mFrameLayout.addView(touchListener.getPullUpdateView(), new FrameLayout.LayoutParams(FrameLayout.LayoutParams.FILL_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT));
		//touchListener.beginRefresh();
		this.setContent(mainView);
	}
	
	private void initTitle(){
		BaseTitleLayout mTitle = this.getTitle();
		LinearLayout mMidTitleLayout = mTitle.getMidTitleLayout();
		mMidTitleLayout.removeAllViews();
		
		
		 View midTitle = mTitle.getmContactTitleLayout();
	     ViewMapUtil.getUtil().viewMapping(contactTitleSelectHolder, midTitle);
	     contactTitleSelectHolder.contactTitleMiddle.setVisibility(View.VISIBLE);
	     //contactTitleSelectHolder.c
	     
		//	contactTitleSelectHolder.contactTitleLeftBtn.
			
//		LinearLayout mTitleLayout = (LinearLayout) SystemService.sInflaterManager.inflate(R.layout.contact_title_button, null);
//		mLeftText = (TextView) mTitleLayout.findViewById(R.id.cy_left_text);
//		CommonUtil.setBoldTextView(mLeftText);
//		mLeftImage = (ImageView) mTitleLayout.findViewById(R.id.cy_left_image);
//		mRightText = (TextView) mTitleLayout.findViewById(R.id.cy_right_text);
//		CommonUtil.setBoldTextView(mRightText);
//		mRightImage = (ImageView) mTitleLayout.findViewById(R.id.cy_right_image);
		
		commonContactsBn2 = contactTitleSelectHolder.contactTitleLeftBtn;
		
		commonContactsBn2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
//				mLeftText.setSelected(true);
//				mRightText.setSelected(false);
//				mLeftImage.setVisibility(View.VISIBLE);
//				mRightImage.setVisibility(View.INVISIBLE);
				//C_ContactsData.getInstance().loadCommonContacts();
//				head.setVisibility(View.GONE);
//				if(head.getParent()!=null){
//					//head=com.renren.mobile.chat.ui.contact.C_ContactsListView@41e341f8
//					//SystemUtil.logd("head="+head.getParent().toString());
//					//((View)head.getParent()).setVisibility(View.GONE);
//				}
//				if(head.getRootView()!=null){
//					//head=com.android.internal.policy.impl.PhoneWindow$DecorView@41d3c4d8
//					//SystemUtil.logd("head="+head.getRootView().toString());
//					//head.getRootView().setVisibility(View.GONE);
//				}
				//mHolder.mContactsListView.
				//if(hasAddHead){
				contactTitleSelectHolder.setLeftBtnSelect();
				mHolder.mContactsListView.removeHeaderView(head);
				//}
				hasAddHead  = false;
				isSelectAll = false;
				hideInputMethod(v);
				//C_ContactsData.getInstance().loadCommonContacts();
				//mAdapter_Common.setData(C_ContactsData.getInstance().get(C_ContactsData.TYPE.COMMON_CONTACTS));
				mHolder.mContactsListView.setAdapter(mAdapter_Common);
				mDataType = C_ContactsData.TYPE.COMMON_CONTACTS;
				mHolder.mLetterBar.setVisibility(View.GONE);
				changeVisiable();
				results=null;
				mHolder.mContactsListView.setDrawAlpheBar(true);
			} 
		});
		allContactsBn2 = contactTitleSelectHolder.contactTitleMiddleBtn;
				
		allContactsBn2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
//				mRightText.setSelected(true);
//				mLeftText.setSelected(false);
//				mLeftImage.setVisibility(View.INVISIBLE);
//				mRightImage.setVisibility(View.VISIBLE);
				contactTitleSelectHolder.setMiddleBtnSelect();
				if(SystemUtil.mDebug){
					SystemUtil.logd("hasAddHead="+hasAddHead);
				}
				if(!hasAddHead){
					hasAddHead =true;
//					int right = (int)mActivity.getResources().getDimension(R.dimen.search_layout_right_special);			
					mHolder.mContactsListView.setAdapter(null);	
					mHolder.mContactsListView.addHeaderView(head);
				}
				if(SystemUtil.mDebug){
					SystemUtil.logd("right="+search_laylout_pading_right_special+"#top="+search_laylout_pading_top);
				}
				mHolder.mSearchFrameLayout.setPadding(search_laylout_pading_left,search_laylout_pading_top,search_laylout_pading_right_special,search_laylout_pading_bottom);
				mHolder.mContactsListView.setAdapter(mAdapter_All);
				isSelectAll = true;
				hideInputMethod(v);
				mDataType = C_ContactsData.TYPE.ALL_CONTACTS;
				mHolder.mSearchEditText.setText("");
				mHolder.mLetterBar.setVisibility(View.VISIBLE);
				changeVisiable();
				mHolder.mContactsListView.setDrawAlpheBar(true);
				results=null;
			}
		});
		
		//LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
			//	LinearLayout.LayoutParams.WRAP_CONTENT,
			//	LinearLayout.LayoutParams.WRAP_CONTENT);
		//mMidTitleLayout.addView(mTitleLayout, layoutParams);
		//mTitleLayout.findViewById(R.id.cy_right).performClick();
		allContactsBn2.performClick();
		mTitle.setTitleButtonLeftBackVisibility(false);
		mTitle.setTitleFunctionButtonBackground(FUNCTION_BUTTON_TYPE.ADDCONTACT);
		
		Button RefreshButton = this.getTitle().getTitleRightFunctionButton();
		RefreshButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(mActivity,SearchContactActivity.class); 
				mActivity.startActivity(i);
			   ((MainFragmentActivity)mActivity).mHandler.sendEmptyMessage(MainFragmentActivity.RETRIEVE_FRIENDREQUEST);
			}
		});
	}
	
	@Override
	public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
		switch(keyCode) {
		case KeyEvent.KEYCODE_SEARCH:
			if(SystemService.sInputMethodManager!=null){
				SystemService.sInputMethodManager.toggleSoftInput(0, 0);
			}
			return true;
		default:
			return super.onKeyDown(keyCode, event);
		} 
	}

	@Override
	public byte getType() {
		return NewContactsDataObserver.TYPE_SEARCH|
				NewContactsDataObserver.TYPE_SPECIAL|
				NewContactsDataObserver.TYPE_GROUP|
				NewContactsDataObserver.TYPE_SIXIN|
				NewContactsDataObserver.TYPE_COMMON
				;
	}
}
