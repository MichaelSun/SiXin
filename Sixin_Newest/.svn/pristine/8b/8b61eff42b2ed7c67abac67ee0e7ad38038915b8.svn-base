package com.renren.mobile.chat.holder;

import java.util.Map;

import view.list.ListViewContentLayout;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.core.util.SystemService;
import com.core.util.ViewMapUtil;
import com.core.util.ViewMapping;
import com.renren.mobile.chat.R;
import com.renren.mobile.chat.base.BaseHolder;
import com.renren.mobile.chat.base.GlobalValue;
import com.renren.mobile.chat.base.inter.Observer;
import com.renren.mobile.chat.base.inter.Subject;
import com.renren.mobile.chat.base.model.ChatBaseItem;
import com.renren.mobile.chat.base.util.SystemUtil;
import com.renren.mobile.chat.base.views.AnimationLinearLayout;
import com.renren.mobile.chat.base.views.BaseImageView;
import com.renren.mobile.chat.base.views.EmotionTextView;
import com.renren.mobile.chat.base.views.NotSynImageView;
import com.renren.mobile.chat.base.views.SwitchImageView;
import com.renren.mobile.chat.views.ChatFeedView;
import com.renren.mobile.chat.views.VoiceLinearLayout;

/**
 * @author dingwei.chen
 * @说明 消息条目的持有类
 * @图片部分  	{@link #updateImage(int, Map);}
 * @语音
 * 	@see #updateVoice(int, Map)
 * */
public class ChatItemHolder extends BaseHolder implements Observer {

	InitViewHolder_From mFromInit = null;
	InitViewHolder_To mToInit = null;
	//弱消息提醒
	public TextView mMessageNotify_TextView = null;
	//空消息
	public View mMessageNull_View = null;
	
	// OUT_TO_LOCAL的消息
	public ViewGroup mMessageFrom_LinearLayout = null;// 接收的消息条目视图,视图中包含mMessageFrom_HeadImage_ImageView和mMessageFrom_Content_LinearLayout
	public ImageView mMessage_Voice_Unlisten_ImageView = null;// 发送失败的icon
	

	// LOCAL_TO_OUT的消息
	public ViewGroup mMessageTo_LinearLayout = null;// 发送出去的消息内容区域
	
	

	// public
	@ViewMapping(ID = R.id.cdw_chat_listview_item_send_error_imageview)
	public TextView mMessage_Time_TextView = null;
	public ImageView mMessage_Error_ImageView = null;// 发送失败的icon
	public ViewGroup mMessage_Content_LinearLayout = null;// 发送出去的消息内容区域
	public ListViewContentLayout mMessage_Content_ViewGroup = null;// 发送出去的消息内容区域
	public LinearLayout mMessage_TextMessage_LinearLayout = null;// 文本消息
	public ViewGroup mMessage_ImageMessage_LinearLayout = null;// 图片消息
	public VoiceLinearLayout mMessage_VoiceMessage_LinearLayout = null;// 声音消息
	public LinearLayout mMessage_FlashMessage_LinearLayout = null;// flash表情
	
	
	public EmotionTextView mMessage_TextView = null;

	public LinearLayout mMessage_HeadImage_LinearLayout = null;// 头像
	public NotSynImageView mMessage_HeadImage_ImageView = null;// 头像
	public BaseImageView mMessage_Image_ImageView = null;// 图片消息
	public SwitchImageView mMessage_Voice_ImageView = null;// 语音消息
	public ImageView mMessage_Flash_ImageView = null;// flash图片
	public TextView mMessage_Flash_TextView = null;// flash文本
	public TextView mMessage_Voice_Time_TextView = null;// 语音消息的时间
	public CheckBox mMessage_Delete_CheckBox = null;// 发送方式
	public ChatFeedView mMessage_FeedView = null;//新鲜事显示
	public View mMessage_Loading = null;//加载控件
	public View mMessage_Attach = null;//附加控件
	public TextView mMessage_UserName = null;//用户名
	protected Subject mSubject = null;
	public View mMessage_FeedDeleteButton = null;
	public View mMessage_Domain = null;
	
	public interface ID {

		int MESSAGE_TIME_ID = R.id.cdw_listview_item_time;// 消息的时间
//		int MESSAGE_DELETE_CHECKBOAX_ID = R.id.delete_checkbox;// 删除按钮
		int MESSAGE_COMEFROM_FROM = R.id.chat_voice_from_linearlayout;// 来自的消息（条目中包含有头像和消息内容）
		int MESSAGE_COMEFROM_TO = R.id.chat_voice_to_linearlayout;// 发送的消息（条目中包含有头像和消息内容）
		int MESSAGE_COMEFROM_NULL = R.id.chat_item_null;// 空消息
		int MESSAGE_COMEFROM_NOTIFY = R.id.chat_item_notify;// 弱消息
	}

