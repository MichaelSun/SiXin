package com.renren.mobile.chat.view;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;

/**
 * Action item, displayed as menu with icon and text.
 * @author yisong.li@renren-inc.com
 * @date 2.12.2011
 * Fuction : Action item, displayed as menu with icon and text.
 * 
 *
 */
public class ActionItem {
	private int icon;
	private String title;
	private int titleColor;
	private boolean isShadow = false;
	private OnClickListener listener;
	private OnTouchListener onTouchListener;
	
	/**
	 * Constructor
	 */
	public ActionItem() {}
	
	/**
	 * Constructor
	 * 
	 * @param icon {@link Drawable} action icon
	 */
	public ActionItem(int icon) {
		this.icon = icon;
	}
	
	/**
	 * Set action title
	 * 
	 * @param title action title
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	
	/**
	 * Set action title
	 * 
	 * @param title action title
	 */
	public void setTitle(String title, int titleColor, boolean isShadow) {
		this.title = title;
		this.titleColor = titleColor;
		this.isShadow = isShadow;
	}
	
	/**
	 * Set action title
	 * 
	 * @param title action title
	 */
	public void setTitle(int titleColor, boolean isShadow) {
		this.titleColor = titleColor;
		this.isShadow = isShadow;
	}
	
	/**
	 * Set action title
	 * 
	 * @param title action title
	 */
	public void setTitle(String title, boolean isShadow) {
		this.title = title;
		this.isShadow = isShadow;
	}
	
	/**
	 * Get action title
	 * 
	 * @return action title
	 */
	public String getTitle() {
		return this.title;
	}
	
	/**
	 * get titleColor
	 * @return
	 */
	public int getTitleColor() {
		return titleColor;
	}
	
	/**
	 * set titleColor
	 * @param titleColor
	 */
	public void setTitleColor(int titleColor) {
		this.titleColor = titleColor;
	}

	/**
	 * whether shadow
	 * @return
	 */
	public boolean isShadow() {
		return isShadow;
	}

	/**
	 * whether shadow
	 * @param isShadow
	 */
	public void setShadow(boolean isShadow) {
		this.isShadow = isShadow;
	}
	
	/**
	 * Set action icon
	 * 
	 * @param icon {@link Drawable} action icon
	 */
	public void setIcon(int icon) {
		this.icon = icon;
	}
	
	/**
	 * Get action icon
	 * @return  {@link Drawable} action icon
	 */
	public int getIcon() {
		return this.icon;
	}
	
	/**
	 * Set on click listener
	 * 
	 * @param listener on click listener {@link View.OnClickListener}
	 */
	public void setOnClickListener(OnClickListener listener) {
		this.listener = listener;
	}
	
	/**
	 * Get on click listener
	 * 
	 * @return on click listener {@link View.OnClickListener}
	 */
	public OnClickListener getListener() {
		return this.listener;
	}
	
	/**
	 * add by xiangchao.fan
	 * @param listener
	 */
	public void setOnTouchListener(OnTouchListener listener){
		this.onTouchListener = listener;
	}

	public OnTouchListener getOnTouchListener() {
		return onTouchListener;
	}
}