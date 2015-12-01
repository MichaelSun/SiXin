package com.renren.mobile.chat.database;

import com.core.database.Column;
import com.core.database.DatabaseTypeConstant;

/**
 * @author yayun.wei
 * @说明 同步聊天记录时间数据库的列字段
 * */
public interface SynHistoryRecord_Column {

	@Column(defineType=DatabaseTypeConstant.INT + " " + DatabaseTypeConstant.PRIMARY)
	public static final String _ID = "_id";
	
	@Column(defineType=DatabaseTypeConstant.LONG)
	public final String TO_CHAT_ID = "syn_to_chat_id"; //聊天对象的id
	
	@Column(defineType=DatabaseTypeConstant.LONG)
	public final String LAST_SYN_TIME="syn_last_syn_time";//最近一次同步的时间
	
    @Column(defineType=DatabaseTypeConstant.LONG)
    public final String LOCAL_ID="syn_local_id";//当前用户的id
}
