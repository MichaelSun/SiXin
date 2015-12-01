package com.renren.mobile.chat.ui.account;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.res.Resources;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.common.manager.BindInfo;
import com.common.manager.LoginManager;
import com.common.manager.LoginManager.BindInfoObserver;
import com.common.manager.LoginManager.LoginInfo;
import com.common.mcs.INetReponseAdapter;
import com.common.mcs.INetRequest;
import com.common.mcs.INetResponse;
import com.common.mcs.McsServiceProvider;
import com.core.json.JsonObject;
import com.core.util.Md5;
import com.core.util.SystemService;
import com.core.util.ViewMapUtil;
import com.core.util.ViewMapping;
import com.renren.mobile.account.LoginControlCenter;
import com.renren.mobile.account.LoginfoModel;
import com.renren.mobile.chat.R;
import com.renren.mobile.chat.RenrenChatApplication;
import com.renren.mobile.chat.base.util.SystemUtil;
import com.renren.mobile.chat.base.views.NotSynImageView;
import com.renren.mobile.chat.ui.BaseScreen;
import com.renren.mobile.chat.ui.contact.C_ContactsData;
import com.renren.mobile.chat.ui.contact.feed.ChatFeedAdapter;

public class BindRenrenAccountScreen extends BaseScreen{
	
	private static final String BIND_RENREN_TYPE = "type";
	
	private static final String BIND_RENREN_PAGE = "page";
	
	private static final String BIND_RENREN_NAME = "name";
	
	private static final String BIND_RENREN_BIND_ID = "bind_id";
	
	private static final String BIND_RENREN_HEAD_URL = "medium_url";
	
	private static ArrayList<BindInfoObserver> mObserverList = new ArrayList<BindInfoObserver>();
	
	public class ViewHolder{
		
		// has bind layout
		@ViewMapping(ID=R.id.has_bind_renren_account_layout)
		public LinearLayout mHasBindRenrenAccountLayout;
		
		@ViewMapping(ID=R.id.renren_account_head)
		public NotSynImageView mRenrenAccount_head;
		
		@ViewMapping(ID=R.id.renren_account_name)
		public TextView mRenrenAccountName;
		
//		@ViewMapping(ID=R.id.renren_account_college)
//		public TextView mRenrenAccountCollege;
		
		@ViewMapping(ID=R.id.btn_clear_bind)
		public Button mClearBind;
		
		// not bind layout
		@ViewMapping(ID=R.id.not_bind_renren_account_layout)
		public ScrollView mNotBindRenrenAccountLayout;
		
		@ViewMapping(ID=R.id.renren_account_id)
		public EditText mRenrenAccountID;
		
		@ViewMapping(ID=R.id.renren_account_pwd)
		public EditText mRenrenAccountPwd;
		
		@ViewMapping(ID=R.id.btn_bind_renren_account)
		public Button mBind;
		
		@ViewMapping(ID=R.id.renren_account_id_del)
		public ImageView mRenrenAccountDel;
		
		@ViewMapping(ID=R.id.renren_account_psw_del)
		public ImageView mRenrenPswDel;
	}
	
	private ViewHolder mHolder;
	private View mBindRenrenAccountView;
	private BindRenrenAccountActivity mBindRenrenAccountActivity;
	
	private Dialog mUnbindDialog;
//	private ProgressDialog mProgressDialog;
	private Dialog mDialog;
	private Resources mResources;
	

