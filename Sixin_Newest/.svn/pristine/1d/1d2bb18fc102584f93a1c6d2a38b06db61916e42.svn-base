package com.renren.mobile.chat.ui.contact;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.common.manager.LoginManager;
import com.common.mcs.INetRequest;
import com.common.mcs.INetResponse;
import com.common.mcs.McsServiceProvider;
import com.core.json.JsonObject;
import com.core.json.JsonValue;
import com.core.util.SystemService;
import com.renren.mobile.chat.R;
import com.renren.mobile.chat.RenrenChatApplication;
import com.renren.mobile.chat.activity.RenRenChatActivity;
import com.renren.mobile.chat.base.util.SystemUtil;
import com.renren.mobile.chat.base.views.NotSynImageView;
import com.renren.mobile.chat.base.views.NotSynImageView.DownloadImageAbleListener;
import com.renren.mobile.chat.common.ResponseError;
import com.renren.mobile.chat.ui.contact.C_ContactsData.NewContactsDataObserver;

public class SearchContactAdapter extends BaseAdapter implements DownloadImageAbleListener{
	
	private List<ContactModel> searchResults = new ArrayList<ContactModel>();
	private boolean isScrolling = false; 
	Activity context;
	
	private ProgressDialog addingDialog;   //正在添加的提示
	
	class ViewHolder {
		LinearLayout itemLayout;
		NotSynImageView headImage;
		TextView userName;
		ImageView OperationBtn;
	}
	
	public SearchContactAdapter(Activity con){
		context=con;
		addingDialog = new ProgressDialog(context);
		addingDialog.setMessage(con.getResources().getText((R.string.contact_adding)));
	}

	@Override
	public int getCount() {
		return searchResults.size();
	}

	@Override
	public ContactModel getItem(int position) {
		return searchResults.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ContactModel model = searchResults.get(position);
		ViewHolder viewHolder = null;
		if(convertView == null) {
			viewHolder = new ViewHolder();
			convertView = SystemService.sInflaterManager.inflate(R.layout.search_contact_listview_item, null);
			viewHolder.itemLayout = (LinearLayout) convertView.findViewById(R.id.search_contact_item_layout);
			viewHolder.headImage = (NotSynImageView) convertView.findViewById(R.id.search_contact_item_head);
			viewHolder.userName = (TextView) convertView.findViewById(R.id.search_contact_item_name);
			viewHolder.OperationBtn = (ImageView) convertView.findViewById(R.id.search_contact_item_operation);
			convertView.setTag(viewHolder);
			viewHolder.headImage.setDownloadAbleListener(this);
		}
		viewHolder = (ViewHolder) convertView.getTag();
		if(viewHolder != null) {
			wrap(viewHolder, model);
		}
		return convertView;
	}
	
	private void wrap(ViewHolder holder, ContactModel model) {
		holder.userName.setText(model.getmContactName());
		int relation = model.getmRelation();
		if(SystemUtil.mDebug){
			SystemUtil.logd("搜索Relationship= "+relation);
		}
		if((relation&ContactBaseModel.Relationship.IS_CONTACT) == ContactBaseModel.Relationship.IS_CONTACT){
			holder.OperationBtn.setBackgroundResource(R.drawable.contact_renren_sixin);
		}else{
			holder.OperationBtn.setBackgroundResource(R.drawable.add_contact_add_icon);
		}		
		setItemHeadImg(holder, model);
		setItemOnclick(holder, model);
	}
	
	/**
	 * 设置item的头像
	 * */
	private void setItemHeadImg(ViewHolder viewHolder, ContactModel contactModel) {
		viewHolder.headImage.clear();
		viewHolder.headImage.setBackgroundColor(NotSynImageView.GRAY);
		contactModel.setmHeadUrl(viewHolder.headImage);				
	}
	
	private void setItemOnclick(ViewHolder holder, final ContactModel contactModel) {
		holder.OperationBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(SystemUtil.mDebug){
					SystemUtil.logd("-------------------------------------");
				}
				if((contactModel.getmRelation()&ContactBaseModel.Relationship.IS_CONTACT) == ContactBaseModel.Relationship.IS_CONTACT) {
					RenRenChatActivity.show(context, contactModel);
				}else {
					makeFriendRequest(contactModel);
				}
			}
		});
		
		holder.itemLayout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				UserInfoActivity.show(context, contactModel.mUserId,contactModel.getDomain());
			}
		});
	}
	
	public void setScrollState(boolean scrollState){
		isScrolling = scrollState;
		notifyDataSetChanged();
	}

	@Override
	public boolean enable() {
		return !isScrolling;
	}

	public void notifyData(List<ContactModel> list) {
		if(SystemUtil.mDebug){
			SystemUtil.logd("notifydata == "+list.size());
		}
		searchResults = list;
		this.notifyDataSetChanged();
	}
	
