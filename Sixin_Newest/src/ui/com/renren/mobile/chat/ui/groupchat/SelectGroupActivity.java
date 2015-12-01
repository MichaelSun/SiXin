package com.renren.mobile.chat.ui.groupchat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.renren.mobile.chat.R;
import com.renren.mobile.chat.RenrenChatApplication;
import com.renren.mobile.chat.actions.models.RoomInfoModelWarpper;
import com.renren.mobile.chat.activity.RenRenChatActivity;
import com.renren.mobile.chat.base.model.ChatBaseItem.MESSAGE_ISGROUP;
import com.renren.mobile.chat.newsfeed.NewsFeedWarpper;
import com.renren.mobile.chat.ui.BaseActivity;
import com.renren.mobile.chat.ui.chatsession.ChatSessionDataModel;
import com.renren.mobile.chat.ui.chatsession.ChatSessionManager;
import com.renren.mobile.chat.ui.contact.C_ContactsData;
import com.renren.mobile.chat.ui.contact.C_ContactsListView;
import com.renren.mobile.chat.ui.contact.ContactBaseModel.Contact_group_type;
import com.renren.mobile.chat.ui.contact.ContactModel;
import com.renren.mobile.chat.ui.contact.RoomInfosData;
import com.renren.mobile.chat.view.BaseTitleLayout;
/**
 * 
 * @author rubin.dong@renren-inc.com
 * @date 
 * 选择一个群组
 *
 */
import com.renren.mobile.chat.R;
import com.renren.mobile.chat.RenrenChatApplication;
public class SelectGroupActivity extends BaseActivity {
	FrameLayout mLinearLayout;
	BaseTitleLayout mBaseTitle;
	C_ContactsListView mListView;
	private LinearLayout mNoDataLayout;
	private SelectGroupAdapter adapter;
	private List<ContactModel> mGroupList = new ArrayList<ContactModel>();
	private Map<Long, Boolean> mTempRoomList = new HashMap<Long, Boolean>();
	private NewsFeedWarpper mNewsFeed;
	private String mFilePath = null;
	public static void show(Context context, NewsFeedWarpper newsFeed){
		Intent intent = new Intent(context, SelectGroupActivity.class);
		intent.putExtra(RenRenChatActivity.PARAM_NEED.CONTEXT_FROM, context.getClass().getSimpleName());
		intent.putExtra(RenRenChatActivity.PARAM_NEED.TO_CHAT_FEED_MODEL, newsFeed);
		context.startActivity(intent);
	}
	/**
	 * 
	 * @param context
	 * @param newsFeed
	 * @param filePath 图片转发
	 */
	public static void show(Context context, NewsFeedWarpper newsFeed, String filePath){
		Intent intent = new Intent(context, SelectGroupActivity.class);
		intent.putExtra(RenRenChatActivity.PARAM_NEED.CONTEXT_FROM, context.getClass().getSimpleName());
		intent.putExtra(RenRenChatActivity.PARAM_NEED.TO_CHAT_FEED_MODEL, newsFeed);
		intent.putExtra(RenRenChatActivity.PARAM_NEED.FORWARD_FILE_PATH, filePath);
		context.startActivity(intent);
	}
	
