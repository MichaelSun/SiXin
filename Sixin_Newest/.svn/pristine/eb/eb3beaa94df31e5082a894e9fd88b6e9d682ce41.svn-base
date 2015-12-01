package com.renren.mobile.chat.ui.setting;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import com.common.manager.LoginManager;
import com.common.manager.LoginManager.LoginInfo;
import com.core.util.SystemService;
import com.core.util.ViewMapUtil;
import com.core.util.ViewMapping;
import com.renren.mobile.chat.R;
import com.renren.mobile.chat.RenrenChatApplication;
import com.renren.mobile.chat.base.util.SystemUtil;
import com.renren.mobile.chat.base.views.NotSynImageView;
import com.renren.mobile.chat.ui.BaseScreen;
import com.renren.mobile.chat.ui.account.BindInfoActivity;
import com.renren.mobile.chat.ui.account.RegisterAccountActivity;
import com.renren.mobile.chat.ui.imageviewer.ImageViewActivity;
import com.renren.mobile.chat.ui.setting.SettingDataManager.CollegeSelectListener;
import com.renren.mobile.chat.ui.setting.SettingDataManager.PhotoUploadSuccessListener;
import com.renren.mobile.chat.view.GeneralSettingLayout;

/**
 * @author xiangchao.fan
 * @description self info setting page screen
 */
public class SelfInfoSettingScreen extends BaseScreen implements CollegeSelectListener, PhotoUploadSuccessListener{

