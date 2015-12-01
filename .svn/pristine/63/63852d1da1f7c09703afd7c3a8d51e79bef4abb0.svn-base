package com.common.emotion.view;

import java.util.List;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import com.common.R;
import com.common.emotion.config.EmotionConfig;
import com.common.emotion.emotion.EmotionRank;
import com.common.emotion.emotion.EmotionRankManager;
import com.common.emotion.view.PluginGroup.AbstractPluginAdapter;
import com.core.util.CommonUtil;

/**
 * 对一级tab下内容的封装，包括左侧纵向的二级tab和右侧的Flipper
 * @author jia.xia
 * */
public class EmotionTab extends LinearLayout implements RankOnIconCheckListener {

	private int mPagerCount = 0;
	private Context mContext;
	private int background_check_id;
	private int background_uncheck_id;
	/**
	 * 右侧ViewFlipper
	 */
	public EmotionViewFlipper flipper = null;
	/**
	 * 左侧的二级tab
	 */
	private VerticalIconContainer secondtab = null;
	/**
	 * 二级tab下对应的EmotionPluginGroup的adapter的容器
	 */
	public List<EmotionPluginAdapter> adapters = null;
	/**
	 * 一级tab的名字
	 */
	public String tabName;
	private final int package_id;
	public final int emotion_style ;
	public  boolean  rankNull = true; //常用是否为空
	 
	private int rankPosition ;  //当前所处的主题位置
	private int pagePosition;   //当前所处的页位置

	public EmotionTab(String emotionName, Context context, int check_id,int uncheck_id,int package_id,int emotion_style) {
		super(context);
		this.mContext = context;
		this.background_check_id = check_id;
		this.background_uncheck_id = uncheck_id;
		this.tabName = emotionName;
		this.package_id = package_id;
		this.emotion_style = emotion_style;
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout view = (LinearLayout) inflater.inflate(
				R.layout.emotion_tab_layout, null);
		this.addView(view);
		flipper = (EmotionViewFlipper) findViewById(R.id.emotion_flipper_container);
		secondtab = (VerticalIconContainer) findViewById(R.id.emotion_secondtab_container);
	}
	/**
	 * 背景
	 * @return
	 */
	public int getBackgroundCheckId() {
		// return mTabPager.mBackgroudLayoutId();
		return this.background_check_id;
	}
	
	public int getBackgroundUncheckId(){
		return this.background_uncheck_id;
	}
	/**
	 * Tab Name
	 * @return
	 */
	public String getTabName() {
		// return mTabPager.getTabName();
		return this.tabName;
	}
	/**
	 * 刷新
	 * @param adaptersIndex adapter的索引
	 * @param isload 是否刷新
	 */
	public void notifyDataSetChanged(int adaptersIndex,boolean isload) {
		if (this.adapters == null)
			return;
		adapters.get(adaptersIndex).notifyDataChange(isload);
	}
	/**
	 * 设置Adapter(表情本身+主题Icon)	
	 * @param adapters:List<EmotionPluginAdapter>
	 * @param iconAdapter:VerticalIconContainerAdapter 
	 * @param stype   :是哪种表情类型
	 */
	public void setAdapter(List<EmotionPluginAdapter> adapters,VerticalIconContainerAdapter iconAdapter,boolean rankIsNull,boolean isShow) {

		this.adapters = adapters;
		this.rankNull = rankIsNull;
		if (this.adapters == null)
			return;

		LayoutInflater inflater = ((Activity) this.mContext)
				.getLayoutInflater();

		for (int k = 0; this.adapters != null && k < this.adapters.size(); k++) {
			AbstractPluginAdapter adapter = this.adapters.get(k);
			View content = inflater.inflate(R.layout.emotion_flipper_content,
					null);
			PointsContainer pointsContainer = (PointsContainer) content
					.findViewById(R.id.emotion_points_container);
			PluginGroup pGroup = (PluginGroup) content
					.findViewById(R.id.emotion_flipper_content);
			//TODO 这里需要动态修改
			if(emotion_style == EmotionConfig.EMOTION_SMALL_STYLE){
				pGroup.setPagerPadding(0, CommonUtil.calcFromDip(6),0,  CommonUtil.calcFromDip(6),CommonUtil.calcFromDip(8));
				pGroup.setPageParameter(EmotionConfig.SMALL_COLUMN_NUM, EmotionConfig.SMALL_ROW_NUM);
			}
			else if(emotion_style == EmotionConfig.EMOTION_COOL_STYLE){
				pGroup.setPagerPadding(0, 0,0, 0,CommonUtil.calcFromDip(7));
				pGroup.setPageParameter(EmotionConfig.COOL_COLUMN_NUM, EmotionConfig.COOL_ROW_NUM);
			}
			//第一次进入不加载表情的getView方法
			pGroup.setAdapter(adapter, false);
			
			mPagerCount = adapter.getCount();
			pointsContainer.addPoints(pGroup.getPageCount());
			pGroup.setOnVerticalFlingListener(flipper);
			pGroup.setOnHorizontalFlingListener(pointsContainer);
			flipper.addView(content);
			
		}
		//设置回调
		secondtab.setSuperOnFling2ContentChangeListener(flipper);
		//设置适配器
		secondtab.setAdapter(iconAdapter);
		//设置反向回调
		flipper.setOnIndicatorListener(secondtab);
		//如果首次进入的时候，常用表情为空，指向第一个二级页面
//		if(rankIsNull == true&&emotion_style == EmotionConfig.EMOTION_SMALL_STYLE){
//			flipper.onShowPointer(1);
//		}
	}

