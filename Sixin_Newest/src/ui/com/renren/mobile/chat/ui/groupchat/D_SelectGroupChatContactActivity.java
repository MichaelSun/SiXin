package com.renren.mobile.chat.ui.groupchat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.common.manager.LoginManager;
import com.common.network.AbstractNotSynRequest;
import com.common.network.AbstractNotSynRequest.OnDataCallback;
import com.common.statistics.BackgroundUtils;
import com.common.statistics.Htf;
import com.common.utils.PinyinSearch;
import com.common.utils.SearchAble;
import com.core.util.SystemService;
import com.core.util.ViewMapUtil;
import com.core.util.ViewMapping;
import com.renren.mobile.chat.R;
import com.renren.mobile.chat.RenrenChatApplication;
import com.renren.mobile.chat.actions.models.CreateRoomModel;
import com.renren.mobile.chat.actions.models.RoomInfoModelWarpper;
import com.renren.mobile.chat.actions.requests.RequestConstructorProxy;
import com.renren.mobile.chat.activity.RenRenChatActivity;
import com.renren.mobile.chat.base.model.ChatBaseItem;
import com.renren.mobile.chat.base.util.SystemUtil;
import com.renren.mobile.chat.base.views.NotSynImageView;
import com.renren.mobile.chat.base.views.NotSynImageView.DownloadImageAbleListener;
import com.renren.mobile.chat.dao.ContactsDAO;
import com.renren.mobile.chat.dao.DAOFactoryImpl;
import com.renren.mobile.chat.dao.IContactsDAO.OnContactsDownloadListener;
import com.renren.mobile.chat.newsfeed.NewsFeedWarpper;
import com.renren.mobile.chat.ui.BaseActivity;
import com.renren.mobile.chat.ui.chatsession.ChatSessionDataModel;
import com.renren.mobile.chat.ui.chatsession.ChatSessionManager;
import com.renren.mobile.chat.ui.contact.C_ContactsData;
import com.renren.mobile.chat.ui.contact.C_LetterBar;
import com.renren.mobile.chat.ui.contact.C_LetterBar.OnLetterSelectListener;
import com.renren.mobile.chat.ui.contact.ContactModel;
import com.renren.mobile.chat.ui.contact.ContactOnTouchListener;
import com.renren.mobile.chat.ui.contact.RoomInfosData;
import com.renren.mobile.chat.ui.photo.PhotoNew;
import com.renren.mobile.chat.util.ChatMessageSender;
import com.renren.mobile.chat.view.BaseTitleLayout;

/**
 * 
 * @author rubin.dong@renren-inc.com
 * @date 
 * 选择联系人界面
 *
 */

