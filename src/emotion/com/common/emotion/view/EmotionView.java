package com.common.emotion.view;

import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.common.R;
import com.common.app.AbstractRenrenApplication;
import com.common.emotion.config.EmotionConfig;
import com.common.emotion.emotion.EmotionRank;
import com.common.emotion.emotion.EmotionRankManager;
import com.common.emotion.view.EmotionCheckGroup.OnCheckListener;
import com.common.utils.Config;
import com.core.util.CommonUtil;


/**
 * @author jia.xia
 * @说明 表情控件
 * */
public class EmotionView extends LinearLayout{
	/**
	 * 一级tab的内容的容器
	 */
	private List<EmotionTab> mPagers = new LinkedList<EmotionTab>();
	/**
	 * 一级tab
	 */
	private EmotionCheckGroup mTabs_RadioGroup = null;
	/**
	 * pager的容器
	 */
	private LinearLayout mPagers_Linearlayout = null;
	private final int EMOTION_VIEW_ID = R.layout.emotion_view_new;
	private CheckListenerImpl mListener = new CheckListenerImpl();
	private static boolean isFirstLoad = true;
	
	public EmotionView(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(EMOTION_VIEW_ID, null);
		this.addView(view);
		mPagers_Linearlayout = (LinearLayout)view.findViewById(R.id.x_emotion_pagers);
		mTabs_RadioGroup = (EmotionCheckGroup)view.findViewById(R.id.x_emotion_tabs);
	}
	/**
	 * 插入Tab
	 * @param pager
	 */
	public void addTab(EmotionTab pager){
		if(!mPagers.contains(pager)){
//			if(mPagers.size()==0){
//				pager.setVisibility(View.VISIBLE);
//			}else{
				pager.setVisibility(View.GONE);
//			}
			mPagers.add(pager);
			LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			//初始化一级tab按钮
			EmotionCheckView checkTab =(EmotionCheckView)inflater.inflate(R.layout.emotion_tab_new, null);
			checkTab.setMessage(pager.getTabName(),pager.getBackgroundUncheckId(),pager.getBackgroundCheckId());
			//点击的时候要取得pager
			checkTab.setTag(pager);
			mTabs_RadioGroup.addEmotionCheck(checkTab);
			mTabs_RadioGroup.setOnCheckListener(mListener);
		}
	}
	/**
	 * 重置Tab名称，用于国际版国际化
	 */
	public void resetTabName(){
		if (Config.VERSION_CURRENT == Config.VERSION_INTERNATIONAL){ //国际
			if(null!=mPagers && mPagers.size()>0&&mTabs_RadioGroup!=null){
				List<EmotionCheckView>checkList = mTabs_RadioGroup.mList;
				if(checkList!=null){
					for(int k = 0;k<mPagers.size()&&k<checkList.size();k++){
						EmotionTab tab = mPagers.get(k);
						EmotionCheckView check = checkList.get(k);
						if(tab.emotion_style == EmotionConfig.EMOTION_SMALL_STYLE){ //Emoji
							check.setTabName(AbstractRenrenApplication.getAppResources().getString(R.string.emotion_emoji_en));
						}
						else{ //炫酷
							check.setTabName(AbstractRenrenApplication.getAppResources().getString(R.string.emotion_sticker_en));
						}
					}
				}
				
			}
		}
		
	}
	/**
	 * 添加附加的两个tab，包括互动和加号
	 * @author zhenning.yang
	 * @param title 文字标题
	 * @param unCheckBackgroundId 未选中背景的Id
	 * @param checkBackgroundId 选中的背景的Id
	 */
	public void addAdditionalTab(String title,int unCheckBackgroundId,int checkBackgroundId){
		LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflater.inflate(R.layout.emotion_additional1, null);
		TextView text =  (TextView)v.findViewById(R.id.emotion_additional_text);
		if(Config.VERSION_CURRENT == Config.VERSION_INTERNATIONAL){ //国际版文案（国内版默认）
			text.setText(R.string.emotion_more_emo_en);
		}else{
			text.setText(R.string.emotion_more_emo_ch);
		}
		EmotionCheckView checkTab =(EmotionCheckView)inflater.inflate(R.layout.emotion_tab_new, null);
		checkTab.setMessage(title, unCheckBackgroundId,checkBackgroundId);
		checkTab.setTag(v);
		mTabs_RadioGroup.addEmotionCheck(checkTab);
		mTabs_RadioGroup.setOnCheckListener(mListener);
		
	}
	
