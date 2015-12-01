package com.renren.mobile.chat.sql;

import java.util.LinkedList;
import java.util.List;

import android.database.Cursor;

import com.core.database.BaseDAO;
import com.core.database.Query;
import com.core.orm.ORMUtil;
import com.renren.mobile.chat.actions.models.RoomInfoModelWarpper;
import com.renren.mobile.chat.base.model.ChatBaseItem.MESSAGE_ISGROUP;
import com.renren.mobile.chat.ui.chatsession.ChatSessionDataModel;
import com.renren.mobile.chat.ui.contact.RoomInfosData;

public class Query_ChatSession extends Query{

	public Query_ChatSession(BaseDAO dao) {
		super(dao);
	}

	@Override
	public Object warpData(Cursor c) {
		List<ChatSessionDataModel> messages = new LinkedList<ChatSessionDataModel>();
		while(c.moveToNext()){
			ChatSessionDataModel model = new ChatSessionDataModel();
			ORMUtil.getInstance().ormQuery(ChatSessionDataModel.class, model, c);
			messages.add(model);
			if(model.mIsGroup==MESSAGE_ISGROUP.IS_GROUP){
				RoomInfoModelWarpper m = RoomInfosData.getInstance().getRoomInfo(model.mGroupId);
				if(model!=null){
					model.mRoomInfo = m;
				}
			}
			
		}
		return messages;
	}

}