public class D_SelectGroupChatContactActivity extends BaseActivity  implements 
	OnLetterSelectListener, TextWatcher,
	OnScrollListener,DownloadImageAbleListener {
	String mStr = "";
	String lastStr = "";
	/**
	 * add by xiangchao.fan 转发信息
	 */
	private int mGroupNumber = 40; 
	private NewsFeedWarpper mNewsFeed;
	private String mFilePath = null;
    private SelectGroupAdapter adapter;
    private List<ContactModel> mGroupList = new ArrayList<ContactModel>();
    private Map<Long, Boolean> mTempRoomList = new HashMap<Long, Boolean>();
	private boolean mIsForward = false;
	ContactsDAO mContactsDAO = null;
	D_SelectContactsScreenHolder mHolder = new D_SelectContactsScreenHolder();
	ArrayList<ContactModel> mSelectedContact = new ArrayList<ContactModel>();
	ContactModel contactModel = new ContactModel();
	int mDataType = -1;
	ContactOnTouchListener mListViewTouchListener;
	Context context = D_SelectGroupChatContactActivity.this;
	D_SelectContactAdapter mAdapter_All;
	D_SelectContactAdapter mAdapter_Common;
	D_SelectContactAdapter mAdapter_Search;
	MyGridViewAdapter mGridViewAdapter;
	BaseTitleLayout mBaseTitle;
	int mComeFrom;
	long[] idList;
	long groupId;
	long mToChatId;
	private ProgressDialog mDialog;
	private int mSelectedNum;
	boolean isSelect = false;
    private Handler scrollHandlder = new Handler();
	List<ContactModel> mMemberList = new ArrayList<ContactModel>();
    int mGridViewItemDipWidth;
	private int search_laylout_pading_left = 0;
	private int search_laylout_pading_top = 0;
	private int search_laylout_pading_right_general = 0;
	private int search_laylout_pading_right_special = 0;
	private int search_laylout_pading_bottom = 0;
    /**
     * 用来控制title中切换常用和全部联系人的插件
     */
    private D_ContactTitleSelectHolder contactTitleSelectHolder = new D_ContactTitleSelectHolder();
	public static interface COMEFROM{
		int MAINFRAGMENT_ACTIVITY = 0;
		int GROUP_CHAT_MESSAGE = 1;
        int FORWARD_CHAT_MESSAGE = 2;
		int SING_CHAT_MESSAGE = 3;
		int CHAT_FEED_REBOLG = 4;
	}
	public static  interface PARAMS{
		String COMEFROM = "COMEFROM";
		String GROUP_MEMBER = "GROUP_MEMBER";
		String BUNDLE = "BUNDLE";
		String GROUP_ID = "GROUP_ID";
		String TO_CHAT_ID = "TO_CHAT_ID";
	}

    public static  interface CHANGE_GRID_FROM{
        int LIST_CLICK = 0;
        int GRID_CLICK = 1;
        int INIT = 2;
    }

	public static interface REQUEST_CODE{
		int SELECTCONTACT = 0;
		int SELECT_GROUP = 1;
	}
	/*
	 * 弹出Dialog
	 */
	private void showDialog(String text) {
		mDialog.setMessage(text);
		mDialog.show();
	}
	/*
	 * 关闭dialog
	 */
	private void dismissDialog(){
		if(mDialog.isShowing()){
			mDialog.dismiss();
		}
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		mNewsFeed = (NewsFeedWarpper) getIntent().getSerializableExtra(RenRenChatActivity.PARAM_NEED.TO_CHAT_FEED_MODEL);
		mFilePath =  getIntent().getStringExtra(RenRenChatActivity.PARAM_NEED.FORWARD_FILE_PATH);
		mIsForward = getIntent().getBooleanExtra(RenRenChatActivity.PARAM_NEED.IS_GET_MESSAGE, false);
		RenrenChatApplication.pushStack(this);
		mDialog = new ProgressDialog(D_SelectGroupChatContactActivity.this);
        mGridViewItemDipWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 49, getResources().getDisplayMetrics());
		initData();
		initView();
		initTitle();
		initSearch();
	}
	private void initData() {
		search_laylout_pading_left = (int)context.getResources().getDimension(R.dimen.search_layout_left);
		search_laylout_pading_top = (int)context.getResources().getDimension(R.dimen.search_layout_top);
		search_laylout_pading_right_general = (int)context.getResources().getDimension(R.dimen.search_layout_right_general);
		search_laylout_pading_right_special = (int)context.getResources().getDimension(R.dimen.search_layout_right_special);
		search_laylout_pading_bottom = (int)context.getResources().getDimension(R.dimen.search_layout_bottom);
		mContactsDAO = DAOFactoryImpl.getInstance().buildDAO(ContactsDAO.class);
//		C_ContactsData.getInstance().registorObserver(this);
		mComeFrom = getIntent().getIntExtra(PARAMS.COMEFROM, 0);
		switch (mComeFrom) {
		case COMEFROM.GROUP_CHAT_MESSAGE:
			idList = getIntent().getLongArrayExtra(PARAMS.GROUP_MEMBER);
			groupId = getIntent().getLongExtra(PARAMS.GROUP_ID, 0);
            for(int i=0; i<idList.length; i++){
                ContactModel contactModel = C_ContactsData.getInstance().getSiXinContact(idList[i], null);
                if(contactModel != null && contactModel.getmUserId() != 0){
                    mMemberList.add(contactModel);
                }
            }
			break;
		case COMEFROM.SING_CHAT_MESSAGE:
			mToChatId = getIntent().getLongExtra(PARAMS.TO_CHAT_ID, 0);
//			ContactModel contactModel = C_ContactsData.getInstance().getContactInfoFromLocal(mToChatId);
			ContactModel contactModel = C_ContactsData.getInstance().getSiXinContact(mToChatId, null);
			mMemberList.add(contactModel);
			break;
		}
	}

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if(requestCode == REQUEST_CODE.SELECT_GROUP) {
//            switch (resultCode) {
//                case RESULT_OK:
//                    if(null != data) {
//                        RenRenChatActivity.show(context, (RenRenChatActivity.CanTalkable)data.getSerializableExtra(RenRenChatActivity.PARAM_NEED.TO_CHAT_USER_MODEL),
//                                (NewsFeedWarpper)data.getSerializableExtra(RenRenChatActivity.PARAM_NEED.TO_CHAT_FEED_MODEL),
//                                data.getStringExtra(RenRenChatActivity.PARAM_NEED.FORWARD_FILE_PATH),
//                                true,RenrenChatApplication.sForwardMessage);
//                        finish();
//                    }
//                    break;
//                case RESULT_CANCELED:
//                    break;
//            }
//        }
//        super.onActivityResult(requestCode, resultCode, data);
//    }

    private void initView() {

		setContentView(R.layout.d_select_contacts);
		ViewMapUtil.getUtil().viewMapping(mHolder, getWindow().getDecorView());
        View contactTitle = SystemService.sInflaterManager.inflate(R.layout.d_contact_title, null);
        mHolder.mSelectGroupBtn = (FrameLayout) contactTitle.findViewById(R.id.select_group_btn);
        mHolder.mSelectGroupNext = (ImageView) contactTitle.findViewById(R.id.select_group_next);
        mHolder.mSelectGroupText = (TextView) contactTitle.findViewById(R.id.select_group_text);
        View contactSearch = SystemService.sInflaterManager.inflate(R.layout.search_edit_text, null);
        mHolder.mSearchLayout = (FrameLayout) contactSearch.findViewById(R.id.search_layout);
        mHolder.mSearchEditText = (EditText) contactSearch.findViewById(R.id.search_edit);
        mHolder.mImageDel = (ImageView) contactSearch.findViewById(R.id.search_del);
//        mHolder.mSearchDivider = (ImageView) contactSearch.findViewById(R.id.search_divider);
        
		mHolder.mLetterBar.setOnLetterSelectListener(this);
        mHolder.mHorizontalScrollView.setSmoothScrollingEnabled(true);
		mAdapter_All = new D_SelectContactAdapter(C_ContactsData.TYPE.SIXIN_CONTACTS, context , true);
        mAdapter_Common = new D_SelectContactAdapter(C_ContactsData.TYPE.COMMON_CONTACTS,context, false);
		mAdapter_Search = new D_SelectContactAdapter(C_ContactsData.TYPE.SEARCH_CONTACTS, context, false);
		mGridViewAdapter = new MyGridViewAdapter();
//		C_ContactsData.getInstance().loadContacts(false);
        int[] indexs = {};
        Log.v("fafa", "mComeFrom = " + mComeFrom);
		if(mComeFrom == COMEFROM.GROUP_CHAT_MESSAGE || mComeFrom == COMEFROM.SING_CHAT_MESSAGE){
			mDataType = C_ContactsData.TYPE.SIXIN_CONTACTS;
			mAdapter_All.setData(mMemberList);
            mAdapter_Common.setData(mMemberList);
            mAdapter_Common.setDataList(C_ContactsData.getInstance().get(C_ContactsData.TYPE.COMMON_CONTACTS), true);
			mAdapter_All.setDataList(C_ContactsData.getInstance().get(C_ContactsData.TYPE.SIXIN_CONTACTS), true);
//            mHolder.mSearchDivider.setVisibility(View.GONE);
            C_ContactsData.getInstance().getFilterContacts(C_ContactsData.TYPE.SIXIN_CONTACTS, mMemberList);
            indexs = C_ContactsData.getInstance().getIndexs(C_ContactsData.TYPE.FILTER_CONTACTS);
		}else if(mComeFrom == COMEFROM.MAINFRAGMENT_ACTIVITY || mComeFrom == COMEFROM.FORWARD_CHAT_MESSAGE){
//			mHolder.mSelectGroupBtn.setVisibility(View.VISIBLE);
//            mHolder.mSearchDivider.setVisibility(View.GONE);
//            mHolder.mSelectGroupBtn.setOnClickListener(new OnClickListener() {
//				@Override
//				public void onClick(View v) {
//					SelectGroupActivity.show(SelectGroupChatContactActivity.this, mNewsFeed, mFilePath, REQUEST_CODE.SELECT_GROUP);
//				}
//			});
			mAdapter_All.setDataList(C_ContactsData.getInstance().get(C_ContactsData.TYPE.SIXIN_CONTACTS), false);
            mAdapter_Common.setDataList(C_ContactsData.getInstance().get(C_ContactsData.TYPE.COMMON_CONTACTS), false);
            
//            int [] sourceIndexs = C_ContactsData.getInstance().getIndexs(C_ContactsData.TYPE.SIXIN_CONTACTS);
//            int[] targetIndexs = new int[sourceIndexs.length];
//            System.arraycopy(sourceIndexs, 0, targetIndexs, 0, sourceIndexs.length);
//            indexs = targetIndexs;
            indexs = C_ContactsData.getInstance().getIndexs(C_ContactsData.TYPE.SIXIN_CONTACTS);
            adapter = new SelectGroupAdapter(D_SelectGroupChatContactActivity.this, mHolder.mContactsListView);
            setForwardList();
            C_ContactsData.getInstance().resetListFlag(mGroupList);
            adapter.setContacts(mGroupList);
		}else if(mComeFrom == COMEFROM.CHAT_FEED_REBOLG){
//			mHolder.mSearchDivider.setVisibility(View.GONE);
			mAdapter_All.setDataList(C_ContactsData.getInstance().get(C_ContactsData.TYPE.SIXIN_CONTACTS), false);
            mAdapter_Common.setDataList(C_ContactsData.getInstance().get(C_ContactsData.TYPE.COMMON_CONTACTS), false);
            indexs = C_ContactsData.getInstance().getIndexs(C_ContactsData.TYPE.SIXIN_CONTACTS);
            indexs[C_LetterBar.INDEX_SEARCH_CHAR ] = 0;
            
            adapter = new SelectGroupAdapter(D_SelectGroupChatContactActivity.this, mHolder.mContactsListView);
            setForwardList();
            C_ContactsData.getInstance().resetListFlag(mGroupList);
            adapter.setContacts(mGroupList);
			
		}
//        indexs[C_LetterBar.INDEX_GROUP_CHAR] = -1;
        mHolder.mLetterBar.initView(indexs);
        mHolder.mSelectGroupDoneButton.setEnabled(false);
		mAdapter_Search.setDataList(C_ContactsData.getInstance().get(C_ContactsData.TYPE.SEARCH_CONTACTS), false);
        mHolder.mContactsListView.addHeaderView(contactSearch);
        mHolder.mContactsListView.setScrollingCacheEnabled(false);
        mHolder.mContactsListView.setDrawingCacheEnabled(false);
        mHolder.mContactsListView.setAlwaysDrawnWithCacheEnabled(false);
        mHolder.mContactsListView.setWillNotCacheDrawing(true);
//        mHolder.mContactsListView.addHeaderView(contactTitle);
		mHolder.mContactsListView.setDivider(null);
		mHolder.mContactsListView.setOnScrollListener(this);
		contactModel.mLastView = true;
		mSelectedContact.add(contactModel);
		changeGridView(CHANGE_GRID_FROM.INIT);
		mHolder.mGridView.setAdapter(mGridViewAdapter);
		mListViewTouchListener = new ContactOnTouchListener(
				mHolder.mSearchEditText, context);
		mHolder.mContactsListView.setOnTouchListener(mListViewTouchListener);
//		C_ContactsData.getInstance().loadContacts(false);		
		mHolder.mImageDel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mHolder.mSearchEditText.setText("");
			}
		});
		/**按回车键后的处理事件*/ 
		mHolder.mSearchEditText.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (KeyEvent.KEYCODE_ENTER == keyCode) {
					return true;
				}
				return false; 
			}
		});
		mHolder.mGridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
                selectGridStateChanged(arg2);
			}
		});
        initDoneButton();
	}

    private void initDoneButton() {

        if(mComeFrom == COMEFROM.GROUP_CHAT_MESSAGE) {

            mHolder.mSelectGroupDoneButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(mSelectedNum > 0){
                        final List<Long> inviteIds = new LinkedList<Long>();
                        for(ContactModel m:mSelectedContact){
                            if(!m.mLastView){
                                inviteIds.add(m.mUserId);
                            }
                        }
                        showDialog(getResources().getString(R.string.SelectGroupChatContactActivity_java_4));		//SelectGroupChatContactActivity_java_4=正在邀请...;
                        mHolder.mSelectGroupDoneButton.setEnabled(false);
                        AbstractNotSynRequest<RoomInfoModelWarpper> abstractNotSynRequest =  RequestConstructorProxy.getInstance().invite(LoginManager.getInstance().mLoginInfo.mUserId,groupId, inviteIds);
                        abstractNotSynRequest.setCallback(new OnDataCallback<RoomInfoModelWarpper>() {
                            @Override
                            public void onSuccess() {

                                dismissDialog();
                                RoomInfoModelWarpper roomInfo = RoomInfosData.getInstance().getRoomInfo(groupId);
                                if(roomInfo != null){
                                    for(ContactModel model : mSelectedContact){
                                        if(!model.mLastView){
                                            roomInfo.addMember(model);
                                        }
                                    }
                                    roomInfo.updateToDB();
                                }
                                setResult(RESULT_OK);
                                finish();
                            }
                            @Override
                            public void onSuccessRecive(RoomInfoModelWarpper data) {
                            }
                            @Override
                            public void onError(int errorCode, final String errorMsg) {

                                RenrenChatApplication.sHandler.post(new Runnable() {
                                    public void run() {
                                        SystemUtil.toast(errorMsg);
                                        mHolder.mSelectGroupDoneButton.setEnabled(true);
                                        dismissDialog();
                                    }
                                });
                            }
                        });
                        ChatMessageSender.getInstance().sendRequestToNet(abstractNotSynRequest);

                    }
                }
            });
        } else {
            mHolder.mSelectGroupDoneButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {

                    if((mComeFrom == COMEFROM.MAINFRAGMENT_ACTIVITY || mComeFrom == COMEFROM.CHAT_FEED_REBOLG ) && mSelectedContact.size() == 2){
                        mSelectedContact.remove(contactModel);
                        RenRenChatActivity.show(context, mSelectedContact.get(0), mNewsFeed, mFilePath, mIsForward,RenrenChatApplication.sForwardMessage);
                        finish();
                        return;
                    }

                    if(mSelectedContact.size() > 1){
                        final List<Long> inviteIds = new LinkedList<Long>();
                        for(ContactModel m:mSelectedContact){
                            if(!m.mLastView){
                                inviteIds.add(m.mUserId);
                            }
                        }
                        if(mComeFrom == COMEFROM.SING_CHAT_MESSAGE) {
                            inviteIds.add(mToChatId);
                        }

                        showDialog(getResources().getString(R.string.SelectGroupChatContactActivity_java_3));		//SelectGroupChatContactActivity_java_3=正在创建群聊...;
                        mHolder.mSelectGroupDoneButton.setEnabled(false);
                        AbstractNotSynRequest request = RequestConstructorProxy.getInstance().createRoom(
                        		LoginManager.getInstance().mLoginInfo.mUserId, inviteIds).setCallback(new OnDataCallback<CreateRoomModel>() {
                            @Override
                            public void onSuccess() {}
                            @Override
                            public void onSuccessRecive(final CreateRoomModel data) {
                                RenrenChatApplication.sHandler.post(new Runnable() {
                                    public void run() {
                                        dismissDialog();
                                        inviteIds.add(0, LoginManager.getInstance().mLoginInfo.mUserId);
                                        RoomInfosData.getInstance().addRoomInfo(data.mRoomId,inviteIds, data.mVersion, data.mSubject);
                                        RoomInfoModelWarpper model = RoomInfosData.getInstance().getRoomInfo(data.mRoomId);
                                        RenRenChatActivity.show(context, model, mNewsFeed,mFilePath, mIsForward,RenrenChatApplication.sForwardMessage);
                                        finish();
                                        
                                        //统计
                                        handleStatistics();
                                    }
                                });
                            }
                            @Override
                            public void onError(int errorCode,final String errorMsg) {
                                RenrenChatApplication.sHandler.post(new Runnable() {
                                    public void run() {
                                        dismissDialog();
                                        mHolder.mSelectGroupDoneButton.setEnabled(true);
                                        SystemUtil.toast(errorMsg);
                                    }
                                });
                            }
                        });
                        ChatMessageSender.getInstance().sendRequestToNet(request);

                    }
                }
            });

        }

    }



    private void contactListSatateChanged(ContactModel model, ImageView select_contact_view) {

        int selectedContactNum = getNowSelectTotalNum();

        if(!model.mSelected){
            mSelectedNum++;
            if(selectedContactNum + 1 <= mGroupNumber){
                model.mSelected = true;
                /*
                                 * 增加已选联系人 mSelectedContact删除lastView对象在添加正在选择的联系人  然后添加lastView对象
                                 * 该逻辑为mGridView服务
                                 */
                mSelectedContact.remove(contactModel);
                mSelectedContact.add(model);
                if(selectedContactNum + 1 != mGroupNumber){
                    mSelectedContact.add(contactModel);
                }
                select_contact_view.setSelected(true);
                resetDoneButton();
                changeGridView(CHANGE_GRID_FROM.LIST_CLICK);
            }else{
                mSelectedNum--;
                Toast.makeText(context, RenrenChatApplication.getmContext().getResources().getString(R.string.SelectGroupChatContactActivity_java_1), 1).show();		//SelectGroupChatContactActivity_java_1=群聊人数已达上限;
            }
        }else {
            mSelectedNum--;
            model.mSelected = false;
            if(selectedContactNum - 1 == mGroupNumber - 1){
                mSelectedContact.add(contactModel);
            }
            mSelectedContact.remove(model);
            select_contact_view.setSelected(false);
            resetDoneButton();
            changeGridView(CHANGE_GRID_FROM.LIST_CLICK);
        }
        if(mSelectedNum == 0){
            mHolder.mSelectGroupDoneButton.setEnabled(false);
        } else {
            mHolder.mSelectGroupDoneButton.setEnabled(true);
        }
    }

    private void selectGridStateChanged(int index) {

        int selectedContactNum = getNowSelectTotalNum();

        ContactModel cm = mSelectedContact.get(index);
        if(cm.mLastView || cm.mContactName == null){
        }else{
            mSelectedNum--;
            cm.mSelected = false;
            mAdapter_All.notifyDataSetChanged();
            mAdapter_Common.notifyDataSetChanged();
            mAdapter_Search.notifyDataSetChanged();
            mSelectedContact.remove(cm);
            if(selectedContactNum == mGroupNumber){
                mSelectedContact.add(contactModel);
            }
            changeGridView(CHANGE_GRID_FROM.GRID_CLICK);
            resetDoneButton();
        }
        if(mSelectedNum == 0){
            mHolder.mSelectGroupDoneButton.setEnabled(false);
        }
    }

    private int getNowSelectTotalNum() {
        switch (mComeFrom) {
        case COMEFROM.GROUP_CHAT_MESSAGE:
            return mSelectedNum + mMemberList.size() + 1;
        case COMEFROM.SING_CHAT_MESSAGE:
            return mSelectedNum + 2;
        case COMEFROM.MAINFRAGMENT_ACTIVITY:
        case COMEFROM.FORWARD_CHAT_MESSAGE:
            return mSelectedNum + 1;
        }
        return 0;
    }

    private void changeGridView(int fromClick) {
        mHolder.mGridView.setNumColumns(mSelectedContact.size());
        LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams)mHolder.mGridView.getLayoutParams();
        linearParams.width = mSelectedContact.size() * mGridViewItemDipWidth;
        mHolder.mGridView.setLayoutParams(linearParams);
        if(fromClick == CHANGE_GRID_FROM.GRID_CLICK || fromClick == CHANGE_GRID_FROM.LIST_CLICK) {
            mGridViewAdapter.notifyDataSetInvalidated();
        }
        if(fromClick == CHANGE_GRID_FROM.LIST_CLICK) {
            scrollHandlder.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mHolder.mHorizontalScrollView.smoothScrollTo(mHolder.mGridView.getRight(), 0);
                }
            }, 10);
        }
    }

	private void resetDoneButton() {
        if(mSelectedNum != 0) {
            mHolder.mSelectGroupDoneButton.setText(getResources().getString(R.string.VoiceOnClickListenner_java_3)
                    + "(" + mSelectedNum + ")");
        } else {
            mHolder.mSelectGroupDoneButton.setText(getResources().getString(R.string.VoiceOnClickListenner_java_3));
        }
	}
	private void initSearch() {
		mHolder.mSearchEditText.addTextChangedListener(this);
	}
	@Override
	public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_SEARCH:
			hideInputMethod();
			return true;
        case KeyEvent.KEYCODE_BACK:
            setResult(RESULT_CANCELED);
            finish();
            return true;
		default:
			return super.onKeyDown(keyCode, event);
		}
	}

	private void initTitle() {
		mBaseTitle = new BaseTitleLayout(D_SelectGroupChatContactActivity.this,mHolder.mLinearLayout);
		LinearLayout midTitleLayout = mBaseTitle.getMidTitleLayout();
		mBaseTitle.getTitleLeft().setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				setResult(RESULT_CANCELED);
                finish();
			}
		});
        midTitleLayout.removeAllViews();
        View midTitle = mBaseTitle.getmContactTitleLayout();
        ViewMapUtil.getUtil().viewMapping(contactTitleSelectHolder, midTitle);
        contactTitleSelectHolder.contactTitleMiddle.setVisibility(View.VISIBLE);
		contactTitleSelectHolder.contactTitleLeftBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                hideInputMethod();
                contactTitleSelectHolder.setLeftBtnSelect();
                mHolder.mContactsListView.setAdapter(mAdapter_Common);
                mDataType = C_ContactsData.TYPE.COMMON_CONTACTS;
                resetListOtherView(View.GONE);
                resetListOnItemClickLeftAndMiddle();
                List<ContactModel> contactModels = mAdapter_Common.getDataList();
                if(contactModels != null && contactModels.size() == 0){
                	mHolder.mNoDataLayout.setVisibility(View.VISIBLE);
                	mHolder.mNoDataTextView.setText("您还没有常用联系人");
                }else{
                	mHolder.mNoDataLayout.setVisibility(View.GONE);
                }
            }
        });
        contactTitleSelectHolder.contactTitleMiddleBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                hideInputMethod();
                contactTitleSelectHolder.setMiddleBtnSelect();
                mHolder.mSearchLayout.setPadding(search_laylout_pading_left,search_laylout_pading_top,search_laylout_pading_right_special,search_laylout_pading_bottom);
                mHolder.mContactsListView.setAdapter(mAdapter_All);
                if(mComeFrom == COMEFROM.MAINFRAGMENT_ACTIVITY || mComeFrom == COMEFROM.CHAT_FEED_REBOLG) {
                    mDataType = C_ContactsData.TYPE.SIXIN_CONTACTS;
                } else {
                    mDataType = C_ContactsData.TYPE.FILTER_CONTACTS;
                }
                resetListOtherView(View.VISIBLE);
                resetListOnItemClickLeftAndMiddle();
                List<ContactModel> contactModels = mAdapter_All.getDataList();
                if(contactModels != null && contactModels.size() == 0){
                	mHolder.mNoDataLayout.setVisibility(View.VISIBLE);
                	mHolder.mNoDataTextView.setText("您还没有私信联系人");
                }else{
                	mHolder.mNoDataLayout.setVisibility(View.GONE);
                }
            }
        });
        if(mComeFrom == COMEFROM.CHAT_FEED_REBOLG || mComeFrom == COMEFROM.MAINFRAGMENT_ACTIVITY) {
            contactTitleSelectHolder.setRightBtnVisible();
            contactTitleSelectHolder.contactTitleRightBtn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    hideInputMethod();
                    contactTitleSelectHolder.setRightBtnSelect();
                    mHolder.mContactsListView.setAdapter(adapter);
                    resetListOtherView(View.GONE);
                    resetListOnItemClickRight();
                    
                    if(mGroupList != null && mGroupList.size() == 0){
                    	mHolder.mNoDataLayout.setVisibility(View.VISIBLE);
                    	mHolder.mNoDataTextView.setText("您还没有群组");
                    }else{
                    	mHolder.mNoDataLayout.setVisibility(View.GONE);
                    }
                }
            });
        }
        List<ContactModel> contactModels = C_ContactsData.getInstance().get(C_ContactsData.TYPE.COMMON_CONTACTS);
        if(contactModels != null && contactModels.size()>0){
        	contactTitleSelectHolder.setLeftBtnSelect();
            contactTitleSelectHolder.contactTitleLeftBtn.performClick();
        }else{
        	contactTitleSelectHolder.setMiddleBtnSelect();
            contactTitleSelectHolder.contactTitleMiddleBtn.performClick();
        }
	}

    private void resetListOnItemClickLeftAndMiddle() {
        mHolder.mContactsListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int position,
                                    long arg3) {
                if(position == 0) {
                    return;
                }
                ImageView select_contact_view = (ImageView)view.findViewById(R.id.select_contact_view);
                List<ContactModel> contactModels = C_ContactsData.getInstance().getFilterContacts(mDataType, mMemberList);
                ContactModel model = contactModels.get(position - 1);
                contactListSatateChanged(model, select_contact_view);
            }
        });
        mHolder.mSelectContactLayout.setVisibility(View.VISIBLE);
    }

    private void resetListOnItemClickRight() {
        mHolder.mContactsListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int position,
                                    long arg3) {
                if(position == 0) {
                    return;
                }
                ContactModel model = (ContactModel)adapter.getItem(position - 1);
                RoomInfoModelWarpper roomInfo = RoomInfosData.getInstance().getRoomInfo(model.getmUserId());
                if(null == roomInfo){
                    SystemUtil.toast(getResources().getString(R.string.MultiChatForwardScreen_java_4));		//MultiChatForwardScreen_java_4=该群已经解散;
                    return;
                }
                if(mIsForward) {
                    createDialog(roomInfo).show();
                } else {
                    RenRenChatActivity.show(context, roomInfo, mNewsFeed,mFilePath, mIsForward,RenrenChatApplication.sForwardMessage);
                    //统计
                    handleStatistics();
                }
            }
        });
        mHolder.mSelectContactLayout.setVisibility(View.GONE);
    }

    private void resetListOtherView(int visibility) {
        mHolder.mSearchEditText.setText("");
        results = null;
        mHolder.mLetterBar.setVisibility(visibility);
        mHolder.mSearchLayout.setVisibility(visibility);
        if(visibility == View.VISIBLE) {
            mHolder.mContactsListView.setDrawAlpheBar(true);
        } else {
            mHolder.mContactsListView.setDrawAlpheBar(false);
        }
    }

	@Override
	public void afterTextChanged(Editable arg0) {
	}
	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
	}
	List<SearchAble> results = null;
	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		mHolder.mNoSearchData.setVisibility(View.GONE);
		String searchKey = s + "";
		if (searchKey.trim().length() == 0) {
			if (this.mDataType == C_ContactsData.TYPE.SEARCH_CONTACTS) {
				mHolder.mSearchLayout.setPadding(search_laylout_pading_left,search_laylout_pading_top,search_laylout_pading_right_special,search_laylout_pading_bottom);
				mHolder.mContactsListView.setAdapter(mAdapter_All);
                if(mComeFrom == COMEFROM.MAINFRAGMENT_ACTIVITY  || mComeFrom == COMEFROM.FORWARD_CHAT_MESSAGE) {
				    mDataType = C_ContactsData.TYPE.SIXIN_CONTACTS;
                } else {
                    mDataType = C_ContactsData.TYPE.FILTER_CONTACTS;
                }
			}
			if (mHolder.mLetterBar != null
					&& mHolder.mLetterBar.getVisibility() == View.GONE) {
				mHolder.mLetterBar.setVisibility(View.VISIBLE);
			}
			mHolder.mContactsListView.setDrawAlpheBar(true);
			mHolder.mImageDel.setVisibility(View.GONE);
//			if(mComeFrom == COMEFROM.MAINFRAGMENT_ACTIVITY){
//				mHolder.mSelectGroupBtn.setVisibility(View.VISIBLE);
//                mHolder.mSearchDivider.setVisibility(View.VISIBLE);
//			}
			return;
		} else {
			if (mHolder.mLetterBar != null
					&& mHolder.mLetterBar.getVisibility() != View.GONE) {
				mHolder.mLetterBar.setVisibility(View.GONE);
			}
			mHolder.mSearchLayout.setPadding(search_laylout_pading_left,search_laylout_pading_top,search_laylout_pading_right_general,search_laylout_pading_bottom);
			mHolder.mImageDel.setVisibility(View.VISIBLE);
			mHolder.mContactsListView.setAdapter(mAdapter_Search);
			mHolder.mContactsListView.setDrawAlpheBar(false);
			this.mDataType = C_ContactsData.TYPE.SEARCH_CONTACTS;
			if(searchKey.length() >= 20){
				SystemUtil.toast(RenrenChatApplication.getmContext().getResources().getString(R.string.D_AttentionScreen_java_2));		//D_AttentionScreen_java_2=最多输入20个字; 
			}
			 mHolder.mSearchEditText.requestFocus();
//			if(mComeFrom == COMEFROM.MAINFRAGMENT_ACTIVITY){
//                mHolder.mSelectGroupBtn.setVisibility(View.GONE);
//                mHolder.mSearchDivider.setVisibility(View.GONE);
//			}
		}
		List<ContactModel>	tmpModels=C_ContactsData.getInstance().getFilterContacts(C_ContactsData.TYPE.SIXIN_CONTACTS, mMemberList);
		List<SearchAble> beforeSearch = new ArrayList<SearchAble>(tmpModels);
		ContactModel.bleachList(results);
		PinyinSearch.mapSearch(searchKey,beforeSearch, results);
		results=beforeSearch;
		if(results.size()==0){
			mHolder.mNoSearchData.setVisibility(View.VISIBLE);
		}
		tmpModels=new ArrayList<ContactModel>();
		for (SearchAble search : beforeSearch) {
			tmpModels.add((ContactModel) search);
		}
		C_ContactsData.getInstance().addToSearchListAndNotify(tmpModels);
		mAdapter_Search.setDataList(tmpModels, false);
