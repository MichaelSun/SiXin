package com.common.emotion.view;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.common.R;
import com.common.app.AbstractRenrenApplication;
import com.common.emotion.config.EmotionConfig;
import com.common.emotion.emotion.EmotionPool;
import com.common.emotion.emotion.EmotionRankManager;
import com.common.emotion.manager.Emotion;
import com.common.emotion.manager.IEmotionManager.OnCoolEmotionSelectCallback;
import com.common.emotion.manager.IEmotionManager.OnEmotionSelectCallback;
import com.common.emotion.view.PluginGroup.AbstractPluginAdapter;
import com.core.json.JsonObject;
import com.core.json.JsonParser;
import com.core.util.CommonUtil;

/**
 * 表情控件数据源
 * 
 * @author jiaxia
 */
public class EmotionPluginAdapter extends AbstractPluginAdapter {
	private String[] paths = null;
	private String[] codes = null;
	private int mPackageId;
	private int mEmotionStyle;
	private OnEmotionSelectCallback mNormalListener = null;
	private OnCoolEmotionSelectCallback mCoolListener = null;
	private View[] data = null;

	private static long time = 0;
	private String[] theme_ids = null;
	private static final String TAG = "emotionadapter";
	public static final int NORMAL_EMOTION_BITMAP_WIDTH_MIDDIP = 30;//1.0中屏手机对应的手机像素高度
	public static final int NORMAL_EMOTION_BITMAP_HEIGHT_MIDDIP = 30;//1.0中屏手机对应的手机像素高度
	public static final int COOL_EMOTION_BITMAP_WIDTH_MIDDIP = 108;//1.0中屏手机对应的手机像素高度
	public static final int COOL_EMOTION_BITMAP_HEIGHT_MIDDIP = 108;//1.0中屏手机对应的手机像素高度
	public LinearLayout.LayoutParams mItemEmotionLayoutParams = null;
	/**
	 * 放入数据(path路径和Code)
	 * 
	 * @param paths
	 *            :表情路径(数组)
	 * @param codes
	 *            :表情Code(数组)
	 * @param package_id
	 *            :包ID
	 * @param emotion_style
	 *            :类型(EmotionConfig.Emotion_SMALL_STYLE)
	 */
	public void setEmotionPluginAdapterData(String[] paths, String[] codes,
			int package_id, int emotion_style, String[] theme_ids) {
		// SystemUtil.log("jjjx", "set:"+paths.length+"|||"+codes.length);
		this.paths = paths;
		this.codes = codes;
		this.mPackageId = package_id;
		this.mEmotionStyle = emotion_style;
		this.theme_ids = theme_ids;
		if (paths != null) {
			data = new View[paths.length];
		}
	}

	
	
	
	/**
	 * 注册普通表情选择监听(和玄酷的选择监听只能注册一个)
	 * 
	 * @param callback
	 */
	public void registerNormalEmotionSelectCallBack(
			OnEmotionSelectCallback callback) {
		this.mNormalListener = callback;
	}

	/**
	 * 注册玄酷表情选择监听(和普通的选择监听只能注册一个)
	 * 
	 * @param callback
	 */
	public void registerCoolEmotionSelectCallBack(
			OnCoolEmotionSelectCallback callback) {
		this.mCoolListener = callback;
	}
	/**
	 * 清掉监听
	 */
	public void removeCoolEmotionSelectCallBack(){
		this.mCoolListener = null;
	}
	/**
	 * 清掉监听
	 */
	public void removeNormalEmotionSelectCallBack(){
		this.mNormalListener = null;
	}

    /**
     * 清楚View的缓存
     */
    public void clearData(){
    	if(null!=data&&data.length>0){
    		for(View v:data){
    			if(v!=null){
    				BitmapDrawable d = (BitmapDrawable) ((ImageView)v).getDrawable();
    				Bitmap b = null;
    				if(d!=null)
    				   b = d.getBitmap();
    				if(null!=b&&!b.isRecycled()){
    					b.recycle();
    					b = null;
    				}
    				d = null;
    			}
    			v = null;
    		}
    	}
        data = null;
    }

