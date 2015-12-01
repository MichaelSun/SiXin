package com.renren.mobile.chat.ui.setting;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
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
import com.renren.mobile.chat.view.BaseTitleLayout;
import com.renren.mobile.chat.view.BaseTitleLayout.FUNCTION_BUTTON_TYPE;

/**
 * @author xiangchao.fan
 * @description 选择学校screen
 */
public class SelectCollegeScreen extends BaseScreen {

	public class ViewHolder{
		@ViewMapping(ID=R.id.search_layout)
		public FrameLayout mSearchLayout;
		
		@ViewMapping(ID=R.id.search_edit)
		public EditText mSearchText;

		@ViewMapping(ID=R.id.search_del)
		public ImageView mSearchDelIcon;
		
		@ViewMapping(ID=R.id.college_list)
		public ListView mContentListView;
		
		@ViewMapping(ID=R.id.college_loading)
		public View mLoading;
		
		/** no data */
		@ViewMapping(ID=R.id.college_nodata)
		public View mNoData;
		
		@ViewMapping(ID=R.id.data_imageView)
		public ImageView mNoDataIamgeView;
		
		@ViewMapping(ID=R.id.data_textView)
		public TextView mNoDataTextView;
		
		@ViewMapping(ID=R.id.data_textView1)
		public TextView mNoDataTextView1;
	}
	private Button mConfirm;
	
	private ViewHolder mHolder;
	private View mSelectCollegeView;
	
	/** 搜索框字符串 */
	private String mStr;
	
	/** 选择的学校 */
	private String mSchool;
	private String mTempSchool;
	
	/** 搜索结果list */
	private List<String> mData = new ArrayList<String>();
	private SelectCollegeAdapter mAdapter;
	
	private BaseTitleLayout mTitleLayout;
	
	public SelectCollegeScreen(Activity activity) {
		super(activity);
		mSelectCollegeView = SystemService.sInflaterManager.inflate(R.layout.f_select_college, null);
		setContent(mSelectCollegeView);
		
		initTitle();
		initView();
		initEvent();
	}

