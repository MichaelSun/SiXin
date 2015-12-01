package com.renren.mobile.chat.ui.contact;

import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.core.util.ViewMapping;
import com.renren.mobile.chat.R;

/**
 * @author dingwei.chen1988@gmail.com
 * */
public class C_ContactsScreenHolder {

	@ViewMapping(ID=R.id.cdw_contacts_listview)
	public C_ContactsListView mContactsListView = null;//联系人列表
	
	@ViewMapping(ID=R.id.cdw_pop_message)
	public TextView mPopText = null;//弹出字母
	
	@ViewMapping(ID=R.id.cdw_letter_bar)
	public C_LetterBar mLetterBar = null;//侧边索引条
	
	@ViewMapping(ID=R.id.search_layout)
	public FrameLayout mSearchFrameLayout=null; //搜索没有结果时候显示
	
	
	@ViewMapping(ID=R.id.cdw_search_edit)
	public EditText mSearchEditText = null;//搜索框
	
	@ViewMapping(ID=R.id.cdw_search_del)
	public ImageView mImageDel = null;//清空文本按钮
	
	@ViewMapping(ID=R.id.contacts_nodata)
	public LinearLayout mNoData=null; //没有数据时候显示
	
	@ViewMapping(ID=R.id.contacts_loaddata)
	public LinearLayout mOnLoading=null; //正在加载数据时候显示  
	
	@ViewMapping(ID=R.id.data_textView)
	public TextView mNoDataText=null; //没有数据时候显示在上面的文字
	
	@ViewMapping(ID=R.id.data_textView1)
	public TextView mNoDataText1=null; //没有数据时候显示在下面的文字
	
	@ViewMapping(ID=R.id.data_imageView)
	public ImageView mTipImage=null;
	
	@ViewMapping(ID=R.id.no_search_data)
	public TextView mNoSearchData=null; //搜索没有结果时候显示
	
	@ViewMapping(ID=R.id.contact_main_frame)
	public FrameLayout mFrameLayout=null; //搜索没有结果时候显示
	
	
	//@ViewMapping(ID=R.id.cdw_search_edittext_layout)
	//public FrameLayout search_edittext_layout=null; //搜索没有结果时候显示
	
	
}
