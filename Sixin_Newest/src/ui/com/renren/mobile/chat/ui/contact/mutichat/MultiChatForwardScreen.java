package com.renren.mobile.chat.ui.contact.mutichat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.common.messagecenter.base.Utils;
import com.common.statistics.BackgroundUtils;
import com.common.statistics.Htf;
import com.renren.mobile.chat.R;
import com.renren.mobile.chat.RenrenChatApplication;
import com.renren.mobile.chat.actions.models.RoomInfoModelWarpper;
import com.renren.mobile.chat.activity.RenRenChatActivity;
import com.renren.mobile.chat.base.model.ChatBaseItem.MESSAGE_ISGROUP;
import com.renren.mobile.chat.base.util.SystemUtil;
import com.renren.mobile.chat.newsfeed.NewsFeedWarpper;
import com.renren.mobile.chat.ui.BaseScreen;
import com.renren.mobile.chat.ui.chatsession.ChatSessionDataModel;
import com.renren.mobile.chat.ui.chatsession.ChatSessionManager;
import com.renren.mobile.chat.ui.contact.C_ContactsData;
import com.renren.mobile.chat.ui.contact.C_ContactsListView;
import com.renren.mobile.chat.ui.contact.ContactBaseModel.Contact_group_type;
import com.renren.mobile.chat.ui.contact.ContactModel;
import com.renren.mobile.chat.ui.contact.RoomInfosData;
import com.renren.mobile.chat.ui.groupchat.D_SelectGroupChatContactActivity;
import com.renren.mobile.chat.view.BaseTitleLayout;
import com.renren.mobile.chat.view.BaseTitleLayout.FUNCTION_BUTTON_TYPE;
/**
 * @author xiangchao.fan 转发中转页Screen
 */
public class MultiChatForwardScreen extends BaseScreen {
	/**
	 * 创建群聊layout
	 */
	private FrameLayout mToCreateNewMultiChat;
	private TextView mText;
	private ImageView mIcon;
	private LinearLayout mNoDataLayout;
	/**
	 * 标题栏
	 */
	private BaseTitleLayout mTitleLayout;
	private LinearLayout mRoot;
	private FrameLayout mLayout;
	/**
	 * 最近联系人和群联系人显示list
	 */
	private C_ContactsListView mListView;
	private MultiChatForwardAdapter adapter;
	/**
	 * 转发信息
	 */
	private ContactModel mContactModel;
	private NewsFeedWarpper mNewsFeed;
	private RoomInfoModelWarpper mRoomInfo;
	private String mFilePath;
	
