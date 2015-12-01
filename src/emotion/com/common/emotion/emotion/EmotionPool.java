package com.common.emotion.emotion;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import com.common.app.AbstractRenrenApplication;
import com.common.emotion.config.EmotionConfig;
import com.common.emotion.manager.Emotion;
import com.core.util.CommonUtil;
import com.core.util.DipUtil;
import com.core.util.GIFDecode;

import java.io.*;
import java.util.HashMap;
import java.util.Iterator;

/****
 * 存储的表情包括 png，gif，gif第一帧
 * @author zxc
 *	
 */
public class EmotionPool {
	public static final int NORMAL_EMOTION_BITMAP_WIDTH_MIDDIP = 24;//中屏手机对应的手机像素高度
	public static final int NORMAL_EMOTION_BITMAP_HEIGHT_MIDDIP = 24;//中屏手机对应的手机像素高度
	private static final String TAG = "zxc";
	private int max_gifsize;
	/**
	 * 载入表情数据
	 * 
	 * @throws IOException
	 * */
	public  void loadEmotionData() {
			CommonUtil.log("abc", this.getSizeOfCache() + "" );
				if(EmotionNameList.Package_idList == null){
					EmotionNameList.initPackList();
				}
				if (EmotionPool.getInstance().mEmotionCache.size() <= 0) {
					for(int i = 0; i < EmotionNameList.Package_idList.size()&&EmotionNameList.Package_idList!=null;++i){
//						SystemUtil.log("zxc", "loadEmotionData by package id   " +EmotionNameList.Package_idList.get(i));
						loadEmotion(EmotionNameList.Package_idList.get(i));
					}
					
				}
				if(EmotionPool.getInstance().mFirstFrameCache.size() <=0){
					loadFirstFrame();
				}
				//add by jia.xia
		   EmotionNameList.initEmotionRank(24); // 初始化排行榜
		   //EmotionManager.getInstance().loadIconAdapter();
	}
	

