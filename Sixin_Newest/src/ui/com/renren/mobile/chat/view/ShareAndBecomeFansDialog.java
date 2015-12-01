package com.renren.mobile.chat.view;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.common.manager.LoginManager;
import com.common.mcs.INetRequest;
import com.common.mcs.INetResponse;
import com.common.mcs.McsServiceProvider;
import com.common.utils.Methods;
import com.core.json.JsonObject;
import com.core.json.JsonValue;
import com.core.util.SystemService;
import com.renren.mobile.chat.R;
import com.renren.mobile.chat.RenrenChatApplication;
import com.renren.mobile.chat.common.ResponseError;
/**
 * @author xiangchao.fan
 * @description 分享、成为私信公共主页fans弹出窗口
 */
public class ShareAndBecomeFansDialog {
	private Context mContext;
	private AlertDialog.Builder mBuilder;
	private LayoutInflater mInflater;
	public ShareAndBecomeFansDialog(Context context){
		this.mContext = context;
		mBuilder = new Builder(context);
		mInflater = SystemService.sInflaterManager;
		initialDialog();
	}
	public void show(){
		mBuilder.create().show();
	}
	public void initialDialog(){
		LinearLayout layout = (LinearLayout) mInflater.inflate(R.layout.fxch_share_and_become_fans, null);
		final CheckBox checkBox = (CheckBox) layout.findViewById(R.id.become_fans_of_sixin);
		checkBox.setChecked(true);
		mBuilder.setTitle(RenrenChatApplication.getmContext().getResources().getString(R.string.ImageViewActivity_java_2));		//ImageViewActivity_java_2=分享到人人网; 
		mBuilder.setView(layout);
		mBuilder.setPositiveButton(RenrenChatApplication.getmContext().getResources().getString(R.string.MultiChatForwardScreen_java_6), new AlertDialog.OnClickListener() {		//MultiChatForwardScreen_java_6=确认; 
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				sendingStatus();
				if(checkBox.isChecked()){
					becomeFansOfSixin();
				}
			}
		});
		mBuilder.setNegativeButton(RenrenChatApplication.getmContext().getResources().getString(R.string.ChatMessageWarpper_FlashEmotion_java_4), new AlertDialog.OnClickListener() {		//ChatMessageWarpper_FlashEmotion_java_4=取消; 
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
	}
	private void becomeFansOfSixin(){
		final int pageId = 601169665;
		McsServiceProvider.getProvider().isFansOfPage(LoginManager.getInstance().getLoginInfo().mUserId, pageId,
				new INetResponse() {
					@Override
					public void response(INetRequest req, JsonValue obj) {
						if (obj != null && obj instanceof JsonObject) {
							final JsonObject map = (JsonObject) obj;
							if (ResponseError.noError(req, map, false)) {
								int result = (int) map.getNum("result");
								if(result == 1){
								}else{
									McsServiceProvider.getProvider().becomeFansOfPage(pageId, new INetResponse() {
										@Override
										public void response(INetRequest req, JsonValue obj) {
											final JsonObject map = (JsonObject) obj;
											if (ResponseError.noError(req, map, false)) {
												int result = (int) map.getNum("result");
												if(result == 1){
												}else{
												}
											}else{
											}
										}
									});
								}
							}else{
							}
						}else{
						}
					}
				});
	}
	private void sendingStatus() {
		McsServiceProvider.getProvider().m_statusSet( new INetResponse() {
			@Override
			public void response(INetRequest req, JsonValue obj) {
				if (obj != null && obj instanceof JsonObject) {
					final JsonObject jObj = (JsonObject) obj;
					if (ResponseError.noError(req, jObj,false)) {
						int result = (int) jObj.getNum("result");
						if (result == 1) {
							RenrenChatApplication.mHandler.post(new Runnable() {
								@Override
								public void run() {
									Toast.makeText(mContext, RenrenChatApplication.getmContext().getResources().getString(R.string.ShareAndBecomeFansDialog_java_1), 1).show();		//ShareAndBecomeFansDialog_java_1=分享成功; 
								}
							});
						} else {
						}
					}
				}
			}
		},false);
	}
}