	public ChatItemHolder(Context context) {
		LayoutInflater inflater = SystemService.sInflaterManager;
		this.mRootView = inflater.inflate(R.layout.cdw_listview_item, null);
		mMessage_Time_TextView = (TextView) this.mRootView
				.findViewById(ID.MESSAGE_TIME_ID);
		mMessageFrom_LinearLayout = (ViewGroup) this.mRootView
				.findViewById(ID.MESSAGE_COMEFROM_FROM);
		mMessageTo_LinearLayout = (ViewGroup) this.mRootView
				.findViewById(ID.MESSAGE_COMEFROM_TO);
		mMessageNull_View = (View)this.mRootView
				.findViewById(ID.MESSAGE_COMEFROM_NULL);
		mMessageNotify_TextView = (TextView)this.mRootView
				.findViewById(ID.MESSAGE_COMEFROM_NOTIFY);
		mFromInit = new InitViewHolder_From(mMessageFrom_LinearLayout);
		mToInit = new InitViewHolder_To(mMessageTo_LinearLayout);
		mFromInit.initView(this, ChatBaseItem.MESSAGE_COMEFROM.OUT_TO_LOCAL);
		mToInit.initView(this, ChatBaseItem.MESSAGE_COMEFROM.LOCAL_TO_OUT);
	}

	public void selectView(int comefrom, int messageType) {

		switch (comefrom) {
		case ChatBaseItem.MESSAGE_COMEFROM.OUT_TO_LOCAL:
			mMessageFrom_LinearLayout.setVisibility(View.VISIBLE);
			mMessageTo_LinearLayout.setVisibility(View.GONE);
			mMessageNull_View.setVisibility(View.GONE);
			mMessageNotify_TextView.setVisibility(View.GONE);
			mFromInit.initView(this, messageType);
			mMessage_Voice_Unlisten_ImageView.setVisibility(View.GONE);
			break;

		case ChatBaseItem.MESSAGE_COMEFROM.LOCAL_TO_OUT:
			mMessageFrom_LinearLayout.setVisibility(View.GONE);
			mMessageTo_LinearLayout.setVisibility(View.VISIBLE);
			mMessageNull_View.setVisibility(View.GONE);
			mToInit.initView(this, messageType);
			mMessageNotify_TextView.setVisibility(View.GONE);
			mMessage_Error_ImageView.setVisibility(View.INVISIBLE);
			break;
		case ChatBaseItem.MESSAGE_COMEFROM.NOTIFY:
			mMessageFrom_LinearLayout.setVisibility(View.GONE);
			mMessageTo_LinearLayout.setVisibility(View.GONE);
			mMessageNull_View.setVisibility(View.GONE);
			mMessageNotify_TextView.setVisibility(View.VISIBLE);
			return;
		case ChatBaseItem.MESSAGE_COMEFROM.NULL:
			mMessageFrom_LinearLayout.setVisibility(View.GONE);
			mMessageTo_LinearLayout.setVisibility(View.VISIBLE);
			mMessageNotify_TextView.setVisibility(View.GONE);
			mToInit.initView(this, messageType);
			mMessageNull_View.setVisibility(View.VISIBLE);
			return;
		}
		this.clearData();
	}

	private void clearData() {
		this.mMessage_TextMessage_LinearLayout.setVisibility(View.GONE);
		this.mMessage_ImageMessage_LinearLayout.setVisibility(View.GONE);
		this.mMessage_VoiceMessage_LinearLayout.setVisibility(View.GONE);
		this.mMessage_FlashMessage_LinearLayout.setVisibility(View.GONE);
	}

	private void updateText(int command, Map<String, Object> data) {
		switch (command) {
		case Subject.COMMAND.COMMAND_MESSAGE_SEND_SHOW_LOADING: {
			mMessage_Loading.setVisibility(View.VISIBLE);
			break;
		}
		case Subject.COMMAND.COMMAND_MESSAGE_SEND_HIDE_LOADING: {
			mMessage_Loading.setVisibility(View.GONE);
			break;
		}
		case Subject.COMMAND.COMMAND_MESSAGE_ERROR: {
			mMessage_Error_ImageView.setVisibility(View.VISIBLE);
			break;
		}
		case Subject.COMMAND.COMMAND_MESSAGE_OVER: {
			mMessage_Error_ImageView.setVisibility(View.INVISIBLE);
			break;
		}
		}
	}

