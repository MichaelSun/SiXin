package com.renren.mobile.chat.ui.contact.mutichat;

import java.io.InputStream;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore.Images;
import android.util.Log;

import com.common.manager.LoginManager;
import com.common.messagecenter.base.Utils;
import com.common.network.DomainUrl;
import com.renren.mobile.account.LoginControlCenter;
import com.renren.mobile.chat.RenrenChatApplication;
import com.renren.mobile.chat.activity.RenRenChatActivity;
import com.renren.mobile.chat.base.model.ChatBaseItem;
import com.renren.mobile.chat.base.util.ImageUtil;
import com.renren.mobile.chat.model.warpper.ChatMessageWarpper_Text;
import com.renren.mobile.chat.newsfeed.NewsFeedWarpper;
import com.renren.mobile.chat.ui.BaseActivity;
import com.renren.mobile.chat.ui.imageviewer.ImageViewActivity;


/**
 * @author xiangchao.fan
 * 转发中转页Activity
 */
public class MultiChatForwardActivity extends BaseActivity {

	private NewsFeedWarpper mNewsFeed;
	private String mFilePath = null;
	private boolean mIsFromOtherApp = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Intent intent = getIntent();
		Bundle extras = intent.getExtras();
		String action = intent.getAction();

		if(Intent.ACTION_SEND.equals(action)){
			
			if(LoginManager.getInstance().isLogout()){
				RenrenChatApplication.sNextIntent = (Intent) getIntent().clone();
				finish();
				LoginControlCenter.getInstance().gotoLogin(this);
			}
			
			mIsFromOtherApp = true;
			if (extras.containsKey(Intent.EXTRA_STREAM)) {
				try {
					Uri uri = (Uri) extras.getParcelable(Intent.EXTRA_STREAM);
					Utils.l("uri:" + uri);
		            if(uri.getScheme().startsWith("content")){
		            	Utils.l("");
		                ContentResolver cr = getContentResolver();
		                Cursor c = cr.query(uri,null,null,null,null);
		                c.moveToFirst();
		                mFilePath = c.getString(c.getColumnIndexOrThrow(Images.Media.DATA));
		                c.close();
		            }
		            Bitmap mBitmapSend = ImageUtil.image_Compression(mFilePath, RenrenChatApplication.screenResolution);
					ImageViewActivity.storeImageToFile(mBitmapSend, mFilePath, false);
		            Utils.l("mFilePath:" + mFilePath);
				} catch (Exception e) {
					Log.e(this.getClass().getName(), e.toString());
				}
			} else if (extras.containsKey(Intent.EXTRA_TEXT)) {
				String shareText = intent.getStringExtra(Intent.EXTRA_TEXT);
				ChatMessageWarpper_Text message = new ChatMessageWarpper_Text();
				message.mLocalUserId = LoginManager.getInstance().getLoginInfo().mUserId;
				message.mMessageContent = shareText;
				message.mComefrom= ChatBaseItem.MESSAGE_COMEFROM.LOCAL_TO_OUT;
				message.mDomain = DomainUrl.SIXIN_DOMAIN;
				RenrenChatApplication.sForwardMessage = message;
				mFilePath = "";
			}
		}else{
			mNewsFeed = (NewsFeedWarpper) getIntent().getSerializableExtra(RenRenChatActivity.PARAM_NEED.TO_CHAT_FEED_MODEL);
			mFilePath = getIntent().getStringExtra(RenRenChatActivity.PARAM_NEED.FORWARD_FILE_PATH);
		}
		Utils.l(mNewsFeed + ":" + mFilePath);
		MultiChatForwardScreen multiChatForwardScreen = new MultiChatForwardScreen(this, mNewsFeed, mFilePath, mIsFromOtherApp);
		setContentView(multiChatForwardScreen.getScreenView());
	}
	
	public static void show(Context context, NewsFeedWarpper newsFeed){
		Intent intent = new Intent(context, MultiChatForwardActivity.class);
		intent.putExtra(RenRenChatActivity.PARAM_NEED.CONTEXT_FROM, context.getClass().getSimpleName());
		intent.putExtra(RenRenChatActivity.PARAM_NEED.TO_CHAT_FEED_MODEL, newsFeed);
		context.startActivity(intent);
	}
	
	/**
	 * 
	 * @param context
	 * @param newsFeed
	 * @param filePath 图片转发
	 */
	public static void show(Context context, NewsFeedWarpper newsFeed, String filePath){
		Intent intent = new Intent(context, MultiChatForwardActivity.class);
		intent.putExtra(RenRenChatActivity.PARAM_NEED.CONTEXT_FROM, context.getClass().getSimpleName());
		intent.putExtra(RenRenChatActivity.PARAM_NEED.TO_CHAT_FEED_MODEL, newsFeed);
		intent.putExtra(RenRenChatActivity.PARAM_NEED.FORWARD_FILE_PATH, filePath);
		Utils.l("filePath = " + filePath);
		context.startActivity(intent);
	}
}
