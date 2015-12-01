package com.renren.mobile.chat.ui.groupchat;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.common.manager.LoginManager;
import com.common.network.AbstractNotSynRequest;
import com.common.network.AbstractNotSynRequest.OnDataCallback;
import com.common.network.DomainUrl;
import com.common.network.NULL;
import com.common.statistics.BackgroundUtils;
import com.common.statistics.Htf;
import com.common.utils.Methods;
import com.core.util.CommonUtil;
import com.core.util.ViewMapUtil;
import com.core.util.ViewMapping;
import com.renren.mobile.chat.R;
import com.renren.mobile.chat.RenrenChatApplication;
import com.renren.mobile.chat.actions.models.RoomInfoModelWarpper;
import com.renren.mobile.chat.actions.models.SynHistoryRecordModel;
import com.renren.mobile.chat.actions.requests.RequestConstructorProxy;
import com.renren.mobile.chat.activity.RenRenChatActivity;
import com.renren.mobile.chat.activity.ThreadPool;
import com.renren.mobile.chat.base.model.ChatBaseItem.MESSAGE_ISGROUP;
import com.renren.mobile.chat.base.util.SystemUtil;
import com.renren.mobile.chat.base.views.NotSynImageView;
import com.renren.mobile.chat.common.RRSharedPreferences;
import com.renren.mobile.chat.dao.DAOFactoryImpl;
import com.renren.mobile.chat.dao.SynHistoryRecordDAO;
import com.renren.mobile.chat.ui.BaseActivity;
import com.renren.mobile.chat.ui.MainFragmentActivity;
import com.renren.mobile.chat.ui.chatsession.ChatSessionHelper;
import com.renren.mobile.chat.ui.contact.C_ContactsData;
import com.renren.mobile.chat.ui.contact.C_ContactsData.NewContactsDataObserver;
import com.renren.mobile.chat.ui.contact.ContactBaseModel;
import com.renren.mobile.chat.ui.contact.ContactModel;
import com.renren.mobile.chat.ui.contact.ContactModelFactory;
import com.renren.mobile.chat.ui.contact.RoomInfosData;
import com.renren.mobile.chat.ui.contact.RoomInfosData.RoomInfosDataObserver;
import com.renren.mobile.chat.ui.contact.UserInfoActivity;
import com.renren.mobile.chat.ui.contact.mutichat.MultiChatNameSettingActivity;
import com.renren.mobile.chat.ui.contact.utils.ContactResrouceUtils;
import com.renren.mobile.chat.ui.groupchat.D_SelectGroupChatContactActivity.PARAMS;
import com.renren.mobile.chat.ui.groupchat.D_SelectGroupChatContactActivity.REQUEST_CODE;
import com.renren.mobile.chat.ui.notification.MessageNotificationManager;
import com.renren.mobile.chat.util.C_Syn;
import com.renren.mobile.chat.util.C_Syn.OnSynListener;
import com.renren.mobile.chat.util.ChatDataHelper;
import com.renren.mobile.chat.util.ChatMessageSender;
import com.renren.mobile.chat.util.ClearServerHistoryUitl;
import com.renren.mobile.chat.util.SynHistoryUtil;
import com.renren.mobile.chat.view.BaseTitleLayout;

/**
 * 
 * @author rubin.dong@renren-inc.com
 * @date 
 * 聊天详情界面
 *
 */


public class D_ChatMessagesActivity extends BaseActivity implements RoomInfosDataObserver, OnSynListener, ClearServerHistoryUitl.HistoryDeleteObserver,NewContactsDataObserver {
    private boolean hasAddContactView = false;
    private boolean hasDelContactView = false;
    /**
     * 用来记录这个聊天信息上一次同步服务器聊天记录的时间
     */
    private long mSynLastTime;
    /**
     * 监控手势操作，用来判断当编辑联系人出发以后，取消这个操作的功能
     */
    private GestureDetector mGestureDetector = new GestureDetector(new Guestliston());
    /**
     * 判断是否正在进行同步聊天记录的标志位
     */
    private boolean isSynChatMessageNow = false;
    private SynHistoryRecordDAO synHistoryRecordDAO;
    /**
     * 判断是否正在进行清空聊天记录的标志位
     */
    private boolean isClearChatMessageNow = false;
    private ClearServerHistoryUitl clearServerHistoryUitl;
	private int mGroupNumber = 40; 
	private GroupMessageAdapter mAdapter;
	private LayoutInflater mInflater;
	private boolean mSign = false;
	private Context context;
	public List<ContactModel> mMembers = new ArrayList<ContactModel>();
	private BaseTitleLayout mBaseTitle;
	boolean mIsOwner;
	long mGroupId;
	long mToChatId;
    private String mDomain;
    private String mUserName;
	RoomInfoModelWarpper roomInfo = null;
	int mComeFrom;
    /**
     * 用于读取该activity相关联的layout布局文件里面的元素
     */
    D_ChatMessageScreenHolder mHolder = new D_ChatMessageScreenHolder();
	private ProgressDialog mDialog;
    /**
     * 用来保存文件的
     */
    RRSharedPreferences mRRSP;

//    @Override
//    public void historyUpdate(boolean isSuccess, final long toChatUserId, final int count, final long updateTime) {
//        long toId;
//        if(mComeFrom == CHAT_MESSAGE_COMEFROM.RENREN_CHAT_GROUP) {
//            toId = mGroupId;
//        } else {
//            toId = mToChatId;
//        }
//        if(toId == toChatUserId) {
//
//            if(isSuccess) {
//                ThreadPool.obtain().executeMainThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        if(mDialog != null) {
//                            mDialog.dismiss();
//                        }
//                        Log.v("@@@", "同步成功！！！ and isSynChatMessageNow is：" + isSynChatMessageNow);
//                        if(count != 0) {
//                            Log.v("@@@", "同步成功 并且有数据！！！");
//                            mHolder.mSynRecordInfo.setText("上次获取时间" + getTimeFromLongToString(updateTime));
//                            CommonUtil.toast(RenrenChatApplication.getAppResources().getString(R.string.ChatMessagesActivity_java_22) + count + RenrenChatApplication.mContext.getResources().getString(R.string.ChatMessagesActivity_java_23));
//                        } else if(isSynChatMessageNow){
//                            Log.v("@@@", "同步成功 没有数据！！！");
//                            CommonUtil.toast(RenrenChatApplication.getAppResources().getString(R.string.chatmessages_layout_20));
//                        }
//                        if(isSynChatMessageNow) {
//                            isSynChatMessageNow = false;
//                        }
//                    }
//                });
//            } else if(isSynChatMessageNow){
//            	 ThreadPool.obtain().executeMainThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        if(mDialog != null) {
//                            mDialog.dismiss();
//                        }
//                        CommonUtil.toast(RenrenChatApplication.getAppResources().getString(R.string.ChatMessagesActivity_java_24));
//                    }
//                });
//                if(isSynChatMessageNow) {
//                    isSynChatMessageNow = false;
//                }
//            }
//        }
//    }

