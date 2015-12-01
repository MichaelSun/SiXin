package com.renren.mobile.chat.sql;

import android.database.Cursor;

import com.core.database.BaseDAO;
import com.core.database.Query;
import com.core.orm.ORMUtil;
import com.renren.mobile.chat.actions.models.RoomBaseInfoModel;
import com.renren.mobile.chat.actions.models.RoomInfoModelWarpper;
import com.renren.mobile.chat.ui.contact.C_ContactsData;
import com.renren.mobile.chat.ui.contact.ContactModel;

public class Query_RoomInfo extends Query{

	public Query_RoomInfo(BaseDAO dao) {
		super(dao);
	}

	@Override
	public Object warpData(Cursor c) {
		RoomInfoModelWarpper message = new RoomInfoModelWarpper();
		if(c.moveToFirst()){
			ORMUtil.getInstance().ormQuery(RoomInfoModelWarpper.class, message, c);//取出
		}
		String[] ids = message.mIds.split("["+RoomBaseInfoModel.SPLIT+"]");
		String[] headurls = message.mHeadUrl.split("["+RoomBaseInfoModel.SPLIT+"]");
		
		for(String id:ids){
			if(id.trim().length()==0){
				continue;
			}
			ContactModel model = C_ContactsData.getInstance().getSiXinContact(Long.parseLong(id),null);
			message.addMember(model);
		}
		                           
		
		return message;
	}

}
