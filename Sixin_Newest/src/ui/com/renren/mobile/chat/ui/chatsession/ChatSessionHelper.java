package com.renren.mobile.chat.ui.chatsession;

import android.content.ContentValues;
import android.text.TextUtils;

import com.common.network.DomainUrl;
import com.renren.mobile.chat.base.model.ChatBaseItem;
import com.renren.mobile.chat.common.TextualUtil;
import com.renren.mobile.chat.dao.ChatSessionDAO;
import com.renren.mobile.chat.dao.DAOFactoryImpl;
import com.renren.mobile.chat.database.ChatSession_Column;
import com.renren.mobile.chat.model.warpper.ChatMessageWarpper;

/**
 * @author rubin.dong@renren-inc.com
 * @data 对话列表数据控制
 * */

public class ChatSessionHelper {
	
	private static ChatSessionHelper sInstance = new ChatSessionHelper();
	ChatSessionDAO mSessionDAO = null;
	
	private ChatSessionHelper(){
		mSessionDAO = DAOFactoryImpl.getInstance().buildDAO(ChatSessionDAO.class);
	}
	
	public static ChatSessionHelper getInstance(){
		return sInstance;
	}
	
	/*插入一条数据*/
	public void insertToTheDatabase(ChatMessageWarpper message){
		mSessionDAO.insertChatSession(message);
	}
	
	/*删除一条数据*/
	public void deleteChatSessionByGroupId(long groupId){
		mSessionDAO.deleteChatSessionByGroupId(groupId);
	}
	
	/*变更数据库*/
	public int updateMessage(ChatMessageWarpper messageWarpper,String messageContent){
		ContentValues value = new ContentValues(7);
		value.put(ChatSession_Column.MESSAGE, messageContent);
		value.put(ChatSession_Column.MESSAGE_ID, messageWarpper.mMessageId);
		value.put(ChatSession_Column.MESSAGE_STATE, messageWarpper.mMessageState);
		value.put(ChatSession_Column.CHAT_TIME, messageWarpper.mMessageReceiveTime);
		value.put(ChatSession_Column.COME_FROM, messageWarpper.mComefrom);
		value.put(ChatSession_Column.MESSAGE_TYPE, messageWarpper.mMessageType);
		value.put(ChatSession_Column.TO_CHAT_NAME, messageWarpper.mUserName);
		if(!TextUtils.isEmpty(messageWarpper.mDomain)){
			value.put(ChatSession_Column.DOMAIN, messageWarpper.mDomain);
		}else{
			value.put(ChatSession_Column.DOMAIN, DomainUrl.SIXIN_DOMAIN);
		}
		int row = mSessionDAO.updateByGroupId(messageWarpper.mGroupId, value);
		return row;
	}
	
	/*变更数据库*/
	public void updateMessageContent(int messageId, String messageContent, long groupId){
		ContentValues value = new ContentValues(2);
		value.put(ChatSession_Column.MESSAGE, messageContent);
		value.put(ChatSession_Column.MESSAGE_ID, messageId);
		mSessionDAO.updateByGroupId(groupId, value);
	}
	
	public void updateMessageTime(long groupId, int messageId, long time){
		ContentValues value = new ContentValues(2);
		value.put(ChatSession_Column.MESSAGE_ID, messageId);
		value.put(ChatSession_Column.CHAT_TIME, time);
		mSessionDAO.updateByGroupId(groupId, value);
	}
	
	/*更新消息发送状态*/
	public void updateMessageState(long messageId, ContentValues values){
		mSessionDAO.updateByMessageId(messageId, values);
	}
	
	/*查询单人聊天的总数*/
	public int querySinglePersonHistory(){
		return this.mSessionDAO.queryCountSinglePerson();
	}
	
	/*从数据库中删除所有人的聊天记录*/
	public void deleteAll(){
		mSessionDAO.deleteAll();
	}

}