	public BindRenrenAccountScreen(BindRenrenAccountActivity activity) {
		super(activity);
		this.mBindRenrenAccountActivity = activity;
		this.mResources = mBindRenrenAccountActivity.getResources();
		mBindRenrenAccountView = SystemService.sInflaterManager.inflate(R.layout.lc_bind_renren_account, null);
		initView();
		initEvent();
		setContent(mBindRenrenAccountView);
		this.getTitle().setTitleMiddle(mActivity.getResources().getString(R.string.BindRenrenAccountScreen_1));
		this.getTitle().setTitleMiddleIcon(R.drawable.ykn_renren_logo);
		this.getTitle().getTitleLeft().setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	public void initView(){
		mHolder = new ViewHolder();
		ViewMapUtil.getUtil().viewMapping(mHolder, mBindRenrenAccountView);
		mUnbindDialog = createUnbindDialog();
		mDialog = new Dialog(mActivity, R.style.sendRequestDialog);
		mDialog.setContentView(R.layout.ykn_dialog);
		mDialog.setOnCancelListener(new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
			}
		});
//		mProgressDialog = new ProgressDialog(mActivity);
		updateLayout();
		mHolder.mRenrenAccountID.addTextChangedListener(mTextWatcher);
		mHolder.mRenrenAccountPwd.addTextChangedListener(mTextWatcher);
		mHolder.mRenrenAccountDel.setOnClickListener(mDelBtnOnClickListener);
		mHolder.mRenrenPswDel.setOnClickListener(mDelBtnOnClickListener);
	}

	/**
	 * 更新绑定视图~
	 */
	private void updateLayout() {
		if(LoginManager.getInstance().getLoginInfo().mBindInfoRenren == null ||(LoginManager.getInstance().getLoginInfo().mBindInfoRenren != null && TextUtils.isEmpty(LoginManager.getInstance().getLoginInfo().mBindInfoRenren.mBindId))){
			mHolder.mRenrenAccountID.setText("");
			mHolder.mRenrenAccountPwd.setText("");
			mHolder.mNotBindRenrenAccountLayout.setVisibility(View.VISIBLE);
			mHolder.mHasBindRenrenAccountLayout.setVisibility(View.GONE);
			mHolder.mRenrenAccountName.setText("");
			mHolder.mRenrenAccount_head.clear();
		}else{
			mHolder.mRenrenAccountName.setText(LoginManager.getInstance().getLoginInfo().mBindInfoRenren.mBindName);
			if(TextUtils.isEmpty(LoginManager.getInstance().getLoginInfo().mBindInfoRenren.mBindMediumUrl)){
				mHolder.mRenrenAccount_head.addUrl(LoginManager.getInstance().getLoginInfo().mMediumUrl);
			}else{
				mHolder.mRenrenAccount_head.addUrl(LoginManager.getInstance().getLoginInfo().mBindInfoRenren.mBindMediumUrl);
			}
			mHolder.mHasBindRenrenAccountLayout.setVisibility(View.VISIBLE);
			mHolder.mNotBindRenrenAccountLayout.setVisibility(View.GONE);
		}
	}
	
	public void initEvent(){
		mHolder.mClearBind.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mUnbindDialog.show();
			}
		});
		
		mHolder.mBind.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mBindRenrenAccountActivity.hideInputMethod();
				String account = mHolder.mRenrenAccountID.getText().toString();
				String psw = mHolder.mRenrenAccountPwd.getText().toString();
				if(!TextUtils.isEmpty(account) && !TextUtils.isEmpty(psw)){
					((TextView)mDialog.findViewById(R.id.message)).setText(mActivity.getResources().getText(R.string.f_bind_renren_dialog_binding));
//					mProgressDialog.setMessage(mActivity.getResources().getText(R.string.f_bind_renren_dialog_binding));
					mDialog.show();
					McsServiceProvider.getProvider().bindRenrenCount(account.toLowerCase(), Md5.toMD5(psw), bindRenrenResponse);
					mHolder.mBind.setEnabled(false);
				}else{
					if(TextUtils.isEmpty(account)){
						SystemUtil.toast(R.string.BindRenrenAccount_null);
					}else if(TextUtils.isEmpty(psw)){
						SystemUtil.toast(R.string.BindRenrenAccount_psw_null);
					}
				}
			}
		});
		
	}
	
	/**
	 * 绑定人人后的回调~
	 */
	private INetResponse bindRenrenResponse = new  INetReponseAdapter(){

		@Override
		public void onSuccess(INetRequest req, JsonObject data) {
			super.onSuccess(req, data);
				LoginInfo info = LoginManager.getInstance().getLoginInfo();
				LoginfoModel loginfo = new LoginfoModel();
				if(info.mBindInfoRenren == null){
					info.mBindInfoRenren = new BindInfo();
				}
				info.mBindInfoRenren.mBindId = data.getString(BIND_RENREN_BIND_ID);
				info.mBindInfoRenren.mBindType = data.getString(BIND_RENREN_TYPE);
				info.mBindInfoRenren.mBindName = data.getString(BIND_RENREN_NAME);
				info.mBindInfoRenren.mBindPage = data.getString(BIND_RENREN_PAGE);
				info.mBindInfoRenren.mBindMediumUrl = data.getString(BIND_RENREN_HEAD_URL);
				loginfo.parse(info);
				LoginManager.getInstance().setLoginfo(info);
				LoginControlCenter.getInstance().updateUserData(loginfo);
//			if(mRenrenInfoOnChangeCallback != null){
//				mRenrenInfoOnChangeCallback.onChange();
//			}
			notifyObservers();
			if(mRenrenBindUpdateContactCallback != null){
				mRenrenBindUpdateContactCallback.onBindRenren();
			}
			RenrenChatApplication.mHandler.post(new Runnable() {
				
				@Override
				public void run() {
					updateLayout();
					mDialog.dismiss();
					SystemUtil.toast(R.string.f_bind_renren_bind_success);
					mHolder.mBind.setEnabled(true);
				}});
			C_ContactsData.getInstance().loadRenRenContact(null);
			/**
			 * 不同绑定人人的调用者的处理~~
			 */
			 if(BindRenrenAccountActivity.BIND_STATUS == BindRenrenAccountActivity.BIND_CONTANT || BindRenrenAccountActivity.BIND_STATUS == BindRenrenAccountActivity.BIND_FEED){
				 mActivity.setResult(Activity.RESULT_OK );
				 mActivity.finish();
			 }
		}

		@Override
		public void onError(INetRequest req,final JsonObject data) {
			super.onError(req, data);
			RenrenChatApplication.sHandler.post(new Runnable() {
				
				@Override
				public void run() {
					mDialog.dismiss();
					SystemUtil.toast(data.getString("error_msg"));//R.string.f_bind_renren_bind_fail);
					mHolder.mBind.setEnabled(true);
				}
			});
		}
	};
	
	/**
	 * 解绑人人后的回调~
	 */
	private INetResponse unBindRenrenResponse = new  INetReponseAdapter(){

		@Override
		public void onSuccess(INetRequest req, JsonObject data) {
			super.onSuccess(req, data);
			{
				LoginInfo info = LoginManager.getInstance().getLoginInfo();
				LoginfoModel loginfo = new LoginfoModel();
				info.mBindInfoRenren.mBindId = "";
				info.mBindInfoRenren.mBindType = "";
				info.mBindInfoRenren.mBindName = "";
				info.mBindInfoRenren.mBindPage = "";
				info.mBindInfoRenren.mBindMediumUrl="";
				loginfo.parse(info);
				LoginManager.getInstance().setLoginfo(info);
				LoginControlCenter.getInstance().updateUserData(loginfo);
			}
			RenrenChatApplication.mHandler.post(new Runnable() {
				
				@Override
				public void run() {
					mDialog.dismiss();
					updateLayout();
					SystemUtil.toast(R.string.f_bind_renren_unbind_success);
					mHolder.mClearBind.setEnabled(true);
					ChatFeedAdapter.mDataList.clear();
				}});
			C_ContactsData.getInstance().deleteAllRenRenContact();
			notifyObservers();
			if(mRenrenBindUpdateContactCallback != null){
				mRenrenBindUpdateContactCallback.onBindRenren();
			}
			
		}

		@Override
		public void onError(INetRequest req, final JsonObject data) {
			super.onError(req, data);
			RenrenChatApplication.sHandler.post(new Runnable() {
				public void run() {
					mDialog.dismiss();
					SystemUtil.toast(data.getString("error_msg"));//R.string.f_bind_renren_unbind_fail);
					mHolder.mClearBind.setEnabled(true);
				}
			});
		}
	};
	
	private Dialog createUnbindDialog(){
		AlertDialog.Builder buider = new AlertDialog.Builder(mActivity);
        buider.setTitle(mResources.getText(R.string.BindRenrenAccount_dialog_title));
        buider.setPositiveButton(mResources.getText(R.string.BindRenrenAccount_dialog_confirm), new DialogInterface.OnClickListener() {
            
            @Override
            public void onClick(final DialogInterface dialog, int which) {
            	McsServiceProvider.getProvider().unBindRenrenCount(LoginManager.getInstance().getLoginInfo().mBindInfoRenren.mBindId, unBindRenrenResponse);
				dialog.dismiss();
				((TextView)mDialog.findViewById(R.id.message)).setText(mResources.getText(R.string.f_bind_renren_dialog_unbinding));
//				mProgressDialog.setMessage(mResources.getText(R.string.f_bind_renren_dialog_unbinding));
				mDialog.show();
				mHolder.mClearBind.setEnabled(false);
            }
        });
        buider.setNegativeButton(mResources.getText(R.string.BindRenrenAccount_dialog_cancel), new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
        return buider.create();
	}
	
	private static RenrenBindUpdateContactCallback mRenrenBindUpdateContactCallback;
	
	/**
	 * 绑定人人成功，更新联系人列表回调~
	 */
	public static interface RenrenBindUpdateContactCallback{
		public void onBindRenren();
	}
	public static void setRenrenBindUpdateContactCallback(RenrenBindUpdateContactCallback renrenBindUpdateContactCallback){
		mRenrenBindUpdateContactCallback = renrenBindUpdateContactCallback;
	}
	
	public static void registerObserver(BindInfoObserver observer) {
		mObserverList.add(observer);
	}

	public static void deleteObserver(BindInfoObserver observer) {
		mObserverList.remove(observer);
	}

	public void notifyObservers() {
		if(mObserverList.size() > 0){
			for (BindInfoObserver observer : mObserverList) {
				observer.update();
			}
		}
	}
	
	public TextWatcher mTextWatcher = new TextWatcher() {
		
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			if(s.length() > 0){
				if(mHolder.mRenrenAccountID.length() > 0){
					mHolder.mRenrenAccountDel.setVisibility(View.VISIBLE);
				}
				if(mHolder.mRenrenAccountPwd.length() > 0){
					mHolder.mRenrenPswDel.setVisibility(View.VISIBLE);
				}
			}else{
				if(mHolder.mRenrenAccountID.length() == 0){
					mHolder.mRenrenAccountDel.setVisibility(View.GONE);
				}
				if(mHolder.mRenrenAccountPwd.length() == 0){
					mHolder.mRenrenPswDel.setVisibility(View.GONE);
				}
			}
		}
		
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
		}
		
		@Override
		public void afterTextChanged(Editable s) {
		}
	};
	
	public OnClickListener mDelBtnOnClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.renren_account_id_del:
				mHolder.mRenrenAccountID.setText("");
				mHolder.mRenrenAccountDel.setVisibility(View.GONE);
				mHolder.mRenrenAccountPwd.setText("");
				mHolder.mRenrenPswDel.setVisibility(View.GONE);
				break;
			case R.id.renren_account_psw_del:
				mHolder.mRenrenAccountPwd.setText("");
				mHolder.mRenrenPswDel.setVisibility(View.GONE);
				break;
			default:
				break;
			}
		}
	};
}