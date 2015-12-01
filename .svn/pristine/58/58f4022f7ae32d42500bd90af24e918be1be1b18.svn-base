package com.common.emotion.manager;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.view.ViewGroup;

import com.common.R;
import com.common.app.AbstractRenrenApplication;
import com.common.emotion.config.EmotionConfig;
import com.common.emotion.dao.EmotionDaoManager;
import com.common.emotion.emotion.*;
import com.common.emotion.manager.EmotionDownload.DownloadEmotionListener;
import com.common.emotion.manager.EmotionPackageDownload.DownloadEmotionPackageListener;
import com.common.emotion.manager.IEmotionManager.OnCoolEmotionSelectCallback;
import com.common.emotion.manager.IEmotionManager.OnEmotionParserCallback;
import com.common.emotion.manager.IEmotionManager.OnEmotionSelectCallback;
import com.common.emotion.model.EmotionPackageModel;
import com.common.emotion.model.EmotionThemeModel;
import com.common.emotion.view.*;
import com.common.utils.Config;
import com.common.utils.Methods;
import com.core.util.CommonUtil;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * 
 * @author jiaxia 表情加载数据库,获取图片资源,加载表情控件
 */
public class EmotionManager {
	/**
	 * 下载表情监听器
	 */
	private DownloadEmotionListener mDownloadEmotionListener = null;
	/**
	 * 下载表情包监听器
	 */
	private DownloadEmotionPackageListener mDownloadEmotionPackageListener = null;
	/**
	 * 中表情解析监听器
	 */
	private OnEmotionParserCallback mParserEmotionListener = null;
	/**
	 * 小表情选择回调
	 */
	private OnEmotionSelectCallback mSelectEmotionListener = null;
	/**
	 * 中表情选择回调
	 */
	private OnCoolEmotionSelectCallback mSelectCoolEmotionListener = null;
	/**
	 * 存储普通表情数据源的列表
	 */
	//private List<EmotionPluginAdapter> mNormalAdapterList = new ArrayList<EmotionPluginAdapter>();

	private static EmotionManager sInstance = null;
//DAO管理器
	private EmotionDaoManager daoManager = EmotionDaoManager.getInstance();

	private AssetManager am = AbstractRenrenApplication.getAppContext()
			.getAssets();
	//存储View
	private List<EmotionTab> mEmotionTab = null;
	/**
	 * 判断常用排行榜初始是否为空.
	 */
	private boolean rankIsNull = true;

	private EmotionManager() {
	}

	public static EmotionManager getInstance() {
		if (sInstance == null) {
			sInstance = new EmotionManager();
		}
		return sInstance;
	}
/**
 * 重新设置Tab的名称
 */
//	private void resetTabName(){
//		if(null!=mEmotionTab&&mEmotionTab.size()>0){
//			for(EmotionTab tab : mEmotionTab){
//				tab.c
//			}
//		}
//	}
	
