package com.renren.mobile.chat.ui.contact;



import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.core.util.SystemService;
import com.core.util.ViewMapUtil;
import com.renren.mobile.chat.R;
import com.renren.mobile.chat.base.util.SystemUtil;
import com.renren.mobile.chat.base.views.NotSynImageView;
import com.renren.mobile.chat.base.views.NotSynImageView.DownloadImageAbleListener;
import com.renren.mobile.chat.ui.account.BindRenrenAccountActivity;
import com.renren.mobile.chat.ui.contact.ContactBaseModel.Contact_group_type;
import com.renren.mobile.chat.ui.groupchat.D_ChatMessagesActivity;
import com.renren.mobile.chat.ui.groupchat.D_ChatMessagesActivity.CHAT_MESSAGE_COMEFROM;
import com.renren.mobile.chat.ui.groupchat.D_ChatMessagesActivity.CHAT_MESSAGE_PARAMS;



public final class C_ContactsAdapter extends BaseAdapter implements DownloadImageAbleListener{

	private int mDataType = -1;
	private Context mActivity;
	private static boolean isScrolling = false; 
	//private static boolean isSelect;
	//private  int groupDip58Topx = 0;
	//private  int dip58Topx = 0;
	
	private List<ContactModel> mDataList = new ArrayList<ContactModel>();
	public C_ContactsAdapter(Context context, int dataType){
		mActivity = context;
		mDataType = dataType;
		//groupDip58Topx=(int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,10, context.getResources().getDisplayMetrics());
		//dip58Topx=(int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,10, context.getResources().getDisplayMetrics());
	}
	
	public void setData(List<ContactModel> dataList){
		synchronized (mDataList) {
			mDataList.clear();
			mDataList.addAll(dataList);
		}
		if(SystemUtil.mDebug){
			SystemUtil.logd("size="+dataList.size());
		}
		this.notifyDataSetChanged();
	}
	
	public void notifyData(List<ContactModel> dataList){
		mDataList=dataList;
		if(SystemUtil.mDebug){
			SystemUtil.logd("size="+dataList.size());
		}
		this.notifyDataSetChanged();
	}
	
	public List<ContactModel> getData(){
		return mDataList;
	}
	
	@Override
	public int getCount() {
		return mDataList.size();
	}

	@Override
	public ContactModel getItem(int position) {
		return mDataList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
	     //Logd.log("getView position="+position);
		ContactModel model = mDataList.get(position);
		//Logd.log("name="+model.getmContactName()+"#"+model.mAlephString);
		if(model!=null){
			C_ContactsItemHolder holder = null;
			if(convertView==null){
				holder = new C_ContactsItemHolder();
				convertView = SystemService.sInflaterManager.inflate(R.layout.cdw_contact_listview_item, null);
				ViewMapUtil.getUtil().viewMapping(holder, convertView);
				convertView.setTag(holder);
				holder.mHeadImage.setDownloadAbleListener(this);
			}
			holder = (C_ContactsItemHolder)convertView.getTag();
			if(holder!=null){
				this.warp(model, holder,position);
				setItemHeadImg(holder,model);
				setItemOnclick(holder.mContactListItemLayout,model);
			}
			return convertView;
		}
		return null;
	}
	
