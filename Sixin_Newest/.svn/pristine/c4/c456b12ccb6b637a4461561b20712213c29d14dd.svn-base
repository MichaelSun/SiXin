package com.renren.mobile.chat.ui.contact.attention;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;

import com.common.utils.PinyinSearch;
import com.common.utils.SearchAble;
import com.core.util.SystemService;
import com.core.util.ViewMapUtil;
import com.renren.mobile.chat.R;
import com.renren.mobile.chat.RenrenChatApplication;
import com.renren.mobile.chat.base.util.SystemUtil;
import com.renren.mobile.chat.base.views.NotSynImageView.DownloadImageAbleListener;
import com.renren.mobile.chat.dao.DAOFactoryImpl;
import com.renren.mobile.chat.dao.ThirdContactsDAO;
import com.renren.mobile.chat.lbs.LocationThread;
import com.renren.mobile.chat.ui.BaseScreen;
import com.renren.mobile.chat.ui.contact.C_ContactsData;
import com.renren.mobile.chat.ui.contact.C_ContactsData.NewContactsDataObserver;
import com.renren.mobile.chat.ui.contact.C_LetterBar.OnLetterSelectListener;
import com.renren.mobile.chat.ui.contact.ContactOnTouchListener;
import com.renren.mobile.chat.ui.contact.ThirdContactModel;
import com.renren.mobile.chat.ui.contact.ThirdContactsActivity;
import com.renren.mobile.chat.ui.contact.feed.ObserverImpl;
import com.renren.mobile.chat.util.ObservableImpl;
/**
 * 
 * @author rubin.dong@renren-inc.com
 * @date 
 * 关注页面
 *
 */
