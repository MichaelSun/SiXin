package com.renren.mobile.chat.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Vector;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.core.json.JsonObject;

/**
 * @author tian.wang
 * */
public class RBaseAdapter extends BaseAdapter {
	protected Vector<Object> dataList = new Vector<Object>();
	protected Activity activity;
	protected View header;
	protected View footer;
	protected LayoutInflater mInflater;

	public RBaseAdapter(ArrayList<Object> data, View header, View footer, Activity activity, ListView listView) {
		this.activity = activity;
		dataList.clear();
		if (null != data) {
			dataList.addAll(data);
		}
		mInflater = (LayoutInflater) this.activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if (header != null) {
			this.header = header;
			listView.addHeaderView(header);
			listView.setHeaderDividersEnabled(false);
		}
		if (footer != null) {
			this.footer = footer;
			listView.addFooterView(footer);
			listView.setFooterDividersEnabled(false);
		}

	}

	public void removeAll() {
		dataList.removeAllElements();
	}

	public void clear() {
		dataList.clear();
		notifyDataSetChanged();
	}
	
	public Vector<Object> getData () {
		return dataList;
	}

	public void addList(ArrayList<Object> data) {
		dataList.addAll(data);
		updateDataList();
		notifyDataSetChanged();
	}
	
	public void addLinkList(LinkedList<Object> data) {
		dataList.addAll(data);
		updateDataList();
		notifyDataSetChanged();
	}

	
	
	public void addVector(Vector<JsonObject> data) {
		final ArrayList<Object> list = new ArrayList<Object>();
		for (Object jo : data) {
			list.add(jo);
		}
		addList(list);
	}

	public void removeAll(Collection<?> collection) {
		dataList.removeAll(collection);
		notifyDataSetChanged();
	}

	synchronized public void update(ArrayList<Object> data) {
		dataList.clear();
		dataList.addAll(data);
		updateDataList();
		notifyDataSetChanged();
	}

	public void updateList() {
		notifyDataSetChanged();
	}

	public void removeItem(Object data) {
		dataList.remove(data);
		updateDataList();
		notifyDataSetChanged();
	}

	public void addItem(Object data) {
		dataList.add(data);
		updateDataList();
		notifyDataSetChanged();
	}

	public void addItem(Object data, int position) {
		dataList.add(position, data);
		updateDataList();
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return dataList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return null;
	}

	protected void setTextViewContent(TextView view, String content) {
		view.setVisibility(View.VISIBLE);
		view.setText(content);
	}

	protected void setImageViewContent(ImageView view, int id) {
		view.setVisibility(View.VISIBLE);
		view.setImageResource(id);
	}

	protected void setHeadImageClickListener(ImageView view, final long uid, final int type, final String name) {
//		view.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				ProfileActivity.show(activity, uid, type, name);
//			}
//		});
	}
	
	
	/**
	 * 此方法主要是用于增加数据的时候更新列表顺序
	 * */
	protected void updateDataList(){
		
	}

}
