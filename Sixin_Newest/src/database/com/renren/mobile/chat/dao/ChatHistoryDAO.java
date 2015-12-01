package com.renren.mobile.chat.dao;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import android.content.ContentValues;

import com.common.manager.LoginManager;
import com.core.database.BaseDAO;
import com.core.database.BaseDBTable;
import com.core.database.DAO;
import com.core.database.Query;
import com.core.orm.ORMUtil;
import com.renren.mobile.chat.RenrenChatApplication;
import com.renren.mobile.chat.activity.RenRenChatActivity.CanTalkable.GROUP;
import com.renren.mobile.chat.activity.ThreadPool;
import com.renren.mobile.chat.base.model.ChatBaseItem;
import com.renren.mobile.chat.base.model.ChatBaseItem.MESSAGE_TYPE;
import com.renren.mobile.chat.base.util.SystemUtil;
import com.renren.mobile.chat.database.ChatHistory_Column;
import com.renren.mobile.chat.model.warpper.ChatMessageWarpper;
import com.renren.mobile.chat.sql.Query_ChatHistory;
import com.renren.mobile.chat.sql.Query_ChatMessage;
import com.renren.mobile.chat.sql.Query_ChatSession;
import com.renren.mobile.chat.sql.Query_Count;
import com.renren.mobile.chat.ui.chatsession.ChatSessionDataModel;
import com.renren.mobile.chat.ui.chatsession.ChatSessionHelper;
import com.renren.mobile.chat.util.CurrentChatSetting;

/**
 * @author dingwei.chen
 * @说明 每一个DAO对应一张数据库表,表示附加在这个表上的所有操作的集合
 * @说明 DAO的实现类都继承于 {@link com.renren.mobile.chat.base.database.BaseDAO}
 * 		DAO的子类只是为了向外部提供更加好用的数据库查询接口一定不要直接操纵
 * @see com.renren.mobile.chat.base.database.BaseDAO 
 * 		使用说明参考BaseDAO的头部说明(任何quid操作都调用BaseDAO的提供的方法不可直接操作database)
 * 
 * */
public class ChatHistoryDAO extends BaseDAO {
	
	NewsFeedDAO mFeedDAO = null;
	private List<ChatHistoryDAOObserver> mObservers = new LinkedList<ChatHistoryDAOObserver>();
	public ChatHistoryDAO(BaseDBTable table) {
		super(table);
		mFeedDAO =DAOFactoryImpl.getInstance().buildDAO(NewsFeedDAO.class);
	}
	/*查询历史表*/
	Query mQuery_ChatHistory = new Query_ChatHistory(this);
	/*查询历史表数目*/
	Query mQuery_ChatHistoryCount = new Query_Count(this);
	/*查询会话列表*/
	Query mQuery_Session = new Query_ChatSession(this);
	/*查询单条聊天记录*/
	Query mQuery_ChatMessage = new Query_ChatMessage(this);
	
	
	
	
	public ChatMessageWarpper queryLastChatHistoryMessageByToChatId(long userId){
		String[] whereString =new String[]{ 
				ChatHistory_Column.LOCAL_USER_ID +" = "+LoginManager.getInstance().getLoginInfo().mUserId ,
				ChatHistory_Column.TO_CHAT_ID +" = "+userId
				};
		ChatMessageWarpper m = (ChatMessageWarpper)this.query1(mQuery_ChatMessage, null, whereString, null, ChatHistory_Column.CHAT_TIME+""+DAO.ORDER.DESC, null);
		return m;
	}
	
	public ChatMessageWarpper queryLastChatHistoryMessageByGroupId(long groupId,int isgroup){
		String[] whereString =new String[]{ 
				ChatHistory_Column.LOCAL_USER_ID +" = "+LoginManager.getInstance().getLoginInfo().mUserId ,
				ChatHistory_Column.GROUP_ID +" = "+groupId,
				ChatHistory_Column.IS_GROUP_MESSAGE+"="+isgroup
		};
		ChatMessageWarpper m = (ChatMessageWarpper)this.query1(mQuery_ChatMessage, null, whereString, null, ChatHistory_Column.CHAT_TIME+""+DAO.ORDER.DESC, null);
		return m;
	}
	