	public SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); 
	
	public static final String DEFAULT_BIRTHDAY = "0-0-0";
	
	public static interface SexType{
		public static final int FEMALE = 0;
		public static final int MALE = 1;
	}
	
	public class ViewHolder {
		@ViewMapping(ID = R.id.self_head)
		public NotSynImageView mSelfHead;

		@ViewMapping(ID = R.id.self_name)
		public TextView mSelfName;

		@ViewMapping(ID = R.id.iv_self_sex)
		public ImageView mSelfSexIcon;
		
		@ViewMapping(ID = R.id.self_sixin_id)
		public TextView mSelfSixinID;

		@ViewMapping(ID = R.id.bind_mobile_num)
		public GeneralSettingLayout mBindMobileNum;

		@ViewMapping(ID = R.id.bind_email)
		public GeneralSettingLayout mBindEmail;

		@ViewMapping(ID = R.id.change_pwd)
		public GeneralSettingLayout mChangePwd;

		@ViewMapping(ID = R.id.self_sex)
		public GeneralSettingLayout mSelfSex;

		@ViewMapping(ID = R.id.self_birthday)
		public GeneralSettingLayout mSelfBirthday;

		@ViewMapping(ID = R.id.self_college)
		public GeneralSettingLayout mSelfCollege;
	}
	
	private Dialog mSexDialog;
	private Dialog mBirthdayDialog;
	
	/** SelfInfoSetting */
	private View mSelfInfoSettingView;

	private ViewHolder mHolder;
	private LoginInfo mLoginInfo;
	
	private String mSex;
	/** 1:male, 2:female */
	private int mSexNum;
	
	private String mBirthday;
	
	private String mCollege;

	private ColorStateList mColorList = new ColorStateList(
			new int[][] {
					{android.R.attr.state_selected}, {android.R.attr.state_pressed}, {android.R.attr.state_focused}, {} }, 
			new int[] 
					{ Color.WHITE, Color.WHITE, Color.WHITE, mActivity.getResources().getColor(R.color.Color_B)});

	public SelfInfoSettingScreen(Activity activity) {
		super(activity);

		mSelfInfoSettingView = SystemService.sInflaterManager.inflate(
				R.layout.f_self_info_setting, null);
		setContent(mSelfInfoSettingView);

		// kaining.yang
		mLoginInfo = LoginManager.getInstance().getLoginInfo();
		SettingDataManager.getInstance().registerCollegeSelectListener(this);
		SettingDataManager.getInstance().registerPhotoUploadSuccessListener(this);
				
		initTitle();
		initView();
		initEvent();

	}

	private void initTitle() {
		this.getTitle().setTitleMiddle(mActivity.getResources().getString(R.string.SelfInfoSettingScreen_1));
		this.getTitle().getTitleLeft()
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						finish();
					}
				});
	}

	private void initView() {
		
		mHolder = new ViewHolder();
		ViewMapUtil.getUtil().viewMapping(mHolder, mSelfInfoSettingView);

		mSexDialog = createSexDialog();

		// self head
		mHolder.mSelfHead.clear();
		mHolder.mSelfHead.addUrl(mLoginInfo.mMediumUrl);

		// self name
		if (!TextUtils.isEmpty(mLoginInfo.mUserName)) {
			mHolder.mSelfName.setText(mLoginInfo.mUserName);
		}

		// self sixin ID
		mHolder.mSelfSixinID.setText(mActivity.getResources().getString(R.string.SelfInfoSettingScreen_2) + " " + mLoginInfo.mUserId);

		// bind mobile 绑定手机
		mHolder.mBindMobileNum.mHolder.mSettingName.setText(mActivity.getResources().getString(R.string.SelfInfoSettingScreen_3));
		mHolder.mBindMobileNum.mHolder.mSettingDescription
				.setText(mActivity.getResources().getString(R.string.SelfInfoSettingScreen_4));
		if (mLoginInfo.mBindInfoMobile != null && !TextUtils.isEmpty(mLoginInfo.mBindInfoMobile.mBindId)) {
			mHolder.mBindMobileNum.mHolder.mContent.setVisibility(View.VISIBLE);
			mHolder.mBindMobileNum.mHolder.mContent.setText(mLoginInfo.mBindInfoMobile.mBindId);
		}

		// bind email 绑定邮箱
		mHolder.mBindEmail.mHolder.mSettingName.setText(mActivity.getResources().getString(R.string.SelfInfoSettingScreen_5));
		mHolder.mBindEmail.mHolder.mSettingDescription
				.setText(mActivity.getResources().getString(R.string.SelfInfoSettingScreen_6));
		if (mLoginInfo.mBindInfoEmail != null && !TextUtils.isEmpty(mLoginInfo.mBindInfoEmail.mBindId)) {
			mHolder.mBindEmail.mHolder.mContent.setVisibility(View.VISIBLE);
			mHolder.mBindEmail.mHolder.mContent.setText(mLoginInfo.mBindInfoEmail.mBindId);
		}

		// change pwd
		mHolder.mChangePwd.mHolder.mSettingName.setText(mActivity.getResources().getString(R.string.SelfInfoSettingScreen_7));
		mHolder.mChangePwd.mHolder.mSettingDescription.setVisibility(View.GONE);

		// self sex
		mHolder.mSelfSex.mHolder.mSettingName.setText(mActivity.getResources().getString(R.string.SelfInfoSettingScreen_8));
		mHolder.mSelfSex.mHolder.mSettingDescription.setVisibility(View.GONE);
		setGender(mLoginInfo.mGender);
		if(mSex != null){
			mHolder.mSelfSex.mHolder.mContent.setVisibility(View.VISIBLE);
			mHolder.mSelfSex.mHolder.mContent.setText(mSex);
			mHolder.mSelfSex.mHolder.mNextPageIcon.setVisibility(View.GONE);
			mHolder.mSelfSex.setEnabled(false);
		}

		// self birthday
		mHolder.mSelfBirthday.mHolder.mSettingName.setText(mActivity.getResources().getString(R.string.SelfInfoSettingScreen_9));
		mHolder.mSelfBirthday.mHolder.mSettingDescription
				.setVisibility(View.GONE);
		if(!TextUtils.isEmpty(mLoginInfo.mBirthday) && !DEFAULT_BIRTHDAY.equals(mLoginInfo.mBirthday)
				|| !TextUtils.isEmpty(SettingDataManager.getInstance().getBirthday()) && !DEFAULT_BIRTHDAY.equals(SettingDataManager.getInstance().getBirthday())){
			mHolder.mSelfBirthday.mHolder.mContent.setVisibility(View.VISIBLE);
			
			if(!TextUtils.isEmpty(mLoginInfo.mBirthday) && !DEFAULT_BIRTHDAY.equals(mLoginInfo.mBirthday)){
				mHolder.mSelfBirthday.mHolder.mContent.setText(mLoginInfo.mBirthday);
			}else{
				mHolder.mSelfBirthday.mHolder.mContent.setText(SettingDataManager.getInstance().getBirthday());
			}
					
			mHolder.mSelfBirthday.mHolder.mNextPageIcon.setVisibility(View.GONE);
//			mHolder.mSelfBirthday.setEnabled(false);
		}

		// self college
		mHolder.mSelfCollege.mHolder.mSettingName.setText(mActivity.getResources().getString(R.string.SelfInfoSettingScreen_10));
		mHolder.mSelfCollege.mHolder.mSettingDescription
				.setVisibility(View.GONE);
		
		if(!TextUtils.isEmpty(mLoginInfo.mSchool) || !TextUtils.isEmpty(SettingDataManager.getInstance().getSchool())){
			mHolder.mSelfCollege.mHolder.mContent.setVisibility(View.VISIBLE);
			if(!TextUtils.isEmpty(mLoginInfo.mSchool)){
				mHolder.mSelfCollege.mHolder.mContent.setText(mLoginInfo.mSchool);
			}else{
				mHolder.mSelfCollege.mHolder.mContent.setText(SettingDataManager.getInstance().getSchool());
			}
			
			mHolder.mSelfCollege.mHolder.mNextPageIcon.setVisibility(View.GONE);
			mHolder.mSelfCollege.setEnabled(false); 
		}
	}
	
	private void initViewData() {
//		if(!TextUtils.isEmpty(mSex)){
//			mHolder.mSelfSex.mHolder.mContent.setVisibility(View.VISIBLE);
//			mHolder.mSelfSex.mHolder.mContent.setText(mSex);
//		}
//		if(!TextUtils.isEmpty(mBirthday)){
//			mHolder.mSelfBirthday.mHolder.mContent.setVisibility(View.VISIBLE);
//			mHolder.mSelfBirthday.mHolder.mContent.setText(mBirthday);
//		}
//		if(!TextUtils.isEmpty(mCollege)){
//			mHolder.mSelfCollege.mHolder.mContent.setVisibility(View.VISIBLE);
//			mHolder.mSelfCollege.mHolder.mContent.setText(mCollege);
//		}
	}

	private void initEvent() {
		mHolder.mSelfHead.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ImageViewActivity.show(mActivity);
			}
		});
		
		// bind mobile
		mHolder.mBindMobileNum.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mLoginInfo.mBindInfoMobile != null && !TextUtils.isEmpty(mLoginInfo.mBindInfoMobile.mBindId)) {
					BindInfoActivity.show(mActivity, mLoginInfo.mBindInfoMobile.mBindId, RegisterAccountActivity.FLAG_BIND_PHONE);
				} else {
					RegisterAccountActivity.show(mActivity, RegisterAccountActivity.FLAG_BIND_PHONE);
				}
				finish();
			}
		});

		// bind email
		mHolder.mBindEmail.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mLoginInfo.mBindInfoEmail != null && !TextUtils.isEmpty(mLoginInfo.mBindInfoEmail.mBindId)) {
					BindInfoActivity.show(mActivity, mLoginInfo.mBindInfoEmail.mBindId, RegisterAccountActivity.FLAG_BIND_EMAIL);
				} else {
					RegisterAccountActivity.show(mActivity, RegisterAccountActivity.FLAG_BIND_EMAIL);
				}
				finish();
			}
		});

		// change pwd
		mHolder.mChangePwd.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				SelfInfoSettingForwardActivity
						.show(mActivity,
								SelfInfoSettingForwardActivity.SelfForwardScreenType.CHANGE_PWD_SCREEN);
			}
		});

		// self sex
		if(TextUtils.isEmpty(mSex)){
			mHolder.mSelfSex.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					mSexDialog.show();
				}
			});
		}

		// self birthday
