package com.data.xmpp;

import com.data.action.ACTION_TYPE;
import com.data.action.Actions;
import com.data.xml.XMLMapping;
import com.data.xml.XMLType;

public class Presence extends XMPPNodeRoot{

	public enum TYPE{
		INVITE,//邀请进入房间
		ERROR,//失败
		
		CREATE_ROOM_MASTER,//群主
		CREATE_ROOM_MEMBER,//群成员
		CREATE_ROOM_ERROR,//创建房间错误
		CREATE_ROOM_SUCCESS,//创建房间成功
		
		DELETE_MEMBER,//删除用户
		NOT_BEDEL_MEMBER,//未被删除用户
		
		NOT_QUIT_MEMBER,//未退群的人
		QUIT_ERROR,//退出群失败
		
		
		DESTROY_ROOM_MEMBER,//被销毁房间的群成员
		DESTROY_ROOM_SUCCESS,
		DESTROY_ROOM_ERROR,
		
		UPLOAD_ROOM_TIMESTAMP_SUCCESS,
		UPLOAD_ROOM_TIMESTAMP_ERROR,
		
	}
	
	@XMLMapping(Type=XMLType.NODE,Name="x")
	public X mXNode = null;

    @XMLMapping(Type = XMLType.NODE, Name = "z")
	public Z mZNode;
	
	
	@Override
	public String getNodeName() {
		return "presence";
	}


	@Override
	public Actions getAction() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public ACTION_TYPE getActionType() {
		// TODO Auto-generated method stub
		return ACTION_TYPE.PRESENCE;
	}

}
