package com.renren.mobile.chat.ui.chatsession;

import java.util.LinkedList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.RemoteException;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.common.binder.LocalBinderPool;
import com.common.messagecenter.base.IGetConnectionState;
import com.common.messagecenter.base.Utils;
import com.common.statistics.BackgroundUtils;
import com.common.statistics.Htf;
import com.common.utils.Config;
import com.core.util.CommonUtil;
import com.renren.mobile.chat.R;
import com.renren.mobile.chat.RenrenChatApplication;
import com.renren.mobile.chat.actions.message.OfflineMessage;
import com.renren.mobile.chat.dao.ChatHistoryDAO;
import com.renren.mobile.chat.dao.DAOFactoryImpl;
import com.renren.mobile.chat.receiver.SingleLoginReceiver;
import com.renren.mobile.chat.ui.BaseScreen;
import com.renren.mobile.chat.ui.groupchat.D_SelectGroupChatContactActivity;
import com.renren.mobile.chat.view.BaseTitleLayout;

/**
 * 会话列表Screen
 * 
 * @author tian.wang 2011-11-25
 * */
public class ChatSessionScreen extends BaseScreen implements IGetConnectionState{
	private ListView mSessionListView;
	private ChatSessionAdapter mAdapter;
	private ProgressBar progressBar;
//	private TextView textView,textView1;
//	private ImageView imageView;
	private Context context;
	private View view;
	final public static int REFRESH_LIST = 0;
	final public static int REFRESH_TEXT_VIEW = 1;
	final public static int RESET_TITLE = 2;
	/*刷新文案 */
	final public static int REFRESH_TEXT = 3;
	/* 接收超时时间 */
	View midTitle;
	LinearLayout mChatSessionLinearLayout;
	TextView mChatSessionLayoutText;
	TextView mChatSessionLoadingText;
	
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case REFRESH_LIST:
				progressBar.setVisibility(View.GONE);
				mAdapter.removeAll();
				mAdapter.addLinkList(ChatSessionManager.getInstance().getSessionList());
				LinkedList<Object> linkedList = ChatSessionManager.getInstance().getSessionList();
				if(linkedList.size()==0){
					view.setVisibility(View.VISIBLE);
				}else{
					view.setVisibility(View.GONE);
				}
				mAdapter.notifyDataSetChanged();
				break;
			case REFRESH_TEXT_VIEW:
				view.setVisibility(View.VISIBLE);
				break;
			case RESET_TITLE:
				resetTitle();
				break;
				
