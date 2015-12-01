package com.renren.mobile.chat.ui.contact.attention;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.common.mcs.INetReponseAdapter;
import com.common.mcs.INetRequest;
import com.common.mcs.INetResponse;
import com.common.mcs.McsServiceProvider;
import com.core.json.JsonObject;
import com.core.util.SystemService;
import com.core.util.ViewMapUtil;
import com.renren.mobile.chat.R;
import com.renren.mobile.chat.RenrenChatApplication;
import com.renren.mobile.chat.base.util.SystemUtil;
import com.renren.mobile.chat.base.views.NotSynImageView.DownloadImageAbleListener;
import com.renren.mobile.chat.dao.DAOFactoryImpl;
import com.renren.mobile.chat.dao.ThirdContactsDAO;
import com.renren.mobile.chat.ui.contact.C_ContactsData;
import com.renren.mobile.chat.ui.contact.ThirdContactModel;
import com.renren.mobile.chat.ui.contact.feed.ChatFeedActivity;
/**
 * 
 * @author rubin.dong@renren-inc.com
 * @date 
 * 关注页面Adapter
 */
public class AttentionAddAdapter extends BaseAdapter{
	private static final int ATTENTION_MAX = 20;
	private int mDataType = -1;
	ThirdContactsDAO mThirdContactsDAO;
	public final static String TAG = "AttentionAddAdapter";
	private DownloadImageAbleListener mDownloadImageAbleListener;
	public List<ThirdContactModel> mDataList = new ArrayList<ThirdContactModel>();
	public AttentionAddAdapter(int dataType,Context context,ListView mListView){
		mDataType = dataType;
		mThirdContactsDAO = DAOFactoryImpl.getInstance().buildDAO(ThirdContactsDAO.class);
	}
	
