package com.renren.mobile.chat.ui.chatsession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.common.emotion.emotion.EmotionString;
import com.common.messagecenter.base.Utils;
import com.common.network.DomainUrl;
import com.common.network.NetRequestListener;
import com.common.statistics.BackgroundUtils;
import com.common.statistics.Htf;
import com.core.util.DeviceUtil;
import com.renren.mobile.chat.R;
import com.renren.mobile.chat.RenrenChatApplication;
import com.renren.mobile.chat.actions.models.RoomInfoModelWarpper;
import com.renren.mobile.chat.activity.RenRenChatActivity;
import com.renren.mobile.chat.activity.RenRenChatActivity.CanTalkable.GROUP;
import com.renren.mobile.chat.activity.ThreadPool;
import com.renren.mobile.chat.base.GlobalValue;
import com.renren.mobile.chat.base.inter.Subject;
import com.renren.mobile.chat.base.model.ChatBaseItem;
import com.renren.mobile.chat.base.model.ChatBaseItem.MESSAGE_ISGROUP;
import com.renren.mobile.chat.base.util.FileUtil;
import com.renren.mobile.chat.base.util.SystemUtil;
import com.renren.mobile.chat.base.views.NotSynImageView;
import com.renren.mobile.chat.common.DateFormat;
import com.renren.mobile.chat.dao.ChatHistoryDAO;
import com.renren.mobile.chat.dao.ChatHistoryDAOObserver;
import com.renren.mobile.chat.database.ChatHistory_Column;
import com.renren.mobile.chat.database.ChatSession_Column;
import com.renren.mobile.chat.model.warpper.ChatMessageWarpper;
import com.renren.mobile.chat.ui.MainFragmentActivity;
import com.renren.mobile.chat.ui.RBaseAdapter;
import com.renren.mobile.chat.ui.contact.C_ContactsData;
import com.renren.mobile.chat.ui.contact.ContactBaseModel;
import com.renren.mobile.chat.ui.contact.ContactModel;
import com.renren.mobile.chat.ui.contact.ContactModelFactory;
import com.renren.mobile.chat.ui.contact.RoomInfosData;
import com.renren.mobile.chat.ui.contact.RoomInfosData.RoomInfosDataObserver;
import com.renren.mobile.chat.ui.groupchat.D_ChatMessagesActivity;
import com.renren.mobile.chat.ui.groupchat.D_ChatMessagesActivity.CHAT_MESSAGE_COMEFROM;
import com.renren.mobile.chat.ui.groupchat.D_ChatMessagesActivity.CHAT_MESSAGE_PARAMS;
import com.renren.mobile.chat.ui.notification.MessageNotificationManager;
import com.renren.mobile.chat.util.ChatDataHelper;
import com.renren.mobile.chat.view.ListViewScrollListener;
/**
 * 会话列表Adapter
 * 
 * @author tian.wang 2011-11-25
 * */
