package com.renren.mobile.chat.ui.contact;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.core.util.SystemService;
import com.renren.mobile.chat.R;
import com.renren.mobile.chat.RenrenChatApplication;
import com.renren.mobile.chat.base.util.SystemUtil;
import com.renren.mobile.chat.ui.BaseActivity;
import com.renren.mobile.chat.ui.contact.C_ContactsData.NewContactsDataObserver;
import com.renren.mobile.chat.view.BaseTitleLayout;


public final class SearchContactActivity extends BaseActivity implements OnScrollListener, TextWatcher,NewContactsDataObserver{

	private FrameLayout mLinearLayout;
	private BaseTitleLayout mBaseTitle;
	private EditText searchEditText;
	private ImageView searchTextClear;
	private Button searchBtn;
	private TextView noResult;
	private ListView searchResultListView;
	private SearchContactAdapter mSearchResultAdapter;
	protected boolean mLineError;
	private ProgressDialog loadingDialog;   //正在搜索的提示
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.contact_search);
		C_ContactsData.getInstance().registorNewObserver(this);
		initTitle();
		initView();
		
//		ContactMessageModel model = new ContactMessageModel();
//		model.setType(ContactMessageModel.TYPE_ADD);
//		model.setGid(100001402);
//		model.setDomain("talk.sixin.com");
//		model.setFrom_type(ContactMessageModel.FROM_TYPE_ADDRESS);
//		model.setFrom_text("通讯录好友");
//		model.setName("李星辰");
//		model.setHead_url("http://hdn.xnimg.cn/photos/hdn321/20110512/2120/tiny_qORI_123268m019118.jpg");
//		model.setBody("李星辰已经将你加为他的联系人");
//		model.setNativeId(System.currentTimeMillis());
//		ContactMessageData.getInstance().notifyData(model);
		
	}
	
	private void initTitle() {
		mLinearLayout = (FrameLayout) findViewById(R.id.title_layout);
		mBaseTitle = new BaseTitleLayout(SearchContactActivity.this,mLinearLayout);
		TextView text = mBaseTitle.getTitleMiddle();
		text.setText(getText(R.string.search_contact_title));
		mBaseTitle.getTitleLeft().setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
	
	private void initView() {
		
		loadingDialog = new ProgressDialog(this);
		loadingDialog.setMessage(getText(R.string.contact_searching));
		
		searchEditText = (EditText) findViewById(R.id.search_edit);
		searchEditText.addTextChangedListener(this);
		searchTextClear = (ImageView) findViewById(R.id.search_del);
		searchTextClear.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				searchEditText.setText("");
			}
		});
		noResult = (TextView) findViewById(R.id.search_contact_nodata);
		searchBtn = (Button) findViewById(R.id.search_button);
		searchResultListView = (ListView) findViewById(R.id.search_contact_listview);
		searchResultListView.setDivider(getResources().getDrawable(R.drawable.listview_item_divider));
		mSearchResultAdapter = new SearchContactAdapter(this);
		searchResultListView.setAdapter(mSearchResultAdapter);
		searchResultListView.setOnScrollListener(this);
		
		searchResultListView.setItemsCanFocus(true);
		searchResultListView.setAddStatesFromChildren(true);
		searchResultListView.setFocusableInTouchMode(true);
		searchResultListView.setVerticalFadingEdgeEnabled(false);
		searchResultListView.setCacheColorHint(0);
		searchResultListView.setScrollingCacheEnabled(false);
		searchResultListView.setDrawingCacheEnabled(false);
		searchResultListView.setAlwaysDrawnWithCacheEnabled(false);
		searchResultListView.setWillNotCacheDrawing(true);
		
		searchBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (TextUtils.isEmpty(searchEditText.getText().toString().trim())){
					Toast.makeText(SearchContactActivity.this, "搜索内容不能为空", Toast.LENGTH_LONG).show();
					return;
				}else{
					showLoadingDialog(true);
					SystemService.sInputMethodManager.hideSoftInputFromWindow(searchEditText.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
					C_ContactsData.getInstance().query_FindContacts_FromNet(searchEditText.getText().toString().trim());
				}	
			}
		});
	}
	
	@Override
	protected void onDestroy() {
		C_ContactsData.getInstance().unRegistorNewObserver(this);
		super.onDestroy();
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		String searchKey = s+"";
		int pading = (int)SearchContactActivity.this.getResources().getDimension(R.dimen.search_edittext_padding);
//		if(SystemUtil.mDebug){
//			SystemUtil.logd("padding="+pading);
//		}
		if(searchKey.trim().length() == 0) {
			searchTextClear.setVisibility(View.GONE);
			SystemService.sInputMethodManager.showSoftInput(searchEditText, InputMethodManager.SHOW_IMPLICIT);
			searchEditText.setPadding(pading, 0, 0, 0);
		}else {
			searchTextClear.setVisibility(View.VISIBLE);
			searchEditText.setPadding(pading, 0, pading, 0);
			if(searchKey.length() >= 20){
				SystemUtil.toast(RenrenChatApplication.getmContext().getResources().getString(R.string.D_AttentionScreen_java_2));		//D_AttentionScreen_java_2=最多输入20个字; 
			}
			searchEditText.requestFocus();
		}
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		boolean isScrolling = false;
		if(scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE){
			isScrolling = false;
		}else{
			isScrolling = true;
		}
		mSearchResultAdapter.setScrollState(isScrolling);
	}
	
	//没用的方法
	@Override
	public void afterTextChanged(Editable s) {
	}
	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
	}
	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
	}
	
	@Override
	public byte getType() {
		return NewContactsDataObserver.TYPE_FIND;
	}

	@Override
	public void notifyDataUpdate(final byte state, final byte type) {
		RenrenChatApplication.sHandler.post(new Runnable() {
			@Override
			public void run() {
				if(SystemUtil.mDebug){
					SystemUtil.errord("搜索联系人state="+state+"#type="+Integer.toBinaryString(type));
				}
				showLoadingDialog(false);
				mLineError=(state!=NewContactsDataObserver.DATA_STATE_OK);
				changeVisiable();
				mSearchResultAdapter.notifyData(C_ContactsData.getInstance().getFindContacts());
          }});
	}
		
	
	
	private final void changeVisiable(){
			if(C_ContactsData.getInstance().getFindContacts().size()==0){//没有私信联系人
				noResult.setVisibility(View.VISIBLE);
				searchResultListView.setVisibility(View.GONE);
			}else{//有联系人数据
				noResult.setVisibility(View.GONE);
				searchResultListView.setVisibility(View.VISIBLE);
			}
	}
	
	private void showLoadingDialog(boolean isShow) {
		if(isShow && !isLoading()) {
			loadingDialog.show();
		}else if(!isShow && isLoading()) {
			loadingDialog.dismiss();
		}
	}
	
	private boolean isLoading() {
		if(loadingDialog.isShowing()) {
			return true;
		}else {
			return false;
		}
	}

	@Override
	protected void onResume() {
		//SystemUtil.logd("onResume");
		mSearchResultAdapter.notifyDataSetChanged();
		super.onResume();
	}
	
	
}
