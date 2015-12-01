package com.renren.mobile.chat.ui.setting;

import java.util.ArrayList;
import java.util.List;

import com.core.util.SystemService;
import com.core.util.ViewMapUtil;
import com.core.util.ViewMapping;
import com.renren.mobile.chat.R;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class SelectCollegeAdapter extends BaseAdapter {

	public class ViewHolder{
		@ViewMapping(ID=R.id.college_name)
		public TextView mCollegeName;
	}
	
	private List<String> mData = new ArrayList<String>();
	
	private Context mContext;
	
	private ViewHolder mHolder;
	
	public SelectCollegeAdapter(Context context){
		mContext = context;
	}
	
	public void setAdapterData(List<String> data){
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
		if(convertView==null){
			mHolder = new ViewHolder();
			convertView = SystemService.sInflaterManager.inflate(R.layout.f_select_college_item, null);
			ViewMapUtil.getUtil().viewMapping(mHolder, convertView);
			convertView.setTag(mHolder);
		}
		mHolder = (ViewHolder)convertView.getTag();
		
		mHolder.mCollegeName.setText(getItem(position).toString());
		return convertView;
	}

}
