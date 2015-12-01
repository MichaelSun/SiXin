package com.renren.mobile.chat.ui.setting;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.common.manager.LoginManager;
import com.common.manager.PhotoUploadManager;
import com.common.manager.LoginManager.LoginInfo;
import com.common.mcs.INetRequest;
import com.common.mcs.INetResponse;
import com.common.mcs.McsServiceProvider;
import com.common.utils.RRSharedPreferences;
import com.core.json.JsonObject;
import com.core.json.JsonParser;
import com.core.json.JsonValue;
import com.core.util.SystemService;
import com.core.util.ViewMapUtil;
import com.core.util.ViewMapping;
import com.renren.mobile.account.LoginControlCenter;
import com.renren.mobile.account.LoginfoModel;
import com.renren.mobile.chat.R;
import com.renren.mobile.chat.RenrenChatApplication;
import com.renren.mobile.chat.base.util.SystemUtil;
import com.renren.mobile.chat.common.ResponseError;

/**
 * @author xiangchao.fan
 * @description 设置页面相关数据管理器
 */
public class SettingDataManager {
	
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	/** sharedPreferenc本地存储名  */
	public static final String SP_NAME = "setting_sp";
	
	private RRSharedPreferences mSP;
	
	/** singleton object */
	private static SettingDataManager sInstance = new SettingDataManager();
	
	/** constructor */
	private SettingDataManager(){
		mSP = new RRSharedPreferences(RenrenChatApplication.mContext, SP_NAME);
		mSelfInfo = new JsonObject();
		mPrivateInfo = new JsonObject();
	}
	
	/** singleton */
	public static SettingDataManager getInstance(){
		return sInstance;
	}
	
	// self info method
	/**************************************************************************/
	/** self_info本地存储名  */
	public static final String SELF_INFO_STORE = "self_info_store";
	public static interface SelfInfoType{
		public static final String GENDER = "gender";
		public static final String BIRTHDAY = "birthday";
		public static final String SCHOOL = "school";
	}
	
	private JsonObject mSelfInfo;
	
	/** self_info loading flag */
	private AtomicBoolean mLoading_self_info = new AtomicBoolean(false);
	
	private void obtainSelfInfo(){
		String temp = mSP.getStringValue(SELF_INFO_STORE, null);
		SystemUtil.f_log("(temp == null):" + (temp == null));
		if(temp != null){
			mSelfInfo = (JsonObject) JsonParser.parse(temp);
		}
		SystemUtil.f_log("mSelfInfo:" + mSelfInfo.toJsonString());
	}
	
	/**
	 * @description 用户selfInfo是否已上传
	 * @return true:已上传，false:未上传
	 */
	public boolean isSelfInfoHasUpload(){
		String temp = mSP.getStringValue(SELF_INFO_STORE, null);
		if(temp != null)
			return false;
		else 
			return true;
	}
	
	public synchronized void putGender(int gender){
		obtainSelfInfo();
		mSelfInfo.put(SelfInfoType.GENDER, gender);
		mSP.put(SELF_INFO_STORE, mSelfInfo.toJsonString());
	}
	
	public synchronized void putBirthday(String birthday){
		obtainSelfInfo();
		mSelfInfo.put(SelfInfoType.BIRTHDAY, birthday);
		mSP.put(SELF_INFO_STORE, mSelfInfo.toJsonString());
	}
	
	public String getBirthday(){
		obtainSelfInfo();
		return mSelfInfo.getString(SelfInfoType.BIRTHDAY);
	}
	
	public synchronized void putSchool(String school){
		obtainSelfInfo();
		mSelfInfo.put(SelfInfoType.SCHOOL, school);
		mSP.put(SELF_INFO_STORE, mSelfInfo.toJsonString());
	}
	
	public String getSchool(){
		obtainSelfInfo();
		return mSelfInfo.getString(SelfInfoType.SCHOOL);
	}
	
	/** 供外部调用接口: 上传用户个人设置 */
	public void toUploadSelfInfo(){
		if(!mLoading_self_info.get()){
			uploadSelfInfo();
		}
	}
	
