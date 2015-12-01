package com.renren.mobile.chat.ui.contact.mutichat;
import android.app.Activity;
import android.app.Dialog;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.FrameLayout;

import com.common.manager.LoginManager;
import com.common.network.AbstractNotSynRequest;
import com.common.network.AbstractNotSynRequest.OnDataCallback;
import com.common.network.NULL;
import com.core.util.SystemService;
import com.core.util.ViewMapUtil;
import com.renren.mobile.chat.R;
import com.renren.mobile.chat.RenrenChatApplication;
import com.renren.mobile.chat.actions.models.RoomInfoModelWarpper;
import com.renren.mobile.chat.actions.requests.RequestConstructorProxy;
import com.renren.mobile.chat.base.util.SystemUtil;
import com.renren.mobile.chat.ui.BaseScreen;
import com.renren.mobile.chat.ui.contact.RoomInfosData;
import com.renren.mobile.chat.ui.setting.SettingDataManager;
import com.renren.mobile.chat.util.ChatMessageSender;
import com.renren.mobile.chat.view.BaseTitleLayout;
import com.renren.mobile.chat.view.BaseTitleLayout.FUNCTION_BUTTON_TYPE;

public class MultiChatNameSettingScreen extends BaseScreen {
	MultiChatNameSettingScreenHolder mHolder = new MultiChatNameSettingScreenHolder();
	/**
	 * 群id
	 */
	long mGroupId;
	RoomInfoModelWarpper mRoomInfo = null;
	/**
	 * 输入框文本
	 */
	private String mStr;
	private String lastStr = "";
	/**
	 * 标题栏确认修改按钮
	 */
	private Button confirm;
	/**
	 * 标题栏
	 */
	private BaseTitleLayout mTitleLayout;
	private FrameLayout mLayout;
	/** 
	 * 输入法管理
	 */
	protected InputMethodManager mInputMethodMangager;
	
	
	private Dialog mDialog;
	/**
	 * constructor
	 * @param activity
	 */
	public MultiChatNameSettingScreen(Activity activity, long groupId) {
		super(activity);
		this.mActivity = activity;
		this.mGroupId = groupId;
		mInputMethodMangager = SystemService.sInputMethodManager;
		mRoomInfo = RoomInfosData.getInstance().getRoomInfo(mGroupId);
		
		mDialog = SettingDataManager.getInstance().createLoadingDialog(mActivity, RenrenChatApplication.getmContext().getResources().getString(R.string.MultiChatNameSettingScreen_java_3));
		
		initialLayout();
		initialTitle();
	}
	/**
	 * 初始化标题栏
	 */
	public void initialTitle(){
		this.mTitleLayout = getTitle();
		mTitleLayout.setTitleMiddle(mActivity.getResources().getString(R.string.MultiChatNameSettingScreen_java_1));		//MultiChatNameSettingScreen_java_1=修改群聊名称; 
		mTitleLayout.setRightButtonBackground(FUNCTION_BUTTON_TYPE.SEND);
		Button cancel = mTitleLayout.getTitleLeft();
		cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		confirm = mTitleLayout.getRightOtherBtn();
		confirm.setText(mActivity.getResources().getString(R.string.MultiChatNameSettingScreen_java_6));
		confirm.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if("".equalsIgnoreCase(mStr)){
					SystemUtil.toast(RenrenChatApplication.getmContext().getResources().getString(R.string.MultiChatNameSettingScreen_java_2));		//MultiChatNameSettingScreen_java_2=请输入群名; 
					return;
				}
				mInputMethodMangager.toggleSoftInput(0, 0);
				if(!mDialog.isShowing()){
					mDialog.show();
				}
				updateGroupName();
			}
		});
		confirm.setEnabled(false);//.setClickable(false);
