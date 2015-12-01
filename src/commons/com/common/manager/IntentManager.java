package com.common.manager;

import android.content.Intent;
import android.net.Uri;

/**
 * author yuchao.zhang
 * <p/>
 * description 封装系统意图的管理类：比如发送短信、发送邮件等
 *             现提供两种发送短信、两种发送邮件的方式（分别为携带收件人地址和不携带地址的方式！）
 */
public class IntentManager {

    private static IntentManager instance = new IntentManager();

    public static IntentManager getInstance() {

        return instance;
    }

    /**
     * 跳转到系统的发短信界面
     *
     * @param number  要发送的电话号码，支持多个 格式如：“13666666666,13777777777”
     * @param content 要发送的信息内容
     * @return 返回发送短信的意图
     */
    public Intent getSendMessageIntent(String number, String content) {

        Uri smsToUri = Uri.parse("smsto://" + number);
        Intent intent = new Intent(android.content.Intent.ACTION_SENDTO, smsToUri);
        intent.putExtra("sms_body", content);

        return intent;
    }

    /**
     * 跳转到系统发短信页面（不携带收件人号码）
     * @param content 短信内容
     * @return 发送短信的跳转意图
     */
    public Intent getSendMessageWithNoNumberIntent(String content) {

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.putExtra("sms_body", content);
        intent.setType("vnd.android-dir/mms-sms");

        return intent;
    }

    /**
     * 跳转到系统的发邮件页面，如果没有创建过账号，则跳转到创建邮件账号的页面
     *
     * @param mails   要发送的邮件地址，支持多个 格式如："12345@renren-inc.com，yuchao.zhang@renren-inc.com"
     * @param subject 要发送的邮件标题
     * @param text    要发送的邮件内容
     * @return 返回发送邮件的意图
     */
    public Intent getSendEmailIntent(String[] mails, String subject, String text) {

        Uri mailToUri = Uri.parse("mailto:");
        Intent intent = new Intent(Intent.ACTION_SENDTO, mailToUri);
        intent.putExtra(Intent.EXTRA_EMAIL, mails);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, text);

        return intent;
    }

    /**
     * 跳转到系统的发邮件页面(不携带收件人)，如果没有创建过账号，则跳转到创建邮件账号的页面
     * @param subject 邮件标题
     * @param text 邮件内容
     * @return 发送邮件的跳转意图
     */
    public Intent getSendEmailWithNoAddressIntent(String subject, String text, String title) {

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, text);
//        intent.setType("message/rfc822");
        intent.setType("text/plain");

        return Intent.createChooser(intent, title);
    }
}