	private boolean mIsFromOtherApp;
	/**
	 * 临时存储会话Session列表群信息，
	 */
	private Map<Long, Boolean> mTempRoomList = new HashMap<Long, Boolean>();
	private List<ContactModel> list = new ArrayList<ContactModel>();
	public static final String ALEPHA_STRING = RenrenChatApplication.getmContext().getResources().getString(R.string.MultiChatForwardScreen_java_1);		//MultiChatForwardScreen_java_1=常用联系人; 
	/**
	 * 确认对话框
	 */
	AlertDialog.Builder mBuilder = null;
	public MultiChatForwardScreen(Activity activity, NewsFeedWarpper newsFeed,
			String filePath, boolean mIsFromOtherApp) {
		super(activity);
		this.mActivity = activity;
		this.mNewsFeed = newsFeed;
		this.mFilePath = filePath;
		initialDialog();
		initialTitle();
		initialLayout();
	}
	public void initialDialog() {
		mBuilder = new Builder(mActivity);
		mBuilder.setTitle(RenrenChatApplication.getmContext().getResources().getString(R.string.MultiChatForwardScreen_java_2));		//MultiChatForwardScreen_java_2=提示; 
	}
	/**
	 * 初始化标题栏
	 */
	public void initialTitle() {
		this.mTitleLayout = getTitle();
		mTitleLayout.setTitleMiddle(RenrenChatApplication.getmContext().getResources().getString(R.string.ChatMessageWarpper_FlashEmotion_java_3));		//ChatMessageWarpper_FlashEmotion_java_3=转发;
		mTitleLayout.setTitleFunctionButtonBackground(FUNCTION_BUTTON_TYPE.ADDCONTACT);
		Button cancel = mTitleLayout.getTitleLeft();
		cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				RenrenChatApplication.sForwardMessage = null;
				finish();
			}
		});
	}
	/**
	 * 初始化layout
	 */
	public void initialLayout(){
		mRoot = (LinearLayout) mInflater.inflate(R.layout.multi_chat_forward, null);
		setContent(mRoot);
		mLayout = (FrameLayout) mRoot.findViewById(R.id.multi_chat_forward_contact_layout);
		mListView = (C_ContactsListView) mRoot.findViewById(R.id.multi_chat_forward_listview);
		mListView.setDivider(null);
		// 滑动时不重绘页面
		mListView.setScrollingCacheEnabled(false);       
		mListView.setDrawingCacheEnabled(false);         
		mListView.setAlwaysDrawnWithCacheEnabled(false); 
		mListView.setWillNotCacheDrawing(true);
		
		mNoDataLayout = (LinearLayout) mRoot.findViewById(R.id.multi_chat_forward_contact_nodata);
		adapter = new MultiChatForwardAdapter(mActivity, mListView);
		setForwardList();
		C_ContactsData.getInstance().resetListFlag(list);
		adapter.setContacts(list);
		mListView.setAdapter(adapter);
		mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				ContactModel model = (ContactModel)adapter.getItem(position);
				/* 如果从特别关注页面转发feed，不需要弹窗**/
				
				if(null != mNewsFeed){
					if(model.getGroupType() == Contact_group_type.SINGLE){
						ContactModel tempModel = C_ContactsData.getInstance().getSiXinContact(model.getmUserId(),null);
						RenRenChatActivity.show(mActivity, tempModel, mNewsFeed,mFilePath, true,RenrenChatApplication.sForwardMessage);
					}else{
						RoomInfoModelWarpper roomInfo = RoomInfosData.getInstance().getRoomInfo(model.getmUserId());
						RenRenChatActivity.show(mActivity, roomInfo, mNewsFeed,mFilePath, true,RenrenChatApplication.sForwardMessage);
					}
					mActivity.finish();
				}else{
					AlertDialog dialog = createDialog(model);
					if(null != dialog){
						dialog.show();
					}
				}
			}
		});
		mToCreateNewMultiChat = (FrameLayout) mRoot.findViewById(R.id.to_create_multi_chat);
		mText = (TextView) mRoot.findViewById(R.id.to_create_multi_chat_textview);
		mIcon = (ImageView) mRoot.findViewById(R.id.to_create_multi_chat_imageview);
		mToCreateNewMultiChat.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(mActivity, D_SelectGroupChatContactActivity.class);
				i.putExtra(RenRenChatActivity.PARAM_NEED.TO_CHAT_FEED_MODEL, mNewsFeed);
				i.putExtra(RenRenChatActivity.PARAM_NEED.FORWARD_FILE_PATH, mFilePath);
				i.putExtra(RenRenChatActivity.PARAM_NEED.IS_GET_MESSAGE, true);
				mActivity.startActivity(i);
				mActivity.finish();
			}
		});
		mToCreateNewMultiChat.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					mText.setTextColor(Color.parseColor("#FFFFFF"));
					mIcon.setImageResource(R.drawable.d_next_page_selected);
					break;
				case MotionEvent.ACTION_UP:
					mText.setTextColor(Color.parseColor("#585858"));
					mIcon.setImageResource(R.drawable.d_next_page_default);
					break;
				case MotionEvent.ACTION_MOVE:
					Rect r = new Rect();
					mToCreateNewMultiChat.getFocusedRect(r);
					if(r.contains((int)event.getX(), (int)event.getY())){						
						mText.setTextColor(Color.parseColor("#FFFFFF"));
						mIcon.setImageResource(R.drawable.d_next_page_selected);
					}else{
						mText.setTextColor(Color.parseColor("#585858"));
						mIcon.setImageResource(R.drawable.d_next_page_default);
					}
					break;
				case MotionEvent.ACTION_CANCEL:
					mText.setTextColor(Color.parseColor("#585858"));
					mIcon.setImageResource(R.drawable.d_next_page_default);
					break;
				}
				return false;
			}
		});
	}
	/**
	 * 设置转发列表————会话列表+保存到联系人列表的群联系人（去重）
	 */
	private void setForwardList(){
		LinkedList<Object> linkedList = ChatSessionManager.getInstance().getSessionList();
		List<ContactModel> roomList = C_ContactsData.getInstance().getmSortGroupContacts();
		list.clear();
		mTempRoomList.clear();
		int size = linkedList.size();
		if(size == 0 && roomList.size() == 0){
			mNoDataLayout.setVisibility(View.GONE);
		}else{
			for(int i=0;i<size;i++){
				ChatSessionDataModel chatSessionDataModle = (ChatSessionDataModel) linkedList.get(i);
				ContactModel model = null;
				ContactModel newModel = null;
				if(chatSessionDataModle.mIsGroup == MESSAGE_ISGROUP.IS_SINGLE){
					model = C_ContactsData.getInstance().getSiXinContact(chatSessionDataModle.getmToId(),null);
					if(model != null){
						newModel = model.createNewContactModel();
					}
				}else{
					RoomInfoModelWarpper tempRoom = RoomInfosData.getInstance().getRoomInfo(chatSessionDataModle.mGroupId);
					if (null != tempRoom) {
						newModel = new ContactModel();
						newModel.setGroupType(Contact_group_type.MULTIPLE);
						newModel.setmContactName(chatSessionDataModle.mName);
						newModel.setmUserId(chatSessionDataModle.mGroupId);
						mTempRoomList.put(chatSessionDataModle.mGroupId, true);
					}
				}
				if(null != newModel){
					newModel.mAlephString = ALEPHA_STRING;
					list.add(newModel);
				}
			}
			for(ContactModel room:roomList){
				if(!mTempRoomList.containsKey(room.getUId())){
					RoomInfoModelWarpper tempRoom = RoomInfosData.getInstance().getRoomInfo(room.getUId());
					if (null != tempRoom) {
						ContactModel model = new ContactModel();
						model.setGroupType(Contact_group_type.MULTIPLE);
						model.setmContactName(room.getName());
						model.setmUserId(room.getUId());
						if(null != model){
							model.mAlephString = ALEPHA_STRING;
							list.add(model);
						}
					}
				}
			}
		}
	}
	/**
	 * 创建dialog
	 * @param model
	 * @return
	 */
	private AlertDialog createDialog(final ContactModel model){
		StringBuilder message = new StringBuilder("");
		if(model.getGroupType() == Contact_group_type.SINGLE){
			ContactModel tempModel = C_ContactsData.getInstance().getSiXinContact(model.getmUserId(),null);
			message.append(RenrenChatApplication.getmContext().getResources().getString(R.string.MultiChatForwardScreen_java_3) + tempModel.getName() + "?");		//MultiChatForwardScreen_java_3=是否要将该信息转发给:; 
		}else{
			RoomInfoModelWarpper roomInfo = RoomInfosData.getInstance().getRoomInfo(model.getmUserId());
			if(null == roomInfo){
				SystemUtil.toast(RenrenChatApplication.getmContext().getResources().getString(R.string.MultiChatForwardScreen_java_4));		//MultiChatForwardScreen_java_4=该群已经解散; 
				return null;
			}
			message.append(RenrenChatApplication.getmContext().getResources().getString(R.string.MultiChatForwardScreen_java_5) + roomInfo.getName() + "?");		//MultiChatForwardScreen_java_5=是否要将该信息转发到群:; 
		}
		mBuilder.setMessage(message);
		mBuilder.setPositiveButton(RenrenChatApplication.getmContext().getResources().getString(R.string.MultiChatForwardScreen_java_6), new AlertDialog.OnClickListener() {		//MultiChatForwardScreen_java_6=确认; 
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				
				if(model.getGroupType() == Contact_group_type.SINGLE){
					ContactModel tempModel = C_ContactsData.getInstance().getSiXinContact(model.getmUserId(),null);
					
					if(mIsFromOtherApp){
						/**
						message.parseUserInfo(mChatActivity.mToChatUser);
						 * this.mHeadUrl = user.getHeadUrl();
					this.mToChatUserId = user.getUId();
					this.mUserName = user.getName();
					this.mGroupId = user.getUId();  //TODO
					this.mDomain = user.getDomain();
						 */
						RenrenChatApplication.sForwardMessage.mHeadUrl = tempModel.getmHeadUrl();
						RenrenChatApplication.sForwardMessage.mToChatUserId = tempModel.mUserId;
						RenrenChatApplication.sForwardMessage.mDomain = tempModel.mDomain;
						RenrenChatApplication.sForwardMessage.mUserName = tempModel.mContactName;
					}
					
					RenRenChatActivity.show(mActivity, tempModel, mNewsFeed,mFilePath, true,RenrenChatApplication.sForwardMessage); //TODO
				}else{
					RoomInfoModelWarpper roomInfo = RoomInfosData.getInstance().getRoomInfo(model.getmUserId());
					RenRenChatActivity.show(mActivity, roomInfo, mNewsFeed,mFilePath, true,RenrenChatApplication.sForwardMessage);
				}
				mActivity.finish();
			}
		});
		mBuilder.setNegativeButton(RenrenChatApplication.getmContext().getResources().getString(R.string.ChatMessageWarpper_FlashEmotion_java_4), new AlertDialog.OnClickListener() {		//ChatMessageWarpper_FlashEmotion_java_4=取消; 
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		AlertDialog dialog = mBuilder.create();
		return dialog;
	}
}
