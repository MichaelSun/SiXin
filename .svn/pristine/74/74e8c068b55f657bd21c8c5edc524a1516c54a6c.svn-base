package com.common.emotion.view;

import java.util.LinkedList;
import java.util.List;
import com.core.util.DipUtil;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * 表情底部横向一级tab的容器
 * @author jiaxia
 *
 */
public class EmotionCheckGroup extends LinearLayout{

	int mIndex = 0;
	private static int mLastIndex = 0;
	List<EmotionCheckView> mList = new LinkedList<EmotionCheckView>();
	public EmotionCheckGroup(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.setOrientation(LinearLayout.HORIZONTAL);
	}
	/**
	 * 添加一个一级tab
	 * @param emotionView
	 */
	public void addEmotionCheck(EmotionCheckView emotionView){
		mList.add(emotionView);
		this.addView(emotionView);
		//默认状态下，第一个tab是被选中的状态
		if(mIndex==0){
			emotionView.check();
		}
		//TODO 这里会不会有问题?
		//emotionView.setHeight(DipUtil.calcFromDip(40));
		emotionView.onAddToGroup(this,mIndex++);
		//调整布局
		LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)emotionView.getLayoutParams();
		params.width = LayoutParams.FILL_PARENT;
		params.height = LayoutParams.FILL_PARENT;
		params.weight = 1;
		emotionView.setLayoutParams(params);
	}
	/**
	 * 选中第N个tab
	 * @param index tab的索引
	 */
	public void onCheckChange(int index){
		int i = 0;
		mLastIndex = index;
		for(EmotionCheckView view:mList){
			if(i==index){
				view.check();
				this.onCheck(view);
			}else{
				view.unCheck();
			}
			i++;
		}
	}
	
	private OnCheckListener mOnCheckListener = null;
	public void setOnCheckListener(OnCheckListener listener){
		this.mOnCheckListener = listener;
	}
	private void onCheck(EmotionCheckView view){
		if(this.mOnCheckListener!=null){
			this.mOnCheckListener.onCheck(view);
		}
	}
	
	
	public static interface OnCheckListener {
		public void onCheck(EmotionCheckView view);
	}
	/**
	 * 初始化选中状态。
	 */
	public void initCheck(){
		this.onCheckChange(mLastIndex);
	}
	

}
