package com.renren.mobile.chat.ui.message;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.telephony.SmsManager;
import com.renren.mobile.chat.RenrenChatApplication;
import com.renren.mobile.chat.base.model.ChatBaseItem;
import com.renren.mobile.chat.base.util.SystemUtil;
import com.renren.mobile.chat.R;
import com.renren.mobile.chat.RenrenChatApplication;
public class RRSmsManager {
	/**
	 * 将系统短信库中的短信置为已读
	 * */
	private static final String MSG_SIGN_RENREN = "renren";
	public static final String SENT_SMS_ACTION = "com.renren.sms.send";
	public static final String DELIVERED_SMS_ACTION = "com.renren.sms.receiver";
	public static final String SMS_CONVERSATION = "sms_conversation";
	public static final String INTENT_ACTION_SMS_RESULT = "com.renren.intent.action.sms";
	private static final int SMS_MAX_LENGTH = 70;
	public static final int DEFAULT_ID = 10000;
	public static RRSmsManager rrSmsManager;
	public static HashMap<String, Integer> smsListNum = new HashMap<String, Integer>();
	private PendingIntent sentPI, deliverPI;
	private RRSmsManager(){
		Init();
	}
	public static RRSmsManager gerInstance(){
		if(rrSmsManager == null)
		{
			rrSmsManager = new RRSmsManager();
		}
		return rrSmsManager;
	}
	private void Init(){
		Intent deliverIntent = new Intent(DELIVERED_SMS_ACTION);  
		deliverPI = PendingIntent.getBroadcast(RenrenChatApplication.mContext, 0,  
		    deliverIntent, 0);
	}
	/**
	 * @param body : sms content
	 * @param address : sms address
	 */
	public boolean sendMessage(String address, String body, long smsID){
		String telNUM = resetTelNum(address);
		SmsManager smsManager = SmsManager.getDefault();
		if(checkTelNum(telNUM)){
				if (body.length() < SMS_MAX_LENGTH) {
					Intent sentIntent = new Intent(SENT_SMS_ACTION);
					sentIntent.putExtra(SMS_CONVERSATION, String.valueOf(smsID));
					smsListNum.put(String.valueOf(smsID), 1);
					sentIntent.putExtra("address", telNUM);
					sentIntent.putExtra("body", body);
					sentPI = PendingIntent.getBroadcast(RenrenChatApplication.mContext, 0, sentIntent,  
							PendingIntent.FLAG_UPDATE_CURRENT);
					try {
						smsManager.sendTextMessage(telNUM, null, body, sentPI, deliverPI);
					} catch (SecurityException e) {
						SystemUtil.toast(RenrenChatApplication.getmContext().getResources().getString(R.string.RRSmsManager_java_1));		//RRSmsManager_java_1=未能获取发送短信权限; 
						return false;
					}
				}else{
					ArrayList<String> msgs = smsManager.divideMessage(body);
					for (String msg : msgs) {
						Intent sentIntent = new Intent(SENT_SMS_ACTION);
						sentIntent.putExtra(SMS_CONVERSATION, smsID);
						sentIntent.putExtra("address", telNUM);
						sentIntent.putExtra("body", body);
						smsListNum.put(String.valueOf(smsID), msgs.size());
						sentPI = PendingIntent.getBroadcast(RenrenChatApplication.mContext, 0, sentIntent,  
								PendingIntent.FLAG_UPDATE_CURRENT);
						try {
							smsManager.sendTextMessage(telNUM, null, msg, sentPI, deliverPI);
						} catch (SecurityException e) {
							SystemUtil.toast(RenrenChatApplication.getmContext().getResources().getString(R.string.RRSmsManager_java_1));		//RRSmsManager_java_1=未能获取发送短信权限; 
							return false;
						}
					}
				}
		}
		return true;
	}
	private boolean checkTelNum(String tel){
		Pattern pattern = Pattern.compile("^[0-9]+$");
		Matcher matcher = pattern.matcher(tel);
		if (!matcher.matches()) {
			return false;
		}
		return true;
	}
	private String resetTelNum(String telNum){
		if(telNum.startsWith("+86")){
			return telNum.substring(3);
		}else{
			return telNum;
		}
	}
	public static void insertToDB(String address, String content){
		ContentValues values = new ContentValues();
		values.put("address", address);
		values.put("body", content);
		values.put("protocol", MSG_SIGN_RENREN);
		RenrenChatApplication.mContext.getContentResolver().insert(Uri.parse("content://sms/sent"), values);
	}
	public static void readMessageById(long id, long date) {
		ContentResolver contentResolver = RenrenChatApplication.getmContext().getContentResolver();
		ContentValues cv = new ContentValues();
		cv.put(SMS.READ, ChatBaseItem.MESSAGE_READ.READ);
		contentResolver.update(SMS.CONTENT_URI, cv, SMS._ID + " = ? and " + SMS.DATE + " = ?", new String[] { String.valueOf(id), String.valueOf(date) });
	}
	public static void readMessageByPhone(String phoneNumber) {
		ContentResolver contentResolver = RenrenChatApplication.getmContext().getContentResolver();
		ContentValues cv = new ContentValues();
		cv.put(SMS.READ, ChatBaseItem.MESSAGE_READ.READ);
		contentResolver.update(SMS.CONTENT_URI, cv, SMS.ADDRESS + " like \"%" + phoneNumber + "\"", null);
	}
}
