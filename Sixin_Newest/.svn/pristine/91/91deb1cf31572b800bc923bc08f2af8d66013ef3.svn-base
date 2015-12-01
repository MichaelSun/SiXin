package view.bar;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.core.util.CommonUtil;
import com.core.util.DipUtil;
import com.renren.mobile.chat.activity.ThreadPool;
import com.renren.mobile.chat.base.util.SystemUtil;

/**
 * @author dingwei.chen
 * */
public class InputBar extends AbstractInputBar{
	
	
	
	public InputBar(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	
	public void setVisible(final View...views){
		this.setVisible(true,views);
	}
	public void setVisible(boolean isRunMain,final View...views){
		if(isRunMain){
			ThreadPool.obtain().executeMainThread(new Runnable() {
				public void run() {
					for(View v:views){
						v.setVisibility(View.VISIBLE);
					}
					onViewShow();
				}
			},200);
		}else{
			for(View v:views){
				v.setVisibility(View.VISIBLE);
			}
		}
		
	}
	
	
	
	public void setGone(final View...views){
		this.setGone(true, views);
	}
	
	public void setGone(boolean isRunMain,final View...views){
		if(isRunMain){
			ThreadPool.obtain().executeMainThread(new Runnable() {
				public void run() {
					for(View v:views){
						v.setVisibility(View.GONE);
					}
				}
			});
		}else{
			for(View v:views){
				v.setVisibility(View.GONE);
				if(v==mView_TextInputButton){
					mView_TextInputButton.setVisibility(View.INVISIBLE);
				}
			}
		}
	}
	
	
	
	
	@Override
	public void initMonitor() {
		mView_TextInputButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				setVisible(false,mView_TextEdit,
						mView_VoiceInputButton);
				setGone(false,mView_VoiceRecord
					   ,mView_TextInputButton);
				setFocusable(true);
				mView_TextEdit.setFocusable(true);
				if(mView_TextEdit.getText().toString().trim().length()>0){
					mView_TextInputButton.setVisibility(View.GONE);
					mView_VoiceInputButton.setVisibility(View.GONE);
					mView_SendButton.setVisibility(View.VISIBLE);
				}
				mView_TextEdit.requestFocus();
				showKeyBoard();
			}
		});
		mView_VoiceInputButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				setGone(mView_PluginViewGroup,
						mView_EmotionsGroup);
				setGone(false,mView_TextEdit,
						mView_VoiceInputButton);
				setVisible(false,mView_VoiceRecord
					   ,mView_TextInputButton);
				setGone(false, mView_KeyBoard);
				setVisible(false, mView_Emotion);
				hideKeyBoard();
			}
		});
		
		mView_Emotion.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				hideKeyBoard();
				
				setGone(mView_PluginViewGroup);
				setGone(false, mView_Emotion);
				setVisible(mView_EmotionsGroup);
				mView_EmotionsGroup.openEmotionView();
				setVisible(false, mView_KeyBoard,mView_TextEdit);
				setGone(false, mView_VoiceRecord);
				if(mView_TextEdit.getText().toString().trim().length()>0){
					setVisible(false, mView_SendButton);
					setGone(false, mView_VoiceInputButton);
					setGone(false, mView_TextInputButton);
				}else{
					setVisible(false, mView_VoiceInputButton);
					setGone(false, mView_TextInputButton);
				}
			}
		});
		mView_KeyBoard.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showKeyBoard();
				setGone(mView_PluginViewGroup,
						mView_EmotionsGroup);
				setVisible(false, mView_Emotion);
				setGone(false, mView_KeyBoard);
			}
		});
		mView_Plugins.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(mView_PluginViewGroup.getVisibility()==View.GONE){
					hideKeyBoard();
					setVisible(mView_PluginViewGroup);
					setVisible(false, mView_Emotion);
					setGone(mView_EmotionsGroup);
					setGone(false, mView_KeyBoard);
					onViewShow();
				}else{
					setGone(false, mView_PluginViewGroup);
				}
			}
		});
		
	}

	@Override
	protected void initVisibleMonitor() {}
	
	public static interface OnViewShowListener{
		public void onViewShow();
	}
	OnViewShowListener mShowListener;
	public void setOnViewShowListener(OnViewShowListener listener){
		this.mShowListener = listener;
	}
	public void onViewShow(){
		if(mShowListener!=null){
			mShowListener.onViewShow();
		}
	}
	
	
	
	public boolean onBack(){
		boolean flag = super.onBack();
		this.setGone(false,mView_PluginViewGroup,mView_EmotionsGroup);
		this.setGone(false,mView_KeyBoard);
		this.setVisible(false, mView_Emotion);
		
		return flag;
	}
	int mHeight = 0;
	@Override
	protected void onKeyBoardShow(int height) {
		SystemUtil.log("inputmethod", "height = "+height);
		if(height>mHeight){
			mHeight = height;
		}
		if(height>0){
			setHeight(mHeight, this.mView_EmotionsGroup,this.mView_PluginGroup);
		}
		onBack();
		if(mListener!=null){
			mListener.onKeyBoardShow();
		}
	}
	public void setHeight(int height,View...views){
		for(View v:views){
			android.view.ViewGroup.LayoutParams params = v.getLayoutParams();
			if(params!=null){
				params.height = height;
				v.setLayoutParams(params);
			}
		}
	}
	
	
	
	
	@Override
	protected void onKeyBoardHide(){}
	public static interface OnKeyBoardShowListener{
		public void onKeyBoardShow();
	}
	
	OnKeyBoardShowListener mListener;
	public void setOnKeyBoardShowListener(OnKeyBoardShowListener listener){
		mListener = listener;
	}
	
	
	
	

	
	
	
}