//		if(TextUtils.isEmpty(mLoginInfo.mBirthday) || DEFAULT_BIRTHDAY.equals(mLoginInfo.mBirthday)){
//		}
		mHolder.mSelfBirthday.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				createBirthdayDialog().show();
			}
		});

		// self college
		if(TextUtils.isEmpty(mLoginInfo.mSchool) && TextUtils.isEmpty(SettingDataManager.getInstance().getSchool())){
			mHolder.mSelfCollege.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					SelfInfoSettingForwardActivity
					.show(mActivity,
							SelfInfoSettingForwardActivity.SelfForwardScreenType.SELECT_COLLEGE_SCREEN);
				}
			});
		}

	}
	
	private void setBirthday(int year, int month, int day){
		if(!(year == 0 && month == 0 && day == 0)){
			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.YEAR, year);
			cal.set(Calendar.MONTH, month);
			cal.set(Calendar.DAY_OF_MONTH, day);
			mBirthday = sdf.format(cal.getTime());
		}
	}
	
	private void setGender(int gender){
		if(gender == 1){
			mSex = mActivity.getResources().getString(R.string.sex_male);
			mHolder.mSelfSexIcon.setBackgroundResource(R.drawable.contact_male);
		}else{
			mSex = mActivity.getResources().getString(R.string.sex_female);
			mHolder.mSelfSexIcon.setBackgroundResource(R.drawable.contact_female);
		}
	}
	
	private Dialog createSexDialog(){
		AlertDialog.Builder buider = new AlertDialog.Builder(mActivity);
        buider.setItems(R.array.gender,new DialogInterface.OnClickListener() {
            
            @Override
            public void onClick(DialogInterface dialog, int which) {
            	if(which == 0){
					mSexNum = SexType.MALE;
            	}else{
            		mSexNum = SexType.FEMALE;
            	}
            	mSex = mActivity.getResources().getStringArray(R.array.gender)[which];
            	
            	mHolder.mSelfSex.mHolder.mContent.setVisibility(View.VISIBLE);
				mHolder.mSelfSex.mHolder.mContent.setText(mSex);
				
				SettingDataManager.getInstance().putGender(mSexNum);
            }
        });
        return buider.create();
		
	}
	
	private Dialog createBirthdayDialog(){
		Calendar c=Calendar.getInstance();
		
		Date date;
		if(!TextUtils.isEmpty(mBirthday)){
			try {
				date = sdf.parse(mBirthday);
				c.setTime(date);
			} catch (ParseException e) {
				SystemUtil.f_log("" + e.toString());
				e.printStackTrace();
			}
		}else if(!TextUtils.isEmpty(mLoginInfo.mBirthday) && !DEFAULT_BIRTHDAY.equals(mLoginInfo.mBirthday)){
			try {
				date = sdf.parse(mLoginInfo.mBirthday);
				c.setTime(date);
			} catch (ParseException e) {
				SystemUtil.f_log("" + e.toString());
				e.printStackTrace();
			}
		}
        return new DatePickerDialog(mActivity, new DatePickerDialog.OnDateSetListener() {
            
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                    int dayOfMonth) {
            	Calendar tempCalendar = Calendar.getInstance();
            	long currentTime = tempCalendar.getTimeInMillis();
            	tempCalendar.set(year, monthOfYear, dayOfMonth);
            	long selectTime = tempCalendar.getTimeInMillis();
            	if(selectTime > currentTime){// 未来时间不可选取
            		SystemUtil.toast(mActivity.getResources().getString(R.string.SelfInfoSettingScreen_11));
            	}else{
            		setBirthday(year, monthOfYear, dayOfMonth);
    				
    				mHolder.mSelfBirthday.mHolder.mContent.setVisibility(View.VISIBLE);
    				mHolder.mSelfBirthday.mHolder.mContent.setText(mBirthday);
    				
    				SettingDataManager.getInstance().putBirthday(mBirthday);
            	}
            }
        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));

	}

	@Override
	public void updateUI_School() {
		RenrenChatApplication.mHandler.post(new Runnable() {
			@Override
			public void run() {
				String school = SettingDataManager.getInstance().getSchool();
				if(school != null){
					mHolder.mSelfCollege.mHolder.mContent.setVisibility(View.VISIBLE);
					mHolder.mSelfCollege.mHolder.mContent.setText(school);
				}
			}
		});
	}

	@Override
	public void updateUI_Photo() {
		RenrenChatApplication.mHandler.post(new Runnable() {
			@Override
			public void run() {
				mHolder.mSelfHead.clear();
				mHolder.mSelfHead.addUrl(LoginManager.getInstance().getLoginInfo().mMediumUrl);
			}
		});
	}
	
	public void unRegisterPhoteUploadSuccessListener(){
		SettingDataManager.getInstance().unRegisterPhotoUploadSuccessListener(this);
	}
}
