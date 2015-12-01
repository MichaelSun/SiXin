package com.common.emotion.view;

import com.common.R;
import com.common.app.AbstractRenrenApplication;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
/**
 * 一级tab的一个可点击的tab页，实际上就是一个button
 * @author jiaxia
 */
public class EmotionCheckView extends Button{
	//下面的这两个颜色是文字的颜色
	public static final int TEXT_COLOR = 0xffDADADA;
//	public static final int DEFAULT_UNSELECTED = R.drawable.emotioncheckviewbg_unselected;
//	public static final int DEFAULT_SELECTED = R.drawable.emotioncheckviewbg_selected;
	private int bg_selected ;
	private int bg_unselected ;
	private int mIndex = -1;
	private EmotionCheckGroup mGroup = null;
	
	public EmotionCheckView(Context context,AttributeSet set){
		super(context,set);
		this.setOnClickListener(new ClickImpl());
	}
	/**
	 * 设置文字或者图片，默认无文字正常图片
	 * @param title 文字,null为留空
	 * @param unCheckBackgroundId 未选中bg,大于0为有效
	 * @param checkBackgroundId 选中bg,大于0为有效
	 */
	public void setMessage(String title,int unCheckBackgroundId,int checkBackgroundId){
		if(title!=null){
			this.setText(title);
		}else{
			this.setText("");
		}
		if(unCheckBackgroundId>0){
			bg_unselected = unCheckBackgroundId;
		}
		if(checkBackgroundId>0){
			bg_selected = checkBackgroundId;
		}
		
	}
	/**
	 * 设置Tab的名称，用于国际化
	 * @param name
	 */
	public void setTabName(String name){
		this.setText(name);
	}
	/**
	 * 
	 * @param showBitmapId  背景图上的原图
	 * @param unCheckBackgroundId   非点击下的背景图
	 * @param checkBackgroundId  点击下的背景图
	 */
	public void setMessage(int showBitmapId,int unCheckBackgroundId,int checkBackgroundId){
		if(showBitmapId>0){
			ImageSpan span = new ImageSpan(AbstractRenrenApplication
					.getAppContext(), showBitmapId);
			SpannableStringBuilder builder = new SpannableStringBuilder();
			builder.append("gen");
			builder.setSpan(span, 0, 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			this.setText(builder);
		}
		if(unCheckBackgroundId>0){
			bg_unselected = unCheckBackgroundId;
		}
		if(checkBackgroundId>0){
			bg_selected = checkBackgroundId;
		}
	}
	/**
	 * 选中
	 */
	public void check(){
		this.setTextColor(TEXT_COLOR);
		
		this.setBackgroundResource(bg_selected);
	}
	/**
	 * 未选中
	 */
	public void unCheck(){
		this.setTextColor(TEXT_COLOR);
		this.setBackgroundResource(bg_unselected);
	}
	public void onAddToGroup(EmotionCheckGroup group,int index){
		this.mGroup = group;
		this.mIndex = index;
	}
	
	public class ClickImpl implements OnClickListener{
		public void onClick(View v) {
			if(mGroup!=null && mIndex!=-1){
				mGroup.onCheckChange(mIndex);
			}
		}
	}
	
	
}