			case REFRESH_TEXT:
				view.setVisibility(View.VISIBLE);
				break;
			}
			
		};

	};

	public ChatSessionScreen(Activity activity) {
		super(activity);
		OfflineMessage.connectionState = this;
		SingleLoginReceiver.sGetConnectionState.add(true, this);
		context = activity;
		initView();
//		startRefreshStatusTimer();
		try {
			 boolean sign = LocalBinderPool.getInstance().isContainBinder() && LocalBinderPool.getInstance().obtainBinder().isRecvOfflineMessage();
			 Utils.l("sign = " + sign + ";\nisContainBinder=" +  LocalBinderPool.getInstance().isContainBinder());
			 if(sign){//如果已经接收完毕
				 Log.v("aab", "sign == true ");
				 this.getTitle().setTitleMiddle(context.getResources().getString(R.string.chat_session_list));
			 }else{
				 setTitleProgress(context.getResources().getString(R.string.ChatSessionScreen_java_1));//连接中
//				startRefreshStatusTimer();//开启定时器
			 }
		} catch (RemoteException e) {}
		
	}

	private void setTitleProgress(String text) {
		LinearLayout midTitleLayout = this.getTitle().getMidTitleLayout();
		midTitleLayout.removeAllViews();
		midTitle = LayoutInflater.from(context).inflate(R.layout.chat_session_title, null);
		mChatSessionLinearLayout = (LinearLayout)midTitle.findViewById(R.id.chat_session_linearLayout);
		mChatSessionLayoutText = (TextView)midTitle.findViewById(R.id.chat_session_layout_text);
		mChatSessionLoadingText = (TextView)midTitle.findViewById(R.id.chat_session_loading_text);
		mChatSessionLoadingText.setText(text);
		midTitleLayout.addView(midTitle);
	}
	
	private void resetTitle() {
		if(mChatSessionLinearLayout != null){
			mChatSessionLinearLayout.setVisibility(View.GONE);
		}
		if(mChatSessionLayoutText != null){
			mChatSessionLayoutText.setVisibility(view.VISIBLE);
		}
		
	}
	

	private Runnable runnable = new Runnable() {
		public void run() {
			handler.sendEmptyMessage(RESET_TITLE);
		}
	};

	/**
	 * 开启刷新在线列表定时器
	 * **/
	private void startRefreshStatusTimer() {
		handler.postDelayed(runnable, Config.OFFLINE_MESSAGE_TIMEOUT);// 开始Timer
	}

	/**
	 * 关闭刷新在线列表定时器
	 * **/
	private void stopRefreshStatusTimer() {
		handler.removeCallbacks(runnable);
	}
	
	public void refresh(){
		ChatSessionManager.getInstance().againLoadSession();
		handler.sendEmptyMessage(REFRESH_LIST);
	}

	private void initView() {
		mSessionListView = new ListView(mActivity);
		mSessionListView.setItemsCanFocus(true);
		mSessionListView.setAddStatesFromChildren(true);
		mSessionListView.setFocusableInTouchMode(true);
		mSessionListView.setVerticalFadingEdgeEnabled(false);
		mSessionListView.setCacheColorHint(0);
		mSessionListView.setDivider(null);
		mSessionListView.setScrollingCacheEnabled(false);
		mSessionListView.setDrawingCacheEnabled(false);
		mSessionListView.setAlwaysDrawnWithCacheEnabled(false);
		mSessionListView.setWillNotCacheDrawing(true);
		mSessionListView.setScrollbarFadingEnabled(true);
//		mSessionListView.setDivider(mActivity.getResources().getDrawable(R.drawable.listview_item_divider));
//		this.getTitle().setTitleMiddle(context.getResources().getString(R.string.chat_session_list));
		
		LayoutInflater mInflater = LayoutInflater.from(mActivity);
		view = mInflater.inflate(R.layout.chat_no_data, null);

		FrameLayout frameLayout = new FrameLayout(mActivity);

		frameLayout.addView(mSessionListView, new FrameLayout.LayoutParams(android.widget.FrameLayout.LayoutParams.FILL_PARENT, android.widget.FrameLayout.LayoutParams.FILL_PARENT));

		progressBar = new ProgressBar(mActivity);

		FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
		FrameLayout.LayoutParams layoutParams2 = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
		layoutParams.gravity = Gravity.CENTER;
		layoutParams2.gravity = Gravity.CENTER;
		int bottomMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, context.getResources().getDisplayMetrics());
		int leftMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 44, context.getResources().getDisplayMetrics());
		layoutParams2.bottomMargin = bottomMargin;
		layoutParams2.leftMargin = leftMargin;
		layoutParams2.rightMargin = leftMargin;
	
		frameLayout.addView(progressBar, layoutParams);
		frameLayout.addView(view,layoutParams2);
		this.setContent(frameLayout);
		/**
		 * 此view主要是为了使得listview可以拖动到底部tab以上
		 * */
		ChatHistoryDAO dao = DAOFactoryImpl.getInstance().buildDAO(ChatHistoryDAO.class);
		
		mAdapter = new ChatSessionAdapter(null, null, null, this.mActivity, mSessionListView,handler);
		ChatSessionManager.getInstance().setAdapterAndHandler(mAdapter,handler);//清空缓存时使用
		mAdapter.attachToDAO(dao);//把适配器注册为数据库的观察者
		mSessionListView.setAdapter(mAdapter);
		BaseTitleLayout title = getTitle();
		title.setTitleFunctionButtonBackground(BaseTitleLayout.FUNCTION_BUTTON_TYPE.MULTICHAT);
//		title.setTitleBackButtonVisibility(false);
		title.setTitleFunctionButtonListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(mActivity, D_SelectGroupChatContactActivity.class);
				mActivity.startActivity(i);
				
//				MessageNotificationManager.getInstance().sendSingleLoginNotification(context, "您的私信帐号已于14：36在其他地方登陆，请确认帐号安全");
			}
		});
	}

	@Override
	public void onShow() {
		handler.sendEmptyMessage(REFRESH_LIST);
	}

	public ChatSessionAdapter getAdapter() {
		return mAdapter;
	}
	
	@Override
	public void clearCache() {
		
		if(mAdapter != null){
			mAdapter.dittachDAO();//解除对数据库变化的监听
//			mAdapter.unRegistorObserver();
		}
		ChatSessionManager.getInstance().clearAll();
	}

	@Override
	public void onConnectSuccess() {
		 RenrenChatApplication.sHandler.post(new Runnable() {
			
			@Override
			public void run() {
				startRefreshStatusTimer();
				setTitleProgress(context.getResources().getString(R.string.ChatSessionScreen_java_2));//收取中
			}
		});
	}

	@Override
	public void onConnectFailed() {
		 RenrenChatApplication.sHandler.post(new Runnable() {
				
				@Override
				public void run() {
					setTitleProgress(context.getResources().getString(R.string.ChatSessionScreen_java_1));//连接中
				}
			});
	}

	@Override
	public void onBeginReonnect() {
		 RenrenChatApplication.sHandler.post(new Runnable() {
				
				@Override
				public void run() {
					setTitleProgress(context.getResources().getString(R.string.ChatSessionScreen_java_1));//连接中
				}
			});
	}

	@Override
	public void onEndReonnect() {
	}

	@Override
	public void onRecvOffLineMessage() {
		 RenrenChatApplication.sHandler.post(new Runnable() {
				
				@Override
				public void run() {
					resetTitle();
				}
			});
	}
}
