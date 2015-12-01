package com.common.emotion.view;

import com.core.util.CommonUtil;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;


/**
 * @author dingwei.chen
 * */
public class PluginGroup extends AbstractPluginGroup{

	private float mStartX = 0;
	private float mPreX = 0;
	private int mCurrentPage = 0;//0,1,2
	private int mOldPageSize = 0;
	
	private DataObserver mData = new DataObserver(){
		@Override
		public void dataChange( boolean load) {
			CommonUtil.log("load", "dataChange load:"+load);
			if(load){ //显示View
				onDataChange(load);
				postInvalidate();
			}
			
		}
	};
	public PluginGroup(Context context, AttributeSet attrs) {
		super(context, attrs);
	}


	
	@Override
	public void onDown(float x, float y) {
		mStartX = x;
		mPreX = mStartX;
	}

	@Override
	public void onMove(float x, float y) {
		float offsetX = mPreX - x;
		mPreX = x;
		this.scrollBy((int)offsetX, 0);
	}

	@Override
	public void onUp(float x, float y) {
		
	}

	@Override
	public void onMovePrePage(float x) {
		mIsBack = false;
		int offsetX = (int)((mCurrentPage)*mWidth-x);
		// TODO 应该根据位移量来确定时间，有个成熟的算法
		CommonUtil.log("delta"," delta per "+(Math.abs(getScrollX())-(offsetX-mWidth)));
		mScroller.startScroll(getScrollX(), 0, offsetX-mWidth, 0,500);
		hListener.onShowPreviousPointer();
	}

	@Override
	public void onMoveNextPage(float x) {
		mIsBack = false;
		int offsetX = (int)((mCurrentPage+1)*mWidth-x);
		CommonUtil.log("delta"," delta next "+(Math.abs(getScrollX())-(offsetX-mWidth)));
		mScroller.startScroll(getScrollX(), 0, offsetX, 0,500);
		hListener.onShowNextPointer();
	}

	@Override
	public void onMoveOver(boolean isNextPage) {
		if(!mIsBack){
			if(isNextPage){
				mCurrentPage++;
			}else{
				mCurrentPage--;
			}
			this.onPageChange();
			mIsBack = false;
		}
		
	}

	@Override
	public int onGetMinOffsetWidth() {
		return getWidth()>>1;
	}
	private boolean mIsBack = false;
	@Override
	protected void onBackOldPosition(float x, DIR dir,float fingermove) {
		mIsBack = true;
		int offset = (int)((mCurrentPage+1)*mWidth-x)-mWidth;
		this.move((int)x, offset);
	}

	@Override
	protected int getCurrentPage() {
		return mCurrentPage;
	}

	@Override
	protected int getPageCount() {
		//return this.getChildCount();
		if(this.mAdapter ==null)
			return 0;
		int count = this.mAdapter.getCount();
		return ((count-1)/mMaxCount)+1;
	}
	
	public abstract static class AbstractPluginAdapter {
		DataObserver mObserver = null;
		public abstract int getCount();
		public abstract View getView(int position,boolean load);
		public void notifyDataChange(boolean load){
			if(mObserver!=null){
				mObserver.dataChange(load);
			}
		}
		public void setObserver(DataObserver observer){
			mObserver = observer;
		}
	}
	public static abstract class DataObserver{
		public abstract void dataChange(boolean load);
	}
	AbstractPluginAdapter mAdapter;
	public void setAdapter(AbstractPluginAdapter adapter,boolean load){
		if(adapter!=null){
			mAdapter = adapter;
			mAdapter.setObserver(mData);
			mAdapter.notifyDataChange(load);
		}
	}
	
	/**
	 * @param page 0-N
	 * */
	public void setPage(int page){
		if(page>=getPageCount()){
			page = getPageCount()-1;
		}
		if(page<0){
			page = 0;
		}
		if(page!=getCurrentPage()){
			int number = page-getCurrentPage();
			this.move(getScrollX(), number*mWidth);
			mCurrentPage = page;
			mIsBack = true;
			onPageChange();
		}
	}
	public int getPageSize(){
		if(mAdapter!=null){
			return ((mAdapter.getCount()-1)/PAGE_MAX_NUMBER)+1;
		}else{
			return 0;
		}
	}
	
	public void onDataChange(boolean load){
		this.setVisibility(View.VISIBLE);
		this.recycle();// don't new PluginPager EveryTime;
		if(mAdapter!=null && mAdapter.getCount()>0){
			int count = this.mAdapter.getCount();
			int pageSize = ((count-1)/mMaxCount)+1;
			this.obtainPagers(pageSize);
			int pageIndex = 0;
			while(pageIndex<pageSize){
				PluginPager pager = mPagers.get(pageIndex);
				pager.removeAllViews();
				int offPage = pageIndex*mMaxCount;
				int index = offPage;
				while(index<count&&index<(offPage+mMaxCount)){
					pager.addView(mAdapter.getView(index,load));
					index++;
				}
				pageIndex++;
			}
			if(this.mOldPageSize>pageSize){
//				SystemUtil.log("cdw", "old page size more than new pagesize");
				if(mCurrentPage==this.mOldPageSize-1){
					this.setPage(pageSize-1);
				}
			}
			if(this.mOldPageSize!=pageSize){
				onPageSizeChange();
			}
			this.mOldPageSize = pageSize;
			
			
		}else{
//			SystemUtil.log("cdw", "adapter is null");
			this.setVisibility(View.GONE);
		}
	}
	
	
	

}
