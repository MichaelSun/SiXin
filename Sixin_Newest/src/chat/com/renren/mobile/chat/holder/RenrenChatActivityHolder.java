package com.renren.mobile.chat.holder;

import view.bar.InputBar;
import view.list.BaseListView;
import view.list.ListViewWarpper;
import view.voice.VoiceView;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.core.util.SystemService;
import com.core.util.ViewMapUtil;
import com.core.util.ViewMapping;
import com.renren.mobile.chat.R;
import com.renren.mobile.chat.base.util.SystemUtil;


/**
 * @author dingwei.chen
 * @说明 人人聊天主界面的UI持有类
 * */
public class RenrenChatActivityHolder extends BaseHolder {
	
	@ViewMapping(ID=R.id.cdw_chat_main_root)
	public LinearLayout mRoot_Linearlayout = null;
	
	@ViewMapping(ID=R.id.cdw_chat_main_title_layout)
	public FrameLayout mRoot_Title = null;
	
	@ViewMapping(ID=R.id.cdw_chat_switch_toast)
	public LinearLayout mRoot_Switch_Toast = null;
	
	@ViewMapping(ID=R.id.cdw_chat_main_unable_view)
	public LinearLayout mRoot_unAble_View = null;
	
	@ViewMapping(ID=R.id.cdw_chat_main_chatlist_baselistview)
	public ListViewWarpper mRoot_ChatList = null;
	
	@ViewMapping(ID=R.id.chatmain_inputbar)
	public InputBar mRoot_InputBar = null;
	
	@ViewMapping(ID=R.id.chatmain_voiceview)
	public VoiceView mRoot_VoiceView = null;
	
	@ViewMapping(ID=R.id.id_framelayout)
	public View mRoot_FrameLayout;
	
	@ViewMapping(ID=R.id.cdw_chat_switch_toast)
	public View mSwitchToast ;
	
	@ViewMapping(ID=R.id.cdw_chat_switch_toast_text)
	public TextView mSwitchTextView ;
	
	public RenrenChatActivityHolder(Context context, int layoutID,ViewGroup root) {
		super(context, layoutID,root);
		ViewMapUtil.getUtil().viewMapping(this, root);
		this.initViews();
	}

	public RenrenChatActivityHolder(Context context,View root){
		ViewMapUtil.getUtil().viewMapping(this, root);
		this.mRoot = root;
		this.initViews();
	}

	
	
	
	
	@Override
	public void initViews() {
//		mRoot_ChatList.addHeaderView(mRoot_ChatHistory);
	}

	public void enable(){
		this.setEnable(true);
	}
	
	public void disable(){
		this.setEnable(false);
	}
	private void setEnable(boolean enabled){
	}

	
	public void clear(){}
	public void setOnCloseFeedWindowListener( OnClickListener listener){
	}
	
	
	
	
	
}
