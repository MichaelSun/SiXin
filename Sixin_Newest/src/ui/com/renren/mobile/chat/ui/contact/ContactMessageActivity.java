package com.renren.mobile.chat.ui.contact;

import com.renren.mobile.chat.R;
import com.renren.mobile.chat.RenrenChatApplication;
import com.renren.mobile.chat.base.util.SystemUtil;
import com.renren.mobile.chat.ui.BaseActivity;
import com.renren.mobile.chat.ui.contact.ContactMessageData.ContactMessageDataObserver;
import com.renren.mobile.chat.view.BaseTitleLayout;
import com.renren.mobile.chat.view.BaseTitleLayout.FUNCTION_BUTTON_TYPE;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public final class ContactMessageActivity extends BaseActivity implements ContactMessageDataObserver,OnScrollListener{
	
	private FrameLayout mLinearLayout;
	private BaseTitleLayout mBaseTitle;
	
	private ListView contactMessageListView;
	private ContactMessageAdapter mAdapter;
	
	private LinearLayout mNodataLayout;
	private TextView mNoDataText;
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contact_message);
		initData();
		initTitle();
		initView();
		if(SystemUtil.mDebug){
			SystemUtil.logd("999999999999999999999999999");
		}
	}
	
	private void initData(){
		mAdapter = new ContactMessageAdapter(this);
		ContactMessageData.getInstance().registorNewObserver(this);
		ContactMessageData.getInstance().loadDada();
	}

	private void initTitle() {
		mLinearLayout = (FrameLayout) findViewById(R.id.title_layout);
		mBaseTitle = new BaseTitleLayout(ContactMessageActivity.this,mLinearLayout);
		TextView text = mBaseTitle.getTitleMiddle();
		text.setText(RenrenChatApplication.getmContext().getResources().getString(R.string.contact_message_friend_title));
		mBaseTitle.getTitleLeft().setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		mBaseTitle.setTitleFunctionButtonBackground(FUNCTION_BUTTON_TYPE.SETTING);
		Button rightButton = mBaseTitle.getTitleRightFunctionButton();
		rightButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//AddContactActivity
				Intent intent = new Intent(ContactMessageActivity.this,ContactOptionActivity.class);
				ContactMessageActivity.this.startActivity(intent);
			}
		});
	}
	
	private void initView() {
		contactMessageListView = (ListView) findViewById(R.id.contact_message_listview);
		contactMessageListView.setDivider(null);
		contactMessageListView.setAdapter(mAdapter);
		contactMessageListView.setOnScrollListener(this);
		contactMessageListView.setItemsCanFocus(true);
		contactMessageListView.setAddStatesFromChildren(true);
		contactMessageListView.setFocusableInTouchMode(true);
		contactMessageListView.setVerticalFadingEdgeEnabled(false);
		contactMessageListView.setCacheColorHint(0);
		contactMessageListView.setScrollingCacheEnabled(false);
		contactMessageListView.setDrawingCacheEnabled(false);
		contactMessageListView.setAlwaysDrawnWithCacheEnabled(false);
		contactMessageListView.setWillNotCacheDrawing(true);
		
		mNodataLayout =(LinearLayout) this.findViewById(R.id.contacts_nodata);
		mNoDataText = (TextView) this.findViewById(R.id.data_textView);
	}

	@Override
	protected void onDestroy() {
		ContactMessageData.getInstance().unRegistorNewObserver(this);
		super.onDestroy();
	}

	@Override
	public void notifyDataUpdate(byte state, final byte type) {
		RenrenChatApplication.mHandler.post(new Runnable(){
			@Override
			public void run() {
				if(SystemUtil.mDebug){
					SystemUtil.logd("notifyDataUpdate contact message type="+type);
				}
				if(type==ContactMessageDataObserver.TYPE_ADD||type==ContactMessageDataObserver.TYPE_UPDATE){
					ContactMessageData.getInstance().clearUnReadCount();
				}
				mAdapter.notifyData();
				changeVisiable();
			}
		});
	}
	
	private final void changeVisiable(){
		if(mAdapter.getCount()==0){//没有私信联系人
			mNoDataText.setText(RenrenChatApplication.getmContext().getResources().getString(R.string.contact_message_friend_nodata));
			mNodataLayout.setVisibility(View.VISIBLE);
			contactMessageListView.setVisibility(View.GONE);
		}else{
			mNodataLayout.setVisibility(View.GONE);
			contactMessageListView.setVisibility(View.VISIBLE);
		}
	}
	
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if(SystemUtil.mDebug){
			SystemUtil.logd("onScrollStateChanged-------------");
		}
		boolean isScrolling=false;
		if(scrollState==AbsListView.OnScrollListener.SCROLL_STATE_IDLE){
			isScrolling=false;
		}else{
			isScrolling=true;
		}
		mAdapter.setScrollState(isScrolling);
//		if(this.mDataType==ALL_CONTACTS){
//			mAdapter_All.setScrollState(isScrolling);
//		}else{
//			mAdapter_Search.setScrollState(isScrolling);
//		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		
	}
	
	
}
