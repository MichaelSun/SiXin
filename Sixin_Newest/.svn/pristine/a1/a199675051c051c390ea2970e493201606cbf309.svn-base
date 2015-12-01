package com.renren.mobile.chat.util;

import android.view.View;
import android.widget.ProgressBar;

import com.common.emotion.emotion.ScrollingLock;
import com.renren.mobile.chat.activity.RenRenChatActivity;
import com.renren.mobile.chat.model.warpper.ChatMessageWarpper;

/**
 * @author dingwei.chen
 * @说明 聊天主界面绘制管理器
 * 和{@link com.renren.mobile.chat.activity.RenRenChatActivity}有高度的耦合度
 * */
public final class ChatPaintThread extends Thread {

	public static final int PAINT_SLEEP_TIME = 200;//绘制时间中断时间
	private RenRenChatActivity mActivity = null;
	private boolean mIsStop = false;
	public ChatPaintThread(RenRenChatActivity activity){
		mActivity = activity;
		View view = null;
//		AnimatedRotateDrawable drawable;
		ProgressBar bar;
	}
	
	public void stopPaint(){
		mIsStop = true;
	}
	public void run(){
		while(!mIsStop){
			if(mActivity.mHolder!=null){
				int index = mActivity.mHolder.mRoot_ChatList.getFirstVisiblePosition();
				if(index!=0){
					index--;
				}
				int count = mActivity.mHolder.mRoot_ChatList.getListItemCount();
				for(int i = index;i<=index+count && i<mActivity.mChatListAdapter.getCount();i++){
					ChatMessageWarpper message = ((ChatMessageWarpper)mActivity.mChatListAdapter.getItem(i));
					if(message.isReflash()){
						mActivity.mHandler.removeCallbacks(mUpdateListViewMessage);
						mActivity.mHandler.post(mUpdateListViewMessage);
					}
				}
			}
			if(ScrollingLock.isScrolling){
				synchronized (ScrollingLock.lock) {
					try{
						ScrollingLock.lock.wait();
					}catch (InterruptedException e) {
						// TODO: handle exception
					}
				}
			}
			try {
				Thread.sleep(PAINT_SLEEP_TIME);
			} catch (InterruptedException e) {}
		}
	}
	
	public Runnable mUpdateListViewMessage = new Runnable() {
		public void run() {
			if(mActivity.mHolder!=null){
				int index = mActivity.mHolder.mRoot_ChatList.getFirstVisiblePosition();
				if(index!=0){
					index--;
				}
				int count = mActivity.mHolder.mRoot_ChatList.getListItemCount();
				for(int i = index;i<=index+count && i<mActivity.mChatListAdapter.getCount();i++){
					
					ChatMessageWarpper message = ((ChatMessageWarpper)mActivity.mChatListAdapter.getItem(i));
					if(message.isReflash()){
						message.reflash();
					}
				}
			}
		}
	};
	
	
}