    @Override
    public void historyClear(int isSuccessFlag, long toChatUserId, boolean isGroup) {
        long toId;
        if(mComeFrom == CHAT_MESSAGE_COMEFROM.RENREN_CHAT_GROUP) {
            toId = mGroupId;
        } else {
            toId = mToChatId;
        }
        if(toId == toChatUserId && isClearChatMessageNow) {

            if(mDialog != null) {
                mDialog.dismiss();
            }
            if(isSuccessFlag == ClearServerHistoryUitl.HistoryClearFlag.CLEAR_SERVER_FAILED) {
            	ThreadPool.obtain().executeMainThread(new Runnable() {
                    @Override
                    public void run() {
                        CommonUtil.toast(RenrenChatApplication.getAppResources().getString(R.string.chatmessages_layout_21));
                    }
                });
            } else {
            	ThreadPool.obtain().executeMainThread(new Runnable() {
                    @Override
                    public void run() {
                        CommonUtil.toast(RenrenChatApplication.mContext.getResources().getString(R.string.chatmessages_layout_15));
                    }
                });
            }
            isClearChatMessageNow = false;
        }
    }

    private String getTimeFromLongToString(long time) {
        Date date = new Date(time);
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }

    public static interface CHAT_MESSAGE_COMEFROM{
		int MAINFRAGMENT_ACTIVITY = 0;
		int RENREN_CHAT_SINGLE = 1;
		int RENREN_CHAT_GROUP = 2;
//        int THIRD_CHAT_SINGLE = 3;
	}

	public static  interface CHAT_MESSAGE_PARAMS{
		String COMEFROM = "COMEFROM";
		String TO_CHAT_ID = "TO_CHAT_ID";
        String USER_DOMAIN = "USER_DOMAIN";
        String USER_NAME = "USER_NAME";
	}

	public static interface CHAT_MESSAGE_REQUEST_CODE{
		int EXIT_GROUP = 3;
		int CREATE_GROUP = 4;
	}

