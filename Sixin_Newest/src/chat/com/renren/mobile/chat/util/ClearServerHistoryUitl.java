package com.renren.mobile.chat.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.util.Log;

import com.common.mcs.INetRequest;
import com.common.mcs.INetResponse;
import com.common.mcs.McsServiceProvider;
import com.common.utils.Methods;
import com.core.json.JsonObject;
import com.core.json.JsonValue;
import com.renren.mobile.chat.dao.ChatHistoryDAO;
import com.renren.mobile.chat.dao.ChatHistoryDAOObserver;
import com.renren.mobile.chat.dao.DAOFactoryImpl;
import com.renren.mobile.chat.database.ChatHistory_Column;
import com.renren.mobile.chat.model.warpper.ChatMessageWarpper;

/**
 * ClearServerHistoryUitl
 * @author : xiaoguang.zhang
 * Date: 12-8-31
 * Time: 下午2:39
 * @说明 : 用来清空服务器聊天记录的一个工具，清空以后会紧接着清空本地的聊天记录
 */
public class ClearServerHistoryUitl implements ChatHistoryDAOObserver {

    private List<HistoryDeleteObserver> mObservers = new ArrayList<HistoryDeleteObserver>();
    private ChatHistoryDAO dao = null;
    private List<Long> clearChatUserIdList = new ArrayList<Long>();
    private Map<Long, Boolean> clearChatMessageUserList = new HashMap<Long, Boolean>();
    private Map<Long, Integer> isClearServerFlagList = new HashMap<Long, Integer>();
    private static ClearServerHistoryUitl clearServerHistoryUitl;

    private class ClearHistoryResponse implements INetResponse {

        private long mChatId;

        @Override
        public void response(INetRequest req, JsonValue obj) {
            if (obj != null && obj instanceof JsonObject) {
                final JsonObject map = (JsonObject) obj;
                if (Methods.checkNoError(req, map)) {
                    isClearServerFlagList.put(mChatId, HistoryClearFlag.CLEAR_SERVER_SUCCESS);
                }else {
                    isClearServerFlagList.put(mChatId, HistoryClearFlag.CLEAR_SERVER_FAILED);
                }
            }else {
                isClearServerFlagList.put(mChatId, HistoryClearFlag.CLEAR_SERVER_FAILED);
            }
            ChatDataHelper.getInstance().deleteChatMessageByGroupId(mChatId);
        }

        private void setChatId(long chatId) {
            mChatId = chatId;
        }
    }

    private ClearServerHistoryUitl() {
        dao = DAOFactoryImpl.getInstance().buildDAO(ChatHistoryDAO.class);
        dao.registorObserver(this);
    }

    public static synchronized ClearServerHistoryUitl getInstance() {
        if(clearServerHistoryUitl == null) {
            clearServerHistoryUitl = new ClearServerHistoryUitl();
        }
        return clearServerHistoryUitl;
    }

    public synchronized void registorObserver(HistoryDeleteObserver observer) {
        if(observer != null && !mObservers.contains(observer)){
            mObservers.add(observer);
        }
    }

    public synchronized void unRegistorObserver(HistoryDeleteObserver observer) {
        if(observer!=null && mObservers.contains(observer)){
            mObservers.remove(observer);
        }
    }

    public synchronized void clearServerChatHistory(boolean isClearServer, long toChatUserId, boolean isGroup) {
        if(!clearChatUserIdList.contains(toChatUserId)) {
            clearChatUserIdList.add(toChatUserId);
            clearChatMessageUserList.put(toChatUserId, isGroup);
            if(isClearServer) {
                ClearHistoryResponse response = new ClearHistoryResponse();
                response.setChatId(toChatUserId);
                McsServiceProvider.getProvider().clearServerHistoryMessages(isGroup, toChatUserId, response);
            } else {
                isClearServerFlagList.put(toChatUserId, HistoryClearFlag.NO_CLEAR_SERVER);
                ChatDataHelper.getInstance().deleteChatMessageByGroupId(toChatUserId);
            }
        }
    }

    public static interface HistoryClearFlag {
        public final static int CLEAR_SERVER_SUCCESS = 0;
        public final static int CLEAR_SERVER_FAILED = 1;
        public final static int NO_CLEAR_SERVER = 2;
    }

    public interface HistoryDeleteObserver {
        public void historyClear(int clearFlag, long toChatUserId, boolean isGroup);
    }

    @Override
    public synchronized void onInsert(ChatMessageWarpper message) {

    }

    @Override
    public synchronized void onDelete(String columnName, long _id) {
        if(clearChatUserIdList.size() > 0 &&
                clearChatUserIdList.contains(_id) &&
                (columnName.equals(ChatHistory_Column.TO_CHAT_ID) || columnName.equals(ChatHistory_Column.GROUP_ID))) {
            clearChatUserIdList.remove(_id);
            for (HistoryDeleteObserver observer : mObservers) {
                if (observer != null) {
                    observer.historyClear(isClearServerFlagList.get(_id), _id, clearChatMessageUserList.get(_id));
                }
            }
            clearChatMessageUserList.remove(_id);
            isClearServerFlagList.remove(_id);
        }
    }

    @Override
    public synchronized void onUpdate(String column, long _id, ContentValues values) {

    }

	@Override
	public void onInsert(List<ChatMessageWarpper> message) {
		// TODO Auto-generated method stub
		
	}
}
