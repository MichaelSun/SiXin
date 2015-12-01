package com.renren.mobile.chat.base.model;

import java.io.Serializable;

import com.renren.mobile.chat.model.warpper.ChatMessageWarpper;


/**
 * @说明 聊天用户模型
 * */
public class ContactInChat implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7070409422352413151L;
	public String mUserHeadUrl = null;
	public String mUserName ;
	public long mUserId;
	public String mGender;
	public String mBirthday;
	public int mOnlineStatus;
	public String mLargeUserHeadUrl = null;
	public boolean  mFocus = false;
	/**
	 * @see com.renren.mobile.chat.ui.contact.ContactModel.Contact_Type
	 * */
//	public int mFriendType;//用户类型
//	public String mPhoneNumber;//用户电话号码
	
	 
	
	/**
     * 是否为人人好友
     */
    public int mIsFriend;
    /**
     * 是否为通讯录好友
     */
//    public int mIsContactFriend;
    
    
	
	
    public void parseFieldToTheChatMessage(ChatMessageWarpper message){
    	message.mHeadUrl = mUserHeadUrl;
    	message.mUserName = mUserName;
    	message.mLargeHeadUrl = mLargeUserHeadUrl;
    }


    
    
    
    
}
