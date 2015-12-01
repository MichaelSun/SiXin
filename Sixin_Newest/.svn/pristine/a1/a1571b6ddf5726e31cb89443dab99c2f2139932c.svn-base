package com.renren.mobile.chat.ui.setting;

import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;

import com.common.mcs.INetRequest;
import com.common.mcs.INetResponse;
import com.common.mcs.McsServiceProvider;
import com.common.utils.LanguageSettingUtil;
import com.common.utils.LanguageSettingUtil.LanguageType;
import com.core.json.JsonObject;
import com.core.json.JsonValue;
import com.core.util.SystemService;
import com.core.util.ViewMapUtil;
import com.core.util.ViewMapping;
import com.renren.mobile.account.LoginControlCenter;
import com.renren.mobile.chat.R;
import com.renren.mobile.chat.RenrenChatApplication;
import com.renren.mobile.chat.base.util.SystemUtil;
import com.renren.mobile.chat.common.RRSharedPreferences;
import com.renren.mobile.chat.common.ResponseError;
import com.renren.mobile.chat.ui.BaseScreen;
import com.renren.mobile.chat.view.GeneralSettingLayout;

/**
 * @author xiangchao.fan
 * @description language setting page screen
 */
public class LanguageSettingScreen extends BaseScreen {
	public class ViewHolder{
		@ViewMapping(ID=R.id.simple_chinese)
		public GeneralSettingLayout mSimpleChinese;
		
		@ViewMapping(ID=R.id.complex_chinese)
		public GeneralSettingLayout mComplexChinese;
		
		@ViewMapping(ID=R.id.english)
		public GeneralSettingLayout mEnglish;
	}
	
	private View mLanguageSettingView;
	private ViewHolder mHolder;
	private Context context;
	
	private Dialog mDialog;
	
	private int mSelectLanguageType;
	
	public LanguageSettingScreen(Activity activity) {
		super(activity);
		context = activity;
		mLanguageSettingView = SystemService.sInflaterManager.inflate(R.layout.f_language_setting, null);
		setContent(mLanguageSettingView);
		
		initTitle();
		initView();
		initEvent();
	}
	
