package com.renren.mobile.chat.view;

import java.util.HashMap;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.core.util.CommonUtil;
import com.renren.mobile.chat.R;
import com.renren.mobile.chat.RenrenChatApplication;

/**
 * title默认显示左侧返回button以及中间的标题，其他需要自己设置可见性
 * */
public class BaseTitleLayout {
	private Context mContext;
	private FrameLayout mTitleLayout;// 主layout
	protected Button mTitleButtonLeftBack;// 返回
	protected Button mTitleButtonLeftOther;
	protected Button mRightOtherButton;
	protected TextView mTitleMiddleTextView;// title主题
	protected View mTitleMiddleTextIcon;// title主题
	protected Button mTitleRefreshButton;// 刷新
	protected Button mTitleFunctionButton;// 功能键
	protected FrameLayout mRightLayout;
	protected ProgressBar mRefreshProgress;
	
	protected LinearLayout mTitleStateLayout;
	protected LinearLayout mTitleMiddleLayout;
	
	protected View mTitleMiddleStateImg;
	protected TextView mTitleMiddleStateText;
	protected LinearLayout mContactTitleLayout;
	
	public interface FUNCTION_BUTTON_TYPE {
		int SETTING = 7;
		int ROOM_INFO = 8;//房间信息
		int PLUGIN = 9;//插件
		int SHARE = 10;//分享
		int REFRESH = 11; //刷新
		int MORE = 12; //更多
		int MULTICHAT = 13; //群聊
		int RIGHT = 14;	//对勾
		int ERROR = 15; //叉
		int ADDCONTACT = 16;//添加联系人
		int ADDCONTACT_USER_INFO = 17;//联系人详情  三个点
		int SEND = 18;//发送按钮
		int CANCEL = 19;//取消按钮
		int RENREN_ICON = 20; //人人的Icon
	}
	private final static HashMap<Integer, Integer> sType2Drawable = new HashMap<Integer, Integer>();
	static{
		sType2Drawable.put(FUNCTION_BUTTON_TYPE.SETTING, 				R.drawable.cy_title_setting);
		sType2Drawable.put(FUNCTION_BUTTON_TYPE.ROOM_INFO, 				R.drawable.title_room_info_bg);
		sType2Drawable.put(FUNCTION_BUTTON_TYPE.PLUGIN, 				R.drawable.cy_title_plugin);
		sType2Drawable.put(FUNCTION_BUTTON_TYPE.SHARE, 					R.drawable.cy_title_share);
		sType2Drawable.put(FUNCTION_BUTTON_TYPE.REFRESH, 				R.drawable.cy_title_refresh);
		sType2Drawable.put(FUNCTION_BUTTON_TYPE.MORE, 					R.drawable.cy_title_more);
		sType2Drawable.put(FUNCTION_BUTTON_TYPE.MULTICHAT, 				R.drawable.cy_title_multichat);
		sType2Drawable.put(FUNCTION_BUTTON_TYPE.RIGHT, 					R.drawable.cy_title_right);
		sType2Drawable.put(FUNCTION_BUTTON_TYPE.ERROR, 					R.drawable.cy_title_error);
		sType2Drawable.put(FUNCTION_BUTTON_TYPE.ADDCONTACT, 			R.drawable.cy_title_addcontact);
		sType2Drawable.put(FUNCTION_BUTTON_TYPE.ADDCONTACT_USER_INFO, 	R.drawable.cy_title_user_info);
		sType2Drawable.put(FUNCTION_BUTTON_TYPE.SEND, 					R.drawable.blue_button);
		sType2Drawable.put(FUNCTION_BUTTON_TYPE.CANCEL, 				R.drawable.grey_button);
		sType2Drawable.put(FUNCTION_BUTTON_TYPE.RENREN_ICON, 			R.drawable.cy_renren_icon);
	}
	