	private void warp(ContactModel model, C_ContactsItemHolder holder,int position){
		//Logd.mark();
		if(mDataType == C_ContactsData.TYPE.ALL_CONTACTS){
			//SystemUtil.logd("=="+model.mIsShowAlephLabel_InAll+"#"+model.getmContactName());
			this.showAlpheBar(model.mIsShowAlephLabel_InAll, holder, model.mAlephString,model.getGroupType()== Contact_group_type.MULTIPLE);
//			ContactModel nextModel = C_ContactsData.getInstance().getItem(mDataType, position+1);
			ContactModel nextModel = null;
			if(position+1<getCount()){
				nextModel = mDataList.get(position+1);
			}
			if(nextModel!=null && nextModel.mIsShowAlephLabel_InAll && nextModel.mAlephString!=null ){
				holder.mDivider.setVisibility(View.GONE);
			}else{
				holder.mDivider.setVisibility(View.VISIBLE);
			}
		}else if(mDataType == C_ContactsData.TYPE.COMMON_CONTACTS){
			holder.mAlpheBar.setVisibility(View.GONE);
			if(position<getCount()){
				holder.mDivider.setVisibility(View.VISIBLE);
			}
//			this.showAlpheBar(model.mIsShowAlephLabel_InOnline, holder, model.mAlephString,model.getGroupType()== Contact_group_type.MULTIPLE);
//			ContactModel nextModel = null;
//			if(position+1<getCount()){
//				nextModel = mDataList.get(position+1);
//			}
//			
//			if(nextModel!=null && nextModel.mIsShowAlephLabel_InOnline && model.mAlephString!=null ){
//				holder.mDivider.setVisibility(View.GONE);
//			}else{
//				holder.mDivider.setVisibility(View.VISIBLE);
//			}
		}else if(mDataType == C_ContactsData.TYPE.SEARCH_CONTACTS){
			this.showAlpheBar(false, holder, model.mAlephString,model.getGroupType()== Contact_group_type.MULTIPLE);
			holder.mDivider.setVisibility(View.VISIBLE);
		}
		
		//最上面的那个字母条
		
		if(mDataType!= C_ContactsData.TYPE.COMMON_CONTACTS){
			int type = model.getGroupType();
			//Logd.log("type="+type);
			if(type == ContactModel.Contact_group_type.SPECIAL_RECOMMEND || type ==ContactModel.Contact_group_type.SPECIAL_RENREN){
				holder.mShadowAlpheBar.setText(model.mAlephString);
				holder.mShadowAlpheBar.setVisibility(View.GONE);
			}else if(model.mAlephString != null){
				//Logd.log("model="+model.mAlephString+"#name="+model.mContactName+"#tupe="+model.groupType+"#"+model.mIsShowAlephLabel_InAll);
				holder.mShadowAlpheBar.setVisibility(View.VISIBLE);
				holder.mShadowAlpheBar.setText(model.mAlephString.toUpperCase());
//				if(model.getGroupType()== Contact_group_type.SINGLE){
//					holder.mShadowAlpheBar.setTextViewLength(dip58Topx);
//				}else{
//					holder.mShadowAlpheBar.setTextViewLength(groupDip58Topx);
//				}
			}else{
				holder.mShadowAlpheBar.setText(model.mAlephString);
				holder.mShadowAlpheBar.setVisibility(View.GONE);
			}
		}else{
			holder.mShadowAlpheBar.setText(model.mAlephString);
			holder.mShadowAlpheBar.setVisibility(View.GONE);
		}
		
		//if(mDataType == C_ContactsData.TYPE.ALL_CONTACTS)
		{
			if(model.getmRelation() == ContactBaseModel.Relationship.BLACK_LIST_CONTACT){
				holder.mBlacklistImg.setVisibility(View.VISIBLE);
			}else{
				holder.mBlacklistImg.setVisibility(View.GONE);
			}
		}
//		else{
//			holder.mBlacklistImg.setVisibility(View.GONE);
//		}
		
//		//头像上在线的那个绿点
//		if(model.mOnlinestatus!=ContactModel.Online_Status.STATUE_OFFLINE){
//			holder.mIsOnlineView.setVisibility(View.VISIBLE);
//		}else{
//			holder.mIsOnlineView.setVisibility(View.GONE);
//		}
//		
//		//在线状态
//		if(model.getOnlineStatus()==Online_Status.STATUE_OFFLINE){
//			if(model.getGroupType()== Contact_group_type.SINGLE){
//				holder.mOnlineMethodView.setVisibility(View.INVISIBLE);
//			}else{
//				holder.mOnlineMethodView.setVisibility(View.GONE);
//			}
//		}else{
//			holder.mOnlineMethodView.setText(model.getOnlineStatusText());
//			holder.mOnlineMethodView.setVisibility(View.VISIBLE);
//		}
		
		
//		if(mDataType == C_ContactsData.TYPE.ONLINE_CONTACTS){
//			holder.mOnlineMethodView.setText(model.getOnlineStatusText());
//		}else{
//			holder.mOnlineMethodView.setText("");
//		}
		
		//显示名字
		if(mDataType == C_ContactsData.TYPE.SEARCH_CONTACTS){
			holder.mUserNameView.setText(model.getDyeContactName());
		}else{
			holder.mUserNameView.setText(model.getmContactName());
			if(model.getGroupType() == ContactBaseModel.Contact_group_type.SPECIAL_RECOMMEND){
				SpecialContactModel scm=(SpecialContactModel)model;
				if(scm.getUnReadedCount()>0){
					holder.mNewMessageCount.setVisibility(View.VISIBLE);
					holder.mNewMessageCount.setText(scm.getUnReadedCount()+"");
					holder.mViceUserName.setVisibility(View.VISIBLE);
					holder.mViceUserName.setText(scm.getUnReadedNames());
				}else{
					holder.mNewMessageCount.setVisibility(View.GONE);
					holder.mViceUserName.setVisibility(View.GONE);
				}
			}else{
				holder.mNewMessageCount.setVisibility(View.GONE);
				holder.mViceUserName.setVisibility(View.GONE);
			}
		}
		
		//是否显示特别关注
//		if(model.mIsAttention == 1){
//			holder.mAttentionView.setVisibility(View.VISIBLE);
//		}else if(model.mIsAttention == 0){
//			holder.mAttentionView.setVisibility(View.GONE);
//		}else {
//			holder.mAttentionView.setVisibility(View.GONE);	
//		}
			
		
	}
	
