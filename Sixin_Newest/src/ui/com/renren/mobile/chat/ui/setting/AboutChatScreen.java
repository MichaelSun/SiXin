package com.renren.mobile.chat.ui.setting;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import com.renren.mobile.chat.R;
import com.renren.mobile.chat.ui.BaseScreen;
import com.renren.mobile.chat.view.BaseTitleLayout;
import com.renren.mobile.chat.RenrenChatApplication;
public class AboutChatScreen extends BaseScreen {
	private LayoutInflater inflater;
	private View myView;
	public AboutChatScreen(Activity activity) {
		super(activity);
		inflater = LayoutInflater.from(this.mActivity);
		myView = inflater.inflate(R.layout.about_chat,null);
		this.setContent(myView);
		initTitle();
	}
	private void initTitle() {
		this.getTitle().setTitleMiddle(RenrenChatApplication.getmContext().getResources().getString(R.string.AboutChatScreen_java_1));		//AboutChatScreen_java_1=关于私信; 
		BaseTitleLayout title = getTitle();
		title.setTitleButtonLeftBackListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
}