	/** title取消按钮文本颜色变化 */
	public static ColorStateList LeftColorList = new ColorStateList(
			new int[][] {
					{ android.R.attr.state_selected}, {android.R.attr.state_pressed}, {android.R.attr.state_focused}, {} }, 
			new int[] 
					{ Color.WHITE, Color.WHITE, Color.WHITE, RenrenChatApplication.mContext.getResources().getColor(R.color.Color_A)});
	
	/** title确定按钮文本颜色变化 */
	public static ColorStateList RightColorList = new ColorStateList(
			new int[][] {
					{ android.R.attr.state_selected}, {android.R.attr.state_pressed}, {android.R.attr.state_focused}, {} }, 
			new int[] 
					{ Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE});

	public BaseTitleLayout(Context context) {
		this.mContext = context;
		LayoutInflater mInflater = LayoutInflater.from(this.mContext);
		mTitleLayout = (FrameLayout) mInflater.inflate(R.layout.base_title_layout, null);
		initTitle();
	}
	
	/**
	 * 在activity的xml文件里使用以下代码include进title
	 * <include android:id="@id/自定义id" layout="@layout/base_title_layout" />  
	 * findView作为参数view
	 * */
	public BaseTitleLayout(Context context,FrameLayout view) {
		this.mContext = context;
		mTitleLayout = view;
		initTitle();
	}
	
	private void initTitle(){
		mTitleButtonLeftBack = (Button) mTitleLayout.findViewById(R.id.title_left_button);
		mTitleMiddleTextView = (TextView) mTitleLayout.findViewById(R.id.title_mid_layout_text);
		mTitleMiddleTextIcon = mTitleLayout.findViewById(R.id.title_mid_text_icon);
		mRightLayout = (FrameLayout) mTitleLayout.findViewById(R.id.title_right_layout);
		mTitleRefreshButton = (Button) mTitleLayout.findViewById(R.id.title_right_refresh_button);
		mTitleFunctionButton = (Button) mTitleLayout.findViewById(R.id.title_right_function_button);
		mRefreshProgress = (ProgressBar) mTitleLayout.findViewById(R.id.title_right_refresh_progress);
		mTitleStateLayout = (LinearLayout) mTitleLayout.findViewById(R.id.title_mid_layout_state);
		mTitleMiddleLayout = (LinearLayout) mTitleLayout.findViewById(R.id.title_mid_layout);
		mTitleMiddleStateImg = mTitleLayout.findViewById(R.id.title_mid_layout_state_img);
		mTitleMiddleStateText = (TextView) mTitleLayout.findViewById(R.id.title_mid_layout_state_text);
		mContactTitleLayout = (LinearLayout) mTitleLayout.findViewById(R.id.contact_title_middle);
		mTitleButtonLeftOther = (Button) mTitleLayout.findViewById(R.id.title_left_button_other);
		mRightOtherButton =  (Button) mTitleLayout.findViewById(R.id.title_right_button_other);
		
	}

	 public LinearLayout getmContactTitleLayout() {
	        return mContactTitleLayout;
	    }
	 
	public ViewGroup getView() {
		return mTitleLayout;
	}

	public FrameLayout getTitleRight() {
		return mRightLayout;
	}

	public Button getTitleLeft() {
		return mTitleButtonLeftBack;
	}

	public TextView getTitleMiddle() {
		return mTitleMiddleTextView;
	}
	
	public void setTitleMiddleIcon(int id){
		mTitleMiddleTextIcon.setVisibility(View.VISIBLE);
		mTitleMiddleTextIcon.setBackgroundResource(id);
	}

	public Button getTitleRightRefreshButton() {
		return mTitleRefreshButton;
	}

	public Button getTitleRightFunctionButton() {
		return mTitleFunctionButton;
	}

	/**
	 * 设置title标题
	 * */
	public void setTitleMiddle(String content) {
		getTitleMiddle().setText(content);
	}
	
	/**
	 * 设置title标题的状态可见性
	 * */
	public void setTitleStateVisibility(boolean visibility){
		if (visibility) {
			mTitleStateLayout.setVisibility(View.VISIBLE);
		} else {
			mTitleStateLayout.setVisibility(View.GONE);
		}
	}
	
