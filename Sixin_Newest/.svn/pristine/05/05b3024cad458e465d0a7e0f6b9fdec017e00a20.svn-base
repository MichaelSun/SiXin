package com.renren.mobile.chat.ui.plugins;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.common.manager.LoginManager;
import com.common.manager.LoginManager.BindInfoObserver;
import com.common.manager.LoginManager.LoginInfo;
import com.common.mcs.INetReponseAdapter;
import com.common.mcs.INetRequest;
import com.common.mcs.INetResponse;
import com.common.mcs.McsServiceProvider;
import com.core.json.JsonObject;
import com.core.util.SystemService;
import com.core.util.ViewMapUtil;
import com.core.util.ViewMapping;
import com.renren.mobile.chat.R;
import com.renren.mobile.chat.RenrenChatApplication;
import com.renren.mobile.chat.base.views.NotSynImageView;
import com.renren.mobile.chat.dao.ContactsDAO.DataObserver;
import com.renren.mobile.chat.dao.ContactsDAO.DataSubject;
import com.renren.mobile.chat.dao.DAOFactoryImpl;
import com.renren.mobile.chat.dao.ThirdContactsDAO;
import com.renren.mobile.chat.lbs.LocationThread;
import com.renren.mobile.chat.ui.account.BindRenrenAccountActivity;
import com.renren.mobile.chat.ui.account.BindRenrenAccountScreen;
import com.renren.mobile.chat.ui.contact.C_ContactsData;
import com.renren.mobile.chat.ui.contact.C_ContactsData.NewContactsDataObserver;
import com.renren.mobile.chat.ui.contact.ThirdContactModel;
import com.renren.mobile.chat.ui.contact.ThirdContactsActivity;
import com.renren.mobile.chat.ui.contact.UserInfoActivity;
import com.renren.mobile.chat.ui.contact.attention.AttentionAddActiviy;
import com.renren.mobile.chat.ui.contact.attention.AttentionAddAdapter;
import com.renren.mobile.chat.ui.contact.attention.AttentionAddScreen;
import com.renren.mobile.chat.ui.contact.attention.AttentionAddScreen.OnAttentionDataChangedCallback;
import com.renren.mobile.chat.ui.contact.attention.setting.AttentionSettingModel;
import com.renren.mobile.chat.ui.contact.feed.ChatFeedActivity;
import com.renren.mobile.chat.ui.groupchat.ChatMessageGridView;

public class AttentionSettingView implements DataObserver, OnAttentionDataChangedCallback,OnTouchListener, NewContactsDataObserver,BindInfoObserver{
	
	private LinearLayout mRootView = null;
	private LayoutViewHolder mViewHolder;
	private static final int ATTENTION_MAX = 20;
	private static final int ATTENTION_ZERO = 0;
	/**
	 * 是否正在显示插号
	 */
	public boolean mSign;
	private boolean mBindRenren = true;
	private AttentionSettingModel mAddModel, mReduceModel;
	private Context mContext;
	ThirdContactsDAO mThirdContactsDAO;
//	private ProgressDialog mDialog;
	AttentionListAdapter mAdapter;
	/**
	 * 该View是否显示在了插件管理器上~
	 */
	private boolean mShow = false;
	
	public class LayoutViewHolder{
		@ViewMapping(ID=R.id.lc_attention_scroll)
		public ScrollView mScrollView;
		
		@ViewMapping(ID=R.id.lc_gridview)
		public ChatMessageGridView mChatMessageGridView;
		
		@ViewMapping(ID=R.id.lc_attention_setting_loadding)
		public LinearLayout mLoading;
	}
	