    public static void show(Activity context, NewsFeedWarpper newsFeed, String filePath , int requestCode){
        Intent intent = new Intent(context, SelectGroupActivity.class);
        intent.putExtra(RenRenChatActivity.PARAM_NEED.CONTEXT_FROM, context.getClass().getSimpleName());
        intent.putExtra(RenRenChatActivity.PARAM_NEED.TO_CHAT_FEED_MODEL, newsFeed);
        intent.putExtra(RenRenChatActivity.PARAM_NEED.FORWARD_FILE_PATH, filePath);
        context.startActivityForResult(intent, requestCode);
    }
    
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.select_group);
		mNewsFeed = (NewsFeedWarpper) getIntent().getSerializableExtra(RenRenChatActivity.PARAM_NEED.TO_CHAT_FEED_MODEL);
		mFilePath =  getIntent().getStringExtra(RenRenChatActivity.PARAM_NEED.FORWARD_FILE_PATH);
		mLinearLayout = (FrameLayout) this.findViewById(R.id.title_layout);
		mListView = (C_ContactsListView) this.findViewById(R.id.select_group_listview);
		mNoDataLayout = (LinearLayout) this.findViewById(R.id.select_group_nodata);
		mListView.setDivider(null);
		adapter = new SelectGroupAdapter(SelectGroupActivity.this, mListView);
		setForwardList();
		C_ContactsData.getInstance().resetListFlag(mGroupList);
		mListView.setAdapter(adapter);
		adapter.setContacts(mGroupList);
		mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				ContactModel model = (ContactModel)adapter.getItem(position);
				RoomInfoModelWarpper roomInfo = RoomInfosData.getInstance().getRoomInfo(model.getmUserId());
                Intent data = new Intent();
                data.putExtra(RenRenChatActivity.PARAM_NEED.TO_CHAT_USER_MODEL,
                        roomInfo);
                data.putExtra(RenRenChatActivity.PARAM_NEED.TO_CHAT_FEED_MODEL,
                        mNewsFeed);
                data.putExtra(RenRenChatActivity.PARAM_NEED.FORWARD_FILE_PATH,
                        mFilePath);
                setResult(RESULT_OK, data);
				finish();
			}
		});
		initTitle();
	}
	
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                setResult(RESULT_CANCELED);
                finish();
                return true;
            default:
                return super.onKeyDown(keyCode, event);
        }
    }
	
	private void initTitle() {
		mBaseTitle = new BaseTitleLayout(SelectGroupActivity.this,mLinearLayout);
		TextView mTextView = mBaseTitle.getTitleMiddle();
		mTextView.setText(RenrenChatApplication.getmContext().getResources().getString(R.string.SelectGroupActivity_java_1));		//SelectGroupActivity_java_1=选择群组; 
		mBaseTitle.getTitleLeft().setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				setResult(RESULT_CANCELED);
				finish();
			}
		});
	}
	private void setForwardList(){
		LinkedList<Object> linkedList = ChatSessionManager.getInstance().getSessionList();
		List<ContactModel> roomList = C_ContactsData.getInstance().getmSortGroupContacts();
		mGroupList.clear();
		mTempRoomList.clear();
		int size = linkedList.size();
		if(size == 0 && roomList.size() == 0){
			TextView dataView = (TextView) mNoDataLayout.findViewById(R.id.data_textView);
			TextView dataView1 = (TextView) mNoDataLayout.findViewById(R.id.data_textView1);
			dataView.setText(RenrenChatApplication.getmContext().getResources().getString(R.string.SelectGroupActivity_java_2));		//SelectGroupActivity_java_2=暂无群组; 
			dataView1.setText("");
			mNoDataLayout.setVisibility(View.VISIBLE);
		}else{
			for(int i=0;i<size;i++){
				ChatSessionDataModel chatSessionDataModle = (ChatSessionDataModel) linkedList.get(i);
				ContactModel model = null;
				ContactModel newModel = null;
				if(chatSessionDataModle.mIsGroup == MESSAGE_ISGROUP.IS_SINGLE){
				}else{
					RoomInfoModelWarpper tempRoom = RoomInfosData.getInstance().getRoomInfo(chatSessionDataModle.mGroupId);
					if (null != tempRoom) {
						newModel = new ContactModel();
						newModel.setGroupType(Contact_group_type.MULTIPLE);
						newModel.setmContactName(chatSessionDataModle.mName);
						newModel.setmUserId(chatSessionDataModle.mGroupId);
						mTempRoomList.put(chatSessionDataModle.mGroupId, true);
					}
				}
				if(null != newModel){
					mGroupList.add(newModel);
				}
			}
			for(ContactModel room:roomList){
				if(!mTempRoomList.containsKey(room.getUId())){
					RoomInfoModelWarpper tempRoom = RoomInfosData.getInstance().getRoomInfo(room.getUId());
					if (null != tempRoom) {
						ContactModel model = new ContactModel();
						model.setGroupType(Contact_group_type.MULTIPLE);
						model.setmContactName(room.getName());
						model.setmUserId(room.getUId());
						if(null != model){
							mGroupList.add(model);
						}
					}
				}
			}
		}
	}
}
