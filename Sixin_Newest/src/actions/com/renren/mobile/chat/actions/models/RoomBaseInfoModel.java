package com.renren.mobile.chat.actions.models;

import java.io.Serializable;

import com.core.orm.ORM;
import com.renren.mobile.chat.database.Room_Column;

/**
 * @author dingwei.chen
 * */
public class RoomBaseInfoModel implements Serializable{

	public static final String SPLIT = ",";
	
	public static enum ROOM_ENABLE{
		DISABLE(1),
		ENABLE(0);
		public int Value;
		ROOM_ENABLE(int v){
			this.Value = v;
		}
	}
	
	
	@ORM(mappingColumn=Room_Column.ROOM_ID)
	public long mRoomId;
	
	@ORM(mappingColumn=Room_Column.ROOM_SUBJECT)
	public String mSubject;
	
	@ORM(mappingColumn=Room_Column.ROOM_VERSION)
	public int mVersion;
	
	@ORM(mappingColumn=Room_Column.ROOM_MASTER_ID)
	public long mOwner;
	
	@ORM(mappingColumn=Room_Column.LOCAL_USER_ID)
	public long mLocalUserId;
	
	@ORM(mappingColumn=Room_Column.ROOM_MEMBER_IDS)
	public String mIds = "";
	
	@ORM(mappingColumn=Room_Column.ROOM_ISDISABLE)
	public int mDisable = ROOM_ENABLE.ENABLE.Value;// 0代表没有废弃，1代表已经废弃
	
	@ORM(mappingColumn=Room_Column.ROOM_MEMBER_NUMBER)
	public int mRoomMemberNumber ;
	
	@ORM(mappingColumn=Room_Column.ROOM_MEMBER_HEADURLS)
	public String mHeadUrl ="";
	
	
	@ORM(mappingColumn=Room_Column.ROOM_IS_CONTACT)
	public int mRoomIsContact = 0;
	
}
