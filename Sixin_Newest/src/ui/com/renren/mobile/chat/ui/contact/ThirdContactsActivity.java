package com.renren.mobile.chat.ui.contact;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.common.utils.PinyinSearch;
import com.common.utils.SearchAble;
import com.core.util.SystemService;
import com.renren.mobile.chat.R;
import com.renren.mobile.chat.RenrenChatApplication;
import com.renren.mobile.chat.base.util.SystemUtil;
import com.renren.mobile.chat.ui.BaseActivity;
import com.renren.mobile.chat.ui.PullUpdateTouchListener;
import com.renren.mobile.chat.ui.contact.C_ContactsData.NewContactsDataObserver;
import com.renren.mobile.chat.view.BaseTitleLayout;
public final class ThirdContactsActivity extends BaseActivity implements  OnScrollListener, NewContactsDataObserver, TextWatcher 
 {
	
	Context context = ThirdContactsActivity.this;;
	FrameLayout mLinearLayout;
	BaseTitleLayout mBaseTitle;
	
	private int mDataType = ALL_CONTACTS;
	private static final byte SEARCH_CONTACTS =1;
	private static final byte ALL_CONTACTS =0;
	
	private C_ContactsListView mContactsListView = null;//联系人列表
	private TextView mNoSearchData=null; //搜索没有结果时候显示
	private LinearLayout mNodataLayout;
	private LinearLayout mOnloading;
	private ImageView mTipImage;
	private TextView mNoDataText;
	
	/** 正在从网络加载 联系人  */
	private static boolean mGetContactsFormNet = false;
	private static boolean mLineError = false;
	
	TextView mTextView;
	long groupId;
	long mToChatId;
	boolean isSelect = false;
	
	View head;
	
	//List<ContactModel> mData = new ArrayList<ContactModel>();
	
	ThirdContactAdapter mAdapter_All;
	ThirdContactAdapter mAdapter_Search;
	
	private PullUpdateTouchListener touchListener;
	private EditText mSearchEditText;
	private ImageView mImageDel;
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		initView();
		initTitle();
		initData();
	}
	private void initData() {
		mAdapter_All = new ThirdContactAdapter(context,ThirdContactAdapter.DATA_TYPE_ALL);
		mAdapter_Search = new ThirdContactAdapter(context,ThirdContactAdapter.DATA_TYPE_SERARCH);
		mContactsListView.setAdapter(mAdapter_All);
		C_ContactsData.getInstance().registorNewObserver(this);
		C_ContactsData.getInstance().loadRenRenContact(touchListener);
	}
	
	private void initTitle() {
		mBaseTitle = new BaseTitleLayout(context,mLinearLayout);
		mTextView = mBaseTitle.getTitleMiddle();
		mBaseTitle.getTitleLeft().setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		mTextView.setText(RenrenChatApplication.getmContext().getResources().getString(R.string.ContactModel_java_13));//ContactModel_java_13
		
	}
	
	private void initView() {
		
		View view = SystemService.sInflaterManager.inflate(
				R.layout.contact_third, null);
		setContentView(view);
		
		mContactsListView = (C_ContactsListView) this.findViewById(R.id.contacts_listview);
		
		
		head = SystemService.sInflaterManager.inflate(
				R.layout.contact_search_edit_text, null);
		
		mContactsListView.addHeaderView(head);
		
		mSearchEditText = (EditText) head.findViewById(R.id.search_edit);
		mSearchEditText.setHint(this.getText(R.string.contact_search_hint));
		mSearchEditText.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (KeyEvent.KEYCODE_ENTER == keyCode) {
					return true;
				}
				return false; 
			}
		});
		
		mImageDel = (ImageView) head.findViewById(R.id.search_del);
		mImageDel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mSearchEditText.setText("");
				SystemService.sInputMethodManager.toggleSoftInput(0, 0);
			}
		});
		
		mNoSearchData = (TextView) this.findViewById(R.id.no_search_data);
		mNodataLayout = (LinearLayout) this.findViewById(R.id.contacts_nodata);
		mOnloading = (LinearLayout) this.findViewById(R.id.contacts_loaddata);
		mTipImage = (ImageView) this.findViewById(R.id.data_imageView);
		mNoDataText = (TextView) this.findViewById(R.id.data_textView);
		
		
		mLinearLayout = (FrameLayout) this.findViewById(R.id.title_layout);
		FrameLayout	mFrameLayout = (FrameLayout) this.findViewById(R.id.main_frame);
		
		mContactsListView.setDivider(null);
		
		mContactsListView.setOnScrollListener(this);
		mContactsListView.setDrawAlpheBar(false);
		
		mContactsListView.setItemsCanFocus(true);
		mContactsListView.setAddStatesFromChildren(true);
		mContactsListView.setFocusableInTouchMode(true);
		mContactsListView.setVerticalFadingEdgeEnabled(false);
		mContactsListView.setCacheColorHint(0);
		mContactsListView.setScrollingCacheEnabled(false);
		mContactsListView.setDrawingCacheEnabled(false);
		mContactsListView.setAlwaysDrawnWithCacheEnabled(false);
		mContactsListView.setWillNotCacheDrawing(true);
		
		mSearchEditText.addTextChangedListener(this);
		
		
		//不要再这个地方设置   会影响下拉刷新  放到adapter里面
