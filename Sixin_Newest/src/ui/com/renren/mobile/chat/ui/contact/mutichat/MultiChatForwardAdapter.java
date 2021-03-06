package com.renren.mobile.chat.ui.contact.mutichat;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.core.util.SystemService;
import com.core.util.ViewMapUtil;
import com.renren.mobile.chat.R;
import com.renren.mobile.chat.actions.models.RoomInfoModelWarpper;
import com.renren.mobile.chat.base.util.SystemUtil;
import com.renren.mobile.chat.base.views.NotSynImageView;
import com.renren.mobile.chat.ui.contact.C_ContactsItemHolder;
import com.renren.mobile.chat.ui.contact.ContactBaseModel.Contact_group_type;
import com.renren.mobile.chat.ui.contact.ContactModel;
import com.renren.mobile.chat.ui.contact.ContactOnscrollListener;
import com.renren.mobile.chat.ui.contact.RoomInfosData;

/**
 * @author xiangchao.fan
 * 转发中转页面listView对应adapter
 */
public class MultiChatForwardAdapter extends BaseAdapter {
	
	private Context mContext;
	
	private ListView mListView;
	
	private List<ContactModel> mContacts = null;
	
	private ContactOnscrollListener mListViewOnScrollListener;
	
	public MultiChatForwardAdapter(Context context, ListView listView){
		this.mContext = context;
		this.mListView = listView;
		
		mListViewOnScrollListener = new ContactOnscrollListener(this.mListView, this);
		this.mListView.setOnScrollListener(mListViewOnScrollListener);
	}
	
	@Override
	public int getCount() {
		if(null != mContacts)
			return mContacts.size();
		return 0;
	}

	@Override
	public Object getItem(int position) {
		return mContacts.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		ContactModel model = mContacts.get(position);
		
		C_ContactsItemHolder viewHolder;
		if(null == convertView){
			viewHolder = new C_ContactsItemHolder();
			convertView = SystemService.sInflaterManager.inflate(R.layout.contact_listview_item, null);
			ViewMapUtil.getUtil().viewMapping(viewHolder, convertView);
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (C_ContactsItemHolder) convertView.getTag();
		}
		warp(model, viewHolder, position);
		setItemHeadImg(viewHolder, model);
		return convertView;
	}
	
	private void warp(ContactModel model, C_ContactsItemHolder holder, int position){
		this.showAlpheBar(model.mIsShowAlephLabel_InOnline, holder, model.mAlephString);
		holder.mAttentionView.setVisibility(View.GONE);
	}
	
	private void showAlpheBar(boolean flag,C_ContactsItemHolder holder,String text){
		if(flag ){
			if(text != null){
				holder.mAlpheBar.setText(text.toUpperCase());
			}
			holder.mAlpheBar.setVisibility(View.VISIBLE);
		}else{
			holder.mAlpheBar.setVisibility(View.GONE);
		}
	}
	
	/**
	 * 设置item的头像
	 * */
	private void setItemHeadImg(C_ContactsItemHolder viewHolder, ContactModel contactModel) {
		
		viewHolder.mHeadImage.clear();
		viewHolder.mHeadImage.setBackgroundColor(NotSynImageView.GRAY);
		if(contactModel.getGroupType() == Contact_group_type.SINGLE){
			viewHolder.mHeadImage.addUrl(contactModel.getmHeadUrl());
			viewHolder.mUserNameView.setText(contactModel.getName());
		}else{
			RoomInfoModelWarpper roomInfo = RoomInfosData.getInstance()
					.getRoomInfo(contactModel.mUserId);
			if(roomInfo != null){
				if(roomInfo.mMembers.size()>0){
					int index = NotSynImageView.MAX_HEAD_NUMBER;
					for(ContactModel model:roomInfo.mMembers){
						viewHolder.mHeadImage.addUrl(model.getmHeadUrl());
						if((index-1)==-1){
							break;
						}
					}
				}else{
					viewHolder.mHeadImage.addUrl(NotSynImageView.GROUP_DEFUALT_HEAD_URL);
				}
				if(null != roomInfo.getName())
					viewHolder.mUserNameView.setText(roomInfo.getName() + "(" + roomInfo.mRoomMemberNumber + ")");
			}else{
				viewHolder.mHeadImage.addUrl(NotSynImageView.GROUP_DEFUALT_HEAD_URL);
			}
		}
	}

	public List<ContactModel> getContacts() {
		return mContacts;
	}

	public void setContacts(List<ContactModel> contacts) {
		this.mContacts = contacts;
	}

}