	public AttentionSettingView(){
		this.mContext = RenrenChatApplication.getCurrentActivity();
		mRootView = (LinearLayout)SystemService.sInflaterManager.inflate(R.layout.lc_attention_setting, null);
		mViewHolder = new LayoutViewHolder();
		ViewMapUtil.getUtil().viewMapping(mViewHolder, mRootView);
		mAddModel = new AttentionSettingModel(mContext.getResources().getString(R.string.D_AttentionSettingActivity_java_1));		//D_AttentionSettingActivity_java_1=添加; 
		mAddModel.setGridViewType(AttentionSettingModel.ATTENTION_MODEL_TYPE_ADD);
		mReduceModel = new AttentionSettingModel(mContext.getResources().getString(R.string.D_AttentionSettingActivity_java_1));		//D_AttentionSettingActivity_java_1=添加; 
		mReduceModel.setGridViewType(AttentionSettingModel.ATTENTION_MODEL_TYPE_REDUCE);
		mThirdContactsDAO = DAOFactoryImpl.getInstance().buildDAO(ThirdContactsDAO.class);
		mAdapter = new AttentionListAdapter();
		LoginInfo mLoginInfo = LoginManager.getInstance().getLoginInfo();
		if(mLoginInfo.mBindInfoRenren == null || (mLoginInfo.mBindInfoRenren != null && TextUtils.isEmpty(mLoginInfo.mBindInfoRenren.mBindId))){
			mAdapter.setListViewData(null);
			mBindRenren = false;
		}else{
			C_ContactsData.getInstance().registorNewObserver(this);
			C_ContactsData.getInstance().loadRenRenContact(null);
		}
		mViewHolder.mChatMessageGridView.setAdapter(mAdapter);
		mViewHolder.mChatMessageGridView.setOnTouchListener(this);
		AttentionAddScreen.setOnAttentionDataChangedCallback(this);
	}
	
