package com.renren.mobile.chat.ui.contact.feed;

import com.renren.mobile.chat.base.views.NewImageView;
import com.renren.mobile.chat.ui.contact.feed.ChatFeedAdapter.FeedPicRecycleObserver;

public class ChatFeedPicRecycleObserver implements FeedPicRecycleObserver {
	public boolean isRecycle = false;

	public ChatFeedPicRecycleObserver(NewImageView newImageView, int num) {
		this.mNewImageView = newImageView;
		this.mNum = num;
	}

	public NewImageView mNewImageView;
	public int mNum;
	private static final byte[] LOCK = new byte[0];

	@Override
	public void recycle(int min, int max) {
		if (mNum < min - 4 || mNum > max + 4) {
			synchronized (LOCK) {
				if (!this.isRecycle) {
					this.mNewImageView.recycle();
					isRecycle = true;
				}
			}
		}
	}
}