	/**
	 * 查询和单个人的对话记录(最近)
	 * 
	 * @param localId
	 *            本地用户ID
	 * @param page
	 *            (从0开始)
	 * @param toChatId
	 *            聊天对象ID
	 * @param pageSize
	 *            页面大小 ==测试用例正常
	 * */
//	@SuppressWarnings("unchecked")
//	public List<ChatMessageWarpper> querySinglePersonChatHistory(long localId, long toChatId, int pageSize, int page, long time) {
//
//		String[] whereStrings = new String[]{
//				ChatHistory_Column.LOCAL_USER_ID+" = "+localId,
//				ChatHistory_Column.TO_CHAT_ID + "=" + toChatId,
//				ChatHistory_Column.CHAT_TIME + " < " + time
//		};
//		String limit = 0 + "," + pageSize;//让它多查出条
//		List<ChatMessageWarpper> list = (List<ChatMessageWarpper>)
//								this.query1(mQuery_ChatHistory, 
//										null, 
//										whereStrings,
//										null, 
//										ChatHistory_Column.CHAT_TIME+""+DAO.ORDER.DESC, 
//										limit);
//		return list;
//	}

	
	
	@SuppressWarnings("unchecked")
	public List<ChatMessageWarpper> querySinglePersonChatHistory(long localId, long toChatId, int pageSize, int page, long time,int isGroup) {

		String[] whereStrings = new String[]{
				ChatHistory_Column.LOCAL_USER_ID+" = "+localId,
				(ChatHistory_Column.GROUP_ID+" = "+toChatId),
				ChatHistory_Column.CHAT_TIME + " < " + time,
				ChatHistory_Column.IS_GROUP_MESSAGE +"="+isGroup
		};
		String limit = 0 + "," + pageSize;//让它多查出条
		List<ChatMessageWarpper> list = (List<ChatMessageWarpper>)
								this.query1(mQuery_ChatHistory, 
										null, 
										whereStrings,
										null, 
										ChatHistory_Column.CHAT_TIME+""+DAO.ORDER.DESC, 
										limit);
		return list;
	}
	
	
	@SuppressWarnings("unchecked")
	public List<ChatMessageWarpper> querySinglePersonChatHistory(long localId, long toChatId, int pageSize, int id,int isGroup) {

		String[] whereStrings = new String[]{
				ChatHistory_Column.LOCAL_USER_ID+" = "+localId,
				(ChatHistory_Column.GROUP_ID+" = "+toChatId),
				ChatHistory_Column._ID + " < " + id,
				ChatHistory_Column.IS_GROUP_MESSAGE +"="+isGroup
		};
		String limit = 0 + "," + pageSize;//让它多查出条
		List<ChatMessageWarpper> list = (List<ChatMessageWarpper>)
								this.query1(mQuery_ChatHistory, 
										null, 
										whereStrings,
										null, 
										ChatHistory_Column._ID+""+DAO.ORDER.DESC, 
										limit);
		return list;
	}
	
	
	
	
	
	/**
	 * 查询聊天记录表中的记录数
	 * */
	public int queryChatHistoryCount() {
		return  this.getTableRecordCount2(null);

	}

	/**
	 * 查询单人聊天的总数
	 * */
	public int queryCountSinglePerson(long localId, long groupId) {
		String[] whereSqls = new String[]{
				ChatHistory_Column.LOCAL_USER_ID+"="+localId,
				ChatHistory_Column.GROUP_ID + "=" + groupId
		};
		return this.getTableRecordCount1(whereSqls);
	}
	/**
	 * 查询单人聊天的总数
	 * */
	public int queryCountSinglePerson(long localId) {
		String[] whereSqls = new String[]{
				ChatHistory_Column.LOCAL_USER_ID+"="+localId
		};
		return this.getTableRecordCount1(whereSqls);
	}
	
	
	