	public AttentionAddAdapter(DownloadImageAbleListener downloadImageAbleListener){
		super();
		this.mDownloadImageAbleListener = downloadImageAbleListener;
	}
	public void setDataList(List<ThirdContactModel> dataList){
		this.mDataList.clear();
		this.mDataList.addAll(dataList);
		notifyDataSetChanged();
	}
	@Override
	public int getCount() {
		return mDataList.size();
	}
	@Override
	public Object getItem(int position) {
		return mDataList.get(position);
	}
	@Override
	public long getItemId(int position) {
		return position;
	}
	public static final int NO_ATTENTION = 0;
	public static final int IS_ATTENTION = 1;
	public static final int ATTENTIONING = 2;
	public static final int CANCEL_ATTENTIONING = 3;
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ThirdContactModel thirdContactModel = mDataList.get(position);
		final AttentionAddHolder viewHolder;
		if(thirdContactModel!=null){
			if(convertView==null){
				viewHolder = new AttentionAddHolder();
				convertView = SystemService.sInflaterManager.inflate(R.layout.d_attention_listview_item, null);
				ViewMapUtil.getUtil().viewMapping(viewHolder, convertView);
				convertView.setTag(viewHolder);
			}else{
				viewHolder = (AttentionAddHolder)convertView.getTag();
			}
			this.warp(thirdContactModel, viewHolder);
			setItemHeadImg(viewHolder,thirdContactModel);
			//TODO 设置关注
			final int attentionState =thirdContactModel.getmIsAttention();
			setBtnState(viewHolder,attentionState);
			if(thirdContactModel.mIsAttention == ThirdContactModel.ATTENTION.IS_ATTENTION){
				viewHolder.mAttentionIcon.setVisibility(View.VISIBLE);
			}else{
				viewHolder.mAttentionIcon.setVisibility(View.GONE);
			}
			viewHolder.mAttentionBtn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if(thirdContactModel.getmIsAttention() == IS_ATTENTION){
						INetResponse AttentionDelResponse = new INetReponseAdapter(){
							@Override
							public void onSuccess(INetRequest req, JsonObject data) {
								super.onSuccess(req, data);
								thirdContactModel.setmIsAttention(NO_ATTENTION);
								C_ContactsData.getInstance().setRenrenAttentionState(thirdContactModel.mUserId, NO_ATTENTION);
								setBtnState(viewHolder, NO_ATTENTION);
								mThirdContactsDAO.update_contact_attention_by_userId(thirdContactModel.mUserId, NO_ATTENTION);
								ChatFeedActivity.needRefresh = true;
							}
							
							@Override
							public void onError(INetRequest req, JsonObject data) {
								super.onError(req, data);
								//TODO 关注失败，提示~
								thirdContactModel.setmIsAttention(IS_ATTENTION);
								setBtnState(viewHolder, IS_ATTENTION);
							}
						};
						thirdContactModel.setmIsAttention(CANCEL_ATTENTIONING);
						setBtnState(viewHolder, CANCEL_ATTENTIONING);
						McsServiceProvider.getProvider().delFocusFriend(AttentionDelResponse, thirdContactModel.mRenRenId);
					}else{
						// 设置关注~
						if(C_ContactsData.getInstance().getRenrenAttentionContacts().size() < ATTENTION_MAX){
							INetResponse AttentionAddResponse = new INetReponseAdapter(){
								@Override
								public void onSuccess(INetRequest req, JsonObject data) {
									super.onSuccess(req, data);
									thirdContactModel.setmIsAttention(IS_ATTENTION);
									C_ContactsData.getInstance().setRenrenAttentionState(thirdContactModel.mUserId, IS_ATTENTION);
									setBtnState(viewHolder, IS_ATTENTION);
									// 关注成功，写入数据库~
									mThirdContactsDAO.update_contact_attention_by_userId(thirdContactModel.mUserId, IS_ATTENTION);
									ChatFeedActivity.needRefresh = true;
								}
								@Override
								public void onError(INetRequest req, JsonObject data) {
									super.onError(req, data);
									thirdContactModel.setmIsAttention(NO_ATTENTION);
									setBtnState(viewHolder, NO_ATTENTION);
									//TODO 关注失败，提示~
								}
							};
							thirdContactModel.setmIsAttention(ATTENTIONING);
							setBtnState(viewHolder, ATTENTIONING);
							McsServiceProvider.getProvider().addFocusFriend(AttentionAddResponse, thirdContactModel.mRenRenId);
						}else{
							SystemUtil.toast(R.string.Methods_java_32);
						}
					}
				}
			});
			return convertView;
		}
		return new TextView(RenrenChatApplication.mContext);
	}
	
	
	private void warp(ThirdContactModel model, AttentionAddHolder viewHolder){
		if(mDataType == C_ContactsData.TYPE.ALL_CONTACTS){
			this.showAlpheBar(model.mIsShowAlephLabel_InAll, viewHolder, model.mAlephString);
		}
		if(mDataType == C_ContactsData.TYPE.SEARCH_CONTACTS){
			this.showAlpheBar(false, viewHolder, model.mAlephString);
		}
//		holder.mShadowAlpheBar.setVisibility(View.GONE);
		if(model.mAlephString != null){
			viewHolder.mShadowAlpheBar.setVisibility(View.VISIBLE);
			viewHolder.mShadowAlpheBar.setText(model.mAlephString.toUpperCase());
		}
		if(mDataType == C_ContactsData.TYPE.SEARCH_CONTACTS){
			viewHolder.mUserNameView.setText(model.getDyeContactName());
		}else{
			viewHolder.mUserNameView.setText(model.mContactName);
		}
	}
	private void showAlpheBar(boolean flag,AttentionAddHolder holder,String text){
		if(flag ){
			if(text != null){
				holder.mAlpheBar.setText(text.toUpperCase());
			}
			holder.mAlpheBar.setVisibility(View.VISIBLE);
		}else{
			holder.mAlpheBar.setVisibility(View.GONE);
		}
	}
	
	private void setBtnState(final AttentionAddHolder viewHolder,final int state){
		RenrenChatApplication.sHandler.post(new Runnable() {
			@Override
			public void run() {
				switch (state) {
				case NO_ATTENTION:
					viewHolder.mAttentionBtn.setBackgroundResource(R.drawable.lc_attention_add_btn);
					viewHolder.mAttentionBtn.setVisibility(View.VISIBLE);
					viewHolder.mAttentionIcon.setVisibility(View.INVISIBLE);
					viewHolder.mProgressbar.setVisibility(View.GONE);
					break;
				case IS_ATTENTION:
					viewHolder.mAttentionBtn.setBackgroundResource(R.drawable.lc_attention_del_btn);
					viewHolder.mAttentionBtn.setVisibility(View.VISIBLE);
					viewHolder.mAttentionIcon.setVisibility(View.VISIBLE);
					viewHolder.mProgressbar.setVisibility(View.GONE);
					break;
				case ATTENTIONING:
				case CANCEL_ATTENTIONING:
					viewHolder.mAttentionBtn.setVisibility(View.INVISIBLE);
					viewHolder.mProgressbar.setVisibility(View.VISIBLE);
					break;
				default:
					break;
				}
			}
		});
	}
	/**
	 * 设置item的头像
	 * */
	private void setItemHeadImg(AttentionAddHolder viewHolder, ThirdContactModel contactModel) {
		String headUrl = contactModel.getmHeadUrl();
		viewHolder.mHeadImage.setDownloadAbleListener(mDownloadImageAbleListener);
		viewHolder.mHeadImage.clear();
		viewHolder.mHeadImage.addUrl(headUrl);
	}
}
