package com.renren.mobile.chat.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.common.manager.LoginManager;
import com.renren.mobile.chat.activity.RenRenChatActivity.CanTalkable.GROUP;
import com.renren.mobile.chat.activity.ThreadPool;
import com.renren.mobile.chat.base.model.ChatBaseItem;
import com.renren.mobile.chat.base.util.SystemUtil;
import com.renren.mobile.chat.common.DateFormat;
import com.renren.mobile.chat.holder.ChatItemHolder;
import com.renren.mobile.chat.model.warpper.ChatMessageFactory;
import com.renren.mobile.chat.model.warpper.ChatMessageWarpper;
import com.renren.mobile.chat.model.warpper.ChatMessageWarpper_Null;
import com.renren.mobile.chat.model.warpper.ChatMessageWarpper_Null.OnFeedDeleteListener;
import com.renren.mobile.chat.newsfeed.NewsFeedWarpper;
import com.renren.mobile.chat.ui.contact.C_ContactsData;
import com.renren.mobile.chat.ui.contact.ContactBaseModel;
import com.renren.mobile.chat.ui.contact.UserInfoActivity;

/**
 * @author dingwei.chen
 * @说明 聊天主界面的适配器(将进行数据检验)
 * */
public abstract class BaseChatListAdapter extends BaseAdapter  {

	protected List<ChatMessageWarpper> mChatMessages = new ArrayList<ChatMessageWarpper>();
	Set<String> mIdSet = new HashSet<String>();
	protected Context mContext = null;
	protected OnNotifyCallback mNotifyCallback = null;
	protected OnItemLongClickCallback mItemLongClickCallback = null;
	protected OnErrorCallback mErrorCallback = null;
	protected ChatMessageWarpper_Null mMessageNull = ChatMessageFactory.getInstance().obtain(ChatMessageWarpper_Null.class);
	protected long mToChatUserId = 0;
	protected String mToChatUserDomain = "";
	protected Handler mHandler = new Handler();
	public static final long MINITE = 60 * 1000L;
	protected static final long INNER_TIME = 5 * MINITE;// 十分钟为间隔周期
	protected static final int INNER_MESSAGE_NUMBER = 10;// 期内20条

	protected long mStartTime = 0l;
	protected int mStartIndex = 0;
	protected int mStartMessageComeFrom = 0;
//	protected OnViewUserInfoCallback mViewUserInfoCallback = null;
	public boolean mIsLoadImage = true;
	public int mIsGroup = GROUP.CONTACT_MODEL.Value;
	private boolean mIsInsertEnable = false;
	
	public void setIsEnable(boolean isEnable){
		this.mIsInsertEnable = isEnable;
	}
	
	public void setIsGroup(int isGroup){
		this.mIsGroup = isGroup;
	}
	public void reset(){
		mStartTime = 0l;
		mStartIndex = 0;
		mStartMessageComeFrom = 0;
	}
	public void resetData(){
		this.clearList();
		this.mIsSyning = false;
		mMessageNull.setNewsFeedModel(null);
		ThreadPool.obtain().executeMainThread(new Runnable() {
			public void run() {
//				add(mMessageNull);
				addNullMessage();
				notifyDataSetChanged();
			}
		});
	}
	public BaseChatListAdapter(Context context) {
		this.mContext = context;
//		this.add(mMessageNull);
		addNullMessage();
	}
	
	protected void addChatMessage(ChatMessageWarpper message) {
		if(message.mGroupId!=mToChatUserId){
			return;
		}
		{//? 去重 start ?
				for(ChatMessageWarpper m:mChatMessages){
					if(m.mMessageId==message.mMessageId){
						return;
					}
				}
				if(message.mComefrom==ChatBaseItem.MESSAGE_COMEFROM.OUT_TO_LOCAL || message.mComefrom==ChatBaseItem.MESSAGE_COMEFROM.NOTIFY){
					for(ChatMessageWarpper m:mChatMessages){
						if(m.mMessageKey!=null&&m.mMessageKey.trim().length()>0&&m.mMessageKey.equals(message.mMessageKey)){//排除重复消息
							return;
						}
					}
				}
		}//? 去重 end ?
		this.add(message);
		if(getCount()>1){
			this.processTime(message, this.getCount() - 1);
		}else{
			this.processTime();
		}
		this.onMessageAddAdapter(message);
	}
	protected void onMessageAddAdapter(ChatMessageWarpper message){
		if(!mIsSyning){//同步中不自动加载
			message.onAddToAdapter();//实现自动下载和自动播放的逻辑
		}
	}
	
	
	