	private synchronized void clearSelfInfo(){
		mSelfInfo.clear();
		mSP.remove(SELF_INFO_STORE);
	}
	
	private void uploadSelfInfo(){
		mLoading_self_info.set(true);
		
		obtainSelfInfo();
		
		Map<String, String> map = new HashMap<String, String>();
		// 设置性别
		final long gender = mSelfInfo.getNum(SelfInfoType.GENDER, -1);
		SystemUtil.f_log("gender:" + gender);
		if(gender != -1){
			map.put("gender", String.valueOf(gender));
		}
		// 设置生日
		final String birthday = mSelfInfo.getString(SelfInfoType.BIRTHDAY);
		if(birthday != null){
			try {
				Calendar cal = Calendar.getInstance();
				Date date = sdf.parse(birthday);
				cal.setTime(date);
				int year = cal.get(Calendar.YEAR);
				int month = cal.get(Calendar.MONTH);
				int day = cal.get(Calendar.DAY_OF_MONTH);
				
				map.put("birth_year", String.valueOf(year));
				map.put("birth_month", String.valueOf(month + 1));
				map.put("birth_day", String.valueOf(day));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		
		// 设置学校
		final String school = mSelfInfo.getString(SelfInfoType.SCHOOL);
		if(school != null){
			map.put("school_name", String.valueOf(school));
		}
		SystemUtil.f_log("map:" + map.toString());
		McsServiceProvider.getProvider().putProfile(new INetResponse() {
			
			@Override
			public void response(final INetRequest req, JsonValue obj) {
				if (obj != null && obj instanceof JsonObject) {
					final JsonObject map = (JsonObject) obj;
					SystemUtil.f_log("map:" + map.toJsonString());
					if (ResponseError.noError(req, map,false)) {
						int result = (int)map.getNum("result");
						if(result == 1){
							SystemUtil.f_log("success");
							
							// 清除本地记录
							clearSelfInfo();
							
							// 更新本地数据库
							updateUserData(gender, birthday, school);
						}
					}
				}
				mLoading_self_info.set(false);
			}
		}, map);
	}
	
	private void updateUserData(long gender, String birthday, String school){
		LoginInfo loginInfo = LoginManager.getInstance().getLoginInfo();
		LoginfoModel loginfo = new LoginfoModel();
		if(gender != -1){
			loginInfo.mGender = (int) gender;
		}
		if(birthday != null){
			loginInfo.mBirthday = birthday;
		}
		if(school != null){
			loginInfo.mSchool = school;
		}
		loginfo.parse(loginInfo);
		LoginControlCenter.getInstance().updateUserData(loginfo);
	}
	/**************************************************************************/
	
	
	
	
	// private info method
	/**************************************************************************/
	/** private_info本地存储名  */
	public static final String PRIVATE_INFO_STORE = "private_info_store";
	
	public static interface PrivateSwitchType{
		final public static int NO_SWITCH_OPEN = 0;
		final public static int VERIFICATION_SWITCH_OPEN  = 1;
		final public static int ID_SEARCHABLE_SWITCH_OPEN = 2;
		final public static int MOBILE_SEARCHABLE_SWITCH_OPEN = 4;
		final public static int NAME_SEARCHABLE_SWITCH_OPEN = 8;
	}
	private int mPrivateSwitchState;
	
	/** 状态开关 */
	private boolean mVerificationRequired;
	private boolean mIdSearchable;
	private boolean mNameSearchable;
	private boolean mPhoneSearchable;
	
	private JsonObject mPrivateInfo;
	
	/** private_info loading flag */
	private AtomicBoolean mLoading_private_info = new AtomicBoolean(false);
	
	/**
	 * @description 用户privateInfo是否已上传，用于用户privateInfo上传失败后再上传时的判断
	 * @return true:已上传，false:未上传
	 */
	public boolean isPrivateInfoHasUpload(){
		int temp = mSP.getIntValue(PRIVATE_INFO_STORE, -1);
		
		SystemUtil.f_log("temp-=============================:" + temp);
		
		if(temp != -1)
			return false;
		else 
			return true;
	}
	
	/**
	 * @description 共外部调用，当第一次进入隐私设置且从网络获取用户隐私信息成功后调用
	 * @param verificationRequired
	 * @param idSearchable
	 * @param phoneSearchable
	 * @param nameSearchable
	 */
	public void obtainPirvateSwitchState(boolean verificationRequired, boolean idSearchable, boolean phoneSearchable, boolean nameSearchable){
		this.mVerificationRequired = verificationRequired;
		this.mIdSearchable = idSearchable;
		this.mPhoneSearchable = phoneSearchable;
		this.mNameSearchable = nameSearchable;
		updatePrivateSwitchState();
		updatePrivateInDB();
	}	
	
	/**
	 * @description 从本地文件中读取mPrivateSwitchState，
	 * key: @see PRIVATE_INFO_STORE
	 */
	public void obtainPrivateSwitchState(){
		mPrivateSwitchState = mSP.getIntValue(PRIVATE_INFO_STORE, PrivateSwitchType.NO_SWITCH_OPEN);
		parsePrivateSwitchState(mPrivateSwitchState);
	}
	
	/**
	 * @description 从已知switchState解析出各属性状态值
	 */
	public void parsePrivateSwitchState(int switchState){
		mPrivateSwitchState = switchState;
		if((switchState & PrivateSwitchType.VERIFICATION_SWITCH_OPEN) == PrivateSwitchType.VERIFICATION_SWITCH_OPEN)
			mVerificationRequired = true;
		else
			mVerificationRequired = false;
		
		if((switchState & PrivateSwitchType.ID_SEARCHABLE_SWITCH_OPEN) == PrivateSwitchType.ID_SEARCHABLE_SWITCH_OPEN)
			mIdSearchable = true;
		else
			mIdSearchable = false;
		
		if((switchState & PrivateSwitchType.MOBILE_SEARCHABLE_SWITCH_OPEN) == PrivateSwitchType.MOBILE_SEARCHABLE_SWITCH_OPEN)
			mPhoneSearchable = true;
		else
			mPhoneSearchable = false;
		
		if((switchState & PrivateSwitchType.NAME_SEARCHABLE_SWITCH_OPEN) == PrivateSwitchType.NAME_SEARCHABLE_SWITCH_OPEN)
			mNameSearchable = true;
		else
			mNameSearchable = false;
	}
	
	private synchronized void updatePrivateSwitchState() {
		computePrivateSwitchState();
		mSP.putIntValue(PRIVATE_INFO_STORE, mPrivateSwitchState);
	}
	
	public boolean getVerificationState() {
		return mVerificationRequired;
	}

	public void setVerificationState(boolean verificationState) {
		this.mVerificationRequired = verificationState;
		updatePrivateSwitchState();
	}

	public boolean getIdSearchableState() {
		return mIdSearchable;
	}

	public void setIdSearchableState(boolean idSearchable) {
		this.mIdSearchable = idSearchable;
		updatePrivateSwitchState();
	}

	public boolean getPhoneSearchable() {
		return mPhoneSearchable;
	}

	public void setPhoneSearchableState(boolean phoneSearchable) {
		this.mPhoneSearchable = phoneSearchable;
		updatePrivateSwitchState();
	}
	
	public boolean getNameSearchable() {
		return mNameSearchable;
	}

	public void setNameSearchableState(boolean nameSearchable) {
		this.mNameSearchable = nameSearchable;
		updatePrivateSwitchState();
	}
	
	/** 供外部调用接口: 上传用户隐私设置 */
	public void toUploadPrivateInfo(){
		if(!mLoading_private_info.get()){
			uploadPrivateInfo();
		}
	}
	
	private void computePrivateSwitchState(){
		mPrivateSwitchState = (mVerificationRequired?1:0) * PrivateSwitchType.VERIFICATION_SWITCH_OPEN
				+ (mIdSearchable?1:0) * PrivateSwitchType.ID_SEARCHABLE_SWITCH_OPEN
				+ (mPhoneSearchable?1:0) * PrivateSwitchType.MOBILE_SEARCHABLE_SWITCH_OPEN
				+ (mNameSearchable?1:0) * PrivateSwitchType.NAME_SEARCHABLE_SWITCH_OPEN;
	}
	
	private synchronized void clearPrivateInfo(){
		mPrivateInfo.clear();
		mSP.remove(PRIVATE_INFO_STORE);
	}
	
	private void uploadPrivateInfo(){
		mLoading_private_info.set(true);
		
		obtainPrivateSwitchState();
		McsServiceProvider.getProvider().putPrivacy(new INetResponse() {

			@Override
			public void response(final INetRequest req, final JsonValue obj) {

				boolean resultFlag = false;
				if (obj != null && obj instanceof JsonObject) {
					final JsonObject map = (JsonObject) obj;
					SystemUtil.f_log("map:" + map.toJsonString());
					if (ResponseError.noError(req, map, false)) {
						int result = (int) map.getNum("result");
						if (result == 1) {
							SystemUtil.f_log("upload private info success");
							resultFlag = true;
							
							clearPrivateInfo();
							
							// update database
							updatePrivateInDB();
						}
					}
				}
				if(!resultFlag){
					
				}
				mLoading_private_info.set(false);
			}
		}, mVerificationRequired, mIdSearchable, mNameSearchable, mPhoneSearchable);
	}
	
	private void updatePrivateInDB(){
		LoginInfo loginInfo = LoginManager.getInstance().getLoginInfo();
		LoginfoModel loginfo = new LoginfoModel();
		if(mPrivateSwitchState >= 0){
			loginInfo.mPrivate = mPrivateSwitchState;
		}
		loginfo.parse(loginInfo);
		LoginControlCenter.getInstance().updateUserData(loginfo);
	}
	/**************************************************************************/
	
	/** 选择学校回调接口 */
	public interface CollegeSelectListener{
		public void updateUI_School();
	}
	
	private CollegeSelectListener mCollegeSelectListener;
	
	public void registerCollegeSelectListener(CollegeSelectListener collegeSelectListener){
		mCollegeSelectListener = collegeSelectListener;
	}
	
	public void notifyCollegeSelectListener(){
		mCollegeSelectListener.updateUI_School();
	}
	
	
	/** 系统设置状态管理 */
	/**************************************************************************/
	public static final String SYSTEM_SETTING_MODE = "system_setting_mode";
	public static interface SystemSwitchType{
		final public static int NO_SWITCH_OPEN = 0;
		final public static int SOUND_SWITCH_OPEN  = 1;
		final public static int VIBRATE_SWITCH_OPEN = 2;
		final public static int PUSH_SWITCH_OPEN = 4;
	}
	private int mSystemSwitchState;
	public boolean mSoundState = true;
	public boolean mVibrateState = true;
	public boolean mPushState = true;
	
	/** mSoundState 和  mVibrateState 的和*/
	public int mRemindState;
	
	public void obtainSwitchState(){
		
		mSystemSwitchState = mSP.getIntValue(SYSTEM_SETTING_MODE, 7);
		if((mSystemSwitchState & SystemSwitchType.SOUND_SWITCH_OPEN) == SystemSwitchType.SOUND_SWITCH_OPEN)
			mSoundState = true;
		else
			mSoundState = false;
		
		if((mSystemSwitchState & SystemSwitchType.VIBRATE_SWITCH_OPEN) == SystemSwitchType.VIBRATE_SWITCH_OPEN)
			mVibrateState = true;
		else
			mVibrateState = false;
		
		if((mSystemSwitchState & SystemSwitchType.PUSH_SWITCH_OPEN) == SystemSwitchType.PUSH_SWITCH_OPEN)
			mPushState = true;
		else
			mPushState = false;
		
		mRemindState = (mSoundState?1:0) * SystemSwitchType.SOUND_SWITCH_OPEN 
		+ (mVibrateState?1:0) * SystemSwitchType.VIBRATE_SWITCH_OPEN;
	}
	
	private synchronized void updateSystemSwitchState() {
		mSystemSwitchState = (mSoundState?1:0) * SystemSwitchType.SOUND_SWITCH_OPEN 
				+ (mVibrateState?1:0) * SystemSwitchType.VIBRATE_SWITCH_OPEN 
				+ (mPushState?1:0) * SystemSwitchType.PUSH_SWITCH_OPEN; 
		mSP.putIntValue(SYSTEM_SETTING_MODE, mSystemSwitchState);
	}

	public void setSoundState(boolean soundState) {
		this.mSoundState = soundState;
		updateSystemSwitchState();
	}

	public void setVibrateState(boolean vibrateState) {
		this.mVibrateState = vibrateState;
		updateSystemSwitchState();
	}

	public void setPushState(boolean pushState) {
		this.mPushState = pushState;
		updateSystemSwitchState();
	}
	
	/**************************************************************************/
	
	
	
	/** 头像上传成功回调接口 */
	/**************************************************************************/
	public interface PhotoUploadSuccessListener{
    	public void updateUI_Photo();
    }
	
	private List<PhotoUploadSuccessListener> mPhotoUploadSuccessListeners = new ArrayList<PhotoUploadSuccessListener>();
	
	public void registerPhotoUploadSuccessListener(PhotoUploadSuccessListener listener){
		if(listener != null){
			mPhotoUploadSuccessListeners.add(listener);
		}
	}
	
	public void unRegisterPhotoUploadSuccessListener(PhotoUploadSuccessListener listener){
		if(listener != null && mPhotoUploadSuccessListeners.contains(listener)){
			mPhotoUploadSuccessListeners.remove(listener);
		}
	}
	
	public void notifyAllPhotoUploadSuccessListeners(){
		for(PhotoUploadSuccessListener listener:mPhotoUploadSuccessListeners)
			listener.updateUI_Photo();
	}
	/**************************************************************************/
	
	
	
	/**************************************************************************/
	public class DialogViewHolder{
		@ViewMapping(ID=R.id.loading_text)
		public TextView mLoadingText;
	}
	/** 通用loading dialog */
	public Dialog createLoadingDialog(Context context, String showText){
		Dialog dialog = new Dialog(context, R.style.GeneralDialog);
		View view = SystemService.sInflaterManager.inflate(R.layout.loading_dialog, null);
		dialog.setContentView(view);
		
		//view.setBackgroundColor(Color.parseColor("#33000000"));
		DialogViewHolder viewHolder = new DialogViewHolder();
		ViewMapUtil.getUtil().viewMapping(viewHolder, view);
		viewHolder.mLoadingText.setText(showText);
		
		return dialog;
	}
	public Dialog createLoadingDialog(Context context, String showText, int textColor){
		Dialog dialog = new Dialog(context, R.style.GeneralDialog);
		View view = SystemService.sInflaterManager.inflate(R.layout.loading_dialog, null);
		dialog.setContentView(view);
		
		//view.setBackgroundColor(Color.parseColor("#33000000"));
		DialogViewHolder viewHolder = new DialogViewHolder();
		ViewMapUtil.getUtil().viewMapping(viewHolder, view);
		viewHolder.mLoadingText.setText(showText);
		viewHolder.mLoadingText.setTextColor(textColor);
		
		return dialog;
	}
	/**************************************************************************/
	
	/** 选择修改头像 */
    public void selectDialog(final Activity activity) {
		final String items[] = { activity.getResources().getString(R.string.SettingDataManager_1), activity.getResources().getString(R.string.SettingDataManager_2) };
		final DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO 
				Intent intent = null;
				switch (which) {
				case 0://拍照上传
					String fileName = "head_"
							+ String.valueOf(System.currentTimeMillis());
					intent = PhotoUploadManager.getInstance()
							.getTakePhotoIntent("/sixin/", fileName,
									".jpg");
					activity.startActivityForResult(intent,
							PhotoUploadManager.REQUEST_CODE_HEAD_TAKE_PHOTO);
					break;
				case 1://本地上传
					activity.startActivityForResult(
									PhotoUploadManager.getInstance()
											.getChooseFromGalleryIntent(),
									PhotoUploadManager.REQUEST_CODE_HEAD_CHOOSE_FROM_GALLERY);
					break;
				default:
					break;
				}
			}
		};
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				PhotoUploadManager.getInstance().createUploadDialog(activity,
						activity.getResources().getString(R.string.SettingDataManager_3), items, dialogClickListener);
			}
		});
	}
    
}
