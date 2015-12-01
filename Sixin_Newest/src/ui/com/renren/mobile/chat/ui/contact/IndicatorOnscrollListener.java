package com.renren.mobile.chat.ui.contact;

import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;

/**
 * 
 * @author eason Lee
 *
 */
public class IndicatorOnscrollListener implements OnScrollListener {

	private ListView mListView;
	
	public IndicatorOnscrollListener(
			ListView mListView) {
		super();
		this.mListView = mListView;
	}
	
	
	
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {

	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		for (int i = 0; i < visibleItemCount; i++) { 
			if(mListView.getAdapter().getItemViewType(firstVisibleItem + i)==ContactModel.Contact_View_Type.TYPE_SEPARATOR){
				ContactListView.positionTitleInScreem = firstVisibleItem + i;
				return;
			}
		}
		ContactListView.positionTitleInScreem = -1;
	}

}
