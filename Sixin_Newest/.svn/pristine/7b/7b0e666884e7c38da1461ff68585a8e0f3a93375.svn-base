package com.renren.mobile.chat.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.Html;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.common.emotion.emotion.EmotionString;
import com.core.util.ViewMapUtil;
import com.core.util.ViewMapping;
import com.renren.mobile.chat.R;
import com.renren.mobile.chat.base.util.SystemUtil;
import com.renren.mobile.chat.base.views.NewImageView;

/**
 * @author dingwei.chen
 * 新鲜事展示控件(内容控件)
 * */
public class ChatFeedView extends LinearLayout{

	
	
	
	@ViewMapping(ID=R.id.feed_view)
	public ViewGroup mFeedView ;
	
	
	@ViewMapping(ID=R.id.description)
	public TextView mDescription;
	public void setDescription(String descrip){
		this.setTextView(mDescription, descrip, true);
	}
	
	
	@ViewMapping(ID=R.id.forward_line)
	public View mForwardLine;
	public void setForwardLine(String forwardLine){
		if(forwardLine!=null&&forwardLine.equals("true")){
			mForwardLine.setVisibility(View.VISIBLE);
		}else{
			mForwardLine.setVisibility(View.GONE);
		}
	}
	
	
	@ViewMapping(ID=R.id.original_name)
	public TextView mOriginal_Name;
	public void setOriginal_Name(String original_Name){
		this.setTextView(mOriginal_Name, original_Name, false);
	}
	
	
	@ViewMapping(ID=R.id.original_descrip)
	public TextView mOriginal_Descrip;
	public void setOriginal_Descrip(String original_Descrip){
		this.setTextView(mOriginal_Descrip, original_Descrip, true);
	}
	
	
	@ViewMapping(ID=R.id.photos)
	public ViewGroup mPhotos;
	
	@ViewMapping(ID=R.id.video_group)
	public ViewGroup mVideoGroup = null;
	public void resetVideoGroupParams(){
		android.view.ViewGroup.LayoutParams params = mVideoGroup.getLayoutParams();
		params.width = mPhoto1.getLayoutParams().width;
		params.height = mPhoto1.getLayoutParams().height;
		mVideoGroup.setLayoutParams(params);
	}
	
	
	@ViewMapping(ID=R.id.photo1)
	public NewImageView mPhoto1;
	public void setPhoto1(String photo1Url,Bitmap defalutBitmap){
		
		if(this.setImageView(mPhoto1, photo1Url, defalutBitmap)){
			mPhotos.setVisibility(View.VISIBLE);
		}else{
			mPhotos.setVisibility(View.GONE);
		}
	}
	
	@ViewMapping(ID=R.id.photo2)
	public NewImageView mPhoto2;
	public void setPhoto2(String photo2Url,Bitmap defalutBitmap){
		if(this.setImageView(mPhoto2, photo2Url, defalutBitmap)){
			mPhotos.setVisibility(View.VISIBLE);
		}
	}
	
	
	@ViewMapping(ID=R.id.summary)
	public TextView mSummary;
	public void setSummary(String summary){
		this.setTextView(mSummary, summary, true);
	}
	
	
	
	@ViewMapping(ID=R.id.place)
	public ViewGroup mLocation;
	
	@ViewMapping(ID=R.id.placename)
	public TextView mPlaceName;
	public void setPlaceName(String placeName){
		if(this.setTextView(mPlaceName, placeName, false)){
			mLocation.setVisibility(View.VISIBLE);
		}else{
			mLocation.setVisibility(View.GONE);
		}
	}
	
	@ViewMapping(ID=R.id.reply)
	public View mReply;
	
	@ViewMapping(ID=R.id.reply_name_content)
	public TextView mReplyNameContent;
	public void setReplyNameAndContent(String name,String content){
		String nameAndContent = null;
		if(name!=null){
			nameAndContent = name+":"+content;
		}
		if(this.setTextView(mReplyNameContent, nameAndContent, true)){
			mReply.setVisibility(View.VISIBLE);
		}else{
			mReply.setVisibility(View.GONE);
		}
	}
	
	@ViewMapping(ID=R.id.video)
	public View mVideoView = null;
	public void setVideo(String video){
		if(video!=null&& video.equals("true")){
			mVideoView.setVisibility(View.VISIBLE);
		}else{
			mVideoView.setVisibility(View.GONE);
		}
	}
	
	
	
	//@ViewMapping(ID=R.id.cdw_feed_divier1)
	//public View  mFeedDivier1 = null;
	
	@ViewMapping(ID=R.id.cdw_feed_divier2)
	public View  mFeedDivier2 = null;
	
	public View mRootView = null;
	
	
	
	public boolean setTextView(TextView textView ,String text,boolean isEmotion){
		if(text!=null){
			if(isEmotion){
				textView.setText(new EmotionString(Html
						.fromHtml(text)));
			}else{
				textView.setText(text);
			}
			textView.setVisibility(View.VISIBLE);
			return true;
		}else{
			textView.setVisibility(View.GONE);
			return false;
		}
	}
	public boolean setImageView(NewImageView imageview ,String imageUrl,Bitmap defalutBitmap){
		mPhoto1.setOnClickListener(null);
		if(imageUrl!=null){
			imageview.setURL(imageUrl, defalutBitmap);
			imageview.setVisibility(View.VISIBLE);
			return true;
		}else{
			imageview.setVisibility(View.INVISIBLE);
			return false;
		}
	}
	
	
	
	public ChatFeedView(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater inflater =(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mRootView= inflater.inflate(R.layout.cdw_feed_layout, this);
		ViewMapUtil.getUtil().viewMapping(this, this);
	}

	
	public void initFeedView() {
		mLocation.setVisibility(View.GONE);
		mReply.setVisibility(View.GONE);
	}
	
	public void setBottomLineDissmiss(){
		//this.mFeedDivier1.setVisibility(View.GONE);
		this.mFeedDivier2.setVisibility(View.GONE);
	}
	
	
	
	AbstractFeedAdapter mAdapter  = null;
	public void setAdapter(AbstractFeedAdapter adapter){
			mAdapter = adapter;
			mAdapter.notifyChange(this);
	}
	
	public void setBackgroundDissmiss(){
		mFeedView.setBackgroundDrawable(null);
	}
	

	
	
}