	public void setShowIndex(int index){
		flipper.onShowPointer(index);
	}
	/**
	 * 当滑动到常用项时，调用此方法！
	 */
	@Override
	public void rankCheck(int position) {
		CommonUtil.log("xjjjj", "rank check package_id:"+package_id);
		this.rankPosition = position;
		if(position == 0){
		EmotionRank rank = EmotionRankManager.getInstance().getEmotionRank(package_id);
	    if(adapters!=null&&rank!=null){
	    	String[] paths = null;
	    	String[] codes = null;
	    	String[] mPaths = null;
	    	String[] mCodes = null;
	    	
	    	String[] themes = null;
	    	if(emotion_style == EmotionConfig.EMOTION_SMALL_STYLE){ //当为小表情时
	    		paths = rank.getTopEmotionPath(EmotionConfig.SMALL_ONE_PAGE_SUM);
		    	codes = rank.getTopEmotionCode(EmotionConfig.SMALL_ONE_PAGE_SUM);
		    	themes = rank.getTopEmotionThemeId(EmotionConfig.SMALL_ONE_PAGE_SUM);
		    	if(paths!=null&&paths.length>0){
		    		if(paths.length == EmotionConfig.SMALL_ONE_PAGE_SUM){//当表情满了后,在最后一个添加删除
		    			mPaths = new String[EmotionConfig.SMALL_ONE_PAGE_SUM];
		    			mCodes = new String[EmotionConfig.SMALL_ONE_PAGE_SUM];
		    			for(int i = 0;i<paths.length;i++){
		    				if(i == paths.length-1){
		    					mPaths[i] = EmotionConfig.DELETE;
			    				mCodes[i] = EmotionConfig.DELETE;
		    				}else{
		    					mPaths[i] = paths[i];
			    				mCodes[i] = codes[i];
		    				}
		    				
		    			}
		    		}
		    		else{  //当表情未满时,数组要加一个位置.
		    			mPaths = new String[EmotionConfig.SMALL_ONE_PAGE_SUM];
		    			mCodes = new String[EmotionConfig.SMALL_ONE_PAGE_SUM];
		    			for(int i = 0;i<paths.length;i++){
		    					mPaths[i] = paths[i];
			    				mCodes[i] = codes[i];
		    			}
                        mPaths[mPaths.length-1] = EmotionConfig.DELETE;
                        mCodes[mCodes.length-1] = EmotionConfig.DELETE;
		    		}
		    	}
	    	}
	    	else if(emotion_style == EmotionConfig.EMOTION_COOL_STYLE){  //中表情
//	    		mPaths = rank.getTopEmotionPath(EmotionConfig.COOL_ONE_PAGE_SUM);
//		    	mCodes = rank.getTopEmotionCode(EmotionConfig.COOL_ONE_PAGE_SUM);
//		    	themes = rank.getTopEmotionThemeId(EmotionConfig.COOL_ONE_PAGE_SUM);
	    		return ;
	    	}
	    	if(mPaths == null||mCodes == null) //没有点击事件的发生
	    		return ;
	    	EmotionPluginAdapter adapter = adapters.get(0);
	    	adapter.setEmotionPluginAdapterData(mPaths, mCodes,package_id,emotion_style,themes);
	    	this.notifyDataSetChanged(0,true);
	    }
		}
		else{
			this.notifyDataSetChanged(position,true);
		}
	}

}