	protected void addChatMessage(ChatMessageWarpper message,int index) {
		if(message.mGroupId!=this.mToChatUserId){
			return;
		}
		if(this.add(index, message)){
			this.processTime(message, this.getCount() - 1);
			this.onMessageAddAdapter(message);
		}
	}
	private void removeNullMessage(){
		if(this.mChatMessages.size()>0&&this.mMessageNull==this.mChatMessages.get(this.mChatMessages.size()-1)){
			this.mChatMessages.remove(this.mChatMessages.size()-1);
			SystemUtil.log("remove", "remove null");
		}
	}
	private void addNullMessage(){
		if(this.mChatMessages.size()==0||(this.mChatMessages.size()>0&&this.mMessageNull!=this.mChatMessages.get(this.mChatMessages.size()-1))){
			this.mChatMessages.add(this.mMessageNull);
			SystemUtil.log("remove", "add null");
		}
		
	}
	private boolean checkContainId(ChatMessageWarpper m){
		if(m.mMessageKey!=null && m.mMessageKey.trim().length()>0){
			if(this.mIdSet.contains(m.mMessageKey)){
				return true;
			}
			this.mIdSet.add(m.mMessageKey);
		}
		return false;
	}
	private boolean add(ChatMessageWarpper m){
		SystemUtil.log("listadd", "add1 "+m.getClass().getSimpleName());
		this.removeNullMessage();
		if(!this.checkContainId(m)){
			mChatMessages.add(m);
			this.addNullMessage();
			return true;
		}
		return false;
	}
	private boolean add(int index,ChatMessageWarpper message){
		SystemUtil.log("listadd", "add2 "+message.getClass().getSimpleName());
		this.removeNullMessage();
		if(!this.checkContainId(message)){
			if(index>mChatMessages.size()){
				index = mChatMessages.size();
			}
			mChatMessages.add(index,message);
		}
		this.addNullMessage();
		return true;
	}
	private boolean addAll(List<ChatMessageWarpper> messages){
		boolean flag = false;
		this.removeNullMessage();
		for(ChatMessageWarpper m:messages){
			if(!this.checkContainId(m)){
				mChatMessages.add(m);
				flag = true;
			}
		}
		this.addNullMessage();
		return flag;
	}
	private boolean clearList(){
		mChatMessages.clear();
		mIdSet.clear();
		return true;
	}
	protected boolean remove(ChatMessageWarpper m){
		mChatMessages.remove(m);
		this.mIdSet.remove(m.mMessageKey);
		return true;
	}
	protected boolean remove(int index){
		ChatMessageWarpper m =mChatMessages.remove(index);
		if(m!=null){
			this.mIdSet.remove(m.mMessageKey);
		}
		return true;
	}
	
	
	protected void addChatMessage(List<ChatMessageWarpper> message) {
		if(addAll(message)){
			this.processTime();
		}
	}
	
	
	
	public void addChatMessageAndNotifyFromHead(List<ChatMessageWarpper> messages, boolean flag) {
		int k = 0;
		for(ChatMessageWarpper m:messages){
			this.addChatMessage(m,k++);
		}
		if (mChatMessages.size() > 0) {
			this.processTime();
			if(flag){
				this.notifyDataSetInvalidated();
			}
		}
	}

