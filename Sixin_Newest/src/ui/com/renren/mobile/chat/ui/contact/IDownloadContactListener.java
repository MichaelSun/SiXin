package com.renren.mobile.chat.ui.contact;
/**
 * 下载联系人信息回调
 * **/
public interface IDownloadContactListener {
	public void onSussess(ContactBaseModel model);
	public void onError();
	public void onDowloadOver();
}
