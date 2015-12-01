package com.renren.mobile.chat.sql;

import java.util.ArrayList;

import android.database.Cursor;

import com.core.database.BaseDAO;
import com.core.database.Query;
import com.core.orm.ORMUtil;
import com.renren.mobile.chat.actions.models.RoomBaseInfoModel;
import com.renren.mobile.chat.actions.models.RoomInfoModelWarpper;
import com.renren.mobile.chat.ui.contact.C_ContactsData;
import com.renren.mobile.chat.ui.contact.ContactBaseModel;
import com.renren.mobile.chat.ui.contact.ContactModel;
import com.renren.mobile.chat.ui.contact.IDownloadContactListener;

public class Query_RoomInfo_List extends Query{

	public Query_RoomInfo_List(BaseDAO dao) {
		super(dao);
	}

	@Override
	public Object warpData(Cursor cursor) {
		
		ArrayList<RoomInfoModelWarpper> roomInfoList = new ArrayList<RoomInfoModelWarpper>();
		try {
			while(cursor.moveToNext()){
				RoomInfoModelWarpper message = new RoomInfoModelWarpper();
				ORMUtil.getInstance().ormQuery(RoomInfoModelWarpper.class, message, cursor);//取出
				String[] ids = message.mIds.split("["+RoomBaseInfoModel.SPLIT+"]");
				
				for(String id:ids){
					if(id.trim().length()==0){
						continue;
					}
					if(id!=null&&id.length()>0){
					//	ContactModel model = C_ContactsData.getInstance().getContactInfoFromLocal(Long.parseLong(id));
						ContactModel model = C_ContactsData.getInstance().getSiXinContact(Long.parseLong(id), null);
						if(model!=null){
							message.addMember(model);
						}else{
//							C_ContactsData.getInstance().getContactInfoFromNet(Long.parseLong(id), new IDownloadContactListener() {
//								@Override
//								public void onSussess(ContactBaseModel model) {
//								}
//								
//								@Override
//								public void onError() {
//								}
//								
//								@Override
//								public void onDowloadOver() {
//								}
//							});
							//Logd.error("没有这个人呀");
						}
						
					}
				}
				roomInfoList.add(message);
			}
		} catch (Exception e) {
			//e.printStackTrace();
		}
		
		return roomInfoList;
	}
}