	/**
	 * 查询未读的消息列表
	 * */
	public ArrayList<ChatMessageWarpper> queryUnreadMessageList() {
		
		String whereString = ChatHistory_Column.READ + "=" + ChatBaseItem.MESSAGE_READ.UNREAD +" AND "+ChatHistory_Column.LOCAL_USER_ID +" = "+LoginManager.getInstance().getLoginInfo().mUserId ;
		ArrayList<ChatMessageWarpper> list = (ArrayList<ChatMessageWarpper>)
										this.query2(mQuery_ChatHistory, null, whereString, null, ChatHistory_Column.CHAT_TIME+""+DAO.ORDER.DESC, null);
		return list;
	}
	
	/**
	 * 查询当前用户未读的消息列表数量
	 * */
	public int queryUnreadMessageListById() {
		
//		String[] whereString =new String[]{ 
//				ChatHistory_Column.LOCAL_USER_ID +" = "+LoginManager.getInstance().getLoginInfo().mUserId ,
//				ChatHistory_Column.READ + "=" + ChatBaseItem.MESSAGE_READ.UNREAD
//				};
		
		String whereString = ChatHistory_Column.READ + "=" + ChatBaseItem.MESSAGE_READ.UNREAD +" AND "+ChatHistory_Column.LOCAL_USER_ID +" = "+LoginManager.getInstance().getLoginInfo().mUserId ;
		
		ArrayList<ChatMessageWarpper> list = (ArrayList<ChatMessageWarpper>)
										this.query2(mQuery_ChatHistory, null, whereString, null, ChatHistory_Column.CHAT_TIME, null);
		return list.size();
	}

	public void readMessage(long id) {
		ContentValues cv = new ContentValues();
		cv.put(ChatHistory_Column.READ, ChatBaseItem.MESSAGE_READ.READ);
		String whereString = ChatHistory_Column._ID+" = "+ String.valueOf(id);
		super.update2(whereString, cv);
	}

	/**
	 * 删除记录
	 * */
	public void deleteChatMessage(ChatMessageWarpper message) {
		if(message.hasNewsFeed() && message.mNewsFeedMessage!=null){
			long id = mFeedDAO.deleteNewsFeedModel(message.mNewsFeedMessage);
		}
		String whereString = ChatHistory_Column._ID + " = " + message.mMessageId;
		this.delete2(whereString);
		this.onDelete(ChatHistory_Column._ID ,message.mMessageId);
	}

	/**
	 * 删除某个人的聊天记录
	 * */
	public void deleteChatMessageByUserId(long id) {
		String whereString = ChatHistory_Column.TO_CHAT_ID + " = " + id +" AND "+ChatHistory_Column.LOCAL_USER_ID +" = "+LoginManager.getInstance().getLoginInfo().mUserId;
		this.delete2(whereString);
		this.onDelete(ChatHistory_Column.TO_CHAT_ID, id);
	}
	
	public void deleteChatMessageByGroupId(long groupId){
		String whereString = ChatHistory_Column.GROUP_ID + " = " + groupId +" AND "+ChatHistory_Column.LOCAL_USER_ID +" = "+LoginManager.getInstance().getLoginInfo().mUserId;
		this.delete2(whereString);
		this.onDelete(ChatHistory_Column.GROUP_ID, groupId);
		
	}

	/**
	 * 删除某个人的聊天记录
	 * update : 添加组信息
	 * */
	public void deleteChatMessageByUserId(long id,int isGroup) {
		String whereString =null;
		if(isGroup==GROUP.CONTACT_MODEL.Value){
			whereString = ChatHistory_Column.TO_CHAT_ID + " = " + id +" AND "+ChatHistory_Column.LOCAL_USER_ID +" = "+LoginManager.getInstance().getLoginInfo().mUserId;
		}else{
			whereString = ChatHistory_Column.GROUP_ID + " = " + id +" AND "+ChatHistory_Column.LOCAL_USER_ID +" = "+LoginManager.getInstance().getLoginInfo().mUserId;
		}
		this.delete2(whereString);
		this.onDelete(ChatHistory_Column.TO_CHAT_ID, id);
	}
	
	/**
	 * 删除所有人的聊天记录
	 * */
	public void deleteAll() {
		this.delete2(null);
		this.onDelete(ChatHistory_Column.TO_CHAT_ID, 0);
	}

