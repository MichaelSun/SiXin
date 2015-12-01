package com.renren.mobile.chat.ui.contact;


import android.os.Handler;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ListView;
/**
 * 
 * @author eason Lee
 *
 */

public class ContactOnscrollListener extends IndicatorOnscrollListener {
	

	BaseAdapter adapter;

	public boolean flag = true;
	
//	private LoadMoreViewItem addMore;

	public ContactOnscrollListener(ListView mListView,BaseAdapter adapter) {
		super(mListView);
		
		this.adapter = adapter;
//		this.addMore = addMore;
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		//辅助完成Indicator功能
		super.onScrollStateChanged(view, scrollState);  
		//辅助完成加载头像功能
		switch (scrollState) {
		case 0://停下来
			this.flag =true;
			new Handler().post(new Runnable() {
				public void run() {
					adapter.notifyDataSetChanged();
				}
			});
			break;
		case 1://手指按下时候滑动
			break;
		case 2://滚动
			this.flag =false;
			break;
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		//辅助完成Indicator功能
		super.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
//		//辅助完成加载头像功能
//		if (addMore != null && addMore.getVisibility() == View.VISIBLE && !addMore.isLoading()
//				&& (firstVisibleItem + visibleItemCount == totalItemCount)) {
//			addMore.wasClick();
//		}
	}
	
	public void clear(){
		adapter = null;
//		addMore = null;
	}

}
