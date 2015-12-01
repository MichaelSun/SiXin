package com.renren.mobile.chat.util;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.View.OnClickListener;

import com.renren.mobile.chat.R;
import com.renren.mobile.chat.newsfeed.NewsFeedWarpper;
import com.renren.mobile.chat.ui.imageviewer.ImageViewActivity;
import com.renren.mobile.chat.ui.imageviewer.ImageViewActivity.NEED_PARAM;
import com.renren.mobile.chat.views.AbstractFeedAdapter;
import com.renren.mobile.chat.views.ChatFeedView;

/**
 * @author dingwei.chen
 * @说明 新鲜事适配器
 * */
public class ChatItemFeedAdapter extends AbstractFeedAdapter{
	
	
	public NewsFeedWarpper m = null;
	public static Bitmap sDefaultBitmap = null;
	public ChatItemFeedAdapter(){}
	public void updateModel(NewsFeedWarpper newsFeedModel){
		this.m = newsFeedModel;
	}
	
	@Override
	protected void processFeedView(ChatFeedView view) {
		if(sDefaultBitmap == null){
			sDefaultBitmap = BitmapFactory
			.decodeResource(view.getResources(), R.drawable.cdw_chat_listview_item_default_image2);
		}
		view.initFeedView();
		if(m!=null){
			view.setDescription(m.mFeedDescription);
			view.setForwardLine(m.mFeed_Show_Forward);
			view.setOriginal_Name(m.mFeedOriginal_Name);
			view.setOriginal_Descrip(m.mFeedOriginal_Descip);
			view.setPhoto1(m.mImage1_Url_Main, sDefaultBitmap);
			view.setPhoto2(m.mImage2_Url_Main, sDefaultBitmap);
			view.setPlaceName(m.mFeedPlaceName);
			view.setReplyNameAndContent(m.mFeed_Reply_UserName, m.mFeed_Reply_Content);
			view.setSummary(m.mFeed_Summary);
			view.setVideo(m.mFeed_Video);
		}
		if(m.mImage1_Url_Large!=null){
			view.mPhoto1.setOnClickListener(new OnViewClickImpl(m.mImage1_Url_Large));
		}
		if(m.mImage2_Url_Large!=null){
			view.mPhoto2.setOnClickListener(new OnViewClickImpl(m.mImage2_Url_Large));
		}
	}
	
	public class OnViewClickImpl implements OnClickListener{
	
		public String mImageUrl = null;
		public OnViewClickImpl(String imageUrl){
			mImageUrl = imageUrl;
		}
		
		public void onClick(View v) {
			Intent intent = new Intent(v.getContext(),ImageViewActivity.class);
			intent.putExtra(NEED_PARAM.REQUEST_CODE, ImageViewActivity.VIEW_LARGE_IMAGE);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.putExtra("url", mImageUrl);
			v.getContext().startActivity(intent);
//			DispatcherActivityCenter.getInstance().dispatch(v.getContext(), intent);
		}
	}
	
}