	/**
	 * 插入一条聊天记录 说明 让模型自己告诉数据库自己要插入那些数据
	 * */

	public void insertChatMessage(ChatMessageWarpper message) {
		if(message==null||message.mMessageType==MESSAGE_TYPE.UNKNOW){
			return;
		}
		if(message.mMessageReceiveTime<=0L){
			message.mMessageReceiveTime = System.currentTimeMillis();
		}
		if(message.hasNewsFeed()){
			if(message.mNewsFeedMessage!=null){//如果消息携带新鲜事信息
				message.mFeedId = mFeedDAO.insertNewsFeedModel(message.mNewsFeedMessage);
			}
		}
		message.mMessageId = (int)this.insert(message.getValueToInsert());
		onInsert(message);
	}

	/**
	 * 去重MSG-KEY
	 * */
	public void cancelRepeatMessageList(ArrayList<ChatMessageWarpper> messageList) {
		Set<String> set = new HashSet<String>(messageList.size()+1);
		for (ChatMessageWarpper message:messageList) {
			if(message.mMessageType==MESSAGE_TYPE.UNKNOW){
				message.mIsSuccessInsert = false;
				continue;
			}
			/*去重*/
			if(message.mMessageKey.length()>1){
				if(set.contains(message.mMessageKey)){//如果存在
					message.mIsSuccessInsert = false;//插入不成功
					continue;
				}else{
					
					set.add(message.mMessageKey);
					if(this.queryContainMessage(message)){//数据库中已经有该消息鸟
						message.mIsSuccessInsert = false;//插入不成功
						continue;
					}
				}
			}
			message.mIsSuccessInsert = true;
			if (!CurrentChatSetting.isChatActivityTop() //不在聊天窗口
			||  CurrentChatSetting.CHAT_ID != message.mGroupId//聊天对象不正确
			) {
				message.mRead = ChatBaseItem.MESSAGE_READ.UNREAD;//置为未读
			}else{
				message.mRead = ChatBaseItem.MESSAGE_READ.READ;//置为已读
			}
			
			if(message.mMessageType == ChatBaseItem.MESSAGE_TYPE.SOFT_INFO){
				message.mRead = ChatBaseItem.MESSAGE_READ.READ;
			}else if(message.mIsSyn){
				message.mRead = ChatBaseItem.MESSAGE_READ.READ;
			}
		}
	}
	
	
	/**
	 * 插入多条聊天记录 运用事务(轮询时候使用)网络回调的时候用这个方法
	 * */
	public void insertChatMessageList(final ArrayList<ChatMessageWarpper> messageList) {
		if(messageList==null||messageList.size()==0){
			return;
		}
		
		for (ChatMessageWarpper message:messageList) {
			message.mMessageReceiveTime = message.mMessageReceiveTime<=0?System.currentTimeMillis():message.mMessageReceiveTime;
			if(message.mIsSuccessInsert 
		    && message.mLocalUserId == LoginManager.getInstance().getLoginInfo().mUserId//避免注销以后还能收到消息
			){
				message.onPreInsertDB();//插入DB前的回调,语音消息通过这个回调来实现语音的后台自动下载
			}
		}
		onInsert(messageList);
		SystemUtil.log("syn", "run thread = "+Thread.currentThread());
		ThreadPool.obtain().execute(new Runnable() {
			@Override
			public void run() {
				beginTransaction();
				for (ChatMessageWarpper message:messageList) {
					if(message.mIsSuccessInsert){
						if(message.mNewsFeedMessage!=null){//如果消息携带新鲜事信息
							message.mFeedId = mFeedDAO.insertNewsFeedModel(message.mNewsFeedMessage);
						}
						message.mMessageId  = (int)insert(message.getValueToInsert());
						
						int row = ChatSessionHelper.getInstance().updateMessage(message,message.getDescribe());
						if(row <= 0){//如果数据库中没有这条记录
							ChatSessionHelper.getInstance().insertToTheDatabase(message);
						}
					}
				}
				commit();
			}
		});
		
	}