	// 得到对话列表中的最早时间
	public long getEarlyTime() {
		if (mChatMessages == null || mChatMessages.size() <= 1) {//因为最后添加了一个空消息
			return System.currentTimeMillis();
		} else {
			return mChatMessages.get(0).mMessageReceiveTime;
		}
	}
	
	// 得到对话列表中的最早时间
		public int getEarlyId() {
			if (mChatMessages == null || mChatMessages.size() <= 1) {//因为最后添加了一个空消息
				return Integer.MAX_VALUE;
			} else {
				return mChatMessages.get(0).mMessageId;
			}
		}
	
	

	public void addAndNotifyCallback(ChatMessageWarpper message) {
		addChatMessage(message);
//		this.notifyDataSetChanged();
	}
	
	public void notifyCallback(){
		if(mNotifyCallback!=null){
			mNotifyCallback.onNotify(getCount());
		}
	}
	

	@Override
	public int getCount() {
		if (this.mChatMessages == null) {
			return 0;
		}
		return this.mChatMessages.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return this.mChatMessages.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ChatItemHolder holder = null;
		if (convertView != null) {
			holder = (ChatItemHolder) convertView.getTag();
		} else {
			holder = new ChatItemHolder(this.mContext);
			holder.getRootView().setTag(holder);
		}
		ChatMessageWarpper data = mChatMessages.get(position);
		holder.unregistorSubject();
		data.registorObserver(holder);
		holder.selectView(data.mComefrom, data.getMessageType());
		this.setData(holder, data);
		return  holder.getRootView();
	}

	// 数据变化时候的回调
	public static interface OnNotifyCallback {
		public void onNotify(int count);
		public void onAllDataDelete();
	}

	public void setNotifyCallback(OnNotifyCallback callback) {
		mNotifyCallback = callback;
	}

	// 条目长按时候回调
	public static interface OnItemLongClickCallback {
		public void onItemLongClick(ChatMessageWarpper item);
	}

	public void setOnItemLongClickCallback(OnItemLongClickCallback callback) {
		this.mItemLongClickCallback = callback;
	}

	public static interface OnErrorCallback {
		public void onErrorCallback(ChatMessageWarpper item);
	}

	public void setOnErrorCallback(OnErrorCallback callback) {
		this.mErrorCallback = callback;
	}

	public void setData(ChatItemHolder holder, ChatMessageWarpper data) {
		this.showHead(holder, data);// 显示头像
		this.showTime(holder, data);// 显示时间
		this.showName(holder, data);
		data.warpView(holder, mItemLongClickCallback, mErrorCallback, mIsLoadImage);// 装饰界面
	}

	public void showName(ChatItemHolder holder, ChatMessageWarpper data){
		if(data.mIsGroupMessage==GROUP.GROUP.Value){
			holder.mMessage_UserName.setVisibility(View.VISIBLE);
			if(data.mComefrom==ChatBaseItem.MESSAGE_COMEFROM.OUT_TO_LOCAL){
				holder.mMessage_UserName.setText(data.mUserName);
			}else{
				holder.mMessage_UserName.setVisibility(View.GONE);
			}
		}else{
			holder.mMessage_UserName.setVisibility(View.GONE);
		}
		
	}
	
	
	private void showTime(ChatItemHolder holder, ChatMessageWarpper data) {
		if (data.mIsShowTime && data!=mMessageNull) {
			holder.mMessage_Time_TextView.setVisibility(View.VISIBLE);
			String date = DateFormat.getDateByChat(data.mMessageReceiveTime);
			String time = DateFormat.getNowStrByChat(data.mMessageReceiveTime);
			holder.mMessage_Time_TextView.setText(date + " " + time);
		} else {
			holder.mMessage_Time_TextView.setVisibility(View.GONE);
		}
	}