public class AttentionAddScreen extends BaseScreen implements NewContactsDataObserver,
	TextWatcher , OnLetterSelectListener, OnScrollListener,DownloadImageAbleListener{
	AttentionAddScreenHolder mHolder = new AttentionAddScreenHolder();
	private static boolean mIsScrolling = false; 
	int mDataType = -1;
	InputMethodManager imm;
	ContactOnTouchListener mListViewTouchListener;
	Context context;
	AttentionAddAdapter mAdapter_All;
	AttentionAddAdapter mAdapter_Search;
	ThirdContactsDAO mThirdContactsDAO;
	View baseView;
	boolean isSelect = false;
	String mStr;
	String lastStr = "";
	public AttentionAddScreen(Activity activity) {
		super(activity);
		context = mActivity;
		baseView = SystemService.sInflaterManager.inflate(
				R.layout.lc_attention_add, null);
		ViewMapUtil.getUtil().viewMapping(mHolder, baseView);
//		mHolder.mLetterBar.setOnLetterSelectListener(this);
		this.initTitle();
		this.initSearch();
		this.setContent(baseView);
		mHolder.mContactsListView.setScrollingCacheEnabled(false);       
		mHolder.mContactsListView.setDrawingCacheEnabled(false);         
		mHolder.mContactsListView.setAlwaysDrawnWithCacheEnabled(false); 
		mHolder.mContactsListView.setWillNotCacheDrawing(true);   
		mHolder.mContactsListView.setDivider(mActivity.getResources().getDrawable(R.drawable.listview_item_divider));
		mAdapter_All = new AttentionAddAdapter(
				C_ContactsData.TYPE.ALL_CONTACTS, mActivity,
				mHolder.mContactsListView);
		mThirdContactsDAO = DAOFactoryImpl.getInstance().buildDAO(ThirdContactsDAO.class);
		C_ContactsData.getInstance().registorNewObserver(this);
		C_ContactsData.getInstance().loadRenRenContact(null);
		mHolder.mLetterBar.setOnLetterSelectListener(this);
		mAdapter_Search = new AttentionAddAdapter(
				C_ContactsData.TYPE.SEARCH_CONTACTS, mActivity,
				mHolder.mContactsListView);
		mAdapter_Search.setDataList(C_ContactsData.getInstance().getRenRenContacts());//C_ContactsData.getInstance().get(C_ContactsData.TYPE.SEARCH_CONTACTS));
		this.onShow();
	}
	@Override
	public void onShow() {
		mHolder.mContactsListView.setAdapter(mAdapter_All);
		mDataType = C_ContactsData.TYPE.ALL_CONTACTS;
		mListViewTouchListener = new ContactOnTouchListener(
				mHolder.mSearchEditText, mActivity);
		mHolder.mContactsListView.setOnTouchListener(mListViewTouchListener);
//		mHolder.mContactsListView.setOnScrollListener(this);
		mHolder.mImageDel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mHolder.mSearchEditText.setText("");
				mHolder.mNoSearchData.setVisibility(View.GONE);
			}
		});
		/**按回车键后的处理事件*/ 
		mHolder.mSearchEditText.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (KeyEvent.KEYCODE_ENTER == keyCode) {
					return true;
				}
				return false; 
			}
		});
	}
	private void initSearch() {
		mHolder.mSearchEditText.addTextChangedListener(this);
	}
	@Override
	public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_SEARCH:
			imm.toggleSoftInput(0, 0);
			hideInputMethod(getScreenView());
			return true;
		default:
			return super.onKeyDown(keyCode, event);
		}
	}
	private void initTitle() {
		this.getTitle().setTitleMiddle(RenrenChatApplication.getmContext().getResources().getString(R.string.D_AttentionScreen_java_1) );		//D_AttentionScreen_java_1=添加关注; 
		this.getTitle().setTitleRightLayoutVisible(false);
		this.getTitle().setTitleButtonLeftBackVisibility(true);
		this.getTitle().setTitleButtonLeftBackListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onFinish();
			}
		});
	}
	
	public void onFinish(){
		hideInputMethod(getScreenView());
		//更新DAO缓存数据
		if(mOnAttentionDataChangedCallback != null){
			mOnAttentionDataChangedCallback.onChange();
		}
		finish();
	}
	
	public void onPause(){
		onFinish();
	}
	
	@Override
	public void afterTextChanged(Editable s) {
	}
	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
	}
	List<SearchAble> results = null;
	/* 检索 */
	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		String searchKey = s + "";
		if (searchKey.trim().length() == 0) {
			if (this.mDataType == C_ContactsData.TYPE.SEARCH_CONTACTS) {
				mHolder.mContactsListView.setAdapter(mAdapter_All);
				mDataType = C_ContactsData.TYPE.ALL_CONTACTS;
			}
			if (mHolder.mLetterBar != null
					&& mHolder.mLetterBar.getVisibility() == View.GONE) {
				mHolder.mLetterBar.setVisibility(View.VISIBLE);
			}
			mHolder.mImageDel.setVisibility(View.GONE);
			mHolder.mContactsListView.setDrawAlpheBar(true);
			return;
		} else {
			if (mHolder.mLetterBar != null
					&& mHolder.mLetterBar.getVisibility() != View.GONE) {
				mHolder.mLetterBar.setVisibility(View.GONE);
			}
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
		List<ThirdContactModel> tmpModels =  mAdapter_All.mDataList;
		
		for (SearchAble contactModel : tmpModels) {
			beforeSearch.add(contactModel);
		}
		
		ThirdContactModel.bleachList(results);
		PinyinSearch.mapSearch(searchKey,beforeSearch, results);
		results=beforeSearch;
		
		if(beforeSearch.size()==0){
		    mHolder.mNoSearchData.setVisibility(View.VISIBLE);
		}else{
			mHolder.mNoSearchData.setVisibility(View.GONE);
		}
		tmpModels=new ArrayList<ThirdContactModel>();
		for (SearchAble search : beforeSearch) {
			tmpModels.add((ThirdContactModel) search);
		}
		mAdapter_Search.setDataList(tmpModels);
		
		
//		List<ContactModel>	tmpModels=C_ContactsData.getInstance().get(C_ContactsData.TYPE.ALL_CONTACTS);
//		List<SearchAble> beforeSearch = new ArrayList<SearchAble>(tmpModels);
//		ContactModel.bleachList(results);
//		PinyinSearch.mapSearch(searchKey,beforeSearch, results);
//		results=beforeSearch;
//		tmpModels=new ArrayList<ContactModel>();
//		for (SearchAble search : beforeSearch) {
//			tmpModels.add((ContactModel) search);
//		}
//		C_ContactsData.getInstance().addToSearchListAndNotify(tmpModels);
	}