	private void updateImage(int command, Map<String, Object> data) {
		
		switch (command) {
		case Subject.COMMAND.COMMAND_MESSAGE_SEND_SHOW_LOADING: {
			mMessage_Loading.setVisibility(View.VISIBLE);
			break;
		}
		case Subject.COMMAND.COMMAND_MESSAGE_SEND_HIDE_LOADING: {
			mMessage_Loading.setVisibility(View.GONE);
			break;
		}
		/*------------------------图片部分-----------------------------------*/
		case Subject.COMMAND.COMMAND_MESSAGE_SEND_SHOW_IMAGE_LAYER: {
			this.mMessage_Image_ImageView.setLayerVisible(true);
			break;
		}
		case Subject.COMMAND.COMMAND_MESSAGE_SEND_HIDE_IMAGE_LAYER: {
			this.mMessage_Image_ImageView.setLayerVisible(false);
			break;
		}
		/* 图片正在上传 */
		case Subject.COMMAND.COMMAND_IMAGE_UPLOADING: {
			this.mMessage_Image_ImageView.setLayerVisible(true);
			mMessage_Error_ImageView.setVisibility(View.INVISIBLE);
			break;
		}

		/* 图片上传失败 */
		case Subject.COMMAND.COMMAND_UPLOAD_IMAGE_ERROR: {
			mMessage_Error_ImageView.setVisibility(View.VISIBLE);
			this.mMessage_Image_ImageView.setLayerVisible(false);
			break;
		}
		/* 图片上传成功 */
		case Subject.COMMAND.COMMAND_UPLOAD_IMAGE_OVER: {
			mMessage_Error_ImageView.setVisibility(View.INVISIBLE);
			this.mMessage_Image_ImageView.setLayerVisible(false);
			break;
		}
		
		
		
		
		
		/* 下载图片完成 */
		case Subject.COMMAND.COMMAND_DOWNLOAD_IMAGE_OVER: {
			this.mMessage_Image_ImageView.setVisibility(View.VISIBLE);
			if (data != null) {
				Bitmap bitmap = (Bitmap) data
						.get(Subject.DATA.COMMAND_IMAGE_DOWNLOAD_OVER);
				mMessage_Image_ImageView.setImageBitmap(bitmap);
			}
			this.mMessage_Error_ImageView.setVisibility(View.INVISIBLE);
			this.mMessage_Image_ImageView.setLayerVisible(false);
			break;
		}
		/* 图片正在下载 */
		case Subject.COMMAND.COMMAND_IMAGE_DOWNLOADING: {
			this.mMessage_Error_ImageView.setVisibility(View.INVISIBLE);
			this.mMessage_Image_ImageView.setImageBitmap(GlobalValue
					.getInstance().getDefualtBitmap(true));
			this.mMessage_Image_ImageView.setLayerVisible(true);
			this.mMessage_Loading.setVisibility(View.VISIBLE);
			break;
		}
		/* 图片下载失败 */
		case Subject.COMMAND.COMMAND_DOWNLOAD_IMAGE_ERROR: {
			mMessage_Error_ImageView.setVisibility(View.VISIBLE);
			this.mMessage_Image_ImageView.setLayerVisible(false);
			mMessage_Image_ImageView.setImageBitmap(GlobalValue.getInstance()
					.getDefualtBitmap(true));
			break;
		}
		}
	}

