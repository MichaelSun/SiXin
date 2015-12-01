package com.renren.mobile.chat.model.facade;

import view.list.ListViewContentLayout;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;

import com.common.manager.MessageManager.OnSendTextListener.SEND_TEXT_STATE;
import com.common.network.DomainUrl;
import com.core.util.DipUtil;
import com.renren.mobile.chat.R;
import com.renren.mobile.chat.activity.RenRenChatActivity.CanTalkable.GROUP;
import com.renren.mobile.chat.base.GlobalValue;
import com.renren.mobile.chat.base.model.ChatBaseItem;
import com.renren.mobile.chat.base.model.ChatBaseItem.MESSAGE_COMEFROM;
import com.renren.mobile.chat.base.model.ChatBaseItem.MESSAGE_TYPE;
import com.renren.mobile.chat.base.util.SystemUtil;
import com.renren.mobile.chat.holder.ChatItemHolder;
import com.renren.mobile.chat.model.warpper.ChatMessageWarpper;
import com.renren.mobile.chat.util.BaseChatListAdapter.OnItemLongClickCallback;
import com.renren.mobile.chat.util.ChatItemFeedAdapter;


/**
 * @author dingwei.chen
 * @说明 聊天条目的装饰器(也被用来处理视图事件)
 * */
public abstract class ChatItemFacader {
	
	public static final ChatItemFeedAdapter FEED_ADAPTER = new ChatItemFeedAdapter();
	public static final int BOTTOM = DipUtil.calcFromDip(6);
	public void facadeMessage(ChatItemHolder holder,ChatMessageWarpper chatmessage,final OnItemLongClickCallback callback){
		holder.mMessage_Attach.setVisibility(View.VISIBLE);
		holder.mMessage_HeadImage_LinearLayout.setVisibility(View.VISIBLE);
		View view = getFacadeView(holder);
		if(view!=null){
			view.setVisibility(View.VISIBLE);
		}else{
			this.showCover(holder, chatmessage);
			this.showBackground(holder, chatmessage, callback);//显示背景
			this.showFeed(holder, chatmessage, callback);//显示新鲜事
			holder.mMessage_HeadImage_LinearLayout.setVisibility(View.VISIBLE);
			this.facade(holder, chatmessage,callback);
			return;
		}
		holder.mRootView.setVisibility(View.VISIBLE);
		holder.mMessage_Content_ViewGroup.setOnClickListener(null);//清空事件响应
		this.showBackground(holder, chatmessage, callback);//显示背景
		this.showFeed(holder, chatmessage, callback);//显示新鲜事
		this.showLoading(holder, chatmessage, callback);//显示loading框
		this.showError(holder, chatmessage, callback);//显示错误
		this.showDomain(holder, chatmessage);
		this.hideFeedDeleteButton(holder, chatmessage);
		this.facade(holder, chatmessage,callback);
		this.showCover(holder, chatmessage);
	}
	private void showCover(ChatItemHolder holder,ChatMessageWarpper chatmessage){
		if(chatmessage.isShowCover()){
			holder.mMessage_Content_ViewGroup.showCover(chatmessage.mComefrom==MESSAGE_COMEFROM.OUT_TO_LOCAL);
		}else{
			holder.mMessage_Content_ViewGroup.hideCover();
		}
	}
	
	
	
	private void showDomain(ChatItemHolder holder,ChatMessageWarpper chatmessage){
		if(holder.mMessage_Domain!=null){
			holder.mMessage_Domain.setVisibility(View.GONE);
		}
		if(chatmessage.mComefrom==ChatBaseItem.MESSAGE_COMEFROM.OUT_TO_LOCAL && holder.mMessage_Domain!=null){
			if(DomainUrl.RENREN_SIXIN_DOMAIN.equals(chatmessage.mDomain)){
				holder.mMessage_Domain.setVisibility(View.VISIBLE);
			}
		}
	}
	
	
	
	private void hideFeedDeleteButton(ChatItemHolder holder,ChatMessageWarpper chatmessage){
		if(holder.mMessage_FeedDeleteButton!=null){
			holder.mMessage_FeedDeleteButton.setVisibility(View.GONE);
		}
	}
	
