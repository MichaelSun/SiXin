package com.renren.mobile.chat.ui.contact;
import java.util.ArrayList;
import java.util.List;

import com.common.manager.LoginManager;
import com.renren.mobile.chat.R;
import com.renren.mobile.chat.RenrenChatApplication;
import com.renren.mobile.chat.base.views.NotSynImageView;
/**
 * 
 * 群组联系人
 */

public final class GroupContactModel extends ContactModel{
/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//	
	/**只有群主一个人 */
	private boolean single;
	private final List<String> mGroupHeadUrls=new ArrayList<String>();
	public GroupContactModel(){
		groupType=Contact_group_type.MULTIPLE;
		mAlephString=RenrenChatApplication.getmContext().getResources().getString(R.string.GroupContactModel_java_1);		//GroupContactModel_java_1=群组; 
	}
	public long getGroupId() {
		return getmUserId();
	}
	public void setGroupId(long groupId){
		setmUserId(groupId);
	}
	private int mDisabled;
	public static int GROUP_DISABLED = 1;
	public static int GROUP_ONT_DISABLED = 0;
	public int isDisabled(){
		return mDisabled;
	}
	public void setDisabled(int disabled){
		this.mDisabled=disabled;
	}
	private int mIsContact;
	public static int CONTACT_YES = 1;
	public static int CONTACT_NO = 0;
	public int isContact(){
		return mIsContact;
	}
	public void setContact(int contact){
		mIsContact=contact;
	}
	private long mCreatorId;
	public long getCreatorId(){
		return mCreatorId;
	}
	public void setCreatorId(long id){
		mCreatorId=id;
	}
	private final List<Long> mGroupIds=new ArrayList<Long>();
	public String getGroupIds(){  
		StringBuilder sb = new StringBuilder();
		final char comma = ',';   
		for (Long id : mGroupIds) {
			sb.append(id).append(comma);
		}
		if(sb.length()>0){
			if(sb.charAt(sb.length()-1) == comma){
				sb.deleteCharAt(sb.length()-1);
			}
		}
		return sb.toString();
	}
	public void addId(long id){
		if(id == LoginManager.getInstance().getLoginInfo().mUserId){
			return ;
		}
		if(!mGroupIds.contains(id)){
			mGroupIds.add(id);
		}
	}
//	
//	
	public int getSize(){
		return mGroupIds.size()+1; 
	}
	public void setGroupName(String name){
		this.setmContactName(name);
	}
	public String getmContactName() {
		if (mContactName == null) {
			return RenrenChatApplication.getmContext().getResources().getString(R.string.GroupContactModel_java_2);		//GroupContactModel_java_2=群聊; 
		}
		StringBuilder sb=new StringBuilder();
		sb.append(mContactName).append('(');
		if(single){
			sb.append(1);
		}else{
			sb.append(mGroupHeadUrls.size()+1);
		}
		sb.append(')');
		return sb.toString();
	}
	public void setmHeadUrl(NotSynImageView nsiv){
		for (String headUrl : mGroupHeadUrls) {
			nsiv.addUrl(headUrl);
		}
	}
//					
	public void addHeadUrl(String url){
		mGroupHeadUrls.add(url);
		single=false;
	}
	public void setDefaultUrl(String url){
		single=true;
		mGroupHeadUrls.add(url);
	}
//			
//			
//			
//			
//			
//			
//			
//				
//					
//			
//			
//			
//			
}
