package com.renren.mobile.chat.ui.groupchat;

import com.renren.mobile.chat.R;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;


/**
 * 
 * @author eason Lee 
 *	@update update by dingwei.chen 2012-5-23
 */
public final class D_LetterBar extends LinearLayout{
	
	private static final char UNDEFINE = '*';

    public static final char CHAR_SEARCH = '~';
	public static final char CHAR_GROUP='@';
	public static final char CHAR_OTHER='#';
	public static final int  INDEX_A =1; //A在数组中的位置
	public static final int  INDEX_GROUP_CHAR=0;//*在数组中的位置
	public static final char[] TAB_INDEX = {
             CHAR_SEARCH,
		     CHAR_GROUP,'A', 'B', 'C', 'D', 
						'E', 'F', 'G', 'H',
						'I', 'J', 'K', 'L',
						'M', 'N', 'O', 'P', 
						'Q', 'R', 'S', 'T', 
						'U', 'V', 'W', 'X', 
						'Y', 'Z' ,CHAR_OTHER
						};
	public static final int  INDEX_OTHER_CHAR=TAB_INDEX.length-1;
	
	private int mChoose = -1;
	/**是不是群聊选择联系人界面，如果是的话不能显示 {@link #CHAR_GROUP} */
	private boolean mIsChooseContacts;
	
	public D_LetterBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.setOrientation(LinearLayout.VERTICAL);
		this.setGravity(Gravity.CENTER);
		this.getBackground().setAlpha(0);
		initView();
	}

    public void setmIsChooseContacts(boolean isChooseContacts) {
        mIsChooseContacts = isChooseContacts;
        removeAllViews();
        initView();
    }

	protected void initView() {
		//Logd.error("id==="+this.getId());
//		if(this.getId()== R.id.letter_bar){
//			mIsChooseContacts=true;
//		}else{
//			mIsChooseContacts=false;
//		}
		boolean skip_char_group=mIsChooseContacts;
		for(char c:TAB_INDEX) {
            if(skip_char_group && c == CHAR_GROUP){//选择联系人界面 跳过@
                skip_char_group=false;
                continue;
            }

			LetterTextView tv = new LetterTextView(getContext());
            if(c == CHAR_SEARCH) {
                tv.setBackgroundResource(R.drawable.s_search_icon);
            } else {
			    tv.setText(c+"");
            }
			addView(tv, new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT, 1.0f));
		}
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
			//Logd.log("textview ontouchEvent ="+event.getAction());
			if(null != this.getText() && null != mListener) {
				char c;
                if(!"".equals(this.getText())) {
                    c = this.getText().charAt(0);
                } else {
                    c = CHAR_SEARCH;
                }
				//Logd.log("c="+c);
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
		//Logd.log("c lettervar c="+c);
		if(mListener!=null){
			//Logd.log("mlisterner");
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
		//Logd.log("c_letterbar ontouchEvent ="+event.getAction());
		 float y = event.getY();
		 int tmpLen = TAB_INDEX.length;
		 if(mIsChooseContacts){
			 tmpLen--;
		 }
		 int index = (int) (y/getHeight()*tmpLen);
		 //Logd.log("index ="+index);
		 //SystemUtil.log("cdw", "action = "+event.getAction());
		 switch(event.getAction()) {
		    case MotionEvent.ACTION_DOWN:
		    case MotionEvent.ACTION_MOVE:
		    	//SystemUtil.log("cdw", "onActionDown Or MOVEW");
		    	//Logd.log("c_letterbar down or move");
		    	this.getBackground().setAlpha(255);
		    	break;
		    case MotionEvent.ACTION_UP:
		    	//SystemUtil.log("cdw", "onActionUp");
		    	//Logd.log("c_lettervar up");
		    	this.getBackground().setAlpha(0);
		    	break;
		  }
		  if(index>=tmpLen){
			  index = tmpLen-1;
		  }
		  if(index>=0 && index<this.getChildCount()) {
		    	getChildAt(index).onTouchEvent(event);
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
