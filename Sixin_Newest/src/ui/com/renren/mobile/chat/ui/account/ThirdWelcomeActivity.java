package com.renren.mobile.chat.ui.account;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.common.emotion.manager.EmotionManager;
import com.common.manager.LoginManager;
import com.common.statistics.LocalStatisticsManager;
import com.renren.mobile.account.LoginControlCenter;
import com.renren.mobile.chat.R;
import com.renren.mobile.chat.RenrenChatApplication;
import com.renren.mobile.chat.base.util.SystemUtil;
import com.renren.mobile.chat.crash.CrashHandler;
import com.renren.mobile.chat.ui.MainFragmentActivity;
import com.renren.mobile.chat.ui.account.LoginSixinActivity.LoginStatusListener;
import com.renren.mobile.chat.ui.contact.C_ContactsData;
import com.renren.mobile.chat.ui.contact.ContactBaseModel;
import com.renren.mobile.chat.ui.contact.IDownloadContactListener;
import com.renren.mobile.chat.ui.notification.MessageNotificationManager;
/*
 * @author kaining.yang
 */
public final class ThirdWelcomeActivity extends Activity implements IDownloadContactListener {
	
	private static final String IS_THIRD_CALLED = "is_third_called";
	
	private static final String IS_LOGINED = "is_logined";
	
	private static final String IS_LOGOUTED = "is_logouted";
	
	private static final String IS_CHAT = "is_chat";
	
	private static final String DELETE_DATA = "delete_data";
	
	private static final String USER_ID = "id";
	
	private static final String USER_ACCOUNT = "account";
	
	private static final String USER_PASSWORD = "password";
	
    /**
     * 第一次登录需要显示欢迎界面，显示欢迎界面的时间
     */
    private int waitTime = 2 * 1000;
    
    private boolean mThirdFlag;
    
    private boolean mLoginedFlag;
    
    private boolean mLogoutedFlag;
    
    private boolean mChatFlag;
    
    private boolean mDeleteDataFlag;
    
    private String mAccount;
    
    private String mPassword;
    
    private long mUserId;
    
    private TextView mLogining;
    
    private LoginStatusListener mLoginStatusListener = null; 
    
    private long mCurrentSession;
    
    private Context mContext;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	SystemUtil.logykn("官方互调启动");
    	super.onCreate(savedInstanceState);
    	// getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN); 
    	setContentView(R.layout.ykn_first_layout);
    	RenrenChatApplication.clearWelcome();
    	mContext = this;
    	
    	// TODO crash log catch
    	CrashHandler.getInstance().init(mContext);
    	
    	// RenrenChatApplication.clearStack(); // 清空历史用户栈
    	mThirdFlag = getIntent().getBooleanExtra(IS_THIRD_CALLED, false);
    	mLoginedFlag = getIntent().getBooleanExtra(IS_LOGINED, false);
    	mLogoutedFlag = getIntent().getBooleanExtra(IS_LOGOUTED, true);
    	mChatFlag = getIntent().getBooleanExtra(IS_CHAT, false);
    	mDeleteDataFlag = getIntent().getBooleanExtra(DELETE_DATA, false);
    	mAccount = getIntent().getStringExtra(USER_ACCOUNT);
    	mPassword = getIntent().getStringExtra(USER_PASSWORD);
    	mUserId = getIntent().getLongExtra(USER_ID, 0);
    	SystemUtil.logykn("mThirdFlag", mThirdFlag + "");
    	SystemUtil.logykn("mLoginedFlag", mLoginedFlag + "");
    	SystemUtil.logykn("mLogoutedFlag", mLogoutedFlag + "");
    	SystemUtil.logykn("mDeleteDataFlag", mDeleteDataFlag + "");
    	SystemUtil.logykn("mAccount", mAccount + "");
    	SystemUtil.logykn("mPassword", mPassword + "");
    	SystemUtil.logykn("mChatFlag", mChatFlag + "");
    	SystemUtil.logykn("mUserId", mUserId + "");
    	
    	// initView
    	mLogining = (TextView) findViewById(R.id.logining);
    	
    	//激活统计
    	LocalStatisticsManager.getInstance().sendActivityRequest();
    	//LocalStatisticsManager.getInstance().activityClient();
    	MessageNotificationManager.getInstance().clearSingleLoginNotification(this);
    	
    	if (!mLogoutedFlag) {
    		mLogining.setVisibility(View.VISIBLE);
    		//mLogining.setText("正在注销...");
    	}
    	new mAsyncTask().execute();
    	