    public static interface CREAT_ALERT_DIALOG_TYPE {
        int DEL_GROUP_FROM_FRIENDS_LIST = 0;
        int CLEAR_CHAT_HISTORY = 1;
        int SYN_CHAT_HISTORY = 2;
        int LEAVE_GROUP = 3;
        int DISTORY_GROUP = 4;
    }

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.d_chatmessages);
		RoomInfosData.getInstance().registorObserver(this);
		mInflater = LayoutInflater.from(D_ChatMessagesActivity.this);
		context = D_ChatMessagesActivity.this;
		RenrenChatApplication.pushStack(this);
		mDialog = new ProgressDialog(D_ChatMessagesActivity.this);
		mComeFrom = getIntent().getIntExtra(CHAT_MESSAGE_PARAMS.COMEFROM, 0);
        ViewMapUtil.getUtil().viewMapping(mHolder, getWindow().getDecorView());
        mAdapter = new GroupMessageAdapter();
        mHolder.mGride.setAdapter(mAdapter);
        mHolder.mGride.setNumColumns(4);
        clearServerHistoryUitl = ClearServerHistoryUitl.getInstance();
        synHistoryRecordDAO = DAOFactoryImpl.getInstance().buildDAO(SynHistoryRecordDAO.class);
        clearServerHistoryUitl.registorObserver(this);
        C_ContactsData.getInstance().registorNewObserver(this);
		switch (mComeFrom) {
		case CHAT_MESSAGE_COMEFROM.MAINFRAGMENT_ACTIVITY:
			initDataContact();
	        initViewContact();
			break;
		case CHAT_MESSAGE_COMEFROM.RENREN_CHAT_GROUP:
			initDataGroup();
			initViewGroup();
			break;
		case CHAT_MESSAGE_COMEFROM.RENREN_CHAT_SINGLE:
			initDataSingle();
			initViewSingle();
			break;
		}
		initTitle();
	}
	private void initDataSingle() {
		mToChatId = getIntent().getLongExtra(CHAT_MESSAGE_PARAMS.TO_CHAT_ID, 0);
        mDomain = getIntent().getStringExtra(CHAT_MESSAGE_PARAMS.USER_DOMAIN);
        mUserName = getIntent().getStringExtra(CHAT_MESSAGE_PARAMS.USER_NAME);
//		ContactModel model = C_ContactsData.getInstance().getContactInfoFromLocal(mToChatId);
//        ContactModel model = null;
//        if(!TextUtils.isEmpty(mDomain) && mDomain.equals(DomainUrl.SIXIN_DOMAIN)) {
//        	model = C_ContactsData.getInstance().getSiXinContact(mToChatId, null);
//        }else {
//        	
//        }
        addToChatModel();
		addMyself();
        SynHistoryRecordModel synHistoryRecordModel = synHistoryRecordDAO.querySynHitoryRecordInfo(mToChatId);
        if(synHistoryRecordModel != null) {
            mSynLastTime = synHistoryRecordModel.mLastSynTime;
        }
        if(isSiXinContact()){
        	 addAddContactView();
        }
       
	}
	
	public boolean isSiXinContact(){
		if(!TextUtils.isEmpty(mDomain)) {
        	ContactBaseModel contactBaseModel = C_ContactsData.getContact(mToChatId, mDomain);
        	if(DomainUrl.SIXIN_DOMAIN.equals(mDomain) || (contactBaseModel.getmRelation()&ContactBaseModel.Relationship.IS_SI_XIN)==ContactBaseModel.Relationship.IS_SI_XIN){
        		return true;
        	}
        }
		return false;
	}

	private void addToChatModel() {
		ContactBaseModel contactBaseModel = C_ContactsData.getContact(mToChatId, mDomain);
		final ContactBaseModel contactModel;
		if(contactBaseModel == null){
			Log.v("jj", "contactBaseModel == null");
			contactBaseModel = ContactModelFactory.createContactModel(mDomain);
			contactBaseModel.mUserId = mToChatId;
			contactBaseModel.mContactName = mUserName;
			contactBaseModel.name = mUserName;
		}else Log.v("jj", "contactBaseModel != null");
		contactBaseModel.mDomain = mDomain;
		contactModel = contactBaseModel;
		
		ContactModel model = new ContactModel();
		model.mUserId = contactModel.mUserId;
		model.mContactName = contactModel.mContactName;
		model.mDomain = contactModel.mDomain;
		model.mSmallHeadUrl = contactModel.getmHeadUrl();
		mMembers.add(model);
	}

    private boolean initDataContact() {
		mGroupId = getIntent().getLongExtra("groupId", 0);
		if(mGroupId != 0){
			roomInfo = RoomInfosData.getInstance().getRoomInfo(mGroupId);
		}
		if (roomInfo != null) {
//			mMembers.addAll(roomInfo.mMembers);
//			addMyself();
            membersSort();
            return true;
		}else{
			finish();
            return false;
		}
	}

    private void membersSort() {
        List<ContactModel> contactModels = new ArrayList<ContactModel>();
        contactModels.addAll(roomInfo.mMembers);
        
        ContactModel model = new ContactModel();
        model.mUserId = LoginManager.getInstance().getLoginInfo().mUserId;
        model.mSmallHeadUrl =  LoginManager.getInstance().getLoginInfo().mMediumUrl;
        model.mLargeHeadUrl = LoginManager.getInstance().getLoginInfo().mHeadUrl;
        model.mContactName = LoginManager.getInstance().getLoginInfo().mUserName;
        contactModels.add(model);//在集合中加入自己
//        Log.v("zz","contactModels.size = " + contactModels.size());
        for(ContactModel contactModel : contactModels){
            if(roomInfo.isOwner(contactModel.mUserId)){
                mMembers.add(0,contactModel);//mMembers中加入群主
                break;
            }
        }
        contactModels.removeAll(mMembers);
//        for(ContactModel contactModel : roomInfo.mMembers){
//        	Log.v("drb","room = " + contactModel.mHeadUrl);
//        }
       
//        ArrayList<ContactModel>  arrayList =   ContactResrouceUtils.combineResrouceOrderd(contactModels);
//        for(ContactModel contactModel: arrayList){
//        	Log.v("drb", contactModel.getmContactName());
//        }
        mMembers.addAll(ContactResrouceUtils.combineResrouceOrderd(contactModels));
    }

	private void initDataGroup() {
        if(initDataContact()) {
            mIsOwner = roomInfo.isOwner(LoginManager.getInstance().getLoginInfo().mUserId);
            if(mMembers.size() != mGroupNumber){
                addAddContactView();
            }
            if(mIsOwner) {
                addDelContactView();
            }
            SynHistoryRecordModel synHistoryRecordModel = synHistoryRecordDAO.querySynHitoryRecordInfo(mGroupId);
            if(synHistoryRecordModel != null) {
                mSynLastTime = synHistoryRecordModel.mLastSynTime;
            }
        }
	}
	/*
	 * 删除最后标志对象
	 */
	public void delLastView(){
		for(Iterator<ContactModel> i = mMembers.iterator(); i.hasNext();){
            ContactModel contactModel = i.next();
			if(contactModel.mGroupMessageAddContactView){
				i.remove();
                hasAddContactView = false;
			} else if(contactModel.mGroupMessageDelContactView) {
                i.remove();
                hasDelContactView = false;
            }
		}
	}
	/*
	 * 判断是否存在最后标志对象
	 */
//	public boolean hasLastView(){
//		for(ContactModel contactModel : mMembers){
//			if(contactModel.mGroupMessageAddContactView){
//				return true;
//			}
//		}
//		return false;
//	}
	private void initViewSingle() {


        mHolder.mSynRecordLinearLayout.setVisibility(View.VISIBLE);
        mHolder.mClearRecordFrameLayout.setVisibility(View.VISIBLE);
        if(!isSiXinContact()){
        	 mHolder.mThirdUserPromptLinearLayout.setVisibility(View.VISIBLE);
        }
        mHolder.mSynRecordLinearLayout.setBackgroundResource(R.drawable.top_bg_selector);
		mHolder.mClearRecordFrameLayout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
                if(isSynChatMessageNow) {
                    CommonUtil.toast(RenrenChatApplication.getAppResources().getString(R.string.chatmessages_layout_17));
                    return;
                }else if(isClearChatMessageNow){
                	 CommonUtil.toast(RenrenChatApplication.getAppResources().getString(R.string.chatmessages_layout_18));
                	 return;
                }
                showAlertDialog(R.string.MultiChatForwardScreen_java_2, R.string.ChatMessagesActivity_java_2, CREAT_ALERT_DIALOG_TYPE.CLEAR_CHAT_HISTORY,
                        mToChatId, false);
			}
		});
        mHolder.mSynRecordLinearLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isSynChatMessageNow) {
                    CommonUtil.toast(RenrenChatApplication.getAppResources().getString(R.string.chatmessages_layout_19));
                    return;
                }
                showAlertDialog(R.string.chatmessages_layout_10, R.string.ChatMessagesActivity_java_20, CREAT_ALERT_DIALOG_TYPE.SYN_CHAT_HISTORY,
                        mToChatId, false);
                //test代码
