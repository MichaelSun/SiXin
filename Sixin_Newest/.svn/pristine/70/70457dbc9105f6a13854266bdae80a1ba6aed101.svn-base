package view.bar;

import java.util.ArrayList;
import java.util.List;

import view.plugin.AbstractPluginGroup.OnPagerListenner;
import view.plugin.PluginGroup;
import view.plugin.PluginGroup.AbstractPluginAdapter;
import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.common.emotion.view.EmotionView;
import com.core.util.CommonUtil;
import com.renren.mobile.chat.R;
import com.renren.mobile.chat.activity.ThreadPool;
import com.renren.mobile.chat.base.util.SystemUtil;

/**
 * @author dingwei.chen
 * */
public abstract class AbstractInputBar extends LinearLayout  {

	LayoutInflater mInflater = null;
	InputMethodManager mImm = null;
	View mView_Emotion ;
	View mView_KeyBoard ;
	View mView_Plugins;
	View mView_SendButton;
	View mView_Send;
	public EmotionEditText mView_TextEdit;
	View mView_VoiceInputButton;
	View mView_TextInputButton;
	public View mView_VoiceRecord;
	public EmotionView mView_EmotionsGroup;
	PluginGroup mView_PluginGroup ;
	View mView_PluginViewGroup;
	View mGroup ;
	int MAX_TOP = 0;
	public static final Handler HANDLER = new Handler();
	public AbstractInputBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mInflater.inflate(R.layout.inputbar, this);
		mImm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
		this.setBackgroundResource(R.drawable.inputbar_background);
		this.setOrientation(LinearLayout.VERTICAL);
		this.initViews();
		this.initMonitor();
		this.initVisibleMonitor();
		this.initListener();
	}

	private void initListener(){
		mView_Send.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String message = mView_TextEdit.getText()!=null?""+mView_TextEdit.getText():null;
				if(message!=null&& message.trim().length()>0){
					onSend(message);
				}
			}
		});
	}
	
	protected abstract void initVisibleMonitor();
	
	private void initViews(){
		mView_Emotion 			 = this.findViewById(R.id.inputbar_emotions);
		mView_KeyBoard 			 = this.findViewById(R.id.inputbar_keyboard);
		mView_Plugins 			 = this.findViewById(R.id.inputbar_plugins);
		mView_TextEdit 			 = (EmotionEditText)this.findViewById(R.id.inputbar_textedit);
		mView_VoiceRecord 		 = this.findViewById(R.id.inputbar_record);
		mView_VoiceInputButton 	 = this.findViewById(R.id.inputbar_voiceinput);
		mView_TextInputButton	 = this.findViewById(R.id.inputbar_textinput);
		mView_EmotionsGroup  	 = (EmotionView)this.findViewById(R.id.inputbar_emotions_group);
		mView_PluginGroup 		 = (PluginGroup)this.findViewById(R.id.inputbar_plugin_group);
		mView_PluginViewGroup	 = this.findViewById(R.id.inputbar_plugin_viewgroup);
		mView_SendButton 		 = this.findViewById(R.id.inputbar_textsend);
		mView_Send		 		 = this.findViewById(R.id.inputbar_send);
		mGroup 					 = this.findViewById(R.id.inputbar_groups);
		mView_TextEdit.addTextChangedListener(new TextWatcher() {
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				int pre = mView_TextEdit.getPreTextNumber();
				int number = s!=null?s.toString().trim().length():0;
				if(number>0){
					if(mView_TextEdit.getVisibility()==View.VISIBLE){
						SystemUtil.log("visible", "mView_SendButton="+mView_SendButton);
						mView_SendButton.setVisibility(View.VISIBLE);
						mView_VoiceInputButton.setVisibility(View.GONE);
					}
					if(pre==0){
						onTyping();
					}
				}else{
					mView_SendButton.setVisibility(View.GONE);
					mView_VoiceInputButton.setVisibility(View.VISIBLE);
					if(number==0&&pre>0){
						if(mView_TextEdit.isNotify()){
							onTypingCancel();
						}
					}
				}
				mView_TextEdit.setTextNumber(number);
			}
			public void beforeTextChanged(CharSequence s, int start, int count,int after) {}
			@Override
			public void afterTextChanged(Editable s) {}
		});
		
	}
	protected  boolean isVisible(View view){
		return view.getVisibility()==View.VISIBLE;
	}
	
	public void setAdapter(AbstractPluginAdapter adapter){
		mView_PluginGroup.setAdapter(adapter);
	}
	
	public void setOnPagerListenner(OnPagerListenner listener){
		mView_PluginGroup.setOnPagerListenner(listener);
	}
	
	public abstract void initMonitor();
	
	protected void showKeyBoard(){
		mImm.showSoftInput(mView_TextEdit, InputMethodManager.SHOW_FORCED);
		mView_TextEdit.setFocusable(true);
		
	}
	public void hideKeyBoard(){
		mImm.hideSoftInputFromWindow(mView_TextEdit.getWindowToken(),0);
	}
	public boolean onBack(){
		return isVisible(mView_PluginViewGroup)||isVisible(mView_EmotionsGroup);
	}
	protected abstract void onKeyBoardShow(int height);
	private int mMax = 0;
	int mOldTop = 0;
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		this.mWidthMeasureSpec = widthMeasureSpec;
		this.mHeightMeasureSpec = heightMeasureSpec;
	};
	private int mWidthMeasureSpec = 0;
	private int mHeightMeasureSpec = 0;
	private boolean mIsKeyboardHide = true;
	protected abstract void onKeyBoardHide();
	@Override
	protected void onLayout(boolean changed,final int l, int t,final int r, int b) {
			int height = getRootViewHeight();
			if(mMax<=height){
				mMax = height;
				if(!mIsKeyboardHide){
					onKeyBoardHide();
					mIsKeyboardHide = true;
				}
			}else{//keyboard show
				if(height!=mOldTop){
					onKeyBoardShow(mOldTop-height);
					mIsKeyboardHide = false;
					mOldTop = height;
					this.onMeasure(mWidthMeasureSpec, mHeightMeasureSpec);
					this.onChangeLayout(mWidthMeasureSpec,mHeightMeasureSpec);
					return;
				}
			}
			mOldTop = height;
		super.onLayout(changed, l, t, r, b);
	}
	public void onChangeLayout(int w,int h){
		if(mLayoutListener!=null){
			mLayoutListener.onChangeLayout(w,h);
		}
	}
	public static interface OnLayoutChangeListener{
		public void onChangeLayout( int w,int h);
		public View getRootView();
	}
	OnLayoutChangeListener mLayoutListener;
	public void setOnLayoutChangeListener(OnLayoutChangeListener listener){
		mLayoutListener = listener;
	}
	public int getRootViewHeight(){
		if(mLayoutListener!=null){
			View view = this.mLayoutListener.getRootView();
			if(view!=null){
				return view.getHeight();
			}
		}
		if(this.getContext() instanceof Activity){
			Window window = (Window)((Activity)this.getContext()).getWindow();
			ViewGroup group = (ViewGroup)window.getDecorView();
			return group.getBottom();
		}
		return 0;
	}
	public static interface OnSendListener{
		public void onSend(String message);
	}
	
	private OnSendListener mSendListener = null;
	public void setOnSendListener(OnSendListener listener){
		this.mSendListener = listener;
	}
	public void onSend(String message){
		if(mSendListener!=null){
			mSendListener.onSend(message);
		}
	}
	public String getText(){
		return mView_TextEdit.getText()+"";
	}
	public void setText(String text){
		this.mView_TextEdit.setText("");
	}
	public static interface OnTypingListener{
		public void onTyping();
		public void onTypingCancel();
	}
	OnTypingListener mTypingListener = null;
	public void setOnTypingListener(OnTypingListener listener){
		mTypingListener = listener;
	}
	public void onTyping(){
		if(this.mTypingListener!=null){
			mTypingListener.onTyping();
		}
	}
	public void onTypingCancel(){
		if(this.mTypingListener!=null){
			mTypingListener.onTypingCancel();
		}
	}
	
	public static class RecordButton extends Button{

		String mTextDown;
		String mTextUp;
		public RecordButton(Context context, AttributeSet attrs) {
			super(context, attrs);
			TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.RecordButton);
			this.initArray(array);
			array.recycle();
		}
		void initArray(TypedArray array){
			mTextDown = array.getString(R.styleable.RecordButton_text_down);
			mTextUp = array.getString(R.styleable.RecordButton_text_up);
			this.setText(mTextUp);
		}
		
		
		@Override
		public boolean onTouchEvent(MotionEvent event) {
			if(event.getAction()==MotionEvent.ACTION_DOWN){
				this.setText(mTextDown);
			}
			if(event.getAction()==MotionEvent.ACTION_UP){
				this.setText(mTextUp);
			}
			return super.onTouchEvent(event);
		}
	}
	
	public void updateHit(int id){
		this.mView_TextEdit.setHint(id);
	}
}
