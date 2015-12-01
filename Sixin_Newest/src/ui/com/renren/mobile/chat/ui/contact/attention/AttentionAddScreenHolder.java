package com.renren.mobile.chat.ui.contact.attention;

import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.core.util.ViewMapping;
import com.renren.mobile.chat.R;
import com.renren.mobile.chat.ui.contact.C_ContactsListView;
import com.renren.mobile.chat.ui.contact.C_LetterBar;

/**
 * @author dingwei.chen1988@gmail.com
 * */
public class AttentionAddScreenHolder {

	@ViewMapping(ID=R.id.lc_attention_add_listview)
	public C_ContactsListView mContactsListView = null;//联系人列表
	
	@ViewMapping(ID=R.id.lc_attention_add_pop_message)
	public TextView mPopText = null;//弹出字母
	
	@ViewMapping(ID=R.id.lc_attention_add_letter_bar)
	public C_LetterBar mLetterBar = null;//侧边索引条
	
	@ViewMapping(ID=R.id.cdw_search_edit)
	public EditText mSearchEditText = null;//搜索框
	
	@ViewMapping(ID=R.id.cdw_search_del)
	public ImageView mImageDel = null;//清空文本按钮
	
	@ViewMapping(ID=R.id.lc_attention_add_loadding)
	public LinearLayout mLoaddataLayout;
	
	@ViewMapping(ID=R.id.lc_attention_add_nodata)
	public LinearLayout mAttentionAddNoDataLayout;
	
	@ViewMapping(ID=R.id.lc_no_search_data)
	public TextView mNoSearchData;
}
