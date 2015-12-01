package com.data.xmpp;

import com.data.action.ACTION_TYPE;
import com.data.action.Actions;
import com.data.util.ShowFieldsUtil;
import com.data.xml.XMLMapping;
import com.data.xml.XMLType;

/**
 * @author dingwei.chen1988@gmail.com
 * @说明 作为根节点
 * */
public class Iq extends XMPPNodeRoot {

	public enum TYPE{
		DELETE_MEMBER_SUCCESS,//删除用户成功
		DELETE_MEMBER_ERROR,//删除用户失败
		
		QUERY_ROOM_MEMBER_SUCCESS,//查看群消息
		QUERY_ROOM_MEMBER_ERROR,
		
		SAVE_ROOM_SUCCESS,
		SAVE_ROOM_ERROR,
		
		QUERY_ROOM_SUCCESS,
		QUERY_ROOM_ERROR,
	}
	
	
	@XMLMapping(Type=XMLType.NODE,Name="query")
	public Query mQueryNode = null;

    @XMLMapping(Type = XMLType.NODE, Name = "z")
	public Z mZNode;
	
	
	
	@Override
	public String getNodeName() {
		return "iq";
	}



	@Override
	public Actions getAction() {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public ACTION_TYPE getActionType() {
		// TODO Auto-generated method stub
		return ACTION_TYPE.IQ;
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return ShowFieldsUtil.showAllFields(0, this);
	}
	

}