	private void showBackground(ChatItemHolder holder,ChatMessageWarpper chatmessage,final OnItemLongClickCallback callback){
		if(chatmessage.mComefrom==ChatBaseItem.MESSAGE_COMEFROM.LOCAL_TO_OUT){
			holder.mMessage_Content_ViewGroup.setBackgroundResource(R.drawable.cdw_chat_listview_item_right);
		}else if(chatmessage.mComefrom==ChatBaseItem.MESSAGE_COMEFROM.OUT_TO_LOCAL){
			holder.mMessage_Content_ViewGroup.setBackgroundResource(R.drawable.cdw_chat_listview_item_left);
		}else if(chatmessage.mComefrom==ChatBaseItem.MESSAGE_COMEFROM.NULL){
			holder.mMessage_Content_ViewGroup.setBackgroundResource(R.drawable.feed_bg_2);
		}
		if(holder.mMessage_Content_ViewGroup instanceof ListViewContentLayout){
			((ListViewContentLayout)holder.mMessage_Content_ViewGroup).setForegroundAlpha(0);
		}
		Drawable drawable= holder.mMessage_Content_ViewGroup.getBackground();
		Rect r = new Rect();
		drawable.getPadding(r);
		if(chatmessage.mComefrom==ChatBaseItem.MESSAGE_COMEFROM.LOCAL_TO_OUT){
			holder.mMessage_HeadImage_LinearLayout.setPadding( DipUtil.calcFromDip(6), 0, DipUtil.calcFromDip(5), 0);
		}else{
			holder.mMessage_HeadImage_LinearLayout.setPadding( DipUtil.calcFromDip(5), 0, DipUtil.calcFromDip(6), 0);
		}
		
		if(chatmessage.isHideBackground()){
			holder.mMessage_Content_ViewGroup.setBackgroundColor(0x00000000);
			holder.mMessage_Content_ViewGroup.setPadding(r.left, 0, r.right, 0);
		}else{
			if((chatmessage.getMessageType()!=MESSAGE_TYPE.IMAGE &&!chatmessage.hasNewsFeed() &&chatmessage.getMessageType()!=MESSAGE_TYPE.FLASH)
			||(chatmessage.getMessageType()==MESSAGE_TYPE.NULL)){
				if(chatmessage.getMessageType()!=MESSAGE_TYPE.NULL){
					if(chatmessage.mComefrom==MESSAGE_COMEFROM.OUT_TO_LOCAL){
						holder.mMessage_Content_ViewGroup
								.setPadding(DIP_SIXTEEN,  DIP_TEN, DIP_TEN,  DIP_TEN);
					}else{
						holder.mMessage_Content_ViewGroup
							.setPadding(DIP_TEN, DIP_TEN, DIP_SIXTEEN, DIP_TEN);
					}
				}else{
					holder.mMessage_Content_ViewGroup
						.setPadding(DipUtil.calcFromDip(14), DipUtil.calcFromDip(12), DipUtil.calcFromDip(20), DipUtil.calcFromDip(14));
					holder.mMessage_HeadImage_LinearLayout.setPadding( DipUtil.calcFromDip(2), 0, DipUtil.calcFromDip(5), DipUtil.calcFromDip(8));
				}
			}else{//图片
				if(chatmessage.mComefrom==MESSAGE_COMEFROM.OUT_TO_LOCAL){
					holder.mMessage_Content_ViewGroup
							.setPadding(DIP_FORTEEN, DIP_SIX, DIP_SIX, DIP_SIX);
				}else{
					holder.mMessage_Content_ViewGroup
						.setPadding(DIP_SIX, DIP_SIX, DIP_FORTEEN,DIP_SIX);
				}
			}
			
		}
	}
//	  nn  18:42:48
//	  上边距10dp
//	  左边边距12dp
//	    nn  18:43:51
//	  右边边距20dp
//	  下面边距14dp
	
	
	
	public static final int DIP_SIX = DipUtil.calcFromDip(6);
	public static final int DIP_FORTEEN = DipUtil.calcFromDip(14);
	public static final int DIP_SIXTEEN = DipUtil.calcFromDip(16);
	public static final int DIP_TEN = DipUtil.calcFromDip(10);
	private void showFeed(ChatItemHolder holder,ChatMessageWarpper chatmessage,final OnItemLongClickCallback callback){
		if(!chatmessage.hasNewsFeed()){
			holder.mMessage_FeedView.setVisibility(View.GONE);
			holder.mMessage_Content_ViewGroup.hideFeed();
		}else{
			if(chatmessage.mNewsFeedMessage!=null){
				LayoutParams params = holder.mMessage_FeedView.getLayoutParams();
				int screenWidth = GlobalValue.getInstance().getScreenWidth();
				params.width = (int)(0.65*screenWidth);
				holder.mMessage_FeedView.setLayoutParams(params);
				holder.mMessage_FeedView.setVisibility(View.VISIBLE);
				FEED_ADAPTER.updateModel(chatmessage.mNewsFeedMessage);
				holder.mMessage_FeedView.setAdapter(FEED_ADAPTER);
				holder.mMessage_FeedView.mFeedDivier2.setVisibility(View.GONE);
				holder.mMessage_FeedView.resetVideoGroupParams();
				holder.mMessage_Content_ViewGroup.showFeed();
			}else{
				holder.mMessage_Content_ViewGroup.hideFeed();
			}
		}
	}
	private void showLoading(ChatItemHolder holder,ChatMessageWarpper chatmessage,final OnItemLongClickCallback callback){
		if(chatmessage.mSendTextState == SEND_TEXT_STATE.SEND_PREPARE){
			holder.mMessage_Loading.setVisibility(View.VISIBLE);
		}else{
			holder.mMessage_Loading.setVisibility(View.INVISIBLE);
		}
	}
	private void showError(ChatItemHolder holder,ChatMessageWarpper chatmessage,final OnItemLongClickCallback callback){
		if(chatmessage.isError()){
			holder.mMessage_Error_ImageView.setVisibility(View.VISIBLE);
		}else{
			holder.mMessage_Error_ImageView.setVisibility(View.INVISIBLE);
		}
	}
	
	public abstract void facade(ChatItemHolder holder,ChatMessageWarpper chatmessage,OnItemLongClickCallback callback);
	public abstract View getFacadeView(ChatItemHolder holder);
	
	
	public void setGone(View...views){
		for(View v:views){
			v.setVisibility(View.GONE);
		}
	}
	
	
	
}