import com.renren.mobile.chat.R;
import com.renren.mobile.chat.RenrenChatApplication;
public class ChatSessionAdapter extends RBaseAdapter implements ChatHistoryDAOObserver ,RoomInfosDataObserver{
	private AlertDialog.Builder menu;
	ListViewScrollListener listener;
	private ChatHistoryDAO mSubject = null;
	private Handler mHandler;
	private Activity activity;
	private class ViewHolder {
		NotSynImageView headImg;
		TextView userName;
		TextView time;
		TextView content;
		RelativeLayout sessionItemLayout;
		TextView notificationCount;
		ImageView fail;
		ImageView blackList;
	}
	public ChatSessionAdapter(ArrayList<Object> data, View header, View footer, Activity activity, ListView listView, Handler handler) {
		super(data, header, footer, activity, listView);
		this.mHandler = handler;
		this.activity = activity;
		listener = new ListViewScrollListener(this);
		listView.setOnScrollListener(listener);
		menu = new AlertDialog.Builder(activity);
		RoomInfosData.getInstance().registorObserver(this);
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.new_chat_session_screen_list_item, null);
			ViewHolder viewHolder = new ViewHolder();
			viewHolder.headImg = (NotSynImageView) convertView.findViewById(R.id.head_img);
			viewHolder.notificationCount = (TextView) convertView.findViewById(R.id.head_notificaiton_count);
			viewHolder.fail = (ImageView) convertView.findViewById(R.id.head_fail);
			viewHolder.userName = (TextView) convertView.findViewById(R.id.chat_session_username);
			viewHolder.time = (TextView) convertView.findViewById(R.id.chat_session_lasttime);
			viewHolder.content = (TextView) convertView.findViewById(R.id.chat_session_lastcontent);
			viewHolder.blackList = (ImageView)convertView.findViewById(R.id.user_is_inblack_view);
			viewHolder.sessionItemLayout = (RelativeLayout) convertView.findViewById(R.id.chat_seesion_layout);
			convertView.setTag(viewHolder);
		}
		setSessionListconvertView(convertView, position);
		return convertView;
	}
	private void showMenu(final ChatSessionDataModel item, final int position,final ChatSessionDataModel chatSessionDataModel) {
		String[] content = { activity.getResources().getString(R.string.see_chat_details), activity.getResources().getString(R.string.delete_chat) };
		if(item.mIsGroup==MESSAGE_ISGROUP.IS_SINGLE  ){
			menu.setTitle(item.mName);
		}else {
			if(item.mRoomInfo!=null){
				menu.setTitle(item.mRoomInfo.mSubject);
			}else{
				menu.setTitle(item.mName);
			}
		}
		menu.setItems(content, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (which == 0) {
					ContactModel contactModel = C_ContactsData.getInstance().getSiXinContact(item.mToId,null);
					if(item.mIsGroup == MESSAGE_ISGROUP.IS_SINGLE){
						if(null!= contactModel){
							Log.v("gg", "contactModel != null");
							Intent i = new Intent(activity,D_ChatMessagesActivity.class);
							i.putExtra(D_ChatMessagesActivity.CHAT_MESSAGE_PARAMS.TO_CHAT_ID, contactModel.mUserId);
							i.putExtra(D_ChatMessagesActivity.CHAT_MESSAGE_PARAMS.USER_DOMAIN, contactModel.mDomain);
							i.putExtra(CHAT_MESSAGE_PARAMS.COMEFROM, CHAT_MESSAGE_COMEFROM.RENREN_CHAT_SINGLE);
							activity.startActivity(i);
						}else{
							Intent i = new Intent(activity,D_ChatMessagesActivity.class);
							i.putExtra(D_ChatMessagesActivity.CHAT_MESSAGE_PARAMS.TO_CHAT_ID, chatSessionDataModel.mToId);
							i.putExtra(D_ChatMessagesActivity.CHAT_MESSAGE_PARAMS.USER_DOMAIN, chatSessionDataModel.mDomain);
							i.putExtra(D_ChatMessagesActivity.CHAT_MESSAGE_PARAMS.USER_NAME, chatSessionDataModel.mName);
							i.putExtra(CHAT_MESSAGE_PARAMS.COMEFROM, CHAT_MESSAGE_COMEFROM.RENREN_CHAT_SINGLE);
							activity.startActivity(i);
						}
					}else{
						RoomInfoModelWarpper warpper = RoomInfosData.getInstance().getRoomInfo(chatSessionDataModel.mGroupId);
						if(warpper.mDisable == 1){
							SystemUtil.toast(R.string.can_not_see_chat_details);
						}else{
							Intent intent = new Intent(activity,D_ChatMessagesActivity.class);
							intent.putExtra("groupId", chatSessionDataModel.mGroupId);
							intent.putExtra(CHAT_MESSAGE_PARAMS.COMEFROM, CHAT_MESSAGE_COMEFROM.RENREN_CHAT_GROUP);
							activity.startActivity(intent);
						}
					}
				} else {
					deleteChatRecord(item.getmToId(),position,chatSessionDataModel);
				}
			}
		});
		menu.show();
	}
	private void deleteChatRecord(final long userId, final int position,final ChatSessionDataModel chatSessionDataModel) {
		new AlertDialog.Builder(activity).setTitle(RenrenChatApplication.getmContext().getResources().getString(R.string.MultiChatForwardScreen_java_2)).setMessage(RenrenChatApplication.getmContext().getResources().getString(R.string.ChatSessionAdapter_java_1)).setPositiveButton(RenrenChatApplication.getmContext().getResources().getString(R.string.VoiceOnClickListenner_java_3), new DialogInterface.OnClickListener() {		//ChatSessionAdapter_java_1=您确定要删除此对话并清空此对话的所有记录吗?; //VoiceOnClickListenner_java_3=确定; 
			public void onClick(DialogInterface dialog, int whichButton) {
				LinkedList<Object> linkedList = ChatSessionManager.getInstance().getSessionList();
				linkedList.remove(chatSessionDataModel);
				dataList.remove(chatSessionDataModel);
				if(linkedList.size() == 0){
					mHandler.sendEmptyMessage(ChatSessionScreen.REFRESH_TEXT_VIEW);
				}
				MessageNotificationManager.getInstance().removeNotificationByGroupId(chatSessionDataModel.mGroupId);
				ChatDataHelper.getInstance().deleteChatMessageByGroupId(chatSessionDataModel.mGroupId);
				ChatSessionHelper.getInstance().deleteChatSessionByGroupId(chatSessionDataModel.mGroupId);
			//	C_ContactsData.getInstance().commonContactsChanged();//通知常用联系人更新
				notifyDataSetChanged();
				Log.v("fff", "deleteChatRecord");
				((MainFragmentActivity) activity).setTabNews(MainFragmentActivity.REFRESH_NEW_MESSAGE);
				RenrenChatApplication.mHandler.post(new Runnable() {
					public void run() {
						try {
							ChatSessionDataModel messageItem = (ChatSessionDataModel) dataList.get(position);
							Long toId = messageItem.getmToId();
								if(DeviceUtil.getInstance().isMountSDCard()){
									String audioUrl = GlobalValue.getInstance().getUserAudioPath(toId);
									String photoUrl = GlobalValue.getInstance().getUserPhotos(toId);
									FileUtil.getInstance().delAllFile(audioUrl);
									FileUtil.getInstance().delAllFile(photoUrl);
								}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
			}
		}).setNegativeButton(RenrenChatApplication.getmContext().getResources().getString(R.string.ChatMessageWarpper_FlashEmotion_java_4), new DialogInterface.OnClickListener() {		//ChatMessageWarpper_FlashEmotion_java_4=取消; 
			public void onClick(DialogInterface dialog, int whichButton) {
			}
		}).create().show();
	}
	private void setSessionListconvertView(View convertView,final int position) {
		final ChatSessionDataModel messageItem = (ChatSessionDataModel) dataList.get(position);
//		final ContactModel contactInChat = new ContactModel();
//		contactInChat.mUserId = messageItem.mToId;
//		contactInChat.mContactName = messageItem.mName;
//		contactInChat.name = messageItem.mName;
//		contactInChat.mHeadUrl = messageItem.mHeadUrl;
//		Utils.l("setSessionListconvertView--->" + messageItem.mDomain);
//		contactInChat.mDomain = messageItem.mDomain;
		Log.v("fff", "messageItem.mToId = " + messageItem.mToId);
//		final ContactBaseModel contactModel ;
		ContactBaseModel contactBaseModel = null;
		
		ViewHolder viewHolder = (ViewHolder) convertView.getTag();
		if(messageItem.mIsGroup==MESSAGE_ISGROUP.IS_SINGLE  ){
			contactBaseModel = C_ContactsData.getContact(messageItem.mToId, messageItem.mDomain);
			if(contactBaseModel == null){
				contactBaseModel = ContactModelFactory.createContactModel(messageItem.mDomain);
				contactBaseModel.mUserId = messageItem.mToId;
				contactBaseModel.mContactName = messageItem.mName;
				contactBaseModel.name = messageItem.mName;
			}
			contactBaseModel.mDomain = messageItem.mDomain;
//			contactModel = contactBaseModel;
//			if(messageItem.mName != null && !messageItem.mName.equals("")){
//				setTextViewContent(viewHolder.userName, messageItem.mName);
//			}else{
				if(contactBaseModel.mContactName != null && !contactBaseModel.mContactName.equals("") )
					setTextViewContent(viewHolder.userName, contactBaseModel.mContactName);
//			}
			
			if(contactBaseModel.isBlacklist(contactBaseModel.mRelation)){
				viewHolder.blackList.setVisibility(View.VISIBLE);
			}else viewHolder.blackList.setVisibility(View.GONE);
			
		}else {
			viewHolder.blackList.setVisibility(View.GONE);
			if(messageItem.mRoomInfo!=null){
				RoomInfoModelWarpper roomInfo = RoomInfosData.getInstance().getRoomInfo(messageItem.mRoomInfo.mRoomId);
				if(roomInfo !=null ){
					messageItem.mRoomInfo = roomInfo;
				}
				if(messageItem.mRoomInfo.mRoomMemberNumber == 0){
					messageItem.mRoomInfo.mRoomMemberNumber = 1;
				}
				setTextViewContent(viewHolder.userName, messageItem.mRoomInfo.mSubject+"("+(messageItem.mRoomInfo.mRoomMemberNumber)+")");
			}else{
				setTextViewContent(viewHolder.userName,  RenrenChatApplication.getmContext().getResources().getString(R.string.ChatSessionAdapter_java_2));		//ChatSessionAdapter_java_2=群聊(1); 
			}
		}
		
		final ContactBaseModel contactModel = contactBaseModel;
		String date = DateFormat.getDateByChatSession(messageItem.mTime);
		if(!date.equals("") && !date.equals(RenrenChatApplication.getmContext().getResources().getString(R.string.ChatSessionAdapter_java_3)) && !date.equals(RenrenChatApplication.getmContext().getResources().getString(R.string.ChatSessionAdapter_java_4))){		//ChatSessionAdapter_java_3=昨天; //ChatSessionAdapter_java_4=前天; 
			setTextViewContent(viewHolder.time,date);
		}else
			setTextViewContent(viewHolder.time, date + " " + DateFormat.getNowStrByChat(messageItem.mTime));
		if(date.equals(RenrenChatApplication.getmContext().getResources().getString(R.string.ChatSessionAdapter_java_3) + "Special")){
			setTextViewContent(viewHolder.time,DateFormat.getDateByChatSession1(messageItem.mTime));
		}
		if(messageItem.mIsGroup==MESSAGE_ISGROUP.IS_SINGLE){
			if (messageItem.mContent != null) {
				viewHolder.content.setText(new EmotionString(messageItem.mContent));
			}else {
				viewHolder.content.setText("");
			}
//			if(messageItem.mType == ChatBaseItem.MESSAGE_TYPE.VOICE){
//				if(messageItem.mComeFrom == ChatBaseItem.MESSAGE_COMEFROM.LOCAL_TO_OUT){
//					viewHolder.content.setText(RenrenChatApplication.getmContext().getResources().getString(R.string.ChatMessageWarpper_Voice_java_2));		//ChatMessageWarpper_Voice_java_2=[语音]; 
//				}else{
//					viewHolder.content.setText(RenrenChatApplication.getmContext().getResources().getString(R.string.ChatMessageWarpper_Voice_java_2));		//ChatMessageWarpper_Voice_java_2=[语音]; 
//				}
//			}else if(messageItem.mType == ChatBaseItem.MESSAGE_TYPE.IMAGE){
//				if(messageItem.mComeFrom == ChatBaseItem.MESSAGE_COMEFROM.LOCAL_TO_OUT){
//					viewHolder.content.setText(RenrenChatApplication.getmContext().getResources().getString(R.string.ChatMessageWarpper_Image_java_4));		//ChatMessageWarpper_Image_java_4=[图片]; 
//				}else{
//					viewHolder.content.setText(RenrenChatApplication.getmContext().getResources().getString(R.string.ChatMessageWarpper_Image_java_4));		//ChatMessageWarpper_Image_java_4=[图片]; 
//				}
//			}else if(messageItem.mType == ChatBaseItem.MESSAGE_TYPE.FLASH){
//				viewHolder.content.setText(RenrenChatApplication.getmContext().getResources().getString(R.string.ChatMessageWarpper_FlashEmotion_java_5));		//ChatMessageWarpper_FlashEmotion_java_5=[炫酷表情]; 
//			}
		}else {
			Log.v("aa", "messageItem.mContent = " + messageItem.mContent);
			if (messageItem.mContent != null) {
				if(messageItem.mComeFrom == ChatBaseItem.MESSAGE_COMEFROM.LOCAL_TO_OUT){
					if(!messageItem.mContent.equals("")){
						 viewHolder.content.setText( new EmotionString(RenrenChatApplication.getmContext().getResources().getString(R.string.ChatSessionAdapter_java_5) + messageItem.mContent));		//ChatSessionAdapter_java_5=我: ; 
					}else viewHolder.content.setText("");
				}else{
					if(messageItem.mType == ChatBaseItem.MESSAGE_TYPE.SOFT_INFO){
						viewHolder.content.setText(new EmotionString(messageItem.mContent));
					}else {
						if(!messageItem.mContent.equals("")){
							viewHolder.content.setText(new EmotionString(messageItem.mName + ": " + messageItem.mContent));
						}else viewHolder.content.setText("");
					}
				}
			}else {
				viewHolder.content.setText("");
			}
//			if(messageItem.mType == ChatBaseItem.MESSAGE_TYPE.VOICE){
//				if(messageItem.mComeFrom == ChatBaseItem.MESSAGE_COMEFROM.LOCAL_TO_OUT){
//					viewHolder.content.setText(RenrenChatApplication.getmContext().getResources().getString(R.string.ChatSessionAdapter_java_6));		//ChatSessionAdapter_java_6=我: [语音]; 
//				}else{
//					viewHolder.content.setText(messageItem.mName + RenrenChatApplication.getmContext().getResources().getString(R.string.ChatSessionAdapter_java_7));		//ChatSessionAdapter_java_7=: [语音]; 
//				}
//			}else if(messageItem.mType == ChatBaseItem.MESSAGE_TYPE.IMAGE){
//				if(messageItem.mComeFrom == ChatBaseItem.MESSAGE_COMEFROM.LOCAL_TO_OUT){
//					viewHolder.content.setText(RenrenChatApplication.getmContext().getResources().getString(R.string.ChatSessionAdapter_java_8));		//ChatSessionAdapter_java_8=我: [图片]; 
//				}else{
//					viewHolder.content.setText(messageItem.mName + RenrenChatApplication.getmContext().getResources().getString(R.string.ChatSessionAdapter_java_9));		//ChatSessionAdapter_java_9=: [图片]; 
//				}
//			}else if(messageItem.mType == ChatBaseItem.MESSAGE_TYPE.FLASH){
//				if(messageItem.mComeFrom == ChatBaseItem.MESSAGE_COMEFROM.LOCAL_TO_OUT){
//					viewHolder.content.setText(RenrenChatApplication.getmContext().getResources().getString(R.string.ChatSessionAdapter_java_10));		//ChatSessionAdapter_java_10=我: [炫酷表情]; 
//				}else{
//					viewHolder.content.setText(messageItem.mName + RenrenChatApplication.getmContext().getResources().getString(R.string.ChatSessionAdapter_java_11));		//ChatSessionAdapter_java_11=: [炫酷表情]; 
//				}
//			}
		}
		if(messageItem.mContent.equals("")){
			viewHolder.content.setText("");
		}
		int unReadCount = messageItem.mUnreadCount;
		Log.v("fafa", "messageItem.mSendState = " + messageItem.mSendState);
		if(messageItem.mSendState == Subject.COMMAND.COMMAND_DOWNLOAD_IMAGE_ERROR
				|| messageItem.mSendState == Subject.COMMAND.COMMAND_MESSAGE_ERROR 
				|| messageItem.mSendState == Subject.COMMAND.COMMAND_UPLOAD_IMAGE_ERROR 
				|| messageItem.mSendState == Subject.COMMAND.COMMAND_UPLOAD_VOICE_ERROR
				|| messageItem.mSendState == Subject.COMMAND.COMMAND_DOWNLOAD_VOICE_ERROR){
			viewHolder.notificationCount.setVisibility(View.GONE);
			viewHolder.fail.setVisibility(View.VISIBLE);
		}else{
			if (unReadCount == 0) {
				viewHolder.notificationCount.setVisibility(View.GONE);
				viewHolder.fail.setVisibility(View.GONE);
			} else {
				viewHolder.notificationCount.setVisibility(View.VISIBLE);
				viewHolder.fail.setVisibility(View.GONE);
				if(unReadCount > 99){
//					viewHolder.notificationCount.setBackgroundResource(R.drawable.chat_num_tip_large1);
					viewHolder.notificationCount.setText("99+");
				}else{
//					viewHolder.notificationCount.setBackgroundResource(R.drawable.chat_num_tip_large1);
					viewHolder.notificationCount.setText(String.valueOf(unReadCount));
				}
			}
		}
		viewHolder.headImg.clear();
		viewHolder.headImg.setBackgroundColor(NotSynImageView.GRAY);
		if(messageItem.mIsGroup==GROUP.CONTACT_MODEL.Value){
			if(messageItem.mComeFrom == ChatBaseItem.MESSAGE_COMEFROM.LOCAL_TO_OUT ||
                    (TextUtils.isEmpty(messageItem.mHeadUrl)&&!TextUtils.isEmpty(messageItem.mDomain) && messageItem.mComeFrom == ChatBaseItem.MESSAGE_COMEFROM.OUT_TO_LOCAL )){
                ContactBaseModel contact = C_ContactsData.getInstance().getContact(messageItem.mToId,messageItem.mDomain);
                if(contact !=null){
                    messageItem.mHeadUrl = contact.getmHeadUrl();;
                }
            }
			viewHolder.headImg.addUrl(messageItem.mHeadUrl);
		}else{
			if(messageItem.mRoomInfo!=null){
				try {
					if(messageItem.mRoomInfo.mMembers.size()>0){
						for(int k = 0;k<NotSynImageView.MAX_HEAD_NUMBER&&k<messageItem.mRoomInfo.mMembers.size();k++){
							viewHolder.headImg.addUrl(messageItem.mRoomInfo.mMembers.get(k).getmHeadUrl());
						}
					}else{
						viewHolder.headImg.addUrl(NotSynImageView.GROUP_DEFUALT_HEAD_URL);
					}
				} catch (Exception e) {}
			}else{
				viewHolder.headImg.addUrl(NotSynImageView.GROUP_DEFUALT_HEAD_URL);
			}
		}
		viewHolder.sessionItemLayout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(messageItem.mIsGroup==MESSAGE_ISGROUP.IS_SINGLE){
//					ContactModel contactModel = C_ContactsData.getInstance().getSiXinContact(contactInChat.mUserId,null);
//					if(contactModel!= null){
					if(contactModel != null){
						RenRenChatActivity.show(activity, contactModel);
					}
//					}else{
//						ContactModel model = new ContactModel();
//						model.mUserId = contactInChat.mUserId;
//						model.mContactName = contactInChat.mContactName;
//						model.name = contactInChat.name;
//						model.mDomain = contactInChat.mDomain;
//						model.mHeadUrl = contactInChat.mHeadUrl;
//						RenRenChatActivity.show(activity, model);
//					}
				}else{
					RoomInfoModelWarpper model = RoomInfosData.getInstance().getRoomInfo(messageItem.mGroupId);
					if(model!=null){
						RenRenChatActivity.show(activity, model);
					}else{
						model = RoomInfosData.getInstance().createUnknowRoom(messageItem.mGroupId);
						RenRenChatActivity.show(activity, model);
					}
					
					// 统计
					BackgroundUtils.getInstance().multiChatStatistics(Htf.MULTI_CHAT_FROM_SIXIN_CHATLIST);
				}
				
//				RenrenChatApplication.sHandler.post(new Runnable() {
//					
//					@Override
//					public void run() {
//						messageItem.mTime = System.currentTimeMillis();
//						LinkedList<Object> linkedList = ChatSessionManager.getInstance().getSessionList();
//						linkedList.remove(messageItem);
//						linkedList.add(0, messageItem);
//						ChatSessionHelper.getInstance().updateMessageTime(messageItem.mGroupId, messageItem.mMessageId, System.currentTimeMillis());
//						
//					}
//				});
			}
		});
		viewHolder.sessionItemLayout.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				 showMenu(messageItem,position,messageItem);
				return true;
			}
		});
	}
	/**
	 * 更新一条信息的发送状态（失败还是成功）
	 * */
	public void updateMessageState(long messageId, int state) {
		int count = getCount();
		ChatSessionDataModel chatSessionDataModel;
		for (int i = 0; i < count; i++) {
			chatSessionDataModel = (ChatSessionDataModel) dataList.get(i);
			if (chatSessionDataModel.mId == messageId) {
				chatSessionDataModel.mSendState = state;
				RenrenChatApplication.mHandler.post(new Runnable() {
					public void run() {
						notifyDataSetChanged();
						Log.v("fff", "updateMessageState");
					}
				});
				break;
			}
		}
	}
	public void attachToDAO(ChatHistoryDAO dao) {
		this.mSubject = dao;
		this.mSubject.registorObserver(this);
	}
	public void dittachDAO() {
		if (this.mSubject != null) {
			this.mSubject.unregistorObserver(this);
			this.mSubject = null;
		}
	}
	public void updateList(long id){
		int count = getCount();
		Log.v("uu", "id = " + id);
		for(int i=0;i<count;i++){
			ChatSessionDataModel chatSessionDataModle = (ChatSessionDataModel) dataList.get(i);
			if(chatSessionDataModle.mGroupId == id){
				Log.v("uu", "mUnreadCount = " + 0);
				chatSessionDataModle.mUnreadCount = 0;
				break;
			}
		}
		RenrenChatApplication.mHandler.post(new Runnable() {
			@Override
			public void run() {
				notifyDataSetChanged();
				Log.v("fff", "updateList");
			}
		});
	}
	
	InsertRunnable mInsertRunnable = new InsertRunnable();
	List<ChatMessageWarpper> mMessageCache = new ArrayList<ChatMessageWarpper>();
	public class InsertRunnable implements Runnable{
		@Override
		public void run() {
			synchronized (mMessageCache) {
				if(mMessageCache.size()>0){
					onInsert(mMessageCache);
					mMessageCache.clear();
				}
			}
		}
	}
	
	@Override
	public void onInsert(final ChatMessageWarpper message) {
		int row = ChatSessionHelper.getInstance().updateMessage(message,message.getDescribe());
		if(row <= 0){
			String old = message.mMessageContent;
            message.mMessageContent = message.getDescribe();
            ChatSessionHelper.getInstance().insertToTheDatabase(message);
            message.mMessageContent =old;
		}
		synchronized (mMessageCache) {
			mMessageCache.add(message);
		}
		ThreadPool.obtain().removeCallbacks(mInsertRunnable);
		ThreadPool.obtain().executeMainThread(mInsertRunnable);
		ChatSessionDataModel messageItem = new ChatSessionDataModelAdapter(message);
		if(messageItem.mIsGroup == MESSAGE_ISGROUP.IS_SINGLE){
			C_ContactsData.getInstance().commonContactsChanged(messageItem);
		}
	}
	@Override
	public void onDelete(String columnName,long _id) {
		if(columnName.equals(ChatHistory_Column.GROUP_ID)){
			int count = getCount();
			ChatSessionDataModel chatSessionDataModel = null;
			for (int i = 0; i < count; i++) {
				chatSessionDataModel = (ChatSessionDataModel) dataList.get(i);
				if (chatSessionDataModel.mGroupId == _id) {
					RoomInfoModelWarpper roomInfo = RoomInfosData.getInstance().getRoomInfo(chatSessionDataModel.mGroupId);
					if(roomInfo == null && chatSessionDataModel.mIsGroup == MESSAGE_ISGROUP.IS_GROUP){
						Log.v("qq", "exit group");
						MessageNotificationManager.getInstance().removeNotificationByGroupId(chatSessionDataModel.mGroupId);
						dataList.remove(chatSessionDataModel);
						LinkedList<Object> linkedList = ChatSessionManager.getInstance().getSessionList();
						linkedList.remove(chatSessionDataModel);
						if(linkedList.size() == 0){
							mHandler.sendEmptyMessage(ChatSessionScreen.REFRESH_TEXT_VIEW);
						}
						break;
					}else {
						Log.v("qq", "clear group");
						chatSessionDataModel.mContent = "";
						MessageNotificationManager.getInstance().removeNotificationByGroupId(chatSessionDataModel.mGroupId);
						this.updateList(_id);
						/*
						 * 当最后一条消息发送失败时，删除全部聊天记录，数据库中的这条记录状态会是发送失败，叹号会存在
						 * 解决办法暂时为 出现以上情况时，把这条记录发送状态更新为 普通消息发送成功
						 */
						updataSessionState(chatSessionDataModel);
						
						ChatSessionHelper.getInstance().updateMessageContent(chatSessionDataModel.mMessageId,"",chatSessionDataModel.mGroupId);
						break;
					}
				}
			}
			RenrenChatApplication.mHandler.post(new Runnable() {
				@Override
				public void run() {
					notifyDataSetChanged();
					Log.v("fff", "delete333");
				}
			});
		}
		if(columnName.equals(ChatHistory_Column.TO_CHAT_ID)){
			int count = getCount();
			ChatSessionDataModel chatSessionDataModel;
			for (int i = 0; i < count; i++) {
				chatSessionDataModel = (ChatSessionDataModel) dataList.get(i);
				if (chatSessionDataModel.mToId == _id) {
					Log.v("qq", "clear single");
					chatSessionDataModel.mContent = "";
					this.updateList(_id);
					
					updataSessionState(chatSessionDataModel);
					
					MessageNotificationManager.getInstance().removeNotificationByGroupId(chatSessionDataModel.mGroupId);
					break;
				}
			}
			RenrenChatApplication.mHandler.post(new Runnable() {
				@Override
				public void run() {
					notifyDataSetChanged();
					Log.v("fff", "ondelete2");
				}
			});
		}
		if(columnName.equals(ChatHistory_Column._ID)){
			Log.v("ww", "22222222222");
			int count = getCount();
			ChatSessionDataModel chatSessionDataModel;
			for (int i = 0; i < count; i++) {
				chatSessionDataModel = (ChatSessionDataModel) dataList.get(i);
				Log.v("ww", "chatSessionDataModel.mId = " + chatSessionDataModel.mMessageId + "===" + "_id = " + _id);
				if (chatSessionDataModel.mMessageId == _id) {
					Log.v("aa", "end delete");
					ChatMessageWarpper messageWarpper;
					messageWarpper = ChatDataHelper.getInstance().queryLastMessageByGroupId(chatSessionDataModel.mGroupId,chatSessionDataModel.mIsGroup);
					if(messageWarpper==null){
						Log.v("aa", "last data");
						chatSessionDataModel.mContent = "";
						updataSessionState(chatSessionDataModel);
						this.notifyDataSetChanged();
						Log.v("fff", "ondelete1");
						return;
					}
					messageWarpper.mDomain = chatSessionDataModel.mDomain;
					ChatSessionHelper.getInstance().updateMessage(messageWarpper,messageWarpper.getDescribe());
					chatSessionDataModel.mMessageId = messageWarpper.mMessageId;
					chatSessionDataModel.mContent = messageWarpper.getDescribe();
					chatSessionDataModel.mName = messageWarpper.mUserName;
					chatSessionDataModel.mId = messageWarpper.mMessageId;
					chatSessionDataModel.mType = messageWarpper.mMessageType;
					chatSessionDataModel.mComeFrom = messageWarpper.mComefrom;
					chatSessionDataModel.mSendState = messageWarpper.mMessageState;
					chatSessionDataModel.mTime = messageWarpper.mMessageReceiveTime;
					
					ChatSessionDataModelAdapter dataModel = new ChatSessionDataModelAdapter(messageWarpper);
					ChatSessionManager.getInstance().delMessage(dataModel, chatSessionDataModel);

					RenrenChatApplication.mHandler.post(new Runnable() {
						@Override
						public void run() {
							notifyDataSetChanged();
							Log.v("fff", "ondelete");
						}
					});
					break;
				}
			}
		}
	}
	private void updataSessionState(ChatSessionDataModel chatSessionDataModel) {
		this.updateMessageState(chatSessionDataModel.mMessageId,Subject.COMMAND.COMMAND_MESSAGE_OVER );
		ContentValues value = new ContentValues(1);
		value.put(ChatSession_Column.MESSAGE_STATE, Subject.COMMAND.COMMAND_MESSAGE_OVER);
		ChatSessionHelper.getInstance().updateMessageState(chatSessionDataModel.mMessageId, value);
	}
	@Override
	public void onUpdate(String columnName,long _id,ContentValues value) {
		if(columnName.equals(ChatHistory_Column.TO_CHAT_ID)){
			this.updateList(_id);
			Log.v("uu", "onUpdate");
		}
		if(columnName.equals(ChatHistory_Column._ID)){
			this.updateMessageState(_id,value.getAsInteger(ChatHistory_Column.MESSAGE_STATE) );
			ChatSessionManager.getInstance().updateMessageState(_id, value.getAsInteger(ChatHistory_Column.MESSAGE_STATE) );
			ChatSessionHelper.getInstance().updateMessageState(_id, value);
		}
	}
	
	private  Map<Long, ChatMessageWarpper> mMap = new LinkedHashMap<Long, ChatMessageWarpper>();
	
	public Map<Long, ChatMessageWarpper> obtainMap(){
		return this.mMap;
	}
	public void recycle(Map<Long, ChatMessageWarpper> map){
		map.clear();
	}
	
	
	@Override
	public void onInsert(List<ChatMessageWarpper> message) {
		if(message.size()<=0){
			return;
		}
		Map<Long, ChatMessageWarpper> map = this.obtainMap();
		for(ChatMessageWarpper chatMessageWarpper : message){
//			Log.v("dada", "chatMessageWarpper.getMessageContent = " + chatMessageWarpper.getMessageContent());
		}
		ChatMessageWarpper m = null;
		for( int i = message.size() - 1;i >-1; i-- ){
			m = message.get(i);
			if(!map.containsKey(m.mGroupId)){
				map.put(m.mGroupId, m);
				Log.v("dada", "name = " + m.mUserName + "content = " + m.getMessageContent());
			}
		}
//		for(int i = 0; i < message.size(); i++){
//			m = message.get(i);
//			if(!map.containsKey(m.mToChatUserId)){
//				map.put(m.mToChatUserId, m);
//				Log.v("dada", "name = " + m.mUserName + "content = " + m.getMessageContent());
//			}
//		}
		
		for(Map.Entry<Long, ChatMessageWarpper> entry : map.entrySet()){
				ChatSessionDataModel messageItem = new ChatSessionDataModelAdapter(entry.getValue());
				ChatSessionManager.getInstance().addMessage(messageItem);
				
		}
		mHandler.removeMessages(ChatSessionScreen.REFRESH_LIST);
		mHandler.sendEmptyMessage(ChatSessionScreen.REFRESH_LIST); 
		this.recycle(map);
	}
	@Override
	public void notifyRoomInfoDataUpdate(byte state, long roomId) {
		Log.v("gaga", "notifyRoomInfoDataUpdate");
		RenrenChatApplication.mHandler.post(new Runnable() {
			@Override
			public void run() {
				notifyDataSetChanged();
			}
		});
	}
	
	public void unRegistorObserver(){
		RoomInfosData.getInstance().unRegistorObserver(this);
	}
}