	public void initTitle(){
		mTitleLayout = this.getTitle();
		mTitleLayout.setTitleMiddle(mActivity.getResources().getString(R.string.SelectCollegeScreen_1));
		mTitleLayout.setLeftButtonBackground(FUNCTION_BUTTON_TYPE.CANCEL);
		mTitleLayout.setRightButtonBackground(FUNCTION_BUTTON_TYPE.SEND);
		
		Button cancel = mTitleLayout.getLeftOtherBtn();
		cancel.setText(mActivity.getResources().getString(R.string.SelectCollegeScreen_5));

		cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});

		mConfirm = mTitleLayout.getRightOtherBtn();
		mConfirm.setText(mActivity.getResources().getString(R.string.SelectCollegeScreen_4));
		
		mConfirm.setEnabled(false);
		//mConfirm.setBackgroundResource(R.drawable.disable_state);
		mTitleLayout.setRightButtonEnableState(FUNCTION_BUTTON_TYPE.SEND, false);
		
		mConfirm.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(!TextUtils.isEmpty(mTempSchool)){
					SettingDataManager.getInstance().putSchool(mTempSchool);
					SettingDataManager.getInstance().notifyCollegeSelectListener();
					finish();
				}else{
					SystemUtil.toast(mActivity.getResources().getString(R.string.SelectCollegeScreen_6));
				}
			}
		});
	}
	
	public void initView(){
		mHolder = new ViewHolder();
		ViewMapUtil.getUtil().viewMapping(mHolder, mSelectCollegeView);
		
		mHolder.mSearchText.setHint(mActivity.getResources().getString(R.string.SelectCollegeScreen_2));
		mHolder.mSearchText.setInputType(InputType.TYPE_CLASS_TEXT);
		mHolder.mSearchLayout.setPadding(10, 6, 10, 6);
		
		mAdapter = new SelectCollegeAdapter(mActivity);
		mHolder.mContentListView.setAdapter(mAdapter);
		mHolder.mContentListView.setDivider(mActivity.getResources().getDrawable(R.drawable.listview_item_divider));
		// 滑动时不重绘页面
		mHolder.mContentListView.setScrollingCacheEnabled(false);       
		mHolder.mContentListView.setDrawingCacheEnabled(false);         
		mHolder.mContentListView.setAlwaysDrawnWithCacheEnabled(false); 
		mHolder.mContentListView.setWillNotCacheDrawing(true);
		
		// no data
		mHolder.mNoDataTextView.setText(mActivity.getResources().getString(R.string.SelectCollegeScreen_3));
	}
	
	public void initEvent(){
		
		mHolder.mContentListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				
				mSchool = (String) mAdapter.getItem(position);
				SystemUtil.f_log("school：" + mSchool);
				mHolder.mSearchText.setText(mSchool);
				mHolder.mSearchText.setSelection(mSchool.length());
				
				mConfirm.setEnabled(true);
				//mConfirm.setBackgroundResource(R.drawable.grey_button);
				mTitleLayout.setRightButtonEnableState(FUNCTION_BUTTON_TYPE.SEND, true);
				
//				SettingDataManager.getInstance().putSchool(mSchool);
//				SettingDataManager.getInstance().notifyCollegeSelectListener();
//				finish();

			}
		});
		
		mHolder.mSearchText.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				mTempSchool = mSchool;
				mSchool = "";
				mStr = mHolder.mSearchText.getText().toString();
				if(mStr.toString().length() >= 2){
					mHolder.mNoData.setVisibility(View.GONE);
					mHolder.mNoData.setVisibility(View.GONE);
					SystemUtil.f_log("search=======================text change:" + mStr);
					McsServiceProvider.getProvider().searchColleges(
							mStr, response);
				}
				if(mStr.toString().length() == 0){
					mHolder.mSearchText.setHint(RenrenChatApplication.getmContext().getResources().getString(R.string.SelectCollegeScreen_2));
					mHolder.mSearchDelIcon.setVisibility(View.GONE);
					mAdapter.setAdapterData(null);
					
					mConfirm.setEnabled(false);
					//mConfirm.setBackgroundResource(R.drawable.disable_state);
					mTitleLayout.setRightButtonEnableState(FUNCTION_BUTTON_TYPE.SEND, false);
					
					return;
				}
				 mHolder.mSearchText.requestFocus();
				mHolder.mSearchDelIcon.setVisibility(View.VISIBLE);
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
			
			@Override
			public void afterTextChanged(Editable s) {
			}
		});
		
		mHolder.mSearchDelIcon.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mHolder.mSearchText.setText("");
				mHolder.mSearchText.setHint(RenrenChatApplication.getmContext().getResources().getString(R.string.SelectCollegeScreen_2));
				mHolder.mSearchDelIcon.setVisibility(View.GONE);
				mHolder.mNoData.setVisibility(View.GONE);
				mHolder.mNoData.setVisibility(View.GONE);
				mAdapter.setAdapterData(null);
			}
		});
		/**按回车键后的处理事件*/ 
		mHolder.mSearchText.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (KeyEvent.KEYCODE_ENTER == keyCode) {
					return true;
				}
				return false; 
			}
		});
	}
	
	/** 接口返回response */
	INetResponse response = new INetResponse() {

		@Override
		public void response(final INetRequest req,
				JsonValue obj) {
			SystemUtil.f_log("map:");
			if (obj != null && obj instanceof JsonObject) {
				final JsonObject map = (JsonObject) obj;
				SystemUtil.f_log("map:" + map.toJsonString());
				RenrenChatApplication.mHandler.post(new Runnable() {
					@Override
					public void run() {
						if (ResponseError.noError(req, map,false)) {
							mData.clear();
							// 接收数据
							JsonArray schools = map.getJsonArray("school");
							if(schools != null){
								for(int i=0;i<schools.size();i++){
									JsonObject school = (JsonObject) schools.get(i);
									mData.add(school.getString("name"));
								}
								mAdapter.setAdapterData(mData);
							}else{
								mHolder.mNoData.setVisibility(View.VISIBLE);
								mAdapter.setAdapterData(null);
							}
						}
					}
				});
			}
		}
	};
	
}
