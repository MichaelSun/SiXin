package com.renren.mobile.chat.actions.models;

import com.core.orm.ORM;
import com.renren.mobile.chat.database.SynHistoryRecord_Column;

import java.io.Serializable;

/**
 * SynHistoryRecordModel
 * @author : xiaoguang.zhang
 * Date: 12-8-31
 * Time: 下午7:20
 * @说明 : 读取某个联系人同步记录的的信息转化成的对象
 */
public class SynHistoryRecordModel implements Serializable {

    @ORM(mappingColumn = SynHistoryRecord_Column.TO_CHAT_ID)
    public long mToChatId;

    @ORM(mappingColumn = SynHistoryRecord_Column.LAST_SYN_TIME)
    public long mLastSynTime;

    @ORM(mappingColumn = SynHistoryRecord_Column.LOCAL_ID)
    public long mLocalId;
}