//			
	}
	@Override
	public void onSelect(char c) {
		mHolder.mPopText.setVisibility(View.VISIBLE);
        if(c == C_LetterBar.INDEX_SEARCH_CHAR) {
            mHolder.mPopText.setText(getResources().getString(R.string.contact_index_search));
            mHolder.mContactsListView.setSelection(0);
            return;
        }
//        if(c == C_LetterBar.CHAR_GROUP) {
//            mHolder.mPopText.setText(c+"");
//            mHolder.mContactsListView.setSelection(1);
//            return;
//        }
		int index = getProximalIndex(c);
		if(SystemUtil.mDebug){
        	SystemUtil.errord("c="+c+"#index="+((index>>8)&0xffffff));
		}
		if(index!=-1){
			c=(char) (index&0xff);
			index=(index>>8)&0xffffff;
            mHolder.mPopText.setText(c+"");
//            Object object=mAdapter_All.getItem(index);
//            if(object instanceof ContactModel){
//            	ContactModel cm= (ContactModel)object;
//            	if(SystemUtil.mDebug){
//            		SystemUtil.errord("name="+cm.getmContactName());
//            	}
//            }
            mHolder.mContactsListView.setSelection(index);
		}else{
			mHolder.mPopText.setVisibility(View.GONE);
		}
		isSelect = true;
		if(this.mDataType == C_ContactsData.TYPE.SIXIN_CONTACTS || this.mDataType == C_ContactsData.TYPE.FILTER_CONTACTS){
			mAdapter_All.setSelectState(isSelect);
		} else if(this.mDataType == C_ContactsData.TYPE.SEARCH_CONTACTS) {
			mAdapter_Search.setSelectState(isSelect);
		}
	}
	private final int getProximalIndex(char c){ 
		if(SystemUtil.mDebug){
			SystemUtil.logd("c="+c);
		}
		int index=0;
		if(mDataType == C_ContactsData.TYPE.SIXIN_CONTACTS){
			index = C_ContactsData.getInstance().getIndex(c, C_ContactsData.TYPE.SIXIN_CONTACTS);
		}else if(mDataType == C_ContactsData.TYPE.FILTER_CONTACTS){
			index = C_ContactsData.getInstance().getIndex(c, C_ContactsData.TYPE.FILTER_CONTACTS);
		}
		if(SystemUtil.mDebug){
			SystemUtil.logd("index="+index);
		}
		if(index!=-1){
			return (index<<8)|c;
		}
//		char tmpchar=c;  
//		char A='A';
//		char Z='Z';
//		if(c == C_LetterBar.CHAR_OTHER){
//			c=(char) (Z+1);
//		}
//		for(char i=1;i<26;i++){
//			tmpchar=(char) (c-i);
//			if(tmpchar>=A&&tmpchar<=Z){
//				if(mDataType == C_ContactsData.TYPE.SIXIN_CONTACTS){
//					index = C_ContactsData.getInstance().getIndex(tmpchar, C_ContactsData.TYPE.SIXIN_CONTACTS);
//				}else if (mDataType == C_ContactsData.TYPE.FILTER_CONTACTS){
//					index = C_ContactsData.getInstance().getIndex(tmpchar, C_ContactsData.TYPE.FILTER_CONTACTS);
//				}
//				if(index!=-1){
//					return (index<<8)|tmpchar;
//				}
//			}
//			tmpchar=(char) (c+i);
//			if(tmpchar>=A&&tmpchar<=Z){
//				 if(mDataType == C_ContactsData.TYPE.SIXIN_CONTACTS){
//					index = C_ContactsData.getInstance().getIndex(tmpchar, C_ContactsData.TYPE.SIXIN_CONTACTS);
//				}else if (mDataType == C_ContactsData.TYPE.FILTER_CONTACTS){
//					index = C_ContactsData.getInstance().getIndex(tmpchar, C_ContactsData.TYPE.FILTER_CONTACTS);
//				}
//				if(index!=-1){
//					return (index<<8)|tmpchar;
//				}
//			}
//		}			
		return -1;
	}
	@Override
	public void onUnSelect() {
		mHolder.mPopText.setVisibility(View.GONE);
		isSelect = false;
		if(this.mDataType==C_ContactsData.TYPE.SIXIN_CONTACTS || this.mDataType==C_ContactsData.TYPE.FILTER_CONTACTS){
			mAdapter_All.setSelectState(isSelect);
		}
	}
	
	private class ViewHolder {

        @ViewMapping(ID = R.id.select_contact_head)
		public NotSynImageView head;

        @ViewMapping(ID = R.id.frameLayout)
		public FrameLayout frameLayout;

        @ViewMapping(ID = R.id.select_contact_head_click)
		public ImageView headClick;
	}
    public class MyGridViewAdapter extends BaseAdapter {
    	public MyGridViewAdapter() {
    	}
		@Override
		public int getCount() {
			return mSelectedContact.size();
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
				convertView = SystemService.sInflaterManager
						.inflate(R.layout.d_select_contact_gridview_item, null);
				ViewMapUtil.getUtil().viewMapping(holder, convertView);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			ContactModel model = mSelectedContact.get(position);
			holder.head.setDownloadAbleListener(D_SelectGroupChatContactActivity.this);
			holder.head.clear();
			if(model.mLastView){
				holder.headClick.setBackgroundDrawable(null);
				holder.head.setImageResource(R.drawable.select_contact_last_bg);
			}else{
				holder.head.addUrl(model.getmHeadUrl());
//				holder.frameLayout.setBackgroundResource(R.drawable.groupchat_contact_head_bg);
				holder.headClick.setBackgroundResource(R.drawable.member_head_selector);
			}
			return convertView;
		}
    }
	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
	}
	boolean isScrolling = false;
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if(scrollState==AbsListView.OnScrollListener.SCROLL_STATE_IDLE){
			isScrolling = false;
		}else{
			isScrolling = true;
		}
		if(this.mDataType==C_ContactsData.TYPE.SIXIN_CONTACTS ||
                (this.mDataType == C_ContactsData.TYPE.FILTER_CONTACTS && contactTitleSelectHolder.isRightBtnSelected())){
			mAdapter_All.setScrollState(isScrolling);
		}else if(this.mDataType == C_ContactsData.TYPE.COMMON_CONTACTS ||
                (this.mDataType == C_ContactsData.TYPE.FILTER_CONTACTS && contactTitleSelectHolder.isLeftBtnSelected())){
            mAdapter_Common.setScrollState(isScrolling);
        } else if(this.mDataType == C_ContactsData.TYPE.SEARCH_CONTACTS) {
			mAdapter_Search.setScrollState(isScrolling);
		}
	}
	@Override
	protected void onDestroy() {
		for(ContactModel model : mSelectedContact){
			model.mSelected = false;
		}
		RenrenChatApplication.popStack(this);
        if(idList != null && idList.length > 0) {
            idList = null;
        }
        if(mMemberList != null && mMemberList.size() > 0) {
            mMemberList.clear();
            mMemberList = null;
        }
        if(mSelectedContact != null && mSelectedContact.size() > 0) {
            mSelectedContact.clear();
            mSelectedContact = null;
        }
        if(results != null && results.size() > 0) {
            results.clear();
            results = null;
        }
        if(mGroupList !=null && mGroupList.size() > 0) {
            mGroupList.clear();
            mGroupList = null;
        }
        if(mTempRoomList != null && mTempRoomList.size() > 0) {
            mTempRoomList.clear();
            mTempRoomList = null;
        }
//        C_ContactsData.getInstance().unRegistorObserver(this);
        adapter = null;
        mAdapter_All = null;
        mAdapter_Common = null;
        mAdapter_Search = null;
        super.onDestroy();
	}

	@Override
	public boolean enable() {
		return !isScrolling;
	}
	@Override
	protected void finalize() throws Throwable {
		super.finalize();
	}




    /**增加选择群的代码*/


    private void setForwardList(){
        LinkedList<Object> linkedList = ChatSessionManager.getInstance().getSessionList();
        List<ContactModel> roomList = C_ContactsData.getInstance().getmSortGroupContacts();
        mGroupList.clear();
        mTempRoomList.clear();
        int size = linkedList.size();
        if(size == 0 && roomList.size() == 0){
//            TextView dataView = (TextView) mNoDataLayout.findViewById(R.id.data_textView);
//            TextView dataView1 = (TextView) mNoDataLayout.findViewById(R.id.data_textView1);
//            dataView.setText(RenrenChatApplication.getmContext().getResources().getString(R.string.SelectGroupActivity_java_2));		//SelectGroupActivity_java_2=暂无群组;
//            dataView1.setText("");
//            mNoDataLayout.setVisibility(View.VISIBLE);
        }else{
            for(int i=0;i<size;i++){
                ChatSessionDataModel chatSessionDataModle = (ChatSessionDataModel) linkedList.get(i);
                ContactModel model = null;
                ContactModel newModel = null;
                if(chatSessionDataModle.mIsGroup == ChatBaseItem.MESSAGE_ISGROUP.IS_SINGLE){
                }else{
                    RoomInfoModelWarpper tempRoom = RoomInfosData.getInstance().getRoomInfo(chatSessionDataModle.mGroupId);
                    if (null != tempRoom) {
                        newModel = new ContactModel();
                        newModel.setGroupType(ContactModel.Contact_group_type.MULTIPLE);
                        newModel.setmContactName(chatSessionDataModle.mName);
                        newModel.setmUserId(chatSessionDataModle.mGroupId);
                        mTempRoomList.put(chatSessionDataModle.mGroupId, true);
                    }
                }
                if(null != newModel){
                    mGroupList.add(newModel);
                }
            }
            for(ContactModel room:roomList){
                if(!mTempRoomList.containsKey(room.getUId())){
                    RoomInfoModelWarpper tempRoom = RoomInfosData.getInstance().getRoomInfo(room.getUId());
                    if (null != tempRoom) {
                        ContactModel model = new ContactModel();
                        model.setGroupType(ContactModel.Contact_group_type.MULTIPLE);
                        model.setmContactName(room.getName());
                        model.setmUserId(room.getUId());
                        if(null != model){
                            mGroupList.add(model);
                        }
                    }
                }
            }
        }
    }

    /**
     * 创建dialog
     * @param roomInfo
     * @return
     */
    private AlertDialog createDialog(final RoomInfoModelWarpper roomInfo){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        StringBuilder message = new StringBuilder("");
        message.append(getResources().getString(R.string.MultiChatForwardScreen_java_5) + roomInfo.getName() + "?");		//MultiChatForwardScreen_java_5=是否要将该信息转发到群:;
        builder.setMessage(message);
        builder.setPositiveButton(getResources().getString(R.string.MultiChatForwardScreen_java_6), new AlertDialog.OnClickListener() {		//MultiChatForwardScreen_java_6=确认;
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                RenRenChatActivity.show(context, roomInfo, mNewsFeed,mFilePath, mIsForward,RenrenChatApplication.sForwardMessage);
                finish();
                
                //统计
                handleStatistics();
            }
        });
        builder.setNegativeButton(RenrenChatApplication.getmContext().getResources().getString(R.string.ChatMessageWarpper_FlashEmotion_java_4), new AlertDialog.OnClickListener() {		//ChatMessageWarpper_FlashEmotion_java_4=取消;
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        return dialog;
    }
    
    /**
     * 转发图片
     * @param context
     * @param filePath
     * @param isForward @see RenrenChatActivity.PARAM_NEED.IS_GET_MESSAGE
     */
    public static void show(Context context, String filePath, boolean isForward){
    	Intent i = new Intent(context, D_SelectGroupChatContactActivity.class);
		i.putExtra(PARAMS.COMEFROM, COMEFROM.CHAT_FEED_REBOLG);
		i.putExtra(RenRenChatActivity.PARAM_NEED.CONTEXT_FROM, context.getClass().getSimpleName());
		i.putExtra(RenRenChatActivity.PARAM_NEED.FORWARD_FILE_PATH, filePath);
		i.putExtra(RenRenChatActivity.PARAM_NEED.IS_GET_MESSAGE, isForward);
		context.startActivity(i);
    }
    
    /**
     * 统计用
     */
    private void handleStatistics(){
    	switch(mComeFrom){
    	case COMEFROM.MAINFRAGMENT_ACTIVITY://来自主页面
    		BackgroundUtils.getInstance().multiChatStatistics(Htf.MULTI_CHAT_FROM_SIXIN_CHATLIST);
    		break;
    	case COMEFROM.SING_CHAT_MESSAGE://来自单聊信息页面
    		BackgroundUtils.getInstance().multiChatStatistics(Htf.MULTI_CHAT_FROM_SINGLE_CHAT);
    		break;
    	case COMEFROM.CHAT_FEED_REBOLG://来自转发
    		if(mNewsFeed != null){
    			BackgroundUtils.getInstance().multiChatStatistics(Htf.MULTI_CHAT_FROM_FORWARD_FEED);
    		}else if(!TextUtils.isEmpty(mFilePath)){
    			BackgroundUtils.getInstance().multiChatStatistics(Htf.MULTI_CHAT_FROM_FORWARD_LARGE_PIC);
    		}
    		break;
    	}
    }
}