//                SynHistoryRecordModel synHistoryRecordModel = new SynHistoryRecordModel();
//                synHistoryRecordModel.mToChatId = mToChatId;
//                synHistoryRecordModel.mLastSynTime = System.currentTimeMillis();
//                synHistoryRecordDAO.updateSynHitoryRecordInfo(mToChatId, synHistoryRecordModel);
//                Log.v("@@@", "synHistoryRecordDAO.updateSynHitoryRecordInfo :" + mToChatId);
            }
        });
        if(mSynLastTime != 0) {
            mHolder.mSynRecordInfo.setText("上次获取时间" + getTimeFromLongToString(mSynLastTime));
        }

	}
	/*
	 * 弹出dialog
	 */
	private void showDialog(String text) {
		mDialog.setMessage(text);
		mDialog.show();
	}
	/*
	 * 关闭dialog
	 */
	private void dismissDialog(){
		try {
			if(mDialog != null){
				mDialog.dismiss();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private void initViewGroup() {

        mHolder.mGroupNameFrameLayout.setVisibility(View.VISIBLE);
        mHolder.mSaveContactFrameLayout.setVisibility(View.VISIBLE);
        mHolder.mSynRecordLinearLayout.setVisibility(View.VISIBLE);
        mHolder.mClearRecordFrameLayout.setVisibility(View.VISIBLE);
        mHolder.mGroupMessageNotifyLinearLayout.setVisibility(View.VISIBLE);
        mHolder.mExitGroupButton.setVisibility(View.VISIBLE);

        mHolder.mGrideViewContainer.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mGestureDetector.onTouchEvent(event);
                return false;
            }
        });

        mHolder.mGride.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mGestureDetector.onTouchEvent(event);
                return false;
            }
        });

//        mHolder.mChatMessageContainer.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                mGestureDetector.onTouchEvent(event);
//                return false;
//            }
//        });

		if(mIsOwner){
			mHolder.mExitGroupButton.setText(getResources().getString(R.string.ChatMessagesActivity_java_3));		//ChatMessagesActivity_java_3=退出并解散;
            mHolder.mExitGroupButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
                    showAlertDialog(R.string.ChatMessagesActivity_java_3, R.string.ChatMessagesActivity_java_4, CREAT_ALERT_DIALOG_TYPE.DISTORY_GROUP,
                            mGroupId, true);
				}
			});
		}else{
			mHolder.mGroupNameImageView.setVisibility(View.GONE);
            mHolder.mExitGroupButton.setText(getResources().getString(R.string.ChatMessagesActivity_java_6));		//ChatMessagesActivity_java_6=退出并删除;
            mHolder.mExitGroupButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
                    showAlertDialog(R.string.ChatMessagesActivity_java_6, R.string.ChatMessagesActivity_java_7, CREAT_ALERT_DIALOG_TYPE.LEAVE_GROUP,
                            mGroupId, true);
				}
			});
		}

		mHolder.mClearRecordFrameLayout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
                if(isSynChatMessageNow) {
                    CommonUtil.toast(RenrenChatApplication.getAppResources().getString(R.string.chatmessages_layout_17));
                    return;
                }else if(isClearChatMessageNow){
                	 CommonUtil.toast(RenrenChatApplication.getAppResources().getString(R.string.chatmessages_layout_18));
                	 return;
                }
                showAlertDialog(R.string.GroupContactModel_java_2, R.string.ChatMessagesActivity_java_9, CREAT_ALERT_DIALOG_TYPE.CLEAR_CHAT_HISTORY,
                        mGroupId, true);
			}
		});


        mHolder.mSynRecordLinearLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isSynChatMessageNow) {
                    CommonUtil.toast(RenrenChatApplication.getAppResources().getString(R.string.chatmessages_layout_19));
                    return;
                }
                showAlertDialog(R.string.chatmessages_layout_10, R.string.ChatMessagesActivity_java_20, CREAT_ALERT_DIALOG_TYPE.SYN_CHAT_HISTORY,
                        mGroupId, true);
                //test代码
