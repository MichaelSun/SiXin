package com.renren.mobile.chat.contact;

import android.provider.ContactsContract.Data;

/**
 * sync adapter在data表中的映射 同在xml/contacts中声明
 *
 */
public class RRSyncContactColumns {

    public static final String MIME_PROFILE = "vnd.android.cursor.item/vnd.renren.profile";

    public static final String DATA_UID = Data.DATA1;
    public static final String DATA_SUMMARY = Data.DATA2;
    public static final String DATA_DETAIL = Data.DATA3;
}