	/**
	 * @说明 查是否含有记录
	 * */
	public boolean queryContainMessage(ChatMessageWarpper message){
		ChatMessageWarpper m = mQuery_ChatMessage.query(
				null,
				ChatHistory_Column.MESSAGE_KEY+" = '"+message.mMessageKey+"'" ,
				null, null, null,ChatMessageWarpper.class);
		return m!=null;
	}
	
	
	/**
	 * @说明 这个方法用于会话列表页面获得该使用者的所有会话列表
	 * @param uid表示的是当前登录用户的ID
	 * */
	public List<ChatSessionDataModel> query_ChatSessions(long uid) {
		/*
		 * 拼接以后结果: select * from chathistory_table where localid = uid group by
		 * to_chat_id order by chat_time desc;
		 */
		String where = ChatHistory_Column.LOCAL_USER_ID + "=" + uid ;
		
		@SuppressWarnings("unchecked")
		List<ChatSessionDataModel> messages = mQuery_Session.query(null, where, 
												ChatHistory_Column.GROUP_ID+","+ChatHistory_Column.IS_GROUP_MESSAGE,
												ChatHistory_Column.CHAT_TIME+DAO.ORDER.DESC,
												null, List.class);
		return messages;
	}

	public void update(ChatMessageWarpper message, ContentValues values,boolean isNotifyUpdate) {
		String whereStr =  ChatHistory_Column._ID + " = " + message.mMessageId;
		this.update2(whereStr, values);
		ORMUtil.getInstance().ormUpdate(ChatMessageWarpper.class, message, values);
		this.onUpdate(ChatHistory_Column._ID, (int)message.mMessageId, values);
	}
	

	/**
	 * 更新聊天记录的已读状态
	 * */
	public void readUserAllMessage(final long userId) {
		final ContentValues cv = new ContentValues();
		cv.put(ChatHistory_Column.READ, ChatBaseItem.MESSAGE_READ.READ);
		String whereStr =  ChatHistory_Column.GROUP_ID + " = " + userId+" AND "+ChatHistory_Column.LOCAL_USER_ID +" = "+LoginManager.getInstance().getLoginInfo().mUserId;
		this.update2(whereStr, cv);
		RenrenChatApplication.sHandler.post(new Runnable() {
			public void run() {
//				for (NewDAOObserver observer : mNewObservers) {
//					((ChatHistoryDAOObserver)observer).onUpdate(
//							ChatHistory_Column.TO_CHAT_ID,
//							(int)userId,
//							cv);
//					
//				}
				for(ChatHistoryDAOObserver observer : mObservers){
					observer.onUpdate(
							ChatHistory_Column.TO_CHAT_ID,
							userId,
							cv);
				}
			}
		});
	}
	
	
	
	
	public void onInsert(final ChatMessageWarpper message){
		post(new Runnable() {
			public void run() {
				for(ChatHistoryDAOObserver observer : mObservers){
					observer.onInsert(message);
				}
			}
		});
		
	}
	
	public void onInsert(final List<ChatMessageWarpper> messages){
		post(new Runnable() {
			public void run() {
				try {
					for(ChatHistoryDAOObserver observer : mObservers){
						SystemUtil.log("@@@", "ob = "+observer);
						observer.onInsert(messages);
					}
				} catch (Exception e) {
					// TODO: handle exception
					SystemUtil.log("@@@", "Exception = "+e);
				}
				
			}
		});
		
	}
	
	
	public void onDelete(final String columnName,final long _id){
		post(new Runnable() {
			public void run() {
				for(ChatHistoryDAOObserver observer :mObservers){
					observer.onDelete(columnName,_id);
				}
			}
		});
	}
	public void onUpdate(final String column,final int _id,final ContentValues values){
		post(new Runnable() {
			public void run() {
				for(ChatHistoryDAOObserver observer :mObservers){
					observer.onUpdate(column, _id, values);
				}
			}
		});
	}
	
	public void registorObserver(ChatHistoryDAOObserver observer){
		if(!this.mObservers.contains(observer)){
			this.mObservers.add(observer);
		}
	}
	public void unregistorObserver(ChatHistoryDAOObserver observer){
		this.mObservers.remove(observer);
	}

}