	@Override
	public int getCount() {
		if (paths == null)
			return 1;
		return paths.length;
	}

	@Override
	public View getView(final int position, boolean load) {
		CommonUtil.log("getview", "getView");
		if (paths == null && getCount() == 1) {
			return new View(AbstractRenrenApplication.getAppContext());
		}
		if (load == false) {
			View v =  new View(AbstractRenrenApplication.getAppContext());
			return  v;
		}
		if(paths[position] == null){   //当页里表情未满,path则为null
			return new View(AbstractRenrenApplication.getAppContext());
		}
		if (data[position] != null) {
			return data[position];
		}
		final ImageView mImageView = new ImageView(AbstractRenrenApplication.getAppContext());
		new Handler().post(new Runnable() {

			@Override
			public void run() {
				if (paths[position].equals(EmotionConfig.DELETE)) {
					mImageView.setBackgroundResource(R.drawable.emotion_delete_selector);
				} else {
					Emotion emotion = EmotionPool.getInstance().getEmotion(
							paths[position]);
					if (emotion != null && emotion.next() != null) {
						mImageView.setImageBitmap(emotion.next());
					}
					mImageView
					.setBackgroundResource(R.drawable.emotion_background_selector);
				}

				// TODO 这里还有待调整，每一个表情在表情面板中的布局
				
				mImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);// 表情缩放类型
				if (mEmotionStyle == EmotionConfig.EMOTION_SMALL_STYLE) { // 小表情布局
					mImageView.setPadding(CommonUtil.calcFromDip(8),
							CommonUtil.calcFromDip(8),
							CommonUtil.calcFromDip(8),
							CommonUtil.calcFromDip(8));
				} else if (mEmotionStyle == EmotionConfig.EMOTION_COOL_STYLE) { // 大表情布局
				}
				data[position] = mImageView;

			}
		});
		mImageView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (paths[position].equals(EmotionConfig.DELETE)) {
					if (mNormalListener != null)
						mNormalListener.mDelBtnClick();
				} else {
					if (mEmotionStyle == EmotionConfig.EMOTION_SMALL_STYLE
							&& mNormalListener != null) {
						CommonUtil.log("onClick",codes[position]+"||"+ paths[position]+"  theme:"+theme_ids[0]);
						if(position >= theme_ids.length){  //通用的主题
							CommonUtil.log("on",mPackageId+ "  click:"+EmotionRankManager.getInstance().getEmotionRank(mPackageId));
							
							EmotionRankManager
							.getInstance()
							.getEmotionRank(mPackageId) 
							.addToRank(paths[position], codes[position],
									theme_ids[0], 1);
						}else{   //常用的主题
							EmotionRankManager
							.getInstance()
							.getEmotionRank(mPackageId)
							.addToRank(paths[position], codes[position],
									theme_ids[position], 1);
						}
						CommonUtil.log("onClick", "select code :"+parserCHJson(codes[position]));
						mNormalListener
								.onEmotionSelect(parserCHJson(codes[position]));
						
					}
					if (mEmotionStyle == EmotionConfig.EMOTION_COOL_STYLE
							&& mCoolListener != null) {
						AbstractRenrenApplication.HANDLER.post(new Runnable() {

							@Override
							public void run() {
								//格式为：（3-笑）
								CommonUtil.log("cool", "click:"+"("
												+ theme_ids[0] + "-"
												+ parserCHJson(codes[position])
												+ ")");
										mCoolListener.onCoolEmotionSelect("("
												+ theme_ids[0] + "-"
												+ parserCHJson(codes[position])
												+ ")");
							}
						});
					}
				}

			}

		});
		return data[position] != null ? data[position] : mImageView;
	}

	/**
	 * 解析JSON串 获得中文转义符或者本身
	 * 
	 * @param str
	 * @return
	 */
	private String parserCHJson(String str) {
		try {
			JsonObject object = (JsonObject) JsonParser.parse(str);
			if (object == null)
				return str;

			return object.getString("ch");
		} catch (ClassCastException e) {
			return str;
		}
	}

	@Override
	protected void finalize() throws Throwable {
		CommonUtil.log(TAG, "finalize");
		super.finalize();
	}

}
