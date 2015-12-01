package com.renren.mobile.chat.view;

import java.util.HashMap;

import com.renren.mobile.chat.R;

public class BaseTitleStateFactory {
	public static interface BaseTitleStateType {
//		 /**
//	     * 私信在线
//	     */
//	    public static final int STATUE_SIXIN_ONLINE = 3;
//	    /**
//	     * 手机客户端在线
//	     */
//	    public static final int STATUE_MOBILE_ONLINE = 2;
//	    /**
//	     * 桌面或网页在线
//	     */
//	    public static final int STATUE_DESKORWEB_ONLINE = 1;
//	    /**
//	     * 离线
//	     */
//	    public static final int STATUE_OFFLINE = 0;
		int TYPE_SIXIN_ONLINE = 3;
		int TYPE_PHONE_ONLINE = 2;
		int TYPE_PC_ONLINE = 1;
		int TYPE_OFFLINE = 0;
		int TYPE_AUDIO_STATE = 5;
		int TYPE_TYPING_STATE = 6;
		int TYPE_UNKOWN= -2;
		int STATUE_IPHONE_ONLINE = 4;
	}
	public static BaseTitleState mOfflineState = new BaseTitleState(BaseTitleStateType.TYPE_OFFLINE,0, "离线");
	public static BaseTitleState mAudioState = new BaseTitleState(BaseTitleStateType.TYPE_AUDIO_STATE,0, "对方正在说话...");
	public static BaseTitleState mTypingState = new BaseTitleState(BaseTitleStateType.TYPE_TYPING_STATE,0, "正在输入...");
	public static BaseTitleState mUnkownState = new BaseTitleState(BaseTitleStateType.TYPE_UNKOWN,0, "");
	public static HashMap<Integer, BaseTitleState> sType2TitleState = new HashMap<Integer, BaseTitleState>();
	static{
		sType2TitleState.put(BaseTitleStateType.TYPE_OFFLINE, mOfflineState);
		sType2TitleState.put(BaseTitleStateType.TYPE_AUDIO_STATE, mAudioState);
		sType2TitleState.put(BaseTitleStateType.TYPE_TYPING_STATE, mTypingState);
		sType2TitleState.put(BaseTitleStateType.TYPE_UNKOWN, mUnkownState);
	}

	public static BaseTitleState getBaseTitleState(int type) {
		return sType2TitleState.get(type);
	}

}