//	private void makeFriendRequest(final ContactModel model) {
//		addingDialog.show();
//		String uid = String.valueOf(model.mUserId);
//		INetResponse response = new INetResponse() {
//			@Override
//			public void response(final INetRequest req, final JsonValue obj) {
//				RenrenChatApplication.mHandler.post(new Runnable() {
//					@Override
//					public void run() {
//						addingDialog.dismiss();
//						JsonObject map = (JsonObject) obj;
//						if (ResponseError.noError(req, map)) {
//							int result = (int) map.getNum("result");
//							if (result == 1) {
//								 if(SystemUtil.mDebug){
//					                	SystemUtil.logd("添加成功");
//									}
//								SystemUtil.toast("添加成功");
//								makeFriendSuccess(model);
//							} else {
//								// SystemUtil.toast("添加失败"+result);
//								int error_code = (int) map.getNum("error_code");
//								String error_msg = map.getString("error_msg");
//								if(SystemUtil.mDebug){
//				                	SystemUtil.logd("添加失败 error_code = "+error_code+"#msg="+error_msg);
//								}
//								SystemUtil.toast("添加失败 error_code = "
//										+ error_code);
//							}
//						} 
//					}
//				});
//			}
//		};
//		McsServiceProvider.getProvider().addContact(response, uid, 0,"");
//
//	}
	
	private void makeFriendRequest(final ContactModel model) {
		addingDialog.show();
		INetResponse response = new INetResponse() {
			@Override
			public void response(final INetRequest req, final JsonValue obj) {
                if(SystemUtil.mDebug){
                	SystemUtil.logd("添加好友="+obj.toJsonString());
				}
                RenrenChatApplication.mHandler.post(new Runnable() {
					@Override
					public void run() {
						addingDialog.dismiss();
						JsonObject map = (JsonObject) obj;
						if (ResponseError.checkError(map)) {
							int result = (int) map.getNum("result");
							if(result == 1){
				                if(SystemUtil.mDebug){
				                	SystemUtil.logd("添加成功");
								}
								SystemUtil.toast(R.string.contact_added);
								notifyDataSetChanged();
								makeFriendSuccess(model);
							}
						}else{ //出错
							int error_code = (int) map.getNum("error_code");
							String error_msg = map.getString("error_msg");
							if(SystemUtil.mDebug){
				                	SystemUtil.logd("添加失败 error_code = "+error_code+"#msg="+error_msg);
							}
							if(error_code == UserInfoActivity.ERROR_CODE_VERIFY){//对方验证开  填写验证信息
								//LayoutInflater inflater = context.getLayoutInflater();
								LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
								View layout = inflater.inflate(R.layout.contact_add_verify_dialog,(ViewGroup) context.findViewById(R.id.contact_add_dialog));
								final EditText et = (EditText)layout.findViewById(R.id.contact_verify_text);
								String str= context.getText(R.string.contact_verify_string).toString();
								String nameString = LoginManager.getInstance().mLoginInfo.mUserName;
								if(!TextUtils.isEmpty(nameString)){
									str = str.replace("XXX",nameString);
									et.setText(str);
								}else{
									et.setText("");
								}

								Builder dialog =new AlertDialog.Builder(context).setView(layout);
								dialog.setNegativeButton(context.getText(R.string.selectphoto_preview_layout_2), new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog, int which) {
										dialog.cancel(); 
									}
								});
								dialog.setPositiveButton(context.getString(R.string.takephoto_preview_layout_1), new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog, int which) {
										if(TextUtils.isEmpty(et.getText().toString())){
											SystemUtil.toast(context.getText(R.string.contact_verify_notnull).toString());
										}else{
											SendVerifyMessage(model.getmUserId(),et.getText().toString());	
										}
									}
								});
								dialog.setCancelable(false);
								dialog.show();
							}else{
								SystemUtil.toast(error_msg);
							}
			               
							
						}
					}
				});
			}

		};
		//SystemUtil.toast("添加成功");
		//makeFriendSuccess();
		//McsServiceProvider.getProvider().addContact(response, String.valueOf(uid), 0,"");
		McsServiceProvider.getProvider().addContact(response, String.valueOf(model.mUserId), 0,"");
	}
	
	
//	/**
//	 * 加为好友成功之后改变界面布局及文案
//	 * */
//	private void makeFriendSuccess(ContactModel model) {
//		Log.v("wyy6", "makeFriendSuccess mRelationshipType = "+model.mRelationshipType);
//		model.mRelationshipType = model.mRelationshipType|RELATIONSHIP_TYPE.IS_FRIEND;   //进行或操作，表示成为好友
//		Log.v("wyy6", " mRelationshipType = "+model.mRelationshipType);
//		this.notifyDataSetChanged();
//		C_ContactsData.getInstance().addContact(model);
//	}
	
	private void makeFriendSuccess(ContactModel model) {
		model.mRelation = model.mRelation|ContactBaseModel.Relationship.IS_CONTACT ;
		C_ContactsData.getInstance().addContact(model);
		//C_ContactsData.getInstance().refreshAllContacts(null);
		C_ContactsData.getInstance().notifyNewObserver(NewContactsDataObserver.DATA_STATE_OK,NewContactsDataObserver.TYPE_FIND);
	}
	
	private void SendVerifyMessage(final Long uid,String message) {
		INetResponse response = new INetResponse() {
			@Override
			public void response(final INetRequest req, final JsonValue obj) {
                if(SystemUtil.mDebug){
                	SystemUtil.logd("验证信息="+obj.toJsonString());
				}
                context.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						JsonObject map = (JsonObject) obj;
						if (ResponseError.checkError(map)) {
							int result = (int) map.getNum("result");
							if(result == 1){
				                if(SystemUtil.mDebug){
				                	SystemUtil.logd("发送验证成功");
								}
				                SystemUtil.toast(R.string.contact_send_ok);
							}
						}else{ //出错
							int error_code = (int) map.getNum("error_code");
							String error_msg = map.getString("error_msg");
							if(SystemUtil.mDebug){
				                	SystemUtil.logd("添加失败 error_code = "+error_code+"#msg="+error_msg);
							}
							SystemUtil.toast(error_msg);
						}
					}
				});
			}

		};
		//SystemUtil.toast("添加成功");
		//makeFriendSuccess();
		McsServiceProvider.getProvider().addContact(response, String.valueOf(uid), 1,message);
	}
	
}