	private void initTitle() {
		this.getTitle().setTitleMiddle(mActivity.getResources().getString(R.string.LanguageSettingScreen_1));
		this.getTitle().getTitleLeft().setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
	
	private void initView(){
		mHolder = new ViewHolder();
		ViewMapUtil.getUtil().viewMapping(mHolder, mLanguageSettingView);
		
		mDialog = createConfirmDialog();
		
		// SimpleChinese setting
		mHolder.mSimpleChinese.mHolder.mSettingName.setText(mActivity.getResources().getString(R.string.LanguageSettingScreen_2));
		mHolder.mSimpleChinese.mHolder.mSettingDescription.setVisibility(View.GONE);
		mHolder.mSimpleChinese.mHolder.mNextPageIcon.setVisibility(View.GONE);
		
		// ComplexChinese setting
		mHolder.mComplexChinese.mHolder.mSettingName.setText(mActivity.getResources().getString(R.string.LanguageSettingScreen_3));
		mHolder.mComplexChinese.mHolder.mSettingDescription.setVisibility(View.GONE);
		mHolder.mComplexChinese.mHolder.mNextPageIcon.setVisibility(View.GONE);
		
		// English setting
		mHolder.mEnglish.mHolder.mSettingName.setText(mActivity.getResources().getString(R.string.LanguageSettingScreen_4));
		mHolder.mEnglish.mHolder.mSettingDescription.setVisibility(View.GONE);
		mHolder.mEnglish.mHolder.mNextPageIcon.setVisibility(View.GONE);
		
		initLanguageType(LanguageSettingUtil.getInstance().getLanguageType());
	}
	
	private void initEvent(){
		// SimpleChinese setting
		mHolder.mSimpleChinese.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mSelectLanguageType = LanguageType.SIMPLE_CHINESE;
				mDialog.show();
			}
		});
		
		// ComplexChinese setting
		mHolder.mComplexChinese.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mSelectLanguageType = LanguageType.COMPLEX_CHINESE;
				mDialog.show();
			}
		});
		
		// English setting
		mHolder.mEnglish.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mSelectLanguageType = LanguageType.ENGLISH;
				mDialog.show();
			}
		});
	}
	
	private void languageSwitch(Locale locale) {
		Resources resources = context.getResources();//获得res资源对象
		Configuration config = resources.getConfiguration();//获得设置对象
		DisplayMetrics dm = resources .getDisplayMetrics();//获得屏幕参数：主要是分辨率，像素等。
		config.locale = locale; 
		resources.updateConfiguration(config, dm);
		Intent i = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());  
		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);  
		context.startActivity(i); 
		RenrenChatApplication.clearStack();//清空历史用户栈
		LoginControlCenter.getInstance().pageJump(context, new RRSharedPreferences(context));
		
		SystemUtil.f_log("language===========================");
		
		SettingForwardActivity.show(context, SettingForwardActivity.ForwardScreenType.SYSTEM_SETTING_SCREEN);
		SystemSettingForwardActivity.show(context, SystemSettingForwardActivity.SystemForwardScreenType.LANGUAGE_SETTING_SCREEN);
		SystemUtil.f_log("language===========================");
	}
	
	private void initLanguageType(int languageType){
		switch(languageType){
		case LanguageType.DEFAULT_VALUE:
		case LanguageType.SIMPLE_CHINESE:
			mHolder.mSimpleChinese.mHolder.mSettingSelect.setVisibility(View.VISIBLE);
			mHolder.mComplexChinese.mHolder.mSettingSelect.setVisibility(View.GONE);
			mHolder.mEnglish.mHolder.mSettingSelect.setVisibility(View.GONE);
			break;
		case LanguageType.COMPLEX_CHINESE:
			mHolder.mSimpleChinese.mHolder.mSettingSelect.setVisibility(View.GONE);
			mHolder.mComplexChinese.mHolder.mSettingSelect.setVisibility(View.VISIBLE);
			mHolder.mEnglish.mHolder.mSettingSelect.setVisibility(View.GONE);
			break;
		case LanguageType.ENGLISH:
			mHolder.mSimpleChinese.mHolder.mSettingSelect.setVisibility(View.GONE);
			mHolder.mComplexChinese.mHolder.mSettingSelect.setVisibility(View.GONE);
			mHolder.mEnglish.mHolder.mSettingSelect.setVisibility(View.VISIBLE);
			break;
		}
	}
	
	private Dialog createConfirmDialog(){
		AlertDialog.Builder buider = new AlertDialog.Builder(mActivity);
        buider.setItems(R.array.confirm_dialog, new DialogInterface.OnClickListener() {

        	@Override
			public void onClick(DialogInterface dialog, int which) {
        		switch(which){
        		case 0:
        			updateLanguage();
        			break;
        		case 1:
        			break;
        		}
        	}
		});
        return buider.create();
		
	}
	
	/**
	 * 变更语言
	 */
	public void updateLanguage(){
		McsServiceProvider.getProvider().uploadLanguage(new INetResponse() {
			
			@Override
			public void response(final INetRequest req, JsonValue obj) {
				if (obj != null && obj instanceof JsonObject) {
					final JsonObject map = (JsonObject) obj;
					SystemUtil.f_log("map:" + map.toJsonString());
					RenrenChatApplication.mHandler.post(new Runnable() {
						@Override
						public void run() {
							if (ResponseError.noError(req, map,false)) {
								int result = (int) map.getNum("result");
								if(result == 1){
									SystemUtil.toast(mActivity.getResources().getString(R.string.LanguageSettingScreen_6));
									initLanguageType(mSelectLanguageType);
									LanguageSettingUtil.getInstance().setLanguageType(mSelectLanguageType);
									
									
									switch (mSelectLanguageType) {
									case LanguageType.SIMPLE_CHINESE:
										languageSwitch(Locale.CHINESE);
										break;
									case LanguageType.COMPLEX_CHINESE:
										languageSwitch(Locale.TAIWAN);
										break;
									case LanguageType.ENGLISH:
										languageSwitch(Locale.ENGLISH);
										break;
									}
								}
							}
						}
					});
				}
			}
		}, LanguageSettingUtil.getInstance().getLanguageStr(mSelectLanguageType));
		
	}

}
