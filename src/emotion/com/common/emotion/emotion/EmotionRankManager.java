package com.common.emotion.emotion;

import java.util.HashMap;

/**
 * 用于存储排行榜上的表情数据管理器
 * @author zxc
 *
 */
public class EmotionRankManager {
	HashMap<Integer, EmotionRank> rankMap = new HashMap<Integer, EmotionRank>();
	private static EmotionRankManager mInstance;
	private final static String TAG = "zxc";
//	private LinkedList<EmotionRank> stack = new LinkedList<EmotionRank>();
	public static EmotionRankManager getInstance(){
		if(mInstance == null){
			mInstance = new EmotionRankManager();
		}
		return mInstance;
	}
	/***
	 * 添加 package_id下的缓存
	 * @param package_id
	 * @param cache
	 */
	public boolean addNewEmotionRank(int package_id, EmotionRank rank){
		if(null != rankMap){
//			Log.d(TAG , "addNewEmotionRank " + package_id + " size " + rank.size);
			rankMap.put(package_id, rank);
			return true;
		}
		return false;
	}
	/**
	 * 
	 * @param package_id 包id
	 * @return 此包下的缓存
	 */
	public EmotionRank getEmotionRank(int package_id){
		if(null != rankMap){
			return rankMap.get(package_id);
		}
		
		return null;
	}
	public EmotionRank remove(int key){
		return rankMap.remove(key);
	}
	
}