	public View getView(){
		mShow = true;
		return mRootView;
	}
	

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if(mSign == true){
			mSign = false;
			mAdapter.setListViewData(AttentionSettingModel.parseFriendsFoucs(C_ContactsData.getInstance().getRenrenAttentionContacts()));//FocusCache.getCache());
		}
		return false;
	}
	
	private class ViewHolder {
		NotSynImageView head;
		ImageView head_click;
		TextView name;
		Button button;
		FrameLayout head_bg;
		FrameLayout lastView;
	}
	
	public class AttentionListAdapter extends BaseAdapter {
    	private ArrayList<AttentionSettingModel> mAttentionList = new ArrayList<AttentionSettingModel>();
    	public AttentionListAdapter() {
    		mAttentionList.clear();
    		mAttentionList.addAll(AttentionSettingModel.parseFriendsFoucs(C_ContactsData.getInstance().getRenrenAttentionContacts()));//FocusCache.getCache());
    	}
    	public void setListViewData(List<AttentionSettingModel> attentionList){
    		mAttentionList.clear();
    		if(attentionList != null && attentionList.size() != 0){
    			this.mAttentionList.addAll(attentionList);
    		}
    		//添加增加和删除按钮
    		if(mAttentionList.size() < ATTENTION_MAX){
    			if(!mSign){
    				mAttentionList.add(mAddModel);
    			}else{
    				//减到零时取消减少状态~
    				if(mAttentionList.size() == ATTENTION_ZERO){
    					mAttentionList.add(mAddModel);
    					mSign = false;
    				}
    			}
    		}
    		if(mAttentionList.size() != ATTENTION_ZERO + 1){
    			if(!mSign){
    				mAttentionList.add(mReduceModel);
    			}
    		}
    		this.notifyDataSetChanged();
    		if(!mShow){
    			if(mAttentionDownLoadCompletedListener != null){
    				mAttentionDownLoadCompletedListener.onCompleted(mRootView);
    			}
    		}
    		
    	}
		@Override
		public int getCount() {
			return mAttentionList.size();
		}
		@Override
		public Object getItem(int position) {
			return position;
		}
		@Override
		public long getItemId(int position) {
			return position;
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final ViewHolder holder;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = SystemService.sInflaterManager.inflate(R.layout.group_member_item, null);
				holder.head = (NotSynImageView) convertView.findViewById(R.id.group_member_head);
				holder.head_click = (ImageView) convertView.findViewById(R.id.member_head_click);
				holder.name = (TextView) convertView.findViewById(R.id.group_member_name);
				holder.button = (Button) convertView.findViewById(R.id.del_member_btn);
				holder.head_bg = (FrameLayout) convertView.findViewById(R.id.frameLayout);
				holder.lastView = (FrameLayout) convertView.findViewById(R.id.frameLayout1);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			final AttentionSettingModel item = mAttentionList.get(position);
			if(item.getGridViewType() == AttentionSettingModel.ATTENTION_MODEL_TYPE_ADD){
				if(mSign){
					holder.name.setText("");
					holder.head_bg.setVisibility(View.GONE);
					holder.button.setVisibility(View.GONE);
					holder.lastView.setVisibility(View.GONE);
				}else{
					holder.name.setText("");//RenrenChatApplication.getmContext().getResources().getString(R.string.D_AttentionSettingActivity_java_1));		//D_AttentionSettingActivity_java_1=添加; 
					holder.head_bg.setVisibility(View.GONE);
					holder.button.setVisibility(View.GONE);
					holder.lastView.setVisibility(View.VISIBLE);
					holder.lastView.setBackgroundResource(R.drawable.d_attention_add_selector);
					holder.lastView.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							if(!mBindRenren){
								BindRenrenAccountScreen.registerObserver(AttentionSettingView.this);
								Intent bindIntent = new Intent(mContext,BindRenrenAccountActivity.class);
								bindIntent.putExtra(BindRenrenAccountActivity.COME_FROM, BindRenrenAccountActivity.BIND_PLUGIN);
								mContext.startActivity(bindIntent);
							}else{
								Intent intent = new Intent(mContext, AttentionAddActiviy.class);
								mContext.startActivity(intent);
							}
						}
					});
				}
			}else if(item.getGridViewType() == AttentionSettingModel.ATTENTION_MODEL_TYPE_REDUCE){
				//减号的逻辑
				if(mSign){
					holder.name.setText("");
					holder.head_bg.setVisibility(View.GONE);
					holder.button.setVisibility(View.GONE);
					holder.lastView.setVisibility(View.GONE);
				}else{
					holder.name.setText("");//RenrenChatApplication.getmContext().getResources().getString(R.string.D_AttentionSettingActivity_java_1));		//D_AttentionSettingActivity_java_1=添加; 
					holder.head_bg.setVisibility(View.GONE);
					holder.button.setVisibility(View.GONE);
					holder.lastView.setVisibility(View.VISIBLE);
					holder.lastView.setBackgroundResource(R.drawable.d_attention_reduce_selector);
					holder.lastView.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							mSign = true;
							mAdapter.setListViewData(AttentionSettingModel.parseFriendsFoucs(C_ContactsData.getInstance().getRenrenAttentionContacts()));
						}
					});
				}
			}else {
				holder.head_bg.setVisibility(View.VISIBLE);
				holder.lastView.setVisibility(View.GONE);
				holder.head.clear();
				holder.head.addUrl(item.getmHeadUrl());
				holder.name.setText(item.mContactName);
				holder.head.setBackgroundResource(R.drawable.member_head_selector);
//				holder.head_bg.setBackgroundResource(R.drawable.member_head_bg);
//				final ThirdContactModel thirdContactModel = mThirdContactsDAO.query_Contact_BySiXinId(item.mUserId);
				final ThirdContactModel thirdContactModel = C_ContactsData.getInstance().getRenrenAttentionContact(item.mUserId);
				if(mSign){
					holder.head_click.setBackgroundDrawable(null);
					holder.button.setVisibility(View.VISIBLE);
					holder.head_click.setClickable(false);
				}else{
					holder.head_click.setClickable(true);
					holder.button.setVisibility(View.GONE);
					holder.head_click.setBackgroundResource(R.drawable.member_head_selector);
					holder.head_click.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							if(thirdContactModel != null){
								UserInfoActivity.show(mContext, thirdContactModel.getmUserId(),thirdContactModel.getmDomain());
							}
						}
					});
				}
				holder.button.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						
