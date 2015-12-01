package com.renren.mobile.chat.ui.setting;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.common.mcs.INetRequest;
import com.common.mcs.INetResponse;
import com.common.mcs.McsServiceProvider;
import com.core.json.JsonArray;
import com.core.json.JsonObject;
import com.core.json.JsonValue;
import com.core.util.SystemService;
import com.core.util.ViewMapUtil;
import com.core.util.ViewMapping;
import com.renren.mobile.chat.R;
import com.renren.mobile.chat.RenrenChatApplication;
import com.renren.mobile.chat.base.util.SystemUtil;
import com.renren.mobile.chat.common.ResponseError;
import com.renren.mobile.chat.ui.BaseScreen;
import com.renren.mobile.chat.ui.PullUpdateTouchListener;
import com.renren.mobile.chat.ui.contact.ContactModel;

/**
 * @author xiangchao.fan
 * @description 黑名单列表
 */
public class BlacklistScreen extends BaseScreen {

	public class ViewHolder{
		
		@ViewMapping(ID=R.id.blacklist_list)
		public ListView mContentListView;
		
		@ViewMapping(ID=R.id.blacklist_frame)
		public FrameLayout mFrameLayout;
		
		@ViewMapping(ID=R.id.blacklist_loading)
		public View mLoading;
		
		/** no data */
		@ViewMapping(ID=R.id.blacklist_nodata)
		public View mNoData;
		
		@ViewMapping(ID=R.id.data_imageView)
		public ImageView mNoDataIamgeView;
		
		@ViewMapping(ID=R.id.data_textView)
		public TextView mNoDataTextView;
		
		@ViewMapping(ID=R.id.data_textView1)
		public TextView mNoDataTextView1;
	}
	
	private ViewHolder mHolder;
	
	private View mBlacklistView;
	
	private List<ContactModel> mData = new ArrayList<ContactModel>();
	private BlacklistAdapter mAdapter;
	
	/** 下拉刷新监听 */
	private PullUpdateTouchListener touchListener;
	
	/** 防止第一次加载黑名单过程中下拉刷新 */
	private boolean isFirtLoading = false;
	
	public BlacklistScreen(Activity activity) {
		super(activity);
		mBlacklistView = SystemService.sInflaterManager.inflate(R.layout.f_blacklist, null);
		setContent(mBlacklistView);
		
		initTitle();
		initView();
		initEvent();
		
		mHolder.mLoading.setVisibility(View.VISIBLE);
		isFirtLoading = true;
		//getBlacklist();
	}

	public void initTitle(){
		this.getTitle().setTitleMiddle(mActivity.getResources().getString(R.string.BlacklistScreen_1));
		this.getTitle().getTitleLeft().setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
	
	public void initView(){
		mHolder = new ViewHolder();
		ViewMapUtil.getUtil().viewMapping(mHolder, mBlacklistView);
		
		mAdapter = new BlacklistAdapter(mActivity);
		mHolder.mContentListView.setAdapter(mAdapter);
		mHolder.mContentListView.setDivider(mActivity.getResources().getDrawable(R.drawable.listview_item_divider));
		// 滑动时不重绘页面
		mHolder.mContentListView.setScrollingCacheEnabled(false);       
		mHolder.mContentListView.setDrawingCacheEnabled(false);         
		mHolder.mContentListView.setAlwaysDrawnWithCacheEnabled(false); 
		mHolder.mContentListView.setWillNotCacheDrawing(true);

		// 下拉刷新
		touchListener = new PullUpdateTouchListener(SystemService.sInflaterManager) {
			
			@Override
			public void refresh() {
				getBlacklist();
			}
			
			@Override
			public boolean isHead() {
				if(isFirtLoading){
					return false;
				}
				ListView listview = mHolder.mContentListView;
//				if(listview.getChildCount() > 0){
//					return listview.getFirstVisiblePosition() == 0 && Math.abs(listview.getChildAt(0).getTop()) <= 10 ;
//				}
				if (listview.getChildAt(0) != null) {
					return listview.getFirstVisiblePosition() == 0
							&& listview.getChildAt(
									listview.getFirstVisiblePosition())
									.getTop() == 0;
				}
				return true;
			}
			
			@Override
			public void afterDo() {
				LastUpdateTime = System.currentTimeMillis();
			}
		};
		touchListener.initView(mHolder.mContentListView, mHolder.mFrameLayout);
		mHolder.mFrameLayout.addView(touchListener.getPullUpdateView(), new FrameLayout.LayoutParams(FrameLayout.LayoutParams.FILL_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT));
	
		// no data
		mHolder.mNoDataTextView.setText(mActivity.getResources().getString(R.string.BlacklistScreen_3));
	}
	
	public void initEvent(){
//		mHolder.mContentListView.setOnItemClickListener(new OnItemClickListener() {
//
//			@Override
//			public void onItemClick(AdapterView<?> parent, View view, int position,
//					long id) {
//				SystemUtil.f_log("================================11111111111111");
//				UserInfoActivity.show(mActivity, (ContactModel)mAdapter.getItem(position), UserInfoActivity.COMEFROM_SIXIN);
//			}
//		});
	}
	
	/** 获取用户profile */
	public void getBlacklist() {
		mHolder.mNoData.setVisibility(View.GONE);
		McsServiceProvider.getProvider().getBlacklist(new INetResponse() {

			@Override
			public void response(final INetRequest req, final JsonValue obj) {
				
				if (obj != null && obj instanceof JsonObject) {
					RenrenChatApplication.HANDLER.post(new Runnable() {
						
						@Override
						public void run() {
							boolean resultFlag = false;
							
							final JsonObject map = (JsonObject) obj;
							SystemUtil.f_log("map:" + map.toJsonString());
							if(ResponseError.noError(req, map, false)) {
								mData.clear();
								JsonArray array = map
										.getJsonArray("contact_list");
								if (array != null) {
									resultFlag = true;
									mData = ContactModel.newParseContactModels(array);
									mAdapter.setAdapterData(mData);
								}
							}
							if(resultFlag){
								mAdapter.setAdapterData(mData);
							}else{
								mHolder.mNoData.setVisibility(View.VISIBLE);
								mAdapter.setAdapterData(null);
							}
							touchListener.onRefreshComplete();
							mHolder.mLoading.setVisibility(View.GONE);
							
							isFirtLoading = false;
						}
					});
				}

			}
		});
	}
	
	public void refresh(){
		SystemUtil.f_log("resume");
		//isFirtLoading = false;
		getBlacklist();
	}
}
