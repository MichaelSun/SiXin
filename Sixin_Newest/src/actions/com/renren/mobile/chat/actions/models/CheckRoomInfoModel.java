package com.renren.mobile.chat.actions.models;

import java.util.ArrayList;

import com.data.util.ShowFieldsUtil;
import com.data.xmpp.Check;

/**
 * @author dingwei.chen1988@gmail.com
 * */
public class CheckRoomInfoModel {
	
	public ArrayList<Long> mIdList = new ArrayList<Long>();
	public ArrayList<Boolean> mUpdateList = new ArrayList<Boolean>();
	public ArrayList<Boolean> mIsMember = new ArrayList<Boolean>();
	
	public void addCheckInfo(Check check){
		this.mIdList.add(check.getRoomId());
		this.mIsMember.add(check.getIsMember());
		this.mUpdateList.add(check.getUpdate());
	}
	
	public long getFristId(){
		if(mIdList.size()>0){
			return mIdList.get(0);
		}
		return 0;
	}
	
	public boolean getFristUpdate(){
		if(mUpdateList.size()>0){
			return mUpdateList.get(0);
		}
		return false;
	}
	
	public boolean getFristIsmember(){
		if(mIsMember.size()>0){
			return mIsMember.get(0);
		}
		return false;
	}
	
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return ShowFieldsUtil.showAllFields(0, this);
	}
}
