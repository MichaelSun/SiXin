package com.renren.mobile.chat.command;

import java.util.Map;

import view.voice.VoiceView.MonitorListener;

import com.common.manager.LoginManager;
import com.common.manager.VoiceManager;
import com.core.orm.ORM;
import com.core.util.DeviceUtil;
import com.core.voice.RecordThread.OnRecordListenner;
import com.renren.mobile.chat.activity.RenRenChatActivity;
import com.renren.mobile.chat.activity.RenRenChatActivity.CanTalkable.GROUP;
import com.renren.mobile.chat.activity.ThreadPool;
import com.renren.mobile.chat.base.GlobalValue;
import com.renren.mobile.chat.base.model.ChatBaseItem;
import com.renren.mobile.chat.base.model.StateMessageModel;
import com.renren.mobile.chat.base.util.SystemUtil;
import com.renren.mobile.chat.model.warpper.ChatMessageWarpper_Voice;
import com.renren.mobile.chat.util.ChatDataHelper;
import com.renren.mobile.chat.util.ChatMessageSender;
import com.renren.mobile.chat.util.StateMessageSender;
import com.renren.mobile.chat.util.VoiceDownloadThread;


/**
 * @author dingwei.chen
 * @说明 录音命令
 * */
public class Command_Recoder extends Command implements MonitorListener{
	
	private static final long MIN_RECORD_TIME = 1500L;
	private static final long MAX_RECORD_TIME = 60000L;
	private static final long WARN_RECORD_TIME = 56000L;
	private static final long SEND_STATE_MESSAGE_TIME = 1000l;
	private static final float STEP = 360/60;
	RenRenChatActivity mChatActivity = null;
	private boolean mIsStart = false;
	private boolean mIsSendState = false;
	public boolean mIsSend = true;
	long mPreClickStartTime = 0L;
	public Command_Recoder(RenRenChatActivity chatActivity) {
		mChatActivity = chatActivity;
	}
	@Override
	public void onStartCommand() {
		mIsStart = DeviceUtil.getInstance().isSDCardHasEnoughSpace();
		if(!mIsStart){
			DeviceUtil.getInstance().toastNotEnoughSpace();
			return;
		}
		if((System.currentTimeMillis()-mPreClickStartTime)<MIN_RECORD_TIME){
			SystemUtil.toast("休息一会儿");
			mIsStart = false;
			return;
		}
		VoiceDownloadThread.getInstance().stopToAutoPlay();//不让语音自动播放
		VoiceManager.getInstance().setRecordListener(createRecordListenner());
		VoiceManager.getInstance().record(GlobalValue.getInstance()
				.createRecordFileName(mChatActivity.mToChatUser.getUId()));
		VoiceManager.getInstance().stopAllPlay();
		mChatActivity.keepScreenOn();//不锁屏
	}

	
	@Override
	public void onEndCommand(Map<String, Object> returnData) {
		if(!mIsStart){
			return;
		}
		mPreClickStartTime = System.currentTimeMillis();
		VoiceManager.getInstance().stopRecord(true);
		mChatActivity.stopKeepScreenOn();
	}
	private long mStartTime = 0L;
	private boolean mIsSuccess = true;
	public OnRecordListenner createRecordListenner(){
		OnRecordListenner listenner =  new OnRecordListenner() {
			
			@Override
			public void onEncoderEnd(String absFileName, byte[] data,boolean isEncodeSuccess) {
				if(!isEncodeSuccess || !mIsSuccess){
					SystemUtil.log("voiceupload", "upload insert-1 "+isEncodeSuccess+":"+mIsSuccess);
					mIsSend = true;
					return;
				}
				SystemUtil.log("voiceupload", "upload insert-2 ");
				long processTime = System.currentTimeMillis()-mStartTime;
				if(processTime>=MAX_RECORD_TIME){
					SystemUtil.log("voiceupload", "upload insert0 ");
					processTime = MAX_RECORD_TIME;
				}
				try {
					SystemUtil.log("voiceupload", "upload insert1 "+isCanSend()+","+(processTime > MIN_RECORD_TIME)+","+mIsSend);
					if(processTime > MIN_RECORD_TIME && isCanSend() && mIsSend){
						ChatMessageWarpper_Voice message = new ChatMessageWarpper_Voice();
						message.mComefrom  = ChatBaseItem.MESSAGE_COMEFROM.LOCAL_TO_OUT;
						message.mMessageContent = absFileName;
						message.mPlayTime = (int)((processTime)/1000);
						message.mLocalUserId = LoginManager.getInstance().getLoginInfo().mUserId;
						message.parseUserInfo(mChatActivity.mToChatUser);
						message.mMessageContent = absFileName;
						message.mIsGroupMessage = mChatActivity.mGroup.Value;
						message.mGroupId = message.mToChatUserId;
						message.mDomain = mChatActivity.mChatListAdapter.getDomain();
						SystemUtil.log("voiceupload", "upload insert2 "+message);
						ChatDataHelper.getInstance().insertToTheDatabase(message);
						ChatMessageSender.getInstance().uploadData(message, data);
						SystemUtil.log("voiceupload", "upload start3 "+data.length);
					}
				} catch (Exception e) {
					// TODO: handle exception
					SystemUtil.log("voiceupload", "upload error "+e+":"+SystemUtil.printStackElements(e.getStackTrace()));
				}
				
				mIsSend = true;
			}
			
			
			@Override
			public void onRecording(final int vsize) {
				long time = System.currentTimeMillis()-mStartTime;
				if(time>SEND_STATE_MESSAGE_TIME && !mIsSendState && mChatActivity.mGroup==GROUP.CONTACT_MODEL){
					StateMessageSender.getInstance().send(LoginManager.getInstance().getLoginInfo().mUserName, 
							LoginManager.getInstance().getLoginInfo().mUserId, 
							mChatActivity.mToChatUser.getUId(), 
							mChatActivity.mToChatUser.getDomain(),
							StateMessageModel.STATE_TYPE.SPEEKING);
					mIsSendState = true;
				}
				ThreadPool.obtain().executeMainThread(new Runnable() {
					@Override
					public void run() {
						mChatActivity.mHolder.mRoot_VoiceView.setVolumn(vsize);
					}
				});
			}
			
			@Override
			public void onRecordStart(String fileName) {
				mStartTime = System.currentTimeMillis();
				ThreadPool.obtain().executeMainThread(new Runnable() {
					@Override
					public void run() {
						mChatActivity.mHolder.mRoot_VoiceView.popView();
					}
				});
				
			}
			@Override
			public void onRecordEnd(String absFileName) {
				if(mIsSendState&& mChatActivity.mGroup==GROUP.CONTACT_MODEL){
					StateMessageSender.getInstance().send(LoginManager.getInstance().getLoginInfo().mUserName, 
							LoginManager.getInstance().getLoginInfo().mUserId, 
							mChatActivity.mToChatUser.getUId(), 
							mChatActivity.mToChatUser.getDomain(),
							StateMessageModel.STATE_TYPE.CANCELED);
					mIsSendState = false;
				}
			}
			@Override
			public boolean isCanRecord() {
				return System.currentTimeMillis()-mStartTime<=MAX_RECORD_TIME;
			}
		};
		return listenner;
	}
	