	private void showHead(ChatItemHolder holder, ChatMessageWarpper data) {
		holder.mMessage_HeadImage_ImageView.clear();
		if(data.mComefrom == ChatBaseItem.MESSAGE_COMEFROM.OUT_TO_LOCAL){
			holder.mMessage_HeadImage_ImageView.setOnClickListener(new OnClickViewUserInfoListener(data));
			if(TextUtils.isEmpty(data.mHeadUrl) ){
				ContactBaseModel contact = C_ContactsData.getInstance().getContact(data.mToChatUserId,data.mDomain);
				if(contact !=null){
					data.mHeadUrl = contact.getmHeadUrl();
				}
			}
			holder.mMessage_HeadImage_ImageView.addUrl(data.mHeadUrl);
		}else{
			SystemUtil.log("head", "head url ");
			holder.mMessage_HeadImage_ImageView.addUrl(LoginManager.getInstance().getLoginInfo().mMediumUrl);
			holder.mMessage_HeadImage_ImageView.setOnClickListener(null);
		}
		
	}
	String mDefualtHeadUrl = null;
	public void setDefaultHeadUrl(String defualtUrl){
		mDefualtHeadUrl = defualtUrl;
	}
	
	
	
	public void setToChatUserId(long toChatUserId) {
		this.mToChatUserId = toChatUserId;
	}

	public void processTime() {
		
		mStartTime = this.getEarlyTime();// 记录开始时间
		mStartIndex = 0;
		int length = mChatMessages.size();// 得到长度
		if (length > 0) {
			mStartMessageComeFrom = mChatMessages.get(0).mComefrom;
			mChatMessages.get(0).mIsShowTime = true;//update
		}
		ChatMessageWarpper message = null;
		for (int k = 1; k < length; k++) {
			message = mChatMessages.get(k);
			if(this.processTime(message, k)){
				break;
			};
		}
	}
	
	
	
	
	public boolean processTime(ChatMessageWarpper message, int index) {
		if(message.mIsShowTime){
			return true;
		}
		if (isTimeOver(message.mMessageReceiveTime, mStartTime)) {// 如果超过时间
			message.mIsShowTime = true;//update
			mStartTime = message.mMessageReceiveTime;
			mStartMessageComeFrom = message.mComefrom;
			mStartIndex = index;
			message.mIsShowHead = true;
		} else {
			if (index - mStartIndex >= INNER_MESSAGE_NUMBER) {// 如果超过内置条数
				mStartTime = message.mMessageReceiveTime;
				message.mIsShowTime = true;//update
				mStartIndex = index;
				message.mIsShowHead = true;
			} else {
				message.mIsShowTime = false;
				if (mStartMessageComeFrom != message.mComefrom) {
					message.mIsShowHead = true;
					mStartMessageComeFrom = message.mComefrom;
				} else {
					message.mIsShowHead = false;
				}
			}
		}
		return false;
	}

	private boolean isTimeOver(long endTime, long startTime) {
		return Math.abs((endTime - startTime)) >= INNER_TIME;
	}

	public void clear() {
				if (mChatMessages != null) {
					for (ChatMessageWarpper message : mChatMessages) {
						message.unregistor();
						ChatMessageFactory.getInstance().recycle(message);
					}
					mChatMessages.clear();
					mChatMessages.add(mMessageNull);
					notifyDataSetChanged();
				}
		
	}
	public class OnClickViewUserInfoListener implements OnClickListener{
		ChatMessageWarpper mMessage = null;
		public OnClickViewUserInfoListener(ChatMessageWarpper message){
			mMessage = message;
		}
		@Override
		public void onClick(View v) {
			UserInfoActivity.show(mContext, mMessage.mToChatUserId,mMessage.mDomain);
		}
	}

	public long getToChatId() {
		return this.mToChatUserId;
	}
//	NewsFeedWarpper mFeedMessage = null;
	public void setFeed(NewsFeedWarpper feed,OnFeedDeleteListener listener){
		mMessageNull.setNewsFeedModel(feed);
		if(feed!=null){
			mMessageNull.setOnFeedDeleteListener(listener);
		}else{
			mMessageNull.mFeedId=-1;
		}
	}
	private boolean mIsSyning = false;
	public void setIsSyning(boolean isSyning){
		mIsSyning = isSyning;
	}
	
}
