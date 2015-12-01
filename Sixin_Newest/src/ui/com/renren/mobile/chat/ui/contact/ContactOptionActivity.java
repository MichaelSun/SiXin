package com.renren.mobile.chat.ui.contact;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.common.manager.LoginManager.BindInfoObserver;
import com.renren.mobile.chat.base.util.SystemUtil;
import com.renren.mobile.chat.R;
import com.renren.mobile.chat.RenrenChatApplication;
import com.renren.mobile.chat.ui.BaseActivity;
import com.renren.mobile.chat.ui.account.BindInfoActivity;
import com.renren.mobile.chat.ui.account.BindRenrenAccountActivity;
import com.renren.mobile.chat.ui.account.BindRenrenAccountScreen;
import com.renren.mobile.chat.ui.account.RegisterAccountActivity;
import com.renren.mobile.chat.ui.account.UploadContactActivity;
import com.renren.mobile.chat.view.BaseTitleLayout;

public class ContactOptionActivity  extends BaseActivity implements BindInfoObserver{
	
	BaseTitleLayout mBaseTitle;
	Context context;
	SpecialContactModel bindContact=C_ContactsData.getInstance().getBindRenRenContact();		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);  
		//RenrenChatApplication.pushStack(this);
		context = this;
		this.setContentView(R.layout.contact_add);
		BindRenrenAccountScreen.registerObserver(this);
		initView();
		initTitle();
	}
	
	private void initView() {
		
		//通讯录找人
		RelativeLayout bindContactList = (RelativeLayout)this.findViewById(R.id.contact_add_list);
		bindContactList.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				UploadContactActivity.showByContact(context);
				ContactOptionActivity.this.finish();
			}
		});
		
		//绑定手机号
	    TextView phone = (TextView)this.findViewById(R.id.contact_add_bind_phone);
		//TextView hasBinded = (TextView)this.findViewById(R.id.contact_add_binded_phone);
		//ImageView phoneImage = (ImageView)this.findViewById(R.id.contact_add_phone_right_img);
		
		if(bindContact.isBindedPhone()){
			String phoneString=RenrenChatApplication.getmContext().getResources().getString(R.string.UserInfoActivity_java_8);
			phone.setText(phoneString+" "+bindContact.getBindedPhone());
			//hasBinded.setText(RenrenChatApplication.getmContext().getResources().getString(R.string.UserInfoActivity_java_9));
			//hasBinded.setVisibility(View.VISIBLE);
			//phoneImage.setVisibility(View.GONE);
		}else{
			phone.setText(RenrenChatApplication.getmContext().getResources().getString(R.string.contact_add_bind_phone));
			//hasBinded.setVisibility(View.GONE);
			//phoneImage.setVisibility(View.VISIBLE);
		}
		RelativeLayout bindphone = (RelativeLayout)this.findViewById(R.id.contact_add_phone_layout);
		bindphone.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (bindContact.isBindedPhone()) {
					BindInfoActivity.show(context, bindContact.getBindedPhone(), RegisterAccountActivity.FLAG_BIND_PHONE, true);
					ContactOptionActivity.this.finish();
				} else {
					RegisterAccountActivity.show(context, RegisterAccountActivity.FLAG_BIND_PHONE, true);
					ContactOptionActivity.this.finish();
				}
			}
		});
		
		
		//人人
		TextView renRenName = (TextView)this.findViewById(R.id.contact_add_bind_renren);
		//TextView bindedRenRen = (TextView)this.findViewById(R.id.contact_add_binded_renren);
		//ImageView renrenRightImg = (ImageView)this.findViewById(R.id.contact_add_renren_right_img);
		
		if(bindContact.isBindedRenRen()){
			renRenName.setText(bindContact.getBindedRenRenName());
			//bindedRenRen.setText(RenrenChatApplication.getmContext().getResources().getString(R.string.UserInfoActivity_java_9));
			//bindedRenRen.setVisibility(View.VISIBLE);
			//renrenRightImg.setVisibility(View.GONE);
		}else{
			renRenName.setText(RenrenChatApplication.getmContext().getResources().getString(R.string.contact_add_bind_renren));
		//	bindedRenRen.setVisibility(View.GONE);
		//	renrenRightImg.setVisibility(View.VISIBLE);
		}
		
		RelativeLayout bindRenRenLayout = (RelativeLayout)this.findViewById(R.id.contact_add_renren_layout);
		bindRenRenLayout.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent i;
				if(bindContact.isBindedRenRen()){
				   i = new Intent(context, ThirdContactsActivity.class);
				}else{
					i = new Intent(context, BindRenrenAccountActivity.class);
					i.putExtra(BindRenrenAccountActivity.COME_FROM, BindRenrenAccountActivity.BIND_CONTANT);
				}
				context.startActivity(i);
			}
		});
	}
	
	private void initTitle() {
		FrameLayout mLinearLayout = (FrameLayout) this.findViewById(R.id.title_layout);
		mBaseTitle = new BaseTitleLayout(ContactOptionActivity.this,mLinearLayout);
		TextView mTextView = mBaseTitle.getTitleMiddle();
		mBaseTitle.getTitleLeft().setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		mTextView.setText(RenrenChatApplication.getmContext().getResources().getString(R.string.contact_add_title));
	}

	@Override
	public void update() {
		if(com.renren.mobile.chat.base.util.SystemUtil.mDebug){
			SystemUtil.logd("绑定发生变化了");
		}
		initView();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		BindRenrenAccountScreen.deleteObserver(this);
		super.onDestroy();
	}
	
	public static void show(Context context) {
		Intent intent =new Intent(context, ContactOptionActivity.class);
    	context.startActivity(intent);
	}
}
