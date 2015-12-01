package com.common.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

/**
 * 拦截短信工具类
 * 
 * @author shichao.song
 * 
 * 
 *         定义一个接收者： 
 *         <receiver
 *         android:name="com.common.utils.InterceptSMSMessage"
 *         android:label="InterceptSMSMessage"> 
 *         <intent-filter android:priority="10000"> 
 *         	<action android:name="android.provider.Telephony.SMS_RECEIVED"/>
 *         </intent-filter> 
 *         </receiver>
 * 
 *         设置优先级： <intent-filter android:priority="10000"> 接收信息服务是ordered
 *         broadcast，所以是按照优先级对信息进行接收并处理的， 此处将优先级设为最高，所以由这个receiver先接收并处理信息
 * 
 * 
 *         我们只需声明接收短信的权限即可 
 *         <uses-permission android:name="android.permission.RECEIVE_SMS"/> <!-- 接收短信权限 -->
 *         <uses-permission android:name="android.permission.SEND_SMS"/> <!-- 发送短信权限 -->
 */
public class InterceptSMSMessage extends BroadcastReceiver {
	/**
	 * The defination of the SMSACTION
	 */
	private final String SMSACTION = "android.provider.Telephony.SMS_RECEIVED";
	
	/**
	 * 短信内容
	 */
	StringBuilder mSmsContent = new StringBuilder();	
	/**
	 * 短信发件人号码
	 */
	StringBuilder mSmsNumber = new StringBuilder();

	@Override
	public void onReceive(Context context, Intent intent) {
		 
		if (intent.getAction().equals(SMSACTION)) {
			// 接收由Intent传来的数据 
			Bundle bundle = intent.getExtras();
			if (bundle != null) {
				// pdus为 android内置短信参数 identifier , 通过bundle.get("")返回一包含pdus的对象 
				Object[] myOBJpdus = (Object[]) bundle.get("pdus");
				// 构建短信对象array,并依据收到的对象长度来创建array的大小 
				SmsMessage[] messages = new SmsMessage[myOBJpdus.length];
				for (int i = 0; i < myOBJpdus.length; i++) {
					messages[i] = SmsMessage
							.createFromPdu((byte[]) myOBJpdus[i]);
				}
				for (SmsMessage currentMessage : messages) {
					mSmsContent.append(currentMessage.getDisplayMessageBody());
					mSmsNumber.append(currentMessage
							.getDisplayOriginatingAddress());
				}
				String smsBody = mSmsContent.toString();
				String smsNumber = mSmsNumber.toString();
				if (smsNumber.contains("+86")) {
					smsNumber = smsNumber.substring(3);
				}
				
				// 确认该短信内容是否满足过滤条件 
				boolean flags_filter = false;
				if (smsNumber.equals("106900867755")) {// 屏蔽人人发来的短信
					flags_filter = true; 
					// 解析smsBody，获取其中的验证码，赋值给verifyCode
					char[] ch = smsBody.toCharArray();
					String verifyCode = "";
					for (int i = 0; i < ch.length; i++) {
						if (Character.isDigit(ch[i]))
							verifyCode += ch[i];
					}

					// count++;
//					if (mVerifyCodeEditText.getVisibility() != View.VISIBLE) {
//						Message message = new Message();
//						message.what = 1;
//						mHandler.sendMessage(message);
//					} 
					
				}
				// 第三步:取消
				if (flags_filter) {
					this.abortBroadcast();
				} 
			} 
		} 
	

	}

}
