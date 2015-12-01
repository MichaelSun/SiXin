package com.renren.mobile.chat.view;


import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;

public class ListViewScrollListener implements OnScrollListener,OnTouchListener{


	BaseAdapter adapter;

	public boolean flag = true;
	
//	private LoadMoreViewItem addMore;

	public ListViewScrollListener(BaseAdapter adapter) {

		this.adapter = adapter;
//		this.addMore = addMore;
	}
	
	

	public void onScrollStateChanged(AbsListView view, int scrollState) {

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

//	@Override
//	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//		if (addMore != null && addMore.getVisibility() == View.VISIBLE && !addMore.isLoading()
//				&& (firstVisibleItem + visibleItemCount == totalItemCount)) {
//			addMore.wasClick();
//		}
//	}
//
//	public void clear(){
//		adapter = null;
//		addMore = null;
//	}
	
	
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		return false;
	}



	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		
	}

}
