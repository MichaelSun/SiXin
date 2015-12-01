package view.list;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.core.util.CommonUtil;
import com.renren.mobile.chat.R;

/**
 * @author dingwei.chen
 * */
public class AbstractListView extends ListView {
	InputMethodManager mImm;
	public static final int HEADER_PADDING = 18;
	public static final int PROGRESS_WIDTH = 30;
	Handler mHandler = new Handler();
	LayoutInflater mInflater = null;
	public AbstractListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.init();
		
	}
	private void init(){
		mImm = (InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
		this.setCacheColorHint(0x00000000);
		this.setWillNotCacheDrawing(true);
		this.setDivider(null);
//		this.onLoadData();
	}

	protected void hideKeyBoard(){
		mImm.hideSoftInputFromWindow(this.getWindowToken(),0);
	}
	
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		this.hideKeyBoard();
		if(ev.getAction()==MotionEvent.ACTION_DOWN){
			onFingerTouch();
		}
		return super.dispatchTouchEvent(ev);
	}
	
	public void onFingerTouch(){
		if(mListener!=null){
			mListener.onFingerTouch();
		}
	}
	public void setOnFingerTouchListener(OnFingerTouchListener listener){
		this.mListener = listener;
	}
	OnFingerTouchListener mListener;
	public static interface OnFingerTouchListener{
		public void onFingerTouch();
	}
	
	
	
	
}
