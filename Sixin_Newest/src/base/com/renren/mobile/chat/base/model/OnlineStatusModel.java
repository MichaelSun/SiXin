package com.renren.mobile.chat.base.model;



/**
 * @author dingwei.chen1988@gmail.com
 * @说明 在线状态消息
 * */
public class OnlineStatusModel {

	public long mContactsUserId;//联系人ID号
	public int mStatus ;
	
	public OnlineStatusModel(long userid,int status_mask){
		this.mContactsUserId = userid;
		//this.mStatus = getStatus(status_mask);
	}

//	public static int getStatus(int statusMask) {
//		if (statusMask == 0) {
//			return Online_Status.STATUE_OFFLINE;// 离线
//		} else if (statusMask >= 8) {
//			if(((statusMask&16) == 16) || ((statusMask&256) == 256)){
//				return Online_Status.STATUE_IPHONE_ONLINE;// iPhone在线
//			}else
//				return (Online_Status.STATUE_MOBILE_ONLINE);// 手机客户端在线
//		} else if (statusMask >= 2) {
//			return (Online_Status.STATUE_DESKORWEB_ONLINE);// 桌面或者网页在线
//		}
//		return Online_Status.STATUE_OFFLINE;
//	}
	
	
	
	public String toString(){
		return "["+mContactsUserId+":"+this.mStatus+"]";
	}
	
}