	/**
	 * 启动表情控件
	 * 
	 * @param view
	 *            com.common.emotion.view.EmotionView
	 * @param f
	 *            FragmentActivity
	 * 
	 */
	public void loadEmotionView(EmotionView view, FragmentActivity f) {
		long start = System.currentTimeMillis();
		if(null!=mEmotionTab&&mEmotionTab.size()>0){ //使用缓存View
			CommonUtil.log("load", "mEmotionTab is not null:"+mEmotionTab.size());
			for(EmotionTab tab : mEmotionTab){
				if(((ViewGroup)tab.getParent()) != null)//清掉引用
				   ((ViewGroup)tab.getParent()).removeView(tab);
				view.addTab(tab);
			}
			//重置Tab的名字，用于国际化
			view.resetTabName();
			/*************加入额外的2个View********************/
			if (Config.VERSION_CURRENT == Config.VERSION_INLAND) {// 国内文案
				view.addAdditionalTab(AbstractRenrenApplication.getAppResources().getString(R.string.emotion_magic_ch), R.drawable.cool_uncheck_bg,
						R.drawable.cool_check_bg);
			} else { //国际版文案 
				view.addAdditionalTab(
						AbstractRenrenApplication.getAppResources().getString(R.string.emotion_magic_en), R.drawable.cool_uncheck_bg,
						R.drawable.cool_check_bg);
			}
			view.addAdditionalTab(R.drawable.emotion_more, R.drawable.cool_uncheck_bg,
					R.drawable.cool_check_bg);
			//view.finishTabInit();
		}
		else{ //生成View
			CommonUtil.log("load", "mEmotionTab is null:"+mEmotionTab);
			List<EmotionPackageModel> packageList = null;

			packageList = daoManager.queryAllPackageOrderBy();// 获取数据库中的包列表

			if (packageList != null && packageList.size() > 0) {
				for (EmotionPackageModel item : packageList) {
					int type = item.package_type;
					String name = item.package_name.trim();
					int hidden = item.package_hidden;
					int id = item.package_id;
					if (hidden == EmotionConfig.EMOTION_SHOW) {// 显示
						if (type == EmotionConfig.EMOTION_PACKAGE_SMALL) { // 小表情
							initEmotionAdapter(view, f, name, id,
									EmotionConfig.EMOTION_SMALL_STYLE);
						} else if (type == EmotionConfig.EMOTION_PACKAGE_COOL) {// 中表情
							initEmotionAdapter(view, f, name, id,
									EmotionConfig.EMOTION_COOL_STYLE);
						}
					}
				}
				
			}
			/*************加入额外的2个View********************/
			if (Config.VERSION_CURRENT == Config.VERSION_INLAND) {// 国内文案
				view.addAdditionalTab(AbstractRenrenApplication.getAppResources().getString(R.string.emotion_magic_ch), R.drawable.cool_uncheck_bg,
						R.drawable.cool_check_bg);
				
			} else { // 国际版文案
				view.addAdditionalTab(AbstractRenrenApplication.getAppResources().getString(R.string.emotion_magic_en), R.drawable.cool_uncheck_bg,
						R.drawable.cool_check_bg);
			}
			view.addAdditionalTab(R.drawable.emotion_more, R.drawable.cool_uncheck_bg,
					R.drawable.cool_check_bg);
		//	view.finishTabInit();

		}
				CommonUtil.log("cost",
				"Emotion loading cost " + (System.currentTimeMillis() - start));
				CommonUtil.log("load", "load EmotionView end");
	}
/**
 * 初始化控件View（会持有View，直到主界面退出）
 * 
 * @param context Context
 */
	/*public void loadEmotionView(Context context){
		CommonUtil.log("load", "loadEmotionView(Context context)");
		//clearEmotionView();
		List<EmotionPackageModel> packageList = null;
		packageList = daoManager.queryAllPackageOrderBy();// 获取数据库中的包列表
		if (packageList != null && packageList.size() > 0) {
			//初始化缓存
			if(mEmotionTab == null)
			    mEmotionTab = new ArrayList<EmotionTab>();
			for (EmotionPackageModel item : packageList) {
				int type = item.package_type;
				String name = item.package_name.trim();
				int hidden = item.package_hidden;
				int id = item.package_id;
				if (hidden == EmotionConfig.EMOTION_SHOW) {// 显示
					if (type == EmotionConfig.EMOTION_PACKAGE_SMALL) { // 小表情
						initEmotionAdapter(context, name, id,
								EmotionConfig.EMOTION_SMALL_STYLE);
					} else if (type == EmotionConfig.EMOTION_PACKAGE_COOL) {// 中表情
						initEmotionAdapter(context, name, id,
								EmotionConfig.EMOTION_COOL_STYLE);
					}
				}
			}
		}
	}*/
	
	/**
	 * 解析资源文件(和数据库进行对比)
	 */
	public void loadEmotionPackage() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				long timeStart = System.currentTimeMillis();
				// SystemUtil.log("XXXXXX", "start Time:" + timeStart);

				try {

					String[] lists = am.list("");
					for (int i = 0; lists != null && i < lists.length; i++) {
						if (lists[i].equals("emotion_small")) { // 判断是否是小表情的资源包
							int result = parserAssets(lists[i],
									EmotionConfig.SMALL_PACKAGE_ID);
							if (result == -1) {
								EmotionPackageModel pModel = new EmotionPackageModel();
								pModel.package_id = EmotionConfig.SMALL_PACKAGE_ID;
								if (Config.VERSION_CURRENT == Config.VERSION_INLAND) {
									pModel.package_name = "基本";
								} else {
									pModel.package_name = "Emoji";
								}
								pModel.package_hidden = 1;
								pModel.package_type = EmotionConfig.EMOTION_PACKAGE_SMALL;
								pModel.package_position = 0;
								daoManager.insertPackage(pModel);
							}

						} else if (lists[i].equals("emotion_cool")) {// 判断是否是中表情的资源包
							int result = parserAssets(lists[i],
									EmotionConfig.COOL_PACKAGE_ID);
							if (result == -1) {
								EmotionPackageModel pModel = new EmotionPackageModel();
								pModel.package_id = EmotionConfig.COOL_PACKAGE_ID;
								if (Config.VERSION_CURRENT == Config.VERSION_INLAND) {
									pModel.package_name = "炫酷";
								} else {
									pModel.package_name = "Sticker";
								}
								pModel.package_hidden = EmotionConfig.EMOTION_SHOW;
								pModel.package_type = EmotionConfig.EMOTION_PACKAGE_COOL;
								pModel.package_position = 1;
								daoManager.insertPackage(pModel);

							}

						}
					}

				} catch (IOException e) {
					e.printStackTrace();
				}
				CommonUtil.log("cost", "cost time"+(System.currentTimeMillis()-timeStart));
				long timeEnd = System.currentTimeMillis();
				EmotionNameList.initEmotion_NameList();
				EmotionPool.getInstance();
				CommonUtil.log("init", "load Db and cache end");
				DataBaseLock.unlock();
				
			}

		}).start();

	}

	/**
	 * 初始化表情ICON的图片资源
	 */