    	/*try {
	    	if (mDeleteDataFlag) {
	    		//删除群组
				RoomInfosData.getInstance().delete_all_rooms();
				//删除聊天记录
				ChatDataHelper.getInstance().deleteAll();
				ChatSessionHelper.getInstance().deleteAll();
				ContactsDAO contactDao = DAOFactoryImpl.getInstance().buildDAO(ContactsDAO.class);
				contactDao.delete_All();
				C_ContactsData.getInstance().deleteAllRenRenContact();
				ContactMessageData.getInstance().delete_All();
				LoginControlCenter.getInstance().logout(ThirdWelcomeActivity.this); // 注销用户
	    	}
	    	C_ContactsData.getInstance().clear(); // 内存中的数据要清空的  因为在线联系人必须重新获取
		} catch (Exception e) {
			e.printStackTrace();
		}
    	
    	go();*/
    }
    
    class mAsyncTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			try {
		    	if (mDeleteDataFlag) {
		    		// 
		    		//SystemUtil.logykn("官方互调注销已登陆用户");
					//LoginControlCenter.getInstance().logout(ThirdWelcomeActivity.this, false); // 注销用户
		    	}
		    	if (!mLogoutedFlag) {
		    		SystemUtil.logykn("官方互调注销已登陆用户");
					LoginControlCenter.getInstance().logout(ThirdWelcomeActivity.this); // 注销用户
		    	}
		    	return null;
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				return null;
			}
		}

		@Override
		protected void onPostExecute(Void result) {
			go();
			super.onPostExecute(result);
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

	}
    
    private void go() {
    	if (!mThirdFlag) {
			// 私信自启动 
		} else if (mThirdFlag && mLoginedFlag) {
			// 官方互调 已正确登陆
			RenrenChatApplication.sHandler.postDelayed(new Runnable() {
				
				@Override
				public void run() {
					enter();
				}
			}, waitTime);
		} else if (mThirdFlag && !mLoginedFlag) {
			// 官方互调 未正确登陆
			login();
			EmotionManager.getInstance().loadEmotionPackage();
		}
    }
    
    private void enter() {
    	if (mChatFlag) {
			RenrenChatApplication.sToChatContactInChat = C_ContactsData.getInstance().getContactByRenRenID(mUserId, ThirdWelcomeActivity.this);
			
			// 若直接获取到则进入 不然等回调
			if (RenrenChatApplication.sToChatContactInChat != null) {
				SystemUtil.logykn("RenrenChatApplication.sToChatContactInChat.mUserId", String.valueOf(RenrenChatApplication.sToChatContactInChat.mUserId));
				SystemUtil.logykn("RenrenChatApplication.sToChatContactInChat.mContactName", RenrenChatApplication.sToChatContactInChat.mContactName);
				MainFragmentActivity.show(mContext, MainFragmentActivity.Tab.CONTRACT);
				ThirdWelcomeActivity.this.finish();
			}
		} else {
			MainFragmentActivity.show(mContext, MainFragmentActivity.Tab.CONTRACT);
			ThirdWelcomeActivity.this.finish();
		}
    }
    
    private void login() {
		
		mLoginStatusListener = new LoginStatusListener() {
			
			@Override
			public void onLoginSuccess(int fill_stage,long session) {
				
				SystemUtil.logykn("授权登陆成功！");		
				if(session != mCurrentSession){
					return ;
				}
				RenrenChatApplication.sIsSingleLoginError = false;
				if(TextUtils.isEmpty(LoginManager.getInstance().getLoginInfo().mLastAccount)) {
					// 从数据库读取
					if (LoginControlCenter.getInstance().loadLastLoginUserData() != null) {
						LoginManager.getInstance().getLoginInfo().mLastAccount = LoginControlCenter.getInstance().loadLastLoginUserData().mAccount;
					}
				}
				if(SystemUtil.mDebug){
					SystemUtil.errord("lastAccount="+LoginManager.getInstance().getLoginInfo().mLastAccount+"#now="+LoginManager.getInstance().getLoginInfo().mAccount);
				}
				if(!TextUtils.isEmpty(LoginManager.getInstance().getLoginInfo().mLastAccount)){
					if(!LoginManager.getInstance().getLoginInfo().mLastAccount.equals(LoginManager.getInstance().getLoginInfo().mAccount)){
						LoginControlCenter.getInstance().onLoginClear();
					}
				}
				// RenrenChatApplication.clearWelcome();
				enter();
				
			}
			
			@Override
			public void onLoginFailed(long error_code, String error_msg, long session) {
				SystemUtil.logykn("授权登陆失败！");		
				SystemUtil.logykn("error_code:" + error_code);
				SystemUtil.logykn("error_msg:" + error_msg);
				final int code = (int) error_code;
				
				RenrenChatApplication.mHandler.post(new Runnable() {
					@Override
					public void run() {
						switch (code) {		
						case 10003:
							mLogining.setVisibility(View.INVISIBLE);
							AlertDialog.Builder builder = new AlertDialog.Builder(ThirdWelcomeActivity.this);
							builder.setMessage(R.string.ykn_login_dialog_message); 
							builder.setPositiveButton(R.string.ykn_confirm,
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											LoginSixinActivity
													.show(ThirdWelcomeActivity.this,
															LoginSixinActivity.LOGIN_THIRD,
															mAccount);
											ThirdWelcomeActivity.this.finish();
										}
									});
							builder.create().show();
							break;
						case 10004:
							LoginSixinActivity
									.show(ThirdWelcomeActivity.this,
											LoginSixinActivity.LOGIN_RENREN,
											mAccount,
											mPassword);
							ThirdWelcomeActivity.this.finish();
							break;
					    default: 
					    	// ThirdWelcomeActivity.this.finish();
					    	mLogining.setVisibility(View.INVISIBLE);
							AlertDialog.Builder builderError = new AlertDialog.Builder(ThirdWelcomeActivity.this);
							builderError.setTitle(R.string.ykn_first_request_failure); 
							builderError.setPositiveButton(R.string.ykn_retry,
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											/*LoginSixinActivity
													.show(ThirdWelcomeActivity.this,
															LoginSixinActivity.LOGIN_THIRD,
															mAccount);
											ThirdWelcomeActivity.this.finish();*/
											go();
										}
									});
							builderError.setNegativeButton(R.string.ykn_cancel,
									new OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											ThirdWelcomeActivity.this.finish();
										}
									});
							builderError.create().show();
							break;
			            }	
					}
				});
			}
			
			@Override
			public void onLoginResponse() {
				//mLogining.setVisibility(View.INVISIBLE);
			}
		};
		
		SystemUtil.logykn("account:" + mAccount);
		SystemUtil.logykn("password:" + mPassword);

		mLogining.setVisibility(View.VISIBLE);
		mCurrentSession = System.currentTimeMillis();
		/**
		 * kaining.yang
		 */
		LoginControlCenter.getInstance().login(
				LoginManager.LOGIN_RENREN,
				mAccount,
				mPassword, 
				null,
				mContext, 
				mLoginStatusListener,
				mCurrentSession);
		
	}
    
	public static void show(Context context, long userId, boolean isThirdCalled, boolean isLogined, boolean isLogouted, boolean isChat, boolean deleteData, String account, String password) {
		Intent intent = new Intent(context, ThirdWelcomeActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.putExtra(USER_ID, userId);
		intent.putExtra(IS_THIRD_CALLED, isThirdCalled);
		intent.putExtra(IS_LOGINED, isLogined);
		intent.putExtra(IS_LOGOUTED, isLogouted);
		intent.putExtra(IS_CHAT, isChat);
		intent.putExtra(DELETE_DATA, deleteData);
		intent.putExtra(USER_ACCOUNT, account);
		intent.putExtra(USER_PASSWORD, password);
		context.startActivity(intent);
	}
	
	public static void show(Context context, long userId, boolean isThirdCalled, boolean isLogined, boolean isChat) {
		show(context, userId, isThirdCalled, isLogined, true, isChat, false, "", "");
	}

	@Override
	public void onSussess(ContactBaseModel model) {
		SystemUtil.logykn("获取ContactModel成功回调");
		if (model != null) {
			RenrenChatApplication.sToChatContactInChat = model;
			SystemUtil.logykn("RenrenChatApplication.sToChatContactInChat.mUserId", String.valueOf(RenrenChatApplication.sToChatContactInChat.mUserId));
			SystemUtil.logykn("RenrenChatApplication.sToChatContactInChat.mContactName", RenrenChatApplication.sToChatContactInChat.mContactName);
			MainFragmentActivity.show(mContext, MainFragmentActivity.Tab.CONTRACT);
			ThirdWelcomeActivity.this.finish();
		}
	}

	@Override
	public void onError() {
		
	}

	@Override
	public void onDowloadOver() {
		
	}

}