//		mContactsListView.setOnItemClickListener(new OnItemClickListener() {
//			@Override
//			public void onItemClick(AdapterView<?> arg0, View view, int position,
//					long arg3) {
//				SystemUtil.logd("position=="+position);
//				UserInfoActivity.show(context,mAdapter_All.getItem(position));
//			}
//		});
		
		// 下拉刷新
		touchListener = new PullUpdateTouchListener(SystemService.sInflaterManager) {
			
			@Override
			public void refresh() {
				//touchListener.onRefreshComplete();
				//query_Contacts_FromNet
				ThirdContactsActivity.setFirstGetContactsFormNet(true);
				C_ContactsData.getInstance().query_third_contacts_fromNet(this);
			}
			
			@Override
			public boolean isHead() {
				if(mAdapter_All.getCount()>0){
					return mContactsListView.getFirstVisiblePosition() == 0 && Math.abs(mContactsListView.getChildAt(0).getTop()) <= 10 ;	
				}else{
					return true;
				}
			}
			
			@Override
			public void afterDo() {
				LastUpdateTime = System.currentTimeMillis();
			}
		};
		touchListener.initView(mContactsListView, mFrameLayout);
		mFrameLayout.addView(touchListener.getPullUpdateView(), new FrameLayout.LayoutParams(FrameLayout.LayoutParams.FILL_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT));
	}
	
	
	
	@Override
	public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_SEARCH:
			hideInputMethod();
			return true;
		default:
			return super.onKeyDown(keyCode, event);
		}
	}
	
	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
	}
	boolean isScrolling = false;
	private List<SearchAble> results;
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		boolean isScrolling=false;
		if(scrollState==AbsListView.OnScrollListener.SCROLL_STATE_IDLE){
			isScrolling=false;
		}else{
			isScrolling=true;
		}
		if(this.mDataType==ALL_CONTACTS){
			mAdapter_All.setScrollState(isScrolling);
		}else{
			mAdapter_Search.setScrollState(isScrolling);
		}
	}
	@Override
	public void notifyDataUpdate(byte state ,byte type){
		//SystemUtil.traces();
		mLineError=(state!=NewContactsDataObserver.DATA_STATE_OK);
		RenrenChatApplication.sHandler.post(new Runnable() {
			@Override
			public void run() {
				mAdapter_All.setDataList(C_ContactsData.getInstance().getRenRenContacts());
				changeVisiable();
			}
		});		
	}
	
	private final void changeVisiable(){
		if(mAdapter_All.getCount()==0){//没有私信联系人
			if (isFirstGetContactsFormNet()) {//正在联网
		        if(SystemUtil.mDebug){
		        	SystemUtil.mark();
				}
				mOnloading.setVisibility(View.VISIBLE);
				mNodataLayout.setVisibility(View.GONE);
			} else if(mLineError){//上次网络更新失败
		        if(SystemUtil.mDebug){
		        	SystemUtil.mark();
				}
				mOnloading.setVisibility(View.GONE);
				mTipImage.setBackgroundResource(R.drawable.connection_fail_logo);
				mNoDataText.setText(RenrenChatApplication.getmContext().getResources().getString(R.string.C_ContactScreen_java_9));		//C_ContactScreen_java_9=网络连接失败，请稍后再试; 
				mNodataLayout.setVisibility(View.VISIBLE);
			}else{
		        if(SystemUtil.mDebug){
		        	SystemUtil.mark();
				}
				mOnloading.setVisibility(View.GONE);
				mNoDataText.setText(RenrenChatApplication.getmContext().getResources().getString(R.string.C_ContactScreen_java_7));		//C_ContactScreen_java_7=快去多多添加好友; 
				//mHolder.mNoDataText1.setText(RenrenChatApplication.getmContext().getResources().getString(R.string.C_ContactScreen_java_8));		//C_ContactScreen_java_8=对方验证后你们就可以聊天了; 
				mTipImage.setBackgroundResource(R.drawable.contact_no_data);
				mNodataLayout.setVisibility(View.VISIBLE);
			}
		}else{
			if (isFirstGetContactsFormNet()){//正在联网
		        if(SystemUtil.mDebug){
		        	SystemUtil.mark();
				}
				//setVisiable(VISIABLE_ON_LOADING);
				mOnloading.setVisibility(View.GONE);
				mNodataLayout.setVisibility(View.GONE);
			}else {//没有联网
		        if(SystemUtil.mDebug){
		        	SystemUtil.mark();
				}
				//setVisiable(VISIABLE_CONTACT_LIST);	
				mOnloading.setVisibility(View.GONE);
				mNodataLayout.setVisibility(View.GONE);
			}
		}
	}
	
	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		mNoSearchData.setVisibility(View.GONE);
		String searchKey = s+"";
		if(searchKey.trim().length()==0){
			if(this.mDataType==SEARCH_CONTACTS){
				mContactsListView.setAdapter(mAdapter_All);
				//hideInputMethod(v);
				mDataType = ALL_CONTACTS;
				mSearchEditText.setText("");
				changeVisiable();
			}
		    mImageDel.setVisibility(View.GONE);
			return;
		}else{
			mImageDel.setVisibility(View.VISIBLE);
			mContactsListView.setAdapter(mAdapter_Search);
			mDataType = SEARCH_CONTACTS;
			mContactsListView.setDrawAlpheBar(false);
			if(searchKey.length() >= 20){
				SystemUtil.toast(RenrenChatApplication.getmContext().getResources().getString(R.string.D_AttentionScreen_java_2));		//D_AttentionScreen_java_2=最多输入20个字; 
			}
			mSearchEditText.requestFocus();
		}
		
		List<SearchAble> beforeSearch = new ArrayList<SearchAble>();
		List<ThirdContactModel> tmpModels =  mAdapter_All.getData();
		
		for (SearchAble contactModel : tmpModels) {
			beforeSearch.add(contactModel);
		}
		
		ThirdContactModel.bleachList(results);
		PinyinSearch.mapSearch(searchKey,beforeSearch, results);
		results=beforeSearch;
		
		if(beforeSearch.size()==0){
			mNoSearchData.setText(context.getText(R.string.contact_search_nodata));
		    mNoSearchData.setVisibility(View.VISIBLE);
		}
		tmpModels=new ArrayList<ThirdContactModel>();
		for (SearchAble search : beforeSearch) {
			tmpModels.add((ThirdContactModel) search);
		}
		mAdapter_Search.setDataList(tmpModels);
		//C_ContactsData.getInstance().addToSearchListAndNotify(tmpModels);
	}
		
	/*没用的方法*/
	@Override
	public void afterTextChanged(Editable s) {}
	
	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,int after) {}
	
	@Override
	public byte getType() {
		return NewContactsDataObserver.TYPE_RENREN;
	}
	@Override
	protected void onDestroy() {
		C_ContactsData.getInstance().unRegistorNewObserver(this);
		super.onDestroy();
	}
	
	public  static final void setFirstGetContactsFormNet(boolean isFirst){
		mGetContactsFormNet=isFirst;
	}
	public  static final boolean isFirstGetContactsFormNet(){
		return mGetContactsFormNet;
	}
	@Override
	protected void onResume() {
//		SystemService.sInputMethodManager.toggleSoftInput(0, 0);//.hideSoftInputFromInputMethod(mSearchEditText.getWindowToken(), InputMethodManager.SHOW_IMPLICIT);
		//hideInputMethod();
		// TODO Auto-generated method stub
		super.onResume();
	}
	
}
