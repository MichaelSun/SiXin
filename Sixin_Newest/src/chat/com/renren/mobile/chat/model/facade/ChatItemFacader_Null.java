package com.renren.mobile.chat.model.facade;

import android.view.View;
import android.view.View.OnClickListener;

import com.renren.mobile.chat.R;
import com.renren.mobile.chat.base.util.SystemUtil;
import com.renren.mobile.chat.holder.ChatItemHolder;
import com.renren.mobile.chat.model.warpper.ChatMessageWarpper;
import com.renren.mobile.chat.model.warpper.ChatMessageWarpper_Null;
import com.renren.mobile.chat.util.BaseChatListAdapter.OnItemLongClickCallback;

public class ChatItemFacader_Null extends ChatItemFacader{

	@Override
	public void facade(ChatItemHolder holder,final ChatMessageWarpper chatmessage,
			OnItemLongClickCallback callback) {
		holder.mMessage_Attach.setVisibility(View.INVISIBLE);
		if(chatmessage.hasNewsFeed()){
			if(holder.mMessage_FeedDeleteButton!=null){
				holder.mMessage_FeedDeleteButton.setVisibility(View.VISIBLE);
				holder.mMessage_FeedDeleteButton.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						((ChatMessageWarpper_Null)chatmessage).deleteFeed();
					}
				});
			}
			holder.mMessageTo_LinearLayout.setVisibility(View.VISIBLE);
			this.setGone(
					holder.mMessage_FlashMessage_LinearLayout,
					holder.mMessage_ImageMessage_LinearLayout,
					holder.mMessage_TextMessage_LinearLayout,
					holder.mMessage_VoiceMessage_LinearLayout,
					//holder.mMessage_FeedView.mFeedDivier1,
					holder.mMessage_FeedView.mFeedDivier2);
			holder.mMessage_FeedView.setVisibility(View.VISIBLE);
		}else{
			holder.mMessage_FeedView.setVisibility(View.GONE);
			this.setGone(holder.mMessageTo_LinearLayout);
		}
	}

	@Override
	public View getFacadeView(ChatItemHolder holder) {
		return null;
	}

}
