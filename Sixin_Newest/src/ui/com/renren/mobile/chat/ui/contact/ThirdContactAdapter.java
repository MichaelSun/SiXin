package com.renren.mobile.chat.ui.contact;


import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.core.util.SystemService;
import com.core.util.ViewMapUtil;
import com.renren.mobile.chat.R;
import com.renren.mobile.chat.RenrenChatApplication;
import com.renren.mobile.chat.activity.RenRenChatActivity;
import com.renren.mobile.chat.base.util.SystemUtil;
import com.renren.mobile.chat.base.views.NotSynImageView.DownloadImageAbleListener;
import com.renren.mobile.chat.ui.contact.C_ContactsItemHolder;


/**
 * 选择联系人Adapter
 */
public class ThirdContactAdapter extends BaseAdapter implements DownloadImageAbleListener{
    
	public static final byte DATA_TYPE_SERARCH = 1;
	public static final byte DATA_TYPE_ALL = 2;
	
	private int mDataType = DATA_TYPE_ALL;
	private Context context;
	//private ListView mListView;
	C_ContactsItemHolder holder = null;
	private boolean isScrolling; 
//	private boolean isSelect;
//	private int dip58Topx = 0;
	Drawable sinxin;

	//List<ContactModel> list = new ArrayList<ContactModel>();
	List<ThirdContactModel> mDataList = new ArrayList<ThirdContactModel>();
	
	public ThirdContactAdapter(Context context,byte dataType){
		//this.mListView = mListView;
		mDataType = dataType;
		this.context = context;
		sinxin = context.getResources().getDrawable(R.drawable.contact_renren_sixin);
	//	dip58Topx=(int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,58, context.getResources().getDisplayMetrics());
	}
	
	
	@Override
	public int getCount() {
		return mDataList.size();
	}

	@Override
	public ThirdContactModel getItem(int position) {
		return mDataList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
//	public void setData(List<ContactModel> models){
//		list = models;
//	}
	
	public void setDataList(List<ThirdContactModel> models){ 
		synchronized (mDataList) {
			this.mDataList.clear();
			this.mDataList.addAll(models);
			notifyDataSetChanged();
		}
	}
	
	public List<ThirdContactModel> getData(){
		return mDataList;
	}
	
//	public void update(List<ContactModel> models){
//		C_ContactsData.getInstance().updateIndex(models);
//	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ThirdContactModel model = mDataList.get(position);
		//SystemUtil.logd("name="+model.getmContactName()+"#"+model.mAleph);
		if(model!=null){
			if(convertView==null || convertView.getTag()==null){
				holder = new C_ContactsItemHolder();
				convertView = SystemService.sInflaterManager.inflate(R.layout.select_contact_listview_item, null);
				ViewMapUtil.getUtil().viewMapping(holder, convertView);
				convertView.setTag(holder);
				holder.mHeadImage.setDownloadAbleListener(this);
			}
			holder = (C_ContactsItemHolder)convertView.getTag();
			this.warp(model, holder,position);
			setItemHeadImg(holder,model);
			setItemOnclick(holder.mContactListItemLayout,model);
			//if(model.mSelected){
				//holder.selectBtn.setBackgroundResource(R.drawable.select_contact_selected);
			//}else{
				//holder.selectBtn.setBackgroundResource(R.drawable.select_contact_default);
			//}
			
			return convertView;
		}
		return new TextView(RenrenChatApplication.mContext);
	}
	private void warp(ThirdContactModel model, C_ContactsItemHolder holder,int position){
		
		//if(mDataType == DATA_TYPE_CHOOSE)
		//{
		//this.showAlpheBar(model.mIsShowAlephLabel_InAll, holder, model.mAlephString);
	///	ThirdContactModel nextModel = null;
//		if (position + 1 < getCount()) {
//			nextModel  = mDataList.get(position+1);
//		}
//		if(nextModel!=null && nextModel.mIsShowAlephLabel_InAll && model.mAlephString!=null ){
//			holder.mDivider.setVisibility(View.GONE);
//		}else{
//			holder.mDivider.setVisibility(View.VISIBLE);
//		}
		//}
//		else if(mDataType == DATA_TYPE_SERARCH){
//			this.showAlpheBar(false, holder, model.mAlephString);
//			holder.mDivider.setVisibility(View.VISIBLE);
//		}
		
		
//		if(model.mAlephString != null){
//			holder.mShadowAlpheBar.setText(model.mAlephString.toUpperCase());
//			holder.mShadowAlpheBar.setTextViewLength(dip58Topx);
//		}
		
		if(mDataType == DATA_TYPE_SERARCH){
			holder.mUserNameView.setText(model.getDyeContactName());
		}else
		{
			holder.mUserNameView.setText(model.getmContactName());
		}
		holder.mAlpheBar.setVisibility(View.GONE);
		if(model.bindSinxin()){
			holder.mSelectContactView.setVisibility(View.VISIBLE);
			holder.mSelectContactView.setBackgroundDrawable(sinxin);
		}else{
			holder.mSelectContactView.setVisibility(View.GONE);
		}
	}
//	private void showAlpheBar(boolean flag,C_ContactsItemHolder holder,String text){
//		//SystemUtil.logd("flag="+flag+"#text="+text);
//		if(flag ){
//			if(text != null){
//				holder.mAlpheBar.setText(text.toUpperCase());
//			}
//			holder.mAlpheBar.setTextViewLength(dip58Topx);
//			holder.mAlpheBar.setVisibility(View.VISIBLE);
//		}else{
//			holder.mAlpheBar.setVisibility(View.GONE);
//		}
//	}
	
	/**
	 * 设置item的头像
	 * */
	private void setItemHeadImg(C_ContactsItemHolder viewHolder, ThirdContactModel contactModel) {
		String headUrl = contactModel.getmHeadUrl();
//		if(SystemUtil.mDebug){
//			SystemUtil.logd("large="+headUrl);
//		}
		viewHolder.mHeadImage.clear();
		viewHolder.mHeadImage.addUrl(headUrl);
	}
	
	private void setItemOnclick(View view, final ThirdContactModel contactModel) {
		view.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
		        if(SystemUtil.mDebug){
		        	SystemUtil.logd("ThirdContactModel = "+contactModel.getName()+"#"+contactModel.getmUserId());
		        //	Toast.makeText(context, "这个地方要改成聊天", Toast.LENGTH_SHORT);
				}
		        RenRenChatActivity.show(context, contactModel);
				//UserInfoActivity.show(context,contactModel.getmUserId(),contactModel.getmDomain());
			}
		});
	}
	
	public void setScrollState(boolean scrollState){
		isScrolling = scrollState;
		if(!scrollState){
			//SystemUtil.logd("setscroolstate notify-------------");
			this.notifyDataSetChanged();
		}
	}
	
//	public void setSelectState(boolean selectState){
//		isSelect = selectState;
//		if(!isSelect){
//			this.notifyDataSetChanged();
//		}
//	}

	@Override
	public boolean enable() {
		//SystemUtil.logd("enable=="+(!isScrolling));
		return !isScrolling;
	}

	
	

}