	OnRecorderSendListenner mCanSendListenner = null;
	public void setOnCanSendListenner(OnRecorderSendListenner listenner){
		mCanSendListenner = listenner;
	}
	public boolean isCanSend(){
		if(mCanSendListenner!=null){
			return mCanSendListenner.isCanSend();
		}
		return true;
	}
	public static interface OnRecorderSendListenner{
		public boolean isCanSend();
	}
	@Override
	public void onVoiceStart() {
		// TODO Auto-generated method stub
		this.onStartCommand();
		mStartTimeShowCancel = 2000;
	}
	@Override
	public int onGetTime() {
		// TODO Auto-generated method stub
		return (int)((System.currentTimeMillis()-mStartTime)/1000);
	}
	@Override
	public int onGetAngle() {
		// TODO Auto-generated method stub
		return (int)(STEP*((System.currentTimeMillis()-mStartTime)/1000.0));
	}
	@Override
	public boolean onIsLessMinTime() {
		// TODO Auto-generated method stub
		return (System.currentTimeMillis()-mStartTime)<MIN_RECORD_TIME;
	}
	/**
	 * @param isSuccess is abondon
	 * */
	@Override
	public void onVoiceEnd(boolean isSuccess) {
		mIsSuccess = isSuccess;
		this.onEndCommand(null);
		
	}
	private int mStartTimeShowCancel = 2000;
	private static final int OFFSET = 15000;
	@Override
	public boolean onIsShowMoveToCancel() {
		// TODO Auto-generated method stub
		long time = System.currentTimeMillis()-mStartTime;
		if(time<MAX_RECORD_TIME){
			if(time>mStartTimeShowCancel){
				mStartTimeShowCancel+=OFFSET;
				return true;
			}
		}
		return false;
	}
	
	
	

	
	
	
}