	/**
	 * 
	 * @param sourceId   
	 * @param unCheckBackgroundId
	 * @param checkBackgroundId
	 */
	public void addAdditionalTab(int sourceId,int unCheckBackgroundId,int checkBackgroundId){
		LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflater.inflate(R.layout.emotion_additional1, null);
		TextView text =  (TextView)v.findViewById(R.id.emotion_additional_text);
		if(Config.VERSION_CURRENT == Config.VERSION_INTERNATIONAL){ //国际版文案（国内版默认）
			text.setText(R.string.emotion_more_en);
		}
		EmotionCheckView checkTab =(EmotionCheckView)inflater.inflate(R.layout.emotion_tab_new, null);
		checkTab.setMessage(sourceId, unCheckBackgroundId,checkBackgroundId);
		checkTab.setTag(v);
		mTabs_RadioGroup.addEmotionCheck(checkTab);
		mTabs_RadioGroup.setOnCheckListener(mListener);
		
	}
	/**
	 * 打开EmotionView，当点击表情按钮时调用
	 */
	public void openEmotionView(){
		CommonUtil.log("load", "openEmotionView:"+mPagers.size());
		if(!isFirstLoad){//如果不是第一次打开，则直接退出。
			finishTabInit();
			return ;
		}
			
		
		if(null!=mPagers&&mPagers.size()>0){
			EmotionTab tab  = mPagers.get(0);
			CommonUtil.log("open", "openEmotionView rankNull:"+tab.rankNull+"|size:"+tab.adapters.size());
			if(tab.emotion_style == EmotionConfig.EMOTION_SMALL_STYLE){
				//通过排行榜的大小来确定是否确定是第几个
				int rankSize = 0;
				EmotionRank rank = EmotionRankManager.getInstance().getEmotionRank(EmotionConfig.SMALL_PACKAGE_ID);
				if(null != rank){
					 rankSize = rank.size();
				}
				
				if(rankSize > 0){
					//Icon默认选择为第一个
					tab.notifyDataSetChanged(0, true);
				}
				else {
					//Icon选择为第2个
					tab.flipper.onShowPointer(1);
					tab.notifyDataSetChanged(1, true);
				}
			}
			else {
				tab.notifyDataSetChanged(0, true);
			}
			finishTabInit();
		}
		isFirstLoad = false;  //设置标志位
	}
	/**
	 * Tab 选择的监听事件
	 * @author jiaxia
	 *
	 */
	public class CheckListenerImpl implements OnCheckListener{

		@Override
		public void onCheck(EmotionCheckView view) {
			if(view.getTag()!=null){
				View pager = (View) view.getTag();
				for(EmotionTab p :mPagers){
					p.setVisibility(View.GONE);
				}
				//通过remove和add实现一级tab的切换
				mPagers_Linearlayout.removeAllViews();
				LinearLayout.LayoutParams params = 
						new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
				params.width = getMeasuredWidth()>0?getMeasuredWidth():LayoutParams.FILL_PARENT;
				mPagers_Linearlayout.addView(pager, params);
				pager.setVisibility(VISIBLE);
				if(pager instanceof EmotionTab){
					//如果是表情的Tab则通知刷新
					((EmotionTab)pager).notifyDataSetChanged(0,true);
				}
			}
		}
	}
	
	public void finishTabInit(){
		mTabs_RadioGroup.initCheck();
	}
	
}