	public LinearLayout getMidTitleLayout(){
		return mTitleMiddleLayout;
	}
	
	/**
	 * 设置title标题的状态的内容
	 * */
	public void setTitleState(BaseTitleState state){
		int type = state == null?BaseTitleStateFactory.BaseTitleStateType.TYPE_UNKOWN:state.getmType();
//		if(state!=null){
			switch (type) {
			case BaseTitleStateFactory.BaseTitleStateType.TYPE_OFFLINE:
				mTitleStateLayout.setVisibility(View.VISIBLE);
				mTitleMiddleStateImg.setVisibility(View.GONE);
				mTitleMiddleStateText.setText(state.getmText());
				break;
			case BaseTitleStateFactory.BaseTitleStateType.TYPE_AUDIO_STATE:
				setTitleMiddle(state.getmText());
				break;
			case BaseTitleStateFactory.BaseTitleStateType.TYPE_UNKOWN:
				mTitleStateLayout.setVisibility(View.INVISIBLE);
				break;
			default:
				mTitleStateLayout.setVisibility(View.VISIBLE);
				mTitleMiddleStateImg.setBackgroundResource(state.getmImgId());
				mTitleMiddleStateImg.setVisibility(View.VISIBLE);
				mTitleMiddleStateText.setText(state.getmText());
				break;
			}
//		}
	}
	/**
	 * 设置title的左侧返回按钮可见性，默认可见
	 * */
	public void setTitleButtonLeftBackVisibility(boolean visibility) {
		if (visibility) {
			mTitleButtonLeftBack.setVisibility(View.VISIBLE);
		} else {
			mTitleButtonLeftBack.setVisibility(View.INVISIBLE);
		}
	}
	
	public void setTitleButtonLeftOtherVisibility(boolean visibility) {
		if (visibility) {
			mTitleButtonLeftOther.setVisibility(View.VISIBLE);
		} else {
			mTitleButtonLeftOther.setVisibility(View.INVISIBLE);
		}
	}

	/**
	 * 设置title的左侧返回按钮的点击事件
	 * */
	public void setTitleButtonLeftBackListener(OnClickListener onClickListener) {
		mTitleButtonLeftBack.setOnClickListener(onClickListener);
	}
	
	public void setTitleButtonLeftOtherListener(OnClickListener onClickListener) {
		mTitleButtonLeftOther.setOnClickListener(onClickListener);
	}

