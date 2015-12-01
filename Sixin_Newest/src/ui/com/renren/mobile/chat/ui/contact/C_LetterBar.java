package com.renren.mobile.chat.ui.contact;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.renren.mobile.chat.R;
import com.renren.mobile.chat.base.util.SystemUtil;

public final class C_LetterBar extends LinearLayout{
	
	public static final char CHAR_SEARCH = '~';
	private static final char UNDEFINE = '*';
	public static final char CHAR_GROUP='@';
	public static final char CHAR_OTHER='#';
	public static final int  INDEX_A = 2; //A在数组中的位置
	public static final int  INDEX_GROUP_CHAR=1;//*在数组中的位置
	public static final int  INDEX_SEARCH_CHAR=0;//*在数组中的位置
	public static final char[] TAB_INDEX = {
		                CHAR_SEARCH,CHAR_GROUP,
		                'A', 'B', 'C', 'D', 
						'E', 'F', 'G', 'H',
						'I', 'J', 'K', 'L',
						'M', 'N', 'O', 'P', 
						'Q', 'R', 'S', 'T', 
						'U', 'V', 'W', 'X', 
						'Y', 'Z' ,
						CHAR_OTHER
						};
	public static final int  INDEX_OTHER_CHAR=TAB_INDEX.length-1;
	
	private int mChoose = -1;
	/**是不是群聊选择联系人界面，如果是的话不能显示 {@link #CHAR_GROUP} */
	//private boolean mIsChooseContacts;
	private byte showedNum;
	
	public C_LetterBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.setOrientation(LinearLayout.VERTICAL);
		this.setGravity(Gravity.CENTER);
		this.getBackground().setAlpha(0);
		//initView();
	}

	public void initView(int[] mFilter_Indexs) {
		//Logd.error("id==="+this.getId());
//		if(this.getId()== R.id.letter_bar){
//			mIsChooseContacts=true;
//		}else{
//			mIsChooseContacts=false;
//		}
		//boolean skip_char_group=mIsChooseContacts;
		this.removeAllViews();
		if(mFilter_Indexs==null){
			return;
		}
		showedNum=0;
		int len = TAB_INDEX.length;
		char c;
		for (int i = 0; i < len; i++) {
			c = TAB_INDEX[i];
			if(mFilter_Indexs[i] != -1){
				showedNum++;
				LetterTextView tv = new LetterTextView(getContext());
				if(c==CHAR_SEARCH){
					LinearLayout ll = new LinearLayout(C_LetterBar.this.getContext());
					tv.setBackgroundResource(R.drawable.contact_letter_bar_search);
					ll.addView(tv);
					addView(ll, new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT, 1.0f));
				}else{
					tv.setText(c+"");
					addView(tv, new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT, 1.0f));
				}
				
			}
		}
//		for(char c:TAB_INDEX) {
////			if(skip_char_group){//选择联系人界面 跳过@
////				skip_char_group=false;
////				continue;
////			}
//			LetterTextView tv = new LetterTextView(getContext());
//			tv.setText(c+"");
//			addView(tv, new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT, 1.0f));
//		}
	}
	
	class LetterTextView extends TextView {

		public LetterTextView(Context context) {
			super(context);
			this.setTextSize(10);
			this.setTextColor(Color.GRAY);
			this.setVisibility(View.VISIBLE);
			this.setBackgroundDrawable(null);
		}
		
		public boolean onTouchEvent(MotionEvent event) {
	        if(SystemUtil.mDebug){
	        	SystemUtil.logd("textview ontouchEvent ="+event.getAction());
			}
			if(null != this.getText() && null != mListener) {
				char c;
				if(!TextUtils.isEmpty((this.getText()))) {
                    c = this.getText().charAt(0);
                } else {
                    c = CHAR_SEARCH;
                }
				//char c = this.getText().charAt(0);
		        if(SystemUtil.mDebug){
		        	SystemUtil.logd("c="+c);
				}
				switch(event.getAction()) {
				case MotionEvent.ACTION_DOWN:
				case MotionEvent.ACTION_MOVE:
					if(c != mChoose) {
						mChoose = c;
						onSelect(c);
					}
					break;
				case MotionEvent.ACTION_UP:
					onUnSelect();
					break;
				}
			}
			return false;
		}
	}
	private void onSelect(char c){
        if(SystemUtil.mDebug){
        	SystemUtil.logd("c lettervar c="+c);
		}
		
		if(mListener!=null){
			 if(SystemUtil.mDebug){
				 SystemUtil.logd("mlisterner");
			 }
			mListener.onSelect(c);
		}
	}
	private void onUnSelect(){
		mChoose = UNDEFINE;
		//Logd.log("onunselect");
		if(mListener!=null){
			//Logd.log("mlistener onunselect"); 
			mListener.onUnSelect();
		}
	}
	@Override
	public boolean onTouchEvent(MotionEvent event) {
        if(SystemUtil.mDebug){
        	 SystemUtil.logd("c_letterbar ontouchEvent ="+event.getAction());
		}
		
		 float y = event.getY();
		// int tmpLen = TAB_INDEX.length;
		 int tmpLen = showedNum;
//		 if(mIsChooseContacts){
//			 tmpLen--;
//		 }
		 int index = (int) (y/getHeight()*tmpLen);
	      if(SystemUtil.mDebug){
	       	 SystemUtil.logd("index ="+index+"#showedNum="+showedNum);
			 SystemUtil.logd("action = "+event.getAction());
			}
		 switch(event.getAction()) {
		    case MotionEvent.ACTION_DOWN:
		    case MotionEvent.ACTION_MOVE:
		        if(SystemUtil.mDebug){
		        	SystemUtil.logd("c_letterbar Or MOVEW");
				}
		    	this.getBackground().setAlpha(255);
		    	break;
		    case MotionEvent.ACTION_UP:
		        if(SystemUtil.mDebug){
		        	SystemUtil.logd("c_lettervar up");
				}
		    	//Logd.log("c_lettervar up");
		    	this.getBackground().setAlpha(0);
		    	break;
		  }
		  if(index>=tmpLen){
			  index = tmpLen-1;
		  }
		  if(index>=0 && index<this.getChildCount()) {
		    	View view=getChildAt(index);
		    	if(view instanceof LinearLayout){
		    		view = ((LinearLayout) view).getChildAt(0);
		    	}
		    	if(view!=null){
		    		view.onTouchEvent(event);
		    	}
		    	
		  }else{
		    	if(event.getAction()==MotionEvent.ACTION_UP){
		    		onUnSelect();
		    	}
		  }
		  return true;
	};
	
	OnLetterSelectListener mListener = null;
	public void setOnLetterSelectListener(OnLetterSelectListener listener){
		mListener = listener;
	}
	
	/*字母选择监听*/
	public static interface OnLetterSelectListener{
		public void onSelect(char c);
		public void onUnSelect();
	}
	
}