	public void updateVoice(int command, Map<String, Object> data) {
		switch (command) {
		case Subject.COMMAND.COMMAND_MESSAGE_SEND_SHOW_LOADING: {
			mMessage_Loading.setVisibility(View.VISIBLE);
			break;
		}
		case Subject.COMMAND.COMMAND_MESSAGE_SEND_HIDE_LOADING: {
			mMessage_Loading.setVisibility(View.GONE);
			break;
		}
		/*------------------------语音部分-----------------------------------*/
	
		/* 上传语音失败 */
		case Subject.COMMAND.COMMAND_UPLOAD_VOICE_ERROR: {
			mMessage_Error_ImageView.setVisibility(View.VISIBLE);
			mMessage_Voice_Time_TextView.setVisibility(View.VISIBLE);
			if (data != null) {
				mMessage_Voice_Time_TextView.setText((String) data
						.get(Subject.DATA.COMMAND_UPDATE_VOICE_TIME));
				mMessage_Voice_ImageView.switchImage((Integer) data
						.get(Subject.DATA.COMMAND_UPDATE_VOICE_VIEW));
			}
			break;
		}
		case Subject.COMMAND.COMMAND_UPLOAD_VOICE_OVER: {
			mMessage_Error_ImageView.setVisibility(View.INVISIBLE);
			mMessage_Voice_Time_TextView.setVisibility(View.VISIBLE);
			if (data != null) {
				mMessage_Voice_Time_TextView.setText((String) data
						.get(Subject.DATA.COMMAND_UPDATE_VOICE_TIME));
				mMessage_Voice_ImageView.switchImage((Integer) data
						.get(Subject.DATA.COMMAND_UPDATE_VOICE_VIEW));
			} 
			break;
		}
		/* 语音开始上传 */
		case Subject.COMMAND.COMMAND_VOICE_UPLOADING: {
			mMessage_Error_ImageView.setVisibility(View.INVISIBLE);
			break;
		}
		
		/*===========================下载==========================*/
		/* 语音开始下载 */
		case Subject.COMMAND.COMMAND_VOICE_DOWNLOADING: {
			mMessage_Error_ImageView.setVisibility(View.INVISIBLE);//接受失败
			mMessage_Voice_Unlisten_ImageView.setVisibility(View.GONE);
			break;
		}
		/* 语音下载失败 */
		case Subject.COMMAND.COMMAND_DOWNLOAD_VOICE_ERROR: {
			mMessage_Error_ImageView.setVisibility(View.VISIBLE);
			break;
		}
		/* 语音下载成功 */
		case Subject.COMMAND.COMMAND_DOWNLOAD_VOICE_OVER: {
			mMessage_Error_ImageView.setVisibility(View.INVISIBLE);
			break;
		}
		/* 语音播放结束 */
		case Subject.COMMAND.COMMAND_PLAY_VOICE_OVER: {
//			mMessage_Error_ImageView.setVisibility(View.GONE);
			if (data != null) {
				mMessage_Voice_Time_TextView.setText((String) data
						.get(Subject.DATA.COMMAND_UPDATE_VOICE_TIME));
				mMessage_Voice_ImageView.switchImage((Integer) data
						.get(Subject.DATA.COMMAND_UPDATE_VOICE_VIEW));
			}
			break;
		}
		/* 语音变更音量 */
		case Subject.COMMAND.COMMAND_UPDATE_VOICE_VIEW: {
			mMessage_Voice_ImageView.switchImage((Integer) data
					.get(Subject.DATA.COMMAND_UPDATE_VOICE_VIEW));
			break;
		}
		/* 修改语音时间动画 */
		case Subject.COMMAND.COMMAND_UPDATE_VOICE_TIME: {
			if (data != null) {
				this.mMessage_Voice_Time_TextView.setVisibility(View.VISIBLE);
				String text = (String) data
						.get(Subject.DATA.COMMAND_UPDATE_VOICE_TIME);
				this.mMessage_Voice_Time_TextView.setText(text);
			}
			break;
		}
		/* 修改语音未读已读 */
		case Subject.COMMAND.COMMAND_MESSAGE_SEND_SHOW_VOICE_UNLISTEN: {
			if (this.mMessage_Voice_Unlisten_ImageView != null) {
				this.mMessage_Voice_Unlisten_ImageView.setVisibility(View.VISIBLE);
			}
			break;
		}
		/* 修改语音时间动画 */
		case Subject.COMMAND.COMMAND_MESSAGE_SEND_HIDE_VOICE_UNLISTEN: {
			if (this.mMessage_Voice_Unlisten_ImageView != null) {
				this.mMessage_Voice_Unlisten_ImageView.setVisibility(View.GONE);
			}
			break;
		}
		}
	}
	public void updateFlash(int command, Map<String, Object> data){
		switch (command) {

		/*------------------------更新flash消息---------------------------*/
		case Subject.COMMAND.COMMAND_UPDATE_FLASH_IMAGE: {
			if (data != null) {
				mMessage_Flash_ImageView.setImageBitmap((Bitmap) data
						.get(Subject.DATA.COMMAND_UPDATE_IMAGE));
			}
			break;
		}
		}
	}
	private void updateBack(int command,Map<String, Object> data){
		switch (command) {
			case Subject.COMMAND.COMMAND_UPDATE_BACKGROUND:
				int backId = (Integer)data.get(Subject.DATA.COMMAND_UPDATE_BACKGROUND);
				if(this.mMessage_Content_ViewGroup instanceof ListViewContentLayout){
					((ListViewContentLayout)this.mMessage_Content_ViewGroup).setForegroundAlpha(backId);
				}
				break;
	
			default:
				break;
		}
	}
	

	@Override
	public void update(int command, Map<String, Object> data) {
		this.updateText(command, data);
		this.updateFlash(command, data);
		this.updateImage(command, data);
		this.updateVoice(command, data);
		this.updateBack(command, data);
	}
	
	public static abstract class InitViewHolder {

		View mView = null;

		public InitViewHolder(View view) {
			mView = view;
		}

		public void initViews() {
			ViewMapUtil.getUtil().viewMapping(this, mView);
		}

		public abstract void initView(ChatItemHolder holder, int messageType);
	}

	@Override
	public void unregistorSubject() {
		if (mSubject != null) {
			mSubject.unregistorObserver(this);
		}
		this.mSubject = null;
	}

	@Override
	public void registorSubject(Subject subject) {
		this.mSubject = subject;
	}
	
	
}
