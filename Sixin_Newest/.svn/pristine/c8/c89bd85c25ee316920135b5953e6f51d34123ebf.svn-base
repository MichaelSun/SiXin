package com.renren.mobile.chat.ui.setting;

import java.util.ArrayList;
import java.util.List;

import com.core.util.SystemService;
import com.core.util.ViewMapUtil;
import com.core.util.ViewMapping;
import com.renren.mobile.chat.R;
import com.renren.mobile.chat.base.util.SystemUtil;
import com.renren.mobile.chat.base.views.NotSynImageView;
import com.renren.mobile.chat.ui.contact.ContactModel;
import com.renren.mobile.chat.ui.contact.UserInfoActivity;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class BlacklistAdapter extends BaseAdapter {

	public class ViewHolder{
		@ViewMapping(ID=R.id.blacklist_listview_item_layout)
		public View mBlacklistItemLayout;
		
		@ViewMapping(ID=R.id.blacklist_name)
		public TextView mBlacklistName;
		
		@ViewMapping(ID=R.id.blacklist_head)
		public NotSynImageView mBlacklistHead;
	}
	
	private List<ContactModel> mData = new ArrayList<ContactModel>();
	
	private Context mContext;
	
	private ViewHolder mHolder;
	
	public BlacklistAdapter(Context context){
		mContext = context;
	}
	
	public void setAdapterData(List<ContactModel> data){
		mData.clear();
		if(data != null){
			mData.addAll(data);
		}
		this.notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mData.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mData.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ContactModel model = mData.get(position);
		
		if(convertView==null){
			mHolder = new ViewHolder();
			convertView = SystemService.sInflaterManager.inflate(R.layout.f_blacklist_item, null);
			ViewMapUtil.getUtil().viewMapping(mHolder, convertView);
			convertView.setTag(mHolder);
		}
		mHolder = (ViewHolder)convertView.getTag();
		
		mHolder.mBlacklistName.setText(model.getmContactName());
		mHolder.mBlacklistHead.clear();
		mHolder.mBlacklistHead.addUrl(model.getHeadUrl());
		
		mHolder.mBlacklistItemLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				SystemUtil.f_log("================================11111111111111");
				//UserInfoActivity.show(mContext, model);
				UserInfoActivity.show(mContext, model.getmUserId(),model.getDomain());
			}
		});
		
		return convertView;
	}

}