//	@Override
//	public void onSelect(char c) {
//		mHolder.mPopText.setVisibility(View.VISIBLE);
//		mHolder.mPopText.setText(c + "");
//		int index = 0;
//		if (mDataType == C_ContactsData.TYPE.ALL_CONTACTS) {
//			index = C_ContactsData.getInstance().getIndex(c,
//					C_ContactsData.TYPE.ALL_CONTACTS);
//		}
//		if (index != -1) {
//			mHolder.mContactsListView.setSelection(index);
//		} else {
//			mHolder.mPopText.setVisibility(View.GONE);
//		}
//		isSelect = true;
//		if(this.mDataType==C_ContactsData.TYPE.ALL_CONTACTS){
//			mAdapter_All.setSelectState(isSelect);
//		}else{
//			mAdapter_Search.setSelectState(isSelect);
//		}
//	}
//	@Override
//	public void onUnSelect() {
//		mHolder.mPopText.setVisibility(View.GONE);
//		isSelect = false;
//		if(this.mDataType==C_ContactsData.TYPE.ALL_CONTACTS){
//			mAdapter_All.setSelectState(isSelect);
//		}else{
//			mAdapter_Search.setSelectState(isSelect);
//		}
//	}
	/**
	 * 是否滚动到最后一行
	 */
	boolean isLastRow = false;
	/**
	 * 是否在第一行
	 */
	boolean isFirstRow = true;
	/**
	 * 是否在上下边框
	 */
	boolean isEdge = false;
	
	public static interface OnAttentionDataChangedCallback{
		public void onChange();
	}
	
	private static OnAttentionDataChangedCallback mOnAttentionDataChangedCallback = null;
	
	public static void setOnAttentionDataChangedCallback(OnAttentionDataChangedCallback onAttentionDataChangedCallback){
		mOnAttentionDataChangedCallback = onAttentionDataChangedCallback;
	}
	@Override
	public void notifyDataUpdate(byte state, byte type) {
		RenrenChatApplication.sHandler.post(new Runnable() {
			@Override
			public void run() {
				List<ThirdContactModel> renrenContact = C_ContactsData.getInstance().getRenRenContacts();
				if(renrenContact.size() > 0){
					mHolder.mNoSearchData.setVisibility(View.GONE);
					mAdapter_All.setDataList(renrenContact);
					mHolder.mLetterBar.initView(C_ContactsData.getInstance().getIndexs(C_ContactsData.TYPE.RENREN_CONTACTS));
					mHolder.mLoaddataLayout.setVisibility(View.GONE);
					C_ContactsData.getInstance().unRegistorNewObserver(AttentionAddScreen.this);
				}else{
					if(ThirdContactsActivity.isFirstGetContactsFormNet() && LocationThread.checkNetAvailable()){
						mHolder.mLoaddataLayout.setVisibility(View.VISIBLE);
					}else{
						mHolder.mLoaddataLayout.setVisibility(View.GONE);
						mHolder.mNoSearchData.setVisibility(View.VISIBLE);
					}
				}
			}
		});		
		
	}
	@Override
	public byte getType() {
		return NewContactsDataObserver.TYPE_RENREN;
	}
	@Override
	public void onSelect(char c) {
		hideInputMethod(baseView);
		//mHolder.mContactsListView.setSelection(3);
		int index = getProximalIndex(c);
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
		mHolder.mPopText.setVisibility(View.GONE);
		mHolder.mLetterBar.getBackground().setAlpha(0);
	}
	
	private final int getProximalIndex(char c){
		if(mDataType!=C_ContactsData.TYPE.ALL_CONTACTS){
			onUnSelect();
		}
		int index = C_ContactsData.getInstance().getIndex(c, C_ContactsData.TYPE.RENREN_CONTACTS);
		if(index!=-1){
			return (index<<8)|c;
		}
		return -1;
	}
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
			mIsScrolling = false;
		} else {
			mIsScrolling = true;
		}
		
	}
	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public boolean enable() {
		return !mIsScrolling;
	}
	
}
