package com.renren.mobile.chat.views;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.PopupWindow;

import com.renren.mobile.chat.holder.ChatItemSelectHolder;
import com.renren.mobile.chat.model.warpper.ChatMessageWarpper;
/**
 * @author dingwei.chen
 * @说明  弹出选项菜单
 * */
public class ChatItemSelectPopupWindow extends PopupWindow {

	ChatItemSelectHolder mHolder = null;
	ChatMessageWarpper mMessage = null;
	View mParent = null;
	private final static int GRAVITY = Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL;
	private boolean mIsVisible = false;
	public ChatItemSelectPopupWindow(Context context,View parent,ChatItemSelectHolder holder){
		super(holder.getRootView());
		mParent = parent;
		mHolder = holder;
		this.setWidth(400);
		this.setHeight(100);
	}
	public void setMessage(ChatMessageWarpper message){
		this.mMessage = message;
	}
	public void show(){
		mIsVisible = true;
		this.showAtLocation(mParent, GRAVITY, 0, 0);
	}
	public ChatItemSelectHolder getHolder(){
		return this.mHolder;
	}
	public boolean isVisible(){
		return mIsVisible;
	}
	@Override
	public void dismiss() {
		// TODO Auto-generated method stub
		super.dismiss();
		this.mIsVisible = false;
	}
	
}