	/**
	 * 设置title刷新按钮的可见性
	 * */
	public void setTitleRefreshButtonVisibility(boolean visibility) {
		if (true == visibility) {
			mTitleRefreshButton.setVisibility(View.VISIBLE);
			mTitleFunctionButton.setVisibility(View.GONE);
		} else {
			mTitleRefreshButton.setVisibility(View.INVISIBLE);
			mTitleFunctionButton.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * 设置title刷新按钮的点击事件
	 * */
	public void setTitleRefreshButtonListener(View.OnClickListener listener) {
		mTitleRefreshButton.setOnClickListener(listener);
	}

	/**
	 * 设置title Progress按钮的可见性，刷新的时候设置可见，刷新完毕后设置不可见
	 * */
	public void setTitleRefreshProgressVisibility(boolean visibility) {
		if (visibility) {
			mRefreshProgress.setVisibility(View.VISIBLE);
			mTitleRefreshButton.setVisibility(View.GONE);
		} else {
			mTitleRefreshButton.setVisibility(View.VISIBLE);
			mRefreshProgress.setVisibility(View.GONE);
		}
	}

	/**
	 * 设置title右侧功能键的背景类型
	 * */
	public void setTitleFunctionButtonBackground(int type) {
		mTitleFunctionButton.setVisibility(View.VISIBLE);
		mTitleFunctionButton.setBackgroundResource(sType2Drawable.get(type));
	}
	
	public void setLeftButtonBackground(int type) {
		mTitleButtonLeftBack.setVisibility(View.GONE);
		mTitleButtonLeftOther.setVisibility(View.VISIBLE);
		mTitleButtonLeftOther.setBackgroundResource(sType2Drawable.get(type));
		mTitleButtonLeftOther.setTextColor(LeftColorList);
	}
	
	public void setLeftButtonEnableState(int type, boolean enableState) {
		if(enableState){
			mTitleButtonLeftOther.setBackgroundResource(sType2Drawable.get(type));
			mTitleButtonLeftOther.setTextColor(LeftColorList);
		}else{
			mTitleButtonLeftOther.setBackgroundResource(R.drawable.disable_state);
			mTitleButtonLeftOther.setTextColor(mContext.getResources().getColor(R.color.Color_E));
		}
	}
	
	public void setRightButtonBackground(int type) {
		mTitleFunctionButton.setVisibility(View.GONE);
		mRightOtherButton.setVisibility(View.VISIBLE);
		mRightOtherButton.setBackgroundResource(sType2Drawable.get(type));
		mRightOtherButton.setTextColor(RightColorList);
	}
	
	public void setRightButtonEnableState(int type, boolean enableState) {
		if(enableState){
			mRightOtherButton.setBackgroundResource(sType2Drawable.get(type));
			mRightOtherButton.setTextColor(RightColorList);
		}else{
			mRightOtherButton.setBackgroundResource(R.drawable.disable_state);
			mRightOtherButton.setTextColor(mContext.getResources().getColor(R.color.Color_E));
		}
	}
	
	public Button getLeftOtherBtn(){
		return mTitleButtonLeftOther;
	}
	
	public Button getRightOtherBtn(){
		return mRightOtherButton;
	}
	
	public void setButtonString(Button button, int id) {
		button.setVisibility(View.VISIBLE);
		button.setText(id);
		button.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
	}
	
	public void setTitleFunctionButtonClickable(boolean clickable){
		mTitleFunctionButton.setClickable(clickable);
	}
	
	
	/**
	 * 设置title左侧功能键的背景
	 */

	public void setTitleBackButtonBackground(int bgId) {
		mTitleFunctionButton.setVisibility(View.VISIBLE);
		mTitleButtonLeftBack.setBackgroundResource(bgId);
	}
	
	/**
	 * 设置title右侧功能键的名称
	 * */
	public void setTitleFunctionButtonText(String content) {
		mTitleFunctionButton.setText(content);
	}
	
	/**
	 * 设置title右侧功能键的回调
	 * */
	public void setTitleFunctionButtonListener(OnClickListener onClickListener) {
//		setTitleFunctionButtonVisibility(true);
		mTitleFunctionButton.setOnClickListener(onClickListener);
	}
	
	/**
	 * 设置title右侧区域的可见性
	 * */
	public void setTitleRightLayoutVisible(boolean visible){
		if(visible){
			mRightLayout.setVisibility(View.VISIBLE);
		}else{
			mRightLayout.setVisibility(View.INVISIBLE);
		}
	}

//	/**
//	 * 设置title右侧功能键的可见性
//	 * */
//	public void setTitleFunctionButtonVisibility(boolean visibility) {
//		if (true == visibility) {
//			mTitleFunctionButton.setVisibility(View.VISIBLE);
//		} else {
//			mTitleFunctionButton.setVisibility(View.GONE);
//			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//			params.setMargins(0, 0, 0, 0);
//			params.weight = 1.0f;
//			mTitleMiddleTextView.setLayoutParams(params);
//		}
//	}



	/**
	 * 设置title的回调
	 * */
	public void setTitleListener(OnClickListener onClickListener) {
		mTitleLayout.setOnClickListener(onClickListener);
	}

	/**
	 * 设置title的背景资源
	 * */
	public void setTitleBackGround(int resid) {
		mTitleLayout.setBackgroundResource(resid);
	}
	
	/**
	 * 设置title的左侧返回按钮的背景
	 * */
	public void setTitleButtonLeftOtherRenren() {
		mTitleButtonLeftOther.setBackgroundResource(R.drawable.cy_title_renren);
	}
	
}