/*	public void loadIconAdapter() {
		// if(mIconAdapterBitmap.size()>0){
		// mIconAdapterBitmap.clear();
		// }
		if (!mIconAdapterBitmap.containsKey("rank_check".hashCode()))
			mIconAdapterBitmap.put("rank_check".hashCode(),
					getBitmap("rank_check"));
		if (!mIconAdapterBitmap.containsKey("rank_uncheck".hashCode()))
			mIconAdapterBitmap.put("rank_uncheck".hashCode(),
					getBitmap("rank_uncheck"));
		List<EmotionPackageModel> pList = daoManager.queryAllPackage();
		for (int k = 0; pList != null && k < pList.size(); k++) {
			List<EmotionThemeModel> tList = daoManager.queryByPackageId(pList
					.get(k).package_id);
			for (int j = 0; tList != null && j < tList.size(); j++) {
				if (tList.get(j).theme_hidden == EmotionConfig.EMOTION_SHOW) {
					String check = tList.get(j).theme_check_icon_path;
					String uncheck = tList.get(j).theme_uncheck_icon_path;
					if (!mIconAdapterBitmap.containsKey(check.hashCode()))
						mIconAdapterBitmap.put(check.hashCode(),
								getBitmap(check));
					if (!mIconAdapterBitmap.containsKey(uncheck.hashCode()))
						mIconAdapterBitmap.put(uncheck.hashCode(),
								getBitmap(uncheck));
				}
			}
		}
	}*/

	private Bitmap getBitmap(String path) {
		InputStream is = null;
		Bitmap b = null;
		try {
			is = am.open(path + ".png");
			b = BitmapFactory.decodeStream(is);
			is.close();

		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		return b;
	}

	/**
	 * 解析不同包下的Json数据
	 * 
	 * @param path
	 * @param style
	 *            :包的ＩＤ,即哪种样式
	 * @return
	 * @throws IOException
	 */
	private int parserAssets(String path, int style) throws IOException {
		List<EmotionThemeModel> pList = null;
		if(daoManager == null)
			daoManager = EmotionDaoManager.getInstance();
		pList = daoManager.queryByPackageId(style);
		String[] innerList = am.list(path);
		if (pList != null) {//数据库中有数据，则匹配一下数据库的操作
			for (int k = 0; innerList != null && k < innerList.length; k++) {
				if (innerList[k].lastIndexOf(".json") != -1) {// 解析Json串
					boolean flag = false;

					for (int j = 0; pList != null && j < pList.size(); j++) {
						if (pList.get(j).theme_path.equals(path
								+ File.separator
								+ innerList[k].substring(0,
										innerList[k].length() - 5))) {
							flag = true;
							break;
						}
					}
					if (flag == false) {

						DataBaseLock.lock();
						EmotionJsonParser.getInstance().emotionJsonParser(
								am.open(path + File.separator + innerList[k]));
					}
				}
			}
		} else {//数据库中无数据，则将所有的文件读入数据库
			DataBaseLock.lock();
			for (int k = 0; innerList != null && k < innerList.length; k++) {
				if (innerList[k].lastIndexOf(".json") != -1) {// 解析Json串
					EmotionJsonParser.getInstance().emotionJsonParser(
							am.open(path + File.separator + innerList[k]));
				}
			}
			return -1;
		}
		return 0;
	}

	/**
	 * 获取中表情信息(需要注册解析监听器)
	 * 
	 * @param emotionName
	 *            报文中的中表情信息或点击中的中表情信息
	 */
	public void getCoolEmotion(String emotionName) {
		/***************** 将消息解析成需要的数据(package_id+name) ******************/
		 CommonUtil.log("cool", "getCoolEmotion:"+emotionName);
		String theme_id = null;
		String name = "";
		String[] data = Methods.parserBracketsPoints(emotionName);
		if (data != null && data.length >= 2) {// 如果正确的话就有2个数
			theme_id = data[0];
			name = data[1];
			if (theme_id.equals("阿狸")) { // 阿狸替换成3
				theme_id = EmotionConfig.ALI_THEME_ID;
			}
		} else {
			// 表示有问题
		}

		/**************** 正常解析 *****************/
		Emotion emotion = null;
		// CommonUtil.log("EmotionManager", "Cool emotion:" + theme_id + "||" +
		// name);
		//
		int pid = EmotionConfig.COOL_PACKAGE_ID;
		final String _name = name;
		final String _themeid = theme_id;
		String path = null;
		Node node = null;
		node = EmotionNameList
				.getEmotionNameList(EmotionConfig.COOL_PACKAGE_ID);
		if (node != null) {
			path = EmotionNameList.getEmotionNameList(
					EmotionConfig.COOL_PACKAGE_ID).getPath(_name, _themeid);
		} else {
			
			return;
		}
		if(path == null){
			try{
				this.mParserEmotionListener.onEmotionLoading(BitmapFactory
						.decodeResource(AbstractRenrenApplication
								.getAppContext().getResources(),
								R.drawable.emotion_default));
			}catch(OutOfMemoryError error ){
				CommonUtil.log("emotion","内存溢出");
				this.mParserEmotionListener.onEmotionLoading(null);
			}
			String[] emotion_ids = { _name };

			EmotionDownload.getInstance().downloadEmotion(_themeid,
					emotion_ids, mParserEmotionListener);
			return ;
		}

		if (ScrollingLock.isScrolling) {
			emotion = EmotionPool.getInstance().getCoolEmotion(path + ".gif");
			// CommonUtil.log("Emotion",
			// "path:"+EmotionNameList.getEmotionNameList(EmotionConfig.COOL_PACKAGE_ID).getPath(_name,_themeid));
			if (emotion == null) {
				// emotion =
				// EmotionPool.getInstance().getEmotion(EmotionNameList.getEmotionNameList(pid).getPath(_name,_themeid));
				emotion = EmotionPool.getInstance().getFirstFrameEmotion(path);
			}
			if (emotion != null) {
				this.mParserEmotionListener.onEmotionSuccess(emotion);
			}

			else {// 下载网络图片
			
			}
			return;
		} else {

			emotion = EmotionPool.getInstance().getCoolEmotion(path + ".gif");

			if (emotion == null || !emotion.mIsFlash
					|| emotion.mEmotionList.size() == 0) {

				emotion = EmotionPool.getInstance().getFirstFrameEmotion(
						EmotionNameList.getEmotionNameList(
								EmotionConfig.COOL_PACKAGE_ID).getPath(_name,
								_themeid));
				if (emotion != null) {
					this.mParserEmotionListener.onEmotionSuccess(emotion);
				} 
				LoadGifThread pool = LoadGifThread.getInstance();

				pool.add(path);
			} else {
				// SystemUtil.log("Emtoion","Emotion is in cache "+emotion.mEmotionName
				// + emotion.mFrameSize + "list size" +
				// emotion.mEmotionList.size());
				mParserEmotionListener.onEmotionSuccess(emotion);
			}

		}
	}

	/**
	 * 注册普通表情选择监听
	 * 
	 * @param listener
	 *            :OnEmotionSelectCallback
	 */
	public void registerNormalEmotionSelectCallBack(
			OnEmotionSelectCallback listener) {
         if(mEmotionTab!=null&&mEmotionTab.size()>=1){
        	 List<EmotionPluginAdapter>mNormal = mEmotionTab.get(0).adapters;
        	 if(null!=mNormal&&mNormal.size()>0){
        		 for (EmotionPluginAdapter adapter : mNormal) {
        				adapter.registerNormalEmotionSelectCallBack(listener);
        		}
        	 }
         }
	}

	/**
	 * 注册中表情选择监听
	 * 
	 * @param listener
	 *            :OnCoolEmotionSelectCallback
	 */
	public void registerCoolEmotionSelectCallBack(
			OnCoolEmotionSelectCallback listener) {
		if(mEmotionTab!=null&&mEmotionTab.size()>=2){
       	 List<EmotionPluginAdapter>mCool = mEmotionTab.get(1).adapters;
       	 if(null!=mCool&&mCool.size()>0){
       		 for (EmotionPluginAdapter adapter : mCool) {
       		    adapter.registerCoolEmotionSelectCallBack(listener);
       		}
       	 }
        }
	}

	/**
	 * 注册中表情解析监听器
	 * 
	 * @param listener
	 *            :OnEmotionParserCallback
	 */
	public void registerCoolEmotionParserCallBack(
			OnEmotionParserCallback listener) {
		this.mParserEmotionListener = listener;
	}

	// private void initRankEmotionAdapter(EmotionView){
	//
	// }
/**
 * 根据不同名字和类型生成不同文案的EmotionTab（国际有国际化的问题）
 * @param name       tab名
 * @param emotion_style  属于什么类型
 * @return
 */
	private EmotionTab createEmotionTab(Context context,int pid,int emotion_style){
		EmotionTab mTab = null ;
		if (Config.VERSION_CURRENT == Config.VERSION_INLAND) {// 国内文案
			if (emotion_style == EmotionConfig.EMOTION_SMALL_STYLE) {
				mTab = new EmotionTab(AbstractRenrenApplication.getAppResources().getString(R.string.emotion_emoji_ch), context, R.drawable.emoji_check_bg,
						R.drawable.emoji_uncheck_bg, pid, emotion_style);
			} else {
				mTab = new EmotionTab(AbstractRenrenApplication.getAppResources().getString(R.string.emotion_sticker_ch), context, R.drawable.cool_check_bg,
						R.drawable.cool_uncheck_bg, pid, emotion_style);
			}
		} else { //国际版文案 
			if (emotion_style == EmotionConfig.EMOTION_SMALL_STYLE) {
				mTab = new EmotionTab(AbstractRenrenApplication.getAppResources().getString(R.string.emotion_emoji_en), context, R.drawable.emoji_check_bg,
						R.drawable.emoji_uncheck_bg, pid, emotion_style);
			} else {
				mTab = new EmotionTab(AbstractRenrenApplication.getAppResources().getString(R.string.emotion_sticker_en), context, R.drawable.cool_check_bg,
						R.drawable.cool_uncheck_bg, pid, emotion_style);
			}
		}
		
		return mTab;
	}
	
	/**
	 * 初始化小表情样式和数据
	 * 
	 * @param view
	 *            :EmotionView
	 * @param f
	 *            :FragmentActivity
	 * @param name
	 *            : tab的名字
	 * @param pid
	 *            :包Ｉｄ
	 * @param emotion_style
	 *            : 小表情还是中表情,页面显示个数不一样
	 */
	private void initEmotionAdapter(EmotionView view, Context context,
			String name, int pid, int emotion_style) {
		EmotionTab mTab = null;
		mTab = createEmotionTab(context,pid,emotion_style);
		final EmotionTab tab = mTab;
		List<EmotionPluginAdapter> adapters = new ArrayList<EmotionPluginAdapter>();
		final List<String> iconCheckPath = new ArrayList<String>();
		final List<String> iconUnCheckPath = new ArrayList<String>();
		/************* 设置常用主题 (只有小表情有常用，玄酷表情没有) ****************/

		if (emotion_style == EmotionConfig.EMOTION_SMALL_STYLE) {
			// EmotionNameList.initEmotionRank(24); // 初始化排行榜

			EmotionRank mRank = EmotionRankManager.getInstance()
					.getEmotionRank(pid);

			String[] mRankPaths = null;
			String[] mRankCodes = null;
			String[] rankPaths = null;
			String[] rankCodes = null;
			String[] rankThemes = null;
			rankIsNull = true;
			CommonUtil.log("Tab", "Rank is :" + mRank);
			if (mRank != null) {
				mRank.settobeDefault();
				if (emotion_style == EmotionConfig.EMOTION_SMALL_STYLE) {
					mRankPaths = mRank
							.getTopEmotionPath(EmotionConfig.SMALL_ONE_PAGE_SUM);
					mRankCodes = mRank
							.getTopEmotionCode(EmotionConfig.SMALL_ONE_PAGE_SUM);
					rankThemes = mRank
							.getTopEmotionThemeId(EmotionConfig.SMALL_ONE_PAGE_SUM);
					CommonUtil.log("Tab", "mRankCodes tab 2 :" + mRankCodes);
					if (mRankPaths != null && mRankPaths.length > 0) {

						if (mRankPaths.length == EmotionConfig.SMALL_ONE_PAGE_SUM) {// 当表情满了后,在最后一个添加删除
							rankPaths = new String[EmotionConfig.SMALL_ONE_PAGE_SUM];
							rankCodes = new String[EmotionConfig.SMALL_ONE_PAGE_SUM];
							for (int i = 0; i < mRankPaths.length; i++) {
								if (i == mRankPaths.length - 1) {
									rankPaths[i] = EmotionConfig.DELETE;
									rankCodes[i] = EmotionConfig.DELETE;
								} else {
									rankPaths[i] = mRankPaths[i];
									rankCodes[i] = mRankCodes[i];
								}

							}
						} else { // 当表情未满时,数组要加一个位置放置删除按钮
							rankPaths = new String[EmotionConfig.SMALL_ONE_PAGE_SUM];
							rankCodes = new String[EmotionConfig.SMALL_ONE_PAGE_SUM];
							for (int i = 0; i < mRankPaths.length; i++) {
								rankPaths[i] = mRankPaths[i];
								rankCodes[i] = mRankCodes[i];
							}
							rankPaths[rankPaths.length - 1] = EmotionConfig.DELETE;
							rankCodes[rankCodes.length - 1] = EmotionConfig.DELETE;
						}
					}
				}
				// 设置标志位
				if (rankPaths != null && rankPaths.length > 0)
					rankIsNull = false;

				// 清除缓存
				mRankPaths = null;
				mRankCodes = null;
			}
			CommonUtil.log("Tab", " tab3 :" + rankCodes);
			EmotionPluginAdapter rankAdapter = new EmotionPluginAdapter();
			rankAdapter.setEmotionPluginAdapterData(rankPaths, rankCodes, pid,
					emotion_style, rankThemes);
			adapters.add(rankAdapter);
			iconCheckPath.add("rank_check");
			iconUnCheckPath.add("rank_uncheck");
			//mNormalAdapterList.add(rankAdapter);
		}
		/****************** 设置其他主题的Ａｄａｐｔｅｒ **************************************/
		Node node = EmotionNameList.getEmotionNameList(pid);
		List<EmotionThemeModel> thList = daoManager.queryByPackageId(pid);
		
		if (node != null && thList != null && thList.size() > 0) {
			for (EmotionThemeModel item : thList) {
				if (item.theme_hidden == EmotionConfig.EMOTION_SHOW) {
					iconCheckPath.add(item.theme_check_icon_path);
					iconUnCheckPath.add(item.theme_uncheck_icon_path);
					String[] paths = null;
					String[] codes = null;
					String[] themes = null;
					paths = node.getEmotionPathArray(item.theme_id,
							emotion_style);
					codes = node.getEmotionCodeArray(item.theme_id,
							emotion_style);
					themes = new String[1];
					themes[0] = item.theme_id;

					EmotionPluginAdapter adapter = new EmotionPluginAdapter();
					adapter.setEmotionPluginAdapterData(paths, codes, pid,
							emotion_style, themes);
					adapters.add(adapter);
					CommonUtil.log("icon", thList.size() + "  theme:"
							+ item.theme_check_icon_path);
				}
			}
		}
		/**
		 * 侧边栏中的adapter
		 */
		VerticalIconContainerAdapter iconAdapter = new VerticalIconContainerAdapter() {

			@Override
			protected VTabIconImageView getTabIconImageView(int position) {
				if (iconCheckPath != null && iconCheckPath.size() > 0
						&& iconUnCheckPath != null
						&& iconUnCheckPath.size() > 0) {
					String path = iconCheckPath.get(position);

					String uncheckPath = iconUnCheckPath.get(position);
					InputStream is = null;
					Bitmap checkBit = null;
					Bitmap uncheckBit = null;
					try {
						is = am.open(path + ".png");
						checkBit = BitmapFactory.decodeStream(is);
						is = null;
						is = am.open(uncheckPath + ".png");
						uncheckBit = BitmapFactory.decodeStream(is);

					} catch (IOException e) {
						e.printStackTrace();
						return null;
					} finally {
						try {
							if (is != null)
								is.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					VTabIconImageView image;
					image = new VTabIconImageView(
							AbstractRenrenApplication.getAppContext(),
							checkBit, uncheckBit, tab);
					return image;
				}
				return null;
			}

			@Override
			public int getCount() {
				return iconCheckPath.size();
			}
		};
		if (emotion_style == EmotionConfig.EMOTION_COOL_STYLE) {
			tab.setAdapter(adapters, iconAdapter, rankIsNull, false);
		} else {
			tab.setAdapter(adapters, iconAdapter, rankIsNull, true);
		}
		if(null == mEmotionTab){
			mEmotionTab = new ArrayList<EmotionTab>();
		}
        mEmotionTab.add(tab);
		view.addTab(tab);
		
	}
	private void initEmotionAdapter(Context context,
			String name, int pid, final int emotion_style) {
		// SystemUtil.log("meme", "mNormalAdapterList.size " +
		// mNormalAdapterList.size());
		EmotionTab mTab = null;
		if (emotion_style == EmotionConfig.EMOTION_SMALL_STYLE) {
			mTab = new EmotionTab(name, context, R.drawable.emoji_check_bg,
					R.drawable.emoji_uncheck_bg, pid, emotion_style);
		} else {
			mTab = new EmotionTab(name, context, R.drawable.cool_check_bg,
					R.drawable.cool_uncheck_bg, pid, emotion_style);
		}
		final EmotionTab tab = mTab;
		Node node = EmotionNameList.getEmotionNameList(pid);
		List<EmotionThemeModel> thList = daoManager.queryByPackageId(pid);
		final List<EmotionPluginAdapter> adapters = new ArrayList<EmotionPluginAdapter>();
		final List<String> iconCheckPath = new ArrayList<String>();
		final List<String> iconUnCheckPath = new ArrayList<String>();
		/************* 设置常用主题 (只有小表情有常用，玄酷表情没有) ****************/

		if (emotion_style == EmotionConfig.EMOTION_SMALL_STYLE) {
			// EmotionNameList.initEmotionRank(24); // 初始化排行榜
            
			EmotionRank mRank = EmotionRankManager.getInstance()
					.getEmotionRank(pid);
			
			String[] mRankPaths = null;
			String[] mRankCodes = null;
			String[] rankPaths = null;
			String[] rankCodes = null;
			String[] rankThemes = null;
			rankIsNull = true;
		   CommonUtil.log("Tab", "Rank is :" + mRank);
			if (mRank != null) {
				mRank.settobeDefault();
				if (emotion_style == EmotionConfig.EMOTION_SMALL_STYLE) {
					mRankPaths = mRank
							.getTopEmotionPath(EmotionConfig.SMALL_ONE_PAGE_SUM);
					mRankCodes = mRank
							.getTopEmotionCode(EmotionConfig.SMALL_ONE_PAGE_SUM);
					rankThemes = mRank
							.getTopEmotionThemeId(EmotionConfig.SMALL_ONE_PAGE_SUM);
					CommonUtil.log("Tab", "mRankCodes tab 2 :" + mRankCodes);
					if (mRankPaths != null && mRankPaths.length > 0) {

						if (mRankPaths.length == EmotionConfig.SMALL_ONE_PAGE_SUM) {// 当表情满了后,在最后一个添加删除
							rankPaths = new String[EmotionConfig.SMALL_ONE_PAGE_SUM];
							rankCodes = new String[EmotionConfig.SMALL_ONE_PAGE_SUM];
							for (int i = 0; i < mRankPaths.length; i++) {
								if (i == mRankPaths.length - 1) {
									rankPaths[i] = EmotionConfig.DELETE;
									rankCodes[i] = EmotionConfig.DELETE;
								} else {
									rankPaths[i] = mRankPaths[i];
									rankCodes[i] = mRankCodes[i];
								}

							}
						} else { // 当表情未满时,数组要加一个位置放置删除按钮
							rankPaths = new String[EmotionConfig.SMALL_ONE_PAGE_SUM];
							rankCodes = new String[EmotionConfig.SMALL_ONE_PAGE_SUM];
							for (int i = 0; i < mRankPaths.length; i++) {
								rankPaths[i] = mRankPaths[i];
								rankCodes[i] = mRankCodes[i];
							}
							rankPaths[rankPaths.length - 1] = EmotionConfig.DELETE;
							rankCodes[rankCodes.length - 1] = EmotionConfig.DELETE;
						}
					}
				}
				// 设置标志位
				if (rankPaths != null && rankPaths.length > 0)
					rankIsNull = false;

				// 清除缓存
				mRankPaths = null;
				mRankCodes = null;
			}
			CommonUtil.log("Tab", " tab3 :" + rankCodes);
			EmotionPluginAdapter rankAdapter = new EmotionPluginAdapter();
			rankAdapter.setEmotionPluginAdapterData(rankPaths, rankCodes, pid,
					emotion_style, rankThemes);
			adapters.add(rankAdapter);
			iconCheckPath.add("rank_check");
			iconUnCheckPath.add("rank_uncheck");
			//mNormalAdapterList.add(rankAdapter);
		}
		/****************** 设置其他主题的Ａｄａｐｔｅｒ **************************************/
		for (int k = 0; node != null && thList != null && k < thList.size(); k++) {
			EmotionThemeModel item = thList.get(k);
			if (item.theme_hidden == EmotionConfig.EMOTION_SHOW) {
				iconCheckPath.add(item.theme_check_icon_path);
				iconUnCheckPath.add(item.theme_uncheck_icon_path);
				String[] paths = null;
				String[] codes = null;
				String[] themes = null;
				paths = node.getEmotionPathArray(item.theme_id, emotion_style);
				codes = node.getEmotionCodeArray(item.theme_id, emotion_style);
				themes = new String[1];
				themes[0] = item.theme_id;

				EmotionPluginAdapter adapter = new EmotionPluginAdapter();
				adapter.setEmotionPluginAdapterData(paths, codes, pid,
						emotion_style, themes);
				adapters.add(adapter);
			//	mNormalAdapterList.add(adapter);
//				CommonUtil.log("icon", thList.size() + "  theme:"
//						+ item.theme_check_icon_path);
			}
		}
		/**
		 * 侧边栏中的Ａｄａｐｔｅｒ
		 */
		final VerticalIconContainerAdapter iconAdapter = new VerticalIconContainerAdapter() {

			@Override
			protected VTabIconImageView getTabIconImageView(int position) {
				if (iconCheckPath != null && iconCheckPath.size() > 0
						&& iconUnCheckPath != null
						&& iconUnCheckPath.size() > 0) {
					String path = iconCheckPath.get(position);

					String uncheckPath = iconUnCheckPath.get(position);
					InputStream is;
					 Bitmap checkBit = null;
					 Bitmap uncheckBit = null;
					 try {
					 is = am.open(path + ".png");
					 checkBit = BitmapFactory.decodeStream(is);
					 is = null;
					 is = am.open(uncheckPath + ".png");
					 uncheckBit = BitmapFactory.decodeStream(is);
					 is.close();
					
					 } catch (IOException e) {
					 e.printStackTrace();
					 return null;
					 }
					VTabIconImageView image;
					image = new VTabIconImageView(
							AbstractRenrenApplication.getAppContext(),
							checkBit, uncheckBit, tab);
					return image;
				}
				return null;
			}

			@Override
			public int getCount() {
				return iconCheckPath.size();
			}
		};
		new Handler().post(new Runnable() {
			
			@Override
			public void run() {
				if (emotion_style == EmotionConfig.EMOTION_COOL_STYLE) {
					tab.setAdapter(adapters, iconAdapter, rankIsNull, false);
				} else {
					tab.setAdapter(adapters, iconAdapter, rankIsNull, true);
				}
				mEmotionTab.add(tab);
			}
		});
		
		//view.addTab(tab);

	}
	/**
	 * 解析Asset下的Json文件
	 * 
	 * @param filePath
	 *            :Assets下的配置文件
	 */
	private void parserAssetStart(String filePath) {
		AssetManager am = null;
		try {
			am = AbstractRenrenApplication.getAppContext().getAssets();
			String[] pathList = am.list(filePath);
			for (int i = 0; pathList != null && i < pathList.length; i++) {
				if (pathList[i].lastIndexOf(".json") != -1) {
					EmotionJsonParser.getInstance().emotionJsonParser(
							am.open(filePath + File.separator + pathList[i]));
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 清空监听
	 */
	public void clearEmotionCallBack() {

        if(null != mEmotionTab && mEmotionTab.size() > 0 ){

            for(EmotionTab tab:mEmotionTab){
            	List<EmotionPluginAdapter>adaptes = tab.adapters;
            	for(EmotionPluginAdapter adapter:adaptes){
            		adapter.removeCoolEmotionSelectCallBack();
                    adapter.removeNormalEmotionSelectCallBack();
            	}
            }
        }
		this.mSelectCoolEmotionListener = null;
		this.mSelectEmotionListener = null;
		this.mParserEmotionListener = null;
	}
	/**
	 * 清除View中持有的资源
	 */
	private void clearEmotionCache(){
		if(null != mEmotionTab && mEmotionTab.size() > 0 ){

            for(EmotionTab tab:mEmotionTab){
            	List<EmotionPluginAdapter>adaptes = tab.adapters;
            	for(EmotionPluginAdapter adapter:adaptes){
            		adapter.removeCoolEmotionSelectCallBack();
                    adapter.removeNormalEmotionSelectCallBack();
                    adapter.clearData();
                    CommonUtil.log("emotionadapter", "clearmEmotionCache");
            	}
            	adaptes.clear();
            }
        }
		EmotionPool.getInstance().clear();
		this.mSelectCoolEmotionListener = null;
		this.mSelectEmotionListener = null;
		this.mParserEmotionListener = null;
	}
	/**
	 * 清除EmotionView的对象，资源等等。
	 */
    public void clearEmotionView(){
    	CommonUtil.log("emotionadapter", "clearEmotionView");
    	if(null!=mEmotionTab&&mEmotionTab.size()>0){
    		CommonUtil.log("emotionadapter", "clearmEmotionTab");
    		clearEmotionCache();
    		mEmotionTab.clear();
    	}
    	daoManager = null;
    	am = null;
    	sInstance = null;
    }
}