	private void showAlpheBar(boolean flag,C_ContactsItemHolder holder,String text,boolean isGroup){
		if(flag ){
//			if (isGroup) {
//				holder.mAlpheBar.setTextViewLength(groupDip58Topx);
//			} else {
//				holder.mAlpheBar.setTextViewLength(dip58Topx);
//			}
			
			if(text != null){
				holder.mAlpheBar.setText(text.toUpperCase());
			}else{
				//Logd.error("--------------------------");
				holder.mAlpheBar.setText(null);
			}
			holder.mAlpheBar.setVisibility(View.VISIBLE);
		}else{
			//Logd.log("text="+text);
			//holder.mAlpheBar.setText(text);
			//SystemUtil.errord("已经隐藏了。。。。。。。。。");
			holder.mAlpheBar.setVisibility(View.GONE);
		}
	}
	
	/**
	 * 设置item的点击事件
	 * 解决个人详情页做出更改要返回后使联系人页做出相应更改的方案：
	 * 1.使用回调
	 * 2.使用observer
	 * 3.使用startActivityForesult()
	 * */
	private void setItemOnclick(View view, final ContactModel contactModel) {
		view.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				int type = contactModel.getGroupType();
				if(type==ContactModel.Contact_group_type.SINGLE){
					UserInfoActivity.show(mActivity, contactModel.mUserId,contactModel.getDomain());
				}else if(type==ContactModel.Contact_group_type.MULTIPLE){
					if(contactModel instanceof GroupContactModel){
						GroupContactModel gcm=(GroupContactModel)contactModel;
						long groupId=gcm.getGroupId();
						Intent intent=new Intent(mActivity, D_ChatMessagesActivity.class);
						intent.putExtra("groupId", groupId);
						intent.putExtra(CHAT_MESSAGE_PARAMS.COMEFROM, CHAT_MESSAGE_COMEFROM.MAINFRAGMENT_ACTIVITY);
						mActivity.startActivity(intent);
					}
				}else if(type==ContactModel.Contact_group_type.SPECIAL_RENREN){
					if(contactModel instanceof SpecialContactModel){
						SpecialContactModel scm= (SpecialContactModel)contactModel;
						Intent i;
						if(scm.isBindedRenRen()){
						   i = new Intent(mActivity, ThirdContactsActivity.class);
						}else{
							i = new Intent(mActivity, BindRenrenAccountActivity.class);
							i.putExtra(BindRenrenAccountActivity.COME_FROM, BindRenrenAccountActivity.BIND_CONTANT);
						}
						mActivity.startActivity(i);
					}
				}else if(type==ContactModel.Contact_group_type.SPECIAL_RECOMMEND){
					Intent intent=new Intent(mActivity, ContactMessageActivity.class);
					mActivity.startActivity(intent);
				}
				
			}
		});
	}
	
	/**
	 * 设置item的头像
	 * */
	private void setItemHeadImg(C_ContactsItemHolder viewHolder, ContactModel contactModel) {
		viewHolder.mHeadImage.clear();
		viewHolder.mHeadImage.setBackgroundColor(NotSynImageView.GRAY);
		contactModel.setmHeadUrl(viewHolder.mHeadImage);				
	}
	
	
	public void setScrollState(boolean scrollState){
		isScrolling=scrollState;
		if(!scrollState){ 
			//SystemUtil.logd("setscroolstate notify-------------");
			notifyDataSetChanged();
		}
	}

	@Override
	public boolean enable() {
		// TODO Auto-generated method stub
	//	SystemUtil.logd("enable=="+(!isScrolling));
		return !isScrolling;
	}

}