//		confirm.setBackgroundResource(R.drawable.disable_state);
		mTitleLayout.setRightButtonEnableState(FUNCTION_BUTTON_TYPE.SEND, false);
	}
	/**
	 * 初始化layout
	 */
	public void initialLayout(){
		mLayout = (FrameLayout) mInflater.inflate(R.layout.multi_chat_name_setting, null);
		setContent(mLayout);
		ViewMapUtil.getUtil().viewMapping(mHolder, mLayout);
		
		if(null != mRoomInfo.getName()){
			if(mRoomInfo.getName().length()>=20){
				mHolder.mInputEditText.setText(mRoomInfo.getName().substring(0, 20));
				mHolder.mInputEditText.setSelection(20);
			}else{
				mHolder.mInputEditText.setText(mRoomInfo.getName());
				mHolder.mInputEditText.setSelection(mRoomInfo.getName().length());
			}
		}else{
			mHolder.mInputEditText.setHint(RenrenChatApplication.getmContext().getResources().getString(R.string.MultiChatNameSettingScreen_java_4));		//MultiChatNameSettingScreen_java_4=未命名; 
		}
		mStr = mHolder.mInputEditText.getText().toString();
		/**按回车键后的处理事件*/ 
		mHolder.mInputEditText.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (KeyEvent.KEYCODE_ENTER == keyCode) {
					return true;
				}
				return false; 
			}
		});
		mHolder.mInputEditText.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				mStr = mHolder.mInputEditText.getText().toString();
				if(mStr.toString().length() >= 20){
					SystemUtil.toast(RenrenChatApplication.getmContext().getResources().getString(R.string.D_AttentionScreen_java_2));		//D_AttentionScreen_java_2=最多输入20个字; 
					return;
				}
				if(mStr.toString().length() == 0){
					mHolder.mInputEditText.setHint(RenrenChatApplication.getmContext().getResources().getString(R.string.MultiChatNameSettingScreen_java_2));		//MultiChatNameSettingScreen_java_2=请输入群名; 
					mHolder.mIcon.setVisibility(View.GONE);
					confirm.setEnabled(false);//confirm.setClickable(false);
					//confirm.setBackgroundResource(R.drawable.disable_state);
					mTitleLayout.setRightButtonEnableState(FUNCTION_BUTTON_TYPE.SEND, false);
					return;
				}
				confirm.setEnabled(true);//confirm.setClickable(true);
//				confirm.setBackgroundResource(R.drawable.blue_button);
				mTitleLayout.setRightButtonEnableState(FUNCTION_BUTTON_TYPE.SEND, true);
				mHolder.mIcon.setVisibility(View.VISIBLE);
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
			@Override
			public void afterTextChanged(Editable s) {
			}
		});
		mHolder.searchEditTextLayout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mHolder.mInputEditText.setSelection(mHolder.mInputEditText.getText().toString().length());
				mInputMethodMangager.showSoftInput(mHolder.mInputEditText, 0);
			}
		});
		mHolder.mIcon.setVisibility(View.VISIBLE);
		mHolder.mIcon.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mHolder.mInputEditText.setText("");
				mHolder.mInputEditText.setHint(RenrenChatApplication.getmContext().getResources().getString(R.string.MultiChatNameSettingScreen_java_2));		//MultiChatNameSettingScreen_java_2=请输入群名; 
				mInputMethodMangager.showSoftInput(mHolder.mInputEditText, 0);
				mHolder.mIcon.setVisibility(View.GONE);
			}
		});
	}

	/**
	 * 修改群名
	 * @param groupId
	 */
	public void updateGroupName(){
    	AbstractNotSynRequest<NULL> abstractNotSynRequest = RequestConstructorProxy.getInstance().updateRoomSubject(LoginManager.getInstance().getLoginInfo().mUserId, mGroupId, mStr);
    	abstractNotSynRequest.setCallback(new OnDataCallback<NULL>() {
			@Override
			public void onSuccess() {
				RenrenChatApplication.mHandler.post(new Runnable() {
					@Override
					public void run() {
						SystemUtil.toast(RenrenChatApplication.getmContext().getResources().getString(R.string.MultiChatNameSettingScreen_java_5));		//MultiChatNameSettingScreen_java_5=修改群名成功; 
//						mHolder.loadingLayout.setVisibility(View.GONE);
						if(mDialog.isShowing()){
							mDialog.dismiss();
						}
						mActivity.finish();
					}
				});
			}
			@Override
			public void onSuccessRecive(NULL data) {
				RenrenChatApplication.mHandler.post(new Runnable() {
					@Override
					public void run() {
					}
				});
			}
			@Override
			public void onError(int errorCode,final String errorMsg) {
				RenrenChatApplication.mHandler.post(new Runnable() {
					@Override
					public void run() {
						SystemUtil.toast(errorMsg);
//						mHolder.loadingLayout.setVisibility(View.GONE);
						if(mDialog.isShowing()){
							mDialog.dismiss();
						}
						mActivity.finish();
					}
				});
			}
		});
    	ChatMessageSender.getInstance().sendRequestToNet(abstractNotSynRequest);
    }
}
