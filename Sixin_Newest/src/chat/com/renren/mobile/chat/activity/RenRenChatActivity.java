package com.renren.mobile.chat.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.common.emotion.emotion.ScrollingLock;
import com.common.emotion.manager.EmotionManager;
import com.common.emotion.manager.IEmotionManager.OnCoolEmotionSelectCallback;
import com.common.emotion.manager.IEmotionManager.OnEmotionSelectCallback;
import com.common.manager.LoginManager;
import com.common.manager.VoiceManager;
import com.core.util.CommonUtil;
import com.core.voice.PCMPlayerSetting;
import com.core.voice.PlayerThread.OnSwitchPlayModeListenner;
import com.renren.mobile.chat.R;
import com.renren.mobile.chat.RenrenChatApplication;
import com.renren.mobile.chat.RenrenChatApplication.OnChatStateCallback;
import com.renren.mobile.chat.actions.models.RoomBaseInfoModel.ROOM_ENABLE;
import com.renren.mobile.chat.actions.models.RoomInfoModelWarpper;
import com.renren.mobile.chat.activity.RenRenChatActivity.CanTalkable.GROUP;
import com.renren.mobile.chat.base.ChatBaseActivity;
import com.renren.mobile.chat.base.model.ChatBaseItem;
import com.renren.mobile.chat.base.model.ChatBaseItem.MESSAGE_ISGROUP;
import com.renren.mobile.chat.base.model.ChatSaveModel;
import com.renren.mobile.chat.base.model.StateMessageModel;
import com.renren.mobile.chat.base.util.SystemUtil;
import com.renren.mobile.chat.command.Command;
import com.renren.mobile.chat.command.Command_Brush;
import com.renren.mobile.chat.command.Command_Recoder;
import com.renren.mobile.chat.command.Command_SelectPhoto;
import com.renren.mobile.chat.command.Command_SendText;
import com.renren.mobile.chat.command.Command_TakePhoto;
import com.renren.mobile.chat.command.Command_Brush.NEED_RETURN_DATA;
import com.renren.mobile.chat.common.RRSharedPreferences;
import com.renren.mobile.chat.dao.ChatHistoryDAO;
import com.renren.mobile.chat.dao.DAOFactoryImpl;
import com.renren.mobile.chat.holder.ChatItemSelectHolder;
import com.renren.mobile.chat.holder.RenrenChatActivityHolder;
import com.renren.mobile.chat.model.warpper.ChatMessageWarpper;
import com.renren.mobile.chat.model.warpper.ChatMessageWarpper_FlashEmotion;
import com.renren.mobile.chat.model.warpper.ChatMessageWarpper_Null.OnFeedDeleteListener;
import com.renren.mobile.chat.newsfeed.NewsFeedWarpper;
import com.renren.mobile.chat.service.ChatService;
import com.renren.mobile.chat.ui.MainFragmentActivity;
import com.renren.mobile.chat.ui.MainFragmentActivity.Tab;
import com.renren.mobile.chat.ui.contact.C_ContactsData;
import com.renren.mobile.chat.ui.contact.C_ContactsData.NewContactsDataObserver;
import com.renren.mobile.chat.ui.contact.ContactBaseModel;
import com.renren.mobile.chat.ui.contact.ContactModel;
import com.renren.mobile.chat.ui.contact.RoomInfosData;
import com.renren.mobile.chat.ui.contact.RoomInfosData.RoomInfosDataObserver;
import com.renren.mobile.chat.ui.groupchat.D_ChatMessagesActivity;
import com.renren.mobile.chat.ui.groupchat.D_ChatMessagesActivity.CHAT_MESSAGE_COMEFROM;
import com.renren.mobile.chat.ui.groupchat.D_ChatMessagesActivity.CHAT_MESSAGE_PARAMS;
import com.renren.mobile.chat.ui.imageviewer.ImageViewActivity;
import com.renren.mobile.chat.ui.plugins.BrushPlugin;
import com.renren.mobile.chat.util.AutoSwitchMode;
import com.renren.mobile.chat.util.AutoSwitchMode.OnProximityChangeListenner;
import com.renren.mobile.chat.util.BaseChatListAdapter.OnNotifyCallback;
import com.renren.mobile.chat.util.C_ChatListAdapter;
import com.renren.mobile.chat.util.C_Syn;
import com.renren.mobile.chat.util.C_Syn.OnSynListener;
import com.renren.mobile.chat.util.ChatCallbackManager;
import com.renren.mobile.chat.util.ChatDataHelper;
import com.renren.mobile.chat.util.ChatDataHelper.OnSupportFeedListener;
import com.renren.mobile.chat.util.ChatMessageSender;
import com.renren.mobile.chat.util.ChatPaintThread;
import com.renren.mobile.chat.util.CurrentChatSetting;
import com.renren.mobile.chat.util.StateMessageSender;
import com.renren.mobile.chat.util.ToastAnimationListennerImpl;
import com.renren.mobile.chat.util.VoiceDownloadThread;
import com.renren.mobile.chat.view.BaseTitleLayout;
import com.renren.mobile.chat.view.BaseTitleLayout.FUNCTION_BUTTON_TYPE;
import com.renren.mobile.chat.view.BaseTitleState;
import com.renren.mobile.chat.view.BaseTitleStateFactory;
import com.renren.mobile.chat.view.BaseTitleStateFactory.BaseTitleStateType;
import com.renren.mobile.chat.views.ChatItemSelectPopupWindow;
import com.renren.mobile.chat.views.ItemLongClickDialogProxy;
import com.renren.mobile.chat.views.ResendDialog;
import view.bar.AbstractInputBar.OnLayoutChangeListener;
import view.bar.AbstractInputBar.OnSendListener;
import view.bar.AbstractInputBar.OnTypingListener;
import view.bar.InputBar.OnKeyBoardShowListener;
import view.bar.InputBar.OnViewShowListener;
import view.list.ListViewWarpper.OnDataChangeListener;
import view.list.ListViewWarpper.OnFingerTouchListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RenRenChatActivity extends ChatBaseActivity implements
        OnChatStateCallback,
        OnSwitchPlayModeListenner,
        OnProximityChangeListenner,
        NewContactsDataObserver,
        OnSupportFeedListener,
        RoomInfosDataObserver,
        OnSendListener,
        OnCoolEmotionSelectCallback,
        OnEmotionSelectCallback,
        OnFingerTouchListener,
        OnKeyBoardShowListener,
        OnViewShowListener,
        OnTypingListener,
        OnSynListener,
        OnFeedDeleteListener,
        OnNotifyCallback{

    private static final String TAG = "RenRenChatActivity";

    public static interface PARAM_NEED {
        public String CONTEXT_FROM       = "CONTEXT_FROM";
        public String TO_CHAT_USER_MODEL = "TO_CHAT_USER_MODEL";
        public String TO_CHAT_FEED_MODEL = "TO_CHAT_FEED_MODEL";
        public String FORWARD_FILE_PATH  = "FORWARD_FILE_PATH";
        public String IS_GET_MESSAGE     = "IS_GET_MESSAGE";
    }

    private boolean mIsGetMessage = false;

    protected boolean mIsLoadData = false;

    public              long                      mLocalUserId             = RenrenChatApplication.USER_ID;
    /* 状态消息消失时间 */
    public static final long                      STATUS_DISMISS_TIME      = 15000L;
    /* 从联系人页面或者会话页面传入的模型 */
    public              CanTalkable               mToChatUser              = null;
    /* 窗口UI持有器 */
    public              RenrenChatActivityHolder  mHolder                  = null;
    /* 弹出条目选择界面 */
    public              ChatItemSelectPopupWindow mChatItemSelectPopWindow = null;
    /* title */
    public              BaseTitleLayout           mBaseTitleLayout         = null;
    /* 绘制线程 */
    ChatPaintThread mPaintThread = null;
    public              C_ChatListAdapter   mChatListAdapter     = new C_ChatListAdapter(this);
    /* 回调管理器,该Activity的部分回调都在这个类里面 */
    private             ChatCallbackManager mCallbackManager     = new ChatCallbackManager(this);
    /* 命令 */
    public              Command             mTakePhoto_Command   = new Command_TakePhoto(this);
    public              Command_Recoder     mRecord_Command      = new Command_Recoder(this);
    public              Command_SelectPhoto mLocalSelect_Command = new Command_SelectPhoto(this);
    public              Command             mSendText_Command    = new Command_SendText(this);
    public              Command_Brush       mBrush_Command       = new Command_Brush(this);
    public static final BaseTitleState      RECORDING_STATE      =
            BaseTitleStateFactory.getBaseTitleState(BaseTitleStateType.TYPE_AUDIO_STATE);
    public static final BaseTitleState      TYPING_STATE         =
            BaseTitleStateFactory.getBaseTitleState(BaseTitleStateType.TYPE_TYPING_STATE);


    /* 在线状态的旧状态 */
    public BaseTitleState mOldState = null;
    /* 设置项 */
    RRSharedPreferences mSetting = null;
    /* 长按条目弹出对话框代理 */
    public  ItemLongClickDialogProxy mLongClickDialogProxy = null;
    /* 重发弹出窗口 */
    public  ResendDialog             mResendDialog         = null;
    /* 播放模式装换器 */
    public  AutoSwitchMode           mSwitchMode           = null;
    /*开启扬声器开关*/
    private boolean                  mIsOpenReceiver       = false;
    /*新鲜事消息*/
    public  NewsFeedWarpper          mNewsfeedMessage      = null;
    /*转发图片路径*/
    public String mForwardFilePath;
    public GROUP mGroup = null;

    public List<ChatMessageWarpper> mLoadFromHistoryData;

    
    ToastAnimationListennerImpl mToastImpl = new ToastAnimationListennerImpl();
    boolean mIsSuccessCreate = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            ChatSaveModel saveModel = (ChatSaveModel) savedInstanceState.get("SAVE");
            
            this.mLocalUserId = (int) saveModel.mLocalUserId;
            if(saveModel.mIsGroup==MESSAGE_ISGROUP.IS_GROUP){
            	RoomInfoModelWarpper model= RoomInfosData.getInstance().getRoomInfo(saveModel.mToUserId);
            	if(model!=null){
            		DataPool.obtain().put(PARAM_NEED.TO_CHAT_USER_MODEL, model);
            		mIsSuccessCreate = true;
            	}else{
            		mIsSuccessCreate = false;
            	}
            }else{
            	ContactBaseModel model= C_ContactsData.getContact(saveModel.mToUserId, saveModel.mDomain);
            	if(model!=null){
            		mIsSuccessCreate = true;
            		DataPool.obtain().put(PARAM_NEED.TO_CHAT_USER_MODEL, model);
            	}else{
            		mIsSuccessCreate = false;
            	}
            }
          	ChatService.start();
        }
        super.onCreate(null);
        if(!mIsSuccessCreate){
        	this.finish();
        	return ;
        }
        
        RenrenChatApplication.pushStack(this);
        ChatDataHelper.getInstance().registorOnSupportFeedListener(this);        /*初始化loading*/
        mLoadingDialog.setMessage(RenrenChatApplication.getAppContext()
                .getResources()
                .getString(R.string.RenRenChatActivity_java_1));
        mSwitchMode = new AutoSwitchMode(this);
        mSwitchMode.setOnProximityChangeListenner(this);
        VoiceManager.getInstance().setOnSwitchPlayerListenner(this);
        this.onNewIntent(getIntent());
    }

    private void initData() {
        this.setData(getIntent());
        this.hideDialog();
        mChatListAdapter.reset();
        this.showDialog();
        if(RenrenChatApplication.sAutoMessage != null){
        	ChatDataHelper.getInstance().insertToTheDatabase(RenrenChatApplication.sAutoMessage);
        	RenrenChatApplication.sAutoMessage = null;
        }
       
    }

    protected void queryHistory(boolean isnotify) {
    	mHolder.mRoot_ChatList.disable();
    	SystemUtil.log("head", "query history ");
        ThreadPool.obtain().execute(new QueryHistoryRunnable(mToChatUser, isnotify));
    }


    public class QueryHistoryRunnable implements Runnable {
        public CanTalkable mCanTalkModel = null;
        public boolean mIsNotify;

        public QueryHistoryRunnable(CanTalkable model, boolean isNotify) {
            mCanTalkModel = model;
            this.mIsNotify = isNotify;
        }

        public void run() {
            mLoadFromHistoryData = ChatDataHelper.getInstance()
                    .queryChatHistory(mCanTalkModel.getLocalUId(),
                            mCanTalkModel.getUId(),
                            PAGE_SIZE,
                            mChatListAdapter.getEarlyId(),
                            mCanTalkModel.isGroup());
            if (mIsNotify) {
                if (mLoadFromHistoryData != null && mLoadFromHistoryData.size() > 0) {
                    addToAdapter(mLoadFromHistoryData, mIsNotify);
                } else {
                	final ChatMessageWarpper m = RenrenChatApplication.sForwardMessage;
                    if ( m != null) {
                        RenrenChatApplication.sForwardMessage = null;
                        m.mGroupId = mToChatUser.getUId();
                        m.mIsGroupMessage = mToChatUser.isGroup();
                        m.mUserName = mToChatUser.getName();
                        m.mComefrom = ChatBaseItem.MESSAGE_COMEFROM.LOCAL_TO_OUT;
                        m.mToChatUserId = mToChatUser.getUId();
                        m.mHeadUrl = mToChatUser.getHeadUrl();
                        m.mDomain = mToChatUser.getDomain();
                        m.mMessageReceiveTime = -1L;
                        m.mMessageKey = "";
                        ChatMessageSender.getInstance().sendMessageToNet(m, true);
                    }
                    if (mForwardFilePath != null) {
                        mLocalSelect_Command.sendMessage(mForwardFilePath);
                        mForwardFilePath = null;
                    }
                    if (mNewsfeedMessage != null) {
                        mChatListAdapter.setFeed(mNewsfeedMessage, RenRenChatActivity.this);
                        mHolder.mRoot_InputBar.updateHit(R.string.InputBar_Hit_2);
                    }
                    mHolder.mRoot_ChatList.enable();
                    ThreadPool.obtain().executeMainThread(new Runnable() {
                        @Override
                        public void run() {
                        	 mHolder.mRoot_ChatList.addHeader();
                        }
                    });
                }
                hideDialog();
            } else {
                CommonUtil.waitTime(100);
                ThreadPool.obtain().executeMainThread(new Runnable() {
                    @Override
                    public void run() {
                        mHolder.mRoot_ChatList.onLoadDataOver();
                    }
                });
                mHolder.mRoot_ChatList.enable();

            }
        }
    }

    public class InsertAdapterRunnable implements Runnable {
        List<ChatMessageWarpper> mList = null;
        public boolean mIsNotify;

        public InsertAdapterRunnable(List<ChatMessageWarpper> list, boolean isNotify) {
            mList = list;
            this.mIsNotify = isNotify;
            mIsLoadData = mList.size() >= PAGE_SIZE;
        }

        @Override
        public void run() {
            mChatListAdapter.addChatMessageAndNotifyFromHead(mList, false);
            mChatListAdapter.notifyDataSetInvalidated();
            if (!mIsLoadData) {
                mHolder.mRoot_ChatList.addHeader();
            }
            mHolder.mRoot_ChatList.setSelection(mList.size());
            if (RenrenChatApplication.sForwardMessage != null) {
                final ChatMessageWarpper m = RenrenChatApplication.sForwardMessage;
                RenrenChatApplication.sForwardMessage = null;
                m.mGroupId = mToChatUser.getUId();
                m.mIsGroupMessage = mToChatUser.isGroup();
                m.mUserName = mToChatUser.getName();
                m.mComefrom = ChatBaseItem.MESSAGE_COMEFROM.LOCAL_TO_OUT;
                m.mToChatUserId = mToChatUser.getUId();
                m.mHeadUrl = mToChatUser.getHeadUrl();
                m.mDomain = mToChatUser.getDomain();
                m.mMessageReceiveTime = -1L;
                m.mMessageKey = "";
                ChatMessageSender.getInstance().sendMessageToNet(m, true);
            }
            if (mForwardFilePath != null) {
                mLocalSelect_Command.sendMessage(mForwardFilePath);
                mForwardFilePath = null;
            }
            if (mNewsfeedMessage != null) {
                mChatListAdapter.setFeed(mNewsfeedMessage, RenRenChatActivity.this);
                mHolder.mRoot_InputBar.updateHit(R.string.InputBar_Hit_2);
            }
            mHolder.mRoot_ChatList.enable();
            hideDialog();
        }
    }

    protected void addToAdapter(List<ChatMessageWarpper> list, boolean isNotify) {
        ThreadPool.obtain().executeMainThread(new InsertAdapterRunnable(list, isNotify));
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (mPaintThread == null) {
            mPaintThread = new ChatPaintThread(this);
            mPaintThread.start();
        }
        CurrentChatSetting.CHAT_ID = this.mToChatUser.getUId();
        CurrentChatSetting.onChatActivityResume();
        this.mRecord_Command.mIsSend = true;
        ThreadPool.obtain().execute(new Runnable() {
            public void run() {
                ChatDataHelper.getInstance().updateMessageByUserId(mToChatUser.getUId());
            }
        });
        ScrollingLock.unlock();
        SystemUtil.log("create","onResume");
    }

    private void initViews() {        /* 界面点击置可用 */        /* 界面不可用面板置不可用 */
    	mHolder.mRoot_InputBar.updateHit(R.string.InputBar_Hit_1);
        mHolder.mRoot_ChatList.hideHeader();
        mHolder.mRoot_unAble_View.setVisibility(View.GONE);
        mHolder.mRoot_unAble_View.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        if (mChatListAdapter != null) {
            mChatListAdapter.clear();
            mChatListAdapter.setIsEnable(false);
        }
        mLongClickDialogProxy = new ItemLongClickDialogProxy(this);
        mResendDialog = new ResendDialog(this);
        mChatItemSelectPopWindow = new ChatItemSelectPopupWindow(this,
                mHolder.getRootView(),
                new ChatItemSelectHolder(this, R.layout.cdw_item_choice_popwindow));
        mCallbackManager.init();
        mChatListAdapter.setNotifyCallback(this);
        mHolder.mRoot_ChatList.setAdapter(mChatListAdapter);
        mBaseTitleLayout = new BaseTitleLayout(this, mHolder.mRoot_Title);
        mBaseTitleLayout.setTitleButtonLeftBackListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            	mHolder.mRoot_InputBar.hideKeyBoard();
            	MainFragmentActivity.show(RenRenChatActivity.this, MainFragmentActivity.Tab.SIXIN);
                finish();
            }
        });
        TextView text = mBaseTitleLayout.getTitleMiddle();
        text.setSingleLine(true);
        text.setText(mToChatUser.getName());
        mBaseTitleLayout.setTitleFunctionButtonBackground(FUNCTION_BUTTON_TYPE.ADDCONTACT_USER_INFO);
        mBaseTitleLayout.setTitleFunctionButtonListener(new OnClickListener() {
            public void onClick(View v) {
                onViewUserInfo();
            }
        });
        mBaseTitleLayout.getTitleRightFunctionButton().setEnabled(true);
        if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
        }
        mHolder.enable();
        if (!mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
        }

        mHolder.mRoot_InputBar.setOnSendListener(this);
        mHolder.mRoot_VoiceView.monitorView(mHolder.mRoot_InputBar.mView_VoiceRecord);
        mHolder.mRoot_ChatList.setOnFingerTouchListener(this);
        mHolder.mRoot_VoiceView.setMonitorListener(mRecord_Command);
        mHolder.mRoot_VoiceView.setOnPreTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View arg0, MotionEvent ev) {
                if (ev.getAction() == MotionEvent.ACTION_DOWN) {
                    mRecord_Command.onStartCommand();
                }
                return true;
            }
        });
        mHolder.mRoot_InputBar.setOnLayoutChangeListener(new OnLayoutChangeListener() {
            @Override
            public void onChangeLayout(int w, int h) {
                mHolder.mRoot_FrameLayout.measure(w, h);
                mHolder.mRoot_FrameLayout
                        .layout(0, 0, mHolder.mRoot_FrameLayout.getWidth(), mHolder.mRoot_FrameLayout.getHeight());
            }

            @Override
            public View getRootView() {
                return mHolder.mRoot_Linearlayout;
            }
        });
        mHolder.mRoot_ChatList.setOnDataChangeListener(new OnDataChangeListener() {
            public void onDataLoadStart() {
                queryHistory(false);
            }

            public void onDataChange() {
                mChatListAdapter.addChatMessageAndNotifyFromHead(mLoadFromHistoryData, false);
                mChatListAdapter.notifyDataSetInvalidated();

                mIsLoadData = mLoadFromHistoryData.size() >= PAGE_SIZE;
                if (!mIsLoadData) {
                    mHolder.mRoot_ChatList.addHeader();
                }
                mHolder.mRoot_ChatList.setSelectionFromTop(mLoadFromHistoryData.size(), 0);
                mLoadFromHistoryData.clear();

            }

            public boolean onIsLoadData() {
                return mIsLoadData;
            }
        });
        mHolder.mRoot_ChatList.setHeaderOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                showDialog();
                C_Syn.getInstance().syn(
                        (mToChatUser.isGroup() != MESSAGE_ISGROUP.IS_SINGLE),
                        mToChatUser.getUId(),
                        mToChatUser.getName(),
                        RenRenChatActivity.this);
            }
        });
        mHolder.mRoot_InputBar.setOnKeyBoardShowListener(this);
        mHolder.mRoot_InputBar.setOnTypingListener(this);
    }

    @Override
    public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
        boolean flag = false;
        SystemUtil.log("show", "0:"+keyCode);
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
            	if(hideDialog()){
            		SystemUtil.log("show", "1");
            		return true;
            	}
            	if(hideVoiceView()){
            		SystemUtil.log("show", "2");
            		return true;
            	}
                if (mHolder.mRoot_InputBar.onBack()) {
                	SystemUtil.log("show", "3");
                    return true;
                } else {
                	SystemUtil.log("show", "4");
                	MainFragmentActivity.show(this, MainFragmentActivity.Tab.SIXIN);
                    return super.onKeyDown(keyCode, event);
                }
            case KeyEvent.KEYCODE_MENU:
                break;
        }
        return flag;
    }
    
    protected boolean hideVoiceView(){
    	boolean flag = mHolder.mRoot_VoiceView.getVisibility()==View.VISIBLE;
    	if(flag){
    		mHolder.mRoot_VoiceView.dismiss();
    	}
    	return flag;
    }
    

    protected void onStart() {
        super.onStart();
        CurrentChatSetting.CHAT_ID = this.mToChatUser.getUId();
        CurrentChatSetting.onChatActivityResume();
        SystemUtil.log("create", "onStart");
    }

    /**
     * 这个方法中有类型检查
     */
    private void setData(Intent intent) {
        RenrenChatApplication.registorChatStateCallback(this);
        Log.d(TAG, "@setData");
        mToChatUser = DataPool.obtain().getObject(RenRenChatActivity.PARAM_NEED.TO_CHAT_USER_MODEL, CanTalkable.class);
        mChatListAdapter.setIsGroup(mToChatUser.isGroup());
        CurrentChatSetting.setGroupValue(mToChatUser.isGroup());
        ChatHistoryDAO dao = DAOFactoryImpl.getInstance().buildDAO(ChatHistoryDAO.class);
        mChatListAdapter.setToChatUserId(mToChatUser.getUId());
        mChatListAdapter.setDefaultHeadUrl(mToChatUser.getHeadUrl());
        mChatListAdapter.attachToDAO(dao);
        this.mGroup = mToChatUser.isGroup() == CanTalkable.GROUP.CONTACT_MODEL.Value ? CanTalkable.GROUP.CONTACT_MODEL :
                CanTalkable.GROUP.GROUP;
        this.mChatListAdapter.setDomain(mToChatUser.getDomain());
        ChatMessageSender.getInstance().setGroup(this.mGroup);
        this.mForwardFilePath = DataPool.obtain().getString(RenRenChatActivity.PARAM_NEED.FORWARD_FILE_PATH);
        this.mNewsfeedMessage =
                DataPool.obtain().getObject(RenRenChatActivity.PARAM_NEED.TO_CHAT_FEED_MODEL, NewsFeedWarpper.class);
        Log.d(TAG, "@setData obtained");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        Map<String, Object> returnData = new HashMap<String, Object>(2);
        switch (requestCode) {
            case ImageViewActivity.TAKE_PHOTO:
                returnData.put(Command_TakePhoto.NEED_RETURN_DATA.RESULT_CODE_INT, resultCode);
                mTakePhoto_Command.onEndCommand(returnData);
                break;
            case ImageViewActivity.SELECT_PHOTO:
                returnData.put(Command_SelectPhoto.NEED_RETURN_DATA.RESULT_CODE_INT, resultCode);
                mLocalSelect_Command.onEndCommand(returnData);
                break;
            case BrushPlugin.REQUEST_CODE_BRUSH_PLUGIN:
                mBrush_Command.onEndCommand(data);
                break;
            case D_ChatMessagesActivity.CHAT_MESSAGE_REQUEST_CODE.EXIT_GROUP:
                finish();
                break;
            case D_ChatMessagesActivity.CHAT_MESSAGE_REQUEST_CODE.CREATE_GROUP:
                finish();
                break;
        }
    }

    private boolean hideDialog() {
    	ThreadPool.obtain().executeMainThread(new Runnable() {
			@Override
			public void run() {
				SystemUtil.log("show", "hide dialg = "+mLoadingDialog);
				if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
		            mLoadingDialog.dismiss();
		        }
			}
		});
    	if (mLoadingDialog != null && mLoadingDialog.isShowing()){
    		return true;
    	}
        return false;
    }

    private void showDialog() {
    	ThreadPool.obtain().executeMainThread(new Runnable() {
			@Override
			public void run() {
				SystemUtil.log("show", "show dialg = "+mLoadingDialog);
				if (mLoadingDialog != null && !mLoadingDialog.isShowing()) {
					SystemUtil.log("show", "show dialog  ");
		            mLoadingDialog.show();
		        }
			}
		});
        
    }

    @Override
    protected void onPause() {
        super.onPause();
        VoiceDownloadThread.getInstance().stopToAutoPlay();
        VoiceManager.getInstance().stopAllPlay();
        VoiceManager.getInstance().stopRecord(false);
        mRecord_Command.mIsSend = false;
        mLongClickDialogProxy.dismiss();
        CurrentChatSetting.onChatActivityStop();
        mHolder.mRoot_VoiceView.hide();
        ScrollingLock.lock();
        SystemUtil.log("create", "onpause");
    }
    
    @Override
    protected void onDestroy() {
    	try {
    		if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
                mLoadingDialog.dismiss();
            }
            mChatListAdapter.distachToDAO();
            mChatListAdapter.clear();
            mChatListAdapter.resetData();
            System.gc();
            RenrenChatApplication.unregistorChatStateCallback(this);
            VoiceManager.getInstance().unregistorSwitchPlayerListenner(this);
            mPaintThread.stopPaint();
            mPaintThread = null;
            RenrenChatApplication.popStack(this);
            C_ContactsData.getInstance().unRegistorNewObserver(this);
            if (this.mGroup == GROUP.GROUP) {
                RoomInfosData.getInstance().unRegistorObserver(this);
            }
            RenrenChatApplication.sForwardMessage = null;
            EmotionManager.getInstance().clearEmotionCallBack();
		} catch (Exception e) {}
        super.onDestroy();
    }
    public void onViewUserInfo() {
        switch (mGroup) {
            case CONTACT_MODEL:
                Intent i = new Intent(RenRenChatActivity.this, D_ChatMessagesActivity.class);
                i.putExtra(D_ChatMessagesActivity.CHAT_MESSAGE_PARAMS.TO_CHAT_ID, this.mToChatUser.getUId());
                i.putExtra(D_ChatMessagesActivity.CHAT_MESSAGE_PARAMS.USER_DOMAIN, this.mToChatUser.getDomain());
                i.putExtra(D_ChatMessagesActivity.CHAT_MESSAGE_PARAMS.USER_NAME, this.mToChatUser.getName());
                i.putExtra(CHAT_MESSAGE_PARAMS.COMEFROM, CHAT_MESSAGE_COMEFROM.RENREN_CHAT_SINGLE);
                startActivityForResult(i, D_ChatMessagesActivity.CHAT_MESSAGE_REQUEST_CODE.CREATE_GROUP);

                break;
            default:
                Intent intent = new Intent(RenRenChatActivity.this, D_ChatMessagesActivity.class);
                RoomInfoModelWarpper modelWarpper = (RoomInfoModelWarpper) mToChatUser;
                intent.putExtra("groupId", modelWarpper.mRoomId);
                intent.putExtra(CHAT_MESSAGE_PARAMS.COMEFROM, CHAT_MESSAGE_COMEFROM.RENREN_CHAT_GROUP);
                startActivityForResult(intent, D_ChatMessagesActivity.CHAT_MESSAGE_REQUEST_CODE.EXIT_GROUP);
                break;
        }
    }

    @Override
    public void onNewIntent(Intent intent) {
        this.mChatListAdapter.resetData();        /*开启绘制线程,读入表情库,开启绘制线程*/
        
        this.initHolder();        /* 查询10条历史记录 */
        
        this.initData();      /*注册新鲜事发送监听*/
        this.initViews();
        this.queryHistory(true);
        ChatDataHelper.getInstance().registorOnSupportFeedListener(this);
        if (this.mGroup == GROUP.GROUP) {
            RoomInfosData.getInstance().unRegistorObserver(this);
            RoomInfosData.getInstance().registorObserver(this);
            this.warpperByGroupMessage((RoomInfoModelWarpper) mToChatUser);
        }
        //Log.d(TAG, "@onNewIntent RoomInfosData");
        mIsGetMessage = intent.getBooleanExtra(PARAM_NEED.IS_GET_MESSAGE, false);
        //Log.d(TAG, "@onNewIntent IS_GET_MESSAGE");
        PluginAdapter adapter = PluginAdapter.obtain(this);
        //Log.d(TAG, "@onNewIntent PluginAdapter");
        mHolder.mRoot_InputBar.setAdapter(adapter);
        mHolder.mRoot_InputBar.setOnViewShowListener(this);
    }

    ;


    private void warpperByGroupMessage(RoomInfoModelWarpper message) {
        if (message == null) {
            return;
        }
        mBaseTitleLayout.setTitleFunctionButtonBackground(FUNCTION_BUTTON_TYPE.ROOM_INFO);
        mBaseTitleLayout.setTitleStateVisibility(false);
        if (!TextUtils.isEmpty(message.mSubject)) {
            mBaseTitleLayout.getTitleMiddle().setText(message.mSubject + "(" + (message.mMembers.size() + 1) + ")");
        }
        if (message.mDisable == ROOM_ENABLE.DISABLE.Value) {
            mBaseTitleLayout.getTitleRightFunctionButton().setEnabled(false);
        } else {
            mBaseTitleLayout.getTitleRightFunctionButton().setEnabled(true);
        }
    }

    private void initHolder() {
        if (mHolder == null) {
            this.setContentView(R.layout.cdw_chat_main);
            mHolder = new RenrenChatActivityHolder(this, getWindow().getDecorView());
            if (mPaintThread == null) {
                mPaintThread = new ChatPaintThread(this);
                mPaintThread.start();
            }

            this.loadEmotionTab();
        } else {
            mHolder.clear();
        }
    }

    @Override
    public void onStateChange(StateMessageModel message) {
        if (message.mToId != (int) this.mToChatUser.getUId() || this.mGroup == GROUP.GROUP) {
            return;
        }
        if (message != null) {
            if (message.mStateType.equals(StateMessageModel.STATE_TYPE.SPEEKING)) {
                ThreadPool.obtain().removeCallbacks(mResetTitleRunnable);
                ThreadPool.obtain().executeMainThread(new Runnable() {
                    public void run() {
                        mBaseTitleLayout.setTitleState(RECORDING_STATE);
                    }
                });
                ThreadPool.obtain().executeMainThread(mResetTitleRunnable, STATUS_DISMISS_TIME);
                return;
            } else if (message.mStateType.equals(StateMessageModel.STATE_TYPE.TYPING)) {
                ThreadPool.obtain().removeCallbacks(mResetTitleRunnable);
                ThreadPool.obtain().executeMainThread(new Runnable() {
                    public void run() {
                        mBaseTitleLayout.setTitleState(TYPING_STATE);
                    }
                });
                ThreadPool.obtain().executeMainThread(mResetTitleRunnable, STATUS_DISMISS_TIME);
                return;
            } else {
                ThreadPool.obtain().executeMainThread(mResetTitleRunnable);
            }
        }
    }

    private Runnable mResetTitleRunnable = new Runnable() {
        public void run() {
            mBaseTitleLayout.setTitleState(mOldState);
            mBaseTitleLayout.setTitleMiddle(mToChatUser.getName());
        }
    };


    @Override
    public void onOpen() {
        if (!mIsOpenReceiver) {
            mSwitchMode.registorSensor();
        }
        mHandler.post(new Runnable() {
            public void run() {
                keepScreenOn();
            }
        });
    }

    @Override
    public void onClose() {
        mHandler.post(new Runnable() {
            public void run() {
                stopKeepScreenOn();
            }
        });
        if (!mIsOpenReceiver) {
            mSwitchMode.unregisterSensor();
            this.initVoiceSetting();
        }
    }

    private void initVoiceSetting() {
        mHandler.post(new Runnable() {
            public void run() {
                mHolder.mRoot_unAble_View.setVisibility(View.GONE);
            }
        });
        if (mIsOpenReceiver) {
            PCMPlayerSetting.switchStreamType(AudioManager.STREAM_VOICE_CALL);
            this.setVolumeControlStream(AudioManager.STREAM_VOICE_CALL);
        } else {
            PCMPlayerSetting.switchStreamType(AudioManager.STREAM_MUSIC);
            this.setVolumeControlStream(AudioManager.STREAM_MUSIC);
        }
    }

    @Override
    public void onCloseEar() {
        ThreadPool.obtain().executeMainThread(new Runnable() {
            public void run() {
                mEnable = false;
                mHolder.mRoot_unAble_View.setVisibility(View.VISIBLE);
                mHolder.mSwitchTextView.setText(R.string.cdw_chat_main_switch_speaker_layout_2);
                mToastImpl.toast(mHolder.mSwitchToast);
            }
        });
    }

    private boolean mEnable = true;

    @Override
    public void onOverEar() {
        if (!mEnable) {
            ThreadPool.obtain().executeMainThread(new Runnable() {
                public void run() {
                    mEnable = true;
                    mHolder.mRoot_unAble_View.setVisibility(View.GONE);
                    mHolder.mSwitchTextView.setText(R.string.cdw_chat_main_switch_speaker_layout_1);
                    mToastImpl.toast(mHolder.mSwitchToast);
                }
            });
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("SAVE", new ChatSaveModel(getIntent()));
    }

    @Override
    public boolean onHasFeed() {
        return this.mNewsfeedMessage != null;
    }

    @Override
    public NewsFeedWarpper onGetFeedMessage() {
        return this.mNewsfeedMessage;
    }

    @Override
    public void onClearFeedMessage() {
        ThreadPool.obtain().executeMainThread(new Runnable() {
            public void run() {
                mNewsfeedMessage = null;
                mChatListAdapter.setFeed(null, null);
                mHolder.mRoot_InputBar.updateHit(R.string.InputBar_Hit_1);
                mChatListAdapter.notifyDataSetChanged();
            }
        });

    }

    /**
     * @param 房间获取状态
     * @value {@link com.renren.mobile.chat.ui.contact.RoomInfosData.RoomInfosDataObserver#DATA_STATE_ERROR}
     * {@link com.renren.mobile.chat.ui.contact.RoomInfosData.RoomInfosDataObserver#DATA_STATE_OK}
     */
    @Override
    public void notifyRoomInfoDataUpdate(byte data_state, long roomId) {
        if (this.mGroup == GROUP.GROUP && data_state == RoomInfosDataObserver.DATA_STATE_OK) {
            RoomInfoModelWarpper newRoomInfo = RoomInfosData.getInstance().getRoomInfo(mToChatUser.getUId());
            if (newRoomInfo != null) {
                mToChatUser = newRoomInfo;
                RenrenChatApplication.mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        warpperByGroupMessage((RoomInfoModelWarpper) mToChatUser);
                    }
                });
            }
        }
    }

    @Override
    public void notifyDataUpdate(byte state, byte type) {
//		int state = C_ContactsData.getInstance().getContactOnlineStatusFromLocal(this.mToChatUser.getUId());
//		mOldState = BaseTitleStateFactory.getBaseTitleState(state);
//		RenrenChatApplication.mHandler.post(new Runnable() {
//			public void run() {
//				mBaseTitleLayout.setTitleState(mOldState);
//			}
//		});
    }


    //--------------------------------------------------------------------------------------------
    //--------------------------------------------------------------------------------------------
    //--------------------------------------------------------------------------------------------
    //--------------------------------------------------------------------------------------------


    @Override
    public void onSend(String message) {
        mSendText_Command.onStartCommand();
    }

    /*emotion*/
    @Override
    public void onCoolEmotionSelect(String emotion) {
        ChatMessageWarpper_FlashEmotion flashMessage = new ChatMessageWarpper_FlashEmotion();
        flashMessage.mMessageContent = emotion;
        flashMessage.parseUserInfo(mToChatUser);
        flashMessage.mComefrom = ChatBaseItem.MESSAGE_COMEFROM.LOCAL_TO_OUT;
        flashMessage.mLocalUserId = LoginManager.getInstance().getLoginInfo().mUserId;
        ChatMessageSender.getInstance().sendMessageToNet(flashMessage, true);
    }

    public static final int EMOTIONEDITTEXT_LENGTH = 240;

    @Override
    public void onEmotionSelect(String emotion) {
        int selectNum = mHolder.mRoot_InputBar.mView_TextEdit
                .getSelectionEnd();
        int num = selectNum + emotion.length();
        if (num <= EMOTIONEDITTEXT_LENGTH) {
            mHolder.mRoot_InputBar.mView_TextEdit.insertEmotion(emotion);
            mHolder.mRoot_InputBar.mView_TextEdit.setSelection(selectNum
                    + emotion.length());
        } else {
            CommonUtil.toast(R.string.cdw_chat_main_input_text_length);
        }
    }

    @Override
    public void mDelBtnClick() {
        mHolder.mRoot_InputBar.mView_TextEdit.delLastCharOrEmotion();
    }

    private void loadEmotionTab() {
        EmotionManager.getInstance().loadEmotionView(
                mHolder.mRoot_InputBar.mView_EmotionsGroup, this);
        EmotionManager.getInstance().registerCoolEmotionSelectCallBack(this);
        EmotionManager.getInstance().registerNormalEmotionSelectCallBack(this);
    }

    public static interface CanTalkable extends java.io.Serializable {
        public long getUId();

        public long getLocalUId();

        public String getName();

        public int isGroup();

        public String getHeadUrl();

        public String getDomain();

        public static enum GROUP {
            CONTACT_MODEL(MESSAGE_ISGROUP.IS_SINGLE),
            GROUP(MESSAGE_ISGROUP.IS_GROUP),;
            public int Value;

            GROUP(int value) {
                this.Value = value;
            }
        }
    }

    public static void show(Context context, CanTalkable contactModel) {
        DataPool.obtain().clear();
        Intent intent = new Intent(context, RenRenChatActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        DataPool.obtain().put(RenRenChatActivity.PARAM_NEED.CONTEXT_FROM, context.getClass().getSimpleName());
        DataPool.obtain().put(RenRenChatActivity.PARAM_NEED.TO_CHAT_USER_MODEL, contactModel);
        context.startActivity(intent);
    }
    
    /** 
     * add by xiangchao.fan 
     * @param autoMessage 私信小秘书自动语言
     * */
    public static void show(Context context, CanTalkable contactModel, ChatMessageWarpper autoMessage) {
        DataPool.obtain().clear();
        Intent intent = new Intent(context, RenRenChatActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        DataPool.obtain().put(RenRenChatActivity.PARAM_NEED.CONTEXT_FROM, context.getClass().getSimpleName());
        DataPool.obtain().put(RenRenChatActivity.PARAM_NEED.TO_CHAT_USER_MODEL, contactModel);
        
        RenrenChatApplication.sAutoMessage = autoMessage;
        
        context.startActivity(intent);
    }


    public static void show(Context context, CanTalkable contactModel, NewsFeedWarpper feedModel) {
        DataPool.obtain().clear();
        Intent intent = new Intent(context, RenRenChatActivity.class);
        DataPool.obtain().put(RenRenChatActivity.PARAM_NEED.CONTEXT_FROM, context.getClass().getSimpleName());
        DataPool.obtain().put(RenRenChatActivity.PARAM_NEED.TO_CHAT_USER_MODEL, contactModel);
        if (feedModel != null) {
            DataPool.obtain().put(RenRenChatActivity.PARAM_NEED.TO_CHAT_FEED_MODEL, feedModel);
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static void show(Context context,
                            CanTalkable contactModel,
                            NewsFeedWarpper feedModel,
                            String imagePath,
                            boolean isShowMessage,
                            ChatMessageWarpper m) {
        DataPool.obtain().clear();
        RenrenChatApplication.sForwardMessage = m;
        show(context, contactModel, feedModel, imagePath, isShowMessage);
    }

    public static void show(Context context,
                            CanTalkable contactModel,
                            NewsFeedWarpper feedModel,
                            String imagePath,
                            boolean isShowMessage) {
        DataPool.obtain().clear();
        Intent intent = new Intent(context, RenRenChatActivity.class);
        DataPool.obtain().put(RenRenChatActivity.PARAM_NEED.CONTEXT_FROM, context.getClass().getSimpleName());
        DataPool.obtain().put(RenRenChatActivity.PARAM_NEED.TO_CHAT_USER_MODEL, contactModel);
        if (feedModel != null) {
            DataPool.obtain().put(RenRenChatActivity.PARAM_NEED.TO_CHAT_FEED_MODEL, feedModel);
        }
        if (imagePath != null && imagePath.trim().length() > 0) {
            DataPool.obtain().put(RenRenChatActivity.PARAM_NEED.FORWARD_FILE_PATH, imagePath);
        }
        DataPool.obtain().put("indexFeed", 1);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(PARAM_NEED.IS_GET_MESSAGE, isShowMessage);
        if (!isShowMessage) {
            RenrenChatApplication.sForwardMessage = null;
        }
        context.startActivity(intent);
    }

    public static void show(Context context, CanTalkable contactModel, NewsFeedWarpper feedModel, String imagePath) {
        DataPool.obtain().clear();
        show(context, contactModel, feedModel, imagePath, false);
    }

    @Override
    public void onFingerTouch() {
        mHolder.mRoot_InputBar.onBack();
    }

    @Override
    public void onKeyBoardShow() {
        mHolder.mRoot_ChatList.setSelection(mChatListAdapter.getCount() - 1);
    }

    @Override
    public void onViewShow() {
//		mHolder.mRoot_ChatList.setSelection(mChatListAdapter.getCount()-1);
        ThreadPool.obtain().executeMainThread(new Runnable() {
            public void run() {
                onKeyBoardShow();
            }
        });

    }

    @Override
    public void onTyping() {
        // TODO Auto-generated method stub
        if (this.mToChatUser.isGroup() != CanTalkable.GROUP.GROUP.Value) {
            StateMessageSender.getInstance().send(
                    this.mToChatUser.getName(),
                    this.mToChatUser.getLocalUId(),
                    this.mToChatUser.getUId(),
                    this.mToChatUser.getDomain(),
                    StateMessageModel.STATE_TYPE.TYPING);
        }
    }

    @Override
    public void onTypingCancel() {
        // TODO Auto-generated method stub
        if (this.mToChatUser.isGroup() != CanTalkable.GROUP.GROUP.Value) {
            StateMessageSender.getInstance().send(
                    this.mToChatUser.getName(),
                    this.mToChatUser.getLocalUId(),
                    this.mToChatUser.getUId(),
                    this.mToChatUser.getDomain(),
                    StateMessageModel.STATE_TYPE.CANCELED);
        }
    }


    @Override
    public byte getType() {
        return NewContactsDataObserver.TYPE_SIXIN;
    }

    @Override
    public void onBeginInsert(long uid, int isGroup) {
    	SystemUtil.log("syn", "ondelete");
        if (uid == mToChatUser.getUId()) {
            mChatListAdapter.resetData();
        }
        ChatDataHelper.getInstance().deleteChatMessageByGroupId(mToChatUser.getUId());
      
    }

    @Override
    public void onSynOver(long uid, int isGroup, boolean isSuccess,long time,int count) {
        hideDialog();
        this.mChatListAdapter.setIsSyning(false);
    }

    @Override
    public void onDeleteFeed() {
        this.onClearFeedMessage();
    }

    @Override
    public void onSynStart() {
        this.mChatListAdapter.setIsSyning(true);
    }

	@Override
	public void onNotify(int count) {
		mHolder.mRoot_ChatList.setSelection(count);
	}

	@Override
	public void onAllDataDelete() {
		ThreadPool.obtain().executeMainThread(new Runnable() {
			public void run() {
				mHolder.mRoot_ChatList.addHeader();
			}
		});
		mIsLoadData = false;
	}
}
