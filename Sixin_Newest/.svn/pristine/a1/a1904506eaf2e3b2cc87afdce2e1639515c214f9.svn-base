package com.renren.mobile.chat.dao;

import java.util.List;

import android.content.ContentValues;

import com.common.manager.LoginManager;
import com.core.database.BaseDAO;
import com.core.database.BaseDBTable;
import com.core.database.DAO;
import com.core.database.Query;
import com.core.orm.ORMUtil;
import com.renren.mobile.chat.base.model.ChatBaseItem;
import com.renren.mobile.chat.database.ChatSession_Column;
import com.renren.mobile.chat.model.warpper.ChatMessageWarpper;
import com.renren.mobile.chat.sql.Query_ChatMessage;
import com.renren.mobile.chat.sql.Query_ChatSession;
import com.renren.mobile.chat.ui.chatsession.ChatSessionDataModel;
import com.renren.mobile.chat.ui.contact.C_ContactsData;
import com.renren.mobile.chat.ui.contact.ContactBaseModel;

/**
 * @author rubin.dong@renren-inc.com
 * @data 对话列表数据基本操作
 * */

public class ChatSessionDAO extends BaseDAO {
	
	NewsFeedDAO mFeedDAO = null;

	public ChatSessionDAO(BaseDBTable table) {
		super(table);
		mFeedDAO = DAOFactoryImpl.getInstance().buildDAO(NewsFeedDAO.class);
	}
	
	/*查询会话列表*/
	Query mQuery_Session = new Query_ChatSession(this);
	/*查询单条聊天记录*/
	Query mQuery_ChatMessage = new Query_ChatMessage(this);

	/**
	 * @说明 这个方法用于会话列表页面获得该使用者的所有会话列表
	 * @param uid表示的是当前登录用户的ID
	 * */
	public List<ChatSessionDataModel> query_ChatSessions(long uid) {
		/*
		 * 拼接以后结果: select * from chathistory_table where localid = uid group by
		 * to_chat_id order by chat_time desc;
		 */
		String where = ChatSession_Column.LOCAL_USER_ID + "=" + uid ;
		@SuppressWarnings("unchecked")
		List<ChatSessionDataModel> messages = mQuery_Session.query(null, where, 
					ChatSession_Column.GROUP_ID+","+ChatSession_Column.IS_GROUP_MESSAGE,
					ChatSession_Column.CHAT_TIME+DAO.ORDER.DESC,
					null, List.class);
		
		return messages;
	}
	
	/**
	 * 向数据库中插入一条记录
	 */
	public void insertChatSession(ChatMessageWarpper message){
		ContentValues values = new ContentValues();
		ORMUtil.getInstance().ormInsert(message.getClass(), message, values);
		values.put(ChatSession_Column.MESSAGE_ID, message.mMessageId);
		if(message.mComefrom == ChatBaseItem.MESSAGE_COMEFROM.LOCAL_TO_OUT){
			ContactBaseModel model = C_ContactsData.getContact(message.mToChatUserId, message.mDomain);
			values.put(ChatSession_Column.MESSAGE_ID, model.mContactName);
		}
		mInsert.insert(values);
	}
	
	/**
	 * 数据库中更新一条数据
	 * @param groupId
	 * @param values
	 * @return
	 */
	public int updateByGroupId(long groupId, ContentValues values) {
		String whereStr =  ChatSession_Column.LOCAL_USER_ID + " = " + LoginManager.getInstance().getLoginInfo().mUserId + " AND " + ChatSession_Column.GROUP_ID + " = " + groupId;
		int row = this.update2(whereStr, values);
		return row;
	}
	
	/**
	 * 数据库中更新一条数据
	 * @param messageId
	 * @param values
	 */
	public void updateByMessageId(long messageId, ContentValues values){
		String whereStr =  ChatSession_Column.LOCAL_USER_ID + " = " + LoginManager.getInstance().getLoginInfo().mUserId + " AND " + ChatSession_Column.MESSAGE_ID + " = " + messageId;
		this.update2(whereStr, values);
	}
	
	/**
	 * 从数据库中删除一条数据
	 * @param groupId
	 */
	public void deleteChatSessionByGroupId(long groupId){
		String whereString = ChatSession_Column.GROUP_ID + " = " + groupId +" AND "+ChatSession_Column.LOCAL_USER_ID +" = "+LoginManager.getInstance().getLoginInfo().mUserId;
		this.delete2(whereString);
	}
	
	/**
	 * 查询单人聊天的总数
	 * */
	public int queryCountSinglePerson() {
		String[] whereSqls = new String[]{
				ChatSession_Column.LOCAL_USER_ID+"="+LoginManager.getInstance().getLoginInfo().mUserId
		};
		return this.getTableRecordCount1(whereSqls);
	}
	
	/**
	 * 删除所有人的聊天记录
	 * */
	public void deleteAll() {
		this.delete2(null);
	}
	
}