//						showDialog(RenrenChatApplication.getmContext().getResources().getString(R.string.D_AttentionSettingActivity_java_6));		//D_AttentionSettingActivity_java_6=正在取消关注...; 
						INetResponse AttentionDelResponse = new INetReponseAdapter(){
							@Override
							public void onSuccess(INetRequest req, JsonObject data) {
								super.onSuccess(req, data);
								C_ContactsData.getInstance().setRenrenAttentionState(thirdContactModel.mUserId, AttentionAddAdapter.NO_ATTENTION);
								mThirdContactsDAO.update_contact_attention_by_userId(thirdContactModel.mUserId, AttentionAddAdapter.NO_ATTENTION);
								RenrenChatApplication.mHandler.post(new Runnable() {
									@Override
									public void run() {
										mAdapter.setListViewData(AttentionSettingModel.parseFriendsFoucs(C_ContactsData.getInstance().getRenrenAttentionContacts()));
										ChatFeedActivity.needRefresh = true;
									}
								});
							}
							@Override
							public void onError(INetRequest req, JsonObject data) {
								super.onError(req, data);
							}
						};
						McsServiceProvider.getProvider().delFocusFriend(AttentionDelResponse, thirdContactModel.mRenRenId);
					}
				});
			}
			return convertView;
		}
    }

	@Override
	public void notifyUpdate(final String columnName,final boolean isSuccess) {
		RenrenChatApplication.mHandler.post(new Runnable() {
			@Override
			public void run() {
				if(columnName.equals("D_AttentionSettingActivity")){
					if(isSuccess){
//						FocusCache.deleteElement(uid);
						mAdapter.setListViewData(AttentionSettingModel.parseFriendsFoucs(C_ContactsData.getInstance().getRenrenAttentionContacts()));
					}else {
					}
				}
			}
		});
	}

	@Override
	public void registorSubject(DataSubject subject) {
		
	}
	//特别关注插件界面数据下载完毕回调~
	private AttentionDownLoadCompletedListener mAttentionDownLoadCompletedListener;
	
	public void setOnAttentionDownLoadCompletedListener(AttentionDownLoadCompletedListener attentionDownLoadCompletedListener){
		this.mAttentionDownLoadCompletedListener = attentionDownLoadCompletedListener;
	}
	
	public static interface AttentionDownLoadCompletedListener{
		public void onCompleted(View view);
	}

	//特别关注好友数据改变回调~
	@Override
	public void onChange() {
		mAdapter.setListViewData(AttentionSettingModel.parseFriendsFoucs(C_ContactsData.getInstance().getRenrenAttentionContacts()));
	}

	@Override
	public void notifyDataUpdate(byte state, byte type) {
		RenrenChatApplication.sHandler.post(new Runnable() {
			@Override
			public void run() {
				List<ThirdContactModel> renrenContact = C_ContactsData.getInstance().getRenRenContacts();
				if(renrenContact.size() > 0){
					List<AttentionSettingModel> attentionList = AttentionSettingModel.parseFriendsFoucs(C_ContactsData.getInstance().getRenrenAttentionContacts());//AttentionSettingModel.parseFriendsFoucs(mThirdContactsDAO.query_Attention_Contact());
					mViewHolder.mLoading.setVisibility(View.GONE);
					mAdapter.setListViewData(attentionList);//FocusCache.getCache());
					C_ContactsData.getInstance().unRegistorNewObserver(AttentionSettingView.this);
				}else{
					if(ThirdContactsActivity.isFirstGetContactsFormNet() && LocationThread.checkNetAvailable()){
						mViewHolder.mLoading.setVisibility(View.VISIBLE);
					}else{
						mViewHolder.mLoading.setVisibility(View.GONE);
						mAdapter.setListViewData(null);
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
	public void update() {
		// 绑定人人成功后~
		mBindRenren = true;
		C_ContactsData.getInstance().registorNewObserver(this);
		C_ContactsData.getInstance().loadRenRenContact(null);
		BindRenrenAccountScreen.deleteObserver(this);
		
	}

}