//                SynHistoryRecordModel synHistoryRecordModel = new SynHistoryRecordModel();
//                synHistoryRecordModel.mToChatId = mGroupId;
//                synHistoryRecordModel.mLastSynTime = System.currentTimeMillis();
//                synHistoryRecordDAO.updateSynHitoryRecordInfo(mGroupId, synHistoryRecordModel);
//                Log.v("@@@", "synHistoryRecordDAO.updateSynHitoryRecordInfo :" + mGroupId);
            }
        });



        /* 群消息提醒逻辑 */
        mRRSP = new RRSharedPreferences(RenrenChatApplication.mContext);
        mHolder.mGroupMessageNotifyBtn.setSelected(mRRSP.getBooleanValue(String.valueOf(mGroupId), true));
        if(mHolder.mGroupMessageNotifyBtn.isSelected()){
            mHolder.mGroupMessageNotifyInfo.setText("已开启");
        }else{
            mHolder.mGroupMessageNotifyInfo.setText("已关闭");
        }

        mHolder.mGroupMessageNotifyLinearLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mHolder.mGroupMessageNotifyBtn.isSelected()){
                    mHolder.mGroupMessageNotifyBtn.setSelected(false);
                    mHolder.mGroupMessageNotifyInfo.setText("已关闭");
                    mRRSP.putBooleanValue(String.valueOf(mGroupId), false);
                }else{
                    mHolder.mGroupMessageNotifyBtn.setSelected(true);
                    mHolder.mGroupMessageNotifyInfo.setText("已开启");
                    mRRSP.putBooleanValue(String.valueOf(mGroupId), true);
                }
            }
        });


        mHolder.mGroupNameFrameLayout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
                if(!mIsOwner){
                    CommonUtil.toast(getResources().getString(R.string.ChatMessagesActivity_java_10));		//ChatMessagesActivity_java_10=只有群主才能修改群名;
                    return;
                }
                MultiChatNameSettingActivity.show(context, mGroupId);
			}
		});

		mHolder.mSaveContact.setText(getResources().getString(R.string.ChatMessagesActivity_java_11));		//ChatMessagesActivity_java_11=保存到联系人列表;
		mHolder.mSaveContactFrameLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                saveContacts(mGroupId);
                //<TODO cf>
            }
        });
        if(roomInfo != null && null != roomInfo.getName()){
			if(roomInfo.getName().length() > 8){
				mHolder.mGroupNameTextView.setText(roomInfo.getName());
			}else {
                mHolder.mGroupNameTextView.setText(roomInfo.getName());
            }
//			mHolder.mHasGroupName.setVisibility(View.GONE);
		}else {
            mHolder.mGroupNameTextView.setVisibility(View.GONE);
//            mHolder.mHasGroupName.setVisibility(View.VISIBLE);
		}

        if(mSynLastTime != 0) {
            mHolder.mSynRecordInfo.setText("上次获取时间" + getTimeFromLongToString(mSynLastTime));
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mHolder.mScrollView.scrollTo(0, 0);
            }
        }, 50);
	}

    class Guestliston extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            if(mSign) {
                cancelDelContact();
            }
            return true;
        }
    }

	private void initViewContact() {
		mHolder.mDelGroupBtn.setVisibility(View.VISIBLE);
        mHolder.mStartChatBtn.setVisibility(View.VISIBLE);

        mHolder.mDelGroupBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
                showAlertDialog(R.string.MultiChatForwardScreen_java_2, R.string.ChatMessagesActivity_java_12, CREAT_ALERT_DIALOG_TYPE.DEL_GROUP_FROM_FRIENDS_LIST,
                        mGroupId, true);
			}
		});
        mHolder.mStartChatBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				RoomInfoModelWarpper infoModelWarpper = RoomInfosData.getInstance().getRoomInfo(mGroupId);
				RenRenChatActivity.show(context, infoModelWarpper);
				finish();
				
				// 统计
				BackgroundUtils.getInstance().multiChatStatistics(Htf.MULTI_CHAT_FROM_CONTACTLIST);
			}
		});

	}

	private void cancelDelContact() {
        mSign = false;
        delLastView();
        if(mMembers.size() != mGroupNumber){
            addAddContactView();
        }
        if(mIsOwner) {
            addDelContactView();
        }
        if(mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }
    }

    private void showAlertDialog(int titleStirngId, int messageStringId, final int type, final long toId,
                                 final boolean isGroup) {
        AlertDialog.Builder builder = new Builder(context);
        final CheckBox checkBox;
        if(CREAT_ALERT_DIALOG_TYPE.CLEAR_CHAT_HISTORY == type) {
            LinearLayout layout = (LinearLayout) mInflater.inflate(R.layout.chat_message_alert_dialog, null);
            checkBox = (CheckBox) layout.findViewById(R.id.delete_chat_history_from_server);
            checkBox.setChecked(false);
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked) {
                        if(!Methods.checkNet(D_ChatMessagesActivity.this, false)) {
                            checkBox.setChecked(false);
                            CommonUtil.toast("当前网络不可用不可以清空云端聊天记录");
                        }
                    }
                }
            });
            builder.setView(layout);
        } else {
            checkBox = null;
            builder.setMessage(getResources().getString(messageStringId));		//ChatMessagesActivity_java_9=是否删除该对话？（只清空聊天记录和删除对话，但不会退出该群聊）;
        }
        builder.setTitle(getResources().getString(titleStirngId));		
        builder.setPositiveButton(getResources().getString(R.string.MultiChatForwardScreen_java_6), new DialogInterface.OnClickListener() {		//MultiChatForwardScreen_java_6=确认;
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                switch (type) {
                case CREAT_ALERT_DIALOG_TYPE.DEL_GROUP_FROM_FRIENDS_LIST:
                    RoomInfosData.getInstance().deleteRoomFromContactOnNet(toId);
                    finish();
                    break;
                case CREAT_ALERT_DIALOG_TYPE.CLEAR_CHAT_HISTORY:
                    showDialog(RenrenChatApplication.mContext.getResources().getString(R.string.chatmessages_layout_14));
                    isClearChatMessageNow = true;
                    if(checkBox != null && checkBox.isChecked()) {
                        Log.v("@@@", "清空云端加本地！");
                        clearServerHistoryUitl.clearServerChatHistory(true, toId, isGroup);
                    } else {
                        Log.v("@@@", "只清空本地！");
                        clearServerHistoryUitl.clearServerChatHistory(false, toId, isGroup);
                    }
                    break;
                case CREAT_ALERT_DIALOG_TYPE.SYN_CHAT_HISTORY:
                    showDialog(RenrenChatApplication.mContext.getResources().getString(R.string.chatmessages_layout_13));
                    isSynChatMessageNow = true;
                    SystemUtil.log("click", "syn history ");
//                    mSynHistoryUtil.synChatHistory(toId, isGroup);
                    C_Syn.getInstance().syn(isGroup, toId, null, D_ChatMessagesActivity.this);
                    break;
                case CREAT_ALERT_DIALOG_TYPE.LEAVE_GROUP:
                    dialog.dismiss();
                    showDialog(getResources().getString(R.string.ChatMessagesActivity_java_8));		//ChatMessagesActivity_java_5=解散中...;
                    leaveGroup(toId);
                    break;
                case CREAT_ALERT_DIALOG_TYPE.DISTORY_GROUP:
                    dialog.dismiss();
                    showDialog(getResources().getString(R.string.ChatMessagesActivity_java_5));		//ChatMessagesActivity_java_5=解散中...;
                    dissolveGroup(toId);
                    break;
                }
            }
        });
        builder.setNegativeButton(getResources().getString(R.string.ChatMessageWarpper_FlashEmotion_java_4), new DialogInterface.OnClickListener() {		//ChatMessageWarpper_FlashEmotion_java_4=取消;
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }


	/*
	 * 在群成员队尾添加一个标志对象，可以邀请其他好友加入聊天群
	 */
	private void addAddContactView() {
		ContactModel contactModel = new ContactModel();
		contactModel.mGroupMessageAddContactView = true;
		mMembers.add(contactModel);
        hasAddContactView = true;
	}

    /*
      * 在邀请好友加入群聊的标志位后添加一个标志对象，只有群主显示用来编辑并且删除群成员
      */
    private void addDelContactView() {
        ContactModel contactModel = new ContactModel();
        contactModel.mGroupMessageDelContactView = true;
        mMembers.add(contactModel);
        hasDelContactView = true;
    }

	/*
	 * 添加自己到群成员列表
	 */
	private void addMyself() {
		ContactModel model = new ContactModel();
		model.mUserId = LoginManager.getInstance().getLoginInfo().mUserId;
		model.mSmallHeadUrl = LoginManager.getInstance().getLoginInfo().mMediumUrl;
		model.mLargeHeadUrl = LoginManager.getInstance().getLoginInfo().mLargeUrl;
		model.mContactName = LoginManager.getInstance().getLoginInfo().mUserName;
		mMembers.add(0,model);
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_CODE.SELECTCONTACT) {
            switch (resultCode) {
            case RESULT_OK:
            	roomInfo = RoomInfosData.getInstance().getRoomInfo(mGroupId);
                if (roomInfo != null) {
                    mMembers.clear();
//                    mMembers.addAll(roomInfo.mMembers);
//                    addMyself();
                    membersSort();
                    if(mMembers.size() != mGroupNumber){
                        addAddContactView();
                    }
                    if(mIsOwner) {
                        addDelContactView();
                    }
                    mAdapter.notifyDataSetChanged();
                }
                break;
            case RESULT_CANCELED:
                break;
            }
        }
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	private void initTitle() {
		mBaseTitle = new BaseTitleLayout(D_ChatMessagesActivity.this,mHolder.mTitleLayout);
		TextView text = mBaseTitle.getTitleMiddle();
		mBaseTitle.getTitleLeft().setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		switch (mComeFrom) {
		case CHAT_MESSAGE_COMEFROM.MAINFRAGMENT_ACTIVITY:
			if(roomInfo!=null && null != roomInfo.getName()){
				if(roomInfo.mRoomMemberNumber == 0){
					roomInfo.mRoomMemberNumber = 1;
				}
//				text.setText(roomInfo.getName()+"("+(roomInfo.mRoomMemberNumber)+")");
				text.setText(RenrenChatApplication.mContext.getResources().getString(R.string.chatmessages_layout_16));
			}
			break;
		case CHAT_MESSAGE_COMEFROM.RENREN_CHAT_GROUP:
			text.setText(RenrenChatApplication.getmContext().getResources().getString(R.string.ChatMessagesActivity_java_13));		//ChatMessagesActivity_java_13=聊天信息; 
			break;
		case CHAT_MESSAGE_COMEFROM.RENREN_CHAT_SINGLE:
			text.setText(RenrenChatApplication.getmContext().getResources().getString(R.string.ChatMessagesActivity_java_13));		//ChatMessagesActivity_java_13=聊天信息; 
			break;
		}
	}
	private class ViewHolder {
        @ViewMapping(ID=R.id.group_member_head)
		public NotSynImageView head;
        @ViewMapping(ID=R.id.member_head_click)
		public ImageView head_click;
        @ViewMapping(ID=R.id.group_member_name)
		public TextView name;
        @ViewMapping(ID=R.id.del_member_btn)
		public Button button;
        @ViewMapping(ID=R.id.frameLayout)
		public FrameLayout head_bg;
        @ViewMapping(ID=R.id.frameLayout1)
		public FrameLayout lastView;
        @ViewMapping(ID=R.id.cdw_is_inblack_view)
		public ImageView blackList;
        
	}
    public class GroupMessageAdapter extends BaseAdapter {
    	public GroupMessageAdapter() {
    	}
		@Override
		public int getCount() {
			return mMembers.size();
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
				convertView = mInflater.inflate(R.layout.d_group_member_item, null);
				ViewMapUtil.getUtil().viewMapping(holder, convertView);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			final ContactModel contactModel = mMembers.get(position);
            Log.v("@@@", "getView");
            Log.v("@@@", "position is:" + position + "; and contactMode is:" + contactModel + "; and" +
                    " contactModel's id is:" + (contactModel == null? 0: contactModel.mUserId));
			if(contactModel != null){
                if(contactModel.mGroupMessageAddContactView){
                    Log.v("@@@", "getView 加号 mSign is:" + mSign);
                    holder.head_bg.setVisibility(View.INVISIBLE);
                    holder.button.setVisibility(View.INVISIBLE);
                    holder.name.setVisibility(View.INVISIBLE);
                    holder.blackList.setVisibility(View.GONE);
                    if(!mSign) {
                        holder.lastView.setVisibility(View.VISIBLE);
                        holder.lastView.setBackgroundResource(R.drawable.chat_setting_add_member_btn_selector);
                        holder.lastView.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                switch (mComeFrom) {
                                case CHAT_MESSAGE_COMEFROM.MAINFRAGMENT_ACTIVITY:
                                    break;
                                case CHAT_MESSAGE_COMEFROM.RENREN_CHAT_GROUP:
                                    Intent intent = new Intent(context, D_SelectGroupChatContactActivity.class);
                                    intent.putExtra(PARAMS.COMEFROM, D_SelectGroupChatContactActivity.COMEFROM.GROUP_CHAT_MESSAGE);
//                                    ArrayList<Integer> arrayList = new ArrayList<Integer>();
                                    long[] value = new long[mMembers.size()];
                                    Bundle bundle = new Bundle();
//                                    for(ContactModel contactModel : mMembers){
//                                    	Log.v("bb", contactModel.mUserId+ "");
//                                        arrayList.add((int)contactModel.mUserId);
//                                    }
                                    
                                    for(int i=0; i<mMembers.size(); i++){
                                    	value[i] = mMembers.get(i).getmUserId();
                                    }
                                    bundle.putLongArray(PARAMS.GROUP_MEMBER, value);
//                                    bundle.putIntegerArrayList(PARAMS.GROUP_MEMBER, arrayList);
                                    intent.putExtra(PARAMS.GROUP_ID, mGroupId);
                                    intent.putExtras(bundle);
                                    startActivityForResult(intent,REQUEST_CODE.SELECTCONTACT);
                                    break;
                                case CHAT_MESSAGE_COMEFROM.RENREN_CHAT_SINGLE:
                                    Intent i = new Intent(context, D_SelectGroupChatContactActivity.class);
                                    i.putExtra(PARAMS.COMEFROM, D_SelectGroupChatContactActivity.COMEFROM.SING_CHAT_MESSAGE);
                                    i.putExtra(PARAMS.TO_CHAT_ID, mToChatId);
                                    startActivity(i);
                                    
                                    break;
                                }
                            }
                        });
                    } else {
                        holder.lastView.setVisibility(View.INVISIBLE);
                    }
                }else if(contactModel.mGroupMessageDelContactView){
                    holder.head_bg.setVisibility(View.INVISIBLE);
                    holder.button.setVisibility(View.INVISIBLE);
                    holder.name.setVisibility(View.INVISIBLE);
                    holder.blackList.setVisibility(View.GONE);
                    Log.v("@@@", "getView 减号 mSign is:" + mSign);
                    if(!mSign) {
                        holder.lastView.setVisibility(View.VISIBLE);
                        holder.lastView.setBackgroundResource(R.drawable.chat_setting_del_member_btn_selector);
                        holder.lastView.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                switch (mComeFrom) {
                                    case CHAT_MESSAGE_COMEFROM.MAINFRAGMENT_ACTIVITY:
                                        break;
                                    case CHAT_MESSAGE_COMEFROM.RENREN_CHAT_GROUP:
                                        if(!mSign){
                                            mSign = true;
        //                                    delLastView();
                                        }
                                        if(mAdapter != null){
                                            mAdapter.notifyDataSetChanged();
                                        }
                                        break;
                                    case CHAT_MESSAGE_COMEFROM.RENREN_CHAT_SINGLE:
                                        break;
                                }
                            }
                        });
                    } else {
                        holder.lastView.setVisibility(View.INVISIBLE);
                    }
                } else {
                    holder.head_bg.setVisibility(View.VISIBLE);
                    holder.lastView.setVisibility(View.INVISIBLE);
                    String headUrl = contactModel.getmHeadUrl();
                    holder.head.clear();
                    holder.head.addUrl(headUrl);
                    Log.v("jj", "headUrl = " + headUrl);
                    holder.name.setText(contactModel.mContactName);
                    holder.name.setVisibility(View.VISIBLE);
                    Log.v("@@@", "getView mSign is:" + mSign);
                    //是否在黑名单里
                    if(!contactModel.isBlacklist(contactModel.mRelation)) {
                        holder.blackList.setVisibility(View.GONE);
                    }else {
                    	 holder.blackList.setVisibility(View.VISIBLE);
                    }
                    ContactBaseModel contact = null;
                    if(contactModel.mUserId != LoginManager.getInstance().mLoginInfo.mUserId){
                    	contact = C_ContactsData.getInstance().getContact(contactModel.mUserId,contactModel.mDomain);
                    }
                    
                    if(contact != null){
                        if(!contact.isBlacklist(contact.mRelation)) {
                            holder.blackList.setVisibility(View.GONE);
                        }else {
                        	 holder.blackList.setVisibility(View.VISIBLE);
                        }
                    }
                    
                    if(mSign){
                        Log.v("@@@", "getView 编辑状态");
                        holder.head_click.setBackgroundDrawable(null);
                        holder.button.setVisibility(View.VISIBLE);
                        holder.head_click.setClickable(false);
                        if(contactModel.mUserId == LoginManager.getInstance().getLoginInfo().mUserId){
                            holder.button.setVisibility(View.INVISIBLE);
                        }else{
                            holder.button.setVisibility(View.VISIBLE);
                        }
                    }else{
                        Log.v("@@@", "getView 隐藏小叉号");
                        holder.button.setVisibility(View.INVISIBLE);
                        if(contactModel.mUserId == LoginManager.getInstance().getLoginInfo().mUserId) {
                            holder.head_click.setBackgroundDrawable(null);
                            holder.head_click.setClickable(false);
                        } else {
                            holder.head_click.setClickable(true);
                            holder.head_click.setBackgroundResource(R.drawable.member_head_selector);
                            holder.head_click.setOnClickListener(new OnClickListener() {
                                @Override
                                public void onClick(View v) {
    //                                C_UserInfoActivity.show(context, contactModel, COMEFROM.MULTI_CHAT_ACTIVITY);
                                    UserInfoActivity.show(context, contactModel.mUserId, contactModel.mDomain);
                                }
                            });
                        }
                    }
                    holder.button.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showDialog(getResources().getString(R.string.ChatMessagesActivity_java_15));		//ChatMessagesActivity_java_15=正在删除成员...;
                            List<Long> list = new ArrayList<Long>();
                            list.add(contactModel.mUserId);
                            removeMember(mGroupId,list,contactModel,holder.button);
                        }
                    });
                }
			}
			return convertView;
		}
    }
	@Override
	protected void onDestroy() {
		if(mMembers.size() > 0){
			mMembers.clear();
		}
//        if(synHistoryRecordDAO != null) {
//            synHistoryRecordDAO.unregistorObserver(this);
//        }
//        mSynHistoryUtil.unRegistorObserver(this);
        clearServerHistoryUitl.unRegistorObserver(this);
        C_ContactsData.getInstance().unRegistorNewObserver(this);
		RenrenChatApplication.popStack(this);
		RoomInfosData.getInstance().unRegistorObserver(this);
		super.onDestroy();
	}
	/*
	 * 删除群成员
	 */
    public void removeMember(long groupId,final List<Long> memberId,final ContactModel contactModel,final Button button){
    	AbstractNotSynRequest<NULL> abstractNotSynRequest = RequestConstructorProxy.getInstance().deleteMember(LoginManager.getInstance().getLoginInfo().mUserId, groupId, memberId);
    	abstractNotSynRequest.setCallback(new OnDataCallback<NULL>() {
			@Override
			public void onSuccess() {
				roomInfo.deleteMember(memberId.get(0));
				roomInfo.updateToDB();
				RenrenChatApplication.mHandler.post(new Runnable() {
					@Override
					public void run() {
						dismissDialog();
						mMembers.remove(contactModel);
						mAdapter.notifyDataSetChanged();
					}
				});
			}
			@Override
			public void onSuccessRecive(NULL data) {
			}
			@Override
			public void onError(int errorCode,final String errorMsg) {
				RenrenChatApplication.mHandler.post(new Runnable() {
					@Override
					public void run() {
						CommonUtil.toast(errorMsg);
						button.setClickable(true);
						dismissDialog();
					}
				});
			}
		});
    	ChatMessageSender.getInstance().sendRequestToNet(abstractNotSynRequest);
    }
    /*
     * 保存群联系人
     */
    public void saveContacts(final long groupId){
    	AbstractNotSynRequest<NULL> abstractNotSynRequest = RequestConstructorProxy.getInstance().saveRoomInfo(LoginManager.getInstance().getLoginInfo().mUserId, groupId);
    	abstractNotSynRequest.setCallback(new OnDataCallback<NULL>() {
			@Override
			public void onSuccess() {
				RenrenChatApplication.mHandler.post(new Runnable() {
					@Override
					public void run() {
						CommonUtil.toast(RenrenChatApplication.getmContext().getResources().getString(R.string.ChatMessagesActivity_java_16));		//ChatMessagesActivity_java_16=保存联系人成功; 
						RoomInfosData.getInstance().saveRoomToContact(groupId);
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
						CommonUtil.toast(errorMsg);
						dismissDialog();
					}
				});
			}
		});
    	ChatMessageSender.getInstance().sendRequestToNet(abstractNotSynRequest);
    }
    /*
     * 解散该群
     */
    public void dissolveGroup(final long groupId){
    	AbstractNotSynRequest<NULL> abstractNotSynRequest = RequestConstructorProxy.getInstance().destroyRoom(LoginManager.getInstance().getLoginInfo().mUserId, groupId);
    	abstractNotSynRequest.setCallback(new OnDataCallback<NULL>() {
			@Override
			public void onSuccess() {
				RenrenChatApplication.mHandler.post(new Runnable() {
					@Override
					public void run() {
						CommonUtil.toast(RenrenChatApplication.getmContext().getResources().getString(R.string.ChatMessagesActivity_java_17));		//ChatMessagesActivity_java_17=解散群成功; 
						dismissDialog();
						ChatDataHelper.getInstance().deleteChatMessageByGroupId(groupId);
                        ChatSessionHelper.getInstance().deleteChatSessionByGroupId(groupId);
						RoomInfosData.getInstance().deleteRoom(groupId);
						RoomInfosData.getInstance().deleteRoomInfoToDB(roomInfo);
						setResult(RESULT_OK);
						finish();
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
						dismissDialog();
						CommonUtil.toast(errorMsg);
					}
				});
			}
		});
    	ChatMessageSender.getInstance().sendRequestToNet(abstractNotSynRequest);
    }
    /*
     * 退出该群
     */
    public void leaveGroup(final long groupId){
    	AbstractNotSynRequest<NULL> abstractNotSynRequest = RequestConstructorProxy.getInstance().leaveRoom(LoginManager.getInstance().getLoginInfo().mUserId, groupId);
    	abstractNotSynRequest.setCallback(new OnDataCallback<NULL>() {
			@Override
			public void onSuccess() {
				RenrenChatApplication.mHandler.post(new Runnable() {
					@Override
					public void run() {
						CommonUtil.toast(RenrenChatApplication.getmContext().getResources().getString(R.string.ChatMessagesActivity_java_18));		//ChatMessagesActivity_java_18=退群成功; 
						dismissDialog();
						ChatDataHelper.getInstance().deleteChatMessageByGroupId(groupId);
                        ChatSessionHelper.getInstance().deleteChatSessionByGroupId(groupId);
						RoomInfosData.getInstance().deleteRoom(groupId);
						RoomInfosData.getInstance().deleteRoomInfoToDB(roomInfo);
						setResult(RESULT_OK);
						finish();
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
						CommonUtil.toast(errorMsg);
						dismissDialog();
					}
				});
			}
		});
    	ChatMessageSender.getInstance().sendRequestToNet(abstractNotSynRequest);
    }
	@Override
	public void notifyRoomInfoDataUpdate(byte state,final long roomId) {
		RenrenChatApplication.mHandler.post(new Runnable() {
			@Override
			public void run() {
				mAdapter.notifyDataSetChanged();
				if(roomInfo!=null && roomInfo.mRoomId == roomId){
					if(mHolder.mGroupNameFrameLayout.getVisibility() == View.VISIBLE && mHolder.mGroupNameTextView != null){
						final RoomInfoModelWarpper tempRoom = RoomInfosData.getInstance().getRoomInfo(roomId);
                        if(tempRoom != null && null != tempRoom.getName()){
                            if(tempRoom.getName().length() > 10){
                                mHolder.mGroupNameTextView.setText(tempRoom.getName());
                            }else {
                                mHolder.mGroupNameTextView.setText(tempRoom.getName());
                            }
                        }
					}
				}
			}
		});
	}
	@Override
	protected void onResume() {
        if (mSign) {
            cancelDelContact();
        }
		super.onResume();
	}
	@Override
	protected void finalize() throws Throwable {
		super.finalize();
	}

	@Override
	public void notifyDataUpdate(byte state, byte type) {
		RenrenChatApplication.mHandler.post(new Runnable() {
			
			@Override
			public void run() {
				mAdapter.notifyDataSetChanged();
			}
		});
		
	}

	@Override
	public byte getType() {
		return NewContactsDataObserver.TYPE_SIXIN;
	}

	@Override
	public void onSynStart() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onBeginInsert(long uid, int isGroup) {
		ChatDataHelper.getInstance().deleteChatMessageByGroupId(uid);
	}

	Runnable mDismissDialog = new Runnable() {
		public void run() {
			SystemUtil.log("dis", "dismiss");
			Log.v("qwe", "dismiss");
			 if(mDialog != null && mDialog.isShowing()) {
                 mDialog.dismiss();
             }
		}
	};
	Runnable mShowDialog = new Runnable() {
		public void run() {
			SystemUtil.log("dis", "show");
			 if(mDialog != null&& !mDialog.isShowing()) {
                 mDialog.show();
             }
		}
	};
	Runnable mUpdateTime = new Runnable() {
		public void run() {
			 mHolder.mSynRecordInfo.setText("上次获取时间" + getTimeFromLongToString(mTime));
		}
	};
	private long mTime = 0L;
	@Override
	public void onSynOver(long uid, int isGroup, boolean isSuccess,final long time,int count) {
		Log.v("qwe", "onSynOver");
		Log.v("qwe", "mToChatId = " + mToChatId + "......" + "uid = " + uid);
		Log.v("qwe", "mGroupId = " + mGroupId + "......" + "uid = " + uid);
//		ThreadPool.obtain().executeMainThread(mDismissDialog);
		if(isGroup == MESSAGE_ISGROUP.IS_SINGLE){
			if(mToChatId != uid){
				return ;
			}
		}else{
			if(mGroupId != uid){
				return ;
			}
		}
		
		if(isSuccess){
			Log.v("qwe", "isSuccess");
			if(count>0){
				mTime = time;
				ThreadPool.obtain().executeMainThread(mUpdateTime);
				ThreadPool.obtain().executeMainThread(mDismissDialog);
				 CommonUtil.toast(
						 RenrenChatApplication.getAppResources().getString(R.string.ChatMessagesActivity_java_22) 
						 + count + 
						 RenrenChatApplication.getAppResources().getString(R.string.ChatMessagesActivity_java_23));
                 
			}else{
				ThreadPool.obtain().executeMainThread(mDismissDialog);
				 CommonUtil.toast(RenrenChatApplication.getAppResources().getString(R.string.chatmessages_layout_20));
			}
			
		}else{
			Log.v("qwe", "failed");
			ThreadPool.obtain().executeMainThread(mDismissDialog);
			CommonUtil.toast(RenrenChatApplication.getAppResources().getString(R.string.ChatMessagesActivity_java_24));
		}
		isSynChatMessageNow = false;
	}
}