	/****
	 * 根据包id获取此包下的所有表情
	 * 载入所有Emotion数据库中的表情
	 * @param package_id 包id
	 */
	public void loadEmotion(int package_id){
	
		Node nodetmp = EmotionNameList.getEmotionNameList(package_id);
		EmotionPool  emotionPool = EmotionPool.getInstance();
		for (int i = 0; nodetmp!=null&&i < nodetmp.showlength;++i) {///???
			try {
				try{

					Emotion emotion = new Emotion(nodetmp.name[i]);
					emotion.mFrameSize = 1;
					emotion.counter = nodetmp.counter[i];
					String strpath = nodetmp.path[i];
					InputStream is = AbstractRenrenApplication.getAppContext().getAssets().open(strpath+".png");
					emotion.addBitmap(BitmapFactory.decodeStream(is));
					emotionPool.addEmotion(nodetmp.path[i], emotion);
					is.close();

				
				}catch(OutOfMemoryError error){
					CommonUtil.log("emotion","内存溢出");
				}
				
			} catch (Exception e) {
				CommonUtil.log(TAG, "loading error ... ");
//				Toast.makeText(AbstractRenrenApplication.getAppContext(), "加载错误 ,错误路径为 ：" + nodetmp.path[i] , Toast.LENGTH_SHORT );
			}
		}
	}
	/***
	 * 加载所有GIF表情第一帧
	 */
	public void loadFirstFrame() {
		Node nodetmp  = EmotionNameList.getEmotionNameList(EmotionConfig.COOL_PACKAGE_ID);
		for(int i = 0; nodetmp!=null&&i<nodetmp.length;++i){
			try{
				try{

					GIFDecode decode = new GIFDecode();
					String strpath = nodetmp.path[i];
					InputStream is = AbstractRenrenApplication.getAppContext()
							.getAssets().open(strpath + ".gif");
					Emotion emotion = new Emotion(nodetmp.name[i]);
					decode.readFirstFrame(is);
					emotion.addBitmap(decode.getFrame(0));
					emotion.mIsFlash = false;
					emotion.mFrameSize=1;
					this.addFirstFrameEmotion(strpath, emotion);
				
				}catch(OutOfMemoryError error){
					CommonUtil.log("emotion","内存溢出");
				}
			} catch (FileNotFoundException e) {
				CommonUtil.log(TAG,"loading error " );
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	/***
	 * 获取表情ＧＩＦ第一帧
	 * @param path
	 * @return
	 */
	private Emotion loadFirstFrame(String path){
		Node nodetmp = EmotionNameList.getEmotionNameList(EmotionConfig.COOL_PACKAGE_ID);
		int i = nodetmp.getIndex(path.hashCode());
		GIFDecode decode = new GIFDecode();
		try{
			try{
				String strpath = nodetmp.path[i];
				InputStream is = AbstractRenrenApplication.getAppContext()
						.getAssets().open(strpath + ".gif");
				Emotion emotion = new Emotion(nodetmp.name[i]);
				decode.readFirstFrame(is);
				emotion.addBitmap(decode.getFrame(0));
				emotion.mIsFlash = true;
				emotion.mFrameSize=1;
				this.addFirstFrameEmotion(strpath, emotion);
				return emotion;
			}catch(OutOfMemoryError error){
				CommonUtil.log("emotion","内存溢出");
				return null;
			}
		} catch (FileNotFoundException e) {
			CommonUtil.log(TAG,"loading error " );
			/**普通的读取操作*/
			try {
				try{
					File file = new File(path+".gif");
					InputStream is = new FileInputStream(file);
					String strpath = nodetmp.path[i];
					Emotion emotion = new Emotion(nodetmp.name[i]);
					decode.readFirstFrame(is);
					emotion.addBitmap(decode.getFrame(0));
					emotion.mIsFlash = true;
					emotion.mFrameSize=1;
					this.addFirstFrameEmotion(strpath, emotion);
					return emotion;
				}catch(OutOfMemoryError error){
					CommonUtil.log("emotion","内存溢出");
				}
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}

			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			CommonUtil.log(TAG,"loading error:IOException " );
			e.printStackTrace();
			return null;
		}
	}
	/***
	 * 添加表情
	 * @param emotion_path 表情路径
	 * @param emotion 表情{@link Emotion}
	 */
	public void addEmotion(String emotion_path, Emotion emotion){
		if(emotion==null){
			return ;
		}
		mEmotionCache.put(emotion_path.hashCode(), emotion);
	}
	/***
	 * 添加cool表情
	 * @param emotion_path
	 * @param emotion
	 */
	public void addCoolEmotion(String emotion_path, Emotion emotion){
		if(mGifEmotionCache.size()>=this.max_gifsize){
//			CommonUtil.log("emotion","clean GIF "  + max_gifsize);
			mGifEmotionCache.clear();
		}
		mGifEmotionCache.put(emotion_path.hashCode(), emotion);
	}
	/****
	 * 添加第一帧
	 * @param emotion_path
	 * @param emotion
	 */
	public void addFirstFrameEmotion(String emotion_path, Emotion emotion){
		mFirstFrameCache.put(emotion_path.hashCode(), emotion);
	}
	/***
	 * 根据emotion取出span
	 * @param emotion {@link Emotion}
	 * @return
	 */
	public EmotionSpan getEmotionSpan(Emotion emotion){
		EmotionSpan span = null;
		if(span!=null){//确保线程安全撒？
			return span;
		}else{
			Bitmap bitmap = emotion.next();
			Drawable drawable = new BitmapDrawable(bitmap);
			drawable.setBounds(new Rect(0, 0,
					DipUtil.calcFromDip(NORMAL_EMOTION_BITMAP_WIDTH_MIDDIP),
					DipUtil.calcFromDip(NORMAL_EMOTION_BITMAP_HEIGHT_MIDDIP)));
			span = new EmotionSpan(drawable);
//			mEmotionSpanTable.put(emotion.mEmotionName, span);
			return span;
		}
	}
	/****
	 * 清空缓存
	 */
	public void clear(){
//		mEmotionSpanTable.clear();
		mEmotionCache.clear();
		mGifEmotionCache.clear();
	}
	/****
	 * 清空GIF缓存
	 */
	public void clearGifCache(){
		mGifEmotionCache.clear();
	}
	public void recycleGifCache(){
		if(mGifEmotionCache.size()<=0){
			return;
		}
		Iterator<Integer> iter = mGifEmotionCache.keySet().iterator();
		for(;iter.hasNext();){
			Emotion e = mGifEmotionCache.get(iter.next());
			for(int i = 0; i < e.mFrameSize;++i){
				if(e.mEmotionList.get(i)!=null && !e.mEmotionList.get(i).isRecycled()){
					e.mEmotionList.get(i).recycle();
				}
			}
		}
		this.clearGifCache();
	}
	public void clearFirstFrameCache(){
		mFirstFrameCache.clear();
	}
	
	
	public EmotionCache mEmotionCache = new EmotionCache(50);
	private HashMap<Integer, Emotion> mGifEmotionCache = new HashMap<Integer, Emotion>();
	private EmotionCache mFirstFrameCache = new EmotionCache(30,false);
	//表情对应表
//	public Map<Integer,Emotion> mEmotionTable = new HashMap<Integer,Emotion>();
	//文本表情对应表
//	public Map<String,EmotionSpan> mEmotionSpanTable = new HashMap<String,EmotionSpan>();

	
	private static EmotionPool sInstance = null;
	private EmotionPool(){
		max_gifsize = getSizeOfCache();
	}
	
	
	/**
	 * 获取实例。同时会加载表情数据
	 * @return
	 */
	public static EmotionPool getInstance(){
//		VMRuntime.getRuntime().setMinimumHeapSize(HEAP_SIZE);
		if(sInstance==null){
			sInstance = new EmotionPool();
			sInstance.loadEmotionData();
		}
		return sInstance;
	}
	

	/**
	 * 线程不安全啊
	 * @return Emotion 得到表情
	 *  @param emotion_path  此表情的路径
	 * */
	public Emotion getEmotion(String emotion_path){
		return mEmotionCache.get(emotion_path.hashCode());
	}
	/***
	 * 得到炫酷表情
	 * @param emotion_path
	 * @return
	 */
	public Emotion getCoolEmotion(String emotion_path){
		if(emotion_path==null){
			return null;
		}
		return (Emotion) mGifEmotionCache.get(emotion_path.hashCode());
	}
	/****
	 * 获取GIF表情第一帧
	 * @param emotion_path
	 * @return
	 */
	public Emotion getFirstFrameEmotion(String emotion_path){
		if(emotion_path==null){
//			SystemUtil.log(TAG, "emotion_path is null");
			return null;
		}
		Emotion emotion = mFirstFrameCache.get(emotion_path.hashCode());
		if(emotion == null){
			return this.loadFirstFrame(emotion_path);
		}
		return emotion;
	}
	
	/***
	 * 此方法是线程安全的，
	 * @param emotion_path
	 * @return Emotion 得到表情
	 */
	public synchronized Emotion getEmotionSync(String emotion_path){
		return mEmotionCache.get(emotion_path.hashCode());
	}
	
	
	/**
	 * @return EmotionSpan
	 * @说明 mEmotionSpanTable是基于事件增加的表
	 * @param index:表情的索引号
	 * */
	public EmotionSpan getEmotionSpan(Emotion emotion,int index){
		EmotionSpan span = null;
		if(span!=null){
			return span;
		}else{
			Bitmap bitmap = emotion.mEmotionList.get(index);
			Drawable drawable = new BitmapDrawable(bitmap);
			drawable.setBounds(new Rect(0, 0,
					DipUtil.calcFromDip(NORMAL_EMOTION_BITMAP_WIDTH_MIDDIP),
					DipUtil.calcFromDip(NORMAL_EMOTION_BITMAP_HEIGHT_MIDDIP)));
			span = new EmotionSpan(drawable);
//			mEmotionSpanTable.put(emotion.mEmotionName+index, span);
			return span;
		}
	}
	
	
	/****
	 * 获取阿狸的gif图片
	 * @param package_id
	 * @param codeStr
	 * @return
	 */
	public Emotion reloadAlEmotionByFlash(int package_id, String codeStr, String theme_id){
		// Node tmpNode = EmotionNameList.getEmotionNameList(package_id); （1）（笑）
		String[] codestr = EmotionNameList.getEmotionNameList(package_id)
				.getEmotionCodeArray(theme_id);
		if(codestr==null){
			return null;
		}
		for (int i = 0; i < codestr.length; ++i) {
			if (codeStr.equals(Node.changeToChineseCode(codestr[i]))) {
				try {
					try{
						GIFDecode decode = new GIFDecode();
						String strpath = EmotionNameList.getEmotionNameList(
	                            package_id).getEmotionPathArray(theme_id)[i];
	                    InputStream is = AbstractRenrenApplication.getAppContext()
	                            .getAssets().open(strpath + ".gif");
	                    decode.read(is);
	                    Emotion emotion = new Emotion(EmotionNameList
	                            .getEmotionNameList(package_id)
	                            .getEmotionNameArray(theme_id)[i]);
						for (int k = 0; k < decode.getFrameCount(); ++k) {
							emotion.addBitmap(decode.getFrame(k));
						}
						emotion.mIsFlash = true;
						this.addCoolEmotion(strpath + ".gif", emotion);
						return emotion;
					}catch(OutOfMemoryError e){
						CommonUtil.log("emotion","内存溢出");
						}
				} catch (FileNotFoundException e) {
					e.printStackTrace();
					Emotion emotion = new Emotion(EmotionNameList
							.getEmotionNameList(package_id)
							.getEmotionNameArray(theme_id)[i]);
					emotion.errorcode = 1;
					return emotion;
				} catch (IOException e) {
					e.printStackTrace();
					Emotion emotion = new Emotion(EmotionNameList
							.getEmotionNameList(package_id)
							.getEmotionNameArray(theme_id)[i]);
					emotion.errorcode = 1;
					return emotion;
				}

			}
		}
		return null;
	}
	/****
	 *  边解析边存入缓存
	 * @param path
	 * @param pid
	 * @return
	 */
	public Emotion reloadAlEmotionByFlash(String path,int pid){
		CommonUtil.log("EmotionPool", "reloadAlEmotionByFlash start path:"+path);
		if(path !=null){
			GIFDecode decode = new GIFDecode();
			try {
				try{
					InputStream is = AbstractRenrenApplication.getAppContext()
							.getAssets().open(path + ".gif");
					Emotion emotion = new Emotion(EmotionNameList
							.getEmotionNameList(pid)
							.getchinestName(EmotionNameList
									.getEmotionNameList(pid).
									getIndex(path.hashCode())));
					emotion.mIsFlash = true;
					this.addCoolEmotion(path + ".gif", emotion);
					decode.readAndSetToEmotion(is, emotion);
					return emotion;
				}catch(OutOfMemoryError e){
					CommonUtil.log("emotion","内存溢出 reloadAlEmotionByFlash");
					this.clearGifCache();
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				CommonUtil.log("EmotionPool", "FileNotFoundException:"+e.toString());
				/**普通的读取操作*/
				try {
					try{
						File file = new File(path+".gif");
						InputStream mInput = new FileInputStream(file);
						Emotion emotion = new Emotion(EmotionNameList
								.getEmotionNameList(pid)
								.getchinestName(EmotionNameList
										.getEmotionNameList(pid).
										getIndex(path.hashCode())));
						emotion.mIsFlash = true;
						this.addCoolEmotion(path + ".gif", emotion);
						decode.readAndSetToEmotion(mInput, emotion);
						return emotion;
					}catch(OutOfMemoryError error){
						CommonUtil.log("emotion","内存溢出");
						
					}
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				}
				Emotion emotion = new Emotion(EmotionNameList
						.getEmotionNameList(pid)
						.getchinestName(EmotionNameList
								.getEmotionNameList(pid).
								getIndex(path.hashCode())));
				emotion.errorcode = 1;
				return emotion;
			} catch (IOException e) {
				e.printStackTrace();
				CommonUtil.log("EmotionPool", "IOException:"+ e.toString());
				Emotion emotion = new Emotion(EmotionNameList
						.getEmotionNameList(pid)
						.getchinestName(EmotionNameList
								.getEmotionNameList(pid).
								getIndex(path.hashCode())));
				emotion.errorcode = 1;
				return emotion;
			}
		}
		return null;
	}
	/***
	 * 根据屏幕的分辨率计算缓存的大小
	 * @return
	 */
	private int getSizeOfCache() {
		int width;
		int height;
		int index = 0;
		for (int i = 0; i < AbstractRenrenApplication.SCREEN.length(); ++i) {
			if (AbstractRenrenApplication.SCREEN.charAt(i) == '*') {
				index = i;
			}
		}
		if (index == 0) {
			return 10;
		}
		try {
			width = Integer.valueOf(AbstractRenrenApplication.SCREEN.substring(
					0, index));
			height = Integer.valueOf(AbstractRenrenApplication.SCREEN
					.substring(index + 1));
		} catch (NumberFormatException e) {
			return 10;
		}
		if (width * height > 1280 * 800) {//屏幕分辨率大于1280*800 缓存 为12
			return 12;
		} else if (width * height > 640 * 960) {//屏幕分辨率大于640*960 缓存 为10
			return 10;
		} else if (width * height > 540 * 960) {//屏幕分辨率540*960 缓存 为8
			return 8;
		} else {//其余的屏幕为6
			return 6;
		}
	}
}
